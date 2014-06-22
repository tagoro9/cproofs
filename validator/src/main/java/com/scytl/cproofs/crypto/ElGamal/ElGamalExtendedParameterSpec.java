package com.scytl.cproofs.crypto.ElGamal;

import com.scytl.cproofs.crypto.Parameters;

import org.spongycastle.jce.spec.ElGamalParameterSpec;

import java.math.BigInteger;

/**
 * Created by victor on 4/5/14.
 */
public class ElGamalExtendedParameterSpec extends ElGamalParameterSpec implements Parameters {

    private BigInteger q;
    private BigInteger y;

    public ElGamalExtendedParameterSpec(BigInteger p, BigInteger g, BigInteger q, BigInteger y) {
        super(p, g);
        if (!check()) {
            throw new IllegalArgumentException("P should equal 2q + 1");
        }
        this.q = q;
        this.y = y;
    }

    @Override
    public boolean check() {
        if (!getP().equals(q.multiply(BigInteger.valueOf(2L)).add(BigInteger.ONE))) {
            return false;
        }
        return true;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }
}
