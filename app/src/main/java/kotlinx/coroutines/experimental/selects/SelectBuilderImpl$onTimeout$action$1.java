package kotlinx.coroutines.experimental.selects;

import kotlin.Metadata;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.jvm.functions.Function1;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002 \u0000H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "R", "run"}, k = 3, mv = {1, 1, 6})
/* compiled from: Select.kt */
final class SelectBuilderImpl$onTimeout$action$1 implements Runnable {
    final /* synthetic */ Function1 $block;
    final /* synthetic */ SelectBuilderImpl this$0;

    SelectBuilderImpl$onTimeout$action$1(SelectBuilderImpl selectBuilderImpl, Function1 function1) {
        this.this$0 = selectBuilderImpl;
        this.$block = function1;
    }

    public final void run() {
        if (this.this$0.trySelect((Object) null)) {
            CoroutinesKt.startCoroutine(this.$block, this.this$0.getCompletion());
        }
    }
}
