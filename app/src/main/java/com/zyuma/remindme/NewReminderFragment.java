package com.zyuma.remindme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by yuma on 2016-04-02.
 */
public class NewReminderFragment extends Fragment {

    private TextView mTitle;
    private EditText mReminderText;
    private EditText mReminderTime;

    public static NewReminderFragment newInstance() {
        NewReminderFragment f = new NewReminderFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_reminder, container, false);

        mTitle = (TextView) view.findViewById(R.id.title);
        //mTitle.setTypeface(MainActivity.lobster_font);
        mReminderText = (EditText) view.findViewById(R.id.reminder_text);
        mReminderTime = (EditText) view.findViewById(R.id.reminder_time);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FAB", "" + mReminderText.getText().toString());
                // TO DO: Collect text and time and save to database and go back. Maybe a toast?

                Random r = new Random();
                int id = r.nextInt(9999 - 0) + 0;
                scheduleNotification(id, mReminderText.getText().toString(), Integer.parseInt(mReminderTime.getText().toString()));

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

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("REMINDER", reminder);
        notificationIntent.putExtra("ID", id);

        PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, time);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        showToast("Reminder set for after " + Integer.toString(time) + " seconds");
    }

}
