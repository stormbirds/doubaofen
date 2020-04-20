package com.azhon.appupdate.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public final class SharePreUtil {
    private static final String SHARE_NAME = "app_update";

    public static void putString(Context context, String str, String str2) {
        context.getSharedPreferences(SHARE_NAME, 0).edit().putString(str, str2).commit();
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(SHARE_NAME, 0).getString(str, str2);
    }

    public static void putBoolean(Context context, String str, boolean z) {
        context.getSharedPreferences(SHARE_NAME, 0).edit().putBoolean(str, z).commit();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return context.getSharedPreferences(SHARE_NAME, 0).getBoolean(str, z);
    }

    public static void putInt(Context context, String str, int i) {
        context.getSharedPreferences(SHARE_NAME, 0).edit().putInt(str, i).commit();
    }

    public static int getInt(Context context, String str, int i) {
        return context.getSharedPreferences(SHARE_NAME, 0).getInt(str, i);
    }

    public static void deleShare(Context context, String str) {
        context.getSharedPreferences(SHARE_NAME, 0).edit().remove(str).commit();
    }

    public static void deleShareAll(Context context) {
        context.getSharedPreferences(SHARE_NAME, 0).edit().clear().commit();
    }

    public static String lookSharePre(Context context) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/data/data/" + context.getPackageName() + "/shared_prefs", "app_update.xml"))));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return sb.toString();
                }
                sb.append(readLine);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到当前配置文件！";
        }
    }
}
