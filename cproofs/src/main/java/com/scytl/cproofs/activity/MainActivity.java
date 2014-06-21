package com.scytl.cproofs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.scytl.cproofs.R;
import com.scytl.cproofs.fragment.MainFragment;

import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity {

    private static final int REQUEST_CODE = 6384;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment(), "main_fragment")
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_read_file) {
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("main_fragment");
            fragment.launchReadFileIntent();
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_read_qr) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
