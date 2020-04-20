package com.yhao.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Miui {
    /* access modifiers changed from: private */
    public static PermissionListener mPermissionListener = null;
    /* access modifiers changed from: private */
    public static List<PermissionListener> mPermissionListenerList = null;
    private static final String miui = "ro.miui.ui.version.name";
    private static final String miui5 = "V5";
    private static final String miui6 = "V6";
    private static final String miui7 = "V7";
    private static final String miui8 = "V8";
    private static final String miui9 = "V9";

    Miui() {
    }

    static boolean rom() {
        LogUtil.d(" Miui  : " + getProp());
        return Build.MANUFACTURER.equals("Xiaomi");
    }

    private static String getProp() {
        return Rom.getProp(miui);
    }

    static void req(Context context, PermissionListener permissionListener) {
        if (PermissionUtil.hasPermission(context)) {
            permissionListener.onSuccess();
            return;
        }
        if (mPermissionListenerList == null) {
            mPermissionListenerList = new ArrayList();
            mPermissionListener = new PermissionListener() {
                public void onSuccess() {
                    for (PermissionListener onSuccess : Miui.mPermissionListenerList) {
                        onSuccess.onSuccess();
                    }
                    Miui.mPermissionListenerList.clear();
                }

                public void onFail() {
                    for (PermissionListener onFail : Miui.mPermissionListenerList) {
                        onFail.onFail();
                    }
                    Miui.mPermissionListenerList.clear();
                }
            };
            req_(context);
        }
        mPermissionListenerList.add(permissionListener);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void req_(final android.content.Context r6) {
        /*
            java.lang.String r0 = getProp()
            int r1 = r0.hashCode()
            r2 = 4
            r3 = 3
            r4 = 2
            r5 = 1
            switch(r1) {
                case 2719: goto L_0x0038;
                case 2720: goto L_0x002e;
                case 2721: goto L_0x0024;
                case 2722: goto L_0x001a;
                case 2723: goto L_0x0010;
                default: goto L_0x000f;
            }
        L_0x000f:
            goto L_0x0042
        L_0x0010:
            java.lang.String r1 = "V9"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 4
            goto L_0x0043
        L_0x001a:
            java.lang.String r1 = "V8"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 3
            goto L_0x0043
        L_0x0024:
            java.lang.String r1 = "V7"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 2
            goto L_0x0043
        L_0x002e:
            java.lang.String r1 = "V6"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 1
            goto L_0x0043
        L_0x0038:
            java.lang.String r1 = "V5"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 0
            goto L_0x0043
        L_0x0042:
            r0 = -1
        L_0x0043:
            if (r0 == 0) goto L_0x0056
            if (r0 == r5) goto L_0x0052
            if (r0 == r4) goto L_0x0052
            if (r0 == r3) goto L_0x004e
            if (r0 == r2) goto L_0x004e
            goto L_0x0059
        L_0x004e:
            reqForMiui89(r6)
            goto L_0x0059
        L_0x0052:
            reqForMiui67(r6)
            goto L_0x0059
        L_0x0056:
            reqForMiui5(r6)
        L_0x0059:
            com.yhao.floatwindow.Miui$2 r0 = new com.yhao.floatwindow.Miui$2
            r0.<init>(r6)
            com.yhao.floatwindow.FloatLifecycle.setResumedListener(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yhao.floatwindow.Miui.req_(android.content.Context):void");
    }

    private static void reqForMiui5(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", packageName, (String) null));
        intent.setFlags(268435456);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            LogUtil.e("intent is not available!");
        }
    }

    private static void reqForMiui67(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(268435456);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            LogUtil.e("intent is not available!");
        }
    }

    private static void reqForMiui89(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(268435456);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
            return;
        }
        Intent intent2 = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent2.setPackage("com.miui.securitycenter");
        intent2.putExtra("extra_pkgname", context.getPackageName());
        intent2.setFlags(268435456);
        if (Rom.isIntentAvailable(intent2, context)) {
            context.startActivity(intent2);
        } else {
            LogUtil.e("intent is not available!");
        }
    }

    private static void addViewToWindow(WindowManager windowManager, View view, WindowManager.LayoutParams layoutParams) {
        setMiUI_International(true);
        windowManager.addView(view, layoutParams);
        setMiUI_International(false);
    }

    private static void setMiUI_International(boolean z) {
        try {
            Field declaredField = Class.forName("miui.os.Build").getDeclaredField("IS_INTERNATIONAL_BUILD");
            declaredField.setAccessible(true);
            declaredField.setBoolean((Object) null, z);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
