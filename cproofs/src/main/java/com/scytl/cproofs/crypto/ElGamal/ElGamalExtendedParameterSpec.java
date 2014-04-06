package com.scytl.cproofs.crypto.ElGamal;

import com.scytl.cproofs.crypto.Parameters;

import org.spongycastle.jce.spec.ElGamalParameterSpec;

import java.math.BigInteger;

/**
 * Created by victor on 4/5/14.
 */
public class ElGamalExtendedParameterSpec extends ElGamalParameterSpec implements Parameters {

    private BigInteger q;

    public ElGamalExtendedParameterSpec(BigInteger p, BigInteger g, BigInteger q) {
        super(p, g);
        // Check if p is 2q +1
        if (!p.equals(q.multiply(BigInteger.valueOf(2L)).add(BigInteger.ONE))) {
            throw new IllegalArgumentException("Prime p should equal 2q +1");
        }
        this.q = q;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }
}
