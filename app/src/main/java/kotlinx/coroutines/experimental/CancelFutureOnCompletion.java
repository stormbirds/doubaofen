package kotlinx.coroutines.experimental;

import java.util.concurrent.Future;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0019\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016R\u0014\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u00058\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/experimental/CancelFutureOnCompletion;", "Lkotlinx/coroutines/experimental/JobNode;", "Lkotlinx/coroutines/experimental/Job;", "job", "future", "Ljava/util/concurrent/Future;", "(Lkotlinx/coroutines/experimental/Job;Ljava/util/concurrent/Future;)V", "invoke", "", "reason", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
final class CancelFutureOnCompletion extends JobNode<Job> {
    public final Future<?> future;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CancelFutureOnCompletion(Job job, Future<?> future2) {
        super(job);
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(future2, "future");
        this.future = future2;
    }

    public void invoke(Throwable th) {
        this.future.cancel(false);
    }

    public String toString() {
        return "CancelFutureOnCompletion[" + this.future + "]";
    }
}
