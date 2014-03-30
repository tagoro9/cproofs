package com.scytl.cproofs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.scytl.cproofs.R;
import com.scytl.cproofs.service.DummyService;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class MainFragment extends RoboFragment {

    @InjectView(R.id.trigger_button)
    Button triggerButton;

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
        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTriggerButtonClick();
            }
        });
    }

    // Handle trigger button click
    public void handleTriggerButtonClick() {
        Log.i(getTag(), "Trigger button clicked");
        Intent intent = new Intent(getActivity(),DummyService.class);
        getActivity().startService(intent);
    }

}
