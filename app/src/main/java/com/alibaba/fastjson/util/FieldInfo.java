package com.alibaba.fastjson.util;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import kotlin.text.Typography;

public class FieldInfo implements Comparable<FieldInfo> {
    public final Class<?> declaringClass;
    public final Field field;
    public final boolean fieldAccess;
    private final JSONField fieldAnnotation;
    public final Class<?> fieldClass;
    public final boolean fieldTransient;
    public final Type fieldType;
    public final boolean getOnly;
    public final boolean isEnum;
    public final String label;
    public final Method method;
    private final JSONField methodAnnotation;
    public final String name;
    public final char[] name_chars;
    private int ordinal = 0;
    public final int serialzeFeatures;

    public FieldInfo(String str, Class<?> cls, Class<?> cls2, Type type, Field field2, int i, int i2) {
        this.name = str;
        this.declaringClass = cls;
        this.fieldClass = cls2;
        this.fieldType = type;
        this.method = null;
        this.field = field2;
        this.ordinal = i;
        this.serialzeFeatures = i2;
        this.isEnum = cls2.isEnum();
        if (field2 != null) {
            int modifiers = field2.getModifiers();
            this.fieldAccess = (modifiers & 1) != 0 || this.method == null;
            this.fieldTransient = Modifier.isTransient(modifiers);
        } else {
            this.fieldTransient = false;
            this.fieldAccess = false;
        }
        this.name_chars = genFieldNameChars();
        if (field2 != null) {
            TypeUtils.setAccessible(field2);
        }
        this.label = "";
        this.fieldAnnotation = null;
        this.methodAnnotation = null;
        this.getOnly = false;
    }

    public FieldInfo(String str, Method method2, Field field2, Class<?> cls, Type type, int i, int i2, JSONField jSONField, JSONField jSONField2, String str2) {
        Type type2;
        Class<?> cls2;
        Type type3;
        Type inheritGenericType;
        Type type4;
        boolean z = false;
        if (field2 != null) {
            String name2 = field2.getName();
            if (name2.equals(str)) {
                str = name2;
            }
        }
        this.name = str;
        this.method = method2;
        this.field = field2;
        this.ordinal = i;
        this.serialzeFeatures = i2;
        this.fieldAnnotation = jSONField;
        this.methodAnnotation = jSONField2;
        if (field2 != null) {
            int modifiers = field2.getModifiers();
            this.fieldAccess = (modifiers & 1) != 0 || method2 == null;
            this.fieldTransient = Modifier.isTransient(modifiers);
        } else {
            this.fieldAccess = false;
            this.fieldTransient = false;
        }
        if (str2 == null || str2.length() <= 0) {
            this.label = "";
        } else {
            this.label = str2;
        }
        this.name_chars = genFieldNameChars();
        if (method2 != null) {
            TypeUtils.setAccessible(method2);
        }
        if (field2 != null) {
            TypeUtils.setAccessible(field2);
        }
        if (method2 != null) {
            Class<?>[] parameterTypes = method2.getParameterTypes();
            if (parameterTypes.length == 1) {
                cls2 = parameterTypes[0];
                type4 = method2.getGenericParameterTypes()[0];
            } else {
                cls2 = method2.getReturnType();
                type4 = method2.getGenericReturnType();
                z = true;
            }
            this.declaringClass = method2.getDeclaringClass();
            type2 = type4;
        } else {
            cls2 = field2.getType();
            type2 = field2.getGenericType();
            this.declaringClass = field2.getDeclaringClass();
        }
        this.getOnly = z;
        if (cls == null || cls2 != Object.class || !(type2 instanceof TypeVariable) || (inheritGenericType = getInheritGenericType(cls, (TypeVariable) type2)) == null) {
            if (!(type2 instanceof Class)) {
                type3 = getFieldType(cls, type, type2);
                if (type3 != type2) {
                    if (type3 instanceof ParameterizedType) {
                        cls2 = TypeUtils.getClass(type3);
                    } else if (type3 instanceof Class) {
                        cls2 = TypeUtils.getClass(type3);
                    }
                }
            } else {
                type3 = type2;
            }
            this.fieldType = type3;
            this.fieldClass = cls2;
            this.isEnum = cls2.isEnum();
            return;
        }
        this.fieldClass = TypeUtils.getClass(inheritGenericType);
        this.fieldType = inheritGenericType;
        this.isEnum = cls2.isEnum();
    }

    /* access modifiers changed from: protected */
    public char[] genFieldNameChars() {
        int length = this.name.length();
        char[] cArr = new char[(length + 3)];
        String str = this.name;
        str.getChars(0, str.length(), cArr, 1);
        cArr[0] = Typography.quote;
        cArr[length + 1] = Typography.quote;
        cArr[length + 2] = ':';
        return cArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0014, code lost:
        r1 = r2.field;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T extends java.lang.annotation.Annotation> T getAnnation(java.lang.Class<T> r3) {
        /*
            r2 = this;
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            if (r3 != r0) goto L_0x0009
            com.alibaba.fastjson.annotation.JSONField r3 = r2.getAnnotation()
            return r3
        L_0x0009:
            r0 = 0
            java.lang.reflect.Method r1 = r2.method
            if (r1 == 0) goto L_0x0012
            java.lang.annotation.Annotation r0 = r1.getAnnotation(r3)
        L_0x0012:
            if (r0 != 0) goto L_0x001c
            java.lang.reflect.Field r1 = r2.field
            if (r1 != 0) goto L_0x001c
            java.lang.annotation.Annotation r0 = r1.getAnnotation(r3)
        L_0x001c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.FieldInfo.getAnnation(java.lang.Class):java.lang.annotation.Annotation");
    }

    public static Type getFieldType(Class<?> cls, Type type, Type type2) {
        if (!(cls == null || type == null)) {
            if (type2 instanceof GenericArrayType) {
                Type genericComponentType = ((GenericArrayType) type2).getGenericComponentType();
                Type fieldType2 = getFieldType(cls, type, genericComponentType);
                return genericComponentType != fieldType2 ? Array.newInstance(TypeUtils.getClass(fieldType2), 0).getClass() : type2;
            } else if (!TypeUtils.isGenericParamType(type)) {
                return type2;
            } else {
                if (type2 instanceof TypeVariable) {
                    ParameterizedType parameterizedType = (ParameterizedType) TypeUtils.getGenericParamType(type);
                    TypeVariable typeVariable = (TypeVariable) type2;
                    TypeVariable[] typeParameters = TypeUtils.getClass(parameterizedType).getTypeParameters();
                    for (int i = 0; i < typeParameters.length; i++) {
                        if (typeParameters[i].getName().equals(typeVariable.getName())) {
                            return parameterizedType.getActualTypeArguments()[i];
                        }
                    }
                }
                if (type2 instanceof ParameterizedType) {
                    ParameterizedType parameterizedType2 = (ParameterizedType) type2;
                    Type[] actualTypeArguments = parameterizedType2.getActualTypeArguments();
                    TypeVariable[] typeVariableArr = null;
                    Type[] typeArr = null;
                    boolean z = false;
                    for (int i2 = 0; i2 < actualTypeArguments.length; i2++) {
                        Type type3 = actualTypeArguments[i2];
                        if (type3 instanceof TypeVariable) {
                            TypeVariable typeVariable2 = (TypeVariable) type3;
                            if (type instanceof ParameterizedType) {
                                if (typeVariableArr == null) {
                                    typeVariableArr = cls.getTypeParameters();
                                }
                                Type[] typeArr2 = typeArr;
                                boolean z2 = z;
                                for (int i3 = 0; i3 < typeVariableArr.length; i3++) {
                                    if (typeVariableArr[i3].getName().equals(typeVariable2.getName())) {
                                        if (typeArr2 == null) {
                                            typeArr2 = ((ParameterizedType) type).getActualTypeArguments();
                                        }
                                        actualTypeArguments[i2] = typeArr2[i3];
                                        z2 = true;
                                    }
                                }
                                z = z2;
                                typeArr = typeArr2;
                            }
                        }
                    }
                    if (z) {
                        return new ParameterizedTypeImpl(actualTypeArguments, parameterizedType2.getOwnerType(), parameterizedType2.getRawType());
                    }
                }
            }
        }
        return type2;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [java.lang.reflect.TypeVariable, java.lang.reflect.TypeVariable<?>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.reflect.Type getInheritGenericType(java.lang.Class<?> r4, java.lang.reflect.TypeVariable<?> r5) {
        /*
            java.lang.reflect.GenericDeclaration r0 = r5.getGenericDeclaration()
        L_0x0004:
            java.lang.reflect.Type r4 = r4.getGenericSuperclass()
            r1 = 0
            if (r4 != 0) goto L_0x000c
            return r1
        L_0x000c:
            boolean r2 = r4 instanceof java.lang.reflect.ParameterizedType
            if (r2 == 0) goto L_0x0030
            r2 = r4
            java.lang.reflect.ParameterizedType r2 = (java.lang.reflect.ParameterizedType) r2
            java.lang.reflect.Type r3 = r2.getRawType()
            if (r3 != r0) goto L_0x0030
            java.lang.reflect.TypeVariable[] r4 = r0.getTypeParameters()
            java.lang.reflect.Type[] r0 = r2.getActualTypeArguments()
            r2 = 0
        L_0x0022:
            int r3 = r4.length
            if (r2 >= r3) goto L_0x002f
            r3 = r4[r2]
            if (r3 != r5) goto L_0x002c
            r4 = r0[r2]
            return r4
        L_0x002c:
            int r2 = r2 + 1
            goto L_0x0022
        L_0x002f:
            return r1
        L_0x0030:
            java.lang.Class r2 = com.alibaba.fastjson.util.TypeUtils.getClass(r4)
            if (r4 != 0) goto L_0x0037
            return r1
        L_0x0037:
            r4 = r2
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.FieldInfo.getInheritGenericType(java.lang.Class, java.lang.reflect.TypeVariable):java.lang.reflect.Type");
    }

    public String toString() {
        return this.name;
    }

    public Member getMember() {
        Method method2 = this.method;
        if (method2 != null) {
            return method2;
        }
        return this.field;
    }

    /* access modifiers changed from: protected */
    public Class<?> getDeclaredClass() {
        Method method2 = this.method;
        if (method2 != null) {
            return method2.getDeclaringClass();
        }
        Field field2 = this.field;
        if (field2 != null) {
            return field2.getDeclaringClass();
        }
        return null;
    }

    public int compareTo(FieldInfo fieldInfo) {
        int i = this.ordinal;
        int i2 = fieldInfo.ordinal;
        if (i < i2) {
            return -1;
        }
        if (i > i2) {
            return 1;
        }
        int compareTo = this.name.compareTo(fieldInfo.name);
        if (compareTo != 0) {
            return compareTo;
        }
        Class<?> declaredClass = getDeclaredClass();
        Class<?> declaredClass2 = fieldInfo.getDeclaredClass();
        if (!(declaredClass == null || declaredClass2 == null || declaredClass == declaredClass2)) {
            if (declaredClass.isAssignableFrom(declaredClass2)) {
                return -1;
            }
            if (declaredClass2.isAssignableFrom(declaredClass)) {
                return 1;
            }
        }
        Field field2 = this.field;
        boolean z = false;
        boolean z2 = field2 != null && field2.getType() == this.fieldClass;
        Field field3 = fieldInfo.field;
        if (field3 != null && field3.getType() == fieldInfo.fieldClass) {
            z = true;
        }
        if (z2 && !z) {
            return 1;
        }
        if (z && !z2) {
            return -1;
        }
        if (fieldInfo.fieldClass.isPrimitive() && !this.fieldClass.isPrimitive()) {
            return 1;
        }
        if (this.fieldClass.isPrimitive() && !fieldInfo.fieldClass.isPrimitive()) {
            return -1;
        }
        if (fieldInfo.fieldClass.getName().startsWith("java.") && !this.fieldClass.getName().startsWith("java.")) {
            return 1;
        }
        if (!this.fieldClass.getName().startsWith("java.") || fieldInfo.fieldClass.getName().startsWith("java.")) {
            return this.fieldClass.getName().compareTo(fieldInfo.fieldClass.getName());
        }
        return -1;
    }

    public JSONField getAnnotation() {
        JSONField jSONField = this.fieldAnnotation;
        if (jSONField != null) {
            return jSONField;
        }
        return this.methodAnnotation;
    }

    public String getFormat() {
        JSONField annotation = getAnnotation();
        if (annotation == null) {
            return null;
        }
        String format = annotation.format();
        if (format.trim().length() == 0) {
            return null;
        }
        return format;
    }

    public Object get(Object obj) throws IllegalAccessException, InvocationTargetException {
        Method method2 = this.method;
        if (method2 != null) {
            return method2.invoke(obj, new Object[0]);
        }
        return this.field.get(obj);
    }

    public void set(Object obj, Object obj2) throws IllegalAccessException, InvocationTargetException {
        Method method2 = this.method;
        if (method2 != null) {
            method2.invoke(obj, new Object[]{obj2});
            return;
        }
        this.field.set(obj, obj2);
    }

    public void setAccessible() throws SecurityException {
        Method method2 = this.method;
        if (method2 != null) {
            TypeUtils.setAccessible(method2);
        } else {
            TypeUtils.setAccessible(this.field);
        }
    }
}
