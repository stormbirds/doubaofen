package cn.hiui.voice;

import android.widget.ListView;
import android.widget.ProgressBar;
import cn.hiui.voice.DownloadActivity;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
final class DownloadActivity$getList$1$onResponse$1 implements Runnable {
    final /* synthetic */ ArrayList $dataList;
    final /* synthetic */ DownloadActivity$getList$1 this$0;

    DownloadActivity$getList$1$onResponse$1(DownloadActivity$getList$1 downloadActivity$getList$1, ArrayList arrayList) {
        this.this$0 = downloadActivity$getList$1;
        this.$dataList = arrayList;
    }

    public final void run() {
        ProgressBar progressBar = (ProgressBar) this.this$0.this$0._$_findCachedViewById(R.id.loading);
        Intrinsics.checkExpressionValueIsNotNull(progressBar, "loading");
        progressBar.setVisibility(8);
        ListView listView = (ListView) this.this$0.this$0._$_findCachedViewById(R.id.list);
        Intrinsics.checkExpressionValueIsNotNull(listView, "list");
        listView.setVisibility(0);
        ListView listView2 = (ListView) this.this$0.this$0._$_findCachedViewById(R.id.list);
        Intrinsics.checkExpressionValueIsNotNull(listView2, "list");
        listView2.setAdapter(new DownloadActivity.Companion.NetDataAdapter(this.$dataList, this.this$0.this$0));
    }
}
