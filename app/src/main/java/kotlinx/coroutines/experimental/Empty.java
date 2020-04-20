package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlinx.coroutines.experimental.JobSupport;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\n\u001a\u00020\u000bH\u0016R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\t¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/experimental/Empty;", "Lkotlinx/coroutines/experimental/JobSupport$Incomplete;", "isActive", "", "(Z)V", "idempotentStart", "", "getIdempotentStart", "()Ljava/lang/Object;", "()Z", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
final class Empty implements JobSupport.Incomplete {
    private final boolean isActive;

    public Object getIdempotentStart() {
        return null;
    }

    public Empty(boolean z) {
        this.isActive = z;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Empty{");
        sb.append(isActive() ? "Active" : "New");
        sb.append("}");
        return sb.toString();
    }
}
