package com.zyuma.remindme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private TextView mTitle;
    private Button mBackButton;
    private int size;

    public static HistoryFragment newInstance() {
        HistoryFragment f = new HistoryFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container,
                false);

        mTitle = (TextView) view.findViewById(R.id.title);
        //mTitle.setTypeface(MainActivity.lobster_font);

        mBackButton = (Button) view.findViewById(R.id.backbutton);
        //mBackButton.setTypeface(MainActivity.lobster_font);
        mBackButton.setOnClickListener(this);

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
