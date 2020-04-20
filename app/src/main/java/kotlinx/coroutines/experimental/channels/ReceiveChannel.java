package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J\u000f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH¦\u0002J\u000f\u0010\t\u001a\u0004\u0018\u00018\u0000H&¢\u0006\u0002\u0010\nJ\u0011\u0010\u000b\u001a\u00028\u0000H¦@ø\u0001\u0000¢\u0006\u0002\u0010\fJ\u0013\u0010\r\u001a\u0004\u0018\u00018\u0000H¦@ø\u0001\u0000¢\u0006\u0002\u0010\fJH\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0001\u0010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00100\u00122\"\u0010\u0013\u001a\u001e\b\u0001\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\u0015\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0014H&ø\u0001\u0000¢\u0006\u0002\u0010\u0016JJ\u0010\u0017\u001a\u00020\u000f\"\u0004\b\u0001\u0010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00100\u00122$\u0010\u0013\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\u0015\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0014H&ø\u0001\u0000¢\u0006\u0002\u0010\u0016R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0005\u0002\u0004\n\u0002\b\t¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "E", "", "isClosedForReceive", "", "()Z", "isEmpty", "iterator", "Lkotlinx/coroutines/experimental/channels/ChannelIterator;", "poll", "()Ljava/lang/Object;", "receive", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "receiveOrNull", "registerSelectReceive", "", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "registerSelectReceiveOrNull", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Channel.kt */
public interface ReceiveChannel<E> {
    boolean isClosedForReceive();

    boolean isEmpty();

    ChannelIterator<E> iterator();

    E poll();

    Object receive(Continuation<? super E> continuation);

    Object receiveOrNull(Continuation<? super E> continuation);

    <R> void registerSelectReceive(SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2);

    <R> void registerSelectReceiveOrNull(SelectInstance<? super R> selectInstance, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2);
}
