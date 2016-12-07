package com.naukri.soapbox.normal;

import android.content.Context;

import com.naukri.soapbox.Interface.Notifier;
import com.naukri.soapbox.bean.NormalNotificationData;
import com.naukri.soapbox.util.NotificationSender;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.SoapBoxFactory;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

/**
 * Created by vishnu.anand on 9/14/2016.
 */
public class NormalNotifier implements Notifier {

    private Context mContext;
    private android.app.Notification mNotification;
    private NotificationSender mNotificationSender;
    private NormalNotificationData mNormalNotificationData;

    public NormalNotifier(Context context, NormalNotificationData normalNotificationData) {

        mContext = context;
        mNormalNotificationData = normalNotificationData;

        setNotificationId();
    }

    @Override
    public boolean validate() {

        if (mNormalNotificationData.notificationSmallIcon == 0) {
            Utilities.throwRunTimeException(SoapBoxConstants.NOTIFICATION_ICON_MISSING_MSG);
        }
        return true;
    }

    @Override
    public void build() {

        mNotificationSender = SoapBoxFactory.getNotificationSender(SoapBoxConstants.NORMAL_NOTIFICATION_TYPE);
        mNotification = mNotificationSender.build(mContext, mNormalNotificationData);
    }

    @Override
    public void send() {
        if (validate()) {
            build();
            if (mNotificationSender != null) {
                mNotificationSender.send(mContext, mNormalNotificationData, mNotification);
            } else {
                SoapBoxLogger.error("Error == ", SoapBoxConstants.NOTIFICATION_SENDER_CLASS_TYPE_ERROR_MSG);
            }
        }
    }

    private void setNotificationId() {
        mNormalNotificationData.notificationId = Utilities.getNotificationId(this, mNormalNotificationData.notificationId);
    }

}
