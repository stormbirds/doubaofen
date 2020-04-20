package org.jetbrains.anko.appcompat.v7;

import android.content.DialogInterface;
import android.view.KeyEvent;
import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 5})
/* compiled from: SupportAlertBuilder.kt */
final class SupportAlertBuilderKt$sam$OnKeyListener$661ed509 implements DialogInterface.OnKeyListener {
    private final /* synthetic */ Function3 function;

    SupportAlertBuilderKt$sam$OnKeyListener$661ed509(Function3 function3) {
        this.function = function3;
    }

    public final /* synthetic */ boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        Object invoke = this.function.invoke(dialogInterface, Integer.valueOf(i), keyEvent);
        Intrinsics.checkExpressionValueIsNotNull(invoke, "invoke(...)");
        return ((Boolean) invoke).booleanValue();
    }
}
