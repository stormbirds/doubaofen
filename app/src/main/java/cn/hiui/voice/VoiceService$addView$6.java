package cn.hiui.voice;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import cn.hiui.voice.VoiceService;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$6 implements View.OnClickListener {
    final /* synthetic */ ConstraintLayout $save_header;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$6(VoiceService voiceService, ConstraintLayout constraintLayout) {
        this.this$0 = voiceService;
        this.$save_header = constraintLayout;
    }

    public final void onClick(View view) {
        this.this$0.recentFileList.clear();
        VoiceService.Companion.DataAdapter access$getAdapter2$p = this.this$0.adapter2;
        if (access$getAdapter2$p != null) {
            access$getAdapter2$p.notifyDataSetChanged();
        }
        this.this$0.saveFile = null;
        ConstraintLayout constraintLayout = this.$save_header;
        Intrinsics.checkExpressionValueIsNotNull(constraintLayout, "save_header");
        constraintLayout.setVisibility(8);
    }
}
