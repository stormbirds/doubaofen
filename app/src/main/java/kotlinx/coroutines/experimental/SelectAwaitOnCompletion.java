package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00040\u0003BH\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012\"\u0010\b\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\tø\u0001\u0000¢\u0006\u0002\u0010\fJ\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016R/\u0010\b\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\tX\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\rR\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/experimental/SelectAwaitOnCompletion;", "T", "R", "Lkotlinx/coroutines/experimental/JobNode;", "Lkotlinx/coroutines/experimental/DeferredCoroutine;", "deferred", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/DeferredCoroutine;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "invoke", "", "reason", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Deferred.kt */
final class SelectAwaitOnCompletion<T, R> extends JobNode<DeferredCoroutine<T>> {
    private final Function2<T, Continuation<? super R>, Object> block;
    private final SelectInstance<R> select;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SelectAwaitOnCompletion(DeferredCoroutine<T> deferredCoroutine, SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        super(deferredCoroutine);
        Intrinsics.checkParameterIsNotNull(deferredCoroutine, "deferred");
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.select = selectInstance;
        this.block = function2;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public void invoke(Throwable th) {
        DeferredCoroutine.selectAwaitCompletion$kotlinx_coroutines_core$default((DeferredCoroutine) this.job, this.select, this.block, (Object) null, 4, (Object) null);
    }

    public String toString() {
        return "SelectAwaitOnCompletion[" + this.select + "]";
    }
}
