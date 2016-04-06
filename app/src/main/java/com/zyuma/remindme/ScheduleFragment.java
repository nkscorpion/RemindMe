package com.zyuma.remindme;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleFragment extends Fragment {

    private TextView mTitle;
    private ArrayList<CustomNotification> mNotificationList;
    private ListView mListView;

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
                FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.container, NewReminderFragment.newInstance());
                fm.addToBackStack(null).commit();
            }
        });

        // Get the scheduled notifications from DB
        mNotificationList = new ArrayList<CustomNotification>();
        new ReadFromDatabaseTask((MainActivity) getActivity()).execute();

        // Setup for list
        mListView = (ListView) view.findViewById(R.id.list);

        return view;
    }

    private class ReadFromDatabaseTask extends AsyncTask<Void, Void, Void> {

        private AlertDialog dialog;
        private MainActivity activity;

        public ReadFromDatabaseTask(MainActivity activity) {
            super();
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Retrieving...");
            builder.setView(new ProgressBar(activity));
            builder.setCancelable(false);
            if (dialog == null) {
                dialog = builder.create();
            }
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            NotificationDBHelper helper = new NotificationDBHelper(activity);
            mNotificationList = helper.getScheduledNotifications();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
            //Setting up the list
            ArrayList<String> reminderList = new ArrayList<String>();
            int size = mNotificationList.size();
            Log.i("SCHEDULE", Integer.toString(size));
            if(size != 0) {
                for (int i=0; i<size; i++) {
                    reminderList.add(mNotificationList.get(i).getReminder());
                    Date resultTime = new Date(mNotificationList.get(i).getReminderTime());
                    Log.i("SCHEDULE", mNotificationList.get(i).getReminder());
                    Log.i("SCHEDULE", sdf.format(resultTime));
                }
            }

            ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, reminderList);
            mListView.setAdapter(a);
        }
    }
}
