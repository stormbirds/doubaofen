package com.tiangou.douxiaomi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.jni.Jni;
import com.tiangou.douxiaomi.tasks.AuthTask;

public class AuthActivity extends AppCompatActivity {
    EditText et_authCode;
    TextView tv_title;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_auth);
        this.tv_title = (TextView) findViewById(R.id.tv_auth_title);
        this.et_authCode = (EditText) findViewById(R.id.et_auth_code);
        initView();
    }

    /* access modifiers changed from: package-private */
    public void initView() {
        if (Jni.getAuthStatus() == 0) {
            this.tv_title.setText("已授权");
        }
        this.et_authCode.setText(SPUtils.getAuthCode(this));
    }

    public void bt_auth(View view) {
        new AuthTask(this.tv_title, this.et_authCode.getText().toString()).execute(new Object[0]);
    }
}
