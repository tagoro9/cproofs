package com.scytl.cproofs.reader;

import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Schnorr.SchnorrSignature;
import com.scytl.cproofs.vote.ElGamalVote;
import com.scytl.cproofs.vote.Vote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 4/12/14.
 */
public class VoteFileReader implements VoteReader {

    private String path;
    private ElGamalExtendedParameterSpec parameters;

    public VoteFileReader(String path, ElGamalExtendedParameterSpec parameters) {
        this.path = path;
        this.parameters = parameters;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Vote read() {
        File voteFile = new File(path);
        try {
            String jsonVoteData = org.apache.commons.io.FileUtils.readFileToString(voteFile);
            Gson gson = new Gson();
            Type stringStringMap = new TypeToken<Map<String, BigInteger>>(){}.getType();
            Map<String,BigInteger> map = gson.fromJson(jsonVoteData, stringStringMap);
            ElGamalMessage message = new ElGamalMessage(map.get("phi"), map.get("gamma"));
            SchnorrSignature signature = new SchnorrSignature(map.get("h"), map.get("t"));
            ElGamalVote vote = new ElGamalVote(parameters, message, signature);
            // Se vote parameters to the ones stored in the app (ignoring the ones that could have been present in the vote file)
            vote.setParameters(parameters);
            return vote;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
