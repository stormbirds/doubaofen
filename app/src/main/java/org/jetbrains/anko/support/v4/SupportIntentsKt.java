package org.jetbrains.anko.support.v4;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.anko.IntentsKt;
import org.jetbrains.anko.internals.AnkoInternals;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0001\u001a&\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u00042\b\b\u0002\u0010\b\u001a\u00020\u0004\u001aN\u0010\t\u001a\u00020\n\"\n\b\u0000\u0010\u000b\u0018\u0001*\u00020\f*\u00020\u00022.\u0010\r\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f0\u000e\"\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000fH\b¢\u0006\u0002\u0010\u0010\u001a\u0012\u0010\u0011\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0004\u001a\u001c\u0010\u0013\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\b\u001a\u00020\u0004\u001a\u001c\u0010\u0014\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\b\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u0004\u001aN\u0010\u0015\u001a\u00020\u0016\"\n\b\u0000\u0010\u000b\u0018\u0001*\u00020\u0017*\u00020\u00022.\u0010\r\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f0\u000e\"\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000fH\b¢\u0006\u0002\u0010\u0018\u001aV\u0010\u0019\u001a\u00020\u0016\"\n\b\u0000\u0010\u000b\u0018\u0001*\u00020\u0017*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u001b2.\u0010\r\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f0\u000e\"\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000fH\b¢\u0006\u0002\u0010\u001c\u001aN\u0010\u001d\u001a\u00020\u0016\"\n\b\u0000\u0010\u000b\u0018\u0001*\u00020\u001e*\u00020\u00022.\u0010\r\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f0\u000e\"\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000fH\b¢\u0006\u0002\u0010\u0018\u001aN\u0010\u001f\u001a\u00020\u0016\"\n\b\u0000\u0010\u000b\u0018\u0001*\u00020\u001e*\u00020\u00022.\u0010\r\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f0\u000e\"\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000fH\b¢\u0006\u0002\u0010\u0018¨\u0006 "}, d2 = {"browse", "", "Landroid/support/v4/app/Fragment;", "url", "", "newTask", "email", "subject", "text", "intentFor", "Landroid/content/Intent;", "T", "", "params", "", "Lkotlin/Pair;", "(Landroid/support/v4/app/Fragment;[Lkotlin/Pair;)Landroid/content/Intent;", "makeCall", "number", "sendSMS", "share", "startActivity", "", "Landroid/app/Activity;", "(Landroid/support/v4/app/Fragment;[Lkotlin/Pair;)V", "startActivityForResult", "requestCode", "", "(Landroid/support/v4/app/Fragment;I[Lkotlin/Pair;)V", "startService", "Landroid/app/Service;", "stopService", "supportV4_release"}, k = 2, mv = {1, 1, 5})
/* compiled from: SupportIntents.kt */
public final class SupportIntentsKt {
    public static final boolean browse(Fragment fragment, String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(fragment, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "url");
        return IntentsKt.browse((Context) fragment.getActivity(), str, z);
    }

    public static /* bridge */ /* synthetic */ boolean browse$default(Fragment fragment, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return browse(fragment, str, z);
    }

    public static final boolean share(Fragment fragment, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(fragment, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "text");
        Intrinsics.checkParameterIsNotNull(str2, "subject");
        return IntentsKt.share((Context) fragment.getActivity(), str, str2);
    }

    public static /* bridge */ /* synthetic */ boolean share$default(Fragment fragment, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "";
        }
        return share(fragment, str, str2);
    }

    public static /* bridge */ /* synthetic */ boolean email$default(Fragment fragment, String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "";
        }
        if ((i & 4) != 0) {
            str3 = "";
        }
        return email(fragment, str, str2, str3);
    }

    public static final boolean email(Fragment fragment, String str, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(fragment, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, NotificationCompat.CATEGORY_EMAIL);
        Intrinsics.checkParameterIsNotNull(str2, "subject");
        Intrinsics.checkParameterIsNotNull(str3, "text");
        return IntentsKt.email((Context) fragment.getActivity(), str, str2, str3);
    }

    public static final boolean makeCall(Fragment fragment, String str) {
        Intrinsics.checkParameterIsNotNull(fragment, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "number");
        return IntentsKt.makeCall((Context) fragment.getActivity(), str);
    }

    public static final boolean sendSMS(Fragment fragment, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(fragment, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "number");
        Intrinsics.checkParameterIsNotNull(str2, "text");
        return IntentsKt.sendSMS((Context) fragment.getActivity(), str, str2);
    }

    public static /* bridge */ /* synthetic */ boolean sendSMS$default(Fragment fragment, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "";
        }
        return sendSMS(fragment, str, str2);
    }

    private static final <T extends Activity> void startActivity(Fragment fragment, Pair<String, ? extends Object>... pairArr) {
        FragmentActivity activity = fragment.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Intrinsics.reifiedOperationMarker(4, "T");
        AnkoInternals.internalStartActivity(activity, Activity.class, pairArr);
    }

    private static final <T extends Service> void startService(Fragment fragment, Pair<String, ? extends Object>... pairArr) {
        FragmentActivity activity = fragment.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Intrinsics.reifiedOperationMarker(4, "T");
        AnkoInternals.internalStartService(activity, Service.class, pairArr);
    }

    private static final <T extends Service> void stopService(Fragment fragment, Pair<String, ? extends Object>... pairArr) {
        FragmentActivity activity = fragment.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Intrinsics.reifiedOperationMarker(4, "T");
        AnkoInternals.internalStopService(activity, Service.class, pairArr);
    }

    private static final <T> Intent intentFor(Fragment fragment, Pair<String, ? extends Object>... pairArr) {
        FragmentActivity activity = fragment.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Intrinsics.reifiedOperationMarker(4, "T");
        return AnkoInternals.createIntent(activity, Object.class, pairArr);
    }

    private static final <T extends Activity> void startActivityForResult(Fragment fragment, int i, Pair<String, ? extends Object>... pairArr) {
        FragmentActivity activity = fragment.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Intrinsics.reifiedOperationMarker(4, "T");
        fragment.startActivityForResult(AnkoInternals.createIntent(activity, Activity.class, pairArr), i);
    }
}
