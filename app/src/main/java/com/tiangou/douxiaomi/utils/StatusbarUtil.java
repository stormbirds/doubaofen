package com.tiangou.douxiaomi.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tiangou.douxiaomi.R;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusbarUtil {
    private static int statusHeight;

    public interface onTabChangeListener {
        void onChange(int i);
    }

    public static String getHandSetInfo() {
        String str = "手机型号:" + Build.MODEL + "\n系统版本:" + Build.VERSION.RELEASE + "\n产品型号:" + Build.PRODUCT + "\n版本显示:" + Build.DISPLAY + "\n系统定制商:" + Build.BRAND + "\n设备参数:" + Build.DEVICE + "\n开发代号:" + Build.VERSION.CODENAME + "\nSDK版本号:" + Build.VERSION.SDK_INT + "\nOS号:" + Build.VERSION.INCREMENTAL + "\nCPU类型:" + Build.CPU_ABI + "\n硬件类型:" + Build.HARDWARE + "\n主机:" + Build.HOST + "\n生产ID:" + Build.ID + "\nROM制造商:" + Build.MANUFACTURER;
        Log.e("tt", str);
        return str;
    }

    private static boolean isMeizu() {
        return "Meizu".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private static boolean isHuaWei() {
        return "HUAWEI".equalsIgnoreCase(Build.MANUFACTURER);
    }

    private static boolean isXiaomi() {
        return "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static void setBgColorLight(Activity activity, int i, Boolean bool) {
        if (isXiaomi()) {
            setMiuiStatusBarDarkMode(activity, bool.booleanValue());
        } else if (isMeizu()) {
            setMeizuStatusBarDarkIcon(activity, bool.booleanValue());
        } else if (Build.VERSION.SDK_INT >= 23) {
            setBlackFontUpM(activity, bool.booleanValue());
        }
        setBgColor(activity, i);
    }

    public static int getBgColorIfLight(Activity activity, int i, int i2) {
        return (!isXiaomi() && !isMeizu() && Build.VERSION.SDK_INT < 23) ? i2 : i;
    }

    public static void setFontBlack(Activity activity, boolean z) {
        if (isXiaomi()) {
            setBgTransparent(activity, !z);
            MIUISetStatusBarLightMode(activity, z);
        } else if (isMeizu()) {
            StatusbarColorMeiZuUtils.setStatusBarDarkIcon(activity, z);
            setBgTransparent(activity, !z);
        } else if (Build.VERSION.SDK_INT >= 23) {
            setBlackFontUpM(activity, z);
            setBgTransparent(activity, !z);
        } else {
            setTranslucentStatus(activity, !z);
            setBgColor(activity, R.color.black);
        }
    }

    public static void setFontBlack(Activity activity, boolean z, boolean z2) {
        if (isXiaomi()) {
            setBgTransparent(activity, z2);
            MIUISetStatusBarLightMode(activity, z);
        } else if (isMeizu()) {
            StatusbarColorMeiZuUtils.setStatusBarDarkIcon(activity, z);
            setBgTransparent(activity, z2);
        } else if (Build.VERSION.SDK_INT >= 23) {
            setBlackFontUpM(activity, z);
            setBgTransparent(activity, z2);
        } else {
            setTranslucentStatus(activity, !z);
            setBgColor(activity, R.color.black);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int i = statusHeight;
        if (i > 0) {
            return i;
        }
        statusHeight = 50;
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            statusHeight = context.getResources().getDimensionPixelSize(identifier);
        }
        Log.e("h", "getStatusBarHeight:" + statusHeight);
        return statusHeight;
    }

    public static void initInTabs(Activity activity) {
        setBgTransparent(activity);
    }

    private static void setStatusBarViewHeight(Activity activity, View view) {
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = statusBarHeight;
        view.setLayoutParams(layoutParams);
        view.setVisibility(0);
    }

    public static void setBgColor(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 19) {
            setWindowStatusBarColor(activity, i);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(i);
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Field declaredField = Class.forName("com.android.internal.policy.DecorView").getDeclaredField("mSemiTransparentStatusBarColor");
                    declaredField.setAccessible(true);
                    declaredField.setInt(activity.getWindow().getDecorView(), 0);
                } catch (Exception unused) {
                }
            }
        }
    }

    public static void setWindowStatusBarColor(Activity activity, int i) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.addFlags(Integer.MIN_VALUE);
                try {
                    window.setStatusBarColor(activity.getResources().getColor(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void setBgTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(activity, true);
            setBgColor(activity, R.color.transparent);
        }
    }

    public static void setBgTransparent(Activity activity, boolean z) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(activity, z);
            if (z) {
                setBgColor(activity, R.color.transparent);
            } else {
                setBgColor(activity, R.color.white);
            }
        }
    }

    public static void setStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(activity, true);
        }
    }

    private static void setTranslucentStatus(Activity activity, boolean z) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (z) {
            attributes.flags |= 67108864;
        } else {
            attributes.flags &= -67108865;
        }
        window.setAttributes(attributes);
    }

    private static boolean setMiuiStatusBarDarkMode(Activity activity, boolean z) {
        Window window = activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(67108864);
        window.getDecorView().setSystemUiVisibility(8192);
        Class<?> cls = activity.getWindow().getClass();
        try {
            Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
            Method method = cls.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
            Window window2 = activity.getWindow();
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(z ? i : 0);
            objArr[1] = Integer.valueOf(i);
            method.invoke(window2, objArr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean z) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt((Object) null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                activity.getWindow().setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private static boolean setHuaWeiStatusBarDark(Activity activity, boolean z) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt((Object) null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                activity.getWindow().setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static void setBlackFontUpM(Activity activity, boolean z) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (z) {
            activity.getWindow().getDecorView().setSystemUiVisibility(8192);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(1024);
        }
    }

    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(67108864);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.getDecorView().setSystemUiVisibility(1280);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(67108864, 67108864);
        }
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean z) {
        Window window = activity.getWindow();
        if (window != null) {
            Class<?> cls = window.getClass();
            try {
                Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
                Method method = cls.getMethod("setExtraFlags", new Class[]{Integer.TYPE, Integer.TYPE});
                if (z) {
                    method.invoke(window, new Object[]{Integer.valueOf(i), Integer.valueOf(i)});
                } else {
                    method.invoke(window, new Object[]{0, Integer.valueOf(i)});
                }
                try {
                    if (Build.VERSION.SDK_INT < 23) {
                        return true;
                    }
                    if (z) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(8192);
                        return true;
                    }
                    activity.getWindow().getDecorView().setSystemUiVisibility(1024);
                    return true;
                } catch (Exception unused) {
                    return true;
                }
            } catch (Exception unused2) {
            }
        }
        return false;
    }

    public static class StatusColorBean {
        public int colorRes;
        public int colorResBackup;
        public boolean isLight;
        public boolean isThrough;
        public View statusBarView;

        public StatusColorBean(int i, boolean z, boolean z2, int i2, View view) {
            this.colorRes = i;
            this.isLight = z;
            this.isThrough = z2;
            this.colorResBackup = i2;
            this.statusBarView = view;
        }

        public StatusColorBean(int i, boolean z, int i2) {
            this.colorRes = i;
            this.isLight = z;
            this.isThrough = false;
            this.colorResBackup = i2;
        }
    }
}
