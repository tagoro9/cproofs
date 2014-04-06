package com.scytl.cproofs.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.ElGamal.ElGamalMessage;
import com.scytl.cproofs.crypto.Message;
import com.scytl.cproofs.crypto.Parameters;
import com.scytl.cproofs.vote.ElGamalVote;
import com.scytl.cproofs.vote.Vote;

import org.spongycastle.crypto.engines.ElGamalEngine;
import org.spongycastle.crypto.params.ElGamalKeyParameters;
import org.spongycastle.jce.interfaces.ElGamalPublicKey;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ElGamalKeySpec;
import org.spongycastle.jce.spec.ElGamalParameterSpec;
import org.spongycastle.jce.spec.ElGamalPrivateKeySpec;
import org.spongycastle.jce.spec.ElGamalPublicKeySpec;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DummyService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.scytl.cproofs.service.action.FOO";
    private static final String ACTION_BAZ = "com.scytl.cproofs.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.scytl.cproofs.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.scytl.cproofs.service.extra.PARAM2";

    public DummyService() {
        super("DummyService");
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //BigInteger gamma= new BigInteger("26977040468970344916817436272560236319864700366615875034090276748359846158795", 10);
        //BigInteger phi= new BigInteger("48264358234471450298543974766103156025093602668074261823205643879340285575404", 10);
        //BigInteger gamma= new BigInteger("51985929438529043069546285295857790390668654177801604555117571429739731600066", 10);
        //BigInteger phi= new BigInteger("86039887611578881074100029400010860188846173427938638709000291207269094356115", 10);

        // This parameters will be provided by the VoteReader
        BigInteger p = new BigInteger("99976470165175388020372618696659135559000541294319102960724692632258331114023", 10);
        BigInteger g = new BigInteger("60699690203962014620521923794584583166473054082899220704088625522935045687608", 10);
        BigInteger q = new BigInteger("49988235082587694010186309348329567779500270647159551480362346316129165557011", 10);
        BigInteger y = new BigInteger("38899979672714366756637464142222961257539929693695420461084339906663818916907", 10);
        BigInteger x = new BigInteger("37287667606334117345303432074042478191789701966842378421629338052171976701925", 10);
        BigInteger gamma= new BigInteger("84102809714541700453291794954667262606709961488098848034143805286538840450669", 10);
        BigInteger phi= new BigInteger("21125054694553918344036988366869566213933640286564391271536277560858896175426", 10);
        // Initialization. The VoteReader will provide those too
        Parameters parameters = new ElGamalExtendedParameterSpec(p, g, q);
        Message message = new ElGamalMessage(phi, gamma);
        Vote vote = new ElGamalVote(parameters, message, x, 1);
        vote.verify();
    }
}
