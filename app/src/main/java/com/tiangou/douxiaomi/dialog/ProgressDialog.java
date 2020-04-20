package com.tiangou.douxiaomi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tiangou.douxiaomi.R;

public class ProgressDialog extends Dialog implements View.OnClickListener {
    private TextView mInfo;
    private Button mSureBtn;
    ProgressBar progressBar;
    private TextView tvProgress;

    public ProgressDialog(Context context) {
        super(context, R.style.CommonDialog);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_progress);
        if (getWindow() != null) {
            getWindow().setGravity(17);
        }
        this.mInfo = (TextView) findViewById(R.id.tv_dialog_content);
        this.tvProgress = (TextView) findViewById(R.id.tv_progress);
        this.progressBar = (ProgressBar) findViewById(R.id.progressbar);
        this.mSureBtn = (Button) findViewById(R.id.btn_dialog_submit);
        this.mSureBtn.setOnClickListener(this);
    }

    public void setProgress(int i) {
        this.progressBar.setProgress(i);
        TextView textView = this.tvProgress;
        textView.setText(i + "%");
        if (i == 100) {
            findViewById(R.id.ll_progress).setVisibility(8);
            this.mInfo.setVisibility(0);
            this.mInfo.setText("下载成功");
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_dialog_submit) {
            dismiss();
        }
    }

    public void setCancelable(boolean z) {
        super.setCancelable(z);
    }
}
