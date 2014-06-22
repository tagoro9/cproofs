package com.scytl.cproofs.reader;

import com.google.gson.Gson;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;

import java.io.File;
import java.io.IOException;

/**
 * Created by victor on 6/22/14.
 */
public class ParametersFileReader implements ParametersReader {

    @Override
    public ElGamalExtendedParameterSpec read(String path) {
        File voteFile = new File(path);
        try {
            String jsonVoteData = org.apache.commons.io.FileUtils.readFileToString(voteFile);
            Gson gson = new Gson();
            return gson.fromJson(jsonVoteData, ElGamalExtendedParameterSpec.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
