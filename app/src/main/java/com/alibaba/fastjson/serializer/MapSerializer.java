package com.alibaba.fastjson.serializer;

public class MapSerializer implements ObjectSerializer {
    public static MapSerializer instance = new MapSerializer();

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0074 A[Catch:{ all -> 0x01f4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r19, java.lang.Object r20, java.lang.Object r21, java.lang.reflect.Type r22, int r23) throws java.io.IOException {
        /*
            r18 = this;
            r7 = r19
            r0 = r20
            com.alibaba.fastjson.serializer.SerializeWriter r8 = r7.out
            if (r0 != 0) goto L_0x000c
            r8.writeNull()
            return
        L_0x000c:
            r9 = r0
            java.util.Map r9 = (java.util.Map) r9
            boolean r1 = r19.containsReference(r20)
            if (r1 == 0) goto L_0x0019
            r19.writeReference(r20)
            return
        L_0x0019:
            com.alibaba.fastjson.serializer.SerialContext r10 = r7.context
            r11 = 0
            r1 = r21
            r7.setContext(r10, r0, r1, r11)
            r1 = 123(0x7b, float:1.72E-43)
            r8.write((int) r1)     // Catch:{ all -> 0x01f4 }
            r19.incrementIndent()     // Catch:{ all -> 0x01f4 }
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName     // Catch:{ all -> 0x01f4 }
            boolean r1 = r8.isEnabled(r1)     // Catch:{ all -> 0x01f4 }
            r12 = 1
            if (r1 == 0) goto L_0x0061
            java.lang.Class r1 = r9.getClass()     // Catch:{ all -> 0x01f4 }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r2 = com.alibaba.fastjson.JSONObject.class
            if (r1 == r2) goto L_0x0042
            java.lang.Class<java.util.HashMap> r2 = java.util.HashMap.class
            if (r1 == r2) goto L_0x0042
            java.lang.Class<java.util.LinkedHashMap> r2 = java.util.LinkedHashMap.class
            if (r1 != r2) goto L_0x004c
        L_0x0042:
            java.lang.String r1 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x01f4 }
            boolean r1 = r9.containsKey(r1)     // Catch:{ all -> 0x01f4 }
            if (r1 == 0) goto L_0x004c
            r1 = 1
            goto L_0x004d
        L_0x004c:
            r1 = 0
        L_0x004d:
            if (r1 != 0) goto L_0x0061
            java.lang.String r1 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x01f4 }
            r8.writeFieldName(r1)     // Catch:{ all -> 0x01f4 }
            java.lang.Class r1 = r20.getClass()     // Catch:{ all -> 0x01f4 }
            java.lang.String r1 = r1.getName()     // Catch:{ all -> 0x01f4 }
            r8.writeString(r1)     // Catch:{ all -> 0x01f4 }
            r1 = 0
            goto L_0x0062
        L_0x0061:
            r1 = 1
        L_0x0062:
            java.util.Set r2 = r9.entrySet()     // Catch:{ all -> 0x01f4 }
            java.util.Iterator r13 = r2.iterator()     // Catch:{ all -> 0x01f4 }
            r14 = 0
            r15 = r14
            r16 = r15
        L_0x006e:
            boolean r2 = r13.hasNext()     // Catch:{ all -> 0x01f4 }
            if (r2 == 0) goto L_0x01d8
            java.lang.Object r2 = r13.next()     // Catch:{ all -> 0x01f4 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x01f4 }
            java.lang.Object r3 = r2.getValue()     // Catch:{ all -> 0x01f4 }
            java.lang.Object r2 = r2.getKey()     // Catch:{ all -> 0x01f4 }
            java.util.List<com.alibaba.fastjson.serializer.PropertyPreFilter> r4 = r7.propertyPreFilters     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x00b6
            int r4 = r4.size()     // Catch:{ all -> 0x01f4 }
            if (r4 <= 0) goto L_0x00b6
            if (r2 == 0) goto L_0x00ac
            boolean r4 = r2 instanceof java.lang.String     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x0093
            goto L_0x00ac
        L_0x0093:
            java.lang.Class r4 = r2.getClass()     // Catch:{ all -> 0x01f4 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x00a1
            boolean r4 = r2 instanceof java.lang.Number     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x00b6
        L_0x00a1:
            java.lang.String r4 = com.alibaba.fastjson.JSON.toJSONString(r2)     // Catch:{ all -> 0x01f4 }
            boolean r4 = r7.applyName(r0, r4)     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x00b6
            goto L_0x006e
        L_0x00ac:
            r4 = r2
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x01f4 }
            boolean r4 = r7.applyName(r0, r4)     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x00b6
            goto L_0x006e
        L_0x00b6:
            java.util.List<com.alibaba.fastjson.serializer.PropertyFilter> r4 = r7.propertyFilters     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x00ea
            int r4 = r4.size()     // Catch:{ all -> 0x01f4 }
            if (r4 <= 0) goto L_0x00ea
            if (r2 == 0) goto L_0x00e0
            boolean r4 = r2 instanceof java.lang.String     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x00c7
            goto L_0x00e0
        L_0x00c7:
            java.lang.Class r4 = r2.getClass()     // Catch:{ all -> 0x01f4 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x00d5
            boolean r4 = r2 instanceof java.lang.Number     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x00ea
        L_0x00d5:
            java.lang.String r4 = com.alibaba.fastjson.JSON.toJSONString(r2)     // Catch:{ all -> 0x01f4 }
            boolean r4 = r7.apply(r0, r4, r3)     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x00ea
            goto L_0x006e
        L_0x00e0:
            r4 = r2
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x01f4 }
            boolean r4 = r7.apply(r0, r4, r3)     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x00ea
            goto L_0x006e
        L_0x00ea:
            java.util.List<com.alibaba.fastjson.serializer.NameFilter> r4 = r7.nameFilters     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x0118
            int r4 = r4.size()     // Catch:{ all -> 0x01f4 }
            if (r4 <= 0) goto L_0x0118
            if (r2 == 0) goto L_0x0112
            boolean r4 = r2 instanceof java.lang.String     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x00fb
            goto L_0x0112
        L_0x00fb:
            java.lang.Class r4 = r2.getClass()     // Catch:{ all -> 0x01f4 }
            boolean r4 = r4.isPrimitive()     // Catch:{ all -> 0x01f4 }
            if (r4 != 0) goto L_0x0109
            boolean r4 = r2 instanceof java.lang.Number     // Catch:{ all -> 0x01f4 }
            if (r4 == 0) goto L_0x0118
        L_0x0109:
            java.lang.String r2 = com.alibaba.fastjson.JSON.toJSONString(r2)     // Catch:{ all -> 0x01f4 }
            java.lang.String r2 = r7.processKey(r0, r2, r3)     // Catch:{ all -> 0x01f4 }
            goto L_0x0118
        L_0x0112:
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x01f4 }
            java.lang.String r2 = r7.processKey(r0, r2, r3)     // Catch:{ all -> 0x01f4 }
        L_0x0118:
            r4 = r2
            java.util.List<com.alibaba.fastjson.serializer.ValueFilter> r2 = r7.valueFilters     // Catch:{ all -> 0x01f4 }
            java.util.List<com.alibaba.fastjson.serializer.ContextValueFilter> r5 = r7.contextValueFilters     // Catch:{ all -> 0x01f4 }
            if (r2 == 0) goto L_0x0125
            int r2 = r2.size()     // Catch:{ all -> 0x01f4 }
            if (r2 > 0) goto L_0x012d
        L_0x0125:
            if (r5 == 0) goto L_0x0153
            int r2 = r5.size()     // Catch:{ all -> 0x01f4 }
            if (r2 <= 0) goto L_0x0153
        L_0x012d:
            if (r4 == 0) goto L_0x014b
            boolean r2 = r4 instanceof java.lang.String     // Catch:{ all -> 0x01f4 }
            if (r2 == 0) goto L_0x0134
            goto L_0x014b
        L_0x0134:
            java.lang.Class r2 = r4.getClass()     // Catch:{ all -> 0x01f4 }
            boolean r2 = r2.isPrimitive()     // Catch:{ all -> 0x01f4 }
            if (r2 != 0) goto L_0x0142
            boolean r2 = r4 instanceof java.lang.Number     // Catch:{ all -> 0x01f4 }
            if (r2 == 0) goto L_0x0153
        L_0x0142:
            java.lang.String r2 = com.alibaba.fastjson.JSON.toJSONString(r4)     // Catch:{ all -> 0x01f4 }
            java.lang.Object r2 = r7.processValue(r14, r0, r2, r3)     // Catch:{ all -> 0x01f4 }
            goto L_0x0152
        L_0x014b:
            r2 = r4
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x01f4 }
            java.lang.Object r2 = r7.processValue(r14, r0, r2, r3)     // Catch:{ all -> 0x01f4 }
        L_0x0152:
            r3 = r2
        L_0x0153:
            if (r3 != 0) goto L_0x015f
            com.alibaba.fastjson.serializer.SerializerFeature r2 = com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue     // Catch:{ all -> 0x01f4 }
            boolean r2 = r8.isEnabled(r2)     // Catch:{ all -> 0x01f4 }
            if (r2 != 0) goto L_0x015f
            goto L_0x006e
        L_0x015f:
            boolean r2 = r4 instanceof java.lang.String     // Catch:{ all -> 0x01f4 }
            r5 = 44
            if (r2 == 0) goto L_0x017c
            r2 = r4
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x01f4 }
            if (r1 != 0) goto L_0x016d
            r8.write((int) r5)     // Catch:{ all -> 0x01f4 }
        L_0x016d:
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat     // Catch:{ all -> 0x01f4 }
            boolean r1 = r8.isEnabled(r1)     // Catch:{ all -> 0x01f4 }
            if (r1 == 0) goto L_0x0178
            r19.println()     // Catch:{ all -> 0x01f4 }
        L_0x0178:
            r8.writeFieldName(r2, r12)     // Catch:{ all -> 0x01f4 }
            goto L_0x01aa
        L_0x017c:
            if (r1 != 0) goto L_0x0181
            r8.write((int) r5)     // Catch:{ all -> 0x01f4 }
        L_0x0181:
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible     // Catch:{ all -> 0x01f4 }
            boolean r1 = r8.isEnabled(r1)     // Catch:{ all -> 0x01f4 }
            if (r1 != 0) goto L_0x019e
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNonStringKeyAsString     // Catch:{ all -> 0x01f4 }
            boolean r1 = r8.isEnabled(r1)     // Catch:{ all -> 0x01f4 }
            if (r1 != 0) goto L_0x019e
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserSecure     // Catch:{ all -> 0x01f4 }
            boolean r1 = r8.isEnabled(r1)     // Catch:{ all -> 0x01f4 }
            if (r1 == 0) goto L_0x019a
            goto L_0x019e
        L_0x019a:
            r7.write((java.lang.Object) r4)     // Catch:{ all -> 0x01f4 }
            goto L_0x01a5
        L_0x019e:
            java.lang.String r1 = com.alibaba.fastjson.JSON.toJSONString(r4)     // Catch:{ all -> 0x01f4 }
            r7.write((java.lang.String) r1)     // Catch:{ all -> 0x01f4 }
        L_0x01a5:
            r1 = 58
            r8.write((int) r1)     // Catch:{ all -> 0x01f4 }
        L_0x01aa:
            if (r3 != 0) goto L_0x01b2
            r8.writeNull()     // Catch:{ all -> 0x01f4 }
        L_0x01af:
            r1 = 0
            goto L_0x006e
        L_0x01b2:
            java.lang.Class r6 = r3.getClass()     // Catch:{ all -> 0x01f4 }
            if (r6 != r15) goto L_0x01c2
            r5 = 0
            r6 = 0
            r1 = r16
            r2 = r19
            r1.write(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x01f4 }
            goto L_0x01af
        L_0x01c2:
            com.alibaba.fastjson.serializer.ObjectSerializer r15 = r7.getObjectWriter(r6)     // Catch:{ all -> 0x01f4 }
            r5 = 0
            r16 = 0
            r1 = r15
            r2 = r19
            r17 = r6
            r6 = r16
            r1.write(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x01f4 }
            r16 = r15
            r15 = r17
            goto L_0x01af
        L_0x01d8:
            r7.context = r10
            r19.decrementIdent()
            com.alibaba.fastjson.serializer.SerializerFeature r0 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat
            boolean r0 = r8.isEnabled(r0)
            if (r0 == 0) goto L_0x01ee
            int r0 = r9.size()
            if (r0 <= 0) goto L_0x01ee
            r19.println()
        L_0x01ee:
            r0 = 125(0x7d, float:1.75E-43)
            r8.write((int) r0)
            return
        L_0x01f4:
            r0 = move-exception
            r7.context = r10
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.MapSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int):void");
    }
}
