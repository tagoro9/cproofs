package com.scytl.cproofs.reader;

import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;

/**
 * Created by victor on 6/22/14.
 */
public interface ParametersReader {
    ElGamalExtendedParameterSpec read(String data);
}
