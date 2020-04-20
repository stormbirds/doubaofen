package com.tiangou.douxiaomi;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static SharedPreferences sp;

    public static void saveBoolean(Context context, String str, boolean z) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.douyin_config, 0);
        }
        sp.edit().putBoolean(str, z).commit();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.douyin_config, 0);
        }
        return sp.getBoolean(str, z);
    }

    public static void saveCustomString(Context context, String str, String str2, String str3) {
        if (sp == null) {
            sp = context.getSharedPreferences(str, 0);
        }
        sp.edit().putString(str2, str3).commit();
    }

    public static String getCustomString(Context context, String str, String str2) {
        if (sp == null) {
            sp = context.getSharedPreferences(str, 0);
        }
        return sp.getString(str2, (String) null);
    }

    public static void saveString(Context context, String str, String str2) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.douyin_config, 0);
        }
        sp.edit().putString(str, str2).commit();
    }

    public static String getString(Context context, String str, String str2) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.douyin_config, 0);
        }
        return sp.getString(str, str2);
    }

    public static int getInt(Context context, String str, int i) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.douyin_config, 0);
        }
        return sp.getInt(str, i);
    }

    public static void saveInt(Context context, String str, int i) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.douyin_config, 0);
        }
        sp.edit().putInt(str, i).commit();
    }

    public static String getAuthCode(Context context) {
        return context.getSharedPreferences(Constants.douyin_config, 0).getString(Constants.sp_authCode, "");
    }

    public static void saveAuthCode(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constants.douyin_config, 0).edit();
        edit.putString(Constants.sp_authCode, str);
        edit.commit();
    }
}
