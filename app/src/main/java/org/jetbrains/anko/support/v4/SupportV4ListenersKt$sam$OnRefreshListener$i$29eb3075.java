package org.jetbrains.anko.support.v4;

import android.support.v4.widget.SwipeRefreshLayout;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 5})
/* compiled from: Listeners.kt */
public final class SupportV4ListenersKt$sam$OnRefreshListener$i$29eb3075 implements SwipeRefreshLayout.OnRefreshListener {
    private final /* synthetic */ Function0 function;

    public SupportV4ListenersKt$sam$OnRefreshListener$i$29eb3075(Function0 function0) {
        this.function = function0;
    }

    public final /* synthetic */ void onRefresh() {
        Intrinsics.checkExpressionValueIsNotNull(this.function.invoke(), "invoke(...)");
    }
}