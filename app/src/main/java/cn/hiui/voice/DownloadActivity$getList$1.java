package cn.hiui.voice;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import cn.hiui.voice.DownloadActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, d2 = {"cn/hiui/voice/DownloadActivity$getList$1", "Lokhttp3/Callback;", "onFailure", "", "call", "Lokhttp3/Call;", "e", "Ljava/io/IOException;", "onResponse", "response", "Lokhttp3/Response;", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
public final class DownloadActivity$getList$1 implements Callback {
    final /* synthetic */ DownloadActivity this$0;

    DownloadActivity$getList$1(DownloadActivity downloadActivity) {
        this.this$0 = downloadActivity;
    }

    public void onFailure(Call call, IOException iOException) {
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(iOException, "e");
        Log.d(DownloadActivity.Companion.getTAG(), "onFailure: ");
        this.this$0.runOnUiThread(new DownloadActivity$getList$1$onFailure$1(this));
    }

    public void onResponse(Call call, Response response) {
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(response, "response");
        ResponseBody body = response.body();
        JSONObject parseObject = JSON.parseObject(body != null ? body.string() : null);
        String tag = DownloadActivity.Companion.getTAG();
        Log.d(tag, "onResponse: " + parseObject.toString());
        Integer integer = parseObject.getInteger("code");
        if (integer != null && integer.intValue() == 0) {
            JSONArray jSONArray = parseObject.getJSONArray("data");
            ArrayList arrayList = new ArrayList();
            Iterator<Object> it = jSONArray.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next != null) {
                    JSONArray jSONArray2 = (JSONArray) next;
                    try {
                        String decode = URLDecoder.decode(jSONArray2.get(0).toString(), Key.STRING_CHARSET_NAME);
                        String decode2 = URLDecoder.decode(jSONArray2.get(1).toString(), Key.STRING_CHARSET_NAME);
                        Intrinsics.checkExpressionValueIsNotNull(decode2, "slk");
                        Intrinsics.checkExpressionValueIsNotNull(decode, "mp3");
                        arrayList.add(new DownloadActivity.Companion.NetData(decode2, decode));
                    } catch (Throwable unused) {
                        Log.d(DownloadActivity.Companion.getTAG(), String.valueOf(jSONArray2.get(0).toString()));
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type com.alibaba.fastjson.JSONArray");
                }
            }
            this.this$0.runOnUiThread(new DownloadActivity$getList$1$onResponse$1(this, arrayList));
            return;
        }
        this.this$0.runOnUiThread(new DownloadActivity$getList$1$onResponse$2(this));
    }
}
