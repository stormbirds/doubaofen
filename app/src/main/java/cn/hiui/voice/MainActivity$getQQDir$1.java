package cn.hiui.voice;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;
import cn.hiui.voice.jni.Jni;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0002\b\u0006"}, d2 = {"<anonymous>", "", "dialogInterface", "Landroid/content/DialogInterface;", "i", "", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: MainActivity.kt */
final class MainActivity$getQQDir$1 extends Lambda implements Function2<DialogInterface, Integer, Unit> {
    final /* synthetic */ List $list;
    final /* synthetic */ MainActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MainActivity$getQQDir$1(MainActivity mainActivity, List list) {
        super(2);
        this.this$0 = mainActivity;
        this.$list = list;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        invoke((DialogInterface) obj, ((Number) obj2).intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(DialogInterface dialogInterface, int i) {
        Intrinsics.checkParameterIsNotNull(dialogInterface, "dialogInterface");
        MainActivity mainActivity = this.this$0;
        Toast makeText = Toast.makeText(mainActivity, "已绑定" + ((String) this.$list.get(i)), 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        SharedPreferences sharedPreferences = this.this$0.getSharedPreferences(Jni.getSbObj(-2), 0);
        sharedPreferences.edit().putString(Jni.getSbObj(-3), (String) this.$list.get(i)).commit();
    }
}
