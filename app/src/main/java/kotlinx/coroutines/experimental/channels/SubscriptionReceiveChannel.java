package kotlinx.coroutines.experimental.channels;

import java.io.Closeable;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003J\b\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/experimental/channels/SubscriptionReceiveChannel;", "T", "Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "Ljava/io/Closeable;", "close", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: BroadcastChannel.kt */
public interface SubscriptionReceiveChannel<T> extends ReceiveChannel<T>, Closeable {
    void close();
}
