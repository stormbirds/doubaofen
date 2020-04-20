package kotlinx.coroutines.experimental.sync;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001a\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a9\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\u001c\u0010\u0003\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004H@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a9\u0010\b\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\u001c\u0010\u0003\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0004H@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u0002\u0004\n\u0002\b\t¨\u0006\t"}, d2 = {"withLock", "T", "Lkotlinx/coroutines/experimental/sync/Mutex;", "action", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/sync/Mutex;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "withMutex", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Mutex.kt */
public final class MutexKt {
    public static final <T> Object withLock(Mutex mutex, Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(mutex, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return new MutexKt$withLock$1(mutex, function1, continuation).doResume(Unit.INSTANCE, (Throwable) null);
    }

    @Deprecated(message = "Use `withLock`", replaceWith = @ReplaceWith(expression = "withLock(action)", imports = {}))
    public static final <T> Object withMutex(Mutex mutex, Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(mutex, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return new MutexKt$withMutex$1(mutex, function1, continuation).doResume(Unit.INSTANCE, (Throwable) null);
    }
}
