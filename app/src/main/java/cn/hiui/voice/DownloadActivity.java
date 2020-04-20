package cn.hiui.voice;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.hiui.voice.jni.Jni;
import com.alibaba.fastjson.JSON;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.anko.AlertBuilder;
import org.jetbrains.anko.AndroidDialogsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\nJ\u0012\u0010\u0011\u001a\u00020\n2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u0019"}, d2 = {"Lcn/hiui/voice/DownloadActivity;", "Landroid/support/v7/app/AppCompatActivity;", "()V", "mMediaPlayer", "Landroid/media/MediaPlayer;", "getMMediaPlayer", "()Landroid/media/MediaPlayer;", "setMMediaPlayer", "(Landroid/media/MediaPlayer;)V", "downloadMp3", "", "data", "Lcn/hiui/voice/DownloadActivity$Companion$NetData;", "downloadSlk", "toFile", "Ljava/io/File;", "getList", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "Companion", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: DownloadActivity.kt */
public final class DownloadActivity extends AppCompatActivity {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final String TAG = DownloadActivity.class.getSimpleName();
    private HashMap _$_findViewCache;
    public MediaPlayer mMediaPlayer;

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

    public final MediaPlayer getMMediaPlayer() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMediaPlayer");
        }
        return mediaPlayer;
    }

    public final void setMMediaPlayer(MediaPlayer mediaPlayer) {
        Intrinsics.checkParameterIsNotNull(mediaPlayer, "<set-?>");
        this.mMediaPlayer = mediaPlayer;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_download);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle((CharSequence) "下载语音");
        }
        ActionBar supportActionBar2 = getSupportActionBar();
        if (supportActionBar2 != null) {
            supportActionBar2.setHomeButtonEnabled(true);
        }
        ActionBar supportActionBar3 = getSupportActionBar();
        if (supportActionBar3 != null) {
            supportActionBar3.setDisplayHomeAsUpEnabled(true);
        }
        ListView listView = (ListView) _$_findCachedViewById(R.id.list);
        Intrinsics.checkExpressionValueIsNotNull(listView, "list");
        listView.setEmptyView((TextView) _$_findCachedViewById(R.id.empty));
        ((ListView) _$_findCachedViewById(R.id.list)).setOnItemClickListener(new DownloadActivity$onCreate$1(this));
        getList();
        this.mMediaPlayer = new MediaPlayer();
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMediaPlayer");
        }
        mediaPlayer.setOnErrorListener(new DownloadActivity$onCreate$2(this));
    }

    public final void getList() {
        ProgressBar progressBar = (ProgressBar) _$_findCachedViewById(R.id.loading);
        Intrinsics.checkExpressionValueIsNotNull(progressBar, "loading");
        progressBar.setVisibility(0);
        ListView listView = (ListView) _$_findCachedViewById(R.id.list);
        Intrinsics.checkExpressionValueIsNotNull(listView, "list");
        listView.setVisibility(8);
        TextView textView = (TextView) _$_findCachedViewById(R.id.empty);
        Intrinsics.checkExpressionValueIsNotNull(textView, "empty");
        textView.setText("Loading");
        new OkHttpClient().newCall(new Request.Builder().url("http://dysj.xuerenwx.com/api/file/getall&uid=NKAMoUblGfKU&project_id=100001").get().build()).enqueue(new DownloadActivity$getList$1(this));
    }

    public final void downloadMp3(Companion.NetData netData) {
        Intrinsics.checkParameterIsNotNull(netData, "data");
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMediaPlayer");
        }
        mediaPlayer.reset();
        MediaPlayer mediaPlayer2 = this.mMediaPlayer;
        if (mediaPlayer2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMediaPlayer");
        }
        mediaPlayer2.setDataSource(netData.getMp3());
        MediaPlayer mediaPlayer3 = this.mMediaPlayer;
        if (mediaPlayer3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMediaPlayer");
        }
        mediaPlayer3.prepare();
        MediaPlayer mediaPlayer4 = this.mMediaPlayer;
        if (mediaPlayer4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mMediaPlayer");
        }
        mediaPlayer4.start();
        AndroidDialogsKt.alert((Context) this, (Function1<? super AlertBuilder<? extends DialogInterface>, Unit>) new DownloadActivity$downloadMp3$1(this, netData)).show();
    }

    public final void downloadSlk(Companion.NetData netData) {
        Intrinsics.checkParameterIsNotNull(netData, "data");
        File file = new File(getExternalFilesDir(Jni.getSbObj(-1)), netData.name());
        if (file.exists()) {
            AndroidDialogsKt.alert((Context) this, (Function1<? super AlertBuilder<? extends DialogInterface>, Unit>) new DownloadActivity$downloadSlk$1(this, file, netData)).show();
        } else {
            downloadSlk(netData, file);
        }
    }

    public final void downloadSlk(Companion.NetData netData, File file) {
        Intrinsics.checkParameterIsNotNull(netData, "data");
        Intrinsics.checkParameterIsNotNull(file, "toFile");
        new OkHttpClient().newCall(new Request.Builder().url(netData.getSlk()).get().build()).enqueue(new DownloadActivity$downloadSlk$2(this, file, netData));
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0003\u0018\u00002\u00020\u0001:\u0002\b\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\n"}, d2 = {"Lcn/hiui/voice/DownloadActivity$Companion;", "", "()V", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "NetData", "NetDataAdapter", "voice_release"}, k = 1, mv = {1, 1, 15})
    /* compiled from: DownloadActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getTAG() {
            return DownloadActivity.TAG;
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u0006\u0010\t\u001a\u00020\u0003J\b\u0010\n\u001a\u00020\u0003H\u0016R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u000b"}, d2 = {"Lcn/hiui/voice/DownloadActivity$Companion$NetData;", "", "slk", "", "mp3", "(Ljava/lang/String;Ljava/lang/String;)V", "getMp3", "()Ljava/lang/String;", "getSlk", "name", "toString", "voice_release"}, k = 1, mv = {1, 1, 15})
        /* compiled from: DownloadActivity.kt */
        public static final class NetData {
            private final String mp3;
            private final String slk;

            public NetData(String str, String str2) {
                Intrinsics.checkParameterIsNotNull(str, "slk");
                Intrinsics.checkParameterIsNotNull(str2, "mp3");
                this.slk = str;
                this.mp3 = str2;
            }

            public final String getSlk() {
                return this.slk;
            }

            public final String getMp3() {
                return this.mp3;
            }

            public String toString() {
                String jSONString = JSON.toJSONString(this);
                Intrinsics.checkExpressionValueIsNotNull(jSONString, "JSON.toJSONString(this)");
                return jSONString;
            }

            public final String name() {
                String str = this.slk;
                int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) str, "/", 0, false, 6, (Object) null) + 1;
                int length = this.slk.length();
                if (str != null) {
                    String substring = str.substring(lastIndexOf$default, length);
                    Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    return substring;
                }
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001b\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J$\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0012\u001a\u00020\u000f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00162\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r¨\u0006\u001a"}, d2 = {"Lcn/hiui/voice/DownloadActivity$Companion$NetDataAdapter;", "Landroid/widget/BaseAdapter;", "data", "", "Lcn/hiui/voice/DownloadActivity$Companion$NetData;", "ctx", "Landroid/content/Context;", "(Ljava/util/List;Landroid/content/Context;)V", "getCtx", "()Landroid/content/Context;", "getData", "()Ljava/util/List;", "setData", "(Ljava/util/List;)V", "getCount", "", "getItem", "", "position", "getItemId", "", "getView", "Landroid/view/View;", "convertView", "parent", "Landroid/view/ViewGroup;", "voice_release"}, k = 1, mv = {1, 1, 15})
        /* compiled from: DownloadActivity.kt */
        public static final class NetDataAdapter extends BaseAdapter {
            private final Context ctx;
            private List<NetData> data;

            public long getItemId(int i) {
                return (long) i;
            }

            public NetDataAdapter(List<NetData> list, Context context) {
                Intrinsics.checkParameterIsNotNull(list, "data");
                Intrinsics.checkParameterIsNotNull(context, "ctx");
                this.data = list;
                this.ctx = context;
            }

            public final List<NetData> getData() {
                return this.data;
            }

            public final void setData(List<NetData> list) {
                Intrinsics.checkParameterIsNotNull(list, "<set-?>");
                this.data = list;
            }

            public final Context getCtx() {
                return this.ctx;
            }

            public Object getItem(int i) {
                return this.data.get(i);
            }

            public int getCount() {
                return this.data.size();
            }

            public View getView(int i, View view, ViewGroup viewGroup) {
                Object item = getItem(i);
                if (item != null) {
                    NetData netData = (NetData) item;
                    if (view == null) {
                        Object systemService = this.ctx.getSystemService("layout_inflater");
                        if (systemService != null) {
                            view = ((LayoutInflater) systemService).inflate(R.layout.listitem_download, (ViewGroup) null);
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type android.view.LayoutInflater");
                        }
                    }
                    if (view == null) {
                        Intrinsics.throwNpe();
                    }
                    View findViewById = view.findViewById(R.id.text1);
                    Intrinsics.checkExpressionValueIsNotNull(findViewById, "v!!.findViewById<TextView>(R.id.text1)");
                    ((TextView) findViewById).setText(String.valueOf(netData.name()));
                    return view;
                }
                throw new TypeCastException("null cannot be cast to non-null type cn.hiui.voice.DownloadActivity.Companion.NetData");
            }
        }
    }
}
