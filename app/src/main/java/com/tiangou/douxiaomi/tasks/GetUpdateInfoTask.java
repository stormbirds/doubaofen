package com.tiangou.douxiaomi.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import com.azhon.appupdate.manager.DownloadManager;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.jni.Jni;

public class GetUpdateInfoTask extends AsyncTask implements DialogInterface.OnClickListener {
    Activity activity;
    int currentVersionCode;
    String loaddownAddres;
    String updateInfo;

    public void onClick(DialogInterface dialogInterface, int i) {
        DownloadManager apkUrl = DownloadManager.getInstance(this.activity).setApkName("抖小秘.apk").setApkUrl(this.loaddownAddres);
        apkUrl.setDownloadPath(Environment.getExternalStorageDirectory() + "/DXMHelper").setSmallIcon(R.mipmap.ic_launcher).setApkDescription(this.updateInfo).download();
    }

    public GetUpdateInfoTask(Activity activity2) {
        try {
            this.activity = activity2;
            this.currentVersionCode = activity2.getPackageManager().getPackageInfo(activity2.getPackageName(), 0).versionCode;
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        if (((Boolean) obj).booleanValue()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle("发现新版本");
            builder.setMessage(this.updateInfo);
            builder.setPositiveButton("升级", this);
            builder.create().show();
        }
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object[] objArr) {
        try {
            Thread.sleep(1000);
            if (Integer.parseInt(Jni.getNewVersion()) <= this.currentVersionCode) {
                return false;
            }
            this.updateInfo = Jni.getUpdateInfo();
            this.loaddownAddres = Jni.getLoaddownAddres();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
