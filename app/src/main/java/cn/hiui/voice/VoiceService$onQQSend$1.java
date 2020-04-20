package cn.hiui.voice;

import android.content.Context;
import android.widget.Toast;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "Landroid/content/Context;", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$onQQSend$1 extends Lambda implements Function1<Context, Unit> {
    public static final VoiceService$onQQSend$1 INSTANCE = new VoiceService$onQQSend$1();

    VoiceService$onQQSend$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Context) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "$receiver");
        Toast makeText = Toast.makeText(context, "语音已写入", 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
    }
}
