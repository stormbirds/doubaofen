package cn.hiui.voice;

import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
final class DownloadActivity$downloadSlk$2$onResponse$1 implements Runnable {
    final /* synthetic */ DownloadActivity$downloadSlk$2 this$0;

    DownloadActivity$downloadSlk$2$onResponse$1(DownloadActivity$downloadSlk$2 downloadActivity$downloadSlk$2) {
        this.this$0 = downloadActivity$downloadSlk$2;
    }

    public final void run() {
        DownloadActivity downloadActivity = this.this$0.this$0;
        Toast makeText = Toast.makeText(downloadActivity, "下载完成:" + this.this$0.$data.name(), 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
    }
}
