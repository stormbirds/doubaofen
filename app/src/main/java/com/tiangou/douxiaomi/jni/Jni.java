package com.tiangou.douxiaomi.jni;

import android.content.Context;

public class Jni {
    public static native Object analysis(String str);

    public static native String getAdvertisement();

    public static native int getAuthStatus();

    public static native String getLoaddownAddres();

    public static native String getNewVersion();

    public static native String getSbObj(int i);

    public static native String getUpdateInfo();

    public static native int init(Context context);

    public static native String login(Context context, String str);

    static {
        System.loadLibrary("xx");
    }
}
