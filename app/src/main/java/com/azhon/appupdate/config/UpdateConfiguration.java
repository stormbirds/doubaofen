package com.azhon.appupdate.config;

import android.app.NotificationChannel;
import android.support.v4.view.PointerIconCompat;
import com.azhon.appupdate.base.BaseHttpDownloadManager;
import com.azhon.appupdate.listener.OnDownloadListener;

public class UpdateConfiguration {
    private boolean breakpointDownload = true;
    private boolean enableLog = true;
    private BaseHttpDownloadManager httpManager;
    private boolean jumpInstallPage = true;
    private NotificationChannel notificationChannel;
    private int notifyId = PointerIconCompat.TYPE_COPY;
    private OnDownloadListener onDownloadListener;
    private boolean showNotification = true;

    public int getNotifyId() {
        return this.notifyId;
    }

    public UpdateConfiguration setNotifyId(int i) {
        this.notifyId = i;
        return this;
    }

    public UpdateConfiguration setHttpManager(BaseHttpDownloadManager baseHttpDownloadManager) {
        this.httpManager = baseHttpDownloadManager;
        return this;
    }

    public BaseHttpDownloadManager getHttpManager() {
        return this.httpManager;
    }

    public boolean isBreakpointDownload() {
        return this.breakpointDownload;
    }

    public UpdateConfiguration setBreakpointDownload(boolean z) {
        this.breakpointDownload = z;
        return this;
    }

    public boolean isEnableLog() {
        return this.enableLog;
    }

    public UpdateConfiguration setEnableLog(boolean z) {
        this.enableLog = z;
        return this;
    }

    public OnDownloadListener getOnDownloadListener() {
        return this.onDownloadListener;
    }

    public UpdateConfiguration setOnDownloadListener(OnDownloadListener onDownloadListener2) {
        this.onDownloadListener = onDownloadListener2;
        return this;
    }

    public boolean isJumpInstallPage() {
        return this.jumpInstallPage;
    }

    public UpdateConfiguration setJumpInstallPage(boolean z) {
        this.jumpInstallPage = z;
        return this;
    }

    public NotificationChannel getNotificationChannel() {
        return this.notificationChannel;
    }

    public boolean isShowNotification() {
        return this.showNotification;
    }

    public UpdateConfiguration setShowNotification(boolean z) {
        this.showNotification = z;
        return this;
    }

    public UpdateConfiguration setNotificationChannel(NotificationChannel notificationChannel2) {
        this.notificationChannel = notificationChannel2;
        return this;
    }
}
