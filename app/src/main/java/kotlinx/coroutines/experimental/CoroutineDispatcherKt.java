package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\u001a%\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0000¢\u0006\u0002\u0010\u0005\u001a \u0010\u0006\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0000¨\u0006\t"}, d2 = {"resumeDirect", "", "T", "Lkotlin/coroutines/experimental/Continuation;", "value", "(Lkotlin/coroutines/experimental/Continuation;Ljava/lang/Object;)V", "resumeDirectWithException", "exception", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: CoroutineDispatcher.kt */
public final class CoroutineDispatcherKt {
    public static final <T> void resumeDirect(Continuation<? super T> continuation, T t) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        if (continuation instanceof DispatchedContinuation) {
            ((DispatchedContinuation) continuation).continuation.resume(t);
        } else {
            continuation.resume(t);
        }
    }

    public static final <T> void resumeDirectWithException(Continuation<? super T> continuation, Throwable th) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        if (continuation instanceof DispatchedContinuation) {
            ((DispatchedContinuation) continuation).continuation.resumeWithException(th);
        } else {
            continuation.resumeWithException(th);
        }
    }
}
