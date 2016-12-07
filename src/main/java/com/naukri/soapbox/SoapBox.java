package com.naukri.soapbox;

import android.app.AlarmManager;
import android.content.Context;

import com.naukri.soapbox.Interface.Notifier;
import com.naukri.soapbox.bean.NotificationData;
import com.naukri.soapbox.database.NotificationDatabase;
import com.naukri.soapbox.service.NotificationDeleteService;
import com.naukri.soapbox.util.SoapBoxConstants;
import com.naukri.soapbox.util.SoapBoxFactory;
import com.naukri.soapbox.util.SoapBoxLogger;
import com.naukri.soapbox.util.Utilities;

/**
 * Created by vishnu.anand on 8/1/2016.
 */
public final class SoapBox {
    private static SoapBox ourInstance;
    private Context mContext;

    private SoapBox(Context context, boolean isLoggingEnabled) {
        mContext = context;
        NotificationDatabase.getInstance(context);
        SoapBoxLogger.setLoggingEnabled(isLoggingEnabled);
        Utilities.setJobAlarm(context, NotificationDeleteService.class, AlarmManager.INTERVAL_DAY);
    }

    public static SoapBox getInstance(Context context, boolean isLoggingEnabled) {
        if (ourInstance == null) {
            ourInstance = new SoapBox(context, isLoggingEnabled);
        }
        return ourInstance;
    }

    public void sendNotification(NotificationData notificationData) {

        NotificationData newNotificationData = Utilities.getNotificationDataAsPerBuildVersion(notificationData);
        Notifier notifier = SoapBoxFactory.getNotifier(mContext, newNotificationData);
        if (notifier != null) {
            notifier.send();
        } else {
            Utilities.throwRunTimeException(SoapBoxConstants.NOTIFICATION_TYPE_ERROR_MSG);
        }
    }


}
