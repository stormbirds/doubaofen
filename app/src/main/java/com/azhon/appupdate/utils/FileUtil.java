package com.azhon.appupdate.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public final class FileUtil {
    public static void createDirDirectory(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static RandomAccessFile createRAFile(String str, String str2) {
        try {
            return new RandomAccessFile(createFile(str, str2), "rwd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File createFile(String str, String str2) {
        return new File(str, str2);
    }

    public static boolean fileExists(String str, String str2) {
        return new File(str, str2).exists();
    }
}
