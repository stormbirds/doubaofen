package org.jetbrains.anko.support.v4;

import android.widget.TabHost;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 5})
/* compiled from: Listeners.kt */
public final class SupportV4ListenersKt$sam$OnTabChangeListener$i$c20f8b6e implements TabHost.OnTabChangeListener {
    private final /* synthetic */ Function1 function;

    public SupportV4ListenersKt$sam$OnTabChangeListener$i$c20f8b6e(Function1 function1) {
        this.function = function1;
    }

    public final /* synthetic */ void onTabChanged(String str) {
        Intrinsics.checkExpressionValueIsNotNull(this.function.invoke(str), "invoke(...)");
    }
}
