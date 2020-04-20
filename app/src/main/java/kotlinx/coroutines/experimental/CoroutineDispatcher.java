package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\"\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\f0\u000b\"\u0004\b\u0000\u0010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\f0\u000bH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0011\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0000H\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "Lkotlin/coroutines/experimental/AbstractCoroutineContextElement;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "()V", "dispatch", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "block", "Ljava/lang/Runnable;", "interceptContinuation", "Lkotlin/coroutines/experimental/Continuation;", "T", "continuation", "isDispatchNeeded", "", "plus", "other", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CoroutineDispatcher.kt */
public abstract class CoroutineDispatcher extends AbstractCoroutineContextElement implements ContinuationInterceptor {
    public abstract void dispatch(CoroutineContext coroutineContext, Runnable runnable);

    public boolean isDispatchNeeded(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return true;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two CoroutineDispatcher objects is meaningless. CoroutineDispatcher is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The dispatcher to the right of `+` just replaces the dispatcher the left of `+`.")
    public final CoroutineDispatcher plus(CoroutineDispatcher coroutineDispatcher) {
        Intrinsics.checkParameterIsNotNull(coroutineDispatcher, "other");
        return coroutineDispatcher;
    }

    public CoroutineDispatcher() {
        super(ContinuationInterceptor.Key);
    }

    public <T> Continuation<T> interceptContinuation(Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return new DispatchedContinuation<>(this, continuation);
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this));
    }
}
