package com.zyuma.remindme;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class StartFragment extends Fragment implements View.OnClickListener {

    private TextView mTitle;
    private Button mCheckButton;
    private Button mScheduleButton;
    private Button mExitButton;

    public static StartFragment newInstance() {
        StartFragment f = new StartFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        mTitle = (TextView) view.findViewById(R.id.title);
        Typeface champagne = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cac_champagne.ttf");
        Typeface raleway_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
        mTitle.setTypeface(champagne);

        mScheduleButton = (Button) view.findViewById(R.id.schedulebutton);
        mScheduleButton.setTypeface(raleway_regular);
        mScheduleButton.setOnClickListener(this);

        mExitButton = (Button) view.findViewById(R.id.exitbutton);
        mExitButton.setTypeface(raleway_regular);
        mExitButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        FragmentTransaction fm = getFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.schedulebutton:

                fm.replace(R.id.container, ScheduleFragment.newInstance());
                fm.addToBackStack(null).commit();
                break;


            case R.id.exitbutton:

                getActivity().finish();
                break;
        }
    }
}
