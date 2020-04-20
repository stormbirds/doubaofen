package kotlinx.coroutines.experimental.selects;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a8\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001f\b\u0004\u0010\u0002\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\b\u0006HHø\u0001\u0000¢\u0006\u0002\u0010\u0007\u0002\u0004\n\u0002\b\t¨\u0006\b"}, d2 = {"selectUnbiased", "R", "builder", "Lkotlin/Function1;", "Lkotlinx/coroutines/experimental/selects/SelectBuilder;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: SelectUnbiased.kt */
public final class SelectUnbiasedKt {
    public static final <R> Object selectUnbiased(Function1<? super SelectBuilder<? super R>, Unit> function1, Continuation<? super R> continuation) {
        Intrinsics.checkParameterIsNotNull(function1, "builder");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        UnbiasedSelectBuilderImpl unbiasedSelectBuilderImpl = new UnbiasedSelectBuilderImpl(CoroutineIntrinsics.normalizeContinuation(continuation));
        try {
            function1.invoke(unbiasedSelectBuilderImpl);
        } catch (Throwable th) {
            unbiasedSelectBuilderImpl.handleBuilderException(th);
        }
        return unbiasedSelectBuilderImpl.initSelectResult();
    }
}
