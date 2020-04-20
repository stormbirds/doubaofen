package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a\b\u0010\t\u001a\u00020\u0005H\u0003\u001a\b\u0010\n\u001a\u00020\u000bH\u0001\u001a\b\u0010\f\u001a\u00020\u000bH\u0001\u001aG\u0010\r\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e2\u0006\u0010\u000f\u001a\u00020\u00012\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u001c\u0010\u0012\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0013H@ø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001aI\u0010\u0017\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e2\u0006\u0010\u000f\u001a\u00020\u00012\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u001c\u0010\u0012\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0013H@ø\u0001\u0000¢\u0006\u0002\u0010\u0016\"\u0018\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001X\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000\"\u0014\u0010\u0006\u001a\u00020\u00058@X\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0002\u0004\n\u0002\b\t¨\u0006\u0018"}, d2 = {"KEEP_ALIVE", "", "kotlin.jvm.PlatformType", "Ljava/lang/Long;", "_scheduledExecutor", "Ljava/util/concurrent/ScheduledExecutorService;", "scheduledExecutor", "getScheduledExecutor", "()Ljava/util/concurrent/ScheduledExecutorService;", "getOrCreateScheduledExecutorSync", "scheduledExecutorShutdownNow", "", "scheduledExecutorShutdownNowAndRelease", "withTimeout", "T", "time", "unit", "Ljava/util/concurrent/TimeUnit;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "", "(JLjava/util/concurrent/TimeUnit;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "withTimeoutOrNull", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Scheduled.kt */
public final class ScheduledKt {
    private static final Long KEEP_ALIVE = Long.getLong("kotlinx.coroutines.ScheduledExecutor.keepAlive", 1000);
    private static volatile ScheduledExecutorService _scheduledExecutor;

    public static final ScheduledExecutorService getScheduledExecutor() {
        ScheduledExecutorService scheduledExecutorService = _scheduledExecutor;
        return scheduledExecutorService != null ? scheduledExecutorService : getOrCreateScheduledExecutorSync();
    }

    private static final synchronized ScheduledExecutorService getOrCreateScheduledExecutorSync() {
        ScheduledExecutorService scheduledExecutorService;
        synchronized (ScheduledKt.class) {
            scheduledExecutorService = _scheduledExecutor;
            if (scheduledExecutorService == null) {
                ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, ScheduledKt$getOrCreateScheduledExecutorSync$1.INSTANCE);
                Long l = KEEP_ALIVE;
                Intrinsics.checkExpressionValueIsNotNull(l, "KEEP_ALIVE");
                scheduledThreadPoolExecutor.setKeepAliveTime(l.longValue(), TimeUnit.MILLISECONDS);
                scheduledThreadPoolExecutor.allowCoreThreadTimeOut(true);
                scheduledThreadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
                try {
                    scheduledThreadPoolExecutor.getClass().getMethod("setRemoveOnCancelPolicy", new Class[]{JvmClassMappingKt.getJavaPrimitiveType(Reflection.getOrCreateKotlinClass(Boolean.TYPE))}).invoke(scheduledThreadPoolExecutor, new Object[]{true});
                } catch (Throwable unused) {
                }
                _scheduledExecutor = scheduledThreadPoolExecutor;
                scheduledExecutorService = scheduledThreadPoolExecutor;
            }
        }
        return scheduledExecutorService;
    }

    public static final synchronized void scheduledExecutorShutdownNow() {
        synchronized (ScheduledKt.class) {
            ScheduledExecutorService scheduledExecutorService = _scheduledExecutor;
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdownNow();
            }
        }
    }

    public static final synchronized void scheduledExecutorShutdownNowAndRelease() {
        synchronized (ScheduledKt.class) {
            ScheduledExecutorService scheduledExecutorService = _scheduledExecutor;
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdownNow();
                _scheduledExecutor = null;
            }
        }
    }

    public static /* bridge */ /* synthetic */ Object withTimeout$default(long j, TimeUnit timeUnit, Function1 function1, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        return withTimeout(j, timeUnit, function1, continuation);
    }

    public static final <T> Object withTimeout(long j, TimeUnit timeUnit, Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        boolean z = false;
        if (j >= ((long) 0)) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException(("Timeout time " + j + " cannot be negative").toString());
        } else if (j > 0) {
            Continuation<? super T> normalizeContinuation = CoroutineIntrinsics.normalizeContinuation(continuation);
            CoroutineContext context = normalizeContinuation.getContext();
            TimeoutExceptionCoroutine timeoutExceptionCoroutine = new TimeoutExceptionCoroutine(j, timeUnit, normalizeContinuation);
            CoroutineContext.Element element = context.get(ContinuationInterceptor.Key);
            if (!(element instanceof Delay)) {
                element = null;
            }
            Delay delay = (Delay) element;
            if (delay != null) {
                JobKt.disposeOnCompletion(timeoutExceptionCoroutine, delay.invokeOnTimeout(j, timeUnit, timeoutExceptionCoroutine));
            } else {
                ScheduledFuture<?> schedule = getScheduledExecutor().schedule(timeoutExceptionCoroutine, j, timeUnit);
                Intrinsics.checkExpressionValueIsNotNull(schedule, "scheduledExecutor.schedule(coroutine, time, unit)");
                JobKt.cancelFutureOnCompletion(timeoutExceptionCoroutine, schedule);
            }
            timeoutExceptionCoroutine.initParentJob((Job) context.get(Job.Key));
            return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(timeoutExceptionCoroutine);
        } else {
            throw new CancellationException("Timed out immediately");
        }
    }

    public static /* bridge */ /* synthetic */ Object withTimeoutOrNull$default(long j, TimeUnit timeUnit, Function1 function1, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        return withTimeoutOrNull(j, timeUnit, function1, continuation);
    }

    public static final <T> Object withTimeoutOrNull(long j, TimeUnit timeUnit, Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        boolean z = false;
        if (j >= ((long) 0)) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException(("Timeout time " + j + " cannot be negative").toString());
        } else if (j <= 0) {
            return null;
        } else {
            Continuation<? super T> normalizeContinuation = CoroutineIntrinsics.normalizeContinuation(continuation);
            CoroutineContext context = normalizeContinuation.getContext();
            TimeoutNullCoroutine timeoutNullCoroutine = new TimeoutNullCoroutine(j, timeUnit, normalizeContinuation);
            CoroutineContext.Element element = context.get(ContinuationInterceptor.Key);
            if (!(element instanceof Delay)) {
                element = null;
            }
            Delay delay = (Delay) element;
            if (delay != null) {
                JobKt.disposeOnCompletion(timeoutNullCoroutine, delay.invokeOnTimeout(j, timeUnit, timeoutNullCoroutine));
            } else {
                ScheduledFuture<?> schedule = getScheduledExecutor().schedule(timeoutNullCoroutine, j, timeUnit);
                Intrinsics.checkExpressionValueIsNotNull(schedule, "scheduledExecutor.schedule(coroutine, time, unit)");
                JobKt.cancelFutureOnCompletion(timeoutNullCoroutine, schedule);
            }
            timeoutNullCoroutine.initParentJob((Job) context.get(Job.Key));
            try {
                return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(timeoutNullCoroutine);
            } catch (TimeoutException unused) {
                return null;
            }
        }
    }
}
