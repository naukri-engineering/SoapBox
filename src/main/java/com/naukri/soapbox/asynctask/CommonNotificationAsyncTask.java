package com.naukri.soapbox.asynctask;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;

import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.database.DatabaseHelper;
import com.naukri.soapbox.util.NotificationBuilder;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

/**
 * Created by vishnu.anand on 8/10/2016.
 */
public class CommonNotificationAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private NotificationData mNotificationData;
    private Notification mNotificationObject;

    public CommonNotificationAsyncTask(Context context, NotificationData notificationBean,
                                       Notification notificationObject) {
        mContext = context;
        mNotificationData = notificationBean;
        mNotificationObject = notificationObject;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            byte[] ObjectByteArray = Utilities.serialize(mNotificationData);
            DatabaseHelper.getDatabaseHelper(mContext).insertNotificationData(ObjectByteArray, mNotificationData.notificationId);

            NotificationBuilder.getInstance(mContext).startNotification(mNotificationData.notificationId,
                    mNotificationObject);
        } catch (Exception e) {
            SoapBoxLogger.error("Notifications error msg", e.getMessage());
        }

        return null;
    }

}
