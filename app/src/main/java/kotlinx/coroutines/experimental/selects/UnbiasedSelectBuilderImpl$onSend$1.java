package kotlinx.coroutines.experimental.selects;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.experimental.channels.SendChannel;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0006\b\u0001\u0010\u0003 \u0000H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "E", "R", "invoke"}, k = 3, mv = {1, 1, 6})
/* compiled from: SelectUnbiased.kt */
final class UnbiasedSelectBuilderImpl$onSend$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Function1 $block;
    final /* synthetic */ Object $element;
    final /* synthetic */ SendChannel receiver$0;
    final /* synthetic */ UnbiasedSelectBuilderImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UnbiasedSelectBuilderImpl$onSend$1(UnbiasedSelectBuilderImpl unbiasedSelectBuilderImpl, SendChannel sendChannel, Object obj, Function1 function1) {
        super(0);
        this.this$0 = unbiasedSelectBuilderImpl;
        this.receiver$0 = sendChannel;
        this.$element = obj;
        this.$block = function1;
    }

    public final void invoke() {
        this.receiver$0.registerSelectSend(this.this$0.getInstance(), this.$element, this.$block);
    }
}
