package com.naukri.soapbox.broadcastrecievers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.bean.SliderCard;
import com.naukri.soapbox.bean.SliderNotificationData;
import com.naukri.soapbox.database.DatabaseHelper;
import com.naukri.soapbox.database.NotificationDatabase;
import com.naukri.soapbox.slider.SliderRemoteViewNotification;
import com.naukri.soapbox.util.NotificationBuilder;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

import java.util.ArrayList;

/**
 * Created by vishnu.anand on 7/30/2016.
 */
public class SliderNotificationBroadcast extends BroadcastReceiver {

    private int mCurrentIndex, mNextIndex, mNotificationId;
    private SliderNotificationData mSliderNotificationData;
    private ArrayList<SliderCard> mNotificationDataList;
    private Notification mNotification;
    private NotificationBuilder mNotificationBuilder;
    private DatabaseHelper mDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {

        mDatabase = DatabaseHelper.getDatabaseHelper(context);
        mNotificationBuilder = NotificationBuilder.getInstance(context);
        getDataFromIntent(intent);

        mSliderNotificationData = getSliderNotificationDataObjectFromDb(mNotificationId);
        if (mSliderNotificationData != null) {

            mNotificationDataList = mSliderNotificationData.sliderCards;
            if (mNotificationDataList != null) {

                fetchNewIndexFromOldIndex(intent);
                setUpdatedRemoteViewToNotificationObject(context);
                startSliderNotification();
            } else {

                deleteSliderNotification();
                SoapBoxLogger.error("Error == ", SoapBoxConstants.SLIDER_NOTIFICATION_DATA_LIST_EMPTY_MSG);
            }
        } else {

            deleteSliderNotification();
            SoapBoxLogger.error("Error == ", SoapBoxConstants.SLIDER_NOTIFICATION_BEAN_NULL_MSG);
        }


    }

    private void deleteSliderNotification() {
        mDatabase.deleteNotification(mNotificationId);
        mNotificationBuilder.deleteNotification(mNotificationId);
    }

    private void startSliderNotification() {
        mNotificationBuilder.startNotification(mSliderNotificationData.notificationId, mNotification);
    }

    private void setUpdatedRemoteViewToNotificationObject(Context context) {
        mNotification = mNotificationBuilder.getCustomNotificationObject(mSliderNotificationData, false);

        mNotification.bigContentView = SliderRemoteViewNotification.getInstance(context).getJobDetailRemoteView(mNextIndex,
                mSliderNotificationData, mNotification);

    }

    private SliderNotificationData getSliderNotificationDataObjectFromDb(int mNotificationId) {

        SliderNotificationData dataBean = null;
        Cursor cursor = mDatabase.getNotificationFromNotificationId(mNotificationId);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                int notificationObjectIndex = cursor.getColumnIndex(NotificationDatabase.NOTIFICATION_OBJECT);
                byte[] object = cursor.getBlob(notificationObjectIndex);

                NotificationData deserializeObject = (NotificationData) Utilities.deserialize(object);
                if (deserializeObject instanceof SliderNotificationData) {
                    dataBean = (SliderNotificationData) deserializeObject;
                }

            } catch (Exception e) {
                SoapBoxLogger.error("Notifications error msg", e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return dataBean;
    }

    private void fetchNewIndexFromOldIndex(Intent intent) {
        if (intent.getAction().equalsIgnoreCase(SoapBoxConstants.SLIDER_NOTIFICATION_BACK_ACTION)) {

            if (mSliderNotificationData.isNotificationCyclic) {
                if (mCurrentIndex != 0) {
                    mNextIndex = mCurrentIndex - 1;
                } else {
                    mNextIndex = mNotificationDataList.size() - 1;
                }
            } else {
                mNextIndex = mCurrentIndex - 1;
            }

        } else if (intent.getAction().equalsIgnoreCase(SoapBoxConstants.SLIDER_NOTIFICATION_FRONT_ACTION)) {

            if (mSliderNotificationData.isNotificationCyclic) {
                if (mCurrentIndex != mNotificationDataList.size() - 1) {
                    mNextIndex = mCurrentIndex + 1;
                } else {
                    mNextIndex = 0;
                }
            } else {
                mNextIndex = mCurrentIndex + 1;
            }
        }
    }

    private void getDataFromIntent(Intent intent) {
        mCurrentIndex = intent.getIntExtra(SoapBoxConstants.INDEX, SoapBoxConstants.STARTING_NOTIFICATION_INDEX);
        mNotificationId = intent.getIntExtra(SoapBoxConstants.NOTIFICATION_ID, 0);
    }

}
