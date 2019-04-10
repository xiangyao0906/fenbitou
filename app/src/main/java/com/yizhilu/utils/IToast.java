package com.yizhilu.utils;

import android.content.Context;
import android.widget.Toast;

import com.yizhilu.base.DemoApplication;

/**
 * @author ming
 * @date 2016/12/28 10:01
 * Explain:
 */

public class IToast {

    private static Toast toast = null;

    public static void show(String message) {
        if (toast == null) {
            toast = Toast.makeText(DemoApplication.getInstance().getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void show(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }


}
