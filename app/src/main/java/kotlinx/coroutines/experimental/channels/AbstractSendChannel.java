package kotlinx.coroutines.experimental.channels;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CancellableContinuation;
import kotlinx.coroutines.experimental.CancellableContinuationImpl;
import kotlinx.coroutines.experimental.CancellableContinuationKt;
import kotlinx.coroutines.experimental.DisposableHandle;
import kotlinx.coroutines.experimental.JobKt;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0006<=>?@AB\u0005¢\u0006\u0002\u0010\u0003J\u0012\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0014J\u0012\u0010\u0018\u001a\u00020\u000b2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J!\u0010\u0019\u001a\u000e\u0012\u0002\b\u00030\u001aj\u0006\u0012\u0002\b\u0003`\u001b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u001dJ!\u0010\u001e\u001a\u000e\u0012\u0002\b\u00030\u001aj\u0006\u0012\u0002\b\u0003`\u001b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u001dJ\u001b\u0010\u001f\u001a\b\u0012\u0004\u0012\u00028\u00000 2\u0006\u0010\u001c\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010!J\u0010\u0010\"\u001a\u00020\u000b2\u0006\u0010#\u001a\u00020$H\u0002J\u0013\u0010%\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00028\u0000¢\u0006\u0002\u0010&J\u0015\u0010'\u001a\u00020(2\u0006\u0010\u001c\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010)J!\u0010*\u001a\u00020(2\u0006\u0010\u001c\u001a\u00028\u00002\n\u0010+\u001a\u0006\u0012\u0002\b\u00030,H\u0014¢\u0006\u0002\u0010-JJ\u0010.\u001a\u00020\u0015\"\u0004\b\u0001\u0010/2\f\u0010+\u001a\b\u0012\u0004\u0012\u0002H/0,2\u0006\u0010\u001c\u001a\u00028\u00002\u001c\u00100\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H/02\u0012\u0006\u0012\u0004\u0018\u00010(01H\u0016ø\u0001\u0000¢\u0006\u0002\u00103J\u0019\u0010#\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u00104J\u0015\u00105\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010&J\u0015\u00106\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010&J\u0019\u00107\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u00104J\u0010\u00108\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u000109H\u0004J\n\u0010:\u001a\u0004\u0018\u00010;H\u0004R\u001a\u0010\u0004\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00058DX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00058DX\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u0007R\u0012\u0010\n\u001a\u00020\u000bX¤\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\fR\u0012\u0010\r\u001a\u00020\u000bX¤\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0011\u0010\u000e\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\fR\u0014\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u0002\u0004\n\u0002\b\t¨\u0006B"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel;", "E", "Lkotlinx/coroutines/experimental/channels/SendChannel;", "()V", "closedForReceive", "Lkotlinx/coroutines/experimental/channels/Closed;", "getClosedForReceive", "()Lkotlinx/coroutines/experimental/channels/Closed;", "closedForSend", "getClosedForSend", "isBufferAlwaysFull", "", "()Z", "isBufferFull", "isClosedForSend", "isFull", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "getQueue", "()Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "afterClose", "", "cause", "", "close", "describeSendBuffered", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/experimental/internal/AddLastDesc;", "element", "(Ljava/lang/Object;)Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "describeSendConflated", "describeTryOffer", "Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$TryOfferDesc;", "(Ljava/lang/Object;)Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$TryOfferDesc;", "enqueueSend", "send", "Lkotlinx/coroutines/experimental/channels/SendElement;", "offer", "(Ljava/lang/Object;)Z", "offerInternal", "", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/selects/SelectInstance;)Ljava/lang/Object;", "registerSelectSend", "R", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "sendBuffered", "sendConflated", "sendSuspend", "takeFirstReceiveOrPeekClosed", "Lkotlinx/coroutines/experimental/channels/ReceiveOrClosed;", "takeFirstSendOrPeekClosed", "Lkotlinx/coroutines/experimental/channels/Send;", "SendBuffered", "SendBufferedDesc", "SendConflatedDesc", "SendSelect", "TryEnqueueSendDesc", "TryOfferDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: AbstractChannel.kt */
public abstract class AbstractSendChannel<E> implements SendChannel<E> {
    private final LockFreeLinkedListHead queue = new LockFreeLinkedListHead();

    /* access modifiers changed from: protected */
    public void afterClose(Throwable th) {
    }

    /* access modifiers changed from: protected */
    public abstract boolean isBufferAlwaysFull();

    /* access modifiers changed from: protected */
    public abstract boolean isBufferFull();

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListHead getQueue() {
        return this.queue;
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E e) {
        ReceiveOrClosed takeFirstReceiveOrPeekClosed;
        Object tryResumeReceive;
        do {
            takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
            if (takeFirstReceiveOrPeekClosed == null) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            tryResumeReceive = takeFirstReceiveOrPeekClosed.tryResumeReceive(e, (Object) null);
        } while (tryResumeReceive == null);
        takeFirstReceiveOrPeekClosed.completeResumeReceive(tryResumeReceive);
        return takeFirstReceiveOrPeekClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        TryOfferDesc describeTryOffer = describeTryOffer(e);
        Object performAtomicTrySelect = selectInstance.performAtomicTrySelect(describeTryOffer);
        if (performAtomicTrySelect != null) {
            return performAtomicTrySelect;
        }
        ReceiveOrClosed receiveOrClosed = (ReceiveOrClosed) describeTryOffer.getResult();
        Object obj = describeTryOffer.resumeToken;
        if (obj == null) {
            Intrinsics.throwNpe();
        }
        receiveOrClosed.completeResumeReceive(obj);
        return receiveOrClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForSend() {
        Object prev = this.queue.getPrev();
        if (!(prev instanceof Closed)) {
            prev = null;
        }
        return (Closed) prev;
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForReceive() {
        Object next = this.queue.getNext();
        if (!(next instanceof Closed)) {
            next = null;
        }
        return (Closed) next;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        r3 = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlinx.coroutines.experimental.channels.Send takeFirstSendOrPeekClosed() {
        /*
            r4 = this;
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead r0 = r4.queue
        L_0x0002:
            java.lang.Object r1 = r0.getNext()
            if (r1 == 0) goto L_0x002c
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r1 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r1
            r2 = r0
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r2 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r2
            r3 = 0
            if (r1 != r2) goto L_0x0011
            goto L_0x0025
        L_0x0011:
            boolean r2 = r1 instanceof kotlinx.coroutines.experimental.channels.Send
            if (r2 != 0) goto L_0x0016
            goto L_0x0025
        L_0x0016:
            r2 = r1
            kotlinx.coroutines.experimental.channels.Send r2 = (kotlinx.coroutines.experimental.channels.Send) r2
            boolean r2 = r2 instanceof kotlinx.coroutines.experimental.channels.Closed
            if (r2 == 0) goto L_0x001e
            goto L_0x0024
        L_0x001e:
            boolean r2 = r1.remove()
            if (r2 == 0) goto L_0x0028
        L_0x0024:
            r3 = r1
        L_0x0025:
            kotlinx.coroutines.experimental.channels.Send r3 = (kotlinx.coroutines.experimental.channels.Send) r3
            return r3
        L_0x0028:
            r1.helpDelete()
            goto L_0x0002
        L_0x002c:
            kotlin.TypeCastException r0 = new kotlin.TypeCastException
            java.lang.String r1 = "null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.AbstractSendChannel.takeFirstSendOrPeekClosed():kotlinx.coroutines.experimental.channels.Send");
    }

    /* access modifiers changed from: protected */
    public final boolean sendBuffered(E e) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        LockFreeLinkedListNode sendBuffered = new SendBuffered(e);
        do {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (!(!(lockFreeLinkedListNode instanceof ReceiveOrClosed))) {
                    return false;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!lockFreeLinkedListNode.addNext(sendBuffered, lockFreeLinkedListHead));
        return true;
    }

    /* access modifiers changed from: protected */
    public final boolean sendConflated(E e) {
        boolean z;
        SendBuffered sendBuffered = new SendBuffered(e);
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (!(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                    if (lockFreeLinkedListNode.addNext(sendBuffered, lockFreeLinkedListHead)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
        if (!z) {
            return false;
        }
        Object prev2 = sendBuffered.getPrev();
        if (prev2 instanceof SendBuffered) {
            ((SendBuffered) prev2).remove();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListNode.AddLastDesc<?> describeSendBuffered(E e) {
        return new SendBufferedDesc<>(this.queue, e);
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0012\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00028\u0001¢\u0006\u0002\u0010\bJ\u001a\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nH\u0014¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendBufferedDesc;", "E", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendBuffered;", "Lkotlinx/coroutines/experimental/internal/AddLastDesc;", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "failure", "", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static class SendBufferedDesc<E> extends LockFreeLinkedListNode.AddLastDesc<SendBuffered<? extends E>> {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public SendBufferedDesc(LockFreeLinkedListHead lockFreeLinkedListHead, E e) {
            super(lockFreeLinkedListHead, new SendBuffered(e));
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListNode.AddLastDesc<?> describeSendConflated(E e) {
        return new SendConflatedDesc<>(this.queue, e);
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0001¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0014¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendConflatedDesc;", "E", "Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendBufferedDesc;", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "finishOnSuccess", "", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class SendConflatedDesc<E> extends SendBufferedDesc<E> {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public SendConflatedDesc(LockFreeLinkedListHead lockFreeLinkedListHead, E e) {
            super(lockFreeLinkedListHead, e);
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            super.finishOnSuccess(lockFreeLinkedListNode, lockFreeLinkedListNode2);
            if (lockFreeLinkedListNode instanceof SendBuffered) {
                lockFreeLinkedListNode.remove();
            }
        }
    }

    public final boolean isClosedForSend() {
        return getClosedForSend() != null;
    }

    public final boolean isFull() {
        return !(this.queue.getNext() instanceof ReceiveOrClosed) && isBufferFull();
    }

    public final Object send(E e, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        if (offer(e)) {
            return Unit.INSTANCE;
        }
        return sendSuspend(e, continuation);
    }

    public final boolean offer(E e) {
        Object offerInternal = offerInternal(e);
        if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
            return true;
        }
        if (offerInternal == AbstractChannelKt.OFFER_FAILED) {
            return false;
        }
        if (offerInternal instanceof Closed) {
            throw ((Closed) offerInternal).getSendException();
        }
        throw new IllegalStateException(("offerInternal returned " + offerInternal).toString());
    }

    /* access modifiers changed from: private */
    public final boolean enqueueSend(SendElement sendElement) {
        int tryCondAddNext;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        if (isBufferAlwaysFull()) {
            LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
            do {
                Object prev = lockFreeLinkedListHead.getPrev();
                if (prev != null) {
                    lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                    if (!(!(lockFreeLinkedListNode instanceof ReceiveOrClosed))) {
                        return false;
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                }
            } while (!lockFreeLinkedListNode.addNext(sendElement, lockFreeLinkedListHead));
        } else {
            LockFreeLinkedListHead lockFreeLinkedListHead2 = this.queue;
            LockFreeLinkedListNode lockFreeLinkedListNode2 = sendElement;
            LockFreeLinkedListNode.CondAddOp abstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1 = new AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1(lockFreeLinkedListNode2, lockFreeLinkedListNode2, this);
            do {
                Object prev2 = lockFreeLinkedListHead2.getPrev();
                if (prev2 != null) {
                    LockFreeLinkedListNode lockFreeLinkedListNode3 = (LockFreeLinkedListNode) prev2;
                    if (!(!(lockFreeLinkedListNode3 instanceof ReceiveOrClosed))) {
                        return false;
                    }
                    tryCondAddNext = lockFreeLinkedListNode3.tryCondAddNext(lockFreeLinkedListNode2, lockFreeLinkedListHead2, abstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1);
                    if (tryCondAddNext != 1) {
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                }
            } while (tryCondAddNext != 2);
            return false;
        }
        return true;
    }

    public boolean close(Throwable th) {
        Closed closed = new Closed(th);
        while (true) {
            ReceiveOrClosed takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
            boolean z = false;
            if (takeFirstReceiveOrPeekClosed == null) {
                LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
                while (true) {
                    Object prev = lockFreeLinkedListHead.getPrev();
                    if (prev != null) {
                        LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                        if (!(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                            if (lockFreeLinkedListNode.addNext(closed, lockFreeLinkedListHead)) {
                                z = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                    }
                }
                if (z) {
                    afterClose(th);
                    return true;
                }
            } else if (takeFirstReceiveOrPeekClosed instanceof Closed) {
                return false;
            } else {
                ((Receive) takeFirstReceiveOrPeekClosed).resumeReceiveClosed(closed);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        r3 = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlinx.coroutines.experimental.channels.ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        /*
            r4 = this;
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead r0 = r4.queue
        L_0x0002:
            java.lang.Object r1 = r0.getNext()
            if (r1 == 0) goto L_0x002c
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r1 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r1
            r2 = r0
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r2 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r2
            r3 = 0
            if (r1 != r2) goto L_0x0011
            goto L_0x0025
        L_0x0011:
            boolean r2 = r1 instanceof kotlinx.coroutines.experimental.channels.ReceiveOrClosed
            if (r2 != 0) goto L_0x0016
            goto L_0x0025
        L_0x0016:
            r2 = r1
            kotlinx.coroutines.experimental.channels.ReceiveOrClosed r2 = (kotlinx.coroutines.experimental.channels.ReceiveOrClosed) r2
            boolean r2 = r2 instanceof kotlinx.coroutines.experimental.channels.Closed
            if (r2 == 0) goto L_0x001e
            goto L_0x0024
        L_0x001e:
            boolean r2 = r1.remove()
            if (r2 == 0) goto L_0x0028
        L_0x0024:
            r3 = r1
        L_0x0025:
            kotlinx.coroutines.experimental.channels.ReceiveOrClosed r3 = (kotlinx.coroutines.experimental.channels.ReceiveOrClosed) r3
            return r3
        L_0x0028:
            r1.helpDelete()
            goto L_0x0002
        L_0x002c:
            kotlin.TypeCastException r0 = new kotlin.TypeCastException
            java.lang.String r1 = "null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.AbstractSendChannel.takeFirstReceiveOrPeekClosed():kotlinx.coroutines.experimental.channels.ReceiveOrClosed");
    }

    /* access modifiers changed from: protected */
    public final TryOfferDesc<E> describeTryOffer(E e) {
        return new TryOfferDesc<>(e, this.queue);
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00028\u0001\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001a\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0014J\u0016\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0003H\u0014R\u0012\u0010\u0005\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$TryOfferDesc;", "E", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "Lkotlinx/coroutines/experimental/channels/ReceiveOrClosed;", "Lkotlinx/coroutines/experimental/internal/RemoveFirstDesc;", "element", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;)V", "Ljava/lang/Object;", "resumeToken", "", "failure", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "validatePrepared", "", "node", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    protected static final class TryOfferDesc<E> extends LockFreeLinkedListNode.RemoveFirstDesc<ReceiveOrClosed<? super E>> {
        public final E element;
        public Object resumeToken;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public TryOfferDesc(E e, LockFreeLinkedListHead lockFreeLinkedListHead) {
            super(lockFreeLinkedListHead);
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
            this.element = e;
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (!(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            if (lockFreeLinkedListNode instanceof Closed) {
                return lockFreeLinkedListNode;
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public boolean validatePrepared(ReceiveOrClosed<? super E> receiveOrClosed) {
            Intrinsics.checkParameterIsNotNull(receiveOrClosed, "node");
            Object tryResumeReceive = receiveOrClosed.tryResumeReceive(this.element, this);
            if (tryResumeReceive == null) {
                return false;
            }
            this.resumeToken = tryResumeReceive;
            return true;
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0004\b\u0002\u0010\u00022\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004`\u0005B<\u0012\u0006\u0010\u0006\u001a\u00028\u0001\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00020\b\u0012\u001c\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\nø\u0001\u0000¢\u0006\u0002\u0010\rJ\u001a\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\fH\u0014J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0014J\u001a\u0010\u0014\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0014\u0002\u0004\n\u0002\b\t¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$TryEnqueueSendDesc;", "E", "R", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendSelect;", "Lkotlinx/coroutines/experimental/internal/AddLastDesc;", "element", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/channels/AbstractSendChannel;Ljava/lang/Object;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "failure", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "finishOnSuccess", "", "onPrepare", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private final class TryEnqueueSendDesc<E, R> extends LockFreeLinkedListNode.AddLastDesc<SendSelect<R>> {
        final /* synthetic */ AbstractSendChannel this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public TryEnqueueSendDesc(AbstractSendChannel abstractSendChannel, E e, SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
            super(abstractSendChannel.getQueue(), new SendSelect(e, selectInstance, function1));
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function1, "block");
            this.this$0 = abstractSendChannel;
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (!(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                return null;
            }
            if (!(lockFreeLinkedListNode instanceof Closed)) {
                lockFreeLinkedListNode = null;
            }
            Closed closed = (Closed) lockFreeLinkedListNode;
            return closed != null ? closed : AbstractChannelKt.ENQUEUE_FAILED;
        }

        /* access modifiers changed from: protected */
        public Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            if (!this.this$0.isBufferFull()) {
                return AbstractChannelKt.ENQUEUE_FAILED;
            }
            return super.onPrepare(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            super.finishOnSuccess(lockFreeLinkedListNode, lockFreeLinkedListNode2);
            ((SendSelect) this.node).disposeOnSelect();
        }
    }

    public <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        while (!selectInstance.isSelected()) {
            if (isFull()) {
                Object performAtomicIfNotSelected = selectInstance.performAtomicIfNotSelected(new TryEnqueueSendDesc(this, e, selectInstance, function1));
                if (performAtomicIfNotSelected != null && performAtomicIfNotSelected != JobKt.getALREADY_SELECTED()) {
                    if (performAtomicIfNotSelected != AbstractChannelKt.ENQUEUE_FAILED) {
                        if (performAtomicIfNotSelected instanceof Closed) {
                            throw ((Closed) performAtomicIfNotSelected).getSendException();
                        }
                        throw new IllegalStateException(("performAtomicIfNotSelected(TryEnqueueSendDesc) returned " + performAtomicIfNotSelected).toString());
                    }
                } else {
                    return;
                }
            } else {
                Object offerSelectInternal = offerSelectInternal(e, selectInstance);
                if (offerSelectInternal != JobKt.getALREADY_SELECTED()) {
                    if (offerSelectInternal != AbstractChannelKt.OFFER_FAILED) {
                        if (offerSelectInternal == AbstractChannelKt.OFFER_SUCCESS) {
                            UndispatchedKt.startCoroutineUndispatched(function1, selectInstance.getCompletion());
                            return;
                        } else if (offerSelectInternal instanceof Closed) {
                            throw ((Closed) offerSelectInternal).getSendException();
                        } else {
                            throw new IllegalStateException(("offerSelectInternal returned " + offerSelectInternal).toString());
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\u00020\u00022\u00020\u00032\u00020\u0004B>\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00010\b\u0012\u001c\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00060\nø\u0001\u0000¢\u0006\u0002\u0010\fJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0006H\u0016J\b\u0010\u0013\u001a\u00020\u0011H\u0016J\u0006\u0010\u0014\u001a\u00020\u0011J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0014\u0010\u0017\u001a\u0004\u0018\u00010\u00062\b\u0010\u0018\u001a\u0004\u0018\u00010\u0006H\u0016R+\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00060\n8\u0006X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\rR\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00010\b8\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendSelect;", "R", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/channels/Send;", "Lkotlinx/coroutines/experimental/DisposableHandle;", "pollResult", "", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "Lkotlin/jvm/functions/Function1;", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "token", "dispose", "disposeOnSelect", "toString", "", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class SendSelect<R> extends LockFreeLinkedListNode implements Send, DisposableHandle {
        public final Function1<Continuation<? super R>, Object> block;
        private final Object pollResult;
        public final SelectInstance<R> select;

        @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
        public void unregister() {
            DisposableHandle.DefaultImpls.unregister(this);
        }

        public Object getPollResult() {
            return this.pollResult;
        }

        public SendSelect(Object obj, SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function1, "block");
            this.pollResult = obj;
            this.select = selectInstance;
            this.block = function1;
        }

        public Object tryResumeSend(Object obj) {
            if (this.select.trySelect(obj)) {
                return AbstractChannelKt.SELECT_STARTED;
            }
            return null;
        }

        public void completeResumeSend(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (obj == AbstractChannelKt.SELECT_STARTED) {
                CoroutinesKt.startCoroutine(this.block, this.select.getCompletion());
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }

        public final void disposeOnSelect() {
            this.select.disposeOnSelect(this);
        }

        public void dispose() {
            remove();
        }

        public String toString() {
            return "SendSelect(" + getPollResult() + ")[" + this.select + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0001¢\u0006\u0002\u0010\u0005J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0016J\u0014\u0010\u000e\u001a\u0004\u0018\u00010\b2\b\u0010\u000f\u001a\u0004\u0018\u00010\bH\u0016R\u0012\u0010\u0004\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b8VX\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractSendChannel$SendBuffered;", "E", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/channels/Send;", "element", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "pollResult", "", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "token", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class SendBuffered<E> extends LockFreeLinkedListNode implements Send {
        public final E element;

        public SendBuffered(E e) {
            this.element = e;
        }

        public Object getPollResult() {
            return this.element;
        }

        public Object tryResumeSend(Object obj) {
            return AbstractChannelKt.SEND_RESUMED;
        }

        public void completeResumeSend(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (!(obj == AbstractChannelKt.SEND_RESUMED)) {
                throw new IllegalStateException("Check failed.".toString());
            }
        }
    }

    private final Object sendSuspend(E e, Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        SendElement sendElement = new SendElement(e, cancellableContinuation);
        while (true) {
            if (enqueueSend(sendElement)) {
                cancellableContinuation.initCancellability();
                CancellableContinuationKt.removeOnCancel(cancellableContinuation, sendElement);
                break;
            }
            Object offerInternal = offerInternal(e);
            if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
                cancellableContinuation.resume(Unit.INSTANCE);
                break;
            } else if (offerInternal != AbstractChannelKt.OFFER_FAILED) {
                if (offerInternal instanceof Closed) {
                    cancellableContinuation.resumeWithException(((Closed) offerInternal).getSendException());
                } else {
                    throw new IllegalStateException(("offerInternal returned " + offerInternal).toString());
                }
            }
        }
        return cancellableContinuationImpl.getResult();
    }
}
