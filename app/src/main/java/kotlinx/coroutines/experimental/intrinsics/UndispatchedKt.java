package kotlinx.coroutines.experimental.intrinsics;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a>\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001aR\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\b\"\u0004\b\u0001\u0010\u0002*\u001e\b\u0001\u0012\u0004\u0012\u0002H\b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\t2\u0006\u0010\n\u001a\u0002H\b2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u0002\u0004\n\u0002\b\t¨\u0006\f"}, d2 = {"startCoroutineUndispatched", "", "R", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "", "completion", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)V", "E", "Lkotlin/Function2;", "element", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)V", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Undispatched.kt */
public final class UndispatchedKt {
    public static final <R> void startCoroutineUndispatched(Function1<? super Continuation<? super R>, ? extends Object> function1, Continuation<? super R> continuation) {
        Intrinsics.checkParameterIsNotNull(function1, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        try {
            Object invoke = ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(continuation);
            if (invoke != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                continuation.resume(invoke);
            }
        } catch (Throwable th) {
            continuation.resumeWithException(th);
        }
    }

    public static final <E, R> void startCoroutineUndispatched(Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2, E e, Continuation<? super R> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        try {
            Object invoke = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(e, continuation);
            if (invoke != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                continuation.resume(invoke);
            }
        } catch (Throwable th) {
            continuation.resumeWithException(th);
        }
    }
}
