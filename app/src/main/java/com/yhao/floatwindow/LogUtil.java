package com.yhao.floatwindow;

import android.util.Log;

class LogUtil {
    private static final String TAG = "FloatWindow";

    LogUtil() {
    }

    static void e(String str) {
        Log.e(TAG, str);
    }

    static void d(String str) {
        Log.d(TAG, str);
    }
}
