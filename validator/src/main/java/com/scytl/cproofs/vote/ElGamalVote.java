package com.scytl.cproofs.vote;

import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Message;
import com.scytl.cproofs.crypto.Parameters;
import com.scytl.cproofs.crypto.Schnorr.SchnorrSignature;
import com.scytl.cproofs.crypto.Signature;
import com.scytl.cproofs.crypto.exceptions.CProofsException;
import com.scytl.cproofs.crypto.exceptions.InvalidParametersException;
import com.scytl.cproofs.crypto.exceptions.InvalidSignatureException;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

/**
 * Created by victor on 4/5/14.
 */
public class ElGamalVote implements Vote {

    private ElGamalExtendedParameterSpec parameters;
    private ElGamalMessage message;
    private SchnorrSignature signature;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public ElGamalVote() {
    }

    public ElGamalVote(Parameters parameters, Message message, Signature signature) {
        this();
        this.parameters = (ElGamalExtendedParameterSpec) parameters;
        this.message = (ElGamalMessage) message;
        this.signature = (SchnorrSignature) signature;
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

    public void setSignature(SchnorrSignature signature) {
        this.signature = signature;
    }

    public void setMessage(ElGamalMessage message) {
        this.message = message;
    }

    public void setParameters(ElGamalExtendedParameterSpec parameters) {
        this.parameters = parameters;
    }

    @Override
    public Boolean verify(String choiceString) throws InvalidParametersException, InvalidSignatureException {
        checkParametersAndSignature();
        BigInteger choice = new BigInteger(choiceString, 10);
        BigInteger t = signature.getT();
        BigInteger h = signature.getH();
        BigInteger c1 = parameters.getG().modPow(t, parameters.getP()).multiply(message.getGamma().modPow(h, parameters.getP())).mod(parameters.getP());
        BigInteger c2 = parameters.getY().modPow(t, parameters.getP());
        BigInteger c21 = message.getPhi().multiply(choice.modInverse(parameters.getP())).mod(parameters.getP());
        BigInteger c22 = c21.modPow(h, parameters.getP());
        c2 = c2.multiply(c22).mod(parameters.getP());
        BigInteger hash = concatenateAndHash(parameters.getG(), parameters.getY(), c1, c2, message.getGamma(), message.getPhi());
        return h.equals(hash);
    }

    private Boolean checkParametersAndSignature() throws InvalidParametersException, InvalidSignatureException {
        if (!parameters.check()) {
           throw new InvalidParametersException();
        }
        if (!signature.check(parameters.getQ())) {
            throw new InvalidSignatureException();
        }
        return true;
    }

    private BigInteger concatenateAndHash(BigInteger g, BigInteger y, BigInteger c1, BigInteger c2, BigInteger gamma, BigInteger phi) {
        MessageDigest messageDigest = getMessageDigest();
        if (null != messageDigest) {
            messageDigest.update(g.toByteArray());
            messageDigest.update(y.toByteArray());
            messageDigest.update(c1.toByteArray());
            messageDigest.update(c2.toByteArray());
            messageDigest.update(gamma.toByteArray());
            messageDigest.update(phi.toByteArray());
            BigInteger hash = new BigInteger(messageDigest.digest());
            return hash.mod(parameters.getP());
        }
        return null;
    }

    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256", "SC");
        } catch (NoSuchAlgorithmException e) {

        } catch (NoSuchProviderException e) {
        }
        return null;
    }
}
