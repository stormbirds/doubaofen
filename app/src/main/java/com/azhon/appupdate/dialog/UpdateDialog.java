package com.azhon.appupdate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.azhon.appupdate.R;
import com.azhon.appupdate.activity.PermissionActivity;
import com.azhon.appupdate.manager.DownloadManager;
import com.azhon.appupdate.service.DownloadService;
import com.azhon.appupdate.utils.PermissionUtil;
import com.azhon.appupdate.utils.ScreenUtil;

public class UpdateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private DownloadManager manager;

    public UpdateDialog(Context context2) {
        super(context2, R.style.UpdateDialog);
        init(context2);
    }

    private void init(Context context2) {
        this.context = context2;
        this.manager = DownloadManager.getInstance();
        View inflate = LayoutInflater.from(context2).inflate(R.layout.dialog_update, (ViewGroup) null);
        setContentView(inflate);
        setWindowSize(context2);
        initView(inflate);
    }

    private void initView(View view) {
        view.findViewById(R.id.ib_close).setOnClickListener(this);
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_size);
        TextView textView3 = (TextView) view.findViewById(R.id.tv_description);
        ((Button) view.findViewById(R.id.btn_update)).setOnClickListener(this);
        if (TextUtils.isEmpty(this.manager.getApkVersionName())) {
            textView.setText(String.format("哇，有新版%s可以下载啦！", new Object[]{this.manager.getApkVersionName()}));
        } else {
            textView.setText(String.format("哇，有新版v%s可以下载啦！", new Object[]{this.manager.getApkVersionName()}));
        }
        if (!TextUtils.isEmpty(this.manager.getApkSize())) {
            textView2.setText(String.format("新版本大小：%sM", new Object[]{this.manager.getApkSize()}));
            textView2.setVisibility(0);
        }
        textView3.setText(this.manager.getApkDescription());
    }

    private void setWindowSize(Context context2) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (((float) ScreenUtil.getWith(context2)) * 0.7f);
        attributes.height = -2;
        attributes.gravity = 17;
        window.setAttributes(attributes);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ib_close) {
            dismiss();
        } else if (id == R.id.btn_update) {
            dismiss();
            if (!PermissionUtil.checkStoragePermission(this.context)) {
                Context context2 = this.context;
                context2.startActivity(new Intent(context2, PermissionActivity.class));
                return;
            }
            Context context3 = this.context;
            context3.startService(new Intent(context3, DownloadService.class));
        }
    }
}
