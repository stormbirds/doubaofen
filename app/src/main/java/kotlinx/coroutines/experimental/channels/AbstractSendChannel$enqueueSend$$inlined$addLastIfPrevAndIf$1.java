package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\n\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0016¨\u0006\u0005"}, d2 = {"kotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$makeCondAddOp$1", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$CondAddOp;", "(Lkotlin/jvm/functions/Function0;Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)V", "prepare", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: LockFreeLinkedList.kt */
public final class AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1 extends LockFreeLinkedListNode.CondAddOp {
    final /* synthetic */ LockFreeLinkedListNode $node;
    final /* synthetic */ AbstractSendChannel this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2, AbstractSendChannel abstractSendChannel) {
        super(lockFreeLinkedListNode2);
        this.$node = lockFreeLinkedListNode;
        this.this$0 = abstractSendChannel;
    }

    public Object prepare() {
        if (this.this$0.isBufferFull()) {
            return null;
        }
        return LockFreeLinkedListKt.getCONDITION_FALSE();
    }
}
