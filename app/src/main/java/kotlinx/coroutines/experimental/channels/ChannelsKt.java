package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aE\u0010\u0002\u001a\u00020\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00052\"\u0010\u0006\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H@ø\u0001\u0000¢\u0006\u0002\u0010\n\u001aE\u0010\u0002\u001a\u00020\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u000b2\"\u0010\u0006\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H@ø\u0001\u0000¢\u0006\u0002\u0010\f\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\t¨\u0006\r"}, d2 = {"DEFAULT_CLOSE_MESSAGE", "", "consumeEach", "", "E", "Lkotlinx/coroutines/experimental/channels/BroadcastChannel;", "action", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/channels/BroadcastChannel;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "(Lkotlinx/coroutines/experimental/channels/ReceiveChannel;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Channels.kt */
public final class ChannelsKt {
    public static final String DEFAULT_CLOSE_MESSAGE = "Channel was closed";

    public static final <E> Object consumeEach(BroadcastChannel<E> broadcastChannel, Function2<? super E, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(broadcastChannel, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return new ChannelsKt$consumeEach$2(broadcastChannel, function2, continuation).doResume(Unit.INSTANCE, (Throwable) null);
    }

    public static final <E> Object consumeEach(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        return new ChannelsKt$consumeEach$1(receiveChannel, function2, continuation).doResume(Unit.INSTANCE, (Throwable) null);
    }
}
