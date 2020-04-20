package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Deferred;
import kotlinx.coroutines.experimental.JobSupport;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\b\u0012\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0011\u0010\u000e\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0011\u0010\u0010\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\r\u0010\u0011\u001a\u00028\u0000H\u0017¢\u0006\u0002\u0010\u0012JH\u0010\u0013\u001a\u00020\u0014\"\u0004\b\u0001\u0010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00150\u00172\"\u0010\u0018\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0017ø\u0001\u0000¢\u0006\u0002\u0010\u001cJV\u0010\u001d\u001a\u00020\u0014\"\u0004\b\u0001\u0010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00150\u00172\"\u0010\u0018\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u00192\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u001bH\u0001ø\u0001\u0000¢\u0006\u0004\b\u001f\u0010 R\u0014\u0010\t\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\nR\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u0002\u0004\n\u0002\b\t¨\u0006!"}, d2 = {"Lkotlinx/coroutines/experimental/DeferredCoroutine;", "T", "Lkotlinx/coroutines/experimental/AbstractCoroutine;", "Lkotlinx/coroutines/experimental/Deferred;", "parentContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "active", "", "(Lkotlin/coroutines/experimental/CoroutineContext;Z)V", "isCancelled", "()Z", "isCompletedExceptionally", "getParentContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "await", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "awaitSuspend", "getCompleted", "()Ljava/lang/Object;", "registerSelectAwait", "", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "selectAwaitCompletion", "state", "selectAwaitCompletion$kotlinx_coroutines_core", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;Ljava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Deferred.kt */
class DeferredCoroutine<T> extends AbstractCoroutine<T> implements Deferred<T> {
    private final CoroutineContext parentContext;

    public boolean isComputing() {
        return Deferred.DefaultImpls.isComputing(this);
    }

    /* access modifiers changed from: protected */
    public CoroutineContext getParentContext() {
        return this.parentContext;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DeferredCoroutine(CoroutineContext coroutineContext, boolean z) {
        super(z);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        this.parentContext = coroutineContext;
    }

    public boolean isCompletedExceptionally() {
        return getState() instanceof JobSupport.CompletedExceptionally;
    }

    public boolean isCancelled() {
        return getState() instanceof JobSupport.Cancelled;
    }

    public Object await(Continuation<? super T> continuation) {
        Object state;
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        do {
            state = getState();
            if (!(state instanceof JobSupport.Incomplete)) {
                if (!(state instanceof JobSupport.CompletedExceptionally)) {
                    return state;
                }
                throw ((JobSupport.CompletedExceptionally) state).getException();
            }
        } while (startInternal$kotlinx_coroutines_core(state) < 0);
        return awaitSuspend(continuation);
    }

    public <R> void registerSelectAwait(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        while (!selectInstance.isSelected()) {
            Object state = getState();
            if (!(state instanceof JobSupport.Incomplete)) {
                if (!selectInstance.trySelect((Object) null)) {
                    return;
                }
                if (state instanceof JobSupport.CompletedExceptionally) {
                    selectInstance.resumeSelectWithException(((JobSupport.CompletedExceptionally) state).getException(), 2);
                    return;
                } else {
                    UndispatchedKt.startCoroutineUndispatched(function2, state, selectInstance.getCompletion());
                    return;
                }
            } else if (startInternal$kotlinx_coroutines_core(state) == 0) {
                selectInstance.disposeOnSelect(invokeOnCompletion(new SelectAwaitOnCompletion(this, selectInstance, function2)));
                return;
            }
        }
    }

    public static /* bridge */ /* synthetic */ void selectAwaitCompletion$kotlinx_coroutines_core$default(DeferredCoroutine deferredCoroutine, SelectInstance selectInstance, Function2 function2, Object obj, int i, Object obj2) {
        if (obj2 == null) {
            if ((i & 4) != 0) {
                obj = deferredCoroutine.getState();
            }
            deferredCoroutine.selectAwaitCompletion$kotlinx_coroutines_core(selectInstance, function2, obj);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: selectAwaitCompletion");
    }

    public final <R> void selectAwaitCompletion$kotlinx_coroutines_core(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2, Object obj) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        if (!selectInstance.trySelect((Object) null)) {
            return;
        }
        if (obj instanceof JobSupport.CompletedExceptionally) {
            selectInstance.resumeSelectWithException(((JobSupport.CompletedExceptionally) obj).getException(), 0);
        } else {
            CoroutinesKt.startCoroutine(function2, obj, selectInstance.getCompletion());
        }
    }

    public T getCompleted() {
        T state = getState();
        if (!(!(state instanceof JobSupport.Incomplete))) {
            throw new IllegalStateException("This deferred value has not completed yet".toString());
        } else if (!(state instanceof JobSupport.CompletedExceptionally)) {
            return state;
        } else {
            throw ((JobSupport.CompletedExceptionally) state).getException();
        }
    }

    private final Object awaitSuspend(Continuation<? super T> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(continuation), true);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        JobKt.disposeOnCompletion(cancellableContinuation, invokeOnCompletion(new DeferredCoroutine$awaitSuspend$$inlined$suspendCancellableCoroutine$lambda$1(cancellableContinuation, this)));
        return cancellableContinuationImpl.getResult();
    }
}
