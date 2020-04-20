package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B'\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0006\u0012\u0004\u0012\u00020\u00070\u0005j\u0002`\b¢\u0006\u0002\u0010\tJ\u0013\u0010\n\u001a\u00020\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006H\u0002J\b\u0010\f\u001a\u00020\rH\u0016R\"\u0010\u0004\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0006\u0012\u0004\u0012\u00020\u00070\u0005j\u0002`\b8\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/experimental/InvokeOnCompletion;", "Lkotlinx/coroutines/experimental/JobNode;", "Lkotlinx/coroutines/experimental/Job;", "job", "handler", "Lkotlin/Function1;", "", "", "Lkotlinx/coroutines/experimental/CompletionHandler;", "(Lkotlinx/coroutines/experimental/Job;Lkotlin/jvm/functions/Function1;)V", "invoke", "reason", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
final class InvokeOnCompletion extends JobNode<Job> {
    public final Function1<Throwable, Unit> handler;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public InvokeOnCompletion(Job job, Function1<? super Throwable, Unit> function1) {
        super(job);
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        this.handler = function1;
    }

    public void invoke(Throwable th) {
        this.handler.invoke(th);
    }

    public String toString() {
        return "InvokeOnCompletion[" + this.handler.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this.handler)) + "]";
    }
}
