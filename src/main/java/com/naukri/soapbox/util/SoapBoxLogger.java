package com.naukri.soapbox.util;

import android.util.Log;

/**
 * Created by vishnu.anand on 9/25/2016.
 */
public class SoapBoxLogger {

    private static boolean isLoggingEnabled = false;

    public static void setLoggingEnabled(boolean enabled) {
        isLoggingEnabled = enabled;
    }

    public static void error(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
    }

    public static void debug(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
    }
}
