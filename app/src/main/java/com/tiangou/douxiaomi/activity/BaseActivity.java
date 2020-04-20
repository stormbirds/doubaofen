package com.tiangou.douxiaomi.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import com.tiangou.douxiaomi.App;

public abstract class BaseActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        App.getInstance().hideFloatWindows();
    }

    /* access modifiers changed from: protected */
    public void showText(String str) {
        App.getInstance().showToast(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public boolean moveTaskToFront() {
        ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
        return isForeground(getPackageName());
    }

    private boolean isForeground(String str) {
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = getRunningAppProcessInfo(str);
        if (runningAppProcessInfo == null || 100 != runningAppProcessInfo.importance) {
            return false;
        }
        return true;
    }

    private ActivityManager.RunningAppProcessInfo getRunningAppProcessInfo(String str) {
        for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (next.processName.equals(str)) {
                return next;
            }
        }
        return null;
    }
}
