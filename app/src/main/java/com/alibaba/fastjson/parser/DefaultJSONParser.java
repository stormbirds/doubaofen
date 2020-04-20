package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    public final JSONLexer lexer;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    static {
        primitiveClasses.add(Boolean.TYPE);
        primitiveClasses.add(Byte.TYPE);
        primitiveClasses.add(Short.TYPE);
        primitiveClasses.add(Integer.TYPE);
        primitiveClasses.add(Long.TYPE);
        primitiveClasses.add(Float.TYPE);
        primitiveClasses.add(Double.TYPE);
        primitiveClasses.add(Boolean.class);
        primitiveClasses.add(Byte.class);
        primitiveClasses.add(Short.class);
        primitiveClasses.add(Integer.class);
        primitiveClasses.add(Long.class);
        primitiveClasses.add(Float.class);
        primitiveClasses.add(Double.class);
        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);
        primitiveClasses.add(String.class);
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat2) {
        this.dateFormat = dateFormat2;
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this((Object) str, (JSONLexer) new JSONScanner(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this((Object) str, (JSONLexer) new JSONScanner(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this((Object) cArr, (JSONLexer) new JSONScanner(cArr, i, i2), parserConfig);
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this((Object) null, jSONLexer, parserConfig);
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.lexer = jSONLexer;
        this.input = obj;
        this.config = parserConfig;
        this.symbolTable = parserConfig.symbolTable;
        jSONLexer.nextToken(12);
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        Object obj = this.input;
        if (obj instanceof char[]) {
            return new String((char[]) obj);
        }
        return obj.toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v23, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v26, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v27, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v28, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v29, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v6, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v47, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v50, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v51, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v52, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v53, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v54, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v55, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v56, resolved type: java.util.Date} */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0213, code lost:
        r0 = new java.util.HashMap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x021f, code lost:
        if ("java.util.Collections$EmptyMap".equals(r5) == false) goto L_0x0226;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0221, code lost:
        r0 = java.util.Collections.emptyMap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0226, code lost:
        r0 = r6.newInstance();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x022a, code lost:
        setContext(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x022d, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0237, code lost:
        setResolveStatus(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x023d, code lost:
        if (r1.context == null) goto L_0x0246;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x0241, code lost:
        if ((r2 instanceof java.lang.Integer) != false) goto L_0x0246;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x0243, code lost:
        popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x024a, code lost:
        if (r17.size() <= 0) goto L_0x0259;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x024c, code lost:
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r6, r1.config);
        parseObject((java.lang.Object) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0255, code lost:
        setContext(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x0258, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:?, code lost:
        r0 = r1.config.getDeserializer((java.lang.reflect.Type) r6).deserialze(r1, r6, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x0263, code lost:
        setContext(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0266, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x0273, code lost:
        r3.nextToken(4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x027c, code lost:
        if (r3.token() != 4) goto L_0x030b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x027e, code lost:
        r0 = r3.stringVal();
        r3.nextToken(13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x028d, code lost:
        if ("@".equals(r0) == false) goto L_0x02a9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x0291, code lost:
        if (r1.context == null) goto L_0x02f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0293, code lost:
        r0 = r1.context;
        r5 = r0.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x0299, code lost:
        if ((r5 instanceof java.lang.Object[]) != false) goto L_0x02f4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x029d, code lost:
        if ((r5 instanceof java.util.Collection) == false) goto L_0x02a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x02a2, code lost:
        if (r0.parent == null) goto L_0x02f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x02a4, code lost:
        r5 = r0.parent.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x02af, code lost:
        if ("..".equals(r0) == false) goto L_0x02c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x02b3, code lost:
        if (r4.object == null) goto L_0x02b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x02b5, code lost:
        r5 = r4.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x02b8, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r4, r0));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x02ca, code lost:
        if ("$".equals(r0) == false) goto L_0x02e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x02cc, code lost:
        r2 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x02cf, code lost:
        if (r2.parent == null) goto L_0x02d4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x02d1, code lost:
        r2 = r2.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x02d6, code lost:
        if (r2.object == null) goto L_0x02dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x02d8, code lost:
        r5 = r2.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x02dc, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r2, r0));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x02e8, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r4, r0));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x02f3, code lost:
        r5 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x02fa, code lost:
        if (r3.token() != 13) goto L_0x0305;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x02fc, code lost:
        r3.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x0301, code lost:
        setContext(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x0304, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x030a, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x0329, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref, " + com.alibaba.fastjson.parser.JSONToken.name(r3.token()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:0x03ab, code lost:
        if (r6 != '}') goto L_0x03bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:207:0x03ad, code lost:
        r3.next();
        r3.resetStringPosition();
        r3.nextToken();
        setContext(r5, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x03b9, code lost:
        setContext(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x03bc, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:212:0x03dd, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, position at " + r3.pos() + ", name " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01e0, code lost:
        r3.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x01eb, code lost:
        if (r3.token() != 13) goto L_0x0237;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01ed, code lost:
        r3.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
        r0 = r1.config.getDeserializer((java.lang.reflect.Type) r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x01f8, code lost:
        if ((r0 instanceof com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer) == false) goto L_0x0201;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01fa, code lost:
        r0 = ((com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer) r0).createInstance(r1, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0203, code lost:
        if ((r0 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L_0x020c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0205, code lost:
        r0 = ((com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0).createInstance(r1, (java.lang.reflect.Type) r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x020c, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x020d, code lost:
        if (r0 != null) goto L_0x022a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0211, code lost:
        if (r6 != java.lang.Cloneable.class) goto L_0x0219;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0267  */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x0464 A[Catch:{ Exception -> 0x022e, all -> 0x0526 }] */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x047d A[Catch:{ Exception -> 0x022e, all -> 0x0526 }] */
    /* JADX WARNING: Removed duplicated region for block: B:258:0x0485 A[Catch:{ Exception -> 0x022e, all -> 0x0526 }] */
    /* JADX WARNING: Removed duplicated region for block: B:260:0x048a A[Catch:{ Exception -> 0x022e, all -> 0x0526 }] */
    /* JADX WARNING: Removed duplicated region for block: B:266:0x049f A[SYNTHETIC, Splitter:B:266:0x049f] */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x0495 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01a7 A[Catch:{ Exception -> 0x022e, all -> 0x0526 }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01c0 A[Catch:{ Exception -> 0x022e, all -> 0x0526 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object parseObject(java.util.Map r17, java.lang.Object r18) {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            r2 = r18
            com.alibaba.fastjson.parser.JSONLexer r3 = r1.lexer
            int r4 = r3.token()
            r5 = 0
            r6 = 8
            if (r4 != r6) goto L_0x0015
            r3.nextToken()
            return r5
        L_0x0015:
            int r4 = r3.token()
            r6 = 13
            if (r4 != r6) goto L_0x0021
            r3.nextToken()
            return r0
        L_0x0021:
            int r4 = r3.token()
            r7 = 12
            r8 = 16
            if (r4 == r7) goto L_0x0059
            int r4 = r3.token()
            if (r4 != r8) goto L_0x0032
            goto L_0x0059
        L_0x0032:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "syntax error, expect {, actual "
            r2.append(r4)
            java.lang.String r4 = r3.tokenName()
            r2.append(r4)
            java.lang.String r4 = ", "
            r2.append(r4)
            java.lang.String r3 = r3.info()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            throw r0
        L_0x0059:
            com.alibaba.fastjson.parser.ParseContext r4 = r1.context
            r9 = 0
        L_0x005c:
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
            char r10 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.Feature r11 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x0526 }
            boolean r11 = r3.isEnabled(r11)     // Catch:{ all -> 0x0526 }
            r12 = 44
            if (r11 == 0) goto L_0x007a
        L_0x006d:
            if (r10 != r12) goto L_0x007a
            r3.next()     // Catch:{ all -> 0x0526 }
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
            char r10 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            goto L_0x006d
        L_0x007a:
            r11 = 48
            r13 = 125(0x7d, float:1.75E-43)
            java.lang.String r15 = ", name "
            java.lang.String r5 = "expect ':' at "
            r7 = 58
            r6 = 34
            java.lang.String r8 = "syntax error"
            r14 = 1
            if (r10 != r6) goto L_0x00bc
            com.alibaba.fastjson.parser.SymbolTable r10 = r1.symbolTable     // Catch:{ all -> 0x0526 }
            java.lang.String r10 = r3.scanSymbol(r10, r6)     // Catch:{ all -> 0x0526 }
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
            char r6 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            if (r6 != r7) goto L_0x009d
        L_0x009a:
            r5 = 0
            goto L_0x01a5
        L_0x009d:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.pos()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            r2.append(r15)     // Catch:{ all -> 0x0526 }
            r2.append(r10)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x00bc:
            if (r10 != r13) goto L_0x00cb
            r3.next()     // Catch:{ all -> 0x0526 }
            r3.resetStringPosition()     // Catch:{ all -> 0x0526 }
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x00cb:
            r6 = 39
            if (r10 != r6) goto L_0x0106
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x0526 }
            boolean r10 = r3.isEnabled(r10)     // Catch:{ all -> 0x0526 }
            if (r10 == 0) goto L_0x0100
            com.alibaba.fastjson.parser.SymbolTable r10 = r1.symbolTable     // Catch:{ all -> 0x0526 }
            java.lang.String r10 = r3.scanSymbol(r10, r6)     // Catch:{ all -> 0x0526 }
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
            char r6 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            if (r6 != r7) goto L_0x00e7
            goto L_0x009a
        L_0x00e7:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.pos()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x0100:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            r0.<init>(r8)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x0106:
            r6 = 26
            if (r10 == r6) goto L_0x0520
            if (r10 == r12) goto L_0x051a
            if (r10 < r11) goto L_0x0112
            r6 = 57
            if (r10 <= r6) goto L_0x0116
        L_0x0112:
            r6 = 45
            if (r10 != r6) goto L_0x0154
        L_0x0116:
            r3.resetStringPosition()     // Catch:{ all -> 0x0526 }
            r3.scanNumber()     // Catch:{ all -> 0x0526 }
            int r6 = r3.token()     // Catch:{ all -> 0x0526 }
            r10 = 2
            if (r6 != r10) goto L_0x0128
            java.lang.Number r6 = r3.integerValue()     // Catch:{ all -> 0x0526 }
            goto L_0x012c
        L_0x0128:
            java.lang.Number r6 = r3.decimalValue(r14)     // Catch:{ all -> 0x0526 }
        L_0x012c:
            r10 = r6
            char r6 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            if (r6 != r7) goto L_0x0135
            goto L_0x009a
        L_0x0135:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.pos()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            r2.append(r15)     // Catch:{ all -> 0x0526 }
            r2.append(r10)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x0154:
            r6 = 123(0x7b, float:1.72E-43)
            if (r10 == r6) goto L_0x019d
            r6 = 91
            if (r10 != r6) goto L_0x015d
            goto L_0x019d
        L_0x015d:
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x0526 }
            boolean r6 = r3.isEnabled(r6)     // Catch:{ all -> 0x0526 }
            if (r6 == 0) goto L_0x0197
            com.alibaba.fastjson.parser.SymbolTable r6 = r1.symbolTable     // Catch:{ all -> 0x0526 }
            java.lang.String r10 = r3.scanSymbolUnQuoted(r6)     // Catch:{ all -> 0x0526 }
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
            char r6 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            if (r6 != r7) goto L_0x0176
            goto L_0x009a
        L_0x0176:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.pos()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            java.lang.String r3 = ", actual "
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            r2.append(r6)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x0197:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            r0.<init>(r8)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x019d:
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            java.lang.Object r10 = r16.parse()     // Catch:{ all -> 0x0526 }
            r5 = 1
        L_0x01a5:
            if (r5 != 0) goto L_0x01ad
            r3.next()     // Catch:{ all -> 0x0526 }
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
        L_0x01ad:
            char r5 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            r3.resetStringPosition()     // Catch:{ all -> 0x0526 }
            java.lang.String r6 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x0526 }
            if (r10 != r6) goto L_0x0267
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x0526 }
            boolean r6 = r3.isEnabled(r6)     // Catch:{ all -> 0x0526 }
            if (r6 != 0) goto L_0x0267
            com.alibaba.fastjson.parser.SymbolTable r5 = r1.symbolTable     // Catch:{ all -> 0x0526 }
            r6 = 34
            java.lang.String r5 = r3.scanSymbol(r5, r6)     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.ParserConfig r6 = r1.config     // Catch:{ all -> 0x0526 }
            java.lang.ClassLoader r6 = r6.getDefaultClassLoader()     // Catch:{ all -> 0x0526 }
            java.lang.Class r6 = com.alibaba.fastjson.util.TypeUtils.loadClass(r5, r6)     // Catch:{ all -> 0x0526 }
            if (r6 != 0) goto L_0x01e0
            java.lang.String r6 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x0526 }
            r0.put(r6, r5)     // Catch:{ all -> 0x0526 }
            r5 = 0
            r6 = 13
        L_0x01dc:
            r8 = 16
            goto L_0x005c
        L_0x01e0:
            r7 = 16
            r3.nextToken(r7)     // Catch:{ all -> 0x0526 }
            int r8 = r3.token()     // Catch:{ all -> 0x0526 }
            r9 = 13
            if (r8 != r9) goto L_0x0237
            r3.nextToken(r7)     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.ParserConfig r0 = r1.config     // Catch:{ Exception -> 0x022e }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r0.getDeserializer((java.lang.reflect.Type) r6)     // Catch:{ Exception -> 0x022e }
            boolean r2 = r0 instanceof com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer     // Catch:{ Exception -> 0x022e }
            if (r2 == 0) goto L_0x0201
            com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer r0 = (com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer) r0     // Catch:{ Exception -> 0x022e }
            java.lang.Object r0 = r0.createInstance(r1, r6)     // Catch:{ Exception -> 0x022e }
            goto L_0x020d
        L_0x0201:
            boolean r2 = r0 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ Exception -> 0x022e }
            if (r2 == 0) goto L_0x020c
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0     // Catch:{ Exception -> 0x022e }
            java.lang.Object r0 = r0.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r1, (java.lang.reflect.Type) r6)     // Catch:{ Exception -> 0x022e }
            goto L_0x020d
        L_0x020c:
            r0 = 0
        L_0x020d:
            if (r0 != 0) goto L_0x022a
            java.lang.Class<java.lang.Cloneable> r0 = java.lang.Cloneable.class
            if (r6 != r0) goto L_0x0219
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x022e }
            r0.<init>()     // Catch:{ Exception -> 0x022e }
            goto L_0x022a
        L_0x0219:
            java.lang.String r0 = "java.util.Collections$EmptyMap"
            boolean r0 = r0.equals(r5)     // Catch:{ Exception -> 0x022e }
            if (r0 == 0) goto L_0x0226
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ Exception -> 0x022e }
            goto L_0x022a
        L_0x0226:
            java.lang.Object r0 = r6.newInstance()     // Catch:{ Exception -> 0x022e }
        L_0x022a:
            r1.setContext(r4)
            return r0
        L_0x022e:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.String r3 = "create instance error"
            r2.<init>(r3, r0)     // Catch:{ all -> 0x0526 }
            throw r2     // Catch:{ all -> 0x0526 }
        L_0x0237:
            r3 = 2
            r1.setResolveStatus(r3)     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.ParseContext r3 = r1.context     // Catch:{ all -> 0x0526 }
            if (r3 == 0) goto L_0x0246
            boolean r3 = r2 instanceof java.lang.Integer     // Catch:{ all -> 0x0526 }
            if (r3 != 0) goto L_0x0246
            r16.popContext()     // Catch:{ all -> 0x0526 }
        L_0x0246:
            int r3 = r17.size()     // Catch:{ all -> 0x0526 }
            if (r3 <= 0) goto L_0x0259
            com.alibaba.fastjson.parser.ParserConfig r2 = r1.config     // Catch:{ all -> 0x0526 }
            java.lang.Object r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r6, (com.alibaba.fastjson.parser.ParserConfig) r2)     // Catch:{ all -> 0x0526 }
            r1.parseObject((java.lang.Object) r0)     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x0259:
            com.alibaba.fastjson.parser.ParserConfig r0 = r1.config     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r0.getDeserializer((java.lang.reflect.Type) r6)     // Catch:{ all -> 0x0526 }
            java.lang.Object r0 = r0.deserialze(r1, r6, r2)     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x0267:
            java.lang.String r6 = "$ref"
            if (r10 != r6) goto L_0x032a
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x0526 }
            boolean r6 = r3.isEnabled(r6)     // Catch:{ all -> 0x0526 }
            if (r6 != 0) goto L_0x032a
            r0 = 4
            r3.nextToken(r0)     // Catch:{ all -> 0x0526 }
            int r0 = r3.token()     // Catch:{ all -> 0x0526 }
            r2 = 4
            if (r0 != r2) goto L_0x030b
            java.lang.String r0 = r3.stringVal()     // Catch:{ all -> 0x0526 }
            r2 = 13
            r3.nextToken(r2)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = "@"
            boolean r2 = r2.equals(r0)     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x02a9
            com.alibaba.fastjson.parser.ParseContext r0 = r1.context     // Catch:{ all -> 0x0526 }
            if (r0 == 0) goto L_0x02f3
            com.alibaba.fastjson.parser.ParseContext r0 = r1.context     // Catch:{ all -> 0x0526 }
            java.lang.Object r5 = r0.object     // Catch:{ all -> 0x0526 }
            boolean r2 = r5 instanceof java.lang.Object[]     // Catch:{ all -> 0x0526 }
            if (r2 != 0) goto L_0x02f4
            boolean r2 = r5 instanceof java.util.Collection     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x02a0
            goto L_0x02f4
        L_0x02a0:
            com.alibaba.fastjson.parser.ParseContext r2 = r0.parent     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x02f3
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x0526 }
            java.lang.Object r5 = r0.object     // Catch:{ all -> 0x0526 }
            goto L_0x02f4
        L_0x02a9:
            java.lang.String r2 = ".."
            boolean r2 = r2.equals(r0)     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x02c4
            java.lang.Object r2 = r4.object     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x02b8
            java.lang.Object r5 = r4.object     // Catch:{ all -> 0x0526 }
            goto L_0x02f4
        L_0x02b8:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r2 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x0526 }
            r2.<init>(r4, r0)     // Catch:{ all -> 0x0526 }
            r1.addResolveTask(r2)     // Catch:{ all -> 0x0526 }
            r1.setResolveStatus(r14)     // Catch:{ all -> 0x0526 }
            goto L_0x02f3
        L_0x02c4:
            java.lang.String r2 = "$"
            boolean r2 = r2.equals(r0)     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x02e8
            r2 = r4
        L_0x02cd:
            com.alibaba.fastjson.parser.ParseContext r5 = r2.parent     // Catch:{ all -> 0x0526 }
            if (r5 == 0) goto L_0x02d4
            com.alibaba.fastjson.parser.ParseContext r2 = r2.parent     // Catch:{ all -> 0x0526 }
            goto L_0x02cd
        L_0x02d4:
            java.lang.Object r5 = r2.object     // Catch:{ all -> 0x0526 }
            if (r5 == 0) goto L_0x02dc
            java.lang.Object r0 = r2.object     // Catch:{ all -> 0x0526 }
            r5 = r0
            goto L_0x02f4
        L_0x02dc:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r5 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x0526 }
            r5.<init>(r2, r0)     // Catch:{ all -> 0x0526 }
            r1.addResolveTask(r5)     // Catch:{ all -> 0x0526 }
            r1.setResolveStatus(r14)     // Catch:{ all -> 0x0526 }
            goto L_0x02f3
        L_0x02e8:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r2 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x0526 }
            r2.<init>(r4, r0)     // Catch:{ all -> 0x0526 }
            r1.addResolveTask(r2)     // Catch:{ all -> 0x0526 }
            r1.setResolveStatus(r14)     // Catch:{ all -> 0x0526 }
        L_0x02f3:
            r5 = 0
        L_0x02f4:
            int r0 = r3.token()     // Catch:{ all -> 0x0526 }
            r2 = 13
            if (r0 != r2) goto L_0x0305
            r0 = 16
            r3.nextToken(r0)     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r5
        L_0x0305:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            r0.<init>(r8)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x030b:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            java.lang.String r5 = "illegal ref, "
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.token()     // Catch:{ all -> 0x0526 }
            java.lang.String r3 = com.alibaba.fastjson.parser.JSONToken.name(r3)     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x032a:
            if (r9 != 0) goto L_0x0334
            com.alibaba.fastjson.parser.ParseContext r6 = r16.setContext(r17, r18)     // Catch:{ all -> 0x0526 }
            if (r4 != 0) goto L_0x0333
            r4 = r6
        L_0x0333:
            r9 = 1
        L_0x0334:
            java.lang.Class r6 = r17.getClass()     // Catch:{ all -> 0x0526 }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r7 = com.alibaba.fastjson.JSONObject.class
            if (r6 != r7) goto L_0x0346
            if (r10 != 0) goto L_0x0341
            java.lang.String r6 = "null"
            goto L_0x0345
        L_0x0341:
            java.lang.String r6 = r10.toString()     // Catch:{ all -> 0x0526 }
        L_0x0345:
            r10 = r6
        L_0x0346:
            r6 = 34
            if (r5 != r6) goto L_0x0373
            r3.scanString()     // Catch:{ all -> 0x0526 }
            java.lang.String r5 = r3.stringVal()     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat     // Catch:{ all -> 0x0526 }
            boolean r6 = r3.isEnabled(r6)     // Catch:{ all -> 0x0526 }
            if (r6 == 0) goto L_0x036f
            com.alibaba.fastjson.parser.JSONScanner r6 = new com.alibaba.fastjson.parser.JSONScanner     // Catch:{ all -> 0x0526 }
            r6.<init>(r5)     // Catch:{ all -> 0x0526 }
            boolean r7 = r6.scanISO8601DateIfMatch()     // Catch:{ all -> 0x0526 }
            if (r7 == 0) goto L_0x036c
            java.util.Calendar r5 = r6.getCalendar()     // Catch:{ all -> 0x0526 }
            java.util.Date r5 = r5.getTime()     // Catch:{ all -> 0x0526 }
        L_0x036c:
            r6.close()     // Catch:{ all -> 0x0526 }
        L_0x036f:
            r0.put(r10, r5)     // Catch:{ all -> 0x0526 }
            goto L_0x0399
        L_0x0373:
            if (r5 < r11) goto L_0x0379
            r6 = 57
            if (r5 <= r6) goto L_0x037d
        L_0x0379:
            r6 = 45
            if (r5 != r6) goto L_0x03de
        L_0x037d:
            r3.scanNumber()     // Catch:{ all -> 0x0526 }
            int r5 = r3.token()     // Catch:{ all -> 0x0526 }
            r6 = 2
            if (r5 != r6) goto L_0x038c
            java.lang.Number r5 = r3.integerValue()     // Catch:{ all -> 0x0526 }
            goto L_0x0396
        L_0x038c:
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.UseBigDecimal     // Catch:{ all -> 0x0526 }
            boolean r5 = r3.isEnabled(r5)     // Catch:{ all -> 0x0526 }
            java.lang.Number r5 = r3.decimalValue(r5)     // Catch:{ all -> 0x0526 }
        L_0x0396:
            r0.put(r10, r5)     // Catch:{ all -> 0x0526 }
        L_0x0399:
            r3.skipWhitespace()     // Catch:{ all -> 0x0526 }
            char r6 = r3.getCurrent()     // Catch:{ all -> 0x0526 }
            if (r6 != r12) goto L_0x03ab
            r3.next()     // Catch:{ all -> 0x0526 }
        L_0x03a5:
            r6 = 13
            r7 = 16
            goto L_0x04f6
        L_0x03ab:
            if (r6 != r13) goto L_0x03bd
            r3.next()     // Catch:{ all -> 0x0526 }
            r3.resetStringPosition()     // Catch:{ all -> 0x0526 }
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            r1.setContext(r5, r10)     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x03bd:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            java.lang.String r5 = "syntax error, position at "
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.pos()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            r2.append(r15)     // Catch:{ all -> 0x0526 }
            r2.append(r10)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x03de:
            r6 = 91
            if (r5 != r6) goto L_0x041a
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.JSONArray r5 = new com.alibaba.fastjson.JSONArray     // Catch:{ all -> 0x0526 }
            r5.<init>()     // Catch:{ all -> 0x0526 }
            r1.parseArray((java.util.Collection) r5, (java.lang.Object) r10)     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.UseObjectArray     // Catch:{ all -> 0x0526 }
            boolean r6 = r3.isEnabled(r6)     // Catch:{ all -> 0x0526 }
            if (r6 == 0) goto L_0x03f9
            java.lang.Object[] r5 = r5.toArray()     // Catch:{ all -> 0x0526 }
        L_0x03f9:
            r0.put(r10, r5)     // Catch:{ all -> 0x0526 }
            int r5 = r3.token()     // Catch:{ all -> 0x0526 }
            r6 = 13
            if (r5 != r6) goto L_0x040b
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x040b:
            int r5 = r3.token()     // Catch:{ all -> 0x0526 }
            r6 = 16
            if (r5 != r6) goto L_0x0414
            goto L_0x03a5
        L_0x0414:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            r0.<init>(r8)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x041a:
            r6 = 123(0x7b, float:1.72E-43)
            if (r5 != r6) goto L_0x04c9
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            if (r2 == 0) goto L_0x042d
            java.lang.Class r5 = r18.getClass()     // Catch:{ all -> 0x0526 }
            java.lang.Class<java.lang.Integer> r6 = java.lang.Integer.class
            if (r5 != r6) goto L_0x042d
            r5 = 1
            goto L_0x042e
        L_0x042d:
            r5 = 0
        L_0x042e:
            com.alibaba.fastjson.JSONObject r6 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.OrderedField     // Catch:{ all -> 0x0526 }
            boolean r7 = r3.isEnabled(r7)     // Catch:{ all -> 0x0526 }
            r6.<init>((boolean) r7)     // Catch:{ all -> 0x0526 }
            if (r5 != 0) goto L_0x0440
            com.alibaba.fastjson.parser.ParseContext r7 = r1.setContext(r4, r6, r10)     // Catch:{ all -> 0x0526 }
            goto L_0x0441
        L_0x0440:
            r7 = 0
        L_0x0441:
            com.alibaba.fastjson.parser.deserializer.FieldTypeResolver r8 = r1.fieldTypeResolver     // Catch:{ all -> 0x0526 }
            if (r8 == 0) goto L_0x0460
            if (r10 == 0) goto L_0x044c
            java.lang.String r8 = r10.toString()     // Catch:{ all -> 0x0526 }
            goto L_0x044d
        L_0x044c:
            r8 = 0
        L_0x044d:
            com.alibaba.fastjson.parser.deserializer.FieldTypeResolver r11 = r1.fieldTypeResolver     // Catch:{ all -> 0x0526 }
            java.lang.reflect.Type r8 = r11.resolve(r0, r8)     // Catch:{ all -> 0x0526 }
            if (r8 == 0) goto L_0x0460
            com.alibaba.fastjson.parser.ParserConfig r11 = r1.config     // Catch:{ all -> 0x0526 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r11 = r11.getDeserializer((java.lang.reflect.Type) r8)     // Catch:{ all -> 0x0526 }
            java.lang.Object r8 = r11.deserialze(r1, r8, r10)     // Catch:{ all -> 0x0526 }
            goto L_0x0462
        L_0x0460:
            r8 = 0
            r14 = 0
        L_0x0462:
            if (r14 != 0) goto L_0x0468
            java.lang.Object r8 = r1.parseObject((java.util.Map) r6, (java.lang.Object) r10)     // Catch:{ all -> 0x0526 }
        L_0x0468:
            if (r7 == 0) goto L_0x046e
            if (r6 == r8) goto L_0x046e
            r7.object = r0     // Catch:{ all -> 0x0526 }
        L_0x046e:
            java.lang.String r6 = r10.toString()     // Catch:{ all -> 0x0526 }
            r1.checkMapResolve(r0, r6)     // Catch:{ all -> 0x0526 }
            java.lang.Class r6 = r17.getClass()     // Catch:{ all -> 0x0526 }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r7 = com.alibaba.fastjson.JSONObject.class
            if (r6 != r7) goto L_0x0485
            java.lang.String r6 = r10.toString()     // Catch:{ all -> 0x0526 }
            r0.put(r6, r8)     // Catch:{ all -> 0x0526 }
            goto L_0x0488
        L_0x0485:
            r0.put(r10, r8)     // Catch:{ all -> 0x0526 }
        L_0x0488:
            if (r5 == 0) goto L_0x048d
            r1.setContext(r8, r10)     // Catch:{ all -> 0x0526 }
        L_0x048d:
            int r6 = r3.token()     // Catch:{ all -> 0x0526 }
            r7 = 13
            if (r6 != r7) goto L_0x049f
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x049f:
            int r6 = r3.token()     // Catch:{ all -> 0x0526 }
            r7 = 16
            if (r6 != r7) goto L_0x04ae
            if (r5 == 0) goto L_0x03a5
            r16.popContext()     // Catch:{ all -> 0x0526 }
            goto L_0x03a5
        L_0x04ae:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            java.lang.String r5 = "syntax error, "
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            java.lang.String r3 = r3.tokenName()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x04c9:
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            java.lang.Object r5 = r16.parse()     // Catch:{ all -> 0x0526 }
            java.lang.Class r6 = r17.getClass()     // Catch:{ all -> 0x0526 }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r7 = com.alibaba.fastjson.JSONObject.class
            if (r6 != r7) goto L_0x04dc
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0526 }
        L_0x04dc:
            r0.put(r10, r5)     // Catch:{ all -> 0x0526 }
            int r5 = r3.token()     // Catch:{ all -> 0x0526 }
            r6 = 13
            if (r5 != r6) goto L_0x04ee
            r3.nextToken()     // Catch:{ all -> 0x0526 }
            r1.setContext(r4)
            return r0
        L_0x04ee:
            int r5 = r3.token()     // Catch:{ all -> 0x0526 }
            r7 = 16
            if (r5 != r7) goto L_0x04f9
        L_0x04f6:
            r5 = 0
            goto L_0x01dc
        L_0x04f9:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0526 }
            r2.<init>()     // Catch:{ all -> 0x0526 }
            java.lang.String r5 = "syntax error, position at "
            r2.append(r5)     // Catch:{ all -> 0x0526 }
            int r3 = r3.pos()     // Catch:{ all -> 0x0526 }
            r2.append(r3)     // Catch:{ all -> 0x0526 }
            r2.append(r15)     // Catch:{ all -> 0x0526 }
            r2.append(r10)     // Catch:{ all -> 0x0526 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0526 }
            r0.<init>(r2)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x051a:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            r0.<init>(r8)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x0520:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0526 }
            r0.<init>(r8)     // Catch:{ all -> 0x0526 }
            throw r0     // Catch:{ all -> 0x0526 }
        L_0x0526:
            r0 = move-exception
            r1.setContext(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public void setConfig(ParserConfig parserConfig) {
        this.config = parserConfig;
    }

    public <T> T parseObject(Class<T> cls) {
        return parseObject((Type) cls, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return parseObject(type, (Object) null);
    }

    public <T> T parseObject(Type type, Object obj) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (this.lexer.token() == 4) {
            type = TypeUtils.unwrap(type);
            if (type == byte[].class) {
                Object bytesValue = this.lexer.bytesValue();
                this.lexer.nextToken();
                return bytesValue;
            } else if (type == char[].class) {
                String stringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return stringVal.toCharArray();
            }
        }
        try {
            return this.config.getDeserializer(type).deserialze(this, type, obj);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable th) {
            throw new JSONException(th.getMessage(), th);
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        parseArray((Class<?>) cls, (Collection) arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, (Object) null);
    }

    /* JADX INFO: finally extract failed */
    public void parseArray(Type type, Collection collection, Object obj) {
        ObjectDeserializer objectDeserializer;
        if (this.lexer.token() == 21 || this.lexer.token() == 22) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == 14) {
            if (Integer.TYPE == type) {
                objectDeserializer = IntegerCodec.instance;
                this.lexer.nextToken(2);
            } else if (String.class == type) {
                objectDeserializer = StringCodec.instance;
                this.lexer.nextToken(4);
            } else {
                objectDeserializer = this.config.getDeserializer(type);
                this.lexer.nextToken(objectDeserializer.getFastMatchToken());
            }
            ParseContext parseContext = this.context;
            setContext(collection, obj);
            int i = 0;
            while (true) {
                try {
                    if (this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                        while (this.lexer.token() == 16) {
                            this.lexer.nextToken();
                        }
                    }
                    if (this.lexer.token() == 15) {
                        setContext(parseContext);
                        this.lexer.nextToken(16);
                        return;
                    }
                    Object obj2 = null;
                    if (Integer.TYPE == type) {
                        collection.add(IntegerCodec.instance.deserialze(this, (Type) null, (Object) null));
                    } else if (String.class == type) {
                        if (this.lexer.token() == 4) {
                            obj2 = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                        } else {
                            Object parse = parse();
                            if (parse != null) {
                                obj2 = parse.toString();
                            }
                        }
                        collection.add(obj2);
                    } else {
                        if (this.lexer.token() == 8) {
                            this.lexer.nextToken();
                        } else {
                            obj2 = objectDeserializer.deserialze(this, type, Integer.valueOf(i));
                        }
                        collection.add(obj2);
                        checkListResolve(collection);
                    }
                    if (this.lexer.token() == 16) {
                        this.lexer.nextToken(objectDeserializer.getFastMatchToken());
                    }
                    i++;
                } catch (Throwable th) {
                    setContext(parseContext);
                    throw th;
                }
            }
        } else {
            throw new JSONException("exepct '[', but " + JSONToken.name(this.lexer.token()) + ", " + this.lexer.info());
        }
    }

    public Object[] parseArray(Type[] typeArr) {
        Object obj;
        boolean z;
        Class<?> cls;
        Object obj2;
        Type[] typeArr2 = typeArr;
        int i = 8;
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        }
        int i2 = 14;
        if (this.lexer.token() == 14) {
            Object[] objArr = new Object[typeArr2.length];
            if (typeArr2.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token() == 15) {
                    this.lexer.nextToken(16);
                    return new Object[0];
                }
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(2);
            int i3 = 0;
            while (i3 < typeArr2.length) {
                if (this.lexer.token() == i) {
                    this.lexer.nextToken(16);
                    obj = null;
                } else {
                    Type type = typeArr2[i3];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token() == 2) {
                            obj = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            obj = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else if (type == String.class) {
                        if (this.lexer.token() == 4) {
                            obj2 = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                        } else {
                            obj2 = TypeUtils.cast(parse(), type, this.config);
                        }
                        obj = obj2;
                    } else {
                        if (i3 != typeArr2.length - 1 || !(type instanceof Class)) {
                            cls = null;
                            z = false;
                        } else {
                            Class cls2 = (Class) type;
                            z = cls2.isArray();
                            cls = cls2.getComponentType();
                        }
                        if (!z || this.lexer.token() == i2) {
                            obj = this.config.getDeserializer(type).deserialze(this, type, (Object) null);
                        } else {
                            ArrayList arrayList = new ArrayList();
                            ObjectDeserializer deserializer = this.config.getDeserializer((Type) cls);
                            int fastMatchToken = deserializer.getFastMatchToken();
                            if (this.lexer.token() != 15) {
                                while (true) {
                                    arrayList.add(deserializer.deserialze(this, type, (Object) null));
                                    if (this.lexer.token() != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(fastMatchToken);
                                }
                                if (this.lexer.token() != 15) {
                                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                                }
                            }
                            obj = TypeUtils.cast((Object) arrayList, type, this.config);
                        }
                    }
                }
                objArr[i3] = obj;
                if (this.lexer.token() == 15) {
                    break;
                } else if (this.lexer.token() == 16) {
                    if (i3 == typeArr2.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i3++;
                    i = 8;
                    i2 = 14;
                } else {
                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                }
            }
            if (this.lexer.token() == 15) {
                this.lexer.nextToken(16);
                return objArr;
            }
            throw new JSONException("syntax error");
        }
        throw new JSONException("syntax error : " + this.lexer.tokenName());
    }

    public void parseObject(Object obj) {
        JavaBeanDeserializer javaBeanDeserializer;
        Object obj2;
        Class<?> cls = obj.getClass();
        ObjectDeserializer deserializer = this.config.getDeserializer((Type) cls);
        if (deserializer instanceof JavaBeanDeserializer) {
            javaBeanDeserializer = (JavaBeanDeserializer) deserializer;
        } else {
            javaBeanDeserializer = deserializer instanceof ASMJavaBeanDeserializer ? ((ASMJavaBeanDeserializer) deserializer).getInnterSerializer() : null;
        }
        if (this.lexer.token() == 12 || this.lexer.token() == 16) {
            while (true) {
                String scanSymbol = this.lexer.scanSymbol(this.symbolTable);
                if (scanSymbol == null) {
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    } else if (this.lexer.token() == 16 && this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    }
                }
                FieldDeserializer fieldDeserializer = javaBeanDeserializer != null ? javaBeanDeserializer.getFieldDeserializer(scanSymbol) : null;
                if (fieldDeserializer != null) {
                    Class<?> cls2 = fieldDeserializer.fieldInfo.fieldClass;
                    Type type = fieldDeserializer.fieldInfo.fieldType;
                    if (cls2 == Integer.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        obj2 = IntegerCodec.instance.deserialze(this, type, (Object) null);
                    } else if (cls2 == String.class) {
                        this.lexer.nextTokenWithColon(4);
                        obj2 = StringCodec.deserialze(this);
                    } else if (cls2 == Long.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        obj2 = LongCodec.instance.deserialze(this, type, (Object) null);
                    } else {
                        ObjectDeserializer deserializer2 = this.config.getDeserializer(cls2, type);
                        this.lexer.nextTokenWithColon(deserializer2.getFastMatchToken());
                        obj2 = deserializer2.deserialze(this, type, (Object) null);
                    }
                    fieldDeserializer.setValue(obj, obj2);
                    if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    }
                } else if (this.lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    this.lexer.nextTokenWithColon();
                    parse();
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken();
                        return;
                    }
                } else {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + scanSymbol);
                }
            }
        } else {
            throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
        }
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length == 1) {
            Type type2 = actualTypeArguments[0];
            if (type2 instanceof Class) {
                ArrayList arrayList = new ArrayList();
                parseArray((Class<?>) (Class) type2, (Collection) arrayList);
                return arrayList;
            } else if (type2 instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type2;
                Type type3 = wildcardType.getUpperBounds()[0];
                if (!Object.class.equals(type3)) {
                    ArrayList arrayList2 = new ArrayList();
                    parseArray((Class<?>) (Class) type3, (Collection) arrayList2);
                    return arrayList2;
                } else if (wildcardType.getLowerBounds().length == 0) {
                    return parse();
                } else {
                    throw new JSONException("not support type : " + type);
                }
            } else {
                if (type2 instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) type2;
                    Type[] bounds = typeVariable.getBounds();
                    if (bounds.length == 1) {
                        Type type4 = bounds[0];
                        if (type4 instanceof Class) {
                            ArrayList arrayList3 = new ArrayList();
                            parseArray((Class<?>) (Class) type4, (Collection) arrayList3);
                            return arrayList3;
                        }
                    } else {
                        throw new JSONException("not support : " + typeVariable);
                    }
                }
                if (type2 instanceof ParameterizedType) {
                    ArrayList arrayList4 = new ArrayList();
                    parseArray((Type) (ParameterizedType) type2, (Collection) arrayList4);
                    return arrayList4;
                }
                throw new JSONException("TODO : " + type);
            }
        } else {
            throw new JSONException("not support type " + type);
        }
    }

    public void acceptType(String str) {
        JSONLexer jSONLexer = this.lexer;
        jSONLexer.nextTokenWithColon();
        if (jSONLexer.token() != 4) {
            throw new JSONException("type not match error");
        } else if (str.equals(jSONLexer.stringVal())) {
            jSONLexer.nextToken();
            if (jSONLexer.token() == 16) {
                jSONLexer.nextToken();
            }
        } else {
            throw new JSONException("type not match error");
        }
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int i) {
        this.resolveStatus = i;
    }

    public Object getObject(String str) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (str.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public void checkListResolve(Collection collection) {
        if (this.resolveStatus != 1) {
            return;
        }
        if (collection instanceof List) {
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = new ResolveFieldDeserializer(this, (List) collection, collection.size() - 1);
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
            return;
        }
        ResolveTask lastResolveTask2 = getLastResolveTask();
        lastResolveTask2.fieldDeserializer = new ResolveFieldDeserializer(collection);
        lastResolveTask2.ownerContext = this.context;
        setResolveStatus(0);
    }

    public void checkMapResolve(Map map, Object obj) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer resolveFieldDeserializer = new ResolveFieldDeserializer(map, obj);
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = resolveFieldDeserializer;
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map map) {
        return parseObject(map, (Object) null);
    }

    public JSONObject parseObject() {
        return (JSONObject) parseObject((Map) new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, (Object) null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v0, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v1, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v3, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v5, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v6, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v13, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v14, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v15, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v16, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v17, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v18, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v20, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v21, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(java.util.Collection r10, java.lang.Object r11) {
        /*
            r9 = this;
            com.alibaba.fastjson.parser.JSONLexer r0 = r9.lexer
            int r1 = r0.token()
            r2 = 21
            if (r1 == r2) goto L_0x0012
            int r1 = r0.token()
            r2 = 22
            if (r1 != r2) goto L_0x0015
        L_0x0012:
            r0.nextToken()
        L_0x0015:
            int r1 = r0.token()
            r2 = 14
            if (r1 != r2) goto L_0x011a
            r1 = 4
            r0.nextToken(r1)
            com.alibaba.fastjson.parser.ParseContext r3 = r9.context
            r9.setContext(r10, r11)
            r11 = 0
            r4 = 0
        L_0x0028:
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x0115 }
            boolean r5 = r0.isEnabled(r5)     // Catch:{ all -> 0x0115 }
            r6 = 16
            if (r5 == 0) goto L_0x003c
        L_0x0032:
            int r5 = r0.token()     // Catch:{ all -> 0x0115 }
            if (r5 != r6) goto L_0x003c
            r0.nextToken()     // Catch:{ all -> 0x0115 }
            goto L_0x0032
        L_0x003c:
            int r5 = r0.token()     // Catch:{ all -> 0x0115 }
            r7 = 2
            r8 = 0
            if (r5 == r7) goto L_0x00fb
            r7 = 3
            if (r5 == r7) goto L_0x00e4
            if (r5 == r1) goto L_0x00bd
            r7 = 6
            if (r5 == r7) goto L_0x00b7
            r7 = 7
            if (r5 == r7) goto L_0x00b1
            r7 = 8
            if (r5 == r7) goto L_0x00ad
            r7 = 12
            if (r5 == r7) goto L_0x0099
            r7 = 20
            if (r5 == r7) goto L_0x0091
            r7 = 23
            if (r5 == r7) goto L_0x008c
            if (r5 == r2) goto L_0x0072
            r7 = 15
            if (r5 == r7) goto L_0x006b
            java.lang.Object r8 = r9.parse()     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x006b:
            r0.nextToken(r6)     // Catch:{ all -> 0x0115 }
            r9.setContext(r3)
            return
        L_0x0072:
            com.alibaba.fastjson.JSONArray r8 = new com.alibaba.fastjson.JSONArray     // Catch:{ all -> 0x0115 }
            r8.<init>()     // Catch:{ all -> 0x0115 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x0115 }
            r9.parseArray((java.util.Collection) r8, (java.lang.Object) r5)     // Catch:{ all -> 0x0115 }
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.UseObjectArray     // Catch:{ all -> 0x0115 }
            boolean r5 = r0.isEnabled(r5)     // Catch:{ all -> 0x0115 }
            if (r5 == 0) goto L_0x0102
            java.lang.Object[] r8 = r8.toArray()     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x008c:
            r0.nextToken(r1)     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x0091:
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0115 }
            java.lang.String r11 = "unclosed jsonArray"
            r10.<init>(r11)     // Catch:{ all -> 0x0115 }
            throw r10     // Catch:{ all -> 0x0115 }
        L_0x0099:
            com.alibaba.fastjson.JSONObject r5 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x0115 }
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.OrderedField     // Catch:{ all -> 0x0115 }
            boolean r7 = r0.isEnabled(r7)     // Catch:{ all -> 0x0115 }
            r5.<init>((boolean) r7)     // Catch:{ all -> 0x0115 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x0115 }
            java.lang.Object r8 = r9.parseObject((java.util.Map) r5, (java.lang.Object) r7)     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x00ad:
            r0.nextToken(r1)     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x00b1:
            java.lang.Boolean r8 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0115 }
            r0.nextToken(r6)     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x00b7:
            java.lang.Boolean r8 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x0115 }
            r0.nextToken(r6)     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x00bd:
            java.lang.String r8 = r0.stringVal()     // Catch:{ all -> 0x0115 }
            r0.nextToken(r6)     // Catch:{ all -> 0x0115 }
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat     // Catch:{ all -> 0x0115 }
            boolean r5 = r0.isEnabled(r5)     // Catch:{ all -> 0x0115 }
            if (r5 == 0) goto L_0x0102
            com.alibaba.fastjson.parser.JSONScanner r5 = new com.alibaba.fastjson.parser.JSONScanner     // Catch:{ all -> 0x0115 }
            r5.<init>(r8)     // Catch:{ all -> 0x0115 }
            boolean r7 = r5.scanISO8601DateIfMatch()     // Catch:{ all -> 0x0115 }
            if (r7 == 0) goto L_0x00e0
            java.util.Calendar r7 = r5.getCalendar()     // Catch:{ all -> 0x0115 }
            java.util.Date r7 = r7.getTime()     // Catch:{ all -> 0x0115 }
            r8 = r7
        L_0x00e0:
            r5.close()     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x00e4:
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.UseBigDecimal     // Catch:{ all -> 0x0115 }
            boolean r5 = r0.isEnabled(r5)     // Catch:{ all -> 0x0115 }
            if (r5 == 0) goto L_0x00f2
            r5 = 1
            java.lang.Number r5 = r0.decimalValue(r5)     // Catch:{ all -> 0x0115 }
            goto L_0x00f6
        L_0x00f2:
            java.lang.Number r5 = r0.decimalValue(r11)     // Catch:{ all -> 0x0115 }
        L_0x00f6:
            r8 = r5
            r0.nextToken(r6)     // Catch:{ all -> 0x0115 }
            goto L_0x0102
        L_0x00fb:
            java.lang.Number r8 = r0.integerValue()     // Catch:{ all -> 0x0115 }
            r0.nextToken(r6)     // Catch:{ all -> 0x0115 }
        L_0x0102:
            r10.add(r8)     // Catch:{ all -> 0x0115 }
            r9.checkListResolve(r10)     // Catch:{ all -> 0x0115 }
            int r5 = r0.token()     // Catch:{ all -> 0x0115 }
            if (r5 != r6) goto L_0x0111
            r0.nextToken(r1)     // Catch:{ all -> 0x0115 }
        L_0x0111:
            int r4 = r4 + 1
            goto L_0x0028
        L_0x0115:
            r10 = move-exception
            r9.setContext(r3)
            throw r10
        L_0x011a:
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r1 = "syntax error, expect [, actual "
            r11.append(r1)
            int r1 = r0.token()
            java.lang.String r1 = com.alibaba.fastjson.parser.JSONToken.name(r1)
            r11.append(r1)
            java.lang.String r1 = ", pos "
            r11.append(r1)
            int r0 = r0.pos()
            r11.append(r0)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseArray(java.util.Collection, java.lang.Object):void");
    }

    public ParseContext getContext() {
        return this.context;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    public ResolveTask getLastResolveTask() {
        List<ResolveTask> list = this.resolveTaskList;
        return list.get(list.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver2) {
        this.fieldTypeResolver = fieldTypeResolver2;
    }

    public void setContext(ParseContext parseContext) {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = parseContext;
        }
    }

    public void popContext() {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = this.context.parent;
            ParseContext[] parseContextArr = this.contextArray;
            int i = this.contextArrayIndex;
            parseContextArr[i - 1] = null;
            this.contextArrayIndex = i - 1;
        }
    }

    public ParseContext setContext(Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, obj, obj2);
    }

    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parseContext, obj, obj2);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext parseContext) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        ParseContext[] parseContextArr = this.contextArray;
        if (parseContextArr == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= parseContextArr.length) {
            ParseContext[] parseContextArr2 = new ParseContext[((parseContextArr.length * 3) / 2)];
            System.arraycopy(parseContextArr, 0, parseContextArr2, 0, parseContextArr.length);
            this.contextArray = parseContextArr2;
        }
        this.contextArray[i] = parseContext;
    }

    public Object parse() {
        return parse((Object) null);
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse((Object) null);
        }
        String stringVal = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return stringVal;
    }

    public Object parse(Object obj) {
        JSONLexer jSONLexer = this.lexer;
        int i = jSONLexer.token();
        if (i == 2) {
            Number integerValue = jSONLexer.integerValue();
            jSONLexer.nextToken();
            return integerValue;
        } else if (i == 3) {
            Number decimalValue = jSONLexer.decimalValue(jSONLexer.isEnabled(Feature.UseBigDecimal));
            jSONLexer.nextToken();
            return decimalValue;
        } else if (i == 4) {
            String stringVal = jSONLexer.stringVal();
            jSONLexer.nextToken(16);
            if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                JSONScanner jSONScanner = new JSONScanner(stringVal);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        return jSONScanner.getCalendar().getTime();
                    }
                    jSONScanner.close();
                } finally {
                    jSONScanner.close();
                }
            }
            return stringVal;
        } else if (i == 12) {
            return parseObject((Map) new JSONObject(jSONLexer.isEnabled(Feature.OrderedField)), obj);
        } else {
            if (i != 14) {
                switch (i) {
                    case 6:
                        jSONLexer.nextToken();
                        return Boolean.TRUE;
                    case 7:
                        jSONLexer.nextToken();
                        return Boolean.FALSE;
                    case 8:
                        jSONLexer.nextToken();
                        return null;
                    case 9:
                        jSONLexer.nextToken(18);
                        if (jSONLexer.token() == 18) {
                            jSONLexer.nextToken(10);
                            accept(10);
                            long longValue = jSONLexer.integerValue().longValue();
                            accept(2);
                            accept(11);
                            return new Date(longValue);
                        }
                        throw new JSONException("syntax error");
                    default:
                        switch (i) {
                            case 20:
                                if (jSONLexer.isBlankInput()) {
                                    return null;
                                }
                                throw new JSONException("unterminated json string, pos " + jSONLexer.getBufferPosition());
                            case 21:
                                jSONLexer.nextToken();
                                HashSet hashSet = new HashSet();
                                parseArray((Collection) hashSet, obj);
                                return hashSet;
                            case 22:
                                jSONLexer.nextToken();
                                TreeSet treeSet = new TreeSet();
                                parseArray((Collection) treeSet, obj);
                                return treeSet;
                            case 23:
                                jSONLexer.nextToken();
                                return null;
                            default:
                                throw new JSONException("syntax error, pos " + jSONLexer.getBufferPosition());
                        }
                }
            } else {
                JSONArray jSONArray = new JSONArray();
                parseArray((Collection) jSONArray, obj);
                return jSONLexer.isEnabled(Feature.UseObjectArray) ? jSONArray.toArray() : jSONArray;
            }
        }
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int i) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(jSONLexer.token()));
    }

    public final void accept(int i, int i2) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken(i2);
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(jSONLexer.token()));
    }

    public void close() {
        JSONLexer jSONLexer = this.lexer;
        try {
            if (jSONLexer.isEnabled(Feature.AutoCloseSource)) {
                if (jSONLexer.token() != 20) {
                    throw new JSONException("not close json text, token : " + JSONToken.name(jSONLexer.token()));
                }
            }
        } finally {
            jSONLexer.close();
        }
    }

    public void handleResovleTask(Object obj) {
        Object obj2;
        List<ResolveTask> list = this.resolveTaskList;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ResolveTask resolveTask = this.resolveTaskList.get(i);
                String access$000 = resolveTask.referenceValue;
                Object obj3 = null;
                if (resolveTask.ownerContext != null) {
                    obj3 = resolveTask.ownerContext.object;
                }
                if (access$000.startsWith("$")) {
                    obj2 = getObject(access$000);
                } else {
                    obj2 = resolveTask.context.object;
                }
                FieldDeserializer fieldDeserializer = resolveTask.fieldDeserializer;
                if (fieldDeserializer != null) {
                    fieldDeserializer.setValue(obj3, obj2);
                }
            }
        }
    }

    public static class ResolveTask {
        /* access modifiers changed from: private */
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        /* access modifiers changed from: private */
        public final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }

        public ParseContext getContext() {
            return this.context;
        }

        public String getReferenceValue() {
            return this.referenceValue;
        }

        public FieldDeserializer getFieldDeserializer() {
            return this.fieldDeserializer;
        }

        public void setFieldDeserializer(FieldDeserializer fieldDeserializer2) {
            this.fieldDeserializer = fieldDeserializer2;
        }

        public ParseContext getOwnerContext() {
            return this.ownerContext;
        }

        public void setOwnerContext(ParseContext parseContext) {
            this.ownerContext = parseContext;
        }
    }

    public void parseExtra(Object obj, String str) {
        Object obj2;
        this.lexer.nextTokenWithColon();
        List<ExtraTypeProvider> list = this.extraTypeProviders;
        Type type = null;
        if (list != null) {
            for (ExtraTypeProvider extraType : list) {
                type = extraType.getExtraType(obj, str);
            }
        }
        if (type == null) {
            obj2 = parse();
        } else {
            obj2 = parseObject(type);
        }
        if (obj instanceof ExtraProcessable) {
            ((ExtraProcessable) obj).processExtra(str, obj2);
            return;
        }
        List<ExtraProcessor> list2 = this.extraProcessors;
        if (list2 != null) {
            for (ExtraProcessor processExtra : list2) {
                processExtra.processExtra(obj, str, obj2);
            }
        }
    }
}
