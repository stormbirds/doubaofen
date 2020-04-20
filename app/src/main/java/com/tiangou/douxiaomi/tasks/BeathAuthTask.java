package com.tiangou.douxiaomi.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.tiangou.douxiaomi.jni.Jni;

public class BeathAuthTask extends AsyncTask {
    Context context;
    String icid;

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        if (obj == null) {
        }
    }

    public BeathAuthTask(String str, Context context2) {
        this.icid = str;
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Object[] objArr) {
        objArr[0].intValue();
        String str = objArr[1];
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
            if (parseInt == 1) {
                return "验证成功1";
            }
            return null;
        } catch (Exception e) {
            return e.toString();
        }
    }
}
