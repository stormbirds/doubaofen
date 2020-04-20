package com.alibaba.fastjson.util;

import com.bumptech.glide.load.Key;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;

public class UTF8Decoder extends CharsetDecoder {
    private static final Charset charset = Charset.forName(Key.STRING_CHARSET_NAME);

    public UTF8Decoder() {
        super(charset, 1.0f, 1.0f);
    }

    public static CoderResult malformedN(ByteBuffer byteBuffer, int i) {
        int i2 = 1;
        if (i == 1) {
            byte b = byteBuffer.get();
            if ((b >> 2) == -2) {
                if (byteBuffer.remaining() < 4) {
                    return CoderResult.UNDERFLOW;
                }
                while (i2 < 5) {
                    if ((byteBuffer.get() & 192) != 128) {
                        return CoderResult.malformedForLength(i2);
                    }
                    i2++;
                }
                return CoderResult.malformedForLength(5);
            } else if ((b >> 1) != -2) {
                return CoderResult.malformedForLength(1);
            } else {
                if (byteBuffer.remaining() < 5) {
                    return CoderResult.UNDERFLOW;
                }
                while (i2 < 6) {
                    if ((byteBuffer.get() & 192) != 128) {
                        return CoderResult.malformedForLength(i2);
                    }
                    i2++;
                }
                return CoderResult.malformedForLength(6);
            }
        } else if (i == 2) {
            return CoderResult.malformedForLength(1);
        } else {
            if (i == 3) {
                byte b2 = byteBuffer.get();
                byte b3 = byteBuffer.get();
                if (!(b2 == -32 && (b3 & 224) == 128) && (b3 & 192) == 128) {
                    i2 = 2;
                }
                return CoderResult.malformedForLength(i2);
            } else if (i == 4) {
                byte b4 = byteBuffer.get() & UByte.MAX_VALUE;
                byte b5 = byteBuffer.get() & UByte.MAX_VALUE;
                if (b4 > 244 || ((b4 == 240 && (b5 < 144 || b5 > 191)) || ((b4 == 244 && (b5 & 240) != 128) || (b5 & 192) != 128))) {
                    return CoderResult.malformedForLength(1);
                }
                if ((byteBuffer.get() & 192) != 128) {
                    return CoderResult.malformedForLength(2);
                }
                return CoderResult.malformedForLength(3);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private static CoderResult malformed(ByteBuffer byteBuffer, int i, CharBuffer charBuffer, int i2, int i3) {
        byteBuffer.position(i - byteBuffer.arrayOffset());
        CoderResult malformedN = malformedN(byteBuffer, i3);
        byteBuffer.position(i);
        charBuffer.position(i2);
        return malformedN;
    }

    private static CoderResult xflow(Buffer buffer, int i, int i2, Buffer buffer2, int i3, int i4) {
        buffer.position(i);
        buffer2.position(i3);
        return (i4 == 0 || i2 - i < i4) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
    }

    /* access modifiers changed from: protected */
    public CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        int i;
        int i2;
        int i3;
        ByteBuffer byteBuffer2 = byteBuffer;
        CharBuffer charBuffer2 = charBuffer;
        byte[] array = byteBuffer.array();
        int arrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
        int arrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
        char[] array2 = charBuffer.array();
        int arrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
        int arrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
        int min = Math.min(arrayOffset2 - arrayOffset, arrayOffset4 - arrayOffset3) + arrayOffset3;
        while (i < min && array[i2] >= 0) {
            array2[i] = (char) array[i2];
            arrayOffset3 = i + 1;
            arrayOffset = i2 + 1;
        }
        while (i2 < arrayOffset2) {
            byte b = array[i2];
            if (b < 0) {
                boolean z = true;
                if ((b >> 5) == -2) {
                    if (arrayOffset2 - i2 < 2 || i >= arrayOffset4) {
                        return xflow(byteBuffer, i2, arrayOffset2, charBuffer, i, 2);
                    }
                    byte b2 = array[i2 + 1];
                    if ((b & 30) != 0 && (b2 & 192) == 128) {
                        z = false;
                    }
                    if (z) {
                        return malformed(byteBuffer2, i2, charBuffer2, i, 2);
                    }
                    i3 = i + 1;
                    array2[i] = (char) (((b << 6) ^ b2) ^ ByteCompanionObject.MIN_VALUE);
                    i2 += 2;
                } else if ((b >> 4) == -2) {
                    if (arrayOffset2 - i2 < 3 || i >= arrayOffset4) {
                        return xflow(byteBuffer, i2, arrayOffset2, charBuffer, i, 3);
                    }
                    byte b3 = array[i2 + 1];
                    byte b4 = array[i2 + 2];
                    if (!(b == -32 && (b3 & 224) == 128) && (b3 & 192) == 128 && (b4 & 192) == 128) {
                        z = false;
                    }
                    if (z) {
                        return malformed(byteBuffer2, i2, charBuffer2, i, 3);
                    }
                    i3 = i + 1;
                    array2[i] = (char) ((((b << 12) ^ (b3 << 6)) ^ b4) ^ ByteCompanionObject.MIN_VALUE);
                    i2 += 3;
                } else if ((b >> 3) != -2) {
                    return malformed(byteBuffer2, i2, charBuffer2, i, 1);
                } else {
                    if (arrayOffset2 - i2 < 4 || arrayOffset4 - i < 2) {
                        return xflow(byteBuffer, i2, arrayOffset2, charBuffer, i, 4);
                    }
                    byte b5 = array[i2 + 1];
                    byte b6 = array[i2 + 2];
                    byte b7 = array[i2 + 3];
                    byte b8 = ((b & 7) << 18) | ((b5 & 63) << 12) | ((b6 & 63) << 6) | (b7 & 63);
                    if ((b5 & 192) == 128 && (b6 & 192) == 128 && (b7 & 192) == 128) {
                        z = false;
                    }
                    if (z || b8 < 65536 || b8 > 1114111) {
                        return malformed(byteBuffer2, i2, charBuffer2, i, 4);
                    }
                    int i4 = i + 1;
                    int i5 = b8 - UByte.MIN_VALUE;
                    array2[i] = (char) (((i5 >> 10) & 1023) | 55296);
                    i = i4 + 1;
                    array2[i4] = (char) ((i5 & 1023) | 56320);
                    i2 += 4;
                }
                i = i3;
            } else if (i >= arrayOffset4) {
                return xflow(byteBuffer, i2, arrayOffset2, charBuffer, i, 1);
            } else {
                array2[i] = (char) b;
                i2++;
                i++;
            }
        }
        return xflow(byteBuffer, i2, arrayOffset2, charBuffer, i, 0);
    }
}
