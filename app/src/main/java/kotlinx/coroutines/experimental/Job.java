package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\bf\u0018\u0000 \u001d2\u00020\u0001:\u0002\u001d\u001eJ\u0014\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH&J\b\u0010\t\u001a\u00020\bH&J\"\u0010\n\u001a\u00020\u000b2\u0018\u0010\f\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0004\u0012\u00020\u000e0\rj\u0002`\u000fH&J\u0011\u0010\u0010\u001a\u00020\u000eH¦@ø\u0001\u0000¢\u0006\u0002\u0010\u0011J\u0011\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0000H\u0002JB\u0010\u0014\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00150\u00172\u001c\u0010\u0018\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u0019\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\rH&ø\u0001\u0000¢\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u0003H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004R\u0012\u0010\u0005\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0004\u0002\u0004\n\u0002\b\t¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/experimental/Job;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "isActive", "", "()Z", "isCompleted", "cancel", "cause", "", "getCompletionException", "invokeOnCompletion", "Lkotlinx/coroutines/experimental/DisposableHandle;", "handler", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/experimental/CompletionHandler;", "join", "(Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "plus", "other", "registerSelectJoin", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "start", "Key", "Registration", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Job.kt */
public interface Job extends CoroutineContext.Element {
    public static final Key Key = new Key((DefaultConstructorMarker) null);

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H'¨\u0006\u0004"}, d2 = {"Lkotlinx/coroutines/experimental/Job$Registration;", "", "unregister", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    @Deprecated(message = "Replace with `DisposableHandle`", replaceWith = @ReplaceWith(expression = "DisposableHandle", imports = {}))
    /* compiled from: Job.kt */
    public interface Registration {
        @Deprecated(message = "Replace with `dispose`", replaceWith = @ReplaceWith(expression = "dispose()", imports = {}))
        void unregister();
    }

    boolean cancel(Throwable th);

    Throwable getCompletionException();

    DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> function1);

    boolean isActive();

    boolean isCompleted();

    Object join(Continuation<? super Unit> continuation);

    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    Job plus(Job job);

    <R> void registerSelectJoin(SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1);

    boolean start();

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0015\u0010\u0004\u001a\u00020\u00022\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0002¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/experimental/Job$Key;", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "Lkotlinx/coroutines/experimental/Job;", "()V", "invoke", "parent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public static final class Key implements CoroutineContext.Key<Job> {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* bridge */ /* synthetic */ Job invoke$default(Key key, Job job, int i, Object obj) {
            if ((i & 1) != 0) {
                job = null;
            }
            return key.invoke(job);
        }

        public final Job invoke(Job job) {
            return new JobImpl(job);
        }
    }

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Job.kt */
    public static final class DefaultImpls {
        @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static Job plus(Job job, Job job2) {
            Intrinsics.checkParameterIsNotNull(job2, "other");
            return job2;
        }

        public static /* bridge */ /* synthetic */ boolean cancel$default(Job job, Throwable th, int i, Object obj) {
            if (obj == null) {
                if ((i & 1) != 0) {
                    th = null;
                }
                return job.cancel(th);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: cancel");
        }
    }
}
