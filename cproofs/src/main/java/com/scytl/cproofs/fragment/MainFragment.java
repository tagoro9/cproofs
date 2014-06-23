package com.scytl.cproofs.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.scytl.cproofs.R;
import com.scytl.cproofs.activity.ResultActivity;
import com.scytl.cproofs.activity.ScannerActivity;
import com.scytl.cproofs.activity.SettingsActivity;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.exceptions.InvalidParametersException;
import com.scytl.cproofs.crypto.exceptions.InvalidSignatureException;
import com.scytl.cproofs.reader.SettingsFileReader;
import com.scytl.cproofs.reader.SettingsReader;
import com.scytl.cproofs.reader.VoteFileReader;
import com.scytl.cproofs.reader.VoteQrReader;
import com.scytl.cproofs.reader.VoteReader;
import com.scytl.cproofs.vote.Vote;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class MainFragment extends RoboFragment {

    @InjectView(R.id.choice_input)
    private EditText choiceText;

    @InjectView(R.id.validate_vote_button)
    private Button validateButton;

    @InjectView(R.id.qr_button)
    private RelativeLayout qrButton;

    @InjectView(R.id.file_button)
    private RelativeLayout fileButton;

    private Vote vote;

    private ElGamalExtendedParameterSpec parameters;

    private static final int READ_FILE_REQUEST_CODE = 6384;
    private static final int READ_QR_REQUEST_CODE = 9876;
    private static final String TAG = "MainFragmentActivity";

    public MainFragment() {}

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
        // Add listener to validate button
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateVoteWithInput(choiceText.getText().toString());
            }
        });
        // Add listener to read buttons
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchReadQrFileIntent();
            }
        });
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchReadFileIntent();
            }
        });
        // Read parameters. If there are no parameters, redirect to settings page
        readParametersFromConfigFile();
        if (null == parameters) {
            // Show a dialog telling the user to load parameters
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.parameters_missing_title);
            builder.setMessage(R.string.parameters_missing_desc);
            builder.setPositiveButton(R.string.parameters_missing_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.parameters_missing_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // cancel the alert box and put a Toast to the user
                    dialog.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), R.string.parameters_missing_toast,
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
    }

    private void readParametersFromConfigFile() {
        SettingsReader settingsReader = new SettingsFileReader();
        parameters = settingsReader.read(SettingsFileReader.SETTINGS_FILE, getActivity());
    }

    // Validate the vote with the input the user entered
    private void validateVoteWithInput(String input) {
        // Get the choice text
        String choice = choiceText.getText().toString();
        if (null != vote) {
            Intent intent = new Intent(getActivity(), ResultActivity.class);
            try {
                Boolean valid = vote.verify(choice);
                intent.putExtra(ResultActivity.VOTE_VALID,valid);
            } catch (InvalidParametersException e) {
                intent.putExtra(ResultActivity.VOTE_ERROR, R.string.invalid_parameters);
            } catch (InvalidSignatureException e) {
                intent.putExtra(ResultActivity.VOTE_ERROR, R.string.invalid_signature);
            }
            startActivity(intent);
        }

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
                // Only allow to validate a vote is there is vote to validate and user has entered an input
                if (choiceText.getText().length() > 0 && null != vote && null != parameters) {
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
        Intent intent = Intent.createChooser(target, null);
        // Start the file reader
        startActivityForResult(intent, READ_FILE_REQUEST_CODE);
    }

    public void launchReadQrFileIntent() {
        // Create intent with the scanner activity to read a QR
        Intent target = new Intent(getActivity(), ScannerActivity.class);
        // Start QR Scanner
        startActivityForResult(target, READ_QR_REQUEST_CODE);
    }

    private void handleVoteRead(Vote readVote) {
        // Store the new vote
        vote = readVote;
        // If vote isn't null
        if (null != vote) {
            // Make the choice input visible
            choiceText.setVisibility(View.VISIBLE);
            // Focus the choice text input
            choiceText.requestFocus();
            // Make the validate button visible
            validateButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case READ_FILE_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(getActivity(), uri);
                            Toast.makeText(getActivity(), "File Selected: " + path, Toast.LENGTH_LONG).show();
                            // Get parameters stored in config file (since they may have changed since startup)
                            readParametersFromConfigFile();
                            // Get file name
                            String fileName = path.substring(path.lastIndexOf("/") + 1);
                            // Create a file Reader with the selected path and current parameters
                            VoteReader reader = new VoteFileReader(path, parameters);
                            // Act depending on vote data
                            handleVoteRead(reader.read());
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
            case READ_QR_REQUEST_CODE:
                // If read process was successful
                if (resultCode == getActivity().RESULT_OK) {
                    Log.i(TAG, "Read from QR code");
                    readParametersFromConfigFile();
                    // Create a reader to parse the QR Data
                    VoteReader reader = new VoteQrReader(data.getStringExtra(ScannerActivity.QR_DATA), parameters);
                    // Act depending on vote data
                    handleVoteRead(reader.read());
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
