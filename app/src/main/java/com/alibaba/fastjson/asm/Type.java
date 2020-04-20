package com.alibaba.fastjson.asm;

public class Type {
    public static final Type BOOLEAN_TYPE = new Type(1, (char[]) null, 1509950721, 1);
    public static final Type BYTE_TYPE = new Type(3, (char[]) null, 1107297537, 1);
    public static final Type CHAR_TYPE = new Type(2, (char[]) null, 1124075009, 1);
    public static final Type DOUBLE_TYPE = new Type(8, (char[]) null, 1141048066, 1);
    public static final Type FLOAT_TYPE = new Type(6, (char[]) null, 1174536705, 1);
    public static final Type INT_TYPE = new Type(5, (char[]) null, 1224736769, 1);
    public static final Type LONG_TYPE = new Type(7, (char[]) null, 1241579778, 1);
    public static final Type SHORT_TYPE = new Type(4, (char[]) null, 1392510721, 1);
    public static final Type VOID_TYPE = new Type(0, (char[]) null, 1443168256, 1);
    private final char[] buf;
    private final int len;
    private final int off;
    protected final int sort;

    private Type(int i, char[] cArr, int i2, int i3) {
        this.sort = i;
        this.buf = cArr;
        this.off = i2;
        this.len = i3;
    }

    public static Type getType(String str) {
        return getType(str.toCharArray(), 0);
    }

    public static int getArgumentsAndReturnSizes(String str) {
        int i;
        int i2 = 1;
        int i3 = 1;
        int i4 = 1;
        while (true) {
            i = i3 + 1;
            char charAt = str.charAt(i3);
            if (charAt == ')') {
                break;
            } else if (charAt == 'L') {
                while (true) {
                    i3 = i + 1;
                    if (str.charAt(i) == ';') {
                        break;
                    }
                    i = i3;
                }
                i4++;
            } else {
                i4 = (charAt == 'D' || charAt == 'J') ? i4 + 2 : i4 + 1;
                i3 = i;
            }
        }
        char charAt2 = str.charAt(i);
        int i5 = i4 << 2;
        if (charAt2 == 'V') {
            i2 = 0;
        } else if (charAt2 == 'D' || charAt2 == 'J') {
            i2 = 2;
        }
        return i5 | i2;
    }

    private static Type getType(char[] cArr, int i) {
        int i2;
        char c = cArr[i];
        if (c == 'F') {
            return FLOAT_TYPE;
        }
        if (c == 'S') {
            return SHORT_TYPE;
        }
        if (c == 'V') {
            return VOID_TYPE;
        }
        if (c == 'I') {
            return INT_TYPE;
        }
        if (c == 'J') {
            return LONG_TYPE;
        }
        if (c == 'Z') {
            return BOOLEAN_TYPE;
        }
        if (c != '[') {
            switch (c) {
                case 'B':
                    return BYTE_TYPE;
                case 'C':
                    return CHAR_TYPE;
                case 'D':
                    return DOUBLE_TYPE;
                default:
                    int i3 = 1;
                    while (cArr[i + i3] != ';') {
                        i3++;
                    }
                    return new Type(10, cArr, i + 1, i3 - 1);
            }
        } else {
            int i4 = 1;
            while (true) {
                i2 = i + i4;
                if (cArr[i2] != '[') {
                    break;
                }
                i4++;
            }
            if (cArr[i2] == 'L') {
                do {
                    i4++;
                } while (cArr[i + i4] != ';');
            }
            return new Type(9, cArr, i, i4 + 1);
        }
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }

    /* access modifiers changed from: package-private */
    public String getDescriptor() {
        return new String(this.buf, this.off, this.len);
    }
}
