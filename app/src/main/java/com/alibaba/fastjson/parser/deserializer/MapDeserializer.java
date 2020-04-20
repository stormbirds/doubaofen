package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();

    public int getFastMatchToken() {
        return 12;
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        if (type == JSONObject.class && defaultJSONParser.getFieldTypeResolver() == null) {
            return defaultJSONParser.parseObject();
        }
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken(16);
            return null;
        }
        Map<Object, Object> createMap = createMap(type);
        ParseContext context = defaultJSONParser.getContext();
        try {
            defaultJSONParser.setContext(context, createMap, obj);
            return deserialze(defaultJSONParser, type, obj, createMap);
        } finally {
            defaultJSONParser.setContext(context);
        }
    }

    /* access modifiers changed from: protected */
    public Object deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, Map map) {
        if (!(type instanceof ParameterizedType)) {
            return defaultJSONParser.parseObject(map, obj);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type type2 = parameterizedType.getActualTypeArguments()[0];
        Type type3 = parameterizedType.getActualTypeArguments()[1];
        if (String.class == type2) {
            return parseMap(defaultJSONParser, map, type3, obj);
        }
        return parseMap(defaultJSONParser, map, type2, type3, obj);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r11 = r10.getConfig().getDeserializer((java.lang.reflect.Type) r3);
        r0.nextToken(16);
        r10.setResolveStatus(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0121, code lost:
        if (r1 == null) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0125, code lost:
        if ((r13 instanceof java.lang.Integer) != false) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0127, code lost:
        r10.popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x012a, code lost:
        r11 = (java.util.Map) r11.deserialze(r10, r3, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0130, code lost:
        r10.setContext(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0133, code lost:
        return r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r10, java.util.Map<java.lang.String, java.lang.Object> r11, java.lang.reflect.Type r12, java.lang.Object r13) {
        /*
            com.alibaba.fastjson.parser.JSONLexer r0 = r10.lexer
            int r1 = r0.token()
            r2 = 12
            if (r1 != r2) goto L_0x01a3
            com.alibaba.fastjson.parser.ParseContext r1 = r10.getContext()
            r2 = 0
        L_0x000f:
            r0.skipWhitespace()     // Catch:{ all -> 0x019e }
            char r3 = r0.getCurrent()     // Catch:{ all -> 0x019e }
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x019e }
            boolean r4 = r0.isEnabled(r4)     // Catch:{ all -> 0x019e }
            if (r4 == 0) goto L_0x002d
        L_0x001e:
            r4 = 44
            if (r3 != r4) goto L_0x002d
            r0.next()     // Catch:{ all -> 0x019e }
            r0.skipWhitespace()     // Catch:{ all -> 0x019e }
            char r3 = r0.getCurrent()     // Catch:{ all -> 0x019e }
            goto L_0x001e
        L_0x002d:
            java.lang.String r4 = "expect ':' at "
            r5 = 58
            r6 = 34
            r7 = 16
            if (r3 != r6) goto L_0x0063
            com.alibaba.fastjson.parser.SymbolTable r3 = r10.getSymbolTable()     // Catch:{ all -> 0x019e }
            java.lang.String r3 = r0.scanSymbol(r3, r6)     // Catch:{ all -> 0x019e }
            r0.skipWhitespace()     // Catch:{ all -> 0x019e }
            char r8 = r0.getCurrent()     // Catch:{ all -> 0x019e }
            if (r8 != r5) goto L_0x004a
            goto L_0x00cc
        L_0x004a:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x019e }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x019e }
            r12.<init>()     // Catch:{ all -> 0x019e }
            r12.append(r4)     // Catch:{ all -> 0x019e }
            int r13 = r0.pos()     // Catch:{ all -> 0x019e }
            r12.append(r13)     // Catch:{ all -> 0x019e }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x019e }
            r11.<init>(r12)     // Catch:{ all -> 0x019e }
            throw r11     // Catch:{ all -> 0x019e }
        L_0x0063:
            r8 = 125(0x7d, float:1.75E-43)
            if (r3 != r8) goto L_0x0074
            r0.next()     // Catch:{ all -> 0x019e }
            r0.resetStringPosition()     // Catch:{ all -> 0x019e }
            r0.nextToken(r7)     // Catch:{ all -> 0x019e }
            r10.setContext(r1)
            return r11
        L_0x0074:
            java.lang.String r8 = "syntax error"
            r9 = 39
            if (r3 != r9) goto L_0x00b3
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x019e }
            boolean r3 = r0.isEnabled(r3)     // Catch:{ all -> 0x019e }
            if (r3 == 0) goto L_0x00ad
            com.alibaba.fastjson.parser.SymbolTable r3 = r10.getSymbolTable()     // Catch:{ all -> 0x019e }
            java.lang.String r3 = r0.scanSymbol(r3, r9)     // Catch:{ all -> 0x019e }
            r0.skipWhitespace()     // Catch:{ all -> 0x019e }
            char r8 = r0.getCurrent()     // Catch:{ all -> 0x019e }
            if (r8 != r5) goto L_0x0094
            goto L_0x00cc
        L_0x0094:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x019e }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x019e }
            r12.<init>()     // Catch:{ all -> 0x019e }
            r12.append(r4)     // Catch:{ all -> 0x019e }
            int r13 = r0.pos()     // Catch:{ all -> 0x019e }
            r12.append(r13)     // Catch:{ all -> 0x019e }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x019e }
            r11.<init>(r12)     // Catch:{ all -> 0x019e }
            throw r11     // Catch:{ all -> 0x019e }
        L_0x00ad:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x019e }
            r11.<init>(r8)     // Catch:{ all -> 0x019e }
            throw r11     // Catch:{ all -> 0x019e }
        L_0x00b3:
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x019e }
            boolean r3 = r0.isEnabled(r3)     // Catch:{ all -> 0x019e }
            if (r3 == 0) goto L_0x0198
            com.alibaba.fastjson.parser.SymbolTable r3 = r10.getSymbolTable()     // Catch:{ all -> 0x019e }
            java.lang.String r3 = r0.scanSymbolUnQuoted(r3)     // Catch:{ all -> 0x019e }
            r0.skipWhitespace()     // Catch:{ all -> 0x019e }
            char r8 = r0.getCurrent()     // Catch:{ all -> 0x019e }
            if (r8 != r5) goto L_0x0177
        L_0x00cc:
            r0.next()     // Catch:{ all -> 0x019e }
            r0.skipWhitespace()     // Catch:{ all -> 0x019e }
            r0.getCurrent()     // Catch:{ all -> 0x019e }
            r0.resetStringPosition()     // Catch:{ all -> 0x019e }
            java.lang.String r4 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x019e }
            r5 = 13
            if (r3 != r4) goto L_0x0134
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x019e }
            boolean r4 = r0.isEnabled(r4)     // Catch:{ all -> 0x019e }
            if (r4 != 0) goto L_0x0134
            com.alibaba.fastjson.parser.SymbolTable r3 = r10.getSymbolTable()     // Catch:{ all -> 0x019e }
            java.lang.String r3 = r0.scanSymbol(r3, r6)     // Catch:{ all -> 0x019e }
            com.alibaba.fastjson.parser.ParserConfig r4 = r10.getConfig()     // Catch:{ all -> 0x019e }
            java.lang.ClassLoader r4 = r4.getDefaultClassLoader()     // Catch:{ all -> 0x019e }
            java.lang.Class r3 = com.alibaba.fastjson.util.TypeUtils.loadClass(r3, r4)     // Catch:{ all -> 0x019e }
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            boolean r4 = r4.isAssignableFrom(r3)     // Catch:{ all -> 0x019e }
            if (r4 == 0) goto L_0x0112
            r0.nextToken(r7)     // Catch:{ all -> 0x019e }
            int r3 = r0.token()     // Catch:{ all -> 0x019e }
            if (r3 != r5) goto L_0x016f
            r0.nextToken(r7)     // Catch:{ all -> 0x019e }
            r10.setContext(r1)
            return r11
        L_0x0112:
            com.alibaba.fastjson.parser.ParserConfig r11 = r10.getConfig()     // Catch:{ all -> 0x019e }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r11 = r11.getDeserializer((java.lang.reflect.Type) r3)     // Catch:{ all -> 0x019e }
            r0.nextToken(r7)     // Catch:{ all -> 0x019e }
            r12 = 2
            r10.setResolveStatus(r12)     // Catch:{ all -> 0x019e }
            if (r1 == 0) goto L_0x012a
            boolean r12 = r13 instanceof java.lang.Integer     // Catch:{ all -> 0x019e }
            if (r12 != 0) goto L_0x012a
            r10.popContext()     // Catch:{ all -> 0x019e }
        L_0x012a:
            java.lang.Object r11 = r11.deserialze(r10, r3, r13)     // Catch:{ all -> 0x019e }
            java.util.Map r11 = (java.util.Map) r11     // Catch:{ all -> 0x019e }
            r10.setContext(r1)
            return r11
        L_0x0134:
            r0.nextToken()     // Catch:{ all -> 0x019e }
            if (r2 == 0) goto L_0x013c
            r10.setContext(r1)     // Catch:{ all -> 0x019e }
        L_0x013c:
            int r4 = r0.token()     // Catch:{ all -> 0x019e }
            r6 = 8
            if (r4 != r6) goto L_0x0149
            r4 = 0
            r0.nextToken()     // Catch:{ all -> 0x019e }
            goto L_0x014d
        L_0x0149:
            java.lang.Object r4 = r10.parseObject((java.lang.reflect.Type) r12, (java.lang.Object) r3)     // Catch:{ all -> 0x019e }
        L_0x014d:
            r11.put(r3, r4)     // Catch:{ all -> 0x019e }
            r10.checkMapResolve(r11, r3)     // Catch:{ all -> 0x019e }
            r10.setContext(r1, r4, r3)     // Catch:{ all -> 0x019e }
            r10.setContext(r1)     // Catch:{ all -> 0x019e }
            int r3 = r0.token()     // Catch:{ all -> 0x019e }
            r4 = 20
            if (r3 == r4) goto L_0x0173
            r4 = 15
            if (r3 != r4) goto L_0x0166
            goto L_0x0173
        L_0x0166:
            if (r3 != r5) goto L_0x016f
            r0.nextToken()     // Catch:{ all -> 0x019e }
            r10.setContext(r1)
            return r11
        L_0x016f:
            int r2 = r2 + 1
            goto L_0x000f
        L_0x0173:
            r10.setContext(r1)
            return r11
        L_0x0177:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x019e }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x019e }
            r12.<init>()     // Catch:{ all -> 0x019e }
            r12.append(r4)     // Catch:{ all -> 0x019e }
            int r13 = r0.pos()     // Catch:{ all -> 0x019e }
            r12.append(r13)     // Catch:{ all -> 0x019e }
            java.lang.String r13 = ", actual "
            r12.append(r13)     // Catch:{ all -> 0x019e }
            r12.append(r8)     // Catch:{ all -> 0x019e }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x019e }
            r11.<init>(r12)     // Catch:{ all -> 0x019e }
            throw r11     // Catch:{ all -> 0x019e }
        L_0x0198:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x019e }
            r11.<init>(r8)     // Catch:{ all -> 0x019e }
            throw r11     // Catch:{ all -> 0x019e }
        L_0x019e:
            r11 = move-exception
            r10.setContext(r1)
            throw r11
        L_0x01a3:
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "syntax error, expect {, actual "
            r11.append(r12)
            int r12 = r0.token()
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object):java.util.Map");
    }

    public static Object parseMap(DefaultJSONParser defaultJSONParser, Map<Object, Object> map, Type type, Type type2, Object obj) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 12 || jSONLexer.token() == 16) {
            ObjectDeserializer deserializer = defaultJSONParser.getConfig().getDeserializer(type);
            ObjectDeserializer deserializer2 = defaultJSONParser.getConfig().getDeserializer(type2);
            jSONLexer.nextToken(deserializer.getFastMatchToken());
            ParseContext context = defaultJSONParser.getContext();
            while (jSONLexer.token() != 13) {
                try {
                    Object obj2 = null;
                    if (jSONLexer.token() != 4 || !jSONLexer.isRef() || jSONLexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                        if (map.size() == 0 && jSONLexer.token() == 4 && JSON.DEFAULT_TYPE_KEY.equals(jSONLexer.stringVal()) && !jSONLexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                            jSONLexer.nextTokenWithColon(4);
                            jSONLexer.nextToken(16);
                            if (jSONLexer.token() == 13) {
                                jSONLexer.nextToken();
                                return map;
                            }
                            jSONLexer.nextToken(deserializer.getFastMatchToken());
                        }
                        Object deserialze = deserializer.deserialze(defaultJSONParser, type, (Object) null);
                        if (jSONLexer.token() == 17) {
                            jSONLexer.nextToken(deserializer2.getFastMatchToken());
                            Object deserialze2 = deserializer2.deserialze(defaultJSONParser, type2, deserialze);
                            defaultJSONParser.checkMapResolve(map, deserialze);
                            map.put(deserialze, deserialze2);
                            if (jSONLexer.token() == 16) {
                                jSONLexer.nextToken(deserializer.getFastMatchToken());
                            }
                        } else {
                            throw new JSONException("syntax error, expect :, actual " + jSONLexer.token());
                        }
                    } else {
                        jSONLexer.nextTokenWithColon(4);
                        if (jSONLexer.token() == 4) {
                            String stringVal = jSONLexer.stringVal();
                            if ("..".equals(stringVal)) {
                                obj2 = context.parent.object;
                            } else if ("$".equals(stringVal)) {
                                ParseContext parseContext = context;
                                while (parseContext.parent != null) {
                                    parseContext = parseContext.parent;
                                }
                                obj2 = parseContext.object;
                            } else {
                                defaultJSONParser.addResolveTask(new DefaultJSONParser.ResolveTask(context, stringVal));
                                defaultJSONParser.setResolveStatus(1);
                            }
                            jSONLexer.nextToken(13);
                            if (jSONLexer.token() == 13) {
                                jSONLexer.nextToken(16);
                                defaultJSONParser.setContext(context);
                                return obj2;
                            }
                            throw new JSONException("illegal ref");
                        }
                        throw new JSONException("illegal ref, " + JSONToken.name(jSONLexer.token()));
                    }
                } finally {
                    defaultJSONParser.setContext(context);
                }
            }
            jSONLexer.nextToken(16);
            defaultJSONParser.setContext(context);
            return map;
        }
        throw new JSONException("syntax error, expect {, actual " + jSONLexer.tokenName());
    }

    /* access modifiers changed from: protected */
    public Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type instanceof ParameterizedType) {
            return createMap(((ParameterizedType) type).getRawType());
        }
        Class cls = (Class) type;
        if (!cls.isInterface()) {
            try {
                return (Map) cls.newInstance();
            } catch (Exception e) {
                throw new JSONException("unsupport type " + type, e);
            }
        } else {
            throw new JSONException("unsupport type " + type);
        }
    }
}
