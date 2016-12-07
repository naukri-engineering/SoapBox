package com.naukri.soapbox.carousel;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.naukri.soapbox.R;
import com.naukri.soapbox.base.RemoteViewNotification;
import com.naukri.soapbox.bean.CarouselImageNotificationData;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.UserBroadcastActionKeys;
import com.naukri.soapbox.util.Utilities;

import java.util.HashMap;

/**
 * Created by vishnu.anand on 9/7/2016.
 */
public class CarouselRemoteViewNotification extends RemoteViewNotification {

    private static CarouselRemoteViewNotification mCarouselNotification;
    private Context mContext;

    private CarouselRemoteViewNotification(Context context) {
        mContext = context;
    }

    public static CarouselRemoteViewNotification getInstance(Context context) {
        if (mCarouselNotification == null) {
            mCarouselNotification = new CarouselRemoteViewNotification(context);
        }
        return mCarouselNotification;
    }

    public RemoteViews getImageCarouselRemoteView(int leftImageIndex, int rightImageIndex,
                                                  CarouselImageNotificationData carouselNotificationBean, Notification notification) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.carousel_notification);

        if (carouselNotificationBean.notificationLargeIcon != 0) {
            initializeTopLayout(remoteViews, carouselNotificationBean.notificationLargeIcon, carouselNotificationBean.notificationTitle,
                    carouselNotificationBean.notificationText);
        } else {
            initializeTopLayout(remoteViews, carouselNotificationBean.notificationSmallIcon, carouselNotificationBean.notificationTitle,
                    carouselNotificationBean.notificationText);
        }

        setOnClickOnTopLayoutOfNotification(mContext, remoteViews, carouselNotificationBean.notificationId);

        showImagesOnCarouselNotification(leftImageIndex, rightImageIndex, remoteViews, carouselNotificationBean, notification);

        showBottomLayoutOfCarouselNotification(leftImageIndex, rightImageIndex, remoteViews, carouselNotificationBean);
        setOnClickIntentOnLeftAndRightImages(leftImageIndex, rightImageIndex, remoteViews, carouselNotificationBean);

        return remoteViews;
    }

    private void showImagesOnCarouselNotification(int leftImageIndex, int rightImageIndex, RemoteViews remoteViews,
                                                  CarouselImageNotificationData carouselNotificationBean, Notification notification) {

        if (carouselNotificationBean.isNotificationCyclic) {
            Utilities.loadImageFromImageUrl(mContext, carouselNotificationBean.carouselCards.get(leftImageIndex), remoteViews,
                    R.id.left_image, carouselNotificationBean.notificationId, notification);
            Utilities.loadImageFromImageUrl(mContext, carouselNotificationBean.carouselCards.get(rightImageIndex), remoteViews,
                    R.id.right_image, carouselNotificationBean.notificationId, notification);
        } else {
            if (leftImageIndex == carouselNotificationBean.carouselCards.size() - 1) {
                remoteViews.setViewVisibility(R.id.right_image, View.GONE);
                remoteViews.setViewVisibility(R.id.right_view_all_txt, View.VISIBLE);
                remoteViews.setTextViewText(R.id.right_view_all_txt, carouselNotificationBean.notificationLastPageText);

                Utilities.loadImageFromImageUrl(mContext, carouselNotificationBean.carouselCards.get(leftImageIndex), remoteViews,
                        R.id.left_image, carouselNotificationBean.notificationId, notification);
                setOnClickIntentOnLastPageOfNotification(R.id.right_view_all_txt, carouselNotificationBean.notificationId, remoteViews);

            } else if (leftImageIndex == carouselNotificationBean.carouselCards.size()) {
                remoteViews.setViewVisibility(R.id.image_layout, View.GONE);
                remoteViews.setViewVisibility(R.id.last_notification_page_layout, View.VISIBLE);

                remoteViews.setTextViewText(R.id.notification_last_page_txt, carouselNotificationBean.notificationLastPageText);
                setOnClickIntentOnLastPageOfNotification(R.id.last_notification_page_layout, carouselNotificationBean.notificationId, remoteViews);

            } else {
                remoteViews.setViewVisibility(R.id.image_layout, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.last_notification_page_layout, View.GONE);
                remoteViews.setViewVisibility(R.id.right_image, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.right_view_all_txt, View.GONE);

                Utilities.loadImageFromImageUrl(mContext, carouselNotificationBean.carouselCards.get(leftImageIndex), remoteViews,
                        R.id.left_image, carouselNotificationBean.notificationId, notification);
                Utilities.loadImageFromImageUrl(mContext, carouselNotificationBean.carouselCards.get(rightImageIndex), remoteViews,
                        R.id.right_image, carouselNotificationBean.notificationId, notification);
            }
        }
    }

    private void showBottomLayoutOfCarouselNotification(int leftImageIndex, int rightImageIndex, RemoteViews remoteViews,
                                                        CarouselImageNotificationData carouselNotificationBean) {
        if (carouselNotificationBean.carouselCards.size() > 2) {
            remoteViews.setViewVisibility(R.id.notification_bottom_layout, View.VISIBLE);

            if (carouselNotificationBean.isNotificationCyclic) {
                showNextAndPreviousBottomLayout(remoteViews);
            } else {
                if (leftImageIndex == SoapBoxConstants.STARTING_NOTIFICATION_INDEX) {
                    showNextAndViewAllBottomLayout(remoteViews);
                } else if (leftImageIndex == carouselNotificationBean.carouselCards.size() - 1 ||
                        leftImageIndex == carouselNotificationBean.carouselCards.size()) {
                    showOnlyPreviousBottomLayout(remoteViews);
                } else {
                    showNextAndPreviousBottomLayout(remoteViews);
                }

                setOnClickIntentOnViewAllButton(leftImageIndex, remoteViews);
            }

            setOnClickIntentOnBottomLayout(SoapBoxConstants.CAROUSEL_NOTIFICATION_BACK_ACTION, R.id.previous_layout, leftImageIndex,
                    rightImageIndex, carouselNotificationBean.notificationId, remoteViews);
            setOnClickIntentOnBottomLayout(SoapBoxConstants.CAROUSEL_NOTIFICATION_FRONT_ACTION, R.id.next_layout, leftImageIndex,
                    rightImageIndex, carouselNotificationBean.notificationId, remoteViews);
        } else {
            remoteViews.setViewVisibility(R.id.notification_bottom_layout, View.GONE);
        }
    }

    private void setOnClickIntentOnLeftAndRightImages(int leftImageIndex, int rightImageIndex,
                                                      RemoteViews remoteViews, CarouselImageNotificationData carouselNotificationBean) {

        if (carouselNotificationBean.returningDataList != null) {
            if (leftImageIndex < carouselNotificationBean.returningDataList.size()) {
                setOnClickIntentOnParticularImage(R.id.left_image, leftImageIndex, carouselNotificationBean.returningDataList.get(leftImageIndex),
                        remoteViews);
            } else {
                setOnClickIntentOnParticularImage(R.id.left_image, leftImageIndex, new HashMap<String, String>(),
                        remoteViews);
            }

            if (rightImageIndex < carouselNotificationBean.returningDataList.size()) {
                setOnClickIntentOnParticularImage(R.id.right_image, rightImageIndex, carouselNotificationBean.returningDataList.get(rightImageIndex),
                        remoteViews);
            } else {
                setOnClickIntentOnParticularImage(R.id.right_image, rightImageIndex, new HashMap<String, String>(),
                        remoteViews);
            }
        } else {
            setOnClickIntentOnParticularImage(R.id.left_image, leftImageIndex, new HashMap<String, String>(),
                    remoteViews);
            setOnClickIntentOnParticularImage(R.id.right_image, rightImageIndex, new HashMap<String, String>(),
                    remoteViews);
        }
    }

    private void setOnClickIntentOnViewAllButton(int leftImageIndex, RemoteViews remoteViews) {
        Intent intent = new Intent(UserBroadcastActionKeys.NOTIFICATION_VIEW_ALL_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_CAROUSEL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, leftImageIndex, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.view_all_layout, pendingIntent);
    }

    private void showOnlyPreviousBottomLayout(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.view_all_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.next_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.previous_layout, View.VISIBLE);
    }

    private void showNextAndPreviousBottomLayout(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.view_all_layout, View.GONE);
        remoteViews.setViewVisibility(R.id.next_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.previous_layout, View.VISIBLE);
    }

    private void showNextAndViewAllBottomLayout(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.view_all_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.next_layout, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.previous_layout, View.GONE);
    }

    private void setOnClickIntentOnLastPageOfNotification(int resourceId, int notificationId, RemoteViews remoteViews) {
        Intent intent = new Intent(UserBroadcastActionKeys.NOTIFICATION_LAST_PAGE_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_CAROUSEL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(resourceId, pendingIntent);
    }

    private void setOnClickIntentOnBottomLayout(String action, int viewResourceId, int leftIndex, int rightIndex, int notificationId,
                                                RemoteViews remoteViews) {
        Intent intent = new Intent(action);
        intent.putExtra(SoapBoxConstants.LEFT_IMAGE_INDEX, leftIndex);
        intent.putExtra(SoapBoxConstants.RIGHT_IMAGE_INDEX, rightIndex);
        intent.putExtra(SoapBoxConstants.NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewResourceId, pendingIntent);
    }

    private void setOnClickIntentOnParticularImage(int viewResourceId, int index, HashMap<String, String> returningObject, RemoteViews remoteViews) {
        Intent intent = new Intent(UserBroadcastActionKeys.NOTIFICATION_IMAGE_CLICK_BROADCAST_ACTION);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_INDEX, index);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_RETURNING_OBJECT, returningObject);
        intent.putExtra(UserBroadcastActionKeys.NOTIFICATION_CLICK_TYPE, UserBroadcastActionKeys.NOTIFICATION_TYPE_CAROUSEL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, index, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewResourceId, pendingIntent);
    }

}
