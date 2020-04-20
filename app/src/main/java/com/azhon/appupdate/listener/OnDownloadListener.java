package com.azhon.appupdate.listener;

import java.io.File;

public interface OnDownloadListener {
    void done(File file);

    void downloading(int i, int i2);

    void error(Exception exc);

    void start();
}
