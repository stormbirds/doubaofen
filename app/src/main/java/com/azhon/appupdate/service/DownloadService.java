package com.azhon.appupdate.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import com.azhon.appupdate.base.BaseHttpDownloadManager;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.azhon.appupdate.manager.HttpDownloadManager;
import com.azhon.appupdate.utils.ApkUtil;
import com.azhon.appupdate.utils.FileUtil;
import com.azhon.appupdate.utils.LogUtil;
import com.azhon.appupdate.utils.NotificationUtil;
import java.io.File;

public final class DownloadService extends Service implements OnDownloadListener {
    private static final String TAG = "DownloadService";
    private String apkName;
    private String apkUrl;
    private String downloadPath;
    private boolean downloading;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Toast.makeText(DownloadService.this, "正在后台下载新版本...", 0).show();
        }
    };
    private boolean jumpInstallPage;
    private OnDownloadListener listener;
    private boolean showNotification;
    private int smallIcon;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            return 1;
        }
        init();
        return super.onStartCommand(intent, i, i2);
    }

    private void init() {
        this.apkUrl = DownloadManager.getInstance().getApkUrl();
        this.apkName = DownloadManager.getInstance().getApkName();
        this.downloadPath = DownloadManager.getInstance().getDownloadPath();
        this.smallIcon = DownloadManager.getInstance().getSmallIcon();
        FileUtil.createDirDirectory(this.downloadPath);
        this.listener = DownloadManager.getInstance().getConfiguration().getOnDownloadListener();
        this.showNotification = DownloadManager.getInstance().getConfiguration().isShowNotification();
        this.jumpInstallPage = DownloadManager.getInstance().getConfiguration().isJumpInstallPage();
        download();
    }

    private synchronized void download() {
        if (this.downloading) {
            LogUtil.e(TAG, "download: 当前正在下载，请务重复下载！");
            return;
        }
        BaseHttpDownloadManager httpManager = DownloadManager.getInstance().getConfiguration().getHttpManager();
        if (httpManager != null) {
            httpManager.download(this.apkUrl, this.apkName, this);
        } else {
            new HttpDownloadManager(this, this.downloadPath).download(this.apkUrl, this.apkName, this);
        }
        this.downloading = true;
    }

    public void start() {
        if (this.showNotification) {
            this.handler.sendEmptyMessage(0);
            NotificationUtil.showNotification(this, this.smallIcon, "开始下载", "可稍后查看下载进度");
        }
        OnDownloadListener onDownloadListener = this.listener;
        if (onDownloadListener != null) {
            onDownloadListener.start();
        }
    }

    public void downloading(int i, int i2) {
        if (this.showNotification) {
            NotificationUtil.showProgressNotification(this, this.smallIcon, "正在下载新版本", "", i, i2);
        }
        OnDownloadListener onDownloadListener = this.listener;
        if (onDownloadListener != null) {
            onDownloadListener.downloading(i, i2);
        }
    }

    public void done(File file) {
        this.downloading = false;
        if (this.showNotification) {
            NotificationUtil.showDoneNotification(this, this.smallIcon, "下载完成", "点击进行安装", file);
        }
        OnDownloadListener onDownloadListener = this.listener;
        if (onDownloadListener != null) {
            onDownloadListener.done(file);
        }
        if (this.jumpInstallPage) {
            ApkUtil.installApk(this, file);
        }
        releaseResources();
    }

    public void error(Exception exc) {
        LogUtil.e(TAG, "error: " + exc);
        this.downloading = false;
        if (this.showNotification) {
            NotificationUtil.showErrorNotification(this, this.smallIcon, "下载出错", "点击继续下载");
        }
        OnDownloadListener onDownloadListener = this.listener;
        if (onDownloadListener != null) {
            onDownloadListener.error(exc);
        }
    }

    private void releaseResources() {
        Handler handler2 = this.handler;
        if (handler2 != null) {
            handler2.removeCallbacksAndMessages((Object) null);
        }
        stopSelf();
        DownloadManager.getInstance().release();
    }
}
