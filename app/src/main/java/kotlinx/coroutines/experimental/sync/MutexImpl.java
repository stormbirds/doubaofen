package kotlinx.coroutines.experimental.sync;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
import kotlinx.coroutines.experimental.internal.AtomicDesc;
import kotlinx.coroutines.experimental.internal.AtomicOp;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.internal.OpDescriptor;
import kotlinx.coroutines.experimental.internal.Symbol;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\b\u0000\u0018\u0000 \u001c2\u00020\u0001:\t\u001c\u001d\u001e\u001f !\"#$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001b\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0006H@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u001b\u0010\u000f\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0006H@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJL\u0010\u0010\u001a\u00020\f\"\u0004\b\u0000\u0010\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00110\u00132\b\u0010\r\u001a\u0004\u0018\u00010\u00062\u001c\u0010\u0014\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0015H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0012\u0010\u001a\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0016J\u0012\u0010\u001b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0016R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00038@X\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\b\u0002\u0004\n\u0002\b\t¨\u0006%"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl;", "Lkotlinx/coroutines/experimental/sync/Mutex;", "locked", "", "(Z)V", "_state", "", "isLocked", "()Z", "isLockedEmptyQueueState", "isLockedEmptyQueueState$kotlinx_coroutines_core", "lock", "", "owner", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "lockSuspend", "registerSelectLock", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "toString", "", "tryLock", "unlock", "Companion", "Empty", "LockCont", "LockSelect", "LockWaiter", "LockedQueue", "TryEnqueueLockDesc", "TryLockDesc", "UnlockOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Mutex.kt */
public final class MutexImpl implements Mutex {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final Symbol ENQUEUE_FAIL = new Symbol("ENQUEUE_FAIL");
    public static final Empty EmptyLocked = new Empty(LOCKED);
    public static final Empty EmptyUnlocked = new Empty(UNLOCKED);
    public static final Symbol LOCKED = new Symbol("LOCKED");
    public static final Symbol LOCK_FAIL = new Symbol("LOCK_FAIL");
    public static final Symbol SELECT_SUCCESS = new Symbol("SELECT_SUCCESS");
    public static final AtomicReferenceFieldUpdater<MutexImpl, Object> STATE;
    public static final Symbol UNLOCKED = new Symbol("UNLOCKED");
    public static final Symbol UNLOCK_FAIL = new Symbol("UNLOCK_FAIL");
    /* access modifiers changed from: private */
    public volatile Object _state;

    public MutexImpl(boolean z) {
        this._state = z ? EmptyLocked : EmptyUnlocked;
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$Companion;", "", "()V", "ENQUEUE_FAIL", "Lkotlinx/coroutines/experimental/internal/Symbol;", "EmptyLocked", "Lkotlinx/coroutines/experimental/sync/MutexImpl$Empty;", "EmptyUnlocked", "LOCKED", "LOCK_FAIL", "SELECT_SUCCESS", "STATE", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlinx/coroutines/experimental/sync/MutexImpl;", "UNLOCKED", "UNLOCK_FAIL", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        AtomicReferenceFieldUpdater<MutexImpl, Object> newUpdater = AtomicReferenceFieldUpdater.newUpdater(MutexImpl.class, Object.class, "_state");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater, "AtomicReferenceFieldUpda…ny::class.java, \"_state\")");
        STATE = newUpdater;
    }

    public boolean isLocked() {
        while (true) {
            Object obj = this._state;
            if (obj instanceof Empty) {
                return ((Empty) obj).locked != UNLOCKED;
            }
            if (obj instanceof LockedQueue) {
                return true;
            }
            if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else {
                throw new IllegalStateException(("Illegal state " + obj).toString());
            }
        }
    }

    public final boolean isLockedEmptyQueueState$kotlinx_coroutines_core() {
        Object obj = this._state;
        return (obj instanceof LockedQueue) && ((LockedQueue) obj).isEmpty();
    }

    public boolean tryLock(Object obj) {
        while (true) {
            Object obj2 = this._state;
            boolean z = true;
            if (obj2 instanceof Empty) {
                if (((Empty) obj2).locked != UNLOCKED) {
                    return false;
                }
                if (STATE.compareAndSet(this, obj2, obj == null ? EmptyLocked : new Empty(obj))) {
                    return true;
                }
            } else if (obj2 instanceof LockedQueue) {
                if (((LockedQueue) obj2).owner == obj) {
                    z = false;
                }
                if (z) {
                    return false;
                }
                throw new IllegalStateException(("Already locked by " + obj).toString());
            } else if (obj2 instanceof OpDescriptor) {
                ((OpDescriptor) obj2).perform(this);
            } else {
                throw new IllegalStateException(("Illegal state " + obj2).toString());
            }
        }
    }

    public Object lock(Object obj, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        if (tryLock(obj)) {
            return Unit.INSTANCE;
        }
        return lockSuspend(obj, continuation);
    }

    public <R> void registerSelectLock(SelectInstance<? super R> selectInstance, Object obj, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        while (!selectInstance.isSelected()) {
            Object obj2 = this._state;
            if (obj2 instanceof Empty) {
                Empty empty = (Empty) obj2;
                if (empty.locked != UNLOCKED) {
                    STATE.compareAndSet(this, obj2, new LockedQueue(empty.locked));
                } else {
                    Object performAtomicTrySelect = selectInstance.performAtomicTrySelect(new TryLockDesc(this, obj));
                    if (performAtomicTrySelect == null) {
                        UndispatchedKt.startCoroutineUndispatched(function1, selectInstance.getCompletion());
                        return;
                    } else if (performAtomicTrySelect != JobKt.getALREADY_SELECTED()) {
                        if (performAtomicTrySelect != LOCK_FAIL) {
                            throw new IllegalStateException(("performAtomicTrySelect(TryLockDesc) returned " + performAtomicTrySelect).toString());
                        }
                    } else {
                        return;
                    }
                }
            } else if (obj2 instanceof LockedQueue) {
                LockedQueue lockedQueue = (LockedQueue) obj2;
                if (lockedQueue.owner != obj) {
                    TryEnqueueLockDesc tryEnqueueLockDesc = new TryEnqueueLockDesc(this, obj, lockedQueue, selectInstance, function1);
                    Object performAtomicIfNotSelected = selectInstance.performAtomicIfNotSelected(tryEnqueueLockDesc);
                    if (performAtomicIfNotSelected == null) {
                        selectInstance.disposeOnSelect((DisposableHandle) tryEnqueueLockDesc.node);
                        return;
                    } else if (performAtomicIfNotSelected != JobKt.getALREADY_SELECTED()) {
                        if (performAtomicIfNotSelected != ENQUEUE_FAIL) {
                            throw new IllegalStateException(("performAtomicIfNotSelected(TryEnqueueLockDesc) returned " + performAtomicIfNotSelected).toString());
                        }
                    } else {
                        return;
                    }
                } else {
                    throw new IllegalStateException(("Already locked by " + obj).toString());
                }
            } else if (obj2 instanceof OpDescriptor) {
                ((OpDescriptor) obj2).perform(this);
            } else {
                throw new IllegalStateException(("Illegal state " + obj2).toString());
            }
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001:\u0001\rB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u001a\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\t\u001a\u00020\nH\u0016R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$TryLockDesc;", "Lkotlinx/coroutines/experimental/internal/AtomicDesc;", "mutex", "Lkotlinx/coroutines/experimental/sync/MutexImpl;", "owner", "", "(Lkotlinx/coroutines/experimental/sync/MutexImpl;Ljava/lang/Object;)V", "complete", "", "op", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "failure", "prepare", "PrepareOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class TryLockDesc extends AtomicDesc {
        public final MutexImpl mutex;
        public final Object owner;

        public TryLockDesc(MutexImpl mutexImpl, Object obj) {
            Intrinsics.checkParameterIsNotNull(mutexImpl, "mutex");
            this.mutex = mutexImpl;
            this.owner = obj;
        }

        @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$TryLockDesc$PrepareOp;", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "op", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "(Lkotlinx/coroutines/experimental/sync/MutexImpl$TryLockDesc;Lkotlinx/coroutines/experimental/internal/AtomicOp;)V", "perform", "", "affected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
        /* compiled from: Mutex.kt */
        private final class PrepareOp extends OpDescriptor {
            private final AtomicOp op;
            final /* synthetic */ TryLockDesc this$0;

            public PrepareOp(TryLockDesc tryLockDesc, AtomicOp atomicOp) {
                Intrinsics.checkParameterIsNotNull(atomicOp, "op");
                this.this$0 = tryLockDesc;
                this.op = atomicOp;
            }

            public Object perform(Object obj) {
                Object obj2 = this.op.isDecided() ? MutexImpl.EmptyUnlocked : this.op;
                AtomicReferenceFieldUpdater<MutexImpl, Object> atomicReferenceFieldUpdater = MutexImpl.STATE;
                if (obj != null) {
                    atomicReferenceFieldUpdater.compareAndSet((MutexImpl) obj, this, obj2);
                    return null;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.sync.MutexImpl");
            }
        }

        public Object prepare(AtomicOp atomicOp) {
            Intrinsics.checkParameterIsNotNull(atomicOp, "op");
            PrepareOp prepareOp = new PrepareOp(this, atomicOp);
            if (!MutexImpl.STATE.compareAndSet(this.mutex, MutexImpl.EmptyUnlocked, prepareOp)) {
                return MutexImpl.LOCK_FAIL;
            }
            return prepareOp.perform(this.mutex);
        }

        public void complete(AtomicOp atomicOp, Object obj) {
            Empty empty;
            Intrinsics.checkParameterIsNotNull(atomicOp, "op");
            if (obj != null) {
                empty = MutexImpl.EmptyUnlocked;
            } else {
                Object obj2 = this.owner;
                empty = obj2 == null ? MutexImpl.EmptyLocked : new Empty(obj2);
            }
            MutexImpl.STATE.compareAndSet(this.mutex, atomicOp, empty);
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004BN\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\f\u0012\u001c\u0010\r\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000f\u0012\u0006\u0012\u0004\u0018\u00010\b0\u000eø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u001a\u0010\u0011\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0014R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$TryEnqueueLockDesc;", "R", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/experimental/sync/MutexImpl$LockSelect;", "Lkotlinx/coroutines/experimental/internal/AddLastDesc;", "mutex", "Lkotlinx/coroutines/experimental/sync/MutexImpl;", "owner", "", "queue", "Lkotlinx/coroutines/experimental/sync/MutexImpl$LockedQueue;", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/sync/MutexImpl;Ljava/lang/Object;Lkotlinx/coroutines/experimental/sync/MutexImpl$LockedQueue;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "onPrepare", "affected", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class TryEnqueueLockDesc<R> extends LockFreeLinkedListNode.AddLastDesc<LockSelect<R>> {
        public final MutexImpl mutex;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public TryEnqueueLockDesc(MutexImpl mutexImpl, Object obj, LockedQueue lockedQueue, SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
            super(lockedQueue, new LockSelect(obj, selectInstance, function1));
            Intrinsics.checkParameterIsNotNull(mutexImpl, "mutex");
            Intrinsics.checkParameterIsNotNull(lockedQueue, "queue");
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function1, "block");
            this.mutex = mutexImpl;
        }

        /* access modifiers changed from: protected */
        public Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            if (this.mutex._state != this.queue) {
                return MutexImpl.ENQUEUE_FAIL;
            }
            return super.onPrepare(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        }
    }

    public void unlock(Object obj) {
        while (true) {
            Object obj2 = this._state;
            boolean z = true;
            if (obj2 instanceof Empty) {
                if (obj == null) {
                    if (((Empty) obj2).locked == UNLOCKED) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException("Mutex is not locked".toString());
                    }
                } else {
                    Empty empty = (Empty) obj2;
                    if (empty.locked != obj) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException(("Mutex is locked by " + empty.locked + " but expected " + obj).toString());
                    }
                }
                if (STATE.compareAndSet(this, obj2, EmptyUnlocked)) {
                    return;
                }
            } else if (obj2 instanceof OpDescriptor) {
                ((OpDescriptor) obj2).perform(this);
            } else if (obj2 instanceof LockedQueue) {
                if (obj != null) {
                    LockedQueue lockedQueue = (LockedQueue) obj2;
                    if (lockedQueue.owner != obj) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException(("Mutex is locked by " + lockedQueue.owner + " but expected " + obj).toString());
                    }
                }
                LockedQueue lockedQueue2 = (LockedQueue) obj2;
                LockFreeLinkedListNode removeFirstOrNull = lockedQueue2.removeFirstOrNull();
                if (removeFirstOrNull == null) {
                    UnlockOp unlockOp = new UnlockOp(lockedQueue2);
                    if (STATE.compareAndSet(this, obj2, unlockOp) && unlockOp.perform(this) == null) {
                        return;
                    }
                } else {
                    LockWaiter lockWaiter = (LockWaiter) removeFirstOrNull;
                    Object tryResumeLockWaiter = lockWaiter.tryResumeLockWaiter();
                    if (tryResumeLockWaiter != null) {
                        Object obj3 = lockWaiter.owner;
                        if (obj3 == null) {
                            obj3 = LOCKED;
                        }
                        lockedQueue2.owner = obj3;
                        lockWaiter.completeResumeLockWaiter(tryResumeLockWaiter);
                        return;
                    }
                }
            } else {
                throw new IllegalStateException(("Illegal state " + obj2).toString());
            }
        }
    }

    public String toString() {
        while (true) {
            Object obj = this._state;
            if (obj instanceof Empty) {
                return "Mutex[" + ((Empty) obj).locked + "]";
            } else if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else if (obj instanceof LockedQueue) {
                return "Mutex[" + ((LockedQueue) obj).owner + "]";
            } else {
                throw new IllegalStateException(("Illegal state " + obj).toString());
            }
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016R\u0010\u0010\u0002\u001a\u00020\u00018\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$Empty;", "", "locked", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class Empty {
        public final Object locked;

        public Empty(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "locked");
            this.locked = obj;
        }

        public String toString() {
            return "Empty[" + this.locked + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u0012\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$LockedQueue;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "owner", "", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class LockedQueue extends LockFreeLinkedListHead {
        public Object owner;

        public LockedQueue(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "owner");
            this.owner = obj;
        }

        public String toString() {
            return "LockedQueue[" + this.owner + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\"\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0004H&J\u0006\u0010\t\u001a\u00020\u0007J\n\u0010\n\u001a\u0004\u0018\u00010\u0004H&R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$LockWaiter;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/DisposableHandle;", "owner", "", "(Ljava/lang/Object;)V", "completeResumeLockWaiter", "", "token", "dispose", "tryResumeLockWaiter", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static abstract class LockWaiter extends LockFreeLinkedListNode implements DisposableHandle {
        public final Object owner;

        public abstract void completeResumeLockWaiter(Object obj);

        public abstract Object tryResumeLockWaiter();

        @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
        public void unregister() {
            DisposableHandle.DefaultImpls.unregister(this);
        }

        public LockWaiter(Object obj) {
            this.owner = obj;
        }

        public final void dispose() {
            remove();
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0003H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$LockCont;", "Lkotlinx/coroutines/experimental/sync/MutexImpl$LockWaiter;", "owner", "", "cont", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/CancellableContinuation;)V", "completeResumeLockWaiter", "token", "toString", "", "tryResumeLockWaiter", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class LockCont extends LockWaiter {
        public final CancellableContinuation<Unit> cont;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public LockCont(Object obj, CancellableContinuation<? super Unit> cancellableContinuation) {
            super(obj);
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.cont = cancellableContinuation;
        }

        public Object tryResumeLockWaiter() {
            return CancellableContinuation.DefaultImpls.tryResume$default(this.cont, Unit.INSTANCE, (Object) null, 2, (Object) null);
        }

        public void completeResumeLockWaiter(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            this.cont.completeResume(obj);
        }

        public String toString() {
            return "LockCont[" + this.owner + ", " + this.cont + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B>\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012\u001c\u0010\u0007\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00040\bø\u0001\u0000¢\u0006\u0002\u0010\nJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0004H\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\n\u0010\u0011\u001a\u0004\u0018\u00010\u0004H\u0016R+\u0010\u0007\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00040\b8\u0006X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u000bR\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$LockSelect;", "R", "Lkotlinx/coroutines/experimental/sync/MutexImpl$LockWaiter;", "owner", "", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Ljava/lang/Object;Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "Lkotlin/jvm/functions/Function1;", "completeResumeLockWaiter", "", "token", "toString", "", "tryResumeLockWaiter", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class LockSelect<R> extends LockWaiter {
        public final Function1<Continuation<? super R>, Object> block;
        public final SelectInstance<R> select;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public LockSelect(Object obj, SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
            super(obj);
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function1, "block");
            this.select = selectInstance;
            this.block = function1;
        }

        public Object tryResumeLockWaiter() {
            if (this.select.trySelect((Object) null)) {
                return MutexImpl.SELECT_SUCCESS;
            }
            return null;
        }

        public void completeResumeLockWaiter(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (obj == MutexImpl.SELECT_SUCCESS) {
                CoroutinesKt.startCoroutine(this.block, this.select.getCompletion());
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }

        public String toString() {
            return "LockSelect[" + this.owner + ", " + this.select + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0016R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/experimental/sync/MutexImpl$UnlockOp;", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "queue", "Lkotlinx/coroutines/experimental/sync/MutexImpl$LockedQueue;", "(Lkotlinx/coroutines/experimental/sync/MutexImpl$LockedQueue;)V", "perform", "", "affected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    private static final class UnlockOp extends OpDescriptor {
        public final LockedQueue queue;

        public UnlockOp(LockedQueue lockedQueue) {
            Intrinsics.checkParameterIsNotNull(lockedQueue, "queue");
            this.queue = lockedQueue;
        }

        public Object perform(Object obj) {
            Object obj2 = this.queue.isEmpty() ? MutexImpl.EmptyUnlocked : this.queue;
            AtomicReferenceFieldUpdater<MutexImpl, Object> atomicReferenceFieldUpdater = MutexImpl.STATE;
            if (obj != null) {
                MutexImpl mutexImpl = (MutexImpl) obj;
                atomicReferenceFieldUpdater.compareAndSet(mutexImpl, this, obj2);
                if (mutexImpl._state == this.queue) {
                    return MutexImpl.UNLOCK_FAIL;
                }
                return null;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.sync.MutexImpl");
        }
    }

    private final Object lockSuspend(Object obj, Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        LockCont lockCont = new LockCont(obj, cancellableContinuation);
        while (true) {
            Object access$get_state$p = this._state;
            if (access$get_state$p instanceof Empty) {
                Empty empty = (Empty) access$get_state$p;
                if (empty.locked != UNLOCKED) {
                    STATE.compareAndSet(this, access$get_state$p, new LockedQueue(empty.locked));
                } else {
                    if (STATE.compareAndSet(this, access$get_state$p, obj == null ? EmptyLocked : new Empty(obj))) {
                        cancellableContinuation.resume(Unit.INSTANCE);
                        break;
                    }
                }
            } else if (access$get_state$p instanceof LockedQueue) {
                LockedQueue lockedQueue = (LockedQueue) access$get_state$p;
                boolean z = false;
                if (lockedQueue.owner != obj) {
                    LockFreeLinkedListNode lockFreeLinkedListNode = lockCont;
                    LockFreeLinkedListNode.CondAddOp mutexImpl$lockSuspend$$inlined$suspendCancellableCoroutine$lambda$1 = new MutexImpl$lockSuspend$$inlined$suspendCancellableCoroutine$lambda$1(lockFreeLinkedListNode, lockFreeLinkedListNode, access$get_state$p, this, obj);
                    while (true) {
                        Object prev = lockedQueue.getPrev();
                        if (prev != null) {
                            int tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(lockFreeLinkedListNode, lockedQueue, mutexImpl$lockSuspend$$inlined$suspendCancellableCoroutine$lambda$1);
                            if (tryCondAddNext != 1) {
                                if (tryCondAddNext == 2) {
                                    break;
                                }
                            } else {
                                z = true;
                                break;
                            }
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                        }
                    }
                    if (z) {
                        cancellableContinuation.initCancellability();
                        CancellableContinuationKt.removeOnCancel(cancellableContinuation, lockFreeLinkedListNode);
                        break;
                    }
                } else {
                    throw new IllegalStateException(("Already locked by " + obj).toString());
                }
            } else if (access$get_state$p instanceof OpDescriptor) {
                ((OpDescriptor) access$get_state$p).perform(this);
            } else {
                throw new IllegalStateException(("Illegal state " + access$get_state$p).toString());
            }
        }
        return cancellableContinuationImpl.getResult();
    }
}
