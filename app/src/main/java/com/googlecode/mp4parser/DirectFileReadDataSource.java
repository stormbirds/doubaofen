package com.googlecode.mp4parser;

import com.googlecode.mp4parser.util.CastUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class DirectFileReadDataSource implements DataSource {
    private static final int TRANSFER_SIZE = 8192;
    private String filename;
    private RandomAccessFile raf;

    public DirectFileReadDataSource(File file) throws IOException {
        this.raf = new RandomAccessFile(file, "r");
        this.filename = file.getName();
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        int remaining = byteBuffer.remaining();
        byte[] bArr = new byte[8192];
        int i = 0;
        int i2 = 0;
        while (i < remaining) {
            i2 = this.raf.read(bArr, 0, Math.min(remaining - i, 8192));
            if (i2 < 0) {
                break;
            }
            i += i2;
            byteBuffer.put(bArr, 0, i2);
        }
        if (i2 >= 0 || i != 0) {
            return i;
        }
        return -1;
    }

    public int readAllInOnce(ByteBuffer byteBuffer) throws IOException {
        byte[] bArr = new byte[byteBuffer.remaining()];
        int read = this.raf.read(bArr);
        byteBuffer.put(bArr, 0, read);
        return read;
    }

    public long size() throws IOException {
        return this.raf.length();
    }

    public long position() throws IOException {
        return this.raf.getFilePointer();
    }

    public void position(long j) throws IOException {
        this.raf.seek(j);
    }

    public long transferTo(long j, long j2, WritableByteChannel writableByteChannel) throws IOException {
        return (long) writableByteChannel.write(map(j, j2));
    }

    public ByteBuffer map(long j, long j2) throws IOException {
        this.raf.seek(j);
        byte[] bArr = new byte[CastUtils.l2i(j2)];
        this.raf.readFully(bArr);
        return ByteBuffer.wrap(bArr);
    }

    public void close() throws IOException {
        this.raf.close();
    }

    public String toString() {
        return this.filename;
    }
}
