package com.scytl.cproofs.reader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Schnorr.SchnorrSignature;
import com.scytl.cproofs.vote.ElGamalVote;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by victor on 6/22/14.
 */
public class ParametersQrReader implements ParametersReader {
    @Override
    public ElGamalExtendedParameterSpec read(String jsonData) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonData, ElGamalExtendedParameterSpec.class);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
