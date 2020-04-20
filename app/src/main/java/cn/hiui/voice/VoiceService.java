package cn.hiui.voice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.hiui.voice.file.PathListener;
import cn.hiui.voice.jni.Jni;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.StringCompanionObject;
import org.jetbrains.anko.AsyncKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 *2\u00020\u00012\u00020\u0002:\u0001*B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\u000e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010 \u001a\u00020!H\u0016J\b\u0010\"\u001a\u00020\u001aH\u0016J\b\u0010#\u001a\u00020\u001aH\u0016J\u0010\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020\u001dH\u0016J\u0010\u0010&\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020\u001dH\u0016J\u0010\u0010'\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020\u001dH\u0016J\u0010\u0010(\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020\u001dH\u0016J\u0006\u0010)\u001a\u00020\u001aR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u0012\u0012\u0004\u0012\u00020\n0\u0010j\b\u0012\u0004\u0012\u00020\n`\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X.¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lcn/hiui/voice/VoiceService;", "Lcn/hiui/voice/file/PathListener$Companion$IMessageEvent;", "Landroid/app/Service;", "()V", "adapter1", "Lcn/hiui/voice/VoiceService$Companion$DataAdapter;", "adapter2", "hookByte", "", "hookFile", "Ljava/io/File;", "list", "Landroid/widget/ListView;", "listener", "Lcn/hiui/voice/file/PathListener;", "recentFileList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "sFloat", "Landroid/widget/LinearLayout;", "saveFile", "selectIndex", "", "windowManager", "Landroid/view/WindowManager;", "addView", "", "loadFileList", "key", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onQQPlay", "filestr", "onQQSend", "onWXPlay", "onWxSend", "removeView", "Companion", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: VoiceService.kt */
public final class VoiceService extends Service implements PathListener.Companion.IMessageEvent {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final String TAG = VoiceService.class.getSimpleName();
    private Companion.DataAdapter adapter1;
    /* access modifiers changed from: private */
    public Companion.DataAdapter adapter2;
    /* access modifiers changed from: private */
    public byte[] hookByte;
    /* access modifiers changed from: private */
    public File hookFile;
    /* access modifiers changed from: private */
    public ListView list;
    private PathListener listener;
    /* access modifiers changed from: private */
    public final ArrayList<File> recentFileList = new ArrayList<>();
    /* access modifiers changed from: private */
    public LinearLayout sFloat;
    /* access modifiers changed from: private */
    public File saveFile;
    /* access modifiers changed from: private */
    public int selectIndex;
    /* access modifiers changed from: private */
    public WindowManager windowManager;

    public IBinder onBind(Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        return null;
    }

    public static final /* synthetic */ ListView access$getList$p(VoiceService voiceService) {
        ListView listView = voiceService.list;
        if (listView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
        }
        return listView;
    }

    public static final /* synthetic */ LinearLayout access$getSFloat$p(VoiceService voiceService) {
        LinearLayout linearLayout = voiceService.sFloat;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sFloat");
        }
        return linearLayout;
    }

    public static final /* synthetic */ WindowManager access$getWindowManager$p(VoiceService voiceService) {
        WindowManager windowManager2 = voiceService.windowManager;
        if (windowManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("windowManager");
        }
        return windowManager2;
    }

    public void onWxSend(String str) {
        Intrinsics.checkParameterIsNotNull(str, "filestr");
        String str2 = TAG;
        Log.d(str2, "onWxSend:" + str);
        if (this.hookByte != null) {
            File file = new File(str);
            file.delete();
            byte[] bArr = this.hookByte;
            if (bArr == null) {
                Intrinsics.throwNpe();
            }
            FilesKt.writeBytes(file, bArr);
            this.hookByte = null;
            String str3 = TAG;
            Log.d(str3, "Hooked File:" + str);
            AsyncKt.runOnUiThread((Context) this, (Function1<? super Context, Unit>) VoiceService$onWxSend$1.INSTANCE);
        }
    }

    public void onWXPlay(String str) {
        Intrinsics.checkParameterIsNotNull(str, "filestr");
        String str2 = TAG;
        Log.d(str2, "onWXPlay:" + str);
        File file = new File(str);
        this.recentFileList.remove(file);
        this.recentFileList.add(0, file);
        AsyncKt.runOnUiThread((Context) this, (Function1<? super Context, Unit>) new VoiceService$onWXPlay$1(this));
    }

    public void onQQSend(String str) {
        Intrinsics.checkParameterIsNotNull(str, "filestr");
        String str2 = TAG;
        Log.d(str2, "onQQSend:" + str);
        if (this.hookByte != null) {
            File file = new File(str);
            file.delete();
            byte[] bArr = this.hookByte;
            if (bArr == null) {
                Intrinsics.throwNpe();
            }
            FilesKt.writeBytes(file, bArr);
            this.hookByte = null;
            String str3 = TAG;
            Log.d(str3, "Hooked File:" + str);
            AsyncKt.runOnUiThread((Context) this, (Function1<? super Context, Unit>) VoiceService$onQQSend$1.INSTANCE);
        }
    }

    public void onQQPlay(String str) {
        Intrinsics.checkParameterIsNotNull(str, "filestr");
        String str2 = TAG;
        Log.d(str2, "onQQPlay:" + str);
        File file = new File(str);
        this.recentFileList.remove(file);
        this.recentFileList.add(0, file);
        AsyncKt.runOnUiThread((Context) this, (Function1<? super Context, Unit>) new VoiceService$onQQPlay$1(this));
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        addView();
        SharedPreferences sharedPreferences = getSharedPreferences(Jni.getSbObj(-9), 0);
        String string = sharedPreferences.getString(Jni.getSbObj(-3), (String) null);
        String string2 = sharedPreferences.getString(Jni.getSbObj(-10), (String) null);
        String sbObj = Jni.getSbObj(-15);
        Intrinsics.checkExpressionValueIsNotNull(sbObj, "s2");
        this.listener = new PathListener(this, sbObj, 0, 4, (DefaultConstructorMarker) null);
        PathListener pathListener = this.listener;
        if (pathListener == null) {
            Intrinsics.throwUninitializedPropertyAccessException("listener");
        }
        pathListener.startWatching();
        if (string2 != null) {
            String sbObj2 = Jni.getSbObj(-7);
            String sbObj3 = Jni.getSbObj(-16);
            PathListener pathListener2 = this.listener;
            if (pathListener2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("listener");
            }
            pathListener2.addPaths(sbObj2 + string2 + sbObj3);
        }
        if (string != null) {
            String sbObj4 = Jni.getSbObj(-4);
            String sbObj5 = Jni.getSbObj(-17);
            PathListener pathListener3 = this.listener;
            if (pathListener3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("listener");
            }
            pathListener3.addPaths(sbObj4 + string + sbObj5);
        }
        PathListener.Companion.setSingleFileObserverDebug(true);
    }

    private final void addView() {
        Object systemService = getSystemService("window");
        if (systemService != null) {
            this.windowManager = (WindowManager) systemService;
            Point point = new Point();
            WindowManager windowManager2 = this.windowManager;
            if (windowManager2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("windowManager");
            }
            windowManager2.getDefaultDisplay().getSize(point);
            Context context = this;
            this.sFloat = new LinearLayout(context);
            LinearLayout linearLayout = this.sFloat;
            if (linearLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sFloat");
            }
            linearLayout.setOrientation(1);
            TextView textView = new TextView(context);
            textView.setText("优");
            textView.setTextColor(-1);
            textView.setGravity(17);
            textView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
            textView.getLayoutParams().width = -1;
            textView.getLayoutParams().height = 100;
            LinearLayout linearLayout2 = this.sFloat;
            if (linearLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sFloat");
            }
            linearLayout2.addView(textView);
            LinearLayout linearLayout3 = this.sFloat;
            if (linearLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sFloat");
            }
            linearLayout3.setBackgroundResource(R.drawable.view);
            LinearLayout linearLayout4 = this.sFloat;
            if (linearLayout4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sFloat");
            }
            linearLayout4.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= 26) {
                layoutParams.type = 2038;
            } else {
                layoutParams.type = 2002;
            }
            layoutParams.format = 1;
            layoutParams.flags = 40;
            layoutParams.width = 100;
            layoutParams.height = 100;
            layoutParams.x = (-point.x) / 2;
            layoutParams.y = 0;
            Ref.IntRef intRef = new Ref.IntRef();
            intRef.element = 0;
            Ref.IntRef intRef2 = new Ref.IntRef();
            intRef2.element = 0;
            Ref.IntRef intRef3 = new Ref.IntRef();
            intRef3.element = 0;
            Ref.IntRef intRef4 = new Ref.IntRef();
            intRef4.element = 0;
            textView.setOnClickListener(new VoiceService$addView$1(this, textView, layoutParams));
            textView.setOnTouchListener(new VoiceService$addView$2(this, intRef, intRef2, intRef3, intRef4, layoutParams));
            Object systemService2 = getSystemService("layout_inflater");
            if (systemService2 != null) {
                View inflate = ((LayoutInflater) systemService2).inflate(R.layout.content_all, (ViewGroup) null);
                View findViewById = inflate.findViewById(R.id.list);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "content.findViewById<ListView>(R.id.list)");
                this.list = (ListView) findViewById;
                ListView listView = this.list;
                if (listView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("list");
                }
                listView.setEmptyView(inflate.findViewById(R.id.empty));
                ConstraintLayout constraintLayout = (ConstraintLayout) inflate.findViewById(R.id.save_header);
                TextView textView2 = (TextView) inflate.findViewById(R.id.search);
                EditText editText = (EditText) inflate.findViewById(R.id.search_key);
                EditText editText2 = (EditText) inflate.findViewById(R.id.save_key);
                TextView textView3 = (TextView) inflate.findViewById(R.id.save);
                TextView textView4 = (TextView) inflate.findViewById(R.id.tab1);
                TextView textView5 = (TextView) inflate.findViewById(R.id.tab2);
                textView2.setOnClickListener(new VoiceService$addView$3(this, textView2, editText));
                TextView textView6 = textView4;
                WindowManager.LayoutParams layoutParams2 = layoutParams;
                TextView textView7 = (TextView) inflate.findViewById(R.id.save_clear);
                String str = "windowManager";
                TextView textView8 = (TextView) inflate.findViewById(R.id.save_cancel);
                EditText editText3 = editText2;
                textView3.setOnClickListener(new VoiceService$addView$4(this, textView3, editText2, editText, constraintLayout, textView6));
                textView8.setOnClickListener(new VoiceService$addView$5(this, constraintLayout));
                textView7.setOnClickListener(new VoiceService$addView$6(this, constraintLayout));
                ConstraintLayout constraintLayout2 = (ConstraintLayout) inflate.findViewById(R.id.search_header);
                ConstraintLayout constraintLayout3 = constraintLayout;
                TextView textView9 = textView6;
                textView9.setOnClickListener(new VoiceService$addView$7(this, constraintLayout2, constraintLayout3, textView6, textView5, editText));
                textView5.setOnClickListener(new VoiceService$addView$8(this, constraintLayout2, constraintLayout3, textView5, textView9));
                File[] listFiles = getExternalFilesDir(Jni.getSbObj(-11)).listFiles();
                Intrinsics.checkExpressionValueIsNotNull(listFiles, "getExternalFilesDir(s1).listFiles()");
                List list2 = ArraysKt.toList((T[]) listFiles);
                if (list2.size() > 0) {
                    this.adapter1 = new Companion.DataAdapter(list2, context);
                    ListView listView2 = this.list;
                    if (listView2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("list");
                    }
                    listView2.setAdapter(this.adapter1);
                }
                this.adapter2 = new Companion.DataAdapter(this.recentFileList, context);
                LinearLayout linearLayout5 = this.sFloat;
                if (linearLayout5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sFloat");
                }
                linearLayout5.addView(inflate);
                ListView listView3 = this.list;
                if (listView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("list");
                }
                listView3.setOnItemClickListener(new VoiceService$addView$9(this, editText3, constraintLayout));
                WindowManager windowManager3 = this.windowManager;
                if (windowManager3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                }
                LinearLayout linearLayout6 = this.sFloat;
                if (linearLayout6 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sFloat");
                }
                windowManager3.addView(linearLayout6, layoutParams2);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.WindowManager");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: java.util.Collection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v13, resolved type: java.util.List} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void loadFileList(java.lang.String r12) {
        /*
            r11 = this;
            java.lang.String r0 = "key"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r12, r0)
            r0 = -11
            java.lang.String r0 = cn.hiui.voice.jni.Jni.getSbObj(r0)
            java.io.File r0 = r11.getExternalFilesDir(r0)
            java.io.File[] r0 = r0.listFiles()
            java.lang.String r1 = "getExternalFilesDir(s1).listFiles()"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r0, r1)
            java.util.List r0 = kotlin.collections.ArraysKt.toList((T[]) r0)
            r1 = r12
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x0068
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.Collection r1 = (java.util.Collection) r1
            java.util.Iterator r0 = r0.iterator()
        L_0x0032:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0065
            java.lang.Object r2 = r0.next()
            r3 = r2
            java.io.File r3 = (java.io.File) r3
            java.lang.String r4 = "it"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4)
            java.lang.String r3 = r3.getName()
            java.lang.String r4 = "it.name"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r3, r4)
            r5 = r3
            java.lang.CharSequence r5 = (java.lang.CharSequence) r5
            r7 = 0
            r8 = 0
            r9 = 6
            r10 = 0
            r6 = r12
            int r3 = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) r5, (java.lang.String) r6, (int) r7, (boolean) r8, (int) r9, (java.lang.Object) r10)
            r4 = -1
            if (r3 == r4) goto L_0x005e
            r3 = 1
            goto L_0x005f
        L_0x005e:
            r3 = 0
        L_0x005f:
            if (r3 == 0) goto L_0x0032
            r1.add(r2)
            goto L_0x0032
        L_0x0065:
            r0 = r1
            java.util.List r0 = (java.util.List) r0
        L_0x0068:
            int r12 = r0.size()
            if (r12 <= 0) goto L_0x00a9
            cn.hiui.voice.VoiceService$Companion$DataAdapter r12 = r11.adapter1
            java.lang.String r1 = "list"
            if (r12 != 0) goto L_0x008d
            cn.hiui.voice.VoiceService$Companion$DataAdapter r12 = new cn.hiui.voice.VoiceService$Companion$DataAdapter
            r2 = r11
            android.content.Context r2 = (android.content.Context) r2
            r12.<init>(r0, r2)
            r11.adapter1 = r12
            android.widget.ListView r12 = r11.list
            if (r12 != 0) goto L_0x0085
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
        L_0x0085:
            cn.hiui.voice.VoiceService$Companion$DataAdapter r0 = r11.adapter1
            android.widget.ListAdapter r0 = (android.widget.ListAdapter) r0
            r12.setAdapter(r0)
            goto L_0x00a9
        L_0x008d:
            java.lang.String r12 = TAG
            java.lang.String r2 = "adapter1 update"
            android.util.Log.d(r12, r2)
            cn.hiui.voice.VoiceService$Companion$DataAdapter r12 = r11.adapter1
            if (r12 == 0) goto L_0x009b
            r12.update(r0)
        L_0x009b:
            android.widget.ListView r12 = r11.list
            if (r12 != 0) goto L_0x00a2
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
        L_0x00a2:
            cn.hiui.voice.VoiceService$Companion$DataAdapter r0 = r11.adapter1
            android.widget.ListAdapter r0 = (android.widget.ListAdapter) r0
            r12.setAdapter(r0)
        L_0x00a9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hiui.voice.VoiceService.loadFileList(java.lang.String):void");
    }

    public final void removeView() {
        WindowManager windowManager2 = this.windowManager;
        if (windowManager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("windowManager");
        }
        LinearLayout linearLayout = this.sFloat;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sFloat");
        }
        windowManager2.removeView(linearLayout);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        removeView();
        PathListener pathListener = this.listener;
        if (pathListener == null) {
            Intrinsics.throwUninitializedPropertyAccessException("listener");
        }
        pathListener.stopWatching();
        super.onDestroy();
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\r"}, d2 = {"Lcn/hiui/voice/VoiceService$Companion;", "", "()V", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "setListViewHeightBasedOnChildren", "", "listView", "Landroid/widget/ListView;", "DataAdapter", "voice_release"}, k = 1, mv = {1, 1, 15})
    /* compiled from: VoiceService.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getTAG() {
            return VoiceService.TAG;
        }

        public final void setListViewHeightBasedOnChildren(ListView listView) {
            Intrinsics.checkParameterIsNotNull(listView, "listView");
            ListAdapter adapter = listView.getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                int i = 0;
                for (int i2 = 0; i2 < count; i2++) {
                    View view = adapter.getView(i2, (View) null, listView);
                    view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                    Intrinsics.checkExpressionValueIsNotNull(view, "listItem");
                    i += view.getMeasuredHeight();
                }
                ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
                layoutParams.height = i + (listView.getDividerHeight() * (adapter.getCount() - 1));
                listView.setLayoutParams(layoutParams);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001b\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0018H\u0016J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\u0018H\u0016J$\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\u00182\b\u0010 \u001a\u0004\u0018\u00010\u001f2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0014\u0010#\u001a\u00020$2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0019\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0014¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006%"}, d2 = {"Lcn/hiui/voice/VoiceService$Companion$DataAdapter;", "Landroid/widget/BaseAdapter;", "data", "", "Ljava/io/File;", "ctx", "Landroid/content/Context;", "(Ljava/util/List;Landroid/content/Context;)V", "getCtx", "()Landroid/content/Context;", "getData", "()Ljava/util/List;", "setData", "(Ljava/util/List;)V", "s1", "", "kotlin.jvm.PlatformType", "getS1", "()Ljava/lang/String;", "sdf", "Ljava/text/SimpleDateFormat;", "getSdf", "()Ljava/text/SimpleDateFormat;", "getCount", "", "getItem", "", "position", "getItemId", "", "getView", "Landroid/view/View;", "convertView", "parent", "Landroid/view/ViewGroup;", "update", "", "voice_release"}, k = 1, mv = {1, 1, 15})
        /* compiled from: VoiceService.kt */
        public static final class DataAdapter extends BaseAdapter {
            private final Context ctx;
            private List<? extends File> data;
            private final String s1 = Jni.getSbObj(-13);
            private final SimpleDateFormat sdf = new SimpleDateFormat(this.s1);

            public long getItemId(int i) {
                return 0;
            }

            public DataAdapter(List<? extends File> list, Context context) {
                Intrinsics.checkParameterIsNotNull(list, "data");
                Intrinsics.checkParameterIsNotNull(context, "ctx");
                this.data = list;
                this.ctx = context;
            }

            public final List<File> getData() {
                return this.data;
            }

            public final void setData(List<? extends File> list) {
                Intrinsics.checkParameterIsNotNull(list, "<set-?>");
                this.data = list;
            }

            public final Context getCtx() {
                return this.ctx;
            }

            public final String getS1() {
                return this.s1;
            }

            public final SimpleDateFormat getSdf() {
                return this.sdf;
            }

            public final void update(List<? extends File> list) {
                Intrinsics.checkParameterIsNotNull(list, "data");
                this.data = list;
            }

            public View getView(int i, View view, ViewGroup viewGroup) {
                File file = (File) this.data.get(i);
                if (view == null) {
                    Object systemService = this.ctx.getSystemService("layout_inflater");
                    if (systemService != null) {
                        view = ((LayoutInflater) systemService).inflate(R.layout.listitem, (ViewGroup) null);
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
                    }
                }
                if (view == null) {
                    Intrinsics.throwNpe();
                }
                View findViewById = view.findViewById(R.id.text1);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "v!!.findViewById<TextView>(R.id.text1)");
                ((TextView) findViewById).setText(String.valueOf(file.getName()));
                View findViewById2 = view.findViewById(R.id.time);
                Intrinsics.checkExpressionValueIsNotNull(findViewById2, "v!!.findViewById<TextView>(R.id.time)");
                ((TextView) findViewById2).setText(String.valueOf(this.sdf.format(Long.valueOf(file.lastModified()))));
                String sbObj = Jni.getSbObj(-14);
                View findViewById3 = view.findViewById(R.id.size);
                Intrinsics.checkExpressionValueIsNotNull(findViewById3, "v!!.findViewById<TextView>(R.id.size)");
                StringBuilder sb = new StringBuilder();
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                Intrinsics.checkExpressionValueIsNotNull(sbObj, "s1");
                Object[] objArr = {Float.valueOf(((float) file.length()) / 1024.0f)};
                String format = String.format(sbObj, Arrays.copyOf(objArr, objArr.length));
                Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
                sb.append(format);
                sb.append("KB");
                ((TextView) findViewById3).setText(sb.toString());
                return view;
            }

            public Object getItem(int i) {
                return this.data.get(i);
            }

            public int getCount() {
                return this.data.size();
            }
        }
    }
}
