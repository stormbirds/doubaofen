package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Job;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\bH&J#\u0010\f\u001a\u0004\u0018\u00010\n2\u0006\u0010\r\u001a\u00028\u00002\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nH&¢\u0006\u0002\u0010\u000fJ\u0012\u0010\u0010\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0011\u001a\u00020\u0012H&J\u0019\u0010\u0013\u001a\u00020\b*\u00020\u00142\u0006\u0010\r\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u0015J\u0014\u0010\u0016\u001a\u00020\b*\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0012H&R\u0012\u0010\u0004\u001a\u00020\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0006¨\u0006\u0017"}, d2 = {"Lkotlinx/coroutines/experimental/CancellableContinuation;", "T", "Lkotlin/coroutines/experimental/Continuation;", "Lkotlinx/coroutines/experimental/Job;", "isCancelled", "", "()Z", "completeResume", "", "token", "", "initCancellability", "tryResume", "value", "idempotent", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "tryResumeWithException", "exception", "", "resumeUndispatched", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "(Lkotlinx/coroutines/experimental/CoroutineDispatcher;Ljava/lang/Object;)V", "resumeUndispatchedWithException", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CancellableContinuation.kt */
public interface CancellableContinuation<T> extends Continuation<T>, Job {
    void completeResume(Object obj);

    void initCancellability();

    boolean isCancelled();

    void resumeUndispatched(CoroutineDispatcher coroutineDispatcher, T t);

    void resumeUndispatchedWithException(CoroutineDispatcher coroutineDispatcher, Throwable th);

    Object tryResume(T t, Object obj);

    Object tryResumeWithException(Throwable th);

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: CancellableContinuation.kt */
    public static final class DefaultImpls {
        @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static <T> Job plus(CancellableContinuation<? super T> cancellableContinuation, Job job) {
            Intrinsics.checkParameterIsNotNull(job, "other");
            return Job.DefaultImpls.plus(cancellableContinuation, job);
        }

        public static /* bridge */ /* synthetic */ Object tryResume$default(CancellableContinuation cancellableContinuation, Object obj, Object obj2, int i, Object obj3) {
            if (obj3 == null) {
                if ((i & 2) != 0) {
                    obj2 = null;
                }
                return cancellableContinuation.tryResume(obj, obj2);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: tryResume");
        }
    }
}
