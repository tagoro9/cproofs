package com.scytl.cproofs.reader;

import android.provider.MediaStore;

import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.scytl.cproofs.vote.ElGamalVote;
import com.scytl.cproofs.vote.Vote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by victor on 4/12/14.
 */
public class VoteFileReader implements VoteReader {

    private String path;

    public VoteFileReader(String path) {
        this.path = path;
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
            ElGamalVote[] votes = gson.fromJson(jsonVoteData, ElGamalVote[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
