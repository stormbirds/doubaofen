package io.reactivex.internal.operators.parallel;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class ParallelSortedJoin<T> extends Flowable<T> {
    final Comparator<? super T> comparator;
    final ParallelFlowable<List<T>> source;

    public ParallelSortedJoin(ParallelFlowable<List<T>> parallelFlowable, Comparator<? super T> comparator2) {
        this.source = parallelFlowable;
        this.comparator = comparator2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        SortedJoinSubscription sortedJoinSubscription = new SortedJoinSubscription(subscriber, this.source.parallelism(), this.comparator);
        subscriber.onSubscribe(sortedJoinSubscription);
        this.source.subscribe(sortedJoinSubscription.subscribers);
    }

    static final class SortedJoinSubscription<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 3481980673745556697L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final Comparator<? super T> comparator;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final int[] indexes;
        final List<T>[] lists;
        final AtomicInteger remaining = new AtomicInteger();
        final AtomicLong requested = new AtomicLong();
        final SortedJoinInnerSubscriber<T>[] subscribers;

        SortedJoinSubscription(Subscriber<? super T> subscriber, int i, Comparator<? super T> comparator2) {
            this.actual = subscriber;
            this.comparator = comparator2;
            SortedJoinInnerSubscriber<T>[] sortedJoinInnerSubscriberArr = new SortedJoinInnerSubscriber[i];
            for (int i2 = 0; i2 < i; i2++) {
                sortedJoinInnerSubscriberArr[i2] = new SortedJoinInnerSubscriber<>(this, i2);
            }
            this.subscribers = sortedJoinInnerSubscriberArr;
            this.lists = new List[i];
            this.indexes = new int[i];
            this.remaining.lazySet(i);
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                if (this.remaining.get() == 0) {
                    drain();
                }
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    Arrays.fill(this.lists, (Object) null);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void cancelAll() {
            for (SortedJoinInnerSubscriber<T> cancel : this.subscribers) {
                cancel.cancel();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext(List<T> list, int i) {
            this.lists[i] = list;
            if (this.remaining.decrementAndGet() == 0) {
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable th) {
            if (this.error.compareAndSet((Object) null, th)) {
                drain();
            } else if (th != this.error.get()) {
                RxJavaPlugins.onError(th);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a3, code lost:
            if (r15 != 0) goto L_0x00df;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a7, code lost:
            if (r1.cancelled == false) goto L_0x00ad;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a9, code lost:
            java.util.Arrays.fill(r3, (java.lang.Object) null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x00ac, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x00ad, code lost:
            r5 = r1.error.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x00b5, code lost:
            if (r5 == null) goto L_0x00c1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x00b7, code lost:
            cancelAll();
            java.util.Arrays.fill(r3, (java.lang.Object) null);
            r2.onError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00c0, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x00c1, code lost:
            r5 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c2, code lost:
            if (r5 >= r4) goto L_0x00d4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x00cc, code lost:
            if (r0[r5] == r3[r5].size()) goto L_0x00d1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00ce, code lost:
            r16 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00d1, code lost:
            r5 = r5 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d4, code lost:
            r16 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d6, code lost:
            if (r16 == false) goto L_0x00df;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00d8, code lost:
            java.util.Arrays.fill(r3, (java.lang.Object) null);
            r2.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00de, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00e3, code lost:
            if (r11 == 0) goto L_0x00f4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ec, code lost:
            if (r7 == kotlin.jvm.internal.LongCompanionObject.MAX_VALUE) goto L_0x00f4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x00ee, code lost:
            r1.requested.addAndGet(-r11);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x00f4, code lost:
            r5 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x00f8, code lost:
            if (r5 != r6) goto L_0x0102;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x00fa, code lost:
            r5 = addAndGet(-r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x00ff, code lost:
            if (r5 != 0) goto L_0x0102;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x0101, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r18 = this;
                r1 = r18
                int r0 = r18.getAndIncrement()
                if (r0 == 0) goto L_0x0009
                return
            L_0x0009:
                org.reactivestreams.Subscriber<? super T> r2 = r1.actual
                java.util.List<T>[] r3 = r1.lists
                int[] r0 = r1.indexes
                int r4 = r0.length
                r6 = 1
            L_0x0011:
                java.util.concurrent.atomic.AtomicLong r7 = r1.requested
                long r7 = r7.get()
                r11 = 0
            L_0x0019:
                r14 = 0
                int r15 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1))
                if (r15 == 0) goto L_0x00a2
                boolean r15 = r1.cancelled
                if (r15 == 0) goto L_0x0026
                java.util.Arrays.fill(r3, r14)
                return
            L_0x0026:
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r15 = r1.error
                java.lang.Object r15 = r15.get()
                java.lang.Throwable r15 = (java.lang.Throwable) r15
                if (r15 == 0) goto L_0x003a
                r18.cancelAll()
                java.util.Arrays.fill(r3, r14)
                r2.onError(r15)
                return
            L_0x003a:
                r15 = -1
                r13 = r14
                r15 = 0
                r17 = -1
            L_0x003f:
                if (r15 >= r4) goto L_0x008b
                r9 = r3[r15]
                r10 = r0[r15]
                int r5 = r9.size()
                if (r5 == r10) goto L_0x0088
                if (r13 != 0) goto L_0x0055
                java.lang.Object r5 = r9.get(r10)
            L_0x0051:
                r13 = r5
                r17 = r15
                goto L_0x0088
            L_0x0055:
                java.lang.Object r5 = r9.get(r10)
                java.util.Comparator<? super T> r9 = r1.comparator     // Catch:{ all -> 0x0067 }
                int r9 = r9.compare(r13, r5)     // Catch:{ all -> 0x0067 }
                if (r9 <= 0) goto L_0x0063
                r9 = 1
                goto L_0x0064
            L_0x0063:
                r9 = 0
            L_0x0064:
                if (r9 == 0) goto L_0x0088
                goto L_0x0051
            L_0x0067:
                r0 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r18.cancelAll()
                java.util.Arrays.fill(r3, r14)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r3 = r1.error
                boolean r3 = r3.compareAndSet(r14, r0)
                if (r3 != 0) goto L_0x007c
                io.reactivex.plugins.RxJavaPlugins.onError(r0)
            L_0x007c:
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r0 = r1.error
                java.lang.Object r0 = r0.get()
                java.lang.Throwable r0 = (java.lang.Throwable) r0
                r2.onError(r0)
                return
            L_0x0088:
                int r15 = r15 + 1
                goto L_0x003f
            L_0x008b:
                if (r13 != 0) goto L_0x0094
                java.util.Arrays.fill(r3, r14)
                r2.onComplete()
                return
            L_0x0094:
                r2.onNext(r13)
                r5 = r0[r17]
                r9 = 1
                int r5 = r5 + r9
                r0[r17] = r5
                r13 = 1
                long r11 = r11 + r13
                goto L_0x0019
            L_0x00a2:
                r9 = 1
                if (r15 != 0) goto L_0x00df
                boolean r5 = r1.cancelled
                if (r5 == 0) goto L_0x00ad
                java.util.Arrays.fill(r3, r14)
                return
            L_0x00ad:
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r5 = r1.error
                java.lang.Object r5 = r5.get()
                java.lang.Throwable r5 = (java.lang.Throwable) r5
                if (r5 == 0) goto L_0x00c1
                r18.cancelAll()
                java.util.Arrays.fill(r3, r14)
                r2.onError(r5)
                return
            L_0x00c1:
                r5 = 0
            L_0x00c2:
                if (r5 >= r4) goto L_0x00d4
                r10 = r0[r5]
                r13 = r3[r5]
                int r13 = r13.size()
                if (r10 == r13) goto L_0x00d1
                r16 = 0
                goto L_0x00d6
            L_0x00d1:
                int r5 = r5 + 1
                goto L_0x00c2
            L_0x00d4:
                r16 = 1
            L_0x00d6:
                if (r16 == 0) goto L_0x00df
                java.util.Arrays.fill(r3, r14)
                r2.onComplete()
                return
            L_0x00df:
                r13 = 0
                int r5 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
                if (r5 == 0) goto L_0x00f4
                r13 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r5 = (r7 > r13 ? 1 : (r7 == r13 ? 0 : -1))
                if (r5 == 0) goto L_0x00f4
                java.util.concurrent.atomic.AtomicLong r5 = r1.requested
                long r7 = -r11
                r5.addAndGet(r7)
            L_0x00f4:
                int r5 = r18.get()
                if (r5 != r6) goto L_0x0102
                int r5 = -r6
                int r5 = r1.addAndGet(r5)
                if (r5 != 0) goto L_0x0102
                return
            L_0x0102:
                r6 = r5
                goto L_0x0011
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.parallel.ParallelSortedJoin.SortedJoinSubscription.drain():void");
        }
    }

    static final class SortedJoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<List<T>> {
        private static final long serialVersionUID = 6751017204873808094L;
        final int index;
        final SortedJoinSubscription<T> parent;

        public void onComplete() {
        }

        SortedJoinInnerSubscriber(SortedJoinSubscription<T> sortedJoinSubscription, int i) {
            this.parent = sortedJoinSubscription;
            this.index = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this, subscription)) {
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(List<T> list) {
            this.parent.innerNext(list, this.index);
        }

        public void onError(Throwable th) {
            this.parent.innerError(th);
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            SubscriptionHelper.cancel(this);
        }
    }
}
