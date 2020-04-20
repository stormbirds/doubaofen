package kotlinx.coroutines.experimental;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u000f\u001a\u00020\u0005H\u0016R\u0014\u0010\t\u001a\u00020\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/experimental/ThreadPoolDispatcher;", "Lkotlinx/coroutines/experimental/ExecutorCoroutineDispatcherBase;", "nThreads", "", "name", "", "job", "Lkotlinx/coroutines/experimental/Job;", "(ILjava/lang/String;Lkotlinx/coroutines/experimental/Job;)V", "executor", "Ljava/util/concurrent/ScheduledExecutorService;", "getExecutor", "()Ljava/util/concurrent/ScheduledExecutorService;", "threadNo", "Ljava/util/concurrent/atomic/AtomicInteger;", "toString", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: ThreadPoolDispatcher.kt */
public final class ThreadPoolDispatcher extends ExecutorCoroutineDispatcherBase {
    private final ScheduledExecutorService executor;
    /* access modifiers changed from: private */
    public final int nThreads;
    /* access modifiers changed from: private */
    public final String name;
    /* access modifiers changed from: private */
    public final AtomicInteger threadNo = new AtomicInteger();

    public ThreadPoolDispatcher(int i, String str, Job job) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Intrinsics.checkParameterIsNotNull(job, "job");
        this.nThreads = i;
        this.name = str;
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(this.nThreads, new ThreadPoolDispatcher$executor$1(this));
        Intrinsics.checkExpressionValueIsNotNull(newScheduledThreadPool, "Executors.newScheduledTh….incrementAndGet())\n    }");
        this.executor = newScheduledThreadPool;
        job.invokeOnCompletion(new Function1<Throwable, Unit>(this) {
            final /* synthetic */ ThreadPoolDispatcher this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((Throwable) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(Throwable th) {
                this.this$0.getExecutor().shutdown();
            }
        });
    }

    public ScheduledExecutorService getExecutor() {
        return this.executor;
    }

    public String toString() {
        return "ThreadPoolDispatcher[" + this.nThreads + ", " + this.name + "]";
    }
}
