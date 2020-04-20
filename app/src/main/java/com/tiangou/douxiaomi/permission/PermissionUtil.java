package com.tiangou.douxiaomi.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import java.util.ArrayList;

public class PermissionUtil {
    public static boolean hasPermission(Context context, String str) {
        return Build.VERSION.SDK_INT < 23 || context.checkSelfPermission(str) == 0;
    }

    public static void requestPermissions(Activity activity, String[] strArr, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.requestPermissions(strArr, i);
        }
    }

    public static String[] getDeniedPermissions(Context context, String[] strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (context.checkSelfPermission(str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.size() > 0) {
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        return null;
    }
}
