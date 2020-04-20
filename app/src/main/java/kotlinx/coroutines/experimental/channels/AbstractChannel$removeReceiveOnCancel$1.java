package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.experimental.CancellableContinuation;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "E", "it", "", "invoke"}, k = 3, mv = {1, 1, 6})
/* compiled from: AbstractChannel.kt */
final class AbstractChannel$removeReceiveOnCancel$1 extends Lambda implements Function1<Throwable, Unit> {
    final /* synthetic */ CancellableContinuation $cont;
    final /* synthetic */ Receive $receive;
    final /* synthetic */ AbstractChannel this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    AbstractChannel$removeReceiveOnCancel$1(AbstractChannel abstractChannel, CancellableContinuation cancellableContinuation, Receive receive) {
        super(1);
        this.this$0 = abstractChannel;
        this.$cont = cancellableContinuation;
        this.$receive = receive;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Throwable th) {
        if (this.$cont.isCancelled() && this.$receive.remove()) {
            this.this$0.onCancelledReceive();
        }
    }
}
