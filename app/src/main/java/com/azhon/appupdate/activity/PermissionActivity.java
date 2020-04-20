package com.azhon.appupdate.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.azhon.appupdate.service.DownloadService;
import com.azhon.appupdate.utils.Constant;
import com.azhon.appupdate.utils.PermissionUtil;

public class PermissionActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PermissionUtil.requestPermission(this, Constant.PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1997) {
            if (iArr[0] == 0) {
                download();
            } else {
                Toast.makeText(this, "请允许使用[存储空间]权限!", 1).show();
                if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    getAppDetailPage(this);
                }
            }
            finish();
        }
    }

    public void download() {
        startService(new Intent(this, DownloadService.class));
    }

    public void getAppDetailPage(Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), (String) null));
        context.startActivity(intent);
    }
}
