package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aU\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\b¢\u0006\u0002\b\fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\r\u001aU\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u000e2'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\b¢\u0006\u0002\b\fø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001aM\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\b¢\u0006\u0002\b\fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u0002\u0004\n\u0002\b\t¨\u0006\u0012"}, d2 = {"async", "Lkotlinx/coroutines/experimental/Deferred;", "T", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "start", "", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/experimental/CoroutineScope;", "Lkotlin/coroutines/experimental/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/Deferred;", "Lkotlinx/coroutines/experimental/CoroutineStart;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlinx/coroutines/experimental/CoroutineStart;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/Deferred;", "defer", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/Deferred;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Deferred.kt */
public final class DeferredKt {
    public static /* bridge */ /* synthetic */ Deferred async$default(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineStart = CoroutineStart.DEFAULT;
        }
        return async(coroutineContext, coroutineStart, function2);
    }

    public static final <T> Deferred<T> async(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        DeferredCoroutine deferredCoroutine;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        CoroutineContext newCoroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext);
        if (coroutineStart.isLazy()) {
            deferredCoroutine = new LazyDeferredCoroutine(newCoroutineContext, function2);
        } else {
            deferredCoroutine = new DeferredCoroutine(newCoroutineContext, true);
        }
        deferredCoroutine.initParentJob((Job) coroutineContext.get(Job.Key));
        coroutineStart.invoke(function2, deferredCoroutine, deferredCoroutine);
        return deferredCoroutine;
    }

    @Deprecated(message = "Use `start = CoroutineStart.XXX` parameter", replaceWith = @ReplaceWith(expression = "async(context, if (start) CoroutineStart.DEFAULT else CoroutineStart.LAZY, block)", imports = {}))
    public static final <T> Deferred<T> async(CoroutineContext coroutineContext, boolean z, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return async(coroutineContext, z ? CoroutineStart.DEFAULT : CoroutineStart.LAZY, function2);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "`defer` was renamed to `async`", replaceWith = @ReplaceWith(expression = "async(context, block = block)", imports = {}))
    public static final <T> Deferred<T> defer(CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return async$default(coroutineContext, (CoroutineStart) null, function2, 2, (Object) null);
    }
}
