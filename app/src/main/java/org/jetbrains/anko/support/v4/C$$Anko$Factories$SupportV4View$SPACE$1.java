package org.jetbrains.anko.support.v4;

import android.content.Context;
import android.support.v4.widget.Space;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Landroid/support/v4/widget/Space;", "ctx", "Landroid/content/Context;", "invoke"}, k = 3, mv = {1, 1, 5})
/* renamed from: org.jetbrains.anko.support.v4.$$Anko$Factories$SupportV4View$SPACE$1  reason: invalid class name */
/* compiled from: Views.kt */
final class C$$Anko$Factories$SupportV4View$SPACE$1 extends Lambda implements Function1<Context, Space> {
    public static final C$$Anko$Factories$SupportV4View$SPACE$1 INSTANCE = new C$$Anko$Factories$SupportV4View$SPACE$1();

    C$$Anko$Factories$SupportV4View$SPACE$1() {
        super(1);
    }

    public final Space invoke(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "ctx");
        return new Space(context);
    }
}
