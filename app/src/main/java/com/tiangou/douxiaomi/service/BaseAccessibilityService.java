package com.tiangou.douxiaomi.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BaseAccessibilityService extends AccessibilityService {
    private AccessibilityManager mAccessibilityManager;
    private Context mContext;

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    }

    public void onInterrupt() {
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
    }

    public boolean checkAccessibilityEnabled(String str) {
        for (AccessibilityServiceInfo id : this.mAccessibilityManager.getEnabledAccessibilityServiceList(16)) {
            if (id.getId().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void goAccess() {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    public boolean performViewClick(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        while (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.isClickable()) {
                return accessibilityNodeInfo.performAction(16);
            }
            accessibilityNodeInfo = accessibilityNodeInfo.getParent();
        }
        return false;
    }

    public boolean performViewLongClick(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        while (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.isLongClickable()) {
                return accessibilityNodeInfo.performAction(32);
            }
            accessibilityNodeInfo = accessibilityNodeInfo.getParent();
        }
        return false;
    }

    public boolean performBackClick() {
        return performGlobalAction(1);
    }

    public void performScrollBackward() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(8192);
    }

    public boolean performScrollForward(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        return accessibilityNodeInfo.performAction(4096);
    }

    public AccessibilityNodeInfo findViewByText(String str) {
        return findViewByText(str, (Boolean) null);
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTextClass(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        ArrayList arrayList = new ArrayList();
        if (accessibilityNodeInfo == null || TextUtils.isEmpty(str2)) {
            return arrayList;
        }
        for (AccessibilityNodeInfo next : accessibilityNodeInfo.findAccessibilityNodeInfosByText(str)) {
            if (!(next == null || next.getClassName() == null || !next.getClassName().equals(str2))) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public AccessibilityNodeInfo findViewByText(String str, Boolean bool) {
        return findViewByText(getRootInActiveWindow(), str, bool);
    }

    public AccessibilityNodeInfo findViewByText(AccessibilityNodeInfo accessibilityNodeInfo, String str, Boolean bool) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText;
        if (!(accessibilityNodeInfo == null || (findAccessibilityNodeInfosByText = accessibilityNodeInfo.findAccessibilityNodeInfosByText(str)) == null || findAccessibilityNodeInfosByText.isEmpty())) {
            if (bool == null) {
                for (AccessibilityNodeInfo next : findAccessibilityNodeInfosByText) {
                    if (next != null) {
                        return next;
                    }
                }
            } else {
                for (AccessibilityNodeInfo next2 : findAccessibilityNodeInfosByText) {
                    if (next2 != null && next2.isClickable() == bool.booleanValue()) {
                        return next2;
                    }
                }
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findViewByID(String str) {
        return findViewByID(getRootInActiveWindow(), str);
    }

    public AccessibilityNodeInfo findViewByID(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId;
        if (!(accessibilityNodeInfo == null || (findAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str)) == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
            for (AccessibilityNodeInfo next : findAccessibilityNodeInfosByViewId) {
                if (next != null) {
                    return next;
                }
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findViewByID(AccessibilityNodeInfo accessibilityNodeInfo, String str, String str2) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId;
        if (!(accessibilityNodeInfo == null || (findAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str)) == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
            for (AccessibilityNodeInfo next : findAccessibilityNodeInfosByViewId) {
                if (next != null && next.getClassName().equals(str2)) {
                    return next;
                }
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findNodeInfosByClassName(String str) {
        AccessibilityNodeInfo rootInActiveWindow;
        if (TextUtils.isEmpty(str) || (rootInActiveWindow = getRootInActiveWindow()) == null) {
            return null;
        }
        for (int i = 0; i < rootInActiveWindow.getChildCount(); i++) {
            AccessibilityNodeInfo child = rootInActiveWindow.getChild(i);
            if (child != null && str.equals(child.getClassName())) {
                return child;
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findNodeByRect(List<AccessibilityNodeInfo> list, int i, int i2) {
        for (int i3 = 0; i3 < list.size(); i3++) {
            AccessibilityNodeInfo accessibilityNodeInfo = list.get(i3);
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            if (rect.contains(i, i2)) {
                return accessibilityNodeInfo;
            }
        }
        return null;
    }

    public boolean clickTextViewByText(String str) {
        return clickTextViewByText(getRootInActiveWindow(), str);
    }

    public boolean clickTextViewByText(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText;
        if (!(accessibilityNodeInfo == null || (findAccessibilityNodeInfosByText = accessibilityNodeInfo.findAccessibilityNodeInfosByText(str)) == null || findAccessibilityNodeInfosByText.isEmpty())) {
            for (AccessibilityNodeInfo next : findAccessibilityNodeInfosByText) {
                if (next != null) {
                    return performViewClick(next);
                }
            }
        }
        return false;
    }

    public boolean clickTextViewByID(String str) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId;
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (!(rootInActiveWindow == null || (findAccessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId(str)) == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
            for (AccessibilityNodeInfo next : findAccessibilityNodeInfosByViewId) {
                if (next != null) {
                    return performViewClick(next);
                }
            }
        }
        return false;
    }

    public boolean inputText(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        if (str == null || "null".equals(str)) {
            str = "";
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, str);
            return accessibilityNodeInfo.performAction(2097152, bundle);
        } else if (Build.VERSION.SDK_INT < 18) {
            return false;
        } else {
            ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", str));
            if (!accessibilityNodeInfo.performAction(1) || !accessibilityNodeInfo.performAction(32768)) {
                return false;
            }
            return true;
        }
    }

    public <T> Observable<T> bind$(Observable<T> observable) {
        return observable.delay(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
