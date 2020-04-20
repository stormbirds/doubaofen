package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.AbstractCoroutine;
import kotlinx.coroutines.experimental.CoroutineExceptionHandlerKt;
import kotlinx.coroutines.experimental.JobSupport;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0004B#\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001a\u0010\u0014\u001a\u00020\u00032\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0014J\u0013\u0010\u0019\u001a\u00020\t2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0001J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0003J\u0016\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00028\u0000H\u0001¢\u0006\u0002\u0010 J\u0010\u0010!\u001a\u0004\u0018\u00018\u0000H\u0001¢\u0006\u0002\u0010\"J\u0011\u0010#\u001a\u00028\u0000HAø\u0001\u0000¢\u0006\u0002\u0010$J\u0013\u0010%\u001a\u0004\u0018\u00018\u0000HAø\u0001\u0000¢\u0006\u0002\u0010$JI\u0010&\u001a\u00020\u0003\"\u0004\b\u0001\u0010'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H'0)2\"\u0010*\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H'0,\u0012\u0006\u0012\u0004\u0018\u00010\u00160+H\u0001ø\u0001\u0000¢\u0006\u0002\u0010-JK\u0010.\u001a\u00020\u0003\"\u0004\b\u0001\u0010'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H'0)2$\u0010*\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H'0,\u0012\u0006\u0012\u0004\u0018\u00010\u00160+H\u0001ø\u0001\u0000¢\u0006\u0002\u0010-JK\u0010/\u001a\u00020\u0003\"\u0004\b\u0001\u0010'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H'0)2\u0006\u0010\u001f\u001a\u00028\u00002\u001c\u0010*\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H'0,\u0012\u0006\u0012\u0004\u0018\u00010\u001600H\u0001ø\u0001\u0000¢\u0006\u0002\u00101J\u0019\u00102\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00028\u0000HAø\u0001\u0000¢\u0006\u0002\u00103R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0012\u0010\r\u001a\u00020\tX\u0005¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0012\u0010\u000f\u001a\u00020\tX\u0005¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000eR\u0012\u0010\u0010\u001a\u00020\tX\u0005¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000eR\u0012\u0010\u0011\u001a\u00020\tX\u0005¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000eR\u0014\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u0002\u0004\n\u0002\b\t¨\u00064"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ChannelCoroutine;", "E", "Lkotlinx/coroutines/experimental/AbstractCoroutine;", "", "Lkotlinx/coroutines/experimental/channels/Channel;", "parentContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "channel", "active", "", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlinx/coroutines/experimental/channels/Channel;Z)V", "getChannel", "()Lkotlinx/coroutines/experimental/channels/Channel;", "isClosedForReceive", "()Z", "isClosedForSend", "isEmpty", "isFull", "getParentContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "afterCompletion", "state", "", "mode", "", "close", "cause", "", "iterator", "Lkotlinx/coroutines/experimental/channels/ChannelIterator;", "offer", "element", "(Ljava/lang/Object;)Z", "poll", "()Ljava/lang/Object;", "receive", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "receiveOrNull", "registerSelectReceive", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "registerSelectReceiveOrNull", "registerSelectSend", "Lkotlin/Function1;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "send", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: ChannelCoroutine.kt */
public class ChannelCoroutine<E> extends AbstractCoroutine<Unit> implements Channel<E> {
    private final Channel<E> channel;
    private final CoroutineContext parentContext;

    public boolean close(Throwable th) {
        return this.channel.close(th);
    }

    public boolean isClosedForReceive() {
        return this.channel.isClosedForReceive();
    }

    public boolean isClosedForSend() {
        return this.channel.isClosedForSend();
    }

    public boolean isEmpty() {
        return this.channel.isEmpty();
    }

    public boolean isFull() {
        return this.channel.isFull();
    }

    public ChannelIterator<E> iterator() {
        return this.channel.iterator();
    }

    public boolean offer(E e) {
        return this.channel.offer(e);
    }

    public E poll() {
        return this.channel.poll();
    }

    public Object receive(Continuation<? super E> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return this.channel.receive(continuation);
    }

    public Object receiveOrNull(Continuation<? super E> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return this.channel.receiveOrNull(continuation);
    }

    public <R> void registerSelectReceive(SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.channel.registerSelectReceive(selectInstance, function2);
    }

    public <R> void registerSelectReceiveOrNull(SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.channel.registerSelectReceiveOrNull(selectInstance, function2);
    }

    public <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.channel.registerSelectSend(selectInstance, e, function1);
    }

    public Object send(E e, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return this.channel.send(e, continuation);
    }

    /* access modifiers changed from: protected */
    public CoroutineContext getParentContext() {
        return this.parentContext;
    }

    public Channel<E> getChannel() {
        return this.channel;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ChannelCoroutine(CoroutineContext coroutineContext, Channel<E> channel2, boolean z) {
        super(z);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(channel2, "channel");
        this.parentContext = coroutineContext;
        this.channel = channel2;
    }

    /* access modifiers changed from: protected */
    public void afterCompletion(Object obj, int i) {
        Throwable th = null;
        if (!(obj instanceof JobSupport.CompletedExceptionally)) {
            obj = null;
        }
        JobSupport.CompletedExceptionally completedExceptionally = (JobSupport.CompletedExceptionally) obj;
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        if (!getChannel().close(th) && th != null) {
            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), th);
        }
    }
}
