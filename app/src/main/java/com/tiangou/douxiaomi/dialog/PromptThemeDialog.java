package com.tiangou.douxiaomi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.tiangou.douxiaomi.R;

public class PromptThemeDialog extends Dialog implements View.OnClickListener {
    private String leftBtnText = "取消";
    private Button mCloseBtn;
    private TextView mInfo;
    private PromptClickSureListener mListener;
    private String mPromptInfo;
    private String mPromptmessage;
    private Button mSureBtn;
    private String rightBtnText = "确定";
    private TextView tvMessage;

    public interface PromptClickSureListener {
        void onClose();

        void onSure();
    }

    public PromptThemeDialog(Context context, String str, PromptClickSureListener promptClickSureListener) {
        super(context, R.style.CommonDialog);
        this.mPromptInfo = str;
        this.mListener = promptClickSureListener;
    }

    public PromptThemeDialog(Context context, String str, String str2, PromptClickSureListener promptClickSureListener) {
        super(context, R.style.CommonDialog);
        this.mPromptInfo = str;
        this.mPromptmessage = str2;
        this.mListener = promptClickSureListener;
    }

    public PromptThemeDialog(Context context, String str, String str2, String str3, String str4, PromptClickSureListener promptClickSureListener) {
        super(context, R.style.CommonDialog);
        this.mPromptInfo = str;
        this.mListener = promptClickSureListener;
        this.mPromptmessage = str2;
        this.leftBtnText = str3;
        this.rightBtnText = str4;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_prompt_theme);
        if (getWindow() != null) {
            getWindow().setGravity(17);
        }
        this.mInfo = (TextView) findViewById(R.id.tv_dialog_content);
        this.mInfo.setText(this.mPromptInfo);
        this.mSureBtn = (Button) findViewById(R.id.btn_dialog_submit);
        this.mCloseBtn = (Button) findViewById(R.id.btn_dialog_cancel);
        this.tvMessage = (TextView) findViewById(R.id.tv_dialog_message);
        if (!TextUtils.isEmpty(this.mPromptmessage)) {
            this.tvMessage.setVisibility(0);
            this.tvMessage.setText(this.mPromptmessage);
        }
        if (!TextUtils.isEmpty(this.leftBtnText)) {
            this.mCloseBtn.setText(this.leftBtnText);
        } else {
            findViewById(R.id.view_line).setVisibility(8);
            this.mCloseBtn.setVisibility(8);
        }
        if (!TextUtils.isEmpty(this.rightBtnText)) {
            this.mSureBtn.setText(this.rightBtnText);
        } else {
            this.mSureBtn.setVisibility(8);
            findViewById(R.id.view_line).setVisibility(8);
        }
        this.mSureBtn.setOnClickListener(this);
        this.mCloseBtn.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_dialog_submit) {
            dismiss();
            PromptClickSureListener promptClickSureListener = this.mListener;
            if (promptClickSureListener != null) {
                promptClickSureListener.onSure();
            }
        }
        if (view.getId() == R.id.btn_dialog_cancel) {
            PromptClickSureListener promptClickSureListener2 = this.mListener;
            if (promptClickSureListener2 != null) {
                promptClickSureListener2.onClose();
            }
            dismiss();
        }
    }

    public void setCancelable(boolean z) {
        super.setCancelable(z);
    }
}
