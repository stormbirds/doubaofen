package com.azhon.appupdate.utils;

import android.content.Context;

public final class ScreenUtil {
    public static int getWith(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
