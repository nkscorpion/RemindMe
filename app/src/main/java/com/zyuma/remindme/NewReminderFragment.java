package com.zyuma.remindme;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by yuma on 2016-04-02.
 */
public class NewReminderFragment extends Fragment {

    private Typeface raleway_italian;
    private Typeface raleway_regular;
    private EditText mReminderField;
    private EditText mReminderTimeField;
    private String mReminder;
    private int mReminderTime;
    private CustomNotification mNotification;
    private boolean flag_savingTaskRunning = false;

    public static NewReminderFragment newInstance() {
        NewReminderFragment f = new NewReminderFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Notification");

        View view = inflater.inflate(R.layout.fragment_new_reminder, container, false);

        raleway_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
        raleway_italian = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Italic.ttf");

        mReminderField = (EditText) view.findViewById(R.id.reminder_text);
        mReminderField.setTypeface(raleway_regular);
        mReminderTimeField = (EditText) view.findViewById(R.id.reminder_time);
        mReminderTimeField.setTypeface(raleway_regular);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FAB", "" + mReminderField.getText().toString());
                // TO DO: Collect text and time and save to database and go back. Maybe a toast?

                mReminder = mReminderField.getText().toString();
                try {
                    mReminderTime = Integer.parseInt(mReminderTimeField.getText().toString());
                    Random r = new Random();
                    int id = r.nextInt(9999 - 0) + 0;
                    scheduleNotification(id, mReminder, mReminderTime);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();

                } catch (NumberFormatException e) {
                    showToast("Enter valid time");
                }

            }
        });

        return view;
    }

    public void showToast(String toastMsg) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getActivity(), toastMsg, duration);
        toast.show();
    }

    private void scheduleNotification(int id, String reminder, int time) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // Schedule pending intent for notification
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("REMINDER", reminder);
        notificationIntent.putExtra("ID", id);
        notificationIntent.putExtra("REMINDER_TIME", getReminderTime(time));

        PendingIntent broadcast = PendingIntent.getBroadcast(
                getActivity(),
                100,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, time);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

        // Update the scheduled notification database
        mNotification = new CustomNotification();
        mNotification.setID(id);
        mNotification.setReminder(reminder);
        mNotification.setWaitTime(time);
        mNotification.setReminderTime(getReminderTime(time));

        if (!flag_savingTaskRunning) {
            flag_savingTaskRunning = true;
            new SaveToDatabaseTask((MainActivity) getActivity()).execute();
            Log.i("NEW", "Saved");
        }
        // Toast for completion
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultTime = new Date(mNotification.getReminderTime());
        showToast(mReminder + " for " + sdf.format(resultTime));
    }

    private long getReminderTime(int time) {
        long reminderTime = System.currentTimeMillis() + ((long) (time*1000));
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        Date resultdate = new Date(reminderTime);
        Log.i("NOW", sdf.format(currentDate));
        Log.i("FUTURE", sdf.format(resultdate));
        return reminderTime;
    }

    private class SaveToDatabaseTask extends AsyncTask<Void, Void, Void> {

        private MainActivity activity;
        private AlertDialog dialog;

        public SaveToDatabaseTask(MainActivity activity) {
            super();
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Saving");
            builder.setView(new ProgressBar(activity));
            builder.setCancelable(false);
            if(dialog == null) {
                dialog = builder.create();
            }
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            NotificationDBHelper helper = new NotificationDBHelper(activity);
            helper.addNotification(mNotification);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
        }

    }

}
