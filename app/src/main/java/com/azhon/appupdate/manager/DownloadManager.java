package com.azhon.appupdate.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;
import com.azhon.appupdate.activity.PermissionActivity;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.dialog.UpdateDialog;
import com.azhon.appupdate.service.DownloadService;
import com.azhon.appupdate.utils.ApkUtil;
import com.azhon.appupdate.utils.Constant;
import com.azhon.appupdate.utils.PermissionUtil;

public class DownloadManager {
    private static Context context;
    private static DownloadManager manager;
    private String apkDescription = "";
    private String apkName = "";
    private String apkSize = "";
    private String apkUrl = "";
    private int apkVersionCode = 1;
    private String apkVersionName = "";
    private UpdateConfiguration configuration;
    private String downloadPath;
    private int smallIcon = -1;

    public static DownloadManager getInstance(Context context2) {
        if (manager == null) {
            manager = new DownloadManager();
        }
        context = context2;
        return manager;
    }

    public static DownloadManager getInstance() {
        DownloadManager downloadManager = manager;
        if (downloadManager != null) {
            return downloadManager;
        }
        throw new RuntimeException("请先调用 getInstance(Context context) !");
    }

    public String getApkUrl() {
        return this.apkUrl;
    }

    public DownloadManager setApkUrl(String str) {
        this.apkUrl = str;
        return this;
    }

    public int getApkVersionCode() {
        return this.apkVersionCode;
    }

    public DownloadManager setApkVersionCode(int i) {
        this.apkVersionCode = i;
        return this;
    }

    public String getApkName() {
        return this.apkName;
    }

    public DownloadManager setApkName(String str) {
        this.apkName = str;
        return this;
    }

    public String getDownloadPath() {
        return this.downloadPath;
    }

    public DownloadManager setDownloadPath(String str) {
        this.downloadPath = str;
        return this;
    }

    public int getSmallIcon() {
        return this.smallIcon;
    }

    public DownloadManager setSmallIcon(int i) {
        this.smallIcon = i;
        return this;
    }

    public DownloadManager setConfiguration(UpdateConfiguration updateConfiguration) {
        this.configuration = updateConfiguration;
        return this;
    }

    public UpdateConfiguration getConfiguration() {
        return this.configuration;
    }

    public String getApkVersionName() {
        return this.apkVersionName;
    }

    public DownloadManager setApkVersionName(String str) {
        this.apkVersionName = str;
        return this;
    }

    public String getApkDescription() {
        return this.apkDescription;
    }

    public DownloadManager setApkDescription(String str) {
        this.apkDescription = str;
        return this;
    }

    public String getApkSize() {
        return this.apkSize;
    }

    public DownloadManager setApkSize(String str) {
        this.apkSize = str;
        return this;
    }

    public void download() {
        if (checkParams()) {
            if (!PermissionUtil.checkStoragePermission(context)) {
                Context context2 = context;
                context2.startActivity(new Intent(context2, PermissionActivity.class));
                return;
            }
            Context context3 = context;
            context3.startService(new Intent(context3, DownloadService.class));
        } else if (this.apkVersionCode > ApkUtil.getVersionCode(context)) {
            new UpdateDialog(context).show();
        } else {
            Toast.makeText(context, "当前已是最新版本!", 0).show();
        }
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty(this.apkUrl)) {
            throw new RuntimeException("apkUrl can not be empty!");
        } else if (TextUtils.isEmpty(this.apkName)) {
            throw new RuntimeException("apkName can not be empty!");
        } else if (!this.apkName.endsWith(Constant.APK_SUFFIX)) {
            throw new RuntimeException("apkName must endsWith .apk!");
        } else if (TextUtils.isEmpty(this.downloadPath)) {
            throw new RuntimeException("downloadPath can not be empty!");
        } else if (this.smallIcon != -1) {
            if (this.configuration == null) {
                this.configuration = new UpdateConfiguration();
            }
            if (this.apkVersionCode <= 1) {
                return true;
            }
            if (!TextUtils.isEmpty(this.apkDescription)) {
                return false;
            }
            throw new RuntimeException("apkDescription can not be empty!");
        } else {
            throw new RuntimeException("smallIcon can not be empty!");
        }
    }

    public void release() {
        manager = null;
    }
}
