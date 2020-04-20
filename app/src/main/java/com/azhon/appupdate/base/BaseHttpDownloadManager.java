package com.azhon.appupdate.base;

import com.azhon.appupdate.listener.OnDownloadListener;

public abstract class BaseHttpDownloadManager {
    public abstract void download(String str, String str2, OnDownloadListener onDownloadListener);
}
