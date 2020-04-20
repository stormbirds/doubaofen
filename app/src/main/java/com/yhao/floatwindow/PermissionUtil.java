package com.yhao.floatwindow;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

class PermissionUtil {
    PermissionUtil() {
    }

    static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(context);
        }
        return hasPermissionBelowMarshmallow(context);
    }

    static boolean hasPermissionOnActivityResult(Context context) {
        if (Build.VERSION.SDK_INT == 26) {
            return hasPermissionForO(context);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(context);
        }
        return hasPermissionBelowMarshmallow(context);
    }

    static boolean hasPermissionBelowMarshmallow(Context context) {
        try {
            if (((Integer) AppOpsManager.class.getMethod("checkOp", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke((AppOpsManager) context.getSystemService("appops"), new Object[]{24, Integer.valueOf(Binder.getCallingUid()), context.getApplicationContext().getPackageName()})).intValue() == 0) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean hasPermissionForO(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            if (windowManager == null) {
                return false;
            }
            View view = new View(context);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(0, 0, Build.VERSION.SDK_INT >= 26 ? 2038 : 2003, 24, -2);
            view.setLayoutParams(layoutParams);
            windowManager.addView(view, layoutParams);
            windowManager.removeView(view);
            return true;
        } catch (Exception e) {
            LogUtil.e("hasPermissionForO e:" + e.toString());
            return false;
        }
    }
}
