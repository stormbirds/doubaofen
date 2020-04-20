package com.googlecode.mp4parser.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ChannelHelper {
    private static ByteBuffer empty = ByteBuffer.allocate(0).asReadOnlyBuffer();

    public static void readFully(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer) throws IOException {
        readFully(readableByteChannel, byteBuffer, byteBuffer.remaining());
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x000e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x000f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int readFully(java.nio.channels.ReadableByteChannel r3, java.nio.ByteBuffer r4, int r5) throws java.io.IOException {
        /*
            r0 = 0
        L_0x0001:
            int r1 = r3.read(r4)
            r2 = -1
            if (r2 != r1) goto L_0x0009
            goto L_0x000c
        L_0x0009:
            int r0 = r0 + r1
            if (r0 != r5) goto L_0x0001
        L_0x000c:
            if (r1 == r2) goto L_0x000f
            return r0
        L_0x000f:
            java.io.EOFException r3 = new java.io.EOFException
            java.lang.String r4 = "End of file. No more boxes."
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.util.ChannelHelper.readFully(java.nio.channels.ReadableByteChannel, java.nio.ByteBuffer, int):int");
    }
}
