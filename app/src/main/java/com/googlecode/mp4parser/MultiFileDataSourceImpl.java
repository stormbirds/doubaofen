package com.googlecode.mp4parser;

import com.googlecode.mp4parser.util.CastUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class MultiFileDataSourceImpl implements DataSource {
    FileChannel[] fcs;
    int index = 0;

    public MultiFileDataSourceImpl(File... fileArr) throws FileNotFoundException {
        this.fcs = new FileChannel[fileArr.length];
        for (int i = 0; i < fileArr.length; i++) {
            this.fcs[i] = new FileInputStream(fileArr[i]).getChannel();
        }
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        int remaining = byteBuffer.remaining();
        int read = this.fcs[this.index].read(byteBuffer);
        if (read == remaining) {
            return read;
        }
        this.index++;
        return read + read(byteBuffer);
    }

    public long size() throws IOException {
        long j = 0;
        for (FileChannel size : this.fcs) {
            j += size.size();
        }
        return j;
    }

    public long position() throws IOException {
        long j = 0;
        int i = 0;
        while (true) {
            int i2 = this.index;
            if (i >= i2) {
                return j + this.fcs[i2].position();
            }
            j += this.fcs[i].size();
            i++;
        }
    }

    public void position(long j) throws IOException {
        int i = 0;
        while (true) {
            FileChannel[] fileChannelArr = this.fcs;
            if (i < fileChannelArr.length) {
                if (j - fileChannelArr[i].size() < 0) {
                    this.fcs[i].position(j);
                    this.index = i;
                    return;
                }
                j -= this.fcs[i].size();
                i++;
            } else {
                return;
            }
        }
    }

    public long transferTo(long j, long j2, WritableByteChannel writableByteChannel) throws IOException {
        long j3 = j2;
        if (j3 == 0) {
            return 0;
        }
        FileChannel[] fileChannelArr = this.fcs;
        int length = fileChannelArr.length;
        int i = 0;
        long j4 = 0;
        while (i < length) {
            FileChannel fileChannel = fileChannelArr[i];
            long size = fileChannel.size();
            if (j < j4 || j >= j4 + size || j + j3 <= j4) {
                j4 += size;
                i++;
            } else {
                long j5 = j - j4;
                long min = Math.min(j3, size - j5);
                fileChannel.transferTo(j5, min, writableByteChannel);
                return min + transferTo(j + min, j3 - min, writableByteChannel);
            }
        }
        return 0;
    }

    public ByteBuffer map(long j, long j2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(CastUtils.l2i(j2));
        transferTo(j, j2, Channels.newChannel(byteArrayOutputStream));
        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
    }

    public void close() throws IOException {
        for (FileChannel close : this.fcs) {
            close.close();
        }
    }
}
