package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanSerializer implements ObjectSerializer {
    private static final char[] false_chars = {'f', 'a', 'l', 's', 'e'};
    private static final char[] true_chars = {'t', 'r', 'u', 'e'};
    protected final Class<?> beanType;
    protected int features;
    private final FieldSerializer[] getters;
    protected final FieldSerializer[] sortedGetters;

    public JavaBeanSerializer(Class<?> cls) {
        this(cls, (Map<String, String>) null);
    }

    public JavaBeanSerializer(Class<?> cls, String... strArr) {
        this(cls, createAliasMap(strArr));
    }

    static Map<String, String> createAliasMap(String... strArr) {
        HashMap hashMap = new HashMap();
        for (String str : strArr) {
            hashMap.put(str, str);
        }
        return hashMap;
    }

    public JavaBeanSerializer(Class<?> cls, Map<String, String> map) {
        this(cls, map, TypeUtils.getSerializeFeatures(cls));
    }

    public JavaBeanSerializer(Class<?> cls, Map<String, String> map, int i) {
        this.features = 0;
        this.features = i;
        this.beanType = cls;
        JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
        if (jSONType != null) {
            SerializerFeature.of(jSONType.serialzeFeatures());
        }
        ArrayList arrayList = new ArrayList();
        for (FieldInfo fieldSerializer : TypeUtils.computeGetters(cls, jSONType, map, false)) {
            arrayList.add(new FieldSerializer(cls, fieldSerializer));
        }
        this.getters = (FieldSerializer[]) arrayList.toArray(new FieldSerializer[arrayList.size()]);
        String[] orders = jSONType != null ? jSONType.orders() : null;
        if (orders == null || orders.length == 0) {
            FieldSerializer[] fieldSerializerArr = this.getters;
            FieldSerializer[] fieldSerializerArr2 = new FieldSerializer[fieldSerializerArr.length];
            System.arraycopy(fieldSerializerArr, 0, fieldSerializerArr2, 0, fieldSerializerArr.length);
            Arrays.sort(fieldSerializerArr2);
            if (Arrays.equals(fieldSerializerArr2, this.getters)) {
                this.sortedGetters = this.getters;
            } else {
                this.sortedGetters = fieldSerializerArr2;
            }
        } else {
            List<FieldInfo> computeGetters = TypeUtils.computeGetters(cls, jSONType, map, true);
            ArrayList arrayList2 = new ArrayList();
            for (FieldInfo fieldSerializer2 : computeGetters) {
                arrayList2.add(new FieldSerializer(cls, fieldSerializer2));
            }
            this.sortedGetters = (FieldSerializer[]) arrayList2.toArray(new FieldSerializer[arrayList2.size()]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:265:0x03ad, code lost:
        if (((java.lang.Boolean) r5).booleanValue() == false) goto L_0x03af;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0189 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01ba A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x01d8 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x01e1 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x01e9 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x021c A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }, LOOP:4: B:140:0x0216->B:142:0x021c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:167:0x026e A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x02a1 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }, LOOP:5: B:182:0x029b->B:184:0x02a1, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x02b7 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x02bf A[ADDED_TO_REGION, Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:205:0x0307 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:218:0x0323 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:267:0x03b5 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x03b9 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x03c6 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x03ca A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:279:0x03d8 A[Catch:{ Exception -> 0x04b0, all -> 0x04ac }] */
    /* JADX WARNING: Removed duplicated region for block: B:341:0x04bd A[SYNTHETIC, Splitter:B:341:0x04bd] */
    /* JADX WARNING: Removed duplicated region for block: B:345:0x04de A[Catch:{ all -> 0x0516 }] */
    /* JADX WARNING: Removed duplicated region for block: B:348:0x04f8 A[Catch:{ all -> 0x0516 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r38, java.lang.Object r39, java.lang.Object r40, java.lang.reflect.Type r41, int r42) throws java.io.IOException {
        /*
            r37 = this;
            r1 = r37
            r8 = r38
            r9 = r39
            r10 = r40
            r0 = r41
            com.alibaba.fastjson.serializer.SerializeWriter r11 = r8.out
            if (r9 != 0) goto L_0x0012
            r11.writeNull()
            return
        L_0x0012:
            r7 = r42
            boolean r2 = r1.writeReference(r8, r9, r7)
            if (r2 == 0) goto L_0x001b
            return
        L_0x001b:
            boolean r2 = r11.sortField
            if (r2 == 0) goto L_0x0022
            com.alibaba.fastjson.serializer.FieldSerializer[] r2 = r1.sortedGetters
            goto L_0x0024
        L_0x0022:
            com.alibaba.fastjson.serializer.FieldSerializer[] r2 = r1.getters
        L_0x0024:
            r12 = r2
            com.alibaba.fastjson.serializer.SerialContext r13 = r8.context
            int r6 = r1.features
            r2 = r38
            r3 = r13
            r4 = r39
            r5 = r40
            r7 = r42
            r2.setContext(r3, r4, r5, r6, r7)
            boolean r2 = r37.isWriteAsArray(r38)
            if (r2 == 0) goto L_0x003e
            r3 = 91
            goto L_0x0040
        L_0x003e:
            r3 = 123(0x7b, float:1.72E-43)
        L_0x0040:
            if (r2 == 0) goto L_0x0045
            r4 = 93
            goto L_0x0047
        L_0x0045:
            r4 = 125(0x7d, float:1.75E-43)
        L_0x0047:
            r11.append((char) r3)     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            int r3 = r12.length     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r3 <= 0) goto L_0x0057
            boolean r3 = r11.prettyFormat     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r3 == 0) goto L_0x0057
            r38.incrementIndent()     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r38.println()     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
        L_0x0057:
            int r3 = r1.features     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            com.alibaba.fastjson.serializer.SerializerFeature r5 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            int r5 = r5.mask     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r3 = r3 & r5
            r5 = 0
            if (r3 != 0) goto L_0x0067
            boolean r3 = r8.isWriteClassName(r0, r9)     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r3 == 0) goto L_0x007b
        L_0x0067:
            java.lang.Class r3 = r39.getClass()     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r3 == r0) goto L_0x007b
            java.lang.String r0 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r11.writeFieldName(r0, r5)     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            java.lang.Class r0 = r39.getClass()     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r8.write((java.lang.Object) r0)     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r0 = 1
            goto L_0x007c
        L_0x007b:
            r0 = 0
        L_0x007c:
            r3 = 44
            if (r0 == 0) goto L_0x0083
            r0 = 44
            goto L_0x0084
        L_0x0083:
            r0 = 0
        L_0x0084:
            boolean r7 = r11.quoteFieldNames     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r7 == 0) goto L_0x008e
            boolean r7 = r11.useSingleQuotes     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r7 != 0) goto L_0x008e
            r7 = 1
            goto L_0x008f
        L_0x008e:
            r7 = 0
        L_0x008f:
            char r0 = r8.writeBefore(r9, r0)     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            if (r0 != r3) goto L_0x0097
            r0 = 1
            goto L_0x0098
        L_0x0097:
            r0 = 0
        L_0x0098:
            boolean r14 = r11.skipTransientField     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            boolean r15 = r11.ignoreNonFieldGetter     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            java.util.List<com.alibaba.fastjson.serializer.LabelFilter> r5 = r8.labelFilters     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            java.util.List<com.alibaba.fastjson.serializer.PropertyFilter> r6 = r8.propertyFilters     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            java.util.List<com.alibaba.fastjson.serializer.NameFilter> r3 = r8.nameFilters     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r16 = r0
            java.util.List<com.alibaba.fastjson.serializer.ValueFilter> r0 = r8.valueFilters     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            java.util.List<com.alibaba.fastjson.serializer.ContextValueFilter> r1 = r8.contextValueFilters     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            java.util.List<com.alibaba.fastjson.serializer.PropertyPreFilter> r10 = r8.propertyPreFilters     // Catch:{ Exception -> 0x04b7, all -> 0x04b4 }
            r18 = r4
            r17 = r13
            r13 = 0
        L_0x00af:
            int r4 = r12.length     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r13 >= r4) goto L_0x0487
            r4 = r12[r13]     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r19 = r12
            com.alibaba.fastjson.util.FieldInfo r12 = r4.fieldInfo     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            java.lang.reflect.Field r12 = r12.field     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r20 = r13
            com.alibaba.fastjson.util.FieldInfo r13 = r4.fieldInfo     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r21 = r7
            java.lang.String r7 = r13.name     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r22 = r2
            java.lang.Class<?> r2 = r13.fieldClass     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r14 == 0) goto L_0x00d1
            if (r12 == 0) goto L_0x00d1
            r23 = r14
            boolean r14 = r13.fieldTransient     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r14 == 0) goto L_0x00d3
            goto L_0x00d7
        L_0x00d1:
            r23 = r14
        L_0x00d3:
            if (r15 == 0) goto L_0x00e7
            if (r12 != 0) goto L_0x00e7
        L_0x00d7:
            r32 = r0
            r36 = r1
            r30 = r3
            r31 = r5
            r33 = r6
            r24 = r10
        L_0x00e3:
            r29 = r15
            goto L_0x03af
        L_0x00e7:
            if (r10 == 0) goto L_0x0108
            java.util.Iterator r12 = r10.iterator()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x00ed:
            boolean r14 = r12.hasNext()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r14 == 0) goto L_0x0108
            java.lang.Object r14 = r12.next()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            com.alibaba.fastjson.serializer.PropertyPreFilter r14 = (com.alibaba.fastjson.serializer.PropertyPreFilter) r14     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r24 = r10
            java.lang.String r10 = r13.name     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r10 = r14.apply(r8, r9, r10)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r10 != 0) goto L_0x0105
            r10 = 0
            goto L_0x010b
        L_0x0105:
            r10 = r24
            goto L_0x00ed
        L_0x0108:
            r24 = r10
            r10 = 1
        L_0x010b:
            if (r10 != 0) goto L_0x0118
        L_0x010d:
            r32 = r0
            r36 = r1
            r30 = r3
            r31 = r5
            r33 = r6
            goto L_0x00e3
        L_0x0118:
            if (r5 == 0) goto L_0x0134
            java.util.Iterator r10 = r5.iterator()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x011e:
            boolean r12 = r10.hasNext()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r12 == 0) goto L_0x0134
            java.lang.Object r12 = r10.next()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            com.alibaba.fastjson.serializer.LabelFilter r12 = (com.alibaba.fastjson.serializer.LabelFilter) r12     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            java.lang.String r14 = r13.label     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r12 = r12.apply(r14)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r12 != 0) goto L_0x011e
            r10 = 0
            goto L_0x0135
        L_0x0134:
            r10 = 1
        L_0x0135:
            if (r10 != 0) goto L_0x0138
            goto L_0x010d
        L_0x0138:
            r10 = 0
            boolean r12 = r13.fieldAccess     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r25 = 0
            if (r12 == 0) goto L_0x017a
            java.lang.Class r12 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r12) goto L_0x0152
            java.lang.reflect.Field r12 = r13.field     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r12 = r12.getInt(r9)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r29 = r10
            r27 = r25
            r10 = 1
        L_0x014e:
            r14 = 0
        L_0x014f:
            r30 = 0
            goto L_0x0187
        L_0x0152:
            java.lang.Class r12 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r12) goto L_0x0161
            java.lang.reflect.Field r12 = r13.field     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            long r27 = r12.getLong(r9)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r29 = r10
            r10 = 1
            r12 = 0
            goto L_0x014e
        L_0x0161:
            java.lang.Class r12 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r12) goto L_0x0173
            java.lang.reflect.Field r12 = r13.field     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r12 = r12.getBoolean(r9)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r29 = r10
            r14 = r12
            r27 = r25
            r10 = 1
            r12 = 0
            goto L_0x014f
        L_0x0173:
            java.lang.reflect.Field r10 = r13.field     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            java.lang.Object r10 = r10.get(r9)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x017e
        L_0x017a:
            java.lang.Object r10 = r4.getPropertyValue(r9)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x017e:
            r29 = r10
            r27 = r25
            r10 = 0
            r12 = 0
            r14 = 0
            r30 = 1
        L_0x0187:
            if (r6 == 0) goto L_0x01d8
            if (r10 == 0) goto L_0x01ac
            r31 = r5
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x019a
            java.lang.Integer r29 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x0195:
            r5 = r29
            r30 = 1
            goto L_0x01b0
        L_0x019a:
            java.lang.Class r5 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x01a3
            java.lang.Long r29 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x0195
        L_0x01a3:
            java.lang.Class r5 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x01ae
            java.lang.Boolean r29 = java.lang.Boolean.valueOf(r14)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x0195
        L_0x01ac:
            r31 = r5
        L_0x01ae:
            r5 = r29
        L_0x01b0:
            java.util.Iterator r29 = r6.iterator()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x01b4:
            boolean r32 = r29.hasNext()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r32 == 0) goto L_0x01d3
            java.lang.Object r32 = r29.next()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r33 = r6
            r6 = r32
            com.alibaba.fastjson.serializer.PropertyFilter r6 = (com.alibaba.fastjson.serializer.PropertyFilter) r6     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r6 = r6.apply(r9, r7, r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r6 != 0) goto L_0x01d0
            r29 = r5
            r6 = r30
            r5 = 0
            goto L_0x01df
        L_0x01d0:
            r6 = r33
            goto L_0x01b4
        L_0x01d3:
            r33 = r6
            r29 = r5
            goto L_0x01dc
        L_0x01d8:
            r31 = r5
            r33 = r6
        L_0x01dc:
            r6 = r30
            r5 = 1
        L_0x01df:
            if (r5 != 0) goto L_0x01e9
            r32 = r0
            r36 = r1
            r30 = r3
            goto L_0x00e3
        L_0x01e9:
            if (r3 == 0) goto L_0x0232
            if (r10 == 0) goto L_0x020d
            if (r6 != 0) goto L_0x020d
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x01fb
            java.lang.Integer r29 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x01f7:
            r5 = r29
            r6 = 1
            goto L_0x020f
        L_0x01fb:
            java.lang.Class r5 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x0204
            java.lang.Long r29 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x01f7
        L_0x0204:
            java.lang.Class r5 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x020d
            java.lang.Boolean r29 = java.lang.Boolean.valueOf(r14)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x01f7
        L_0x020d:
            r5 = r29
        L_0x020f:
            java.util.Iterator r29 = r3.iterator()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r30 = r3
            r3 = r7
        L_0x0216:
            boolean r32 = r29.hasNext()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r32 == 0) goto L_0x022d
            java.lang.Object r32 = r29.next()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r34 = r6
            r6 = r32
            com.alibaba.fastjson.serializer.NameFilter r6 = (com.alibaba.fastjson.serializer.NameFilter) r6     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            java.lang.String r3 = r6.process(r9, r3, r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r6 = r34
            goto L_0x0216
        L_0x022d:
            r34 = r6
            r29 = r34
            goto L_0x0239
        L_0x0232:
            r30 = r3
            r3 = r7
            r5 = r29
            r29 = r6
        L_0x0239:
            boolean r6 = r11.writeNonStringValueAsString     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r6 == 0) goto L_0x026a
            java.lang.Class r6 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r6) goto L_0x0247
            java.lang.String r5 = java.lang.Integer.toString(r12)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x0245:
            r6 = 1
            goto L_0x026c
        L_0x0247:
            java.lang.Class r6 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r6) goto L_0x0250
            java.lang.String r5 = java.lang.Long.toString(r27)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x0245
        L_0x0250:
            java.lang.Class r6 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r6) goto L_0x0259
            java.lang.String r5 = java.lang.Boolean.toString(r14)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x0245
        L_0x0259:
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            if (r2 != r6) goto L_0x025e
            goto L_0x026a
        L_0x025e:
            boolean r6 = r5 instanceof java.lang.Number     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r6 != 0) goto L_0x0266
            boolean r6 = r5 instanceof java.lang.Boolean     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r6 == 0) goto L_0x026a
        L_0x0266:
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x026a:
            r6 = r29
        L_0x026c:
            if (r0 == 0) goto L_0x02b7
            if (r10 == 0) goto L_0x0290
            if (r6 != 0) goto L_0x0290
            r29 = r5
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x027e
            java.lang.Integer r5 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x027c:
            r6 = 1
            goto L_0x0294
        L_0x027e:
            java.lang.Class r5 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x0287
            java.lang.Long r5 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x027c
        L_0x0287:
            java.lang.Class r5 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r5) goto L_0x0292
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r14)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x027c
        L_0x0290:
            r29 = r5
        L_0x0292:
            r5 = r29
        L_0x0294:
            java.util.Iterator r29 = r0.iterator()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r32 = r0
            r0 = r5
        L_0x029b:
            boolean r34 = r29.hasNext()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r34 == 0) goto L_0x02b2
            java.lang.Object r34 = r29.next()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r35 = r5
            r5 = r34
            com.alibaba.fastjson.serializer.ValueFilter r5 = (com.alibaba.fastjson.serializer.ValueFilter) r5     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            java.lang.Object r0 = r5.process(r9, r7, r0)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r5 = r35
            goto L_0x029b
        L_0x02b2:
            r35 = r5
            r29 = r0
            goto L_0x02bd
        L_0x02b7:
            r32 = r0
            r29 = r5
            r35 = r29
        L_0x02bd:
            if (r1 == 0) goto L_0x0307
            if (r10 == 0) goto L_0x02e1
            if (r6 != 0) goto L_0x02e1
            java.lang.Class r0 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r0) goto L_0x02cf
            java.lang.Integer r29 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x02cb:
            r35 = r29
            r6 = 1
            goto L_0x02e1
        L_0x02cf:
            java.lang.Class r0 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r0) goto L_0x02d8
            java.lang.Long r29 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x02cb
        L_0x02d8:
            java.lang.Class r0 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r0) goto L_0x02e1
            java.lang.Boolean r29 = java.lang.Boolean.valueOf(r14)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x02cb
        L_0x02e1:
            java.util.Iterator r0 = r1.iterator()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r5 = r29
        L_0x02e7:
            boolean r29 = r0.hasNext()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r29 == 0) goto L_0x0304
            java.lang.Object r29 = r0.next()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r34 = r0
            r0 = r29
            com.alibaba.fastjson.serializer.ContextValueFilter r0 = (com.alibaba.fastjson.serializer.ContextValueFilter) r0     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r36 = r1
            com.alibaba.fastjson.serializer.BeanContext r1 = r4.fieldContext     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            java.lang.Object r5 = r0.process(r1, r9, r7, r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r0 = r34
            r1 = r36
            goto L_0x02e7
        L_0x0304:
            r36 = r1
            goto L_0x030b
        L_0x0307:
            r36 = r1
            r5 = r29
        L_0x030b:
            r0 = r35
            if (r6 == 0) goto L_0x031d
            if (r5 != 0) goto L_0x031d
            if (r22 != 0) goto L_0x031d
            boolean r1 = r4.writeNull     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != 0) goto L_0x031d
            boolean r1 = r11.writeMapNullValue     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != 0) goto L_0x031d
            goto L_0x00e3
        L_0x031d:
            if (r5 == 0) goto L_0x03b5
            boolean r1 = r11.notWriteDefaultValue     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 == 0) goto L_0x03b5
            java.lang.Class<?> r1 = r13.fieldClass     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r29 = r15
            java.lang.Class r15 = java.lang.Byte.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x033a
            boolean r15 = r5 instanceof java.lang.Byte     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x033a
            r15 = r5
            java.lang.Byte r15 = (java.lang.Byte) r15     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            byte r15 = r15.byteValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 != 0) goto L_0x033a
            goto L_0x03af
        L_0x033a:
            java.lang.Class r15 = java.lang.Short.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x034c
            boolean r15 = r5 instanceof java.lang.Short     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x034c
            r15 = r5
            java.lang.Short r15 = (java.lang.Short) r15     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            short r15 = r15.shortValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 != 0) goto L_0x034c
            goto L_0x03af
        L_0x034c:
            java.lang.Class r15 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x035e
            boolean r15 = r5 instanceof java.lang.Integer     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x035e
            r15 = r5
            java.lang.Integer r15 = (java.lang.Integer) r15     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r15 = r15.intValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 != 0) goto L_0x035e
            goto L_0x03af
        L_0x035e:
            java.lang.Class r15 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x0372
            boolean r15 = r5 instanceof java.lang.Long     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x0372
            r15 = r5
            java.lang.Long r15 = (java.lang.Long) r15     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            long r34 = r15.longValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r15 = (r34 > r25 ? 1 : (r34 == r25 ? 0 : -1))
            if (r15 != 0) goto L_0x0372
            goto L_0x03af
        L_0x0372:
            java.lang.Class r15 = java.lang.Float.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x0388
            boolean r15 = r5 instanceof java.lang.Float     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x0388
            r15 = r5
            java.lang.Float r15 = (java.lang.Float) r15     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            float r15 = r15.floatValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r25 = 0
            int r15 = (r15 > r25 ? 1 : (r15 == r25 ? 0 : -1))
            if (r15 != 0) goto L_0x0388
            goto L_0x03af
        L_0x0388:
            java.lang.Class r15 = java.lang.Double.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x039e
            boolean r15 = r5 instanceof java.lang.Double     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x039e
            r15 = r5
            java.lang.Double r15 = (java.lang.Double) r15     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            double r25 = r15.doubleValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r34 = 0
            int r15 = (r25 > r34 ? 1 : (r25 == r34 ? 0 : -1))
            if (r15 != 0) goto L_0x039e
            goto L_0x03af
        L_0x039e:
            java.lang.Class r15 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != r15) goto L_0x03b7
            boolean r1 = r5 instanceof java.lang.Boolean     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 == 0) goto L_0x03b7
            r1 = r5
            java.lang.Boolean r1 = (java.lang.Boolean) r1     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r1 != 0) goto L_0x03b7
        L_0x03af:
            r0 = 0
            r1 = 44
            r7 = 1
            goto L_0x046d
        L_0x03b5:
            r29 = r15
        L_0x03b7:
            if (r16 == 0) goto L_0x03c6
            r1 = 44
            r11.write((int) r1)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r15 = r11.prettyFormat     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r15 == 0) goto L_0x03c8
            r38.println()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03c8
        L_0x03c6:
            r1 = 44
        L_0x03c8:
            if (r3 == r7) goto L_0x03d8
            if (r22 != 0) goto L_0x03d1
            r7 = 1
            r11.writeFieldName(r3, r7)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d2
        L_0x03d1:
            r7 = 1
        L_0x03d2:
            r8.write((java.lang.Object) r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x03d5:
            r0 = 0
            goto L_0x046b
        L_0x03d8:
            r7 = 1
            if (r0 == r5) goto L_0x03e4
            if (r22 != 0) goto L_0x03e0
            r4.writePrefix(r8)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x03e0:
            r8.write((java.lang.Object) r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x03e4:
            if (r22 != 0) goto L_0x03f5
            if (r21 == 0) goto L_0x03f2
            char[] r0 = r13.name_chars     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            char[] r3 = r13.name_chars     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r3 = r3.length     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r13 = 0
            r11.write((char[]) r0, (int) r13, (int) r3)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03f5
        L_0x03f2:
            r4.writePrefix(r8)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x03f5:
            if (r10 == 0) goto L_0x042d
            if (r6 != 0) goto L_0x042d
            java.lang.Class r0 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r0) goto L_0x0403
            com.alibaba.fastjson.serializer.SerializeWriter r0 = r8.out     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r0.writeInt(r12)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x0403:
            java.lang.Class r0 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r0) goto L_0x040f
            com.alibaba.fastjson.serializer.SerializeWriter r0 = r8.out     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r2 = r27
            r0.writeLong(r2)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x040f:
            java.lang.Class r0 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r2 != r0) goto L_0x03d5
            if (r14 == 0) goto L_0x0421
            com.alibaba.fastjson.serializer.SerializeWriter r0 = r8.out     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            char[] r2 = true_chars     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            char[] r3 = true_chars     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r3 = r3.length     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r4 = 0
            r0.write((char[]) r2, (int) r4, (int) r3)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x0421:
            com.alibaba.fastjson.serializer.SerializeWriter r0 = r8.out     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            char[] r2 = false_chars     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            char[] r3 = false_chars     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r3 = r3.length     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r4 = 0
            r0.write((char[]) r2, (int) r4, (int) r3)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x042d:
            if (r22 != 0) goto L_0x0467
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            if (r2 != r0) goto L_0x0462
            if (r5 != 0) goto L_0x0452
            int r0 = r11.features     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            com.alibaba.fastjson.serializer.SerializerFeature r2 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r2 = r2.mask     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r0 = r0 & r2
            if (r0 != 0) goto L_0x044c
            int r0 = r4.features     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            com.alibaba.fastjson.serializer.SerializerFeature r2 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            int r2 = r2.mask     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r0 = r0 & r2
            if (r0 == 0) goto L_0x0448
            goto L_0x044c
        L_0x0448:
            r11.writeNull()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x044c:
            java.lang.String r0 = ""
            r11.writeString(r0)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x0452:
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            boolean r0 = r11.useSingleQuotes     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r0 == 0) goto L_0x045d
            r11.writeStringWithSingleQuote(r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x03d5
        L_0x045d:
            r0 = 0
            r11.writeStringWithDoubleQuote(r5, r0)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x046b
        L_0x0462:
            r0 = 0
            r4.writeValue(r8, r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            goto L_0x046b
        L_0x0467:
            r0 = 0
            r4.writeValue(r8, r5)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x046b:
            r16 = 1
        L_0x046d:
            int r13 = r20 + 1
            r12 = r19
            r7 = r21
            r2 = r22
            r14 = r23
            r10 = r24
            r15 = r29
            r3 = r30
            r5 = r31
            r0 = r32
            r6 = r33
            r1 = r36
            goto L_0x00af
        L_0x0487:
            r19 = r12
            r0 = 0
            r1 = 44
            if (r16 == 0) goto L_0x0490
            r0 = 44
        L_0x0490:
            r8.writeAfter(r9, r0)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r2 = r19
            int r0 = r2.length     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r0 <= 0) goto L_0x04a2
            boolean r0 = r11.prettyFormat     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            if (r0 == 0) goto L_0x04a2
            r38.decrementIdent()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r38.println()     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
        L_0x04a2:
            r4 = r18
            r11.append((char) r4)     // Catch:{ Exception -> 0x04b0, all -> 0x04ac }
            r1 = r17
            r8.context = r1
            return
        L_0x04ac:
            r0 = move-exception
            r1 = r17
            goto L_0x0517
        L_0x04b0:
            r0 = move-exception
            r1 = r17
            goto L_0x04b9
        L_0x04b4:
            r0 = move-exception
            r1 = r13
            goto L_0x0517
        L_0x04b7:
            r0 = move-exception
            r1 = r13
        L_0x04b9:
            java.lang.String r2 = "write javaBean error"
            if (r9 == 0) goto L_0x04d9
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0516 }
            r3.<init>()     // Catch:{ all -> 0x0516 }
            r3.append(r2)     // Catch:{ all -> 0x0516 }
            java.lang.String r2 = ", class "
            r3.append(r2)     // Catch:{ all -> 0x0516 }
            java.lang.Class r2 = r39.getClass()     // Catch:{ all -> 0x0516 }
            java.lang.String r2 = r2.getName()     // Catch:{ all -> 0x0516 }
            r3.append(r2)     // Catch:{ all -> 0x0516 }
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x0516 }
        L_0x04d9:
            r3 = r2
            r2 = r40
            if (r2 == 0) goto L_0x04f2
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0516 }
            r4.<init>()     // Catch:{ all -> 0x0516 }
            r4.append(r3)     // Catch:{ all -> 0x0516 }
            java.lang.String r3 = ", fieldName : "
            r4.append(r3)     // Catch:{ all -> 0x0516 }
            r4.append(r2)     // Catch:{ all -> 0x0516 }
            java.lang.String r3 = r4.toString()     // Catch:{ all -> 0x0516 }
        L_0x04f2:
            java.lang.String r2 = r0.getMessage()     // Catch:{ all -> 0x0516 }
            if (r2 == 0) goto L_0x0510
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0516 }
            r2.<init>()     // Catch:{ all -> 0x0516 }
            r2.append(r3)     // Catch:{ all -> 0x0516 }
            java.lang.String r3 = ", "
            r2.append(r3)     // Catch:{ all -> 0x0516 }
            java.lang.String r3 = r0.getMessage()     // Catch:{ all -> 0x0516 }
            r2.append(r3)     // Catch:{ all -> 0x0516 }
            java.lang.String r3 = r2.toString()     // Catch:{ all -> 0x0516 }
        L_0x0510:
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0516 }
            r2.<init>(r3, r0)     // Catch:{ all -> 0x0516 }
            throw r2     // Catch:{ all -> 0x0516 }
        L_0x0516:
            r0 = move-exception
        L_0x0517:
            r8.context = r1
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JavaBeanSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int):void");
    }

    public boolean writeReference(JSONSerializer jSONSerializer, Object obj, int i) {
        SerialContext serialContext = jSONSerializer.context;
        int i2 = SerializerFeature.DisableCircularReferenceDetect.mask;
        if ((serialContext != null && ((serialContext.features & i2) != 0 || (i & i2) != 0)) || jSONSerializer.references == null || !jSONSerializer.references.containsKey(obj)) {
            return false;
        }
        jSONSerializer.writeReference(obj);
        return true;
    }

    public boolean isWriteAsArray(JSONSerializer jSONSerializer) {
        return (this.features & SerializerFeature.BeanToArray.mask) != 0 || jSONSerializer.out.beanToArray;
    }

    public FieldSerializer getFieldSerializer(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = this.sortedGetters.length - 1;
        while (i <= length) {
            int i2 = (i + length) >>> 1;
            int compareTo = this.sortedGetters[i2].fieldInfo.name.compareTo(str);
            if (compareTo < 0) {
                i = i2 + 1;
            } else if (compareTo <= 0) {
                return this.sortedGetters[i2];
            } else {
                length = i2 - 1;
            }
        }
        return null;
    }

    public List<Object> getFieldValues(Object obj) throws Exception {
        ArrayList arrayList = new ArrayList(this.sortedGetters.length);
        for (FieldSerializer propertyValue : this.sortedGetters) {
            arrayList.add(propertyValue.getPropertyValue(obj));
        }
        return arrayList;
    }
}
