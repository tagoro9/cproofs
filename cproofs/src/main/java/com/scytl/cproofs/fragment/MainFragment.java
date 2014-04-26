package com.scytl.cproofs.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.scytl.cproofs.R;
import com.scytl.cproofs.crypto.exceptions.CProofsException;
import com.scytl.cproofs.crypto.exceptions.InvalidParametersException;
import com.scytl.cproofs.crypto.exceptions.InvalidSignatureException;
import com.scytl.cproofs.reader.VoteFileReader;
import com.scytl.cproofs.reader.VoteReader;
import com.scytl.cproofs.service.DummyService;
import com.scytl.cproofs.vote.Vote;

import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class MainFragment extends RoboFragment {

    @InjectView(R.id.file_text)
    private TextView fileText;

    @InjectView(R.id.choice_text)
    private EditText choiceText;

    @InjectView(R.id.validate_button)
    private Button validateButton;

    @InjectView(R.id.no_file_layout)
    private RelativeLayout noFileLayout;

    private List<Vote> votes;

    private static final int REQUEST_CODE = 6384;
    private static final String TAG = "FileChooserExampleActivity";

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        choiceText.addTextChangedListener(createTextWatcher());
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateVoteWithInput(choiceText.getText().toString());
            }
        });
    }

    private void validateVoteWithInput(String input) {
        try {
            Boolean valid = votes.get(0).verify(input);
            displayVote(votes.get(0), valid);
        } catch (InvalidParametersException e) {
            displayError();
        }
        catch (InvalidSignatureException e) {
            displayError();
        }
    }

    private void displayVote(Vote vote, Boolean valid) {
        showNoFileMessage(false);
    }

    private void showNoFileMessage(boolean show) {
        noFileLayout.setVisibility(show ? 1 : 0);
    }

    private void displayError() {

    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (choiceText.getText().length() > 0 && null != votes && votes.size() > 0) {
                    validateButton.setEnabled(true);
                }
                else {
                    validateButton.setEnabled(false);
                }
            }
        };
    }

    public void launchReadFileIntent() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {

                            // Get the file path from the URI
                            final String path = FileUtils.getPath(getActivity(), uri);
                            Toast.makeText(getActivity(),
                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
                            fileText.setText(path);
                            // Create a file Reader with the selected path
                            VoteReader reader = new VoteFileReader(path);
                            // Read the votes in the file
                            votes = reader.read();
                            // Focus the choice text input
                            choiceText.requestFocus();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
