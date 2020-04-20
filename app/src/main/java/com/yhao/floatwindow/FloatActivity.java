package com.yhao.floatwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class FloatActivity extends Activity {
    private static PermissionListener mPermissionListener;
    /* access modifiers changed from: private */
    public static List<PermissionListener> mPermissionListenerList;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 23) {
            requestAlertWindowPermission();
        }
    }

    private void requestAlertWindowPermission() {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 756232212);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 756232212) {
            if (PermissionUtil.hasPermissionOnActivityResult(this)) {
                mPermissionListener.onSuccess();
            } else {
                mPermissionListener.onFail();
            }
        }
        finish();
    }

    static synchronized void request(Context context, PermissionListener permissionListener) {
        Class<FloatActivity> cls = FloatActivity.class;
        synchronized (cls) {
            if (PermissionUtil.hasPermission(context)) {
                permissionListener.onSuccess();
                return;
            }
            if (mPermissionListenerList == null) {
                mPermissionListenerList = new ArrayList();
                mPermissionListener = new PermissionListener() {
                    public void onSuccess() {
                        for (PermissionListener onSuccess : FloatActivity.mPermissionListenerList) {
                            onSuccess.onSuccess();
                        }
                        FloatActivity.mPermissionListenerList.clear();
                    }

                    public void onFail() {
                        for (PermissionListener onFail : FloatActivity.mPermissionListenerList) {
                            onFail.onFail();
                        }
                        FloatActivity.mPermissionListenerList.clear();
                    }
                };
                Intent intent = new Intent(context, cls);
                intent.setFlags(268435456);
                context.startActivity(intent);
            }
            mPermissionListenerList.add(permissionListener);
        }
    }
}
