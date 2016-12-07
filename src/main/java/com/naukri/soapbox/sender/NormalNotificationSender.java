package com.naukri.soapbox.sender;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.naukri.soapbox.bean.NormalNotificationData;
import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.util.NotificationBuilder;
import com.naukri.soapbox.util.NotificationSender;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.UserBroadcastActionKeys;

public class NormalNotificationSender implements NotificationSender {

    NormalNotificationData mNormalNotificationData;

    @Override
    public void send(Context context, NotificationData notificationBean, Notification notification) {

        NotificationBuilder.getInstance(context).startNotification(notificationBean.notificationId,
                notification);
    }

    @Override
    public Notification build(Context context, NotificationData notificationData) {
        PendingIntent resultPendingIntent = null;
        mNormalNotificationData = (NormalNotificationData) notificationData;
        int intentType = getIntentType();
        Intent contentIntent = getContentIntent();

        NotificationCompat.Builder builder = NotificationBuilder.getInstance(context).getNormalNotificationObject(notificationData);

        if (intentType == SoapBoxConstants.INTENT_TYPE_SERVICE) {
            resultPendingIntent = PendingIntent.getService(context, notificationData.notificationId, contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (intentType == SoapBoxConstants.INTENT_TYPE_BROADCAST) {
            resultPendingIntent = PendingIntent.getBroadcast(context, notificationData.notificationId, contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (intentType == SoapBoxConstants.INTENT_TYPE_ACTIVITY) {
            resultPendingIntent = PendingIntent.getActivity(context, notificationData.notificationId, contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        builder.setContentIntent(resultPendingIntent);
        return builder.build();
    }


    private int getIntentType() {
        if (mNormalNotificationData.intentType != 0) {
            return mNormalNotificationData.intentType;
        } else {
            return SoapBoxConstants.INTENT_TYPE_BROADCAST;
        }

    }

    private Intent getContentIntent() {
        if (mNormalNotificationData.contentIntent != null) {
            return mNormalNotificationData.contentIntent;
        } else {
            return getNormalNotificationClickIntent();
        }
    }

    private Intent getNormalNotificationClickIntent() {
        Intent intent = new Intent(UserBroadcastActionKeys.NORMAL_NOTIFICATION_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_NORMAL);
        return intent;
    }

}
