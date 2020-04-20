package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.JobSupport;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0011\u0018\u0000 %*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0002%&B\u001b\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001a\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\nH\u0014J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0014H\u0016J\n\u0010\u0018\u001a\u0004\u0018\u00010\u0014H\u0001J\b\u0010\u0019\u001a\u00020\u0012H\u0016J!\u0010\u001a\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u001b\u001a\u00028\u00002\b\u0010\u001c\u001a\u0004\u0018\u00010\u0014H\u0016¢\u0006\u0002\u0010\u001dJ\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0019\u0010!\u001a\u00020\u0012*\u00020\"2\u0006\u0010\u001b\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010#J\u0014\u0010$\u001a\u00020\u0012*\u00020\"2\u0006\u0010\u001f\u001a\u00020 H\u0016R\u0012\u0010\t\u001a\u00020\n8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00058\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8TX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010¨\u0006'"}, d2 = {"Lkotlinx/coroutines/experimental/CancellableContinuationImpl;", "T", "Lkotlinx/coroutines/experimental/AbstractCoroutine;", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "delegate", "Lkotlin/coroutines/experimental/Continuation;", "active", "", "(Lkotlin/coroutines/experimental/Continuation;Z)V", "decision", "", "isCancelled", "()Z", "parentContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "getParentContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "afterCompletion", "", "state", "", "mode", "completeResume", "token", "getResult", "initCancellability", "tryResume", "value", "idempotent", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "tryResumeWithException", "exception", "", "resumeUndispatched", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "(Lkotlinx/coroutines/experimental/CoroutineDispatcher;Ljava/lang/Object;)V", "resumeUndispatchedWithException", "Companion", "CompletedIdempotentResult", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CancellableContinuation.kt */
public class CancellableContinuationImpl<T> extends AbstractCoroutine<T> implements CancellableContinuation<T> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final AtomicIntegerFieldUpdater<CancellableContinuationImpl<?>> DECISION;
    public static final int RESUMED = 2;
    public static final int SUSPENDED = 1;
    public static final int UNDECIDED = 0;
    private volatile int decision;
    protected final Continuation<T> delegate;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CancellableContinuationImpl(Continuation<? super T> continuation, boolean z) {
        super(z);
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        this.delegate = continuation;
    }

    /* access modifiers changed from: protected */
    public CoroutineContext getParentContext() {
        return this.delegate.getContext();
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001d\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0001\u0010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u0007¢\u0006\u0002\u0010\rR\u001a\u0010\u0003\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00050\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/experimental/CancellableContinuationImpl$Companion;", "", "()V", "DECISION", "Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;", "Lkotlinx/coroutines/experimental/CancellableContinuationImpl;", "RESUMED", "", "SUSPENDED", "UNDECIDED", "getSuccessfulResult", "T", "state", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: CancellableContinuation.kt */
    protected static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final <T> T getSuccessfulResult(Object obj) {
            return obj instanceof CompletedIdempotentResult ? ((CompletedIdempotentResult) obj).result : obj;
        }
    }

    static {
        AtomicIntegerFieldUpdater<CancellableContinuationImpl<?>> newUpdater = AtomicIntegerFieldUpdater.newUpdater(CancellableContinuationImpl.class, "decision");
        Intrinsics.checkExpressionValueIsNotNull(newUpdater, "AtomicIntegerFieldUpdate…::class.java, \"decision\")");
        DECISION = newUpdater;
    }

    public void initCancellability() {
        initParentJob((Job) getParentContext().get(Job.Key));
    }

    public final Object getResult() {
        if (this.decision == 0 && DECISION.compareAndSet(this, 0, 1)) {
            return IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        Object state = getState();
        if (!(state instanceof JobSupport.CompletedExceptionally)) {
            return Companion.getSuccessfulResult(state);
        }
        throw ((JobSupport.CompletedExceptionally) state).getException();
    }

    public boolean isCancelled() {
        return getState() instanceof JobSupport.Cancelled;
    }

    public Object tryResume(T t, Object obj) {
        Object state;
        T t2;
        do {
            state = getState();
            if (state instanceof JobSupport.Incomplete) {
                JobSupport.Incomplete incomplete = (JobSupport.Incomplete) state;
                Object idempotentStart = incomplete.getIdempotentStart();
                if (obj == null && idempotentStart == null) {
                    t2 = t;
                } else {
                    t2 = new CompletedIdempotentResult(idempotentStart, obj, t, incomplete);
                }
            } else {
                if (state instanceof CompletedIdempotentResult) {
                    CompletedIdempotentResult completedIdempotentResult = (CompletedIdempotentResult) state;
                    if (completedIdempotentResult.idempotentResume == obj) {
                        if (completedIdempotentResult.result == t) {
                            return completedIdempotentResult.token;
                        }
                        throw new IllegalStateException("Non-idempotent resume".toString());
                    }
                }
                return null;
            }
        } while (!tryUpdateState(state, t2));
        return state;
    }

    public Object tryResumeWithException(Throwable th) {
        Object state;
        Intrinsics.checkParameterIsNotNull(th, "exception");
        do {
            state = getState();
            if (!(state instanceof JobSupport.Incomplete)) {
                return null;
            }
        } while (!tryUpdateState(state, new JobSupport.CompletedExceptionally(((JobSupport.Incomplete) state).getIdempotentStart(), th)));
        return state;
    }

    public void completeResume(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "token");
        completeUpdateState(obj, getState(), getDefaultResumeMode());
    }

    /* access modifiers changed from: protected */
    public void afterCompletion(Object obj, int i) {
        if (this.decision == 0 && DECISION.compareAndSet(this, 0, 2)) {
            return;
        }
        if (obj instanceof JobSupport.CompletedExceptionally) {
            Throwable exception = ((JobSupport.CompletedExceptionally) obj).getException();
            if (i == 0) {
                this.delegate.resumeWithException(exception);
            } else if (i == 1) {
                Continuation<T> continuation = this.delegate;
                if (continuation != null) {
                    DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
                    String updateContext = CoroutineContextKt.updateContext(dispatchedContinuation.getContext());
                    try {
                        dispatchedContinuation.continuation.resumeWithException(exception);
                        Unit unit = Unit.INSTANCE;
                    } finally {
                        CoroutineContextKt.restoreContext(updateContext);
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.DispatchedContinuation<T>");
                }
            } else if (i == 2) {
                CoroutineDispatcherKt.resumeDirectWithException(this.delegate, exception);
            } else {
                throw new IllegalStateException(("Invalid mode " + i).toString());
            }
        } else {
            Object successfulResult = Companion.getSuccessfulResult(obj);
            if (i == 0) {
                this.delegate.resume(successfulResult);
            } else if (i == 1) {
                Continuation<T> continuation2 = this.delegate;
                if (continuation2 != null) {
                    DispatchedContinuation dispatchedContinuation2 = (DispatchedContinuation) continuation2;
                    String updateContext2 = CoroutineContextKt.updateContext(dispatchedContinuation2.getContext());
                    try {
                        dispatchedContinuation2.continuation.resume(successfulResult);
                        Unit unit2 = Unit.INSTANCE;
                    } finally {
                        CoroutineContextKt.restoreContext(updateContext2);
                    }
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.DispatchedContinuation<T>");
                }
            } else if (i == 2) {
                CoroutineDispatcherKt.resumeDirect(this.delegate, successfulResult);
            } else {
                throw new IllegalStateException(("Invalid mode " + i).toString());
            }
        }
    }

    public void resumeUndispatched(CoroutineDispatcher coroutineDispatcher, T t) {
        Intrinsics.checkParameterIsNotNull(coroutineDispatcher, "$receiver");
        Continuation<T> continuation = this.delegate;
        if (!(continuation instanceof DispatchedContinuation)) {
            continuation = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
        if (dispatchedContinuation != null) {
            if (dispatchedContinuation.dispatcher == coroutineDispatcher) {
                resume(t, 1);
                return;
            }
            throw new IllegalStateException("Must be invoked from the context CoroutineDispatcher".toString());
        }
        throw new IllegalArgumentException("Must be used with DispatchedContinuation");
    }

    public void resumeUndispatchedWithException(CoroutineDispatcher coroutineDispatcher, Throwable th) {
        Intrinsics.checkParameterIsNotNull(coroutineDispatcher, "$receiver");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        Continuation<T> continuation = this.delegate;
        if (!(continuation instanceof DispatchedContinuation)) {
            continuation = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
        if (dispatchedContinuation != null) {
            if (dispatchedContinuation.dispatcher == coroutineDispatcher) {
                resumeWithException(th, 1);
                return;
            }
            throw new IllegalStateException("Must be invoked from the context CoroutineDispatcher".toString());
        }
        throw new IllegalArgumentException("Must be used with DispatchedContinuation");
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B+\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\nH\u0016R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/experimental/CancellableContinuationImpl$CompletedIdempotentResult;", "Lkotlinx/coroutines/experimental/JobSupport$CompletedIdempotentStart;", "idempotentStart", "", "idempotentResume", "result", "token", "Lkotlinx/coroutines/experimental/JobSupport$Incomplete;", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lkotlinx/coroutines/experimental/JobSupport$Incomplete;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: CancellableContinuation.kt */
    private static final class CompletedIdempotentResult extends JobSupport.CompletedIdempotentStart {
        public final Object idempotentResume;
        public final Object result;
        public final JobSupport.Incomplete token;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public CompletedIdempotentResult(Object obj, Object obj2, Object obj3, JobSupport.Incomplete incomplete) {
            super(obj);
            Intrinsics.checkParameterIsNotNull(incomplete, "token");
            this.idempotentResume = obj2;
            this.result = obj3;
            this.token = incomplete;
        }

        public String toString() {
            return "CompletedIdempotentResult[" + this.result + "]";
        }
    }
}
