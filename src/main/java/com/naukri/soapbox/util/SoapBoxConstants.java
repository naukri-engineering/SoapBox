package com.naukri.soapbox.util;

/**
 * Created by vishnu.anand on 8/1/2016.
 */
public class SoapBoxConstants {

    /*This constant is used as actions of the broadcast when the  custom notification move back or front*/
    public static final String SLIDER_NOTIFICATION_BACK_ACTION = "back_slider_broadcast";
    public static final String SLIDER_NOTIFICATION_FRONT_ACTION = "front_slider_broadcast";
    public static final String CAROUSEL_NOTIFICATION_BACK_ACTION = "back_carousel_broadcast";
    public static final String CAROUSEL_NOTIFICATION_FRONT_ACTION = "front_carousel_broadcast";

    /*this Constant is used in put extra keys in notification intent for back and front moving or remove notification
      from notification bar.*/
    public static final String INDEX = "index";
    public static final String LEFT_IMAGE_INDEX = "left_index";
    public static final String RIGHT_IMAGE_INDEX = "right_index";
    public static final String NOTIFICATION_ID = "notification_id";

    public static final int STARTING_NOTIFICATION_INDEX = 0;
    public static final String EMPTY_TEXT = "";

    /*Error Messgaes Constants*/
    public static final String NOTIFICATION_ICON_MISSING_MSG = "Notification Icon is missing, It is mandatory for showing notification";
    public static final String SLIDER_NOTIFICATION_BEAN_NULL_MSG = "Slider Notification bean is null, Failed To show Notification";
    public static final String SLIDER_NOTIFICATION_DATA_BEAN_NULL_MSG = "Slider Notification Data bean is null, Failed To show Notification";
    public static final String CAROUSEL_NOTIFICATION_BEAN_NULL_MSG = "Carousel Notification bean is null, Failed To show Notification";
    public static final String CAROUSEL_NOTIFICATION_DATA_BEAN_NULL_MSG = "Slider Notification image bean is null, Failed To show Notification";
    public static final String ANDROID_DEVICE_VERSION_LESS_THAN_JELLYBEAN_MSG = "Android device version less than required Version for big custom notification";
    public static final String SLIDER_NOTIFICATION_DATA_LIST_EMPTY_MSG = "Slider Notification data list is Empty";
    public static final String NOTIFICATION_TYPE_ERROR_MSG = "notification type mismatch error";
    public static final String NOTIFICATION_SENDER_CLASS_TYPE_ERROR_MSG = "notification Sender Class type Not Found Error";

    public static final int NORMAL_NOTIFICATION_TYPE = 1;
    public static final int CUSTOM_NOTIFICATION_TYPE = 2;

    // Intent type for normal notifications, define which type of intent your are using for particular notification.
    public static final int INTENT_TYPE_ACTIVITY = 1;
    public static final int INTENT_TYPE_BROADCAST = 2;
    public static final int INTENT_TYPE_SERVICE = 3;

    public static final String TAG = "SoapBox";

}
