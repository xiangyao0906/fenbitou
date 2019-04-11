package com.fenbitou.utils;


import android.util.Log;



public class ILog {

    private final static String TAG = "log268";
    private static boolean isOn = true;

    public static void i(String massage) {
        if (isOn) {
            Log.i(TAG, massage);
        }
    }

}
