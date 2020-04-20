package kotlinx.coroutines.experimental.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0004H&J\u0014\u0010\f\u001a\u0004\u0018\u00010\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u0004H\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u00042\b\u0010\n\u001a\u0004\u0018\u00010\u0004J\n\u0010\u000f\u001a\u0004\u0018\u00010\u0004H&J\u0010\u0010\u0010\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\u0004R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/experimental/internal/AtomicOp;", "Lkotlinx/coroutines/experimental/internal/OpDescriptor;", "()V", "_consensus", "", "isDecided", "", "()Z", "complete", "", "affected", "failure", "decide", "decision", "perform", "prepare", "tryDecide", "Companion", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Atomic.kt */
public abstract class AtomicOp extends OpDescriptor {
    /* access modifiers changed from: private */
    public static final AtomicReferenceFieldUpdater<AtomicOp, Object> CONSENSUS;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Object UNDECIDED = new Symbol("UNDECIDED");
    private volatile Object _consensus = Companion.getUNDECIDED();

    public abstract void complete(Object obj, Object obj2);

    public abstract Object prepare();

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\"\u0010\u0003\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/experimental/internal/AtomicOp$Companion;", "", "()V", "CONSENSUS", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "getCONSENSUS", "()Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "UNDECIDED", "getUNDECIDED", "()Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Atomic.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* access modifiers changed from: private */
        public final AtomicReferenceFieldUpdater<AtomicOp, Object> getCONSENSUS() {
            return AtomicOp.CONSENSUS;
        }

        /* access modifiers changed from: private */
        public final Object getUNDECIDED() {
            return AtomicOp.UNDECIDED;
        }
    }

    static {
        AtomicReferenceFieldUpdater<AtomicOp, Object> newUpdater = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater, "AtomicReferenceFieldUpda…class.java, \"_consensus\")");
        CONSENSUS = newUpdater;
    }

    public final boolean isDecided() {
        return this._consensus != Companion.getUNDECIDED();
    }

    public final boolean tryDecide(Object obj) {
        if (obj != Companion.getUNDECIDED()) {
            return Companion.getCONSENSUS().compareAndSet(this, Companion.getUNDECIDED(), obj);
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    private final Object decide(Object obj) {
        return tryDecide(obj) ? obj : this._consensus;
    }

    public final Object perform(Object obj) {
        Object obj2 = this._consensus;
        if (obj2 == Companion.getUNDECIDED()) {
            obj2 = decide(prepare());
        }
        complete(obj, obj2);
        return obj2;
    }
}
