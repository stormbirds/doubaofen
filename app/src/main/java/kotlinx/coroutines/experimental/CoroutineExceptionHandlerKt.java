package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\u001a\u0016\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005¨\u0006\u0006"}, d2 = {"handleCoroutineException", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "exception", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: CoroutineExceptionHandler.kt */
public final class CoroutineExceptionHandlerKt {
    public static final void handleCoroutineException(CoroutineContext coroutineContext, Throwable th) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        CoroutineExceptionHandler coroutineExceptionHandler = (CoroutineExceptionHandler) coroutineContext.get(CoroutineExceptionHandler.Key);
        if (coroutineExceptionHandler != null) {
            coroutineExceptionHandler.handleException(coroutineContext, th);
        } else if (!(th instanceof CancellationException)) {
            Job job = (Job) coroutineContext.get(Job.Key);
            if (!(job != null ? job.cancel(th) : false)) {
                Thread currentThread = Thread.currentThread();
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
            }
        }
    }
}
