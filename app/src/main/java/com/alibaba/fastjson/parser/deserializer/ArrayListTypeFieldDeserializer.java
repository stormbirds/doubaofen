package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {
    private ObjectDeserializer deserializer;
    private int itemFastMatchToken;
    private final Type itemType;

    public int getFastMatchToken() {
        return 14;
    }

    public ArrayListTypeFieldDeserializer(ParserConfig parserConfig, Class<?> cls, FieldInfo fieldInfo) {
        super(cls, fieldInfo);
        if (fieldInfo.fieldType instanceof ParameterizedType) {
            this.itemType = ((ParameterizedType) fieldInfo.fieldType).getActualTypeArguments()[0];
        } else {
            this.itemType = Object.class;
        }
    }

    public void parseField(DefaultJSONParser defaultJSONParser, Object obj, Type type, Map<String, Object> map) {
        if (defaultJSONParser.lexer.token() == 8) {
            setValue(obj, (String) null);
            return;
        }
        ArrayList arrayList = new ArrayList();
        ParseContext context = defaultJSONParser.getContext();
        defaultJSONParser.setContext(context, obj, this.fieldInfo.name);
        parseArray(defaultJSONParser, type, arrayList);
        defaultJSONParser.setContext(context);
        if (obj == null) {
            map.put(this.fieldInfo.name, arrayList);
        } else {
            setValue(obj, (Object) arrayList);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(com.alibaba.fastjson.parser.DefaultJSONParser r12, java.lang.reflect.Type r13, java.util.Collection r14) {
        /*
            r11 = this;
            java.lang.reflect.Type r0 = r11.itemType
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r11.deserializer
            boolean r2 = r0 instanceof java.lang.reflect.TypeVariable
            r3 = 0
            if (r2 == 0) goto L_0x005e
            boolean r2 = r13 instanceof java.lang.reflect.ParameterizedType
            if (r2 == 0) goto L_0x005e
            r2 = r0
            java.lang.reflect.TypeVariable r2 = (java.lang.reflect.TypeVariable) r2
            r4 = r13
            java.lang.reflect.ParameterizedType r4 = (java.lang.reflect.ParameterizedType) r4
            r5 = 0
            java.lang.reflect.Type r6 = r4.getRawType()
            boolean r6 = r6 instanceof java.lang.Class
            if (r6 == 0) goto L_0x0022
            java.lang.reflect.Type r5 = r4.getRawType()
            java.lang.Class r5 = (java.lang.Class) r5
        L_0x0022:
            r6 = -1
            if (r5 == 0) goto L_0x0045
            java.lang.reflect.TypeVariable[] r7 = r5.getTypeParameters()
            int r7 = r7.length
            r8 = 0
        L_0x002b:
            if (r8 >= r7) goto L_0x0045
            java.lang.reflect.TypeVariable[] r9 = r5.getTypeParameters()
            r9 = r9[r8]
            java.lang.String r9 = r9.getName()
            java.lang.String r10 = r2.getName()
            boolean r9 = r9.equals(r10)
            if (r9 == 0) goto L_0x0042
            goto L_0x0046
        L_0x0042:
            int r8 = r8 + 1
            goto L_0x002b
        L_0x0045:
            r8 = -1
        L_0x0046:
            if (r8 == r6) goto L_0x005e
            java.lang.reflect.Type[] r0 = r4.getActualTypeArguments()
            r0 = r0[r8]
            java.lang.reflect.Type r2 = r11.itemType
            boolean r2 = r0.equals(r2)
            if (r2 != 0) goto L_0x005e
            com.alibaba.fastjson.parser.ParserConfig r1 = r12.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r1.getDeserializer((java.lang.reflect.Type) r0)
        L_0x005e:
            com.alibaba.fastjson.parser.JSONLexer r2 = r12.lexer
            int r4 = r2.token()
            r5 = 14
            if (r4 == r5) goto L_0x009d
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r14 = "exepct '[', but "
            r12.append(r14)
            int r14 = r2.token()
            java.lang.String r14 = com.alibaba.fastjson.parser.JSONToken.name(r14)
            r12.append(r14)
            java.lang.String r12 = r12.toString()
            if (r13 == 0) goto L_0x0097
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r12)
            java.lang.String r12 = ", type : "
            r14.append(r12)
            r14.append(r13)
            java.lang.String r12 = r14.toString()
        L_0x0097:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r12)
            throw r13
        L_0x009d:
            if (r1 != 0) goto L_0x00b1
            com.alibaba.fastjson.parser.ParserConfig r13 = r12.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r13.getDeserializer((java.lang.reflect.Type) r0)
            r11.deserializer = r1
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r13 = r11.deserializer
            int r13 = r13.getFastMatchToken()
            r11.itemFastMatchToken = r13
        L_0x00b1:
            int r13 = r11.itemFastMatchToken
            r2.nextToken(r13)
        L_0x00b6:
            com.alibaba.fastjson.parser.Feature r13 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas
            boolean r13 = r2.isEnabled(r13)
            r4 = 16
            if (r13 == 0) goto L_0x00ca
        L_0x00c0:
            int r13 = r2.token()
            if (r13 != r4) goto L_0x00ca
            r2.nextToken()
            goto L_0x00c0
        L_0x00ca:
            int r13 = r2.token()
            r5 = 15
            if (r13 != r5) goto L_0x00d6
            r2.nextToken(r4)
            return
        L_0x00d6:
            java.lang.Integer r13 = java.lang.Integer.valueOf(r3)
            java.lang.Object r13 = r1.deserialze(r12, r0, r13)
            r14.add(r13)
            r12.checkListResolve(r14)
            int r13 = r2.token()
            if (r13 != r4) goto L_0x00ef
            int r13 = r11.itemFastMatchToken
            r2.nextToken(r13)
        L_0x00ef:
            int r3 = r3 + 1
            goto L_0x00b6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer.parseArray(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.util.Collection):void");
    }
}
