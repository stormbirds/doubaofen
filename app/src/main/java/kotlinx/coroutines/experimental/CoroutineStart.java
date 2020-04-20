package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\\\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\b\"\u0004\b\u0001\u0010\t2'\u0010\n\u001a#\b\u0001\u0012\u0004\u0012\u0002H\b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\f\u0012\u0006\u0012\u0004\u0018\u00010\r0\u000b¢\u0006\u0002\b\u000e2\u0006\u0010\u000f\u001a\u0002H\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\t0\fH\u0002ø\u0001\u0000¢\u0006\u0002\u0010\u0011R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014\u0002\u0004\n\u0002\b\t¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/experimental/CoroutineStart;", "", "(Ljava/lang/String;I)V", "isLazy", "", "()Z", "invoke", "", "R", "T", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "receiver", "completion", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)V", "DEFAULT", "LAZY", "UNDISPATCHED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CoroutineStart.kt */
public enum CoroutineStart {
    DEFAULT,
    LAZY,
    UNDISPATCHED;

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            $EnumSwitchMapping$0 = new int[CoroutineStart.values().length];
            $EnumSwitchMapping$0[CoroutineStart.DEFAULT.ordinal()] = 1;
            $EnumSwitchMapping$0[CoroutineStart.UNDISPATCHED.ordinal()] = 2;
            $EnumSwitchMapping$0[CoroutineStart.LAZY.ordinal()] = 3;
        }
    }

    public final <R, T> void invoke(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        int i = WhenMappings.$EnumSwitchMapping$0[ordinal()];
        if (i == 1) {
            CoroutinesKt.startCoroutine(function2, r, continuation);
        } else if (i == 2) {
            UndispatchedKt.startCoroutineUndispatched(function2, r, continuation);
        } else if (i != 3) {
            throw new NoWhenBranchMatchedException();
        }
    }

    public final boolean isLazy() {
        return this == LAZY;
    }
}
