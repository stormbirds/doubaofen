package com.tiangou.douxiaomi.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.tiangou.douxiaomi.SPUtils;
import com.tiangou.douxiaomi.jni.Jni;

public class InitTask extends AsyncTask {
    Context context;

    public InitTask(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        if (((Boolean) obj).booleanValue()) {
            String authCode = SPUtils.getAuthCode(this.context);
            if (!TextUtils.isEmpty(authCode)) {
                new CheckAuthTask(authCode, this.context).execute(new Object[0]);
                new GetUpdateInfoTask((Activity) this.context).execute(new Object[0]);
                new GetAdvertisementTask(this.context).execute(new Object[0]);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object[] objArr) {
        if (Jni.init(this.context) != 0) {
            return false;
        }
        return true;
    }
}
