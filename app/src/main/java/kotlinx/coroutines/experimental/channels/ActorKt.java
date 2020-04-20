package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineContextKt;
import kotlinx.coroutines.experimental.CoroutineStart;
import kotlinx.coroutines.experimental.Job;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00008\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001ae\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2-\u0010\t\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\n¢\u0006\u0002\b\u000fø\u0001\u0000¢\u0006\u0002\u0010\u0010\u0002\u0004\n\u0002\b\t¨\u0006\u0011"}, d2 = {"actor", "Lkotlinx/coroutines/experimental/channels/ActorJob;", "E", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "capacity", "", "start", "Lkotlinx/coroutines/experimental/CoroutineStart;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/experimental/channels/ActorScope;", "Lkotlin/coroutines/experimental/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/experimental/CoroutineContext;ILkotlinx/coroutines/experimental/CoroutineStart;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/channels/ActorJob;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Actor.kt */
public final class ActorKt {
    public static /* bridge */ /* synthetic */ ActorJob actor$default(CoroutineContext coroutineContext, int i, CoroutineStart coroutineStart, Function2 function2, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            coroutineStart = CoroutineStart.DEFAULT;
        }
        return actor(coroutineContext, i, coroutineStart, function2);
    }

    public static final <E> ActorJob<E> actor(CoroutineContext coroutineContext, int i, CoroutineStart coroutineStart, Function2<? super ActorScope<E>, ? super Continuation<? super Unit>, ? extends Object> function2) {
        ActorCoroutine actorCoroutine;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        CoroutineContext newCoroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext);
        Channel invoke = Channel.Factory.invoke(i);
        if (coroutineStart.isLazy()) {
            actorCoroutine = new LazyActorCoroutine(newCoroutineContext, invoke, function2);
        } else {
            actorCoroutine = new ActorCoroutine(newCoroutineContext, invoke, true);
        }
        actorCoroutine.initParentJob((Job) coroutineContext.get(Job.Key));
        coroutineStart.invoke(function2, actorCoroutine, actorCoroutine);
        return actorCoroutine;
    }
}
