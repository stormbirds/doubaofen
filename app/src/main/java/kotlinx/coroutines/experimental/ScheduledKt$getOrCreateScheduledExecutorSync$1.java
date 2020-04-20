package kotlinx.coroutines.experimental;

import java.util.concurrent.ThreadFactory;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "Ljava/lang/Thread;", "r", "Ljava/lang/Runnable;", "kotlin.jvm.PlatformType", "newThread"}, k = 3, mv = {1, 1, 6})
/* compiled from: Scheduled.kt */
final class ScheduledKt$getOrCreateScheduledExecutorSync$1 implements ThreadFactory {
    public static final ScheduledKt$getOrCreateScheduledExecutorSync$1 INSTANCE = new ScheduledKt$getOrCreateScheduledExecutorSync$1();

    ScheduledKt$getOrCreateScheduledExecutorSync$1() {
    }

    public final Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "kotlinx.coroutines.ScheduledExecutor");
        thread.setDaemon(true);
        return thread;
    }
}
