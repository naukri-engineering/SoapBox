package com.naukri.soapbox.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.naukri.soapbox.Interface.Notifier;
import com.naukri.soapbox.bean.NormalNotificationData;
import com.naukri.soapbox.bean.NotificationData;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by vishnu.anand on 8/8/2016.
 */
public class Utilities {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static void throwRunTimeException(String message) {
        throw new RuntimeException(message);
    }

    public static void loadImageFromImageUrl(Context context, String url, RemoteViews remoteView, int imageResourceId, int notificationId,
                                             Notification notification) {
        if (url != null && !url.equalsIgnoreCase("")) {
            remoteView.setViewVisibility(imageResourceId, View.VISIBLE);
            Picasso
                    .with(context)
                    .load(url)
                    .resize(300, 200)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(remoteView, imageResourceId, notificationId, notification);
        } else {
            remoteView.setViewVisibility(imageResourceId, View.GONE);
        }
    }

    public static void setJobAlarm(Context context, Class<? extends Service> cls,
                                   long interval) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, interval,
                    pendingIntent);
        }
    }

    public static int getNotificationId(Notifier notifier, int notificationId) {
        if (notificationId == 0) {
            return notifier.hashCode();
        } else {
            return notificationId;
        }
    }

    public static NotificationData getNotificationDataAsPerBuildVersion(NotificationData notificationData) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return notificationData;
        } else {
            if (notificationData instanceof NormalNotificationData) {
                return notificationData;
            } else {
                NormalNotificationData normalNotificationData = new NormalNotificationData();
                normalNotificationData.notificationId = notificationData.notificationId;
                normalNotificationData.notificationSmallIcon = notificationData.notificationSmallIcon;
                normalNotificationData.notificationLargeIcon = notificationData.notificationLargeIcon;
                normalNotificationData.notificationTitle = notificationData.notificationTitle;
                normalNotificationData.notificationText = notificationData.notificationText;
                return normalNotificationData;
            }
        }
    }

}
