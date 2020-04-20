package com.googlecode.mp4parser.authoring;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class SampleImpl implements Sample {
    private ByteBuffer[] data;
    private final long offset;
    private final Container parent;
    private final long size;

    public SampleImpl(ByteBuffer byteBuffer) {
        this.offset = -1;
        this.size = (long) byteBuffer.limit();
        this.data = new ByteBuffer[]{byteBuffer};
        this.parent = null;
    }

    public SampleImpl(ByteBuffer[] byteBufferArr) {
        this.offset = -1;
        int i = 0;
        for (ByteBuffer remaining : byteBufferArr) {
            i += remaining.remaining();
        }
        this.size = (long) i;
        this.data = byteBufferArr;
        this.parent = null;
    }

    public SampleImpl(long j, long j2, ByteBuffer byteBuffer) {
        this.offset = j;
        this.size = j2;
        this.data = new ByteBuffer[]{byteBuffer};
        this.parent = null;
    }

    public SampleImpl(long j, long j2, Container container) {
        this.offset = j;
        this.size = j2;
        this.data = null;
        this.parent = container;
    }

    /* access modifiers changed from: protected */
    public void ensureData() {
        if (this.data == null) {
            Container container = this.parent;
            if (container != null) {
                try {
                    this.data = new ByteBuffer[]{container.getByteBuffer(this.offset, this.size)};
                } catch (IOException e) {
                    throw new RuntimeException("couldn't read sample " + this, e);
                }
            } else {
                throw new RuntimeException("Missing parent container, can't read sample " + this);
            }
        }
    }

    public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
        ensureData();
        for (ByteBuffer duplicate : this.data) {
            writableByteChannel.write(duplicate.duplicate());
        }
    }

    public long getSize() {
        return this.size;
    }

    public ByteBuffer asByteBuffer() {
        ensureData();
        ByteBuffer wrap = ByteBuffer.wrap(new byte[CastUtils.l2i(this.size)]);
        for (ByteBuffer duplicate : this.data) {
            wrap.put(duplicate.duplicate());
        }
        wrap.rewind();
        return wrap;
    }

    public String toString() {
        return "SampleImpl" + "{offset=" + this.offset + "{size=" + this.size + '}';
    }
}
