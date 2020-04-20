package cn.hiui.voice;

import android.support.constraint.ConstraintLayout;
import android.widget.AdapterView;
import android.widget.EditText;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\u0010\u0000\u001a\u00020\u00012\u0016\u0010\u0002\u001a\u0012\u0012\u0002\b\u0003 \u0004*\b\u0012\u0002\b\u0003\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\n¢\u0006\u0002\b\u000b"}, d2 = {"<anonymous>", "", "parent", "Landroid/widget/AdapterView;", "kotlin.jvm.PlatformType", "view", "Landroid/view/View;", "position", "", "id", "", "onItemClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
final class VoiceService$addView$9 implements AdapterView.OnItemClickListener {
    final /* synthetic */ EditText $saveKey;
    final /* synthetic */ ConstraintLayout $save_header;
    final /* synthetic */ VoiceService this$0;

    VoiceService$addView$9(VoiceService voiceService, EditText editText, ConstraintLayout constraintLayout) {
        this.this$0 = voiceService;
        this.$saveKey = editText;
        this.$save_header = constraintLayout;
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.widget.AdapterView<?>, java.lang.Object, android.widget.AdapterView] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onItemClick(android.widget.AdapterView<?> r2, android.view.View r3, int r4, long r5) {
        /*
            r1 = this;
            java.lang.String r3 = "parent"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r2, r3)
            android.widget.Adapter r2 = r2.getAdapter()
            java.lang.Object r2 = r2.getItem(r4)
            if (r2 == 0) goto L_0x00bc
            java.io.File r2 = (java.io.File) r2
            cn.hiui.voice.VoiceService$Companion r3 = cn.hiui.voice.VoiceService.Companion
            java.lang.String r3 = r3.getTAG()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            cn.hiui.voice.VoiceService r5 = r1.this$0
            int r5 = r5.selectIndex
            r4.append(r5)
            r5 = 32
            r4.append(r5)
            java.lang.String r5 = r2.getPath()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r3, r4)
            cn.hiui.voice.VoiceService r3 = r1.this$0
            int r3 = r3.selectIndex
            r4 = 0
            if (r3 == 0) goto L_0x0060
            r5 = 1
            if (r3 == r5) goto L_0x0045
            goto L_0x00bb
        L_0x0045:
            cn.hiui.voice.VoiceService r3 = r1.this$0
            r3.saveFile = r2
            android.widget.EditText r3 = r1.$saveKey
            java.lang.String r2 = r2.getName()
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2
            r3.setText(r2)
            android.support.constraint.ConstraintLayout r2 = r1.$save_header
            java.lang.String r3 = "save_header"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r2, r3)
            r2.setVisibility(r4)
            goto L_0x00bb
        L_0x0060:
            cn.hiui.voice.VoiceService r3 = r1.this$0
            byte[] r5 = kotlin.io.FilesKt.readBytes(r2)
            r3.hookByte = r5
            cn.hiui.voice.VoiceService r3 = r1.this$0
            r5 = 0
            java.io.File r3 = r3.getExternalFilesDir(r5)
            r5 = -12
            java.lang.String r5 = cn.hiui.voice.jni.Jni.getSbObj(r5)
            cn.hiui.voice.VoiceService r6 = r1.this$0
            java.io.File r0 = new java.io.File
            r0.<init>(r3, r5)
            r6.hookFile = r0
            cn.hiui.voice.VoiceService r3 = r1.this$0
            java.io.File r3 = r3.hookFile
            if (r3 == 0) goto L_0x0096
            cn.hiui.voice.VoiceService r5 = r1.this$0
            byte[] r5 = r5.hookByte
            if (r5 != 0) goto L_0x0093
            kotlin.jvm.internal.Intrinsics.throwNpe()
        L_0x0093:
            kotlin.io.FilesKt.writeBytes(r3, r5)
        L_0x0096:
            cn.hiui.voice.VoiceService r3 = r1.this$0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r2 = r2.getName()
            r5.append(r2)
            java.lang.String r2 = "已加载"
            r5.append(r2)
            java.lang.String r2 = r5.toString()
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2
            android.widget.Toast r2 = android.widget.Toast.makeText(r3, r2, r4)
            r2.show()
            java.lang.String r3 = "Toast\n        .makeText(…         show()\n        }"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r2, r3)
        L_0x00bb:
            return
        L_0x00bc:
            kotlin.TypeCastException r2 = new kotlin.TypeCastException
            java.lang.String r3 = "null cannot be cast to non-null type java.io.File"
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hiui.voice.VoiceService$addView$9.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
    }
}
