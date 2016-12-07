package com.naukri.soapbox.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.naukri.soapbox.bean.NotificationData;

/**
 * Created by vishnu.anand on 8/1/2016.
 */
public class NotificationBuilder {

    private static NotificationBuilder mBuilder;
    private static Notification mNotification;
    private Context mContext;

    private NotificationBuilder(Context context) {
        mContext = context;
    }

    public static NotificationBuilder getInstance(Context context) {
        if (mBuilder == null) {
            mBuilder = new NotificationBuilder(context);
        }
        return mBuilder;
    }

    public Notification getCustomNotificationObject(NotificationData notificationData, boolean isNotificationNew) {

        if (isNotificationNew) {
            makeNewNotificationObject(notificationData.notificationSmallIcon, notificationData.notificationLargeIcon,
                    notificationData.notificationTitle, notificationData.notificationText);
        } else {
            if (mNotification == null) {
                makeNewNotificationObject(notificationData.notificationSmallIcon, notificationData.notificationLargeIcon,
                        notificationData.notificationTitle, notificationData.notificationText);
            }
        }

        return mNotification;
    }

    private void makeNewNotificationObject(int smallIcon, int largeIcon, String contentTitle, String contentText) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(smallIcon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true);

        Bitmap largeIconBitmap = showLargeIcon(builder, largeIcon);

        mNotification = builder.build();

        hideSmallIcon(largeIconBitmap);
    }

    public NotificationCompat.Builder getNormalNotificationObject(NotificationData notificationData) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(notificationData.notificationSmallIcon)
                .setContentTitle(notificationData.notificationTitle)
                .setContentText(notificationData.notificationText)
                .setAutoCancel(true);

        return builder;
    }

    public void startNotification(int notificationId, Notification notification) {
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationId, notification);
    }

    public void deleteNotification(int notificationId) {
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(notificationId);
    }

    private Bitmap showLargeIcon(NotificationCompat.Builder builder, int largeIcon) {
        if (largeIcon != 0) {
            Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(largeIcon)).getBitmap();
            if (bitmap != null) {
                builder.setLargeIcon(bitmap);
            }

            return bitmap;
        } else {
            return null;
        }

    }

    private void hideSmallIcon(Bitmap largeIconBitmap) {

        // Hide small icon if large icon is shown from the notification.
        int smallIconId = mContext.getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
        if (smallIconId != 0 && largeIconBitmap != null) {
            mNotification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
        }
    }

}
