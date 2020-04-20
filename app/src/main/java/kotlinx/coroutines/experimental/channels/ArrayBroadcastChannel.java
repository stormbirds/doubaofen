package kotlinx.coroutines.experimental.channels;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.experimental.JobKt;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001/B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\u0012\u0010\u001c\u001a\u00020\u00122\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\u0016\u0010\u001f\u001a\u00020\u001b2\f\u0010 \u001a\b\u0012\u0004\u0012\u00028\u00000\u0018H\u0002J\b\u0010!\u001a\u00020\u0010H\u0002J\u0015\u0010\"\u001a\u00028\u00002\u0006\u0010#\u001a\u00020\u0010H\u0003¢\u0006\u0002\u0010$J\u0015\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010'J!\u0010(\u001a\u00020\t2\u0006\u0010&\u001a\u00028\u00002\n\u0010)\u001a\u0006\u0012\u0002\b\u00030*H\u0014¢\u0006\u0002\u0010+J\u000e\u0010,\u001a\b\u0012\u0004\u0012\u00028\u00000-H\u0016J\b\u0010.\u001a\u00020\u001bH\u0002R\u0018\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0004¢\u0006\u0004\n\u0002\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0012\u0010\u000f\u001a\u00020\u00108\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128TX\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00128TX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0013R\u0012\u0010\u0015\u001a\u00020\u00058\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00180\u0017X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0019\u001a\u00020\u00108\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000¨\u00060"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ArrayBroadcastChannel;", "E", "Lkotlinx/coroutines/experimental/channels/AbstractSendChannel;", "Lkotlinx/coroutines/experimental/channels/BroadcastChannel;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "bufferLock", "Ljava/util/concurrent/locks/ReentrantLock;", "getCapacity", "()I", "head", "", "isBufferAlwaysFull", "", "()Z", "isBufferFull", "size", "subs", "Ljava/util/concurrent/CopyOnWriteArrayList;", "Lkotlinx/coroutines/experimental/channels/ArrayBroadcastChannel$Subscriber;", "tail", "checkSubOffers", "", "close", "cause", "", "closeSubscriber", "sub", "computeMinHead", "elementAt", "index", "(J)Ljava/lang/Object;", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/selects/SelectInstance;)Ljava/lang/Object;", "open", "Lkotlinx/coroutines/experimental/channels/SubscriptionReceiveChannel;", "updateHead", "Subscriber", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: ArrayBroadcastChannel.kt */
public final class ArrayBroadcastChannel<E> extends AbstractSendChannel<E> implements BroadcastChannel<E> {
    private final Object[] buffer;
    private final ReentrantLock bufferLock;
    private final int capacity;
    private volatile long head;
    private volatile int size;
    private final CopyOnWriteArrayList<Subscriber<E>> subs;
    /* access modifiers changed from: private */
    public volatile long tail;

    /* access modifiers changed from: protected */
    public boolean isBufferAlwaysFull() {
        return false;
    }

    public final int getCapacity() {
        return this.capacity;
    }

    public ArrayBroadcastChannel(int i) {
        this.capacity = i;
        if (this.capacity < 1 ? false : true) {
            this.bufferLock = new ReentrantLock();
            this.buffer = new Object[this.capacity];
            this.subs = new CopyOnWriteArrayList<>();
            return;
        }
        throw new IllegalStateException(("ArrayBroadcastChannel capacity must be at least 1, but " + this.capacity + " was specified").toString());
    }

    /* access modifiers changed from: protected */
    public boolean isBufferFull() {
        return this.size >= this.capacity;
    }

    public SubscriptionReceiveChannel<E> open() {
        Subscriber subscriber = new Subscriber(this, this.head);
        this.subs.add(subscriber);
        long j = this.head;
        if (j != subscriber.subHead) {
            subscriber.subHead = j;
            updateHead();
        }
        return subscriber;
    }

    public boolean close(Throwable th) {
        if (!super.close(th)) {
            return false;
        }
        checkSubOffers();
        return true;
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E e) {
        Lock lock = this.bufferLock;
        lock.lock();
        try {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            int i = this.size;
            if (i >= this.capacity) {
                Object obj = AbstractChannelKt.OFFER_FAILED;
                lock.unlock();
                return obj;
            }
            long j = this.tail;
            this.buffer[(int) (j % ((long) this.capacity))] = e;
            this.size = i + 1;
            this.tail = j + ((long) 1);
            Unit unit = Unit.INSTANCE;
            lock.unlock();
            checkSubOffers();
            return AbstractChannelKt.OFFER_SUCCESS;
        } finally {
            lock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Lock lock = this.bufferLock;
        lock.lock();
        try {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend != null) {
                return closedForSend;
            }
            int i = this.size;
            if (i >= this.capacity) {
                Object obj = AbstractChannelKt.OFFER_FAILED;
                lock.unlock();
                return obj;
            } else if (!selectInstance.trySelect((Object) null)) {
                Object already_selected = JobKt.getALREADY_SELECTED();
                lock.unlock();
                return already_selected;
            } else {
                long j = this.tail;
                this.buffer[(int) (j % ((long) this.capacity))] = e;
                this.size = i + 1;
                this.tail = j + ((long) 1);
                Unit unit = Unit.INSTANCE;
                lock.unlock();
                checkSubOffers();
                return AbstractChannelKt.OFFER_SUCCESS;
            }
        } finally {
            lock.unlock();
        }
    }

    /* access modifiers changed from: private */
    public final void closeSubscriber(Subscriber<E> subscriber) {
        this.subs.remove(subscriber);
        if (this.head == subscriber.subHead) {
            updateHead();
        }
    }

    private final void checkSubOffers() {
        Iterator<Subscriber<E>> it = this.subs.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (it.next().checkOffer()) {
                z = true;
            }
        }
        if (z) {
            updateHead();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007c, code lost:
        r4 = r1.buffer;
        r10 = (int) (r7 % ((long) r1.capacity));
        r9 = (kotlinx.coroutines.experimental.channels.Send) r0.element;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0088, code lost:
        if (r9 == null) goto L_0x00b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008a, code lost:
        r4[r10] = r9.getPollResult();
        r1.size = r13 + 1;
        r1.tail = r7 + ((long) 1);
        r4 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x009a, code lost:
        r6.unlock();
        r0 = (kotlinx.coroutines.experimental.channels.Send) r0.element;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a1, code lost:
        if (r0 != null) goto L_0x00a6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a3, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a6, code lost:
        r4 = r15.element;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a8, code lost:
        if (r4 != null) goto L_0x00ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00aa, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00bc, code lost:
        throw new kotlin.TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.channels.Send");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateHead() {
        /*
            r17 = this;
            r1 = r17
            long r2 = r17.computeMinHead()
        L_0x0006:
            kotlin.jvm.internal.Ref$ObjectRef r0 = new kotlin.jvm.internal.Ref$ObjectRef
            r0.<init>()
            r4 = 0
            r5 = r4
            kotlinx.coroutines.experimental.channels.Send r5 = (kotlinx.coroutines.experimental.channels.Send) r5
            r0.element = r5
            kotlin.jvm.internal.Ref$ObjectRef r5 = new kotlin.jvm.internal.Ref$ObjectRef
            r5.<init>()
            r5.element = r4
            java.util.concurrent.locks.ReentrantLock r6 = r1.bufferLock
            java.util.concurrent.locks.Lock r6 = (java.util.concurrent.locks.Lock) r6
            r6.lock()
            long r7 = r1.tail     // Catch:{ all -> 0x00cb }
            long r9 = r1.head     // Catch:{ all -> 0x00cb }
            long r11 = kotlin.ranges.RangesKt.coerceAtMost((long) r2, (long) r7)     // Catch:{ all -> 0x00cb }
            int r13 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r13 > 0) goto L_0x002f
            r6.unlock()
            return
        L_0x002f:
            int r13 = r1.size     // Catch:{ all -> 0x00cb }
        L_0x0031:
            int r14 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r14 >= 0) goto L_0x00c7
            java.lang.Object[] r14 = r1.buffer     // Catch:{ all -> 0x00cb }
            int r15 = r1.capacity     // Catch:{ all -> 0x00cb }
            r16 = r5
            long r4 = (long) r15     // Catch:{ all -> 0x00cb }
            long r4 = r9 % r4
            int r5 = (int) r4     // Catch:{ all -> 0x00cb }
            r4 = 0
            r14[r5] = r4     // Catch:{ all -> 0x00cb }
            int r4 = r1.capacity     // Catch:{ all -> 0x00cb }
            r5 = 1
            if (r13 < r4) goto L_0x0049
            r4 = 1
            goto L_0x004a
        L_0x0049:
            r4 = 0
        L_0x004a:
            r14 = 1
            long r9 = r9 + r14
            r1.head = r9     // Catch:{ all -> 0x00cb }
            int r13 = r13 + -1
            r1.size = r13     // Catch:{ all -> 0x00cb }
            if (r4 == 0) goto L_0x00c0
        L_0x0055:
            kotlinx.coroutines.experimental.channels.Send r4 = r17.takeFirstSendOrPeekClosed()     // Catch:{ all -> 0x00cb }
            if (r4 == 0) goto L_0x00c0
            r0.element = r4     // Catch:{ all -> 0x00cb }
            T r4 = r0.element     // Catch:{ all -> 0x00cb }
            kotlinx.coroutines.experimental.channels.Send r4 = (kotlinx.coroutines.experimental.channels.Send) r4     // Catch:{ all -> 0x00cb }
            boolean r4 = r4 instanceof kotlinx.coroutines.experimental.channels.Closed     // Catch:{ all -> 0x00cb }
            if (r4 == 0) goto L_0x0066
            goto L_0x00c0
        L_0x0066:
            T r4 = r0.element     // Catch:{ all -> 0x00cb }
            kotlinx.coroutines.experimental.channels.Send r4 = (kotlinx.coroutines.experimental.channels.Send) r4     // Catch:{ all -> 0x00cb }
            if (r4 != 0) goto L_0x006f
            kotlin.jvm.internal.Intrinsics.throwNpe()     // Catch:{ all -> 0x00cb }
        L_0x006f:
            r14 = 0
            java.lang.Object r4 = r4.tryResumeSend(r14)     // Catch:{ all -> 0x00cb }
            r15 = r16
            r15.element = r4     // Catch:{ all -> 0x00cb }
            T r4 = r15.element     // Catch:{ all -> 0x00cb }
            if (r4 == 0) goto L_0x00bd
            java.lang.Object[] r4 = r1.buffer     // Catch:{ all -> 0x00cb }
            int r9 = r1.capacity     // Catch:{ all -> 0x00cb }
            long r9 = (long) r9     // Catch:{ all -> 0x00cb }
            long r9 = r7 % r9
            int r10 = (int) r9     // Catch:{ all -> 0x00cb }
            T r9 = r0.element     // Catch:{ all -> 0x00cb }
            kotlinx.coroutines.experimental.channels.Send r9 = (kotlinx.coroutines.experimental.channels.Send) r9     // Catch:{ all -> 0x00cb }
            if (r9 == 0) goto L_0x00b5
            java.lang.Object r9 = r9.getPollResult()     // Catch:{ all -> 0x00cb }
            r4[r10] = r9     // Catch:{ all -> 0x00cb }
            int r13 = r13 + 1
            r1.size = r13     // Catch:{ all -> 0x00cb }
            long r4 = (long) r5     // Catch:{ all -> 0x00cb }
            long r7 = r7 + r4
            r1.tail = r7     // Catch:{ all -> 0x00cb }
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00cb }
            r6.unlock()
            T r0 = r0.element
            kotlinx.coroutines.experimental.channels.Send r0 = (kotlinx.coroutines.experimental.channels.Send) r0
            if (r0 != 0) goto L_0x00a6
            kotlin.jvm.internal.Intrinsics.throwNpe()
        L_0x00a6:
            T r4 = r15.element
            if (r4 != 0) goto L_0x00ad
            kotlin.jvm.internal.Intrinsics.throwNpe()
        L_0x00ad:
            r0.completeResumeSend(r4)
            r17.checkSubOffers()
            goto L_0x0006
        L_0x00b5:
            kotlin.TypeCastException r0 = new kotlin.TypeCastException     // Catch:{ all -> 0x00cb }
            java.lang.String r2 = "null cannot be cast to non-null type kotlinx.coroutines.experimental.channels.Send"
            r0.<init>(r2)     // Catch:{ all -> 0x00cb }
            throw r0     // Catch:{ all -> 0x00cb }
        L_0x00bd:
            r16 = r15
            goto L_0x0055
        L_0x00c0:
            r15 = r16
            r14 = 0
            r4 = r14
            r5 = r15
            goto L_0x0031
        L_0x00c7:
            r6.unlock()
            return
        L_0x00cb:
            r0 = move-exception
            r6.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.ArrayBroadcastChannel.updateHead():void");
    }

    private final long computeMinHead() {
        Iterator<Subscriber<E>> it = this.subs.iterator();
        long j = LongCompanionObject.MAX_VALUE;
        while (it.hasNext()) {
            j = RangesKt.coerceAtMost(j, it.next().subHead);
        }
        return j;
    }

    /* access modifiers changed from: private */
    public final E elementAt(long j) {
        return this.buffer[(int) (j % ((long) this.capacity))];
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u001b\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0011\u001a\u00020\nH\u0007J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\nH\u0002J\n\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J\n\u0010\u0017\u001a\u0004\u0018\u00010\u0016H\u0014J\u0016\u0010\u0018\u001a\u0004\u0018\u00010\u00162\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001aH\u0014R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00010\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8TX\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u0014\u0010\f\u001a\u00020\n8TX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000bR\u0014\u0010\r\u001a\u00020\n8TX\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0014\u0010\u000e\u001a\u00020\n8TX\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000bR\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ArrayBroadcastChannel$Subscriber;", "E", "Lkotlinx/coroutines/experimental/channels/AbstractChannel;", "Lkotlinx/coroutines/experimental/channels/SubscriptionReceiveChannel;", "broadcastChannel", "Lkotlinx/coroutines/experimental/channels/ArrayBroadcastChannel;", "subHead", "", "(Lkotlinx/coroutines/experimental/channels/ArrayBroadcastChannel;J)V", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "checkOffer", "close", "", "needsToCheckOfferWithoutLock", "peekUnderLock", "", "pollInternal", "pollSelectInternal", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: ArrayBroadcastChannel.kt */
    private static final class Subscriber<E> extends AbstractChannel<E> implements SubscriptionReceiveChannel<E> {
        private final ArrayBroadcastChannel<E> broadcastChannel;
        private final ReentrantLock lock = new ReentrantLock();
        public volatile long subHead;

        /* access modifiers changed from: protected */
        public boolean isBufferAlwaysEmpty() {
            return false;
        }

        public Subscriber(ArrayBroadcastChannel<E> arrayBroadcastChannel, long j) {
            Intrinsics.checkParameterIsNotNull(arrayBroadcastChannel, "broadcastChannel");
            this.broadcastChannel = arrayBroadcastChannel;
            this.subHead = j;
        }

        /* access modifiers changed from: protected */
        public boolean isBufferEmpty() {
            return this.subHead >= this.broadcastChannel.tail;
        }

        /* access modifiers changed from: protected */
        public boolean isBufferAlwaysFull() {
            throw new IllegalStateException("Should not be used".toString());
        }

        /* access modifiers changed from: protected */
        public boolean isBufferFull() {
            throw new IllegalStateException("Should not be used".toString());
        }

        public void close() {
            if (close((Throwable) null)) {
                this.broadcastChannel.closeSubscriber(this);
            }
        }

        public final boolean checkOffer() {
            Closed closed = null;
            boolean z = false;
            while (true) {
                if (!needsToCheckOfferWithoutLock() || !this.lock.tryLock()) {
                    break;
                }
                try {
                    Object peekUnderLock = peekUnderLock();
                    if (peekUnderLock != AbstractChannelKt.POLL_FAILED) {
                        if (peekUnderLock instanceof Closed) {
                            closed = peekUnderLock;
                            break;
                        }
                        ReceiveOrClosed takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                        if (takeFirstReceiveOrPeekClosed == null) {
                            break;
                        } else if (takeFirstReceiveOrPeekClosed instanceof Closed) {
                            break;
                        } else {
                            Object tryResumeReceive = takeFirstReceiveOrPeekClosed.tryResumeReceive(peekUnderLock, (Object) null);
                            if (tryResumeReceive != null) {
                                this.subHead += (long) 1;
                                this.lock.unlock();
                                if (takeFirstReceiveOrPeekClosed == null) {
                                    Intrinsics.throwNpe();
                                }
                                takeFirstReceiveOrPeekClosed.completeResumeReceive(tryResumeReceive);
                                z = true;
                            }
                        }
                    }
                } finally {
                    this.lock.unlock();
                }
            }
            this.lock.unlock();
            if (closed != null) {
                close(closed.closeCause);
            }
            return z;
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0025  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0027  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x002c  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0037  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x003a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object pollInternal() {
            /*
                r7 = this;
                java.util.concurrent.locks.ReentrantLock r0 = r7.lock
                r0.lock()
                java.lang.Object r0 = r7.peekUnderLock()     // Catch:{ all -> 0x0040 }
                boolean r1 = r0 instanceof kotlinx.coroutines.experimental.channels.Closed     // Catch:{ all -> 0x0040 }
                r2 = 1
                if (r1 == 0) goto L_0x000f
                goto L_0x0013
            L_0x000f:
                java.lang.Object r1 = kotlinx.coroutines.experimental.channels.AbstractChannelKt.POLL_FAILED     // Catch:{ all -> 0x0040 }
                if (r0 != r1) goto L_0x0015
            L_0x0013:
                r1 = 0
                goto L_0x001c
            L_0x0015:
                long r3 = r7.subHead     // Catch:{ all -> 0x0040 }
                long r5 = (long) r2     // Catch:{ all -> 0x0040 }
                long r3 = r3 + r5
                r7.subHead = r3     // Catch:{ all -> 0x0040 }
                r1 = 1
            L_0x001c:
                java.util.concurrent.locks.ReentrantLock r3 = r7.lock
                r3.unlock()
                boolean r3 = r0 instanceof kotlinx.coroutines.experimental.channels.Closed
                if (r3 != 0) goto L_0x0027
                r3 = 0
                goto L_0x0028
            L_0x0027:
                r3 = r0
            L_0x0028:
                kotlinx.coroutines.experimental.channels.Closed r3 = (kotlinx.coroutines.experimental.channels.Closed) r3
                if (r3 == 0) goto L_0x0031
                java.lang.Throwable r3 = r3.closeCause
                r7.close(r3)
            L_0x0031:
                boolean r3 = r7.checkOffer()
                if (r3 == 0) goto L_0x0038
                r1 = 1
            L_0x0038:
                if (r1 == 0) goto L_0x003f
                kotlinx.coroutines.experimental.channels.ArrayBroadcastChannel<E> r1 = r7.broadcastChannel
                r1.updateHead()
            L_0x003f:
                return r0
            L_0x0040:
                r0 = move-exception
                java.util.concurrent.locks.ReentrantLock r1 = r7.lock
                r1.unlock()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.ArrayBroadcastChannel.Subscriber.pollInternal():java.lang.Object");
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: protected */
        public Object pollSelectInternal(SelectInstance<?> selectInstance) {
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            this.lock.lock();
            try {
                Object peekUnderLock = peekUnderLock();
                Closed closed = null;
                boolean z = true;
                boolean z2 = false;
                if (!(peekUnderLock instanceof Closed)) {
                    if (peekUnderLock != AbstractChannelKt.POLL_FAILED) {
                        if (!selectInstance.trySelect((Object) null)) {
                            peekUnderLock = JobKt.getALREADY_SELECTED();
                        } else {
                            this.subHead += (long) 1;
                            z2 = true;
                        }
                    }
                }
                this.lock.unlock();
                if (peekUnderLock instanceof Closed) {
                    closed = peekUnderLock;
                }
                Closed closed2 = closed;
                if (closed2 != null) {
                    close(closed2.closeCause);
                }
                if (!checkOffer()) {
                    z = z2;
                }
                if (z) {
                    this.broadcastChannel.updateHead();
                }
                return peekUnderLock;
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }

        private final boolean needsToCheckOfferWithoutLock() {
            if (getClosedForReceive() != null) {
                return false;
            }
            if (!isBufferEmpty() || this.broadcastChannel.getClosedForReceive() != null) {
                return true;
            }
            return false;
        }

        private final Object peekUnderLock() {
            long j = this.subHead;
            Closed<?> closedForReceive = this.broadcastChannel.getClosedForReceive();
            if (j >= this.broadcastChannel.tail) {
                return closedForReceive != null ? closedForReceive : AbstractChannelKt.POLL_FAILED;
            }
            return this.broadcastChannel.elementAt(j);
        }
    }
}
