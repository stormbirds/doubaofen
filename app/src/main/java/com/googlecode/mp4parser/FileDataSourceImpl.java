package com.googlecode.mp4parser;

import android.support.v4.os.EnvironmentCompat;
import com.googlecode.mp4parser.util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class FileDataSourceImpl implements DataSource {
    private static Logger LOG = Logger.getLogger(FileDataSourceImpl.class);
    FileChannel fc;
    String filename;

    public FileDataSourceImpl(File file) throws FileNotFoundException {
        this.fc = new FileInputStream(file).getChannel();
        this.filename = file.getName();
    }

    public FileDataSourceImpl(String str) throws FileNotFoundException {
        File file = new File(str);
        this.fc = new FileInputStream(file).getChannel();
        this.filename = file.getName();
    }

    public FileDataSourceImpl(FileChannel fileChannel) {
        this.fc = fileChannel;
        this.filename = EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public FileDataSourceImpl(FileChannel fileChannel, String str) {
        this.fc = fileChannel;
        this.filename = str;
    }

    public synchronized int read(ByteBuffer byteBuffer) throws IOException {
        return this.fc.read(byteBuffer);
    }

    public synchronized long size() throws IOException {
        return this.fc.size();
    }

    public synchronized long position() throws IOException {
        return this.fc.position();
    }

    public synchronized void position(long j) throws IOException {
        this.fc.position(j);
    }

    public synchronized long transferTo(long j, long j2, WritableByteChannel writableByteChannel) throws IOException {
        return this.fc.transferTo(j, j2, writableByteChannel);
    }

    public synchronized ByteBuffer map(long j, long j2) throws IOException {
        return this.fc.map(FileChannel.MapMode.READ_ONLY, j, j2);
    }

    public void close() throws IOException {
        this.fc.close();
    }

    public String toString() {
        return this.filename;
    }
}
