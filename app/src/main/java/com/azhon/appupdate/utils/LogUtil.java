package com.azhon.appupdate.utils;

import android.util.Log;
import com.azhon.appupdate.manager.DownloadManager;

public final class LogUtil {
    private static boolean b = DownloadManager.getInstance().getConfiguration().isEnableLog();

    public static void e(String str, String str2) {
        if (b) {
            Log.e(str, str2);
        }
    }

    public static void e(String str, int i) {
        if (b) {
            Log.e(str, String.valueOf(i));
        }
    }

    public static void e(String str, float f) {
        if (b) {
            Log.e(str, String.valueOf(f));
        }
    }

    public static void e(String str, Long l) {
        if (b) {
            Log.e(str, String.valueOf(l));
        }
    }

    public static void e(String str, double d) {
        if (b) {
            Log.e(str, String.valueOf(d));
        }
    }

    public static void e(String str, boolean z) {
        if (b) {
            Log.e(str, String.valueOf(z));
        }
    }
}
