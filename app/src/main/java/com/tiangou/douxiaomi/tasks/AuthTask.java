package com.tiangou.douxiaomi.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.jni.Jni;

public class AuthTask extends AsyncTask {
    Context context;
    String icid;
    TextView title;

    public AuthTask(TextView textView, String str) {
        this.icid = str;
        this.title = textView;
        this.context = textView.getContext();
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Object[] objArr) {
        int intValue = objArr[0].intValue();
        String str = objArr[1];
        if (intValue == 1) {
            this.title.setText(str);
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        if (obj != null) {
            Toast.makeText(this.context, obj.toString(), 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object[] objArr) {
        try {
            String login = Jni.login(this.context, this.icid);
            publishProgress(new Object[]{0, login});
            String str = login.split("&")[0];
            String str2 = login.split("&")[1];
            int parseInt = Integer.parseInt(str);
            if (parseInt == 0) {
                return str2;
            }
            if (parseInt != 1) {
                return null;
            }
            SPUtils.saveAuthCode(this.context, this.icid);
            publishProgress(new Object[]{1, "已授权"});
            return "授权成功";
        } catch (Exception e) {
            return e.toString();
        }
    }
}
