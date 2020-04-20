package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002 \u0000H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "T", "run"}, k = 3, mv = {1, 1, 6})
/* compiled from: CoroutineDispatcher.kt */
final class DispatchedContinuation$resume$1 implements Runnable {
    final /* synthetic */ Object $value;
    final /* synthetic */ DispatchedContinuation this$0;

    DispatchedContinuation$resume$1(DispatchedContinuation dispatchedContinuation, Object obj) {
        this.this$0 = dispatchedContinuation;
        this.$value = obj;
    }

    public final void run() {
        DispatchedContinuation dispatchedContinuation = this.this$0;
        Object obj = this.$value;
        String updateContext = CoroutineContextKt.updateContext(dispatchedContinuation.getContext());
        try {
            dispatchedContinuation.continuation.resume(obj);
            Unit unit = Unit.INSTANCE;
        } finally {
            CoroutineContextKt.restoreContext(updateContext);
        }
    }
}
