package com.scytl.cproofs.reader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Schnorr.SchnorrSignature;
import com.scytl.cproofs.vote.ElGamalVote;
import com.scytl.cproofs.vote.Vote;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by victor on 6/22/14.
 */
public class VoteQrReader implements VoteReader {

    private String data;
    private ElGamalExtendedParameterSpec parameters;

    public VoteQrReader(String data, ElGamalExtendedParameterSpec parameters) {
        this.data = data;
        this.parameters = parameters;
    }

    @Override
    public Vote read() {
        Gson gson = new Gson();
        try {
            Type stringStringMap = new TypeToken<Map<String, BigInteger>>(){}.getType();
            Map<String,BigInteger> map = gson.fromJson(data, stringStringMap);
            ElGamalMessage message = new ElGamalMessage(map.get("phi"), map.get("gamma"));
            SchnorrSignature signature = new SchnorrSignature(map.get("h"), map.get("t"));
            ElGamalVote vote = new ElGamalVote(parameters, message, signature);
            return vote;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
