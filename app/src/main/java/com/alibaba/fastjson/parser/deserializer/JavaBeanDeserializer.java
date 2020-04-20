package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;

public class JavaBeanDeserializer implements ObjectDeserializer {
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private final FieldDeserializer[] fieldDeserializers;
    protected final FieldDeserializer[] sortedFieldDeserializers = new FieldDeserializer[this.beanInfo.sortedFields.length];

    public int getFastMatchToken() {
        return 12;
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) {
        this.clazz = cls;
        this.beanInfo = JavaBeanInfo.build(cls, type);
        int length = this.beanInfo.sortedFields.length;
        for (int i = 0; i < length; i++) {
            this.sortedFieldDeserializers[i] = parserConfig.createFieldDeserializer(parserConfig, this.beanInfo, this.beanInfo.sortedFields[i]);
        }
        this.fieldDeserializers = new FieldDeserializer[this.beanInfo.fields.length];
        int length2 = this.beanInfo.fields.length;
        for (int i2 = 0; i2 < length2; i2++) {
            this.fieldDeserializers[i2] = getFieldDeserializer(this.beanInfo.fields[i2].name);
        }
    }

    public FieldDeserializer getFieldDeserializer(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = this.sortedFieldDeserializers.length - 1;
        while (i <= length) {
            int i2 = (i + length) >>> 1;
            int compareTo = this.sortedFieldDeserializers[i2].fieldInfo.name.compareTo(str);
            if (compareTo < 0) {
                i = i2 + 1;
            } else if (compareTo <= 0) {
                return this.sortedFieldDeserializers[i2];
            } else {
                length = i2 - 1;
            }
        }
        return null;
    }

    public Object createInstance(DefaultJSONParser defaultJSONParser, Type type) {
        Object obj;
        if ((type instanceof Class) && this.clazz.isInterface()) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{(Class) type}, new JSONObject());
        } else if (this.beanInfo.defaultConstructor == null) {
            return null;
        } else {
            try {
                Constructor<?> constructor = this.beanInfo.defaultConstructor;
                if (this.beanInfo.defaultConstructorParameterSize == 0) {
                    obj = constructor.newInstance(new Object[0]);
                } else {
                    obj = constructor.newInstance(new Object[]{defaultJSONParser.getContext().object});
                }
                if (defaultJSONParser != null && defaultJSONParser.lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
                    for (FieldInfo fieldInfo : this.beanInfo.fields) {
                        if (fieldInfo.fieldClass == String.class) {
                            try {
                                fieldInfo.set(obj, "");
                            } catch (Exception e) {
                                throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                            }
                        }
                    }
                }
                return obj;
            } catch (Exception e2) {
                throw new JSONException("create instance error, class " + this.clazz.getName(), e2);
            }
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return deserialze(defaultJSONParser, type, obj, (Object) null);
    }

    public <T> T deserialzeArrayMapping(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 14) {
            T createInstance = createInstance(defaultJSONParser, type);
            int i = 0;
            int length = this.sortedFieldDeserializers.length;
            while (i < length) {
                char c = i == length + -1 ? ']' : ',';
                FieldDeserializer fieldDeserializer = this.sortedFieldDeserializers[i];
                Class<?> cls = fieldDeserializer.fieldInfo.fieldClass;
                if (cls == Integer.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanInt(c));
                } else if (cls == String.class) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanString(c));
                } else if (cls == Long.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanLong(c));
                } else if (cls.isEnum()) {
                    fieldDeserializer.setValue((Object) createInstance, (Object) jSONLexer.scanEnum(cls, defaultJSONParser.getSymbolTable(), c));
                } else {
                    jSONLexer.nextToken(14);
                    fieldDeserializer.setValue((Object) createInstance, defaultJSONParser.parseObject(fieldDeserializer.fieldInfo.fieldType));
                    if (c == ']') {
                        if (jSONLexer.token() == 15) {
                            jSONLexer.nextToken(16);
                        } else {
                            throw new JSONException("syntax error");
                        }
                    } else if (c == ',' && jSONLexer.token() != 16) {
                        throw new JSONException("syntax error");
                    }
                }
                i++;
            }
            jSONLexer.nextToken(16);
            return createInstance;
        }
        throw new JSONException("error");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x01b9, code lost:
        if (r10.matchStat == -2) goto L_0x01bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x01fb, code lost:
        r10.nextTokenWithColon(4);
        r0 = r10.token();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0203, code lost:
        if (r0 != 4) goto L_0x0283;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0205, code lost:
        r0 = r10.stringVal();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x020f, code lost:
        if ("@".equals(r0) == false) goto L_0x0215;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x0211, code lost:
        r1 = r13.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x021b, code lost:
        if ("..".equals(r0) == false) goto L_0x0232;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x021d, code lost:
        r3 = r13.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0221, code lost:
        if (r3.object == null) goto L_0x0226;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x0223, code lost:
        r1 = r3.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x0226, code lost:
        r8.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r3, r0));
        r8.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x0238, code lost:
        if ("$".equals(r0) == false) goto L_0x0255;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x023a, code lost:
        r3 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x023d, code lost:
        if (r3.parent == null) goto L_0x0242;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x023f, code lost:
        r3 = r3.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x0244, code lost:
        if (r3.object == null) goto L_0x0249;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x0246, code lost:
        r1 = r3.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x0249, code lost:
        r8.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r3, r0));
        r8.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x0255, code lost:
        r8.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r13, r0));
        r8.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x0260, code lost:
        r10.nextToken(13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x0269, code lost:
        if (r10.token() != 13) goto L_0x027b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x026b, code lost:
        r10.nextToken(16);
        r8.setContext(r13, r1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:0x0273, code lost:
        if (r2 == null) goto L_0x0277;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x0275, code lost:
        r2.object = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:193:0x0277, code lost:
        r8.setContext(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x027a, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x0282, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x029d, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref, " + com.alibaba.fastjson.parser.JSONToken.name(r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:214:0x02fe, code lost:
        if (r2 == null) goto L_0x0302;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:215:0x0300, code lost:
        r2.object = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:216:0x0302, code lost:
        r8.setContext(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:217:0x0305, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:266:0x03ac, code lost:
        r12 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:288:0x03ef, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:291:0x040e, code lost:
        throw new com.alibaba.fastjson.JSONException("create instance error, " + r7.beanInfo.creatorConstructor.toGenericString(), r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:296:0x041f, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:299:0x043e, code lost:
        throw new com.alibaba.fastjson.JSONException("create factory method error, " + r7.beanInfo.factoryMethod.toString(), r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:313:0x045c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:316:0x0464, code lost:
        throw new com.alibaba.fastjson.JSONException("build object error", r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:317:0x0465, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:318:0x0466, code lost:
        r2 = r12;
        r1 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:327:0x04a6, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, unexpect token " + com.alibaba.fastjson.parser.JSONToken.name(r10.token()));
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:269:0x03b2, B:285:0x03e5, B:294:0x0415, B:307:0x044e] */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x01c9 A[Catch:{ all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:221:0x030e A[Catch:{ all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:226:0x0320 A[Catch:{ all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:230:0x0331  */
    /* JADX WARNING: Removed duplicated region for block: B:253:0x036e A[Catch:{ all -> 0x04a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:262:0x039f A[Catch:{ all -> 0x04a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:263:0x03a1 A[Catch:{ all -> 0x04a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:334:0x04b5  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0069 A[Catch:{ all -> 0x0045 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r20, java.lang.reflect.Type r21, java.lang.Object r22, java.lang.Object r23) {
        /*
            r19 = this;
            r7 = r19
            r8 = r20
            r0 = r21
            r9 = r22
            java.lang.Class<com.alibaba.fastjson.JSON> r1 = com.alibaba.fastjson.JSON.class
            if (r0 == r1) goto L_0x04bb
            java.lang.Class<com.alibaba.fastjson.JSONObject> r1 = com.alibaba.fastjson.JSONObject.class
            if (r0 != r1) goto L_0x0012
            goto L_0x04bb
        L_0x0012:
            com.alibaba.fastjson.parser.JSONLexer r1 = r8.lexer
            r10 = r1
            com.alibaba.fastjson.parser.JSONLexerBase r10 = (com.alibaba.fastjson.parser.JSONLexerBase) r10
            int r1 = r10.token()
            r2 = 8
            r11 = 16
            r12 = 0
            if (r1 != r2) goto L_0x0026
            r10.nextToken(r11)
            return r12
        L_0x0026:
            com.alibaba.fastjson.parser.ParseContext r2 = r20.getContext()
            if (r23 == 0) goto L_0x0030
            if (r2 == 0) goto L_0x0030
            com.alibaba.fastjson.parser.ParseContext r2 = r2.parent
        L_0x0030:
            r13 = r2
            r14 = 13
            if (r1 != r14) goto L_0x004b
            r10.nextToken(r11)     // Catch:{ all -> 0x0045 }
            if (r23 != 0) goto L_0x003f
            java.lang.Object r0 = r19.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r20, (java.lang.reflect.Type) r21)     // Catch:{ all -> 0x0045 }
            goto L_0x0041
        L_0x003f:
            r0 = r23
        L_0x0041:
            r8.setContext(r13)
            return r0
        L_0x0045:
            r0 = move-exception
            r1 = r23
        L_0x0048:
            r2 = r12
            goto L_0x04b3
        L_0x004b:
            r2 = 14
            r15 = 0
            if (r1 != r2) goto L_0x0071
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r7.beanInfo     // Catch:{ all -> 0x0045 }
            int r2 = r2.parserFeatures     // Catch:{ all -> 0x0045 }
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean     // Catch:{ all -> 0x0045 }
            int r3 = r3.mask     // Catch:{ all -> 0x0045 }
            r2 = r2 & r3
            if (r2 != 0) goto L_0x0066
            com.alibaba.fastjson.parser.Feature r2 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean     // Catch:{ all -> 0x0045 }
            boolean r2 = r10.isEnabled(r2)     // Catch:{ all -> 0x0045 }
            if (r2 == 0) goto L_0x0064
            goto L_0x0066
        L_0x0064:
            r2 = 0
            goto L_0x0067
        L_0x0066:
            r2 = 1
        L_0x0067:
            if (r2 == 0) goto L_0x0071
            java.lang.Object r0 = r19.deserialzeArrayMapping(r20, r21, r22, r23)     // Catch:{ all -> 0x0045 }
            r8.setContext(r13)
            return r0
        L_0x0071:
            r2 = 12
            r5 = 4
            if (r1 == r2) goto L_0x00c8
            if (r1 == r11) goto L_0x00c8
            boolean r0 = r10.isBlankInput()     // Catch:{ all -> 0x0045 }
            if (r0 == 0) goto L_0x0082
            r8.setContext(r13)
            return r12
        L_0x0082:
            if (r1 != r5) goto L_0x0095
            java.lang.String r0 = r10.stringVal()     // Catch:{ all -> 0x0045 }
            int r0 = r0.length()     // Catch:{ all -> 0x0045 }
            if (r0 != 0) goto L_0x0095
            r10.nextToken()     // Catch:{ all -> 0x0045 }
            r8.setContext(r13)
            return r12
        L_0x0095:
            java.lang.StringBuffer r0 = new java.lang.StringBuffer     // Catch:{ all -> 0x0045 }
            r0.<init>()     // Catch:{ all -> 0x0045 }
            java.lang.String r1 = "syntax error, expect {, actual "
            r0.append(r1)     // Catch:{ all -> 0x0045 }
            java.lang.String r1 = r10.tokenName()     // Catch:{ all -> 0x0045 }
            r0.append(r1)     // Catch:{ all -> 0x0045 }
            java.lang.String r1 = ", pos "
            r0.append(r1)     // Catch:{ all -> 0x0045 }
            int r1 = r10.pos()     // Catch:{ all -> 0x0045 }
            r0.append(r1)     // Catch:{ all -> 0x0045 }
            boolean r1 = r9 instanceof java.lang.String     // Catch:{ all -> 0x0045 }
            if (r1 == 0) goto L_0x00be
            java.lang.String r1 = ", fieldName "
            r0.append(r1)     // Catch:{ all -> 0x0045 }
            r0.append(r9)     // Catch:{ all -> 0x0045 }
        L_0x00be:
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0045 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0045 }
            r1.<init>(r0)     // Catch:{ all -> 0x0045 }
            throw r1     // Catch:{ all -> 0x0045 }
        L_0x00c8:
            int r1 = r8.resolveStatus     // Catch:{ all -> 0x04ae }
            r2 = 2
            if (r1 != r2) goto L_0x00cf
            r8.resolveStatus = r15     // Catch:{ all -> 0x0045 }
        L_0x00cf:
            r1 = r23
            r2 = r12
            r3 = r2
            r4 = 0
        L_0x00d4:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r15 = r7.sortedFieldDeserializers     // Catch:{ all -> 0x04ac }
            int r15 = r15.length     // Catch:{ all -> 0x04ac }
            if (r4 >= r15) goto L_0x00e2
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r15 = r7.sortedFieldDeserializers     // Catch:{ all -> 0x04ac }
            r15 = r15[r4]     // Catch:{ all -> 0x04ac }
            com.alibaba.fastjson.util.FieldInfo r12 = r15.fieldInfo     // Catch:{ all -> 0x04ac }
            java.lang.Class<?> r6 = r12.fieldClass     // Catch:{ all -> 0x04ac }
            goto L_0x00e5
        L_0x00e2:
            r6 = 0
            r12 = 0
            r15 = 0
        L_0x00e5:
            if (r15 == 0) goto L_0x01c4
            char[] r5 = r12.name_chars     // Catch:{ all -> 0x04ac }
            java.lang.Class r11 = java.lang.Integer.TYPE     // Catch:{ all -> 0x04ac }
            r14 = -2
            if (r6 == r11) goto L_0x01a7
            java.lang.Class<java.lang.Integer> r11 = java.lang.Integer.class
            if (r6 != r11) goto L_0x00f4
            goto L_0x01a7
        L_0x00f4:
            java.lang.Class r11 = java.lang.Long.TYPE     // Catch:{ all -> 0x04ac }
            if (r6 == r11) goto L_0x0195
            java.lang.Class<java.lang.Long> r11 = java.lang.Long.class
            if (r6 != r11) goto L_0x00fe
            goto L_0x0195
        L_0x00fe:
            java.lang.Class<java.lang.String> r11 = java.lang.String.class
            if (r6 != r11) goto L_0x0112
            java.lang.String r5 = r10.scanFieldString(r5)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x010c
            goto L_0x01b3
        L_0x010c:
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 != r14) goto L_0x01c0
            goto L_0x01bb
        L_0x0112:
            java.lang.Class r11 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x04ac }
            if (r6 == r11) goto L_0x0183
            java.lang.Class<java.lang.Boolean> r11 = java.lang.Boolean.class
            if (r6 != r11) goto L_0x011c
            goto L_0x0183
        L_0x011c:
            java.lang.Class r11 = java.lang.Float.TYPE     // Catch:{ all -> 0x04ac }
            if (r6 == r11) goto L_0x0171
            java.lang.Class<java.lang.Float> r11 = java.lang.Float.class
            if (r6 != r11) goto L_0x0125
            goto L_0x0171
        L_0x0125:
            java.lang.Class r11 = java.lang.Double.TYPE     // Catch:{ all -> 0x04ac }
            if (r6 == r11) goto L_0x015f
            java.lang.Class<java.lang.Double> r11 = java.lang.Double.class
            if (r6 != r11) goto L_0x012e
            goto L_0x015f
        L_0x012e:
            boolean r11 = r6.isEnum()     // Catch:{ all -> 0x04ac }
            if (r11 == 0) goto L_0x0156
            com.alibaba.fastjson.parser.ParserConfig r11 = r20.getConfig()     // Catch:{ all -> 0x04ac }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r11 = r11.getDeserializer((java.lang.reflect.Type) r6)     // Catch:{ all -> 0x04ac }
            boolean r11 = r11 instanceof com.alibaba.fastjson.parser.deserializer.EnumDeserializer     // Catch:{ all -> 0x04ac }
            if (r11 == 0) goto L_0x0156
            com.alibaba.fastjson.parser.SymbolTable r11 = r8.symbolTable     // Catch:{ all -> 0x04ac }
            java.lang.String r5 = r10.scanFieldSymbol(r5, r11)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x0150
            java.lang.Enum r5 = java.lang.Enum.valueOf(r6, r5)     // Catch:{ all -> 0x04ac }
            goto L_0x01b3
        L_0x0150:
            int r5 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r5 != r14) goto L_0x01c4
            goto L_0x01bb
        L_0x0156:
            boolean r5 = r10.matchField(r5)     // Catch:{ all -> 0x04ac }
            if (r5 == 0) goto L_0x01bb
            r5 = 1
            goto L_0x01c5
        L_0x015f:
            double r17 = r10.scanFieldDouble((char[]) r5)     // Catch:{ all -> 0x04ac }
            java.lang.Double r5 = java.lang.Double.valueOf(r17)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x016c
            goto L_0x01b3
        L_0x016c:
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 != r14) goto L_0x01c0
            goto L_0x01bb
        L_0x0171:
            float r5 = r10.scanFieldFloat(r5)     // Catch:{ all -> 0x04ac }
            java.lang.Float r5 = java.lang.Float.valueOf(r5)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x017e
            goto L_0x01b3
        L_0x017e:
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 != r14) goto L_0x01c0
            goto L_0x01bb
        L_0x0183:
            boolean r5 = r10.scanFieldBoolean(r5)     // Catch:{ all -> 0x04ac }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x0190
            goto L_0x01b3
        L_0x0190:
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 != r14) goto L_0x01c0
            goto L_0x01bb
        L_0x0195:
            long r17 = r10.scanFieldLong(r5)     // Catch:{ all -> 0x04ac }
            java.lang.Long r5 = java.lang.Long.valueOf(r17)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x01a2
            goto L_0x01b3
        L_0x01a2:
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 != r14) goto L_0x01c0
            goto L_0x01bb
        L_0x01a7:
            int r5 = r10.scanFieldInt(r5)     // Catch:{ all -> 0x04ac }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x04ac }
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 <= 0) goto L_0x01b7
        L_0x01b3:
            r14 = r5
            r5 = 1
            r11 = 1
            goto L_0x01c7
        L_0x01b7:
            int r11 = r10.matchStat     // Catch:{ all -> 0x04ac }
            if (r11 != r14) goto L_0x01c0
        L_0x01bb:
            r17 = r4
        L_0x01bd:
            r6 = 1
            goto L_0x02d7
        L_0x01c0:
            r14 = r5
            r5 = 0
            r11 = 0
            goto L_0x01c7
        L_0x01c4:
            r5 = 0
        L_0x01c5:
            r11 = 0
            r14 = 0
        L_0x01c7:
            if (r5 != 0) goto L_0x030e
            r17 = r4
            com.alibaba.fastjson.parser.SymbolTable r4 = r8.symbolTable     // Catch:{ all -> 0x04ac }
            java.lang.String r4 = r10.scanSymbol(r4)     // Catch:{ all -> 0x04ac }
            if (r4 != 0) goto L_0x01f3
            r18 = r6
            int r6 = r10.token()     // Catch:{ all -> 0x04ac }
            r23 = r14
            r14 = 13
            if (r6 != r14) goto L_0x01e6
            r14 = 16
            r10.nextToken(r14)     // Catch:{ all -> 0x04ac }
            goto L_0x02d2
        L_0x01e6:
            r14 = 16
            if (r6 != r14) goto L_0x01f7
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x04ac }
            boolean r6 = r10.isEnabled(r6)     // Catch:{ all -> 0x04ac }
            if (r6 == 0) goto L_0x01f7
            goto L_0x01bd
        L_0x01f3:
            r18 = r6
            r23 = r14
        L_0x01f7:
            java.lang.String r6 = "$ref"
            if (r6 != r4) goto L_0x029e
            r6 = 4
            r10.nextTokenWithColon(r6)     // Catch:{ all -> 0x04ac }
            int r0 = r10.token()     // Catch:{ all -> 0x04ac }
            if (r0 != r6) goto L_0x0283
            java.lang.String r0 = r10.stringVal()     // Catch:{ all -> 0x04ac }
            java.lang.String r3 = "@"
            boolean r3 = r3.equals(r0)     // Catch:{ all -> 0x04ac }
            if (r3 == 0) goto L_0x0215
            java.lang.Object r0 = r13.object     // Catch:{ all -> 0x04ac }
            r1 = r0
            goto L_0x0260
        L_0x0215:
            java.lang.String r3 = ".."
            boolean r3 = r3.equals(r0)     // Catch:{ all -> 0x04ac }
            if (r3 == 0) goto L_0x0232
            com.alibaba.fastjson.parser.ParseContext r3 = r13.parent     // Catch:{ all -> 0x04ac }
            java.lang.Object r4 = r3.object     // Catch:{ all -> 0x04ac }
            if (r4 == 0) goto L_0x0226
            java.lang.Object r1 = r3.object     // Catch:{ all -> 0x04ac }
            goto L_0x0260
        L_0x0226:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x04ac }
            r4.<init>(r3, r0)     // Catch:{ all -> 0x04ac }
            r8.addResolveTask(r4)     // Catch:{ all -> 0x04ac }
            r0 = 1
            r8.resolveStatus = r0     // Catch:{ all -> 0x04ac }
            goto L_0x0260
        L_0x0232:
            java.lang.String r3 = "$"
            boolean r3 = r3.equals(r0)     // Catch:{ all -> 0x04ac }
            if (r3 == 0) goto L_0x0255
            r3 = r13
        L_0x023b:
            com.alibaba.fastjson.parser.ParseContext r4 = r3.parent     // Catch:{ all -> 0x04ac }
            if (r4 == 0) goto L_0x0242
            com.alibaba.fastjson.parser.ParseContext r3 = r3.parent     // Catch:{ all -> 0x04ac }
            goto L_0x023b
        L_0x0242:
            java.lang.Object r4 = r3.object     // Catch:{ all -> 0x04ac }
            if (r4 == 0) goto L_0x0249
            java.lang.Object r1 = r3.object     // Catch:{ all -> 0x04ac }
            goto L_0x0260
        L_0x0249:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x04ac }
            r4.<init>(r3, r0)     // Catch:{ all -> 0x04ac }
            r8.addResolveTask(r4)     // Catch:{ all -> 0x04ac }
            r0 = 1
            r8.resolveStatus = r0     // Catch:{ all -> 0x04ac }
            goto L_0x0260
        L_0x0255:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x04ac }
            r3.<init>(r13, r0)     // Catch:{ all -> 0x04ac }
            r8.addResolveTask(r3)     // Catch:{ all -> 0x04ac }
            r6 = 1
            r8.resolveStatus = r6     // Catch:{ all -> 0x04ac }
        L_0x0260:
            r0 = 13
            r10.nextToken(r0)     // Catch:{ all -> 0x04ac }
            int r3 = r10.token()     // Catch:{ all -> 0x04ac }
            if (r3 != r0) goto L_0x027b
            r0 = 16
            r10.nextToken(r0)     // Catch:{ all -> 0x04ac }
            r8.setContext(r13, r1, r9)     // Catch:{ all -> 0x04ac }
            if (r2 == 0) goto L_0x0277
            r2.object = r1
        L_0x0277:
            r8.setContext(r13)
            return r1
        L_0x027b:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x04ac }
            java.lang.String r3 = "illegal ref"
            r0.<init>(r3)     // Catch:{ all -> 0x04ac }
            throw r0     // Catch:{ all -> 0x04ac }
        L_0x0283:
            com.alibaba.fastjson.JSONException r3 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x04ac }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x04ac }
            r4.<init>()     // Catch:{ all -> 0x04ac }
            java.lang.String r5 = "illegal ref, "
            r4.append(r5)     // Catch:{ all -> 0x04ac }
            java.lang.String r0 = com.alibaba.fastjson.parser.JSONToken.name(r0)     // Catch:{ all -> 0x04ac }
            r4.append(r0)     // Catch:{ all -> 0x04ac }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x04ac }
            r3.<init>(r0)     // Catch:{ all -> 0x04ac }
            throw r3     // Catch:{ all -> 0x04ac }
        L_0x029e:
            r6 = 1
            java.lang.String r14 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x04ac }
            if (r14 != r4) goto L_0x0316
            r14 = 4
            r10.nextTokenWithColon(r14)     // Catch:{ all -> 0x04ac }
            int r4 = r10.token()     // Catch:{ all -> 0x04ac }
            if (r4 != r14) goto L_0x0306
            java.lang.String r4 = r10.stringVal()     // Catch:{ all -> 0x04ac }
            r5 = 16
            r10.nextToken(r5)     // Catch:{ all -> 0x04ac }
            boolean r5 = r0 instanceof java.lang.Class     // Catch:{ all -> 0x04ac }
            if (r5 == 0) goto L_0x02e6
            r5 = r0
            java.lang.Class r5 = (java.lang.Class) r5     // Catch:{ all -> 0x04ac }
            java.lang.String r5 = r5.getName()     // Catch:{ all -> 0x04ac }
            boolean r5 = r4.equals(r5)     // Catch:{ all -> 0x04ac }
            if (r5 == 0) goto L_0x02e6
            int r4 = r10.token()     // Catch:{ all -> 0x04ac }
            r5 = 13
            if (r4 != r5) goto L_0x02d7
            r10.nextToken()     // Catch:{ all -> 0x04ac }
        L_0x02d2:
            r14 = r1
            r12 = r2
            r11 = r3
            goto L_0x03ae
        L_0x02d7:
            r14 = r1
            r16 = r2
            r11 = r3
            r15 = r17
            r1 = 0
            r2 = 16
            r3 = 13
            r4 = 0
            r12 = 1
            goto L_0x0479
        L_0x02e6:
            com.alibaba.fastjson.parser.ParserConfig r0 = r20.getConfig()     // Catch:{ all -> 0x04ac }
            java.lang.ClassLoader r0 = r0.getDefaultClassLoader()     // Catch:{ all -> 0x04ac }
            java.lang.Class r0 = com.alibaba.fastjson.util.TypeUtils.loadClass(r4, r0)     // Catch:{ all -> 0x04ac }
            com.alibaba.fastjson.parser.ParserConfig r3 = r20.getConfig()     // Catch:{ all -> 0x04ac }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r3 = r3.getDeserializer((java.lang.reflect.Type) r0)     // Catch:{ all -> 0x04ac }
            java.lang.Object r0 = r3.deserialze(r8, r0, r9)     // Catch:{ all -> 0x04ac }
            if (r2 == 0) goto L_0x0302
            r2.object = r1
        L_0x0302:
            r8.setContext(r13)
            return r0
        L_0x0306:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x04ac }
            java.lang.String r3 = "syntax error"
            r0.<init>(r3)     // Catch:{ all -> 0x04ac }
            throw r0     // Catch:{ all -> 0x04ac }
        L_0x030e:
            r17 = r4
            r18 = r6
            r23 = r14
            r6 = 1
            r4 = 0
        L_0x0316:
            if (r1 != 0) goto L_0x032c
            if (r3 != 0) goto L_0x032c
            java.lang.Object r1 = r19.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r20, (java.lang.reflect.Type) r21)     // Catch:{ all -> 0x04ac }
            if (r1 != 0) goto L_0x0328
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x04ac }
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r14 = r7.fieldDeserializers     // Catch:{ all -> 0x04ac }
            int r14 = r14.length     // Catch:{ all -> 0x04ac }
            r3.<init>(r14)     // Catch:{ all -> 0x04ac }
        L_0x0328:
            com.alibaba.fastjson.parser.ParseContext r2 = r8.setContext(r13, r1, r9)     // Catch:{ all -> 0x04ac }
        L_0x032c:
            r14 = r1
            r16 = r2
            if (r5 == 0) goto L_0x036e
            if (r11 != 0) goto L_0x033c
            r15.parseField(r8, r14, r0, r3)     // Catch:{ all -> 0x04a7 }
        L_0x0336:
            r11 = r3
            r15 = r17
            r12 = 1
            goto L_0x0397
        L_0x033c:
            if (r14 != 0) goto L_0x0346
            java.lang.String r1 = r12.name     // Catch:{ all -> 0x04a7 }
            r5 = r23
            r3.put(r1, r5)     // Catch:{ all -> 0x04a7 }
            goto L_0x0367
        L_0x0346:
            r5 = r23
            if (r5 != 0) goto L_0x0364
            java.lang.Class r1 = java.lang.Integer.TYPE     // Catch:{ all -> 0x04a7 }
            r2 = r18
            if (r2 == r1) goto L_0x0367
            java.lang.Class r1 = java.lang.Long.TYPE     // Catch:{ all -> 0x04a7 }
            if (r2 == r1) goto L_0x0367
            java.lang.Class r1 = java.lang.Float.TYPE     // Catch:{ all -> 0x04a7 }
            if (r2 == r1) goto L_0x0367
            java.lang.Class r1 = java.lang.Double.TYPE     // Catch:{ all -> 0x04a7 }
            if (r2 == r1) goto L_0x0367
            java.lang.Class r1 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x04a7 }
            if (r2 == r1) goto L_0x0367
            r15.setValue((java.lang.Object) r14, (java.lang.Object) r5)     // Catch:{ all -> 0x04a7 }
            goto L_0x0367
        L_0x0364:
            r15.setValue((java.lang.Object) r14, (java.lang.Object) r5)     // Catch:{ all -> 0x04a7 }
        L_0x0367:
            int r1 = r10.matchStat     // Catch:{ all -> 0x04a7 }
            r5 = 4
            if (r1 != r5) goto L_0x0336
            r11 = r3
            goto L_0x03ac
        L_0x036e:
            r5 = 4
            r1 = r19
            r2 = r20
            r11 = r3
            r3 = r4
            r15 = r17
            r4 = r14
            r12 = 4
            r5 = r21
            r12 = 1
            r6 = r11
            boolean r1 = r1.parseField(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x04a7 }
            if (r1 != 0) goto L_0x0397
            int r1 = r10.token()     // Catch:{ all -> 0x04a7 }
            r2 = 13
            if (r1 != r2) goto L_0x038f
            r10.nextToken()     // Catch:{ all -> 0x04a7 }
            goto L_0x03ac
        L_0x038f:
            r1 = 0
            r2 = 16
        L_0x0392:
            r3 = 13
            r4 = 0
            goto L_0x0479
        L_0x0397:
            int r1 = r10.token()     // Catch:{ all -> 0x04a7 }
            r2 = 16
            if (r1 != r2) goto L_0x03a1
            r1 = 0
            goto L_0x0392
        L_0x03a1:
            int r1 = r10.token()     // Catch:{ all -> 0x04a7 }
            r3 = 13
            if (r1 != r3) goto L_0x0469
            r10.nextToken(r2)     // Catch:{ all -> 0x04a7 }
        L_0x03ac:
            r12 = r16
        L_0x03ae:
            if (r14 != 0) goto L_0x043f
            if (r11 != 0) goto L_0x03c8
            java.lang.Object r1 = r19.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r20, (java.lang.reflect.Type) r21)     // Catch:{ all -> 0x0465 }
            if (r12 != 0) goto L_0x03c0
            com.alibaba.fastjson.parser.ParseContext r12 = r8.setContext(r13, r1, r9)     // Catch:{ all -> 0x03bd }
            goto L_0x03c0
        L_0x03bd:
            r0 = move-exception
            goto L_0x0048
        L_0x03c0:
            if (r12 == 0) goto L_0x03c4
            r12.object = r1
        L_0x03c4:
            r8.setContext(r13)
            return r1
        L_0x03c8:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r7.beanInfo     // Catch:{ all -> 0x0465 }
            com.alibaba.fastjson.util.FieldInfo[] r0 = r0.fields     // Catch:{ all -> 0x0465 }
            int r1 = r0.length     // Catch:{ all -> 0x0465 }
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch:{ all -> 0x0465 }
            r3 = 0
        L_0x03d0:
            if (r3 >= r1) goto L_0x03df
            r4 = r0[r3]     // Catch:{ all -> 0x0465 }
            java.lang.String r4 = r4.name     // Catch:{ all -> 0x0465 }
            java.lang.Object r4 = r11.get(r4)     // Catch:{ all -> 0x0465 }
            r2[r3] = r4     // Catch:{ all -> 0x0465 }
            int r3 = r3 + 1
            goto L_0x03d0
        L_0x03df:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r7.beanInfo     // Catch:{ all -> 0x0465 }
            java.lang.reflect.Constructor<?> r0 = r0.creatorConstructor     // Catch:{ all -> 0x0465 }
            if (r0 == 0) goto L_0x040f
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r7.beanInfo     // Catch:{ Exception -> 0x03ef }
            java.lang.reflect.Constructor<?> r0 = r0.creatorConstructor     // Catch:{ Exception -> 0x03ef }
            java.lang.Object r0 = r0.newInstance(r2)     // Catch:{ Exception -> 0x03ef }
        L_0x03ed:
            r14 = r0
            goto L_0x043f
        L_0x03ef:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0465 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0465 }
            r2.<init>()     // Catch:{ all -> 0x0465 }
            java.lang.String r3 = "create instance error, "
            r2.append(r3)     // Catch:{ all -> 0x0465 }
            com.alibaba.fastjson.util.JavaBeanInfo r3 = r7.beanInfo     // Catch:{ all -> 0x0465 }
            java.lang.reflect.Constructor<?> r3 = r3.creatorConstructor     // Catch:{ all -> 0x0465 }
            java.lang.String r3 = r3.toGenericString()     // Catch:{ all -> 0x0465 }
            r2.append(r3)     // Catch:{ all -> 0x0465 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0465 }
            r1.<init>(r2, r0)     // Catch:{ all -> 0x0465 }
            throw r1     // Catch:{ all -> 0x0465 }
        L_0x040f:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r7.beanInfo     // Catch:{ all -> 0x0465 }
            java.lang.reflect.Method r0 = r0.factoryMethod     // Catch:{ all -> 0x0465 }
            if (r0 == 0) goto L_0x043f
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r7.beanInfo     // Catch:{ Exception -> 0x041f }
            java.lang.reflect.Method r0 = r0.factoryMethod     // Catch:{ Exception -> 0x041f }
            r1 = 0
            java.lang.Object r0 = r0.invoke(r1, r2)     // Catch:{ Exception -> 0x041f }
            goto L_0x03ed
        L_0x041f:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0465 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0465 }
            r2.<init>()     // Catch:{ all -> 0x0465 }
            java.lang.String r3 = "create factory method error, "
            r2.append(r3)     // Catch:{ all -> 0x0465 }
            com.alibaba.fastjson.util.JavaBeanInfo r3 = r7.beanInfo     // Catch:{ all -> 0x0465 }
            java.lang.reflect.Method r3 = r3.factoryMethod     // Catch:{ all -> 0x0465 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0465 }
            r2.append(r3)     // Catch:{ all -> 0x0465 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0465 }
            r1.<init>(r2, r0)     // Catch:{ all -> 0x0465 }
            throw r1     // Catch:{ all -> 0x0465 }
        L_0x043f:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r7.beanInfo     // Catch:{ all -> 0x0465 }
            java.lang.reflect.Method r0 = r0.buildMethod     // Catch:{ all -> 0x0465 }
            if (r0 != 0) goto L_0x044d
            if (r12 == 0) goto L_0x0449
            r12.object = r14
        L_0x0449:
            r8.setContext(r13)
            return r14
        L_0x044d:
            r4 = 0
            java.lang.Object[] r1 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x045c }
            java.lang.Object r0 = r0.invoke(r14, r1)     // Catch:{ Exception -> 0x045c }
            if (r12 == 0) goto L_0x0458
            r12.object = r14
        L_0x0458:
            r8.setContext(r13)
            return r0
        L_0x045c:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0465 }
            java.lang.String r2 = "build object error"
            r1.<init>(r2, r0)     // Catch:{ all -> 0x0465 }
            throw r1     // Catch:{ all -> 0x0465 }
        L_0x0465:
            r0 = move-exception
            r2 = r12
            r1 = r14
            goto L_0x04b3
        L_0x0469:
            r1 = 0
            r4 = 0
            int r5 = r10.token()     // Catch:{ all -> 0x04a7 }
            r6 = 18
            if (r5 == r6) goto L_0x0488
            int r5 = r10.token()     // Catch:{ all -> 0x04a7 }
            if (r5 == r12) goto L_0x0488
        L_0x0479:
            int r5 = r15 + 1
            r12 = r1
            r4 = r5
            r3 = r11
            r1 = r14
            r2 = r16
            r5 = 4
            r11 = 16
            r14 = 13
            goto L_0x00d4
        L_0x0488:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x04a7 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x04a7 }
            r1.<init>()     // Catch:{ all -> 0x04a7 }
            java.lang.String r2 = "syntax error, unexpect token "
            r1.append(r2)     // Catch:{ all -> 0x04a7 }
            int r2 = r10.token()     // Catch:{ all -> 0x04a7 }
            java.lang.String r2 = com.alibaba.fastjson.parser.JSONToken.name(r2)     // Catch:{ all -> 0x04a7 }
            r1.append(r2)     // Catch:{ all -> 0x04a7 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x04a7 }
            r0.<init>(r1)     // Catch:{ all -> 0x04a7 }
            throw r0     // Catch:{ all -> 0x04a7 }
        L_0x04a7:
            r0 = move-exception
            r1 = r14
            r2 = r16
            goto L_0x04b3
        L_0x04ac:
            r0 = move-exception
            goto L_0x04b3
        L_0x04ae:
            r0 = move-exception
            r1 = r12
            r2 = r1
            r1 = r23
        L_0x04b3:
            if (r2 == 0) goto L_0x04b7
            r2.object = r1
        L_0x04b7:
            r8.setContext(r13)
            throw r0
        L_0x04bb:
            java.lang.Object r0 = r20.parse()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public boolean parseField(DefaultJSONParser defaultJSONParser, String str, Object obj, Type type, Map<String, Object> map) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        FieldDeserializer smartMatch = smartMatch(str);
        if (smartMatch != null) {
            jSONLexer.nextTokenWithColon(smartMatch.getFastMatchToken());
            smartMatch.parseField(defaultJSONParser, obj, type, map);
            return true;
        } else if (jSONLexer.isEnabled(Feature.IgnoreNotMatch)) {
            defaultJSONParser.parseExtra(obj, str);
            return false;
        } else {
            throw new JSONException("setter not found, class " + this.clazz.getName() + ", property " + str);
        }
    }

    public FieldDeserializer smartMatch(String str) {
        FieldDeserializer fieldDeserializer;
        if (str == null) {
            return null;
        }
        FieldDeserializer fieldDeserializer2 = getFieldDeserializer(str);
        if (fieldDeserializer2 == null) {
            boolean startsWith = str.startsWith("is");
            FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
            int length = fieldDeserializerArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                fieldDeserializer = fieldDeserializerArr[i];
                FieldInfo fieldInfo = fieldDeserializer.fieldInfo;
                Class<?> cls = fieldInfo.fieldClass;
                String str2 = fieldInfo.name;
                if (!str2.equalsIgnoreCase(str) && (!startsWith || (!(cls == Boolean.TYPE || cls == Boolean.class) || !str2.equalsIgnoreCase(str.substring(2))))) {
                    i++;
                }
            }
            fieldDeserializer2 = fieldDeserializer;
        }
        if (fieldDeserializer2 != null || str.indexOf(95) == -1) {
            return fieldDeserializer2;
        }
        String replaceAll = str.replaceAll("_", "");
        FieldDeserializer fieldDeserializer3 = getFieldDeserializer(replaceAll);
        if (fieldDeserializer3 != null) {
            return fieldDeserializer3;
        }
        for (FieldDeserializer fieldDeserializer4 : this.sortedFieldDeserializers) {
            if (fieldDeserializer4.fieldInfo.name.equalsIgnoreCase(replaceAll)) {
                return fieldDeserializer4;
            }
        }
        return fieldDeserializer3;
    }

    public final boolean isSupportArrayToBean(JSONLexer jSONLexer) {
        return Feature.isEnabled(this.beanInfo.parserFeatures, Feature.SupportArrayToBean) || jSONLexer.isEnabled(Feature.SupportArrayToBean);
    }

    public Object createInstance(Map<String, Object> map, ParserConfig parserConfig) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (this.beanInfo.creatorConstructor == null && this.beanInfo.buildMethod == null) {
            Object createInstance = createInstance((DefaultJSONParser) null, (Type) this.clazz);
            for (Map.Entry next : map.entrySet()) {
                Object value = next.getValue();
                FieldDeserializer fieldDeserializer = getFieldDeserializer((String) next.getKey());
                if (fieldDeserializer != null) {
                    Method method = fieldDeserializer.fieldInfo.method;
                    if (method != null) {
                        method.invoke(createInstance, new Object[]{TypeUtils.cast(value, method.getGenericParameterTypes()[0], parserConfig)});
                    } else {
                        fieldDeserializer.fieldInfo.field.set(createInstance, TypeUtils.cast(value, fieldDeserializer.fieldInfo.fieldType, parserConfig));
                    }
                }
            }
            return createInstance;
        }
        FieldInfo[] fieldInfoArr = this.beanInfo.fields;
        int length = fieldInfoArr.length;
        Object[] objArr = new Object[length];
        for (int i = 0; i < length; i++) {
            objArr[i] = map.get(fieldInfoArr[i].name);
        }
        if (this.beanInfo.creatorConstructor != null) {
            try {
                return this.beanInfo.creatorConstructor.newInstance(objArr);
            } catch (Exception e) {
                throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e);
            }
        } else if (this.beanInfo.factoryMethod == null) {
            return null;
        } else {
            try {
                return this.beanInfo.factoryMethod.invoke((Object) null, objArr);
            } catch (Exception e2) {
                throw new JSONException("create factory method error, " + this.beanInfo.factoryMethod.toString(), e2);
            }
        }
    }
}
