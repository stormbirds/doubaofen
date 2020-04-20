package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004B\u001b\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\t¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ProducerCoroutine;", "E", "Lkotlinx/coroutines/experimental/channels/ChannelCoroutine;", "Lkotlinx/coroutines/experimental/channels/ProducerScope;", "Lkotlinx/coroutines/experimental/channels/ProducerJob;", "parentContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "channel", "Lkotlinx/coroutines/experimental/channels/Channel;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlinx/coroutines/experimental/channels/Channel;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Produce.kt */
final class ProducerCoroutine<E> extends ChannelCoroutine<E> implements ProducerScope<E>, ProducerJob<E> {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ProducerCoroutine(CoroutineContext coroutineContext, Channel<E> channel) {
        super(coroutineContext, channel, true);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(channel, "channel");
    }
}
