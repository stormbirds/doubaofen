package com.tiangou.douxiaomi.http;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.azhon.appupdate.utils.Constant;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final int ACTION_DOWN_FAILED = 293;
    private final int ACTION_DOWN_LOADDING = 292;
    public final int ACTION_DOWN_SUCCESS = 291;
    private Call call;
    /* access modifiers changed from: private */
    public int d;
    /* access modifiers changed from: private */
    public long downloadLength;
    File file;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (DownloadUtil.this.listener != null) {
                String string = message.getData().getString("tag");
                switch (message.what) {
                    case 291:
                        DownloadUtil.this.listener.onDownloadSuccess(string, DownloadUtil.this.file.toString());
                        return;
                    case 292:
                        DownloadUtil.this.listener.onDownloading(string, DownloadUtil.this.d);
                        return;
                    case 293:
                        DownloadUtil.this.listener.onDownloadFailed(string);
                        return;
                    default:
                        return;
                }
            }
        }
    };
    public OnDownloadListener listener = null;
    private final OkHttpClient okHttpClient = new OkHttpClient();

    public interface OnDownloadListener {
        void onDownloadFailed(String str);

        void onDownloadSuccess(String str, String str2);

        void onDownloading(String str, int i);
    }

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
    }

    public void download(String str, String str2, String str3, String str4, OnDownloadListener onDownloadListener) {
        try {
            this.listener = onDownloadListener;
            this.file = new File(isExistDir(str2), str3 == null ? getNameFromUrl(str) : str3);
            Request.Builder builder = new Request.Builder();
            this.call = this.okHttpClient.newCall(builder.addHeader("RANGE", "bytes=" + this.downloadLength + "-").url(str).addHeader("Accept-Encoding", "identity").build());
            final String str5 = str4;
            final String str6 = str;
            final String str7 = str2;
            final String str8 = str3;
            final OnDownloadListener onDownloadListener2 = onDownloadListener;
            this.call.enqueue(new Callback() {
                public void onFailure(Call call, IOException iOException) {
                    long unused = DownloadUtil.this.downloadLength = 0;
                    Message message = new Message();
                    message.what = 293;
                    message.getData().putString("tag", str5);
                    DownloadUtil.this.handler.sendMessage(message);
                }

                /* JADX WARNING: Removed duplicated region for block: B:28:0x0138  */
                /* JADX WARNING: Removed duplicated region for block: B:30:0x013d  */
                /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onResponse(okhttp3.Call r19, okhttp3.Response r20) throws java.io.IOException {
                    /*
                        r18 = this;
                        r1 = r18
                        okhttp3.ResponseBody r0 = r20.body()
                        long r2 = r0.contentLength()
                        java.lang.Long r0 = java.lang.Long.valueOf(r2)
                        com.tiangou.douxiaomi.http.DownloadUtil r2 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        long r2 = r2.downloadLength
                        r4 = 291(0x123, float:4.08E-43)
                        java.lang.String r5 = "tag"
                        r6 = 0
                        int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                        if (r8 != 0) goto L_0x0082
                        com.tiangou.douxiaomi.http.DownloadUtil r2 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        java.io.File r2 = r2.file
                        boolean r2 = r2.exists()
                        if (r2 == 0) goto L_0x0082
                        com.tiangou.douxiaomi.http.DownloadUtil r2 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        java.io.File r3 = r2.file
                        long r8 = r3.length()
                        long unused = r2.downloadLength = r8
                        long r2 = r0.longValue()
                        com.tiangou.douxiaomi.http.DownloadUtil r8 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        long r8 = r8.downloadLength
                        int r10 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                        if (r10 == 0) goto L_0x005c
                        long r2 = r0.longValue()
                        r8 = -1
                        int r0 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                        if (r0 != 0) goto L_0x004c
                        goto L_0x005c
                    L_0x004c:
                        com.tiangou.douxiaomi.http.DownloadUtil r8 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        java.lang.String r9 = r4
                        java.lang.String r10 = r5
                        java.lang.String r11 = r6
                        java.lang.String r12 = r3
                        com.tiangou.douxiaomi.http.DownloadUtil$OnDownloadListener r13 = r7
                        r8.download(r9, r10, r11, r12, r13)
                        goto L_0x0081
                    L_0x005c:
                        android.os.Message r0 = new android.os.Message
                        r0.<init>()
                        r0.what = r4
                        android.os.Bundle r2 = r0.getData()
                        java.lang.String r3 = r3
                        r2.putString(r5, r3)
                        com.tiangou.douxiaomi.http.DownloadUtil r2 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        android.os.Handler r2 = r2.handler
                        r2.sendMessage(r0)
                        okhttp3.ResponseBody r0 = r20.body()
                        r0.close()
                        com.tiangou.douxiaomi.http.DownloadUtil r0 = com.tiangou.douxiaomi.http.DownloadUtil.this
                        long unused = r0.downloadLength = r6
                    L_0x0081:
                        return
                    L_0x0082:
                        r2 = 0
                        okhttp3.ResponseBody r3 = r20.body()     // Catch:{ IOException -> 0x0130 }
                        java.io.InputStream r3 = r3.byteStream()     // Catch:{ IOException -> 0x0130 }
                        java.io.RandomAccessFile r8 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x012d }
                        com.tiangou.douxiaomi.http.DownloadUtil r9 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012d }
                        java.io.File r9 = r9.file     // Catch:{ IOException -> 0x012d }
                        java.lang.String r10 = "rw"
                        r8.<init>(r9, r10)     // Catch:{ IOException -> 0x012d }
                        com.tiangou.douxiaomi.http.DownloadUtil r2 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        long r9 = r2.downloadLength     // Catch:{ IOException -> 0x012b }
                        r8.seek(r9)     // Catch:{ IOException -> 0x012b }
                        r2 = 1024(0x400, float:1.435E-42)
                        byte[] r2 = new byte[r2]     // Catch:{ IOException -> 0x012b }
                        java.lang.String r9 = "qyh"
                        java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x012b }
                        r10.<init>()     // Catch:{ IOException -> 0x012b }
                        java.lang.String r11 = "inputstream"
                        r10.append(r11)     // Catch:{ IOException -> 0x012b }
                        int r11 = r3.available()     // Catch:{ IOException -> 0x012b }
                        r10.append(r11)     // Catch:{ IOException -> 0x012b }
                        java.lang.String r10 = r10.toString()     // Catch:{ IOException -> 0x012b }
                        android.util.Log.e(r9, r10)     // Catch:{ IOException -> 0x012b }
                        r9 = 0
                        r10 = 0
                    L_0x00bf:
                        r11 = -1
                        int r12 = r3.read(r2)     // Catch:{ IOException -> 0x012b }
                        if (r11 == r12) goto L_0x0105
                        int r10 = r10 + r12
                        r8.write(r2, r9, r12)     // Catch:{ IOException -> 0x012b }
                        com.tiangou.douxiaomi.http.DownloadUtil r11 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        long r12 = (long) r10     // Catch:{ IOException -> 0x012b }
                        com.tiangou.douxiaomi.http.DownloadUtil r14 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        long r14 = r14.downloadLength     // Catch:{ IOException -> 0x012b }
                        long r12 = r12 + r14
                        r14 = 100
                        long r12 = r12 * r14
                        com.tiangou.douxiaomi.http.DownloadUtil r14 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        long r14 = r14.downloadLength     // Catch:{ IOException -> 0x012b }
                        long r16 = r0.longValue()     // Catch:{ IOException -> 0x012b }
                        long r14 = r14 + r16
                        long r12 = r12 / r14
                        int r13 = (int) r12     // Catch:{ IOException -> 0x012b }
                        int unused = r11.d = r13     // Catch:{ IOException -> 0x012b }
                        android.os.Message r11 = new android.os.Message     // Catch:{ IOException -> 0x012b }
                        r11.<init>()     // Catch:{ IOException -> 0x012b }
                        android.os.Bundle r12 = r11.getData()     // Catch:{ IOException -> 0x012b }
                        java.lang.String r13 = r3     // Catch:{ IOException -> 0x012b }
                        r12.putString(r5, r13)     // Catch:{ IOException -> 0x012b }
                        r12 = 292(0x124, float:4.09E-43)
                        r11.what = r12     // Catch:{ IOException -> 0x012b }
                        com.tiangou.douxiaomi.http.DownloadUtil r12 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        android.os.Handler r12 = r12.handler     // Catch:{ IOException -> 0x012b }
                        r12.sendMessage(r11)     // Catch:{ IOException -> 0x012b }
                        goto L_0x00bf
                    L_0x0105:
                        okhttp3.ResponseBody r0 = r20.body()     // Catch:{ IOException -> 0x012b }
                        r0.close()     // Catch:{ IOException -> 0x012b }
                        android.os.Message r0 = new android.os.Message     // Catch:{ IOException -> 0x012b }
                        r0.<init>()     // Catch:{ IOException -> 0x012b }
                        r0.what = r4     // Catch:{ IOException -> 0x012b }
                        android.os.Bundle r2 = r0.getData()     // Catch:{ IOException -> 0x012b }
                        java.lang.String r4 = r3     // Catch:{ IOException -> 0x012b }
                        r2.putString(r5, r4)     // Catch:{ IOException -> 0x012b }
                        com.tiangou.douxiaomi.http.DownloadUtil r2 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        android.os.Handler r2 = r2.handler     // Catch:{ IOException -> 0x012b }
                        r2.sendMessage(r0)     // Catch:{ IOException -> 0x012b }
                        com.tiangou.douxiaomi.http.DownloadUtil r0 = com.tiangou.douxiaomi.http.DownloadUtil.this     // Catch:{ IOException -> 0x012b }
                        long unused = r0.downloadLength = r6     // Catch:{ IOException -> 0x012b }
                        goto L_0x0136
                    L_0x012b:
                        r0 = move-exception
                        goto L_0x0133
                    L_0x012d:
                        r0 = move-exception
                        r8 = r2
                        goto L_0x0133
                    L_0x0130:
                        r0 = move-exception
                        r3 = r2
                        r8 = r3
                    L_0x0133:
                        r0.printStackTrace()
                    L_0x0136:
                        if (r3 == 0) goto L_0x013b
                        r3.close()
                    L_0x013b:
                        if (r8 == 0) goto L_0x0140
                        r8.close()
                    L_0x0140:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.tiangou.douxiaomi.http.DownloadUtil.AnonymousClass2.onResponse(okhttp3.Call, okhttp3.Response):void");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (downloadUtil != null) {
            this.call.cancel();
        }
    }

    private String isExistDir(String str) throws IOException {
        File file2 = new File(Environment.getExternalStorageDirectory(), str);
        if (!file2.mkdirs()) {
            file2.createNewFile();
        }
        return file2.getAbsolutePath();
    }

    public String getNameFromUrl(String str) {
        if (str.endsWith(".jpg") || str.endsWith(".png") || str.endsWith(Constant.APK_SUFFIX) || str.endsWith(".txt") || str.endsWith(".jpeg") || str.endsWith(".doc")) {
            return str.substring(str.lastIndexOf("/") + 1);
        }
        return str;
    }
}
