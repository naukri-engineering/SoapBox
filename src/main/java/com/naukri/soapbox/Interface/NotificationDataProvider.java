package com.naukri.soapbox.Interface;

import android.content.Intent;

/**
 * Created by vishnu.anand on 10/17/2016.
 */

public interface NotificationDataProvider {

    int getIntentType();

    Intent getContentIntent();
}
