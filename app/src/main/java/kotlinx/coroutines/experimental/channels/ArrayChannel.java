package kotlinx.coroutines.experimental.channels;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.experimental.JobKt;
import kotlinx.coroutines.experimental.channels.AbstractChannel;
import kotlinx.coroutines.experimental.channels.AbstractSendChannel;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0015\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u0018J!\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00028\u00002\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0014¢\u0006\u0002\u0010\u001cJ\n\u0010\u001d\u001a\u0004\u0018\u00010\bH\u0014J\u0016\u0010\u001e\u001a\u0004\u0018\u00010\b2\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0014R\u0018\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8DX\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u000e8DX\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000fR\u0014\u0010\u0011\u001a\u00020\u000e8DX\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fR\u0014\u0010\u0012\u001a\u00020\u000e8DX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000fR\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0015\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ArrayChannel;", "E", "Lkotlinx/coroutines/experimental/channels/AbstractChannel;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "getCapacity", "()I", "head", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "size", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/selects/SelectInstance;)Ljava/lang/Object;", "pollInternal", "pollSelectInternal", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: ArrayChannel.kt */
public class ArrayChannel<E> extends AbstractChannel<E> {
    private final Object[] buffer;
    private final int capacity;
    private int head;
    private final ReentrantLock lock;
    private volatile int size;

    /* access modifiers changed from: protected */
    public final boolean isBufferAlwaysEmpty() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferAlwaysFull() {
        return false;
    }

    public final int getCapacity() {
        return this.capacity;
    }

    public ArrayChannel(int i) {
        this.capacity = i;
        if (this.capacity < 1 ? false : true) {
            this.lock = new ReentrantLock();
            this.buffer = new Object[this.capacity];
            return;
        }
        throw new IllegalStateException(("ArrayChannel capacity must be at least 1, but " + this.capacity + " was specified").toString());
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferEmpty() {
        return this.size == 0;
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferFull() {
        return this.size == this.capacity;
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E e) {
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (ReceiveOrClosed) null;
        Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        objectRef2.element = null;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            if (i < this.capacity) {
                this.size = i + 1;
                if (i == 0) {
                    do {
                        T takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                        if (takeFirstReceiveOrPeekClosed != null) {
                            objectRef.element = takeFirstReceiveOrPeekClosed;
                            if (((ReceiveOrClosed) objectRef.element) instanceof Closed) {
                                this.size = i;
                                ReceiveOrClosed receiveOrClosed = (ReceiveOrClosed) objectRef.element;
                                if (receiveOrClosed == null) {
                                    Intrinsics.throwNpe();
                                }
                                lock2.unlock();
                                return receiveOrClosed;
                            }
                            ReceiveOrClosed receiveOrClosed2 = (ReceiveOrClosed) objectRef.element;
                            if (receiveOrClosed2 == null) {
                                Intrinsics.throwNpe();
                            }
                            objectRef2.element = receiveOrClosed2.tryResumeReceive(e, (Object) null);
                        }
                    } while (objectRef2.element == null);
                    this.size = i;
                    Unit unit = Unit.INSTANCE;
                    lock2.unlock();
                    ReceiveOrClosed receiveOrClosed3 = (ReceiveOrClosed) objectRef.element;
                    if (receiveOrClosed3 == null) {
                        Intrinsics.throwNpe();
                    }
                    T t = objectRef2.element;
                    if (t == null) {
                        Intrinsics.throwNpe();
                    }
                    receiveOrClosed3.completeResumeReceive(t);
                    ReceiveOrClosed receiveOrClosed4 = (ReceiveOrClosed) objectRef.element;
                    if (receiveOrClosed4 == null) {
                        Intrinsics.throwNpe();
                    }
                    return receiveOrClosed4.getOfferResult();
                }
                this.buffer[(this.head + i) % this.capacity] = e;
                Object obj = AbstractChannelKt.OFFER_SUCCESS;
                lock2.unlock();
                return obj;
            }
            Object obj2 = AbstractChannelKt.OFFER_FAILED;
            lock2.unlock();
            return obj2;
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (ReceiveOrClosed) null;
        Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        objectRef2.element = null;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            if (i < this.capacity) {
                this.size = i + 1;
                if (i == 0) {
                    AbstractSendChannel.TryOfferDesc describeTryOffer = describeTryOffer(e);
                    Object performAtomicTrySelect = selectInstance.performAtomicTrySelect(describeTryOffer);
                    if (performAtomicTrySelect == null) {
                        this.size = i;
                        objectRef.element = (ReceiveOrClosed) describeTryOffer.getResult();
                        objectRef2.element = describeTryOffer.resumeToken;
                        if (objectRef2.element != null) {
                            Unit unit = Unit.INSTANCE;
                            lock2.unlock();
                            ReceiveOrClosed receiveOrClosed = (ReceiveOrClosed) objectRef.element;
                            if (receiveOrClosed == null) {
                                Intrinsics.throwNpe();
                            }
                            T t = objectRef2.element;
                            if (t == null) {
                                Intrinsics.throwNpe();
                            }
                            receiveOrClosed.completeResumeReceive(t);
                            ReceiveOrClosed receiveOrClosed2 = (ReceiveOrClosed) objectRef.element;
                            if (receiveOrClosed2 == null) {
                                Intrinsics.throwNpe();
                            }
                            return receiveOrClosed2.getOfferResult();
                        }
                        throw new IllegalStateException("Check failed.".toString());
                    } else if (performAtomicTrySelect != AbstractChannelKt.OFFER_FAILED) {
                        if (performAtomicTrySelect != JobKt.getALREADY_SELECTED()) {
                            if (!(performAtomicTrySelect instanceof Closed)) {
                                throw new IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + performAtomicTrySelect).toString());
                            }
                        }
                        this.size = i;
                        lock2.unlock();
                        return performAtomicTrySelect;
                    }
                }
                if (!selectInstance.trySelect((Object) null)) {
                    this.size = i;
                    Object already_selected = JobKt.getALREADY_SELECTED();
                    lock2.unlock();
                    return already_selected;
                }
                this.buffer[(this.head + i) % this.capacity] = e;
                Object obj = AbstractChannelKt.OFFER_SUCCESS;
                lock2.unlock();
                return obj;
            }
            Object obj2 = AbstractChannelKt.OFFER_FAILED;
            lock2.unlock();
            return obj2;
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object pollInternal() {
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (Send) null;
        Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        objectRef2.element = null;
        Ref.ObjectRef objectRef3 = new Ref.ObjectRef();
        objectRef3.element = null;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            if (i == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            objectRef3.element = this.buffer[this.head];
            this.buffer[this.head] = null;
            this.size = i - 1;
            Object obj = AbstractChannelKt.POLL_FAILED;
            if (i == this.capacity) {
                while (true) {
                    T takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                    if (takeFirstSendOrPeekClosed == null) {
                        break;
                    }
                    objectRef.element = takeFirstSendOrPeekClosed;
                    Send send = (Send) objectRef.element;
                    if (send == null) {
                        Intrinsics.throwNpe();
                    }
                    objectRef2.element = send.tryResumeSend((Object) null);
                    if (objectRef2.element != null) {
                        Send send2 = (Send) objectRef.element;
                        if (send2 == null) {
                            Intrinsics.throwNpe();
                        }
                        obj = send2.getPollResult();
                    }
                }
            }
            if (obj != AbstractChannelKt.POLL_FAILED && !(obj instanceof Closed)) {
                this.size = i;
                this.buffer[(this.head + i) % this.capacity] = obj;
            }
            this.head = (this.head + 1) % this.capacity;
            Unit unit = Unit.INSTANCE;
            lock2.unlock();
            if (objectRef2.element != null) {
                Send send3 = (Send) objectRef.element;
                if (send3 == null) {
                    Intrinsics.throwNpe();
                }
                T t = objectRef2.element;
                if (t == null) {
                    Intrinsics.throwNpe();
                }
                send3.completeResumeSend(t);
            }
            return objectRef3.element;
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object pollSelectInternal(SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (Send) null;
        Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
        objectRef2.element = null;
        Ref.ObjectRef objectRef3 = new Ref.ObjectRef();
        objectRef3.element = null;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            if (i == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            objectRef3.element = this.buffer[this.head];
            this.buffer[this.head] = null;
            this.size = i - 1;
            T t = AbstractChannelKt.POLL_FAILED;
            if (i == this.capacity) {
                AbstractChannel.TryPollDesc describeTryPoll = describeTryPoll();
                T performAtomicTrySelect = selectInstance.performAtomicTrySelect(describeTryPoll);
                if (performAtomicTrySelect == null) {
                    objectRef.element = (Send) describeTryPoll.getResult();
                    objectRef2.element = describeTryPoll.resumeToken;
                    if (objectRef2.element != null) {
                        Send send = (Send) objectRef.element;
                        if (send == null) {
                            Intrinsics.throwNpe();
                        }
                        t = send.getPollResult();
                    } else {
                        throw new IllegalStateException("Check failed.".toString());
                    }
                } else if (performAtomicTrySelect != AbstractChannelKt.POLL_FAILED) {
                    if (performAtomicTrySelect == JobKt.getALREADY_SELECTED()) {
                        this.size = i;
                        this.buffer[this.head] = objectRef3.element;
                        lock2.unlock();
                        return performAtomicTrySelect;
                    } else if (performAtomicTrySelect instanceof Closed) {
                        objectRef.element = (Send) performAtomicTrySelect;
                        objectRef2.element = ((Closed) performAtomicTrySelect).tryResumeSend((Object) null);
                        t = performAtomicTrySelect;
                    } else {
                        throw new IllegalStateException(("performAtomicTrySelect(describeTryOffer) returned " + performAtomicTrySelect).toString());
                    }
                }
            }
            if (t != AbstractChannelKt.POLL_FAILED && !(t instanceof Closed)) {
                this.size = i;
                this.buffer[(this.head + i) % this.capacity] = t;
            } else if (!selectInstance.trySelect((Object) null)) {
                this.size = i;
                this.buffer[this.head] = objectRef3.element;
                Object already_selected = JobKt.getALREADY_SELECTED();
                lock2.unlock();
                return already_selected;
            }
            this.head = (this.head + 1) % this.capacity;
            Unit unit = Unit.INSTANCE;
            lock2.unlock();
            if (objectRef2.element != null) {
                Send send2 = (Send) objectRef.element;
                if (send2 == null) {
                    Intrinsics.throwNpe();
                }
                T t2 = objectRef2.element;
                if (t2 == null) {
                    Intrinsics.throwNpe();
                }
                send2.completeResumeSend(t2);
            }
            return objectRef3.element;
        } finally {
            lock2.unlock();
        }
    }
}
