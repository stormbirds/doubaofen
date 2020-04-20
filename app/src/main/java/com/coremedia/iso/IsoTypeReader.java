package com.coremedia.iso;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import kotlin.UByte;

public final class IsoTypeReader {
    public static int byte2int(byte b) {
        return b < 0 ? b + UByte.MIN_VALUE : b;
    }

    public static long readUInt32BE(ByteBuffer byteBuffer) {
        return (((long) readUInt8(byteBuffer)) << 24) + (((long) readUInt8(byteBuffer)) << 16) + (((long) readUInt8(byteBuffer)) << 8) + (((long) readUInt8(byteBuffer)) << 0);
    }

    public static long readUInt32(ByteBuffer byteBuffer) {
        long j = (long) byteBuffer.getInt();
        return j < 0 ? j + 4294967296L : j;
    }

    public static int readUInt24(ByteBuffer byteBuffer) {
        return (readUInt16(byteBuffer) << 8) + 0 + byte2int(byteBuffer.get());
    }

    public static int readUInt16(ByteBuffer byteBuffer) {
        return (byte2int(byteBuffer.get()) << 8) + 0 + byte2int(byteBuffer.get());
    }

    public static int readUInt16BE(ByteBuffer byteBuffer) {
        return byte2int(byteBuffer.get()) + 0 + (byte2int(byteBuffer.get()) << 8);
    }

    public static int readUInt8(ByteBuffer byteBuffer) {
        return byte2int(byteBuffer.get());
    }

    public static String readString(ByteBuffer byteBuffer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            byte b = byteBuffer.get();
            if (b == 0) {
                return Utf8.convert(byteArrayOutputStream.toByteArray());
            }
            byteArrayOutputStream.write(b);
        }
    }

    public static String readString(ByteBuffer byteBuffer, int i) {
        byte[] bArr = new byte[i];
        byteBuffer.get(bArr);
        return Utf8.convert(bArr);
    }

    public static long readUInt64(ByteBuffer byteBuffer) {
        long readUInt32 = (readUInt32(byteBuffer) << 32) + 0;
        if (readUInt32 >= 0) {
            return readUInt32 + readUInt32(byteBuffer);
        }
        throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
    }

    public static double readFixedPoint1616(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.get(bArr);
        return ((double) ((((0 | ((bArr[0] << 24) & UByte.MIN_VALUE)) | ((bArr[1] << 16) & UByte.MIN_VALUE)) | ((bArr[2] << 8) & UByte.MIN_VALUE)) | (bArr[3] & UByte.MAX_VALUE))) / 65536.0d;
    }

    public static double readFixedPoint0230(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.get(bArr);
        return ((double) ((((0 | ((bArr[0] << 24) & UByte.MIN_VALUE)) | ((bArr[1] << 16) & UByte.MIN_VALUE)) | ((bArr[2] << 8) & UByte.MIN_VALUE)) | (bArr[3] & UByte.MAX_VALUE))) / 1.073741824E9d;
    }

    public static float readFixedPoint88(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[2];
        byteBuffer.get(bArr);
        return ((float) ((short) (((short) (0 | ((bArr[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) | (bArr[1] & UByte.MAX_VALUE)))) / 256.0f;
    }

    public static String readIso639(ByteBuffer byteBuffer) {
        int readUInt16 = readUInt16(byteBuffer);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append((char) (((readUInt16 >> ((2 - i) * 5)) & 31) + 96));
        }
        return sb.toString();
    }

    public static String read4cc(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.get(bArr);
        try {
            return new String(bArr, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static long readUInt48(ByteBuffer byteBuffer) {
        long readUInt16 = ((long) readUInt16(byteBuffer)) << 32;
        if (readUInt16 >= 0) {
            return readUInt16 + readUInt32(byteBuffer);
        }
        throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
    }
}
