package kotlinx.coroutines.experimental;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J#\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H@ø\u0001\u0000¢\u0006\u0002\u0010\bJ \u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH\u0016J&\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\u000fH&\u0002\u0004\n\u0002\b\t¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/experimental/Delay;", "", "delay", "", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "(JLjava/util/concurrent/TimeUnit;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "invokeOnTimeout", "Lkotlinx/coroutines/experimental/DisposableHandle;", "block", "Ljava/lang/Runnable;", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Delay.kt */
public interface Delay {
    Object delay(long j, TimeUnit timeUnit, Continuation<? super Unit> continuation);

    DisposableHandle invokeOnTimeout(long j, TimeUnit timeUnit, Runnable runnable);

    void scheduleResumeAfterDelay(long j, TimeUnit timeUnit, CancellableContinuation<? super Unit> cancellableContinuation);

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Delay.kt */
    public static final class DefaultImpls {
        public static /* bridge */ /* synthetic */ Object delay$default(Delay delay, long j, TimeUnit timeUnit, Continuation continuation, int i, Object obj) {
            if (obj == null) {
                if ((i & 2) != 0) {
                    timeUnit = TimeUnit.MILLISECONDS;
                }
                return delay.delay(j, timeUnit, continuation);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: delay");
        }

        public static Object delay(Delay delay, long j, TimeUnit timeUnit, Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
            boolean z = false;
            int i = (j > ((long) 0) ? 1 : (j == ((long) 0) ? 0 : -1));
            if (i >= 0) {
                z = true;
            }
            if (!z) {
                throw new IllegalArgumentException(("Delay time " + j + " cannot be negative").toString());
            } else if (i <= 0) {
                return Unit.INSTANCE;
            } else {
                CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
                cancellableContinuationImpl.initCancellability();
                delay.scheduleResumeAfterDelay(j, timeUnit, cancellableContinuationImpl);
                return cancellableContinuationImpl.getResult();
            }
        }

        public static DisposableHandle invokeOnTimeout(Delay delay, long j, TimeUnit timeUnit, Runnable runnable) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Intrinsics.checkParameterIsNotNull(runnable, "block");
            ScheduledFuture<?> schedule = ScheduledKt.getScheduledExecutor().schedule(runnable, j, timeUnit);
            Intrinsics.checkExpressionValueIsNotNull(schedule, "scheduledExecutor.schedule(block, time, unit)");
            return new DisposableFutureHandle(schedule);
        }
    }
}
