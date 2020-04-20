package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0012\u0010\b\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\f\u0010\u000b\u001a\u00060\fj\u0002`\rH\u0016J\"\u0010\u000e\u001a\u00020\u000f2\u0018\u0010\u0010\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u00020\u00120\u0011j\u0002`\u0013H\u0016J\u0011\u0010\u0014\u001a\u00020\u0012H@ø\u0001\u0000¢\u0006\u0002\u0010\u0015JB\u0010\u0016\u001a\u00020\u0012\"\u0004\b\u0000\u0010\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00170\u00192\u001c\u0010\u001a\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00170\u001b\u0012\u0006\u0012\u0004\u0018\u00010\u001c0\u0011H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u001dJ\b\u0010\u001e\u001a\u00020\u0005H\u0016R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u00058VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0006\u0002\u0004\n\u0002\b\t¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/experimental/NonCancellable;", "Lkotlin/coroutines/experimental/AbstractCoroutineContextElement;", "Lkotlinx/coroutines/experimental/Job;", "()V", "isActive", "", "()Z", "isCompleted", "cancel", "cause", "", "getCompletionException", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/experimental/CancellationException;", "invokeOnCompletion", "Lkotlinx/coroutines/experimental/DisposableHandle;", "handler", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/experimental/CompletionHandler;", "join", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "registerSelectJoin", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "start", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: NonCancellable.kt */
public final class NonCancellable extends AbstractCoroutineContextElement implements Job {
    public static final NonCancellable INSTANCE = null;

    public boolean cancel(Throwable th) {
        return false;
    }

    public boolean isActive() {
        return true;
    }

    public boolean isCompleted() {
        return false;
    }

    public boolean start() {
        return false;
    }

    static {
        new NonCancellable();
    }

    private NonCancellable() {
        super(Job.Key);
        INSTANCE = this;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    public Job plus(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "other");
        return Job.DefaultImpls.plus(this, job);
    }

    public Object join(Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        throw new UnsupportedOperationException("This job is always active");
    }

    public <R> void registerSelectJoin(SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        throw new UnsupportedOperationException("This job is always active");
    }

    public CancellationException getCompletionException() {
        throw new IllegalStateException("This job is always active");
    }

    public DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return NonDisposableHandle.INSTANCE;
    }
}
