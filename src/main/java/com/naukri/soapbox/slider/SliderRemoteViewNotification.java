package com.naukri.soapbox.slider;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.naukri.soapbox.R;
import com.naukri.soapbox.base.RemoteViewNotification;
import com.naukri.soapbox.bean.SliderNotificationData;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.UserBroadcastActionKeys;
import com.naukri.soapbox.util.Utilities;

import java.util.HashMap;

/**
 * Created by vishnu.anand on 8/1/2016.
 */
public class SliderRemoteViewNotification extends RemoteViewNotification {

    private static SliderRemoteViewNotification mSliderNotification;
    private Context mContext;

    private SliderRemoteViewNotification(Context context) {
        mContext = context;
    }

    public static SliderRemoteViewNotification getInstance(Context context) {
        if (mSliderNotification == null) {
            mSliderNotification = new SliderRemoteViewNotification(context);
        }
        return mSliderNotification;
    }

    public RemoteViews getJobDetailRemoteView(int currentIndex, SliderNotificationData sliderNotificationBean, Notification notification) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.slider_notification);

        if (sliderNotificationBean.notificationLargeIcon != 0) {
            initializeTopLayout(remoteViews, sliderNotificationBean.notificationLargeIcon, sliderNotificationBean.notificationTitle,
                    sliderNotificationBean.notificationText);
        } else {
            initializeTopLayout(remoteViews, sliderNotificationBean.notificationSmallIcon, sliderNotificationBean.notificationTitle,
                    sliderNotificationBean.notificationText);
        }

        setOnClickOnTopLayoutOfNotification(mContext, remoteViews, sliderNotificationBean.notificationId);

        if (sliderNotificationBean.isNotificationCyclic) {
            showParticularJobDetail(currentIndex, remoteViews, sliderNotificationBean, notification);
        } else {

            if (currentIndex == sliderNotificationBean.sliderCards.size()) {
                remoteViews.setViewVisibility(R.id.job_detail_layout, View.GONE);
                remoteViews.setViewVisibility(R.id.last_notification_page_layout, View.VISIBLE);

                remoteViews.setTextViewText(R.id.notification_last_page_txt, sliderNotificationBean.notificationLastPageText);
            } else {
                showParticularJobDetail(currentIndex, remoteViews, sliderNotificationBean, notification);
            }

            setOnClickIntentOnLastPageOfNotification(currentIndex, remoteViews);
        }

        showBottomLayoutOfSliderNotification(currentIndex, remoteViews, sliderNotificationBean);
        implementJobDetailOnClick(currentIndex, remoteViews, sliderNotificationBean);

        return remoteViews;
    }

    private void implementJobDetailOnClick(int currentIndex, RemoteViews remoteViews, SliderNotificationData sliderNotificationBean) {
        if (sliderNotificationBean.returningDataList != null && sliderNotificationBean.returningDataList.size() > currentIndex) {
            setonClickIntentOnParticularJobDetail(currentIndex, sliderNotificationBean.returningDataList.get(currentIndex), remoteViews);
        } else {
            if (currentIndex != sliderNotificationBean.sliderCards.size()) {
                setonClickIntentOnParticularJobDetail(currentIndex, new HashMap<String, String>(), remoteViews);
            }
        }
    }

    private void showBottomLayoutOfSliderNotification(int currentIndex, RemoteViews remoteViews,
                                                      SliderNotificationData sliderNotificationBean) {
        if (sliderNotificationBean.sliderCards.size() > 1) {
            remoteViews.setViewVisibility(R.id.notification_bottom_layout, View.VISIBLE);

            if (sliderNotificationBean.isNotificationCyclic) {
                showNextAndPreviousBottomLayout(remoteViews);
            } else {
                if (currentIndex == SoapBoxConstants.STARTING_NOTIFICATION_INDEX) {
                    showNextAndViewAllBottomLayout(remoteViews);
                } else if (currentIndex == sliderNotificationBean.sliderCards.size()) {
                    showOnlyPreviousBottomLayout(remoteViews);
                } else {
                    showNextAndPreviousBottomLayout(remoteViews);
                }

                setOnClickIntentOnViewAllButton(currentIndex, remoteViews);
            }

            setOnClickIntentOnBottomButtons(SoapBoxConstants.SLIDER_NOTIFICATION_BACK_ACTION, R.id.previous_layout,
                    currentIndex, sliderNotificationBean.notificationId, remoteViews);
            setOnClickIntentOnBottomButtons(SoapBoxConstants.SLIDER_NOTIFICATION_FRONT_ACTION, R.id.next_layout,
                    currentIndex, sliderNotificationBean.notificationId, remoteViews);
        } else {
            remoteViews.setViewVisibility(R.id.notification_bottom_layout, View.GONE);
        }
    }

    private void showOnlyPreviousBottomLayout(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.view_all_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.next_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.previous_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.prev_image_layout, View.VISIBLE);
    }

    private void showNextAndPreviousBottomLayout(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.view_all_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.next_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.previous_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.prev_image_layout, View.VISIBLE);
    }

    private void showNextAndViewAllBottomLayout(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.view_all_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.next_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.previous_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.prev_image_layout, View.GONE);
    }


    private void showParticularJobDetail(int currentIndex, RemoteViews remoteViews, SliderNotificationData sliderNotificationBean,
                                         Notification notification) {

        remoteViews.setViewVisibility(R.id.job_detail_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.last_notification_page_layout, View.GONE);

        remoteViews.setTextViewText(R.id.job_title, sliderNotificationBean.sliderCards.get(currentIndex).title);
        remoteViews.setTextViewText(R.id.job_description, sliderNotificationBean.sliderCards.get(currentIndex).description);
        remoteViews.setImageViewResource(R.id.job_icon, sliderNotificationBean.notificationSmallIcon);

        Utilities.loadImageFromImageUrl(mContext, sliderNotificationBean.sliderCards.get(currentIndex).iconUrl, remoteViews,
                R.id.job_icon, sliderNotificationBean.notificationId, notification);
    }

    private void setonClickIntentOnParticularJobDetail(int currentIndex, HashMap<String, String> returningObject, RemoteViews remoteViews) {
        Intent intent = new Intent(UserBroadcastActionKeys.NOTIFICATION_JOB_DETAIL_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_INDEX, currentIndex);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_RETURNING_OBJECT, returningObject);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_SLIDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, currentIndex, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.job_detail_layout, pendingIntent);
    }

    private void setOnClickIntentOnViewAllButton(int currentIndex, RemoteViews remoteViews) {
        Intent intent = new Intent(UserBroadcastActionKeys.NOTIFICATION_VIEW_ALL_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_SLIDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, currentIndex, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.view_all_layout, pendingIntent);
    }

    private void setOnClickIntentOnLastPageOfNotification(int currentIndex, RemoteViews remoteViews) {
        Intent intent = new Intent(UserBroadcastActionKeys.NOTIFICATION_LAST_PAGE_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_SLIDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, currentIndex, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.last_notification_page_layout, pendingIntent);
    }

    private void setOnClickIntentOnBottomButtons(String action, int imageResourceId, int currentIndex, int notificationId,
                                                 RemoteViews remoteViews) {

        Intent intent = new Intent(action);
        intent.putExtra(SoapBoxConstants.INDEX, currentIndex);
        intent.putExtra(SoapBoxConstants.NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(imageResourceId, pendingIntent);
    }
}
