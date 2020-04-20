package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class SerialWriterStringEncoder {
    private static final ThreadLocal<SoftReference<byte[]>> bytesBufLocal = new ThreadLocal<>();
    private final CharsetEncoder encoder;

    private static int scale(int i, float f) {
        return (int) (((double) i) * ((double) f));
    }

    public SerialWriterStringEncoder(Charset charset) {
        this(charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
    }

    public SerialWriterStringEncoder(CharsetEncoder charsetEncoder) {
        this.encoder = charsetEncoder;
    }

    public byte[] encode(char[] cArr, int i, int i2) {
        if (i2 == 0) {
            return new byte[0];
        }
        this.encoder.reset();
        return encode(cArr, i, i2, getBytes(scale(i2, this.encoder.maxBytesPerChar())));
    }

    public CharsetEncoder getEncoder() {
        return this.encoder;
    }

    public byte[] encode(char[] cArr, int i, int i2, byte[] bArr) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        try {
            CoderResult encode = this.encoder.encode(CharBuffer.wrap(cArr, i, i2), wrap, true);
            if (!encode.isUnderflow()) {
                encode.throwException();
            }
            CoderResult flush = this.encoder.flush(wrap);
            if (!flush.isUnderflow()) {
                flush.throwException();
            }
            int position = wrap.position();
            byte[] bArr2 = new byte[position];
            System.arraycopy(bArr, 0, bArr2, 0, position);
            return bArr2;
        } catch (CharacterCodingException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static void clearBytes() {
        bytesBufLocal.set((Object) null);
    }

    public static byte[] getBytes(int i) {
        SoftReference softReference = bytesBufLocal.get();
        if (softReference == null) {
            return allocateBytes(i);
        }
        byte[] bArr = (byte[]) softReference.get();
        if (bArr == null) {
            return allocateBytes(i);
        }
        return bArr.length < i ? allocateBytes(i) : bArr;
    }

    private static byte[] allocateBytes(int i) {
        int i2;
        if (i > 131072) {
            return new byte[i];
        }
        if ((i >>> 10) <= 0) {
            i2 = 1024;
        } else {
            i2 = 1 << (32 - Integer.numberOfLeadingZeros(i - 1));
        }
        byte[] bArr = new byte[i2];
        bytesBufLocal.set(new SoftReference(bArr));
        return bArr;
    }
}
