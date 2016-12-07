package com.naukri.soapbox.broadcastrecievers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.naukri.soapbox.bean.CarouselImageNotificationData;
import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.carousel.CarouselRemoteViewNotification;
import com.naukri.soapbox.database.DatabaseHelper;
import com.naukri.soapbox.database.NotificationDatabase;
import com.naukri.soapbox.util.NotificationBuilder;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

import java.util.ArrayList;

/**
 * Created by vishnu.anand on 8/2/2016.
 */
public class CarouselNotificationBroadcast extends BroadcastReceiver {

    private int mCurrentLeftIndex, mCurrentRightIndex, mNextLeftIndex, mNextRightIndex, mNotificationId;
    private CarouselImageNotificationData mCarouselNotificationBean;
    private ArrayList<String> mNotificationImageList;
    private Notification mNotification;
    private NotificationBuilder mNotificationBuilder;
    private DatabaseHelper mDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {

        mDatabase = DatabaseHelper.getDatabaseHelper(context);
        mNotificationBuilder = NotificationBuilder.getInstance(context);
        getDataFromIntent(intent);

        mCarouselNotificationBean = getNotificationObjectFromDb(mNotificationId);
        if (mCarouselNotificationBean != null) {

            mNotificationImageList = mCarouselNotificationBean.carouselCards;
            if (mNotificationImageList != null) {

                fetchNewIndexFromOldIndex(intent);
                setUpdatedRemoteViewToNotificationObject(context);
                startCarouselNotification();
            } else {
                deleteCarouselNotification();
                SoapBoxLogger.error("Error == ", SoapBoxConstants.CAROUSEL_NOTIFICATION_DATA_BEAN_NULL_MSG);
            }
        } else {
            deleteCarouselNotification();
            SoapBoxLogger.error("Error == ", SoapBoxConstants.CAROUSEL_NOTIFICATION_BEAN_NULL_MSG);
        }
    }

    private void deleteCarouselNotification() {
        mDatabase.deleteNotification(mNotificationId);
        mNotificationBuilder.deleteNotification(mNotificationId);
    }

    private void startCarouselNotification() {
        mNotificationBuilder.startNotification(mCarouselNotificationBean.notificationId, mNotification);
    }

    private void setUpdatedRemoteViewToNotificationObject(Context context) {

        mNotification = mNotificationBuilder.getCustomNotificationObject(mCarouselNotificationBean, false);

        mNotification.bigContentView = CarouselRemoteViewNotification.getInstance(context).
                getImageCarouselRemoteView(mNextLeftIndex, mNextRightIndex, mCarouselNotificationBean, mNotification);
    }

    private CarouselImageNotificationData getNotificationObjectFromDb(int mNotificationId) {
        CarouselImageNotificationData carouselObject = null;
        Cursor cursor = mDatabase.getNotificationFromNotificationId(mNotificationId);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                int notificationObjectIndex = cursor.getColumnIndex(NotificationDatabase.NOTIFICATION_OBJECT);
                byte[] object = cursor.getBlob(notificationObjectIndex);

                NotificationData notificationObject = (NotificationData) Utilities.deserialize(object);
                if (notificationObject instanceof CarouselImageNotificationData) {
                    carouselObject = (CarouselImageNotificationData) notificationObject;
                }
            } catch (Exception e) {
                SoapBoxLogger.error("Notifications error msg", e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return carouselObject;
    }


    private void fetchNewIndexFromOldIndex(Intent intent) {
        if (intent.getAction().equalsIgnoreCase(SoapBoxConstants.CAROUSEL_NOTIFICATION_BACK_ACTION)) {

            if (mCarouselNotificationBean.isNotificationCyclic) {
                if (mCurrentLeftIndex > 1 && mCurrentRightIndex != 0) {
                    mNextLeftIndex = mCurrentLeftIndex - 2;
                    mNextRightIndex = mCurrentRightIndex - 2;
                } else if (mCurrentLeftIndex > 1 && mCurrentRightIndex == 0) {
                    mNextLeftIndex = mCurrentLeftIndex - 2;
                    mNextRightIndex = mNotificationImageList.size() - 2;
                } else if (mCurrentLeftIndex == 1) {
                    mNextLeftIndex = mNotificationImageList.size() - 1;
                    mNextRightIndex = 0;
                } else {
                    mNextLeftIndex = mNotificationImageList.size() - 2;
                    mNextRightIndex = mNotificationImageList.size() - 1;
                }
            } else {
                mNextLeftIndex = mCurrentLeftIndex - 2;
                mNextRightIndex = mCurrentRightIndex - 2;
            }
        } else if (intent.getAction().equalsIgnoreCase(SoapBoxConstants.CAROUSEL_NOTIFICATION_FRONT_ACTION)) {

            if (mCarouselNotificationBean.isNotificationCyclic) {
                if (((mNotificationImageList.size() - 1) - mCurrentRightIndex) >= 2 &&
                        mCurrentRightIndex != SoapBoxConstants.STARTING_NOTIFICATION_INDEX) {
                    mNextLeftIndex = mCurrentLeftIndex + 2;
                    mNextRightIndex = mCurrentRightIndex + 2;
                } else if (((mNotificationImageList.size() - 1) - mCurrentRightIndex) >= 2 &&
                        mCurrentRightIndex == SoapBoxConstants.STARTING_NOTIFICATION_INDEX) {
                    mNextLeftIndex = 1;
                    mNextRightIndex = mCurrentRightIndex + 2;
                } else if (((mNotificationImageList.size() - 1) - mCurrentRightIndex) == 1) {
                    mNextLeftIndex = mCurrentLeftIndex + 2;
                    mNextRightIndex = 0;
                } else {
                    mNextLeftIndex = 0;
                    mNextRightIndex = 1;
                }
            } else {
                mNextLeftIndex = mCurrentLeftIndex + 2;
                mNextRightIndex = mCurrentRightIndex + 2;
            }
        }
    }

    private void getDataFromIntent(Intent intent) {
        mCurrentLeftIndex = intent.getIntExtra(SoapBoxConstants.LEFT_IMAGE_INDEX,
                SoapBoxConstants.STARTING_NOTIFICATION_INDEX);
        mCurrentRightIndex = intent.getIntExtra(SoapBoxConstants.RIGHT_IMAGE_INDEX,
                SoapBoxConstants.STARTING_NOTIFICATION_INDEX + 1);
        mNotificationId = intent.getIntExtra(SoapBoxConstants.NOTIFICATION_ID, 0);
    }

}
