package com.tiangou.douxiaomi.service;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.tiangou.douxiaomi.App;

public class AccessService extends BaseAccessibilityService {
    private static AccessService mInstance;

    public void onInterrupt() {
    }

    public static AccessService get() {
        if (mInstance == null) {
            mInstance = new AccessService();
        }
        return mInstance;
    }

    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        if (eventType == 32 || eventType == 2048) {
            if (App.getInstance().getService() == null) {
                App.getInstance().setService(this);
            }
        } else if (eventType == 4096) {
            Log.e("qyh", "event==" + accessibilityEvent.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void onServiceConnected() {
        super.onServiceConnected();
        Log.e("qyh", "douyin2==========服务开启");
    }
}
