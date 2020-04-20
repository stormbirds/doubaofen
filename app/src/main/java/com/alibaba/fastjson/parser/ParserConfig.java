package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.serializer.AtomicCodec;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharArrayCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CharsetCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.CurrencyCodec;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.alibaba.fastjson.serializer.ReferenceCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.ServiceLoader;
import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessControlException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import kotlin.text.Typography;

public class ParserConfig {
    public static final String DENY_PROPERTY = "fastjson.parser.deny";
    private static boolean awtError = false;
    public static ParserConfig global = new ParserConfig();
    private static boolean jdk8Error = false;
    private boolean asmEnable;
    protected ASMDeserializerFactory asmFactory;
    protected ClassLoader defaultClassLoader;
    private String[] denyList;
    private final IdentityHashMap<Type, ObjectDeserializer> derializers;
    public final SymbolTable symbolTable;

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public ParserConfig() {
        this((ASMDeserializerFactory) null, (ClassLoader) null);
    }

    public ParserConfig(ClassLoader classLoader) {
        this((ASMDeserializerFactory) null, classLoader);
    }

    public ParserConfig(ASMDeserializerFactory aSMDeserializerFactory) {
        this(aSMDeserializerFactory, (ClassLoader) null);
    }

    private ParserConfig(ASMDeserializerFactory aSMDeserializerFactory, ClassLoader classLoader) {
        this.derializers = new IdentityHashMap<>();
        this.asmEnable = !ASMUtils.IS_ANDROID;
        this.symbolTable = new SymbolTable(4096);
        this.denyList = new String[]{"java.lang.Thread"};
        if (aSMDeserializerFactory == null && !ASMUtils.IS_ANDROID) {
            if (classLoader == null) {
                try {
                    aSMDeserializerFactory = new ASMDeserializerFactory(new ASMClassLoader());
                } catch (ExceptionInInitializerError | NoClassDefFoundError | AccessControlException unused) {
                }
            } else {
                aSMDeserializerFactory = new ASMDeserializerFactory(classLoader);
            }
        }
        this.asmFactory = aSMDeserializerFactory;
        if (aSMDeserializerFactory == null) {
            this.asmEnable = false;
        }
        this.derializers.put(SimpleDateFormat.class, MiscCodec.instance);
        this.derializers.put(Timestamp.class, SqlDateDeserializer.instance_timestamp);
        this.derializers.put(Date.class, SqlDateDeserializer.instance);
        this.derializers.put(Time.class, TimeDeserializer.instance);
        this.derializers.put(java.util.Date.class, DateCodec.instance);
        this.derializers.put(Calendar.class, CalendarCodec.instance);
        this.derializers.put(JSONObject.class, MapDeserializer.instance);
        this.derializers.put(JSONArray.class, CollectionCodec.instance);
        this.derializers.put(Map.class, MapDeserializer.instance);
        this.derializers.put(HashMap.class, MapDeserializer.instance);
        this.derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        this.derializers.put(TreeMap.class, MapDeserializer.instance);
        this.derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        this.derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);
        this.derializers.put(Collection.class, CollectionCodec.instance);
        this.derializers.put(List.class, CollectionCodec.instance);
        this.derializers.put(ArrayList.class, CollectionCodec.instance);
        this.derializers.put(Object.class, JavaObjectDeserializer.instance);
        this.derializers.put(String.class, StringCodec.instance);
        this.derializers.put(StringBuffer.class, StringCodec.instance);
        this.derializers.put(StringBuilder.class, StringCodec.instance);
        this.derializers.put(Character.TYPE, CharacterCodec.instance);
        this.derializers.put(Character.class, CharacterCodec.instance);
        this.derializers.put(Byte.TYPE, NumberDeserializer.instance);
        this.derializers.put(Byte.class, NumberDeserializer.instance);
        this.derializers.put(Short.TYPE, NumberDeserializer.instance);
        this.derializers.put(Short.class, NumberDeserializer.instance);
        this.derializers.put(Integer.TYPE, IntegerCodec.instance);
        this.derializers.put(Integer.class, IntegerCodec.instance);
        this.derializers.put(Long.TYPE, LongCodec.instance);
        this.derializers.put(Long.class, LongCodec.instance);
        this.derializers.put(BigInteger.class, BigIntegerCodec.instance);
        this.derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        this.derializers.put(Float.TYPE, FloatCodec.instance);
        this.derializers.put(Float.class, FloatCodec.instance);
        this.derializers.put(Double.TYPE, NumberDeserializer.instance);
        this.derializers.put(Double.class, NumberDeserializer.instance);
        this.derializers.put(Boolean.TYPE, BooleanCodec.instance);
        this.derializers.put(Boolean.class, BooleanCodec.instance);
        this.derializers.put(Class.class, MiscCodec.instance);
        this.derializers.put(char[].class, CharArrayCodec.instance);
        this.derializers.put(AtomicBoolean.class, BooleanCodec.instance);
        this.derializers.put(AtomicInteger.class, IntegerCodec.instance);
        this.derializers.put(AtomicLong.class, LongCodec.instance);
        this.derializers.put(AtomicReference.class, ReferenceCodec.instance);
        this.derializers.put(WeakReference.class, ReferenceCodec.instance);
        this.derializers.put(SoftReference.class, ReferenceCodec.instance);
        this.derializers.put(UUID.class, MiscCodec.instance);
        this.derializers.put(TimeZone.class, MiscCodec.instance);
        this.derializers.put(Locale.class, MiscCodec.instance);
        this.derializers.put(Currency.class, CurrencyCodec.instance);
        this.derializers.put(InetAddress.class, MiscCodec.instance);
        this.derializers.put(Inet4Address.class, MiscCodec.instance);
        this.derializers.put(Inet6Address.class, MiscCodec.instance);
        this.derializers.put(InetSocketAddress.class, MiscCodec.instance);
        this.derializers.put(File.class, MiscCodec.instance);
        this.derializers.put(URI.class, MiscCodec.instance);
        this.derializers.put(URL.class, MiscCodec.instance);
        this.derializers.put(Pattern.class, MiscCodec.instance);
        this.derializers.put(Charset.class, CharsetCodec.instance);
        this.derializers.put(Number.class, NumberDeserializer.instance);
        this.derializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        this.derializers.put(AtomicLongArray.class, AtomicCodec.instance);
        this.derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);
        this.derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Closeable.class, JavaObjectDeserializer.instance);
        if (!awtError) {
            try {
                this.derializers.put(Class.forName("java.awt.Point"), AwtCodec.instance);
                this.derializers.put(Class.forName("java.awt.Font"), AwtCodec.instance);
                this.derializers.put(Class.forName("java.awt.Rectangle"), AwtCodec.instance);
                this.derializers.put(Class.forName("java.awt.Color"), AwtCodec.instance);
            } catch (Throwable unused2) {
                awtError = true;
            }
        }
        if (!jdk8Error) {
            try {
                this.derializers.put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.ZoneId"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
                this.derializers.put(Class.forName("java.util.Optional"), OptionalCodec.instance);
                this.derializers.put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
                this.derializers.put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
                this.derializers.put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
            } catch (Throwable unused3) {
                jdk8Error = true;
            }
        }
        addDeny("java.lang.Thread");
        configFromPropety(System.getProperties());
    }

    public void configFromPropety(Properties properties) {
        String property = properties.getProperty(DENY_PROPERTY);
        if (property != null && property.length() > 0) {
            String[] split = property.split(",");
            for (String addDeny : split) {
                addDeny(addDeny);
            }
        }
    }

    public boolean isAsmEnable() {
        return this.asmEnable;
    }

    public void setAsmEnable(boolean z) {
        this.asmEnable = z;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return this.derializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer objectDeserializer = this.derializers.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        if (type instanceof Class) {
            return getDeserializer((Class) type, type);
        }
        if (!(type instanceof ParameterizedType)) {
            return JavaObjectDeserializer.instance;
        }
        Type rawType = ((ParameterizedType) type).getRawType();
        if (rawType instanceof Class) {
            return getDeserializer((Class) rawType, type);
        }
        return getDeserializer(rawType);
    }

    public ObjectDeserializer getDeserializer(Class<?> cls, Type type) {
        ObjectDeserializer objectDeserializer;
        Class<?> mappingTo;
        ObjectDeserializer objectDeserializer2 = this.derializers.get(type);
        if (objectDeserializer2 != null) {
            return objectDeserializer2;
        }
        if (type == null) {
            type = cls;
        }
        ObjectDeserializer objectDeserializer3 = this.derializers.get(type);
        if (objectDeserializer3 != null) {
            return objectDeserializer3;
        }
        JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
        if (jSONType != null && (mappingTo = jSONType.mappingTo()) != Void.class) {
            return getDeserializer(mappingTo, mappingTo);
        }
        if ((type instanceof WildcardType) || (type instanceof TypeVariable) || (type instanceof ParameterizedType)) {
            objectDeserializer3 = this.derializers.get(cls);
        }
        if (objectDeserializer3 != null) {
            return objectDeserializer3;
        }
        int i = 0;
        while (true) {
            String[] strArr = this.denyList;
            if (i < strArr.length) {
                String str = strArr[i];
                String replace = cls.getName().replace(Typography.dollar, '.');
                if (!replace.startsWith(str)) {
                    i++;
                } else {
                    throw new JSONException("parser deny : " + replace);
                }
            } else {
                try {
                    for (AutowiredObjectDeserializer next : ServiceLoader.load(AutowiredObjectDeserializer.class, Thread.currentThread().getContextClassLoader())) {
                        for (Type put : next.getAutowiredFor()) {
                            this.derializers.put(put, next);
                        }
                    }
                } catch (Exception unused) {
                }
                ObjectDeserializer objectDeserializer4 = this.derializers.get(type);
                if (objectDeserializer4 != null) {
                    return objectDeserializer4;
                }
                if (cls.isEnum()) {
                    objectDeserializer = new EnumDeserializer(cls);
                } else if (cls.isArray()) {
                    objectDeserializer = ObjectArrayCodec.instance;
                } else if (cls == Set.class || cls == HashSet.class || cls == Collection.class || cls == List.class || cls == ArrayList.class) {
                    objectDeserializer = CollectionCodec.instance;
                } else if (Collection.class.isAssignableFrom(cls)) {
                    objectDeserializer = CollectionCodec.instance;
                } else if (Map.class.isAssignableFrom(cls)) {
                    objectDeserializer = MapDeserializer.instance;
                } else if (Throwable.class.isAssignableFrom(cls)) {
                    objectDeserializer = new ThrowableDeserializer(this, cls);
                } else {
                    objectDeserializer = createJavaBeanDeserializer(cls, type);
                }
                putDeserializer(type, objectDeserializer);
                return objectDeserializer;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0086, code lost:
        r0 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.fastjson.parser.deserializer.ObjectDeserializer createJavaBeanDeserializer(java.lang.Class<?> r9, java.lang.reflect.Type r10) {
        /*
            r8 = this;
            boolean r0 = r8.asmEnable
            r1 = 0
            if (r0 == 0) goto L_0x0035
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r2 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r2 = r9.getAnnotation(r2)
            com.alibaba.fastjson.annotation.JSONType r2 = (com.alibaba.fastjson.annotation.JSONType) r2
            if (r2 == 0) goto L_0x0016
            boolean r3 = r2.asm()
            if (r3 != 0) goto L_0x0016
            r0 = 0
        L_0x0016:
            if (r0 == 0) goto L_0x0035
            java.lang.Class r2 = com.alibaba.fastjson.util.JavaBeanInfo.getBuilderClass(r2)
            if (r2 != 0) goto L_0x001f
            r2 = r9
        L_0x001f:
            int r3 = r2.getModifiers()
            boolean r3 = java.lang.reflect.Modifier.isPublic(r3)
            if (r3 != 0) goto L_0x002b
            r0 = 0
            goto L_0x0035
        L_0x002b:
            java.lang.Class r2 = r2.getSuperclass()
            java.lang.Class<java.lang.Object> r3 = java.lang.Object.class
            if (r2 == r3) goto L_0x0035
            if (r2 != 0) goto L_0x001f
        L_0x0035:
            java.lang.reflect.TypeVariable[] r2 = r9.getTypeParameters()
            int r2 = r2.length
            if (r2 == 0) goto L_0x003d
            r0 = 0
        L_0x003d:
            if (r0 == 0) goto L_0x004c
            com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory r2 = r8.asmFactory
            if (r2 == 0) goto L_0x004c
            com.alibaba.fastjson.util.ASMClassLoader r2 = r2.classLoader
            boolean r2 = r2.isExternalClass(r9)
            if (r2 == 0) goto L_0x004c
            r0 = 0
        L_0x004c:
            if (r0 == 0) goto L_0x0056
            java.lang.String r0 = r9.getName()
            boolean r0 = com.alibaba.fastjson.util.ASMUtils.checkName(r0)
        L_0x0056:
            if (r0 == 0) goto L_0x00de
            boolean r2 = r9.isInterface()
            if (r2 == 0) goto L_0x005f
            r0 = 0
        L_0x005f:
            com.alibaba.fastjson.util.JavaBeanInfo r2 = com.alibaba.fastjson.util.JavaBeanInfo.build(r9, r10)
            if (r0 == 0) goto L_0x006d
            com.alibaba.fastjson.util.FieldInfo[] r3 = r2.fields
            int r3 = r3.length
            r4 = 200(0xc8, float:2.8E-43)
            if (r3 <= r4) goto L_0x006d
            r0 = 0
        L_0x006d:
            java.lang.reflect.Constructor<?> r3 = r2.defaultConstructor
            if (r0 == 0) goto L_0x007a
            if (r3 != 0) goto L_0x007a
            boolean r3 = r9.isInterface()
            if (r3 != 0) goto L_0x007a
            r0 = 0
        L_0x007a:
            com.alibaba.fastjson.util.FieldInfo[] r2 = r2.fields
            int r3 = r2.length
            r4 = 0
        L_0x007e:
            if (r4 >= r3) goto L_0x00de
            r5 = r2[r4]
            boolean r6 = r5.getOnly
            if (r6 == 0) goto L_0x0088
        L_0x0086:
            r0 = 0
            goto L_0x00de
        L_0x0088:
            java.lang.Class<?> r6 = r5.fieldClass
            int r7 = r6.getModifiers()
            boolean r7 = java.lang.reflect.Modifier.isPublic(r7)
            if (r7 != 0) goto L_0x0095
            goto L_0x0086
        L_0x0095:
            boolean r7 = r6.isMemberClass()
            if (r7 == 0) goto L_0x00a6
            int r7 = r6.getModifiers()
            boolean r7 = java.lang.reflect.Modifier.isStatic(r7)
            if (r7 != 0) goto L_0x00a6
            goto L_0x0086
        L_0x00a6:
            java.lang.reflect.Member r7 = r5.getMember()
            if (r7 == 0) goto L_0x00bb
            java.lang.reflect.Member r7 = r5.getMember()
            java.lang.String r7 = r7.getName()
            boolean r7 = com.alibaba.fastjson.util.ASMUtils.checkName(r7)
            if (r7 != 0) goto L_0x00bb
            goto L_0x0086
        L_0x00bb:
            com.alibaba.fastjson.annotation.JSONField r5 = r5.getAnnotation()
            if (r5 == 0) goto L_0x00cc
            java.lang.String r5 = r5.name()
            boolean r5 = com.alibaba.fastjson.util.ASMUtils.checkName(r5)
            if (r5 != 0) goto L_0x00cc
            goto L_0x0086
        L_0x00cc:
            boolean r5 = r6.isEnum()
            if (r5 == 0) goto L_0x00db
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r8.getDeserializer((java.lang.reflect.Type) r6)
            boolean r5 = r5 instanceof com.alibaba.fastjson.parser.deserializer.EnumDeserializer
            if (r5 != 0) goto L_0x00db
            goto L_0x0086
        L_0x00db:
            int r4 = r4 + 1
            goto L_0x007e
        L_0x00de:
            if (r0 == 0) goto L_0x00f1
            boolean r2 = r9.isMemberClass()
            if (r2 == 0) goto L_0x00f1
            int r2 = r9.getModifiers()
            boolean r2 = java.lang.reflect.Modifier.isStatic(r2)
            if (r2 != 0) goto L_0x00f1
            r0 = 0
        L_0x00f1:
            if (r0 != 0) goto L_0x00f9
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = new com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            r0.<init>(r8, r9, r10)
            return r0
        L_0x00f9:
            com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory r0 = r8.asmFactory     // Catch:{ NoSuchMethodException -> 0x0122, JSONException -> 0x011c, Exception -> 0x0100 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r9 = r0.createJavaBeanDeserializer(r8, r9, r10)     // Catch:{ NoSuchMethodException -> 0x0122, JSONException -> 0x011c, Exception -> 0x0100 }
            return r9
        L_0x0100:
            r10 = move-exception
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "create asm deserializer error, "
            r1.append(r2)
            java.lang.String r9 = r9.getName()
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            r0.<init>(r9, r10)
            throw r0
        L_0x011c:
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = new com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            r0.<init>(r8, r9, r10)
            return r0
        L_0x0122:
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = new com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            r0.<init>(r8, r9, r10)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.ParserConfig.createJavaBeanDeserializer(java.lang.Class, java.lang.reflect.Type):com.alibaba.fastjson.parser.deserializer.ObjectDeserializer");
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, FieldInfo fieldInfo) {
        Class<?> cls = javaBeanInfo.clazz;
        Class<?> cls2 = fieldInfo.fieldClass;
        if (cls2 == List.class || cls2 == ArrayList.class) {
            return new ArrayListTypeFieldDeserializer(parserConfig, cls, fieldInfo);
        }
        return new DefaultFieldDeserializer(parserConfig, cls, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer objectDeserializer) {
        this.derializers.put(type, objectDeserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    public boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == Boolean.class || cls == Character.class || cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class || cls == BigInteger.class || cls == BigDecimal.class || cls == String.class || cls == java.util.Date.class || cls == Date.class || cls == Time.class || cls == Timestamp.class;
    }

    public static Field getField(Class<?> cls, String str) {
        Field field0 = getField0(cls, str);
        if (field0 == null) {
            field0 = getField0(cls, "_" + str);
        }
        if (field0 != null) {
            return field0;
        }
        return getField0(cls, "m_" + str);
    }

    private static Field getField0(Class<?> cls, String str) {
        for (Field field : cls.getDeclaredFields()) {
            if (str.equals(field.getName())) {
                return field;
            }
        }
        if (cls.getSuperclass() == null || cls.getSuperclass() == Object.class) {
            return null;
        }
        return getField(cls.getSuperclass(), str);
    }

    public ClassLoader getDefaultClassLoader() {
        return this.defaultClassLoader;
    }

    public void setDefaultClassLoader(ClassLoader classLoader) {
        this.defaultClassLoader = classLoader;
    }

    public void addDeny(String str) {
        if (str != null && str.length() != 0) {
            String[] strArr = this.denyList;
            String[] strArr2 = new String[(strArr.length + 1)];
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
            strArr2[strArr2.length - 1] = str;
            this.denyList = strArr2;
        }
    }
}
