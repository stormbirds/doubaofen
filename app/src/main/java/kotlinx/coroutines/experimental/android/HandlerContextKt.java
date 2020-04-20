package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import android.os.Looper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\n\u0010\u0004\u001a\u00020\u0001*\u00020\u0005\"\u0011\u0010\u0000\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006\u0006"}, d2 = {"UI", "Lkotlinx/coroutines/experimental/android/HandlerContext;", "getUI", "()Lkotlinx/coroutines/experimental/android/HandlerContext;", "asCoroutineDispatcher", "Landroid/os/Handler;", "kotlinx-coroutines-android"}, k = 2, mv = {1, 1, 6})
/* compiled from: HandlerContext.kt */
public final class HandlerContextKt {
    private static final HandlerContext UI = new HandlerContext(new Handler(Looper.getMainLooper()), "UI");

    public static final HandlerContext getUI() {
        return UI;
    }

    public static final HandlerContext asCoroutineDispatcher(Handler handler) {
        Intrinsics.checkParameterIsNotNull(handler, "$receiver");
        return new HandlerContext(handler, (String) null, 2, (DefaultConstructorMarker) null);
    }
}
