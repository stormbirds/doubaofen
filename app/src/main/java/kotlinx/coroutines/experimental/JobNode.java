package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.DisposableHandle;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.JobSupport;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\b \u0018\u0000*\n\b\u0000\u0010\u0001 \u0001*\u00020\u00022\u00020\u00032\u00020\u00042\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0006\u0012\u0004\u0012\u00020\u00070\u0005j\u0002`\b2\u00020\tB\r\u0012\u0006\u0010\n\u001a\u00028\u0000¢\u0006\u0002\u0010\u000bJ\u0006\u0010\u0014\u001a\u00020\u0007J\u0013\u0010\u0015\u001a\u00020\u00072\b\u0010\u0016\u001a\u0004\u0018\u00010\u0006H¦\u0002R\u0013\u0010\f\u001a\u0004\u0018\u00010\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u00118F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0012R\u0012\u0010\n\u001a\u00028\u00008\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0013¨\u0006\u0017"}, d2 = {"Lkotlinx/coroutines/experimental/JobNode;", "J", "Lkotlinx/coroutines/experimental/Job;", "Lkotlinx/coroutines/experimental/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/experimental/DisposableHandle;", "Lkotlin/Function1;", "", "", "Lkotlinx/coroutines/experimental/CompletionHandler;", "Lkotlinx/coroutines/experimental/JobSupport$Incomplete;", "job", "(Lkotlinx/coroutines/experimental/Job;)V", "idempotentStart", "", "getIdempotentStart", "()Ljava/lang/Object;", "isActive", "", "()Z", "Lkotlinx/coroutines/experimental/Job;", "dispose", "invoke", "reason", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
public abstract class JobNode<J extends Job> extends LockFreeLinkedListNode implements DisposableHandle, Function1<Throwable, Unit>, JobSupport.Incomplete {
    public final J job;

    public final Object getIdempotentStart() {
        return null;
    }

    public abstract void invoke(Throwable th);

    public final boolean isActive() {
        return true;
    }

    @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
    public void unregister() {
        DisposableHandle.DefaultImpls.unregister(this);
    }

    public JobNode(J j) {
        Intrinsics.checkParameterIsNotNull(j, "job");
        this.job = j;
    }

    public final void dispose() {
        J j = this.job;
        if (j != null) {
            ((JobSupport) j).removeNode$kotlinx_coroutines_core(this);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobSupport");
    }
}
