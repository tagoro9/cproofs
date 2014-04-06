package com.scytl.cproofs.vote;

import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Message;
import com.scytl.cproofs.crypto.Parameters;

import org.spongycastle.jce.spec.ElGamalPrivateKeySpec;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

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
    private BigInteger choice;

    private Cipher cipher;
    private KeyFactory keyFactory;

    private PrivateKey privateKey;

    public ElGamalVote(Parameters parameters, Message message, BigInteger x, int choice) {
        this.parameters = (ElGamalExtendedParameterSpec) parameters;
        this.message = (ElGamalMessage) message;
        this.choice = BigInteger.valueOf(choice);

        try {
            this.cipher = Cipher.getInstance("ElGamal", "SC");
            KeyFactory keyFactory = KeyFactory.getInstance("ElGamal");
            ElGamalPrivateKeySpec privateKeySpec = new ElGamalPrivateKeySpec(x, this.parameters);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
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
    public Boolean verify() {
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipher.update(message.toByteArray());
            byte[] output = cipher.doFinal();
            BigInteger result = new BigInteger(output);
            return result.equals(choice);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }
}
