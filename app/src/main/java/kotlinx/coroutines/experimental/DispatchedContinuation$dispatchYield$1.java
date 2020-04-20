package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002 \u0000H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "T", "run"}, k = 3, mv = {1, 1, 6})
/* compiled from: CoroutineDispatcher.kt */
final class DispatchedContinuation$dispatchYield$1 implements Runnable {
    final /* synthetic */ CoroutineContext $context;
    final /* synthetic */ Job $job;
    final /* synthetic */ Object $value;
    final /* synthetic */ DispatchedContinuation this$0;

    DispatchedContinuation$dispatchYield$1(DispatchedContinuation dispatchedContinuation, CoroutineContext coroutineContext, Job job, Object obj) {
        this.this$0 = dispatchedContinuation;
        this.$context = coroutineContext;
        this.$job = job;
        this.$value = obj;
    }

    public final void run() {
        String updateContext = CoroutineContextKt.updateContext(this.$context);
        try {
            if (this.$job == null || !this.$job.isCompleted()) {
                this.this$0.continuation.resume(this.$value);
            } else {
                this.this$0.continuation.resumeWithException(this.$job.getCompletionException());
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            CoroutineContextKt.restoreContext(updateContext);
        }
    }
}
