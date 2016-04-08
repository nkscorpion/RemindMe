package com.zyuma.remindme;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    private ArrayList<CustomNotification> mNotificationList;
    private ListView mListView;
    private ScheduleAdapter mAdapter;

    public static ScheduleFragment newInstance() {
        ScheduleFragment f = new ScheduleFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Schedule");

        View view = inflater.inflate(R.layout.fragment_schedule, container,
                false);



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
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                final int p = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to cancel this reminder?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();
                        // Cancel notification
                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(mNotificationList.get(p).getID());
                        // Remove notification entry in DB
                        Intent msgIntent = new Intent(getActivity(), DeleteNotificationEntryService.class);
                        msgIntent.putExtra("ID", mNotificationList.get(p).getID());
                        getActivity().startService(msgIntent);
                        // Remove from list
                        mAdapter.remove(mAdapter.getItem(p));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                // Create the AlertDialog object and return it
                AlertDialog alertdialog = builder.create();
                alertdialog.show();

                //showCancelDialog(position);
                Toast.makeText(getActivity(), "ID: " + mNotificationList.get(position).getID(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

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
            if(size != 0) {
                for (int i=0; i<size; i++) {
                    reminderList.add(mNotificationList.get(i).getReminder());
                }
            }

            mAdapter = new ScheduleAdapter(getActivity(), mNotificationList);
            mListView.setAdapter(mAdapter);
        }
    }

//    private void showCancelDialog(int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Do you want to cancel this reminder?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int i) {
//                Log.i("CANCEL DIALOG", "ID: " + p);
//                Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();
//                // Cancel notification
//                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(position);
//                // Remove notification entry in DB
//                Intent msgIntent = new Intent(getActivity(), DeleteNotificationEntryService.class);
//                msgIntent.putExtra("ID", i);
//                getActivity().startService(msgIntent);
//                // Remove from list
//                mAdapter.remove(mAdapter.getItem(position));
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//        // Create the AlertDialog object and return it
//        AlertDialog alertdialog = builder.create();
//        alertdialog.show();
//    }
}
