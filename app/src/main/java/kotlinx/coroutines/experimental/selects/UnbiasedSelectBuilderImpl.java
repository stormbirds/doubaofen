package kotlinx.coroutines.experimental.selects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Deferred;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.channels.ReceiveChannel;
import kotlinx.coroutines.experimental.channels.SendChannel;
import kotlinx.coroutines.experimental.sync.Mutex;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0001\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u0013H\u0001J\n\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0001J>\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u001c\u0010\u001b\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u001cH\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u001dJD\u0010\u001e\u001a\u00020\t\"\u0004\b\u0001\u0010\u001f*\b\u0012\u0004\u0012\u0002H\u001f0 2\"\u0010\u001b\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u001f\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150!H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\"J2\u0010#\u001a\u00020\t*\u00020$2\u001c\u0010\u001b\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u001cH\u0016ø\u0001\u0000¢\u0006\u0002\u0010%J<\u0010&\u001a\u00020\t*\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u00152\u001c\u0010\u001b\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u001cH\u0016ø\u0001\u0000¢\u0006\u0002\u0010)JD\u0010*\u001a\u00020\t\"\u0004\b\u0001\u0010+*\b\u0012\u0004\u0012\u0002H+0,2\"\u0010\u001b\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H+\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150!H\u0016ø\u0001\u0000¢\u0006\u0002\u0010-JF\u0010.\u001a\u00020\t\"\u0004\b\u0001\u0010+*\b\u0012\u0004\u0012\u0002H+0,2$\u0010\u001b\u001a \b\u0001\u0012\u0006\u0012\u0004\u0018\u0001H+\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150!H\u0016ø\u0001\u0000¢\u0006\u0002\u0010-JF\u0010/\u001a\u00020\t\"\u0004\b\u0001\u0010+*\b\u0012\u0004\u0012\u0002H+002\u0006\u00101\u001a\u0002H+2\u001c\u0010\u001b\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u001cH\u0016ø\u0001\u0000¢\u0006\u0002\u00102R-\u0010\u0006\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0007j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b`\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u0002\u0004\n\u0002\b\t¨\u00063"}, d2 = {"Lkotlinx/coroutines/experimental/selects/UnbiasedSelectBuilderImpl;", "R", "Lkotlinx/coroutines/experimental/selects/SelectBuilder;", "cont", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlin/coroutines/experimental/Continuation;)V", "clauses", "Ljava/util/ArrayList;", "Lkotlin/Function0;", "", "Lkotlin/collections/ArrayList;", "getClauses", "()Ljava/util/ArrayList;", "instance", "Lkotlinx/coroutines/experimental/selects/SelectBuilderImpl;", "getInstance", "()Lkotlinx/coroutines/experimental/selects/SelectBuilderImpl;", "handleBuilderException", "e", "", "initSelectResult", "", "onTimeout", "time", "", "unit", "Ljava/util/concurrent/TimeUnit;", "block", "Lkotlin/Function1;", "(JLjava/util/concurrent/TimeUnit;Lkotlin/jvm/functions/Function1;)V", "onAwait", "T", "Lkotlinx/coroutines/experimental/Deferred;", "Lkotlin/Function2;", "(Lkotlinx/coroutines/experimental/Deferred;Lkotlin/jvm/functions/Function2;)V", "onJoin", "Lkotlinx/coroutines/experimental/Job;", "(Lkotlinx/coroutines/experimental/Job;Lkotlin/jvm/functions/Function1;)V", "onLock", "Lkotlinx/coroutines/experimental/sync/Mutex;", "owner", "(Lkotlinx/coroutines/experimental/sync/Mutex;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "onReceive", "E", "Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "(Lkotlinx/coroutines/experimental/channels/ReceiveChannel;Lkotlin/jvm/functions/Function2;)V", "onReceiveOrNull", "onSend", "Lkotlinx/coroutines/experimental/channels/SendChannel;", "element", "(Lkotlinx/coroutines/experimental/channels/SendChannel;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: SelectUnbiased.kt */
public final class UnbiasedSelectBuilderImpl<R> implements SelectBuilder<R> {
    private final ArrayList<Function0<Unit>> clauses = new ArrayList<>();
    private final SelectBuilderImpl<R> instance;

    public UnbiasedSelectBuilderImpl(Continuation<? super R> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "cont");
        this.instance = new SelectBuilderImpl<>(continuation);
    }

    public final SelectBuilderImpl<R> getInstance() {
        return this.instance;
    }

    public final ArrayList<Function0<Unit>> getClauses() {
        return this.clauses;
    }

    public final void handleBuilderException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "e");
        this.instance.handleBuilderException(th);
    }

    public final Object initSelectResult() {
        if (!this.instance.isSelected()) {
            try {
                Collections.shuffle(this.clauses);
                for (Function0 invoke : this.clauses) {
                    invoke.invoke();
                }
            } catch (Throwable th) {
                this.instance.handleBuilderException(th);
            }
        }
        return this.instance.initSelectResult();
    }

    public void onJoin(Job job, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(job, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onJoin$1(this, job, function1));
    }

    public <T> void onAwait(Deferred<? extends T> deferred, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(deferred, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onAwait$1(this, deferred, function2));
    }

    public <E> void onSend(SendChannel<? super E> sendChannel, E e, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(sendChannel, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onSend$1(this, sendChannel, e, function1));
    }

    public <E> void onReceive(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onReceive$1(this, receiveChannel, function2));
    }

    public <E> void onReceiveOrNull(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(receiveChannel, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onReceiveOrNull$1(this, receiveChannel, function2));
    }

    public void onLock(Mutex mutex, Object obj, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(mutex, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onLock$1(this, mutex, obj, function1));
    }

    public void onTimeout(long j, TimeUnit timeUnit, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        this.clauses.add(new UnbiasedSelectBuilderImpl$onTimeout$1(this, j, timeUnit, function1));
    }
}
