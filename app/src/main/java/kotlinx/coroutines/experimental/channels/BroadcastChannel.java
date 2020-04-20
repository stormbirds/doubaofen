package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002J\u000e\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H&¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/experimental/channels/BroadcastChannel;", "E", "Lkotlinx/coroutines/experimental/channels/SendChannel;", "open", "Lkotlinx/coroutines/experimental/channels/SubscriptionReceiveChannel;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: BroadcastChannel.kt */
public interface BroadcastChannel<E> extends SendChannel<E> {
    SubscriptionReceiveChannel<E> open();
}
