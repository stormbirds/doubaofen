package com.coremedia.iso;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;

public final class Utf8 {
    public static byte[] convert(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    public static String convert(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    public static int utf8StringLengthInBytes(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return str.getBytes(Key.STRING_CHARSET_NAME).length;
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException();
        }
    }
}
