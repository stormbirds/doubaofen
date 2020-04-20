package com.tiangou.douxiaomi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DensityUtil {
    private static int[] deviceWidthHeight = new int[2];

    public static int[] getDeviceInfo(Context context) {
        int[] iArr = deviceWidthHeight;
        if (iArr[0] == 0 && iArr[1] == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            deviceWidthHeight[0] = displayMetrics.widthPixels;
            deviceWidthHeight[1] = displayMetrics.heightPixels;
        }
        return deviceWidthHeight;
    }

    public static int getWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public static int getHeight(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Point getScreenSize(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (Build.VERSION.SDK_INT < 13) {
            return new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        }
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point;
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }
}
