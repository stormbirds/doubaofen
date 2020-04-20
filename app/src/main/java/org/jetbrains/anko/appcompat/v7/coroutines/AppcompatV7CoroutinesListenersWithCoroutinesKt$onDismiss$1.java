package org.jetbrains.anko.appcompat.v7.coroutines;

import android.widget.PopupWindow;
import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.experimental.BuildersKt;
import kotlinx.coroutines.experimental.CoroutineStart;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "onDismiss"}, k = 3, mv = {1, 1, 5})
/* compiled from: ListenersWithCoroutines.kt */
final class AppcompatV7CoroutinesListenersWithCoroutinesKt$onDismiss$1 implements PopupWindow.OnDismissListener {
    final /* synthetic */ CoroutineContext $context;
    final /* synthetic */ Function2 $handler;

    AppcompatV7CoroutinesListenersWithCoroutinesKt$onDismiss$1(CoroutineContext coroutineContext, Function2 function2) {
        this.$context = coroutineContext;
        this.$handler = function2;
    }

    public final void onDismiss() {
        BuildersKt.launch$default(this.$context, (CoroutineStart) null, this.$handler, 2, (Object) null);
    }
}
