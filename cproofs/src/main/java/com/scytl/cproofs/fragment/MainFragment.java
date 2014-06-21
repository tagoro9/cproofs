package com.scytl.cproofs.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fima.cardsui.objects.Card;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.scytl.cproofs.R;
import com.scytl.cproofs.activity.SettingsActivity;
import com.scytl.cproofs.cards.VoteCard;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.crypto.exceptions.InvalidParametersException;
import com.scytl.cproofs.crypto.exceptions.InvalidSignatureException;
import com.scytl.cproofs.reader.SettingsFileReader;
import com.scytl.cproofs.reader.SettingsReader;
import com.scytl.cproofs.reader.VoteFileReader;
import com.scytl.cproofs.reader.VoteReader;
import com.scytl.cproofs.vote.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @InjectView(R.id.cardsview)
    private CardUI voteCards;

    @InjectView(R.id.cards_container)
    private LinearLayout cardsContainer;

    @InjectView(R.id.clear_button)
    private Button clearButton;

    @InjectView(R.id.current_file_container)
    private LinearLayout currentFileContainer;

    private Map<String, CardStack> stacks;

    private List<Vote> votes;

    private ElGamalExtendedParameterSpec parameters;

    private static final int REQUEST_CODE = 6384;
    private static final String TAG = "MainFragmentActivity";

    public MainFragment() {
        stacks = new HashMap<String, CardStack>();
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
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCards();
            }
        });
        configureCards();
        // Read parameters. If there are no parameters, redirect to settings page
        // Read application parameters
        SettingsReader settingsReader = new SettingsFileReader();
        parameters = settingsReader.read(SettingsFileReader.SETTINGS_FILE, getActivity());
        if (null == parameters) {
            // Show a dialog telling the user to load parameters
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Parameters missing");
            builder.setMessage("There are no cipher parameters stored. You need to provide one");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // cancel the alert box and put a Toast to the user
                    dialog.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), "There are no parameters stored",
                            Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
    }

    private void clearCards() {
        voteCards.clearCards();
        stacks.clear();
    }

    private void configureCards() {
        voteCards.setSwipeable(false);
    }

    private void validateVoteWithInput(String input) {
        showNoFileMessage(false);
        showCards(true);
        String fileName = fileText.getText().toString();
        String choice = choiceText.getText().toString();
        for (Vote vote : votes) {
            try {
                Boolean valid = vote.verify(input);
                displayVote(fileName, choice, vote, valid);
            } catch (InvalidParametersException e) {
                displayError(fileName, choice, vote, getResources().getString(R.string.invalid_parameters), "#f2a400");
            } catch (InvalidSignatureException e) {
                displayError(fileName, choice, vote, getResources().getString(R.string.invalid_signature), "#ff33b6ea");
            }
        }
        voteCards.refresh();
    }

    private void showCards(boolean show) {
        cardsContainer.setVisibility(View.VISIBLE);
    }

    private CardStack getStackForFile(String file) {
        CardStack stack = stacks.get(file);
        // If there is no stack for current file, create a new one
        if (null == stack) {
            stack = new CardStack("Votes in file  " + file);
            stacks.put(file, stack);
            voteCards.addStack(stack);
        }
        return stack;
    }

    private void displayVote(String file, String choice, Vote vote, Boolean valid) {
        String color = valid ? "#4ac925" : "#e00707";
        String description = getResources().getString(valid? R.string.valid_vote : R.string.invalid_vote);
        VoteCard card = new VoteCard("Validation of choice  " + choice, description, color, color, false, false);
        getStackForFile(file).add(card);
    }

    private void showNoFileMessage(boolean show) {
        noFileLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void displayError(String file, String choice, Vote vote, String error, String color) {
        VoteCard card = new VoteCard("Validation of choice " + choice, error, color, color, false, false);
        getStackForFile(file).add(card);
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
                if (choiceText.getText().length() > 0 && null != votes && votes.size() > 0 && null != parameters) {
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
                            // Get parameters
                            SettingsReader settings = new SettingsFileReader();
                            parameters = settings.read(SettingsFileReader.SETTINGS_FILE, getActivity());
                            // Get file name
                            String fileName = path.substring(path.lastIndexOf("/") + 1);
                            // Update view to notify user what file is loaded at the moment
                            fileText.setText(fileName);
                            // Create a file Reader with the selected path and current parameters
                            VoteReader reader = new VoteFileReader(path, parameters);
                            // Display file text
                            currentFileContainer.setVisibility(View.VISIBLE);
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
