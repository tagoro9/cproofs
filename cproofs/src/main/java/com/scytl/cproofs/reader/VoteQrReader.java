package com.scytl.cproofs.reader;

import com.google.gson.Gson;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.vote.ElGamalVote;
import com.scytl.cproofs.vote.Vote;

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
            ElGamalVote vote = gson.fromJson(data, ElGamalVote.class);
            vote.setParameters(parameters);
            return vote;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
