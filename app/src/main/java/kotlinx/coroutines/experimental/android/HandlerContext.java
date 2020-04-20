package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CancellableContinuation;
import kotlinx.coroutines.experimental.CoroutineDispatcher;
import kotlinx.coroutines.experimental.Delay;
import kotlinx.coroutines.experimental.DisposableHandle;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0019\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\f\u001a\u00020\rH\u0016J&\u0010\u001a\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/experimental/android/HandlerContext;", "Lkotlinx/coroutines/experimental/CoroutineDispatcher;", "Lkotlinx/coroutines/experimental/Delay;", "handler", "Landroid/os/Handler;", "name", "", "(Landroid/os/Handler;Ljava/lang/String;)V", "dispatch", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "block", "Ljava/lang/Runnable;", "equals", "", "other", "", "hashCode", "", "invokeOnTimeout", "Lkotlinx/coroutines/experimental/DisposableHandle;", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/experimental/CancellableContinuation;", "toString", "kotlinx-coroutines-android"}, k = 1, mv = {1, 1, 6})
/* compiled from: HandlerContext.kt */
public final class HandlerContext extends CoroutineDispatcher implements Delay {
    /* access modifiers changed from: private */
    public final Handler handler;
    private final String name;

    public Object delay(long j, TimeUnit timeUnit, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return Delay.DefaultImpls.delay(this, j, timeUnit, continuation);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ HandlerContext(Handler handler2, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(handler2, (i & 2) != 0 ? null : str);
    }

    public HandlerContext(Handler handler2, String str) {
        Intrinsics.checkParameterIsNotNull(handler2, "handler");
        this.handler = handler2;
        this.name = str;
    }

    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        this.handler.post(runnable);
    }

    public void scheduleResumeAfterDelay(long j, TimeUnit timeUnit, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "continuation");
        this.handler.postDelayed(new HandlerContext$scheduleResumeAfterDelay$1(this, cancellableContinuation), timeUnit.toMillis(j));
    }

    public DisposableHandle invokeOnTimeout(long j, TimeUnit timeUnit, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        this.handler.postDelayed(runnable, timeUnit.toMillis(j));
        return new HandlerContext$invokeOnTimeout$1(this, runnable);
    }

    public String toString() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        String handler2 = this.handler.toString();
        Intrinsics.checkExpressionValueIsNotNull(handler2, "handler.toString()");
        return handler2;
    }

    public boolean equals(Object obj) {
        return (obj instanceof HandlerContext) && ((HandlerContext) obj).handler == this.handler;
    }

    public int hashCode() {
        return System.identityHashCode(this.handler);
    }
}
