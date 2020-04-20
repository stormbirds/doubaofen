package com.tiangou.douxiaomi.utils;

import android.os.Environment;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class Mp4Util {
    public static final int EXTRACT_VIDEO = 6;
    public static final int MUSIC_EXTRACT = 5;
    public static final int MUSIC_EXTRACT_ERROR = 5404;
    public static String paht = (Environment.getExternalStorageDirectory() + "/");

    public static void modifyFileMD5(File file, File file2) {
        try {
            copyFile(file, file2);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
            randomAccessFile.seek(Long.valueOf(randomAccessFile.length() - 1).longValue());
            randomAccessFile.write(new byte[]{(byte) (randomAccessFile.readByte() + 1)});
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFile(File file, File file2) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (-1 != read) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean splitAac(String str, String str2) {
        try {
            Track track = null;
            for (Track next : MovieCreator.build(str).getTracks()) {
                if ("soun".equals(next.getHandler())) {
                    track = next;
                }
            }
            Movie movie = new Movie();
            movie.addTrack(track);
            Container build = new DefaultMp4Builder().build(movie);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str2));
            build.writeContainer(fileOutputStream.getChannel());
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean splitMp4(String str, String str2) {
        try {
            Track track = null;
            for (Track next : MovieCreator.build(str).getTracks()) {
                if ("vide".equals(next.getHandler())) {
                    track = next;
                }
            }
            Movie movie = new Movie();
            movie.addTrack(track);
            Container build = new DefaultMp4Builder().build(movie);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str2));
            build.writeContainer(fileOutputStream.getChannel());
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
