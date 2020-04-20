package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000,\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a=\u0010\u0004\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u001a\b\u0004\u0010\b\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\n\u0012\u0004\u0012\u00020\u000b0\tHHø\u0001\u0000¢\u0006\u0002\u0010\f\u001a\u0016\u0010\r\u001a\u00020\u000e*\u0006\u0012\u0002\b\u00030\n2\u0006\u0010\u000f\u001a\u00020\u0010\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\u0011"}, d2 = {"MODE_DIRECT", "", "MODE_DISPATCHED", "MODE_UNDISPATCHED", "suspendCancellableCoroutine", "T", "holdCancellability", "", "block", "Lkotlin/Function1;", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "", "(ZLkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "removeOnCancel", "Lkotlinx/coroutines/experimental/DisposableHandle;", "node", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: CancellableContinuation.kt */
public final class CancellableContinuationKt {
    public static final int MODE_DIRECT = 2;
    public static final int MODE_DISPATCHED = 0;
    public static final int MODE_UNDISPATCHED = 1;

    public static /* bridge */ /* synthetic */ Object suspendCancellableCoroutine$default(boolean z, Function1 function1, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        if (!z) {
            cancellableContinuationImpl.initCancellability();
        }
        function1.invoke(cancellableContinuationImpl);
        return cancellableContinuationImpl.getResult();
    }

    public static final <T> Object suspendCancellableCoroutine(boolean z, Function1<? super CancellableContinuation<? super T>, Unit> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        if (!z) {
            cancellableContinuationImpl.initCancellability();
        }
        function1.invoke(cancellableContinuationImpl);
        return cancellableContinuationImpl.getResult();
    }

    public static final DisposableHandle removeOnCancel(CancellableContinuation<?> cancellableContinuation, LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        return cancellableContinuation.invokeOnCompletion(new RemoveOnCancel(cancellableContinuation, lockFreeLinkedListNode));
    }
}
