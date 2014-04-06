package com.scytl.cproofs.crypto.Schnorr;

import com.scytl.cproofs.crypto.Signature;

import java.math.BigInteger;

/**
 * Created by victor on 4/6/14.
 */
public class SchnorrSignature implements Signature {
    private BigInteger h;
    private BigInteger t;

    public SchnorrSignature(BigInteger h, BigInteger t) {
        this.h = h;
        this.t = t;
    }

    public BigInteger getH() {
        return h;
    }

    public void setH(BigInteger h) {
        this.h = h;
    }

    public BigInteger getT() {
        return t;
    }

    public void setT(BigInteger t) {
        this.t = t;
    }
}
