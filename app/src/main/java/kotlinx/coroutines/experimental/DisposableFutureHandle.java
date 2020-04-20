package kotlinx.coroutines.experimental;

import java.util.concurrent.Future;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.DisposableHandle;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0012\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/experimental/DisposableFutureHandle;", "Lkotlinx/coroutines/experimental/DisposableHandle;", "future", "Ljava/util/concurrent/Future;", "(Ljava/util/concurrent/Future;)V", "dispose", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Delay.kt */
public final class DisposableFutureHandle implements DisposableHandle {
    private final Future<?> future;

    public DisposableFutureHandle(Future<?> future2) {
        Intrinsics.checkParameterIsNotNull(future2, "future");
        this.future = future2;
    }

    @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
    public void unregister() {
        DisposableHandle.DefaultImpls.unregister(this);
    }

    public void dispose() {
        this.future.cancel(false);
    }

    public String toString() {
        return "DisposableFutureHandle[" + this.future + "]";
    }
}
