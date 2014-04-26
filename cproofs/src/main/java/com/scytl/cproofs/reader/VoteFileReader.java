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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<Vote> read() {
        File voteFile = new File(path);
        try {
            String jsonVoteData = org.apache.commons.io.FileUtils.readFileToString(voteFile);
            Gson gson = new Gson();
            Vote[] votes = gson.fromJson(jsonVoteData, ElGamalVote[].class);
            return new ArrayList<Vote>(Arrays.asList(votes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
