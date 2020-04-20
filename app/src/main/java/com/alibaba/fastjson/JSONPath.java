package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ASMJavaBeanSerializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class JSONPath implements JSONAware {
    private static int CACHE_SIZE = 1024;
    private static ConcurrentMap<String, JSONPath> pathCache = new ConcurrentHashMap(128, 0.75f, 1);
    private ParserConfig parserConfig;
    private final String path;
    private Segement[] segments;
    private SerializeConfig serializeConfig;

    interface Filter {
        boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3);
    }

    enum Operator {
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        LIKE,
        NOT_LIKE,
        RLIKE,
        NOT_RLIKE,
        IN,
        NOT_IN,
        BETWEEN,
        NOT_BETWEEN
    }

    interface Segement {
        Object eval(JSONPath jSONPath, Object obj, Object obj2);
    }

    public JSONPath(String str) {
        this(str, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance());
    }

    public JSONPath(String str, SerializeConfig serializeConfig2, ParserConfig parserConfig2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.path = str;
        this.serializeConfig = serializeConfig2;
        this.parserConfig = parserConfig2;
    }

    /* access modifiers changed from: protected */
    public void init() {
        if (this.segments == null) {
            if ("*".equals(this.path)) {
                this.segments = new Segement[]{WildCardSegement.instance};
                return;
            }
            this.segments = new JSONPathParser(this.path).explain();
        }
    }

    public Object eval(Object obj) {
        if (obj == null) {
            return null;
        }
        init();
        int i = 0;
        Object obj2 = obj;
        while (true) {
            Segement[] segementArr = this.segments;
            if (i >= segementArr.length) {
                return obj2;
            }
            obj2 = segementArr[i].eval(this, obj, obj2);
            i++;
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        init();
        Object obj2 = obj;
        int i = 0;
        while (true) {
            Segement[] segementArr = this.segments;
            if (i >= segementArr.length) {
                return true;
            }
            obj2 = segementArr[i].eval(this, obj, obj2);
            if (obj2 == null) {
                return false;
            }
            i++;
        }
    }

    public boolean containsValue(Object obj, Object obj2) {
        Object eval = eval(obj);
        if (eval == obj2) {
            return true;
        }
        if (eval == null) {
            return false;
        }
        if (!(eval instanceof Iterable)) {
            return eq(eval, obj2);
        }
        for (Object eq : (Iterable) eval) {
            if (eq(eq, obj2)) {
                return true;
            }
        }
        return false;
    }

    public int size(Object obj) {
        if (obj == null) {
            return -1;
        }
        init();
        int i = 0;
        Object obj2 = obj;
        while (true) {
            Segement[] segementArr = this.segments;
            if (i >= segementArr.length) {
                return evalSize(obj2);
            }
            obj2 = segementArr[i].eval(this, obj, obj2);
            i++;
        }
    }

    public void arrayAdd(Object obj, Object... objArr) {
        if (objArr != null && objArr.length != 0 && obj != null) {
            init();
            int i = 0;
            Object obj2 = obj;
            Object obj3 = null;
            int i2 = 0;
            while (true) {
                Segement[] segementArr = this.segments;
                if (i2 >= segementArr.length) {
                    break;
                }
                if (i2 == segementArr.length - 1) {
                    obj3 = obj2;
                }
                obj2 = this.segments[i2].eval(this, obj, obj2);
                i2++;
            }
            if (obj2 == null) {
                throw new JSONPathException("value not found in path " + this.path);
            } else if (obj2 instanceof Collection) {
                Collection collection = (Collection) obj2;
                int length = objArr.length;
                while (i < length) {
                    collection.add(objArr[i]);
                    i++;
                }
            } else {
                Class<?> cls = obj2.getClass();
                if (cls.isArray()) {
                    int length2 = Array.getLength(obj2);
                    Object newInstance = Array.newInstance(cls.getComponentType(), objArr.length + length2);
                    System.arraycopy(obj2, 0, newInstance, 0, length2);
                    while (i < objArr.length) {
                        Array.set(newInstance, length2 + i, objArr[i]);
                        i++;
                    }
                    Segement[] segementArr2 = this.segments;
                    Segement segement = segementArr2[segementArr2.length - 1];
                    if (segement instanceof PropertySegement) {
                        ((PropertySegement) segement).setValue(this, obj3, newInstance);
                    } else if (segement instanceof ArrayAccessSegement) {
                        ((ArrayAccessSegement) segement).setValue(this, obj3, newInstance);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    public boolean set(Object obj, Object obj2) {
        if (obj == null) {
            return false;
        }
        init();
        Object obj3 = null;
        Object obj4 = obj;
        int i = 0;
        while (true) {
            Segement[] segementArr = this.segments;
            if (i >= segementArr.length) {
                break;
            } else if (i == segementArr.length - 1) {
                obj3 = obj4;
                break;
            } else {
                obj4 = segementArr[i].eval(this, obj, obj4);
                if (obj4 == null) {
                    break;
                }
                i++;
            }
        }
        if (obj3 == null) {
            return false;
        }
        Segement[] segementArr2 = this.segments;
        Segement segement = segementArr2[segementArr2.length - 1];
        if (segement instanceof PropertySegement) {
            ((PropertySegement) segement).setValue(this, obj3, obj2);
            return true;
        } else if (segement instanceof ArrayAccessSegement) {
            return ((ArrayAccessSegement) segement).setValue(this, obj3, obj2);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Object eval(Object obj, String str) {
        return compile(str).eval(obj);
    }

    public static int size(Object obj, String str) {
        JSONPath compile = compile(str);
        return compile.evalSize(compile.eval(obj));
    }

    public static boolean contains(Object obj, String str) {
        if (obj == null) {
            return false;
        }
        return compile(str).contains(obj);
    }

    public static boolean containsValue(Object obj, String str, Object obj2) {
        return compile(str).containsValue(obj, obj2);
    }

    public static void arrayAdd(Object obj, String str, Object... objArr) {
        compile(str).arrayAdd(obj, objArr);
    }

    public static boolean set(Object obj, String str, Object obj2) {
        return compile(str).set(obj, obj2);
    }

    public static JSONPath compile(String str) {
        JSONPath jSONPath = (JSONPath) pathCache.get(str);
        if (jSONPath != null) {
            return jSONPath;
        }
        JSONPath jSONPath2 = new JSONPath(str);
        if (pathCache.size() >= CACHE_SIZE) {
            return jSONPath2;
        }
        pathCache.putIfAbsent(str, jSONPath2);
        return (JSONPath) pathCache.get(str);
    }

    public static Object read(String str, String str2) {
        return compile(str2).eval(JSON.parse(str));
    }

    public String getPath() {
        return this.path;
    }

    static class JSONPathParser {
        private char ch;
        private int level;
        private final String path;
        private int pos;

        static boolean isDigitFirst(char c) {
            return c == '-' || c == '+' || (c >= '0' && c <= '9');
        }

        public JSONPathParser(String str) {
            this.path = str;
            next();
        }

        /* access modifiers changed from: package-private */
        public void next() {
            String str = this.path;
            int i = this.pos;
            this.pos = i + 1;
            this.ch = str.charAt(i);
        }

        /* access modifiers changed from: package-private */
        public boolean isEOF() {
            return this.pos >= this.path.length();
        }

        /* access modifiers changed from: package-private */
        public Segement readSegement() {
            char c;
            if (this.level == 0 && this.path.length() == 1) {
                if (isDigitFirst(this.ch)) {
                    return new ArrayAccessSegement(this.ch - '0');
                }
                char c2 = this.ch;
                if ((c2 >= 'a' && c2 <= 'z') || ((c = this.ch) >= 'A' && c <= 'Z')) {
                    return new PropertySegement(Character.toString(this.ch));
                }
            }
            while (!isEOF()) {
                skipWhitespace();
                char c3 = this.ch;
                if (c3 == '@') {
                    next();
                    return SelfSegement.instance;
                } else if (c3 == '$') {
                    next();
                } else if (c3 == '.' || c3 == '/') {
                    next();
                    char c4 = this.ch;
                    if (c4 == '*') {
                        if (!isEOF()) {
                            next();
                        }
                        return WildCardSegement.instance;
                    } else if (isDigitFirst(c4)) {
                        return parseArrayAccess(false);
                    } else {
                        String readName = readName();
                        if (this.ch != '(') {
                            return new PropertySegement(readName);
                        }
                        next();
                        if (this.ch == ')') {
                            if (!isEOF()) {
                                next();
                            }
                            if ("size".equals(readName)) {
                                return SizeSegement.instance;
                            }
                            throw new UnsupportedOperationException();
                        }
                        throw new UnsupportedOperationException();
                    }
                } else if (c3 == '[') {
                    return parseArrayAccess(true);
                } else {
                    if (this.level == 0) {
                        return new PropertySegement(readName());
                    }
                    throw new UnsupportedOperationException();
                }
            }
            return null;
        }

        public final void skipWhitespace() {
            while (true) {
                char c = this.ch;
                if (c > ' ') {
                    return;
                }
                if (c == ' ' || c == 13 || c == 10 || c == 9 || c == 12 || c == 8) {
                    next();
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x004c  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x0050  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.alibaba.fastjson.JSONPath.Segement parseArrayAccess(boolean r12) {
            /*
                r11 = this;
                if (r12 == 0) goto L_0x0007
                r0 = 91
                r11.accept(r0)
            L_0x0007:
                char r0 = r11.ch
                r1 = 63
                r2 = 40
                r3 = 0
                r4 = 1
                if (r0 != r1) goto L_0x0027
                r11.next()
                r11.accept(r2)
                char r0 = r11.ch
                r1 = 64
                if (r0 != r1) goto L_0x0025
                r11.next()
                r0 = 46
                r11.accept(r0)
            L_0x0025:
                r0 = 1
                goto L_0x0028
            L_0x0027:
                r0 = 0
            L_0x0028:
                r1 = 93
                if (r0 != 0) goto L_0x006f
                char r5 = r11.ch
                boolean r5 = com.alibaba.fastjson.util.IOUtils.firstIdentifier(r5)
                if (r5 == 0) goto L_0x0035
                goto L_0x006f
            L_0x0035:
                int r0 = r11.pos
                int r0 = r0 - r4
            L_0x0038:
                char r2 = r11.ch
                r3 = 47
                if (r2 == r1) goto L_0x004a
                if (r2 == r3) goto L_0x004a
                boolean r2 = r11.isEOF()
                if (r2 != 0) goto L_0x004a
                r11.next()
                goto L_0x0038
            L_0x004a:
                if (r12 == 0) goto L_0x0050
                int r2 = r11.pos
            L_0x004e:
                int r2 = r2 - r4
                goto L_0x0059
            L_0x0050:
                char r2 = r11.ch
                if (r2 != r3) goto L_0x0057
                int r2 = r11.pos
                goto L_0x004e
            L_0x0057:
                int r2 = r11.pos
            L_0x0059:
                java.lang.String r3 = r11.path
                java.lang.String r0 = r3.substring(r0, r2)
                com.alibaba.fastjson.JSONPath$Segement r0 = r11.buildArraySegement(r0)
                if (r12 == 0) goto L_0x006e
                boolean r12 = r11.isEOF()
                if (r12 != 0) goto L_0x006e
                r11.accept(r1)
            L_0x006e:
                return r0
            L_0x006f:
                java.lang.String r5 = r11.readName()
                r11.skipWhitespace()
                r6 = 41
                if (r0 == 0) goto L_0x0091
                char r7 = r11.ch
                if (r7 != r6) goto L_0x0091
                r11.next()
                if (r12 == 0) goto L_0x0086
                r11.accept(r1)
            L_0x0086:
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$NotNullSegement r0 = new com.alibaba.fastjson.JSONPath$NotNullSegement
                r0.<init>(r5)
                r12.<init>(r0)
                return r12
            L_0x0091:
                if (r12 == 0) goto L_0x00a5
                char r7 = r11.ch
                if (r7 != r1) goto L_0x00a5
                r11.next()
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$NotNullSegement r0 = new com.alibaba.fastjson.JSONPath$NotNullSegement
                r0.<init>(r5)
                r12.<init>(r0)
                return r12
            L_0x00a5:
                com.alibaba.fastjson.JSONPath$Operator r7 = r11.readOp()
                r11.skipWhitespace()
                com.alibaba.fastjson.JSONPath$Operator r8 = com.alibaba.fastjson.JSONPath.Operator.BETWEEN
                if (r7 == r8) goto L_0x0349
                com.alibaba.fastjson.JSONPath$Operator r8 = com.alibaba.fastjson.JSONPath.Operator.NOT_BETWEEN
                if (r7 != r8) goto L_0x00b6
                goto L_0x0349
            L_0x00b6:
                com.alibaba.fastjson.JSONPath$Operator r8 = com.alibaba.fastjson.JSONPath.Operator.IN
                if (r7 == r8) goto L_0x020a
                com.alibaba.fastjson.JSONPath$Operator r8 = com.alibaba.fastjson.JSONPath.Operator.NOT_IN
                if (r7 != r8) goto L_0x00c0
                goto L_0x020a
            L_0x00c0:
                char r2 = r11.ch
                r8 = 39
                if (r2 == r8) goto L_0x012e
                r8 = 34
                if (r2 != r8) goto L_0x00cb
                goto L_0x012e
            L_0x00cb:
                boolean r2 = isDigitFirst(r2)
                if (r2 == 0) goto L_0x00ea
                long r2 = r11.readLongValue()
                if (r0 == 0) goto L_0x00da
                r11.accept(r6)
            L_0x00da:
                if (r12 == 0) goto L_0x00df
                r11.accept(r1)
            L_0x00df:
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$IntOpSegement r0 = new com.alibaba.fastjson.JSONPath$IntOpSegement
                r0.<init>(r5, r2, r7)
                r12.<init>(r0)
                return r12
            L_0x00ea:
                char r12 = r11.ch
                r2 = 110(0x6e, float:1.54E-43)
                if (r12 != r2) goto L_0x0128
                java.lang.String r12 = r11.readName()
                java.lang.String r2 = "null"
                boolean r12 = r2.equals(r12)
                if (r12 == 0) goto L_0x0128
                if (r0 == 0) goto L_0x0101
                r11.accept(r6)
            L_0x0101:
                r11.accept(r1)
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.EQ
                if (r7 != r12) goto L_0x0113
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$NullSegement r0 = new com.alibaba.fastjson.JSONPath$NullSegement
                r0.<init>(r5)
                r12.<init>(r0)
                return r12
            L_0x0113:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.NE
                if (r7 != r12) goto L_0x0122
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$NotNullSegement r0 = new com.alibaba.fastjson.JSONPath$NotNullSegement
                r0.<init>(r5)
                r12.<init>(r0)
                return r12
            L_0x0122:
                java.lang.UnsupportedOperationException r12 = new java.lang.UnsupportedOperationException
                r12.<init>()
                throw r12
            L_0x0128:
                java.lang.UnsupportedOperationException r12 = new java.lang.UnsupportedOperationException
                r12.<init>()
                throw r12
            L_0x012e:
                java.lang.String r2 = r11.readString()
                if (r0 == 0) goto L_0x0137
                r11.accept(r6)
            L_0x0137:
                if (r12 == 0) goto L_0x013c
                r11.accept(r1)
            L_0x013c:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.RLIKE
                if (r7 != r12) goto L_0x014b
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$RlikeSegement r0 = new com.alibaba.fastjson.JSONPath$RlikeSegement
                r0.<init>(r5, r2, r3)
                r12.<init>(r0)
                return r12
            L_0x014b:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.NOT_RLIKE
                if (r7 != r12) goto L_0x015a
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$RlikeSegement r0 = new com.alibaba.fastjson.JSONPath$RlikeSegement
                r0.<init>(r5, r2, r4)
                r12.<init>(r0)
                return r12
            L_0x015a:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.LIKE
                if (r7 == r12) goto L_0x0162
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.NOT_LIKE
                if (r7 != r12) goto L_0x018a
            L_0x0162:
                java.lang.String r12 = "%%"
                int r0 = r2.indexOf(r12)
                java.lang.String r1 = "%"
                r6 = -1
                if (r0 == r6) goto L_0x0172
                java.lang.String r2 = r2.replaceAll(r12, r1)
                goto L_0x0162
            L_0x0172:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.NOT_LIKE
                if (r7 != r12) goto L_0x0178
                r12 = 1
                goto L_0x0179
            L_0x0178:
                r12 = 0
            L_0x0179:
                r0 = 37
                int r8 = r2.indexOf(r0)
                if (r8 != r6) goto L_0x0195
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.LIKE
                if (r7 != r12) goto L_0x0188
                com.alibaba.fastjson.JSONPath$Operator r7 = com.alibaba.fastjson.JSONPath.Operator.EQ
                goto L_0x018a
            L_0x0188:
                com.alibaba.fastjson.JSONPath$Operator r7 = com.alibaba.fastjson.JSONPath.Operator.NE
            L_0x018a:
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$StringOpSegement r0 = new com.alibaba.fastjson.JSONPath$StringOpSegement
                r0.<init>(r5, r2, r7)
                r12.<init>(r0)
                return r12
            L_0x0195:
                java.lang.String[] r1 = r2.split(r1)
                r6 = 0
                r7 = 2
                if (r8 != 0) goto L_0x01c8
                int r8 = r2.length()
                int r8 = r8 - r4
                char r2 = r2.charAt(r8)
                if (r2 != r0) goto L_0x01b1
                int r0 = r1.length
                int r0 = r0 - r4
                java.lang.String[] r0 = new java.lang.String[r0]
                int r2 = r0.length
                java.lang.System.arraycopy(r1, r4, r0, r3, r2)
                goto L_0x01d4
            L_0x01b1:
                int r0 = r1.length
                int r0 = r0 - r4
                r0 = r1[r0]
                int r2 = r1.length
                if (r2 <= r7) goto L_0x01c4
                int r2 = r1.length
                int r2 = r2 - r7
                java.lang.String[] r2 = new java.lang.String[r2]
                int r7 = r2.length
                java.lang.System.arraycopy(r1, r4, r2, r3, r7)
                r4 = r6
                r6 = r0
                r0 = r2
                goto L_0x01fa
            L_0x01c4:
                r4 = r6
                r6 = r0
                r0 = r4
                goto L_0x01fa
            L_0x01c8:
                int r8 = r2.length()
                int r8 = r8 - r4
                char r2 = r2.charAt(r8)
                if (r2 != r0) goto L_0x01d6
                r0 = r1
            L_0x01d4:
                r4 = r6
                goto L_0x01fa
            L_0x01d6:
                int r0 = r1.length
                if (r0 != r4) goto L_0x01de
                r0 = r1[r3]
                r4 = r0
                r0 = r6
                goto L_0x01fa
            L_0x01de:
                int r0 = r1.length
                if (r0 != r7) goto L_0x01e9
                r0 = r1[r3]
                r1 = r1[r4]
                r4 = r0
                r0 = r6
                r6 = r1
                goto L_0x01fa
            L_0x01e9:
                r0 = r1[r3]
                int r2 = r1.length
                int r2 = r2 - r4
                r2 = r1[r2]
                int r6 = r1.length
                int r6 = r6 - r7
                java.lang.String[] r6 = new java.lang.String[r6]
                int r7 = r6.length
                java.lang.System.arraycopy(r1, r4, r6, r3, r7)
                r4 = r0
                r0 = r6
                r6 = r2
            L_0x01fa:
                com.alibaba.fastjson.JSONPath$FilterSegement r1 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$MatchSegement r8 = new com.alibaba.fastjson.JSONPath$MatchSegement
                r2 = r8
                r3 = r5
                r5 = r6
                r6 = r0
                r7 = r12
                r2.<init>(r3, r4, r5, r6, r7)
                r1.<init>(r8)
                return r1
            L_0x020a:
                com.alibaba.fastjson.JSONPath$Operator r8 = com.alibaba.fastjson.JSONPath.Operator.NOT_IN
                if (r7 != r8) goto L_0x0210
                r8 = 1
                goto L_0x0211
            L_0x0210:
                r8 = 0
            L_0x0211:
                r11.accept(r2)
                java.util.ArrayList r2 = new java.util.ArrayList
                r2.<init>()
                java.lang.Object r7 = r11.readValue()
                r2.add(r7)
            L_0x0220:
                r11.skipWhitespace()
                char r7 = r11.ch
                r9 = 44
                if (r7 == r9) goto L_0x033d
                r11.accept(r6)
                if (r0 == 0) goto L_0x0231
                r11.accept(r6)
            L_0x0231:
                if (r12 == 0) goto L_0x0236
                r11.accept(r1)
            L_0x0236:
                java.util.Iterator r12 = r2.iterator()
                r0 = 1
                r1 = 1
                r6 = 1
            L_0x023d:
                boolean r7 = r12.hasNext()
                if (r7 == 0) goto L_0x026d
                java.lang.Object r7 = r12.next()
                if (r7 != 0) goto L_0x024d
                if (r0 == 0) goto L_0x023d
                r0 = 0
                goto L_0x023d
            L_0x024d:
                java.lang.Class r7 = r7.getClass()
                if (r0 == 0) goto L_0x0265
                java.lang.Class<java.lang.Byte> r9 = java.lang.Byte.class
                if (r7 == r9) goto L_0x0265
                java.lang.Class<java.lang.Short> r9 = java.lang.Short.class
                if (r7 == r9) goto L_0x0265
                java.lang.Class<java.lang.Integer> r9 = java.lang.Integer.class
                if (r7 == r9) goto L_0x0265
                java.lang.Class<java.lang.Long> r9 = java.lang.Long.class
                if (r7 == r9) goto L_0x0265
                r0 = 0
                r6 = 0
            L_0x0265:
                if (r1 == 0) goto L_0x023d
                java.lang.Class<java.lang.String> r9 = java.lang.String.class
                if (r7 == r9) goto L_0x023d
                r1 = 0
                goto L_0x023d
            L_0x026d:
                int r12 = r2.size()
                if (r12 != r4) goto L_0x0291
                java.lang.Object r12 = r2.get(r3)
                if (r12 != 0) goto L_0x0291
                if (r8 == 0) goto L_0x0286
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$NotNullSegement r0 = new com.alibaba.fastjson.JSONPath$NotNullSegement
                r0.<init>(r5)
                r12.<init>(r0)
                return r12
            L_0x0286:
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$NullSegement r0 = new com.alibaba.fastjson.JSONPath$NullSegement
                r0.<init>(r5)
                r12.<init>(r0)
                return r12
            L_0x0291:
                if (r0 == 0) goto L_0x02d8
                int r12 = r2.size()
                if (r12 != r4) goto L_0x02b5
                java.lang.Object r12 = r2.get(r3)
                java.lang.Number r12 = (java.lang.Number) r12
                long r0 = r12.longValue()
                if (r8 == 0) goto L_0x02a8
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.NE
                goto L_0x02aa
            L_0x02a8:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.EQ
            L_0x02aa:
                com.alibaba.fastjson.JSONPath$FilterSegement r2 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$IntOpSegement r3 = new com.alibaba.fastjson.JSONPath$IntOpSegement
                r3.<init>(r5, r0, r12)
                r2.<init>(r3)
                return r2
            L_0x02b5:
                int r12 = r2.size()
                long[] r12 = new long[r12]
            L_0x02bb:
                int r0 = r12.length
                if (r3 >= r0) goto L_0x02cd
                java.lang.Object r0 = r2.get(r3)
                java.lang.Number r0 = (java.lang.Number) r0
                long r0 = r0.longValue()
                r12[r3] = r0
                int r3 = r3 + 1
                goto L_0x02bb
            L_0x02cd:
                com.alibaba.fastjson.JSONPath$FilterSegement r0 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$IntInSegement r1 = new com.alibaba.fastjson.JSONPath$IntInSegement
                r1.<init>(r5, r12, r8)
                r0.<init>(r1)
                return r0
            L_0x02d8:
                if (r1 == 0) goto L_0x030c
                int r12 = r2.size()
                if (r12 != r4) goto L_0x02f8
                java.lang.Object r12 = r2.get(r3)
                java.lang.String r12 = (java.lang.String) r12
                if (r8 == 0) goto L_0x02eb
                com.alibaba.fastjson.JSONPath$Operator r0 = com.alibaba.fastjson.JSONPath.Operator.NE
                goto L_0x02ed
            L_0x02eb:
                com.alibaba.fastjson.JSONPath$Operator r0 = com.alibaba.fastjson.JSONPath.Operator.EQ
            L_0x02ed:
                com.alibaba.fastjson.JSONPath$FilterSegement r1 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$StringOpSegement r2 = new com.alibaba.fastjson.JSONPath$StringOpSegement
                r2.<init>(r5, r12, r0)
                r1.<init>(r2)
                return r1
            L_0x02f8:
                int r12 = r2.size()
                java.lang.String[] r12 = new java.lang.String[r12]
                r2.toArray(r12)
                com.alibaba.fastjson.JSONPath$FilterSegement r0 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$StringInSegement r1 = new com.alibaba.fastjson.JSONPath$StringInSegement
                r1.<init>(r5, r12, r8)
                r0.<init>(r1)
                return r0
            L_0x030c:
                if (r6 == 0) goto L_0x0337
                int r12 = r2.size()
                java.lang.Long[] r12 = new java.lang.Long[r12]
            L_0x0314:
                int r0 = r12.length
                if (r3 >= r0) goto L_0x032c
                java.lang.Object r0 = r2.get(r3)
                java.lang.Number r0 = (java.lang.Number) r0
                if (r0 == 0) goto L_0x0329
                long r0 = r0.longValue()
                java.lang.Long r0 = java.lang.Long.valueOf(r0)
                r12[r3] = r0
            L_0x0329:
                int r3 = r3 + 1
                goto L_0x0314
            L_0x032c:
                com.alibaba.fastjson.JSONPath$FilterSegement r0 = new com.alibaba.fastjson.JSONPath$FilterSegement
                com.alibaba.fastjson.JSONPath$IntObjInSegement r1 = new com.alibaba.fastjson.JSONPath$IntObjInSegement
                r1.<init>(r5, r12, r8)
                r0.<init>(r1)
                return r0
            L_0x0337:
                java.lang.UnsupportedOperationException r12 = new java.lang.UnsupportedOperationException
                r12.<init>()
                throw r12
            L_0x033d:
                r11.next()
                java.lang.Object r7 = r11.readValue()
                r2.add(r7)
                goto L_0x0220
            L_0x0349:
                com.alibaba.fastjson.JSONPath$Operator r12 = com.alibaba.fastjson.JSONPath.Operator.NOT_BETWEEN
                if (r7 != r12) goto L_0x034f
                r8 = 1
                goto L_0x0350
            L_0x034f:
                r8 = 0
            L_0x0350:
                java.lang.Object r12 = r11.readValue()
                java.lang.String r0 = r11.readName()
                java.lang.String r1 = "and"
                boolean r0 = r1.equalsIgnoreCase(r0)
                if (r0 == 0) goto L_0x03a7
                java.lang.Object r0 = r11.readValue()
                if (r12 == 0) goto L_0x039f
                if (r0 == 0) goto L_0x039f
                java.lang.Class r1 = r12.getClass()
                boolean r1 = com.alibaba.fastjson.JSONPath.isInt(r1)
                if (r1 == 0) goto L_0x0397
                java.lang.Class r1 = r0.getClass()
                boolean r1 = com.alibaba.fastjson.JSONPath.isInt(r1)
                if (r1 == 0) goto L_0x0397
                com.alibaba.fastjson.JSONPath$IntBetweenSegement r1 = new com.alibaba.fastjson.JSONPath$IntBetweenSegement
                java.lang.Number r12 = (java.lang.Number) r12
                long r6 = r12.longValue()
                java.lang.Number r0 = (java.lang.Number) r0
                long r9 = r0.longValue()
                r2 = r1
                r3 = r5
                r4 = r6
                r6 = r9
                r2.<init>(r3, r4, r6, r8)
                com.alibaba.fastjson.JSONPath$FilterSegement r12 = new com.alibaba.fastjson.JSONPath$FilterSegement
                r12.<init>(r1)
                return r12
            L_0x0397:
                com.alibaba.fastjson.JSONPathException r12 = new com.alibaba.fastjson.JSONPathException
                java.lang.String r0 = r11.path
                r12.<init>(r0)
                throw r12
            L_0x039f:
                com.alibaba.fastjson.JSONPathException r12 = new com.alibaba.fastjson.JSONPathException
                java.lang.String r0 = r11.path
                r12.<init>(r0)
                throw r12
            L_0x03a7:
                com.alibaba.fastjson.JSONPathException r12 = new com.alibaba.fastjson.JSONPathException
                java.lang.String r0 = r11.path
                r12.<init>(r0)
                throw r12
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.JSONPathParser.parseArrayAccess(boolean):com.alibaba.fastjson.JSONPath$Segement");
        }

        /* access modifiers changed from: protected */
        public long readLongValue() {
            int i = this.pos - 1;
            char c = this.ch;
            if (c == '+' || c == '-') {
                next();
            }
            while (true) {
                char c2 = this.ch;
                if (c2 < '0' || c2 > '9') {
                } else {
                    next();
                }
            }
            return Long.parseLong(this.path.substring(i, this.pos - 1));
        }

        /* access modifiers changed from: protected */
        public Object readValue() {
            skipWhitespace();
            if (isDigitFirst(this.ch)) {
                return Long.valueOf(readLongValue());
            }
            char c = this.ch;
            if (c == '\"' || c == '\'') {
                return readString();
            }
            if (c != 'n') {
                throw new UnsupportedOperationException();
            } else if ("null".equals(readName())) {
                return null;
            } else {
                throw new JSONPathException(this.path);
            }
        }

        /* access modifiers changed from: protected */
        public Operator readOp() {
            Operator operator;
            char c = this.ch;
            if (c == '=') {
                next();
                operator = Operator.EQ;
            } else if (c == '!') {
                next();
                accept('=');
                operator = Operator.NE;
            } else if (c == '<') {
                next();
                if (this.ch == '=') {
                    next();
                    operator = Operator.LE;
                } else {
                    operator = Operator.LT;
                }
            } else if (c == '>') {
                next();
                if (this.ch == '=') {
                    next();
                    operator = Operator.GE;
                } else {
                    operator = Operator.GT;
                }
            } else {
                operator = null;
            }
            if (operator != null) {
                return operator;
            }
            String readName = readName();
            if ("not".equalsIgnoreCase(readName)) {
                skipWhitespace();
                String readName2 = readName();
                if ("like".equalsIgnoreCase(readName2)) {
                    return Operator.NOT_LIKE;
                }
                if ("rlike".equalsIgnoreCase(readName2)) {
                    return Operator.NOT_RLIKE;
                }
                if ("in".equalsIgnoreCase(readName2)) {
                    return Operator.NOT_IN;
                }
                if ("between".equalsIgnoreCase(readName2)) {
                    return Operator.NOT_BETWEEN;
                }
                throw new UnsupportedOperationException();
            } else if ("like".equalsIgnoreCase(readName)) {
                return Operator.LIKE;
            } else {
                if ("rlike".equalsIgnoreCase(readName)) {
                    return Operator.RLIKE;
                }
                if ("in".equalsIgnoreCase(readName)) {
                    return Operator.IN;
                }
                if ("between".equalsIgnoreCase(readName)) {
                    return Operator.BETWEEN;
                }
                throw new UnsupportedOperationException();
            }
        }

        /* access modifiers changed from: package-private */
        public String readName() {
            skipWhitespace();
            char c = this.ch;
            if (c == '\\' || IOUtils.firstIdentifier(c)) {
                StringBuilder sb = new StringBuilder();
                while (!isEOF()) {
                    char c2 = this.ch;
                    if (c2 == '\\') {
                        next();
                        sb.append(this.ch);
                        if (isEOF()) {
                            break;
                        }
                        next();
                    } else if (!IOUtils.isIdent(c2)) {
                        break;
                    } else {
                        sb.append(this.ch);
                        next();
                    }
                }
                if (isEOF() && IOUtils.isIdent(this.ch)) {
                    sb.append(this.ch);
                }
                return sb.toString();
            }
            throw new JSONPathException("illeal jsonpath syntax. " + this.path);
        }

        /* access modifiers changed from: package-private */
        public String readString() {
            char c = this.ch;
            next();
            int i = this.pos - 1;
            while (this.ch != c && !isEOF()) {
                next();
            }
            String substring = this.path.substring(i, isEOF() ? this.pos : this.pos - 1);
            accept(c);
            return substring;
        }

        /* access modifiers changed from: package-private */
        public void accept(char c) {
            if (this.ch != c) {
                throw new JSONPathException("expect '" + c + ", but '" + this.ch + "'");
            } else if (!isEOF()) {
                next();
            }
        }

        public Segement[] explain() {
            String str = this.path;
            if (str == null || str.isEmpty()) {
                throw new IllegalArgumentException();
            }
            Segement[] segementArr = new Segement[8];
            while (true) {
                Segement readSegement = readSegement();
                if (readSegement == null) {
                    break;
                }
                int i = this.level;
                this.level = i + 1;
                segementArr[i] = readSegement;
            }
            int i2 = this.level;
            if (i2 == segementArr.length) {
                return segementArr;
            }
            Segement[] segementArr2 = new Segement[i2];
            System.arraycopy(segementArr, 0, segementArr2, 0, i2);
            return segementArr2;
        }

        /* access modifiers changed from: package-private */
        public Segement buildArraySegement(String str) {
            int length = str.length();
            int i = 0;
            char charAt = str.charAt(0);
            int i2 = 1;
            int i3 = length - 1;
            char charAt2 = str.charAt(i3);
            int indexOf = str.indexOf(44);
            int i4 = -1;
            if (str.length() <= 2 || charAt != '\'' || charAt2 != '\'') {
                int indexOf2 = str.indexOf(58);
                if (indexOf == -1 && indexOf2 == -1) {
                    return new ArrayAccessSegement(Integer.parseInt(str));
                }
                if (indexOf != -1) {
                    String[] split = str.split(",");
                    int[] iArr = new int[split.length];
                    while (i < split.length) {
                        iArr[i] = Integer.parseInt(split[i]);
                        i++;
                    }
                    return new MultiIndexSegement(iArr);
                } else if (indexOf2 != -1) {
                    String[] split2 = str.split(":");
                    int[] iArr2 = new int[split2.length];
                    for (int i5 = 0; i5 < split2.length; i5++) {
                        String str2 = split2[i5];
                        if (!str2.isEmpty()) {
                            iArr2[i5] = Integer.parseInt(str2);
                        } else if (i5 == 0) {
                            iArr2[i5] = 0;
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                    int i6 = iArr2[0];
                    if (iArr2.length > 1) {
                        i4 = iArr2[1];
                    }
                    if (iArr2.length == 3) {
                        i2 = iArr2[2];
                    }
                    if (i4 >= 0 && i4 < i6) {
                        throw new UnsupportedOperationException("end must greater than or equals start. start " + i6 + ",  end " + i4);
                    } else if (i2 > 0) {
                        return new RangeSegement(i6, i4, i2);
                    } else {
                        throw new UnsupportedOperationException("step must greater than zero : " + i2);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            } else if (indexOf == -1) {
                return new PropertySegement(str.substring(1, i3));
            } else {
                String[] split3 = str.split(",");
                String[] strArr = new String[split3.length];
                while (i < split3.length) {
                    String str3 = split3[i];
                    strArr[i] = str3.substring(1, str3.length() - 1);
                    i++;
                }
                return new MultiPropertySegement(strArr);
            }
        }
    }

    static class SelfSegement implements Segement {
        public static final SelfSegement instance = new SelfSegement();

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return obj2;
        }

        SelfSegement() {
        }
    }

    static class SizeSegement implements Segement {
        public static final SizeSegement instance = new SizeSegement();

        SizeSegement() {
        }

        public Integer eval(JSONPath jSONPath, Object obj, Object obj2) {
            return Integer.valueOf(jSONPath.evalSize(obj2));
        }
    }

    static class PropertySegement implements Segement {
        private final String propertyName;

        public PropertySegement(String str) {
            this.propertyName = str;
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getPropertyValue(obj2, this.propertyName, true);
        }

        public void setValue(JSONPath jSONPath, Object obj, Object obj2) {
            jSONPath.setPropertyValue(obj, this.propertyName, obj2);
        }
    }

    static class MultiPropertySegement implements Segement {
        private final String[] propertyNames;

        public MultiPropertySegement(String[] strArr) {
            this.propertyNames = strArr;
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            ArrayList arrayList = new ArrayList(this.propertyNames.length);
            for (String propertyValue : this.propertyNames) {
                arrayList.add(jSONPath.getPropertyValue(obj2, propertyValue, true));
            }
            return arrayList;
        }
    }

    static class WildCardSegement implements Segement {
        public static WildCardSegement instance = new WildCardSegement();

        WildCardSegement() {
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getPropertyValues(obj2);
        }
    }

    static class ArrayAccessSegement implements Segement {
        private final int index;

        public ArrayAccessSegement(int i) {
            this.index = i;
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getArrayItem(obj2, this.index);
        }

        public boolean setValue(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.setArrayItem(jSONPath, obj, this.index, obj2);
        }
    }

    static class MultiIndexSegement implements Segement {
        private final int[] indexes;

        public MultiIndexSegement(int[] iArr) {
            this.indexes = iArr;
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            ArrayList arrayList = new ArrayList(this.indexes.length);
            int i = 0;
            while (true) {
                int[] iArr = this.indexes;
                if (i >= iArr.length) {
                    return arrayList;
                }
                arrayList.add(jSONPath.getArrayItem(obj2, iArr[i]));
                i++;
            }
        }
    }

    static class RangeSegement implements Segement {
        private final int end;
        private final int start;
        private final int step;

        public RangeSegement(int i, int i2, int i3) {
            this.start = i;
            this.end = i2;
            this.step = i3;
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            int intValue = SizeSegement.instance.eval(jSONPath, obj, obj2).intValue();
            int i = this.start;
            if (i < 0) {
                i += intValue;
            }
            int i2 = this.end;
            if (i2 < 0) {
                i2 += intValue;
            }
            ArrayList arrayList = new ArrayList(((i2 - i) / this.step) + 1);
            while (i <= i2 && i < intValue) {
                arrayList.add(jSONPath.getArrayItem(obj2, i));
                i += this.step;
            }
            return arrayList;
        }
    }

    static class NotNullSegement implements Filter {
        private final String propertyName;

        public NotNullSegement(String str) {
            this.propertyName = str;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, false) != null;
        }
    }

    static class NullSegement implements Filter {
        private final String propertyName;

        public NullSegement(String str) {
            this.propertyName = str;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, false) == null;
        }
    }

    static class IntInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long[] values;

        public IntInSegement(String str, long[] jArr, boolean z) {
            this.propertyName = str;
            this.values = jArr;
            this.not = z;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long longValue = ((Number) propertyValue).longValue();
                for (long j : this.values) {
                    if (j == longValue) {
                        return !this.not;
                    }
                }
            }
            return this.not;
        }
    }

    static class IntBetweenSegement implements Filter {
        private final long endValue;
        private final boolean not;
        private final String propertyName;
        private final long startValue;

        public IntBetweenSegement(String str, long j, long j2, boolean z) {
            this.propertyName = str;
            this.startValue = j;
            this.endValue = j2;
            this.not = z;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long longValue = ((Number) propertyValue).longValue();
                if (longValue >= this.startValue && longValue <= this.endValue) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    static class IntObjInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final Long[] values;

        public IntObjInSegement(String str, Long[] lArr, boolean z) {
            this.propertyName = str;
            this.values = lArr;
            this.not = z;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            int i = 0;
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (propertyValue == null) {
                Long[] lArr = this.values;
                int length = lArr.length;
                while (i < length) {
                    if (lArr[i] == null) {
                        return !this.not;
                    }
                    i++;
                }
                return this.not;
            }
            if (propertyValue instanceof Number) {
                long longValue = ((Number) propertyValue).longValue();
                Long[] lArr2 = this.values;
                int length2 = lArr2.length;
                while (i < length2) {
                    Long l = lArr2[i];
                    if (l != null && l.longValue() == longValue) {
                        return !this.not;
                    }
                    i++;
                }
            }
            return this.not;
        }
    }

    static class StringInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final String[] values;

        public StringInSegement(String str, String[] strArr, boolean z) {
            this.propertyName = str;
            this.values = strArr;
            this.not = z;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            for (String str : this.values) {
                if (str == propertyValue) {
                    return !this.not;
                }
                if (str != null && str.equals(propertyValue)) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    static class IntOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final long value;

        public IntOpSegement(String str, long j, Operator operator) {
            this.propertyName = str;
            this.value = j;
            this.op = operator;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (propertyValue == null || !(propertyValue instanceof Number)) {
                return false;
            }
            long longValue = ((Number) propertyValue).longValue();
            if (this.op == Operator.EQ) {
                if (longValue == this.value) {
                    return true;
                }
                return false;
            } else if (this.op == Operator.NE) {
                if (longValue != this.value) {
                    return true;
                }
                return false;
            } else if (this.op == Operator.GE) {
                if (longValue >= this.value) {
                    return true;
                }
                return false;
            } else if (this.op == Operator.GT) {
                if (longValue > this.value) {
                    return true;
                }
                return false;
            } else if (this.op == Operator.LE) {
                if (longValue <= this.value) {
                    return true;
                }
                return false;
            } else if (this.op != Operator.LT || longValue >= this.value) {
                return false;
            } else {
                return true;
            }
        }
    }

    static class MatchSegement implements Filter {
        private final String[] containsValues;
        private final String endsWithValue;
        private final int minLength;
        private final boolean not;
        private final String propertyName;
        private final String startsWithValue;

        public MatchSegement(String str, String str2, String str3, String[] strArr, boolean z) {
            this.propertyName = str;
            this.startsWithValue = str2;
            this.endsWithValue = str3;
            this.containsValues = strArr;
            this.not = z;
            int length = str2 != null ? str2.length() + 0 : 0;
            length = str3 != null ? length + str3.length() : length;
            if (strArr != null) {
                for (String length2 : strArr) {
                    length += length2.length();
                }
            }
            this.minLength = length;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            int i;
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            String obj4 = propertyValue.toString();
            if (obj4.length() < this.minLength) {
                return this.not;
            }
            String str = this.startsWithValue;
            if (str == null) {
                i = 0;
            } else if (!obj4.startsWith(str)) {
                return this.not;
            } else {
                i = this.startsWithValue.length() + 0;
            }
            String[] strArr = this.containsValues;
            if (strArr != null) {
                for (String str2 : strArr) {
                    int indexOf = obj4.indexOf(str2, i);
                    if (indexOf == -1) {
                        return this.not;
                    }
                    i = indexOf + str2.length();
                }
            }
            String str3 = this.endsWithValue;
            if (str3 == null || obj4.endsWith(str3)) {
                return !this.not;
            }
            return this.not;
        }
    }

    static class RlikeSegement implements Filter {
        private final boolean not;
        private final Pattern pattern;
        private final String propertyName;

        public RlikeSegement(String str, String str2, boolean z) {
            this.propertyName = str;
            this.pattern = Pattern.compile(str2);
            this.not = z;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (propertyValue == null) {
                return false;
            }
            boolean matches = this.pattern.matcher(propertyValue.toString()).matches();
            return this.not ? !matches : matches;
        }
    }

    static class StringOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final String value;

        public StringOpSegement(String str, String str2, Operator operator) {
            this.propertyName = str;
            this.value = str2;
            this.op = operator;
        }

        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, false);
            if (this.op == Operator.EQ) {
                return this.value.equals(propertyValue);
            }
            if (this.op == Operator.NE) {
                return !this.value.equals(propertyValue);
            }
            if (propertyValue == null) {
                return false;
            }
            int compareTo = this.value.compareTo(propertyValue.toString());
            if (this.op == Operator.GE) {
                if (compareTo <= 0) {
                    return true;
                }
                return false;
            } else if (this.op == Operator.GT) {
                if (compareTo < 0) {
                    return true;
                }
                return false;
            } else if (this.op == Operator.LE) {
                if (compareTo >= 0) {
                    return true;
                }
                return false;
            } else if (this.op != Operator.LT || compareTo <= 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static class FilterSegement implements Segement {
        private final Filter filter;

        public FilterSegement(Filter filter2) {
            this.filter = filter2;
        }

        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            if (obj2 instanceof Iterable) {
                for (Object next : (Iterable) obj2) {
                    if (this.filter.apply(jSONPath, obj, obj2, next)) {
                        arrayList.add(next);
                    }
                }
                return arrayList;
            } else if (this.filter.apply(jSONPath, obj, obj2, obj2)) {
                return obj2;
            } else {
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object getArrayItem(Object obj, int i) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                if (i < list.size()) {
                    return list.get(i);
                }
                return null;
            } else if (Math.abs(i) <= list.size()) {
                return list.get(list.size() + i);
            } else {
                return null;
            }
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if (i >= 0) {
                if (i < length) {
                    return Array.get(obj, i);
                }
                return null;
            } else if (Math.abs(i) <= length) {
                return Array.get(obj, length + i);
            } else {
                return null;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean setArrayItem(JSONPath jSONPath, Object obj, int i, Object obj2) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                list.set(i, obj2);
            } else {
                list.set(list.size() + i, obj2);
            }
            return true;
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if (i >= 0) {
                if (i < length) {
                    Array.set(obj, i, obj2);
                }
            } else if (Math.abs(i) <= length) {
                Array.set(obj, length + i, obj2);
            }
            return true;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: protected */
    public Collection<Object> getPropertyValues(Object obj) {
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer != null) {
            try {
                return javaBeanSerializer.getFieldValues(obj);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path, e);
            }
        } else if (obj instanceof Map) {
            return ((Map) obj).values();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    static boolean eq(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj.getClass() == obj2.getClass()) {
            return obj.equals(obj2);
        }
        if (!(obj instanceof Number)) {
            return obj.equals(obj2);
        }
        if (obj2 instanceof Number) {
            return eqNotNull((Number) obj, (Number) obj2);
        }
        return false;
    }

    static boolean eqNotNull(Number number, Number number2) {
        Class<?> cls = number.getClass();
        boolean isInt = isInt(cls);
        Class<?> cls2 = number.getClass();
        boolean isInt2 = isInt(cls2);
        if (!isInt || !isInt2) {
            boolean isDouble = isDouble(cls);
            boolean isDouble2 = isDouble(cls2);
            if (((!isDouble || !isDouble2) && ((!isDouble || !isInt) && (!isDouble2 || !isInt))) || number.doubleValue() != number2.doubleValue()) {
                return false;
            }
            return true;
        } else if (number.longValue() == number2.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean isDouble(Class<?> cls) {
        return cls == Float.class || cls == Double.class;
    }

    protected static boolean isInt(Class<?> cls) {
        return cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class;
    }

    /* access modifiers changed from: protected */
    public Object getPropertyValue(Object obj, String str, boolean z) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return ((Map) obj).get(str);
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer != null) {
            try {
                FieldSerializer fieldSerializer = javaBeanSerializer.getFieldSerializer(str);
                if (fieldSerializer == null) {
                    return null;
                }
                return fieldSerializer.getPropertyValue(obj);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e);
            }
        } else if (obj instanceof List) {
            List list = (List) obj;
            ArrayList arrayList = new ArrayList(list.size());
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(getPropertyValue(list.get(i), str, z));
            }
            return arrayList;
        } else {
            throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str);
        }
    }

    /* access modifiers changed from: protected */
    public boolean setPropertyValue(Object obj, String str, Object obj2) {
        if (obj instanceof Map) {
            ((Map) obj).put(str, obj2);
            return true;
        } else if (obj instanceof List) {
            for (Object next : (List) obj) {
                if (next != null) {
                    setPropertyValue(next, str, obj2);
                }
            }
            return true;
        } else {
            ObjectDeserializer deserializer = this.parserConfig.getDeserializer((Type) obj.getClass());
            JavaBeanDeserializer javaBeanDeserializer = null;
            if (deserializer instanceof JavaBeanDeserializer) {
                javaBeanDeserializer = (JavaBeanDeserializer) deserializer;
            } else if (deserializer instanceof ASMJavaBeanDeserializer) {
                javaBeanDeserializer = ((ASMJavaBeanDeserializer) deserializer).getInnterSerializer();
            }
            if (javaBeanDeserializer != null) {
                FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
                if (fieldDeserializer == null) {
                    return false;
                }
                fieldDeserializer.setValue(obj, obj2);
                return true;
            }
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: protected */
    public JavaBeanSerializer getJavaBeanSerializer(Class<?> cls) {
        ObjectSerializer objectWriter = this.serializeConfig.getObjectWriter(cls);
        if (objectWriter instanceof JavaBeanSerializer) {
            return (JavaBeanSerializer) objectWriter;
        }
        if (objectWriter instanceof ASMJavaBeanSerializer) {
            return ((ASMJavaBeanSerializer) objectWriter).getJavaBeanSerializer();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int evalSize(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        int i = 0;
        if (obj instanceof Map) {
            for (Object obj2 : ((Map) obj).values()) {
                if (obj2 != null) {
                    i++;
                }
            }
            return i;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            return -1;
        }
        try {
            List<Object> fieldValues = javaBeanSerializer.getFieldValues(obj);
            int i2 = 0;
            while (i < fieldValues.size()) {
                if (fieldValues.get(i) != null) {
                    i2++;
                }
                i++;
            }
            return i2;
        } catch (Exception e) {
            throw new JSONException("evalSize error : " + this.path, e);
        }
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        jSONSerializer.write(this.path);
    }

    public String toJSONString() {
        return JSON.toJSONString(this.path);
    }
}
