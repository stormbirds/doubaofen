package org.jetbrains.anko.support.v4;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 5})
/* compiled from: Listeners.kt */
public final class SupportV4ListenersKt$sam$OnAdapterChangeListener$i$cdf477fd implements ViewPager.OnAdapterChangeListener {
    private final /* synthetic */ Function3 function;

    public SupportV4ListenersKt$sam$OnAdapterChangeListener$i$cdf477fd(Function3 function3) {
        this.function = function3;
    }

    public final /* synthetic */ void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
        Intrinsics.checkParameterIsNotNull(viewPager, "p0");
        Intrinsics.checkExpressionValueIsNotNull(this.function.invoke(viewPager, pagerAdapter, pagerAdapter2), "invoke(...)");
    }
}
