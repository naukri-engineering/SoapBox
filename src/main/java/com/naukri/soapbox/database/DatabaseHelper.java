package com.naukri.soapbox.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by vishnu.anand on 8/7/2016.
 */
public class DatabaseHelper {

    private static DatabaseHelper mDatabaseHelper;
    private Context  mContext;

    public static DatabaseHelper getDatabaseHelper(Context context) {
        if(mDatabaseHelper == null)
        {
            mDatabaseHelper = new DatabaseHelper(context);
        }
        return mDatabaseHelper;
    }

    private DatabaseHelper(Context context)
    {
        mContext = context;
    }

    private SQLiteDatabase getReadableDatabase() {
        return NotificationDatabase.getInstance(mContext).getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return NotificationDatabase.getInstance(mContext).getWritableDatabase();
    }

    public Cursor getAllNotificationFromDatabase() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(NotificationDatabase.TABLE_NOTIFICATION, null, null, null, null, null, null, null);

        return cursor;
    }

    public Cursor getNotificationFromNotificationId(int notificationId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(NotificationDatabase.TABLE_NOTIFICATION, null,
                NotificationDatabase.NOTIFICATION_ID + " = ? ", new String[]{String.valueOf(notificationId)},
                null, null, null, null);

        return cursor;
    }

    public void insertNotificationData(byte[] notificationObjectByteArray, int notificationId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationDatabase.NOTIFICATION_OBJECT, notificationObjectByteArray);
        contentValues.put(NotificationDatabase.NOTIFICATION_ID, notificationId);

        int update = db.update(NotificationDatabase.TABLE_NOTIFICATION, contentValues,
                NotificationDatabase.NOTIFICATION_ID + " = ? ", new String[]{String.valueOf(notificationId)});

        if (update == 0) {
            db.insert(NotificationDatabase.TABLE_NOTIFICATION, null, contentValues);
        }

    }

    public void deleteNotification(final int notificationId) {
        final SQLiteDatabase db = getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.delete(NotificationDatabase.TABLE_NOTIFICATION, NotificationDatabase.NOTIFICATION_ID + " = ? ",
                        new String[]{String.valueOf(String.valueOf(notificationId))});
            }
        }).start();
    }

    public void deleteAllNotifications() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NotificationDatabase.TABLE_NOTIFICATION, null, null);

    }


}
