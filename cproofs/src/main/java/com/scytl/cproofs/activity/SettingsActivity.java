package com.scytl.cproofs.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.scytl.cproofs.R;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;
import com.scytl.cproofs.fragment.MainFragment;
import com.scytl.cproofs.reader.ParametersFileReader;
import com.scytl.cproofs.reader.ParametersQrReader;
import com.scytl.cproofs.reader.ParametersReader;
import com.scytl.cproofs.reader.SettingsFileReader;
import com.scytl.cproofs.reader.SettingsReader;
import com.scytl.cproofs.reader.VoteFileReader;
import com.scytl.cproofs.reader.VoteReader;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class SettingsActivity extends RoboFragmentActivity {

    private static final int PARAMETERS_FILE_REQUEST_CODE = 7586;
    private static final int PARAMETERS_QR_REQUEST_CODE = 7587;
    private static final String TAG = "SettingsActivity";

    @InjectView(R.id.settings_parameters_p)
    private TextView pText;
    @InjectView(R.id.settings_parameters_q)
    private TextView qText;
    @InjectView(R.id.settings_parameters_g)
    private TextView gText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Create a file Reader with the selected path
        SettingsReader reader = new SettingsFileReader();
        // Read parameters file
        ElGamalExtendedParameterSpec parameters = reader.read(SettingsFileReader.SETTINGS_FILE, this);
        // Show parameters if they exist
        if (null != parameters) {
            pText.setText(parameters.getP().toString());
            qText.setText(parameters.getQ().toString());
            gText.setText(parameters.getG().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void launchReadFileIntent() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, PARAMETERS_FILE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_read_file) {
            launchReadFileIntent();
        }
        if (id == R.id.action_read_qr) {
            launchReadQrFileIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchReadQrFileIntent() {
        // Create intent with the scanner activity to read a QR
        Intent target = new Intent(this, ScannerActivity.class);
        // Start QR Scanner
        startActivityForResult(target, PARAMETERS_QR_REQUEST_CODE);
    }

    private void storeParameters(ElGamalExtendedParameterSpec parameters) {
        SettingsFileReader reader = new SettingsFileReader();
        if (null != parameters) {
            if (true == reader.write(parameters, this)) {
                // Show parameters in screen
                pText.setText(parameters.getP().toString());
                qText.setText(parameters.getQ().toString());
                gText.setText(parameters.getG().toString());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PARAMETERS_FILE_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {

                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(this,
                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
                            // Create a file Reader with the selected path
                            ParametersReader reader = new ParametersFileReader();
                            // Store parameters in settings file if they are valid
                            storeParameters(reader.read(path));
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
            case PARAMETERS_QR_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Read from QR code");
                    // Create a reader to parse the QR Data
                    ParametersReader reader = new ParametersQrReader();
                    // Act depending on vote data
                    storeParameters(reader.read(data.getStringExtra(ScannerActivity.QR_DATA)));
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
