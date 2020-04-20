package com.googlecode.mp4parser;

import android.support.v4.os.EnvironmentCompat;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class FileDataSourceViaHeapImpl implements DataSource {
    private static Logger LOG = Logger.getLogger(FileDataSourceViaHeapImpl.class);
    FileChannel fc;
    String filename;

    public FileDataSourceViaHeapImpl(File file) throws FileNotFoundException {
        this.fc = new FileInputStream(file).getChannel();
        this.filename = file.getName();
    }

    public FileDataSourceViaHeapImpl(String str) throws FileNotFoundException {
        File file = new File(str);
        this.fc = new FileInputStream(file).getChannel();
        this.filename = file.getName();
    }

    public FileDataSourceViaHeapImpl(FileChannel fileChannel) {
        this.fc = fileChannel;
        this.filename = EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public FileDataSourceViaHeapImpl(FileChannel fileChannel, String str) {
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
        ByteBuffer allocate;
        allocate = ByteBuffer.allocate(CastUtils.l2i(j2));
        this.fc.read(allocate, j);
        return (ByteBuffer) allocate.rewind();
    }

    public void close() throws IOException {
        this.fc.close();
    }

    public String toString() {
        return this.filename;
    }
}
