package org.jetbrains.anko;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u0006H\b¢\u0006\u0002\u0010\bJ0\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\u0010\t\u001a\u0004\u0018\u00010\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\b¢\u0006\u0002\u0010\fJI\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\u0010\t\u001a\u0004\u0018\u00010\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010\u0012J&\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\b¢\u0006\u0002\u0010\u0015J?\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010\u0016J&\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\b¢\u0006\u0002\u0010\u0019J?\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010\u001aJ5\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010\u001bJ$\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\u0006\u0010\u001c\u001a\u00020\u001dH\b¢\u0006\u0002\u0010\u001eJ=\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\u0006\u0010\u001c\u001a\u00020\u001d2\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010\u001fJ0\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\b\u0002\u0010 \u001a\u00020\u001d2\b\b\u0002\u0010!\u001a\u00020\u001dH\b¢\u0006\u0002\u0010\"JI\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\b\u0002\u0010 \u001a\u00020\u001d2\b\b\u0002\u0010!\u001a\u00020\u001d2\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010#J8\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\b\u0002\u0010 \u001a\u00020\u001d2\b\b\u0002\u0010!\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020%H\b¢\u0006\u0002\u0010&JQ\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u0007*\u0002H\u00062\b\b\u0002\u0010 \u001a\u00020\u001d2\b\b\u0002\u0010!\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020%2\u0017\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e¢\u0006\u0002\b\u0011H\b¢\u0006\u0002\u0010'¨\u0006("}, d2 = {"Lorg/jetbrains/anko/_TableRow;", "Landroid/widget/TableRow;", "ctx", "Landroid/content/Context;", "(Landroid/content/Context;)V", "lparams", "T", "Landroid/view/View;", "(Landroid/view/View;)Landroid/view/View;", "c", "attrs", "Landroid/util/AttributeSet;", "(Landroid/view/View;Landroid/content/Context;Landroid/util/AttributeSet;)Landroid/view/View;", "init", "Lkotlin/Function1;", "Landroid/widget/TableRow$LayoutParams;", "", "Lkotlin/ExtensionFunctionType;", "(Landroid/view/View;Landroid/content/Context;Landroid/util/AttributeSet;Lkotlin/jvm/functions/Function1;)Landroid/view/View;", "p", "Landroid/view/ViewGroup$LayoutParams;", "(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)Landroid/view/View;", "(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;Lkotlin/jvm/functions/Function1;)Landroid/view/View;", "source", "Landroid/view/ViewGroup$MarginLayoutParams;", "(Landroid/view/View;Landroid/view/ViewGroup$MarginLayoutParams;)Landroid/view/View;", "(Landroid/view/View;Landroid/view/ViewGroup$MarginLayoutParams;Lkotlin/jvm/functions/Function1;)Landroid/view/View;", "(Landroid/view/View;Lkotlin/jvm/functions/Function1;)Landroid/view/View;", "column", "", "(Landroid/view/View;I)Landroid/view/View;", "(Landroid/view/View;ILkotlin/jvm/functions/Function1;)Landroid/view/View;", "width", "height", "(Landroid/view/View;II)Landroid/view/View;", "(Landroid/view/View;IILkotlin/jvm/functions/Function1;)Landroid/view/View;", "initWeight", "", "(Landroid/view/View;IIF)Landroid/view/View;", "(Landroid/view/View;IIFLkotlin/jvm/functions/Function1;)Landroid/view/View;", "anko-sdk25_release"}, k = 1, mv = {1, 1, 5})
/* compiled from: Layouts.kt */
public class _TableRow extends TableRow {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public _TableRow(Context context) {
        super(context);
        Intrinsics.checkParameterIsNotNull(context, "ctx");
    }

    public final <T extends View> T lparams(T t, Context context, AttributeSet attributeSet, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        if (context == null) {
            Intrinsics.throwNpe();
        }
        if (attributeSet == null) {
            Intrinsics.throwNpe();
        }
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(context, attributeSet);
        function1.invoke(layoutParams);
        t.setLayoutParams(layoutParams);
        return t;
    }

    public final <T extends View> T lparams(T t, Context context, AttributeSet attributeSet) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        if (context == null) {
            Intrinsics.throwNpe();
        }
        if (attributeSet == null) {
            Intrinsics.throwNpe();
        }
        t.setLayoutParams(new TableRow.LayoutParams(context, attributeSet));
        return t;
    }

    public static /* bridge */ /* synthetic */ View lparams$default(_TableRow _tablerow, View view, int i, int i2, Function1 function1, int i3, Object obj) {
        if (obj == null) {
            if ((i3 & 1) != 0) {
                i = -2;
            }
            if ((i3 & 2) != 0) {
                i2 = -2;
            }
            Intrinsics.checkParameterIsNotNull(view, "$receiver");
            Intrinsics.checkParameterIsNotNull(function1, "init");
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(i, i2);
            function1.invoke(layoutParams);
            view.setLayoutParams(layoutParams);
            return view;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lparams");
    }

    public final <T extends View> T lparams(T t, int i, int i2, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(i, i2);
        function1.invoke(layoutParams);
        t.setLayoutParams(layoutParams);
        return t;
    }

    public static /* bridge */ /* synthetic */ View lparams$default(_TableRow _tablerow, View view, int i, int i2, int i3, Object obj) {
        if (obj == null) {
            if ((i3 & 1) != 0) {
                i = -2;
            }
            if ((i3 & 2) != 0) {
                i2 = -2;
            }
            Intrinsics.checkParameterIsNotNull(view, "$receiver");
            view.setLayoutParams(new TableRow.LayoutParams(i, i2));
            return view;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lparams");
    }

    public final <T extends View> T lparams(T t, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        t.setLayoutParams(new TableRow.LayoutParams(i, i2));
        return t;
    }

    public static /* bridge */ /* synthetic */ View lparams$default(_TableRow _tablerow, View view, int i, int i2, float f, Function1 function1, int i3, Object obj) {
        if (obj == null) {
            if ((i3 & 1) != 0) {
                i = -2;
            }
            if ((i3 & 2) != 0) {
                i2 = -2;
            }
            Intrinsics.checkParameterIsNotNull(view, "$receiver");
            Intrinsics.checkParameterIsNotNull(function1, "init");
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(i, i2, f);
            function1.invoke(layoutParams);
            view.setLayoutParams(layoutParams);
            return view;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lparams");
    }

    public final <T extends View> T lparams(T t, int i, int i2, float f, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(i, i2, f);
        function1.invoke(layoutParams);
        t.setLayoutParams(layoutParams);
        return t;
    }

    public static /* bridge */ /* synthetic */ View lparams$default(_TableRow _tablerow, View view, int i, int i2, float f, int i3, Object obj) {
        if (obj == null) {
            if ((i3 & 1) != 0) {
                i = -2;
            }
            if ((i3 & 2) != 0) {
                i2 = -2;
            }
            Intrinsics.checkParameterIsNotNull(view, "$receiver");
            view.setLayoutParams(new TableRow.LayoutParams(i, i2, f));
            return view;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lparams");
    }

    public final <T extends View> T lparams(T t, int i, int i2, float f) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        t.setLayoutParams(new TableRow.LayoutParams(i, i2, f));
        return t;
    }

    public final <T extends View> T lparams(T t, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        function1.invoke(layoutParams);
        t.setLayoutParams(layoutParams);
        return t;
    }

    public final <T extends View> T lparams(T t) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        t.setLayoutParams(new TableRow.LayoutParams());
        return t;
    }

    public final <T extends View> T lparams(T t, int i, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(i);
        function1.invoke(layoutParams);
        t.setLayoutParams(layoutParams);
        return t;
    }

    public final <T extends View> T lparams(T t, int i) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        t.setLayoutParams(new TableRow.LayoutParams(i));
        return t;
    }

    public final <T extends View> T lparams(T t, ViewGroup.LayoutParams layoutParams, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        if (layoutParams == null) {
            Intrinsics.throwNpe();
        }
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(layoutParams);
        function1.invoke(layoutParams2);
        t.setLayoutParams(layoutParams2);
        return t;
    }

    public final <T extends View> T lparams(T t, ViewGroup.LayoutParams layoutParams) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        if (layoutParams == null) {
            Intrinsics.throwNpe();
        }
        t.setLayoutParams(new TableRow.LayoutParams(layoutParams));
        return t;
    }

    public final <T extends View> T lparams(T t, ViewGroup.MarginLayoutParams marginLayoutParams, Function1<? super TableRow.LayoutParams, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        if (marginLayoutParams == null) {
            Intrinsics.throwNpe();
        }
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(marginLayoutParams);
        function1.invoke(layoutParams);
        t.setLayoutParams(layoutParams);
        return t;
    }

    public final <T extends View> T lparams(T t, ViewGroup.MarginLayoutParams marginLayoutParams) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        if (marginLayoutParams == null) {
            Intrinsics.throwNpe();
        }
        t.setLayoutParams(new TableRow.LayoutParams(marginLayoutParams));
        return t;
    }
}
