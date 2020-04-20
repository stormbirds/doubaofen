package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.JobSupport;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b&\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u0018\u001a\u00020\tH\u0014J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0004J\u0013\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00028\u0000¢\u0006\u0002\u0010\u001fJ\u001d\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00028\u00002\u0006\u0010 \u001a\u00020\u0010H\u0004¢\u0006\u0002\u0010!J\u000e\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001cJ\u0018\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u0010H\u0004J\b\u0010$\u001a\u00020%H\u0016R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\t8FX\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u00108TX\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0012\u0010\u0016\u001a\u00020\tX¤\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u000e¨\u0006&"}, d2 = {"Lkotlinx/coroutines/experimental/AbstractCoroutine;", "T", "Lkotlinx/coroutines/experimental/JobSupport;", "Lkotlin/coroutines/experimental/Continuation;", "Lkotlinx/coroutines/experimental/CoroutineScope;", "active", "", "(Z)V", "_context", "Lkotlin/coroutines/experimental/CoroutineContext;", "context", "context$annotations", "()V", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "defaultResumeMode", "", "getDefaultResumeMode", "()I", "ignoreRepeatedResume", "getIgnoreRepeatedResume", "()Z", "parentContext", "getParentContext", "createContext", "handleCompletionException", "", "closeException", "", "resume", "value", "(Ljava/lang/Object;)V", "mode", "(Ljava/lang/Object;I)V", "resumeWithException", "exception", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CoroutineScope.kt */
public abstract class AbstractCoroutine<T> extends JobSupport implements Continuation<T>, CoroutineScope {
    private CoroutineContext _context;

    public static /* synthetic */ void context$annotations() {
    }

    /* access modifiers changed from: protected */
    public int getDefaultResumeMode() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean getIgnoreRepeatedResume() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract CoroutineContext getParentContext();

    public AbstractCoroutine(boolean z) {
        super(z);
    }

    public final CoroutineContext getContext() {
        CoroutineContext coroutineContext = this._context;
        if (coroutineContext != null) {
            return coroutineContext;
        }
        CoroutineContext createContext = createContext();
        this._context = createContext;
        return createContext;
    }

    /* access modifiers changed from: protected */
    public CoroutineContext createContext() {
        return getParentContext().plus(this);
    }

    public final void resume(T t) {
        resume(t, getDefaultResumeMode());
    }

    /* access modifiers changed from: protected */
    public final void resume(T t, int i) {
        Object state;
        do {
            state = getState();
            if (!(state instanceof JobSupport.Incomplete)) {
                if (!(state instanceof JobSupport.Cancelled) && !getIgnoreRepeatedResume()) {
                    throw new IllegalStateException("Already resumed, but got value " + t);
                }
                return;
            }
        } while (!updateState(state, t, i));
    }

    public final void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        resumeWithException(th, getDefaultResumeMode());
    }

    /* access modifiers changed from: protected */
    public final void resumeWithException(Throwable th, int i) {
        Object state;
        Intrinsics.checkParameterIsNotNull(th, "exception");
        do {
            state = getState();
            if (!(state instanceof JobSupport.Incomplete)) {
                if (state instanceof JobSupport.Cancelled) {
                    if (!Intrinsics.areEqual((Object) th, (Object) ((JobSupport.Cancelled) state).getException())) {
                        CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), th);
                        return;
                    }
                    return;
                } else if (getIgnoreRepeatedResume()) {
                    CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), th);
                    return;
                } else {
                    throw new IllegalStateException("Already resumed, but got exception " + th, th);
                }
            }
        } while (!updateState(state, new JobSupport.CompletedExceptionally(((JobSupport.Incomplete) state).getIdempotentStart(), th), i));
    }

    /* access modifiers changed from: protected */
    public final void handleCompletionException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "closeException");
        CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), th);
    }

    public String toString() {
        String str;
        Object state = getState();
        if (state instanceof JobSupport.Incomplete) {
            str = "";
        } else {
            str = "[" + state + "]";
        }
        return getClass().getSimpleName() + "{" + JobSupport.Companion.stateToString(state) + "}" + str + "@" + Integer.toHexString(System.identityHashCode(this));
    }
}
