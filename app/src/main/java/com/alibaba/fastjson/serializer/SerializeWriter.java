package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import kotlin.UByte;
import kotlin.text.Typography;

public final class SerializeWriter extends Writer {
    private static final ThreadLocal<SoftReference<char[]>> bufLocal = new ThreadLocal<>();
    protected boolean beanToArray;
    protected boolean browserCompatible;
    protected boolean browserSecure;
    protected char[] buf;
    protected SoftReference<char[]> bufLocalRef;
    protected int count;
    protected boolean disableCheckSpecialChar;
    protected boolean disableCircularReferenceDetect;
    protected int features;
    protected boolean ignoreNonFieldGetter;
    protected char keySeperator;
    protected boolean notWriteDefaultValue;
    protected boolean notWriteRootClassName;
    protected boolean prettyFormat;
    protected boolean quoteFieldNames;
    protected boolean skipTransientField;
    protected boolean sortField;
    protected boolean useSingleQuotes;
    protected boolean writeClassName;
    protected boolean writeDirect;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected boolean writeMapNullValue;
    protected boolean writeNonStringValueAsString;
    private final Writer writer;

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(Writer writer2) {
        this.writer = writer2;
        this.features = JSON.DEFAULT_GENERATE_FEATURE;
        computeFeatures();
        this.bufLocalRef = bufLocal.get();
        SoftReference<char[]> softReference = this.bufLocalRef;
        if (softReference != null) {
            this.buf = softReference.get();
            bufLocal.set((Object) null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
    }

    public SerializeWriter(SerializerFeature... serializerFeatureArr) {
        this((Writer) null, serializerFeatureArr);
    }

    public SerializeWriter(Writer writer2, SerializerFeature... serializerFeatureArr) {
        this(writer2, 0, serializerFeatureArr);
    }

    public SerializeWriter(Writer writer2, int i, SerializerFeature... serializerFeatureArr) {
        this.writer = writer2;
        this.bufLocalRef = bufLocal.get();
        SoftReference<char[]> softReference = this.bufLocalRef;
        if (softReference != null) {
            this.buf = softReference.get();
            bufLocal.set((Object) null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
        for (SerializerFeature mask : serializerFeatureArr) {
            i |= mask.getMask();
        }
        this.features = i;
        computeFeatures();
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public SerializeWriter(int i) {
        this((Writer) null, i);
    }

    public SerializeWriter(Writer writer2, int i) {
        this.writer = writer2;
        if (i > 0) {
            this.buf = new char[i];
            return;
        }
        throw new IllegalArgumentException("Negative initial size: " + i);
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        if (z) {
            this.features |= serializerFeature.getMask();
            if (serializerFeature == SerializerFeature.WriteEnumUsingToString) {
                this.features &= ~SerializerFeature.WriteEnumUsingName.getMask();
            } else if (serializerFeature == SerializerFeature.WriteEnumUsingName) {
                this.features &= ~SerializerFeature.WriteEnumUsingToString.getMask();
            }
        } else {
            this.features = (~serializerFeature.getMask()) & this.features;
        }
        computeFeatures();
    }

    /* access modifiers changed from: protected */
    public void computeFeatures() {
        boolean z;
        boolean z2 = true;
        this.browserSecure = (this.features & SerializerFeature.BrowserSecure.mask) != 0;
        this.browserCompatible = (this.features & SerializerFeature.BrowserCompatible.mask) != 0;
        this.quoteFieldNames = (this.features & SerializerFeature.QuoteFieldNames.mask) != 0;
        this.useSingleQuotes = (this.features & SerializerFeature.UseSingleQuotes.mask) != 0;
        this.sortField = (this.features & SerializerFeature.SortField.mask) != 0;
        this.disableCircularReferenceDetect = (this.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0;
        this.beanToArray = (this.features & SerializerFeature.BeanToArray.mask) != 0;
        this.prettyFormat = (this.features & SerializerFeature.PrettyFormat.mask) != 0;
        this.writeClassName = (this.features & SerializerFeature.WriteClassName.mask) != 0;
        this.notWriteRootClassName = (this.features & SerializerFeature.NotWriteRootClassName.mask) != 0;
        this.skipTransientField = (this.features & SerializerFeature.SkipTransientField.mask) != 0;
        this.ignoreNonFieldGetter = (this.features & SerializerFeature.IgnoreNonFieldGetter.mask) != 0;
        this.writeNonStringValueAsString = (this.features & SerializerFeature.WriteNonStringValueAsString.mask) != 0;
        this.notWriteDefaultValue = (this.features & SerializerFeature.NotWriteDefaultValue.mask) != 0;
        this.writeEnumUsingName = (this.features & SerializerFeature.WriteEnumUsingName.mask) != 0;
        this.writeEnumUsingToString = (this.features & SerializerFeature.WriteEnumUsingToString.mask) != 0;
        this.writeMapNullValue = (this.features & SerializerFeature.WriteMapNullValue.mask) != 0;
        this.disableCheckSpecialChar = (this.features & SerializerFeature.DisableCheckSpecialChar.mask) != 0;
        if (!this.quoteFieldNames || this.useSingleQuotes || (z = this.browserCompatible) || this.browserSecure || z || !this.writeEnumUsingName || this.writeEnumUsingToString || this.writeNonStringValueAsString || (this.features & SerializerFeature.WriteSlashAsSpecial.mask) != 0) {
            z2 = false;
        }
        this.writeDirect = z2;
        this.keySeperator = this.useSingleQuotes ? '\'' : Typography.quote;
    }

    public boolean isPrettyFormat() {
        return this.prettyFormat;
    }

    public boolean isSortField() {
        return this.sortField;
    }

    public boolean isNotWriteDefaultValue() {
        return this.notWriteDefaultValue;
    }

    public boolean isWriteMapNullValue() {
        return this.writeMapNullValue;
    }

    public boolean isIgnoreNonFieldGetter() {
        return this.ignoreNonFieldGetter;
    }

    public boolean isSkipTransientField() {
        return this.skipTransientField;
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        return (serializerFeature.mask & this.features) != 0;
    }

    public void write(int i) {
        int i2 = this.count + 1;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                flush();
                i2 = 1;
            }
        }
        this.buf[this.count] = (char) i;
        this.count = i2;
    }

    public void write(char[] cArr, int i, int i2) {
        int i3;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            int i4 = this.count + i2;
            if (i4 > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(i4);
                } else {
                    do {
                        char[] cArr2 = this.buf;
                        int length = cArr2.length;
                        int i5 = this.count;
                        int i6 = length - i5;
                        System.arraycopy(cArr, i, cArr2, i5, i6);
                        this.count = this.buf.length;
                        flush();
                        i2 -= i6;
                        i += i6;
                    } while (i2 > this.buf.length);
                    i4 = i2;
                }
            }
            System.arraycopy(cArr, i, this.buf, this.count, i2);
            this.count = i4;
        }
    }

    public void expandCapacity(int i) {
        int length = ((this.buf.length * 3) / 2) + 1;
        if (length >= i) {
            i = length;
        }
        char[] cArr = new char[i];
        System.arraycopy(this.buf, 0, cArr, 0, this.count);
        this.buf = cArr;
    }

    public void write(String str, int i, int i2) {
        int i3;
        int i4 = this.count + i2;
        if (i4 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i4);
            } else {
                while (true) {
                    char[] cArr = this.buf;
                    int length = cArr.length;
                    int i5 = this.count;
                    int i6 = length - i5;
                    i3 = i + i6;
                    str.getChars(i, i3, cArr, i5);
                    this.count = this.buf.length;
                    flush();
                    i2 -= i6;
                    if (i2 <= this.buf.length) {
                        break;
                    }
                    i = i3;
                }
                i4 = i2;
                i = i3;
            }
        }
        str.getChars(i, i2 + i, this.buf, this.count);
        this.count = i4;
    }

    public void writeTo(Writer writer2) throws IOException {
        if (this.writer == null) {
            writer2.write(this.buf, 0, this.count);
            return;
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public void writeTo(OutputStream outputStream, String str) throws IOException {
        writeTo(outputStream, Charset.forName(str));
    }

    public void writeTo(OutputStream outputStream, Charset charset) throws IOException {
        if (this.writer == null) {
            outputStream.write(new String(this.buf, 0, this.count).getBytes(charset));
            return;
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public SerializeWriter append(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? "null" : charSequence.toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    public SerializeWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        String charSequence2 = charSequence.subSequence(i, i2).toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write((int) c);
        return this;
    }

    public void reset() {
        this.count = 0;
    }

    public char[] toCharArray() {
        if (this.writer == null) {
            int i = this.count;
            char[] cArr = new char[i];
            System.arraycopy(this.buf, 0, cArr, 0, i);
            return cArr;
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public byte[] toBytes(String str) {
        if (this.writer == null) {
            if (str == null) {
                str = Key.STRING_CHARSET_NAME;
            }
            return new SerialWriterStringEncoder(Charset.forName(str)).encode(this.buf, 0, this.count);
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public void close() {
        SoftReference<char[]> softReference;
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 8192) {
            SoftReference<char[]> softReference2 = this.bufLocalRef;
            if (softReference2 == null || softReference2.get() != this.buf) {
                softReference = new SoftReference<>(this.buf);
            } else {
                softReference = this.bufLocalRef;
            }
            bufLocal.set(softReference);
        }
        this.buf = null;
    }

    public void write(String str) {
        if (str == null) {
            writeNull();
        } else {
            write(str, 0, str.length());
        }
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int stringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int i2 = this.count + stringSize;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                char[] cArr = new char[stringSize];
                IOUtils.getChars(i, stringSize, cArr);
                write(cArr, 0, cArr.length);
                return;
            }
        }
        IOUtils.getChars(i, i2, this.buf);
        this.count = i2;
    }

    public void writeByteArray(byte[] bArr) {
        byte[] bArr2 = bArr;
        int length = bArr2.length;
        char c = this.useSingleQuotes ? '\'' : Typography.quote;
        if (length == 0) {
            write(this.useSingleQuotes ? "''" : "\"\"");
            return;
        }
        char[] cArr = IOUtils.CA;
        int i = (length / 3) * 3;
        int i2 = length - 1;
        int i3 = this.count;
        int i4 = (((i2 / 3) + 1) << 2) + i3 + 2;
        int i5 = 0;
        if (i4 > this.buf.length) {
            if (this.writer != null) {
                write((int) c);
                int i6 = 0;
                while (i6 < i) {
                    int i7 = i6 + 1;
                    int i8 = i7 + 1;
                    byte b = ((bArr2[i6] & UByte.MAX_VALUE) << 16) | ((bArr2[i7] & UByte.MAX_VALUE) << 8) | (bArr2[i8] & UByte.MAX_VALUE);
                    write((int) cArr[(b >>> 18) & 63]);
                    write((int) cArr[(b >>> 12) & 63]);
                    write((int) cArr[(b >>> 6) & 63]);
                    write((int) cArr[b & 63]);
                    i6 = i8 + 1;
                }
                int i9 = length - i;
                if (i9 > 0) {
                    int i10 = (bArr2[i] & UByte.MAX_VALUE) << 10;
                    if (i9 == 2) {
                        i5 = (bArr2[i2] & UByte.MAX_VALUE) << 2;
                    }
                    int i11 = i10 | i5;
                    write((int) cArr[i11 >> 12]);
                    write((int) cArr[(i11 >>> 6) & 63]);
                    write((int) i9 == 2 ? cArr[i11 & 63] : '=');
                    write(61);
                }
                write((int) c);
                return;
            }
            expandCapacity(i4);
        }
        this.count = i4;
        int i12 = i3 + 1;
        this.buf[i3] = c;
        int i13 = 0;
        while (i13 < i) {
            int i14 = i13 + 1;
            int i15 = i14 + 1;
            byte b2 = ((bArr2[i13] & UByte.MAX_VALUE) << 16) | ((bArr2[i14] & UByte.MAX_VALUE) << 8);
            int i16 = i15 + 1;
            byte b3 = b2 | (bArr2[i15] & UByte.MAX_VALUE);
            char[] cArr2 = this.buf;
            int i17 = i12 + 1;
            cArr2[i12] = cArr[(b3 >>> 18) & 63];
            int i18 = i17 + 1;
            cArr2[i17] = cArr[(b3 >>> 12) & 63];
            int i19 = i18 + 1;
            cArr2[i18] = cArr[(b3 >>> 6) & 63];
            i12 = i19 + 1;
            cArr2[i19] = cArr[b3 & 63];
            i13 = i16;
        }
        int i20 = length - i;
        if (i20 > 0) {
            int i21 = (bArr2[i] & UByte.MAX_VALUE) << 10;
            if (i20 == 2) {
                i5 = (bArr2[i2] & UByte.MAX_VALUE) << 2;
            }
            int i22 = i21 | i5;
            char[] cArr3 = this.buf;
            cArr3[i4 - 5] = cArr[i22 >> 12];
            cArr3[i4 - 4] = cArr[(i22 >>> 6) & 63];
            cArr3[i4 - 3] = i20 == 2 ? cArr[i22 & 63] : '=';
            this.buf[i4 - 2] = '=';
        }
        this.buf[i4 - 1] = c;
    }

    public void writeFloatAndChar(float f, char c) {
        String f2 = Float.toString(f);
        if (f2.endsWith(".0")) {
            f2 = f2.substring(0, f2.length() - 2);
        }
        write(f2);
        write((int) c);
    }

    public void writeDoubleAndChar(double d, char c) {
        String d2 = Double.toString(d);
        if (d2.endsWith(".0")) {
            d2 = d2.substring(0, d2.length() - 2);
        }
        write(d2);
        write((int) c);
    }

    public void writeBooleanAndChar(boolean z, char c) {
        if (z) {
            if (c == ',') {
                write("true,");
            } else if (c == ']') {
                write("true]");
            } else {
                write("true");
                write((int) c);
            }
        } else if (c == ',') {
            write("false,");
        } else if (c == ']') {
            write("false]");
        } else {
            write("false");
            write((int) c);
        }
    }

    public void writeCharacterAndChar(char c, char c2) {
        writeString(Character.toString(c));
        write((int) c2);
    }

    public void writeEnum(Enum<?> enumR, char c) {
        if (enumR == null) {
            writeNull();
            write(44);
        } else if (isEnabled(SerializerFeature.WriteEnumUsingName)) {
            writeEnumValue(enumR.name(), c);
        } else if (isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            writeEnumValue(enumR.toString(), c);
        } else {
            writeIntAndChar(enumR.ordinal(), c);
        }
    }

    private void writeEnumValue(String str, char c) {
        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            write(39);
            write(str);
            write(39);
            write((int) c);
            return;
        }
        write(34);
        write(str);
        write(34);
        write((int) c);
    }

    public void writeIntAndChar(int i, char c) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            write((int) c);
            return;
        }
        int stringSize = this.count + (i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i));
        int i2 = stringSize + 1;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                writeInt(i);
                write((int) c);
                return;
            }
            expandCapacity(i2);
        }
        IOUtils.getChars(i, stringSize, this.buf);
        this.buf[stringSize] = c;
        this.count = i2;
    }

    public void writeLongAndChar(long j, char c) throws IOException {
        boolean z = this.browserCompatible && !isEnabled(SerializerFeature.WriteClassName) && (j > 9007199254740991L || j < -9007199254740991L);
        if (j == Long.MIN_VALUE) {
            if (z) {
                write("\"-9223372036854775808\"");
            } else {
                write("-9223372036854775808");
            }
            write((int) c);
            return;
        }
        int stringSize = this.count + (j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j));
        if (z) {
            stringSize += 2;
        }
        int i = stringSize + 1;
        if (i > this.buf.length) {
            if (this.writer != null) {
                writeLong(j);
                write((int) c);
                return;
            }
            expandCapacity(i);
        }
        if (z) {
            char[] cArr = this.buf;
            cArr[this.count] = Typography.quote;
            int i2 = stringSize - 1;
            IOUtils.getChars(j, i2, cArr);
            this.buf[i2] = Typography.quote;
        } else {
            IOUtils.getChars(j, stringSize, this.buf);
        }
        this.buf[stringSize] = c;
        this.count = i;
    }

    public void writeLong(long j) {
        boolean z = this.browserCompatible && !isEnabled(SerializerFeature.WriteClassName) && (j > 9007199254740991L || j < -9007199254740991L);
        if (j != Long.MIN_VALUE) {
            int stringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
            int i = this.count + stringSize;
            if (z) {
                i += 2;
            }
            if (i > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(i);
                } else {
                    char[] cArr = new char[stringSize];
                    IOUtils.getChars(j, stringSize, cArr);
                    if (z) {
                        write(34);
                        write(cArr, 0, cArr.length);
                        write(34);
                        return;
                    }
                    write(cArr, 0, cArr.length);
                    return;
                }
            }
            if (z) {
                char[] cArr2 = this.buf;
                cArr2[this.count] = Typography.quote;
                int i2 = i - 1;
                IOUtils.getChars(j, i2, cArr2);
                this.buf[i2] = Typography.quote;
            } else {
                IOUtils.getChars(j, i, this.buf);
            }
            this.count = i;
        } else if (z) {
            write("\"-9223372036854775808\"");
        } else {
            write("-9223372036854775808");
        }
    }

    public void writeNull() {
        write("null");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:187:0x0395, code lost:
        if (r11 == -1) goto L_0x0397;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:207:0x03cb, code lost:
        if (r11 == -1) goto L_0x0397;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeStringWithDoubleQuote(java.lang.String r19, char r20) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            if (r1 != 0) goto L_0x0011
            r18.writeNull()
            if (r2 == 0) goto L_0x0010
            r0.write((int) r2)
        L_0x0010:
            return
        L_0x0011:
            int r3 = r19.length()
            int r4 = r0.count
            int r4 = r4 + r3
            int r4 = r4 + 2
            if (r2 == 0) goto L_0x001e
            int r4 = r4 + 1
        L_0x001e:
            char[] r5 = r0.buf
            int r5 = r5.length
            r6 = 0
            r7 = 47
            r8 = 48
            r9 = 117(0x75, float:1.64E-43)
            r10 = 12
            r11 = 8
            r12 = 34
            r13 = 92
            r14 = 4
            r15 = 1
            if (r4 <= r5) goto L_0x0182
            java.io.Writer r5 = r0.writer
            if (r5 == 0) goto L_0x017f
            r0.write((int) r12)
        L_0x003b:
            int r3 = r19.length()
            if (r6 >= r3) goto L_0x0176
            char r3 = r1.charAt(r6)
            boolean r4 = r0.browserSecure
            if (r4 == 0) goto L_0x009d
            if (r3 < r8) goto L_0x004f
            r4 = 57
            if (r3 <= r4) goto L_0x016f
        L_0x004f:
            r4 = 97
            if (r3 < r4) goto L_0x0057
            r4 = 122(0x7a, float:1.71E-43)
            if (r3 <= r4) goto L_0x016f
        L_0x0057:
            r4 = 65
            if (r3 < r4) goto L_0x005f
            r4 = 90
            if (r3 <= r4) goto L_0x016f
        L_0x005f:
            r4 = 44
            if (r3 == r4) goto L_0x016f
            r4 = 46
            if (r3 == r4) goto L_0x016f
            r4 = 95
            if (r3 == r4) goto L_0x016f
            r0.write((int) r13)
            r0.write((int) r9)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 12
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 8
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 4
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r3 = r3 & 15
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0172
        L_0x009d:
            boolean r4 = r0.browserCompatible
            if (r4 == 0) goto L_0x011b
            if (r3 == r11) goto L_0x0110
            if (r3 == r10) goto L_0x0110
            r4 = 10
            if (r3 == r4) goto L_0x0110
            r4 = 13
            if (r3 == r4) goto L_0x0110
            r4 = 9
            if (r3 == r4) goto L_0x0110
            if (r3 == r12) goto L_0x0110
            if (r3 == r7) goto L_0x0110
            if (r3 != r13) goto L_0x00b8
            goto L_0x0110
        L_0x00b8:
            r4 = 32
            if (r3 >= r4) goto L_0x00db
            r0.write((int) r13)
            r0.write((int) r9)
            r0.write((int) r8)
            r0.write((int) r8)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r3 = r3 * 2
            char r4 = r4[r3]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r3 = r3 + r15
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0172
        L_0x00db:
            r4 = 127(0x7f, float:1.78E-43)
            if (r3 < r4) goto L_0x016f
            r0.write((int) r13)
            r0.write((int) r9)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 12
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 8
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 4
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r3 = r3 & 15
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0172
        L_0x0110:
            r0.write((int) r13)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0172
        L_0x011b:
            byte[] r4 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r4 = r4.length
            if (r3 >= r4) goto L_0x0126
            byte[] r4 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r4 = r4[r3]
            if (r4 != 0) goto L_0x0130
        L_0x0126:
            if (r3 != r7) goto L_0x016f
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            boolean r4 = r0.isEnabled(r4)
            if (r4 == 0) goto L_0x016f
        L_0x0130:
            r0.write((int) r13)
            byte[] r4 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r4 = r4[r3]
            if (r4 != r14) goto L_0x0167
            r0.write((int) r9)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 12
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 8
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 4
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r3 = r3 & 15
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0172
        L_0x0167:
            char[] r4 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0172
        L_0x016f:
            r0.write((int) r3)
        L_0x0172:
            int r6 = r6 + 1
            goto L_0x003b
        L_0x0176:
            r0.write((int) r12)
            if (r2 == 0) goto L_0x017e
            r0.write((int) r2)
        L_0x017e:
            return
        L_0x017f:
            r0.expandCapacity(r4)
        L_0x0182:
            int r5 = r0.count
            int r14 = r5 + 1
            int r10 = r14 + r3
            char[] r11 = r0.buf
            r11[r5] = r12
            r1.getChars(r6, r3, r11, r14)
            r0.count = r4
            boolean r3 = r0.browserSecure
            r5 = -1
            if (r3 == 0) goto L_0x0258
            r1 = r14
        L_0x0197:
            if (r1 >= r10) goto L_0x01c5
            char[] r3 = r0.buf
            char r3 = r3[r1]
            if (r3 < r8) goto L_0x01a3
            r6 = 57
            if (r3 <= r6) goto L_0x01c2
        L_0x01a3:
            r6 = 97
            if (r3 < r6) goto L_0x01ab
            r6 = 122(0x7a, float:1.71E-43)
            if (r3 <= r6) goto L_0x01c2
        L_0x01ab:
            r6 = 65
            if (r3 < r6) goto L_0x01b3
            r6 = 90
            if (r3 <= r6) goto L_0x01c2
        L_0x01b3:
            r6 = 44
            if (r3 == r6) goto L_0x01c2
            r6 = 46
            if (r3 == r6) goto L_0x01c2
            r6 = 95
            if (r3 == r6) goto L_0x01c2
            int r4 = r4 + 5
            r5 = r1
        L_0x01c2:
            int r1 = r1 + 1
            goto L_0x0197
        L_0x01c5:
            char[] r1 = r0.buf
            int r1 = r1.length
            if (r4 <= r1) goto L_0x01cd
            r0.expandCapacity(r4)
        L_0x01cd:
            r0.count = r4
        L_0x01cf:
            if (r5 < r14) goto L_0x0242
            char[] r1 = r0.buf
            char r1 = r1[r5]
            if (r1 < r8) goto L_0x01db
            r3 = 57
            if (r1 <= r3) goto L_0x023f
        L_0x01db:
            r3 = 97
            if (r1 < r3) goto L_0x01e3
            r3 = 122(0x7a, float:1.71E-43)
            if (r1 <= r3) goto L_0x023f
        L_0x01e3:
            r3 = 65
            if (r1 < r3) goto L_0x01eb
            r3 = 90
            if (r1 <= r3) goto L_0x023f
        L_0x01eb:
            r3 = 44
            if (r1 == r3) goto L_0x023f
            r3 = 46
            if (r1 == r3) goto L_0x023f
            r3 = 95
            if (r1 == r3) goto L_0x023f
            char[] r3 = r0.buf
            int r4 = r5 + 1
            int r6 = r5 + 6
            int r7 = r10 - r5
            int r7 = r7 - r15
            java.lang.System.arraycopy(r3, r4, r3, r6, r7)
            char[] r3 = r0.buf
            r3[r5] = r13
            r3[r4] = r9
            int r4 = r5 + 2
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r7 = r1 >>> 12
            r7 = r7 & 15
            char r6 = r6[r7]
            r3[r4] = r6
            char[] r3 = r0.buf
            int r4 = r5 + 3
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r7 = r1 >>> 8
            r7 = r7 & 15
            char r6 = r6[r7]
            r3[r4] = r6
            char[] r3 = r0.buf
            int r4 = r5 + 4
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r7 = r1 >>> 4
            r7 = r7 & 15
            char r6 = r6[r7]
            r3[r4] = r6
            char[] r3 = r0.buf
            int r4 = r5 + 5
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r1 = r1 & 15
            char r1 = r6[r1]
            r3[r4] = r1
            int r10 = r10 + 5
        L_0x023f:
            int r5 = r5 + -1
            goto L_0x01cf
        L_0x0242:
            if (r2 == 0) goto L_0x0250
            char[] r1 = r0.buf
            int r3 = r0.count
            int r4 = r3 + -2
            r1[r4] = r12
            int r3 = r3 - r15
            r1[r3] = r2
            goto L_0x0257
        L_0x0250:
            char[] r1 = r0.buf
            int r2 = r0.count
            int r2 = r2 - r15
            r1[r2] = r12
        L_0x0257:
            return
        L_0x0258:
            boolean r3 = r0.browserCompatible
            if (r3 == 0) goto L_0x0382
            r1 = r14
        L_0x025d:
            if (r1 >= r10) goto L_0x0291
            char[] r3 = r0.buf
            char r3 = r3[r1]
            if (r3 == r12) goto L_0x028b
            if (r3 == r7) goto L_0x028b
            if (r3 != r13) goto L_0x026a
            goto L_0x028b
        L_0x026a:
            r6 = 8
            if (r3 == r6) goto L_0x028b
            r6 = 12
            if (r3 == r6) goto L_0x028b
            r6 = 10
            if (r3 == r6) goto L_0x028b
            r6 = 13
            if (r3 == r6) goto L_0x028b
            r6 = 9
            if (r3 != r6) goto L_0x027f
            goto L_0x028b
        L_0x027f:
            r6 = 32
            if (r3 >= r6) goto L_0x0286
        L_0x0283:
            int r4 = r4 + 5
            goto L_0x028d
        L_0x0286:
            r6 = 127(0x7f, float:1.78E-43)
            if (r3 < r6) goto L_0x028e
            goto L_0x0283
        L_0x028b:
            int r4 = r4 + 1
        L_0x028d:
            r5 = r1
        L_0x028e:
            int r1 = r1 + 1
            goto L_0x025d
        L_0x0291:
            char[] r1 = r0.buf
            int r1 = r1.length
            if (r4 <= r1) goto L_0x0299
            r0.expandCapacity(r4)
        L_0x0299:
            r0.count = r4
        L_0x029b:
            if (r5 < r14) goto L_0x036c
            char[] r1 = r0.buf
            char r3 = r1[r5]
            r4 = 8
            if (r3 == r4) goto L_0x0350
            r6 = 12
            if (r3 == r6) goto L_0x0350
            r11 = 10
            if (r3 == r11) goto L_0x0350
            r11 = 13
            if (r3 == r11) goto L_0x0350
            r11 = 9
            if (r3 != r11) goto L_0x02b7
            goto L_0x0350
        L_0x02b7:
            if (r3 == r12) goto L_0x033d
            if (r3 == r7) goto L_0x033d
            if (r3 != r13) goto L_0x02bf
            goto L_0x033d
        L_0x02bf:
            r11 = 32
            if (r3 >= r11) goto L_0x02f4
            int r11 = r5 + 1
            int r4 = r5 + 6
            int r16 = r10 - r5
            int r6 = r16 + -1
            java.lang.System.arraycopy(r1, r11, r1, r4, r6)
            char[] r1 = r0.buf
            r1[r5] = r13
            r1[r11] = r9
            int r4 = r5 + 2
            r1[r4] = r8
            int r4 = r5 + 3
            r1[r4] = r8
            int r4 = r5 + 4
            char[] r6 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r3 = r3 * 2
            char r6 = r6[r3]
            r1[r4] = r6
            char[] r1 = r0.buf
            int r4 = r5 + 5
            char[] r6 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r3 = r3 + r15
            char r3 = r6[r3]
            r1[r4] = r3
        L_0x02f1:
            int r10 = r10 + 5
            goto L_0x0368
        L_0x02f4:
            r4 = 127(0x7f, float:1.78E-43)
            if (r3 < r4) goto L_0x0368
            int r4 = r5 + 1
            int r6 = r5 + 6
            int r11 = r10 - r5
            int r11 = r11 - r15
            java.lang.System.arraycopy(r1, r4, r1, r6, r11)
            char[] r1 = r0.buf
            r1[r5] = r13
            r1[r4] = r9
            int r4 = r5 + 2
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r11 = r3 >>> 12
            r11 = r11 & 15
            char r6 = r6[r11]
            r1[r4] = r6
            char[] r1 = r0.buf
            int r4 = r5 + 3
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r11 = r3 >>> 8
            r11 = r11 & 15
            char r6 = r6[r11]
            r1[r4] = r6
            char[] r1 = r0.buf
            int r4 = r5 + 4
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r11 = r3 >>> 4
            r11 = r11 & 15
            char r6 = r6[r11]
            r1[r4] = r6
            char[] r1 = r0.buf
            int r4 = r5 + 5
            char[] r6 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r3 = r3 & 15
            char r3 = r6[r3]
            r1[r4] = r3
            goto L_0x02f1
        L_0x033d:
            char[] r1 = r0.buf
            int r4 = r5 + 1
            int r6 = r5 + 2
            int r11 = r10 - r5
            int r11 = r11 - r15
            java.lang.System.arraycopy(r1, r4, r1, r6, r11)
            char[] r1 = r0.buf
            r1[r5] = r13
            r1[r4] = r3
            goto L_0x0366
        L_0x0350:
            char[] r1 = r0.buf
            int r4 = r5 + 1
            int r6 = r5 + 2
            int r11 = r10 - r5
            int r11 = r11 - r15
            java.lang.System.arraycopy(r1, r4, r1, r6, r11)
            char[] r1 = r0.buf
            r1[r5] = r13
            char[] r6 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r3 = r6[r3]
            r1[r4] = r3
        L_0x0366:
            int r10 = r10 + 1
        L_0x0368:
            int r5 = r5 + -1
            goto L_0x029b
        L_0x036c:
            if (r2 == 0) goto L_0x037a
            char[] r1 = r0.buf
            int r3 = r0.count
            int r4 = r3 + -2
            r1[r4] = r12
            int r3 = r3 - r15
            r1[r3] = r2
            goto L_0x0381
        L_0x037a:
            char[] r1 = r0.buf
            int r2 = r0.count
            int r2 = r2 - r15
            r1[r2] = r12
        L_0x0381:
            return
        L_0x0382:
            r3 = r14
            r11 = -1
            r12 = 0
            r17 = -1
        L_0x0387:
            if (r3 >= r10) goto L_0x03d5
            char[] r7 = r0.buf
            char r7 = r7[r3]
            r8 = 8232(0x2028, float:1.1535E-41)
            if (r7 != r8) goto L_0x039f
            int r6 = r6 + 1
            int r4 = r4 + 4
            if (r11 != r5) goto L_0x039b
        L_0x0397:
            r11 = r3
            r17 = r11
            goto L_0x039d
        L_0x039b:
            r17 = r3
        L_0x039d:
            r12 = r7
            goto L_0x03ce
        L_0x039f:
            r8 = 93
            if (r7 < r8) goto L_0x03b3
            r8 = 127(0x7f, float:1.78E-43)
            if (r7 < r8) goto L_0x03ce
            r8 = 160(0xa0, float:2.24E-43)
            if (r7 > r8) goto L_0x03ce
            if (r11 != r5) goto L_0x03ae
            r11 = r3
        L_0x03ae:
            int r6 = r6 + 1
            int r4 = r4 + 4
            goto L_0x039b
        L_0x03b3:
            int r8 = r0.features
            boolean r8 = isSpecial(r7, r8)
            if (r8 == 0) goto L_0x03ce
            int r6 = r6 + 1
            byte[] r8 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r8 = r8.length
            if (r7 >= r8) goto L_0x03cb
            byte[] r8 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r8 = r8[r7]
            r12 = 4
            if (r8 != r12) goto L_0x03cb
            int r4 = r4 + 4
        L_0x03cb:
            if (r11 != r5) goto L_0x039b
            goto L_0x0397
        L_0x03ce:
            int r3 = r3 + 1
            r7 = 47
            r8 = 48
            goto L_0x0387
        L_0x03d5:
            if (r6 <= 0) goto L_0x0548
            int r4 = r4 + r6
            char[] r3 = r0.buf
            int r3 = r3.length
            if (r4 <= r3) goto L_0x03e0
            r0.expandCapacity(r4)
        L_0x03e0:
            r0.count = r4
            if (r6 != r15) goto L_0x047c
            r1 = 8232(0x2028, float:1.1535E-41)
            if (r12 != r1) goto L_0x0410
            int r1 = r17 + 1
            int r3 = r17 + 6
            int r10 = r10 - r17
            int r10 = r10 - r15
            char[] r4 = r0.buf
            java.lang.System.arraycopy(r4, r1, r4, r3, r10)
            char[] r3 = r0.buf
            r3[r17] = r13
            r3[r1] = r9
            int r1 = r1 + r15
            r4 = 50
            r3[r1] = r4
            int r1 = r1 + r15
            r4 = 48
            r3[r1] = r4
            int r1 = r1 + r15
            r4 = 50
            r3[r1] = r4
            int r1 = r1 + r15
            r4 = 56
            r3[r1] = r4
            goto L_0x0548
        L_0x0410:
            byte[] r1 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r1 = r1.length
            if (r12 >= r1) goto L_0x0464
            byte[] r1 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r1 = r1[r12]
            r3 = 4
            if (r1 != r3) goto L_0x0464
            int r1 = r17 + 1
            int r3 = r17 + 6
            int r10 = r10 - r17
            int r10 = r10 - r15
            char[] r4 = r0.buf
            java.lang.System.arraycopy(r4, r1, r4, r3, r10)
            char[] r3 = r0.buf
            r3[r17] = r13
            int r4 = r1 + 1
            r3[r1] = r9
            int r1 = r4 + 1
            char[] r5 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r6 = r12 >>> 12
            r6 = r6 & 15
            char r5 = r5[r6]
            r3[r4] = r5
            char[] r3 = r0.buf
            int r4 = r1 + 1
            char[] r5 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r6 = r12 >>> 8
            r6 = r6 & 15
            char r5 = r5[r6]
            r3[r1] = r5
            char[] r1 = r0.buf
            int r3 = r4 + 1
            char[] r5 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r6 = r12 >>> 4
            r6 = r6 & 15
            char r5 = r5[r6]
            r1[r4] = r5
            char[] r1 = r0.buf
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r5 = r12 & 15
            char r4 = r4[r5]
            r1[r3] = r4
            goto L_0x0548
        L_0x0464:
            int r1 = r17 + 1
            int r3 = r17 + 2
            int r10 = r10 - r17
            int r10 = r10 - r15
            char[] r4 = r0.buf
            java.lang.System.arraycopy(r4, r1, r4, r3, r10)
            char[] r3 = r0.buf
            r3[r17] = r13
            char[] r4 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r4 = r4[r12]
            r3[r1] = r4
            goto L_0x0548
        L_0x047c:
            if (r6 <= r15) goto L_0x0548
            int r3 = r11 - r14
        L_0x0480:
            int r4 = r19.length()
            if (r3 >= r4) goto L_0x0548
            char r4 = r1.charAt(r3)
            byte[] r5 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r5 = r5.length
            if (r4 >= r5) goto L_0x0499
            byte[] r5 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r5 = r5[r4]
            if (r5 != 0) goto L_0x0496
            goto L_0x0499
        L_0x0496:
            r5 = 47
            goto L_0x04a5
        L_0x0499:
            r5 = 47
            if (r4 != r5) goto L_0x04f8
            com.alibaba.fastjson.serializer.SerializerFeature r6 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            boolean r6 = r0.isEnabled(r6)
            if (r6 == 0) goto L_0x04f8
        L_0x04a5:
            char[] r6 = r0.buf
            int r7 = r11 + 1
            r6[r11] = r13
            byte[] r6 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r6 = r6[r4]
            r8 = 4
            if (r6 != r8) goto L_0x04ed
            char[] r6 = r0.buf
            int r10 = r7 + 1
            r6[r7] = r9
            int r7 = r10 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r12 = r4 >>> 12
            r12 = r12 & 15
            char r11 = r11[r12]
            r6[r10] = r11
            char[] r6 = r0.buf
            int r10 = r7 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r12 = r4 >>> 8
            r12 = r12 & 15
            char r11 = r11[r12]
            r6[r7] = r11
            char[] r6 = r0.buf
            int r7 = r10 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r12 = r4 >>> 4
            r12 = r12 & 15
            char r11 = r11[r12]
            r6[r10] = r11
            char[] r6 = r0.buf
            int r10 = r7 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r4 = r4 & 15
            char r4 = r11[r4]
            r6[r7] = r4
            goto L_0x053b
        L_0x04ed:
            char[] r6 = r0.buf
            int r10 = r7 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r4 = r11[r4]
            r6[r7] = r4
            goto L_0x053b
        L_0x04f8:
            r8 = 4
            r6 = 8232(0x2028, float:1.1535E-41)
            if (r4 != r6) goto L_0x053d
            char[] r6 = r0.buf
            int r7 = r11 + 1
            r6[r11] = r13
            int r10 = r7 + 1
            r6[r7] = r9
            int r7 = r10 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r12 = r4 >>> 12
            r12 = r12 & 15
            char r11 = r11[r12]
            r6[r10] = r11
            char[] r6 = r0.buf
            int r10 = r7 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r12 = r4 >>> 8
            r12 = r12 & 15
            char r11 = r11[r12]
            r6[r7] = r11
            char[] r6 = r0.buf
            int r7 = r10 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r12 = r4 >>> 4
            r12 = r12 & 15
            char r11 = r11[r12]
            r6[r10] = r11
            char[] r6 = r0.buf
            int r10 = r7 + 1
            char[] r11 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r4 = r4 & 15
            char r4 = r11[r4]
            r6[r7] = r4
        L_0x053b:
            r11 = r10
            goto L_0x0544
        L_0x053d:
            char[] r6 = r0.buf
            int r7 = r11 + 1
            r6[r11] = r4
            r11 = r7
        L_0x0544:
            int r3 = r3 + 1
            goto L_0x0480
        L_0x0548:
            if (r2 == 0) goto L_0x0558
            char[] r1 = r0.buf
            int r3 = r0.count
            int r4 = r3 + -2
            r5 = 34
            r1[r4] = r5
            int r3 = r3 - r15
            r1[r3] = r2
            goto L_0x0561
        L_0x0558:
            r5 = 34
            char[] r1 = r0.buf
            int r2 = r0.count
            int r2 = r2 - r15
            r1[r2] = r5
        L_0x0561:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithDoubleQuote(java.lang.String, char):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c1, code lost:
        if (r14 == -1) goto L_0x00c3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00fb, code lost:
        if (r14 == -1) goto L_0x00c3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeStringWithDoubleQuoteDirect(java.lang.String r19, char r20) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            if (r1 != 0) goto L_0x0011
            r18.writeNull()
            if (r2 == 0) goto L_0x0010
            r0.write((int) r2)
        L_0x0010:
            return
        L_0x0011:
            int r3 = r19.length()
            int r4 = r0.count
            int r4 = r4 + r3
            int r4 = r4 + 2
            if (r2 == 0) goto L_0x001e
            int r4 = r4 + 1
        L_0x001e:
            char[] r5 = r0.buf
            int r5 = r5.length
            r6 = 117(0x75, float:1.64E-43)
            r7 = 0
            r8 = 34
            r9 = 92
            r10 = 4
            if (r4 <= r5) goto L_0x0098
            java.io.Writer r5 = r0.writer
            if (r5 == 0) goto L_0x0095
            r0.write((int) r8)
        L_0x0032:
            int r3 = r19.length()
            if (r7 >= r3) goto L_0x008c
            char r3 = r1.charAt(r7)
            byte[] r4 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r4 = r4.length
            if (r3 >= r4) goto L_0x0086
            byte[] r4 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r4 = r4[r3]
            if (r4 == 0) goto L_0x0086
            r0.write((int) r9)
            byte[] r4 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r4 = r4[r3]
            if (r4 != r10) goto L_0x007e
            r0.write((int) r6)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 12
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 8
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r5 = r3 >>> 4
            r5 = r5 & 15
            char r4 = r4[r5]
            r0.write((int) r4)
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r3 = r3 & 15
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0089
        L_0x007e:
            char[] r4 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r3 = r4[r3]
            r0.write((int) r3)
            goto L_0x0089
        L_0x0086:
            r0.write((int) r3)
        L_0x0089:
            int r7 = r7 + 1
            goto L_0x0032
        L_0x008c:
            r0.write((int) r8)
            if (r2 == 0) goto L_0x0094
            r0.write((int) r2)
        L_0x0094:
            return
        L_0x0095:
            r0.expandCapacity(r4)
        L_0x0098:
            int r5 = r0.count
            int r11 = r5 + 1
            int r12 = r11 + r3
            char[] r13 = r0.buf
            r13[r5] = r8
            r1.getChars(r7, r3, r13, r11)
            r0.count = r4
            r3 = -1
            r13 = r4
            r4 = r11
            r5 = 0
            r14 = -1
            r15 = 0
            r16 = -1
        L_0x00af:
            r7 = 8232(0x2028, float:1.1535E-41)
            if (r4 >= r12) goto L_0x0104
            char[] r6 = r0.buf
            char r6 = r6[r4]
            r10 = 93
            if (r6 < r10) goto L_0x00db
            if (r6 != r7) goto L_0x00cb
            int r5 = r5 + 1
            int r13 = r13 + 4
            if (r14 != r3) goto L_0x00c7
        L_0x00c3:
            r14 = r4
            r16 = r14
            goto L_0x00c9
        L_0x00c7:
            r16 = r4
        L_0x00c9:
            r15 = r6
            goto L_0x00fe
        L_0x00cb:
            r7 = 127(0x7f, float:1.78E-43)
            if (r6 < r7) goto L_0x00fe
            r7 = 160(0xa0, float:2.24E-43)
            if (r6 > r7) goto L_0x00fe
            if (r14 != r3) goto L_0x00d6
            r14 = r4
        L_0x00d6:
            int r5 = r5 + 1
            int r13 = r13 + 4
            goto L_0x00c7
        L_0x00db:
            r7 = 31
            if (r6 <= r7) goto L_0x00e7
            if (r6 == r9) goto L_0x00e7
            if (r6 != r8) goto L_0x00e4
            goto L_0x00e7
        L_0x00e4:
            r17 = 0
            goto L_0x00e9
        L_0x00e7:
            r17 = 1
        L_0x00e9:
            if (r17 == 0) goto L_0x00fe
            int r5 = r5 + 1
            byte[] r7 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r7 = r7.length
            if (r6 >= r7) goto L_0x00fb
            byte[] r7 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r7 = r7[r6]
            r10 = 4
            if (r7 != r10) goto L_0x00fb
            int r13 = r13 + 4
        L_0x00fb:
            if (r14 != r3) goto L_0x00c7
            goto L_0x00c3
        L_0x00fe:
            int r4 = r4 + 1
            r6 = 117(0x75, float:1.64E-43)
            r10 = 4
            goto L_0x00af
        L_0x0104:
            if (r5 <= 0) goto L_0x027f
            int r13 = r13 + r5
            char[] r3 = r0.buf
            int r3 = r3.length
            if (r13 <= r3) goto L_0x010f
            r0.expandCapacity(r13)
        L_0x010f:
            r0.count = r13
            r3 = 1
            if (r5 != r3) goto L_0x01af
            if (r15 != r7) goto L_0x013e
            int r1 = r16 + 1
            int r4 = r16 + 6
            int r12 = r12 - r16
            int r12 = r12 - r3
            char[] r5 = r0.buf
            java.lang.System.arraycopy(r5, r1, r5, r4, r12)
            char[] r4 = r0.buf
            r4[r16] = r9
            r5 = 117(0x75, float:1.64E-43)
            r4[r1] = r5
            int r1 = r1 + r3
            r5 = 50
            r4[r1] = r5
            int r1 = r1 + r3
            r6 = 48
            r4[r1] = r6
            int r1 = r1 + r3
            r4[r1] = r5
            int r1 = r1 + r3
            r3 = 56
            r4[r1] = r3
            goto L_0x027f
        L_0x013e:
            byte[] r1 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r1 = r1.length
            if (r15 >= r1) goto L_0x0196
            byte[] r1 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r1 = r1[r15]
            r3 = 4
            if (r1 != r3) goto L_0x0196
            int r1 = r16 + 1
            int r3 = r16 + 6
            int r12 = r12 - r16
            r4 = 1
            int r12 = r12 - r4
            char[] r4 = r0.buf
            java.lang.System.arraycopy(r4, r1, r4, r3, r12)
            char[] r3 = r0.buf
            r3[r16] = r9
            int r4 = r1 + 1
            r5 = 117(0x75, float:1.64E-43)
            r3[r1] = r5
            int r1 = r4 + 1
            char[] r5 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r6 = r15 >>> 12
            r6 = r6 & 15
            char r5 = r5[r6]
            r3[r4] = r5
            char[] r3 = r0.buf
            int r4 = r1 + 1
            char[] r5 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r6 = r15 >>> 8
            r6 = r6 & 15
            char r5 = r5[r6]
            r3[r1] = r5
            char[] r1 = r0.buf
            int r3 = r4 + 1
            char[] r5 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r6 = r15 >>> 4
            r6 = r6 & 15
            char r5 = r5[r6]
            r1[r4] = r5
            char[] r1 = r0.buf
            char[] r4 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r5 = r15 & 15
            char r4 = r4[r5]
            r1[r3] = r4
            r4 = 1
            goto L_0x027f
        L_0x0196:
            int r1 = r16 + 1
            int r3 = r16 + 2
            int r12 = r12 - r16
            r4 = 1
            int r12 = r12 - r4
            char[] r5 = r0.buf
            java.lang.System.arraycopy(r5, r1, r5, r3, r12)
            char[] r3 = r0.buf
            r3[r16] = r9
            char[] r5 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r5 = r5[r15]
            r3[r1] = r5
            goto L_0x027f
        L_0x01af:
            r4 = 1
            if (r5 <= r4) goto L_0x027f
            int r3 = r14 - r11
        L_0x01b4:
            int r4 = r19.length()
            if (r3 >= r4) goto L_0x027f
            char r4 = r1.charAt(r3)
            byte[] r5 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            int r5 = r5.length
            if (r4 >= r5) goto L_0x01c9
            byte[] r5 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r5 = r5[r4]
            if (r5 != 0) goto L_0x01d5
        L_0x01c9:
            r5 = 47
            if (r4 != r5) goto L_0x022d
            com.alibaba.fastjson.serializer.SerializerFeature r5 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            boolean r5 = r0.isEnabled(r5)
            if (r5 == 0) goto L_0x022d
        L_0x01d5:
            char[] r5 = r0.buf
            int r6 = r14 + 1
            r5[r14] = r9
            byte[] r5 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r5 = r5[r4]
            r10 = 4
            if (r5 != r10) goto L_0x021f
            char[] r5 = r0.buf
            int r11 = r6 + 1
            r12 = 117(0x75, float:1.64E-43)
            r5[r6] = r12
            int r6 = r11 + 1
            char[] r12 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r13 = r4 >>> 12
            r13 = r13 & 15
            char r12 = r12[r13]
            r5[r11] = r12
            char[] r5 = r0.buf
            int r11 = r6 + 1
            char[] r12 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r13 = r4 >>> 8
            r13 = r13 & 15
            char r12 = r12[r13]
            r5[r6] = r12
            char[] r5 = r0.buf
            int r6 = r11 + 1
            char[] r12 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r13 = r4 >>> 4
            r13 = r13 & 15
            char r12 = r12[r13]
            r5[r11] = r12
            char[] r5 = r0.buf
            int r11 = r6 + 1
            char[] r12 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r4 = r4 & 15
            char r4 = r12[r4]
            r5[r6] = r4
            goto L_0x0229
        L_0x021f:
            char[] r5 = r0.buf
            int r11 = r6 + 1
            char[] r12 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r4 = r12[r4]
            r5[r6] = r4
        L_0x0229:
            r14 = r11
            r12 = 117(0x75, float:1.64E-43)
            goto L_0x027b
        L_0x022d:
            r10 = 4
            if (r4 != r7) goto L_0x0272
            char[] r5 = r0.buf
            int r6 = r14 + 1
            r5[r14] = r9
            int r11 = r6 + 1
            r12 = 117(0x75, float:1.64E-43)
            r5[r6] = r12
            int r6 = r11 + 1
            char[] r13 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r14 = r4 >>> 12
            r14 = r14 & 15
            char r13 = r13[r14]
            r5[r11] = r13
            char[] r5 = r0.buf
            int r11 = r6 + 1
            char[] r13 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r14 = r4 >>> 8
            r14 = r14 & 15
            char r13 = r13[r14]
            r5[r6] = r13
            char[] r5 = r0.buf
            int r6 = r11 + 1
            char[] r13 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r14 = r4 >>> 4
            r14 = r14 & 15
            char r13 = r13[r14]
            r5[r11] = r13
            char[] r5 = r0.buf
            int r11 = r6 + 1
            char[] r13 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r4 = r4 & 15
            char r4 = r13[r4]
            r5[r6] = r4
            r14 = r11
            goto L_0x027b
        L_0x0272:
            r12 = 117(0x75, float:1.64E-43)
            char[] r5 = r0.buf
            int r6 = r14 + 1
            r5[r14] = r4
            r14 = r6
        L_0x027b:
            int r3 = r3 + 1
            goto L_0x01b4
        L_0x027f:
            if (r2 == 0) goto L_0x028e
            char[] r1 = r0.buf
            int r3 = r0.count
            int r4 = r3 + -2
            r1[r4] = r8
            r4 = 1
            int r3 = r3 - r4
            r1[r3] = r2
            goto L_0x0296
        L_0x028e:
            r4 = 1
            char[] r1 = r0.buf
            int r2 = r0.count
            int r2 = r2 - r4
            r1[r2] = r8
        L_0x0296:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithDoubleQuoteDirect(java.lang.String, char):void");
    }

    public void writeFieldNull(char c, String str) {
        write((int) c);
        writeFieldName(str);
        writeNull();
    }

    public void writeFieldEmptyList(char c, String str) {
        write((int) c);
        writeFieldName(str);
        write("[]");
    }

    public void writeFieldNullString(char c, String str) {
        write((int) c);
        writeFieldName(str);
        if (isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
            writeString("");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullBoolean(char c, String str) {
        write((int) c);
        writeFieldName(str);
        if (isEnabled(SerializerFeature.WriteNullBooleanAsFalse)) {
            write("false");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullList(char c, String str) {
        write((int) c);
        writeFieldName(str);
        if (isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            write("[]");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullNumber(char c, String str) {
        write((int) c);
        writeFieldName(str);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            write(48);
        } else {
            writeNull();
        }
    }

    public void writeFieldValue(char c, String str, char c2) {
        write((int) c);
        writeFieldName(str);
        if (c2 == 0) {
            writeString("\u0000");
        } else {
            writeString(Character.toString(c2));
        }
    }

    public void writeFieldValue(char c, String str, boolean z) {
        int i = z ? 4 : 5;
        int length = str.length();
        int i2 = this.count + length + 4 + i;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write((int) c);
                writeString(str);
                write(58);
                write(z);
                return;
            }
            expandCapacity(i2);
        }
        int i3 = this.count;
        this.count = i2;
        char[] cArr = this.buf;
        cArr[i3] = c;
        int i4 = i3 + length + 1;
        cArr[i3 + 1] = this.keySeperator;
        str.getChars(0, length, cArr, i3 + 2);
        this.buf[i4 + 1] = this.keySeperator;
        if (z) {
            System.arraycopy(":true".toCharArray(), 0, this.buf, i4 + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, this.buf, i4 + 2, 6);
        }
    }

    public void write(boolean z) {
        if (z) {
            write("true");
        } else {
            write("false");
        }
    }

    public void writeFieldValue(char c, String str, int i) {
        if (i == Integer.MIN_VALUE || !this.quoteFieldNames) {
            writeFieldValue1(c, str, i);
            return;
        }
        int stringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int length = str.length();
        int i2 = this.count + length + 4 + stringSize;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                writeFieldValue1(c, str, i);
                return;
            }
            expandCapacity(i2);
        }
        int i3 = this.count;
        this.count = i2;
        char[] cArr = this.buf;
        cArr[i3] = c;
        int i4 = i3 + length + 1;
        cArr[i3 + 1] = this.keySeperator;
        str.getChars(0, length, cArr, i3 + 2);
        char[] cArr2 = this.buf;
        cArr2[i4 + 1] = this.keySeperator;
        cArr2[i4 + 2] = ':';
        IOUtils.getChars(i, this.count, cArr2);
    }

    public void writeFieldValue1(char c, String str, int i) {
        write((int) c);
        writeFieldName(str);
        writeInt(i);
    }

    public void writeFieldValue(char c, String str, long j) {
        if (j == Long.MIN_VALUE || !this.quoteFieldNames) {
            writeFieldValue1(c, str, j);
            return;
        }
        int stringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int length = str.length();
        int i = this.count + length + 4 + stringSize;
        if (i > this.buf.length) {
            if (this.writer != null) {
                write((int) c);
                writeFieldName(str);
                writeLong(j);
                return;
            }
            expandCapacity(i);
        }
        int i2 = this.count;
        this.count = i;
        char[] cArr = this.buf;
        cArr[i2] = c;
        int i3 = i2 + length + 1;
        cArr[i2 + 1] = this.keySeperator;
        str.getChars(0, length, cArr, i2 + 2);
        char[] cArr2 = this.buf;
        cArr2[i3 + 1] = this.keySeperator;
        cArr2[i3 + 2] = ':';
        IOUtils.getChars(j, this.count, cArr2);
    }

    public void writeFieldValue1(char c, String str, long j) {
        write((int) c);
        writeFieldName(str);
        writeLong(j);
    }

    public void writeFieldValue(char c, String str, float f) {
        write((int) c);
        writeFieldName(str);
        if (f == 0.0f) {
            write(48);
        } else if (Float.isNaN(f)) {
            writeNull();
        } else if (Float.isInfinite(f)) {
            writeNull();
        } else {
            String f2 = Float.toString(f);
            if (f2.endsWith(".0")) {
                f2 = f2.substring(0, f2.length() - 2);
            }
            write(f2);
        }
    }

    public void writeFieldValue(char c, String str, double d) {
        write((int) c);
        writeFieldName(str);
        if (d == 0.0d) {
            write(48);
        } else if (Double.isNaN(d)) {
            writeNull();
        } else if (Double.isInfinite(d)) {
            writeNull();
        } else {
            String d2 = Double.toString(d);
            if (d2.endsWith(".0")) {
                d2 = d2.substring(0, d2.length() - 2);
            }
            write(d2);
        }
    }

    public void writeFieldValue(char c, String str, String str2) {
        if (!this.quoteFieldNames) {
            write((int) c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
            } else {
                writeString(str2);
            }
        } else if (this.useSingleQuotes) {
            write((int) c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
            } else {
                writeString(str2);
            }
        } else if (this.browserSecure) {
            write((int) c);
            writeStringWithDoubleQuote(str, ':');
            writeStringWithDoubleQuote(str2, 0);
        } else if (this.browserCompatible) {
            write((int) c);
            writeStringWithDoubleQuote(str, ':');
            writeStringWithDoubleQuote(str2, 0);
        } else {
            writeFieldValueStringWithDoubleQuoteCheck(c, str, str2);
        }
    }

    public void writeFieldValueStringWithDoubleQuoteCheck(char c, String str, String str2) {
        int i;
        int i2;
        int i3;
        String str3 = str;
        String str4 = str2;
        int length = str.length();
        int i4 = this.count;
        if (str4 == null) {
            i2 = i4 + length + 8;
            i = 4;
        } else {
            i = str2.length();
            i2 = i4 + length + i + 6;
        }
        int i5 = 0;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write((int) c);
                writeStringWithDoubleQuote(str3, ':');
                writeStringWithDoubleQuote(str4, 0);
                return;
            }
            expandCapacity(i2);
        }
        char[] cArr = this.buf;
        int i6 = this.count;
        cArr[i6] = c;
        int i7 = i6 + 2;
        int i8 = i7 + length;
        cArr[i6 + 1] = Typography.quote;
        str3.getChars(0, length, cArr, i7);
        this.count = i2;
        char[] cArr2 = this.buf;
        cArr2[i8] = Typography.quote;
        int i9 = i8 + 1;
        int i10 = i9 + 1;
        cArr2[i9] = ':';
        if (str4 == null) {
            int i11 = i10 + 1;
            cArr2[i10] = 'n';
            int i12 = i11 + 1;
            cArr2[i11] = 'u';
            cArr2[i12] = 'l';
            cArr2[i12 + 1] = 'l';
            return;
        }
        int i13 = i10 + 1;
        cArr2[i10] = Typography.quote;
        int i14 = i13 + i;
        str4.getChars(0, i, cArr2, i13);
        if (!this.disableCheckSpecialChar) {
            int i15 = i2;
            char c2 = 0;
            int i16 = -1;
            int i17 = -1;
            for (int i18 = i13; i18 < i14; i18++) {
                char c3 = this.buf[i18];
                if (c3 >= ']') {
                    if (c3 >= 127 && (c3 == 8232 || c3 <= 160)) {
                        if (i16 == -1) {
                            i16 = i18;
                        }
                        i5++;
                        i15 += 4;
                    }
                } else if (isSpecial(c3, this.features)) {
                    i5++;
                    if (c3 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[c3] == 4) {
                        i15 += 4;
                    }
                    if (i16 == -1) {
                        i16 = i18;
                        i17 = i16;
                        c2 = c3;
                    }
                }
                i17 = i18;
                c2 = c3;
            }
            if (i5 > 0) {
                int i19 = i15 + i5;
                if (i19 > this.buf.length) {
                    expandCapacity(i19);
                }
                this.count = i19;
                if (i5 == 1) {
                    if (c2 == 8232) {
                        int i20 = i17 + 1;
                        char[] cArr3 = this.buf;
                        System.arraycopy(cArr3, i20, cArr3, i17 + 6, (i14 - i17) - 1);
                        char[] cArr4 = this.buf;
                        cArr4[i17] = '\\';
                        cArr4[i20] = 'u';
                        int i21 = i20 + 1;
                        cArr4[i21] = '2';
                        int i22 = i21 + 1;
                        cArr4[i22] = '0';
                        int i23 = i22 + 1;
                        cArr4[i23] = '2';
                        cArr4[i23 + 1] = '8';
                    } else if (c2 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c2] != 4) {
                        int i24 = i17 + 1;
                        char[] cArr5 = this.buf;
                        System.arraycopy(cArr5, i24, cArr5, i17 + 2, (i14 - i17) - 1);
                        char[] cArr6 = this.buf;
                        cArr6[i17] = '\\';
                        cArr6[i24] = IOUtils.replaceChars[c2];
                    } else {
                        int i25 = i17 + 1;
                        char[] cArr7 = this.buf;
                        System.arraycopy(cArr7, i25, cArr7, i17 + 6, (i14 - i17) - 1);
                        char[] cArr8 = this.buf;
                        cArr8[i17] = '\\';
                        int i26 = i25 + 1;
                        cArr8[i25] = 'u';
                        int i27 = i26 + 1;
                        cArr8[i26] = IOUtils.DIGITS[(c2 >>> 12) & 15];
                        int i28 = i27 + 1;
                        this.buf[i27] = IOUtils.DIGITS[(c2 >>> 8) & 15];
                        this.buf[i28] = IOUtils.DIGITS[(c2 >>> 4) & 15];
                        this.buf[i28 + 1] = IOUtils.DIGITS[c2 & 15];
                    }
                } else if (i5 > 1) {
                    for (int i29 = i16 - i13; i29 < str2.length(); i29++) {
                        char charAt = str4.charAt(i29);
                        if ((charAt < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[charAt] != 0) || (charAt == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            int i30 = i16 + 1;
                            this.buf[i16] = '\\';
                            if (IOUtils.specicalFlags_doubleQuotes[charAt] == 4) {
                                char[] cArr9 = this.buf;
                                int i31 = i30 + 1;
                                cArr9[i30] = 'u';
                                int i32 = i31 + 1;
                                cArr9[i31] = IOUtils.DIGITS[(charAt >>> 12) & 15];
                                int i33 = i32 + 1;
                                this.buf[i32] = IOUtils.DIGITS[(charAt >>> 8) & 15];
                                int i34 = i33 + 1;
                                this.buf[i33] = IOUtils.DIGITS[(charAt >>> 4) & 15];
                                i3 = i34 + 1;
                                this.buf[i34] = IOUtils.DIGITS[charAt & 15];
                            } else {
                                i3 = i30 + 1;
                                this.buf[i30] = IOUtils.replaceChars[charAt];
                            }
                            i16 = i3;
                        } else if (charAt == 8232) {
                            char[] cArr10 = this.buf;
                            int i35 = i16 + 1;
                            cArr10[i16] = '\\';
                            int i36 = i35 + 1;
                            cArr10[i35] = 'u';
                            int i37 = i36 + 1;
                            cArr10[i36] = IOUtils.DIGITS[(charAt >>> 12) & 15];
                            int i38 = i37 + 1;
                            this.buf[i37] = IOUtils.DIGITS[(charAt >>> 8) & 15];
                            int i39 = i38 + 1;
                            this.buf[i38] = IOUtils.DIGITS[(charAt >>> 4) & 15];
                            this.buf[i39] = IOUtils.DIGITS[charAt & 15];
                            i16 = i39 + 1;
                        } else {
                            this.buf[i16] = charAt;
                            i16++;
                        }
                    }
                }
            }
        }
        this.buf[this.count - 1] = Typography.quote;
    }

    public void writeFieldValueStringWithDoubleQuote(char c, String str, String str2) {
        int i;
        int i2;
        int length = str.length();
        int i3 = this.count;
        if (str2 == null) {
            i2 = 4;
            i = length + 8;
        } else {
            i2 = str2.length();
            i = length + i2 + 6;
        }
        int i4 = i3 + i;
        if (i4 > this.buf.length) {
            if (this.writer != null) {
                write((int) c);
                writeStringWithDoubleQuote(str, ':');
                writeStringWithDoubleQuote(str2, 0);
                return;
            }
            expandCapacity(i4);
        }
        char[] cArr = this.buf;
        int i5 = this.count;
        cArr[i5] = c;
        int i6 = i5 + 2;
        int i7 = i6 + length;
        cArr[i5 + 1] = Typography.quote;
        str.getChars(0, length, cArr, i6);
        this.count = i4;
        char[] cArr2 = this.buf;
        cArr2[i7] = Typography.quote;
        int i8 = i7 + 1;
        int i9 = i8 + 1;
        cArr2[i8] = ':';
        if (str2 == null) {
            int i10 = i9 + 1;
            cArr2[i9] = 'n';
            int i11 = i10 + 1;
            cArr2[i10] = 'u';
            cArr2[i11] = 'l';
            cArr2[i11 + 1] = 'l';
            return;
        }
        cArr2[i9] = Typography.quote;
        str2.getChars(0, i2, cArr2, i9 + 1);
        this.buf[this.count - 1] = Typography.quote;
    }

    static boolean isSpecial(char c, int i) {
        if (c == ' ') {
            return false;
        }
        if (c == '/') {
            return (SerializerFeature.WriteSlashAsSpecial.mask & i) != 0;
        }
        if (c <= '#' || c == '\\') {
            return c <= 31 || c == '\\' || c == '\"';
        }
        return false;
    }

    public void writeFieldValue(char c, String str, Enum<?> enumR) {
        if (enumR == null) {
            write((int) c);
            writeFieldName(str);
            writeNull();
        } else if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            writeEnumFieldValue(c, str, enumR.name());
        } else if (this.writeEnumUsingToString) {
            writeEnumFieldValue(c, str, enumR.toString());
        } else {
            writeFieldValue(c, str, enumR.ordinal());
        }
    }

    private void writeEnumFieldValue(char c, String str, String str2) {
        if (this.useSingleQuotes) {
            writeFieldValue(c, str, str2);
        } else {
            writeFieldValueStringWithDoubleQuote(c, str, str2);
        }
    }

    public void writeFieldValue(char c, String str, BigDecimal bigDecimal) {
        write((int) c);
        writeFieldName(str);
        if (bigDecimal == null) {
            writeNull();
        } else {
            write(bigDecimal.toString());
        }
    }

    public void writeString(String str, char c) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(str);
            write((int) c);
            return;
        }
        writeStringWithDoubleQuote(str, c);
    }

    public void writeString(String str) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(str);
        } else {
            writeStringWithDoubleQuote(str, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void writeStringWithSingleQuote(String str) {
        int i = 0;
        if (str == null) {
            int i2 = this.count + 4;
            if (i2 > this.buf.length) {
                expandCapacity(i2);
            }
            "null".getChars(0, 4, this.buf, this.count);
            this.count = i2;
            return;
        }
        int length = str.length();
        int i3 = this.count + length + 2;
        if (i3 > this.buf.length) {
            if (this.writer != null) {
                write(39);
                while (i < str.length()) {
                    char charAt = str.charAt(i);
                    if (charAt <= 13 || charAt == '\\' || charAt == '\'' || (charAt == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write(92);
                        write((int) IOUtils.replaceChars[charAt]);
                    } else {
                        write((int) charAt);
                    }
                    i++;
                }
                write(39);
                return;
            }
            expandCapacity(i3);
        }
        int i4 = this.count;
        int i5 = i4 + 1;
        int i6 = i5 + length;
        char[] cArr = this.buf;
        cArr[i4] = '\'';
        str.getChars(0, length, cArr, i5);
        this.count = i3;
        int i7 = -1;
        char c = 0;
        for (int i8 = i5; i8 < i6; i8++) {
            char c2 = this.buf[i8];
            if (c2 <= 13 || c2 == '\\' || c2 == '\'' || (c2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                i++;
                i7 = i8;
                c = c2;
            }
        }
        int i9 = i3 + i;
        if (i9 > this.buf.length) {
            expandCapacity(i9);
        }
        this.count = i9;
        if (i == 1) {
            char[] cArr2 = this.buf;
            int i10 = i7 + 1;
            System.arraycopy(cArr2, i10, cArr2, i7 + 2, (i6 - i7) - 1);
            char[] cArr3 = this.buf;
            cArr3[i7] = '\\';
            cArr3[i10] = IOUtils.replaceChars[c];
        } else if (i > 1) {
            char[] cArr4 = this.buf;
            int i11 = i7 + 1;
            System.arraycopy(cArr4, i11, cArr4, i7 + 2, (i6 - i7) - 1);
            char[] cArr5 = this.buf;
            cArr5[i7] = '\\';
            cArr5[i11] = IOUtils.replaceChars[c];
            int i12 = i6 + 1;
            for (int i13 = i11 - 2; i13 >= i5; i13--) {
                char c3 = this.buf[i13];
                if (c3 <= 13 || c3 == '\\' || c3 == '\'' || (c3 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    char[] cArr6 = this.buf;
                    int i14 = i13 + 1;
                    System.arraycopy(cArr6, i14, cArr6, i13 + 2, (i12 - i13) - 1);
                    char[] cArr7 = this.buf;
                    cArr7[i13] = '\\';
                    cArr7[i14] = IOUtils.replaceChars[c3];
                    i12++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeFieldName(String str) {
        writeFieldName(str, false);
    }

    public void writeFieldName(String str, boolean z) {
        if (str == null) {
            write("null:");
        } else if (this.useSingleQuotes) {
            if (this.quoteFieldNames) {
                writeStringWithSingleQuote(str);
                write(58);
                return;
            }
            writeKeyWithSingleQuoteIfHasSpecial(str);
        } else if (this.quoteFieldNames) {
            writeStringWithDoubleQuote(str, ':');
        } else {
            boolean z2 = str.length() == 0;
            int i = 0;
            while (true) {
                if (i >= str.length()) {
                    break;
                } else if (isSpecial(str.charAt(i), 0)) {
                    z2 = true;
                    break;
                } else {
                    i++;
                }
            }
            if (z2) {
                writeStringWithDoubleQuote(str, ':');
                return;
            }
            write(str);
            write(58);
        }
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String str) {
        int i;
        String str2 = str;
        byte[] bArr = IOUtils.specicalFlags_singleQuotes;
        int length = str.length();
        boolean z = true;
        int i2 = this.count + length + 1;
        int i3 = 0;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else if (length == 0) {
                write(39);
                write(39);
                write(58);
                return;
            } else {
                int i4 = 0;
                while (true) {
                    if (i4 < length) {
                        char charAt = str2.charAt(i4);
                        if (charAt < bArr.length && bArr[charAt] != 0) {
                            break;
                        }
                        i4++;
                    } else {
                        z = false;
                        break;
                    }
                }
                if (z) {
                    write(39);
                }
                while (i3 < length) {
                    char charAt2 = str2.charAt(i3);
                    if (charAt2 >= bArr.length || bArr[charAt2] == 0) {
                        write((int) charAt2);
                    } else {
                        write(92);
                        write((int) IOUtils.replaceChars[charAt2]);
                    }
                    i3++;
                }
                if (z) {
                    write(39);
                }
                write(58);
                return;
            }
        }
        if (length == 0) {
            int i5 = this.count;
            if (i5 + 3 > this.buf.length) {
                expandCapacity(i5 + 3);
            }
            char[] cArr = this.buf;
            int i6 = this.count;
            this.count = i6 + 1;
            cArr[i6] = '\'';
            int i7 = this.count;
            this.count = i7 + 1;
            cArr[i7] = '\'';
            int i8 = this.count;
            this.count = i8 + 1;
            cArr[i8] = ':';
            return;
        }
        int i9 = this.count;
        int i10 = i9 + length;
        str2.getChars(0, length, this.buf, i9);
        this.count = i2;
        int i11 = i9;
        boolean z2 = false;
        while (i11 < i10) {
            char[] cArr2 = this.buf;
            char c = cArr2[i11];
            if (c >= bArr.length || bArr[c] == 0) {
                i = i11;
            } else if (!z2) {
                i2 += 3;
                if (i2 > cArr2.length) {
                    expandCapacity(i2);
                }
                this.count = i2;
                char[] cArr3 = this.buf;
                int i12 = i11 + 1;
                System.arraycopy(cArr3, i12, cArr3, i11 + 3, (i10 - i11) - 1);
                char[] cArr4 = this.buf;
                System.arraycopy(cArr4, i3, cArr4, 1, i11);
                char[] cArr5 = this.buf;
                cArr5[i9] = '\'';
                cArr5[i12] = '\\';
                int i13 = i12 + 1;
                cArr5[i13] = IOUtils.replaceChars[c];
                i10 += 2;
                this.buf[this.count - 2] = '\'';
                i = i13;
                z2 = true;
            } else {
                i2++;
                if (i2 > cArr2.length) {
                    expandCapacity(i2);
                }
                this.count = i2;
                char[] cArr6 = this.buf;
                i = i11 + 1;
                System.arraycopy(cArr6, i, cArr6, i11 + 2, i10 - i11);
                char[] cArr7 = this.buf;
                cArr7[i11] = '\\';
                cArr7[i] = IOUtils.replaceChars[c];
                i10++;
            }
            i11 = i + 1;
            i3 = 0;
        }
        this.buf[i2 - 1] = ':';
    }

    public void flush() {
        Writer writer2 = this.writer;
        if (writer2 != null) {
            try {
                writer2.write(this.buf, 0, this.count);
                this.writer.flush();
                this.count = 0;
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }
}
