package com.scytl.cproofs.reader;

import android.content.Context;

import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;

/**
 * Created by victor on 6/21/14.
 */
public interface SettingsReader {
    ElGamalExtendedParameterSpec read(String fileName);
    ElGamalExtendedParameterSpec read(String fileName, Context context);
    Boolean write(ElGamalExtendedParameterSpec parameters, Context context);
}
