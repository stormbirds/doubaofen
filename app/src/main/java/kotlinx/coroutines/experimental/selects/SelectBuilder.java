package kotlinx.coroutines.experimental.selects;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.experimental.Deferred;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.channels.ReceiveChannel;
import kotlinx.coroutines.experimental.channels.SendChannel;
import kotlinx.coroutines.experimental.sync.Mutex;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002J@\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\u001c\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\nH&ø\u0001\u0000¢\u0006\u0002\u0010\fJD\u0010\r\u001a\u00020\u0004\"\u0004\b\u0001\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u000f2\"\u0010\t\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0010H&ø\u0001\u0000¢\u0006\u0002\u0010\u0011J2\u0010\u0012\u001a\u00020\u0004*\u00020\u00132\u001c\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\nH&ø\u0001\u0000¢\u0006\u0002\u0010\u0014J>\u0010\u0015\u001a\u00020\u0004*\u00020\u00162\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00022\u001c\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\nH&ø\u0001\u0000¢\u0006\u0002\u0010\u0018JD\u0010\u0019\u001a\u00020\u0004\"\u0004\b\u0001\u0010\u001a*\b\u0012\u0004\u0012\u0002H\u001a0\u001b2\"\u0010\t\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0010H&ø\u0001\u0000¢\u0006\u0002\u0010\u001cJF\u0010\u001d\u001a\u00020\u0004\"\u0004\b\u0001\u0010\u001a*\b\u0012\u0004\u0012\u0002H\u001a0\u001b2$\u0010\t\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u0001H\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0010H&ø\u0001\u0000¢\u0006\u0002\u0010\u001cJF\u0010\u001e\u001a\u00020\u0004\"\u0004\b\u0001\u0010\u001a*\b\u0012\u0004\u0012\u0002H\u001a0\u001f2\u0006\u0010 \u001a\u0002H\u001a2\u001c\u0010\t\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\nH&ø\u0001\u0000¢\u0006\u0002\u0010!\u0002\u0004\n\u0002\b\t¨\u0006\""}, d2 = {"Lkotlinx/coroutines/experimental/selects/SelectBuilder;", "R", "", "onTimeout", "", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(JLjava/util/concurrent/TimeUnit;Lkotlin/jvm/functions/Function1;)V", "onAwait", "T", "Lkotlinx/coroutines/experimental/Deferred;", "Lkotlin/Function2;", "(Lkotlinx/coroutines/experimental/Deferred;Lkotlin/jvm/functions/Function2;)V", "onJoin", "Lkotlinx/coroutines/experimental/Job;", "(Lkotlinx/coroutines/experimental/Job;Lkotlin/jvm/functions/Function1;)V", "onLock", "Lkotlinx/coroutines/experimental/sync/Mutex;", "owner", "(Lkotlinx/coroutines/experimental/sync/Mutex;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "onReceive", "E", "Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "(Lkotlinx/coroutines/experimental/channels/ReceiveChannel;Lkotlin/jvm/functions/Function2;)V", "onReceiveOrNull", "onSend", "Lkotlinx/coroutines/experimental/channels/SendChannel;", "element", "(Lkotlinx/coroutines/experimental/channels/SendChannel;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Select.kt */
public interface SelectBuilder<R> {
    <T> void onAwait(Deferred<? extends T> deferred, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2);

    void onJoin(Job job, Function1<? super Continuation<? super R>, ? extends Object> function1);

    void onLock(Mutex mutex, Object obj, Function1<? super Continuation<? super R>, ? extends Object> function1);

    <E> void onReceive(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2);

    <E> void onReceiveOrNull(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2);

    <E> void onSend(SendChannel<? super E> sendChannel, E e, Function1<? super Continuation<? super R>, ? extends Object> function1);

    void onTimeout(long j, TimeUnit timeUnit, Function1<? super Continuation<? super R>, ? extends Object> function1);

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Select.kt */
    public static final class DefaultImpls {
        public static /* bridge */ /* synthetic */ void onLock$default(SelectBuilder selectBuilder, Mutex mutex, Object obj, Function1 function1, int i, Object obj2) {
            if (obj2 == null) {
                if ((i & 1) != 0) {
                    obj = null;
                }
                selectBuilder.onLock(mutex, obj, function1);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onLock");
        }

        public static /* bridge */ /* synthetic */ void onTimeout$default(SelectBuilder selectBuilder, long j, TimeUnit timeUnit, Function1 function1, int i, Object obj) {
            if (obj == null) {
                if ((i & 2) != 0) {
                    timeUnit = TimeUnit.MILLISECONDS;
                }
                selectBuilder.onTimeout(j, timeUnit, function1);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onTimeout");
        }
    }
}
