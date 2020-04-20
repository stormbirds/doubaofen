package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.SoftReference;

public final class JSONReaderScanner extends JSONLexerBase {
    public static int BUF_INIT_LEN = 8192;
    private static final ThreadLocal<SoftReference<char[]>> BUF_REF_LOCAL = new ThreadLocal<>();
    private char[] buf;
    private int bufLength;
    private Reader reader;

    public JSONReaderScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(String str, int i) {
        this((Reader) new StringReader(str), i);
    }

    public JSONReaderScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader2) {
        this(reader2, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader2, int i) {
        super(i);
        this.reader = reader2;
        SoftReference softReference = BUF_REF_LOCAL.get();
        if (softReference != null) {
            this.buf = (char[]) softReference.get();
            BUF_REF_LOCAL.set((Object) null);
        }
        if (this.buf == null) {
            this.buf = new char[BUF_INIT_LEN];
        }
        try {
            this.bufLength = reader2.read(this.buf);
            this.bp = -1;
            next();
            if (this.ch == 65279) {
                next();
            }
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public JSONReaderScanner(char[] cArr, int i, int i2) {
        this((Reader) new CharArrayReader(cArr, 0, i), i2);
    }

    public final char charAt(int i) {
        int i2 = this.bufLength;
        if (i >= i2) {
            if (i2 == -1) {
                if (i < this.sp) {
                    return this.buf[i];
                }
                return 26;
            } else if (this.bp == 0) {
                char[] cArr = this.buf;
                char[] cArr2 = new char[((cArr.length * 3) / 2)];
                System.arraycopy(cArr, this.bp, cArr2, 0, this.bufLength);
                int length = cArr2.length;
                int i3 = this.bufLength;
                try {
                    this.bufLength += this.reader.read(cArr2, i3, length - i3);
                    this.buf = cArr2;
                } catch (IOException e) {
                    throw new JSONException(e.getMessage(), e);
                }
            } else {
                int i4 = this.bufLength - this.bp;
                if (i4 > 0) {
                    System.arraycopy(this.buf, this.bp, this.buf, 0, i4);
                }
                try {
                    this.bufLength = this.reader.read(this.buf, i4, this.buf.length - i4);
                    int i5 = this.bufLength;
                    if (i5 == 0) {
                        throw new JSONException("illegal state, textLength is zero");
                    } else if (i5 == -1) {
                        return 26;
                    } else {
                        this.bufLength = i5 + i4;
                        i -= this.bp;
                        this.np -= this.bp;
                        this.bp = 0;
                    }
                } catch (IOException e2) {
                    throw new JSONException(e2.getMessage(), e2);
                }
            }
        }
        return this.buf[i];
    }

    public final int indexOf(char c, int i) {
        int i2 = i - this.bp;
        while (c != charAt(this.bp + i2)) {
            if (c == 26) {
                return -1;
            }
            i2++;
        }
        return i2 + this.bp;
    }

    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.buf, i, i2, i3);
    }

    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        int i2 = this.bufLength;
        if (i >= i2) {
            if (i2 == -1) {
                return 26;
            }
            if (this.sp > 0) {
                int i3 = this.bufLength - this.sp;
                if (this.ch == '\"') {
                    i3--;
                }
                char[] cArr = this.buf;
                System.arraycopy(cArr, i3, cArr, 0, this.sp);
            }
            this.np = -1;
            int i4 = this.sp;
            this.bp = i4;
            try {
                int i5 = this.bp;
                int length = this.buf.length - i5;
                if (length == 0) {
                    char[] cArr2 = new char[(this.buf.length * 2)];
                    System.arraycopy(this.buf, 0, cArr2, 0, this.buf.length);
                    this.buf = cArr2;
                    length = this.buf.length - i5;
                }
                this.bufLength = this.reader.read(this.buf, this.bp, length);
                int i6 = this.bufLength;
                if (i6 == 0) {
                    throw new JSONException("illegal stat, textLength is zero");
                } else if (i6 == -1) {
                    this.ch = 26;
                    return 26;
                } else {
                    this.bufLength = i6 + this.bp;
                    i = i4;
                }
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
        char c = this.buf[i];
        this.ch = c;
        return c;
    }

    /* access modifiers changed from: protected */
    public final void copyTo(int i, int i2, char[] cArr) {
        System.arraycopy(this.buf, i, cArr, 0, i2);
    }

    public final boolean charArrayCompare(char[] cArr) {
        for (int i = 0; i < cArr.length; i++) {
            if (charAt(this.bp + i) != cArr[i]) {
                return false;
            }
        }
        return true;
    }

    public byte[] bytesValue() {
        return IOUtils.decodeFast(this.buf, this.np + 1, this.sp);
    }

    /* access modifiers changed from: protected */
    public final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        System.arraycopy(this.buf, i, cArr, i2, i3);
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return new String(this.sbuf, 0, this.sp);
        }
        int i = this.np + 1;
        if (i < 0) {
            throw new IllegalStateException();
        } else if (i <= this.buf.length - this.sp) {
            return new String(this.buf, i, this.sp);
        } else {
            throw new IllegalStateException();
        }
    }

    public final String subString(int i, int i2) {
        if (i2 >= 0) {
            return new String(this.buf, i, i2);
        }
        throw new StringIndexOutOfBoundsException(i2);
    }

    public final String numberString() {
        int i = this.np;
        if (i == -1) {
            i = 0;
        }
        char charAt = charAt((this.sp + i) - 1);
        int i2 = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i2--;
        }
        return new String(this.buf, i, i2);
    }

    public void close() {
        super.close();
        BUF_REF_LOCAL.set(new SoftReference(this.buf));
        this.buf = null;
        IOUtils.close(this.reader);
    }

    public boolean isEOF() {
        if (this.bufLength == -1 || this.bp == this.buf.length) {
            return true;
        }
        return this.ch == 26 && this.bp + 1 == this.buf.length;
    }
}
