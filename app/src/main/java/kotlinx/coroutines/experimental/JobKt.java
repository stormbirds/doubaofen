package kotlinx.coroutines.experimental;

import java.util.concurrent.Future;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.Symbol;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0016\u0010\u0007\u001a\u00020\b*\u00020\t2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b\u001a\u0012\u0010\f\u001a\u00020\b*\u00020\t2\u0006\u0010\r\u001a\u00020\b\u001a\u0015\u0010\u000e\u001a\u00020\u000f*\u00020\tH@ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a\u0014\u0010\u0011\u001a\u00020\b*\u00020\t2\u0006\u0010\u0012\u001a\u00020\bH\u0007\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000*\n\u0010\u0013\"\u00020\u00142\u00020\u0014*&\u0010\u0015\"\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0017\u0012\u0004\u0012\u00020\u000f0\u00162\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0017\u0012\u0004\u0012\u00020\u000f0\u0016*8\b\u0007\u0010\u0018\"\u00020\u00192\u00020\u0019B*\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001c\u0012\u001c\b\u001d\u0012\u0018\b\u000bB\u0014\b\u001e\u0012\u0006\b\u001f\u0012\u0002\b\f\u0012\b\b \u0012\u0004\b\b(!\u0002\u0004\n\u0002\b\t¨\u0006\""}, d2 = {"ALREADY_SELECTED", "", "getALREADY_SELECTED", "()Ljava/lang/Object;", "EmptyActive", "Lkotlinx/coroutines/experimental/Empty;", "EmptyNew", "cancelFutureOnCompletion", "Lkotlinx/coroutines/experimental/DisposableHandle;", "Lkotlinx/coroutines/experimental/Job;", "future", "Ljava/util/concurrent/Future;", "disposeOnCompletion", "handle", "join", "", "(Lkotlinx/coroutines/experimental/Job;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "unregisterOnCompletion", "registration", "CancellationException", "Ljava/util/concurrent/CancellationException;", "CompletionHandler", "Lkotlin/Function1;", "", "EmptyRegistration", "Lkotlinx/coroutines/experimental/NonDisposableHandle;", "Lkotlin/Deprecated;", "message", "Replace with `NonDisposableHandle`", "replaceWith", "Lkotlin/ReplaceWith;", "imports", "expression", "NonDisposableHandle", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Job.kt */
public final class JobKt {
    private static final Object ALREADY_SELECTED = new Symbol("ALREADY_SELECTED");
    /* access modifiers changed from: private */
    public static final Empty EmptyActive = new Empty(true);
    /* access modifiers changed from: private */
    public static final Empty EmptyNew = new Empty(false);

    @Deprecated(message = "Replace with `NonDisposableHandle`", replaceWith = @ReplaceWith(expression = "NonDisposableHandle", imports = {}))
    public static /* synthetic */ void EmptyRegistration$annotations() {
    }

    @Deprecated(message = "Renamed to `disposeOnCompletion`", replaceWith = @ReplaceWith(expression = "disposeOnCompletion(registration)", imports = {}))
    public static final DisposableHandle unregisterOnCompletion(Job job, DisposableHandle disposableHandle) {
        Intrinsics.checkParameterIsNotNull(job, "$receiver");
        Intrinsics.checkParameterIsNotNull(disposableHandle, "registration");
        return job.invokeOnCompletion(new DisposeOnCompletion(job, disposableHandle));
    }

    public static final DisposableHandle disposeOnCompletion(Job job, DisposableHandle disposableHandle) {
        Intrinsics.checkParameterIsNotNull(job, "$receiver");
        Intrinsics.checkParameterIsNotNull(disposableHandle, "handle");
        return job.invokeOnCompletion(new DisposeOnCompletion(job, disposableHandle));
    }

    public static final DisposableHandle cancelFutureOnCompletion(Job job, Future<?> future) {
        Intrinsics.checkParameterIsNotNull(job, "$receiver");
        Intrinsics.checkParameterIsNotNull(future, "future");
        return job.invokeOnCompletion(new CancelFutureOnCompletion(job, future));
    }

    @Deprecated(message = "`join` is now a member function of `Job`")
    public static final Object join(Job job, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(job, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return job.join(continuation);
    }

    public static final Object getALREADY_SELECTED() {
        return ALREADY_SELECTED;
    }
}
