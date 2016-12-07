package com.naukri.soapbox.util;

/**
 * Created by vishnu.anand on 8/12/2016.
 */
public class UserBroadcastActionKeys {

    // This is the action name of broadcast, application use this action for getting callback after custom notification click.
    public static final String NOTIFICATION_JOB_DETAIL_CLICK_BROADCAST_ACTION = "notification_job_detail_click_broadcast";
    public static final String NOTIFICATION_VIEW_ALL_CLICK_BROADCAST_ACTION = "notification_view_all_click_broadcast";
    public static final String NOTIFICATION_LAST_PAGE_CLICK_BROADCAST_ACTION = "notification_last_page_click_broadcast";
    public static final String NOTIFICATION_IMAGE_CLICK_BROADCAST_ACTION = "notification_image_click_broadcast";
    public static final String NORMAL_NOTIFICATION_CLICK_BROADCAST_ACTION = "normal_notification_click_broadcast";

    /*these constants is used by the application for getting the values in the resulting broadcast after custom notification clicked */
    public static final String NOTIFICATION_CLICK_INDEX = "index";
    public static final String NOTIFICATION_CLICK_TYPE = "notification_type";
    public static final String NOTIFICATION_TYPE_CAROUSEL = "carousel";
    public static final String NOTIFICATION_TYPE_SLIDER = "slider";
    public static final String NOTIFICATION_TYPE_NORMAL = "normal";
    public static final String NOTIFICATION_RETURNING_OBJECT = "returning_object";
}
