package cn.hiui.voice.file;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 15})
/* compiled from: PathListener.kt */
final class PathListener$startWatching$1 implements Runnable {
    final /* synthetic */ PathListener this$0;

    PathListener$startWatching$1(PathListener pathListener) {
        this.this$0 = pathListener;
    }

    public final void run() {
        PathListener pathListener = this.this$0;
        pathListener.addPaths(pathListener.getMPath());
    }
}
