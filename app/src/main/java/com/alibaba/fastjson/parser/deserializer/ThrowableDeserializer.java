package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import java.lang.reflect.Constructor;

public class ThrowableDeserializer extends JavaBeanDeserializer {
    public int getFastMatchToken() {
        return 12;
    }

    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        if (java.lang.Throwable.class.isAssignableFrom(r14) != false) goto L_0x0037;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r13, java.lang.reflect.Type r14, java.lang.Object r15) {
        /*
            r12 = this;
            com.alibaba.fastjson.parser.JSONLexer r15 = r13.lexer
            int r0 = r15.token()
            r1 = 8
            r2 = 0
            if (r0 != r1) goto L_0x000f
            r15.nextToken()
            return r2
        L_0x000f:
            int r0 = r13.getResolveStatus()
            r3 = 2
            java.lang.String r4 = "syntax error"
            if (r0 != r3) goto L_0x001d
            r0 = 0
            r13.setResolveStatus(r0)
            goto L_0x0025
        L_0x001d:
            int r0 = r15.token()
            r3 = 12
            if (r0 != r3) goto L_0x010a
        L_0x0025:
            if (r14 == 0) goto L_0x0036
            boolean r0 = r14 instanceof java.lang.Class
            if (r0 == 0) goto L_0x0036
            java.lang.Class r14 = (java.lang.Class) r14
            java.lang.Class<java.lang.Throwable> r0 = java.lang.Throwable.class
            boolean r0 = r0.isAssignableFrom(r14)
            if (r0 == 0) goto L_0x0036
            goto L_0x0037
        L_0x0036:
            r14 = r2
        L_0x0037:
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r3 = r14
            r14 = r2
            r5 = r14
            r6 = r5
        L_0x0040:
            com.alibaba.fastjson.parser.SymbolTable r7 = r13.getSymbolTable()
            java.lang.String r7 = r15.scanSymbol(r7)
            r8 = 13
            r9 = 16
            if (r7 != 0) goto L_0x0068
            int r10 = r15.token()
            if (r10 != r8) goto L_0x0059
            r15.nextToken(r9)
            goto L_0x00e8
        L_0x0059:
            int r10 = r15.token()
            if (r10 != r9) goto L_0x0068
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas
            boolean r10 = r15.isEnabled(r10)
            if (r10 == 0) goto L_0x0068
            goto L_0x0040
        L_0x0068:
            r10 = 4
            r15.nextTokenWithColon(r10)
            java.lang.String r11 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY
            boolean r11 = r11.equals(r7)
            if (r11 == 0) goto L_0x0094
            int r3 = r15.token()
            if (r3 != r10) goto L_0x008e
            java.lang.String r3 = r15.stringVal()
            com.alibaba.fastjson.parser.ParserConfig r7 = r13.getConfig()
            java.lang.ClassLoader r7 = r7.getDefaultClassLoader()
            java.lang.Class r3 = com.alibaba.fastjson.util.TypeUtils.loadClass(r3, r7)
            r15.nextToken(r9)
            goto L_0x00df
        L_0x008e:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r4)
            throw r13
        L_0x0094:
            java.lang.String r11 = "message"
            boolean r11 = r11.equals(r7)
            if (r11 == 0) goto L_0x00b8
            int r5 = r15.token()
            if (r5 != r1) goto L_0x00a4
            r5 = r2
            goto L_0x00ae
        L_0x00a4:
            int r5 = r15.token()
            if (r5 != r10) goto L_0x00b2
            java.lang.String r5 = r15.stringVal()
        L_0x00ae:
            r15.nextToken()
            goto L_0x00df
        L_0x00b2:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r4)
            throw r13
        L_0x00b8:
            java.lang.String r10 = "cause"
            boolean r11 = r10.equals(r7)
            if (r11 == 0) goto L_0x00c7
            java.lang.Object r14 = r12.deserialze(r13, r2, r10)
            java.lang.Throwable r14 = (java.lang.Throwable) r14
            goto L_0x00df
        L_0x00c7:
            java.lang.String r10 = "stackTrace"
            boolean r10 = r10.equals(r7)
            if (r10 == 0) goto L_0x00d8
            java.lang.Class<java.lang.StackTraceElement[]> r6 = java.lang.StackTraceElement[].class
            java.lang.Object r6 = r13.parseObject(r6)
            java.lang.StackTraceElement[] r6 = (java.lang.StackTraceElement[]) r6
            goto L_0x00df
        L_0x00d8:
            java.lang.Object r10 = r13.parse()
            r0.put(r7, r10)
        L_0x00df:
            int r7 = r15.token()
            if (r7 != r8) goto L_0x0040
            r15.nextToken(r9)
        L_0x00e8:
            if (r3 != 0) goto L_0x00f0
            java.lang.Exception r13 = new java.lang.Exception
            r13.<init>(r5, r14)
            goto L_0x00fb
        L_0x00f0:
            java.lang.Throwable r13 = r12.createException(r5, r14, r3)     // Catch:{ Exception -> 0x0101 }
            if (r13 != 0) goto L_0x00fb
            java.lang.Exception r13 = new java.lang.Exception     // Catch:{ Exception -> 0x0101 }
            r13.<init>(r5, r14)     // Catch:{ Exception -> 0x0101 }
        L_0x00fb:
            if (r6 == 0) goto L_0x0100
            r13.setStackTrace(r6)
        L_0x0100:
            return r13
        L_0x0101:
            r13 = move-exception
            com.alibaba.fastjson.JSONException r14 = new com.alibaba.fastjson.JSONException
            java.lang.String r15 = "create instance error"
            r14.<init>(r15, r13)
            throw r14
        L_0x010a:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r4)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    private Throwable createException(String str, Throwable th, Class<?> cls) throws Exception {
        Constructor constructor = null;
        Constructor constructor2 = null;
        Constructor constructor3 = null;
        for (Constructor constructor4 : cls.getConstructors()) {
            Class<Throwable>[] parameterTypes = constructor4.getParameterTypes();
            if (parameterTypes.length == 0) {
                constructor3 = constructor4;
            } else if (parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                constructor2 = constructor4;
            } else if (parameterTypes.length == 2 && parameterTypes[0] == String.class && parameterTypes[1] == Throwable.class) {
                constructor = constructor4;
            }
        }
        if (constructor != null) {
            return (Throwable) constructor.newInstance(new Object[]{str, th});
        } else if (constructor2 != null) {
            return (Throwable) constructor2.newInstance(new Object[]{str});
        } else if (constructor3 != null) {
            return (Throwable) constructor3.newInstance(new Object[0]);
        } else {
            return null;
        }
    }
}
