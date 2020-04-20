package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/experimental/Unconfined;", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "()V", "dispatch", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "block", "Ljava/lang/Runnable;", "isDispatchNeeded", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CoroutineContext.kt */
public final class Unconfined extends CoroutineDispatcher {
    public static final Unconfined INSTANCE = null;

    public boolean isDispatchNeeded(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return false;
    }

    public String toString() {
        return "Unconfined";
    }

    static {
        new Unconfined();
    }

    private Unconfined() {
        INSTANCE = this;
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        throw new UnsupportedOperationException();
    }
}
