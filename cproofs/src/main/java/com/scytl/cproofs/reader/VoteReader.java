package com.scytl.cproofs.reader;

import com.scytl.cproofs.vote.Vote;

import java.util.List;

/**
 * Created by victor on 4/12/14.
 */
public interface VoteReader {
    List<Vote> read();
}
