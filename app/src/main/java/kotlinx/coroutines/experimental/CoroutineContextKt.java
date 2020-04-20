package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicLong;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007\u001a\b\u0010\t\u001a\u00020\nH\u0000\u001a\u0012\u0010\u000b\u001a\u00020\n2\b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u0001\u001a\u0012\u0010\r\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\u0007H\u0001\u001a*\u0010\u000e\u001a\u0002H\u000f\"\u0004\b\u0000\u0010\u000f2\u0006\u0010\b\u001a\u00020\u00072\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011H\b¢\u0006\u0002\u0010\u0012\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000*8\b\u0007\u0010\u0013\"\u00020\u00142\u00020\u0014B*\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0017\u0012\u001c\b\u0018\u0012\u0018\b\u000bB\u0014\b\u0019\u0012\u0006\b\u001a\u0012\u0002\b\f\u0012\b\b\u001b\u0012\u0004\b\b(\u001c¨\u0006\u001d"}, d2 = {"COROUTINE_ID", "Ljava/util/concurrent/atomic/AtomicLong;", "DEBUG", "", "DEBUG_PROPERTY_NAME", "", "newCoroutineContext", "Lkotlin/coroutines/experimental/CoroutineContext;", "context", "resetCoroutineId", "", "restoreContext", "oldName", "updateContext", "withCoroutineContext", "T", "block", "Lkotlin/Function0;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "Here", "Lkotlinx/coroutines/experimental/Unconfined;", "Lkotlin/Deprecated;", "message", "`Here` was renamed to `Unconfined`", "replaceWith", "Lkotlin/ReplaceWith;", "imports", "expression", "Unconfined", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: CoroutineContext.kt */
public final class CoroutineContextKt {
    private static final AtomicLong COROUTINE_ID = new AtomicLong();
    private static final boolean DEBUG;
    private static final String DEBUG_PROPERTY_NAME = "kotlinx.coroutines.debug";

    @Deprecated(message = "`Here` was renamed to `Unconfined`", replaceWith = @ReplaceWith(expression = "Unconfined", imports = {}))
    public static /* synthetic */ void Here$annotations() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
        if (r1.equals("auto") != false) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
        if (r1.equals("on") != false) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0045, code lost:
        if (r1.equals("") != false) goto L_0x0047;
     */
    static {
        /*
            java.lang.String r0 = "kotlinx.coroutines.debug"
            java.lang.String r1 = java.lang.System.getProperty(r0)
            if (r1 != 0) goto L_0x0009
            goto L_0x0025
        L_0x0009:
            int r2 = r1.hashCode()
            if (r2 == 0) goto L_0x003f
            r3 = 3551(0xddf, float:4.976E-42)
            if (r2 == r3) goto L_0x0036
            r3 = 109935(0x1ad6f, float:1.54052E-40)
            if (r2 == r3) goto L_0x002c
            r3 = 3005871(0x2dddaf, float:4.212122E-39)
            if (r2 != r3) goto L_0x0052
            java.lang.String r2 = "auto"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0052
        L_0x0025:
            java.lang.Class<kotlinx.coroutines.experimental.CoroutineId> r0 = kotlinx.coroutines.experimental.CoroutineId.class
            boolean r0 = r0.desiredAssertionStatus()
            goto L_0x0048
        L_0x002c:
            java.lang.String r2 = "off"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0052
            r0 = 0
            goto L_0x0048
        L_0x0036:
            java.lang.String r2 = "on"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0052
            goto L_0x0047
        L_0x003f:
            java.lang.String r2 = ""
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x0052
        L_0x0047:
            r0 = 1
        L_0x0048:
            DEBUG = r0
            java.util.concurrent.atomic.AtomicLong r0 = new java.util.concurrent.atomic.AtomicLong
            r0.<init>()
            COROUTINE_ID = r0
            return
        L_0x0052:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "System property '"
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = "' has unrecognized value '"
            r2.append(r0)
            r2.append(r1)
            java.lang.String r0 = "'"
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.experimental.CoroutineContextKt.<clinit>():void");
    }

    public static final void resetCoroutineId() {
        COROUTINE_ID.set(0);
    }

    public static final CoroutineContext newCoroutineContext(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DEBUG ? coroutineContext.plus(new CoroutineId(COROUTINE_ID.incrementAndGet())) : coroutineContext;
    }

    public static final <T> T withCoroutineContext(CoroutineContext coroutineContext, Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function0, "block");
        String updateContext = updateContext(coroutineContext);
        try {
            return function0.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            restoreContext(updateContext);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final String updateContext(CoroutineContext coroutineContext) {
        CoroutineId coroutineId;
        String str;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        if (!DEBUG || (coroutineId = (CoroutineId) coroutineContext.get(CoroutineId.Key)) == null) {
            return null;
        }
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        CoroutineName coroutineName = (CoroutineName) coroutineContext.get(CoroutineName.Key);
        if (coroutineName == null || (str = coroutineName.getName()) == null) {
            str = "coroutine";
        }
        StringBuilder sb = new StringBuilder(name.length() + str.length() + 10);
        sb.append(name);
        sb.append(" @");
        sb.append(str);
        sb.append('#');
        sb.append(coroutineId.getId());
        String sb2 = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(sb2, "StringBuilder(capacity).…builderAction).toString()");
        currentThread.setName(sb2);
        return name;
    }

    public static final void restoreContext(String str) {
        if (str != null) {
            Thread.currentThread().setName(str);
        }
    }
}
