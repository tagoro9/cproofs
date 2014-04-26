package com.scytl.cproofs.crypto;

import java.math.BigInteger;

/**
 * Created by victor on 4/6/14.
 */
public interface Signature {
    boolean check(BigInteger q);
}
