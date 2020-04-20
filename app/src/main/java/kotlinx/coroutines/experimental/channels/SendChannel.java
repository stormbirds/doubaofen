package kotlinx.coroutines.experimental.channels;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.selects.SelectInstance;

@Metadata(bv = {1, 0, 1}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002J\u0014\u0010\u0007\u001a\u00020\u00042\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\tH&J\u0015\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00028\u0000H&¢\u0006\u0002\u0010\fJJ\u0010\r\u001a\u00020\u000e\"\u0004\b\u0001\u0010\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00112\u0006\u0010\u000b\u001a\u00028\u00002\u001c\u0010\u0012\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000f0\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0013H&ø\u0001\u0000¢\u0006\u0002\u0010\u0015J\u0019\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00028\u0000H¦@ø\u0001\u0000¢\u0006\u0002\u0010\u0017R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0005\u0002\u0004\n\u0002\b\t¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/experimental/channels/SendChannel;", "E", "", "isClosedForSend", "", "()Z", "isFull", "close", "cause", "", "offer", "element", "(Ljava/lang/Object;)Z", "registerSelectSend", "", "R", "select", "Lkotlinx/coroutines/experimental/selects/SelectInstance;", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlinx/coroutines/experimental/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "send", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: Channel.kt */
public interface SendChannel<E> {
    boolean close(Throwable th);

    boolean isClosedForSend();

    boolean isFull();

    boolean offer(E e);

    <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function1<? super Continuation<? super R>, ? extends Object> function1);

    Object send(E e, Continuation<? super Unit> continuation);

    @Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 6})
    /* compiled from: Channel.kt */
    public static final class DefaultImpls {
        public static /* bridge */ /* synthetic */ boolean close$default(SendChannel sendChannel, Throwable th, int i, Object obj) {
            if (obj == null) {
                if ((i & 1) != 0) {
                    th = null;
                }
                return sendChannel.close(th);
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: close");
        }
    }
}
