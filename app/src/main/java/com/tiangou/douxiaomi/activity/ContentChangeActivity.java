package com.tiangou.douxiaomi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.R;

public class ContentChangeActivity extends Activity {
    EditText etContent;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_content);
        this.etContent = (EditText) findViewById(R.id.et_content);
        this.etContent.setText(App.getInstance().config.getContent());
    }

    public void save(View view) {
        App.getInstance().config.setContent(this.etContent.getText().toString());
        finish();
    }
}
