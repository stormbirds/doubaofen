package com.azhon.appupdate.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionUtil {
    public static boolean checkStoragePermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;
    }

    public static void requestPermission(Activity activity, int i) {
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, i);
    }

    public static boolean checkOreoInstallPermission(Context context) {
        return context.getPackageManager().canRequestPackageInstalls();
    }
}
