package com.naukri.soapbox.slider;

import android.content.Context;

import com.naukri.soapbox.Interface.Notifier;
import com.naukri.soapbox.bean.SliderNotificationData;
import com.naukri.soapbox.util.NotificationSender;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.SoapBoxFactory;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

/**
 * Created by vishnu.anand on 8/1/2016.
 */
public class SliderNotifier implements Notifier {

    private Context mContext;
    private android.app.Notification mNotification;
    private NotificationSender mNotificationSender;
    private SliderNotificationData mSliderNotificationData;

    public SliderNotifier(Context context, SliderNotificationData sliderNotificationBean) {

        mContext = context;
        mSliderNotificationData = sliderNotificationBean;

        setNotificationId();
    }

    @Override
    public boolean validate() {

        if (mSliderNotificationData == null) {
            Utilities.throwRunTimeException(SoapBoxConstants.SLIDER_NOTIFICATION_BEAN_NULL_MSG);
        } else if (mSliderNotificationData.notificationSmallIcon == 0) {
            Utilities.throwRunTimeException(SoapBoxConstants.NOTIFICATION_ICON_MISSING_MSG);
        } else if (mSliderNotificationData.sliderCards == null || mSliderNotificationData.sliderCards.size() == 0) {
            Utilities.throwRunTimeException(SoapBoxConstants.SLIDER_NOTIFICATION_DATA_BEAN_NULL_MSG);
        }

        return true;
    }

    @Override
    public void build() {

        mNotificationSender = SoapBoxFactory.getNotificationSender(SoapBoxConstants.CUSTOM_NOTIFICATION_TYPE);
        mNotification = mNotificationSender.build(mContext, mSliderNotificationData);

        mNotification.bigContentView = SliderRemoteViewNotification.getInstance(mContext).getJobDetailRemoteView(
                SoapBoxConstants.STARTING_NOTIFICATION_INDEX, mSliderNotificationData, mNotification);

    }

    @Override
    public void send() {
        if (validate()) {
            build();
            if (mNotificationSender != null) {
                mNotificationSender.send(mContext, mSliderNotificationData, mNotification);
            } else {
                SoapBoxLogger.error("Error == ", SoapBoxConstants.NOTIFICATION_SENDER_CLASS_TYPE_ERROR_MSG);
            }
        }
    }

    private void setNotificationId() {
        mSliderNotificationData.notificationId = Utilities.getNotificationId(this, mSliderNotificationData.notificationId);
    }

}
