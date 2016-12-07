package com.naukri.soapbox.util;

import android.content.Context;

import com.naukri.soapbox.bean.CarouselImageNotificationData;
import com.naukri.soapbox.bean.NormalNotificationData;
import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.bean.SliderNotificationData;
import com.naukri.soapbox.carousel.CarouselNotifier;
import com.naukri.soapbox.normal.NormalNotifier;
import com.naukri.soapbox.sender.CustomNotificationSender;
import com.naukri.soapbox.sender.NormalNotificationSender;
import com.naukri.soapbox.slider.SliderNotifier;

/**
 * Created by vishnu.anand on 9/14/2016.
 */
public class SoapBoxFactory {

    public static com.naukri.soapbox.Interface.Notifier getNotifier(Context context, NotificationData notificationData) {
        if (notificationData instanceof NormalNotificationData) {
            return new NormalNotifier(context, (NormalNotificationData) notificationData);
        } else if (notificationData instanceof SliderNotificationData) {
            return new SliderNotifier(context, (SliderNotificationData) notificationData);
        } else if (notificationData instanceof CarouselImageNotificationData) {
            return new CarouselNotifier(context, (CarouselImageNotificationData) notificationData);
        } else {
            return null;
        }
    }

    public static NotificationSender getNotificationSender(int senderType) {

        if (senderType == SoapBoxConstants.CUSTOM_NOTIFICATION_TYPE) {
            return new CustomNotificationSender();
        } else if (senderType == SoapBoxConstants.NORMAL_NOTIFICATION_TYPE) {
            return new NormalNotificationSender();
        } else {
            return null;
        }

    }
}


