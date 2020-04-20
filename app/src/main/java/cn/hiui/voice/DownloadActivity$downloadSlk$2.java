package cn.hiui.voice;

import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import cn.hiui.voice.DownloadActivity;
import java.io.File;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, d2 = {"cn/hiui/voice/DownloadActivity$downloadSlk$2", "Lokhttp3/Callback;", "onFailure", "", "call", "Lokhttp3/Call;", "e", "Ljava/io/IOException;", "onResponse", "response", "Lokhttp3/Response;", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
public final class DownloadActivity$downloadSlk$2 implements Callback {
    final /* synthetic */ DownloadActivity.Companion.NetData $data;
    final /* synthetic */ File $toFile;
    final /* synthetic */ DownloadActivity this$0;

    DownloadActivity$downloadSlk$2(DownloadActivity downloadActivity, File file, DownloadActivity.Companion.NetData netData) {
        this.this$0 = downloadActivity;
        this.$toFile = file;
        this.$data = netData;
    }

    public void onFailure(Call call, IOException iOException) {
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(iOException, "e");
        Toast makeText = Toast.makeText(this.this$0, "网络错误", 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        makeText.show();
    }

    public void onResponse(Call call, Response response) {
        Intrinsics.checkParameterIsNotNull(call, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(response, "response");
        File file = this.$toFile;
        ResponseBody body = response.body();
        if (body == null) {
            Intrinsics.throwNpe();
        }
        byte[] bytes = body.bytes();
        Intrinsics.checkExpressionValueIsNotNull(bytes, "response.body()!!.bytes()");
        FilesKt.writeBytes(file, bytes);
        this.this$0.runOnUiThread(new DownloadActivity$downloadSlk$2$onResponse$1(this));
    }
}
