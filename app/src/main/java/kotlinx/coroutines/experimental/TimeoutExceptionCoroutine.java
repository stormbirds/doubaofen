package kotlinx.coroutines.experimental;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004B#\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\nJ\u0015\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0012J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0010H\u0016R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lkotlinx/coroutines/experimental/TimeoutExceptionCoroutine;", "T", "Lkotlinx/coroutines/experimental/JobSupport;", "Ljava/lang/Runnable;", "Lkotlin/coroutines/experimental/Continuation;", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "cont", "(JLjava/util/concurrent/TimeUnit;Lkotlin/coroutines/experimental/Continuation;)V", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "resume", "", "value", "(Ljava/lang/Object;)V", "resumeWithException", "exception", "", "run", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Scheduled.kt */
final class TimeoutExceptionCoroutine<T> extends JobSupport implements Runnable, Continuation<T> {
    private final Continuation<T> cont;
    private final CoroutineContext context = this.cont.getContext().plus(this);
    private final long time;
    private final TimeUnit unit;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TimeoutExceptionCoroutine(long j, TimeUnit timeUnit, Continuation<? super T> continuation) {
        super(true);
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(continuation, "cont");
        this.time = j;
        this.unit = timeUnit;
        this.cont = continuation;
    }

    public CoroutineContext getContext() {
        return this.context;
    }

    public void run() {
        cancel(new TimeoutException(this.time, this.unit));
    }

    public void resume(T t) {
        CoroutineDispatcherKt.resumeDirect(this.cont, t);
    }

    public void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        CoroutineDispatcherKt.resumeDirectWithException(this.cont, th);
    }
}
