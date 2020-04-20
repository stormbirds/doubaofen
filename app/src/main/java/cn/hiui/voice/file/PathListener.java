package cn.hiui.voice.file;

import android.os.FileObserver;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Stack;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\b\u0018\u0000 *2\u00020\u0001:\u0002*+B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010!\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020$J\u001a\u0010%\u001a\u00020 2\u0006\u0010&\u001a\u00020\u00072\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u000e\u0010'\u001a\u00020 2\u0006\u0010\u0004\u001a\u00020\u0005J\b\u0010(\u001a\u00020 H\u0016J\b\u0010)\u001a\u00020 H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR6\u0010\u000e\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u000fj\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0001`\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R6\u0010\u0018\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00070\u000fj\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0007`\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0012\"\u0004\b\u001a\u0010\u0014R\u001a\u0010\u001b\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0017\"\u0004\b\u001d\u0010\u001e¨\u0006,"}, d2 = {"Lcn/hiui/voice/file/PathListener;", "Landroid/os/FileObserver;", "listener", "Lcn/hiui/voice/file/PathListener$Companion$IMessageEvent;", "path", "", "mask", "", "(Lcn/hiui/voice/file/PathListener$Companion$IMessageEvent;Ljava/lang/String;I)V", "getListener", "()Lcn/hiui/voice/file/PathListener$Companion$IMessageEvent;", "mMask", "getMMask", "()I", "mObserversMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getMObserversMap", "()Ljava/util/HashMap;", "setMObserversMap", "(Ljava/util/HashMap;)V", "mPath", "getMPath", "()Ljava/lang/String;", "modifyCountMap", "getModifyCountMap", "setModifyCountMap", "sendingFile", "getSendingFile", "setSendingFile", "(Ljava/lang/String;)V", "addPath", "", "addPaths", "clearSendingFile", "dely", "", "onEvent", "event", "removePath", "startWatching", "stopWatching", "Companion", "SingleFileObserver", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: PathListener.kt */
public final class PathListener extends FileObserver {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static boolean SingleFileObserverDebug;
    /* access modifiers changed from: private */
    public static final String TAG = PathListener.class.getSimpleName();
    private final Companion.IMessageEvent listener;
    private final int mMask;
    private HashMap<String, FileObserver> mObserversMap;
    private final String mPath;
    private HashMap<String, Integer> modifyCountMap;
    private String sendingFile;

    public final void clearSendingFile(long j) {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PathListener(Companion.IMessageEvent iMessageEvent, String str, int i) {
        super(str, i);
        Intrinsics.checkParameterIsNotNull(iMessageEvent, "listener");
        Intrinsics.checkParameterIsNotNull(str, "path");
        this.listener = iMessageEvent;
        this.mPath = str;
        this.mMask = i;
        this.sendingFile = "";
        this.mObserversMap = new HashMap<>();
        this.modifyCountMap = new HashMap<>();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ PathListener(Companion.IMessageEvent iMessageEvent, String str, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(iMessageEvent, str, (i2 & 4) != 0 ? 4095 : i);
    }

    public final Companion.IMessageEvent getListener() {
        return this.listener;
    }

    public final String getMPath() {
        return this.mPath;
    }

    public final int getMMask() {
        return this.mMask;
    }

    public final String getSendingFile() {
        return this.sendingFile;
    }

    public final void setSendingFile(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.sendingFile = str;
    }

    public final HashMap<String, FileObserver> getMObserversMap() {
        return this.mObserversMap;
    }

    public final void setMObserversMap(HashMap<String, FileObserver> hashMap) {
        Intrinsics.checkParameterIsNotNull(hashMap, "<set-?>");
        this.mObserversMap = hashMap;
    }

    public final HashMap<String, Integer> getModifyCountMap() {
        return this.modifyCountMap;
    }

    public final void setModifyCountMap(HashMap<String, Integer> hashMap) {
        Intrinsics.checkParameterIsNotNull(hashMap, "<set-?>");
        this.modifyCountMap = hashMap;
    }

    public void startWatching() {
        Log.d(TAG, "startWatching");
        new Thread(new PathListener$startWatching$1(this)).start();
    }

    public final void addPath(String str) {
        Intrinsics.checkParameterIsNotNull(str, "path");
        if (!this.mObserversMap.containsKey(str)) {
            SingleFileObserver singleFileObserver = new SingleFileObserver(this, str, this.mMask);
            singleFileObserver.startWatching();
            this.mObserversMap.put(str, singleFileObserver);
        }
    }

    public final void removePath(String str) {
        Intrinsics.checkParameterIsNotNull(str, "path");
        String str2 = TAG;
        Log.d(str2, "removePath: " + str);
        FileObserver fileObserver = this.mObserversMap.get(str);
        if (fileObserver != null) {
            fileObserver.stopWatching();
        }
        this.mObserversMap.remove(str);
    }

    public final void addPaths(String str) {
        Intrinsics.checkParameterIsNotNull(str, "path");
        Log.d(TAG, "addPaths: " + str);
        Stack stack = new Stack();
        stack.push(str);
        while (!stack.isEmpty()) {
            String str2 = (String) stack.pop();
            File file = new File(str2);
            if (file.exists() && file.isDirectory() && !this.mObserversMap.containsKey(str2)) {
                Intrinsics.checkExpressionValueIsNotNull(str2, "parent");
                SingleFileObserver singleFileObserver = new SingleFileObserver(this, str2, this.mMask);
                singleFileObserver.startWatching();
                this.mObserversMap.put(str2, singleFileObserver);
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        if (!file2.isDirectory() || file2.getName().equals(".") || file2.getName().equals("..")) {
                            Intrinsics.checkExpressionValueIsNotNull(file2, "f");
                            String name = file2.getName();
                            Intrinsics.checkExpressionValueIsNotNull(name, "f.name");
                            StringsKt.endsWith(name, "amr", true);
                        } else {
                            Intrinsics.checkExpressionValueIsNotNull(file2, "f");
                            stack.push(file2.getPath());
                        }
                    }
                }
            }
        }
    }

    public void stopWatching() {
        Log.d(TAG, "stopWatching");
        for (FileObserver stopWatching : this.mObserversMap.values()) {
            stopWatching.stopWatching();
        }
    }

    public void onEvent(int i, String str) {
        int i2 = i;
        String str2 = str;
        if (str2 == null || StringsKt.endsWith(str2, "log", true) || StringsKt.endsWith(str2, "/null", true)) {
            return;
        }
        if (i2 == 1) {
            String str3 = TAG;
            Log.d(str3, "ACCESS:" + str2);
            if (!Intrinsics.areEqual((Object) this.sendingFile, (Object) str2)) {
                if (StringsKt.startsWith(str2, "/sdcard/tencent/MicroMsg/", true) && StringsKt.endsWith(str2, ".amr", true)) {
                    File file = new File(str2);
                    if (System.currentTimeMillis() - file.lastModified() > ((long) 1000) && file.lastModified() != 0) {
                        this.listener.onWXPlay(str2);
                    }
                    clearSendingFile(1000);
                }
                if (StringsKt.startsWith(str2, "/sdcard/tencent/MobileQQ/", true) && StringsKt.endsWith(str2, ".slk", true)) {
                    File file2 = new File(str2);
                    if (System.currentTimeMillis() - file2.lastModified() > ((long) 1000) && file2.lastModified() != 0) {
                        this.listener.onQQPlay(str2);
                    }
                    clearSendingFile(1000);
                }
                if (StringsKt.startsWith(str2, "/sdcard/tencent/MobileQQ/", true) && StringsKt.endsWith(str2, ".amr", true)) {
                    File file3 = new File(str2);
                    if (System.currentTimeMillis() - file3.lastModified() > ((long) 1000) && file3.lastModified() != 0) {
                        this.listener.onQQPlay(str2);
                    }
                    clearSendingFile(1000);
                }
            }
        } else if (i2 != 2) {
            switch (i2) {
                case 4:
                    String str4 = TAG;
                    Log.d(str4, "ATTRIB:" + str2);
                    return;
                case 8:
                    String str5 = TAG;
                    Log.d(str5, "CLOSE_WRITE:" + str2);
                    if (StringsKt.startsWith(str2, "/sdcard/tencent/MicroMsg/", true) && StringsKt.endsWith(str2, ".amr", true)) {
                        Integer num = this.modifyCountMap.get(str2);
                        if (num != null && num.intValue() > 1) {
                            this.sendingFile = str2;
                        }
                        this.modifyCountMap.remove(str2);
                        clearSendingFile(1000);
                        return;
                    }
                    return;
                case 16:
                    String str6 = TAG;
                    Log.d(str6, "CLOSE_NOWRITE:" + str2);
                    return;
                case 32:
                    String str7 = TAG;
                    Log.d(str7, "OPEN:" + str2);
                    return;
                case 64:
                    String str8 = TAG;
                    Log.d(str8, "MOVED_FROM:" + str2);
                    return;
                case 128:
                    String str9 = TAG;
                    Log.d(str9, "MOVED_TO:" + str2);
                    if (StringsKt.startsWith(str2, "/sdcard/tencent/MobileQQ/", true) && StringsKt.endsWith(str2, ".slk", true) && StringsKt.indexOf$default((CharSequence) str2, "stream_", 0, false, 6, (Object) null) > 0) {
                        this.listener.onQQSend(str2);
                        this.sendingFile = str2;
                        clearSendingFile(1000);
                        return;
                    }
                    return;
                case 256:
                    String str10 = TAG;
                    Log.d(str10, "CREATE:" + str2);
                    return;
                case 512:
                    String str11 = TAG;
                    Log.d(str11, "DELETE:" + str2);
                    return;
                case 1024:
                    String str12 = TAG;
                    Log.d(str12, "DELETE_SELF:" + str2);
                    return;
                case 2048:
                    String str13 = TAG;
                    Log.d(str13, "MOVE_SELF:" + str2);
                    return;
                case 32768:
                    String str14 = TAG;
                    Log.d(str14, "OPEN: " + str2);
                    return;
                case 1073741888:
                    String str15 = TAG;
                    Log.d(str15, "MOVE_FROM: " + str2);
                    return;
                case 1073741952:
                    String str16 = TAG;
                    Log.d(str16, "MOVE_TO: " + str2);
                    return;
                case 1073742080:
                    String str17 = TAG;
                    Log.d(str17, "Create: " + str2);
                    File file4 = new File(str2);
                    if (file4.exists() && file4.isDirectory()) {
                        addPaths(str2);
                        return;
                    }
                    return;
                case 1073742336:
                    String str18 = TAG;
                    Log.d(str18, "Delete: " + str2);
                    new File(str2);
                    removePath(str2);
                    return;
                default:
                    return;
            }
        } else {
            String str19 = TAG;
            Log.d(str19, "MODIFY:" + str2);
            if (StringsKt.startsWith(str2, "/sdcard/tencent/MicroMsg/", true) && StringsKt.endsWith(str2, ".amr", true)) {
                Integer num2 = this.modifyCountMap.get(str2);
                if (num2 == null) {
                    num2 = 0;
                }
                this.modifyCountMap.put(str2, Integer.valueOf(num2.intValue() + 1));
                if (num2.intValue() == 5) {
                    Integer num3 = this.modifyCountMap.get(str2);
                    if (num3 != null && num3.intValue() > 1) {
                        this.listener.onWxSend(str2);
                    }
                    this.sendingFile = str2;
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001a\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00072\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010\u0011\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u000fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Lcn/hiui/voice/file/PathListener$SingleFileObserver;", "Landroid/os/FileObserver;", "handler", "Lcn/hiui/voice/file/PathListener;", "path", "", "mask", "", "(Lcn/hiui/voice/file/PathListener;Ljava/lang/String;I)V", "getHandler", "()Lcn/hiui/voice/file/PathListener;", "mPath", "getMPath", "()Ljava/lang/String;", "onEvent", "", "event", "startWatching", "stopWatching", "voice_release"}, k = 1, mv = {1, 1, 15})
    /* compiled from: PathListener.kt */
    public static final class SingleFileObserver extends FileObserver {
        private final PathListener handler;
        private final String mPath;

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ SingleFileObserver(PathListener pathListener, String str, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(pathListener, str, (i2 & 4) != 0 ? 4095 : i);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public SingleFileObserver(PathListener pathListener, String str, int i) {
            super(str, i);
            Intrinsics.checkParameterIsNotNull(pathListener, "handler");
            Intrinsics.checkParameterIsNotNull(str, "path");
            this.mPath = str;
            this.handler = pathListener;
        }

        public final String getMPath() {
            return this.mPath;
        }

        public final PathListener getHandler() {
            return this.handler;
        }

        public void startWatching() {
            super.startWatching();
            if (PathListener.Companion.getSingleFileObserverDebug()) {
                String tag = PathListener.Companion.getTAG();
                Log.d(tag, "startWatching " + this.mPath);
            }
        }

        public void stopWatching() {
            super.stopWatching();
            if (PathListener.Companion.getSingleFileObserverDebug()) {
                String tag = PathListener.Companion.getTAG();
                Log.d(tag, "stopWatching " + this.mPath);
            }
        }

        public void onEvent(int i, String str) {
            this.handler.onEvent(i, this.mPath + "/" + str);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001:\u0001\u000eB\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0019\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\n¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000f"}, d2 = {"Lcn/hiui/voice/file/PathListener$Companion;", "", "()V", "SingleFileObserverDebug", "", "getSingleFileObserverDebug", "()Z", "setSingleFileObserverDebug", "(Z)V", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "IMessageEvent", "voice_release"}, k = 1, mv = {1, 1, 15})
    /* compiled from: PathListener.kt */
    public static final class Companion {

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\t"}, d2 = {"Lcn/hiui/voice/file/PathListener$Companion$IMessageEvent;", "", "onQQPlay", "", "file", "", "onQQSend", "onWXPlay", "onWxSend", "voice_release"}, k = 1, mv = {1, 1, 15})
        /* compiled from: PathListener.kt */
        public interface IMessageEvent {
            void onQQPlay(String str);

            void onQQSend(String str);

            void onWXPlay(String str);

            void onWxSend(String str);
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean getSingleFileObserverDebug() {
            return PathListener.SingleFileObserverDebug;
        }

        public final void setSingleFileObserverDebug(boolean z) {
            PathListener.SingleFileObserverDebug = z;
        }

        public final String getTAG() {
            return PathListener.TAG;
        }
    }
}
