package cn.hiui.voice;

import android.content.DialogInterface;
import cn.hiui.voice.DownloadActivity;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.anko.AlertBuilder;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "Lorg/jetbrains/anko/AlertBuilder;", "Landroid/content/DialogInterface;", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
final class DownloadActivity$downloadMp3$1 extends Lambda implements Function1<AlertBuilder<? extends DialogInterface>, Unit> {
    final /* synthetic */ DownloadActivity.Companion.NetData $data;
    final /* synthetic */ DownloadActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DownloadActivity$downloadMp3$1(DownloadActivity downloadActivity, DownloadActivity.Companion.NetData netData) {
        super(1);
        this.this$0 = downloadActivity;
        this.$data = netData;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((AlertBuilder<? extends DialogInterface>) (AlertBuilder) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(AlertBuilder<? extends DialogInterface> alertBuilder) {
        Intrinsics.checkParameterIsNotNull(alertBuilder, "$receiver");
        alertBuilder.setTitle("正在试听:" + this.$data.name());
        alertBuilder.negativeButton("取消", (Function1<? super DialogInterface, Unit>) new Function1<DialogInterface, Unit>(this) {
            final /* synthetic */ DownloadActivity$downloadMp3$1 this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((DialogInterface) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(DialogInterface dialogInterface) {
                Intrinsics.checkParameterIsNotNull(dialogInterface, "it");
                this.this$0.this$0.getMMediaPlayer().stop();
            }
        });
        alertBuilder.positiveButton("下载", (Function1<? super DialogInterface, Unit>) new Function1<DialogInterface, Unit>(this) {
            final /* synthetic */ DownloadActivity$downloadMp3$1 this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((DialogInterface) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(DialogInterface dialogInterface) {
                Intrinsics.checkParameterIsNotNull(dialogInterface, "it");
                this.this$0.this$0.downloadSlk(this.this$0.$data);
                this.this$0.this$0.getMMediaPlayer().stop();
            }
        });
        alertBuilder.onCancelled(new Function1<DialogInterface, Unit>(this) {
            final /* synthetic */ DownloadActivity$downloadMp3$1 this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((DialogInterface) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(DialogInterface dialogInterface) {
                Intrinsics.checkParameterIsNotNull(dialogInterface, "it");
                this.this$0.this$0.getMMediaPlayer().stop();
            }
        });
    }
}
