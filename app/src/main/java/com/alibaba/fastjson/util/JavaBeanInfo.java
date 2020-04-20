package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class JavaBeanInfo {
    public final Method buildMethod;
    public final Class<?> builderClass;
    public final Class<?> clazz;
    public final Constructor<?> creatorConstructor;
    public final Constructor<?> defaultConstructor;
    public final int defaultConstructorParameterSize;
    public final Method factoryMethod;
    public final FieldInfo[] fields;
    public final JSONType jsonType;
    public final int parserFeatures;
    public final FieldInfo[] sortedFields;

    public JavaBeanInfo(Class<?> cls, Class<?> cls2, Constructor<?> constructor, Constructor<?> constructor2, Method method, Method method2, JSONType jSONType, List<FieldInfo> list) {
        this.clazz = cls;
        this.builderClass = cls2;
        this.defaultConstructor = constructor;
        this.creatorConstructor = constructor2;
        this.factoryMethod = method;
        this.parserFeatures = TypeUtils.getParserFeatures(cls);
        this.buildMethod = method2;
        this.jsonType = jSONType;
        this.fields = new FieldInfo[list.size()];
        list.toArray(this.fields);
        FieldInfo[] fieldInfoArr = this.fields;
        FieldInfo[] fieldInfoArr2 = new FieldInfo[fieldInfoArr.length];
        int i = 0;
        System.arraycopy(fieldInfoArr, 0, fieldInfoArr2, 0, fieldInfoArr.length);
        Arrays.sort(fieldInfoArr2);
        this.sortedFields = Arrays.equals(this.fields, fieldInfoArr2) ? this.fields : fieldInfoArr2;
        this.defaultConstructorParameterSize = constructor != null ? constructor.getParameterTypes().length : i;
    }

    private static FieldInfo getField(List<FieldInfo> list, String str) {
        for (FieldInfo next : list) {
            if (next.name.equals(str)) {
                return next;
            }
        }
        return null;
    }

    static boolean add(List<FieldInfo> list, FieldInfo fieldInfo) {
        FieldInfo fieldInfo2;
        int size = list.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            fieldInfo2 = list.get(size);
            if (!fieldInfo2.name.equals(fieldInfo.name) || (fieldInfo2.getOnly && !fieldInfo.getOnly)) {
                size--;
            }
        }
        if (fieldInfo2.fieldClass.isAssignableFrom(fieldInfo.fieldClass)) {
            list.remove(size);
        } else if (fieldInfo2.compareTo(fieldInfo) >= 0) {
            return false;
        } else {
            list.remove(size);
        }
        list.add(fieldInfo);
        return true;
    }

    /* JADX WARNING: type inference failed for: r0v28, types: [java.lang.annotation.Annotation] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x049e  */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x053a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.fastjson.util.JavaBeanInfo build(java.lang.Class<?> r30, java.lang.reflect.Type r31) {
        /*
            r11 = r30
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r0 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r0 = r11.getAnnotation(r0)
            r12 = r0
            com.alibaba.fastjson.annotation.JSONType r12 = (com.alibaba.fastjson.annotation.JSONType) r12
            java.lang.Class r13 = getBuilderClass(r12)
            java.lang.reflect.Field[] r14 = r30.getDeclaredFields()
            java.lang.reflect.Method[] r15 = r30.getMethods()
            if (r13 != 0) goto L_0x001b
            r0 = r11
            goto L_0x001c
        L_0x001b:
            r0 = r13
        L_0x001c:
            java.lang.reflect.Constructor r16 = getDefaultConstructor(r0)
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            r17 = 0
            if (r16 != 0) goto L_0x015a
            boolean r0 = r30.isInterface()
            if (r0 != 0) goto L_0x015a
            int r0 = r30.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isAbstract(r0)
            if (r0 != 0) goto L_0x015a
            java.lang.reflect.Constructor r8 = getCreatorConstructor(r30)
            java.lang.String r7 = "illegal json creator"
            if (r8 == 0) goto L_0x00bc
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r8)
            java.lang.Class[] r15 = r8.getParameterTypes()
            int r0 = r15.length
            if (r0 <= 0) goto L_0x00ac
            java.lang.annotation.Annotation[][] r16 = r8.getParameterAnnotations()
            r6 = 0
        L_0x0050:
            int r0 = r15.length
            if (r6 >= r0) goto L_0x00ac
            r0 = r16[r6]
            int r1 = r0.length
            r2 = 0
        L_0x0057:
            if (r2 >= r1) goto L_0x0066
            r3 = r0[r2]
            boolean r4 = r3 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r4 == 0) goto L_0x0063
            r0 = r3
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            goto L_0x0068
        L_0x0063:
            int r2 = r2 + 1
            goto L_0x0057
        L_0x0066:
            r0 = r17
        L_0x0068:
            if (r0 == 0) goto L_0x00a5
            r3 = r15[r6]
            java.lang.reflect.Type[] r1 = r8.getGenericParameterTypes()
            r4 = r1[r6]
            java.lang.String r1 = r0.name()
            java.lang.reflect.Field r5 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r1, r14)
            int r18 = r0.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r1 = r0.serialzeFeatures()
            int r19 = com.alibaba.fastjson.serializer.SerializerFeature.of(r1)
            com.alibaba.fastjson.util.FieldInfo r2 = new com.alibaba.fastjson.util.FieldInfo
            java.lang.String r1 = r0.name()
            r0 = r2
            r9 = r2
            r2 = r30
            r21 = r6
            r6 = r18
            r18 = r14
            r14 = r7
            r7 = r19
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            add(r10, r9)
            int r6 = r21 + 1
            r7 = r14
            r14 = r18
            goto L_0x0050
        L_0x00a5:
            r14 = r7
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            r0.<init>(r14)
            throw r0
        L_0x00ac:
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r3 = 0
            r5 = 0
            r6 = 0
            r0 = r9
            r1 = r30
            r2 = r13
            r4 = r8
            r7 = r12
            r8 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        L_0x00bc:
            r18 = r14
            r14 = r7
            java.lang.reflect.Method r8 = getFactoryMethod(r11, r15)
            if (r8 == 0) goto L_0x0143
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r8)
            java.lang.Class[] r9 = r8.getParameterTypes()
            int r0 = r9.length
            if (r0 <= 0) goto L_0x0133
            java.lang.annotation.Annotation[][] r15 = r8.getParameterAnnotations()
            r7 = 0
        L_0x00d4:
            int r0 = r9.length
            if (r7 >= r0) goto L_0x0133
            r0 = r15[r7]
            int r1 = r0.length
            r2 = 0
        L_0x00db:
            if (r2 >= r1) goto L_0x00ea
            r3 = r0[r2]
            boolean r4 = r3 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r4 == 0) goto L_0x00e7
            r0 = r3
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            goto L_0x00ec
        L_0x00e7:
            int r2 = r2 + 1
            goto L_0x00db
        L_0x00ea:
            r0 = r17
        L_0x00ec:
            if (r0 == 0) goto L_0x012d
            r3 = r9[r7]
            java.lang.reflect.Type[] r1 = r8.getGenericParameterTypes()
            r4 = r1[r7]
            java.lang.String r1 = r0.name()
            r6 = r18
            java.lang.reflect.Field r5 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r1, r6)
            int r16 = r0.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r1 = r0.serialzeFeatures()
            int r18 = com.alibaba.fastjson.serializer.SerializerFeature.of(r1)
            com.alibaba.fastjson.util.FieldInfo r2 = new com.alibaba.fastjson.util.FieldInfo
            java.lang.String r1 = r0.name()
            r0 = r2
            r31 = r9
            r9 = r2
            r2 = r30
            r22 = r6
            r6 = r16
            r16 = r7
            r7 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            add(r10, r9)
            int r7 = r16 + 1
            r9 = r31
            r18 = r22
            goto L_0x00d4
        L_0x012d:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            r0.<init>(r14)
            throw r0
        L_0x0133:
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r3 = 0
            r4 = 0
            r6 = 0
            r0 = r9
            r1 = r30
            r2 = r13
            r5 = r8
            r7 = r12
            r8 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        L_0x0143:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "default constructor not found. "
            r1.append(r2)
            r1.append(r11)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x015a:
            r22 = r14
            if (r16 == 0) goto L_0x0161
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r16)
        L_0x0161:
            if (r13 == 0) goto L_0x02d1
            java.lang.Class<com.alibaba.fastjson.annotation.JSONPOJOBuilder> r0 = com.alibaba.fastjson.annotation.JSONPOJOBuilder.class
            java.lang.annotation.Annotation r0 = r13.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONPOJOBuilder r0 = (com.alibaba.fastjson.annotation.JSONPOJOBuilder) r0
            if (r0 == 0) goto L_0x0172
            java.lang.String r0 = r0.withPrefix()
            goto L_0x0174
        L_0x0172:
            r0 = r17
        L_0x0174:
            if (r0 == 0) goto L_0x017c
            int r1 = r0.length()
            if (r1 != 0) goto L_0x017e
        L_0x017c:
            java.lang.String r0 = "with"
        L_0x017e:
            r14 = r0
            java.lang.reflect.Method[] r9 = r13.getMethods()
            int r8 = r9.length
            r7 = 0
        L_0x0185:
            if (r7 >= r8) goto L_0x028b
            r2 = r9[r7]
            int r0 = r2.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)
            if (r0 == 0) goto L_0x01a1
        L_0x0193:
            r25 = r7
            r26 = r8
            r20 = r9
            r21 = r12
            r18 = r14
            r14 = 0
            r12 = r10
            goto L_0x027e
        L_0x01a1:
            java.lang.Class r0 = r2.getReturnType()
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x01ac
            goto L_0x0193
        L_0x01ac:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = r2.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            if (r0 != 0) goto L_0x01ba
            com.alibaba.fastjson.annotation.JSONField r0 = com.alibaba.fastjson.util.TypeUtils.getSupperMethodAnnotation(r11, r2)
        L_0x01ba:
            r18 = r0
            if (r18 == 0) goto L_0x0213
            boolean r0 = r18.deserialize()
            if (r0 != 0) goto L_0x01c5
            goto L_0x0193
        L_0x01c5:
            int r6 = r18.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r0 = r18.serialzeFeatures()
            int r19 = com.alibaba.fastjson.serializer.SerializerFeature.of(r0)
            java.lang.String r0 = r18.name()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x0207
            java.lang.String r1 = r18.name()
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r21 = 0
            r23 = 0
            r0 = r5
            r4 = r30
            r24 = r5
            r5 = r31
            r25 = r7
            r7 = r19
            r26 = r8
            r8 = r18
            r20 = r9
            r9 = r21
            r21 = r12
            r12 = r10
            r10 = r23
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r24
            add(r12, r0)
            goto L_0x0228
        L_0x0207:
            r25 = r7
            r26 = r8
            r20 = r9
            r21 = r12
            r12 = r10
            r7 = r19
            goto L_0x021e
        L_0x0213:
            r25 = r7
            r26 = r8
            r20 = r9
            r21 = r12
            r12 = r10
            r6 = 0
            r7 = 0
        L_0x021e:
            java.lang.String r0 = r2.getName()
            boolean r1 = r0.startsWith(r14)
            if (r1 != 0) goto L_0x022c
        L_0x0228:
            r18 = r14
            r14 = 0
            goto L_0x027e
        L_0x022c:
            int r1 = r0.length()
            int r3 = r14.length()
            if (r1 > r3) goto L_0x0237
            goto L_0x0228
        L_0x0237:
            int r1 = r14.length()
            char r1 = r0.charAt(r1)
            boolean r3 = java.lang.Character.isUpperCase(r1)
            if (r3 != 0) goto L_0x0246
            goto L_0x0228
        L_0x0246:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            int r4 = r14.length()
            java.lang.String r0 = r0.substring(r4)
            r3.<init>(r0)
            char r0 = java.lang.Character.toLowerCase(r1)
            r10 = 0
            r3.setCharAt(r10, r0)
            java.lang.String r1 = r3.toString()
            com.alibaba.fastjson.util.FieldInfo r9 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r19 = 0
            r23 = 0
            r0 = r9
            r4 = r30
            r5 = r31
            r8 = r18
            r27 = r9
            r9 = r19
            r18 = r14
            r14 = 0
            r10 = r23
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r27
            add(r12, r0)
        L_0x027e:
            int r7 = r25 + 1
            r10 = r12
            r14 = r18
            r9 = r20
            r12 = r21
            r8 = r26
            goto L_0x0185
        L_0x028b:
            r21 = r12
            r14 = 0
            r12 = r10
            if (r13 == 0) goto L_0x02d5
            java.lang.Class<com.alibaba.fastjson.annotation.JSONPOJOBuilder> r0 = com.alibaba.fastjson.annotation.JSONPOJOBuilder.class
            java.lang.annotation.Annotation r0 = r13.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONPOJOBuilder r0 = (com.alibaba.fastjson.annotation.JSONPOJOBuilder) r0
            if (r0 == 0) goto L_0x02a0
            java.lang.String r0 = r0.buildMethod()
            goto L_0x02a2
        L_0x02a0:
            r0 = r17
        L_0x02a2:
            if (r0 == 0) goto L_0x02aa
            int r1 = r0.length()
            if (r1 != 0) goto L_0x02ac
        L_0x02aa:
            java.lang.String r0 = "build"
        L_0x02ac:
            java.lang.Class[] r1 = new java.lang.Class[r14]     // Catch:{ NoSuchMethodException | SecurityException -> 0x02b3 }
            java.lang.reflect.Method r0 = r13.getMethod(r0, r1)     // Catch:{ NoSuchMethodException | SecurityException -> 0x02b3 }
            goto L_0x02b5
        L_0x02b3:
            r0 = r17
        L_0x02b5:
            if (r0 != 0) goto L_0x02c1
            java.lang.String r1 = "create"
            java.lang.Class[] r2 = new java.lang.Class[r14]     // Catch:{ NoSuchMethodException | SecurityException -> 0x02c0 }
            java.lang.reflect.Method r0 = r13.getMethod(r1, r2)     // Catch:{ NoSuchMethodException | SecurityException -> 0x02c0 }
            goto L_0x02c1
        L_0x02c0:
        L_0x02c1:
            if (r0 == 0) goto L_0x02c9
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r0)
            r18 = r0
            goto L_0x02d7
        L_0x02c9:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r1 = "buildMethod not found."
            r0.<init>(r1)
            throw r0
        L_0x02d1:
            r21 = r12
            r14 = 0
            r12 = r10
        L_0x02d5:
            r18 = r17
        L_0x02d7:
            int r10 = r15.length
            r9 = 0
        L_0x02d9:
            r8 = 4
            r7 = 3
            r6 = 1
            if (r9 >= r10) goto L_0x0494
            r2 = r15[r9]
            java.lang.String r0 = r2.getName()
            int r1 = r0.length()
            if (r1 >= r8) goto L_0x02f4
        L_0x02ea:
            r26 = r9
            r24 = r10
        L_0x02ee:
            r14 = r22
            r22 = 0
            goto L_0x0489
        L_0x02f4:
            int r1 = r2.getModifiers()
            boolean r1 = java.lang.reflect.Modifier.isStatic(r1)
            if (r1 == 0) goto L_0x02ff
            goto L_0x02ea
        L_0x02ff:
            java.lang.Class r1 = r2.getReturnType()
            java.lang.Class r3 = java.lang.Void.TYPE
            boolean r1 = r1.equals(r3)
            if (r1 != 0) goto L_0x0316
            java.lang.Class r1 = r2.getReturnType()
            boolean r1 = r1.equals(r11)
            if (r1 != 0) goto L_0x0316
            goto L_0x02ea
        L_0x0316:
            java.lang.Class[] r1 = r2.getParameterTypes()
            int r3 = r1.length
            if (r3 == r6) goto L_0x031e
            goto L_0x02ea
        L_0x031e:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r3 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r3 = r2.getAnnotation(r3)
            com.alibaba.fastjson.annotation.JSONField r3 = (com.alibaba.fastjson.annotation.JSONField) r3
            if (r3 != 0) goto L_0x032c
            com.alibaba.fastjson.annotation.JSONField r3 = com.alibaba.fastjson.util.TypeUtils.getSupperMethodAnnotation(r11, r2)
        L_0x032c:
            r19 = r3
            if (r19 == 0) goto L_0x0379
            boolean r3 = r19.deserialize()
            if (r3 != 0) goto L_0x0337
            goto L_0x02ea
        L_0x0337:
            int r20 = r19.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r3 = r19.serialzeFeatures()
            int r23 = com.alibaba.fastjson.serializer.SerializerFeature.of(r3)
            java.lang.String r3 = r19.name()
            int r3 = r3.length()
            if (r3 == 0) goto L_0x0374
            java.lang.String r1 = r19.name()
            com.alibaba.fastjson.util.FieldInfo r8 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r24 = 0
            r25 = 0
            r0 = r8
            r4 = r30
            r5 = r31
            r6 = r20
            r7 = r23
            r14 = r8
            r8 = r19
            r26 = r9
            r9 = r24
            r24 = r10
            r10 = r25
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            add(r12, r14)
            goto L_0x02ee
        L_0x0374:
            r26 = r9
            r24 = r10
            goto L_0x0381
        L_0x0379:
            r26 = r9
            r24 = r10
            r20 = 0
            r23 = 0
        L_0x0381:
            java.lang.String r3 = "set"
            boolean r3 = r0.startsWith(r3)
            if (r3 != 0) goto L_0x038b
            goto L_0x02ee
        L_0x038b:
            char r3 = r0.charAt(r7)
            boolean r4 = java.lang.Character.isUpperCase(r3)
            if (r4 != 0) goto L_0x03c6
            r4 = 512(0x200, float:7.175E-43)
            if (r3 <= r4) goto L_0x039a
            goto L_0x03c6
        L_0x039a:
            r4 = 95
            if (r3 != r4) goto L_0x03a3
            java.lang.String r0 = r0.substring(r8)
            goto L_0x03ee
        L_0x03a3:
            r4 = 102(0x66, float:1.43E-43)
            if (r3 != r4) goto L_0x03ac
            java.lang.String r0 = r0.substring(r7)
            goto L_0x03ee
        L_0x03ac:
            int r3 = r0.length()
            r4 = 5
            if (r3 < r4) goto L_0x02ee
            char r3 = r0.charAt(r8)
            boolean r3 = java.lang.Character.isUpperCase(r3)
            if (r3 == 0) goto L_0x02ee
            java.lang.String r0 = r0.substring(r7)
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.decapitalize(r0)
            goto L_0x03ee
        L_0x03c6:
            boolean r3 = com.alibaba.fastjson.util.TypeUtils.compatibleWithJavaBean
            if (r3 == 0) goto L_0x03d3
            java.lang.String r0 = r0.substring(r7)
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.decapitalize(r0)
            goto L_0x03ee
        L_0x03d3:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            char r4 = r0.charAt(r7)
            char r4 = java.lang.Character.toLowerCase(r4)
            r3.append(r4)
            java.lang.String r0 = r0.substring(r8)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
        L_0x03ee:
            r3 = r0
            r14 = r22
            java.lang.reflect.Field r0 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r3, r14)
            r10 = 0
            if (r0 != 0) goto L_0x0422
            r1 = r1[r10]
            java.lang.Class r4 = java.lang.Boolean.TYPE
            if (r1 != r4) goto L_0x0422
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "is"
            r0.append(r1)
            char r1 = r3.charAt(r10)
            char r1 = java.lang.Character.toUpperCase(r1)
            r0.append(r1)
            java.lang.String r1 = r3.substring(r6)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.reflect.Field r0 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r0, r14)
        L_0x0422:
            r4 = r0
            if (r4 == 0) goto L_0x046b
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = r4.getAnnotation(r0)
            r9 = r0
            com.alibaba.fastjson.annotation.JSONField r9 = (com.alibaba.fastjson.annotation.JSONField) r9
            if (r9 == 0) goto L_0x0468
            int r6 = r9.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r0 = r9.serialzeFeatures()
            int r7 = com.alibaba.fastjson.serializer.SerializerFeature.of(r0)
            java.lang.String r0 = r9.name()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x0465
            java.lang.String r1 = r9.name()
            com.alibaba.fastjson.util.FieldInfo r8 = new com.alibaba.fastjson.util.FieldInfo
            r20 = 0
            r0 = r8
            r3 = r4
            r4 = r30
            r5 = r31
            r28 = r8
            r8 = r19
            r22 = 0
            r10 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r28
            add(r12, r0)
            goto L_0x0489
        L_0x0465:
            r22 = 0
            goto L_0x0473
        L_0x0468:
            r22 = 0
            goto L_0x046f
        L_0x046b:
            r22 = 0
            r9 = r17
        L_0x046f:
            r6 = r20
            r7 = r23
        L_0x0473:
            com.alibaba.fastjson.util.FieldInfo r10 = new com.alibaba.fastjson.util.FieldInfo
            r20 = 0
            r0 = r10
            r1 = r3
            r3 = r4
            r4 = r30
            r5 = r31
            r8 = r19
            r11 = r10
            r10 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            add(r12, r11)
        L_0x0489:
            int r9 = r26 + 1
            r11 = r30
            r22 = r14
            r10 = r24
            r14 = 0
            goto L_0x02d9
        L_0x0494:
            r22 = 0
            java.lang.reflect.Field[] r11 = r30.getFields()
            int r14 = r11.length
            r15 = 0
        L_0x049c:
            if (r15 >= r14) goto L_0x0531
            r3 = r11[r15]
            int r0 = r3.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)
            if (r0 == 0) goto L_0x04b1
        L_0x04aa:
            r17 = r11
            r11 = 3
            r23 = 1
            goto L_0x0528
        L_0x04b1:
            java.util.Iterator r0 = r12.iterator()
        L_0x04b5:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x04cf
            java.lang.Object r1 = r0.next()
            com.alibaba.fastjson.util.FieldInfo r1 = (com.alibaba.fastjson.util.FieldInfo) r1
            java.lang.String r1 = r1.name
            java.lang.String r2 = r3.getName()
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x04b5
            r0 = 1
            goto L_0x04d0
        L_0x04cf:
            r0 = 0
        L_0x04d0:
            if (r0 == 0) goto L_0x04d3
            goto L_0x04aa
        L_0x04d3:
            java.lang.String r0 = r3.getName()
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r3.getAnnotation(r1)
            r9 = r1
            com.alibaba.fastjson.annotation.JSONField r9 = (com.alibaba.fastjson.annotation.JSONField) r9
            if (r9 == 0) goto L_0x0501
            int r1 = r9.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r2 = r9.serialzeFeatures()
            int r2 = com.alibaba.fastjson.serializer.SerializerFeature.of(r2)
            java.lang.String r4 = r9.name()
            int r4 = r4.length()
            if (r4 == 0) goto L_0x04fc
            java.lang.String r0 = r9.name()
        L_0x04fc:
            r10 = r1
            r17 = r2
            r1 = r0
            goto L_0x0505
        L_0x0501:
            r1 = r0
            r10 = 0
            r17 = 0
        L_0x0505:
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r2 = 0
            r19 = 0
            r20 = 0
            r0 = r5
            r4 = r30
            r29 = r5
            r5 = r31
            r23 = 1
            r6 = r10
            r10 = 3
            r7 = r17
            r8 = r19
            r17 = r11
            r11 = 3
            r10 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r29
            add(r12, r0)
        L_0x0528:
            int r15 = r15 + 1
            r11 = r17
            r6 = 1
            r7 = 3
            r8 = 4
            goto L_0x049c
        L_0x0531:
            r11 = 3
            java.lang.reflect.Method[] r14 = r30.getMethods()
            int r15 = r14.length
            r10 = 0
        L_0x0538:
            if (r10 >= r15) goto L_0x0605
            r2 = r14[r10]
            java.lang.String r0 = r2.getName()
            int r1 = r0.length()
            r9 = 4
            if (r1 >= r9) goto L_0x054d
        L_0x0547:
            r19 = r10
            r17 = 4
            goto L_0x0600
        L_0x054d:
            int r1 = r2.getModifiers()
            boolean r1 = java.lang.reflect.Modifier.isStatic(r1)
            if (r1 == 0) goto L_0x0558
            goto L_0x0547
        L_0x0558:
            java.lang.String r1 = "get"
            boolean r1 = r0.startsWith(r1)
            if (r1 == 0) goto L_0x0547
            char r1 = r0.charAt(r11)
            boolean r1 = java.lang.Character.isUpperCase(r1)
            if (r1 == 0) goto L_0x0547
            java.lang.Class[] r1 = r2.getParameterTypes()
            int r1 = r1.length
            if (r1 == 0) goto L_0x0572
            goto L_0x0547
        L_0x0572:
            java.lang.Class<java.util.Collection> r1 = java.util.Collection.class
            java.lang.Class r3 = r2.getReturnType()
            boolean r1 = r1.isAssignableFrom(r3)
            if (r1 != 0) goto L_0x05a2
            java.lang.Class<java.util.Map> r1 = java.util.Map.class
            java.lang.Class r3 = r2.getReturnType()
            boolean r1 = r1.isAssignableFrom(r3)
            if (r1 != 0) goto L_0x05a2
            java.lang.Class<java.util.concurrent.atomic.AtomicBoolean> r1 = java.util.concurrent.atomic.AtomicBoolean.class
            java.lang.Class r3 = r2.getReturnType()
            if (r1 == r3) goto L_0x05a2
            java.lang.Class<java.util.concurrent.atomic.AtomicInteger> r1 = java.util.concurrent.atomic.AtomicInteger.class
            java.lang.Class r3 = r2.getReturnType()
            if (r1 == r3) goto L_0x05a2
            java.lang.Class<java.util.concurrent.atomic.AtomicLong> r1 = java.util.concurrent.atomic.AtomicLong.class
            java.lang.Class r3 = r2.getReturnType()
            if (r1 != r3) goto L_0x0547
        L_0x05a2:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r2.getAnnotation(r1)
            r8 = r1
            com.alibaba.fastjson.annotation.JSONField r8 = (com.alibaba.fastjson.annotation.JSONField) r8
            if (r8 == 0) goto L_0x05bc
            java.lang.String r1 = r8.name()
            int r1 = r1.length()
            if (r1 <= 0) goto L_0x05bc
            java.lang.String r0 = r8.name()
            goto L_0x05d7
        L_0x05bc:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            char r3 = r0.charAt(r11)
            char r3 = java.lang.Character.toLowerCase(r3)
            r1.append(r3)
            java.lang.String r0 = r0.substring(r9)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
        L_0x05d7:
            r1 = r0
            com.alibaba.fastjson.util.FieldInfo r0 = getField(r12, r1)
            if (r0 == 0) goto L_0x05e0
            goto L_0x0547
        L_0x05e0:
            com.alibaba.fastjson.util.FieldInfo r7 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r6 = 0
            r17 = 0
            r19 = 0
            r20 = 0
            r0 = r7
            r4 = r30
            r5 = r31
            r11 = r7
            r7 = r17
            r17 = 4
            r9 = r19
            r19 = r10
            r10 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            add(r12, r11)
        L_0x0600:
            int r10 = r19 + 1
            r11 = 3
            goto L_0x0538
        L_0x0605:
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r4 = 0
            r5 = 0
            r0 = r9
            r1 = r30
            r2 = r13
            r3 = r16
            r6 = r18
            r7 = r21
            r8 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.JavaBeanInfo.build(java.lang.Class, java.lang.reflect.Type):com.alibaba.fastjson.util.JavaBeanInfo");
    }

    static Constructor<?> getDefaultConstructor(Class<?> cls) {
        Constructor<?> constructor = null;
        if (Modifier.isAbstract(cls.getModifiers())) {
            return null;
        }
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        int length = declaredConstructors.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Constructor<?> constructor2 = declaredConstructors[i];
            if (constructor2.getParameterTypes().length == 0) {
                constructor = constructor2;
                break;
            }
            i++;
        }
        if (constructor != null || !cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) {
            return constructor;
        }
        for (Constructor<?> constructor3 : declaredConstructors) {
            Class[] parameterTypes = constructor3.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(cls.getDeclaringClass())) {
                return constructor3;
            }
        }
        return constructor;
    }

    public static Constructor<?> getCreatorConstructor(Class<?> cls) {
        Constructor<?> constructor = null;
        for (Constructor<?> constructor2 : cls.getDeclaredConstructors()) {
            if (((JSONCreator) constructor2.getAnnotation(JSONCreator.class)) != null) {
                if (constructor == null) {
                    constructor = constructor2;
                } else {
                    throw new JSONException("multi-JSONCreator");
                }
            }
        }
        return constructor;
    }

    private static Method getFactoryMethod(Class<?> cls, Method[] methodArr) {
        Method method = null;
        for (Method method2 : methodArr) {
            if (Modifier.isStatic(method2.getModifiers()) && cls.isAssignableFrom(method2.getReturnType()) && ((JSONCreator) method2.getAnnotation(JSONCreator.class)) != null) {
                if (method == null) {
                    method = method2;
                } else {
                    throw new JSONException("multi-JSONCreator");
                }
            }
        }
        return method;
    }

    public static Class<?> getBuilderClass(JSONType jSONType) {
        Class<?> builder;
        if (jSONType == null || (builder = jSONType.builder()) == Void.class) {
            return null;
        }
        return builder;
    }
}
