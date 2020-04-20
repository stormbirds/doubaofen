package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002BM\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012-\u0010\u0007\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\b¢\u0006\u0002\b\rø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0015\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u000bH\u0014JJ\u0010\u0017\u001a\u00020\u000b\"\u0004\b\u0001\u0010\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001a2\u0006\u0010\u0014\u001a\u00028\u00002\u001c\u0010\u0007\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u001bH\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u001cJ\u0019\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u001eR:\u0010\u0007\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\b¢\u0006\u0002\b\rX\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u000fR\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068VX\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011\u0002\u0004\n\u0002\b\t¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/experimental/channels/LazyActorCoroutine;", "E", "Lkotlinx/coroutines/experimental/channels/ActorCoroutine;", "parentContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "channel", "Lkotlinx/coroutines/experimental/channels/Channel;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/experimental/channels/ActorScope;", "Lkotlin/coroutines/experimental/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlinx/coroutines/experimental/channels/Channel;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "getChannel", "()Lkotlinx/coroutines/experimental/channels/Channel;", "offer", "", "element", "(Ljava/lang/Object;)Z", "onStart", "registerSelectSend", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "Lkotlin/Function1;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "send", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Actor.kt */
final class LazyActorCoroutine<E> extends ActorCoroutine<E> {
    private final Function2<ActorScope<E>, Continuation<? super Unit>, Object> block;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LazyActorCoroutine(CoroutineContext coroutineContext, Channel<E> channel, Function2<? super ActorScope<E>, ? super Continuation<? super Unit>, ? extends Object> function2) {
        super(coroutineContext, channel, false);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(channel, "channel");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.block = function2;
    }

    public Channel<E> getChannel() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        CoroutinesKt.startCoroutine(this.block, this, this);
    }

    public Object send(E e, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        start();
        return super.send(e, continuation);
    }

    public boolean offer(E e) {
        start();
        return super.offer(e);
    }

    public <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        start();
        super.registerSelectSend(selectInstance, e, function1);
    }
}
