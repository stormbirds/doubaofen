package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u001a\u001a\u0010\b\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¨\u0006\t"}, d2 = {"newFixedThreadPoolContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "nThreads", "", "name", "", "parent", "Lkotlinx/coroutines/experimental/Job;", "newSingleThreadContext", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: ThreadPoolDispatcher.kt */
public final class ThreadPoolDispatcherKt {
    public static /* bridge */ /* synthetic */ CoroutineContext newSingleThreadContext$default(String str, Job job, int i, Object obj) {
        if ((i & 2) != 0) {
            job = null;
        }
        return newSingleThreadContext(str, job);
    }

    public static final CoroutineContext newSingleThreadContext(String str, Job job) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        return newFixedThreadPoolContext(1, str, job);
    }

    public static /* bridge */ /* synthetic */ CoroutineContext newFixedThreadPoolContext$default(int i, String str, Job job, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            job = null;
        }
        return newFixedThreadPoolContext(i, str, job);
    }

    public static final CoroutineContext newFixedThreadPoolContext(int i, String str, Job job) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        boolean z = true;
        if (i < 1) {
            z = false;
        }
        if (z) {
            Job invoke = Job.Key.invoke(job);
            return invoke.plus(new ThreadPoolDispatcher(i, str, invoke));
        }
        throw new IllegalArgumentException(("Expected at least one thread, but " + i + " specified").toString());
    }
}
