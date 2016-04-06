package com.zyuma.remindme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private String mReminder;
    private int mID;
    private long mReminderTime;
    private int color = 0x9B26AF;

    @Override
    public void onReceive(Context context, Intent intent) {

        mReminder = intent.getStringExtra("REMINDER");
        mID = intent.getIntExtra("ID", 0);
        mReminderTime = intent.getLongExtra("REMINDER_TIME", 0);

        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultTime = new Date(mReminderTime);
        Notification notification = builder.setContentTitle(mReminder)
                .setContentText("at: " + sdf.format(resultTime))
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(color)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mID, notification);

        Log.i("ALARM", "before INTENT SERVICE");
        Intent msgIntent = new Intent(context, DeleteNotificationEntryService.class);
        msgIntent.putExtra("ID", mID);
        context.startService(msgIntent);

    }


}

