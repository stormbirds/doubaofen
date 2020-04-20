package kotlinx.coroutines.experimental;

import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutineContext;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\bf\u0018\u0000 \b2\u00020\u0001:\u0001\bJ\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/experimental/CoroutineExceptionHandler;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "handleException", "", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "exception", "", "Key", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
/* compiled from: CoroutineExceptionHandler.kt */
public interface CoroutineExceptionHandler extends CoroutineContext.Element {
    public static final Key Key = new Key((DefaultConstructorMarker) null);

    void handleException(CoroutineContext coroutineContext, Throwable th);

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lkotlinx/coroutines/experimental/CoroutineExceptionHandler$Key;", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "Lkotlinx/coroutines/experimental/CoroutineExceptionHandler;", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 6})
    /* compiled from: CoroutineExceptionHandler.kt */
    public static final class Key implements CoroutineContext.Key<CoroutineExceptionHandler> {
        private Key() {
        }

        public /* synthetic */ Key(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}
