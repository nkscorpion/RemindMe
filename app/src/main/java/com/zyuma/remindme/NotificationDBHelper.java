package com.zyuma.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by yuma on 2016-04-05.
 */
public class NotificationDBHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "scheduledNotificationDatabase";

    // Notification table name
    private static final String TABLE_NOTIFICATIONS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_REMINDER = "name";
    private static final String KEY_WAIT_TIME = "wait_time";
    private static final String KEY_REMINDER_TIME = "reminder_time";

    private static String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_REMINDER + " TEXT,"
            + KEY_WAIT_TIME + " INTEGER,"
            + KEY_REMINDER_TIME + " INTEGER"
            + ")";

    private static final String DROP_NOTIFICATION_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NOTIFICATIONS;

    public NotificationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(DROP_NOTIFICATION_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void addNotification(CustomNotification cn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, cn.getID());
        values.put(KEY_REMINDER, cn.getReminder());
        values.put(KEY_WAIT_TIME, cn.getWaitTime());
        values.put(KEY_REMINDER_TIME, cn.getReminderTime());

        // Inserting Row
        long result = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<CustomNotification> getScheduledNotifications() {

        ArrayList<CustomNotification> notificationList = new ArrayList<CustomNotification>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_NOTIFICATIONS, null);

        if (cursor.moveToFirst()) {
            do {
                CustomNotification cn = new CustomNotification();
                cn.setID(cursor.getInt(0));
                cn.setReminder(cursor.getString(1));
                cn.setWaitTime(cursor.getInt(2));
                cn.setReminderTime(cursor.getLong(3));
                notificationList.add(cn);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notificationList;
    }

    public void deleteNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        Log.i("DB", "DELETED");
        db.close();
    }

}
