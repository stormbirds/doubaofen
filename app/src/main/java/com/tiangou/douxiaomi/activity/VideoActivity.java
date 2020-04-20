package com.tiangou.douxiaomi.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.dialog.PromptThemeDialog;
import com.tiangou.douxiaomi.utils.Mp4Util;
import java.io.File;
import java.io.IOException;

public class VideoActivity extends BaseActivity implements View.OnClickListener {
    String exportPath = "/抖小秘/视频/";
    String filePath;
    TextView tvAudio;
    TextView tvFile;
    TextView tvMd5Change;
    TextView tvVideo;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video);
        this.tvFile = (TextView) findViewById(R.id.tv_file);
        this.tvMd5Change = (TextView) findViewById(R.id.tv_md5_change);
        this.tvAudio = (TextView) findViewById(R.id.tv_audio);
        this.tvVideo = (TextView) findViewById(R.id.tv_video);
        this.tvFile.setOnClickListener(this);
        this.tvMd5Change.setOnClickListener(this);
        this.tvAudio.setOnClickListener(this);
        this.tvVideo.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_audio:
                if (TextUtils.isEmpty(this.tvFile.getText())) {
                    Toast.makeText(this, "请先选择视频", 0).show();
                    return;
                } else {
                    getAudio();
                    return;
                }
            case R.id.tv_file:
                chooseVideo();
                return;
            case R.id.tv_md5_change:
                if (TextUtils.isEmpty(this.tvFile.getText())) {
                    Toast.makeText(this, "请先选择视频", 0).show();
                    return;
                } else {
                    changeMd5();
                    return;
                }
            case R.id.tv_video:
                if (TextUtils.isEmpty(this.tvFile.getText())) {
                    Toast.makeText(this, "请先选择视频", 0).show();
                    return;
                } else {
                    getVideo();
                    return;
                }
            default:
                return;
        }
    }

    private void getVideo() {
        Mp4Util.splitMp4(new File(this.tvFile.getText().toString()).getAbsolutePath(), getLocalFile(2).getAbsolutePath());
        Toast.makeText(this, "提取成功", 0).show();
        new PromptThemeDialog(this, "提取成功，路径为" + this.exportPath, new PromptThemeDialog.PromptClickSureListener() {
            public void onClose() {
            }

            public void onSure() {
            }
        }).show();
    }

    private void getAudio() {
        Mp4Util.splitAac(new File(this.tvFile.getText().toString()).getAbsolutePath(), getLocalFile(1).getAbsolutePath());
        Toast.makeText(this, "提取成功", 0).show();
        new PromptThemeDialog(this, "提取成功，路径为" + this.exportPath, new PromptThemeDialog.PromptClickSureListener() {
            public void onClose() {
            }

            public void onSure() {
            }
        }).show();
    }

    private void changeMd5() {
        Mp4Util.modifyFileMD5(new File(this.tvFile.getText().toString()), getLocalFile(0));
        Toast.makeText(this, "修改成功", 0).show();
        new PromptThemeDialog(this, "修改成功，路径为" + this.exportPath, new PromptThemeDialog.PromptClickSureListener() {
            public void onClose() {
            }

            public void onSure() {
            }
        }).show();
    }

    private void chooseVideo() {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/mp4");
        startActivityForResult(intent, 1);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1) {
            Uri data = intent.getData();
            String[] strArr = {"_data"};
            Cursor query = getContentResolver().query(data, strArr, (String) null, (String[]) null, (String) null);
            this.filePath = "";
            if (query != null) {
                query.moveToFirst();
                this.filePath = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
            }
            Log.e("qyh", "file==" + this.filePath);
            if (TextUtils.isEmpty(this.filePath)) {
                this.filePath = data.getPath();
            }
            this.tvFile.setText(this.filePath);
        }
    }

    public File getLocalFile(int i) {
        String str;
        try {
            str = isExistDir(this.exportPath);
        } catch (IOException e) {
            e.printStackTrace();
            str = null;
        }
        String[] split = this.tvFile.getText().toString().split("\\/");
        String str2 = split[split.length - 1].split("\\.")[0];
        if (i == 0) {
            str2 = "[MD5]" + split[split.length - 1];
        }
        if (i == 1) {
            str2 = split[split.length - 1].split("\\.")[0] + ".mp3";
        }
        if (i == 2) {
            str2 = split[split.length - 1];
        }
        return new File(str, str2);
    }

    public String isExistDir(String str) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (!file.mkdirs()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }
}
