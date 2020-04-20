package org.jetbrains.anko.support.v4;

import android.support.v4.widget.NestedScrollView;
import kotlin.Metadata;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 5})
/* compiled from: Listeners.kt */
public final class SupportV4ListenersKt$sam$OnScrollChangeListener$i$e1483bad implements NestedScrollView.OnScrollChangeListener {
    private final /* synthetic */ Function5 function;

    public SupportV4ListenersKt$sam$OnScrollChangeListener$i$e1483bad(Function5 function5) {
        this.function = function5;
    }

    public final /* synthetic */ void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
        Intrinsics.checkExpressionValueIsNotNull(this.function.invoke(nestedScrollView, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)), "invoke(...)");
    }
}
