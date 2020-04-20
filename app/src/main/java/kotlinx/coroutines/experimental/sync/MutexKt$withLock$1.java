package kotlinx.coroutines.experimental.sync;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.sync.Mutex;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
/* compiled from: Mutex.kt */
final class MutexKt$withLock$1 extends CoroutineImpl {
    final /* synthetic */ Function1 $action;
    private Mutex p$;
    private Function1 p$0;
    final /* synthetic */ Mutex receiver$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MutexKt$withLock$1(Mutex mutex, Function1 function1, Continuation continuation) {
        super(2, continuation);
        this.receiver$0 = mutex;
        this.$action = function1;
    }

    public final Object doResume(Object obj, Throwable th) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    if (th != null) {
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            Mutex.DefaultImpls.unlock$default(this.receiver$0, (Object) null, 1, (Object) null);
                            throw th2;
                        }
                    }
                    Mutex.DefaultImpls.unlock$default(this.receiver$0, (Object) null, 1, (Object) null);
                    return obj;
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            } else if (th != null) {
                throw th;
            }
        } else if (th == null) {
            Mutex mutex = this.receiver$0;
            this.label = 1;
            if (Mutex.DefaultImpls.lock$default(mutex, (Object) null, this, 1, (Object) null) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            throw th;
        }
        Function1 function1 = this.$action;
        this.label = 2;
        obj = function1.invoke(this);
        if (obj == coroutine_suspended) {
            return coroutine_suspended;
        }
        Mutex.DefaultImpls.unlock$default(this.receiver$0, (Object) null, 1, (Object) null);
        return obj;
    }
}
