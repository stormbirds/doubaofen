package com.yhao.floatwindow;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

class FloatLifecycle extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final long delay = 300;
    private static int num;
    private static ResumedListener sResumedListener;
    private Class[] activities;
    /* access modifiers changed from: private */
    public boolean appBackground;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public LifecycleListener mLifecycleListener;
    /* access modifiers changed from: private */
    public int resumeCount;
    private boolean showFlag;
    private int startCount;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    FloatLifecycle(Context context, boolean z, Class[] clsArr, LifecycleListener lifecycleListener) {
        this.showFlag = z;
        this.activities = clsArr;
        num++;
        this.mLifecycleListener = lifecycleListener;
        ((Application) context).registerActivityLifecycleCallbacks(this);
        context.registerReceiver(this, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public static void setResumedListener(ResumedListener resumedListener) {
        sResumedListener = resumedListener;
    }

    private boolean needShow(Activity activity) {
        Class[] clsArr = this.activities;
        if (clsArr == null) {
            return true;
        }
        for (Class isInstance : clsArr) {
            if (isInstance.isInstance(activity)) {
                return this.showFlag;
            }
        }
        return !this.showFlag;
    }

    public void onActivityResumed(Activity activity) {
        ResumedListener resumedListener = sResumedListener;
        if (resumedListener != null) {
            num--;
            if (num == 0) {
                resumedListener.onResumed();
                sResumedListener = null;
            }
        }
        this.resumeCount++;
        if (needShow(activity)) {
            this.mLifecycleListener.onShow();
        } else {
            this.mLifecycleListener.onHide();
        }
        if (this.appBackground) {
            this.appBackground = false;
        }
    }

    public void onActivityPaused(Activity activity) {
        this.resumeCount--;
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (FloatLifecycle.this.resumeCount == 0) {
                    boolean unused = FloatLifecycle.this.appBackground = true;
                    FloatLifecycle.this.mLifecycleListener.onBackToDesktop();
                }
            }
        }, delay);
    }

    public void onActivityStarted(Activity activity) {
        this.startCount++;
    }

    public void onActivityStopped(Activity activity) {
        this.startCount--;
        if (this.startCount == 0) {
            this.mLifecycleListener.onBackToDesktop();
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS") && SYSTEM_DIALOG_REASON_HOME_KEY.equals(intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY))) {
            this.mLifecycleListener.onBackToDesktop();
        }
    }
}
