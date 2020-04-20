package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007R\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/experimental/RunContinuationCoroutine;", "T", "Lkotlinx/coroutines/experimental/CancellableContinuationImpl;", "parentContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "continuation", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/coroutines/experimental/Continuation;)V", "getParentContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Builders.kt */
final class RunContinuationCoroutine<T> extends CancellableContinuationImpl<T> {
    private final CoroutineContext parentContext;

    /* access modifiers changed from: protected */
    public CoroutineContext getParentContext() {
        return this.parentContext;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public RunContinuationCoroutine(CoroutineContext coroutineContext, Continuation<? super T> continuation) {
        super(continuation, true);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        this.parentContext = coroutineContext;
    }
}
