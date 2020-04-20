package kotlinx.coroutines.experimental.selects;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function1;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
/* compiled from: WhileSelect.kt */
final class WhileSelectKt$whileSelect$1 extends CoroutineImpl {
    final /* synthetic */ Function1 $builder;
    private Object L$0;
    private Object L$1;
    private Function1 p$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    WhileSelectKt$whileSelect$1(Function1 function1, Continuation continuation) {
        super(1, continuation);
        this.$builder = function1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x004e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object doResume(java.lang.Object r6, java.lang.Throwable r7) {
        /*
            r5 = this;
            java.lang.Object r0 = kotlin.coroutines.experimental.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r5.label
            r2 = 1
            if (r1 == 0) goto L_0x0020
            if (r1 != r2) goto L_0x0018
            java.lang.Object r1 = r5.L$1
            kotlinx.coroutines.experimental.selects.WhileSelectKt$whileSelect$1 r1 = (kotlinx.coroutines.experimental.selects.WhileSelectKt$whileSelect$1) r1
            java.lang.Object r1 = r5.L$0
            kotlin.jvm.functions.Function1 r1 = (kotlin.jvm.functions.Function1) r1
            if (r7 != 0) goto L_0x0017
            r7 = r5
            goto L_0x0046
        L_0x0017:
            throw r7
        L_0x0018:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x0020:
            if (r7 != 0) goto L_0x0053
            r6 = r5
        L_0x0023:
            kotlin.jvm.functions.Function1 r7 = r6.$builder
            r6.L$0 = r7
            r6.L$1 = r6
            r6.label = r2
            kotlin.coroutines.experimental.Continuation r1 = kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics.normalizeContinuation(r6)
            kotlinx.coroutines.experimental.selects.SelectBuilderImpl r3 = new kotlinx.coroutines.experimental.selects.SelectBuilderImpl
            r3.<init>(r1)
            r7.invoke(r3)     // Catch:{ all -> 0x0038 }
            goto L_0x003c
        L_0x0038:
            r7 = move-exception
            r3.handleBuilderException(r7)
        L_0x003c:
            java.lang.Object r7 = r3.initSelectResult()
            if (r7 != r0) goto L_0x0043
            return r0
        L_0x0043:
            r4 = r7
            r7 = r6
            r6 = r4
        L_0x0046:
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            if (r6 == 0) goto L_0x0050
            r6 = r7
            goto L_0x0023
        L_0x0050:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L_0x0053:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.selects.WhileSelectKt$whileSelect$1.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
    }
}
