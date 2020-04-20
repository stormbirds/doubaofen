package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0006J!\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00028\u0000H\u0000¢\u0006\u0004\b\u0010\u0010\u0011J\u0015\u0010\u0012\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00028\u0000H\b¢\u0006\u0002\u0010\u0013J\u0011\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\bJ\u0010\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0016R\u0012\u0010\u0007\u001a\u00020\bX\u0005¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/experimental/DispatchedContinuation;", "T", "Lkotlin/coroutines/experimental/Continuation;", "dispatcher", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "continuation", "(Lkotlinx/coroutines/experimental/CoroutineDispatcher;Lkotlin/coroutines/experimental/Continuation;)V", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "dispatchYield", "", "job", "Lkotlinx/coroutines/experimental/Job;", "value", "dispatchYield$kotlinx_coroutines_core", "(Lkotlinx/coroutines/experimental/Job;Ljava/lang/Object;)V", "resume", "(Ljava/lang/Object;)V", "resumeUndispatched", "resumeUndispatchedWithException", "exception", "", "resumeWithException", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CoroutineDispatcher.kt */
public final class DispatchedContinuation<T> implements Continuation<T> {
    public final Continuation<T> continuation;
    public final CoroutineDispatcher dispatcher;

    public CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    public DispatchedContinuation(CoroutineDispatcher coroutineDispatcher, Continuation<? super T> continuation2) {
        Intrinsics.checkParameterIsNotNull(coroutineDispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        this.dispatcher = coroutineDispatcher;
        this.continuation = continuation2;
    }

    public void resume(T t) {
        CoroutineContext context = this.continuation.getContext();
        if (this.dispatcher.isDispatchNeeded(context)) {
            this.dispatcher.dispatch(context, new DispatchedContinuation$resume$1(this, t));
            return;
        }
        String updateContext = CoroutineContextKt.updateContext(getContext());
        try {
            this.continuation.resume(t);
            Unit unit = Unit.INSTANCE;
        } finally {
            CoroutineContextKt.restoreContext(updateContext);
        }
    }

    public final void resumeUndispatched(T t) {
        String updateContext = CoroutineContextKt.updateContext(getContext());
        try {
            this.continuation.resume(t);
            Unit unit = Unit.INSTANCE;
        } finally {
            InlineMarker.finallyStart(1);
            CoroutineContextKt.restoreContext(updateContext);
            InlineMarker.finallyEnd(1);
        }
    }

    public void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        CoroutineContext context = this.continuation.getContext();
        if (this.dispatcher.isDispatchNeeded(context)) {
            this.dispatcher.dispatch(context, new DispatchedContinuation$resumeWithException$1(this, th));
            return;
        }
        String updateContext = CoroutineContextKt.updateContext(getContext());
        try {
            this.continuation.resumeWithException(th);
            Unit unit = Unit.INSTANCE;
        } finally {
            CoroutineContextKt.restoreContext(updateContext);
        }
    }

    public final void resumeUndispatchedWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        String updateContext = CoroutineContextKt.updateContext(getContext());
        try {
            this.continuation.resumeWithException(th);
            Unit unit = Unit.INSTANCE;
        } finally {
            InlineMarker.finallyStart(1);
            CoroutineContextKt.restoreContext(updateContext);
            InlineMarker.finallyEnd(1);
        }
    }

    public final void dispatchYield$kotlinx_coroutines_core(Job job, T t) {
        CoroutineContext context = this.continuation.getContext();
        this.dispatcher.dispatch(context, new DispatchedContinuation$dispatchYield$1(this, context, job, t));
    }
}
