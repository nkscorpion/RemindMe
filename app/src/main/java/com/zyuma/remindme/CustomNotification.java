package com.zyuma.remindme;

/**
 * Created by yuma on 2016-04-05.
 */
public class CustomNotification {

    //private variables
    int _id;
    String _reminder;
    int _wait_time;
    long _reminder_time;

    // Empty constructor
    public CustomNotification() {

    }

    // constructor
    public CustomNotification(int id, String reminder, int wait_time, long reminder_time) {
        this._id = id;
        this._reminder = reminder;
        this._wait_time = wait_time;
        this._reminder_time = reminder_time;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getReminder() {
        return this._reminder;
    }

    // setting name
    public void setReminder(String reminder) {
        this._reminder = reminder;
    }

    // getting phone number
    public int getWaitTime() {
        return this._wait_time;
    }

    // setting phone number
    public void setWaitTime(int waittime) {
        this._wait_time = waittime;
    }

    // getting phone number
    public long getReminderTime() {
        return this._reminder_time;
    }

    // setting phone number
    public void setReminderTime(long timestamp) {
        this._reminder_time = timestamp;
    }

}
