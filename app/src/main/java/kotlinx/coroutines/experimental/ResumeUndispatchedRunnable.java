package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0006H\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/experimental/ResumeUndispatchedRunnable;", "Ljava/lang/Runnable;", "dispatcher", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "continuation", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "", "(Lkotlinx/coroutines/experimental/CoroutineDispatcher;Lkotlinx/coroutines/experimental/CancellableContinuation;)V", "run", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Executors.kt */
final class ResumeUndispatchedRunnable implements Runnable {
    private final CancellableContinuation<Unit> continuation;
    private final CoroutineDispatcher dispatcher;

    public ResumeUndispatchedRunnable(CoroutineDispatcher coroutineDispatcher, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(coroutineDispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        this.dispatcher = coroutineDispatcher;
        this.continuation = cancellableContinuation;
    }

    public void run() {
        this.continuation.resumeUndispatched(this.dispatcher, Unit.INSTANCE);
    }
}
