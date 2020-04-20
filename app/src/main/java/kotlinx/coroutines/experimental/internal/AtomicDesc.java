package kotlinx.coroutines.experimental.internal;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H&J\u0012\u0010\b\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0006H&¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/experimental/internal/AtomicDesc;", "", "()V", "complete", "", "op", "Lkotlinx/coroutines/experimental/internal/AtomicOp;", "failure", "prepare", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Atomic.kt */
public abstract class AtomicDesc {
    public abstract void complete(AtomicOp atomicOp, Object obj);

    public abstract Object prepare(AtomicOp atomicOp);
}
