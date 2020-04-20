package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CancellableContinuation;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u001d\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0004H\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\u00042\b\u0010\u0010\u001a\u0004\u0018\u00010\u0004H\u0016R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/experimental/channels/SendElement;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/channels/Send;", "pollResult", "", "cont", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/CancellableContinuation;)V", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "token", "toString", "", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: AbstractChannel.kt */
public final class SendElement extends LockFreeLinkedListNode implements Send {
    public final CancellableContinuation<Unit> cont;
    private final Object pollResult;

    public Object getPollResult() {
        return this.pollResult;
    }

    public SendElement(Object obj, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
        this.pollResult = obj;
        this.cont = cancellableContinuation;
    }

    public Object tryResumeSend(Object obj) {
        return this.cont.tryResume(Unit.INSTANCE, obj);
    }

    public void completeResumeSend(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "token");
        this.cont.completeResume(obj);
    }

    public String toString() {
        return "SendElement(" + getPollResult() + ")[" + this.cont + "]";
    }
}
