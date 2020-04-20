package org.jetbrains.anko;

import android.content.Context;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000ª\u0001\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÁ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\bR\u001d\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r0\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\bR\u001d\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00100\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\bR\u001d\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00130\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\bR\u001d\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00160\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\bR\u001d\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00190\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\bR\u001d\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001c0\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\bR\u001d\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001f0\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010\bR\u001d\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\bR\u001d\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020%0\u0004¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\bR\u001d\u0010'\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020(0\u0004¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\bR\u001d\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020+0\u0004¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\bR\u001d\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020.0\u0004¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\bR\u001d\u00100\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002010\u0004¢\u0006\b\n\u0000\u001a\u0004\b2\u0010\bR\u001d\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002040\u0004¢\u0006\b\n\u0000\u001a\u0004\b5\u0010\bR\u001d\u00106\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002070\u0004¢\u0006\b\n\u0000\u001a\u0004\b8\u0010\bR\u001d\u00109\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020:0\u0004¢\u0006\b\n\u0000\u001a\u0004\b;\u0010\bR\u001d\u0010<\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020=0\u0004¢\u0006\b\n\u0000\u001a\u0004\b>\u0010\b¨\u0006?"}, d2 = {"org/jetbrains/anko/$$Anko$Factories$Sdk25ViewGroup", "", "()V", "ABSOLUTE_LAYOUT", "Lkotlin/Function1;", "Landroid/content/Context;", "Lorg/jetbrains/anko/_AbsoluteLayout;", "getABSOLUTE_LAYOUT", "()Lkotlin/jvm/functions/Function1;", "ACTION_MENU_VIEW", "Lorg/jetbrains/anko/_ActionMenuView;", "getACTION_MENU_VIEW", "APP_WIDGET_HOST_VIEW", "Lorg/jetbrains/anko/_AppWidgetHostView;", "getAPP_WIDGET_HOST_VIEW", "FRAME_LAYOUT", "Lorg/jetbrains/anko/_FrameLayout;", "getFRAME_LAYOUT", "GALLERY", "Lorg/jetbrains/anko/_Gallery;", "getGALLERY", "GRID_LAYOUT", "Lorg/jetbrains/anko/_GridLayout;", "getGRID_LAYOUT", "GRID_VIEW", "Lorg/jetbrains/anko/_GridView;", "getGRID_VIEW", "HORIZONTAL_SCROLL_VIEW", "Lorg/jetbrains/anko/_HorizontalScrollView;", "getHORIZONTAL_SCROLL_VIEW", "IMAGE_SWITCHER", "Lorg/jetbrains/anko/_ImageSwitcher;", "getIMAGE_SWITCHER", "LINEAR_LAYOUT", "Lorg/jetbrains/anko/_LinearLayout;", "getLINEAR_LAYOUT", "RADIO_GROUP", "Lorg/jetbrains/anko/_RadioGroup;", "getRADIO_GROUP", "RELATIVE_LAYOUT", "Lorg/jetbrains/anko/_RelativeLayout;", "getRELATIVE_LAYOUT", "SCROLL_VIEW", "Lorg/jetbrains/anko/_ScrollView;", "getSCROLL_VIEW", "TABLE_LAYOUT", "Lorg/jetbrains/anko/_TableLayout;", "getTABLE_LAYOUT", "TABLE_ROW", "Lorg/jetbrains/anko/_TableRow;", "getTABLE_ROW", "TEXT_SWITCHER", "Lorg/jetbrains/anko/_TextSwitcher;", "getTEXT_SWITCHER", "TOOLBAR", "Lorg/jetbrains/anko/_Toolbar;", "getTOOLBAR", "VIEW_ANIMATOR", "Lorg/jetbrains/anko/_ViewAnimator;", "getVIEW_ANIMATOR", "VIEW_SWITCHER", "Lorg/jetbrains/anko/_ViewSwitcher;", "getVIEW_SWITCHER", "anko-sdk25_release"}, k = 1, mv = {1, 1, 5})
/* renamed from: org.jetbrains.anko.$$Anko$Factories$Sdk25ViewGroup  reason: invalid class name */
/* compiled from: Views.kt */
public final class C$$Anko$Factories$Sdk25ViewGroup {
    private static final Function1<Context, _AbsoluteLayout> ABSOLUTE_LAYOUT = null;
    private static final Function1<Context, _ActionMenuView> ACTION_MENU_VIEW = null;
    private static final Function1<Context, _AppWidgetHostView> APP_WIDGET_HOST_VIEW = null;
    private static final Function1<Context, _FrameLayout> FRAME_LAYOUT = null;
    private static final Function1<Context, _Gallery> GALLERY = null;
    private static final Function1<Context, _GridLayout> GRID_LAYOUT = null;
    private static final Function1<Context, _GridView> GRID_VIEW = null;
    private static final Function1<Context, _HorizontalScrollView> HORIZONTAL_SCROLL_VIEW = null;
    private static final Function1<Context, _ImageSwitcher> IMAGE_SWITCHER = null;
    public static final C$$Anko$Factories$Sdk25ViewGroup INSTANCE = null;
    private static final Function1<Context, _LinearLayout> LINEAR_LAYOUT = null;
    private static final Function1<Context, _RadioGroup> RADIO_GROUP = null;
    private static final Function1<Context, _RelativeLayout> RELATIVE_LAYOUT = null;
    private static final Function1<Context, _ScrollView> SCROLL_VIEW = null;
    private static final Function1<Context, _TableLayout> TABLE_LAYOUT = null;
    private static final Function1<Context, _TableRow> TABLE_ROW = null;
    private static final Function1<Context, _TextSwitcher> TEXT_SWITCHER = null;
    private static final Function1<Context, _Toolbar> TOOLBAR = null;
    private static final Function1<Context, _ViewAnimator> VIEW_ANIMATOR = null;
    private static final Function1<Context, _ViewSwitcher> VIEW_SWITCHER = null;

    static {
        new C$$Anko$Factories$Sdk25ViewGroup();
    }

    private C$$Anko$Factories$Sdk25ViewGroup() {
        INSTANCE = this;
        APP_WIDGET_HOST_VIEW = C$$Anko$Factories$Sdk25ViewGroup$APP_WIDGET_HOST_VIEW$1.INSTANCE;
        ABSOLUTE_LAYOUT = C$$Anko$Factories$Sdk25ViewGroup$ABSOLUTE_LAYOUT$1.INSTANCE;
        ACTION_MENU_VIEW = C$$Anko$Factories$Sdk25ViewGroup$ACTION_MENU_VIEW$1.INSTANCE;
        FRAME_LAYOUT = C$$Anko$Factories$Sdk25ViewGroup$FRAME_LAYOUT$1.INSTANCE;
        GALLERY = C$$Anko$Factories$Sdk25ViewGroup$GALLERY$1.INSTANCE;
        GRID_LAYOUT = C$$Anko$Factories$Sdk25ViewGroup$GRID_LAYOUT$1.INSTANCE;
        GRID_VIEW = C$$Anko$Factories$Sdk25ViewGroup$GRID_VIEW$1.INSTANCE;
        HORIZONTAL_SCROLL_VIEW = C$$Anko$Factories$Sdk25ViewGroup$HORIZONTAL_SCROLL_VIEW$1.INSTANCE;
        IMAGE_SWITCHER = C$$Anko$Factories$Sdk25ViewGroup$IMAGE_SWITCHER$1.INSTANCE;
        LINEAR_LAYOUT = C$$Anko$Factories$Sdk25ViewGroup$LINEAR_LAYOUT$1.INSTANCE;
        RADIO_GROUP = C$$Anko$Factories$Sdk25ViewGroup$RADIO_GROUP$1.INSTANCE;
        RELATIVE_LAYOUT = C$$Anko$Factories$Sdk25ViewGroup$RELATIVE_LAYOUT$1.INSTANCE;
        SCROLL_VIEW = C$$Anko$Factories$Sdk25ViewGroup$SCROLL_VIEW$1.INSTANCE;
        TABLE_LAYOUT = C$$Anko$Factories$Sdk25ViewGroup$TABLE_LAYOUT$1.INSTANCE;
        TABLE_ROW = C$$Anko$Factories$Sdk25ViewGroup$TABLE_ROW$1.INSTANCE;
        TEXT_SWITCHER = C$$Anko$Factories$Sdk25ViewGroup$TEXT_SWITCHER$1.INSTANCE;
        TOOLBAR = C$$Anko$Factories$Sdk25ViewGroup$TOOLBAR$1.INSTANCE;
        VIEW_ANIMATOR = C$$Anko$Factories$Sdk25ViewGroup$VIEW_ANIMATOR$1.INSTANCE;
        VIEW_SWITCHER = C$$Anko$Factories$Sdk25ViewGroup$VIEW_SWITCHER$1.INSTANCE;
    }

    public final Function1<Context, _AppWidgetHostView> getAPP_WIDGET_HOST_VIEW() {
        return APP_WIDGET_HOST_VIEW;
    }

    public final Function1<Context, _AbsoluteLayout> getABSOLUTE_LAYOUT() {
        return ABSOLUTE_LAYOUT;
    }

    public final Function1<Context, _ActionMenuView> getACTION_MENU_VIEW() {
        return ACTION_MENU_VIEW;
    }

    public final Function1<Context, _FrameLayout> getFRAME_LAYOUT() {
        return FRAME_LAYOUT;
    }

    public final Function1<Context, _Gallery> getGALLERY() {
        return GALLERY;
    }

    public final Function1<Context, _GridLayout> getGRID_LAYOUT() {
        return GRID_LAYOUT;
    }

    public final Function1<Context, _GridView> getGRID_VIEW() {
        return GRID_VIEW;
    }

    public final Function1<Context, _HorizontalScrollView> getHORIZONTAL_SCROLL_VIEW() {
        return HORIZONTAL_SCROLL_VIEW;
    }

    public final Function1<Context, _ImageSwitcher> getIMAGE_SWITCHER() {
        return IMAGE_SWITCHER;
    }

    public final Function1<Context, _LinearLayout> getLINEAR_LAYOUT() {
        return LINEAR_LAYOUT;
    }

    public final Function1<Context, _RadioGroup> getRADIO_GROUP() {
        return RADIO_GROUP;
    }

    public final Function1<Context, _RelativeLayout> getRELATIVE_LAYOUT() {
        return RELATIVE_LAYOUT;
    }

    public final Function1<Context, _ScrollView> getSCROLL_VIEW() {
        return SCROLL_VIEW;
    }

    public final Function1<Context, _TableLayout> getTABLE_LAYOUT() {
        return TABLE_LAYOUT;
    }

    public final Function1<Context, _TableRow> getTABLE_ROW() {
        return TABLE_ROW;
    }

    public final Function1<Context, _TextSwitcher> getTEXT_SWITCHER() {
        return TEXT_SWITCHER;
    }

    public final Function1<Context, _Toolbar> getTOOLBAR() {
        return TOOLBAR;
    }

    public final Function1<Context, _ViewAnimator> getVIEW_ANIMATOR() {
        return VIEW_ANIMATOR;
    }

    public final Function1<Context, _ViewSwitcher> getVIEW_SWITCHER() {
        return VIEW_SWITCHER;
    }
}
