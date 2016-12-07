package com.naukri.soapbox.util;

import android.app.Notification;
import android.content.Context;

import com.naukri.soapbox.bean.NotificationData;

/**
 * Created by minni on 4/10/16.
 */
public interface NotificationSender {

    void send(Context context, NotificationData notificationBean, Notification notification);

    Notification build(Context context, NotificationData notificationData);
}
