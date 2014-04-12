package com.scytl.cproofs.vote;

import com.scytl.cproofs.crypto.Message;
import com.scytl.cproofs.crypto.Parameters;
import com.scytl.cproofs.crypto.Signature;

import java.io.Serializable;

/**
 * Created by victor on 4/5/14.
 */
public interface Vote {
    Parameters getParameters();
    Message getMessage();
    Serializable getChoice();
    Boolean verify();
    Signature getSignature();
    /*Message encrypt();
    Serializable decrypt();*/
}
