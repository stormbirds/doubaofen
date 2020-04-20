package kotlinx.coroutines.experimental.channels;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineContextKt;
import kotlinx.coroutines.experimental.Job;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a]\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062-\u0010\u0007\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\b¢\u0006\u0002\b\rH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000e\u001a[\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062-\u0010\u0007\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\b¢\u0006\u0002\b\rø\u0001\u0000¢\u0006\u0002\u0010\u000e*J\b\u0007\u0010\u0010\u001a\u0004\b\u0000\u0010\u0002\"\b\u0012\u0004\u0012\u0002H\u00020\t2\b\u0012\u0004\u0012\u0002H\u00020\tB*\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u001c\b\u0014\u0012\u0018\b\u000bB\u0014\b\u0015\u0012\u0006\b\u0016\u0012\u0002\b\f\u0012\b\b\u0017\u0012\u0004\b\b(\u0018*J\b\u0007\u0010\u0019\u001a\u0004\b\u0000\u0010\u0002\"\b\u0012\u0004\u0012\u0002H\u00020\u00012\b\u0012\u0004\u0012\u0002H\u00020\u0001B*\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u001a\u0012\u001c\b\u0014\u0012\u0018\b\u000bB\u0014\b\u0015\u0012\u0006\b\u0016\u0012\u0002\b\f\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0002\u0004\n\u0002\b\t¨\u0006\u001c"}, d2 = {"buildChannel", "Lkotlinx/coroutines/experimental/channels/ProducerJob;", "E", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "capacity", "", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/experimental/channels/ProducerScope;", "Lkotlin/coroutines/experimental/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/experimental/CoroutineContext;ILkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/experimental/channels/ProducerJob;", "produce", "ChannelBuilder", "Lkotlin/Deprecated;", "message", "Renamed to `ProducerScope`", "replaceWith", "Lkotlin/ReplaceWith;", "imports", "expression", "ProducerScope", "ChannelJob", "Renamed to `ProducerJob`", "ProducerJob", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 6})
/* compiled from: Produce.kt */
public final class ProduceKt {
    @Deprecated(message = "Renamed to `ProducerScope`", replaceWith = @ReplaceWith(expression = "ProducerScope", imports = {}))
    public static /* synthetic */ void ChannelBuilder$annotations() {
    }

    @Deprecated(message = "Renamed to `ProducerJob`", replaceWith = @ReplaceWith(expression = "ProducerJob", imports = {}))
    public static /* synthetic */ void ChannelJob$annotations() {
    }

    public static /* bridge */ /* synthetic */ ProducerJob produce$default(CoroutineContext coroutineContext, int i, Function2 function2, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return produce(coroutineContext, i, function2);
    }

    public static final <E> ProducerJob<E> produce(CoroutineContext coroutineContext, int i, Function2<? super ProducerScope<? super E>, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        ProducerCoroutine producerCoroutine = new ProducerCoroutine(CoroutineContextKt.newCoroutineContext(coroutineContext), Channel.Factory.invoke(i));
        producerCoroutine.initParentJob((Job) coroutineContext.get(Job.Key));
        CoroutinesKt.startCoroutine(function2, producerCoroutine, producerCoroutine);
        return producerCoroutine;
    }

    @Deprecated(message = "Renamed to `produce`", replaceWith = @ReplaceWith(expression = "produce(context, capacity, block)", imports = {}))
    public static /* bridge */ /* synthetic */ ProducerJob buildChannel$default(CoroutineContext coroutineContext, int i, Function2 function2, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return buildChannel(coroutineContext, i, function2);
    }

    @Deprecated(message = "Renamed to `produce`", replaceWith = @ReplaceWith(expression = "produce(context, capacity, block)", imports = {}))
    public static final <E> ProducerJob<E> buildChannel(CoroutineContext coroutineContext, int i, Function2<? super ProducerScope<? super E>, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        return produce(coroutineContext, i, function2);
    }
}
