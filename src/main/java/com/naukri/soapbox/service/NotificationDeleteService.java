package com.naukri.soapbox.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.naukri.soapbox.database.DatabaseHelper;
import com.naukri.soapbox.database.NotificationDatabase;
import com.naukri.soapbox.util.NotificationBuilder;
import com.naukri.soapbox.util.SoapBoxLogger;

/**
 * Created by vishnu.anand on 9/9/2016.
 */
public class NotificationDeleteService extends IntentService {

    public static final String TAG = "NotificationDeleteService";

    public NotificationDeleteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        DatabaseHelper database = DatabaseHelper.getDatabaseHelper(getApplicationContext());
        Cursor cursor = database.getAllNotificationFromDatabase();

        if (cursor != null && cursor.getCount() > 0) {
            try {
                while (cursor.moveToNext()) {
                    int notificationIdIndex = cursor.getColumnIndex(NotificationDatabase.NOTIFICATION_ID);
                    int notificationId = cursor.getInt(notificationIdIndex);
                    NotificationBuilder.getInstance(getApplicationContext()).deleteNotification(notificationId);
                }

                database.deleteAllNotifications();
            } catch (Exception e) {
                SoapBoxLogger.error("Notifications error msg", e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

    }
}
