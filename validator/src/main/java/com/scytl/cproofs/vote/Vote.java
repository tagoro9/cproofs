package com.scytl.cproofs.vote;

import com.scytl.cproofs.crypto.Message;
import com.scytl.cproofs.crypto.Parameters;
import com.scytl.cproofs.crypto.Signature;
import com.scytl.cproofs.crypto.exceptions.CProofsException;
import com.scytl.cproofs.crypto.exceptions.InvalidParametersException;
import com.scytl.cproofs.crypto.exceptions.InvalidSignatureException;

import java.io.Serializable;

/**
 * Created by victor on 4/5/14.
 */
public interface Vote {
    Parameters getParameters();
    Message getMessage();
    Boolean verify(String choice) throws InvalidParametersException, InvalidSignatureException;
    Signature getSignature();
}
