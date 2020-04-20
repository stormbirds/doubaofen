package cn.hiui.voice;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
final class DownloadActivity$getList$1$onFailure$1 implements Runnable {
    final /* synthetic */ DownloadActivity$getList$1 this$0;

    DownloadActivity$getList$1$onFailure$1(DownloadActivity$getList$1 downloadActivity$getList$1) {
        this.this$0 = downloadActivity$getList$1;
    }

    public final void run() {
        Toast makeText = Toast.makeText(this.this$0.this$0, "网络错误", 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        makeText.show();
        TextView textView = (TextView) this.this$0.this$0._$_findCachedViewById(R.id.empty);
        Intrinsics.checkExpressionValueIsNotNull(textView, "empty");
        textView.setText("网络错误");
        ProgressBar progressBar = (ProgressBar) this.this$0.this$0._$_findCachedViewById(R.id.loading);
        Intrinsics.checkExpressionValueIsNotNull(progressBar, "loading");
        progressBar.setVisibility(8);
        ListView listView = (ListView) this.this$0.this$0._$_findCachedViewById(R.id.list);
        Intrinsics.checkExpressionValueIsNotNull(listView, "list");
        listView.setVisibility(0);
    }
}
