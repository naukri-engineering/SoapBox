package com.naukri.soapbox.sender;

import android.app.Notification;
import android.content.Context;

import com.naukri.soapbox.asynctask.CommonNotificationAsyncTask;
import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.util.NotificationBuilder;
import com.naukri.soapbox.util.NotificationSender;

/**
 * Created by minni on 4/10/16.
 */

public class CustomNotificationSender implements NotificationSender {

    public void send(Context context, NotificationData notificationBean, Notification notification) {

        CommonNotificationAsyncTask asyncTask = new CommonNotificationAsyncTask(context, notificationBean, notification);
        asyncTask.execute();
    }

    @Override
    public Notification build(Context context, NotificationData notificationData) {
        return NotificationBuilder.getInstance(context).getCustomNotificationObject(notificationData, true);
    }
}
