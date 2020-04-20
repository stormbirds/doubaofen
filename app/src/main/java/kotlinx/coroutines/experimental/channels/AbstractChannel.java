package kotlinx.coroutines.experimental.channels;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CancellableContinuation;
import kotlinx.coroutines.experimental.CancellableContinuationImpl;
import kotlinx.coroutines.experimental.DisposableHandle;
import kotlinx.coroutines.experimental.JobKt;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u00070123456B\u0005¢\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u000eH\u0004J\u0016\u0010\u000f\u001a\u00020\u00062\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u0011H\u0002J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00000\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0015H\u0014J\u000f\u0010\u0017\u001a\u0004\u0018\u00018\u0000H\u0007¢\u0006\u0002\u0010\u0018J\n\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0014J\u0016\u0010\u001b\u001a\u0004\u0018\u00010\u001a2\n\u0010\u001c\u001a\u0006\u0012\u0002\b\u00030\u001dH\u0014J\u0011\u0010\u0010\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\u0013\u0010\u001f\u001a\u0004\u0018\u00018\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\u0019\u0010 \u001a\u0004\u0018\u00018\u00002\b\u0010!\u001a\u0004\u0018\u00010\u001aH\u0003¢\u0006\u0002\u0010\"J\u0013\u0010#\u001a\u0004\u0018\u00018\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\u0017\u0010$\u001a\u00028\u00002\b\u0010!\u001a\u0004\u0018\u00010\u001aH\u0003¢\u0006\u0002\u0010\"J\u0011\u0010%\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJH\u0010&\u001a\u00020\u0015\"\u0004\b\u0001\u0010'2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H'0\u001d2\"\u0010(\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H'0*\u0012\u0006\u0012\u0004\u0018\u00010\u001a0)H\u0017ø\u0001\u0000¢\u0006\u0002\u0010+JJ\u0010,\u001a\u00020\u0015\"\u0004\b\u0001\u0010'2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H'0\u001d2$\u0010(\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H'0*\u0012\u0006\u0012\u0004\u0018\u00010\u001a0)H\u0017ø\u0001\u0000¢\u0006\u0002\u0010+J \u0010-\u001a\u00020\u00152\n\u0010.\u001a\u0006\u0012\u0002\b\u00030/2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0011H\u0002R\u0014\u0010\u0005\u001a\u00020\u00068DX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0012\u0010\t\u001a\u00020\u0006X¤\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\bR\u0012\u0010\n\u001a\u00020\u0006X¤\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\bR\u0011\u0010\f\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\f\u0010\b\u0002\u0004\n\u0002\b\t¨\u00067"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel;", "E", "Lkotlinx/coroutines/experimental/channels/AbstractSendChannel;", "Lkotlinx/coroutines/experimental/channels/Channel;", "()V", "hasReceiveOrClosed", "", "getHasReceiveOrClosed", "()Z", "isBufferAlwaysEmpty", "isBufferEmpty", "isClosedForReceive", "isEmpty", "describeTryPoll", "Lkotlinx/coroutines/experimental/channels/AbstractChannel$TryPollDesc;", "enqueueReceive", "receive", "Lkotlinx/coroutines/experimental/channels/Receive;", "iterator", "Lkotlinx/coroutines/experimental/channels/ChannelIterator;", "onCancelledReceive", "", "onEnqueuedReceive", "poll", "()Ljava/lang/Object;", "pollInternal", "", "pollSelectInternal", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "receiveOrNull", "receiveOrNullResult", "result", "(Ljava/lang/Object;)Ljava/lang/Object;", "receiveOrNullSuspend", "receiveResult", "receiveSuspend", "registerSelectReceive", "R", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "registerSelectReceiveOrNull", "removeReceiveOnCancel", "cont", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "IdempotentTokenValue", "Itr", "ReceiveElement", "ReceiveHasNext", "ReceiveSelect", "TryEnqueueReceiveDesc", "TryPollDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: AbstractChannel.kt */
public abstract class AbstractChannel<E> extends AbstractSendChannel<E> implements Channel<E> {
    /* access modifiers changed from: protected */
    public abstract boolean isBufferAlwaysEmpty();

    /* access modifiers changed from: protected */
    public abstract boolean isBufferEmpty();

    /* access modifiers changed from: protected */
    public void onCancelledReceive() {
    }

    /* access modifiers changed from: protected */
    public void onEnqueuedReceive() {
    }

    /* access modifiers changed from: protected */
    public Object pollInternal() {
        Send takeFirstSendOrPeekClosed;
        Object tryResumeSend;
        do {
            takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
            if (takeFirstSendOrPeekClosed == null) {
                return AbstractChannelKt.POLL_FAILED;
            }
            tryResumeSend = takeFirstSendOrPeekClosed.tryResumeSend((Object) null);
        } while (tryResumeSend == null);
        takeFirstSendOrPeekClosed.completeResumeSend(tryResumeSend);
        return takeFirstSendOrPeekClosed.getPollResult();
    }

    /* access modifiers changed from: protected */
    public Object pollSelectInternal(SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        TryPollDesc describeTryPoll = describeTryPoll();
        Object performAtomicTrySelect = selectInstance.performAtomicTrySelect(describeTryPoll);
        if (performAtomicTrySelect != null) {
            return performAtomicTrySelect;
        }
        Send send = (Send) describeTryPoll.getResult();
        Object obj = describeTryPoll.resumeToken;
        if (obj == null) {
            Intrinsics.throwNpe();
        }
        send.completeResumeSend(obj);
        return describeTryPoll.pollResult;
    }

    /* access modifiers changed from: protected */
    public final boolean getHasReceiveOrClosed() {
        return getQueue().getNext() instanceof ReceiveOrClosed;
    }

    public final boolean isClosedForReceive() {
        return getClosedForReceive() != null && isBufferEmpty();
    }

    public final boolean isEmpty() {
        return !(getQueue().getNext() instanceof Send) && isBufferEmpty();
    }

    public final Object receive(Continuation<? super E> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        Object pollInternal = pollInternal();
        if (pollInternal != AbstractChannelKt.POLL_FAILED) {
            return receiveResult(pollInternal);
        }
        return receiveSuspend(continuation);
    }

    private final E receiveResult(Object obj) {
        if (!(obj instanceof Closed)) {
            return obj;
        }
        throw ((Closed) obj).getReceiveException();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean enqueueReceive(kotlinx.coroutines.experimental.channels.Receive<? super E> r8) {
        /*
            r7 = this;
            boolean r0 = r7.isBufferAlwaysEmpty()
            java.lang.String r1 = "null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */"
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x002c
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead r0 = r7.getQueue()
        L_0x000e:
            java.lang.Object r4 = r0.getPrev()
            if (r4 == 0) goto L_0x0026
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r4
            boolean r5 = r4 instanceof kotlinx.coroutines.experimental.channels.Send
            r5 = r5 ^ r3
            if (r5 != 0) goto L_0x001c
            goto L_0x0052
        L_0x001c:
            r5 = r8
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r5
            boolean r4 = r4.addNext(r5, r0)
            if (r4 == 0) goto L_0x000e
            goto L_0x0051
        L_0x0026:
            kotlin.TypeCastException r8 = new kotlin.TypeCastException
            r8.<init>(r1)
            throw r8
        L_0x002c:
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead r0 = r7.getQueue()
            kotlinx.coroutines.experimental.channels.AbstractChannel$enqueueReceive$$inlined$addLastIfPrevAndIf$1 r4 = new kotlinx.coroutines.experimental.channels.AbstractChannel$enqueueReceive$$inlined$addLastIfPrevAndIf$1
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r8 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r8
            r4.<init>(r8, r8, r7)
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode$CondAddOp r4 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode.CondAddOp) r4
        L_0x0039:
            java.lang.Object r5 = r0.getPrev()
            if (r5 == 0) goto L_0x0058
            kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode) r5
            boolean r6 = r5 instanceof kotlinx.coroutines.experimental.channels.Send
            r6 = r6 ^ r3
            if (r6 != 0) goto L_0x0047
            goto L_0x0052
        L_0x0047:
            int r5 = r5.tryCondAddNext(r8, r0, r4)
            if (r5 == r3) goto L_0x0051
            r6 = 2
            if (r5 == r6) goto L_0x0052
            goto L_0x0039
        L_0x0051:
            r2 = 1
        L_0x0052:
            if (r2 == 0) goto L_0x0057
            r7.onEnqueuedReceive()
        L_0x0057:
            return r2
        L_0x0058:
            kotlin.TypeCastException r8 = new kotlin.TypeCastException
            r8.<init>(r1)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.AbstractChannel.enqueueReceive(kotlinx.coroutines.experimental.channels.Receive):boolean");
    }

    public final Object receiveOrNull(Continuation<? super E> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        Object pollInternal = pollInternal();
        if (pollInternal != AbstractChannelKt.POLL_FAILED) {
            return receiveOrNullResult(pollInternal);
        }
        return receiveOrNullSuspend(continuation);
    }

    private final E receiveOrNullResult(Object obj) {
        if (!(obj instanceof Closed)) {
            return obj;
        }
        Closed closed = (Closed) obj;
        if (closed.closeCause == null) {
            return null;
        }
        throw closed.closeCause;
    }

    public final E poll() {
        Object pollInternal = pollInternal();
        if (pollInternal == AbstractChannelKt.POLL_FAILED) {
            return null;
        }
        return receiveOrNullResult(pollInternal);
    }

    public final ChannelIterator<E> iterator() {
        return new Itr<>(this);
    }

    /* access modifiers changed from: protected */
    public final TryPollDesc<E> describeTryPoll() {
        return new TryPollDesc<>(getQueue());
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u0012\u0012\u0004\u0012\u00020\u00030\u0002j\b\u0012\u0004\u0012\u00020\u0003`\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u001a\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0014J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0003H\u0015R\u0016\u0010\b\u001a\u0004\u0018\u00018\u00018\u0006@\u0006X\u000e¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$TryPollDesc;", "E", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "Lkotlinx/coroutines/experimental/channels/Send;", "Lkotlinx/coroutines/experimental/internal/RemoveFirstDesc;", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;)V", "pollResult", "Ljava/lang/Object;", "resumeToken", "", "failure", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "validatePrepared", "", "node", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    protected static final class TryPollDesc<E> extends LockFreeLinkedListNode.RemoveFirstDesc<Send> {
        public E pollResult;
        public Object resumeToken;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public TryPollDesc(LockFreeLinkedListHead lockFreeLinkedListHead) {
            super(lockFreeLinkedListHead);
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (lockFreeLinkedListNode instanceof Closed) {
                return lockFreeLinkedListNode;
            }
            if (!(lockFreeLinkedListNode instanceof Send)) {
                return AbstractChannelKt.POLL_FAILED;
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public boolean validatePrepared(Send send) {
            Intrinsics.checkParameterIsNotNull(send, "node");
            Object tryResumeSend = send.tryResumeSend(this);
            if (tryResumeSend == null) {
                return false;
            }
            this.resumeToken = tryResumeSend;
            this.pollResult = send.getPollResult();
            return true;
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u0000*\u0004\b\u0002\u0010\u00022>\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u0004R\b\u0012\u0004\u0012\u00028\u00000\u00050\u0003j\u001e\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u0004R\b\u0012\u0004\u0012\u00028\u00000\u0005`\u0006BD\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00020\b\u0012$\u0010\t\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00018\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\n\u0012\u0006\u0010\r\u001a\u00020\u000eø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u001a\u0010\u0010\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fH\u0014J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0014J\u001a\u0010\u0016\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0014\u0002\u0004\n\u0002\b\t¨\u0006\u0017"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$TryEnqueueReceiveDesc;", "E", "R", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/experimental/channels/AbstractChannel$ReceiveSelect;", "Lkotlinx/coroutines/experimental/channels/AbstractChannel;", "Lkotlinx/coroutines/experimental/internal/AddLastDesc;", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "nullOnClose", "", "(Lkotlinx/coroutines/experimental/channels/AbstractChannel;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;Z)V", "failure", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "finishOnSuccess", "", "onPrepare", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private final class TryEnqueueReceiveDesc<E, R> extends LockFreeLinkedListNode.AddLastDesc<AbstractChannel<E>.ReceiveSelect<R, ? super E>> {
        final /* synthetic */ AbstractChannel this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public TryEnqueueReceiveDesc(AbstractChannel abstractChannel, SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2, boolean z) {
            super(abstractChannel.getQueue(), new ReceiveSelect(abstractChannel, selectInstance, function2, z));
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.this$0 = abstractChannel;
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (lockFreeLinkedListNode instanceof Send) {
                return AbstractChannelKt.ENQUEUE_FAILED;
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            if (!this.this$0.isBufferEmpty()) {
                return AbstractChannelKt.ENQUEUE_FAILED;
            }
            return super.onPrepare(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            super.finishOnSuccess(lockFreeLinkedListNode, lockFreeLinkedListNode2);
            this.this$0.onEnqueuedReceive();
            ((ReceiveSelect) this.node).removeOnSelectCompletion();
        }
    }

    public <R> void registerSelectReceive(SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        while (!selectInstance.isSelected()) {
            if (isEmpty()) {
                Object performAtomicIfNotSelected = selectInstance.performAtomicIfNotSelected(new TryEnqueueReceiveDesc(this, selectInstance, function2, false));
                if (performAtomicIfNotSelected != null && performAtomicIfNotSelected != JobKt.getALREADY_SELECTED()) {
                    if (performAtomicIfNotSelected != AbstractChannelKt.ENQUEUE_FAILED) {
                        throw new IllegalStateException(("performAtomicIfNotSelected(TryEnqueueReceiveDesc) returned " + performAtomicIfNotSelected).toString());
                    }
                } else {
                    return;
                }
            } else {
                Object pollSelectInternal = pollSelectInternal(selectInstance);
                if (pollSelectInternal != JobKt.getALREADY_SELECTED()) {
                    if (pollSelectInternal != AbstractChannelKt.POLL_FAILED) {
                        if (!(pollSelectInternal instanceof Closed)) {
                            UndispatchedKt.startCoroutineUndispatched(function2, pollSelectInternal, selectInstance.getCompletion());
                            return;
                        }
                        throw ((Closed) pollSelectInternal).getReceiveException();
                    }
                } else {
                    return;
                }
            }
        }
    }

    public <R> void registerSelectReceiveOrNull(SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        while (!selectInstance.isSelected()) {
            if (isEmpty()) {
                Object performAtomicIfNotSelected = selectInstance.performAtomicIfNotSelected(new TryEnqueueReceiveDesc(this, selectInstance, function2, true));
                if (performAtomicIfNotSelected != null && performAtomicIfNotSelected != JobKt.getALREADY_SELECTED()) {
                    if (performAtomicIfNotSelected != AbstractChannelKt.ENQUEUE_FAILED) {
                        throw new IllegalStateException(("performAtomicIfNotSelected(TryEnqueueReceiveDesc) returned " + performAtomicIfNotSelected).toString());
                    }
                } else {
                    return;
                }
            } else {
                Object pollSelectInternal = pollSelectInternal(selectInstance);
                if (pollSelectInternal != JobKt.getALREADY_SELECTED()) {
                    if (pollSelectInternal != AbstractChannelKt.POLL_FAILED) {
                        if (pollSelectInternal instanceof Closed) {
                            Closed closed = (Closed) pollSelectInternal;
                            if (closed.closeCause != null) {
                                throw closed.closeCause;
                            } else if (selectInstance.trySelect((Object) null)) {
                                UndispatchedKt.startCoroutineUndispatched(function2, null, selectInstance.getCompletion());
                                return;
                            } else {
                                return;
                            }
                        } else {
                            UndispatchedKt.startCoroutineUndispatched(function2, pollSelectInternal, selectInstance.getCompletion());
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final void removeReceiveOnCancel(CancellableContinuation<?> cancellableContinuation, Receive<?> receive) {
        cancellableContinuation.invokeOnCompletion(new AbstractChannel$removeReceiveOnCancel$1(this, cancellableContinuation, receive));
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004¢\u0006\u0002\u0010\u0005J\u0011\u0010\u000e\u001a\u00020\u000fHBø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0012\u0010\u0011\u001a\u00020\u000f2\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0002J\u0011\u0010\u0012\u001a\u00020\u000fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0011\u0010\u0013\u001a\u00028\u0001HBø\u0001\u0000¢\u0006\u0002\u0010\u0010R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u0002\u0004\n\u0002\b\t¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$Itr;", "E", "Lkotlinx/coroutines/experimental/channels/ChannelIterator;", "channel", "Lkotlinx/coroutines/experimental/channels/AbstractChannel;", "(Lkotlinx/coroutines/experimental/channels/AbstractChannel;)V", "getChannel", "()Lkotlinx/coroutines/experimental/channels/AbstractChannel;", "result", "", "getResult", "()Ljava/lang/Object;", "setResult", "(Ljava/lang/Object;)V", "hasNext", "", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "hasNextResult", "hasNextSuspend", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class Itr<E> implements ChannelIterator<E> {
        private final AbstractChannel<E> channel;
        private Object result = AbstractChannelKt.POLL_FAILED;

        public Itr(AbstractChannel<E> abstractChannel) {
            Intrinsics.checkParameterIsNotNull(abstractChannel, "channel");
            this.channel = abstractChannel;
        }

        public final AbstractChannel<E> getChannel() {
            return this.channel;
        }

        public final Object getResult() {
            return this.result;
        }

        public final void setResult(Object obj) {
            this.result = obj;
        }

        public Object hasNext(Continuation<? super Boolean> continuation) {
            Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
            if (this.result != AbstractChannelKt.POLL_FAILED) {
                return Boolean.valueOf(hasNextResult(this.result));
            }
            this.result = this.channel.pollInternal();
            if (this.result != AbstractChannelKt.POLL_FAILED) {
                return Boolean.valueOf(hasNextResult(this.result));
            }
            return hasNextSuspend(continuation);
        }

        private final boolean hasNextResult(Object obj) {
            if (!(obj instanceof Closed)) {
                return true;
            }
            Closed closed = (Closed) obj;
            if (closed.closeCause == null) {
                return false;
            }
            throw closed.getReceiveException();
        }

        public Object next(Continuation<? super E> continuation) {
            Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
            Object obj = this.result;
            if (obj instanceof Closed) {
                throw ((Closed) obj).getReceiveException();
            } else if (obj == AbstractChannelKt.POLL_FAILED) {
                return this.channel.receive(continuation);
            } else {
                this.result = AbstractChannelKt.POLL_FAILED;
                return obj;
            }
        }

        private final Object hasNextSuspend(Continuation<? super Boolean> continuation) {
            CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
            CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
            ReceiveHasNext receiveHasNext = new ReceiveHasNext(this, cancellableContinuation);
            while (true) {
                Receive receive = receiveHasNext;
                if (!getChannel().enqueueReceive(receive)) {
                    Object pollInternal = getChannel().pollInternal();
                    setResult(pollInternal);
                    if (!(pollInternal instanceof Closed)) {
                        if (pollInternal != AbstractChannelKt.POLL_FAILED) {
                            cancellableContinuation.resume(true);
                            break;
                        }
                    } else {
                        Closed closed = (Closed) pollInternal;
                        if (closed.closeCause == null) {
                            cancellableContinuation.resume(false);
                        } else {
                            cancellableContinuation.resumeWithException(closed.getReceiveException());
                        }
                    }
                } else {
                    cancellableContinuation.initCancellability();
                    getChannel().removeReceiveOnCancel(cancellableContinuation, receive);
                    break;
                }
            }
            return cancellableContinuationImpl.getResult();
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0014\u0010\f\u001a\u00020\t2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J!\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0012\u001a\u00028\u00012\b\u0010\u0013\u001a\u0004\u0018\u00010\u000bH\u0016¢\u0006\u0002\u0010\u0014R\u0018\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$ReceiveElement;", "E", "Lkotlinx/coroutines/experimental/channels/Receive;", "cont", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "nullOnClose", "", "(Lkotlinx/coroutines/experimental/CancellableContinuation;Z)V", "completeResumeReceive", "", "token", "", "resumeReceiveClosed", "closed", "Lkotlinx/coroutines/experimental/channels/Closed;", "toString", "", "tryResumeReceive", "value", "idempotent", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class ReceiveElement<E> extends Receive<E> {
        public final CancellableContinuation<E> cont;
        public final boolean nullOnClose;

        public ReceiveElement(CancellableContinuation<? super E> cancellableContinuation, boolean z) {
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.cont = cancellableContinuation;
            this.nullOnClose = z;
        }

        public Object tryResumeReceive(E e, Object obj) {
            return this.cont.tryResume(e, obj);
        }

        public void completeResumeReceive(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            this.cont.completeResume(obj);
        }

        public void resumeReceiveClosed(Closed<?> closed) {
            Intrinsics.checkParameterIsNotNull(closed, "closed");
            if (closed.closeCause != null || !this.nullOnClose) {
                this.cont.resumeWithException(closed.getReceiveException());
            } else {
                this.cont.resume(null);
            }
        }

        public String toString() {
            return "ReceiveElement[" + this.cont + ",nullOnClose=" + this.nullOnClose + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B!\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0014\u0010\r\u001a\u00020\n2\n\u0010\u000e\u001a\u0006\u0012\u0002\b\u00030\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J!\u0010\u0012\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0013\u001a\u00028\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\fH\u0016¢\u0006\u0002\u0010\u0015R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$ReceiveHasNext;", "E", "Lkotlinx/coroutines/experimental/channels/Receive;", "iterator", "Lkotlinx/coroutines/experimental/channels/AbstractChannel$Itr;", "cont", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "", "(Lkotlinx/coroutines/experimental/channels/AbstractChannel$Itr;Lkotlinx/coroutines/experimental/CancellableContinuation;)V", "completeResumeReceive", "", "token", "", "resumeReceiveClosed", "closed", "Lkotlinx/coroutines/experimental/channels/Closed;", "toString", "", "tryResumeReceive", "value", "idempotent", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class ReceiveHasNext<E> extends Receive<E> {
        public final CancellableContinuation<Boolean> cont;
        public final Itr<E> iterator;

        public ReceiveHasNext(Itr<E> itr, CancellableContinuation<? super Boolean> cancellableContinuation) {
            Intrinsics.checkParameterIsNotNull(itr, "iterator");
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.iterator = itr;
            this.cont = cancellableContinuation;
        }

        public Object tryResumeReceive(E e, Object obj) {
            Object tryResume = this.cont.tryResume(true, obj);
            if (tryResume != null) {
                if (obj != null) {
                    return new IdempotentTokenValue(tryResume, e);
                }
                this.iterator.setResult(e);
            }
            return tryResume;
        }

        public void completeResumeReceive(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (obj instanceof IdempotentTokenValue) {
                IdempotentTokenValue idempotentTokenValue = (IdempotentTokenValue) obj;
                this.iterator.setResult(idempotentTokenValue.value);
                this.cont.completeResume(idempotentTokenValue.token);
                return;
            }
            this.cont.completeResume(obj);
        }

        public void resumeReceiveClosed(Closed<?> closed) {
            Object obj;
            Intrinsics.checkParameterIsNotNull(closed, "closed");
            if (closed.closeCause == null) {
                obj = CancellableContinuation.DefaultImpls.tryResume$default(this.cont, false, (Object) null, 2, (Object) null);
            } else {
                obj = this.cont.tryResumeWithException(closed.getReceiveException());
            }
            if (obj != null) {
                this.iterator.setResult(closed);
                this.cont.completeResume(obj);
            }
        }

        public String toString() {
            return "ReceiveHasNext[" + this.cont + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0006\b\u0002\u0010\u0002 \u00002\b\u0012\u0004\u0012\u0002H\u00020\u00032\u00020\u0004BD\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0006\u0012$\u0010\u0007\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00018\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\b\u0012\u0006\u0010\u000b\u001a\u00020\fø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nH\u0017J\b\u0010\u0012\u001a\u00020\u0010H\u0016J\u0006\u0010\u0013\u001a\u00020\u0010J\u0014\u0010\u0014\u001a\u00020\u00102\n\u0010\u0015\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J!\u0010\u0019\u001a\u0004\u0018\u00010\n2\u0006\u0010\u001a\u001a\u00028\u00022\b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u0016¢\u0006\u0002\u0010\u001cR3\u0010\u0007\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00018\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\b8\u0006X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u000eR\u0010\u0010\u000b\u001a\u00020\f8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u00068\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$ReceiveSelect;", "R", "E", "Lkotlinx/coroutines/experimental/channels/Receive;", "Lkotlinx/coroutines/experimental/DisposableHandle;", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "nullOnClose", "", "(Lkotlinx/coroutines/experimental/channels/AbstractChannel;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;Z)V", "Lkotlin/jvm/functions/Function2;", "completeResumeReceive", "", "token", "dispose", "removeOnSelectCompletion", "resumeReceiveClosed", "closed", "Lkotlinx/coroutines/experimental/channels/Closed;", "toString", "", "tryResumeReceive", "value", "idempotent", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private final class ReceiveSelect<R, E> extends Receive<E> implements DisposableHandle {
        public final Function2<E, Continuation<? super R>, Object> block;
        public final boolean nullOnClose;
        public final SelectInstance<R> select;
        final /* synthetic */ AbstractChannel this$0;

        public ReceiveSelect(AbstractChannel abstractChannel, SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2, boolean z) {
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.this$0 = abstractChannel;
            this.select = selectInstance;
            this.block = function2;
            this.nullOnClose = z;
        }

        @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
        public void unregister() {
            DisposableHandle.DefaultImpls.unregister(this);
        }

        public Object tryResumeReceive(E e, Object obj) {
            if (this.select.trySelect(obj)) {
                return e != null ? e : AbstractChannelKt.NULL_VALUE;
            }
            return null;
        }

        public void completeResumeReceive(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (obj == AbstractChannelKt.NULL_VALUE) {
                obj = null;
            }
            CoroutinesKt.startCoroutine(this.block, obj, this.select.getCompletion());
        }

        public void resumeReceiveClosed(Closed<?> closed) {
            Intrinsics.checkParameterIsNotNull(closed, "closed");
            if (!this.select.trySelect((Object) null)) {
                return;
            }
            if (closed.closeCause != null || !this.nullOnClose) {
                this.select.resumeSelectWithException(closed.getReceiveException(), 0);
            } else {
                CoroutinesKt.startCoroutine(this.block, null, this.select.getCompletion());
            }
        }

        public final void removeOnSelectCompletion() {
            this.select.disposeOnSelect(this);
        }

        public void dispose() {
            if (remove()) {
                this.this$0.onCancelledReceive();
            }
        }

        public String toString() {
            return "ReceiveSelect[" + this.select + ",nullOnClose=" + this.nullOnClose + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00028\u0001¢\u0006\u0002\u0010\u0005R\u0010\u0010\u0003\u001a\u00020\u00028\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/channels/AbstractChannel$IdempotentTokenValue;", "E", "", "token", "value", "(Ljava/lang/Object;Ljava/lang/Object;)V", "Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: AbstractChannel.kt */
    private static final class IdempotentTokenValue<E> {
        public final Object token;
        public final E value;

        public IdempotentTokenValue(Object obj, E e) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            this.token = obj;
            this.value = e;
        }
    }

    private final Object receiveSuspend(Continuation<? super E> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        ReceiveElement receiveElement = new ReceiveElement(cancellableContinuation, false);
        while (true) {
            Receive receive = receiveElement;
            if (!enqueueReceive(receive)) {
                Object pollInternal = pollInternal();
                if (!(pollInternal instanceof Closed)) {
                    if (pollInternal != AbstractChannelKt.POLL_FAILED) {
                        cancellableContinuation.resume(pollInternal);
                        break;
                    }
                } else {
                    cancellableContinuation.resumeWithException(((Closed) pollInternal).getReceiveException());
                    break;
                }
            } else {
                cancellableContinuation.initCancellability();
                removeReceiveOnCancel(cancellableContinuation, receive);
                break;
            }
        }
        return cancellableContinuationImpl.getResult();
    }

    private final Object receiveOrNullSuspend(Continuation<? super E> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        ReceiveElement receiveElement = new ReceiveElement(cancellableContinuation, true);
        while (true) {
            Receive receive = receiveElement;
            if (!enqueueReceive(receive)) {
                Object pollInternal = pollInternal();
                if (!(pollInternal instanceof Closed)) {
                    if (pollInternal != AbstractChannelKt.POLL_FAILED) {
                        cancellableContinuation.resume(pollInternal);
                        break;
                    }
                } else {
                    Closed closed = (Closed) pollInternal;
                    if (closed.closeCause == null) {
                        cancellableContinuation.resume(null);
                    } else {
                        cancellableContinuation.resumeWithException(closed.closeCause);
                    }
                }
            } else {
                cancellableContinuation.initCancellability();
                removeReceiveOnCancel(cancellableContinuation, receive);
                break;
            }
        }
        return cancellableContinuationImpl.getResult();
    }
}
