package kotlinx.coroutines.experimental;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\u0004\u0018\u0001H\b\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\nH\b¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\u0004H\u0002J\b\u0010\r\u001a\u00020\u0004H\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0004H\u0003J\u0015\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0018H\u0001¢\u0006\u0002\b\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\r\u0010\u0005\u001a\u00020\u0011H\u0001¢\u0006\u0002\b\u001cR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/experimental/CommonPool;", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "()V", "_pool", "Ljava/util/concurrent/ExecutorService;", "usePrivatePool", "", "Try", "T", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "createPlainPool", "createPool", "defaultParallelism", "", "dispatch", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "Ljava/lang/Runnable;", "getOrCreatePoolSync", "shutdownAndRelease", "timeout", "", "shutdownAndRelease$kotlinx_coroutines_core", "toString", "", "usePrivatePool$kotlinx_coroutines_core", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CommonPool.kt */
public final class CommonPool extends CoroutineDispatcher {
    public static final CommonPool INSTANCE = null;
    private static volatile ExecutorService _pool;
    private static boolean usePrivatePool;

    public String toString() {
        return "CommonPool";
    }

    static {
        new CommonPool();
    }

    private CommonPool() {
        INSTANCE = this;
    }

    private final <T> T Try(Function0<? extends T> function0) {
        try {
            return function0.invoke();
        } catch (Throwable unused) {
            return null;
        }
    }

    private final ExecutorService createPool() {
        Class<?> cls;
        ExecutorService executorService;
        ExecutorService executorService2 = null;
        try {
            cls = Class.forName("java.util.concurrent.ForkJoinPool");
        } catch (Throwable unused) {
            cls = null;
        }
        if (cls == null) {
            return createPlainPool();
        }
        if (!usePrivatePool) {
            try {
                Method method = cls.getMethod("commonPool", new Class[0]);
                Object invoke = method != null ? method.invoke((Object) null, new Object[0]) : null;
                if (!(invoke instanceof ExecutorService)) {
                    invoke = null;
                }
                executorService = (ExecutorService) invoke;
            } catch (Throwable unused2) {
                executorService = null;
            }
            if (executorService != null) {
                return executorService;
            }
        }
        try {
            Object newInstance = cls.getConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(INSTANCE.defaultParallelism())});
            if (!(newInstance instanceof ExecutorService)) {
                newInstance = null;
            }
            executorService2 = (ExecutorService) newInstance;
        } catch (Throwable unused3) {
        }
        if (executorService2 != null) {
            return executorService2;
        }
        return createPlainPool();
    }

    private final ExecutorService createPlainPool() {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(defaultParallelism(), new CommonPool$createPlainPool$1(new AtomicInteger()));
        Intrinsics.checkExpressionValueIsNotNull(newFixedThreadPool, "Executors.newFixedThread…Daemon = true }\n        }");
        return newFixedThreadPool;
    }

    private final int defaultParallelism() {
        return RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
    }

    private final synchronized ExecutorService getOrCreatePoolSync() {
        ExecutorService executorService;
        executorService = _pool;
        if (executorService == null) {
            executorService = createPool();
            _pool = executorService;
        }
        return executorService;
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        ExecutorService executorService = _pool;
        if (executorService == null) {
            executorService = getOrCreatePoolSync();
        }
        executorService.execute(runnable);
    }

    public final synchronized void usePrivatePool$kotlinx_coroutines_core() {
        shutdownAndRelease$kotlinx_coroutines_core(0);
        usePrivatePool = true;
    }

    public final synchronized void shutdownAndRelease$kotlinx_coroutines_core(long j) {
        ExecutorService executorService = _pool;
        if (executorService != null) {
            executorService.shutdown();
            if (j > ((long) 0)) {
                executorService.awaitTermination(j, TimeUnit.MILLISECONDS);
            }
            _pool = null;
        }
        usePrivatePool = false;
    }
}
