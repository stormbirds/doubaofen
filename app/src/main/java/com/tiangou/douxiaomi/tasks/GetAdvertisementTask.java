package com.tiangou.douxiaomi.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.tiangou.douxiaomi.event.AdGetSuccessEvent;
import com.tiangou.douxiaomi.jni.Jni;
import de.greenrobot.event.EventBus;

public class GetAdvertisementTask extends AsyncTask {
    String ad_url1;
    Context context;
    String img1Url;

    public GetAdvertisementTask(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        EventBus.getDefault().post(new AdGetSuccessEvent((String) obj));
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object[] objArr) {
        try {
            Thread.sleep(1000);
            return Jni.getAdvertisement();
        } catch (Exception unused) {
            return null;
        }
    }
}
