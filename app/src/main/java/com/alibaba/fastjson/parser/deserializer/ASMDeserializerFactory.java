package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.MethodWriter;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public class ASMDeserializerFactory implements Opcodes {
    static final String DefaultJSONParser = ASMUtils.type(DefaultJSONParser.class);
    static final String JSONLexerBase = ASMUtils.type(JSONLexerBase.class);
    static final String JSONToken = ASMUtils.type(JSONToken.class);
    public final ASMClassLoader classLoader;
    protected final AtomicLong seed = new AtomicLong();

    public ASMDeserializerFactory(ClassLoader classLoader2) {
        this.classLoader = classLoader2 instanceof ASMClassLoader ? (ASMClassLoader) classLoader2 : new ASMClassLoader(classLoader2);
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) throws Exception {
        if (!cls.isPrimitive()) {
            String str = "FastjsonASMDeserializer_" + this.seed.incrementAndGet() + "_" + cls.getSimpleName();
            String name = ASMDeserializerFactory.class.getPackage().getName();
            String str2 = name.replace('.', '/') + "/" + str;
            String str3 = name + "." + str;
            ClassWriter classWriter = new ClassWriter();
            classWriter.visit(49, 33, str2, ASMUtils.type(ASMJavaBeanDeserializer.class), (String[]) null);
            JavaBeanInfo build = JavaBeanInfo.build(cls, type);
            _init(classWriter, new Context(str2, parserConfig, build, 3));
            _createInstance(classWriter, new Context(str2, parserConfig, build, 3));
            _deserialze(classWriter, new Context(str2, parserConfig, build, 4));
            _deserialzeArrayMapping(classWriter, new Context(str2, parserConfig, build, 4));
            byte[] byteArray = classWriter.toByteArray();
            return (ObjectDeserializer) defineClassPublic(str3, byteArray, 0, byteArray.length).getConstructor(new Class[]{ParserConfig.class, Class.class}).newInstance(new Object[]{parserConfig, cls});
        }
        throw new IllegalArgumentException("not support type :" + cls.getName());
    }

    private Class<?> defineClassPublic(String str, byte[] bArr, int i, int i2) {
        return this.classLoader.defineClassPublic(str, bArr, i, i2);
    }

    /* access modifiers changed from: package-private */
    public void _setFlag(MethodVisitor methodVisitor, Context context, int i) {
        String str = "_asm_flag_" + (i / 32);
        methodVisitor.visitVarInsn(21, context.var(str));
        methodVisitor.visitLdcInsn(Integer.valueOf(1 << i));
        methodVisitor.visitInsn(128);
        methodVisitor.visitVarInsn(54, context.var(str));
    }

    /* access modifiers changed from: package-private */
    public void _isFlag(MethodVisitor methodVisitor, Context context, int i, Label label) {
        methodVisitor.visitVarInsn(21, context.var("_asm_flag_" + (i / 32)));
        methodVisitor.visitLdcInsn(Integer.valueOf(1 << i));
        methodVisitor.visitInsn(Opcodes.IAND);
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, label);
    }

    /* access modifiers changed from: package-private */
    public void _deserialzeArrayMapping(ClassWriter classWriter, Context context) {
        int i;
        FieldInfo[] fieldInfoArr;
        int i2;
        int i3;
        Context context2 = context;
        MethodWriter methodWriter = new MethodWriter(classWriter, 1, "deserialzeArrayMapping", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;", (String) null, (String[]) null);
        defineVarLexer(context2, methodWriter);
        _createInstance(context2, (MethodVisitor) methodWriter);
        FieldInfo[] fieldInfoArr2 = context.beanInfo.sortedFields;
        int length = fieldInfoArr2.length;
        int i4 = 0;
        while (i4 < length) {
            boolean z = i4 == length + -1;
            int i5 = z ? 93 : 44;
            FieldInfo fieldInfo = fieldInfoArr2[i4];
            Class<?> cls = fieldInfo.fieldClass;
            Type type = fieldInfo.fieldType;
            if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE) {
                fieldInfoArr = fieldInfoArr2;
                i = length;
                methodWriter.visitVarInsn(25, context2.var("lexer"));
                methodWriter.visitVarInsn(16, i5);
                methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanInt", "(C)I");
                methodWriter.visitVarInsn(54, context2.var(fieldInfo.name + "_asm"));
            } else {
                if (cls == Long.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanLong", "(C)J");
                    methodWriter.visitVarInsn(55, context2.var(fieldInfo.name + "_asm", 2));
                } else if (cls == Boolean.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanBoolean", "(C)Z");
                    methodWriter.visitVarInsn(54, context2.var(fieldInfo.name + "_asm"));
                } else if (cls == Float.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanFloat", "(C)F");
                    methodWriter.visitVarInsn(56, context2.var(fieldInfo.name + "_asm"));
                } else if (cls == Double.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanDouble", "(C)D");
                    methodWriter.visitVarInsn(57, context2.var(fieldInfo.name + "_asm", 2));
                } else if (cls == Character.TYPE) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanString", "(C)Ljava/lang/String;");
                    methodWriter.visitInsn(3);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "charAt", "(I)C");
                    methodWriter.visitVarInsn(54, context2.var(fieldInfo.name + "_asm"));
                } else if (cls == String.class) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitVarInsn(16, i5);
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanString", "(C)Ljava/lang/String;");
                    methodWriter.visitVarInsn(58, context2.var(fieldInfo.name + "_asm"));
                } else if (cls.isEnum()) {
                    methodWriter.visitVarInsn(25, context2.var("lexer"));
                    methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls)));
                    methodWriter.visitVarInsn(25, 1);
                    String str = DefaultJSONParser;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getSymbolTable", "()" + ASMUtils.desc((Class<?>) SymbolTable.class));
                    methodWriter.visitVarInsn(16, i5);
                    String str2 = JSONLexerBase;
                    methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str2, "scanEnum", "(Ljava/lang/Class;" + ASMUtils.desc((Class<?>) SymbolTable.class) + "C)Ljava/lang/Enum;");
                    methodWriter.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
                    methodWriter.visitVarInsn(58, context2.var(fieldInfo.name + "_asm"));
                } else {
                    fieldInfoArr = fieldInfoArr2;
                    i = length;
                    if (Collection.class.isAssignableFrom(cls)) {
                        Class<?> collectionItemClass = TypeUtils.getCollectionItemClass(type);
                        if (collectionItemClass == String.class) {
                            methodWriter.visitVarInsn(25, context2.var("lexer"));
                            methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls)));
                            methodWriter.visitVarInsn(16, i5);
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "scanStringArray", "(Ljava/lang/Class;C)Ljava/util/Collection;");
                            methodWriter.visitVarInsn(58, context2.var(fieldInfo.name + "_asm"));
                        } else {
                            methodWriter.visitVarInsn(25, 1);
                            if (i4 == 0) {
                                String str3 = JSONToken;
                                i3 = Opcodes.GETSTATIC;
                                methodWriter.visitFieldInsn(Opcodes.GETSTATIC, str3, "LBRACKET", "I");
                            } else {
                                i3 = Opcodes.GETSTATIC;
                                methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "COMMA", "I");
                            }
                            methodWriter.visitFieldInsn(i3, JSONToken, "LBRACKET", "I");
                            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "accept", "(II)V");
                            _newCollection(methodWriter, cls, i4, false);
                            methodWriter.visitInsn(89);
                            methodWriter.visitVarInsn(58, context2.var(fieldInfo.name + "_asm"));
                            _getCollectionFieldItemDeser(context2, methodWriter, fieldInfo, collectionItemClass);
                            methodWriter.visitVarInsn(25, 1);
                            methodWriter.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(collectionItemClass)));
                            methodWriter.visitVarInsn(25, 3);
                            String type2 = ASMUtils.type(ASMUtils.class);
                            methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, type2, "parseArray", "(Ljava/util/Collection;" + ASMUtils.desc((Class<?>) ObjectDeserializer.class) + "L" + DefaultJSONParser + ";" + "Ljava/lang/reflect/Type;Ljava/lang/Object;)V");
                        }
                    } else if (cls.isArray()) {
                        methodWriter.visitVarInsn(25, context2.var("lexer"));
                        methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "LBRACKET", "I");
                        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
                        methodWriter.visitVarInsn(25, 1);
                        methodWriter.visitVarInsn(25, 0);
                        methodWriter.visitLdcInsn(Integer.valueOf(i4));
                        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(ASMJavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
                        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "parseObject", "(Ljava/lang/reflect/Type;)Ljava/lang/Object;");
                        methodWriter.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
                        methodWriter.visitVarInsn(58, context2.var(fieldInfo.name + "_asm"));
                    } else {
                        methodWriter.visitVarInsn(25, 1);
                        if (i4 == 0) {
                            String str4 = JSONToken;
                            i2 = Opcodes.GETSTATIC;
                            methodWriter.visitFieldInsn(Opcodes.GETSTATIC, str4, "LBRACKET", "I");
                        } else {
                            i2 = Opcodes.GETSTATIC;
                            methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "COMMA", "I");
                        }
                        methodWriter.visitFieldInsn(i2, JSONToken, "LBRACKET", "I");
                        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "accept", "(II)V");
                        Class<?> cls2 = cls;
                        String str5 = "(II)V";
                        _deserObject(context, methodWriter, fieldInfo, cls2, i4);
                        methodWriter.visitVarInsn(25, 1);
                        if (!z) {
                            methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "COMMA", "I");
                            methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "LBRACKET", "I");
                        } else {
                            methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "RBRACKET", "I");
                            methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "EOF", "I");
                        }
                        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "accept", str5);
                    }
                }
                fieldInfoArr = fieldInfoArr2;
                i = length;
            }
            i4++;
            fieldInfoArr2 = fieldInfoArr;
            length = i;
        }
        _batchSet(context2, methodWriter, false);
        methodWriter.visitVarInsn(25, context2.var("lexer"));
        methodWriter.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "COMMA", "I");
        methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        methodWriter.visitVarInsn(25, context2.var("instance"));
        methodWriter.visitInsn(Opcodes.ARETURN);
        methodWriter.visitMaxs(5, context.variantIndex);
        methodWriter.visitEnd();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x08bb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void _deserialze(com.alibaba.fastjson.asm.ClassWriter r29, com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory.Context r30) {
        /*
            r28 = this;
            r8 = r28
            r9 = r30
            com.alibaba.fastjson.util.FieldInfo[] r0 = r30.fieldInfoList
            int r0 = r0.length
            if (r0 != 0) goto L_0x000c
            return
        L_0x000c:
            com.alibaba.fastjson.util.FieldInfo[] r0 = r30.fieldInfoList
            int r1 = r0.length
            r10 = 0
            r2 = 0
        L_0x0013:
            if (r2 >= r1) goto L_0x003d
            r3 = r0[r2]
            java.lang.Class<?> r4 = r3.fieldClass
            java.lang.reflect.Type r3 = r3.fieldType
            java.lang.Class r5 = java.lang.Character.TYPE
            if (r4 != r5) goto L_0x0020
            return
        L_0x0020:
            java.lang.Class<java.util.Collection> r5 = java.util.Collection.class
            boolean r4 = r5.isAssignableFrom(r4)
            if (r4 == 0) goto L_0x003a
            boolean r4 = r3 instanceof java.lang.reflect.ParameterizedType
            if (r4 == 0) goto L_0x0039
            java.lang.reflect.ParameterizedType r3 = (java.lang.reflect.ParameterizedType) r3
            java.lang.reflect.Type[] r3 = r3.getActualTypeArguments()
            r3 = r3[r10]
            boolean r3 = r3 instanceof java.lang.Class
            if (r3 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            return
        L_0x003a:
            int r2 = r2 + 1
            goto L_0x0013
        L_0x003d:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r30.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r0 = r0.sortedFields
            com.alibaba.fastjson.util.FieldInfo[] unused = r9.fieldInfoList = r0
            com.alibaba.fastjson.asm.MethodWriter r11 = new com.alibaba.fastjson.asm.MethodWriter
            r3 = 1
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "(L"
            r0.append(r1)
            java.lang.String r1 = DefaultJSONParser
            r0.append(r1)
            java.lang.String r1 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;"
            r0.append(r1)
            java.lang.String r5 = r0.toString()
            r6 = 0
            r7 = 0
            java.lang.String r4 = "deserialze"
            r1 = r11
            r2 = r29
            r1.<init>(r2, r3, r4, r5, r6, r7)
            com.alibaba.fastjson.asm.Label r12 = new com.alibaba.fastjson.asm.Label
            r12.<init>()
            com.alibaba.fastjson.asm.Label r13 = new com.alibaba.fastjson.asm.Label
            r13.<init>()
            com.alibaba.fastjson.asm.Label r14 = new com.alibaba.fastjson.asm.Label
            r14.<init>()
            com.alibaba.fastjson.asm.Label r15 = new com.alibaba.fastjson.asm.Label
            r15.<init>()
            r8.defineVarLexer(r9, r11)
            com.alibaba.fastjson.parser.Feature r0 = com.alibaba.fastjson.parser.Feature.SortFeidFastMatch
            r8._isEnable(r9, r11, r0)
            r0 = 153(0x99, float:2.14E-43)
            r11.visitJumpInsn(r0, r13)
            com.alibaba.fastjson.asm.Label r0 = new com.alibaba.fastjson.asm.Label
            r0.<init>()
            r7 = 25
            r11.visitVarInsn(r7, r10)
            java.lang.String r6 = "lexer"
            int r1 = r9.var(r6)
            r11.visitVarInsn(r7, r1)
            r1 = 183(0xb7, float:2.56E-43)
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer> r2 = com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.class
            java.lang.String r2 = com.alibaba.fastjson.util.ASMUtils.type(r2)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "("
            r3.append(r4)
            java.lang.Class<com.alibaba.fastjson.parser.JSONLexer> r4 = com.alibaba.fastjson.parser.JSONLexer.class
            java.lang.String r4 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r4)
            r3.append(r4)
            java.lang.String r4 = ")Z"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "isSupportArrayToBean"
            r11.visitMethodInsn(r1, r2, r4, r3)
            r1 = 153(0x99, float:2.14E-43)
            r11.visitJumpInsn(r1, r0)
            int r1 = r9.var(r6)
            r11.visitVarInsn(r7, r1)
            java.lang.String r1 = JSONLexerBase
            r5 = 182(0xb6, float:2.55E-43)
            java.lang.String r2 = "token"
            java.lang.String r3 = "()I"
            r11.visitMethodInsn(r5, r1, r2, r3)
            r1 = 178(0xb2, float:2.5E-43)
            java.lang.String r2 = JSONToken
            java.lang.String r4 = "I"
            java.lang.String r3 = "LBRACKET"
            r11.visitFieldInsn(r1, r2, r3, r4)
            r1 = 160(0xa0, float:2.24E-43)
            r11.visitJumpInsn(r1, r0)
            r11.visitVarInsn(r7, r10)
            r3 = 1
            r11.visitVarInsn(r7, r3)
            r2 = 2
            r11.visitVarInsn(r7, r2)
            r1 = 3
            r11.visitVarInsn(r7, r1)
            r10 = 183(0xb7, float:2.56E-43)
            java.lang.String r2 = r30.className
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "(L"
            r1.append(r3)
            java.lang.String r3 = DefaultJSONParser
            r1.append(r3)
            java.lang.String r3 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;"
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r3 = "deserialzeArrayMapping"
            r11.visitMethodInsn(r10, r2, r3, r1)
            r1 = 176(0xb0, float:2.47E-43)
            r11.visitInsn(r1)
            r11.visitLabel(r0)
            int r0 = r9.var(r6)
            r11.visitVarInsn(r7, r0)
            java.lang.Class r0 = r30.clazz
            java.lang.String r0 = r0.getName()
            r11.visitLdcInsn(r0)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r1 = "scanType"
            java.lang.String r2 = "(Ljava/lang/String;)I"
            r11.visitMethodInsn(r5, r0, r1, r2)
            r0 = 178(0xb2, float:2.5E-43)
            java.lang.String r1 = JSONLexerBase
            java.lang.String r2 = "NOT_MATCH"
            r11.visitFieldInsn(r0, r1, r2, r4)
            r0 = 159(0x9f, float:2.23E-43)
            r11.visitJumpInsn(r0, r13)
            r0 = 1
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = DefaultJSONParser
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "()"
            r1.append(r2)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r2 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r2 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "getContext"
            r11.visitMethodInsn(r5, r0, r2, r1)
            java.lang.String r0 = "mark_context"
            int r0 = r9.var(r0)
            r10 = 58
            r11.visitVarInsn(r10, r0)
            r0 = 3
            r11.visitInsn(r0)
            java.lang.String r0 = "matchedCount"
            int r0 = r9.var(r0)
            r3 = 54
            r11.visitVarInsn(r3, r0)
            r8._createInstance((com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory.Context) r9, (com.alibaba.fastjson.asm.MethodVisitor) r11)
            r0 = 1
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = DefaultJSONParser
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "()"
            r1.append(r2)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r2 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r2 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "getContext"
            r11.visitMethodInsn(r5, r0, r2, r1)
            java.lang.String r0 = "context"
            int r0 = r9.var(r0)
            r11.visitVarInsn(r10, r0)
            r0 = 1
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = "context"
            int r0 = r9.var(r0)
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = "instance"
            int r0 = r9.var(r0)
            r11.visitVarInsn(r7, r0)
            r0 = 3
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = DefaultJSONParser
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "("
            r1.append(r2)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r2 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r2 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r2)
            r1.append(r2)
            java.lang.String r2 = "Ljava/lang/Object;Ljava/lang/Object;)"
            r1.append(r2)
            java.lang.Class<com.alibaba.fastjson.parser.ParseContext> r2 = com.alibaba.fastjson.parser.ParseContext.class
            java.lang.String r2 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "setContext"
            r11.visitMethodInsn(r5, r0, r2, r1)
            java.lang.String r0 = "childContext"
            int r0 = r9.var(r0)
            r11.visitVarInsn(r10, r0)
            int r0 = r9.var(r6)
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r2 = "matchStat"
            r1 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r1, r0, r2, r4)
            r0 = 178(0xb2, float:2.5E-43)
            java.lang.String r1 = JSONLexerBase
            java.lang.String r10 = "END"
            r11.visitFieldInsn(r0, r1, r10, r4)
            r0 = 159(0x9f, float:2.23E-43)
            r11.visitJumpInsn(r0, r14)
            r0 = 3
            r11.visitInsn(r0)
            int r1 = r9.var(r2)
            r11.visitIntInsn(r3, r1)
            com.alibaba.fastjson.util.FieldInfo[] r1 = r30.fieldInfoList
            int r10 = r1.length
            r1 = 0
        L_0x023a:
            if (r1 >= r10) goto L_0x025f
            r11.visitInsn(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "_asm_flag_"
            r0.append(r5)
            int r5 = r1 / 32
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r11.visitVarInsn(r3, r0)
            int r1 = r1 + 32
            r0 = 3
            r5 = 182(0xb6, float:2.55E-43)
            goto L_0x023a
        L_0x025f:
            com.alibaba.fastjson.parser.Feature r0 = com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty
            r8._isEnable(r9, r11, r0)
            java.lang.String r0 = "initStringFieldAsEmpty"
            int r0 = r9.var(r0)
            r11.visitIntInsn(r3, r0)
            r0 = 0
        L_0x026e:
            java.lang.String r5 = "_asm"
            if (r0 >= r10) goto L_0x03ba
            com.alibaba.fastjson.util.FieldInfo[] r1 = r30.fieldInfoList
            r1 = r1[r0]
            java.lang.Class<?> r3 = r1.fieldClass
            java.lang.Class r7 = java.lang.Boolean.TYPE
            if (r3 == r7) goto L_0x0382
            java.lang.Class r7 = java.lang.Byte.TYPE
            if (r3 == r7) goto L_0x0382
            java.lang.Class r7 = java.lang.Short.TYPE
            if (r3 == r7) goto L_0x0382
            java.lang.Class r7 = java.lang.Integer.TYPE
            if (r3 != r7) goto L_0x028c
            goto L_0x0382
        L_0x028c:
            java.lang.Class r7 = java.lang.Long.TYPE
            if (r3 != r7) goto L_0x02bb
            r3 = 9
            r11.visitInsn(r3)
            r3 = 55
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = r1.name
            r7.append(r1)
            r7.append(r5)
            java.lang.String r1 = r7.toString()
            r5 = 2
            int r1 = r9.var(r1, r5)
            r11.visitVarInsn(r3, r1)
        L_0x02b0:
            r25 = r12
            r22 = r13
            r23 = r14
            r24 = r15
        L_0x02b8:
            r3 = 3
            goto L_0x03a8
        L_0x02bb:
            java.lang.Class r7 = java.lang.Float.TYPE
            if (r3 != r7) goto L_0x02df
            r3 = 11
            r11.visitInsn(r3)
            r3 = 56
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = r1.name
            r7.append(r1)
            r7.append(r5)
            java.lang.String r1 = r7.toString()
            int r1 = r9.var(r1)
            r11.visitVarInsn(r3, r1)
            goto L_0x02b0
        L_0x02df:
            java.lang.Class r7 = java.lang.Double.TYPE
            if (r3 != r7) goto L_0x0304
            r3 = 14
            r11.visitInsn(r3)
            r3 = 57
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = r1.name
            r7.append(r1)
            r7.append(r5)
            java.lang.String r1 = r7.toString()
            r5 = 2
            int r1 = r9.var(r1, r5)
            r11.visitVarInsn(r3, r1)
            goto L_0x02b0
        L_0x0304:
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            if (r3 != r7) goto L_0x0351
            com.alibaba.fastjson.asm.Label r7 = new com.alibaba.fastjson.asm.Label
            r7.<init>()
            r22 = r13
            com.alibaba.fastjson.asm.Label r13 = new com.alibaba.fastjson.asm.Label
            r13.<init>()
            r23 = r14
            r14 = 21
            r24 = r15
            java.lang.String r15 = "initStringFieldAsEmpty"
            int r15 = r9.var(r15)
            r11.visitVarInsn(r14, r15)
            r14 = 153(0x99, float:2.14E-43)
            r11.visitJumpInsn(r14, r13)
            r8._setFlag(r11, r9, r0)
            int r14 = r9.var(r6)
            r15 = 25
            r11.visitVarInsn(r15, r14)
            java.lang.String r14 = JSONLexerBase
            java.lang.String r15 = "stringDefaultValue"
            r25 = r12
            java.lang.String r12 = "()Ljava/lang/String;"
            r8 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r8, r14, r15, r12)
            r8 = 167(0xa7, float:2.34E-43)
            r11.visitJumpInsn(r8, r7)
            r11.visitLabel(r13)
            r8 = 1
            r11.visitInsn(r8)
            r11.visitLabel(r7)
            goto L_0x035d
        L_0x0351:
            r25 = r12
            r22 = r13
            r23 = r14
            r24 = r15
            r8 = 1
            r11.visitInsn(r8)
        L_0x035d:
            r7 = 192(0xc0, float:2.69E-43)
            java.lang.String r3 = com.alibaba.fastjson.util.ASMUtils.type(r3)
            r11.visitTypeInsn(r7, r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r1 = r1.name
            r3.append(r1)
            r3.append(r5)
            java.lang.String r1 = r3.toString()
            int r1 = r9.var(r1)
            r3 = 58
            r11.visitVarInsn(r3, r1)
            goto L_0x02b8
        L_0x0382:
            r25 = r12
            r22 = r13
            r23 = r14
            r24 = r15
            r3 = 3
            r11.visitInsn(r3)
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = r1.name
            r7.append(r1)
            r7.append(r5)
            java.lang.String r1 = r7.toString()
            int r1 = r9.var(r1)
            r5 = 54
            r11.visitVarInsn(r5, r1)
        L_0x03a8:
            int r0 = r0 + 1
            r13 = r22
            r14 = r23
            r15 = r24
            r12 = r25
            r3 = 54
            r7 = 25
            r8 = r28
            goto L_0x026e
        L_0x03ba:
            r25 = r12
            r22 = r13
            r23 = r14
            r24 = r15
            r3 = 3
            r8 = 0
        L_0x03c4:
            if (r8 >= r10) goto L_0x0972
            com.alibaba.fastjson.util.FieldInfo[] r0 = r30.fieldInfoList
            r7 = r0[r8]
            java.lang.Class<?> r12 = r7.fieldClass
            java.lang.reflect.Type r0 = r7.fieldType
            com.alibaba.fastjson.asm.Label r1 = new com.alibaba.fastjson.asm.Label
            r1.<init>()
            java.lang.Class r13 = java.lang.Boolean.TYPE
            java.lang.String r14 = "[C"
            java.lang.String r15 = "_asm_prefix__"
            if (r12 != r13) goto L_0x0431
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldBoolean"
            java.lang.String r13 = "([C)Z"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r7 = r7.name
            r0.append(r7)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r7 = 54
            r11.visitVarInsn(r7, r0)
        L_0x0429:
            r26 = r10
            r13 = 182(0xb6, float:2.55E-43)
            r14 = 58
            goto L_0x0814
        L_0x0431:
            java.lang.Class r13 = java.lang.Byte.TYPE
            if (r12 != r13) goto L_0x0482
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldInt"
            java.lang.String r13 = "([C)I"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r7 = r7.name
            r0.append(r7)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r7 = 54
            r11.visitVarInsn(r7, r0)
            goto L_0x0429
        L_0x0482:
            java.lang.Class r13 = java.lang.Short.TYPE
            if (r12 != r13) goto L_0x04d4
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldInt"
            java.lang.String r13 = "([C)I"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r7 = r7.name
            r0.append(r7)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r7 = 54
            r11.visitVarInsn(r7, r0)
            goto L_0x0429
        L_0x04d4:
            java.lang.Class r13 = java.lang.Integer.TYPE
            if (r12 != r13) goto L_0x0526
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldInt"
            java.lang.String r13 = "([C)I"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r7 = r7.name
            r0.append(r7)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r7 = 54
            r11.visitVarInsn(r7, r0)
            goto L_0x0429
        L_0x0526:
            java.lang.Class r13 = java.lang.Long.TYPE
            if (r12 != r13) goto L_0x0579
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldLong"
            java.lang.String r13 = "([C)J"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            r0 = 55
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r7 = r7.name
            r12.append(r7)
            r12.append(r5)
            java.lang.String r7 = r12.toString()
            r12 = 2
            int r7 = r9.var(r7, r12)
            r11.visitVarInsn(r0, r7)
            goto L_0x0429
        L_0x0579:
            java.lang.Class r13 = java.lang.Float.TYPE
            if (r12 != r13) goto L_0x05cb
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldFloat"
            java.lang.String r13 = "([C)F"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            r0 = 56
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r7 = r7.name
            r12.append(r7)
            r12.append(r5)
            java.lang.String r7 = r12.toString()
            int r7 = r9.var(r7)
            r11.visitVarInsn(r0, r7)
            goto L_0x0429
        L_0x05cb:
            java.lang.Class r13 = java.lang.Double.TYPE
            if (r12 != r13) goto L_0x061e
            int r0 = r9.var(r6)
            r12 = 25
            r11.visitVarInsn(r12, r0)
            r0 = 0
            r11.visitVarInsn(r12, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = r7.name
            r12.append(r13)
            r12.append(r15)
            java.lang.String r12 = r12.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r12, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r12 = "scanFieldDouble"
            java.lang.String r13 = "([C)D"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r12, r13)
            r0 = 57
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r7 = r7.name
            r12.append(r7)
            r12.append(r5)
            java.lang.String r7 = r12.toString()
            r13 = 2
            int r7 = r9.var(r7, r13)
            r11.visitVarInsn(r0, r7)
            goto L_0x0429
        L_0x061e:
            r13 = 2
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            if (r12 != r3) goto L_0x0671
            int r0 = r9.var(r6)
            r3 = 25
            r11.visitVarInsn(r3, r0)
            r0 = 0
            r11.visitVarInsn(r3, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r12 = r7.name
            r3.append(r12)
            r3.append(r15)
            java.lang.String r3 = r3.toString()
            r12 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r12, r0, r3, r14)
            java.lang.String r0 = JSONLexerBase
            java.lang.String r3 = "scanFieldString"
            java.lang.String r12 = "([C)Ljava/lang/String;"
            r14 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r14, r0, r3, r12)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = r7.name
            r0.append(r3)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r3 = 58
            r11.visitVarInsn(r3, r0)
            goto L_0x0429
        L_0x0671:
            boolean r3 = r12.isEnum()
            if (r3 == 0) goto L_0x0796
            int r0 = r9.var(r6)
            r3 = 25
            r11.visitVarInsn(r3, r0)
            r0 = 0
            r11.visitVarInsn(r3, r0)
            java.lang.String r0 = r30.className
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r7.name
            r3.append(r13)
            r3.append(r15)
            java.lang.String r3 = r3.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r0, r3, r14)
            com.alibaba.fastjson.asm.Label r0 = new com.alibaba.fastjson.asm.Label
            r0.<init>()
            r3 = 1
            r11.visitInsn(r3)
            r3 = 192(0xc0, float:2.69E-43)
            java.lang.String r13 = com.alibaba.fastjson.util.ASMUtils.type(r12)
            r11.visitTypeInsn(r3, r13)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r7.name
            r3.append(r13)
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            int r3 = r9.var(r3)
            r13 = 58
            r11.visitVarInsn(r13, r3)
            r3 = 1
            r13 = 25
            r11.visitVarInsn(r13, r3)
            java.lang.String r13 = DefaultJSONParser
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "()"
            r14.append(r15)
            java.lang.Class<com.alibaba.fastjson.parser.SymbolTable> r15 = com.alibaba.fastjson.parser.SymbolTable.class
            java.lang.String r15 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r15)
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            java.lang.String r15 = "getSymbolTable"
            r3 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r3, r13, r15, r14)
            java.lang.String r3 = JSONLexerBase
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "([C"
            r13.append(r14)
            java.lang.Class<com.alibaba.fastjson.parser.SymbolTable> r14 = com.alibaba.fastjson.parser.SymbolTable.class
            java.lang.String r14 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r14)
            r13.append(r14)
            java.lang.String r14 = ")Ljava/lang/String;"
            r13.append(r14)
            java.lang.String r13 = r13.toString()
            java.lang.String r14 = "scanFieldSymbol"
            r15 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r15, r3, r14, r13)
            r3 = 89
            r11.visitInsn(r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r7.name
            r3.append(r13)
            java.lang.String r13 = "_asm_enumName"
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            int r3 = r9.var(r3)
            r13 = 58
            r11.visitVarInsn(r13, r3)
            r3 = 198(0xc6, float:2.77E-43)
            r11.visitJumpInsn(r3, r0)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r7.name
            r3.append(r13)
            java.lang.String r13 = "_asm_enumName"
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            int r3 = r9.var(r3)
            r13 = 25
            r11.visitVarInsn(r13, r3)
            r3 = 184(0xb8, float:2.58E-43)
            java.lang.String r13 = com.alibaba.fastjson.util.ASMUtils.type(r12)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "(Ljava/lang/String;)"
            r14.append(r15)
            java.lang.String r12 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r12)
            r14.append(r12)
            java.lang.String r12 = r14.toString()
            java.lang.String r14 = "valueOf"
            r11.visitMethodInsn(r3, r13, r14, r12)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r7 = r7.name
            r3.append(r7)
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            int r3 = r9.var(r3)
            r7 = 58
            r11.visitVarInsn(r7, r3)
            r11.visitLabel(r0)
            goto L_0x0429
        L_0x0796:
            java.lang.Class<java.util.Collection> r3 = java.util.Collection.class
            boolean r3 = r3.isAssignableFrom(r12)
            if (r3 == 0) goto L_0x0930
            int r3 = r9.var(r6)
            r13 = 25
            r11.visitVarInsn(r13, r3)
            r3 = 0
            r11.visitVarInsn(r13, r3)
            java.lang.String r3 = r30.className
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r26 = r10
            java.lang.String r10 = r7.name
            r13.append(r10)
            r13.append(r15)
            java.lang.String r10 = r13.toString()
            r13 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r13, r3, r10, r14)
            java.lang.Class r10 = com.alibaba.fastjson.util.TypeUtils.getCollectionItemClass(r0)
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            if (r10 != r0) goto L_0x08ee
            java.lang.String r0 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r12)
            com.alibaba.fastjson.asm.Type r0 = com.alibaba.fastjson.asm.Type.getType(r0)
            r11.visitLdcInsn(r0)
            java.lang.String r0 = JSONLexerBase
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r10 = "([CLjava/lang/Class;)"
            r3.append(r10)
            java.lang.Class<java.util.Collection> r10 = java.util.Collection.class
            java.lang.String r10 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r10)
            r3.append(r10)
            java.lang.String r3 = r3.toString()
            java.lang.String r10 = "scanFieldStringArray"
            r13 = 182(0xb6, float:2.55E-43)
            r11.visitMethodInsn(r13, r0, r10, r3)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = r7.name
            r0.append(r3)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            int r0 = r9.var(r0)
            r14 = 58
            r11.visitVarInsn(r14, r0)
        L_0x0814:
            int r0 = r9.var(r6)
            r3 = 25
            r11.visitVarInsn(r3, r0)
            java.lang.String r0 = JSONLexerBase
            r7 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r7, r0, r2, r4)
            com.alibaba.fastjson.asm.Label r0 = new com.alibaba.fastjson.asm.Label
            r0.<init>()
            r10 = 158(0x9e, float:2.21E-43)
            r11.visitJumpInsn(r10, r0)
            r15 = r28
            r15._setFlag(r11, r9, r8)
            r11.visitLabel(r0)
            int r0 = r9.var(r6)
            r11.visitVarInsn(r3, r0)
            java.lang.String r0 = JSONLexerBase
            r11.visitFieldInsn(r7, r0, r2, r4)
            r0 = 89
            r11.visitInsn(r0)
            int r0 = r9.var(r2)
            r3 = 54
            r11.visitVarInsn(r3, r0)
            r0 = 178(0xb2, float:2.5E-43)
            java.lang.String r3 = JSONLexerBase
            java.lang.String r7 = "NOT_MATCH"
            r11.visitFieldInsn(r0, r3, r7, r4)
            r0 = 159(0x9f, float:2.23E-43)
            r3 = r25
            r11.visitJumpInsn(r0, r3)
            int r0 = r9.var(r6)
            r7 = 25
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = JSONLexerBase
            r7 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r7, r0, r2, r4)
            r0 = 158(0x9e, float:2.21E-43)
            r11.visitJumpInsn(r0, r1)
            r0 = 21
            java.lang.String r7 = "matchedCount"
            int r7 = r9.var(r7)
            r11.visitVarInsn(r0, r7)
            r0 = 4
            r11.visitInsn(r0)
            r0 = 96
            r11.visitInsn(r0)
            java.lang.String r0 = "matchedCount"
            int r0 = r9.var(r0)
            r7 = 54
            r11.visitVarInsn(r7, r0)
            int r0 = r9.var(r6)
            r10 = 25
            r11.visitVarInsn(r10, r0)
            java.lang.String r0 = JSONLexerBase
            r10 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r10, r0, r2, r4)
            r0 = 178(0xb2, float:2.5E-43)
            java.lang.String r10 = JSONLexerBase
            java.lang.String r12 = "END"
            r11.visitFieldInsn(r0, r10, r12, r4)
            r0 = 159(0x9f, float:2.23E-43)
            r10 = r24
            r11.visitJumpInsn(r0, r10)
            r11.visitLabel(r1)
            int r0 = r26 + -1
            if (r8 != r0) goto L_0x08d9
            int r0 = r9.var(r6)
            r1 = 25
            r11.visitVarInsn(r1, r0)
            java.lang.String r0 = JSONLexerBase
            r12 = 180(0xb4, float:2.52E-43)
            r11.visitFieldInsn(r12, r0, r2, r4)
            r0 = 178(0xb2, float:2.5E-43)
            java.lang.String r1 = JSONLexerBase
            java.lang.String r7 = "END"
            r11.visitFieldInsn(r0, r1, r7, r4)
            r0 = 160(0xa0, float:2.24E-43)
            r11.visitJumpInsn(r0, r3)
        L_0x08d9:
            r24 = r2
            r12 = r3
            r18 = r4
            r20 = r5
            r27 = r6
            r13 = r10
            r7 = 25
            r10 = 182(0xb6, float:2.55E-43)
            r14 = 1
            r16 = 180(0xb4, float:2.52E-43)
            r17 = 54
            goto L_0x095f
        L_0x08ee:
            r1 = r24
            r3 = r25
            r13 = 182(0xb6, float:2.55E-43)
            r14 = 58
            r18 = 54
            r19 = 180(0xb4, float:2.52E-43)
            r15 = r28
            r0 = r28
            r13 = r1
            r14 = 3
            r16 = 180(0xb4, float:2.52E-43)
            r21 = 25
            r1 = r30
            r24 = r2
            r14 = 2
            r2 = r11
            r14 = 1
            r17 = 54
            r18 = r4
            r4 = r7
            r20 = r5
            r7 = 182(0xb6, float:2.55E-43)
            r5 = r12
            r27 = r6
            r6 = r10
            r10 = 182(0xb6, float:2.55E-43)
            r12 = 25
            r7 = r8
            r0._deserialze_list_obj(r1, r2, r3, r4, r5, r6, r7)
            int r0 = r26 + -1
            if (r8 != r0) goto L_0x092b
            r6 = r25
            r15._deserialize_endCheck(r9, r11, r6)
            r12 = r6
            goto L_0x092d
        L_0x092b:
            r12 = r25
        L_0x092d:
            r7 = 25
            goto L_0x095f
        L_0x0930:
            r14 = 1
            r16 = 180(0xb4, float:2.52E-43)
            r17 = 54
            r15 = r28
            r18 = r4
            r20 = r5
            r27 = r6
            r26 = r10
            r13 = r24
            r6 = r25
            r5 = 25
            r10 = 182(0xb6, float:2.55E-43)
            r24 = r2
            r0 = r28
            r1 = r30
            r2 = r11
            r3 = r6
            r4 = r7
            r7 = 25
            r5 = r12
            r12 = r6
            r6 = r8
            r0._deserialze_obj(r1, r2, r3, r4, r5, r6)
            int r0 = r26 + -1
            if (r8 != r0) goto L_0x095f
            r15._deserialize_endCheck(r9, r11, r12)
        L_0x095f:
            int r8 = r8 + 1
            r25 = r12
            r4 = r18
            r5 = r20
            r2 = r24
            r10 = r26
            r6 = r27
            r3 = 3
            r24 = r13
            goto L_0x03c4
        L_0x0972:
            r13 = r24
            r12 = r25
            r7 = 25
            r10 = 182(0xb6, float:2.55E-43)
            r14 = 1
            r15 = r28
            r11.visitLabel(r13)
            java.lang.Class r0 = r30.clazz
            boolean r0 = r0.isInterface()
            if (r0 != 0) goto L_0x099b
            java.lang.Class r0 = r30.clazz
            int r0 = r0.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isAbstract(r0)
            if (r0 != 0) goto L_0x099b
            r15._batchSet(r9, r11)
        L_0x099b:
            r0 = r23
            r11.visitLabel(r0)
            r15._setContext(r9, r11)
            java.lang.String r0 = "instance"
            int r0 = r9.var(r0)
            r11.visitVarInsn(r7, r0)
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r30.beanInfo
            java.lang.reflect.Method r0 = r0.buildMethod
            if (r0 == 0) goto L_0x09dc
            java.lang.Class r1 = r30.getInstClass()
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.type(r1)
            java.lang.String r2 = r0.getName()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "()"
            r3.append(r4)
            java.lang.Class r0 = r0.getReturnType()
            java.lang.String r0 = com.alibaba.fastjson.util.ASMUtils.desc((java.lang.Class<?>) r0)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r11.visitMethodInsn(r10, r1, r2, r0)
        L_0x09dc:
            r0 = 176(0xb0, float:2.47E-43)
            r11.visitInsn(r0)
            r11.visitLabel(r12)
            r15._batchSet(r9, r11)
            r0 = 0
            r11.visitVarInsn(r7, r0)
            r11.visitVarInsn(r7, r14)
            r0 = 2
            r11.visitVarInsn(r7, r0)
            r0 = 3
            r11.visitVarInsn(r7, r0)
            java.lang.String r0 = "instance"
            int r0 = r9.var(r0)
            r11.visitVarInsn(r7, r0)
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer> r0 = com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.class
            java.lang.String r0 = com.alibaba.fastjson.util.ASMUtils.type(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "(L"
            r1.append(r2)
            java.lang.String r2 = DefaultJSONParser
            r1.append(r2)
            java.lang.String r2 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "parseRest"
            r11.visitMethodInsn(r10, r0, r2, r1)
            r0 = 192(0xc0, float:2.69E-43)
            java.lang.Class r1 = r30.clazz
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.type(r1)
            r11.visitTypeInsn(r0, r1)
            r0 = 176(0xb0, float:2.47E-43)
            r11.visitInsn(r0)
            r0 = r22
            r11.visitLabel(r0)
            r0 = 0
            r11.visitVarInsn(r7, r0)
            r11.visitVarInsn(r7, r14)
            r0 = 2
            r11.visitVarInsn(r7, r0)
            r0 = 3
            r11.visitVarInsn(r7, r0)
            r0 = 183(0xb7, float:2.56E-43)
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer> r1 = com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer.class
            java.lang.String r1 = com.alibaba.fastjson.util.ASMUtils.type(r1)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "(L"
            r2.append(r3)
            java.lang.String r3 = DefaultJSONParser
            r2.append(r3)
            java.lang.String r3 = ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "deserialze"
            r11.visitMethodInsn(r0, r1, r3, r2)
            r0 = 176(0xb0, float:2.47E-43)
            r11.visitInsn(r0)
            r0 = 5
            int r1 = r30.variantIndex
            r11.visitMaxs(r0, r1)
            r11.visitEnd()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory._deserialze(com.alibaba.fastjson.asm.ClassWriter, com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory$Context):void");
    }

    private void _isEnable(Context context, MethodVisitor methodVisitor, Feature feature) {
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, ASMUtils.type(Feature.class), feature.name(), ASMUtils.desc((Class<?>) Feature.class));
        String str = JSONLexerBase;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "isEnabled", "(" + ASMUtils.desc((Class<?>) Feature.class) + ")Z");
    }

    private void defineVarLexer(Context context, MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, DefaultJSONParser, "lexer", ASMUtils.desc((Class<?>) JSONLexer.class));
        methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, JSONLexerBase);
        methodVisitor.visitVarInsn(58, context.var("lexer"));
    }

    private void _createInstance(Context context, MethodVisitor methodVisitor) {
        Constructor<?> constructor = context.beanInfo.defaultConstructor;
        if (Modifier.isPublic(constructor.getModifiers())) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(context.getInstClass()));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(constructor.getDeclaringClass()), "<init>", "()V");
            methodVisitor.visitVarInsn(58, context.var("instance"));
            return;
        }
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitVarInsn(25, 1);
        String type = ASMUtils.type(ASMJavaBeanDeserializer.class);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, type, "createInstance", "(L" + DefaultJSONParser + ";)Ljava/lang/Object;");
        methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(context.getInstClass()));
        methodVisitor.visitVarInsn(58, context.var("instance"));
    }

    private void _batchSet(Context context, MethodVisitor methodVisitor) {
        _batchSet(context, methodVisitor, true);
    }

    private void _batchSet(Context context, MethodVisitor methodVisitor, boolean z) {
        int length = context.fieldInfoList.length;
        for (int i = 0; i < length; i++) {
            Label label = new Label();
            if (z) {
                _isFlag(methodVisitor, context, i, label);
            }
            _loadAndSet(context, methodVisitor, context.fieldInfoList[i]);
            if (z) {
                methodVisitor.visitLabel(label);
            }
        }
    }

    private void _loadAndSet(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo) {
        Class<?> cls = fieldInfo.fieldClass;
        Type type = fieldInfo.fieldType;
        if (cls == Boolean.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(21, context.var(fieldInfo.name + "_asm"));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == Byte.TYPE || cls == Short.TYPE || cls == Integer.TYPE || cls == Character.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(21, context.var(fieldInfo.name + "_asm"));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == Long.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(22, context.var(fieldInfo.name + "_asm", 2));
            if (fieldInfo.method != null) {
                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(context.getInstClass()), fieldInfo.method.getName(), ASMUtils.desc(fieldInfo.method));
                if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                    methodVisitor.visitInsn(87);
                    return;
                }
                return;
            }
            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
        } else if (cls == Float.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(23, context.var(fieldInfo.name + "_asm"));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == Double.TYPE) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(24, context.var(fieldInfo.name + "_asm", 2));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls == String.class) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            _set(context, methodVisitor, fieldInfo);
        } else if (cls.isEnum()) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            _set(context, methodVisitor, fieldInfo);
        } else if (Collection.class.isAssignableFrom(cls)) {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            if (TypeUtils.getCollectionItemClass(type) == String.class) {
                methodVisitor.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
                methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
            } else {
                methodVisitor.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            }
            _set(context, methodVisitor, fieldInfo);
        } else {
            methodVisitor.visitVarInsn(25, context.var("instance"));
            methodVisitor.visitVarInsn(25, context.var(fieldInfo.name + "_asm"));
            _set(context, methodVisitor, fieldInfo);
        }
    }

    private void _set(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo) {
        if (fieldInfo.method != null) {
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.method.getName(), ASMUtils.desc(fieldInfo.method));
            if (!fieldInfo.method.getReturnType().equals(Void.TYPE)) {
                methodVisitor.visitInsn(87);
                return;
            }
            return;
        }
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(fieldInfo.declaringClass), fieldInfo.field.getName(), ASMUtils.desc(fieldInfo.fieldClass));
    }

    private void _setContext(Context context, MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitVarInsn(25, context.var("context"));
        String str = DefaultJSONParser;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "setContext", "(" + ASMUtils.desc((Class<?>) ParseContext.class) + ")V");
        Label label = new Label();
        methodVisitor.visitVarInsn(25, context.var("childContext"));
        methodVisitor.visitJumpInsn(Opcodes.IFNULL, label);
        methodVisitor.visitVarInsn(25, context.var("childContext"));
        methodVisitor.visitVarInsn(25, context.var("instance"));
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(ParseContext.class), "object", "Ljava/lang/Object;");
        methodVisitor.visitLabel(label);
    }

    private void _deserialize_endCheck(Context context, MethodVisitor methodVisitor, Label label) {
        methodVisitor.visitIntInsn(21, context.var("matchedCount"));
        methodVisitor.visitJumpInsn(Opcodes.IFLE, label);
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "RBRACE", "I");
        methodVisitor.visitJumpInsn(160, label);
        methodVisitor.visitVarInsn(25, context.var("lexer"));
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "COMMA", "I");
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
    }

    private void _deserialze_list_obj(Context context, MethodVisitor methodVisitor, Label label, FieldInfo fieldInfo, Class<?> cls, Class<?> cls2, int i) {
        Context context2 = context;
        MethodVisitor methodVisitor2 = methodVisitor;
        Label label2 = label;
        FieldInfo fieldInfo2 = fieldInfo;
        Class<?> cls3 = cls;
        int i2 = i;
        Label label3 = new Label();
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "matchField", "([C)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFEQ, label3);
        _setFlag(methodVisitor2, context2, i2);
        Label label4 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "NULL", "I");
        methodVisitor2.visitJumpInsn(160, label4);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "COMMA", "I");
        String str = "COMMA";
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label3);
        methodVisitor2.visitLabel(label4);
        Label label5 = new Label();
        Label label6 = new Label();
        Label label7 = label3;
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "SET", "I");
        methodVisitor2.visitJumpInsn(160, label6);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "LBRACKET", "I");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "LBRACKET", "I");
        methodVisitor2.visitJumpInsn(160, label2);
        _newCollection(methodVisitor2, cls3, i2, true);
        Label label8 = label5;
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label8);
        methodVisitor2.visitLabel(label6);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "LBRACKET", "I");
        methodVisitor2.visitJumpInsn(160, label2);
        _newCollection(methodVisitor2, cls3, i2, false);
        methodVisitor2.visitLabel(label8);
        StringBuilder sb = new StringBuilder();
        FieldInfo fieldInfo3 = fieldInfo;
        sb.append(fieldInfo3.name);
        sb.append("_asm");
        methodVisitor2.visitVarInsn(58, context2.var(sb.toString()));
        _getCollectionFieldItemDeser(context2, methodVisitor2, fieldInfo3, cls2);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, ASMUtils.type(ObjectDeserializer.class), "getFastMatchToken", "()I");
        methodVisitor2.visitVarInsn(54, context2.var("fastMatchToken"));
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitVarInsn(21, context2.var("fastMatchToken"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        methodVisitor2.visitVarInsn(25, 1);
        String str2 = DefaultJSONParser;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str2, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitVarInsn(58, context2.var("listContext"));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var(fieldInfo3.name + "_asm"));
        methodVisitor2.visitLdcInsn(fieldInfo3.name);
        String str3 = DefaultJSONParser;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str3, "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)" + ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitInsn(87);
        Label label9 = new Label();
        Label label10 = new Label();
        methodVisitor2.visitInsn(3);
        methodVisitor2.visitVarInsn(54, context2.var("i"));
        methodVisitor2.visitLabel(label9);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "RBRACKET", "I");
        methodVisitor2.visitJumpInsn(Opcodes.IF_ICMPEQ, label10);
        methodVisitor2.visitVarInsn(25, 0);
        String access$300 = context.className;
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, access$300, fieldInfo3.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls2)));
        methodVisitor2.visitVarInsn(21, context2.var("i"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        String type = ASMUtils.type(ObjectDeserializer.class);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, type, "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        methodVisitor2.visitVarInsn(58, context2.var("list_item_value"));
        methodVisitor2.visitIincInsn(context2.var("i"), 1);
        methodVisitor2.visitVarInsn(25, context2.var(fieldInfo3.name + "_asm"));
        methodVisitor2.visitVarInsn(25, context2.var("list_item_value"));
        if (cls.isInterface()) {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEINTERFACE, ASMUtils.type(cls), "add", "(Ljava/lang/Object;)Z");
        } else {
            methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(cls), "add", "(Ljava/lang/Object;)Z");
        }
        methodVisitor2.visitInsn(87);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var(fieldInfo3.name + "_asm"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "checkListResolve", "(Ljava/util/Collection;)V");
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        String str4 = str;
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, str4, "I");
        methodVisitor2.visitJumpInsn(160, label9);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitVarInsn(21, context2.var("fastMatchToken"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label9);
        methodVisitor2.visitLabel(label10);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitVarInsn(25, context2.var("listContext"));
        String str5 = DefaultJSONParser;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str5, "setContext", "(" + ASMUtils.desc((Class<?>) ParseContext.class) + ")V");
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "token", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, "RBRACKET", "I");
        methodVisitor2.visitJumpInsn(160, label);
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, JSONToken, str4, "I");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "nextToken", "(I)V");
        methodVisitor2.visitLabel(label7);
    }

    private void _getCollectionFieldItemDeser(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo, Class<?> cls) {
        Label label = new Label();
        methodVisitor.visitVarInsn(25, 0);
        String access$300 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, access$300, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitVarInsn(25, 1);
        String str = DefaultJSONParser;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        methodVisitor.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(cls)));
        String type = ASMUtils.type(ParserConfig.class);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "getDeserializer", "(Ljava/lang/reflect/Type;)" + ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        String access$3002 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, access$3002, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitLabel(label);
        methodVisitor.visitVarInsn(25, 0);
        String access$3003 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, access$3003, fieldInfo.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
    }

    private void _newCollection(MethodVisitor methodVisitor, Class<?> cls, int i, boolean z) {
        if (cls.isAssignableFrom(ArrayList.class) && !z) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
        } else if (cls.isAssignableFrom(LinkedList.class) && !z) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(LinkedList.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(LinkedList.class), "<init>", "()V");
        } else if (cls.isAssignableFrom(HashSet.class)) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(HashSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(HashSet.class), "<init>", "()V");
        } else if (cls.isAssignableFrom(TreeSet.class)) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(TreeSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(TreeSet.class), "<init>", "()V");
        } else if (cls.isAssignableFrom(LinkedHashSet.class)) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(LinkedHashSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(LinkedHashSet.class), "<init>", "()V");
        } else if (z) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ASMUtils.type(HashSet.class));
            methodVisitor.visitInsn(89);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(HashSet.class), "<init>", "()V");
        } else {
            methodVisitor.visitVarInsn(25, 0);
            methodVisitor.visitLdcInsn(Integer.valueOf(i));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(ASMJavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, ASMUtils.type(TypeUtils.class), "createCollection", "(Ljava/lang/reflect/Type;)Ljava/util/Collection;");
        }
        methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
    }

    private void _deserialze_obj(Context context, MethodVisitor methodVisitor, Label label, FieldInfo fieldInfo, Class<?> cls, int i) {
        Context context2 = context;
        MethodVisitor methodVisitor2 = methodVisitor;
        FieldInfo fieldInfo2 = fieldInfo;
        Label label2 = new Label();
        Label label3 = new Label();
        methodVisitor2.visitVarInsn(25, context2.var("lexer"));
        methodVisitor2.visitVarInsn(25, 0);
        String access$300 = context.className;
        methodVisitor2.visitFieldInsn(Opcodes.GETFIELD, access$300, fieldInfo2.name + "_asm_prefix__", "[C");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, JSONLexerBase, "matchField", "([C)Z");
        methodVisitor2.visitJumpInsn(Opcodes.IFNE, label2);
        methodVisitor2.visitInsn(1);
        methodVisitor2.visitVarInsn(58, context2.var(fieldInfo2.name + "_asm"));
        methodVisitor2.visitJumpInsn(Opcodes.GOTO, label3);
        methodVisitor2.visitLabel(label2);
        int i2 = i;
        _setFlag(methodVisitor2, context2, i2);
        methodVisitor2.visitVarInsn(21, context2.var("matchedCount"));
        methodVisitor2.visitInsn(4);
        methodVisitor2.visitInsn(96);
        methodVisitor2.visitVarInsn(54, context2.var("matchedCount"));
        _deserObject(context, methodVisitor, fieldInfo, cls, i2);
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "getResolveStatus", "()I");
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, DefaultJSONParser, "NeedToResolve", "I");
        methodVisitor2.visitJumpInsn(160, label3);
        methodVisitor2.visitVarInsn(25, 1);
        String str = DefaultJSONParser;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getLastResolveTask", "()" + ASMUtils.desc((Class<?>) DefaultJSONParser.ResolveTask.class));
        methodVisitor2.visitVarInsn(58, context2.var("resolveTask"));
        methodVisitor2.visitVarInsn(25, context2.var("resolveTask"));
        methodVisitor2.visitVarInsn(25, 1);
        String str2 = DefaultJSONParser;
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str2, "getContext", "()" + ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(DefaultJSONParser.ResolveTask.class), "ownerContext", ASMUtils.desc((Class<?>) ParseContext.class));
        methodVisitor2.visitVarInsn(25, context2.var("resolveTask"));
        methodVisitor2.visitVarInsn(25, 0);
        methodVisitor2.visitLdcInsn(fieldInfo2.name);
        String type = ASMUtils.type(ASMJavaBeanDeserializer.class);
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "getFieldDeserializer", "(Ljava/lang/String;)" + ASMUtils.desc((Class<?>) FieldDeserializer.class));
        methodVisitor2.visitFieldInsn(Opcodes.PUTFIELD, ASMUtils.type(DefaultJSONParser.ResolveTask.class), "fieldDeserializer", ASMUtils.desc((Class<?>) FieldDeserializer.class));
        methodVisitor2.visitVarInsn(25, 1);
        methodVisitor2.visitFieldInsn(Opcodes.GETSTATIC, DefaultJSONParser, "NONE", "I");
        methodVisitor2.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DefaultJSONParser, "setResolveStatus", "(I)V");
        methodVisitor2.visitLabel(label3);
    }

    private void _deserObject(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo, Class<?> cls, int i) {
        _getFieldDeser(context, methodVisitor, fieldInfo);
        methodVisitor.visitVarInsn(25, 1);
        if (fieldInfo.fieldType instanceof Class) {
            methodVisitor.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo.fieldClass)));
        } else {
            methodVisitor.visitVarInsn(25, 0);
            methodVisitor.visitLdcInsn(Integer.valueOf(i));
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ASMUtils.type(ASMJavaBeanDeserializer.class), "getFieldType", "(I)Ljava/lang/reflect/Type;");
        }
        methodVisitor.visitLdcInsn(fieldInfo.name);
        String type = ASMUtils.type(ObjectDeserializer.class);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, type, "deserialze", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, ASMUtils.type(cls));
        methodVisitor.visitVarInsn(58, context.var(fieldInfo.name + "_asm"));
    }

    private void _getFieldDeser(Context context, MethodVisitor methodVisitor, FieldInfo fieldInfo) {
        Label label = new Label();
        methodVisitor.visitVarInsn(25, 0);
        String access$300 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, access$300, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, label);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitVarInsn(25, 1);
        String str = DefaultJSONParser;
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, str, "getConfig", "()" + ASMUtils.desc((Class<?>) ParserConfig.class));
        methodVisitor.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.desc(fieldInfo.fieldClass)));
        String type = ASMUtils.type(ParserConfig.class);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, type, "getDeserializer", "(Ljava/lang/reflect/Type;)" + ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        String access$3002 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, access$3002, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
        methodVisitor.visitLabel(label);
        methodVisitor.visitVarInsn(25, 0);
        String access$3003 = context.className;
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, access$3003, fieldInfo.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class));
    }

    static class Context {
        /* access modifiers changed from: private */
        public final JavaBeanInfo beanInfo;
        /* access modifiers changed from: private */
        public final String className;
        /* access modifiers changed from: private */
        public final Class<?> clazz;
        /* access modifiers changed from: private */
        public FieldInfo[] fieldInfoList;
        /* access modifiers changed from: private */
        public int variantIndex = 5;
        private final Map<String, Integer> variants = new HashMap();

        public Context(String str, ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, int i) {
            this.className = str;
            this.clazz = javaBeanInfo.clazz;
            this.variantIndex = i;
            this.beanInfo = javaBeanInfo;
            this.fieldInfoList = javaBeanInfo.fields;
        }

        public Class<?> getInstClass() {
            Class<?> cls = this.beanInfo.builderClass;
            return cls == null ? this.clazz : cls;
        }

        public int var(String str, int i) {
            if (this.variants.get(str) == null) {
                this.variants.put(str, Integer.valueOf(this.variantIndex));
                this.variantIndex += i;
            }
            return this.variants.get(str).intValue();
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
    }

    private void _init(ClassWriter classWriter, Context context) {
        ClassWriter classWriter2 = classWriter;
        for (FieldInfo fieldInfo : context.fieldInfoList) {
            new FieldWriter(classWriter, 1, fieldInfo.name + "_asm_prefix__", "[C").visitEnd();
        }
        for (FieldInfo fieldInfo2 : context.fieldInfoList) {
            Class<?> cls = fieldInfo2.fieldClass;
            if (!cls.isPrimitive() && !cls.isEnum()) {
                if (Collection.class.isAssignableFrom(cls)) {
                    new FieldWriter(classWriter, 1, fieldInfo2.name + "_asm_list_item_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class)).visitEnd();
                } else {
                    new FieldWriter(classWriter, 1, fieldInfo2.name + "_asm_deser__", ASMUtils.desc((Class<?>) ObjectDeserializer.class)).visitEnd();
                }
            }
        }
        MethodWriter methodWriter = new MethodWriter(classWriter, 1, "<init>", "(" + ASMUtils.desc((Class<?>) ParserConfig.class) + "Ljava/lang/Class;)V", (String) null, (String[]) null);
        methodWriter.visitVarInsn(25, 0);
        methodWriter.visitVarInsn(25, 1);
        methodWriter.visitVarInsn(25, 2);
        methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(ASMJavaBeanDeserializer.class), "<init>", "(" + ASMUtils.desc((Class<?>) ParserConfig.class) + "Ljava/lang/Class;)V");
        for (FieldInfo fieldInfo3 : context.fieldInfoList) {
            methodWriter.visitVarInsn(25, 0);
            methodWriter.visitLdcInsn("\"" + fieldInfo3.name + "\":");
            methodWriter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C");
            methodWriter.visitFieldInsn(Opcodes.PUTFIELD, context.className, fieldInfo3.name + "_asm_prefix__", "[C");
        }
        methodWriter.visitInsn(Opcodes.RETURN);
        methodWriter.visitMaxs(4, 4);
        methodWriter.visitEnd();
    }

    private void _createInstance(ClassWriter classWriter, Context context) {
        MethodWriter methodWriter = new MethodWriter(classWriter, 1, "createInstance", "(L" + DefaultJSONParser + ";Ljava/lang/reflect/Type;)Ljava/lang/Object;", (String) null, (String[]) null);
        methodWriter.visitTypeInsn(Opcodes.NEW, ASMUtils.type(context.getInstClass()));
        methodWriter.visitInsn(89);
        methodWriter.visitMethodInsn(Opcodes.INVOKESPECIAL, ASMUtils.type(context.getInstClass()), "<init>", "()V");
        methodWriter.visitInsn(Opcodes.ARETURN);
        methodWriter.visitMaxs(3, 3);
        methodWriter.visitEnd();
    }
}
