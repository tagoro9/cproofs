package com.scytl.cproofs.vote;

import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Message;
import com.scytl.cproofs.crypto.Parameters;
import com.scytl.cproofs.crypto.Schnorr.SchnorrSignature;
import com.scytl.cproofs.crypto.Signature;

import org.spongycastle.jce.interfaces.ElGamalPrivateKey;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ElGamalPrivateKeySpec;
import org.spongycastle.jce.spec.ElGamalPublicKeySpec;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by victor on 4/5/14.
 */
public class ElGamalVote implements Vote {

    private ElGamalExtendedParameterSpec parameters;
    private ElGamalMessage message;
    private SchnorrSignature signature;
    private BigInteger choice;
    private BigInteger y;
    private BigInteger x;

    private KeyFactory keyFactory;
    private Cipher cipher;

    private ElGamalPublicKeySpec publicKey;
    private ElGamalPrivateKeySpec privateKey;

    private static BigInteger       ZERO = BigInteger.valueOf(0);
    private static BigInteger       ONE = BigInteger.valueOf(1);
    private static BigInteger       TWO = BigInteger.valueOf(2);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public ElGamalVote() {
        try {
            keyFactory = KeyFactory.getInstance("ElGamal", "SC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public ElGamalVote(Parameters parameters, Message message, Signature signature, BigInteger y, int choice) {
        this();
        this.parameters = (ElGamalExtendedParameterSpec) parameters;
        this.message = (ElGamalMessage) message;
        this.choice = BigInteger.valueOf(choice);
        this.signature = (SchnorrSignature) signature;
        this.y = y;
    }

    public ElGamalVote(Parameters parameters, Message message, Signature signature, BigInteger y, BigInteger x, int choice) {
        this(parameters, message, signature, y, choice);
        this.x = x;
    }


    @Override
    public Serializable getChoice() {
        return choice;
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Signature getSignature() {
        return signature;
    }

    public void setChoice(BigInteger choice) {
        this.choice = choice;
    }

    public void setSignature(SchnorrSignature signature) {
        this.signature = signature;
    }

    public void setMessage(ElGamalMessage message) {
        this.message = message;
    }

    public void setParameters(ElGamalExtendedParameterSpec parameters) {
        this.parameters = parameters;
    }

    public Message encrypt() {
        /*try {
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(publicKey));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        cipher.update(choice.toByteArray());
        byte[] output = null;
        try {
            output = cipher.doFinal();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        byte[]  gammaBytes = new byte[output.length / 2];
        byte[]  phiBytes = new byte[output.length / 2];

        System.arraycopy(output, 0, gammaBytes, 0, gammaBytes.length);
        System.arraycopy(output, gammaBytes.length, phiBytes, 0, phiBytes.length);
        message = new ElGamalMessage(new BigInteger(phiBytes), new BigInteger(gammaBytes));
        return message;*/
        BigInteger                  input = new BigInteger(1, choice.toByteArray());
        Random random = new SecureRandom();
        BigInteger p = parameters.getP();
        BigInteger g = parameters.getG();
        int                         pBitLength = p.bitLength();
        BigInteger                  k = new BigInteger(pBitLength, random);

        while (k.equals(ZERO) || (k.compareTo(p.subtract(TWO)) > 0))
        {
            k = new BigInteger(pBitLength, random);
        }

        BigInteger  gamma = g.modPow(k, p);
        BigInteger  phi = input.multiply(publicKey.getY().modPow(k, p)).mod(p);
        message = new ElGamalMessage(phi, gamma);
        sign(k);
        return message;
    }

    public BigInteger decrypt() {
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(privateKey));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        cipher.update(message.toByteArray());
        byte[] output = null;
        try {
            output = cipher.doFinal();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new BigInteger(output);
    }

    private BigInteger generateK() {
        Random rnd = new SecureRandom();
        BigInteger r;
        do {
            r = new BigInteger(parameters.getQ().bitLength(), rnd);
        } while (r.compareTo(parameters.getQ()) >= 0 && r.compareTo(BigInteger.ONE) <= 0);
        return r;
    }

    public Signature sign(BigInteger r) {
        Boolean valid = true;
        do {
            BigInteger k = generateK();
            BigInteger c1 = parameters.getG().modPow(k, parameters.getP());
            BigInteger c2 = publicKey.getY().modPow(k, parameters.getP());
            BigInteger h = concatAndHash(parameters.getG(), publicKey.getY(), c1, c2, message.getGamma(), message.getPhi());
            if (h.compareTo(BigInteger.ZERO) == 0) {
                valid = false;
            }
            else {
                BigInteger t = k.subtract(r.multiply(h)).mod(parameters.getQ());
                if (t.compareTo(BigInteger.ZERO) == 0) {
                    valid = false;
                }
                else {
                    signature.setH(h);
                    signature.setT(t);
                }
            }
        } while (!valid);
        return signature;
    }

    @Override
    public Boolean verify() {
        // Check that h and t are between [1, q-1] (maybe in the signature creation, we will need the parameters in the signature too)
        BigInteger t = signature.getT();
        BigInteger h = signature.getH();
        BigInteger c1 = parameters.getG().modPow(t, parameters.getP()).multiply(message.getGamma().modPow(h, parameters.getP())).mod(parameters.getP());
        BigInteger c2 = publicKey.getY().modPow(t, parameters.getP());
        BigInteger c21 = message.getPhi().multiply(choice.modInverse(parameters.getP())).mod(parameters.getP());
        BigInteger c22 = c21.modPow(h, parameters.getP());
        c2 = c2.multiply(c22).mod(parameters.getP());
        BigInteger hash = concatAndHash(parameters.getG(), publicKey.getY(), c1, c2, message.getGamma(), message.getPhi());
        return false;
    }

    private BigInteger concatAndHash(BigInteger g, BigInteger y, BigInteger c1, BigInteger c2, BigInteger gamma, BigInteger phi) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256", "SC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        messageDigest.update(g.toByteArray());
        messageDigest.update(y.toByteArray());
        messageDigest.update(c1.toByteArray());
        messageDigest.update(c2.toByteArray());
        messageDigest.update(gamma.toByteArray());
        messageDigest.update(phi.toByteArray());
        BigInteger hash = new BigInteger(messageDigest.digest());
        return hash.mod(parameters.getP());
    }
}
