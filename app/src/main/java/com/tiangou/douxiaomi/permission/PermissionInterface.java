package com.tiangou.douxiaomi.permission;

public interface PermissionInterface {
    String[] getPermissions();

    int getPermissionsRequestCode();

    void requestPermissionsFail();

    void requestPermissionsSuccess();
}
