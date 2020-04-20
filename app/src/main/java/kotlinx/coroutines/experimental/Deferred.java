package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J\u0011\u0010\n\u001a\u00028\u0000H¦@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\r\u0010\f\u001a\u00028\u0000H&¢\u0006\u0002\u0010\rJH\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0001\u0010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00100\u00122\"\u0010\u0013\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\u0015\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u0014H&ø\u0001\u0000¢\u0006\u0002\u0010\u0017R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0005R\u001a\u0010\u0007\u001a\u00020\u00048VX\u0004¢\u0006\f\u0012\u0004\b\b\u0010\t\u001a\u0004\b\u0007\u0010\u0005\u0002\u0004\n\u0002\b\t¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/experimental/Deferred;", "T", "Lkotlinx/coroutines/experimental/Job;", "isCancelled", "", "()Z", "isCompletedExceptionally", "isComputing", "isComputing$annotations", "()V", "await", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "getCompleted", "()Ljava/lang/Object;", "registerSelectAwait", "", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Deferred.kt */
public interface Deferred<T> extends Job {
    Object await(Continuation<? super T> continuation);

    T getCompleted();

    boolean isCancelled();

    boolean isCompletedExceptionally();

    boolean isComputing();

    <R> void registerSelectAwait(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2);

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Deferred.kt */
    public static final class DefaultImpls {
        @Deprecated(message = "Use `isActive`", replaceWith = @ReplaceWith(expression = "isActive", imports = {}))
        public static /* synthetic */ void isComputing$annotations() {
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static <T> Job plus(Deferred<? extends T> deferred, Job job) {
            Intrinsics.checkParameterIsNotNull(job, "other");
            return Job.DefaultImpls.plus(deferred, job);
        }

        public static <T> boolean isComputing(Deferred<? extends T> deferred) {
            return deferred.isActive();
        }
    }
}
