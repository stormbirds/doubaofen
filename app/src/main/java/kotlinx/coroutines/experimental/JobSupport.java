package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.internal.AtomicDesc;
import kotlinx.coroutines.experimental.internal.AtomicOp;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.internal.OpDescriptor;
import kotlinx.coroutines.experimental.internal.Symbol;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\f\b\u0016\u0018\u0000 E2\u00020\u00012\u00020\u0002:\u0007CDEFGHIB\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u001a\u0010\u0011\u001a\u00020\u00122\b\u0010\u000e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0013\u001a\u00020\u0014H\u0014J\u0010\u0010\u0015\u001a\u00020\u00042\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017J\"\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u00072\b\u0010\u001a\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0013\u001a\u00020\u0014H\u0004J\u0006\u0010\u001b\u001a\u00020\u0017J\u0010\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u0017H\u0014J\u0010\u0010\u001e\u001a\u00020\u00122\b\u0010\u001f\u001a\u0004\u0018\u00010\u0002J \u0010 \u001a\u00020\r2\u0018\u0010!\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0017\u0012\u0004\u0012\u00020\u00120\"j\u0002`#J\u0011\u0010$\u001a\u00020\u0012H@ø\u0001\u0000¢\u0006\u0002\u0010%J\u0011\u0010&\u001a\u00020\u0012H@ø\u0001\u0000¢\u0006\u0002\u0010%J&\u0010'\u001a\u0006\u0012\u0002\b\u00030(2\u0018\u0010!\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0017\u0012\u0004\u0012\u00020\u00120\"j\u0002`#H\u0002J\u0017\u0010)\u001a\u00020\u00122\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0010¢\u0006\u0002\b*J\b\u0010+\u001a\u00020\u0012H\u0014J\u0010\u0010,\u001a\u0004\u0018\u00010\u00072\u0006\u0010-\u001a\u00020.J\u0010\u0010/\u001a\u0004\u0018\u00010\u00072\u0006\u0010-\u001a\u00020.JB\u00100\u001a\u00020\u0012\"\u0004\b\u0000\u001012\f\u00102\u001a\b\u0012\u0004\u0012\u0002H1032\u001c\u00104\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H105\u0012\u0006\u0012\u0004\u0018\u00010\u00070\"H\u0016ø\u0001\u0000¢\u0006\u0002\u00106J\u0019\u00107\u001a\u00020\u00122\n\u00108\u001a\u0006\u0012\u0002\b\u00030(H\u0000¢\u0006\u0002\b9J\u0006\u0010:\u001a\u00020\u0004J\u0017\u0010;\u001a\u00020\u00142\b\u0010\u000e\u001a\u0004\u0018\u00010\u0007H\u0000¢\u0006\u0002\b<J\b\u0010=\u001a\u00020>H\u0016J\u0010\u0010?\u001a\u00020\u00042\b\u0010@\u001a\u0004\u0018\u00010\u0007J\u001a\u0010A\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00072\b\u0010\u001a\u001a\u0004\u0018\u00010\u0007H\u0004J\"\u0010B\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u00072\b\u0010\u001a\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0013\u001a\u00020\u0014H\u0004R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\n\u0010\tR\u0011\u0010\u000b\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\tR\u0014\u0010\f\u001a\u0004\u0018\u00010\r8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u00078DX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\u0002\u0004\n\u0002\b\t¨\u0006J"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport;", "Lkotlin/coroutines/experimental/AbstractCoroutineContextElement;", "Lkotlinx/coroutines/experimental/Job;", "active", "", "(Z)V", "_state", "", "isActive", "()Z", "isCompleted", "isSelected", "parentHandle", "Lkotlinx/coroutines/experimental/DisposableHandle;", "state", "getState", "()Ljava/lang/Object;", "afterCompletion", "", "mode", "", "cancel", "cause", "", "completeUpdateState", "expect", "update", "getCompletionException", "handleCompletionException", "closeException", "initParentJob", "parent", "invokeOnCompletion", "handler", "Lkotlin/Function1;", "Lkotlinx/coroutines/experimental/CompletionHandler;", "join", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "joinSuspend", "makeNode", "Lkotlinx/coroutines/experimental/JobNode;", "onParentCompletion", "onParentCompletion$kotlinx_coroutines_core", "onStart", "performAtomicIfNotSelected", "desc", "Lkotlinx/coroutines/experimental/internal/AtomicDesc;", "performAtomicTrySelect", "registerSelectJoin", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "removeNode", "node", "removeNode$kotlinx_coroutines_core", "start", "startInternal", "startInternal$kotlinx_coroutines_core", "toString", "", "trySelect", "idempotent", "tryUpdateState", "updateState", "AtomicSelectOp", "Cancelled", "Companion", "CompletedExceptionally", "CompletedIdempotentStart", "Incomplete", "NodeList", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
public class JobSupport extends AbstractCoroutineContextElement implements Job {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final AtomicReferenceFieldUpdater<JobSupport, Object> STATE;
    /* access modifiers changed from: private */
    public volatile Object _state;
    private volatile DisposableHandle parentHandle;

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u0001X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004R\u0012\u0010\u0005\u001a\u00020\u0006X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$Incomplete;", "", "idempotentStart", "getIdempotentStart", "()Ljava/lang/Object;", "isActive", "", "()Z", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public interface Incomplete {
        Object getIdempotentStart();

        boolean isActive();
    }

    /* access modifiers changed from: protected */
    public void afterCompletion(Object obj, int i) {
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public JobSupport(boolean z) {
        super(Job.Key);
        this._state = z ? JobKt.EmptyActive : JobKt.EmptyNew;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    public Job plus(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "other");
        return Job.DefaultImpls.plus(this, job);
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0001R\"\u0010\u0003\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$Companion;", "", "()V", "STATE", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlinx/coroutines/experimental/JobSupport;", "getSTATE", "()Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "stateToString", "", "state", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    protected static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* access modifiers changed from: private */
        public final AtomicReferenceFieldUpdater<JobSupport, Object> getSTATE() {
            return JobSupport.STATE;
        }

        public final String stateToString(Object obj) {
            if (obj instanceof Incomplete) {
                return ((Incomplete) obj).isActive() ? "Active" : "New";
            }
            return "Completed";
        }
    }

    static {
        AtomicReferenceFieldUpdater<JobSupport, Object> newUpdater = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater, "AtomicReferenceFieldUpda…ny::class.java, \"_state\")");
        STATE = newUpdater;
    }

    public final void initParentJob(Job job) {
        if (!(this.parentHandle == null)) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
        } else {
            DisposableHandle invokeOnCompletion = job.invokeOnCompletion(new ParentOnCompletion(job, this));
            this.parentHandle = invokeOnCompletion;
            if (isCompleted()) {
                invokeOnCompletion.dispose();
            }
        }
    }

    public void onParentCompletion$kotlinx_coroutines_core(Throwable th) {
        if (!(th instanceof CancellationException)) {
            th = null;
        }
        cancel((CancellationException) th);
    }

    /* access modifiers changed from: protected */
    public final Object getState() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean updateState(Object obj, Object obj2, int i) {
        Intrinsics.checkParameterIsNotNull(obj, "expect");
        if (!tryUpdateState(obj, obj2)) {
            return false;
        }
        completeUpdateState(obj, obj2, i);
        return true;
    }

    /* access modifiers changed from: protected */
    public final boolean tryUpdateState(Object obj, Object obj2) {
        Intrinsics.checkParameterIsNotNull(obj, "expect");
        if (!((obj instanceof Incomplete) && !(obj2 instanceof Incomplete))) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!Companion.getSTATE().compareAndSet(this, obj, obj2)) {
            return false;
        } else {
            DisposableHandle disposableHandle = this.parentHandle;
            if (disposableHandle != null) {
                disposableHandle.dispose();
            }
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public final void completeUpdateState(Object obj, Object obj2, int i) {
        Intrinsics.checkParameterIsNotNull(obj, "expect");
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj2 instanceof CompletedExceptionally) ? null : obj2);
        Throwable th = completedExceptionally != null ? completedExceptionally.cause : null;
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = (Throwable) null;
        if (obj instanceof JobNode) {
            try {
                ((JobNode) obj).invoke(th);
            } catch (Throwable th2) {
                objectRef.element = th2;
            }
        } else if (obj instanceof NodeList) {
            NodeList nodeList = (NodeList) obj;
            Object next = nodeList.getNext();
            if (next != null) {
                for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) nodeList); lockFreeLinkedListNode = LockFreeLinkedListKt.unwrap(lockFreeLinkedListNode.getNext())) {
                    if (lockFreeLinkedListNode instanceof JobNode) {
                        try {
                            ((JobNode) lockFreeLinkedListNode).invoke(th);
                        } catch (Throwable th3) {
                            Throwable th4 = (Throwable) objectRef.element;
                            if (th4 != null) {
                                ExceptionsKt.addSuppressed(th4, th3);
                                if (th4 != null) {
                                }
                            }
                            JobSupport jobSupport = this;
                            objectRef.element = th3;
                            Unit unit = Unit.INSTANCE;
                        }
                    }
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
            }
        } else if (!(obj instanceof Empty)) {
            throw new IllegalStateException("Check failed.".toString());
        }
        Throwable th5 = (Throwable) objectRef.element;
        if (th5 != null) {
            handleCompletionException(th5);
        }
        afterCompletion(obj2, i);
    }

    public final boolean isActive() {
        Object state = getState();
        return (state instanceof Incomplete) && ((Incomplete) state).isActive();
    }

    public final boolean isCompleted() {
        return !(getState() instanceof Incomplete);
    }

    public final boolean isSelected() {
        Object state = getState();
        return !(state instanceof Incomplete) || ((Incomplete) state).isActive();
    }

    public final boolean start() {
        int startInternal$kotlinx_coroutines_core;
        do {
            startInternal$kotlinx_coroutines_core = startInternal$kotlinx_coroutines_core(getState());
            if (startInternal$kotlinx_coroutines_core == 0) {
                return false;
            }
        } while (startInternal$kotlinx_coroutines_core != 1);
        return true;
    }

    public final int startInternal$kotlinx_coroutines_core(Object obj) {
        if (obj == JobKt.EmptyNew) {
            if (!Companion.getSTATE().compareAndSet(this, obj, JobKt.EmptyActive)) {
                return -1;
            }
            onStart();
            return 1;
        } else if (!(obj instanceof NodeList) || ((NodeList) obj).isActive()) {
            return 0;
        } else {
            if (!NodeList.ACTIVE.compareAndSet(obj, (Object) null, NodeList.ACTIVE_STATE)) {
                return -1;
            }
            onStart();
            return 1;
        }
    }

    public final boolean trySelect(Object obj) {
        if (obj == null) {
            return start();
        }
        if (!(obj instanceof OpDescriptor)) {
            while (true) {
                Object state = getState();
                if (state == JobKt.EmptyNew) {
                    Companion.getSTATE().compareAndSet(this, state, new NodeList(false));
                } else if (!(state instanceof NodeList)) {
                    return (state instanceof CompletedIdempotentStart) && ((CompletedIdempotentStart) state).idempotentStart == obj;
                } else {
                    Object active = ((NodeList) state).getActive();
                    if (active == obj) {
                        return true;
                    }
                    if (active != null) {
                        return false;
                    }
                    if (NodeList.ACTIVE.compareAndSet(state, (Object) null, obj)) {
                        onStart();
                        return true;
                    }
                }
            }
        } else {
            throw new IllegalStateException("cannot use OpDescriptor as idempotent marker".toString());
        }
    }

    public final Object performAtomicTrySelect(AtomicDesc atomicDesc) {
        Intrinsics.checkParameterIsNotNull(atomicDesc, "desc");
        return new AtomicSelectOp(this, atomicDesc, true).perform((Object) null);
    }

    public final Object performAtomicIfNotSelected(AtomicDesc atomicDesc) {
        Intrinsics.checkParameterIsNotNull(atomicDesc, "desc");
        return new AtomicSelectOp(this, atomicDesc, false).perform((Object) null);
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0016J\u0012\u0010\f\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\nH\u0002J\n\u0010\r\u001a\u0004\u0018\u00010\nH\u0016J\b\u0010\u000e\u001a\u0004\u0018\u00010\nR\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$AtomicSelectOp;", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "desc", "Lkotlinx/coroutines/experimental/internal/AtomicDesc;", "activate", "", "(Lkotlinx/coroutines/experimental/JobSupport;Lkotlinx/coroutines/experimental/internal/AtomicDesc;Z)V", "complete", "", "affected", "", "failure", "completeSelect", "prepare", "prepareIfNotSelected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    private final class AtomicSelectOp extends AtomicOp {
        public final boolean activate;
        public final AtomicDesc desc;
        final /* synthetic */ JobSupport this$0;

        public AtomicSelectOp(JobSupport jobSupport, AtomicDesc atomicDesc, boolean z) {
            Intrinsics.checkParameterIsNotNull(atomicDesc, "desc");
            this.this$0 = jobSupport;
            this.desc = atomicDesc;
            this.activate = z;
        }

        public Object prepare() {
            Object prepareIfNotSelected = prepareIfNotSelected();
            return prepareIfNotSelected != null ? prepareIfNotSelected : this.desc.prepare(this);
        }

        public void complete(Object obj, Object obj2) {
            completeSelect(obj2);
            this.desc.complete(this, obj2);
        }

        public final Object prepareIfNotSelected() {
            while (true) {
                Object access$get_state$p = this.this$0._state;
                AtomicSelectOp atomicSelectOp = this;
                if (access$get_state$p == atomicSelectOp) {
                    return null;
                }
                if (access$get_state$p instanceof OpDescriptor) {
                    ((OpDescriptor) access$get_state$p).perform(this.this$0);
                } else if (access$get_state$p == JobKt.EmptyNew) {
                    if (JobSupport.Companion.getSTATE().compareAndSet(this.this$0, access$get_state$p, this)) {
                        return null;
                    }
                } else if (!(access$get_state$p instanceof NodeList)) {
                    return JobKt.getALREADY_SELECTED();
                } else {
                    Object obj = ((NodeList) access$get_state$p)._active;
                    if (obj == null) {
                        if (NodeList.ACTIVE.compareAndSet(access$get_state$p, (Object) null, this)) {
                            return null;
                        }
                    } else if (obj == atomicSelectOp) {
                        return null;
                    } else {
                        if (!(obj instanceof OpDescriptor)) {
                            return JobKt.getALREADY_SELECTED();
                        }
                        ((OpDescriptor) obj).perform(access$get_state$p);
                    }
                }
            }
        }

        private final void completeSelect(Object obj) {
            boolean z = obj == null;
            Object access$get_state$p = this.this$0._state;
            AtomicSelectOp atomicSelectOp = this;
            if (access$get_state$p == atomicSelectOp) {
                if (JobSupport.Companion.getSTATE().compareAndSet(this.this$0, this, (!z || !this.activate) ? JobKt.EmptyNew : JobKt.EmptyActive) && z) {
                    this.this$0.onStart();
                }
            } else if ((access$get_state$p instanceof NodeList) && ((NodeList) access$get_state$p)._active == atomicSelectOp) {
                if (NodeList.ACTIVE.compareAndSet(access$get_state$p, this, (!z || !this.activate) ? null : NodeList.ACTIVE_STATE) && z) {
                    this.this$0.onStart();
                }
            }
        }
    }

    public final Throwable getCompletionException() {
        Object state = getState();
        if (state instanceof Incomplete) {
            throw new IllegalStateException("Job has not completed yet");
        } else if (state instanceof CompletedExceptionally) {
            return ((CompletedExceptionally) state).getException();
        } else {
            return new CancellationException("Job has completed normally");
        }
    }

    public final DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        Throwable th = null;
        objectRef.element = (JobNode) null;
        while (true) {
            Object state = getState();
            if (state == JobKt.EmptyActive) {
                T t = (JobNode) objectRef.element;
                if (t == null) {
                    t = makeNode(function1);
                    objectRef.element = t;
                }
                if (Companion.getSTATE().compareAndSet(this, state, t)) {
                    return (DisposableHandle) t;
                }
            } else {
                boolean z = false;
                if (state == JobKt.EmptyNew) {
                    Companion.getSTATE().compareAndSet(this, state, new NodeList(false));
                } else if (state instanceof JobNode) {
                    JobNode jobNode = (JobNode) state;
                    jobNode.addOneIfEmpty(new NodeList(true));
                    Companion.getSTATE().compareAndSet(this, state, jobNode.getNext());
                } else if (state instanceof NodeList) {
                    T t2 = (JobNode) objectRef.element;
                    if (t2 == null) {
                        t2 = makeNode(function1);
                        objectRef.element = t2;
                    }
                    NodeList nodeList = (NodeList) state;
                    LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) t2;
                    LockFreeLinkedListNode.CondAddOp jobSupport$invokeOnCompletion$$inlined$addLastIf$1 = new JobSupport$invokeOnCompletion$$inlined$addLastIf$1(lockFreeLinkedListNode, lockFreeLinkedListNode, this, state);
                    while (true) {
                        Object prev = nodeList.getPrev();
                        if (prev != null) {
                            int tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(lockFreeLinkedListNode, nodeList, jobSupport$invokeOnCompletion$$inlined$addLastIf$1);
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
                        return (DisposableHandle) t2;
                    }
                } else {
                    if (!(state instanceof CompletedExceptionally)) {
                        state = null;
                    }
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) state;
                    if (completedExceptionally != null) {
                        th = completedExceptionally.getException();
                    }
                    function1.invoke(th);
                    return NonDisposableHandle.INSTANCE;
                }
            }
        }
    }

    public final Object join(Continuation<? super Unit> continuation) {
        Incomplete incomplete;
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        do {
            Object state = getState();
            if (!(state instanceof Incomplete)) {
                state = null;
            }
            incomplete = (Incomplete) state;
            if (incomplete == null) {
                return Unit.INSTANCE;
            }
        } while (startInternal$kotlinx_coroutines_core(incomplete) < 0);
        return joinSuspend(continuation);
    }

    public <R> void registerSelectJoin(SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        while (!selectInstance.isSelected()) {
            Object state = getState();
            if (!(state instanceof Incomplete)) {
                if (selectInstance.trySelect((Object) null)) {
                    UndispatchedKt.startCoroutineUndispatched(function1, selectInstance.getCompletion());
                    return;
                }
                return;
            } else if (startInternal$kotlinx_coroutines_core(state) == 0) {
                selectInstance.disposeOnSelect(invokeOnCompletion(new SelectJoinOnCompletion(this, selectInstance, function1)));
                return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0024 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x000d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void removeNode$kotlinx_coroutines_core(kotlinx.coroutines.experimental.JobNode<?> r4) {
        /*
            r3 = this;
            java.lang.String r0 = "node"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r0)
        L_0x0005:
            java.lang.Object r0 = r3.getState()
            boolean r1 = r0 instanceof kotlinx.coroutines.experimental.JobNode
            if (r1 == 0) goto L_0x0024
            r1 = r3
            kotlinx.coroutines.experimental.JobSupport r1 = (kotlinx.coroutines.experimental.JobSupport) r1
            if (r0 == r1) goto L_0x0013
            return
        L_0x0013:
            kotlinx.coroutines.experimental.JobSupport$Companion r1 = Companion
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r1 = r1.getSTATE()
            kotlinx.coroutines.experimental.Empty r2 = kotlinx.coroutines.experimental.JobKt.EmptyActive
            boolean r0 = r1.compareAndSet(r3, r0, r2)
            if (r0 == 0) goto L_0x0005
            return
        L_0x0024:
            boolean r0 = r0 instanceof kotlinx.coroutines.experimental.JobSupport.NodeList
            if (r0 == 0) goto L_0x002b
            r4.remove()
        L_0x002b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.JobSupport.removeNode$kotlinx_coroutines_core(kotlinx.coroutines.experimental.JobNode):void");
    }

    public final boolean cancel(Throwable th) {
        Incomplete incomplete;
        do {
            Object state = getState();
            if (!(state instanceof Incomplete)) {
                state = null;
            }
            incomplete = (Incomplete) state;
            if (incomplete == null) {
                return false;
            }
        } while (!updateState(incomplete, new Cancelled(incomplete.getIdempotentStart(), th), 0));
        return true;
    }

    /* access modifiers changed from: protected */
    public void handleCompletionException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "closeException");
        throw th;
    }

    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> function1) {
        JobNode<?> jobNode = (JobNode) (!(function1 instanceof JobNode) ? null : function1);
        if (jobNode != null) {
            if (!(jobNode.job == this)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            } else if (jobNode != null) {
                return jobNode;
            }
        }
        return new InvokeOnCompletion(this, function1);
    }

    public String toString() {
        return getClass().getSimpleName() + "{" + Companion.stateToString(getState()) + "}@" + Integer.toHexString(System.identityHashCode(this));
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000 \u00102\u00020\u00012\u00020\u0002:\u0001\u0010B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00078F¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u0004\u0018\u00010\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\tR\u0014\u0010\f\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$NodeList;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListHead;", "Lkotlinx/coroutines/experimental/JobSupport$Incomplete;", "active", "", "(Z)V", "_active", "", "getActive", "()Ljava/lang/Object;", "idempotentStart", "getIdempotentStart", "isActive", "()Z", "toString", "", "Companion", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    private static final class NodeList extends LockFreeLinkedListHead implements Incomplete {
        public static final AtomicReferenceFieldUpdater<NodeList, Object> ACTIVE;
        public static final Symbol ACTIVE_STATE = new Symbol("ACTIVE_STATE");
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        public volatile Object _active;

        public NodeList(boolean z) {
            this._active = z ? ACTIVE_STATE : null;
        }

        public final Object getActive() {
            while (true) {
                Object obj = this._active;
                if (!(obj instanceof OpDescriptor)) {
                    return obj;
                }
                ((OpDescriptor) obj).perform(this);
            }
        }

        public boolean isActive() {
            return getActive() != null;
        }

        public Object getIdempotentStart() {
            Object active = getActive();
            if (active == ACTIVE_STATE) {
                return null;
            }
            return active;
        }

        @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$NodeList$Companion;", "", "()V", "ACTIVE", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlinx/coroutines/experimental/JobSupport$NodeList;", "ACTIVE_STATE", "Lkotlinx/coroutines/experimental/internal/Symbol;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
        /* compiled from: Job.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        static {
            AtomicReferenceFieldUpdater<NodeList, Object> newUpdater = AtomicReferenceFieldUpdater.newUpdater(NodeList.class, Object.class, "_active");
            Intrinsics.checkExpressionValueIsNotNull(newUpdater, "AtomicReferenceFieldUpda…y::class.java, \"_active\")");
            ACTIVE = newUpdater;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("List");
            sb.append(isActive() ? "{Active}" : "{New}");
            sb.append("[");
            Ref.BooleanRef booleanRef = new Ref.BooleanRef();
            booleanRef.element = true;
            Object next = getNext();
            if (next != null) {
                for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) this); lockFreeLinkedListNode = LockFreeLinkedListKt.unwrap(lockFreeLinkedListNode.getNext())) {
                    if (lockFreeLinkedListNode instanceof JobNode) {
                        JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                        if (booleanRef.element) {
                            booleanRef.element = false;
                        } else {
                            sb.append(", ");
                        }
                        sb.append(jobNode);
                    }
                }
                sb.append("]");
                String sb2 = sb.toString();
                Intrinsics.checkExpressionValueIsNotNull(sb2, "StringBuilder().apply(builderAction).toString()");
                return sb2;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u0003R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00018\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0004"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$CompletedIdempotentStart;", "", "idempotentStart", "(Ljava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public static class CompletedIdempotentStart {
        public final Object idempotentStart;

        public CompletedIdempotentStart(Object obj) {
            this.idempotentStart = obj;
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0016R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00058\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$CompletedExceptionally;", "Lkotlinx/coroutines/experimental/JobSupport$CompletedIdempotentStart;", "idempotentStart", "", "cause", "", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "_exception", "exception", "getException", "()Ljava/lang/Throwable;", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public static class CompletedExceptionally extends CompletedIdempotentStart {
        private volatile Throwable _exception = this.cause;
        public final Throwable cause;

        public CompletedExceptionally(Object obj, Throwable th) {
            super(obj);
            this.cause = th;
        }

        public final Throwable getException() {
            Throwable th = this._exception;
            if (th != null) {
                return th;
            }
            Throwable cancellationException = new CancellationException("Job was cancelled");
            this._exception = cancellationException;
            return cancellationException;
        }

        public String toString() {
            return getClass().getSimpleName() + "[" + getException() + "]";
        }
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/JobSupport$Cancelled;", "Lkotlinx/coroutines/experimental/JobSupport$CompletedExceptionally;", "idempotentStart", "", "cause", "", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public static final class Cancelled extends CompletedExceptionally {
        public Cancelled(Object obj, Throwable th) {
            super(obj, th);
        }
    }

    private final Object joinSuspend(Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        JobKt.disposeOnCompletion(cancellableContinuation, invokeOnCompletion(new ResumeOnCompletion(this, cancellableContinuation)));
        return cancellableContinuationImpl.getResult();
    }
}
