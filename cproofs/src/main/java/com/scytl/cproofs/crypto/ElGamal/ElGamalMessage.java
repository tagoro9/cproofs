package com.scytl.cproofs.crypto.ElGamal;

import com.scytl.cproofs.crypto.Message;

import org.spongycastle.jce.spec.ElGamalPublicKeySpec;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created by victor on 4/5/14.
 */
public class ElGamalMessage implements Message {

    private BigInteger phi;
    private BigInteger gamma;

    public ElGamalMessage(BigInteger phi, BigInteger gamma) {
        this.phi = phi;
        this.gamma = gamma;
    }

    public BigInteger getPhi() {
        return phi;
    }

    public void setPhi(BigInteger phi) {
        this.phi = phi;
    }

    public BigInteger getGamma() {
        return gamma;
    }

    public void setGamma(BigInteger gamma) {
        this.gamma = gamma;
    }

    public byte[] toByteArray() {
        byte[] gammaBytes = gamma.toByteArray();
        byte[] phiBytes = phi.toByteArray();
        int BLOCKSIZE = 64;
        byte[] cipherText = new byte[BLOCKSIZE];
        // http://www.massapi.com/source/lcrypto-j2me-126/src/org/bouncycastle/crypto/engines/ElGamalEngine.java.html
        if (gammaBytes.length > cipherText.length / 2)
        {
            System.arraycopy(gammaBytes, 1, cipherText, cipherText.length / 2 - (gammaBytes.length - 1), gammaBytes.length - 1);
        }
        else
        {
            System.arraycopy(gammaBytes, 0, cipherText, cipherText.length / 2 - gammaBytes.length, gammaBytes.length);
        }

        if (phiBytes.length > cipherText.length / 2)
        {
            System.arraycopy(phiBytes, 1, cipherText, cipherText.length - (phiBytes.length - 1), phiBytes.length - 1);
        }
        else
        {
            System.arraycopy(phiBytes, 0, cipherText, cipherText.length - phiBytes.length, phiBytes.length);
        }
        return cipherText;
    }
}
