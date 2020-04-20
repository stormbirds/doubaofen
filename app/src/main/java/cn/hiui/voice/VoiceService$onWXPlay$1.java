package cn.hiui.voice;

import android.content.Context;
import cn.hiui.voice.VoiceService;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "Landroid/content/Context;", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$onWXPlay$1 extends Lambda implements Function1<Context, Unit> {
    final /* synthetic */ VoiceService this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    VoiceService$onWXPlay$1(VoiceService voiceService) {
        super(1);
        this.this$0 = voiceService;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Context) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "$receiver");
        VoiceService.Companion.DataAdapter access$getAdapter2$p = this.this$0.adapter2;
        if (access$getAdapter2$p != null) {
            access$getAdapter2$p.notifyDataSetChanged();
        }
    }
}
