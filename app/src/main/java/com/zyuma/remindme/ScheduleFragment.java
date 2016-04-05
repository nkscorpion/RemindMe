package com.zyuma.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    private TextView mTitle;

    public static ScheduleFragment newInstance() {
        ScheduleFragment f = new ScheduleFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container,
                false);

        mTitle = (TextView) view.findViewById(R.id.title);
        //mTitle.setTypeface(MainActivity.lobster_font);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FAB", "PRESSED");
                FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.container, NewReminderFragment.newInstance());
                fm.addToBackStack(null).commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fm = getFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.backbutton:

                fm.replace(R.id.container, StartFragment.newInstance()).commit();
                break;
        }
    }
}
