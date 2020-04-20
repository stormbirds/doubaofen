package com.googlecode.mp4parser;

import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class MemoryDataSourceImpl implements DataSource {
    ByteBuffer data;

    public void close() throws IOException {
    }

    public MemoryDataSourceImpl(byte[] bArr) {
        this.data = ByteBuffer.wrap(bArr);
    }

    public MemoryDataSourceImpl(ByteBuffer byteBuffer) {
        this.data = byteBuffer;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        if (this.data.remaining() == 0 && byteBuffer.remaining() != 0) {
            return -1;
        }
        int min = Math.min(byteBuffer.remaining(), this.data.remaining());
        if (byteBuffer.hasArray()) {
            byteBuffer.put(this.data.array(), this.data.position(), min);
            ByteBuffer byteBuffer2 = this.data;
            byteBuffer2.position(byteBuffer2.position() + min);
        } else {
            byte[] bArr = new byte[min];
            this.data.get(bArr);
            byteBuffer.put(bArr);
        }
        return min;
    }

    public long size() throws IOException {
        return (long) this.data.capacity();
    }

    public long position() throws IOException {
        return (long) this.data.position();
    }

    public void position(long j) throws IOException {
        this.data.position(CastUtils.l2i(j));
    }

    public long transferTo(long j, long j2, WritableByteChannel writableByteChannel) throws IOException {
        return (long) writableByteChannel.write((ByteBuffer) ((ByteBuffer) this.data.position(CastUtils.l2i(j))).slice().limit(CastUtils.l2i(j2)));
    }

    public ByteBuffer map(long j, long j2) throws IOException {
        int position = this.data.position();
        this.data.position(CastUtils.l2i(j));
        ByteBuffer slice = this.data.slice();
        slice.limit(CastUtils.l2i(j2));
        this.data.position(position);
        return slice;
    }
}
