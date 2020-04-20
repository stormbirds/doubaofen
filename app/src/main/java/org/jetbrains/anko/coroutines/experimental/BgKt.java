package org.jetbrains.anko.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineStart;
import kotlinx.coroutines.experimental.Deferred;
import kotlinx.coroutines.experimental.DeferredKt;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.ThreadPoolDispatcherKt;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a%\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\n0\t\"\u0004\b\u0000\u0010\n2\u000e\b\u0004\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\n0\fH\b\"$\u0010\u0000\u001a\u00020\u00018\u0000@\u0000X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007¨\u0006\r"}, d2 = {"POOL", "Lkotlin/coroutines/experimental/CoroutineContext;", "POOL$annotations", "()V", "getPOOL", "()Lkotlin/coroutines/experimental/CoroutineContext;", "setPOOL", "(Lkotlin/coroutines/experimental/CoroutineContext;)V", "bg", "Lkotlinx/coroutines/experimental/Deferred;", "T", "block", "Lkotlin/Function0;", "anko-coroutines_release"}, k = 2, mv = {1, 1, 5})
/* compiled from: bg.kt */
public final class BgKt {
    private static CoroutineContext POOL = ThreadPoolDispatcherKt.newFixedThreadPoolContext$default(Runtime.getRuntime().availableProcessors() * 2, "bg", (Job) null, 4, (Object) null);

    private static /* synthetic */ void POOL$annotations() {
    }

    public static final CoroutineContext getPOOL() {
        return POOL;
    }

    public static final void setPOOL(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "<set-?>");
        POOL = coroutineContext;
    }

    public static final <T> Deferred<T> bg(Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        return DeferredKt.async$default(getPOOL(), (CoroutineStart) null, new BgKt$bg$1(function0, (Continuation) null), 2, (Object) null);
    }
}
