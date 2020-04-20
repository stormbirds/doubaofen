package cn.hiui.voice;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$1 implements View.OnClickListener {
    final /* synthetic */ WindowManager.LayoutParams $layoutParams;
    final /* synthetic */ TextView $sTitle;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$1(VoiceService voiceService, TextView textView, WindowManager.LayoutParams layoutParams) {
        this.this$0 = voiceService;
        this.$sTitle = textView;
        this.$layoutParams = layoutParams;
    }

    public final void onClick(View view) {
        if (!Intrinsics.areEqual((Object) this.$sTitle.getText(), (Object) "优")) {
            this.$sTitle.setText("优");
            WindowManager.LayoutParams layoutParams = this.$layoutParams;
            layoutParams.x -= 250;
            WindowManager.LayoutParams layoutParams2 = this.$layoutParams;
            layoutParams2.y -= 250;
            WindowManager.LayoutParams layoutParams3 = this.$layoutParams;
            layoutParams3.width = 100;
            layoutParams3.height = 100;
            layoutParams3.flags = 40;
            VoiceService.access$getWindowManager$p(this.this$0).updateViewLayout(VoiceService.access$getSFloat$p(this.this$0), this.$layoutParams);
            return;
        }
        this.$sTitle.setText("优音助手");
        this.$layoutParams.x += ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        this.$layoutParams.y += ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        WindowManager.LayoutParams layoutParams4 = this.$layoutParams;
        layoutParams4.width = 600;
        layoutParams4.height = 600;
        layoutParams4.flags = 32;
        VoiceService.access$getWindowManager$p(this.this$0).updateViewLayout(VoiceService.access$getSFloat$p(this.this$0), this.$layoutParams);
    }
}
