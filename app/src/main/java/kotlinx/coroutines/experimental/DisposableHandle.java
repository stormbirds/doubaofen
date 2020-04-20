package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlinx.coroutines.experimental.Job;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H\u0017¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/experimental/DisposableHandle;", "Lkotlinx/coroutines/experimental/Job$Registration;", "dispose", "", "unregister", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
public interface DisposableHandle extends Job.Registration {
    void dispose();

    @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
    void unregister();

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public static final class DefaultImpls {
        @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
        public static void unregister(DisposableHandle disposableHandle) {
            disposableHandle.dispose();
        }
    }
}
