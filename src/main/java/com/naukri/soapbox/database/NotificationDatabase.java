package com.naukri.soapbox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vishnu.anand on 8/7/2016.
 */
public class NotificationDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static NotificationDatabase mInstance = null;

    private static final String DATABASE_NAME = "NotificationDatabase";

    public static final String TABLE_NOTIFICATION = "Notification";

    public static final String ID = "_id";
    public static final String NOTIFICATION_OBJECT = "notification_object";
    public static final String NOTIFICATION_ID = "notification_id";

    private String CREATE_TABLE_NOTIFICATION = "CREATE TABLE " + TABLE_NOTIFICATION + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOTIFICATION_ID + " INTEGER,"
            + NOTIFICATION_OBJECT + " BLOB )";




    public NotificationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static NotificationDatabase getInstance(Context activityContext) {

        if (mInstance == null) {
            mInstance = new NotificationDatabase(activityContext.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_NOTIFICATION);

        onCreate(db);
    }

}
