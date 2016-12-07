package com.naukri.soapbox.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vishnu.anand on 8/2/2016.
 */
public class CarouselImageNotificationData extends NotificationData implements Serializable {

    public boolean isNotificationCyclic;  // it is used to tell whether notification list is cyclic or not.
    public String notificationLastPageText;  // it is used only when notification is not cyclic in nature. This string is used for last page text of notification.

    public ArrayList<String> carouselCards;  // this list if used for inflating data in notifications.
    public ArrayList<HashMap<String, String>> returningDataList;  // this list is used to return particular hash map after pressing each notification.
}
