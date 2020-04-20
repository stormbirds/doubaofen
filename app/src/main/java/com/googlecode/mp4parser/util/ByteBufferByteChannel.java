package com.googlecode.mp4parser.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class ByteBufferByteChannel implements ByteChannel {
    ByteBuffer byteBuffer;

    public void close() throws IOException {
    }

    public boolean isOpen() {
        return true;
    }

    public ByteBufferByteChannel(ByteBuffer byteBuffer2) {
        this.byteBuffer = byteBuffer2;
    }

    public int read(ByteBuffer byteBuffer2) throws IOException {
        int remaining = byteBuffer2.remaining();
        if (this.byteBuffer.remaining() <= 0) {
            return -1;
        }
        byteBuffer2.put((ByteBuffer) this.byteBuffer.duplicate().limit(this.byteBuffer.position() + byteBuffer2.remaining()));
        ByteBuffer byteBuffer3 = this.byteBuffer;
        byteBuffer3.position(byteBuffer3.position() + remaining);
        return remaining;
    }

    public int write(ByteBuffer byteBuffer2) throws IOException {
        int remaining = byteBuffer2.remaining();
        this.byteBuffer.put(byteBuffer2);
        return remaining;
    }
}
