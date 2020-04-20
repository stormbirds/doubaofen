package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.asm.Type;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ASMSerializerFactory implements Opcodes {
    static final String JSONSerializer = ASMUtils.type(JSONSerializer.class);
    static final String JavaBeanSerializer = ASMUtils.type(JavaBeanSerializer.class);
    static final String JavaBeanSerializer_desc = ("L" + ASMUtils.type(JavaBeanSerializer.class) + ";");
    static final String SerialContext_desc = ASMUtils.desc((Class<?>) SerialContext.class);
    static final String SerializeWriter = ASMUtils.type(SerializeWriter.class);
    protected final ASMClassLoader classLoader = new ASMClassLoader();
    private final AtomicLong seed = new AtomicLong();

    static class Context {
        static final int features = 5;
        static int fieldName = 6;
        static final int obj = 2;
        static int original = 7;
        static final int paramFieldName = 3;
        static final int paramFieldType = 4;
        static int processValue = 8;
        static final int serializer = 1;
        /* access modifiers changed from: private */
        public final int beanSerializeFeatures;
        /* access modifiers changed from: private */
        public final String className;
        /* access modifiers changed from: private */
        public int variantIndex = 9;
        private Map<String, Integer> variants = new HashMap();
        /* access modifiers changed from: private */
        public final boolean writeDirect;

        public Context(String str, int i, boolean z) {
            this.className = str;
            this.beanSerializeFeatures = i;
            this.writeDirect = z;
            if (this.writeDirect) {
                processValue = 8;
            }
        }

        public int var(String str) {
            if (this.variants.get(str) == null) {
                Map<String, Integer> map = this.variants;
                int i = this.variantIndex;
                this.variantIndex = i + 1;
                map.put(str, Integer.valueOf(i));
            }
            return this.variants.get(str).intValue();
        }

        public int var(String str, int i) {
            if (this.variants.get(str) == null) {
                this.variants.put(str, Integer.valueOf(this.variantIndex));
                this.variantIndex += i;
            }
            return this.variants.get(str).intValue();
        }
    }

    public ObjectSerializer createJavaBeanSerializer(Class<?> cls, Map<String, String> map) throws Exception {
        List<FieldInfo> list;
        boolean z;
        String str;
        List<FieldInfo> list2;
        int i;
        int i2;
        Class<?> cls2;
        String str2;
        String str3;
        boolean z2;
        int i3;
        ASMSerializerFactory aSMSerializerFactory = this;
        Class<?> cls3 = cls;
        Map<String, String> map2 = map;
        if (!cls.isPrimitive()) {
            JSONType jSONType = (JSONType) cls3.getAnnotation(JSONType.class);
            int i4 = 0;
            List<FieldInfo> computeGetters = TypeUtils.computeGetters(cls3, jSONType, map2, false);
            for (FieldInfo next : computeGetters) {
                if (next.field == null && next.method != null && next.method.getDeclaringClass().isInterface()) {
                    return new JavaBeanSerializer(cls3);
                }
            }
            String[] orders = jSONType != null ? jSONType.orders() : null;
            if (orders == null || orders.length == 0) {
                list = new ArrayList<>(computeGetters);
                Collections.sort(list);
            } else {
                list = TypeUtils.computeGetters(cls3, jSONType, map2, true);
            }
            int size = computeGetters.size();
            int i5 = 0;
            while (true) {
                if (i5 >= size) {
                    z = true;
                    break;
                } else if (!computeGetters.get(i5).equals(list.get(i5))) {
                    z = false;
                    break;
                } else {
                    i5++;
                }
            }
            if (list.size() > 256) {
                return null;
            }
            for (FieldInfo member : list) {
                if (!ASMUtils.checkName(member.getMember().getName())) {
                    return null;
                }
            }
            String str4 = "ASMSerializer_" + aSMSerializerFactory.seed.incrementAndGet() + "_" + cls.getSimpleName();
            String name = ASMSerializerFactory.class.getPackage().getName();
            String str5 = name.replace('.', '/') + "/" + str4;
            String str6 = name + "." + str4;
            int serializeFeatures = TypeUtils.getSerializeFeatures(cls);
            ClassWriter classWriter = new ClassWriter();
            classWriter.visit(49, 33, str5, ASMUtils.type(ASMJavaBeanSerializer.class), new String[]{ASMUtils.type(ObjectSerializer.class)});
            for (FieldInfo next2 : list) {
                if (!next2.fieldClass.isPrimitive() && !next2.fieldClass.isEnum() && next2.fieldClass != String.class) {
                    new FieldWriter(classWriter, 1, next2.name + "_asm_fieldType", "Ljava/lang/reflect/Type;").visitEnd();
                }
            }
            ClassWriter classWriter2 = classWriter;
            MethodWriter methodWriter = r11;
            MethodWriter methodWriter2 = new MethodWriter(classWriter2, 1, "<init>", "()V", (String) null, (String[]) null);
            int i6 = 25;
            methodWriter.visitVarInsn(25, 0);
            methodWriter.visitLdcInsn(Type.getType(ASMUtils.desc(cls)));
            methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(ASMJavaBeanSerializer.class), "<init>", "(Ljava/lang/Class;)V");
            for (FieldInfo next3 : list) {
                if (!next3.fieldClass.isPrimitive() && !next3.fieldClass.isEnum() && next3.fieldClass != String.class) {
                    methodWriter.visitVarInsn(i6, i4);
                    methodWriter.visitLdcInsn(Type.getType(ASMUtils.desc(next3.declaringClass)));
                    if (next3.method != null) {
                        methodWriter.visitLdcInsn(next3.method.getName());
                        methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, ASMUtils.type(ASMUtils.class), "getMethodType", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");
                    } else {
                        methodWriter.visitLdcInsn(next3.field.getName());
                        methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, ASMUtils.type(ASMUtils.class), "getFieldType", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");
                    }
                    methodWriter.visitFieldInsn(Opcodes.PUTFIELD, str5, next3.name + "_asm_fieldType", "Ljava/lang/reflect/Type;");
                    i4 = 0;
                    i6 = 25;
                }
            }
            methodWriter.visitInsn(Opcodes.RETURN);
            methodWriter.visitMaxs(4, 4);
            methodWriter.visitEnd();
            int i7 = 0;
            while (true) {
                str = "getWriter";
                if (i7 >= 2) {
                    break;
                }
                if (i7 == 0) {
                    str3 = "write";
                    str2 = str6;
                    z2 = true;
                } else {
                    str3 = "write1";
                    str2 = str6;
                    z2 = false;
                }
                Context context = new Context(str5, serializeFeatures, z2);
                List<FieldInfo> list3 = computeGetters;
                String str7 = "out";
                int i8 = serializeFeatures;
                String str8 = ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V";
                int i9 = i7;
                MethodWriter methodWriter3 = r11;
                MethodWriter methodWriter4 = new MethodWriter(classWriter2, 1, str3, "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V", (String) null, new String[]{"java/io/IOException"});
                methodWriter3.visitVarInsn(25, 1);
                methodWriter3.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, str, "()L" + SerializeWriter + ";");
                methodWriter3.visitVarInsn(58, context.var(str7));
                if (z || (jSONType != null && !jSONType.alphabetic())) {
                    i3 = 2;
                } else {
                    Label label = new Label();
                    methodWriter3.visitVarInsn(25, context.var(str7));
                    methodWriter3.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "isSortField", "()Z");
                    methodWriter3.visitJumpInsn(Opcodes.IFNE, label);
                    methodWriter3.visitVarInsn(25, 0);
                    methodWriter3.visitVarInsn(25, 1);
                    i3 = 2;
                    methodWriter3.visitVarInsn(25, 2);
                    methodWriter3.visitVarInsn(25, 3);
                    methodWriter3.visitVarInsn(25, 4);
                    methodWriter3.visitVarInsn(21, 5);
                    methodWriter3.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str5, "writeUnsorted", "(L" + JSONSerializer + str8);
                    methodWriter3.visitInsn(Opcodes.RETURN);
                    methodWriter3.visitLabel(label);
                }
                if (context.writeDirect) {
                    Label label2 = new Label();
                    methodWriter3.visitVarInsn(25, 1);
                    methodWriter3.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeDirect", "()Z");
                    methodWriter3.visitJumpInsn(Opcodes.IFNE, label2);
                    methodWriter3.visitVarInsn(25, 0);
                    methodWriter3.visitVarInsn(25, 1);
                    methodWriter3.visitVarInsn(25, i3);
                    methodWriter3.visitVarInsn(25, 3);
                    methodWriter3.visitVarInsn(25, 4);
                    methodWriter3.visitVarInsn(21, 5);
                    methodWriter3.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str5, "write1", "(L" + JSONSerializer + str8);
                    methodWriter3.visitInsn(Opcodes.RETURN);
                    methodWriter3.visitLabel(label2);
                }
                methodWriter3.visitVarInsn(25, i3);
                methodWriter3.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
                methodWriter3.visitVarInsn(58, context.var("entity"));
                aSMSerializerFactory = this;
                Class<?> cls4 = cls;
                aSMSerializerFactory.generateWriteMethod(cls4, methodWriter3, list, context);
                methodWriter3.visitInsn(Opcodes.RETURN);
                methodWriter3.visitMaxs(7, context.variantIndex + i3);
                methodWriter3.visitEnd();
                i7 = i9 + 1;
                cls3 = cls4;
                str6 = str2;
                serializeFeatures = i8;
                computeGetters = list3;
            }
            Class<?> cls5 = cls3;
            List<FieldInfo> list4 = computeGetters;
            String str9 = str6;
            int i10 = serializeFeatures;
            String str10 = "out";
            String str11 = ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V";
            String str12 = "()L";
            String str13 = str;
            if (!z) {
                i2 = i10;
                Context context2 = new Context(str5, i2, false);
                MethodWriter methodWriter5 = r11;
                list2 = list;
                cls2 = cls5;
                MethodWriter methodWriter6 = new MethodWriter(classWriter2, 1, "writeUnsorted", "(L" + JSONSerializer + str11, (String) null, new String[]{"java/io/IOException"});
                methodWriter5.visitVarInsn(25, 1);
                methodWriter5.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, str13, str12 + SerializeWriter + ";");
                methodWriter5.visitVarInsn(58, context2.var(str10));
                i = 2;
                methodWriter5.visitVarInsn(25, 2);
                methodWriter5.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
                methodWriter5.visitVarInsn(58, context2.var("entity"));
                aSMSerializerFactory.generateWriteMethod(cls2, methodWriter5, list4, context2);
                methodWriter5.visitInsn(Opcodes.RETURN);
                methodWriter5.visitMaxs(7, context2.variantIndex + 2);
                methodWriter5.visitEnd();
            } else {
                list2 = list;
                cls2 = cls5;
                i2 = i10;
                i = 2;
            }
            Context context3 = new Context(str5, i2, false);
            MethodWriter methodWriter7 = new MethodWriter(classWriter2, 1, "writeAsArray", "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V", (String) null, new String[]{"java/io/IOException"});
            methodWriter7.visitVarInsn(25, 1);
            methodWriter7.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, str13, str12 + SerializeWriter + ";");
            methodWriter7.visitVarInsn(58, context3.var(str10));
            methodWriter7.visitVarInsn(25, i);
            methodWriter7.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
            methodWriter7.visitVarInsn(58, context3.var("entity"));
            aSMSerializerFactory.generateWriteAsArray(cls2, methodWriter7, list2, context3);
            methodWriter7.visitInsn(Opcodes.RETURN);
            methodWriter7.visitMaxs(7, context3.variantIndex + i);
            methodWriter7.visitEnd();
            byte[] byteArray = classWriter2.toByteArray();
            return (ObjectSerializer) aSMSerializerFactory.classLoader.defineClassPublic(str9, byteArray, 0, byteArray.length).newInstance();
        }
        Class<?> cls6 = cls3;
        throw new JSONException("unsupportd class " + cls.getName());
    }

    private void generateWriteAsArray(Class<?> cls, MethodVisitor methodVisitor, List<FieldInfo> list, Context context) throws Exception {
        int i;
        List<FieldInfo> list2;
        MethodVisitor methodVisitor2 = methodVisitor;
        Context context2 = context;
        int i2 = 25;
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        int i3 = 16;
        methodVisitor2.visitVarInsn(16, 91);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        int size = list.size();
        if (size == 0) {
            methodVisitor2.visitVarInsn(25, context2.var("out"));
            methodVisitor2.visitVarInsn(16, 93);
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
            return;
        }
        for (int i4 = 0; i4 < size; i4++) {
            if (i4 == size - 1) {
                list2 = list;
                i = 93;
            } else {
                list2 = list;
                i = 44;
            }
            FieldInfo fieldInfo = list2.get(i4);
            Class<?> cls2 = fieldInfo.fieldClass;
            methodVisitor2.visitLdcInsn(fieldInfo.name);
            methodVisitor2.visitVarInsn(58, Context.fieldName);
            if (cls2 == Byte.TYPE || cls2 == Short.TYPE || cls2 == Integer.TYPE) {
                methodVisitor2.visitVarInsn(i2, context2.var("out"));
                _get(methodVisitor2, context2, fieldInfo);
                methodVisitor2.visitVarInsn(i3, i);
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeIntAndChar", "(IC)V");
            } else if (cls2 == Long.TYPE) {
                methodVisitor2.visitVarInsn(i2, context2.var("out"));
                _get(methodVisitor2, context2, fieldInfo);
                methodVisitor2.visitVarInsn(i3, i);
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeLongAndChar", "(JC)V");
            } else {
                int i5 = Opcodes.INVOKEVIRTUAL;
                if (cls2 == Float.TYPE) {
                    methodVisitor2.visitVarInsn(i2, context2.var("out"));
                    _get(methodVisitor2, context2, fieldInfo);
                    methodVisitor2.visitVarInsn(i3, i);
                    methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFloatAndChar", "(FC)V");
                } else if (cls2 == Double.TYPE) {
                    methodVisitor2.visitVarInsn(i2, context2.var("out"));
                    _get(methodVisitor2, context2, fieldInfo);
                    methodVisitor2.visitVarInsn(i3, i);
                    methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeDoubleAndChar", "(DC)V");
                } else if (cls2 == Boolean.TYPE) {
                    methodVisitor2.visitVarInsn(i2, context2.var("out"));
                    _get(methodVisitor2, context2, fieldInfo);
                    methodVisitor2.visitVarInsn(i3, i);
                    methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeBooleanAndChar", "(ZC)V");
                } else if (cls2 == Character.TYPE) {
                    methodVisitor2.visitVarInsn(i2, context2.var("out"));
                    _get(methodVisitor2, context2, fieldInfo);
                    methodVisitor2.visitVarInsn(i3, i);
                    methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeCharacterAndChar", "(CC)V");
                } else if (cls2 == String.class) {
                    methodVisitor2.visitVarInsn(i2, context2.var("out"));
                    _get(methodVisitor2, context2, fieldInfo);
                    methodVisitor2.visitVarInsn(i3, i);
                    methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeString", "(Ljava/lang/String;C)V");
                } else if (cls2.isEnum()) {
                    methodVisitor2.visitVarInsn(i2, context2.var("out"));
                    _get(methodVisitor2, context2, fieldInfo);
                    methodVisitor2.visitVarInsn(i3, i);
                    methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeEnum", "(Ljava/lang/Enum;C)V");
                } else {
                    String format = fieldInfo.getFormat();
                    methodVisitor2.visitVarInsn(i2, 1);
                    _get(methodVisitor2, context2, fieldInfo);
                    if (format != null) {
                        methodVisitor2.visitLdcInsn(format);
                        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFormat", "(Ljava/lang/Object;Ljava/lang/String;)V");
                    } else {
                        methodVisitor2.visitVarInsn(i2, Context.fieldName);
                        if (!(fieldInfo.fieldType instanceof Class) || !((Class) fieldInfo.fieldType).isPrimitive()) {
                            methodVisitor2.visitVarInsn(i2, 0);
                            String access$200 = context.className;
                            methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, access$200, fieldInfo.name + "_asm_fieldType", "Ljava/lang/reflect/Type;");
                            methodVisitor2.visitLdcInsn(Integer.valueOf(fieldInfo.serialzeFeatures));
                            String str = JSONSerializer;
                            i5 = Opcodes.INVOKEVIRTUAL;
                            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
                        } else {
                            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
                            i5 = Opcodes.INVOKEVIRTUAL;
                        }
                    }
                    i2 = 25;
                    methodVisitor2.visitVarInsn(25, context2.var("out"));
                    i3 = 16;
                    methodVisitor2.visitVarInsn(16, i);
                    methodVisitor2.visitMethodInsn(i5, SerializeWriter, "write", "(I)V");
                }
            }
        }
    }

    private void generateWriteMethod(Class<?> cls, MethodVisitor methodVisitor, List<FieldInfo> list, Context context) throws Exception {
        Class<?> cls2 = cls;
        MethodVisitor methodVisitor2 = methodVisitor;
        Context context2 = context;
        Label label = new Label();
        int size = list.size();
        Label label2 = new Label();
        Label label3 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "isPrettyFormat", "()Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label2);
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, context.className, "nature", JavaBeanSerializer_desc);
        methodVisitor2.visitJumpInsn(Opcodes.IFNONNULL, label3);
        methodVisitor2.visitLabel(label3);
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, context.className, "nature", JavaBeanSerializer_desc);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, 2);
        methodVisitor2.visitVarInsn(25, 3);
        methodVisitor2.visitVarInsn(25, 4);
        methodVisitor2.visitVarInsn(21, 5);
        String str = JavaBeanSerializer;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "write", "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
        methodVisitor2.visitInsn(Opcodes.RETURN);
        methodVisitor2.visitLabel(label2);
        Label label4 = new Label();
        Label label5 = new Label();
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, context.className, "nature", JavaBeanSerializer_desc);
        methodVisitor2.visitJumpInsn(Opcodes.IFNONNULL, label5);
        methodVisitor2.visitLabel(label5);
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, context.className, "nature", JavaBeanSerializer_desc);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, 2);
        methodVisitor2.visitVarInsn(21, 5);
        String str2 = JavaBeanSerializer;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str2, "writeReference", "(L" + JSONSerializer + ";Ljava/lang/Object;I)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label4);
        methodVisitor2.visitInsn(Opcodes.RETURN);
        methodVisitor2.visitLabel(label4);
        Label label6 = new Label();
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, context.className, "nature", JavaBeanSerializer_desc);
        methodVisitor2.visitVarInsn(25, 1);
        String str3 = JavaBeanSerializer;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str3, "isWriteAsArray", "(L" + JSONSerializer + ";)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label6);
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, 2);
        methodVisitor2.visitVarInsn(25, 3);
        methodVisitor2.visitVarInsn(25, 4);
        String access$200 = context.className;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, access$200, "writeAsArray", "(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
        methodVisitor2.visitInsn(Opcodes.RETURN);
        methodVisitor2.visitLabel(label6);
        methodVisitor2.visitVarInsn(25, 1);
        String str4 = JSONSerializer;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, "getContext", "()" + SerialContext_desc);
        methodVisitor2.visitVarInsn(58, context2.var("parent"));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var("parent"));
        methodVisitor2.visitVarInsn(25, 2);
        methodVisitor2.visitVarInsn(25, 3);
        methodVisitor2.visitLdcInsn(Integer.valueOf(context.beanSerializeFeatures));
        String str5 = JSONSerializer;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str5, "setContext", "(" + SerialContext_desc + "Ljava/lang/Object;Ljava/lang/Object;I)V");
        Label label7 = new Label();
        Label label8 = new Label();
        Label label9 = new Label();
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, 4);
        methodVisitor2.visitVarInsn(25, 2);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "isWriteClassName", "(Ljava/lang/reflect/Type;Ljava/lang/Object;)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label8);
        methodVisitor2.visitVarInsn(25, 4);
        methodVisitor2.visitVarInsn(25, 2);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
        methodVisitor2.visitJumpInsn(Opcodes.IF_ACMPEQ, label8);
        methodVisitor2.visitLabel(label9);
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitLdcInsn("{\"" + JSON.DEFAULT_TYPE_KEY + "\":\"" + cls.getName() + "\"");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(Ljava/lang/String;)V");
        methodVisitor2.visitVarInsn(16, 44);
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label7);
        methodVisitor2.visitLabel(label8);
        methodVisitor2.visitVarInsn(16, 123);
        methodVisitor2.visitLabel(label7);
        methodVisitor2.visitVarInsn(54, context2.var("seperator"));
        if (!context.writeDirect) {
            _before(methodVisitor2, context2);
        }
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "isNotWriteDefaultValue", "()Z");
        methodVisitor2.visitVarInsn(54, context2.var("notWriteDefaultValue"));
        if (!context.writeDirect) {
            methodVisitor2.visitVarInsn(25, 1);
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "checkValue", "()Z");
            methodVisitor2.visitVarInsn(54, context2.var("checkValue"));
            methodVisitor2.visitVarInsn(25, 1);
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "hasNameFilters", "()Z");
            methodVisitor2.visitVarInsn(54, context2.var("hasNameFilters"));
        }
        for (int i = 0; i < size; i++) {
            FieldInfo fieldInfo = list.get(i);
            Class<?> cls3 = fieldInfo.fieldClass;
            methodVisitor2.visitLdcInsn(fieldInfo.name);
            methodVisitor2.visitVarInsn(58, Context.fieldName);
            if (cls3 == Byte.TYPE) {
                _byte(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Short.TYPE) {
                _short(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Integer.TYPE) {
                _int(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Long.TYPE) {
                _long(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Float.TYPE) {
                _float(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Double.TYPE) {
                _double(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Boolean.TYPE) {
                _boolean(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == Character.TYPE) {
                _char(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == String.class) {
                _string(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3 == BigDecimal.class) {
                _decimal(cls2, methodVisitor2, fieldInfo, context2);
            } else if (List.class.isAssignableFrom(cls3)) {
                _list(cls2, methodVisitor2, fieldInfo, context2);
            } else if (cls3.isEnum()) {
                _enum(cls2, methodVisitor2, fieldInfo, context2);
            } else {
                _object(cls2, methodVisitor2, fieldInfo, context2);
            }
        }
        if (!context.writeDirect) {
            _after(methodVisitor2, context2);
        }
        Label label10 = new Label();
        Label label11 = new Label();
        methodVisitor2.visitVarInsn(21, context2.var("seperator"));
        methodVisitor2.visitIntInsn(16, 123);
        methodVisitor2.visitJumpInsn(160, label10);
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitVarInsn(16, 123);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        methodVisitor2.visitLabel(label10);
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitVarInsn(16, 125);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        methodVisitor2.visitLabel(label11);
        methodVisitor2.visitLabel(label);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var("parent"));
        String str6 = JSONSerializer;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str6, "setContext", "(" + SerialContext_desc + ")V");
    }

    private void _object(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(58, context.var("object"));
        _filters(methodVisitor, fieldInfo, context, label);
        _writeObject(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitLabel(label);
    }

    private void _enum(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        JSONField annotation = fieldInfo.getAnnotation();
        int i = 0;
        if (annotation != null) {
            SerializerFeature[] serialzeFeatures = annotation.serialzeFeatures();
            int length = serialzeFeatures.length;
            int i2 = 0;
            while (i < length) {
                if (serialzeFeatures[i] == SerializerFeature.WriteEnumUsingToString) {
                    i2 = 1;
                }
                i++;
            }
            i = i2;
        }
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label3);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Enum");
        methodVisitor.visitVarInsn(58, context.var("enum"));
        _filters(methodVisitor, fieldInfo, context, label3);
        methodVisitor.visitVarInsn(25, context.var("enum"));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label);
        _if_write_null(methodVisitor, fieldInfo, context);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label2);
        methodVisitor.visitLabel(label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(25, context.var("enum"));
        if (i != 0) {
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Ljava/lang/String;)V");
        } else if (context.writeDirect) {
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Enum", "name", "()Ljava/lang/String;");
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValueStringWithDoubleQuote", "(CLjava/lang/String;Ljava/lang/String;)V");
        } else {
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Ljava/lang/Enum;)V");
        }
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLabel(label3);
    }

    private void _long(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(55, context.var("long", 2));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(22, context.var("long", 2));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;J)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _float(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(56, context.var("float"));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(23, context.var("float"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;F)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _double(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(57, context.var("double", 2));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(24, context.var("double", 2));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;D)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _char(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(54, context.var("char"));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(21, context.var("char"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;C)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _boolean(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(54, context.var("boolean"));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(21, context.var("boolean"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Z)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _get(MethodVisitor methodVisitor, Context context, FieldInfo fieldInfo) {
        Method method = fieldInfo.method;
        if (method != null) {
            methodVisitor.visitVarInsn(25, context.var("entity"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(method.getDeclaringClass()), method.getName(), ASMUtils.desc(method));
            return;
        }
        methodVisitor.visitVarInsn(25, context.var("entity"));
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
    }

    private void _byte(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(54, context.var("byte"));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(21, context.var("byte"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _short(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(54, context.var("short"));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(21, context.var("short"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _int(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(54, context.var("int"));
        _filters(methodVisitor, fieldInfo, context, label);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(21, context.var("int"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label);
    }

    private void _decimal(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(58, context.var("decimal"));
        _filters(methodVisitor, fieldInfo, context, label);
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitVarInsn(25, context.var("decimal"));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label3);
        _if_write_null(methodVisitor, fieldInfo, context);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label4);
        methodVisitor.visitLabel(label3);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitVarInsn(25, context.var("decimal"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Ljava/math/BigDecimal;)V");
        _seperator(methodVisitor, context);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label4);
        methodVisitor.visitLabel(label4);
        methodVisitor.visitLabel(label);
    }

    private void _string(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        _nameApply(methodVisitor, fieldInfo, context, label);
        _get(methodVisitor, context, fieldInfo);
        methodVisitor.visitVarInsn(58, context.var("string"));
        _filters(methodVisitor, fieldInfo, context, label);
        Label label2 = new Label();
        Label label3 = new Label();
        methodVisitor.visitVarInsn(25, context.var("string"));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label2);
        _if_write_null(methodVisitor, fieldInfo, context);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label3);
        methodVisitor.visitLabel(label2);
        if (context.writeDirect) {
            methodVisitor.visitVarInsn(25, context.var("out"));
            methodVisitor.visitVarInsn(21, context.var("seperator"));
            methodVisitor.visitVarInsn(25, Context.fieldName);
            methodVisitor.visitVarInsn(25, context.var("string"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValueStringWithDoubleQuoteCheck", "(CLjava/lang/String;Ljava/lang/String;)V");
        } else {
            methodVisitor.visitVarInsn(25, context.var("out"));
            methodVisitor.visitVarInsn(21, context.var("seperator"));
            methodVisitor.visitVarInsn(25, Context.fieldName);
            methodVisitor.visitVarInsn(25, context.var("string"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Ljava/lang/String;)V");
        }
        _seperator(methodVisitor, context);
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLabel(label);
    }

    private void _list(Class<?> cls, MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        java.lang.reflect.Type type;
        Label label;
        Label label2;
        String str;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        MethodVisitor methodVisitor2 = methodVisitor;
        FieldInfo fieldInfo2 = fieldInfo;
        Context context2 = context;
        java.lang.reflect.Type type2 = fieldInfo2.fieldType;
        if (type2 instanceof Class) {
            type = Object.class;
        } else {
            type = ((ParameterizedType) type2).getActualTypeArguments()[0];
        }
        Class cls2 = null;
        if (type instanceof Class) {
            cls2 = (Class) type;
        }
        Label label3 = new Label();
        Label label4 = new Label();
        Label label5 = new Label();
        Label label6 = new Label();
        methodVisitor2.visitLabel(label4);
        _nameApply(methodVisitor2, fieldInfo2, context2, label3);
        _get(methodVisitor2, context2, fieldInfo2);
        methodVisitor2.visitTypeInsn(Opcodes.CHECKCAST, "java/util/List");
        methodVisitor2.visitVarInsn(58, context2.var("list"));
        _filters(methodVisitor2, fieldInfo2, context2, label3);
        methodVisitor2.visitVarInsn(25, context2.var("list"));
        methodVisitor2.visitJumpInsn(Opcodes.IFNONNULL, label5);
        _if_write_null(methodVisitor2, fieldInfo2, context2);
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label6);
        methodVisitor2.visitLabel(label5);
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitVarInsn(21, context2.var("seperator"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitVarInsn(25, Context.fieldName);
        methodVisitor2.visitInsn(3);
        Label label7 = label3;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldName", "(Ljava/lang/String;Z)V");
        methodVisitor2.visitVarInsn(25, context2.var("list"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I");
        methodVisitor2.visitVarInsn(54, context2.var("int"));
        Label label8 = new Label();
        Label label9 = new Label();
        Label label10 = new Label();
        methodVisitor2.visitLabel(label8);
        Label label11 = label6;
        methodVisitor2.visitVarInsn(21, context2.var("int"));
        methodVisitor2.visitInsn(3);
        methodVisitor2.visitJumpInsn(160, label9);
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitLdcInsn("[]");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(Ljava/lang/String;)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label10);
        methodVisitor2.visitLabel(label9);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var("list"));
        methodVisitor2.visitVarInsn(25, Context.fieldName);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)V");
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitVarInsn(16, 91);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        methodVisitor2.visitInsn(1);
        methodVisitor2.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(ObjectSerializer.class));
        methodVisitor2.visitVarInsn(58, context2.var("list_ser"));
        Label label12 = new Label();
        Label label13 = new Label();
        methodVisitor2.visitInsn(3);
        Label label14 = label10;
        methodVisitor2.visitVarInsn(54, context2.var("i"));
        methodVisitor2.visitLabel(label12);
        methodVisitor2.visitVarInsn(21, context2.var("i"));
        methodVisitor2.visitVarInsn(21, context2.var("int"));
        methodVisitor2.visitInsn(4);
        methodVisitor2.visitInsn(100);
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPGE, label13);
        if (type == String.class) {
            str = "int";
            methodVisitor2.visitVarInsn(25, context2.var("out"));
            methodVisitor2.visitVarInsn(25, context2.var("list"));
            methodVisitor2.visitVarInsn(21, context2.var("i"));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
            methodVisitor2.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/String");
            methodVisitor2.visitVarInsn(16, 44);
            if (context.writeDirect) {
                label2 = label13;
                label = label12;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeStringWithDoubleQuoteDirect", "(Ljava/lang/String;C)V");
            } else {
                label = label12;
                label2 = label13;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeString", "(Ljava/lang/String;C)V");
            }
        } else {
            label = label12;
            label2 = label13;
            str = "int";
            methodVisitor2.visitVarInsn(25, 1);
            methodVisitor2.visitVarInsn(25, context2.var("list"));
            methodVisitor2.visitVarInsn(21, context2.var("i"));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
            methodVisitor2.visitVarInsn(21, context2.var("i"));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            if (cls2 == null || !Modifier.isPublic(cls2.getModifiers())) {
                i5 = Opcodes.INVOKEVIRTUAL;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
            } else {
                methodVisitor2.visitLdcInsn(Type.getType(ASMUtils.desc((Class<?>) (Class) type)));
                methodVisitor2.visitLdcInsn(Integer.valueOf(fieldInfo2.serialzeFeatures));
                String str2 = JSONSerializer;
                i5 = Opcodes.INVOKEVIRTUAL;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str2, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            }
            methodVisitor2.visitVarInsn(25, context2.var("out"));
            methodVisitor2.visitVarInsn(16, 44);
            methodVisitor2.visitMethodInsn(i5, SerializeWriter, "write", "(I)V");
        }
        methodVisitor2.visitIincInsn(context2.var("i"), 1);
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label);
        methodVisitor2.visitLabel(label2);
        if (type == String.class) {
            methodVisitor2.visitVarInsn(25, context2.var("out"));
            methodVisitor2.visitVarInsn(25, context2.var("list"));
            methodVisitor2.visitVarInsn(21, context2.var(str));
            methodVisitor2.visitInsn(4);
            methodVisitor2.visitInsn(100);
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
            methodVisitor2.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/String");
            methodVisitor2.visitVarInsn(16, 93);
            if (context.writeDirect) {
                String str3 = SerializeWriter;
                i = Opcodes.INVOKEVIRTUAL;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str3, "writeStringWithDoubleQuoteDirect", "(Ljava/lang/String;C)V");
            } else {
                i = Opcodes.INVOKEVIRTUAL;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeString", "(Ljava/lang/String;C)V");
            }
            i3 = 1;
            i2 = 25;
        } else {
            methodVisitor2.visitVarInsn(25, 1);
            methodVisitor2.visitVarInsn(25, context2.var("list"));
            methodVisitor2.visitVarInsn(21, context2.var("i"));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;");
            methodVisitor2.visitVarInsn(21, context2.var("i"));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            if (cls2 == null || !Modifier.isPublic(cls2.getModifiers())) {
                i4 = Opcodes.INVOKEVIRTUAL;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
            } else {
                methodVisitor2.visitLdcInsn(Type.getType(ASMUtils.desc((Class<?>) (Class) type)));
                methodVisitor2.visitLdcInsn(Integer.valueOf(fieldInfo2.serialzeFeatures));
                String str4 = JSONSerializer;
                i4 = Opcodes.INVOKEVIRTUAL;
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str4, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            }
            i2 = 25;
            methodVisitor2.visitVarInsn(25, context2.var("out"));
            methodVisitor2.visitVarInsn(16, 93);
            methodVisitor2.visitMethodInsn(i, SerializeWriter, "write", "(I)V");
            i3 = 1;
        }
        methodVisitor2.visitVarInsn(i2, i3);
        methodVisitor2.visitMethodInsn(i, JSONSerializer, "popContext", "()V");
        methodVisitor2.visitLabel(label14);
        _seperator(methodVisitor2, context2);
        methodVisitor2.visitLabel(label11);
        methodVisitor2.visitLabel(label7);
    }

    private void _filters(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context, Label label) {
        if (fieldInfo.field != null && Modifier.isTransient(fieldInfo.field.getModifiers())) {
            methodVisitor.visitVarInsn(25, context.var("out"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "isSkipTransientField", "()Z");
            methodVisitor.visitJumpInsn(Opcodes.IFNE, label);
        }
        _notWriteDefault(methodVisitor, fieldInfo, context, label);
        if (!context.writeDirect) {
            _apply(methodVisitor, fieldInfo, context);
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
            _processKey(methodVisitor, fieldInfo, context);
            Label label2 = new Label();
            methodVisitor.visitVarInsn(21, context.var("checkValue"));
            methodVisitor.visitJumpInsn(Opcodes.IFNE, label);
            _processValue(methodVisitor, fieldInfo, context);
            methodVisitor.visitVarInsn(25, Context.original);
            methodVisitor.visitVarInsn(25, Context.processValue);
            methodVisitor.visitJumpInsn(Opcodes.IF_ACMPEQ, label2);
            _writeObject(methodVisitor, fieldInfo, context, label);
            methodVisitor.visitJumpInsn(Opcodes.GOTO, label);
            methodVisitor.visitLabel(label2);
        }
    }

    private void _nameApply(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context, Label label) {
        if (!context.writeDirect) {
            methodVisitor.visitVarInsn(25, 1);
            methodVisitor.visitVarInsn(25, 2);
            methodVisitor.visitVarInsn(25, Context.fieldName);
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "applyName", "(Ljava/lang/Object;Ljava/lang/String;)Z");
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
            _labelApply(methodVisitor, fieldInfo, context, label);
        }
        if (fieldInfo.field == null) {
            methodVisitor.visitVarInsn(25, context.var("out"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "isIgnoreNonFieldGetter", "()Z");
            methodVisitor.visitJumpInsn(Opcodes.IFNE, label);
        }
    }

    private void _labelApply(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context, Label label) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitLdcInsn(fieldInfo.label);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "applyLabel", "(Ljava/lang/String;)Z");
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
    }

    private void _writeObject(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context, Label label) {
        String format = fieldInfo.getFormat();
        Label label2 = new Label();
        if (context.writeDirect) {
            methodVisitor.visitVarInsn(25, context.var("object"));
        } else {
            methodVisitor.visitVarInsn(25, Context.processValue);
        }
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label2);
        _if_write_null(methodVisitor, fieldInfo, context);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, label);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "write", "(I)V");
        methodVisitor.visitVarInsn(25, context.var("out"));
        methodVisitor.visitVarInsn(25, Context.fieldName);
        methodVisitor.visitInsn(3);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldName", "(Ljava/lang/String;Z)V");
        methodVisitor.visitVarInsn(25, 1);
        if (context.writeDirect) {
            methodVisitor.visitVarInsn(25, context.var("object"));
        } else {
            methodVisitor.visitVarInsn(25, Context.processValue);
        }
        if (format != null) {
            methodVisitor.visitLdcInsn(format);
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFormat", "(Ljava/lang/Object;Ljava/lang/String;)V");
        } else {
            methodVisitor.visitVarInsn(25, Context.fieldName);
            if (!(fieldInfo.fieldType instanceof Class) || !((Class) fieldInfo.fieldType).isPrimitive()) {
                if (fieldInfo.fieldClass == String.class) {
                    methodVisitor.visitLdcInsn(Type.getType(ASMUtils.desc((Class<?>) String.class)));
                } else {
                    methodVisitor.visitVarInsn(25, 0);
                    String access$200 = context.className;
                    methodVisitor.visitFieldInsn(Opcodes.GETFIELD, access$200, fieldInfo.name + "_asm_fieldType", "Ljava/lang/reflect/Type;");
                }
                methodVisitor.visitLdcInsn(Integer.valueOf(fieldInfo.serialzeFeatures));
                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
            } else {
                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
            }
        }
        _seperator(methodVisitor, context);
    }

    private void _before(MethodVisitor methodVisitor, Context context) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, 2);
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeBefore", "(Ljava/lang/Object;C)C");
        methodVisitor.visitVarInsn(54, context.var("seperator"));
    }

    private void _after(MethodVisitor methodVisitor, Context context) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, 2);
        methodVisitor.visitVarInsn(21, context.var("seperator"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "writeAfter", "(Ljava/lang/Object;C)C");
        methodVisitor.visitVarInsn(54, context.var("seperator"));
    }

    private void _notWriteDefault(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context, Label label) {
        Label label2 = new Label();
        methodVisitor.visitVarInsn(21, context.var("notWriteDefaultValue"));
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, label2);
        Class<?> cls = fieldInfo.fieldClass;
        if (cls == Boolean.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("boolean"));
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        } else if (cls == Byte.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("byte"));
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        } else if (cls == Short.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("short"));
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        } else if (cls == Integer.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("int"));
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        } else if (cls == Long.TYPE) {
            methodVisitor.visitVarInsn(22, context.var("long"));
            methodVisitor.visitInsn(9);
            methodVisitor.visitInsn(Opcodes.LCMP);
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        } else if (cls == Float.TYPE) {
            methodVisitor.visitVarInsn(23, context.var("float"));
            methodVisitor.visitInsn(11);
            methodVisitor.visitInsn(Opcodes.FCMPL);
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        } else if (cls == Double.TYPE) {
            methodVisitor.visitVarInsn(24, context.var("double"));
            methodVisitor.visitInsn(14);
            methodVisitor.visitInsn(Opcodes.DCMPL);
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
        }
        methodVisitor.visitLabel(label2);
    }

    private void _apply(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Class<?> cls = fieldInfo.fieldClass;
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, 2);
        methodVisitor.visitVarInsn(25, Context.fieldName);
        if (cls == Byte.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("byte"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
        } else if (cls == Short.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("short"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
        } else if (cls == Integer.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("int"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        } else if (cls == Character.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("char"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
        } else if (cls == Long.TYPE) {
            methodVisitor.visitVarInsn(22, context.var("long", 2));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        } else if (cls == Float.TYPE) {
            methodVisitor.visitVarInsn(23, context.var("float"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
        } else if (cls == Double.TYPE) {
            methodVisitor.visitVarInsn(24, context.var("double", 2));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        } else if (cls == Boolean.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("boolean"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
        } else if (cls == BigDecimal.class) {
            methodVisitor.visitVarInsn(25, context.var("decimal"));
        } else if (cls == String.class) {
            methodVisitor.visitVarInsn(25, context.var("string"));
        } else if (cls.isEnum()) {
            methodVisitor.visitVarInsn(25, context.var("enum"));
        } else if (List.class.isAssignableFrom(cls)) {
            methodVisitor.visitVarInsn(25, context.var("list"));
        } else {
            methodVisitor.visitVarInsn(25, context.var("object"));
        }
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "apply", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
    }

    private void _processValue(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Class<?> cls = fieldInfo.fieldClass;
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, context.className, "nature", JavaBeanSerializer_desc);
        methodVisitor.visitVarInsn(25, 2);
        methodVisitor.visitVarInsn(25, Context.fieldName);
        if (cls == Byte.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("byte"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Short.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("short"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Integer.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("int"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Character.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("char"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Long.TYPE) {
            methodVisitor.visitVarInsn(22, context.var("long", 2));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Float.TYPE) {
            methodVisitor.visitVarInsn(23, context.var("float"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Double.TYPE) {
            methodVisitor.visitVarInsn(24, context.var("double", 2));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == Boolean.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("boolean"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
            methodVisitor.visitInsn(89);
            methodVisitor.visitVarInsn(58, Context.original);
        } else if (cls == BigDecimal.class) {
            methodVisitor.visitVarInsn(25, context.var("decimal"));
            methodVisitor.visitVarInsn(58, Context.original);
            methodVisitor.visitVarInsn(25, Context.original);
        } else if (cls == String.class) {
            methodVisitor.visitVarInsn(25, context.var("string"));
            methodVisitor.visitVarInsn(58, Context.original);
            methodVisitor.visitVarInsn(25, Context.original);
        } else if (cls.isEnum()) {
            methodVisitor.visitVarInsn(25, context.var("enum"));
            methodVisitor.visitVarInsn(58, Context.original);
            methodVisitor.visitVarInsn(25, Context.original);
        } else if (List.class.isAssignableFrom(cls)) {
            methodVisitor.visitVarInsn(25, context.var("list"));
            methodVisitor.visitVarInsn(58, Context.original);
            methodVisitor.visitVarInsn(25, Context.original);
        } else {
            methodVisitor.visitVarInsn(25, context.var("object"));
            methodVisitor.visitVarInsn(58, Context.original);
            methodVisitor.visitVarInsn(25, Context.original);
        }
        String str = JSONSerializer;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "processValue", "(" + JavaBeanSerializer_desc + "Ljava/lang/Object;Ljava/lang/String;" + "Ljava/lang/Object;" + ")Ljava/lang/Object;");
        methodVisitor.visitVarInsn(58, Context.processValue);
    }

    private void _processKey(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        Label label = new Label();
        methodVisitor.visitVarInsn(21, context.var("hasNameFilters"));
        methodVisitor.visitJumpInsn(Opcodes.IFNE, label);
        Class<?> cls = fieldInfo.fieldClass;
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, 2);
        methodVisitor.visitVarInsn(25, Context.fieldName);
        if (cls == Byte.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("byte"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
        } else if (cls == Short.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("short"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
        } else if (cls == Integer.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("int"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        } else if (cls == Character.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("char"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
        } else if (cls == Long.TYPE) {
            methodVisitor.visitVarInsn(22, context.var("long", 2));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        } else if (cls == Float.TYPE) {
            methodVisitor.visitVarInsn(23, context.var("float"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
        } else if (cls == Double.TYPE) {
            methodVisitor.visitVarInsn(24, context.var("double", 2));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        } else if (cls == Boolean.TYPE) {
            methodVisitor.visitVarInsn(21, context.var("boolean"));
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
        } else if (cls == BigDecimal.class) {
            methodVisitor.visitVarInsn(25, context.var("decimal"));
        } else if (cls == String.class) {
            methodVisitor.visitVarInsn(25, context.var("string"));
        } else if (cls.isEnum()) {
            methodVisitor.visitVarInsn(25, context.var("enum"));
        } else if (List.class.isAssignableFrom(cls)) {
            methodVisitor.visitVarInsn(25, context.var("list"));
        } else {
            methodVisitor.visitVarInsn(25, context.var("object"));
        }
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONSerializer, "processKey", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
        methodVisitor.visitVarInsn(58, Context.fieldName);
        methodVisitor.visitLabel(label);
    }

    private void _if_write_null(MethodVisitor methodVisitor, FieldInfo fieldInfo, Context context) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        MethodVisitor methodVisitor2 = methodVisitor;
        Context context2 = context;
        Class<?> cls = fieldInfo.fieldClass;
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        methodVisitor2.visitLabel(label);
        JSONField annotation = fieldInfo.getAnnotation();
        int i = 0;
        if (annotation != null) {
            SerializerFeature[] serialzeFeatures = annotation.serialzeFeatures();
            int length = serialzeFeatures.length;
            int i2 = 0;
            z4 = false;
            z3 = false;
            z2 = false;
            z = false;
            while (i < length) {
                SerializerFeature serializerFeature = serialzeFeatures[i];
                if (serializerFeature == SerializerFeature.WriteMapNullValue) {
                    i2 = 1;
                } else if (serializerFeature == SerializerFeature.WriteNullNumberAsZero) {
                    z3 = true;
                } else if (serializerFeature == SerializerFeature.WriteNullStringAsEmpty) {
                    z4 = true;
                } else if (serializerFeature == SerializerFeature.WriteNullBooleanAsFalse) {
                    z2 = true;
                } else if (serializerFeature == SerializerFeature.WriteNullListAsEmpty) {
                    z = true;
                }
                i++;
            }
            i = i2;
        } else {
            z4 = false;
            z3 = false;
            z2 = false;
            z = false;
        }
        if (i == 0) {
            methodVisitor2.visitVarInsn(25, context2.var("out"));
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "isWriteMapNullValue", "()Z");
            methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label2);
        }
        methodVisitor2.visitLabel(label3);
        methodVisitor2.visitVarInsn(25, context2.var("out"));
        methodVisitor2.visitVarInsn(21, context2.var("seperator"));
        methodVisitor2.visitVarInsn(25, Context.fieldName);
        if (cls == String.class || cls == Character.class) {
            if (z4) {
                methodVisitor2.visitLdcInsn("");
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Ljava/lang/String;)V");
            } else {
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldNullString", "(CLjava/lang/String;)V");
            }
        } else if (Number.class.isAssignableFrom(cls)) {
            if (z3) {
                methodVisitor2.visitInsn(3);
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;I)V");
            } else {
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldNullNumber", "(CLjava/lang/String;)V");
            }
        } else if (cls == Boolean.class) {
            if (z2) {
                methodVisitor2.visitInsn(3);
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldValue", "(CLjava/lang/String;Z)V");
            } else {
                methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldNullBoolean", "(CLjava/lang/String;)V");
            }
        } else if (!Collection.class.isAssignableFrom(cls) && !cls.isArray()) {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldNull", "(CLjava/lang/String;)V");
        } else if (z) {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldEmptyList", "(CLjava/lang/String;)V");
        } else {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, SerializeWriter, "writeFieldNullList", "(CLjava/lang/String;)V");
        }
        _seperator(methodVisitor2, context2);
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label4);
        methodVisitor2.visitLabel(label2);
        methodVisitor2.visitLabel(label4);
    }

    private void _seperator(MethodVisitor methodVisitor, Context context) {
        methodVisitor.visitVarInsn(16, 44);
        methodVisitor.visitVarInsn(54, context.var("seperator"));
    }
}
