package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.text.Typography;

public abstract class JSONLexerBase implements JSONLexer, Closeable {
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    private static final ThreadLocal<SoftReference<char[]>> SBUF_REF_LOCAL = new ThreadLocal<>();
    protected static final int[] digits = new int[103];
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected int bp;
    protected Calendar calendar = null;
    protected char ch;
    protected int eofPos;
    protected int features;
    protected boolean hasSpecial;
    protected Locale locale = JSON.defaultLocale;
    public int matchStat = 0;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    private final SoftReference<char[]> sbufRef;
    protected int sp;
    protected String stringDefaultValue = null;
    protected TimeZone timeZone = JSON.defaultTimeZone;
    protected int token;

    public static boolean isWhitespace(char c) {
        return c <= ' ' && (c == ' ' || c == 10 || c == 13 || c == 9 || c == 12 || c == 8);
    }

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    /* access modifiers changed from: protected */
    public abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    public abstract byte[] bytesValue();

    /* access modifiers changed from: protected */
    public abstract boolean charArrayCompare(char[] cArr);

    public abstract char charAt(int i);

    /* access modifiers changed from: protected */
    public abstract void copyTo(int i, int i2, char[] cArr);

    public abstract int indexOf(char c, int i);

    public String info() {
        return "";
    }

    public abstract boolean isEOF();

    public abstract char next();

    public abstract String numberString();

    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    /* access modifiers changed from: protected */
    public void lexError(String str, Object... objArr) {
        this.token = 1;
    }

    static {
        for (int i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (int i2 = 97; i2 <= 102; i2++) {
            digits[i2] = (i2 - 97) + 10;
        }
        for (int i3 = 65; i3 <= 70; i3++) {
            digits[i3] = (i3 - 65) + 10;
        }
    }

    public JSONLexerBase(int i) {
        this.features = i;
        if ((i & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
        this.sbufRef = SBUF_REF_LOCAL.get();
        SoftReference<char[]> softReference = this.sbufRef;
        if (softReference != null) {
            this.sbuf = softReference.get();
            SBUF_REF_LOCAL.set((Object) null);
        }
        if (this.sbuf == null) {
            this.sbuf = new char[256];
        }
    }

    public final int matchStat() {
        return this.matchStat;
    }

    public final void nextToken() {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            char c = this.ch;
            if (c == '/') {
                skipComment();
            } else if (c == '\"') {
                scanString();
                return;
            } else if (c == ',') {
                next();
                this.token = 16;
                return;
            } else if (c < '0' || c > '9') {
                char c2 = this.ch;
                if (c2 == '-') {
                    scanNumber();
                    return;
                }
                if (!(c2 == 12 || c2 == 13 || c2 == ' ')) {
                    if (c2 == ':') {
                        next();
                        this.token = 17;
                        return;
                    } else if (c2 == 'N') {
                        scanNULL();
                        return;
                    } else if (c2 == '[') {
                        next();
                        this.token = 14;
                        return;
                    } else if (c2 == ']') {
                        next();
                        this.token = 15;
                        return;
                    } else if (c2 == 'f') {
                        scanFalse();
                        return;
                    } else if (c2 == 'n') {
                        scanNullOrNew();
                        return;
                    } else if (c2 == '{') {
                        next();
                        this.token = 12;
                        return;
                    } else if (c2 == '}') {
                        next();
                        this.token = 13;
                        return;
                    } else if (c2 == 'S') {
                        scanSet();
                        return;
                    } else if (c2 == 'T') {
                        scanTreeSet();
                        return;
                    } else if (c2 == 't') {
                        scanTrue();
                        return;
                    } else if (c2 != 'u') {
                        switch (c2) {
                            case 8:
                            case 9:
                            case 10:
                                break;
                            default:
                                switch (c2) {
                                    case '\'':
                                        if (isEnabled(Feature.AllowSingleQuotes)) {
                                            scanStringSingleQuote();
                                            return;
                                        }
                                        throw new JSONException("Feature.AllowSingleQuotes is false");
                                    case '(':
                                        next();
                                        this.token = 10;
                                        return;
                                    case ')':
                                        next();
                                        this.token = 11;
                                        return;
                                    default:
                                        if (!isEOF()) {
                                            char c3 = this.ch;
                                            if (c3 <= 31 || c3 == 127) {
                                                next();
                                                continue;
                                            } else {
                                                lexError("illegal.char", String.valueOf(c3));
                                                next();
                                                return;
                                            }
                                        } else if (this.token != 20) {
                                            this.token = 20;
                                            int i = this.eofPos;
                                            this.bp = i;
                                            this.pos = i;
                                            return;
                                        } else {
                                            throw new JSONException("EOF error");
                                        }
                                }
                        }
                    } else {
                        scanUndefined();
                        return;
                    }
                }
                next();
            } else {
                scanNumber();
                return;
            }
        }
    }

    public final void nextToken(int i) {
        this.sp = 0;
        while (true) {
            if (i == 2) {
                char c = this.ch;
                if (c < '0' || c > '9') {
                    char c2 = this.ch;
                    if (c2 == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (c2 == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (c2 == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                } else {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                }
            } else if (i == 4) {
                char c3 = this.ch;
                if (c3 == '\"') {
                    this.pos = this.bp;
                    scanString();
                    return;
                } else if (c3 < '0' || c3 > '9') {
                    char c4 = this.ch;
                    if (c4 == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (c4 == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                } else {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                }
            } else if (i == 12) {
                char c5 = this.ch;
                if (c5 == '{') {
                    this.token = 12;
                    next();
                    return;
                } else if (c5 == '[') {
                    this.token = 14;
                    next();
                    return;
                }
            } else if (i != 18) {
                if (i != 20) {
                    switch (i) {
                        case 14:
                            char c6 = this.ch;
                            if (c6 == '[') {
                                this.token = 14;
                                next();
                                return;
                            } else if (c6 == '{') {
                                this.token = 12;
                                next();
                                return;
                            }
                            break;
                        case 15:
                            if (this.ch == ']') {
                                this.token = 15;
                                next();
                                return;
                            }
                            break;
                        case 16:
                            char c7 = this.ch;
                            if (c7 == ',') {
                                this.token = 16;
                                next();
                                return;
                            } else if (c7 == '}') {
                                this.token = 13;
                                next();
                                return;
                            } else if (c7 == ']') {
                                this.token = 15;
                                next();
                                return;
                            } else if (c7 == 26) {
                                this.token = 20;
                                return;
                            }
                            break;
                    }
                }
                if (this.ch == 26) {
                    this.token = 20;
                    return;
                }
            } else {
                nextIdent();
                return;
            }
            char c8 = this.ch;
            if (c8 == ' ' || c8 == 10 || c8 == 13 || c8 == 9 || c8 == 12 || c8 == 8) {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        while (isWhitespace(this.ch)) {
            next();
        }
        char c = this.ch;
        if (c == '_' || Character.isLetter(c)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char c) {
        this.sp = 0;
        while (true) {
            char c2 = this.ch;
            if (c2 == c) {
                next();
                nextToken();
                return;
            } else if (c2 == ' ' || c2 == 10 || c2 == 13 || c2 == 9 || c2 == 12 || c2 == 8) {
                next();
            } else {
                throw new JSONException("not match " + c + " - " + this.ch);
            }
        }
    }

    public final int token() {
        return this.token;
    }

    public final String tokenName() {
        return JSONToken.name(this.token);
    }

    public final int pos() {
        return this.pos;
    }

    public final int getBufferPosition() {
        return this.bp;
    }

    public final String stringDefaultValue() {
        return this.stringDefaultValue;
    }

    public final Number integerValue() throws NumberFormatException {
        long j;
        int i;
        long j2;
        boolean z = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i2 = this.np;
        int i3 = this.sp + i2;
        char c = ' ';
        char charAt = charAt(i3 - 1);
        if (charAt == 'B') {
            i3--;
            c = 'B';
        } else if (charAt == 'L') {
            i3--;
            c = 'L';
        } else if (charAt == 'S') {
            i3--;
            c = 'S';
        }
        if (charAt(this.np) == '-') {
            j = Long.MIN_VALUE;
            i2++;
            z = true;
        } else {
            j = -9223372036854775807L;
        }
        long j3 = MULTMIN_RADIX_TEN;
        if (i2 < i3) {
            i = i2 + 1;
            j2 = (long) (-digits[charAt(i2)]);
        } else {
            j2 = 0;
            i = i2;
        }
        while (i < i3) {
            int i4 = i + 1;
            int i5 = digits[charAt(i)];
            if (j2 < j3) {
                return new BigInteger(numberString());
            }
            long j4 = j2 * 10;
            long j5 = (long) i5;
            if (j4 < j + j5) {
                return new BigInteger(numberString());
            }
            j2 = j4 - j5;
            i = i4;
            j3 = MULTMIN_RADIX_TEN;
        }
        if (!z) {
            long j6 = -j2;
            if (j6 > 2147483647L || c == 'L') {
                return Long.valueOf(j6);
            }
            if (c == 'S') {
                return Short.valueOf((short) ((int) j6));
            }
            if (c == 'B') {
                return Byte.valueOf((byte) ((int) j6));
            }
            return Integer.valueOf((int) j6);
        } else if (i <= this.np + 1) {
            throw new NumberFormatException(numberString());
        } else if (j2 < -2147483648L || c == 'L') {
            return Long.valueOf(j2);
        } else {
            if (c == 'S') {
                return Short.valueOf((short) ((int) j2));
            }
            if (c == 'B') {
                return Byte.valueOf((byte) ((int) j2));
            }
            return Integer.valueOf((int) j2);
        }
    }

    public final void nextTokenWithColon(int i) {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithComma(int i) {
        nextTokenWithChar(',');
    }

    public float floatValue() {
        return Float.parseFloat(numberString());
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public void config(Feature feature, boolean z) {
        this.features = Feature.config(this.features, feature, z);
        if ((this.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
    }

    public final boolean isEnabled(Feature feature) {
        return (feature.mask & this.features) != 0;
    }

    public final char getCurrent() {
        return this.ch;
    }

    /* access modifiers changed from: protected */
    public void skipComment() {
        next();
        char c = this.ch;
        if (c == '/') {
            do {
                next();
            } while (this.ch != 10);
            next();
        } else if (c == '*') {
            while (true) {
                next();
                if (this.ch == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                }
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        char c = this.ch;
        if (c == '\"') {
            return scanSymbol(symbolTable, Typography.quote);
        }
        if (c == '\'') {
            if (isEnabled(Feature.AllowSingleQuotes)) {
                return scanSymbol(symbolTable, '\'');
            }
            throw new JSONException("syntax error");
        } else if (c == '}') {
            next();
            this.token = 13;
            return null;
        } else if (c == ',') {
            next();
            this.token = 16;
            return null;
        } else if (c == 26) {
            this.token = 20;
            return null;
        } else if (isEnabled(Feature.AllowUnQuotedFieldNames)) {
            return scanSymbolUnQuoted(symbolTable);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public final String scanSymbol(SymbolTable symbolTable, char c) {
        String str;
        this.np = this.bp;
        this.sp = 0;
        boolean z = false;
        int i = 0;
        while (true) {
            char next = next();
            if (next == c) {
                this.token = 4;
                if (!z) {
                    int i2 = this.np;
                    str = addSymbol(i2 == -1 ? 0 : i2 + 1, this.sp, i, symbolTable);
                } else {
                    str = symbolTable.addSymbol(this.sbuf, 0, this.sp, i);
                }
                this.sp = 0;
                next();
                return str;
            } else if (next == 26) {
                throw new JSONException("unclosed.str");
            } else if (next == '\\') {
                if (!z) {
                    int i3 = this.sp;
                    char[] cArr = this.sbuf;
                    if (i3 >= cArr.length) {
                        int length = cArr.length * 2;
                        if (i3 <= length) {
                            i3 = length;
                        }
                        char[] cArr2 = new char[i3];
                        char[] cArr3 = this.sbuf;
                        System.arraycopy(cArr3, 0, cArr2, 0, cArr3.length);
                        this.sbuf = cArr2;
                    }
                    arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                    z = true;
                }
                char next2 = next();
                if (next2 == '\"') {
                    i = (i * 31) + 34;
                    putChar(Typography.quote);
                } else if (next2 != '\'') {
                    if (next2 != 'F') {
                        if (next2 == '\\') {
                            i = (i * 31) + 92;
                            putChar('\\');
                        } else if (next2 == 'b') {
                            i = (i * 31) + 8;
                            putChar(8);
                        } else if (next2 != 'f') {
                            if (next2 == 'n') {
                                i = (i * 31) + 10;
                                putChar(10);
                            } else if (next2 == 'r') {
                                i = (i * 31) + 13;
                                putChar(13);
                            } else if (next2 != 'x') {
                                switch (next2) {
                                    case '/':
                                        i = (i * 31) + 47;
                                        putChar('/');
                                        break;
                                    case '0':
                                        i = (i * 31) + next2;
                                        putChar(0);
                                        break;
                                    case '1':
                                        i = (i * 31) + next2;
                                        putChar(1);
                                        break;
                                    case '2':
                                        i = (i * 31) + next2;
                                        putChar(2);
                                        break;
                                    case '3':
                                        i = (i * 31) + next2;
                                        putChar(3);
                                        break;
                                    case '4':
                                        i = (i * 31) + next2;
                                        putChar(4);
                                        break;
                                    case '5':
                                        i = (i * 31) + next2;
                                        putChar(5);
                                        break;
                                    case '6':
                                        i = (i * 31) + next2;
                                        putChar(6);
                                        break;
                                    case '7':
                                        i = (i * 31) + next2;
                                        putChar(7);
                                        break;
                                    default:
                                        switch (next2) {
                                            case 't':
                                                i = (i * 31) + 9;
                                                putChar(9);
                                                break;
                                            case 'u':
                                                int parseInt = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                                                i = (i * 31) + parseInt;
                                                putChar((char) parseInt);
                                                break;
                                            case 'v':
                                                i = (i * 31) + 11;
                                                putChar(11);
                                                break;
                                            default:
                                                this.ch = next2;
                                                throw new JSONException("unclosed.str.lit");
                                        }
                                }
                            } else {
                                char next3 = next();
                                this.ch = next3;
                                char next4 = next();
                                this.ch = next4;
                                int[] iArr = digits;
                                char c2 = (char) ((iArr[next3] * 16) + iArr[next4]);
                                i = (i * 31) + c2;
                                putChar(c2);
                            }
                        }
                    }
                    i = (i * 31) + 12;
                    putChar(12);
                } else {
                    i = (i * 31) + 39;
                    putChar('\'');
                }
            } else {
                i = (i * 31) + next;
                if (!z) {
                    this.sp++;
                } else {
                    int i4 = this.sp;
                    char[] cArr4 = this.sbuf;
                    if (i4 == cArr4.length) {
                        putChar(next);
                    } else {
                        this.sp = i4 + 1;
                        cArr4[i4] = next;
                    }
                }
            }
        }
    }

    public final void resetStringPosition() {
        this.sp = 0;
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        boolean[] zArr = IOUtils.firstIdentifierFlags;
        int i = this.ch;
        if (i >= zArr.length || zArr[i]) {
            boolean[] zArr2 = IOUtils.identifierFlags;
            this.np = this.bp;
            this.sp = 1;
            while (true) {
                char next = next();
                if (next < zArr2.length && !zArr2[next]) {
                    break;
                }
                i = (i * 31) + next;
                this.sp++;
            }
            this.ch = charAt(this.bp);
            this.token = 18;
            if (this.sp == 4 && i == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
                return null;
            }
            return addSymbol(this.np, this.sp, i, symbolTable);
        }
        throw new JSONException("illegal identifier : " + this.ch + info());
    }

    public final void scanString() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next = next();
            if (next == '\"') {
                this.token = 4;
                this.ch = next();
                return;
            } else if (next == 26) {
                if (!isEOF()) {
                    putChar(26);
                } else {
                    throw new JSONException("unclosed string : " + next);
                }
            } else if (next == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    int i = this.sp;
                    char[] cArr = this.sbuf;
                    if (i >= cArr.length) {
                        int length = cArr.length * 2;
                        if (i <= length) {
                            i = length;
                        }
                        char[] cArr2 = new char[i];
                        char[] cArr3 = this.sbuf;
                        System.arraycopy(cArr3, 0, cArr2, 0, cArr3.length);
                        this.sbuf = cArr2;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                char next2 = next();
                if (next2 == '\"') {
                    putChar(Typography.quote);
                } else if (next2 != '\'') {
                    if (next2 != 'F') {
                        if (next2 == '\\') {
                            putChar('\\');
                        } else if (next2 == 'b') {
                            putChar(8);
                        } else if (next2 != 'f') {
                            if (next2 == 'n') {
                                putChar(10);
                            } else if (next2 == 'r') {
                                putChar(13);
                            } else if (next2 != 'x') {
                                switch (next2) {
                                    case '/':
                                        putChar('/');
                                        break;
                                    case '0':
                                        putChar(0);
                                        break;
                                    case '1':
                                        putChar(1);
                                        break;
                                    case '2':
                                        putChar(2);
                                        break;
                                    case '3':
                                        putChar(3);
                                        break;
                                    case '4':
                                        putChar(4);
                                        break;
                                    case '5':
                                        putChar(5);
                                        break;
                                    case '6':
                                        putChar(6);
                                        break;
                                    case '7':
                                        putChar(7);
                                        break;
                                    default:
                                        switch (next2) {
                                            case 't':
                                                putChar(9);
                                                break;
                                            case 'u':
                                                putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                                                break;
                                            case 'v':
                                                putChar(11);
                                                break;
                                            default:
                                                this.ch = next2;
                                                throw new JSONException("unclosed string : " + next2);
                                        }
                                }
                            } else {
                                char next3 = next();
                                char next4 = next();
                                int[] iArr = digits;
                                putChar((char) ((iArr[next3] * 16) + iArr[next4]));
                            }
                        }
                    }
                    putChar(12);
                } else {
                    putChar('\'');
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else {
                int i2 = this.sp;
                char[] cArr4 = this.sbuf;
                if (i2 == cArr4.length) {
                    putChar(next);
                } else {
                    this.sp = i2 + 1;
                    cArr4[i2] = next;
                }
            }
        }
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(TimeZone timeZone2) {
        this.timeZone = timeZone2;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale2) {
        this.locale = locale2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0038  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int intValue() {
        /*
            r12 = this;
            int r0 = r12.np
            r1 = 0
            r2 = -1
            if (r0 != r2) goto L_0x0008
            r12.np = r1
        L_0x0008:
            int r0 = r12.np
            int r2 = r12.sp
            int r2 = r2 + r0
            char r3 = r12.charAt(r0)
            r4 = 45
            r5 = 1
            if (r3 != r4) goto L_0x001e
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            int r0 = r0 + 1
            r3 = 1
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            goto L_0x0025
        L_0x001e:
            r3 = -2147483647(0xffffffff80000001, float:-1.4E-45)
            r3 = 0
            r4 = -2147483647(0xffffffff80000001, float:-1.4E-45)
        L_0x0025:
            r6 = -214748364(0xfffffffff3333334, double:NaN)
            if (r0 >= r2) goto L_0x0036
            int[] r1 = digits
            int r8 = r0 + 1
            char r0 = r12.charAt(r0)
            r0 = r1[r0]
            int r1 = -r0
        L_0x0035:
            r0 = r8
        L_0x0036:
            if (r0 >= r2) goto L_0x0071
            int r8 = r0 + 1
            char r0 = r12.charAt(r0)
            r9 = 76
            if (r0 == r9) goto L_0x0070
            r9 = 83
            if (r0 == r9) goto L_0x0070
            r9 = 66
            if (r0 != r9) goto L_0x004b
            goto L_0x0070
        L_0x004b:
            int[] r9 = digits
            r0 = r9[r0]
            long r9 = (long) r1
            int r11 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1))
            if (r11 < 0) goto L_0x0066
            int r1 = r1 * 10
            int r9 = r4 + r0
            if (r1 < r9) goto L_0x005c
            int r1 = r1 - r0
            goto L_0x0035
        L_0x005c:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r12.numberString()
            r0.<init>(r1)
            throw r0
        L_0x0066:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r12.numberString()
            r0.<init>(r1)
            throw r0
        L_0x0070:
            r0 = r8
        L_0x0071:
            if (r3 == 0) goto L_0x0083
            int r2 = r12.np
            int r2 = r2 + r5
            if (r0 <= r2) goto L_0x0079
            return r1
        L_0x0079:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r12.numberString()
            r0.<init>(r1)
            throw r0
        L_0x0083:
            int r0 = -r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.intValue():int");
    }

    public void close() {
        SoftReference<char[]> softReference;
        if (this.sbuf.length <= 8192) {
            SoftReference<char[]> softReference2 = this.sbufRef;
            if (softReference2 == null || softReference2.get() != this.sbuf) {
                softReference = new SoftReference<>(this.sbuf);
            } else {
                softReference = this.sbufRef;
            }
            SBUF_REF_LOCAL.set(softReference);
        }
        this.sbuf = null;
    }

    public final boolean isRef() {
        if (this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f') {
            return true;
        }
        return false;
    }

    public final int scanType(String str) {
        this.matchStat = 0;
        if (!charArrayCompare(typeFieldName)) {
            return -2;
        }
        int length = this.bp + typeFieldName.length;
        int length2 = str.length();
        for (int i = 0; i < length2; i++) {
            if (str.charAt(i) != charAt(length + i)) {
                return -1;
            }
        }
        int i2 = length + length2;
        if (charAt(i2) != '\"') {
            return -1;
        }
        int i3 = i2 + 1;
        this.ch = charAt(i3);
        char c = this.ch;
        if (c == ',') {
            int i4 = i3 + 1;
            this.ch = charAt(i4);
            this.bp = i4;
            this.token = 16;
            return 3;
        }
        if (c == '}') {
            i3++;
            this.ch = charAt(i3);
            char c2 = this.ch;
            if (c2 == ',') {
                this.token = 16;
                i3++;
                this.ch = charAt(i3);
            } else if (c2 == ']') {
                this.token = 15;
                i3++;
                this.ch = charAt(i3);
            } else if (c2 == '}') {
                this.token = 13;
                i3++;
                this.ch = charAt(i3);
            } else if (c2 != 26) {
                return -1;
            } else {
                this.token = 20;
            }
            this.matchStat = 4;
        }
        this.bp = i3;
        return this.matchStat;
    }

    public final boolean matchField(char[] cArr) {
        if (!charArrayCompare(cArr)) {
            return false;
        }
        this.bp += cArr.length;
        this.ch = charAt(this.bp);
        char c = this.ch;
        if (c == '{') {
            next();
            this.token = 12;
        } else if (c == '[') {
            next();
            this.token = 14;
        } else if (c == 'S' && charAt(this.bp + 1) == 'e' && charAt(this.bp + 2) == 't' && charAt(this.bp + 3) == '[') {
            this.bp += 3;
            this.ch = charAt(this.bp);
            this.token = 21;
        } else {
            nextToken();
        }
        return true;
    }

    public String scanFieldString(char[] cArr) {
        boolean z = false;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int indexOf = indexOf(Typography.quote, this.bp + cArr.length + 1);
        if (indexOf != -1) {
            int length2 = this.bp + cArr.length + 1;
            String subString = subString(length2, indexOf - length2);
            int length3 = this.bp + cArr.length + 1;
            while (true) {
                if (length3 >= indexOf) {
                    break;
                } else if (charAt(length3) == '\\') {
                    z = true;
                    break;
                } else {
                    length3++;
                }
            }
            if (z) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            int i2 = this.bp;
            int length4 = i + (indexOf - ((cArr.length + i2) + 1)) + 1;
            int i3 = length4 + 1;
            char charAt = charAt(i2 + length4);
            if (charAt == ',') {
                this.bp += i3 - 1;
                next();
                this.matchStat = 3;
                return subString;
            } else if (charAt == '}') {
                int i4 = i3 + 1;
                char charAt2 = charAt(this.bp + i3);
                if (charAt2 == ',') {
                    this.token = 16;
                    this.bp += i4 - 1;
                    next();
                } else if (charAt2 == ']') {
                    this.token = 15;
                    this.bp += i4 - 1;
                    next();
                } else if (charAt2 == '}') {
                    this.token = 13;
                    this.bp += i4 - 1;
                    next();
                } else if (charAt2 == 26) {
                    this.token = 20;
                    this.bp += i4 - 1;
                    this.ch = 26;
                } else {
                    this.matchStat = -1;
                    return stringDefaultValue();
                }
                this.matchStat = 4;
                return subString;
            } else {
                this.matchStat = -1;
                return stringDefaultValue();
            }
        } else {
            throw new JSONException("unclosed str");
        }
    }

    public String scanString(char c) {
        boolean z = false;
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            } else if (charAt(this.bp + 4) == c) {
                this.bp += 4;
                next();
                this.matchStat = 3;
                return null;
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (charAt != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        } else {
            int i = this.bp + 1;
            int indexOf = indexOf(Typography.quote, i);
            if (indexOf != -1) {
                String subString = subString(this.bp + 1, indexOf - i);
                int i2 = this.bp + 1;
                while (true) {
                    if (i2 >= indexOf) {
                        break;
                    } else if (charAt(i2) == '\\') {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (z) {
                    this.matchStat = -1;
                    return stringDefaultValue();
                }
                int i3 = this.bp;
                int i4 = (indexOf - (i3 + 1)) + 1 + 1;
                int i5 = i4 + 1;
                if (charAt(i3 + i4) == c) {
                    this.bp += i5 - 1;
                    next();
                    this.matchStat = 3;
                    return subString;
                }
                this.matchStat = -1;
                return subString;
            }
            throw new JSONException("unclosed str");
        }
    }

    public String scanFieldSymbol(char[] cArr, SymbolTable symbolTable) {
        int i = 0;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i2 = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return null;
        }
        while (true) {
            int i3 = i2 + 1;
            char charAt = charAt(this.bp + i2);
            if (charAt == '\"') {
                int i4 = this.bp;
                int length2 = cArr.length + i4 + 1;
                String addSymbol = addSymbol(length2, ((i4 + i3) - length2) - 1, i, symbolTable);
                int i5 = i3 + 1;
                char charAt2 = charAt(this.bp + i3);
                if (charAt2 == ',') {
                    this.bp += i5 - 1;
                    next();
                    this.matchStat = 3;
                    return addSymbol;
                } else if (charAt2 == '}') {
                    int i6 = i5 + 1;
                    char charAt3 = charAt(this.bp + i5);
                    if (charAt3 == ',') {
                        this.token = 16;
                        this.bp += i6 - 1;
                        next();
                    } else if (charAt3 == ']') {
                        this.token = 15;
                        this.bp += i6 - 1;
                        next();
                    } else if (charAt3 == '}') {
                        this.token = 13;
                        this.bp += i6 - 1;
                        next();
                    } else if (charAt3 == 26) {
                        this.token = 20;
                        this.bp += i6 - 1;
                        this.ch = 26;
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                    this.matchStat = 4;
                    return addSymbol;
                } else {
                    this.matchStat = -1;
                    return null;
                }
            } else {
                i = (i * 31) + charAt;
                if (charAt == '\\') {
                    this.matchStat = -1;
                    return null;
                }
                i2 = i3;
            }
        }
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Enum<?> scanEnum(java.lang.Class<?> r1, com.alibaba.fastjson.parser.SymbolTable r2, char r3) {
        /*
            r0 = this;
            java.lang.String r2 = r0.scanSymbolWithSeperator(r2, r3)
            if (r2 != 0) goto L_0x0008
            r1 = 0
            return r1
        L_0x0008:
            java.lang.Enum r1 = java.lang.Enum.valueOf(r1, r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanEnum(java.lang.Class, com.alibaba.fastjson.parser.SymbolTable, char):java.lang.Enum");
    }

    public String scanSymbolWithSeperator(SymbolTable symbolTable, char c) {
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            } else if (charAt(this.bp + 4) == c) {
                this.bp += 4;
                next();
                this.matchStat = 3;
                return null;
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (charAt != '\"') {
            this.matchStat = -1;
            return null;
        } else {
            int i = 1;
            int i2 = 0;
            while (true) {
                int i3 = i + 1;
                char charAt2 = charAt(this.bp + i);
                if (charAt2 == '\"') {
                    int i4 = this.bp;
                    int i5 = i4 + 0 + 1;
                    String addSymbol = addSymbol(i5, ((i4 + i3) - i5) - 1, i2, symbolTable);
                    int i6 = i3 + 1;
                    if (charAt(this.bp + i3) == c) {
                        this.bp += i6 - 1;
                        next();
                        this.matchStat = 3;
                        return addSymbol;
                    }
                    this.matchStat = -1;
                    return addSymbol;
                }
                i2 = (i2 * 31) + charAt2;
                if (charAt2 == '\\') {
                    this.matchStat = -1;
                    return null;
                }
                i = i3;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005e, code lost:
        r8 = r7.bp;
        r0 = r0 + r8;
        r9.add(subString(r0, ((r8 + r5) - r0) - 1));
        r0 = r5 + 1;
        r8 = charAt(r7.bp + r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0077, code lost:
        if (r8 != ',') goto L_0x0084;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0079, code lost:
        r8 = charAt(r7.bp + r0);
        r0 = r0 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0086, code lost:
        if (r8 != ']') goto L_0x00fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0088, code lost:
        r5 = r0 + 1;
        r8 = charAt(r7.bp + r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0091, code lost:
        if (r8 != ',') goto L_0x00a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0093, code lost:
        r7.bp += r5 - 1;
        next();
        r7.matchStat = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a0, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a3, code lost:
        if (r8 != '}') goto L_0x00f9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a5, code lost:
        r6 = r5 + 1;
        r8 = charAt(r7.bp + r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00ae, code lost:
        if (r8 != ',') goto L_0x00bf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00b0, code lost:
        r7.token = 16;
        r7.bp += r6 - 1;
        next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00bf, code lost:
        if (r8 != ']') goto L_0x00d0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c1, code lost:
        r7.token = 15;
        r7.bp += r6 - 1;
        next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00d0, code lost:
        if (r8 != '}') goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00d2, code lost:
        r7.token = 13;
        r7.bp += r6 - 1;
        next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00e3, code lost:
        if (r8 != 26) goto L_0x00f6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00e5, code lost:
        r7.bp += r6 - 1;
        r7.token = 20;
        r7.ch = 26;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00f2, code lost:
        r7.matchStat = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00f5, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00f6, code lost:
        r7.matchStat = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f8, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00f9, code lost:
        r7.matchStat = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00fb, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00fc, code lost:
        r7.matchStat = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00fe, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r8, java.lang.Class<?> r9) {
        /*
            r7 = this;
            r0 = 0
            r7.matchStat = r0
            boolean r0 = r7.charArrayCompare(r8)
            r1 = 0
            if (r0 != 0) goto L_0x000e
            r8 = -2
            r7.matchStat = r8
            return r1
        L_0x000e:
            java.lang.Class<java.util.HashSet> r0 = java.util.HashSet.class
            boolean r0 = r9.isAssignableFrom(r0)
            if (r0 == 0) goto L_0x001c
            java.util.HashSet r9 = new java.util.HashSet
            r9.<init>()
            goto L_0x0030
        L_0x001c:
            java.lang.Class<java.util.ArrayList> r0 = java.util.ArrayList.class
            boolean r0 = r9.isAssignableFrom(r0)
            if (r0 == 0) goto L_0x002a
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            goto L_0x0030
        L_0x002a:
            java.lang.Object r9 = r9.newInstance()     // Catch:{ Exception -> 0x0109 }
            java.util.Collection r9 = (java.util.Collection) r9     // Catch:{ Exception -> 0x0109 }
        L_0x0030:
            int r8 = r8.length
            int r0 = r7.bp
            int r2 = r8 + 1
            int r0 = r0 + r8
            char r8 = r7.charAt(r0)
            r0 = 91
            r3 = -1
            if (r8 == r0) goto L_0x0042
            r7.matchStat = r3
            return r1
        L_0x0042:
            int r8 = r7.bp
            int r0 = r2 + 1
            int r8 = r8 + r2
            char r8 = r7.charAt(r8)
        L_0x004b:
            r2 = 34
            if (r8 == r2) goto L_0x0052
            r7.matchStat = r3
            return r1
        L_0x0052:
            r8 = r0
        L_0x0053:
            int r4 = r7.bp
            int r5 = r8 + 1
            int r4 = r4 + r8
            char r8 = r7.charAt(r4)
            if (r8 != r2) goto L_0x00ff
            int r8 = r7.bp
            int r0 = r0 + r8
            int r8 = r8 + r5
            int r8 = r8 - r0
            int r8 = r8 + -1
            java.lang.String r8 = r7.subString(r0, r8)
            r9.add(r8)
            int r8 = r7.bp
            int r0 = r5 + 1
            int r8 = r8 + r5
            char r8 = r7.charAt(r8)
            r2 = 44
            if (r8 != r2) goto L_0x0084
            int r8 = r7.bp
            int r2 = r0 + 1
            int r8 = r8 + r0
            char r8 = r7.charAt(r8)
            r0 = r2
            goto L_0x004b
        L_0x0084:
            r4 = 93
            if (r8 != r4) goto L_0x00fc
            int r8 = r7.bp
            int r5 = r0 + 1
            int r8 = r8 + r0
            char r8 = r7.charAt(r8)
            if (r8 != r2) goto L_0x00a1
            int r8 = r7.bp
            int r5 = r5 + -1
            int r8 = r8 + r5
            r7.bp = r8
            r7.next()
            r8 = 3
            r7.matchStat = r8
            return r9
        L_0x00a1:
            r0 = 125(0x7d, float:1.75E-43)
            if (r8 != r0) goto L_0x00f9
            int r8 = r7.bp
            int r6 = r5 + 1
            int r8 = r8 + r5
            char r8 = r7.charAt(r8)
            if (r8 != r2) goto L_0x00bf
            r8 = 16
            r7.token = r8
            int r8 = r7.bp
            int r6 = r6 + -1
            int r8 = r8 + r6
            r7.bp = r8
            r7.next()
            goto L_0x00f2
        L_0x00bf:
            if (r8 != r4) goto L_0x00d0
            r8 = 15
            r7.token = r8
            int r8 = r7.bp
            int r6 = r6 + -1
            int r8 = r8 + r6
            r7.bp = r8
            r7.next()
            goto L_0x00f2
        L_0x00d0:
            if (r8 != r0) goto L_0x00e1
            r8 = 13
            r7.token = r8
            int r8 = r7.bp
            int r6 = r6 + -1
            int r8 = r8 + r6
            r7.bp = r8
            r7.next()
            goto L_0x00f2
        L_0x00e1:
            r0 = 26
            if (r8 != r0) goto L_0x00f6
            int r8 = r7.bp
            int r6 = r6 + -1
            int r8 = r8 + r6
            r7.bp = r8
            r8 = 20
            r7.token = r8
            r7.ch = r0
        L_0x00f2:
            r8 = 4
            r7.matchStat = r8
            return r9
        L_0x00f6:
            r7.matchStat = r3
            return r1
        L_0x00f9:
            r7.matchStat = r3
            return r1
        L_0x00fc:
            r7.matchStat = r3
            return r1
        L_0x00ff:
            r4 = 92
            if (r8 != r4) goto L_0x0106
            r7.matchStat = r3
            return r1
        L_0x0106:
            r8 = r5
            goto L_0x0053
        L_0x0109:
            r8 = move-exception
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException
            java.lang.String r0 = r8.getMessage()
            r9.<init>(r0, r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    public Collection<String> scanStringArray(Class<?> cls, char c) {
        int i;
        char c2;
        Collection<String> createCollection = TypeUtils.createCollection(cls);
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            } else if (charAt(this.bp + 4) == c) {
                this.bp += 4;
                next();
                this.matchStat = 3;
                return null;
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (charAt != '[') {
            this.matchStat = -1;
            return null;
        } else {
            char charAt2 = charAt(this.bp + 1);
            int i2 = 2;
            while (true) {
                if (charAt2 == 'n' && charAt(this.bp + i2) == 'u' && charAt(this.bp + i2 + 1) == 'l' && charAt(this.bp + i2 + 2) == 'l') {
                    int i3 = i2 + 3;
                    i = i3 + 1;
                    c2 = charAt(this.bp + i3);
                } else if (charAt2 != '\"') {
                    this.matchStat = -1;
                    return null;
                } else {
                    int i4 = i2;
                    while (true) {
                        int i5 = i4 + 1;
                        char charAt3 = charAt(this.bp + i4);
                        if (charAt3 == '\"') {
                            int i6 = this.bp;
                            int i7 = i2 + i6;
                            createCollection.add(subString(i7, ((i6 + i5) - i7) - 1));
                            i = i5 + 1;
                            c2 = charAt(this.bp + i5);
                            break;
                        } else if (charAt3 == '\\') {
                            this.matchStat = -1;
                            return null;
                        } else {
                            i4 = i5;
                        }
                    }
                }
                if (c2 == ',') {
                    i2 = i + 1;
                    charAt2 = charAt(this.bp + i);
                } else if (c2 == ']') {
                    int i8 = i + 1;
                    if (charAt(this.bp + i) == c) {
                        this.bp += i8 - 1;
                        next();
                        this.matchStat = 3;
                        return createCollection;
                    }
                    this.matchStat = -1;
                    return createCollection;
                } else {
                    this.matchStat = -1;
                    return null;
                }
            }
        }
    }

    public int scanFieldInt(char[] cArr) {
        int i;
        char charAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt2 = charAt(this.bp + length);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i3 = digits[charAt2];
        while (true) {
            i = i2 + 1;
            charAt = charAt(this.bp + i2);
            if (charAt >= '0' && charAt <= '9') {
                i3 = (i3 * 10) + digits[charAt];
                i2 = i;
            }
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        } else if (i3 < 0) {
            this.matchStat = -1;
            return 0;
        } else if (charAt == ',') {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return i3;
        } else if (charAt == '}') {
            int i4 = i + 1;
            char charAt3 = charAt(this.bp + i);
            if (charAt3 == ',') {
                this.token = 16;
                this.bp += i4 - 1;
                next();
            } else if (charAt3 == ']') {
                this.token = 15;
                this.bp += i4 - 1;
                next();
            } else if (charAt3 == '}') {
                this.token = 13;
                this.bp += i4 - 1;
                next();
            } else if (charAt3 == 26) {
                this.token = 20;
                this.bp += i4 - 1;
                this.ch = 26;
            } else {
                this.matchStat = -1;
                return 0;
            }
            this.matchStat = 4;
            return i3;
        } else {
            this.matchStat = -1;
            return 0;
        }
    }

    public boolean scanBoolean(char c) {
        boolean z = false;
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        int i = 5;
        if (charAt == 't') {
            if (charAt(this.bp + 1) == 'r' && charAt(this.bp + 1 + 1) == 'u' && charAt(this.bp + 1 + 2) == 'e') {
                charAt = charAt(this.bp + 4);
                z = true;
            } else {
                this.matchStat = -1;
                return false;
            }
        } else if (charAt != 'f') {
            i = 1;
        } else if (charAt(this.bp + 1) == 'a' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 's' && charAt(this.bp + 1 + 3) == 'e') {
            charAt = charAt(this.bp + 5);
            i = 6;
        } else {
            this.matchStat = -1;
            return false;
        }
        if (charAt == c) {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            return z;
        }
        this.matchStat = -1;
        return z;
    }

    public int scanInt(char c) {
        int i;
        char charAt;
        this.matchStat = 0;
        char charAt2 = charAt(this.bp + 0);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i2 = digits[charAt2];
        int i3 = 1;
        while (true) {
            i = i3 + 1;
            charAt = charAt(this.bp + i3);
            if (charAt >= '0' && charAt <= '9') {
                i2 = (i2 * 10) + digits[charAt];
                i3 = i;
            }
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        } else if (i2 < 0) {
            this.matchStat = -1;
            return 0;
        } else if (charAt == c) {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return i2;
        } else {
            this.matchStat = -1;
            return i2;
        }
    }

    public boolean scanFieldBoolean(char[] cArr) {
        boolean z;
        int i;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt = charAt(this.bp + length);
        if (charAt == 't') {
            int i3 = i2 + 1;
            if (charAt(this.bp + i2) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int i4 = i3 + 1;
            if (charAt(this.bp + i3) != 'u') {
                this.matchStat = -1;
                return false;
            }
            i = i4 + 1;
            if (charAt(this.bp + i4) != 'e') {
                this.matchStat = -1;
                return false;
            }
            z = true;
        } else if (charAt == 'f') {
            int i5 = i2 + 1;
            if (charAt(this.bp + i2) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int i6 = i5 + 1;
            if (charAt(this.bp + i5) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int i7 = i6 + 1;
            if (charAt(this.bp + i6) != 's') {
                this.matchStat = -1;
                return false;
            }
            int i8 = i7 + 1;
            if (charAt(this.bp + i7) != 'e') {
                this.matchStat = -1;
                return false;
            }
            i = i8;
            z = false;
        } else {
            this.matchStat = -1;
            return false;
        }
        int i9 = i + 1;
        char charAt2 = charAt(this.bp + i);
        if (charAt2 == ',') {
            this.bp += i9 - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return z;
        } else if (charAt2 == '}') {
            int i10 = i9 + 1;
            char charAt3 = charAt(this.bp + i9);
            if (charAt3 == ',') {
                this.token = 16;
                this.bp += i10 - 1;
                next();
            } else if (charAt3 == ']') {
                this.token = 15;
                this.bp += i10 - 1;
                next();
            } else if (charAt3 == '}') {
                this.token = 13;
                this.bp += i10 - 1;
                next();
            } else if (charAt3 == 26) {
                this.token = 20;
                this.bp += i10 - 1;
                this.ch = 26;
            } else {
                this.matchStat = -1;
                return false;
            }
            this.matchStat = 4;
            return z;
        } else {
            this.matchStat = -1;
            return false;
        }
    }

    public long scanFieldLong(char[] cArr) {
        int i;
        char charAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt2 = charAt(this.bp + length);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        long j = (long) digits[charAt2];
        while (true) {
            i = i2 + 1;
            charAt = charAt(this.bp + i2);
            if (charAt >= '0' && charAt <= '9') {
                j = (j * 10) + ((long) digits[charAt]);
                i2 = i;
            }
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        } else if (j < 0) {
            this.matchStat = -1;
            return 0;
        } else if (charAt == ',') {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return j;
        } else if (charAt == '}') {
            int i3 = i + 1;
            char charAt3 = charAt(this.bp + i);
            if (charAt3 == ',') {
                this.token = 16;
                this.bp += i3 - 1;
                next();
            } else if (charAt3 == ']') {
                this.token = 15;
                this.bp += i3 - 1;
                next();
            } else if (charAt3 == '}') {
                this.token = 13;
                this.bp += i3 - 1;
                next();
            } else if (charAt3 == 26) {
                this.token = 20;
                this.bp += i3 - 1;
                this.ch = 26;
            } else {
                this.matchStat = -1;
                return 0;
            }
            this.matchStat = 4;
            return j;
        } else {
            this.matchStat = -1;
            return 0;
        }
    }

    public long scanLong(char c) {
        int i;
        char charAt;
        this.matchStat = 0;
        char charAt2 = charAt(this.bp + 0);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        long j = (long) digits[charAt2];
        int i2 = 1;
        while (true) {
            i = i2 + 1;
            charAt = charAt(this.bp + i2);
            if (charAt >= '0' && charAt <= '9') {
                j = (j * 10) + ((long) digits[charAt]);
                i2 = i;
            }
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        } else if (j < 0) {
            this.matchStat = -1;
            return 0;
        } else if (charAt == c) {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return j;
        } else {
            this.matchStat = -1;
            return j;
        }
    }

    public final float scanFieldFloat(char[] cArr) {
        int i;
        char charAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0.0f;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt2 = charAt(this.bp + length);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0.0f;
        }
        while (true) {
            i = i2 + 1;
            charAt = charAt(this.bp + i2);
            if (charAt >= '0' && charAt <= '9') {
                i2 = i;
            }
        }
        if (charAt == '.') {
            int i3 = i + 1;
            char charAt3 = charAt(this.bp + i);
            if (charAt3 >= '0' && charAt3 <= '9') {
                while (true) {
                    i = i3 + 1;
                    charAt = charAt(this.bp + i3);
                    if (charAt < '0' || charAt > '9') {
                        break;
                    }
                    i3 = i;
                }
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        }
        int i4 = this.bp;
        int length2 = cArr.length + i4;
        float parseFloat = Float.parseFloat(subString(length2, ((i4 + i) - length2) - 1));
        if (charAt == ',') {
            this.bp += i - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return parseFloat;
        } else if (charAt == '}') {
            int i5 = i + 1;
            char charAt4 = charAt(this.bp + i);
            if (charAt4 == ',') {
                this.token = 16;
                this.bp += i5 - 1;
                next();
            } else if (charAt4 == ']') {
                this.token = 15;
                this.bp += i5 - 1;
                next();
            } else if (charAt4 == '}') {
                this.token = 13;
                this.bp += i5 - 1;
                next();
            } else if (charAt4 == 26) {
                this.bp += i5 - 1;
                this.token = 20;
                this.ch = 26;
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
            this.matchStat = 4;
            return parseFloat;
        } else {
            this.matchStat = -1;
            return 0.0f;
        }
    }

    public final float scanFloat(char c) {
        int i;
        char charAt;
        int i2;
        char c2;
        this.matchStat = 0;
        char charAt2 = charAt(this.bp + 0);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0.0f;
        }
        int i3 = 1;
        while (true) {
            i = i3 + 1;
            charAt = charAt(this.bp + i3);
            if (charAt >= '0' && charAt <= '9') {
                i3 = i;
            }
        }
        if (charAt == '.') {
            int i4 = i + 1;
            char charAt3 = charAt(this.bp + i);
            if (charAt3 >= '0' && charAt3 <= '9') {
                while (true) {
                    i2 = i4 + 1;
                    c2 = charAt(this.bp + i4);
                    if (c2 < '0' || c2 > '9') {
                        break;
                    }
                    i4 = i2;
                }
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        } else {
            c2 = charAt;
            i2 = i;
        }
        int i5 = this.bp;
        float parseFloat = Float.parseFloat(subString(i5, ((i5 + i2) - i5) - 1));
        if (c2 == c) {
            this.bp += i2 - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return parseFloat;
        }
        this.matchStat = -1;
        return parseFloat;
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final double scanFieldDouble(char[] r11) {
        /*
            r10 = this;
            r0 = 0
            r10.matchStat = r0
            boolean r0 = r10.charArrayCompare(r11)
            r1 = 0
            if (r0 != 0) goto L_0x000f
            r11 = -2
            r10.matchStat = r11
            return r1
        L_0x000f:
            int r0 = r11.length
            int r3 = r10.bp
            int r4 = r0 + 1
            int r3 = r3 + r0
            char r0 = r10.charAt(r3)
            r3 = -1
            r5 = 48
            if (r0 < r5) goto L_0x0108
            r6 = 57
            if (r0 > r6) goto L_0x0108
        L_0x0022:
            int r0 = r10.bp
            int r7 = r4 + 1
            int r0 = r0 + r4
            char r0 = r10.charAt(r0)
            if (r0 < r5) goto L_0x0031
            if (r0 > r6) goto L_0x0031
            r4 = r7
            goto L_0x0022
        L_0x0031:
            r4 = 46
            if (r0 != r4) goto L_0x0054
            int r0 = r10.bp
            int r4 = r7 + 1
            int r0 = r0 + r7
            char r0 = r10.charAt(r0)
            if (r0 < r5) goto L_0x0051
            if (r0 > r6) goto L_0x0051
        L_0x0042:
            int r0 = r10.bp
            int r7 = r4 + 1
            int r0 = r0 + r4
            char r0 = r10.charAt(r0)
            if (r0 < r5) goto L_0x0054
            if (r0 > r6) goto L_0x0054
            r4 = r7
            goto L_0x0042
        L_0x0051:
            r10.matchStat = r3
            return r1
        L_0x0054:
            r4 = 101(0x65, float:1.42E-43)
            if (r0 == r4) goto L_0x005c
            r4 = 69
            if (r0 != r4) goto L_0x0087
        L_0x005c:
            int r0 = r10.bp
            int r4 = r7 + 1
            int r0 = r0 + r7
            char r0 = r10.charAt(r0)
            r7 = 43
            if (r0 == r7) goto L_0x0070
            r7 = 45
            if (r0 != r7) goto L_0x006e
            goto L_0x0070
        L_0x006e:
            r7 = r4
            goto L_0x0079
        L_0x0070:
            int r0 = r10.bp
            int r7 = r4 + 1
            int r0 = r0 + r4
            char r0 = r10.charAt(r0)
        L_0x0079:
            if (r0 < r5) goto L_0x0087
            if (r0 > r6) goto L_0x0087
            int r0 = r10.bp
            int r4 = r7 + 1
            int r0 = r0 + r7
            char r0 = r10.charAt(r0)
            goto L_0x006e
        L_0x0087:
            int r4 = r10.bp
            int r11 = r11.length
            int r11 = r11 + r4
            int r4 = r4 + r7
            int r4 = r4 - r11
            int r4 = r4 + -1
            java.lang.String r11 = r10.subString(r11, r4)
            double r4 = java.lang.Double.parseDouble(r11)
            r11 = 16
            r6 = 44
            if (r0 != r6) goto L_0x00ad
            int r0 = r10.bp
            int r7 = r7 + -1
            int r0 = r0 + r7
            r10.bp = r0
            r10.next()
            r0 = 3
            r10.matchStat = r0
            r10.token = r11
            return r4
        L_0x00ad:
            r8 = 125(0x7d, float:1.75E-43)
            if (r0 != r8) goto L_0x0105
            int r0 = r10.bp
            int r9 = r7 + 1
            int r0 = r0 + r7
            char r0 = r10.charAt(r0)
            if (r0 != r6) goto L_0x00c9
            r10.token = r11
            int r11 = r10.bp
            int r9 = r9 + -1
            int r11 = r11 + r9
            r10.bp = r11
            r10.next()
            goto L_0x00fe
        L_0x00c9:
            r11 = 93
            if (r0 != r11) goto L_0x00dc
            r11 = 15
            r10.token = r11
            int r11 = r10.bp
            int r9 = r9 + -1
            int r11 = r11 + r9
            r10.bp = r11
            r10.next()
            goto L_0x00fe
        L_0x00dc:
            if (r0 != r8) goto L_0x00ed
            r11 = 13
            r10.token = r11
            int r11 = r10.bp
            int r9 = r9 + -1
            int r11 = r11 + r9
            r10.bp = r11
            r10.next()
            goto L_0x00fe
        L_0x00ed:
            r11 = 26
            if (r0 != r11) goto L_0x0102
            r0 = 20
            r10.token = r0
            int r0 = r10.bp
            int r9 = r9 + -1
            int r0 = r0 + r9
            r10.bp = r0
            r10.ch = r11
        L_0x00fe:
            r11 = 4
            r10.matchStat = r11
            return r4
        L_0x0102:
            r10.matchStat = r3
            return r1
        L_0x0105:
            r10.matchStat = r3
            return r1
        L_0x0108:
            r10.matchStat = r3
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldDouble(char[]):double");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final double scanFieldDouble(char r10) {
        /*
            r9 = this;
            r0 = 0
            r9.matchStat = r0
            int r1 = r9.bp
            int r1 = r1 + r0
            char r0 = r9.charAt(r1)
            r1 = 0
            r3 = -1
            r4 = 48
            if (r0 < r4) goto L_0x00a2
            r5 = 57
            if (r0 > r5) goto L_0x00a2
            r0 = 1
            r6 = 1
        L_0x0017:
            int r7 = r9.bp
            int r8 = r6 + 1
            int r7 = r7 + r6
            char r6 = r9.charAt(r7)
            if (r6 < r4) goto L_0x0026
            if (r6 > r5) goto L_0x0026
            r6 = r8
            goto L_0x0017
        L_0x0026:
            r7 = 46
            if (r6 != r7) goto L_0x0049
            int r6 = r9.bp
            int r7 = r8 + 1
            int r6 = r6 + r8
            char r6 = r9.charAt(r6)
            if (r6 < r4) goto L_0x0046
            if (r6 > r5) goto L_0x0046
        L_0x0037:
            int r1 = r9.bp
            int r2 = r7 + 1
            int r1 = r1 + r7
            char r6 = r9.charAt(r1)
            if (r6 < r4) goto L_0x004a
            if (r6 > r5) goto L_0x004a
            r7 = r2
            goto L_0x0037
        L_0x0046:
            r9.matchStat = r3
            return r1
        L_0x0049:
            r2 = r8
        L_0x004a:
            r1 = 101(0x65, float:1.42E-43)
            if (r6 == r1) goto L_0x0052
            r1 = 69
            if (r6 != r1) goto L_0x007e
        L_0x0052:
            int r1 = r9.bp
            int r6 = r2 + 1
            int r1 = r1 + r2
            char r1 = r9.charAt(r1)
            r2 = 43
            if (r1 == r2) goto L_0x0066
            r2 = 45
            if (r1 != r2) goto L_0x0064
            goto L_0x0066
        L_0x0064:
            r2 = r6
            goto L_0x006f
        L_0x0066:
            int r1 = r9.bp
            int r2 = r6 + 1
            int r1 = r1 + r6
            char r1 = r9.charAt(r1)
        L_0x006f:
            r6 = r1
            if (r6 < r4) goto L_0x007e
            if (r6 > r5) goto L_0x007e
            int r1 = r9.bp
            int r6 = r2 + 1
            int r1 = r1 + r2
            char r1 = r9.charAt(r1)
            goto L_0x0064
        L_0x007e:
            int r1 = r9.bp
            int r4 = r1 + r2
            int r4 = r4 - r1
            int r4 = r4 - r0
            java.lang.String r1 = r9.subString(r1, r4)
            double r4 = java.lang.Double.parseDouble(r1)
            if (r6 != r10) goto L_0x009f
            int r10 = r9.bp
            int r2 = r2 - r0
            int r10 = r10 + r2
            r9.bp = r10
            r9.next()
            r10 = 3
            r9.matchStat = r10
            r10 = 16
            r9.token = r10
            return r4
        L_0x009f:
            r9.matchStat = r3
            return r4
        L_0x00a2:
            r9.matchStat = r3
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldDouble(char):double");
    }

    public final void scanTrue() {
        if (this.ch == 't') {
            next();
            if (this.ch == 'r') {
                next();
                if (this.ch == 'u') {
                    next();
                    if (this.ch == 'e') {
                        next();
                        char c = this.ch;
                        if (c == ' ' || c == ',' || c == '}' || c == ']' || c == 10 || c == 13 || c == 9 || c == 26 || c == 12 || c == 8 || c == ':') {
                            this.token = 6;
                            return;
                        }
                        throw new JSONException("scan true error");
                    }
                    throw new JSONException("error parse true");
                }
                throw new JSONException("error parse true");
            }
            throw new JSONException("error parse true");
        }
        throw new JSONException("error parse true");
    }

    public final void scanTreeSet() {
        if (this.ch == 'T') {
            next();
            if (this.ch == 'r') {
                next();
                if (this.ch == 'e') {
                    next();
                    if (this.ch == 'e') {
                        next();
                        if (this.ch == 'S') {
                            next();
                            if (this.ch == 'e') {
                                next();
                                if (this.ch == 't') {
                                    next();
                                    char c = this.ch;
                                    if (c == ' ' || c == 10 || c == 13 || c == 9 || c == 12 || c == 8 || c == '[' || c == '(') {
                                        this.token = 22;
                                        return;
                                    }
                                    throw new JSONException("scan treeSet error");
                                }
                                throw new JSONException("error parse treeSet");
                            }
                            throw new JSONException("error parse treeSet");
                        }
                        throw new JSONException("error parse treeSet");
                    }
                    throw new JSONException("error parse treeSet");
                }
                throw new JSONException("error parse treeSet");
            }
            throw new JSONException("error parse treeSet");
        }
        throw new JSONException("error parse treeSet");
    }

    public final void scanNullOrNew() {
        if (this.ch == 'n') {
            next();
            char c = this.ch;
            if (c == 'u') {
                next();
                if (this.ch == 'l') {
                    next();
                    if (this.ch == 'l') {
                        next();
                        char c2 = this.ch;
                        if (c2 == ' ' || c2 == ',' || c2 == '}' || c2 == ']' || c2 == 10 || c2 == 13 || c2 == 9 || c2 == 26 || c2 == 12 || c2 == 8) {
                            this.token = 8;
                            return;
                        }
                        throw new JSONException("scan null error");
                    }
                    throw new JSONException("error parse null");
                }
                throw new JSONException("error parse null");
            } else if (c == 'e') {
                next();
                if (this.ch == 'w') {
                    next();
                    char c3 = this.ch;
                    if (c3 == ' ' || c3 == ',' || c3 == '}' || c3 == ']' || c3 == 10 || c3 == 13 || c3 == 9 || c3 == 26 || c3 == 12 || c3 == 8) {
                        this.token = 9;
                        return;
                    }
                    throw new JSONException("scan new error");
                }
                throw new JSONException("error parse new");
            } else {
                throw new JSONException("error parse new");
            }
        } else {
            throw new JSONException("error parse null or new");
        }
    }

    public final void scanNULL() {
        if (this.ch == 'N') {
            next();
            if (this.ch == 'U') {
                next();
                if (this.ch == 'L') {
                    next();
                    if (this.ch == 'L') {
                        next();
                        char c = this.ch;
                        if (c == ' ' || c == ',' || c == '}' || c == ']' || c == 10 || c == 13 || c == 9 || c == 26 || c == 12 || c == 8) {
                            this.token = 8;
                            return;
                        }
                        throw new JSONException("scan NULL error");
                    }
                    throw new JSONException("error parse NULL");
                }
                throw new JSONException("error parse NULL");
            }
            throw new JSONException("error parse NULL");
        }
        throw new JSONException("error parse NULL");
    }

    public final void scanUndefined() {
        if (this.ch == 'u') {
            next();
            if (this.ch == 'n') {
                next();
                if (this.ch == 'd') {
                    next();
                    if (this.ch == 'e') {
                        next();
                        if (this.ch == 'f') {
                            next();
                            if (this.ch == 'i') {
                                next();
                                if (this.ch == 'n') {
                                    next();
                                    if (this.ch == 'e') {
                                        next();
                                        if (this.ch == 'd') {
                                            next();
                                            char c = this.ch;
                                            if (c == ' ' || c == ',' || c == '}' || c == ']' || c == 10 || c == 13 || c == 9 || c == 26 || c == 12 || c == 8) {
                                                this.token = 23;
                                                return;
                                            }
                                            throw new JSONException("scan undefined error");
                                        }
                                        throw new JSONException("error parse undefined");
                                    }
                                    throw new JSONException("error parse undefined");
                                }
                                throw new JSONException("error parse undefined");
                            }
                            throw new JSONException("error parse undefined");
                        }
                        throw new JSONException("error parse undefined");
                    }
                    throw new JSONException("error parse undefined");
                }
                throw new JSONException("error parse undefined");
            }
            throw new JSONException("error parse undefined");
        }
        throw new JSONException("error parse undefined");
    }

    public final void scanFalse() {
        if (this.ch == 'f') {
            next();
            if (this.ch == 'a') {
                next();
                if (this.ch == 'l') {
                    next();
                    if (this.ch == 's') {
                        next();
                        if (this.ch == 'e') {
                            next();
                            char c = this.ch;
                            if (c == ' ' || c == ',' || c == '}' || c == ']' || c == 10 || c == 13 || c == 9 || c == 26 || c == 12 || c == 8 || c == ':') {
                                this.token = 7;
                                return;
                            }
                            throw new JSONException("scan false error");
                        }
                        throw new JSONException("error parse false");
                    }
                    throw new JSONException("error parse false");
                }
                throw new JSONException("error parse false");
            }
            throw new JSONException("error parse false");
        }
        throw new JSONException("error parse false");
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String stringVal = stringVal();
        if ("null".equals(stringVal)) {
            this.token = 8;
        } else if ("new".equals(stringVal)) {
            this.token = 9;
        } else if ("true".equals(stringVal)) {
            this.token = 6;
        } else if ("false".equals(stringVal)) {
            this.token = 7;
        } else if ("undefined".equals(stringVal)) {
            this.token = 23;
        } else {
            this.token = 18;
        }
    }

    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char charAt = charAt(i);
            if (charAt == 26) {
                return true;
            }
            if (!isWhitespace(charAt)) {
                return false;
            }
            i++;
        }
    }

    public final void skipWhitespace() {
        while (true) {
            char c = this.ch;
            if (c > '/') {
                return;
            }
            if (c == ' ' || c == 13 || c == 10 || c == 9 || c == 12 || c == 8) {
                next();
            } else if (c == '/') {
                skipComment();
            } else {
                return;
            }
        }
    }

    private void scanStringSingleQuote() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next = next();
            if (next == '\'') {
                this.token = 4;
                next();
                return;
            } else if (next == 26) {
                if (!isEOF()) {
                    putChar(26);
                } else {
                    throw new JSONException("unclosed single-quote string");
                }
            } else if (next == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    int i = this.sp;
                    char[] cArr = this.sbuf;
                    if (i > cArr.length) {
                        char[] cArr2 = new char[(i * 2)];
                        System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
                        this.sbuf = cArr2;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                char next2 = next();
                if (next2 == '\"') {
                    putChar(Typography.quote);
                } else if (next2 != '\'') {
                    if (next2 != 'F') {
                        if (next2 == '\\') {
                            putChar('\\');
                        } else if (next2 == 'b') {
                            putChar(8);
                        } else if (next2 != 'f') {
                            if (next2 == 'n') {
                                putChar(10);
                            } else if (next2 == 'r') {
                                putChar(13);
                            } else if (next2 != 'x') {
                                switch (next2) {
                                    case '/':
                                        putChar('/');
                                        break;
                                    case '0':
                                        putChar(0);
                                        break;
                                    case '1':
                                        putChar(1);
                                        break;
                                    case '2':
                                        putChar(2);
                                        break;
                                    case '3':
                                        putChar(3);
                                        break;
                                    case '4':
                                        putChar(4);
                                        break;
                                    case '5':
                                        putChar(5);
                                        break;
                                    case '6':
                                        putChar(6);
                                        break;
                                    case '7':
                                        putChar(7);
                                        break;
                                    default:
                                        switch (next2) {
                                            case 't':
                                                putChar(9);
                                                break;
                                            case 'u':
                                                putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                                                break;
                                            case 'v':
                                                putChar(11);
                                                break;
                                            default:
                                                this.ch = next2;
                                                throw new JSONException("unclosed single-quote string");
                                        }
                                }
                            } else {
                                putChar((char) ((digits[next()] * 16) + digits[next()]));
                            }
                        }
                    }
                    putChar(12);
                } else {
                    putChar('\'');
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else {
                int i2 = this.sp;
                char[] cArr3 = this.sbuf;
                if (i2 == cArr3.length) {
                    putChar(next);
                } else {
                    this.sp = i2 + 1;
                    cArr3[i2] = next;
                }
            }
        }
    }

    public final void scanSet() {
        if (this.ch == 'S') {
            next();
            if (this.ch == 'e') {
                next();
                if (this.ch == 't') {
                    next();
                    char c = this.ch;
                    if (c == ' ' || c == 10 || c == 13 || c == 9 || c == 12 || c == 8 || c == '[' || c == '(') {
                        this.token = 21;
                        return;
                    }
                    throw new JSONException("scan set error");
                }
                throw new JSONException("error parse set");
            }
            throw new JSONException("error parse set");
        }
        throw new JSONException("error parse set");
    }

    /* access modifiers changed from: protected */
    public final void putChar(char c) {
        int i = this.sp;
        char[] cArr = this.sbuf;
        if (i == cArr.length) {
            char[] cArr2 = new char[(cArr.length * 2)];
            System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
            this.sbuf = cArr2;
        }
        char[] cArr3 = this.sbuf;
        int i2 = this.sp;
        this.sp = i2 + 1;
        cArr3[i2] = c;
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00ce  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void scanNumber() {
        /*
            r9 = this;
            int r0 = r9.bp
            r9.np = r0
            char r0 = r9.ch
            r1 = 45
            r2 = 1
            if (r0 != r1) goto L_0x0013
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
        L_0x0013:
            char r0 = r9.ch
            r3 = 57
            r4 = 48
            if (r0 < r4) goto L_0x0026
            if (r0 > r3) goto L_0x0026
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
            goto L_0x0013
        L_0x0026:
            r0 = 0
            char r5 = r9.ch
            r6 = 46
            if (r5 != r6) goto L_0x0045
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
        L_0x0035:
            char r0 = r9.ch
            if (r0 < r4) goto L_0x0044
            if (r0 > r3) goto L_0x0044
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
            goto L_0x0035
        L_0x0044:
            r0 = 1
        L_0x0045:
            char r5 = r9.ch
            r6 = 76
            if (r5 != r6) goto L_0x0054
            int r1 = r9.sp
            int r1 = r1 + r2
            r9.sp = r1
            r9.next()
            goto L_0x0091
        L_0x0054:
            r6 = 83
            if (r5 != r6) goto L_0x0061
            int r1 = r9.sp
            int r1 = r1 + r2
            r9.sp = r1
            r9.next()
            goto L_0x0091
        L_0x0061:
            r6 = 66
            if (r5 != r6) goto L_0x006e
            int r1 = r9.sp
            int r1 = r1 + r2
            r9.sp = r1
            r9.next()
            goto L_0x0091
        L_0x006e:
            r6 = 70
            if (r5 != r6) goto L_0x007b
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
            goto L_0x00c8
        L_0x007b:
            r7 = 68
            if (r5 != r7) goto L_0x0088
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
            goto L_0x00c8
        L_0x0088:
            r8 = 101(0x65, float:1.42E-43)
            if (r5 == r8) goto L_0x0093
            r8 = 69
            if (r5 != r8) goto L_0x0091
            goto L_0x0093
        L_0x0091:
            r2 = r0
            goto L_0x00c8
        L_0x0093:
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
            char r0 = r9.ch
            r5 = 43
            if (r0 == r5) goto L_0x00a3
            if (r0 != r1) goto L_0x00ab
        L_0x00a3:
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
        L_0x00ab:
            char r0 = r9.ch
            if (r0 < r4) goto L_0x00ba
            if (r0 > r3) goto L_0x00ba
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
            goto L_0x00ab
        L_0x00ba:
            char r0 = r9.ch
            if (r0 == r7) goto L_0x00c0
            if (r0 != r6) goto L_0x00c8
        L_0x00c0:
            int r0 = r9.sp
            int r0 = r0 + r2
            r9.sp = r0
            r9.next()
        L_0x00c8:
            if (r2 == 0) goto L_0x00ce
            r0 = 3
            r9.token = r0
            goto L_0x00d1
        L_0x00ce:
            r0 = 2
            r9.token = r0
        L_0x00d1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanNumber():void");
    }

    public final long longValue() throws NumberFormatException {
        long j;
        long j2;
        int i;
        boolean z = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i2 = this.np;
        int i3 = this.sp + i2;
        if (charAt(i2) == '-') {
            j = Long.MIN_VALUE;
            i2++;
            z = true;
        } else {
            j = -9223372036854775807L;
        }
        if (i2 < i3) {
            j2 = (long) (-digits[charAt(i2)]);
            i2++;
        } else {
            j2 = 0;
        }
        while (true) {
            if (i2 >= i3) {
                break;
            }
            i = i2 + 1;
            char charAt = charAt(i2);
            if (charAt == 'L' || charAt == 'S' || charAt == 'B') {
                i2 = i;
            } else {
                int i4 = digits[charAt];
                if (j2 >= MULTMIN_RADIX_TEN) {
                    long j3 = j2 * 10;
                    long j4 = (long) i4;
                    if (j3 >= j + j4) {
                        j2 = j3 - j4;
                        i2 = i;
                    } else {
                        throw new NumberFormatException(numberString());
                    }
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
        }
        i2 = i;
        if (!z) {
            return -j2;
        }
        if (i2 > this.np + 1) {
            return j2;
        }
        throw new NumberFormatException(numberString());
    }

    public final Number decimalValue(boolean z) {
        char charAt = charAt((this.np + this.sp) - 1);
        if (charAt == 'F') {
            return Float.valueOf(Float.parseFloat(numberString()));
        }
        if (charAt == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        }
        if (z) {
            return decimalValue();
        }
        return Double.valueOf(doubleValue());
    }

    public final BigDecimal decimalValue() {
        return new BigDecimal(numberString());
    }
}
