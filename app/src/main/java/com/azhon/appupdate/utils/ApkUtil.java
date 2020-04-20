package com.azhon.appupdate.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import java.io.File;

public final class ApkUtil {
    public static void installApk(Context context, File file) {
        Uri uri;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.addCategory("android.intent.category.DEFAULT");
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), file);
            intent.addFlags(1);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }
}
