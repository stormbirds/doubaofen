package com.azhon.appupdate.manager;

import android.content.Context;
import com.azhon.appupdate.base.BaseHttpDownloadManager;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.utils.Constant;
import com.azhon.appupdate.utils.FileUtil;
import com.azhon.appupdate.utils.LogUtil;
import com.azhon.appupdate.utils.SharePreUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpDownloadManager extends BaseHttpDownloadManager {
    private static final String TAG = "HttpDownloadManager";
    private String apkName;
    private String apkUrl;
    private Context context;
    private String downloadPath;
    private OnDownloadListener listener;
    private Runnable runnable = new Runnable() {
        public void run() {
            if (DownloadManager.getInstance().getConfiguration().isBreakpointDownload()) {
                HttpDownloadManager.this.breakpointDownload();
            } else {
                HttpDownloadManager.this.fullDownload();
            }
        }
    };

    public HttpDownloadManager(Context context2, String str) {
        this.context = context2;
        this.downloadPath = str;
    }

    public void download(String str, String str2, OnDownloadListener onDownloadListener) {
        this.apkUrl = str;
        this.apkName = str2;
        this.listener = onDownloadListener;
        new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName(Constant.THREAD_NAME);
                return thread;
            }
        }).execute(this.runnable);
    }

    /* access modifiers changed from: private */
    public void breakpointDownload() {
        this.listener.start();
        int contentLength = getContentLength();
        if (contentLength <= 0) {
            this.listener.error(new RuntimeException("获取到的文件大小为0！"));
            return;
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.apkUrl).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(Constant.HTTP_TIME_OUT);
            httpURLConnection.setConnectTimeout(Constant.HTTP_TIME_OUT);
            if (!FileUtil.fileExists(this.downloadPath, this.apkName)) {
                SharePreUtil.putInt(this.context, "progress", 0);
            }
            int i = SharePreUtil.getInt(this.context, "progress", 0);
            httpURLConnection.setRequestProperty("Range", "bytes=" + i + "-" + contentLength);
            if (httpURLConnection.getResponseCode() == 206) {
                InputStream inputStream = httpURLConnection.getInputStream();
                RandomAccessFile createRAFile = FileUtil.createRAFile(this.downloadPath, this.apkName);
                if (createRAFile != null) {
                    createRAFile.seek((long) i);
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        createRAFile.write(bArr, 0, read);
                        i += read;
                        SharePreUtil.putInt(this.context, "progress", i);
                        this.listener.downloading(contentLength, i);
                    }
                    SharePreUtil.putInt(this.context, "progress", 0);
                    createRAFile.close();
                    this.listener.done(FileUtil.createFile(this.downloadPath, this.apkName));
                } else {
                    this.listener.error(new RuntimeException("apk存储文件创建失败！"));
                }
                inputStream.close();
            } else {
                LogUtil.e(TAG, "breakpointDownload: 当前下载地址可能不支持断点下载，使用全量下载");
                fullDownload();
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            this.listener.error(e);
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void fullDownload() {
        this.listener.start();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.apkUrl).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(Constant.HTTP_TIME_OUT);
            httpURLConnection.setConnectTimeout(Constant.HTTP_TIME_OUT);
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                int contentLength = httpURLConnection.getContentLength();
                byte[] bArr = new byte[4096];
                File createFile = FileUtil.createFile(this.downloadPath, this.apkName);
                FileOutputStream fileOutputStream = new FileOutputStream(createFile);
                int i = 0;
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                    i += read;
                    this.listener.downloading(contentLength, i);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                this.listener.done(createFile);
            } else {
                this.listener.error(new SocketTimeoutException("连接超时！"));
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
            this.listener.error(e);
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0046 A[SYNTHETIC, Splitter:B:24:0x0046] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0052 A[SYNTHETIC, Splitter:B:31:0x0052] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getContentLength() {
        /*
            r5 = this;
            r0 = 0
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            java.lang.String r3 = r5.apkUrl     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            r2.<init>(r3)     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            java.net.URLConnection r2 = r2.openConnection()     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            java.lang.String r1 = "GET"
            r2.setRequestMethod(r1)     // Catch:{ Exception -> 0x0038 }
            r1 = 5000(0x1388, float:7.006E-42)
            r2.setReadTimeout(r1)     // Catch:{ Exception -> 0x0038 }
            r2.setConnectTimeout(r1)     // Catch:{ Exception -> 0x0038 }
            int r1 = r2.getResponseCode()     // Catch:{ Exception -> 0x0038 }
            r3 = 200(0xc8, float:2.8E-43)
            if (r1 != r3) goto L_0x0029
            int r1 = r2.getContentLength()     // Catch:{ Exception -> 0x0038 }
            goto L_0x002a
        L_0x0029:
            r1 = 0
        L_0x002a:
            r2.disconnect()     // Catch:{ Exception -> 0x0038 }
            if (r2 == 0) goto L_0x0037
            r2.disconnect()     // Catch:{ Exception -> 0x0033 }
            goto L_0x0037
        L_0x0033:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0037:
            return r1
        L_0x0038:
            r1 = move-exception
            goto L_0x0041
        L_0x003a:
            r0 = move-exception
            r2 = r1
            goto L_0x0050
        L_0x003d:
            r2 = move-exception
            r4 = r2
            r2 = r1
            r1 = r4
        L_0x0041:
            r1.printStackTrace()     // Catch:{ all -> 0x004f }
            if (r2 == 0) goto L_0x004e
            r2.disconnect()     // Catch:{ Exception -> 0x004a }
            goto L_0x004e
        L_0x004a:
            r1 = move-exception
            r1.printStackTrace()
        L_0x004e:
            return r0
        L_0x004f:
            r0 = move-exception
        L_0x0050:
            if (r2 == 0) goto L_0x005a
            r2.disconnect()     // Catch:{ Exception -> 0x0056 }
            goto L_0x005a
        L_0x0056:
            r1 = move-exception
            r1.printStackTrace()
        L_0x005a:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.azhon.appupdate.manager.HttpDownloadManager.getContentLength():int");
    }
}
