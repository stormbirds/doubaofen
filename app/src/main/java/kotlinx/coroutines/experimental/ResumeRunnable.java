package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/ResumeRunnable;", "Ljava/lang/Runnable;", "continuation", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlin/coroutines/experimental/Continuation;)V", "run", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Executors.kt */
public final class ResumeRunnable implements Runnable {
    private final Continuation<Unit> continuation;

    public ResumeRunnable(Continuation<? super Unit> continuation2) {
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        this.continuation = continuation2;
    }

    public void run() {
        this.continuation.resume(Unit.INSTANCE);
    }
}
