package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00020\u0001B\u0019\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/experimental/RemoveOnCancel;", "Lkotlinx/coroutines/experimental/JobNode;", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "cont", "node", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "(Lkotlinx/coroutines/experimental/CancellableContinuation;Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)V", "getNode", "()Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "invoke", "", "reason", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CancellableContinuation.kt */
final class RemoveOnCancel extends JobNode<CancellableContinuation<?>> {
    private final LockFreeLinkedListNode node;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public RemoveOnCancel(CancellableContinuation<?> cancellableContinuation, LockFreeLinkedListNode lockFreeLinkedListNode) {
        super(cancellableContinuation);
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        this.node = lockFreeLinkedListNode;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public final LockFreeLinkedListNode getNode() {
        return this.node;
    }

    public void invoke(Throwable th) {
        if (((CancellableContinuation) this.job).isCancelled()) {
            this.node.remove();
        }
    }

    public String toString() {
        return "RemoveOnCancel[" + this.node + "]";
    }
}
