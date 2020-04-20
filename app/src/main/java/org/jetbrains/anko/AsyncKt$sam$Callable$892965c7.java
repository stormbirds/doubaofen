package org.jetbrains.anko;

import java.util.concurrent.Callable;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

@Metadata(bv = {1, 0, 1}, k = 3, mv = {1, 1, 5})
/* compiled from: Async.kt */
final class AsyncKt$sam$Callable$892965c7 implements Callable {
    private final /* synthetic */ Function0 function;

    AsyncKt$sam$Callable$892965c7(Function0 function0) {
        this.function = function0;
    }

    public final /* synthetic */ V call() {
        return this.function.invoke();
    }
}
