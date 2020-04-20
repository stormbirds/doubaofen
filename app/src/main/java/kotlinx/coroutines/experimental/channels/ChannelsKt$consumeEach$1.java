package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
/* compiled from: Channels.kt */
final class ChannelsKt$consumeEach$1 extends CoroutineImpl {
    final /* synthetic */ Function2 $action;
    private Object L$0;
    private Object L$1;
    private ReceiveChannel p$;
    private Function2 p$0;
    final /* synthetic */ ReceiveChannel receiver$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ChannelsKt$consumeEach$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.receiver$0 = receiveChannel;
        this.$action = function2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x006f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object doResume(java.lang.Object r8, java.lang.Throwable r9) {
        /*
            r7 = this;
            java.lang.Object r0 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r7.label
            r2 = 3
            r3 = 2
            r4 = 1
            if (r1 == 0) goto L_0x0035
            if (r1 == r4) goto L_0x002c
            if (r1 == r3) goto L_0x0023
            if (r1 != r2) goto L_0x001b
            java.lang.Object r8 = r7.L$1
            kotlinx.coroutines.experimental.channels.ChannelIterator r8 = (kotlinx.coroutines.experimental.channels.ChannelIterator) r8
            java.lang.Object r1 = r7.L$0
            if (r9 != 0) goto L_0x001a
            goto L_0x003d
        L_0x001a:
            throw r9
        L_0x001b:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r9)
            throw r8
        L_0x0023:
            java.lang.Object r1 = r7.L$0
            kotlinx.coroutines.experimental.channels.ChannelIterator r1 = (kotlinx.coroutines.experimental.channels.ChannelIterator) r1
            if (r9 != 0) goto L_0x002b
            r9 = r7
            goto L_0x0060
        L_0x002b:
            throw r9
        L_0x002c:
            java.lang.Object r1 = r7.L$0
            kotlinx.coroutines.experimental.channels.ChannelIterator r1 = (kotlinx.coroutines.experimental.channels.ChannelIterator) r1
            if (r9 != 0) goto L_0x0034
            r9 = r7
            goto L_0x004d
        L_0x0034:
            throw r9
        L_0x0035:
            if (r9 != 0) goto L_0x0074
            kotlinx.coroutines.experimental.channels.ReceiveChannel r8 = r7.receiver$0
            kotlinx.coroutines.experimental.channels.ChannelIterator r8 = r8.iterator()
        L_0x003d:
            r1 = r8
            r8 = r7
        L_0x003f:
            r8.L$0 = r1
            r8.label = r4
            java.lang.Object r9 = r1.hasNext(r8)
            if (r9 != r0) goto L_0x004a
            return r0
        L_0x004a:
            r6 = r9
            r9 = r8
            r8 = r6
        L_0x004d:
            java.lang.Boolean r8 = (java.lang.Boolean) r8
            boolean r8 = r8.booleanValue()
            if (r8 == 0) goto L_0x0071
            r9.L$0 = r1
            r9.label = r3
            java.lang.Object r8 = r1.next(r9)
            if (r8 != r0) goto L_0x0060
            return r0
        L_0x0060:
            kotlin.jvm.functions.Function2 r5 = r9.$action
            r9.L$0 = r8
            r9.L$1 = r1
            r9.label = r2
            java.lang.Object r8 = r5.invoke(r8, r9)
            if (r8 != r0) goto L_0x006f
            return r0
        L_0x006f:
            r8 = r9
            goto L_0x003f
        L_0x0071:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        L_0x0074:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.channels.ChannelsKt$consumeEach$1.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
    }
}
