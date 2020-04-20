package kotlinx.coroutines.experimental;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.coroutines.experimental.EmptyCoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aI\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052'\u0010\u0006\u001a#\b\u0001\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0007¢\u0006\u0002\b\fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\r\u001aI\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u000e2'\u0010\u0006\u001a#\b\u0001\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0007¢\u0006\u0002\b\fø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a=\u0010\u0010\u001a\u0002H\u0011\"\u0004\b\u0000\u0010\u00112\u0006\u0010\u0002\u001a\u00020\u00032\u001c\u0010\u0006\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0012H@ø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aI\u0010\u0014\u001a\u0002H\u0011\"\u0004\b\u0000\u0010\u00112\b\b\u0002\u0010\u0002\u001a\u00020\u00032'\u0010\u0006\u001a#\b\u0001\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\t\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u0007¢\u0006\u0002\b\fH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u0002\u0004\n\u0002\b\t¨\u0006\u0016"}, d2 = {"launch", "Lkotlinx/coroutines/experimental/Job;", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "start", "", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/experimental/CoroutineScope;", "Lkotlin/coroutines/experimental/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/Job;", "Lkotlinx/coroutines/experimental/CoroutineStart;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlinx/coroutines/experimental/CoroutineStart;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/Job;", "run", "T", "Lkotlin/Function1;", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "runBlocking", "(Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Builders.kt */
public final class BuildersKt {
    public static /* bridge */ /* synthetic */ Job launch$default(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineStart = CoroutineStart.DEFAULT;
        }
        return launch(coroutineContext, coroutineStart, (Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object>) function2);
    }

    public static final Job launch(CoroutineContext coroutineContext, CoroutineStart coroutineStart, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        StandaloneCoroutine standaloneCoroutine;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        CoroutineContext newCoroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext);
        if (coroutineStart.isLazy()) {
            standaloneCoroutine = new LazyStandaloneCoroutine(newCoroutineContext, function2);
        } else {
            standaloneCoroutine = new StandaloneCoroutine(newCoroutineContext, true);
        }
        standaloneCoroutine.initParentJob((Job) coroutineContext.get(Job.Key));
        coroutineStart.invoke(function2, standaloneCoroutine, standaloneCoroutine);
        return standaloneCoroutine;
    }

    @Deprecated(message = "Use `start = CoroutineStart.XXX` parameter", replaceWith = @ReplaceWith(expression = "launch(context, if (start) CoroutineStart.DEFAULT else CoroutineStart.LAZY, block)", imports = {}))
    public static final Job launch(CoroutineContext coroutineContext, boolean z, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return launch(coroutineContext, z ? CoroutineStart.DEFAULT : CoroutineStart.LAZY, function2);
    }

    public static final <T> Object run(CoroutineContext coroutineContext, Function1<? super Continuation<? super T>, ? extends Object> function1, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
        Continuation<? super T> normalizeContinuation = CoroutineIntrinsics.normalizeContinuation(continuation);
        CoroutineContext context = normalizeContinuation.getContext();
        if (coroutineContext == context || ((coroutineContext instanceof CoroutineContext.Element) && context.get(((CoroutineContext.Element) coroutineContext).getKey()) == coroutineContext)) {
            return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(normalizeContinuation);
        }
        CoroutineContext plus = context.plus(coroutineContext);
        if (plus == context) {
            return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(normalizeContinuation);
        }
        if (Intrinsics.areEqual((Object) (ContinuationInterceptor) plus.get(ContinuationInterceptor.Key), (Object) (ContinuationInterceptor) context.get(ContinuationInterceptor.Key))) {
            return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function1, 1)).invoke(new RunContinuationDirect(plus, normalizeContinuation));
        }
        RunContinuationCoroutine runContinuationCoroutine = new RunContinuationCoroutine(plus, normalizeContinuation);
        runContinuationCoroutine.initCancellability();
        CoroutinesKt.startCoroutine(function1, runContinuationCoroutine);
        return runContinuationCoroutine.getResult();
    }

    public static /* bridge */ /* synthetic */ Object runBlocking$default(CoroutineContext coroutineContext, Function2 function2, int i, Object obj) throws InterruptedException {
        if ((i & 1) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        return runBlocking(coroutineContext, function2);
    }

    public static final <T> T runBlocking(CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) throws InterruptedException {
        EventLoopImpl eventLoopImpl;
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Thread currentThread = Thread.currentThread();
        if (coroutineContext.get(ContinuationInterceptor.Key) == null) {
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
            eventLoopImpl = new EventLoopImpl(currentThread);
        } else {
            eventLoopImpl = null;
        }
        CoroutineContext newCoroutineContext = CoroutineContextKt.newCoroutineContext(coroutineContext.plus(eventLoopImpl != null ? eventLoopImpl : EmptyCoroutineContext.INSTANCE));
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        BlockingCoroutine blockingCoroutine = new BlockingCoroutine(newCoroutineContext, currentThread, eventLoopImpl != null);
        blockingCoroutine.initParentJob((Job) coroutineContext.get(Job.Key));
        if (eventLoopImpl != null) {
            eventLoopImpl.initParentJob(blockingCoroutine);
        }
        CoroutinesKt.startCoroutine(function2, blockingCoroutine, blockingCoroutine);
        return blockingCoroutine.joinBlocking();
    }
}
