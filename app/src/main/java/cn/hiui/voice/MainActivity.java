package cn.hiui.voice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.hiui.voice.VoiceService;
import cn.hiui.voice.jni.Jni;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import org.jetbrains.anko.AndroidSelectorsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0006\u0018\u0000 '2\u00020\u0001:\u0001'B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\nJ\b\u0010\f\u001a\u00020\nH\u0002J\"\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0012\u0010\u0013\u001a\u00020\n2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J-\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u000e\u0010\u001e\u001a\n\u0012\u0006\b\u0001\u0012\u00020 0\u001f2\u0006\u0010!\u001a\u00020\"H\u0016¢\u0006\u0002\u0010#J\b\u0010$\u001a\u00020\nH\u0014J\u0006\u0010%\u001a\u00020\nJ\b\u0010&\u001a\u00020\nH\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006("}, d2 = {"Lcn/hiui/voice/MainActivity;", "Landroid/support/v7/app/AppCompatActivity;", "()V", "adapter", "Lcn/hiui/voice/VoiceService$Companion$DataAdapter;", "getAdapter", "()Lcn/hiui/voice/VoiceService$Companion$DataAdapter;", "setAdapter", "(Lcn/hiui/voice/VoiceService$Companion$DataAdapter;)V", "getQQDir", "", "getWxDir", "loadFile", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onRequestPermissionsResult", "permissions", "", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "requestAlertWindowPermission", "requestStoragePermissions", "Companion", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: MainActivity.kt */
public final class MainActivity extends AppCompatActivity {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final int REQUEST_CODE = 0;
    /* access modifiers changed from: private */
    public static final int REQUEST_CODE_PERMISSION = 1;
    /* access modifiers changed from: private */
    public static final String TAG = MainActivity.class.getSimpleName();
    private HashMap _$_findViewCache;
    private VoiceService.Companion.DataAdapter adapter;

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public final VoiceService.Companion.DataAdapter getAdapter() {
        return this.adapter;
    }

    public final void setAdapter(VoiceService.Companion.DataAdapter dataAdapter) {
        this.adapter = dataAdapter;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main1);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle((CharSequence) "语音库");
        }
        ListView listView = (ListView) _$_findCachedViewById(R.id.list);
        Intrinsics.checkExpressionValueIsNotNull(listView, "list");
        listView.setEmptyView((TextView) _$_findCachedViewById(R.id.empty));
        ((ListView) _$_findCachedViewById(R.id.list)).setOnItemClickListener(new MainActivity$onCreate$1(this));
    }

    /* access modifiers changed from: private */
    public final void loadFile() {
        File[] listFiles = getExternalFilesDir(Jni.getSbObj(-11)).listFiles();
        Intrinsics.checkExpressionValueIsNotNull(listFiles, "getExternalFilesDir(s).listFiles()");
        List list = ArraysKt.toList((T[]) listFiles);
        if (list.size() > 0) {
            this.adapter = new VoiceService.Companion.DataAdapter(list, this);
            ListView listView = (ListView) _$_findCachedViewById(R.id.list);
            Intrinsics.checkExpressionValueIsNotNull(listView, "list");
            listView.setAdapter(this.adapter);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Context context = this;
        if (!Settings.canDrawOverlays(context)) {
            requestAlertWindowPermission();
            return;
        }
        requestStoragePermissions();
        loadFile();
        startService(new Intent(context, VoiceService.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        Log.d(TAG, String.valueOf(menuItem.getTitle()));
        int itemId = menuItem.getItemId();
        if (itemId == R.id.menu_wx) {
            getWxDir();
        } else if (itemId == R.id.menu_qq) {
            getQQDir();
        } else if (itemId == R.id.menu_status) {
            Context context = this;
            stopService(new Intent(context, VoiceService.class));
            startService(new Intent(context, VoiceService.class));
        } else if (itemId == R.id.menu_downlooad) {
            startActivity(new Intent(this, DownloadActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private final void requestStoragePermissions() {
        String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE"};
        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (checkSelfPermission != 0 && checkSelfPermission == -1) {
            Activity activity = this;
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                ActivityCompat.requestPermissions(activity, strArr, REQUEST_CODE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(activity, strArr, REQUEST_CODE_PERMISSION);
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "permissions");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != REQUEST_CODE_PERMISSION) {
            return;
        }
        if (iArr[0] == 0) {
            Toast.makeText(this, "权限请求成功", 0).show();
        } else {
            Toast.makeText(this, "权限请求失败，应用需要储存卡写入权限，请在设置界面打开", 0).show();
        }
    }

    public final void requestAlertWindowPermission() {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == REQUEST_CODE && Settings.canDrawOverlays(this)) {
            Log.i(TAG, "onActivityResult granted");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void getWxDir() {
        /*
            r12 = this;
            r0 = -7
            java.lang.String r0 = cn.hiui.voice.jni.Jni.getSbObj(r0)
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            r0 = -5
            java.lang.String r0 = cn.hiui.voice.jni.Jni.getSbObj(r0)
            java.lang.String r2 = "Jni.getSbObj(-5)"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r0, r2)
            if (r0 == 0) goto L_0x00a5
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            java.lang.CharSequence r0 = kotlin.text.StringsKt.trim((java.lang.CharSequence) r0)
            java.lang.String r0 = r0.toString()
            kotlin.text.Regex r2 = new kotlin.text.Regex
            r2.<init>((java.lang.String) r0)
            boolean r0 = r1.exists()
            if (r0 == 0) goto L_0x00a4
            boolean r0 = r1.isDirectory()
            if (r0 == 0) goto L_0x00a4
            java.lang.String[] r0 = r1.list()
            java.lang.String r1 = "dir.list()"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r0, r1)
            java.util.List r0 = kotlin.collections.ArraysKt.toList((T[]) r0)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.Collection r1 = (java.util.Collection) r1
            java.util.Iterator r0 = r0.iterator()
        L_0x004b:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x008b
            java.lang.Object r3 = r0.next()
            r4 = r3
            java.lang.String r4 = (java.lang.String) r4
            r5 = -8
            java.lang.String r7 = cn.hiui.voice.jni.Jni.getSbObj(r5)
            int r5 = r4.length()
            r6 = 31
            if (r5 <= r6) goto L_0x0084
            java.lang.String r5 = "it"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r4, r5)
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4
            java.lang.String r5 = "s4"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r7, r5)
            r8 = 0
            r9 = 0
            r10 = 6
            r11 = 0
            r6 = r4
            int r5 = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) r6, (java.lang.String) r7, (int) r8, (boolean) r9, (int) r10, (java.lang.Object) r11)
            if (r5 >= 0) goto L_0x0084
            boolean r4 = r2.matches(r4)
            if (r4 == 0) goto L_0x0084
            r4 = 1
            goto L_0x0085
        L_0x0084:
            r4 = 0
        L_0x0085:
            if (r4 == 0) goto L_0x004b
            r1.add(r3)
            goto L_0x004b
        L_0x008b:
            java.util.List r1 = (java.util.List) r1
            java.lang.String r0 = TAG
            java.lang.String r2 = java.lang.String.valueOf(r1)
            android.util.Log.d(r0, r2)
            java.lang.String r0 = "选择微信ID"
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            cn.hiui.voice.MainActivity$getWxDir$1 r2 = new cn.hiui.voice.MainActivity$getWxDir$1
            r2.<init>(r12, r1)
            kotlin.jvm.functions.Function2 r2 = (kotlin.jvm.functions.Function2) r2
            org.jetbrains.anko.AndroidSelectorsKt.selector((android.content.Context) r12, (java.lang.CharSequence) r0, (java.util.List<? extends java.lang.CharSequence>) r1, (kotlin.jvm.functions.Function2<? super android.content.DialogInterface, ? super java.lang.Integer, kotlin.Unit>) r2)
        L_0x00a4:
            return
        L_0x00a5:
            kotlin.TypeCastException r0 = new kotlin.TypeCastException
            java.lang.String r1 = "null cannot be cast to non-null type kotlin.CharSequence"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hiui.voice.MainActivity.getWxDir():void");
    }

    public final void getQQDir() {
        File file = new File(Jni.getSbObj(-4));
        String sbObj = Jni.getSbObj(-6);
        Intrinsics.checkExpressionValueIsNotNull(sbObj, "s5");
        Regex regex = new Regex(sbObj);
        if (file.exists() && file.isDirectory()) {
            String[] list = file.list();
            Intrinsics.checkExpressionValueIsNotNull(list, "dir.list()");
            Collection arrayList = new ArrayList();
            for (Object next : ArraysKt.toList((T[]) list)) {
                String str = (String) next;
                Intrinsics.checkExpressionValueIsNotNull(str, "it");
                if (regex.matches(str)) {
                    arrayList.add(next);
                }
            }
            List list2 = (List) arrayList;
            Log.d(TAG, String.valueOf(list2));
            AndroidSelectorsKt.selector((Context) this, (CharSequence) "选择QQ号", (List<? extends CharSequence>) list2, (Function2<? super DialogInterface, ? super Integer, Unit>) new MainActivity$getQQDir$1(this, list2));
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0019\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\n¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000e"}, d2 = {"Lcn/hiui/voice/MainActivity$Companion;", "", "()V", "REQUEST_CODE", "", "getREQUEST_CODE", "()I", "REQUEST_CODE_PERMISSION", "getREQUEST_CODE_PERMISSION", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "voice_release"}, k = 1, mv = {1, 1, 15})
    /* compiled from: MainActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getTAG() {
            return MainActivity.TAG;
        }

        public final int getREQUEST_CODE() {
            return MainActivity.REQUEST_CODE;
        }

        public final int getREQUEST_CODE_PERMISSION() {
            return MainActivity.REQUEST_CODE_PERMISSION;
        }
    }
}
