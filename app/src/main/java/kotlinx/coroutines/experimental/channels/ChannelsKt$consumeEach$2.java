package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
/* compiled from: Channels.kt */
final class ChannelsKt$consumeEach$2 extends CoroutineImpl {
    final /* synthetic */ Function2 $action;
    private int I$0;
    private Object L$0;
    private Object L$1;
    private Object L$2;
    private Object L$3;
    private BroadcastChannel p$;
    private Function2 p$0;
    final /* synthetic */ BroadcastChannel receiver$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ChannelsKt$consumeEach$2(BroadcastChannel broadcastChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.receiver$0 = broadcastChannel;
        this.$action = function2;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:(2:62|63)|66|67|68) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002a, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002b, code lost:
        r0 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00e1, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00ea, code lost:
        r12 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00f1, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00f2, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:8:0x0029, B:33:0x0087] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:66:0x00f4 */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a5 A[Catch:{ Exception -> 0x00ea, all -> 0x00e1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ce A[Catch:{ Exception -> 0x00ea, all -> 0x00e1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00ea A[ExcHandler: Exception (e java.lang.Exception), PHI: r6 
      PHI: (r6v2 java.io.Closeable) = (r6v4 java.io.Closeable), (r6v5 java.io.Closeable), (r6v7 java.io.Closeable), (r6v16 java.io.Closeable), (r6v16 java.io.Closeable) binds: [B:38:0x009d, B:33:0x0087, B:30:0x007e, B:8:0x0029, B:9:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:33:0x0087] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object doResume(java.lang.Object r12, java.lang.Throwable r13) {
        /*
            r11 = this;
            java.lang.Object r0 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r11.label
            r2 = 3
            r3 = 2
            r4 = 1
            if (r1 == 0) goto L_0x0071
            if (r1 == r4) goto L_0x0051
            if (r1 == r3) goto L_0x0036
            if (r1 != r2) goto L_0x002e
            java.lang.Object r12 = r11.L$3
            java.lang.Object r12 = r11.L$2
            kotlinx.coroutines.experimental.channels.ChannelIterator r12 = (kotlinx.coroutines.experimental.channels.ChannelIterator) r12
            java.lang.Object r1 = r11.L$1
            kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel r1 = (kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel) r1
            int r5 = r11.I$0
            java.lang.Object r6 = r11.L$0
            java.io.Closeable r6 = (java.io.Closeable) r6
            if (r13 != 0) goto L_0x0029
            r13 = r5
            r5 = r1
            r1 = r12
            r12 = r11
            goto L_0x0087
        L_0x0029:
            throw r13     // Catch:{ Exception -> 0x00ea, all -> 0x002a }
        L_0x002a:
            r12 = move-exception
            r0 = r5
            goto L_0x00f7
        L_0x002e:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.String r13 = "call to 'resume' before 'invoke' with coroutine"
            r12.<init>(r13)
            throw r12
        L_0x0036:
            java.lang.Object r1 = r11.L$2
            kotlinx.coroutines.experimental.channels.ChannelIterator r1 = (kotlinx.coroutines.experimental.channels.ChannelIterator) r1
            java.lang.Object r5 = r11.L$1
            kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel r5 = (kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel) r5
            int r6 = r11.I$0
            java.lang.Object r7 = r11.L$0
            java.io.Closeable r7 = (java.io.Closeable) r7
            if (r13 != 0) goto L_0x0050
            r13 = r11
            r9 = r7
            r7 = r0
            r0 = r6
            r6 = r9
            r10 = r5
            r5 = r1
            r1 = r10
            goto L_0x00b9
        L_0x0050:
            throw r13     // Catch:{ Exception -> 0x006d, all -> 0x0068 }
        L_0x0051:
            java.lang.Object r1 = r11.L$2
            kotlinx.coroutines.experimental.channels.ChannelIterator r1 = (kotlinx.coroutines.experimental.channels.ChannelIterator) r1
            java.lang.Object r5 = r11.L$1
            kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel r5 = (kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel) r5
            int r6 = r11.I$0
            java.lang.Object r7 = r11.L$0
            java.io.Closeable r7 = (java.io.Closeable) r7
            if (r13 != 0) goto L_0x0067
            r13 = r11
            r9 = r7
            r7 = r0
            r0 = r6
            r6 = r9
            goto L_0x009d
        L_0x0067:
            throw r13     // Catch:{ Exception -> 0x006d, all -> 0x0068 }
        L_0x0068:
            r12 = move-exception
            r0 = r6
            r6 = r7
            goto L_0x00f7
        L_0x006d:
            r12 = move-exception
            r6 = r7
            goto L_0x00eb
        L_0x0071:
            if (r13 != 0) goto L_0x00ff
            kotlinx.coroutines.experimental.channels.BroadcastChannel r12 = r11.receiver$0
            kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel r12 = r12.open()
            r6 = r12
            java.io.Closeable r6 = (java.io.Closeable) r6
            r12 = 0
            r13 = r6
            kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel r13 = (kotlinx.coroutines.experimental.channels.SubscriptionReceiveChannel) r13     // Catch:{ Exception -> 0x00ea, all -> 0x00e6 }
            kotlinx.coroutines.experimental.channels.ChannelIterator r1 = r13.iterator()     // Catch:{ Exception -> 0x00ea, all -> 0x00e6 }
            r12 = r11
            r5 = r13
            r13 = 0
        L_0x0087:
            r12.L$0 = r6     // Catch:{ Exception -> 0x00ea, all -> 0x00e3 }
            r12.I$0 = r13     // Catch:{ Exception -> 0x00ea, all -> 0x00e3 }
            r12.L$1 = r5     // Catch:{ Exception -> 0x00ea, all -> 0x00e3 }
            r12.L$2 = r1     // Catch:{ Exception -> 0x00ea, all -> 0x00e3 }
            r12.label = r4     // Catch:{ Exception -> 0x00ea, all -> 0x00e3 }
            java.lang.Object r7 = r1.hasNext(r12)     // Catch:{ Exception -> 0x00ea, all -> 0x00e3 }
            if (r7 != r0) goto L_0x0098
            return r0
        L_0x0098:
            r9 = r13
            r13 = r12
            r12 = r7
            r7 = r0
            r0 = r9
        L_0x009d:
            java.lang.Boolean r12 = (java.lang.Boolean) r12     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            boolean r12 = r12.booleanValue()     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            if (r12 == 0) goto L_0x00d5
            r13.L$0 = r6     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.I$0 = r0     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.L$1 = r5     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.L$2 = r1     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.label = r3     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            java.lang.Object r12 = r1.next(r13)     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            if (r12 != r7) goto L_0x00b6
            return r7
        L_0x00b6:
            r9 = r5
            r5 = r1
            r1 = r9
        L_0x00b9:
            kotlin.jvm.functions.Function2 r8 = r13.$action     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.L$0 = r6     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.I$0 = r0     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.L$1 = r1     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.L$2 = r5     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.L$3 = r12     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            r13.label = r2     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            java.lang.Object r12 = r8.invoke(r12, r13)     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            if (r12 != r7) goto L_0x00ce
            return r7
        L_0x00ce:
            r12 = r13
            r13 = r0
            r0 = r7
            r9 = r5
            r5 = r1
            r1 = r9
            goto L_0x0087
        L_0x00d5:
            kotlin.Unit r12 = kotlin.Unit.INSTANCE     // Catch:{ Exception -> 0x00ea, all -> 0x00e1 }
            if (r0 != 0) goto L_0x00de
            if (r6 == 0) goto L_0x00de
            r6.close()
        L_0x00de:
            kotlin.Unit r12 = kotlin.Unit.INSTANCE
            return r12
        L_0x00e1:
            r12 = move-exception
            goto L_0x00f7
        L_0x00e3:
            r12 = move-exception
            r0 = r13
            goto L_0x00f7
        L_0x00e6:
            r13 = move-exception
            r12 = r13
            r0 = 0
            goto L_0x00f7
        L_0x00ea:
            r12 = move-exception
        L_0x00eb:
            if (r6 == 0) goto L_0x00f4
            r6.close()     // Catch:{ Exception -> 0x00f4 }
            goto L_0x00f4
        L_0x00f1:
            r12 = move-exception
            r0 = 1
            goto L_0x00f7
        L_0x00f4:
            java.lang.Throwable r12 = (java.lang.Throwable) r12     // Catch:{ all -> 0x00f1 }
            throw r12     // Catch:{ all -> 0x00f1 }
        L_0x00f7:
            if (r0 != 0) goto L_0x00fe
            if (r6 == 0) goto L_0x00fe
            r6.close()
        L_0x00fe:
            throw r12
        L_0x00ff:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.ChannelsKt$consumeEach$2.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
    }
}
