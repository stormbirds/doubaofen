package kotlinx.coroutines.experimental.android;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlinx.coroutines.experimental.DisposableHandle;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"kotlinx/coroutines/experimental/android/HandlerContext$invokeOnTimeout$1", "Lkotlinx/coroutines/experimental/DisposableHandle;", "(Lkotlinx/coroutines/experimental/android/HandlerContext;Ljava/lang/Runnable;)V", "dispose", "", "kotlinx-coroutines-android"}, k = 1, mv = {1, 1, 6})
/* compiled from: HandlerContext.kt */
public final class HandlerContext$invokeOnTimeout$1 implements DisposableHandle {
    final /* synthetic */ Runnable $block;
    final /* synthetic */ HandlerContext this$0;

    HandlerContext$invokeOnTimeout$1(HandlerContext handlerContext, Runnable runnable) {
        this.this$0 = handlerContext;
        this.$block = runnable;
    }

    @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
    public void unregister() {
        DisposableHandle.DefaultImpls.unregister(this);
    }

    public void dispose() {
        this.this$0.handler.removeCallbacks(this.$block);
    }
}
