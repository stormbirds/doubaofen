package com.tiangou.douxiaomi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.R;
import com.tiangou.douxiaomi.Utils;
import com.tiangou.douxiaomi.service.AccessService;
import com.tiangou.douxiaomi.utils.StatusbarUtil;
import ezy.assist.compat.SettingsCompat;
import java.util.ArrayList;

public class SettingActivity extends Activity implements View.OnClickListener {
    CheckBox cbMan;
    CheckBox cbSendRandom;
    CheckBox cbSettingStatus;
    CheckBox cbSusStatus;
    CheckBox cbUnknow;
    CheckBox cbWomen;
    EditText etDurationMax;
    EditText etDurationMin;
    EditText etForwardTxt;
    EditText etLookMax;
    EditText etLookMin;
    EditText etMessageCount;
    EditText etOperationMax;
    EditText etOprationMin;
    EditText etPersonNum;
    EditText etRunNum;
    EditText etRunSleep;
    EditText etRunSleepMax;
    EditText etYangAtten;
    EditText etYangComment;
    EditText etYangForward;
    EditText etYangMax;
    EditText etYangMin;
    EditText etYangPraise;
    EditText etYangTotal;
    TextView tvContentNum;
    TextView tvUserNum;

    private void setOthers() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting_new);
        StatusbarUtil.setBgTransparent(this);
        initView();
        setListener();
        setOthers();
    }

    private void initView() {
        this.cbMan = (CheckBox) findViewById(R.id.cb_man);
        this.cbWomen = (CheckBox) findViewById(R.id.cb_women);
        this.cbUnknow = (CheckBox) findViewById(R.id.cb_unknow);
        this.cbSettingStatus = (CheckBox) findViewById(R.id.cb_setting_status);
        this.cbSusStatus = (CheckBox) findViewById(R.id.cb_sus_status);
        this.cbSendRandom = (CheckBox) findViewById(R.id.cb_send_random);
        this.tvContentNum = (TextView) findViewById(R.id.tv_content_num);
        this.etLookMin = (EditText) findViewById(R.id.et_yang_look_min);
        this.etLookMax = (EditText) findViewById(R.id.et_yang_look_max);
        this.etOprationMin = (EditText) findViewById(R.id.et_operation_min);
        this.etOperationMax = (EditText) findViewById(R.id.et_operation_max);
        this.etMessageCount = (EditText) findViewById(R.id.et_message_count);
        this.etRunSleep = (EditText) findViewById(R.id.et_run_sleep);
        this.etRunSleepMax = (EditText) findViewById(R.id.et_run_sleep_max);
        this.etRunNum = (EditText) findViewById(R.id.et_run_num);
        this.etPersonNum = (EditText) findViewById(R.id.et_person_num);
        this.etDurationMin = (EditText) findViewById(R.id.et_duration_min);
        this.etDurationMax = (EditText) findViewById(R.id.et_duration_max);
        this.tvUserNum = (TextView) findViewById(R.id.tv_user_num);
        this.etYangTotal = (EditText) findViewById(R.id.et_yang_totle);
        this.etYangComment = (EditText) findViewById(R.id.et_comment_num);
        this.etYangAtten = (EditText) findViewById(R.id.et_atten_num);
        this.etYangPraise = (EditText) findViewById(R.id.et_praise_num);
        this.etYangForward = (EditText) findViewById(R.id.et_forward_num);
        this.etForwardTxt = (EditText) findViewById(R.id.et_forward_text);
        this.etYangMin = (EditText) findViewById(R.id.et_yang_min);
        this.etYangMax = (EditText) findViewById(R.id.et_yang_max);
    }

    private void setListener() {
        this.tvContentNum.setOnClickListener(this);
        this.cbSettingStatus.setOnClickListener(this);
        this.cbSusStatus.setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.tv_user_num).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        solveStatus();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    private void solveStatus() {
        if (Utils.isSettingOpen(AccessService.class, this)) {
            this.cbSettingStatus.setChecked(true);
        } else {
            this.cbSettingStatus.setChecked(false);
        }
        if (SettingsCompat.canDrawOverlays(this)) {
            this.cbSusStatus.setChecked(true);
        } else {
            this.cbSusStatus.setChecked(false);
        }
        App.getInstance().config.getContent();
        if (App.getInstance().config.message == null || App.getInstance().config.message.length == 0) {
            this.tvContentNum.setTextColor(Color.parseColor("#ff0000"));
            this.tvContentNum.setText("未设置");
        } else {
            this.tvContentNum.setTextColor(Color.parseColor("#ff669900"));
            TextView textView = this.tvContentNum;
            textView.setText(App.getInstance().config.message.length + "条");
        }
        if (TextUtils.isEmpty(App.getInstance().config.getUsers())) {
            this.tvUserNum.setTextColor(Color.parseColor("#ff0000"));
            this.tvUserNum.setText("未设置");
        } else {
            this.tvUserNum.setTextColor(Color.parseColor("#ff669900"));
            TextView textView2 = this.tvUserNum;
            textView2.setText(App.getInstance().config.users.split("\n").length + "人");
        }
        EditText editText = this.etPersonNum;
        editText.setText(App.getInstance().config.maxCount + "");
        EditText editText2 = this.etDurationMax;
        editText2.setText((App.getInstance().config.durationMax / 1000) + "");
        EditText editText3 = this.etDurationMin;
        editText3.setText((App.getInstance().config.durationMin / 1000) + "");
        EditText editText4 = this.etRunNum;
        editText4.setText(App.getInstance().config.runNum + "");
        EditText editText5 = this.etRunSleep;
        editText5.setText((App.getInstance().config.runSleep / 1000) + "");
        EditText editText6 = this.etRunSleepMax;
        editText6.setText((App.getInstance().config.runSleepMax / 1000) + "");
        EditText editText7 = this.etYangPraise;
        editText7.setText(App.getInstance().config.yangPraise + "");
        EditText editText8 = this.etYangForward;
        editText8.setText(App.getInstance().config.yangForward + "");
        EditText editText9 = this.etYangAtten;
        editText9.setText(App.getInstance().config.yangAtten + "");
        EditText editText10 = this.etYangTotal;
        editText10.setText(App.getInstance().config.yangTotal + "");
        EditText editText11 = this.etYangComment;
        editText11.setText(App.getInstance().config.yangComment + "");
        EditText editText12 = this.etYangMax;
        editText12.setText((App.getInstance().config.yangMax / 1000) + "");
        EditText editText13 = this.etYangMin;
        editText13.setText((App.getInstance().config.yangMin / 1000) + "");
        EditText editText14 = this.etMessageCount;
        editText14.setText(App.getInstance().config.messageCount + "");
        this.etForwardTxt.setText(App.getInstance().config.forwardStr);
        this.cbSendRandom.setChecked(App.getInstance().config.Disorder.booleanValue());
        this.cbMan.setChecked(App.getInstance().config.userSex.contains(0));
        this.cbWomen.setChecked(App.getInstance().config.userSex.contains(1));
        this.cbUnknow.setChecked(App.getInstance().config.userSex.contains(2));
        EditText editText15 = this.etLookMin;
        editText15.setText((App.getInstance().config.lookMin / 1000) + "");
        EditText editText16 = this.etLookMax;
        editText16.setText((App.getInstance().config.lookMax / 1000) + "");
        EditText editText17 = this.etOprationMin;
        editText17.setText((App.getInstance().config.operationMin / 1000) + "");
        EditText editText18 = this.etOperationMax;
        editText18.setText((App.getInstance().config.operationMax / 1000) + "");
    }

    private int getInputInt(EditText editText, int i) {
        String obj = editText.getText().toString();
        int parseInt = !TextUtils.isEmpty(obj) ? Integer.parseInt(obj) : 0;
        return parseInt < i ? i : parseInt;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_setting_status:
                Utils.jumpToSetting(this);
                return;
            case R.id.cb_sus_status:
                manageDrawOverlays(this);
                return;
            case R.id.iv_back:
                finish();
                return;
            case R.id.tv_content_num:
                startActivity(new Intent(this, ContentChangeActivity.class));
                return;
            case R.id.tv_save:
                saveData();
                return;
            case R.id.tv_user_num:
                startActivity(new Intent(this, UserListActivity.class));
                return;
            default:
                return;
        }
    }

    private void saveData() {
        App.getInstance().config.maxCount = getInputInt(this.etPersonNum, 1);
        App.getInstance().config.durationMin = getInputInt(this.etDurationMin, 2) * 1000;
        App.getInstance().config.durationMax = getInputInt(this.etDurationMax, 2) * 1000;
        App.getInstance().config.runNum = getInputInt(this.etRunNum, 1);
        App.getInstance().config.runSleep = getInputInt(this.etRunSleep, 1) * 1000;
        App.getInstance().config.runSleepMax = getInputInt(this.etRunSleepMax, 1) * 1000;
        App.getInstance().config.yangAtten = getInputInt(this.etYangAtten, 0);
        App.getInstance().config.yangTotal = getInputInt(this.etYangTotal, 1);
        App.getInstance().config.yangComment = getInputInt(this.etYangComment, 0);
        App.getInstance().config.yangPraise = getInputInt(this.etYangPraise, 0);
        App.getInstance().config.yangForward = getInputInt(this.etYangForward, 0);
        App.getInstance().config.forwardStr = this.etForwardTxt.getText().toString();
        App.getInstance().config.messageCount = getInputInt(this.etMessageCount, 1);
        App.getInstance().config.Disorder = Boolean.valueOf(this.cbSendRandom.isChecked());
        App.getInstance().config.yangMin = getInputInt(this.etYangMin, 2) * 1000;
        App.getInstance().config.yangMax = getInputInt(this.etYangMax, 5) * 1000;
        App.getInstance().config.lookMin = getInputInt(this.etLookMin, 1) * 1000;
        App.getInstance().config.lookMax = getInputInt(this.etLookMax, 1) * 1000;
        App.getInstance().config.operationMin = getInputInt(this.etOprationMin, 1) * 1000;
        App.getInstance().config.operationMax = getInputInt(this.etOperationMax, 1) * 1000;
        App.getInstance().config.userSex = new ArrayList();
        if (this.cbMan.isChecked()) {
            App.getInstance().config.userSex.add(0);
        }
        if (this.cbWomen.isChecked()) {
            App.getInstance().config.userSex.add(1);
        }
        if (this.cbUnknow.isChecked()) {
            App.getInstance().config.userSex.add(2);
        }
        App.getInstance().config.saveData();
        finish();
    }

    public void manageDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT < 23 && Build.VERSION.SDK_INT >= 18) {
            SettingsCompat.manageDrawOverlays(context);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }
}
