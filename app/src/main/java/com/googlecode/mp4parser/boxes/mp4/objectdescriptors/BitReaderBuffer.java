package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitReaderBuffer {
    private ByteBuffer buffer;
    int initialPos;
    int position;

    public BitReaderBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
        this.initialPos = byteBuffer.position();
    }

    public boolean readBool() {
        return readBits(1) == 1;
    }

    public int readBits(int i) {
        int i2;
        int i3 = this.buffer.get(this.initialPos + (this.position / 8));
        if (i3 < 0) {
            i3 += 256;
        }
        int i4 = this.position;
        int i5 = 8 - (i4 % 8);
        if (i <= i5) {
            i2 = ((i3 << (i4 % 8)) & 255) >> ((i4 % 8) + (i5 - i));
            this.position = i4 + i;
        } else {
            int i6 = i - i5;
            i2 = (readBits(i5) << i6) + readBits(i6);
        }
        this.buffer.position(this.initialPos + ((int) Math.ceil(((double) this.position) / 8.0d)));
        return i2;
    }

    public int getPosition() {
        return this.position;
    }

    public int byteSync() {
        int i = 8 - (this.position % 8);
        if (i == 8) {
            i = 0;
        }
        readBits(i);
        return i;
    }

    public int remainingBits() {
        return (this.buffer.limit() * 8) - this.position;
    }
}
