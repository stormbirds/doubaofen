package com.tiangou.douxiaomi.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.bean.AnalysisBean;
import com.tiangou.douxiaomi.bean.BaseModel;
import com.tiangou.douxiaomi.dialog.ProgressDialog;
import com.tiangou.douxiaomi.http.DownloadUtil;
import com.tiangou.douxiaomi.http.HttpRxListener;
import com.tiangou.douxiaomi.http.RtRxOkHttp;
import com.tiangou.douxiaomi.jni.Jni;
import java.io.File;

public class WaterMarkActivity extends Activity implements View.OnClickListener {
    AnalysisBean bean;
    EditText etUrl;
    ImageView ivCover;
    TextView tvAnalysis;
    TextView tvDownloadImg;
    TextView tvDownloadVideo;
    TextView tvTitle;

    private void setOthers() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setListener();
        setOthers();
    }

    private void initView() {
        setContentView(R.layout.activity_watermark);
        this.tvAnalysis = (TextView) findViewById(R.id.tv_analysis);
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.etUrl = (EditText) findViewById(R.id.et_url);
        this.tvDownloadImg = (TextView) findViewById(R.id.tv_download_img);
        this.tvDownloadVideo = (TextView) findViewById(R.id.tv_download_video);
        this.ivCover = (ImageView) findViewById(R.id.iv_cover);
    }

    private void setListener() {
        this.tvAnalysis.setOnClickListener(this);
        this.tvDownloadVideo.setOnClickListener(this);
        this.tvDownloadImg.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_analysis:
                startAnalysis();
                return;
            case R.id.tv_download_img:
                sendImg(true, this.bean.img);
                return;
            case R.id.tv_download_video:
                sendImg(false, this.bean.videourl);
                return;
            default:
                return;
        }
    }

    private void startAnalysis() {
        if (Jni.getAuthStatus() != 0) {
            Toast.makeText(this, "没有授权", 1).show();
            return;
        }
        String str = null;
        this.bean = null;
        findViewById(R.id.ll_download).setVisibility(8);
        if (TextUtils.isEmpty(this.etUrl.getText().toString())) {
            Toast.makeText(this, "请输入链接", 0).show();
            return;
        }
        String obj = this.etUrl.getText().toString();
        try {
            str = "http" + obj.split("http")[1].split("复制此链接")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.etUrl.getText().toString();
        }
        Log.e("qyh", "url==" + str);
        RtRxOkHttp.getInstance().createRtRx(RtRxOkHttp.getApiService().analysis(str), new HttpRxListener() {
            public void httpResponse(Object obj, boolean z, int i) {
                if (z) {
                    BaseModel baseModel = (BaseModel) obj;
                    if (baseModel.code == 101) {
                        WaterMarkActivity.this.bean = (AnalysisBean) baseModel.data;
                        WaterMarkActivity.this.displayView();
                    } else {
                        Toast.makeText(WaterMarkActivity.this, "解析失败", 0).show();
                    }
                } else {
                    Toast.makeText(WaterMarkActivity.this, "解析失败", 0).show();
                }
                WaterMarkActivity.this.etUrl.setText((CharSequence) null);
            }
        }, 3);
    }

    /* access modifiers changed from: private */
    public void displayView() {
        if (this.bean != null) {
            Toast.makeText(this, "解析成功", 0).show();
            findViewById(R.id.ll_download).setVisibility(0);
            Glide.with((Activity) this).load(this.bean.img).into(this.ivCover);
            TextView textView = this.tvTitle;
            textView.setText("" + this.bean.title);
        }
    }

    public void sendImg(Boolean bool, String str) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        String str2 = File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;
        StringBuilder sb = new StringBuilder();
        sb.append(this.bean.title);
        sb.append(bool.booleanValue() ? ".jpg" : ".mp4");
        String sb2 = sb.toString();
        progressDialog.show();
        DownloadUtil.get().download(str, str2, sb2, "-1", new DownloadUtil.OnDownloadListener() {
            public void onDownloadFailed(String str) {
            }

            public void onDownloadSuccess(String str, String str2) {
                progressDialog.setProgress(100);
                WaterMarkActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(str2))));
            }

            public void onDownloading(String str, int i) {
                progressDialog.setProgress(i);
            }
        });
    }
}
