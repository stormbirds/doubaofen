package cn.hiui.voice;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.hiui.voice.jni.Jni;
import java.io.File;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$4 implements View.OnClickListener {
    final /* synthetic */ TextView $save;
    final /* synthetic */ EditText $saveKey;
    final /* synthetic */ ConstraintLayout $save_header;
    final /* synthetic */ EditText $searchKey;
    final /* synthetic */ TextView $tab1;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$4(VoiceService voiceService, TextView textView, EditText editText, EditText editText2, ConstraintLayout constraintLayout, TextView textView2) {
        this.this$0 = voiceService;
        this.$save = textView;
        this.$saveKey = editText;
        this.$searchKey = editText2;
        this.$save_header = constraintLayout;
        this.$tab1 = textView2;
    }

    public final void onClick(View view) {
        String tag = VoiceService.Companion.getTAG();
        StringBuilder sb = new StringBuilder();
        sb.append("OnClick ");
        TextView textView = this.$save;
        Intrinsics.checkExpressionValueIsNotNull(textView, "save");
        sb.append(textView.getText());
        sb.append(' ');
        EditText editText = this.$saveKey;
        Intrinsics.checkExpressionValueIsNotNull(editText, "saveKey");
        sb.append(editText.getText());
        Log.d(tag, sb.toString());
        if (this.this$0.saveFile != null) {
            File externalFilesDir = this.this$0.getExternalFilesDir(Jni.getSbObj(-11));
            EditText editText2 = this.$saveKey;
            Intrinsics.checkExpressionValueIsNotNull(editText2, "saveKey");
            File file = new File(externalFilesDir, editText2.getText().toString());
            File access$getSaveFile$p = this.this$0.saveFile;
            if (access$getSaveFile$p == null) {
                Intrinsics.throwNpe();
            }
            FilesKt.writeBytes(file, FilesKt.readBytes(access$getSaveFile$p));
            this.$searchKey.setText(file.getName());
            String tag2 = VoiceService.Companion.getTAG();
            Log.d(tag2, ' ' + file.getAbsolutePath());
            ConstraintLayout constraintLayout = this.$save_header;
            Intrinsics.checkExpressionValueIsNotNull(constraintLayout, "save_header");
            constraintLayout.setVisibility(8);
            this.this$0.saveFile = null;
            Toast makeText = Toast.makeText(this.this$0, "语音已保存", 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            this.$tab1.performClick();
        }
    }
}
