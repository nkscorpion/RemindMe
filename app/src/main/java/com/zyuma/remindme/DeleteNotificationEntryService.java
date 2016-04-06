package com.zyuma.remindme;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yuma on 2016-04-06.
 */
public class DeleteNotificationEntryService extends IntentService {

    private int mID;

    public DeleteNotificationEntryService() {
        super("SimpleIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("INTENTSERVICE", "before delete");
        mID = intent.getIntExtra("ID", 0);
        NotificationDBHelper helper = new NotificationDBHelper(getApplicationContext());
        helper.deleteNotification(mID);
    }
}
