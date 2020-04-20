package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/experimental/PoolThread;", "Ljava/lang/Thread;", "dispatcher", "Lkotlinx/coroutines/experimental/ThreadPoolDispatcher;", "target", "Ljava/lang/Runnable;", "name", "", "(Lkotlinx/coroutines/experimental/ThreadPoolDispatcher;Ljava/lang/Runnable;Ljava/lang/String;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: ThreadPoolDispatcher.kt */
public final class PoolThread extends Thread {
    public final ThreadPoolDispatcher dispatcher;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PoolThread(ThreadPoolDispatcher threadPoolDispatcher, Runnable runnable, String str) {
        super(runnable, str);
        Intrinsics.checkParameterIsNotNull(threadPoolDispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(runnable, "target");
        Intrinsics.checkParameterIsNotNull(str, "name");
        this.dispatcher = threadPoolDispatcher;
        setDaemon(true);
    }
}
