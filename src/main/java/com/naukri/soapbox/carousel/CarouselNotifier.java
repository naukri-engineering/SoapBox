package com.naukri.soapbox.carousel;

import android.content.Context;

import com.naukri.soapbox.Interface.Notifier;
import com.naukri.soapbox.bean.CarouselImageNotificationData;
import com.naukri.soapbox.util.NotificationSender;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.SoapBoxFactory;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

/**
 * Created by vishnu.anand on 8/1/2016.
 */
public class CarouselNotifier implements Notifier {

    private Context mContext;
    private android.app.Notification mNotification;
    private NotificationSender mNotificationSender;
    private CarouselImageNotificationData mCarouselNotificationDataBean;

    public CarouselNotifier(Context context, CarouselImageNotificationData carouselNotificationBean) {

        mContext = context;
        mCarouselNotificationDataBean = carouselNotificationBean;

        setNotificationId();
    }

    @Override
    public boolean validate() {

        if (mCarouselNotificationDataBean == null) {
            Utilities.throwRunTimeException(SoapBoxConstants.CAROUSEL_NOTIFICATION_BEAN_NULL_MSG);
        } else if (mCarouselNotificationDataBean.notificationSmallIcon == 0) {
            Utilities.throwRunTimeException(SoapBoxConstants.NOTIFICATION_ICON_MISSING_MSG);
        } else if (mCarouselNotificationDataBean.carouselCards == null || mCarouselNotificationDataBean.carouselCards.size() == 0) {
            Utilities.throwRunTimeException(SoapBoxConstants.CAROUSEL_NOTIFICATION_DATA_BEAN_NULL_MSG);
        }

        return true;
    }

    @Override
    public void build() {

        mNotificationSender = SoapBoxFactory.getNotificationSender(SoapBoxConstants.CUSTOM_NOTIFICATION_TYPE);
        mNotification = mNotificationSender.build(mContext, mCarouselNotificationDataBean);

        mNotification.bigContentView = CarouselRemoteViewNotification.getInstance(mContext).getImageCarouselRemoteView(
                SoapBoxConstants.STARTING_NOTIFICATION_INDEX, SoapBoxConstants.STARTING_NOTIFICATION_INDEX + 1,
                mCarouselNotificationDataBean, mNotification);
    }

    @Override
    public void send() {
        if (validate()) {
            build();
            if (mNotificationSender != null) {
                mNotificationSender.send(mContext, mCarouselNotificationDataBean, mNotification);
            } else {
                SoapBoxLogger.error("Error == ", SoapBoxConstants.NOTIFICATION_SENDER_CLASS_TYPE_ERROR_MSG);
            }
        }
    }

    private void setNotificationId() {
        mCarouselNotificationDataBean.notificationId = Utilities.getNotificationId(this, mCarouselNotificationDataBean.notificationId);
    }


}
