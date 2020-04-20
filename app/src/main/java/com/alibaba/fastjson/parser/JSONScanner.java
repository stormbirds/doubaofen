package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;
import java.util.Calendar;
import java.util.TimeZone;

public final class JSONScanner extends JSONLexerBase {
    public final int ISO8601_LEN_0;
    public final int ISO8601_LEN_1;
    public final int ISO8601_LEN_2;
    private final int len;
    private final String text;

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if ((c == '1' || c == '2') && c2 >= '0' && c2 <= '9' && c3 >= '0' && c3 <= '9' && c4 >= '0' && c4 <= '9') {
            if (c5 == '0') {
                if (c6 < '1' || c6 > '9') {
                    return false;
                }
            } else if (!(c5 == '1' && (c6 == '0' || c6 == '1' || c6 == '2'))) {
                return false;
            }
            return i == 48 ? i2 >= 49 && i2 <= 57 : (i == 49 || i == 50) ? i2 >= 48 && i2 <= 57 : i == 51 && (i2 == 48 || i2 == 49);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        if (r6 <= '4') goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkTime(char r5, char r6, char r7, char r8, char r9, char r10) {
        /*
            r4 = this;
            r0 = 57
            r1 = 0
            r2 = 48
            if (r5 != r2) goto L_0x000c
            if (r6 < r2) goto L_0x000b
            if (r6 <= r0) goto L_0x0020
        L_0x000b:
            return r1
        L_0x000c:
            r3 = 49
            if (r5 != r3) goto L_0x0015
            if (r6 < r2) goto L_0x0014
            if (r6 <= r0) goto L_0x0020
        L_0x0014:
            return r1
        L_0x0015:
            r3 = 50
            if (r5 != r3) goto L_0x0042
            if (r6 < r2) goto L_0x0042
            r5 = 52
            if (r6 <= r5) goto L_0x0020
            goto L_0x0042
        L_0x0020:
            r5 = 53
            r6 = 54
            if (r7 < r2) goto L_0x002d
            if (r7 > r5) goto L_0x002d
            if (r8 < r2) goto L_0x002c
            if (r8 <= r0) goto L_0x0032
        L_0x002c:
            return r1
        L_0x002d:
            if (r7 != r6) goto L_0x0042
            if (r8 == r2) goto L_0x0032
            return r1
        L_0x0032:
            if (r9 < r2) goto L_0x003b
            if (r9 > r5) goto L_0x003b
            if (r10 < r2) goto L_0x003a
            if (r10 <= r0) goto L_0x0040
        L_0x003a:
            return r1
        L_0x003b:
            if (r9 != r6) goto L_0x0042
            if (r10 == r2) goto L_0x0040
            return r1
        L_0x0040:
            r5 = 1
            return r5
        L_0x0042:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.checkTime(char, char, char, char, char, char):boolean");
    }

    public JSONScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String str, int i) {
        super(i);
        this.ISO8601_LEN_0 = 10;
        this.ISO8601_LEN_1 = 19;
        this.ISO8601_LEN_2 = 23;
        this.text = str;
        this.len = this.text.length();
        this.bp = -1;
        next();
        if (this.ch == 65279) {
            next();
        }
    }

    public final char charAt(int i) {
        if (i >= this.len) {
            return 26;
        }
        return this.text.charAt(i);
    }

    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        char charAt = charAt(i);
        this.ch = charAt;
        return charAt;
    }

    public JSONScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    /* access modifiers changed from: protected */
    public final void copyTo(int i, int i2, char[] cArr) {
        this.text.getChars(i, i2 + i, cArr, 0);
    }

    static boolean charArrayCompare(String str, int i, char[] cArr) {
        int length = cArr.length;
        if (length + i > str.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != str.charAt(i + i2)) {
                return false;
            }
        }
        return true;
    }

    public final boolean charArrayCompare(char[] cArr) {
        return charArrayCompare(this.text, this.bp, cArr);
    }

    public final int indexOf(char c, int i) {
        return this.text.indexOf(c, i);
    }

    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, i, i2, i3);
    }

    public byte[] bytesValue() {
        return IOUtils.decodeFast(this.text, this.np + 1, this.sp);
    }

    public final String stringVal() {
        if (!this.hasSpecial) {
            return subString(this.np + 1, this.sp);
        }
        return new String(this.sbuf, 0, this.sp);
    }

    public final String subString(int i, int i2) {
        if (!ASMUtils.IS_ANDROID) {
            return this.text.substring(i, i2 + i);
        }
        if (i2 < this.sbuf.length) {
            this.text.getChars(i, i + i2, this.sbuf, 0);
            return new String(this.sbuf, 0, i2);
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return new String(cArr);
    }

    public final String numberString() {
        char charAt = charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        char charAt;
        int i5;
        int i6;
        int i7;
        char charAt2;
        char charAt3;
        int i8 = this.len - this.bp;
        if (!z && i8 > 13) {
            char charAt4 = charAt(this.bp);
            char charAt5 = charAt(this.bp + 1);
            char charAt6 = charAt(this.bp + 2);
            char charAt7 = charAt(this.bp + 3);
            char charAt8 = charAt(this.bp + 4);
            char charAt9 = charAt(this.bp + 5);
            char charAt10 = charAt((this.bp + i8) - 1);
            char charAt11 = charAt((this.bp + i8) - 2);
            if (charAt4 == '/' && charAt5 == 'D' && charAt6 == 'a' && charAt7 == 't' && charAt8 == 'e' && charAt9 == '(' && charAt10 == '/' && charAt11 == ')') {
                int i9 = -1;
                for (int i10 = 6; i10 < i8; i10++) {
                    char charAt12 = charAt(this.bp + i10);
                    if (charAt12 != '+') {
                        if (charAt12 < '0' || charAt12 > '9') {
                            break;
                        }
                    } else {
                        i9 = i10;
                    }
                }
                if (i9 == -1) {
                    return false;
                }
                int i11 = this.bp + 6;
                long parseLong = Long.parseLong(subString(i11, i9 - i11));
                this.calendar = Calendar.getInstance(this.timeZone, this.locale);
                this.calendar.setTimeInMillis(parseLong);
                this.token = 5;
                return true;
            }
        }
        if (i8 == 8 || i8 == 14 || i8 == 17) {
            if (z) {
                return false;
            }
            char charAt13 = charAt(this.bp);
            char charAt14 = charAt(this.bp + 1);
            char charAt15 = charAt(this.bp + 2);
            char charAt16 = charAt(this.bp + 3);
            char charAt17 = charAt(this.bp + 4);
            char charAt18 = charAt(this.bp + 5);
            char charAt19 = charAt(this.bp + 6);
            char charAt20 = charAt(this.bp + 7);
            if (!checkDate(charAt13, charAt14, charAt15, charAt16, charAt17, charAt18, charAt19, charAt20)) {
                return false;
            }
            setCalendar(charAt13, charAt14, charAt15, charAt16, charAt17, charAt18, charAt19, charAt20);
            if (i8 != 8) {
                char charAt21 = charAt(this.bp + 8);
                char charAt22 = charAt(this.bp + 9);
                char charAt23 = charAt(this.bp + 10);
                char charAt24 = charAt(this.bp + 11);
                char charAt25 = charAt(this.bp + 12);
                char charAt26 = charAt(this.bp + 13);
                if (!checkTime(charAt21, charAt22, charAt23, charAt24, charAt25, charAt26)) {
                    return false;
                }
                if (i8 == 17) {
                    char charAt27 = charAt(this.bp + 14);
                    char charAt28 = charAt(this.bp + 15);
                    char charAt29 = charAt(this.bp + 16);
                    if (charAt27 < '0' || charAt27 > '9' || charAt28 < '0' || charAt28 > '9' || charAt29 < '0' || charAt29 > '9') {
                        return false;
                    }
                    i = (digits[charAt27] * 100) + (digits[charAt28] * 10) + digits[charAt29];
                } else {
                    i = 0;
                }
                i4 = (digits[charAt21] * 10) + digits[charAt22];
                i3 = (digits[charAt23] * 10) + digits[charAt24];
                i2 = (digits[charAt25] * 10) + digits[charAt26];
            } else {
                i4 = 0;
                i3 = 0;
                i2 = 0;
                i = 0;
            }
            this.calendar.set(11, i4);
            this.calendar.set(12, i3);
            this.calendar.set(13, i2);
            this.calendar.set(14, i);
            this.token = 5;
            return true;
        } else if (i8 < this.ISO8601_LEN_0 || charAt(this.bp + 4) != '-' || charAt(this.bp + 7) != '-') {
            return false;
        } else {
            char charAt30 = charAt(this.bp);
            char charAt31 = charAt(this.bp + 1);
            char charAt32 = charAt(this.bp + 2);
            char charAt33 = charAt(this.bp + 3);
            char charAt34 = charAt(this.bp + 5);
            char charAt35 = charAt(this.bp + 6);
            char charAt36 = charAt(this.bp + 8);
            char charAt37 = charAt(this.bp + 9);
            if (!checkDate(charAt30, charAt31, charAt32, charAt33, charAt34, charAt35, charAt36, charAt37)) {
                return false;
            }
            setCalendar(charAt30, charAt31, charAt32, charAt33, charAt34, charAt35, charAt36, charAt37);
            char charAt38 = charAt(this.bp + 10);
            if (charAt38 == 'T' || (charAt38 == ' ' && !z)) {
                if (i8 < this.ISO8601_LEN_1 || charAt(this.bp + 13) != ':' || charAt(this.bp + 16) != ':') {
                    return false;
                }
                char charAt39 = charAt(this.bp + 11);
                char charAt40 = charAt(this.bp + 12);
                char charAt41 = charAt(this.bp + 14);
                char charAt42 = charAt(this.bp + 15);
                char charAt43 = charAt(this.bp + 17);
                char charAt44 = charAt(this.bp + 18);
                if (!checkTime(charAt39, charAt40, charAt41, charAt42, charAt43, charAt44)) {
                    return false;
                }
                setTime(charAt39, charAt40, charAt41, charAt42, charAt43, charAt44);
                char charAt45 = charAt(this.bp + 19);
                if (charAt45 == '.') {
                    if (i8 >= this.ISO8601_LEN_2 && (charAt = charAt(this.bp + 20)) >= '0' && charAt <= '9') {
                        int i12 = digits[charAt];
                        char charAt46 = charAt(this.bp + 21);
                        if (charAt46 < '0' || charAt46 > '9') {
                            i6 = 2;
                            i5 = 1;
                        } else {
                            i12 = (i12 * 10) + digits[charAt46];
                            i6 = 2;
                            i5 = 2;
                        }
                        if (i5 == i6 && (charAt3 = charAt(this.bp + 22)) >= '0' && charAt3 <= '9') {
                            i12 = (i12 * 10) + digits[charAt3];
                            i5 = 3;
                        }
                        this.calendar.set(14, i12);
                        char charAt47 = charAt(this.bp + 20 + i5);
                        if (charAt47 == '+' || charAt47 == '-') {
                            char charAt48 = charAt(this.bp + 20 + i5 + 1);
                            if (charAt48 >= '0' && charAt48 <= '1' && (charAt2 = charAt(this.bp + 20 + i5 + 2)) >= '0' && charAt2 <= '9') {
                                char charAt49 = charAt(this.bp + 20 + i5 + 3);
                                if (charAt49 == ':') {
                                    if (charAt(this.bp + 20 + i5 + 4) != '0' || charAt(this.bp + 20 + i5 + 5) != '0') {
                                        return false;
                                    }
                                    i7 = 6;
                                } else if (charAt49 != '0') {
                                    i7 = 3;
                                } else if (charAt(this.bp + 20 + i5 + 4) != '0') {
                                    return false;
                                } else {
                                    i7 = 5;
                                }
                                setTimeZone(charAt47, charAt48, charAt2);
                            }
                        } else if (charAt47 == 'Z') {
                            if (this.calendar.getTimeZone().getRawOffset() != 0) {
                                String[] availableIDs = TimeZone.getAvailableIDs(0);
                                if (availableIDs.length > 0) {
                                    this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
                                }
                            }
                            i7 = 1;
                        } else {
                            i7 = 0;
                        }
                        int i13 = i5 + 20 + i7;
                        char charAt50 = charAt(this.bp + i13);
                        if (charAt50 != 26 && charAt50 != '\"') {
                            return false;
                        }
                        int i14 = this.bp + i13;
                        this.bp = i14;
                        this.ch = charAt(i14);
                        this.token = 5;
                        return true;
                    }
                    return false;
                }
                this.calendar.set(14, 0);
                int i15 = this.bp + 19;
                this.bp = i15;
                this.ch = charAt(i15);
                this.token = 5;
                if (charAt45 == 'Z' && this.calendar.getTimeZone().getRawOffset() != 0) {
                    String[] availableIDs2 = TimeZone.getAvailableIDs(0);
                    if (availableIDs2.length > 0) {
                        this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs2[0]));
                    }
                }
                return true;
            } else if (charAt38 == '\"' || charAt38 == 26) {
                this.calendar.set(11, 0);
                this.calendar.set(12, 0);
                this.calendar.set(13, 0);
                this.calendar.set(14, 0);
                int i16 = this.bp + 10;
                this.bp = i16;
                this.ch = charAt(i16);
                this.token = 5;
                return true;
            } else if ((charAt38 != '+' && charAt38 != '-') || this.len != 16 || charAt(this.bp + 13) != ':' || charAt(this.bp + 14) != '0' || charAt(this.bp + 15) != '0') {
                return false;
            } else {
                setTime('0', '0', '0', '0', '0', '0');
                this.calendar.set(14, 0);
                setTimeZone(charAt38, charAt(this.bp + 11), charAt(this.bp + 12));
                return true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setTime(char c, char c2, char c3, char c4, char c5, char c6) {
        int i = (digits[c] * 10) + digits[c2];
        int i2 = (digits[c3] * 10) + digits[c4];
        int i3 = (digits[c5] * 10) + digits[c6];
        this.calendar.set(11, i);
        this.calendar.set(12, i2);
        this.calendar.set(13, i3);
    }

    /* access modifiers changed from: protected */
    public void setTimeZone(char c, char c2, char c3) {
        int i = ((digits[c2] * 10) + digits[c3]) * 3600 * 1000;
        if (c == '-') {
            i = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i) {
            String[] availableIDs = TimeZone.getAvailableIDs(i);
            if (availableIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
            }
        }
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        int i = (digits[c] * 1000) + (digits[c2] * 100) + (digits[c3] * 10) + digits[c4];
        int i2 = (digits[c7] * 10) + digits[c8];
        this.calendar.set(1, i);
        this.calendar.set(2, ((digits[c5] * 10) + digits[c6]) - 1);
        this.calendar.set(5, i2);
    }

    public boolean isEOF() {
        if (this.bp != this.len) {
            return this.ch == 26 && this.bp + 1 == this.len;
        }
        return true;
    }

    public int scanFieldInt(char[] cArr) {
        int i;
        char charAt;
        this.matchStat = 0;
        int i2 = this.bp;
        char c = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = this.bp + cArr.length;
        int i3 = length + 1;
        char charAt2 = charAt(length);
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i4 = digits[charAt2];
        while (true) {
            i = i3 + 1;
            charAt = charAt(i3);
            if (charAt >= '0' && charAt <= '9') {
                i4 = (i4 * 10) + digits[charAt];
                i3 = i;
            }
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        } else if (i4 < 0) {
            this.matchStat = -1;
            return 0;
        } else {
            if (charAt == ',' || charAt == '}') {
                this.bp = i - 1;
            }
            if (charAt == ',') {
                int i5 = this.bp + 1;
                this.bp = i5;
                this.ch = charAt(i5);
                this.matchStat = 3;
                this.token = 16;
                return i4;
            }
            if (charAt == '}') {
                int i6 = this.bp + 1;
                this.bp = i6;
                char charAt3 = charAt(i6);
                if (charAt3 == ',') {
                    this.token = 16;
                    int i7 = this.bp + 1;
                    this.bp = i7;
                    this.ch = charAt(i7);
                } else if (charAt3 == ']') {
                    this.token = 15;
                    int i8 = this.bp + 1;
                    this.bp = i8;
                    this.ch = charAt(i8);
                } else if (charAt3 == '}') {
                    this.token = 13;
                    int i9 = this.bp + 1;
                    this.bp = i9;
                    this.ch = charAt(i9);
                } else if (charAt3 == 26) {
                    this.token = 20;
                } else {
                    this.bp = i2;
                    this.ch = c;
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
            }
            return i4;
        }
    }

    public String scanFieldString(char[] cArr) {
        boolean z = false;
        this.matchStat = 0;
        int i = this.bp;
        char c = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int length = this.bp + cArr.length;
        int i2 = length + 1;
        if (charAt(length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int indexOf = this.text.indexOf(34, i2);
        if (indexOf != -1) {
            String subString = subString(i2, indexOf - i2);
            int i3 = 0;
            while (true) {
                if (i3 >= subString.length()) {
                    break;
                } else if (subString.charAt(i3) == '\\') {
                    z = true;
                    break;
                } else {
                    i3++;
                }
            }
            if (z) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            int i4 = indexOf + 1;
            char charAt = charAt(i4);
            if (charAt == ',' || charAt == '}') {
                this.bp = i4;
                this.ch = charAt;
                if (charAt == ',') {
                    int i5 = this.bp + 1;
                    this.bp = i5;
                    this.ch = charAt(i5);
                    this.matchStat = 3;
                    return subString;
                } else if (charAt == '}') {
                    int i6 = this.bp + 1;
                    this.bp = i6;
                    char charAt2 = charAt(i6);
                    if (charAt2 == ',') {
                        this.token = 16;
                        int i7 = this.bp + 1;
                        this.bp = i7;
                        this.ch = charAt(i7);
                    } else if (charAt2 == ']') {
                        this.token = 15;
                        int i8 = this.bp + 1;
                        this.bp = i8;
                        this.ch = charAt(i8);
                    } else if (charAt2 == '}') {
                        this.token = 13;
                        int i9 = this.bp + 1;
                        this.bp = i9;
                        this.ch = charAt(i9);
                    } else if (charAt2 == 26) {
                        this.token = 20;
                    } else {
                        this.bp = i;
                        this.ch = c;
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
                this.matchStat = -1;
                return stringDefaultValue();
            }
        } else {
            throw new JSONException("unclosed str");
        }
    }

    public String scanFieldSymbol(char[] cArr, SymbolTable symbolTable) {
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = this.bp + cArr.length;
        int i = length + 1;
        if (charAt(length) != '\"') {
            this.matchStat = -1;
            return null;
        }
        int i2 = i;
        int i3 = 0;
        while (true) {
            int i4 = i2 + 1;
            char charAt = charAt(i2);
            if (charAt == '\"') {
                this.bp = i4;
                char charAt2 = charAt(this.bp);
                this.ch = charAt2;
                String addSymbol = symbolTable.addSymbol(this.text, i, (i4 - i) - 1, i3);
                if (charAt2 == ',') {
                    int i5 = this.bp + 1;
                    this.bp = i5;
                    this.ch = charAt(i5);
                    this.matchStat = 3;
                    return addSymbol;
                } else if (charAt2 == '}') {
                    int i6 = this.bp + 1;
                    this.bp = i6;
                    char charAt3 = charAt(i6);
                    if (charAt3 == ',') {
                        this.token = 16;
                        int i7 = this.bp + 1;
                        this.bp = i7;
                        this.ch = charAt(i7);
                    } else if (charAt3 == ']') {
                        this.token = 15;
                        int i8 = this.bp + 1;
                        this.bp = i8;
                        this.ch = charAt(i8);
                    } else if (charAt3 == '}') {
                        this.token = 13;
                        int i9 = this.bp + 1;
                        this.bp = i9;
                        this.ch = charAt(i9);
                    } else if (charAt3 == 26) {
                        this.token = 20;
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
                i3 = (i3 * 31) + charAt;
                if (charAt == '\\') {
                    this.matchStat = -1;
                    return null;
                }
                i2 = i4;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005c, code lost:
        r10.add(subString(r0, (r4 - r0) - 1));
        r9 = r4 + 1;
        r0 = charAt(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006f, code lost:
        if (r0 != ',') goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0071, code lost:
        r0 = r9 + 1;
        r9 = charAt(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007a, code lost:
        if (r0 != ']') goto L_0x010b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007c, code lost:
        r0 = r9 + 1;
        r9 = charAt(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0086, code lost:
        if (isWhitespace(r9) == false) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0088, code lost:
        r7 = charAt(r0);
        r0 = r0 + 1;
        r9 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0092, code lost:
        r8.bp = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0094, code lost:
        if (r9 != ',') goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0096, code lost:
        r8.ch = charAt(r8.bp);
        r8.matchStat = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a1, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00a4, code lost:
        if (r9 != '}') goto L_0x0108;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a6, code lost:
        r9 = charAt(r8.bp);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b0, code lost:
        if (isWhitespace(r9) == false) goto L_0x00be;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b2, code lost:
        r9 = r0 + 1;
        r0 = charAt(r0);
        r8.bp = r9;
        r7 = r0;
        r0 = r9;
        r9 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00be, code lost:
        if (r9 != ',') goto L_0x00d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c0, code lost:
        r8.token = 16;
        r9 = r8.bp + 1;
        r8.bp = r9;
        r8.ch = charAt(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00d1, code lost:
        if (r9 != ']') goto L_0x00e4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00d3, code lost:
        r8.token = 15;
        r9 = r8.bp + 1;
        r8.bp = r9;
        r8.ch = charAt(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00e4, code lost:
        if (r9 != '}') goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00e6, code lost:
        r8.token = 13;
        r9 = r8.bp + 1;
        r8.bp = r9;
        r8.ch = charAt(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f9, code lost:
        if (r9 != 26) goto L_0x0105;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00fb, code lost:
        r8.token = 20;
        r8.ch = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0101, code lost:
        r8.matchStat = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0104, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0105, code lost:
        r8.matchStat = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0107, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0108, code lost:
        r8.matchStat = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x010a, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x010b, code lost:
        r8.matchStat = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x010d, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r9, java.lang.Class<?> r10) {
        /*
            r8 = this;
            r0 = 0
            r8.matchStat = r0
            java.lang.String r0 = r8.text
            int r1 = r8.bp
            boolean r0 = charArrayCompare(r0, r1, r9)
            r1 = 0
            if (r0 != 0) goto L_0x0012
            r9 = -2
            r8.matchStat = r9
            return r1
        L_0x0012:
            java.lang.Class<java.util.HashSet> r0 = java.util.HashSet.class
            boolean r0 = r10.isAssignableFrom(r0)
            if (r0 == 0) goto L_0x0020
            java.util.HashSet r10 = new java.util.HashSet
            r10.<init>()
            goto L_0x0034
        L_0x0020:
            java.lang.Class<java.util.ArrayList> r0 = java.util.ArrayList.class
            boolean r0 = r10.isAssignableFrom(r0)
            if (r0 == 0) goto L_0x002e
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            goto L_0x0034
        L_0x002e:
            java.lang.Object r10 = r10.newInstance()     // Catch:{ Exception -> 0x0118 }
            java.util.Collection r10 = (java.util.Collection) r10     // Catch:{ Exception -> 0x0118 }
        L_0x0034:
            int r0 = r8.bp
            int r9 = r9.length
            int r0 = r0 + r9
            int r9 = r0 + 1
            char r0 = r8.charAt(r0)
            r2 = 91
            r3 = -1
            if (r0 == r2) goto L_0x0046
            r8.matchStat = r3
            return r1
        L_0x0046:
            int r0 = r9 + 1
            char r9 = r8.charAt(r9)
        L_0x004c:
            r2 = 34
            if (r9 == r2) goto L_0x0053
            r8.matchStat = r3
            return r1
        L_0x0053:
            r9 = r0
        L_0x0054:
            int r4 = r9 + 1
            char r9 = r8.charAt(r9)
            if (r9 != r2) goto L_0x010e
            int r9 = r4 - r0
            int r9 = r9 + -1
            java.lang.String r9 = r8.subString(r0, r9)
            r10.add(r9)
            int r9 = r4 + 1
            char r0 = r8.charAt(r4)
            r2 = 44
            if (r0 != r2) goto L_0x0078
            int r0 = r9 + 1
            char r9 = r8.charAt(r9)
            goto L_0x004c
        L_0x0078:
            r4 = 93
            if (r0 != r4) goto L_0x010b
            int r0 = r9 + 1
            char r9 = r8.charAt(r9)
        L_0x0082:
            boolean r5 = isWhitespace(r9)
            if (r5 == 0) goto L_0x0092
            int r9 = r0 + 1
            char r0 = r8.charAt(r0)
            r7 = r0
            r0 = r9
            r9 = r7
            goto L_0x0082
        L_0x0092:
            r8.bp = r0
            if (r9 != r2) goto L_0x00a2
            int r9 = r8.bp
            char r9 = r8.charAt(r9)
            r8.ch = r9
            r9 = 3
            r8.matchStat = r9
            return r10
        L_0x00a2:
            r5 = 125(0x7d, float:1.75E-43)
            if (r9 != r5) goto L_0x0108
            int r9 = r8.bp
            char r9 = r8.charAt(r9)
        L_0x00ac:
            boolean r6 = isWhitespace(r9)
            if (r6 == 0) goto L_0x00be
            int r9 = r0 + 1
            char r0 = r8.charAt(r0)
            r8.bp = r9
            r7 = r0
            r0 = r9
            r9 = r7
            goto L_0x00ac
        L_0x00be:
            if (r9 != r2) goto L_0x00d1
            r9 = 16
            r8.token = r9
            int r9 = r8.bp
            int r9 = r9 + 1
            r8.bp = r9
            char r9 = r8.charAt(r9)
            r8.ch = r9
            goto L_0x0101
        L_0x00d1:
            if (r9 != r4) goto L_0x00e4
            r9 = 15
            r8.token = r9
            int r9 = r8.bp
            int r9 = r9 + 1
            r8.bp = r9
            char r9 = r8.charAt(r9)
            r8.ch = r9
            goto L_0x0101
        L_0x00e4:
            if (r9 != r5) goto L_0x00f7
            r9 = 13
            r8.token = r9
            int r9 = r8.bp
            int r9 = r9 + 1
            r8.bp = r9
            char r9 = r8.charAt(r9)
            r8.ch = r9
            goto L_0x0101
        L_0x00f7:
            r0 = 26
            if (r9 != r0) goto L_0x0105
            r0 = 20
            r8.token = r0
            r8.ch = r9
        L_0x0101:
            r9 = 4
            r8.matchStat = r9
            return r10
        L_0x0105:
            r8.matchStat = r3
            return r1
        L_0x0108:
            r8.matchStat = r3
            return r1
        L_0x010b:
            r8.matchStat = r3
            return r1
        L_0x010e:
            r5 = 92
            if (r9 != r5) goto L_0x0115
            r8.matchStat = r3
            return r1
        L_0x0115:
            r9 = r4
            goto L_0x0054
        L_0x0118:
            r9 = move-exception
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException
            java.lang.String r0 = r9.getMessage()
            r10.<init>(r0, r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    public long scanFieldLong(char[] cArr) {
        int i;
        char charAt;
        this.matchStat = 0;
        int i2 = this.bp;
        char c = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = this.bp + cArr.length;
        int i3 = length + 1;
        char charAt2 = charAt(length);
        if (charAt2 < '0' || charAt2 > '9') {
            this.bp = i2;
            this.ch = c;
            this.matchStat = -1;
            return 0;
        }
        long j = (long) digits[charAt2];
        while (true) {
            i = i3 + 1;
            charAt = charAt(i3);
            if (charAt >= '0' && charAt <= '9') {
                j = (j * 10) + ((long) digits[charAt]);
                i3 = i;
            }
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (charAt == ',' || charAt == '}') {
            this.bp = i - 1;
        }
        if (j < 0) {
            this.bp = i2;
            this.ch = c;
            this.matchStat = -1;
            return 0;
        } else if (charAt == ',') {
            int i4 = this.bp + 1;
            this.bp = i4;
            this.ch = charAt(i4);
            this.matchStat = 3;
            this.token = 16;
            return j;
        } else if (charAt == '}') {
            int i5 = this.bp + 1;
            this.bp = i5;
            char charAt3 = charAt(i5);
            if (charAt3 == ',') {
                this.token = 16;
                int i6 = this.bp + 1;
                this.bp = i6;
                this.ch = charAt(i6);
            } else if (charAt3 == ']') {
                this.token = 15;
                int i7 = this.bp + 1;
                this.bp = i7;
                this.ch = charAt(i7);
            } else if (charAt3 == '}') {
                this.token = 13;
                int i8 = this.bp + 1;
                this.bp = i8;
                this.ch = charAt(i8);
            } else if (charAt3 == 26) {
                this.token = 20;
            } else {
                this.bp = i2;
                this.ch = c;
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

    public boolean scanFieldBoolean(char[] cArr) {
        char c;
        boolean z;
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = this.bp + cArr.length;
        int i = length + 1;
        char charAt = charAt(length);
        if (charAt == 't') {
            int i2 = i + 1;
            if (charAt(i) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int i3 = i2 + 1;
            if (charAt(i2) != 'u') {
                this.matchStat = -1;
                return false;
            }
            int i4 = i3 + 1;
            if (charAt(i3) != 'e') {
                this.matchStat = -1;
                return false;
            }
            this.bp = i4;
            c = charAt(this.bp);
            z = true;
        } else if (charAt == 'f') {
            int i5 = i + 1;
            if (charAt(i) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int i6 = i5 + 1;
            if (charAt(i5) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int i7 = i6 + 1;
            if (charAt(i6) != 's') {
                this.matchStat = -1;
                return false;
            }
            int i8 = i7 + 1;
            if (charAt(i7) != 'e') {
                this.matchStat = -1;
                return false;
            }
            this.bp = i8;
            c = charAt(this.bp);
            z = false;
        } else {
            this.matchStat = -1;
            return false;
        }
        if (c == ',') {
            int i9 = this.bp + 1;
            this.bp = i9;
            this.ch = charAt(i9);
            this.matchStat = 3;
            this.token = 16;
        } else if (c == '}') {
            int i10 = this.bp + 1;
            this.bp = i10;
            char charAt2 = charAt(i10);
            if (charAt2 == ',') {
                this.token = 16;
                int i11 = this.bp + 1;
                this.bp = i11;
                this.ch = charAt(i11);
            } else if (charAt2 == ']') {
                this.token = 15;
                int i12 = this.bp + 1;
                this.bp = i12;
                this.ch = charAt(i12);
            } else if (charAt2 == '}') {
                this.token = 13;
                int i13 = this.bp + 1;
                this.bp = i13;
                this.ch = charAt(i13);
            } else if (charAt2 == 26) {
                this.token = 20;
            } else {
                this.matchStat = -1;
                return false;
            }
            this.matchStat = 4;
        } else {
            this.matchStat = -1;
            return false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        this.text.getChars(i, i3 + i, cArr, i2);
    }

    public String info() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("pos ");
        sb.append(this.bp);
        sb.append(", json : ");
        if (this.text.length() < 65536) {
            str = this.text;
        } else {
            str = this.text.substring(0, 65536);
        }
        sb.append(str);
        return sb.toString();
    }
}
