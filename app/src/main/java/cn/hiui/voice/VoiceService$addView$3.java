package cn.hiui.voice;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$3 implements View.OnClickListener {
    final /* synthetic */ TextView $search;
    final /* synthetic */ EditText $searchKey;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$3(VoiceService voiceService, TextView textView, EditText editText) {
        this.this$0 = voiceService;
        this.$search = textView;
        this.$searchKey = editText;
    }

    public final void onClick(View view) {
        String tag = VoiceService.Companion.getTAG();
        StringBuilder sb = new StringBuilder();
        sb.append("OnClick ");
        TextView textView = this.$search;
        Intrinsics.checkExpressionValueIsNotNull(textView, "search");
        sb.append(textView.getText());
        sb.append(' ');
        EditText editText = this.$searchKey;
        Intrinsics.checkExpressionValueIsNotNull(editText, "searchKey");
        sb.append(editText.getText());
        Log.d(tag, sb.toString());
        VoiceService voiceService = this.this$0;
        EditText editText2 = this.$searchKey;
        Intrinsics.checkExpressionValueIsNotNull(editText2, "searchKey");
        voiceService.loadFileList(editText2.getText().toString());
    }
}
