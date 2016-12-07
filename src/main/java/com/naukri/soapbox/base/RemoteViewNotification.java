package com.naukri.soapbox.base;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.naukri.soapbox.R;
import com.naukri.soapbox.util.UserBroadcastActionKeys;

/**
 * Created by vishnu.anand on 10/14/2016.
 */

public class RemoteViewNotification {

    protected void initializeTopLayout(RemoteViews remoteViews, int icon, String title, String text) {

        remoteViews.setImageViewResource(R.id.small_icon, icon);
        remoteViews.setTextViewText(R.id.content_title, title);
        remoteViews.setTextViewText(R.id.content_text, text);
    }

    protected void setOnClickOnTopLayoutOfNotification(Context context, RemoteViews remoteViews, int notificationId) {
        Intent intent = new Intent(UserBroadcastActionKeys.NORMAL_NOTIFICATION_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_NORMAL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.top_layout, pendingIntent);
    }

}
