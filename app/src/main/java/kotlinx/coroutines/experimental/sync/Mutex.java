package kotlinx.coroutines.experimental.sync;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013J\u001d\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0001H¦@ø\u0001\u0000¢\u0006\u0002\u0010\bJL\u0010\t\u001a\u00020\u0006\"\u0004\b\u0000\u0010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\n0\f2\b\u0010\u0007\u001a\u0004\u0018\u00010\u00012\u001c\u0010\r\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000eH&ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0014\u0010\u0011\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0001H&J\u0014\u0010\u0012\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0001H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004\u0002\u0004\n\u0002\b\t¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/experimental/sync/Mutex;", "", "isLocked", "", "()Z", "lock", "", "owner", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "registerSelectLock", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "tryLock", "unlock", "Factory", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Mutex.kt */
public interface Mutex {
    public static final Factory Factory = new Factory((DefaultConstructorMarker) null);

    boolean isLocked();

    Object lock(Object obj, Continuation<? super Unit> continuation);

    <R> void registerSelectLock(SelectInstance<? super R> selectInstance, Object obj, Function1<? super Continuation<? super R>, ? extends Object> function1);

    boolean tryLock(Object obj);

    void unlock(Object obj);

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0002¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/sync/Mutex$Factory;", "", "()V", "invoke", "Lkotlinx/coroutines/experimental/sync/Mutex;", "locked", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    public static final class Factory {
        private Factory() {
        }

        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* bridge */ /* synthetic */ Mutex invoke$default(Factory factory, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                z = false;
            }
            return factory.invoke(z);
        }

        public final Mutex invoke(boolean z) {
            return new MutexImpl(z);
        }
    }

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Mutex.kt */
    public static final class DefaultImpls {
        public static /* bridge */ /* synthetic */ boolean tryLock$default(Mutex mutex, Object obj, int i, Object obj2) {
            if (obj2 == null) {
                if ((i & 1) != 0) {
                    obj = null;
                }
                return mutex.tryLock(obj);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: tryLock");
        }

        public static /* bridge */ /* synthetic */ Object lock$default(Mutex mutex, Object obj, Continuation continuation, int i, Object obj2) {
            if (obj2 == null) {
                if ((i & 1) != 0) {
                    obj = null;
                }
                return mutex.lock(obj, continuation);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lock");
        }

        public static /* bridge */ /* synthetic */ void unlock$default(Mutex mutex, Object obj, int i, Object obj2) {
            if (obj2 == null) {
                if ((i & 1) != 0) {
                    obj = null;
                }
                mutex.unlock(obj);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: unlock");
        }
    }
}
