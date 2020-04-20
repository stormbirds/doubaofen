package kotlinx.coroutines.experimental.channels;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.Job;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003R\u0018\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/experimental/channels/ProducerJob;", "E", "Lkotlinx/coroutines/experimental/Job;", "Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "channel", "getChannel", "()Lkotlinx/coroutines/experimental/channels/ReceiveChannel;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Produce.kt */
public interface ProducerJob<E> extends Job, ReceiveChannel<E> {

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Produce.kt */
    public static final class DefaultImpls {
        @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
        public static <E> Job plus(ProducerJob<? extends E> producerJob, Job job) {
            Intrinsics.checkParameterIsNotNull(job, "other");
            return Job.DefaultImpls.plus(producerJob, job);
        }
    }

    ReceiveChannel<E> getChannel();
}
