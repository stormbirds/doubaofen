package cn.hiui.voice;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.anko.Sdk25PropertiesKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$8 implements View.OnClickListener {
    final /* synthetic */ ConstraintLayout $save_header;
    final /* synthetic */ ConstraintLayout $search_header;
    final /* synthetic */ TextView $tab1;
    final /* synthetic */ TextView $tab2;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$8(VoiceService voiceService, ConstraintLayout constraintLayout, ConstraintLayout constraintLayout2, TextView textView, TextView textView2) {
        this.this$0 = voiceService;
        this.$search_header = constraintLayout;
        this.$save_header = constraintLayout2;
        this.$tab2 = textView;
        this.$tab1 = textView2;
    }

    public final void onClick(View view) {
        this.this$0.selectIndex = 1;
        ConstraintLayout constraintLayout = this.$search_header;
        Intrinsics.checkExpressionValueIsNotNull(constraintLayout, "search_header");
        constraintLayout.setVisibility(8);
        ConstraintLayout constraintLayout2 = this.$save_header;
        Intrinsics.checkExpressionValueIsNotNull(constraintLayout2, "save_header");
        constraintLayout2.setVisibility(8);
        TextView textView = this.$tab2;
        Intrinsics.checkExpressionValueIsNotNull(textView, "tab2");
        Sdk25PropertiesKt.setTextColor(textView, -1);
        TextView textView2 = this.$tab1;
        Intrinsics.checkExpressionValueIsNotNull(textView2, "tab1");
        Sdk25PropertiesKt.setTextColor(textView2, -3355444);
        VoiceService.access$getList$p(this.this$0).setAdapter(this.this$0.adapter2);
    }
}
