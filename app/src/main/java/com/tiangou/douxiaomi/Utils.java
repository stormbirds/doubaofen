package com.tiangou.douxiaomi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {
    public static void openDouYin(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(queryIntentActivities, new ResolveInfo.DisplayNameComparator(packageManager));
        for (ResolveInfo next : queryIntentActivities) {
            String str = next.activityInfo.packageName;
            String str2 = next.activityInfo.name;
            if (str.contains("com.ss.android.ugc.aweme")) {
                ComponentName componentName = new ComponentName(str, str2);
                Intent intent2 = new Intent();
                intent2.setComponent(componentName);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            }
        }
    }

    public static void openFlyChat(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(queryIntentActivities, new ResolveInfo.DisplayNameComparator(packageManager));
        for (ResolveInfo next : queryIntentActivities) {
            String str = next.activityInfo.packageName;
            String str2 = next.activityInfo.name;
            if (str.contains("com.feiliao.flipchat.android")) {
                ComponentName componentName = new ComponentName(str, str2);
                Intent intent2 = new Intent();
                intent2.setComponent(componentName);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            }
        }
    }

    public static int getNum(int i, int i2) {
        return i2 > i ? new Random().nextInt(i2 - i) + i : i;
    }

    public static int dp2Px(Context context, float f) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static boolean isSettingOpen(Class cls, Context context) {
        try {
            if (Settings.Secure.getInt(context.getContentResolver(), "accessibility_enabled", 0) != 1) {
                return false;
            }
            String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
            if (!TextUtils.isEmpty(string)) {
                TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
                simpleStringSplitter.setString(string);
                while (simpleStringSplitter.hasNext()) {
                    String next = simpleStringSplitter.next();
                    if (next.equalsIgnoreCase(context.getPackageName() + "/" + cls.getName())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Throwable th) {
            Log.e("qyh", "isSettingOpen: " + th.getMessage());
        }
        return false;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0019, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        android.util.Log.e("qyh", "jumpToSetting: " + r2.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x000b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void jumpToSetting(android.content.Context r2) {
        /*
            java.lang.String r0 = "android.settings.ACCESSIBILITY_SETTINGS"
            android.content.Intent r1 = new android.content.Intent     // Catch:{ all -> 0x000b }
            r1.<init>(r0)     // Catch:{ all -> 0x000b }
            r2.startActivity(r1)     // Catch:{ all -> 0x000b }
            goto L_0x0034
        L_0x000b:
            android.content.Intent r1 = new android.content.Intent     // Catch:{ all -> 0x0019 }
            r1.<init>(r0)     // Catch:{ all -> 0x0019 }
            r0 = 268435456(0x10000000, float:2.5243549E-29)
            r1.addFlags(r0)     // Catch:{ all -> 0x0019 }
            r2.startActivity(r1)     // Catch:{ all -> 0x0019 }
            goto L_0x0034
        L_0x0019:
            r2 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "jumpToSetting: "
            r0.append(r1)
            java.lang.String r2 = r2.getMessage()
            r0.append(r2)
            java.lang.String r2 = r0.toString()
            java.lang.String r0 = "qyh"
            android.util.Log.e(r0, r2)
        L_0x0034:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.Utils.jumpToSetting(android.content.Context):void");
    }

    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isEmptyArray(List list) {
        return list == null || list.size() == 0;
    }

    public static int getTime() {
        return Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000));
    }

    public Boolean checkSusPermiss(Context context) {
        return false;
    }

    public static void LogV(String str) {
        Log.v("xx", str);
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
