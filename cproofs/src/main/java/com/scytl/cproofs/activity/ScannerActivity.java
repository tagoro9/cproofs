package com.scytl.cproofs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import roboguice.activity.RoboActivity;

/**
 * Created by victor on 6/22/14.
 */
public class ScannerActivity extends RoboActivity implements ZXingScannerView.ResultHandler {

    public static String QR_DATA = "qr_data";
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        //create a new intent...
        Intent intent = new Intent();
        intent.putExtra(QR_DATA, rawResult.getText());
        setResult(RESULT_OK, intent);
        finish();
    }

}
