package kotlinx.coroutines.experimental;

import java.util.concurrent.Executor;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\f\u0010\u0003\u001a\u00020\u0001*\u00020\u0002H\u0007¨\u0006\u0004"}, d2 = {"asCoroutineDispatcher", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "Ljava/util/concurrent/Executor;", "toCoroutineDispatcher", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Executors.kt */
public final class ExecutorsKt {
    @Deprecated(message = "Renamed to `asCoroutineDispatcher`", replaceWith = @ReplaceWith(expression = "asCoroutineDispatcher()", imports = {}))
    public static final CoroutineDispatcher toCoroutineDispatcher(Executor executor) {
        Intrinsics.checkParameterIsNotNull(executor, "$receiver");
        return new ExecutorCoroutineDispatcher(executor);
    }

    public static final CoroutineDispatcher asCoroutineDispatcher(Executor executor) {
        Intrinsics.checkParameterIsNotNull(executor, "$receiver");
        return new ExecutorCoroutineDispatcher(executor);
    }
}
