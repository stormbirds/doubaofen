package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\b\u0017\u0018\u0000 <2\u00020\u0001:\u0005:;<=>B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u0012J%\u0010\u0013\u001a\u00020\b2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00122\u000e\b\u0004\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0015H\bJ-\u0010\u0016\u001a\u00020\b2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00122\u0016\u0010\u0017\u001a\u0012\u0012\b\u0012\u00060\u0000j\u0002`\u0012\u0012\u0004\u0012\u00020\b0\u0018H\bJ=\u0010\u0019\u001a\u00020\b2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00122\u0016\u0010\u0017\u001a\u0012\u0012\b\u0012\u00060\u0000j\u0002`\u0012\u0012\u0004\u0012\u00020\b0\u00182\u000e\b\u0004\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0015H\bJ \u0010\u001a\u001a\u00020\b2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00122\n\u0010\n\u001a\u00060\u0000j\u0002`\u0012H\u0001J\u0012\u0010\u001b\u001a\u00020\b2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u0012J'\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u001e0\u001d\"\f\b\u0000\u0010\u001e*\u00060\u0000j\u0002`\u00122\u0006\u0010\u0011\u001a\u0002H\u001e¢\u0006\u0002\u0010\u001fJ\n\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u0010\u0010\"\u001a\f\u0012\b\u0012\u00060\u0000j\u0002`\u00120#J\u0014\u0010$\u001a\u00020\u00102\n\u0010\n\u001a\u00060\u0000j\u0002`\u0012H\u0002J\u0014\u0010%\u001a\u00020\u00102\n\u0010\n\u001a\u00060\u0000j\u0002`\u0012H\u0002J\b\u0010&\u001a\u00020\u0010H\u0001J\u001e\u0010'\u001a\u00020\u00102\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00122\b\u0010(\u001a\u0004\u0018\u00010)H\u0002J%\u0010*\u001a\u00020+2\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00122\u000e\b\u0004\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0015H\bJ\f\u0010,\u001a\u00060\u0000j\u0002`\u0012H\u0002J\b\u0010-\u001a\u00020\bH\u0016J\u0018\u0010.\u001a\u0004\u0018\u0001H\u001e\"\u0006\b\u0000\u0010\u001e\u0018\u0001H\b¢\u0006\u0002\u0010\fJ,\u0010/\u001a\u0004\u0018\u0001H\u001e\"\u0006\b\u0000\u0010\u001e\u0018\u00012\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u001e\u0012\u0004\u0012\u00020\b0\u0018H\b¢\u0006\u0002\u00100J\u000e\u00101\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u0012J\b\u00102\u001a\u00020\u0006H\u0002J\b\u00103\u001a\u000204H\u0016J(\u00105\u001a\u0002062\n\u0010\u0011\u001a\u00060\u0000j\u0002`\u00122\n\u0010\n\u001a\u00060\u0000j\u0002`\u00122\u0006\u00107\u001a\u00020+H\u0001J%\u00108\u001a\u00020\u00102\n\u0010\r\u001a\u00060\u0000j\u0002`\u00122\n\u0010\n\u001a\u00060\u0000j\u0002`\u0012H\u0000¢\u0006\u0002\b9R\u0012\u0010\u0003\u001a\u00020\u00018\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u00020\u00018\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\n\u001a\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f¨\u0006?"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "", "()V", "_next", "_prev", "_removedRef", "Lkotlinx/coroutines/experimental/internal/Removed;", "isRemoved", "", "()Z", "next", "getNext", "()Ljava/lang/Object;", "prev", "getPrev", "addLast", "", "node", "Lkotlinx/coroutines/experimental/internal/Node;", "addLastIf", "condition", "Lkotlin/Function0;", "addLastIfPrev", "predicate", "Lkotlin/Function1;", "addLastIfPrevAndIf", "addNext", "addOneIfEmpty", "describeAddLast", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "T", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "describeRemove", "Lkotlinx/coroutines/experimental/internal/AtomicDesc;", "describeRemoveFirst", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "finishAdd", "finishRemove", "helpDelete", "helpInsert", "op", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "makeCondAddOp", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$CondAddOp;", "markPrev", "remove", "removeFirstIfIsInstanceOf", "removeFirstIfIsInstanceOfOrPeekIf", "(Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "removeFirstOrNull", "removed", "toString", "", "tryCondAddNext", "", "condAdd", "validateNode", "validateNode$kotlinx_coroutines_core", "AbstractAtomicDesc", "AddLastDesc", "Companion", "CondAddOp", "RemoveFirstDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: LockFreeLinkedList.kt */
public class LockFreeLinkedListNode {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> NEXT;
    public static final AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> PREV;
    public static final AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Removed> REMOVED_REF;
    /* access modifiers changed from: private */
    public volatile Object _next = this;
    /* access modifiers changed from: private */
    public volatile Object _prev = this;
    private volatile Removed _removedRef;

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R \u0010\u0003\u001a\u0012\u0012\b\u0012\u00060\u0005j\u0002`\u0006\u0012\u0004\u0012\u00020\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R \u0010\u0007\u001a\u0012\u0012\b\u0012\u00060\u0005j\u0002`\u0006\u0012\u0004\u0012\u00020\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\"\u0010\b\u001a\u0014\u0012\b\u0012\u00060\u0005j\u0002`\u0006\u0012\u0006\u0012\u0004\u0018\u00010\t0\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$Companion;", "", "()V", "NEXT", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/internal/Node;", "PREV", "REMOVED_REF", "Lkotlinx/coroutines/experimental/internal/Removed;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: LockFreeLinkedList.kt */
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        Class<LockFreeLinkedListNode> cls = LockFreeLinkedListNode.class;
        AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> newUpdater = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_next");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater, "AtomicReferenceFieldUpda…Any::class.java, \"_next\")");
        NEXT = newUpdater;
        AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> newUpdater2 = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_prev");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater2, "AtomicReferenceFieldUpda…Any::class.java, \"_prev\")");
        PREV = newUpdater2;
        AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Removed> newUpdater3 = AtomicReferenceFieldUpdater.newUpdater(cls, Removed.class, "_removedRef");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater3, "AtomicReferenceFieldUpda…lass.java, \"_removedRef\")");
        REMOVED_REF = newUpdater3;
    }

    /* access modifiers changed from: private */
    public final Removed removed() {
        Removed removed = this._removedRef;
        if (removed != null) {
            return removed;
        }
        Removed removed2 = new Removed(this);
        REMOVED_REF.lazySet(this, removed2);
        return removed2;
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b!\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004¢\u0006\u0002\u0010\u0005J\u001c\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0016R\u0014\u0010\u0002\u001a\u00060\u0003j\u0002`\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\n\u0018\u00010\u0003j\u0004\u0018\u0001`\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$CondAddOp;", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "newNode", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/internal/Node;", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)V", "oldNext", "complete", "", "affected", "", "failure", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: LockFreeLinkedList.kt */
    public static abstract class CondAddOp extends AtomicOp {
        public final LockFreeLinkedListNode newNode;
        public LockFreeLinkedListNode oldNext;

        public CondAddOp(LockFreeLinkedListNode lockFreeLinkedListNode) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "newNode");
            this.newNode = lockFreeLinkedListNode;
        }

        public void complete(Object obj, Object obj2) {
            if (obj != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                boolean z = obj2 == null;
                if (LockFreeLinkedListNode.NEXT.compareAndSet(obj, this, z ? this.newNode : this.oldNext) && z) {
                    LockFreeLinkedListNode lockFreeLinkedListNode2 = this.newNode;
                    LockFreeLinkedListNode lockFreeLinkedListNode3 = this.oldNext;
                    if (lockFreeLinkedListNode3 == null) {
                        Intrinsics.throwNpe();
                    }
                    lockFreeLinkedListNode2.finishAdd(lockFreeLinkedListNode3);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
        }
    }

    public final CondAddOp makeCondAddOp(LockFreeLinkedListNode lockFreeLinkedListNode, Function0<Boolean> function0) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(function0, "condition");
        return new LockFreeLinkedListNode$makeCondAddOp$1(function0, lockFreeLinkedListNode, lockFreeLinkedListNode);
    }

    public final boolean isRemoved() {
        return getNext() instanceof Removed;
    }

    public final Object getNext() {
        while (true) {
            Object obj = this._next;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    public final Object getPrev() {
        while (true) {
            Object obj = this._prev;
            if (obj instanceof Removed) {
                return obj;
            }
            if (obj != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                if (lockFreeLinkedListNode.getNext() == this) {
                    return obj;
                }
                helpInsert(lockFreeLinkedListNode, (OpDescriptor) null);
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    public final boolean addOneIfEmpty(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        PREV.lazySet(lockFreeLinkedListNode, this);
        NEXT.lazySet(lockFreeLinkedListNode, this);
        while (getNext() == this) {
            if (NEXT.compareAndSet(this, this, lockFreeLinkedListNode)) {
                lockFreeLinkedListNode.finishAdd(this);
                return true;
            }
        }
        return false;
    }

    public final void addLast(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Object prev;
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        do {
            prev = getPrev();
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!((LockFreeLinkedListNode) prev).addNext(lockFreeLinkedListNode, this));
    }

    public final <T extends LockFreeLinkedListNode> AddLastDesc<T> describeAddLast(T t) {
        Intrinsics.checkParameterIsNotNull(t, "node");
        return new AddLastDesc<>(this, t);
    }

    public final boolean addLastIfPrev(LockFreeLinkedListNode lockFreeLinkedListNode, Function1<? super LockFreeLinkedListNode, Boolean> function1) {
        LockFreeLinkedListNode lockFreeLinkedListNode2;
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        do {
            Object prev = getPrev();
            if (prev != null) {
                lockFreeLinkedListNode2 = (LockFreeLinkedListNode) prev;
                if (!function1.invoke(lockFreeLinkedListNode2).booleanValue()) {
                    return false;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!lockFreeLinkedListNode2.addNext(lockFreeLinkedListNode, this));
        return true;
    }

    public final boolean addNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
        PREV.lazySet(lockFreeLinkedListNode, this);
        NEXT.lazySet(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        if (!NEXT.compareAndSet(this, lockFreeLinkedListNode2, lockFreeLinkedListNode)) {
            return false;
        }
        lockFreeLinkedListNode.finishAdd(lockFreeLinkedListNode2);
        return true;
    }

    public final int tryCondAddNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2, CondAddOp condAddOp) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
        Intrinsics.checkParameterIsNotNull(condAddOp, "condAdd");
        PREV.lazySet(lockFreeLinkedListNode, this);
        NEXT.lazySet(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        condAddOp.oldNext = lockFreeLinkedListNode2;
        if (!NEXT.compareAndSet(this, lockFreeLinkedListNode2, condAddOp)) {
            return 0;
        }
        return condAddOp.perform(this) == null ? 1 : 2;
    }

    public boolean remove() {
        Object next;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        do {
            next = getNext();
            boolean z = false;
            if (next instanceof Removed) {
                return false;
            }
            if (next != this) {
                z = true;
            }
            if (!z) {
                throw new IllegalStateException("Check failed.".toString());
            } else if (next != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!NEXT.compareAndSet(this, next, lockFreeLinkedListNode.removed()));
        finishRemove(lockFreeLinkedListNode);
        return true;
    }

    public AtomicDesc describeRemove() {
        if (isRemoved()) {
            return null;
        }
        return new LockFreeLinkedListNode$describeRemove$1(this);
    }

    public final LockFreeLinkedListNode removeFirstOrNull() {
        while (true) {
            Object next = getNext();
            if (next != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
                if (lockFreeLinkedListNode == this) {
                    return null;
                }
                if (lockFreeLinkedListNode.remove()) {
                    return lockFreeLinkedListNode;
                }
                lockFreeLinkedListNode.helpDelete();
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    public final RemoveFirstDesc<LockFreeLinkedListNode> describeRemoveFirst() {
        return new RemoveFirstDesc<>(this);
    }

    private final <T> T removeFirstIfIsInstanceOf() {
        while (true) {
            T next = getNext();
            if (next != null) {
                T t = (LockFreeLinkedListNode) next;
                if (t == this) {
                    return null;
                }
                Intrinsics.reifiedOperationMarker(3, "T");
                if (!(t instanceof Object)) {
                    return null;
                }
                if (t.remove()) {
                    return t;
                }
                t.helpDelete();
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    private final <T> T removeFirstIfIsInstanceOfOrPeekIf(Function1<? super T, Boolean> function1) {
        while (true) {
            T next = getNext();
            if (next != null) {
                T t = (LockFreeLinkedListNode) next;
                if (t == this) {
                    return null;
                }
                Intrinsics.reifiedOperationMarker(3, "T");
                if (!(t instanceof Object)) {
                    return null;
                }
                if (function1.invoke(t).booleanValue() || t.remove()) {
                    return t;
                }
                t.helpDelete();
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000*\u000e\b\u0000\u0010\u0001 \u0001*\u00060\u0002j\u0002`\u00032\u00020\u0004B\u0019\u0012\n\u0010\u0005\u001a\u00060\u0002j\u0002`\u0003\u0012\u0006\u0010\u0006\u001a\u00028\u0000¢\u0006\u0002\u0010\u0007J \u0010\u0010\u001a\u00020\u00112\n\u0010\u0012\u001a\u00060\u0002j\u0002`\u00032\n\u0010\u0013\u001a\u00060\u0002j\u0002`\u0003H\u0014J\"\u0010\u0014\u001a\u0004\u0018\u00010\u00152\n\u0010\u0012\u001a\u00060\u0002j\u0002`\u00032\n\u0010\u0013\u001a\u00060\u0002j\u0002`\u0003H\u0014J\u001c\u0010\u0016\u001a\u00020\u00172\n\u0010\u0012\u001a\u00060\u0002j\u0002`\u00032\u0006\u0010\u0013\u001a\u00020\u0015H\u0014J\u0014\u0010\u0018\u001a\u00060\u0002j\u0002`\u00032\u0006\u0010\u0019\u001a\u00020\u001aH\u0004J \u0010\u001b\u001a\u00020\u00152\n\u0010\u0012\u001a\u00060\u0002j\u0002`\u00032\n\u0010\u0013\u001a\u00060\u0002j\u0002`\u0003H\u0014R$\u0010\b\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u0003@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0012\u0010\u0006\u001a\u00028\u00008\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\rR\u001c\u0010\u000e\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00038DX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\nR\u0014\u0010\u0005\u001a\u00060\u0002j\u0002`\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AddLastDesc;", "T", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/internal/Node;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AbstractAtomicDesc;", "queue", "node", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)V", "affectedNode", "getAffectedNode", "()Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "setAffectedNode", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)V", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "originalNext", "getOriginalNext", "finishOnSuccess", "", "affected", "next", "onPrepare", "", "retry", "", "takeAffectedNode", "op", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "updatedNext", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: LockFreeLinkedList.kt */
    public static class AddLastDesc<T extends LockFreeLinkedListNode> extends AbstractAtomicDesc {
        private LockFreeLinkedListNode affectedNode;
        public final T node;
        public final LockFreeLinkedListNode queue;

        public AddLastDesc(LockFreeLinkedListNode lockFreeLinkedListNode, T t) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "queue");
            Intrinsics.checkParameterIsNotNull(t, "node");
            this.queue = lockFreeLinkedListNode;
            this.node = t;
            T access$get_next$p = this.node._next;
            T t2 = this.node;
            if (!(access$get_next$p == t2 && t2._prev == this.node)) {
                throw new IllegalStateException("Check failed.".toString());
            }
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode takeAffectedNode(OpDescriptor opDescriptor) {
            Intrinsics.checkParameterIsNotNull(opDescriptor, "op");
            while (true) {
                Object access$get_prev$p = this.queue._prev;
                if (access$get_prev$p != null) {
                    LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) access$get_prev$p;
                    Object access$get_next$p = lockFreeLinkedListNode._next;
                    LockFreeLinkedListNode lockFreeLinkedListNode2 = this.queue;
                    if (access$get_next$p == lockFreeLinkedListNode2 || access$get_next$p == opDescriptor) {
                        return lockFreeLinkedListNode;
                    }
                    if (access$get_next$p instanceof OpDescriptor) {
                        ((OpDescriptor) access$get_next$p).perform(lockFreeLinkedListNode);
                    } else {
                        lockFreeLinkedListNode2.helpInsert(lockFreeLinkedListNode, opDescriptor);
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                }
            }
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getAffectedNode() {
            return this.affectedNode;
        }

        public final void setAffectedNode(LockFreeLinkedListNode lockFreeLinkedListNode) {
            this.affectedNode = lockFreeLinkedListNode;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getOriginalNext() {
            return this.queue;
        }

        /* access modifiers changed from: protected */
        public boolean retry(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            return obj != this.queue;
        }

        /* access modifiers changed from: protected */
        public Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            this.affectedNode = lockFreeLinkedListNode;
            return null;
        }

        /* access modifiers changed from: protected */
        public Object updatedNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> atomicReferenceFieldUpdater = LockFreeLinkedListNode.PREV;
            T t = this.node;
            atomicReferenceFieldUpdater.compareAndSet(t, t, lockFreeLinkedListNode);
            AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> atomicReferenceFieldUpdater2 = LockFreeLinkedListNode.NEXT;
            T t2 = this.node;
            atomicReferenceFieldUpdater2.compareAndSet(t2, t2, this.queue);
            return this.node;
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            this.node.finishAdd(this.queue);
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0011\u0012\n\u0010\u0003\u001a\u00060\u0004j\u0002`\u0005¢\u0006\u0002\u0010\u0006J\u001e\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\u0010\u0015\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0016\u001a\u00020\u0014H\u0014J \u0010\u0017\u001a\u00020\u00182\n\u0010\u0015\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0016\u001a\u00060\u0004j\u0002`\u0005H\u0004J\"\u0010\u0019\u001a\u0004\u0018\u00010\u00142\n\u0010\u0015\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0016\u001a\u00060\u0004j\u0002`\u0005H\u0005J\u001c\u0010\u001a\u001a\u00020\u001b2\n\u0010\u0015\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0016\u001a\u00020\u0014H\u0004J\u0014\u0010\u001c\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u001d\u001a\u00020\u001eH\u0004J \u0010\u001f\u001a\u00020\u00142\n\u0010\u0015\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0016\u001a\u00060\u0004j\u0002`\u0005H\u0004J\u0015\u0010 \u001a\u00020\u001b2\u0006\u0010!\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\"R$\u0010\u0007\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u0005@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u0006R$\u0010\u000b\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u0005@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u0006R\u0014\u0010\u0003\u001a\u00060\u0004j\u0002`\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u00028\u00008FX\u0004¢\u0006\f\u0012\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012¨\u0006#"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "T", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AbstractAtomicDesc;", "queue", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/internal/Node;", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;)V", "affectedNode", "getAffectedNode", "()Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "setAffectedNode", "originalNext", "getOriginalNext", "setOriginalNext", "result", "result$annotations", "()V", "getResult", "()Ljava/lang/Object;", "failure", "", "affected", "next", "finishOnSuccess", "", "onPrepare", "retry", "", "takeAffectedNode", "op", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "updatedNext", "validatePrepared", "node", "(Ljava/lang/Object;)Z", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: LockFreeLinkedList.kt */
    public static class RemoveFirstDesc<T> extends AbstractAtomicDesc {
        private LockFreeLinkedListNode affectedNode;
        private LockFreeLinkedListNode originalNext;
        public final LockFreeLinkedListNode queue;

        public static /* synthetic */ void result$annotations() {
        }

        /* access modifiers changed from: protected */
        public boolean validatePrepared(T t) {
            return true;
        }

        public RemoveFirstDesc(LockFreeLinkedListNode lockFreeLinkedListNode) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "queue");
            this.queue = lockFreeLinkedListNode;
        }

        public final T getResult() {
            T t = this.affectedNode;
            if (t == null) {
                Intrinsics.throwNpe();
            }
            return (Object) t;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode takeAffectedNode(OpDescriptor opDescriptor) {
            Intrinsics.checkParameterIsNotNull(opDescriptor, "op");
            Object next = this.queue.getNext();
            if (next != null) {
                return (LockFreeLinkedListNode) next;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getAffectedNode() {
            return this.affectedNode;
        }

        public final void setAffectedNode(LockFreeLinkedListNode lockFreeLinkedListNode) {
            this.affectedNode = lockFreeLinkedListNode;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getOriginalNext() {
            return this.originalNext;
        }

        public final void setOriginalNext(LockFreeLinkedListNode lockFreeLinkedListNode) {
            this.originalNext = lockFreeLinkedListNode;
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (lockFreeLinkedListNode == this.queue) {
                return LockFreeLinkedListKt.getLIST_EMPTY();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public final boolean retry(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (!(obj instanceof Removed)) {
                return false;
            }
            lockFreeLinkedListNode.helpDelete();
            return true;
        }

        /* access modifiers changed from: protected */
        public final Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            if (!(!(lockFreeLinkedListNode instanceof LockFreeLinkedListHead))) {
                throw new IllegalStateException("Check failed.".toString());
            } else if (!validatePrepared(lockFreeLinkedListNode)) {
                return LockFreeLinkedListKt.REMOVE_PREPARED;
            } else {
                this.affectedNode = lockFreeLinkedListNode;
                this.originalNext = lockFreeLinkedListNode2;
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public final Object updatedNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            return lockFreeLinkedListNode2.removed();
        }

        /* access modifiers changed from: protected */
        public final void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            lockFreeLinkedListNode.finishRemove(lockFreeLinkedListNode2);
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001:\u0001\u001aB\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u001e\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0011\u001a\u00020\u000fH\u0014J \u0010\u0012\u001a\u00020\u000b2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0011\u001a\u00060\u0004j\u0002`\u0005H$J\"\u0010\u0013\u001a\u0004\u0018\u00010\u000f2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0011\u001a\u00060\u0004j\u0002`\u0005H$J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\f\u001a\u00020\rJ\u001c\u0010\u0015\u001a\u00020\u00162\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0011\u001a\u00020\u000fH\u0014J\u0014\u0010\u0017\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\f\u001a\u00020\u0018H\u0014J \u0010\u0019\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0011\u001a\u00060\u0004j\u0002`\u0005H$R\u001a\u0010\u0003\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u0005X¤\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u0005X¤\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u0007¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AbstractAtomicDesc;", "Lkotlinx/coroutines/experimental/internal/AtomicDesc;", "()V", "affectedNode", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/internal/Node;", "getAffectedNode", "()Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "originalNext", "getOriginalNext", "complete", "", "op", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "failure", "", "affected", "next", "finishOnSuccess", "onPrepare", "prepare", "retry", "", "takeAffectedNode", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "updatedNext", "PrepareOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: LockFreeLinkedList.kt */
    public static abstract class AbstractAtomicDesc extends AtomicDesc {
        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            return null;
        }

        /* access modifiers changed from: protected */
        public abstract void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2);

        /* access modifiers changed from: protected */
        public abstract LockFreeLinkedListNode getAffectedNode();

        /* access modifiers changed from: protected */
        public abstract LockFreeLinkedListNode getOriginalNext();

        /* access modifiers changed from: protected */
        public abstract Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2);

        /* access modifiers changed from: protected */
        public boolean retry(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            return false;
        }

        /* access modifiers changed from: protected */
        public abstract Object updatedNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2);

        /* access modifiers changed from: protected */
        public LockFreeLinkedListNode takeAffectedNode(OpDescriptor opDescriptor) {
            Intrinsics.checkParameterIsNotNull(opDescriptor, "op");
            LockFreeLinkedListNode affectedNode = getAffectedNode();
            if (affectedNode == null) {
                Intrinsics.throwNpe();
            }
            return affectedNode;
        }

        @Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B!\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u000bH\u0016R\u0010\u0010\u0007\u001a\u00020\b8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u00060\u0003j\u0002`\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AbstractAtomicDesc$PrepareOp;", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "next", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/internal/Node;", "op", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "desc", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AbstractAtomicDesc;", "(Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/experimental/internal/AtomicOp;Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode$AbstractAtomicDesc;)V", "perform", "", "affected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
        /* compiled from: LockFreeLinkedList.kt */
        private static final class PrepareOp extends OpDescriptor {
            public final AbstractAtomicDesc desc;
            public final LockFreeLinkedListNode next;
            public final AtomicOp op;

            public PrepareOp(LockFreeLinkedListNode lockFreeLinkedListNode, AtomicOp atomicOp, AbstractAtomicDesc abstractAtomicDesc) {
                Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "next");
                Intrinsics.checkParameterIsNotNull(atomicOp, "op");
                Intrinsics.checkParameterIsNotNull(abstractAtomicDesc, "desc");
                this.next = lockFreeLinkedListNode;
                this.op = atomicOp;
                this.desc = abstractAtomicDesc;
            }

            public Object perform(Object obj) {
                if (obj != null) {
                    LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                    Object onPrepare = this.desc.onPrepare(lockFreeLinkedListNode, this.next);
                    if (onPrepare != null) {
                        if (onPrepare == LockFreeLinkedListKt.REMOVE_PREPARED) {
                            if (LockFreeLinkedListNode.NEXT.compareAndSet(obj, this, this.next.removed())) {
                                lockFreeLinkedListNode.helpDelete();
                            }
                        } else {
                            this.op.tryDecide(onPrepare);
                            LockFreeLinkedListNode.NEXT.compareAndSet(obj, this, this.next);
                        }
                        return onPrepare;
                    }
                    boolean z = true;
                    if (this.desc.getAffectedNode() == obj) {
                        if (this.desc.getOriginalNext() != this.next) {
                            z = false;
                        }
                        if (z) {
                            LockFreeLinkedListNode.NEXT.compareAndSet(obj, this, this.op.isDecided() ? this.next : this.op);
                            return null;
                        }
                        throw new IllegalStateException("Check failed.".toString());
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        }

        public final Object prepare(AtomicOp atomicOp) {
            Object perform;
            Intrinsics.checkParameterIsNotNull(atomicOp, "op");
            while (true) {
                LockFreeLinkedListNode takeAffectedNode = takeAffectedNode(atomicOp);
                Object access$get_next$p = takeAffectedNode._next;
                if (atomicOp.isDecided() || access$get_next$p == atomicOp) {
                    return null;
                }
                if (access$get_next$p instanceof OpDescriptor) {
                    ((OpDescriptor) access$get_next$p).perform(takeAffectedNode);
                } else {
                    Object failure = failure(takeAffectedNode, access$get_next$p);
                    if (failure != null) {
                        return failure;
                    }
                    if (retry(takeAffectedNode, access$get_next$p)) {
                        continue;
                    } else if (access$get_next$p != null) {
                        PrepareOp prepareOp = new PrepareOp((LockFreeLinkedListNode) access$get_next$p, atomicOp, this);
                        if (LockFreeLinkedListNode.NEXT.compareAndSet(takeAffectedNode, access$get_next$p, prepareOp) && (perform = prepareOp.perform(takeAffectedNode)) != LockFreeLinkedListKt.REMOVE_PREPARED) {
                            return perform;
                        }
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                    }
                }
            }
        }

        public final void complete(AtomicOp atomicOp, Object obj) {
            Intrinsics.checkParameterIsNotNull(atomicOp, "op");
            boolean z = obj == null;
            LockFreeLinkedListNode affectedNode = getAffectedNode();
            if (affectedNode != null) {
                LockFreeLinkedListNode originalNext = getOriginalNext();
                if (originalNext != null) {
                    if (LockFreeLinkedListNode.NEXT.compareAndSet(affectedNode, atomicOp, z ? updatedNext(affectedNode, originalNext) : originalNext) && z) {
                        finishOnSuccess(affectedNode, originalNext);
                        return;
                    }
                    return;
                }
                AbstractAtomicDesc abstractAtomicDesc = this;
                if (!(!z)) {
                    throw new IllegalStateException("Check failed.".toString());
                }
                return;
            }
            AbstractAtomicDesc abstractAtomicDesc2 = this;
            if (!(!z)) {
                throw new IllegalStateException("Check failed.".toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public final void finishAdd(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Object obj;
        do {
            obj = lockFreeLinkedListNode._prev;
            if ((obj instanceof Removed) || getNext() != lockFreeLinkedListNode) {
                return;
            }
        } while (!PREV.compareAndSet(lockFreeLinkedListNode, obj, this));
        if (!(getNext() instanceof Removed)) {
            return;
        }
        if (obj != null) {
            lockFreeLinkedListNode.helpInsert((LockFreeLinkedListNode) obj, (OpDescriptor) null);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
    }

    /* access modifiers changed from: private */
    public final void finishRemove(LockFreeLinkedListNode lockFreeLinkedListNode) {
        helpDelete();
        lockFreeLinkedListNode.helpInsert(LockFreeLinkedListKt.unwrap(this._prev), (OpDescriptor) null);
    }

    private final LockFreeLinkedListNode markPrev() {
        Object obj;
        AtomicReferenceFieldUpdater<LockFreeLinkedListNode, Object> atomicReferenceFieldUpdater;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        do {
            obj = this._prev;
            if (obj instanceof Removed) {
                return ((Removed) obj).ref;
            }
            atomicReferenceFieldUpdater = PREV;
            if (obj != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (!atomicReferenceFieldUpdater.compareAndSet(this, obj, lockFreeLinkedListNode.removed()));
        return lockFreeLinkedListNode;
    }

    public final void helpDelete() {
        Object next;
        LockFreeLinkedListNode lockFreeLinkedListNode = null;
        LockFreeLinkedListNode markPrev = markPrev();
        Object obj = this._next;
        if (obj != null) {
            LockFreeLinkedListNode lockFreeLinkedListNode2 = ((Removed) obj).ref;
            while (true) {
                LockFreeLinkedListNode lockFreeLinkedListNode3 = lockFreeLinkedListNode;
                while (true) {
                    Object next2 = lockFreeLinkedListNode2.getNext();
                    if (next2 instanceof Removed) {
                        lockFreeLinkedListNode2.markPrev();
                        lockFreeLinkedListNode2 = ((Removed) next2).ref;
                    } else {
                        next = markPrev.getNext();
                        if (next instanceof Removed) {
                            if (lockFreeLinkedListNode3 != null) {
                                break;
                            }
                            markPrev = LockFreeLinkedListKt.unwrap(markPrev._prev);
                        } else if (next != this) {
                            if (next != null) {
                                LockFreeLinkedListNode lockFreeLinkedListNode4 = (LockFreeLinkedListNode) next;
                                if (lockFreeLinkedListNode4 != lockFreeLinkedListNode2) {
                                    LockFreeLinkedListNode lockFreeLinkedListNode5 = lockFreeLinkedListNode4;
                                    lockFreeLinkedListNode3 = markPrev;
                                    markPrev = lockFreeLinkedListNode5;
                                } else {
                                    return;
                                }
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                            }
                        } else if (NEXT.compareAndSet(markPrev, this, lockFreeLinkedListNode2)) {
                            return;
                        }
                    }
                }
                markPrev.markPrev();
                NEXT.compareAndSet(lockFreeLinkedListNode3, markPrev, ((Removed) next).ref);
                markPrev = lockFreeLinkedListNode3;
            }
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Removed");
        }
    }

    /* access modifiers changed from: private */
    public final void helpInsert(LockFreeLinkedListNode lockFreeLinkedListNode, OpDescriptor opDescriptor) {
        Object obj;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = null;
        while (true) {
            LockFreeLinkedListNode lockFreeLinkedListNode3 = lockFreeLinkedListNode2;
            while (true) {
                obj = lockFreeLinkedListNode._next;
                if (obj != opDescriptor) {
                    if (obj instanceof OpDescriptor) {
                        ((OpDescriptor) obj).perform(lockFreeLinkedListNode);
                    } else if (!(obj instanceof Removed)) {
                        Object obj2 = this._prev;
                        if (!(obj2 instanceof Removed)) {
                            if (obj != this) {
                                if (obj != null) {
                                    lockFreeLinkedListNode3 = lockFreeLinkedListNode;
                                    lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                                } else {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
                                }
                            } else if (obj2 != lockFreeLinkedListNode) {
                                if (PREV.compareAndSet(this, obj2, lockFreeLinkedListNode) && !(lockFreeLinkedListNode._prev instanceof Removed)) {
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else if (lockFreeLinkedListNode3 != null) {
                        break;
                    } else {
                        lockFreeLinkedListNode = LockFreeLinkedListKt.unwrap(lockFreeLinkedListNode._prev);
                    }
                } else {
                    return;
                }
            }
            lockFreeLinkedListNode.markPrev();
            NEXT.compareAndSet(lockFreeLinkedListNode3, lockFreeLinkedListNode, ((Removed) obj).ref);
            lockFreeLinkedListNode = lockFreeLinkedListNode3;
        }
    }

    public final void validateNode$kotlinx_coroutines_core(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "prev");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
        boolean z = true;
        if (lockFreeLinkedListNode == this._prev) {
            if (lockFreeLinkedListNode2 != this._next) {
                z = false;
            }
            if (!z) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(this));
    }

    public final boolean addLastIf(LockFreeLinkedListNode lockFreeLinkedListNode, Function0<Boolean> function0) {
        int tryCondAddNext;
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(function0, "condition");
        CondAddOp lockFreeLinkedListNode$makeCondAddOp$1 = new LockFreeLinkedListNode$makeCondAddOp$1(function0, lockFreeLinkedListNode, lockFreeLinkedListNode);
        do {
            Object prev = getPrev();
            if (prev != null) {
                tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(lockFreeLinkedListNode, this, lockFreeLinkedListNode$makeCondAddOp$1);
                if (tryCondAddNext == 1) {
                    return true;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (tryCondAddNext != 2);
        return false;
    }

    public final boolean addLastIfPrevAndIf(LockFreeLinkedListNode lockFreeLinkedListNode, Function1<? super LockFreeLinkedListNode, Boolean> function1, Function0<Boolean> function0) {
        int tryCondAddNext;
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Intrinsics.checkParameterIsNotNull(function0, "condition");
        CondAddOp lockFreeLinkedListNode$makeCondAddOp$1 = new LockFreeLinkedListNode$makeCondAddOp$1(function0, lockFreeLinkedListNode, lockFreeLinkedListNode);
        do {
            Object prev = getPrev();
            if (prev != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode2 = (LockFreeLinkedListNode) prev;
                if (!function1.invoke(lockFreeLinkedListNode2).booleanValue()) {
                    return false;
                }
                tryCondAddNext = lockFreeLinkedListNode2.tryCondAddNext(lockFreeLinkedListNode, this, lockFreeLinkedListNode$makeCondAddOp$1);
                if (tryCondAddNext == 1) {
                    return true;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } while (tryCondAddNext != 2);
        return false;
    }
}
