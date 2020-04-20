package com.tiangou.douxiaomi.permission;

import android.app.Activity;

public class PermissionHelper {
    private Activity mActivity;
    private PermissionInterface mPermissionInterface;

    public PermissionHelper(Activity activity, PermissionInterface permissionInterface) {
        this.mActivity = activity;
        this.mPermissionInterface = permissionInterface;
    }

    public void requestPermissions() {
        String[] deniedPermissions = PermissionUtil.getDeniedPermissions(this.mActivity, this.mPermissionInterface.getPermissions());
        if (deniedPermissions == null || deniedPermissions.length <= 0) {
            this.mPermissionInterface.requestPermissionsSuccess();
        } else {
            PermissionUtil.requestPermissions(this.mActivity, deniedPermissions, this.mPermissionInterface.getPermissionsRequestCode());
        }
    }

    public boolean requestPermissionsResult(int i, String[] strArr, int[] iArr) {
        boolean z = false;
        if (i != this.mPermissionInterface.getPermissionsRequestCode()) {
            return false;
        }
        int length = iArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                z = true;
                break;
            } else if (iArr[i2] == -1) {
                break;
            } else {
                i2++;
            }
        }
        if (z) {
            this.mPermissionInterface.requestPermissionsSuccess();
        } else {
            this.mPermissionInterface.requestPermissionsFail();
        }
        return true;
    }
}
