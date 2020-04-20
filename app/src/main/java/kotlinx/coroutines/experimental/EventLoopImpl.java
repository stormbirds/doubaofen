package kotlinx.coroutines.experimental;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlinx.coroutines.experimental.Delay;
import kotlinx.coroutines.experimental.DisposableHandle;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0005*+,-.B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\rJ \u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u001e\u001a\u00020\u001bH\u0016J\u0014\u0010\u001f\u001a\u00020 2\n\u0010!\u001a\u00060\tR\u00020\u0000H\u0002J\u0010\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020$H\u0002J&\u0010%\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00110'H\u0016J\u0006\u0010(\u001a\u00020\u0011J\b\u0010)\u001a\u00020\u0011H\u0002R\"\u0010\u0007\u001a\u0016\u0012\b\u0012\u00060\tR\u00020\u0000\u0012\b\u0012\u00060\tR\u00020\u00000\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, d2 = {"Lkotlinx/coroutines/experimental/EventLoopImpl;", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "Lkotlinx/coroutines/experimental/EventLoop;", "Lkotlinx/coroutines/experimental/Delay;", "thread", "Ljava/lang/Thread;", "(Ljava/lang/Thread;)V", "delayed", "Ljava/util/concurrent/ConcurrentSkipListMap;", "Lkotlinx/coroutines/experimental/EventLoopImpl$DelayedTask;", "nextSequence", "Ljava/util/concurrent/atomic/AtomicLong;", "parentJob", "Lkotlinx/coroutines/experimental/Job;", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "dispatch", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "block", "Ljava/lang/Runnable;", "initParentJob", "coroutine", "invokeOnTimeout", "Lkotlinx/coroutines/experimental/DisposableHandle;", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "processNextEvent", "scheduleDelayed", "", "delayedTask", "scheduleQueued", "queuedTask", "Lkotlinx/coroutines/experimental/EventLoopImpl$QueuedTask;", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "shutdown", "unpark", "DelayedResumeTask", "DelayedRunnableTask", "DelayedTask", "QueuedRunnableTask", "QueuedTask", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: EventLoop.kt */
public final class EventLoopImpl extends CoroutineDispatcher implements EventLoop, Delay {
    /* access modifiers changed from: private */
    public final ConcurrentSkipListMap<DelayedTask, DelayedTask> delayed = new ConcurrentSkipListMap<>();
    /* access modifiers changed from: private */
    public final AtomicLong nextSequence = new AtomicLong();
    /* access modifiers changed from: private */
    public Job parentJob;
    private final LockFreeLinkedListHead queue = new LockFreeLinkedListHead();
    private final Thread thread;

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0005¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/experimental/EventLoopImpl$QueuedTask;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlin/Function0;", "", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: EventLoop.kt */
    private static abstract class QueuedTask extends LockFreeLinkedListNode implements Function0<Unit> {
    }

    public Object delay(long j, TimeUnit timeUnit, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return Delay.DefaultImpls.delay(this, j, timeUnit, continuation);
    }

    public EventLoopImpl(Thread thread2) {
        Intrinsics.checkParameterIsNotNull(thread2, "thread");
        this.thread = thread2;
    }

    public final void initParentJob(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "coroutine");
        if (this.parentJob == null) {
            this.parentJob = job;
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        if (scheduleQueued(new QueuedRunnableTask(runnable))) {
            unpark();
        } else {
            runnable.run();
        }
    }

    public void scheduleResumeAfterDelay(long j, TimeUnit timeUnit, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        if (scheduleDelayed(new DelayedResumeTask(this, j, timeUnit, cancellableContinuation))) {
            unpark();
        } else {
            ScheduledKt.getScheduledExecutor().schedule(new ResumeRunnable(cancellableContinuation), j, timeUnit);
        }
    }

    public DisposableHandle invokeOnTimeout(long j, TimeUnit timeUnit, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        DelayedRunnableTask delayedRunnableTask = new DelayedRunnableTask(this, j, timeUnit, runnable);
        scheduleDelayed(delayedRunnableTask);
        return delayedRunnableTask;
    }

    public long processNextEvent() {
        DelayedTask key;
        DelayedTask key2;
        if (Thread.currentThread() != this.thread) {
            return LongCompanionObject.MAX_VALUE;
        }
        while (true) {
            Map.Entry<DelayedTask, DelayedTask> firstEntry = this.delayed.firstEntry();
            if (firstEntry != null && (key2 = firstEntry.getKey()) != null) {
                if (key2.nanoTime - System.nanoTime() > ((long) 0) || !scheduleQueued(key2)) {
                    break;
                }
                this.delayed.remove(key2);
            } else {
                break;
            }
        }
        LockFreeLinkedListNode removeFirstOrNull = this.queue.removeFirstOrNull();
        if (!(removeFirstOrNull instanceof QueuedTask)) {
            removeFirstOrNull = null;
        }
        QueuedTask queuedTask = (QueuedTask) removeFirstOrNull;
        if (queuedTask != null) {
            queuedTask.invoke();
        }
        if (!this.queue.isEmpty()) {
            return 0;
        }
        Map.Entry<DelayedTask, DelayedTask> firstEntry2 = this.delayed.firstEntry();
        if (firstEntry2 == null || (key = firstEntry2.getKey()) == null) {
            return LongCompanionObject.MAX_VALUE;
        }
        return key.nanoTime - System.nanoTime();
    }

    public final void shutdown() {
        DelayedTask key;
        while (true) {
            LockFreeLinkedListNode removeFirstOrNull = this.queue.removeFirstOrNull();
            if (removeFirstOrNull == null) {
                while (true) {
                    Map.Entry<DelayedTask, DelayedTask> pollFirstEntry = this.delayed.pollFirstEntry();
                    if (pollFirstEntry != null && (key = pollFirstEntry.getKey()) != null) {
                        key.cancel();
                    } else {
                        return;
                    }
                }
            } else if (removeFirstOrNull != null) {
                ((QueuedTask) removeFirstOrNull).invoke();
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.EventLoopImpl.QueuedTask");
            }
        }
    }

    private final boolean scheduleQueued(QueuedTask queuedTask) {
        int tryCondAddNext;
        if (this.parentJob == null) {
            this.queue.addLast(queuedTask);
            return true;
        }
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        LockFreeLinkedListNode lockFreeLinkedListNode = queuedTask;
        LockFreeLinkedListNode.CondAddOp eventLoopImpl$scheduleQueued$$inlined$addLastIf$1 = new EventLoopImpl$scheduleQueued$$inlined$addLastIf$1(lockFreeLinkedListNode, lockFreeLinkedListNode, this);
        do {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(lockFreeLinkedListNode, lockFreeLinkedListHead, eventLoopImpl$scheduleQueued$$inlined$addLastIf$1);
                if (tryCondAddNext == 1) {
                    return true;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (tryCondAddNext != 2);
        return false;
    }

    private final boolean scheduleDelayed(DelayedTask delayedTask) {
        this.delayed.put(delayedTask, delayedTask);
        Job job = this.parentJob;
        if (!Intrinsics.areEqual((Object) job != null ? Boolean.valueOf(job.isActive()) : null, (Object) false)) {
            return true;
        }
        delayedTask.dispose();
        return false;
    }

    private final void unpark() {
        Thread currentThread = Thread.currentThread();
        Thread thread2 = this.thread;
        if (currentThread != thread2) {
            LockSupport.unpark(thread2);
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0005\u001a\u00020\u0006H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/EventLoopImpl$QueuedRunnableTask;", "Lkotlinx/coroutines/experimental/EventLoopImpl$QueuedTask;", "block", "Ljava/lang/Runnable;", "(Ljava/lang/Runnable;)V", "invoke", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: EventLoop.kt */
    private static final class QueuedRunnableTask extends QueuedTask {
        private final Runnable block;

        public QueuedRunnableTask(Runnable runnable) {
            Intrinsics.checkParameterIsNotNull(runnable, "block");
            this.block = runnable;
        }

        public void invoke() {
            this.block.run();
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b¢\u0004\u0018\u00002\u00020\u00012\f\u0012\b\u0012\u00060\u0000R\u00020\u00030\u00022\u00020\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\f\u001a\u00020\rH\u0016J\u0015\u0010\u000e\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0000R\u00020\u0003H\u0002J\u0006\u0010\u0011\u001a\u00020\rR\u0010\u0010\n\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/experimental/EventLoopImpl$DelayedTask;", "Lkotlinx/coroutines/experimental/EventLoopImpl$QueuedTask;", "", "Lkotlinx/coroutines/experimental/EventLoopImpl;", "Lkotlinx/coroutines/experimental/DisposableHandle;", "time", "", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "(Lkotlinx/coroutines/experimental/EventLoopImpl;JLjava/util/concurrent/TimeUnit;)V", "nanoTime", "sequence", "cancel", "", "compareTo", "", "other", "dispose", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: EventLoop.kt */
    private abstract class DelayedTask extends QueuedTask implements Comparable<DelayedTask>, DisposableHandle {
        public final long nanoTime;
        public final long sequence;
        final /* synthetic */ EventLoopImpl this$0;

        public void cancel() {
        }

        public DelayedTask(EventLoopImpl eventLoopImpl, long j, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
            this.this$0 = eventLoopImpl;
            this.nanoTime = System.nanoTime() + timeUnit.toNanos(j);
            this.sequence = eventLoopImpl.nextSequence.getAndIncrement();
        }

        @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
        public void unregister() {
            DisposableHandle.DefaultImpls.unregister(this);
        }

        public int compareTo(DelayedTask delayedTask) {
            Intrinsics.checkParameterIsNotNull(delayedTask, "other");
            long j = (long) 0;
            int i = ((this.nanoTime - delayedTask.nanoTime) > j ? 1 : ((this.nanoTime - delayedTask.nanoTime) == j ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            if (i < 0) {
                return -1;
            }
            int i2 = ((this.sequence - delayedTask.sequence) > j ? 1 : ((this.sequence - delayedTask.sequence) == j ? 0 : -1));
            if (i2 > 0) {
                return 1;
            }
            if (i2 < 0) {
                return -1;
            }
            return 0;
        }

        public final void dispose() {
            this.this$0.delayed.remove(this);
            cancel();
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\tH\u0016J\t\u0010\f\u001a\u00020\tH\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/experimental/EventLoopImpl$DelayedResumeTask;", "Lkotlinx/coroutines/experimental/EventLoopImpl$DelayedTask;", "Lkotlinx/coroutines/experimental/EventLoopImpl;", "time", "", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "cont", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "", "(Lkotlinx/coroutines/experimental/EventLoopImpl;JLjava/util/concurrent/TimeUnit;Lkotlinx/coroutines/experimental/CancellableContinuation;)V", "cancel", "invoke", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: EventLoop.kt */
    private final class DelayedResumeTask extends DelayedTask {
        private final CancellableContinuation<Unit> cont;
        final /* synthetic */ EventLoopImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public DelayedResumeTask(EventLoopImpl eventLoopImpl, long j, TimeUnit timeUnit, CancellableContinuation<? super Unit> cancellableContinuation) {
            super(eventLoopImpl, j, timeUnit);
            Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.this$0 = eventLoopImpl;
            this.cont = cancellableContinuation;
        }

        public void invoke() {
            this.cont.resumeUndispatched(this.this$0, Unit.INSTANCE);
        }

        public void cancel() {
            if (this.cont.isActive()) {
                ScheduledKt.getScheduledExecutor().schedule(new ResumeRunnable(this.cont), this.nanoTime - System.nanoTime(), TimeUnit.NANOSECONDS);
            }
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\t\u0010\n\u001a\u00020\u000bH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/experimental/EventLoopImpl$DelayedRunnableTask;", "Lkotlinx/coroutines/experimental/EventLoopImpl$DelayedTask;", "Lkotlinx/coroutines/experimental/EventLoopImpl;", "time", "", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "block", "Ljava/lang/Runnable;", "(Lkotlinx/coroutines/experimental/EventLoopImpl;JLjava/util/concurrent/TimeUnit;Ljava/lang/Runnable;)V", "invoke", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: EventLoop.kt */
    private final class DelayedRunnableTask extends DelayedTask {
        private final Runnable block;
        final /* synthetic */ EventLoopImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public DelayedRunnableTask(EventLoopImpl eventLoopImpl, long j, TimeUnit timeUnit, Runnable runnable) {
            super(eventLoopImpl, j, timeUnit);
            Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
            Intrinsics.checkParameterIsNotNull(runnable, "block");
            this.this$0 = eventLoopImpl;
            this.block = runnable;
        }

        public void invoke() {
            this.block.run();
        }
    }
}
