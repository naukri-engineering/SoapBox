package com.naukri.soapbox.bean;

import com.naukri.soapbox.util.SoapBoxConstants;

import java.io.Serializable;

/**
 * Created by vishnu.anand on 9/2/2016.
 */
public class NotificationData implements Serializable {

    public int notificationSmallIcon;
    public int notificationLargeIcon;
    public int notificationId; // This field is used only when you want one notification in whole application otherwise don't set this value.

    public String notificationTitle = SoapBoxConstants.EMPTY_TEXT;
    public String notificationText = SoapBoxConstants.EMPTY_TEXT;
}
