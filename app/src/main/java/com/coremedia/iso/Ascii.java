package com.coremedia.iso;

import java.io.UnsupportedEncodingException;

public final class Ascii {
    public static byte[] convert(String str) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes("us-ascii");
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    public static String convert(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            return new String(bArr, "us-ascii");
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }
}
