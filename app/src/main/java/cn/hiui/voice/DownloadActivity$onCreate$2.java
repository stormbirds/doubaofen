package cn.hiui.voice;

import android.media.MediaPlayer;
import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\n¢\u0006\u0002\b\b"}, d2 = {"<anonymous>", "", "mp", "Landroid/media/MediaPlayer;", "kotlin.jvm.PlatformType", "what", "", "extra", "onError"}, k = 3, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
final class DownloadActivity$onCreate$2 implements MediaPlayer.OnErrorListener {
    final /* synthetic */ DownloadActivity this$0;

    DownloadActivity$onCreate$2(DownloadActivity downloadActivity) {
        this.this$0 = downloadActivity;
    }

    public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Toast makeText = Toast.makeText(this.this$0, "播放失败", 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        makeText.show();
        return true;
    }
}
