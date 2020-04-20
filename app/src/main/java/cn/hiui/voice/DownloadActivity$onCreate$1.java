package cn.hiui.voice;

import android.widget.AdapterView;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\u0010\u0000\u001a\u00020\u00012\u0016\u0010\u0002\u001a\u0012\u0012\u0002\b\u0003 \u0004*\b\u0012\u0002\b\u0003\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\n¢\u0006\u0002\b\u000b"}, d2 = {"<anonymous>", "", "parent", "Landroid/widget/AdapterView;", "kotlin.jvm.PlatformType", "view", "Landroid/view/View;", "position", "", "id", "", "onItemClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
final class DownloadActivity$onCreate$1 implements AdapterView.OnItemClickListener {
    final /* synthetic */ DownloadActivity this$0;

    DownloadActivity$onCreate$1(DownloadActivity downloadActivity) {
        this.this$0 = downloadActivity;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [android.widget.AdapterView<?>, java.lang.Object, android.widget.AdapterView] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onItemClick(android.widget.AdapterView<?> r1, android.view.View r2, int r3, long r4) {
        /*
            r0 = this;
            java.lang.String r2 = "parent"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r1, r2)
            android.widget.Adapter r1 = r1.getAdapter()
            if (r1 != 0) goto L_0x000e
            kotlin.jvm.internal.Intrinsics.throwNpe()
        L_0x000e:
            java.lang.Object r1 = r1.getItem(r3)
            if (r1 == 0) goto L_0x0045
            cn.hiui.voice.DownloadActivity$Companion$NetData r1 = (cn.hiui.voice.DownloadActivity.Companion.NetData) r1
            cn.hiui.voice.MainActivity$Companion r2 = cn.hiui.voice.MainActivity.Companion
            java.lang.String r2 = r2.getTAG()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "click "
            r3.append(r4)
            java.lang.String r4 = r1.name()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.i(r2, r3)
            cn.hiui.voice.DownloadActivity r2 = r0.this$0
            cn.hiui.voice.DownloadActivity$onCreate$1$1 r3 = new cn.hiui.voice.DownloadActivity$onCreate$1$1
            r3.<init>(r0, r1)
            kotlin.jvm.functions.Function1 r3 = (kotlin.jvm.functions.Function1) r3
            org.jetbrains.anko.AlertBuilder r1 = org.jetbrains.anko.AndroidDialogsKt.alert((android.content.Context) r2, (kotlin.jvm.functions.Function1<? super org.jetbrains.anko.AlertBuilder<? extends android.content.DialogInterface>, kotlin.Unit>) r3)
            r1.show()
            return
        L_0x0045:
            kotlin.TypeCastException r1 = new kotlin.TypeCastException
            java.lang.String r2 = "null cannot be cast to non-null type cn.hiui.voice.DownloadActivity.Companion.NetData"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hiui.voice.DownloadActivity$onCreate$1.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
    }
}
