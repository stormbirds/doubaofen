package com.tiangou.douxiaomi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.tiangou.douxiaomi.App;
import com.tiangou.douxiaomi.R;

public class UserListActivity extends Activity {
    EditText etContent;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_user_list);
        this.etContent = (EditText) findViewById(R.id.et_content);
        this.etContent.setText(App.getInstance().config.getUsers());
    }

    public void save(View view) {
        try {
            if (!this.etContent.getText().toString().equals(App.getInstance().config.users)) {
                App.getInstance().config.setUsers(this.etContent.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
