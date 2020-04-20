package cn.hiui.voice;

import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "v", "Landroid/view/View;", "kotlin.jvm.PlatformType", "event", "Landroid/view/MotionEvent;", "onTouch"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$2 implements View.OnTouchListener {
    final /* synthetic */ WindowManager.LayoutParams $layoutParams;
    final /* synthetic */ Ref.IntRef $movedX;
    final /* synthetic */ Ref.IntRef $movedY;
    final /* synthetic */ Ref.IntRef $x;
    final /* synthetic */ Ref.IntRef $y;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$2(VoiceService voiceService, Ref.IntRef intRef, Ref.IntRef intRef2, Ref.IntRef intRef3, Ref.IntRef intRef4, WindowManager.LayoutParams layoutParams) {
        this.this$0 = voiceService;
        this.$x = intRef;
        this.$y = intRef2;
        this.$movedX = intRef3;
        this.$movedY = intRef4;
        this.$layoutParams = layoutParams;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        Intrinsics.checkExpressionValueIsNotNull(motionEvent, NotificationCompat.CATEGORY_EVENT);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.$x.element = (int) motionEvent.getRawX();
            this.$y.element = (int) motionEvent.getRawY();
            this.$movedX.element = 0;
            this.$movedY.element = 0;
        } else if (action != 1) {
            if (action == 2) {
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                int i = rawX - this.$x.element;
                int i2 = rawY - this.$y.element;
                this.$movedX.element += i;
                this.$movedY.element += i2;
                this.$x.element = rawX;
                this.$y.element = rawY;
                this.$layoutParams.x += i;
                this.$layoutParams.y += i2;
                VoiceService.access$getWindowManager$p(this.this$0).updateViewLayout(VoiceService.access$getSFloat$p(this.this$0), this.$layoutParams);
                if (Math.abs(this.$movedX.element) > 3 || Math.abs(this.$movedX.element) > 3) {
                    return true;
                }
            }
        } else if (Math.abs(this.$movedX.element) > 3 || Math.abs(this.$movedX.element) > 3) {
            return true;
        }
        return false;
    }
}
