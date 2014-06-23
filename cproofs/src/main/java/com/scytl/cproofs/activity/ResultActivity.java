package com.scytl.cproofs.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.scytl.cproofs.R;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ResultActivity extends RoboActivity {

    @InjectView(R.id.result_image)
    ImageView resultImage;

    @InjectView(R.id.result_message)
    TextView resultMessage;

    public static String VOTE_VALID = "vote_valid";
    public static String VOTE_ERROR = "vote_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String voteError = getIntent().getStringExtra(VOTE_ERROR);
        Boolean voteValid = getIntent().getBooleanExtra(VOTE_VALID, false);
        // If there is an error,
        if (null != voteError) {
            resultMessage.setText(voteError);
            resultImage.setImageResource(R.drawable.caution);
        }
        else if (true == voteValid) {
            resultMessage.setText(R.string.valid_vote);
            resultImage.setImageResource(R.drawable.check);
        }
        else {
            resultMessage.setText(R.string.invalid_vote);
            resultImage.setImageResource(R.drawable.x);
        }
    }
}
