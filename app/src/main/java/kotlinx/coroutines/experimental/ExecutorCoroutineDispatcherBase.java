package kotlinx.coroutines.experimental;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Delay;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b \u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\f\u001a\u00020\rH\u0016J&\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016R\u0012\u0010\u0004\u001a\u00020\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/experimental/ExecutorCoroutineDispatcherBase;", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "Lkotlinx/coroutines/experimental/Delay;", "()V", "executor", "Ljava/util/concurrent/Executor;", "getExecutor", "()Ljava/util/concurrent/Executor;", "dispatch", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "block", "Ljava/lang/Runnable;", "equals", "", "other", "", "hashCode", "", "invokeOnTimeout", "Lkotlinx/coroutines/experimental/DisposableHandle;", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Executors.kt */
public abstract class ExecutorCoroutineDispatcherBase extends CoroutineDispatcher implements Delay {
    public abstract Executor getExecutor();

    public Object delay(long j, TimeUnit timeUnit, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return Delay.DefaultImpls.delay(this, j, timeUnit, continuation);
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        getExecutor().execute(runnable);
    }

    public void scheduleResumeAfterDelay(long j, TimeUnit timeUnit, CancellableContinuation<? super Unit> cancellableContinuation) {
        ScheduledFuture<?> scheduledFuture;
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        Executor executor = getExecutor();
        if (!(executor instanceof ScheduledExecutorService)) {
            executor = null;
        }
        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) executor;
        if (scheduledExecutorService == null || (scheduledFuture = scheduledExecutorService.schedule(new ResumeUndispatchedRunnable(this, cancellableContinuation), j, timeUnit)) == null) {
            scheduledFuture = ScheduledKt.getScheduledExecutor().schedule(new ResumeRunnable(cancellableContinuation), j, timeUnit);
        }
        Intrinsics.checkExpressionValueIsNotNull(scheduledFuture, "timeout");
        JobKt.cancelFutureOnCompletion(cancellableContinuation, scheduledFuture);
    }

    public DisposableHandle invokeOnTimeout(long j, TimeUnit timeUnit, Runnable runnable) {
        ScheduledFuture<?> scheduledFuture;
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        Executor executor = getExecutor();
        if (!(executor instanceof ScheduledExecutorService)) {
            executor = null;
        }
        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) executor;
        if (scheduledExecutorService == null || (scheduledFuture = scheduledExecutorService.schedule(runnable, j, timeUnit)) == null) {
            scheduledFuture = ScheduledKt.getScheduledExecutor().schedule(runnable, j, timeUnit);
        }
        Intrinsics.checkExpressionValueIsNotNull(scheduledFuture, "timeout");
        return new DisposableFutureHandle(scheduledFuture);
    }

    public String toString() {
        return getExecutor().toString();
    }

    public boolean equals(Object obj) {
        return (obj instanceof ExecutorCoroutineDispatcherBase) && ((ExecutorCoroutineDispatcherBase) obj).getExecutor() == getExecutor();
    }

    public int hashCode() {
        return System.identityHashCode(getExecutor());
    }
}
