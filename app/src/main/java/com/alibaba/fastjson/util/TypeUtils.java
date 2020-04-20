package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeUtils {
    public static boolean compatibleWithJavaBean = false;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap();
    private static Class<?> optionalClass = null;
    private static boolean optionalClassInited = false;
    private static Method oracleDateMethod = null;
    private static boolean oracleDateMethodInited = false;
    private static Method oracleTimestampMethod = null;
    private static boolean oracleTimestampMethodInited = false;
    private static boolean setAccessibleEnable = true;

    static {
        try {
            String property = System.getProperty("fastjson.compatibleWithJavaBean");
            if ("true".equals(property)) {
                compatibleWithJavaBean = true;
            } else if ("false".equals(property)) {
                compatibleWithJavaBean = false;
            }
        } catch (Throwable unused) {
        }
        addBaseClassMappings();
    }

    public static String castToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Byte castToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str));
        }
        throw new JSONException("can not cast to byte, value : " + obj);
    }

    public static Character castToChar(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0) {
                return null;
            }
            if (str.length() == 1) {
                return Character.valueOf(str.charAt(0));
            }
            throw new JSONException("can not cast to char, value : " + obj);
        }
        throw new JSONException("can not cast to char, value : " + obj);
    }

    public static Short castToShort(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str));
        }
        throw new JSONException("can not cast to short, value : " + obj);
    }

    public static BigDecimal castToBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0) {
            return null;
        }
        return new BigDecimal(obj2);
    }

    public static BigInteger castToBigInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }
        if ((obj instanceof Float) || (obj instanceof Double)) {
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0 || "null".equals(obj2) || "NULL".equals(obj2)) {
            return null;
        }
        return new BigInteger(obj2);
    }

    public static Float castToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || "null".equals(obj2) || "NULL".equals(obj2)) {
                return null;
            }
            if (obj2.indexOf(44) != 0) {
                obj2 = obj2.replaceAll(",", "");
            }
            return Float.valueOf(Float.parseFloat(obj2));
        }
        throw new JSONException("can not cast to float, value : " + obj);
    }

    public static Double castToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || "null".equals(obj2) || "NULL".equals(obj2)) {
                return null;
            }
            if (obj2.indexOf(44) != 0) {
                obj2 = obj2.replaceAll(",", "");
            }
            return Double.valueOf(Double.parseDouble(obj2));
        }
        throw new JSONException("can not cast to double, value : " + obj);
    }

    public static Date castToDate(Object obj) {
        String str;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof Calendar) {
            return ((Calendar) obj).getTime();
        }
        long j = -1;
        if (obj instanceof Number) {
            return new Date(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (str2.indexOf(45) != -1) {
                if (str2.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                    str = JSON.DEFFAULT_DATE_FORMAT;
                } else if (str2.length() == 10) {
                    str = "yyyy-MM-dd";
                } else {
                    str = str2.length() == 19 ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd HH:mm:ss.SSS";
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, JSON.defaultLocale);
                simpleDateFormat.setTimeZone(JSON.defaultTimeZone);
                try {
                    return simpleDateFormat.parse(str2);
                } catch (ParseException unused) {
                    throw new JSONException("can not cast to Date, value : " + str2);
                }
            } else if (str2.length() == 0) {
                return null;
            } else {
                j = Long.parseLong(str2);
            }
        }
        if (j >= 0) {
            return new Date(j);
        }
        Class<?> cls = obj.getClass();
        if ("oracle.sql.TIMESTAMP".equals(cls.getName())) {
            if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                try {
                    oracleTimestampMethod = cls.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException unused2) {
                } catch (Throwable th) {
                    oracleTimestampMethodInited = true;
                    throw th;
                }
                oracleTimestampMethodInited = true;
            }
            try {
                return (Date) oracleTimestampMethod.invoke(obj, new Object[0]);
            } catch (Exception e) {
                throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e);
            }
        } else if ("oracle.sql.DATE".equals(cls.getName())) {
            if (oracleDateMethod == null && !oracleDateMethodInited) {
                try {
                    oracleDateMethod = cls.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException unused3) {
                } catch (Throwable th2) {
                    oracleDateMethodInited = true;
                    throw th2;
                }
                oracleDateMethodInited = true;
            }
            try {
                return (Date) oracleDateMethod.invoke(obj, new Object[0]);
            } catch (Exception e2) {
                throw new JSONException("can not cast oracle.sql.DATE to Date", e2);
            }
        } else {
            throw new JSONException("can not cast to Date, value : " + obj);
        }
    }

    public static java.sql.Date castToSqlDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof java.sql.Date) {
            return (java.sql.Date) obj;
        }
        if (obj instanceof Date) {
            return new java.sql.Date(((Date) obj).getTime());
        }
        if (obj instanceof Calendar) {
            return new java.sql.Date(((Calendar) obj).getTimeInMillis());
        }
        long longValue = obj instanceof Number ? ((Number) obj).longValue() : 0;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            longValue = Long.parseLong(str);
        }
        if (longValue > 0) {
            return new java.sql.Date(longValue);
        }
        throw new JSONException("can not cast to Date, value : " + obj);
    }

    public static Timestamp castToTimestamp(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Calendar) {
            return new Timestamp(((Calendar) obj).getTimeInMillis());
        }
        if (obj instanceof Timestamp) {
            return (Timestamp) obj;
        }
        if (obj instanceof Date) {
            return new Timestamp(((Date) obj).getTime());
        }
        long longValue = obj instanceof Number ? ((Number) obj).longValue() : 0;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            longValue = Long.parseLong(str);
        }
        if (longValue > 0) {
            return new Timestamp(longValue);
        }
        throw new JSONException("can not cast to Date, value : " + obj);
    }

    public static Long castToLong(Object obj) {
        Calendar calendar = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (str.indexOf(44) != 0) {
                str = str.replaceAll(",", "");
            }
            try {
                return Long.valueOf(Long.parseLong(str));
            } catch (NumberFormatException unused) {
                JSONScanner jSONScanner = new JSONScanner(str);
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    calendar = jSONScanner.getCalendar();
                }
                jSONScanner.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        throw new JSONException("can not cast to long, value : " + obj);
    }

    public static Integer castToInt(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (str.indexOf(44) != 0) {
                str = str.replaceAll(",", "");
            }
            return Integer.valueOf(Integer.parseInt(str));
        } else if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else {
            throw new JSONException("can not cast to int, value : " + obj);
        }
    }

    public static byte[] castToBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return IOUtils.decodeFast((String) obj);
        }
        throw new JSONException("can not cast to int, value : " + obj);
    }

    public static Boolean castToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof Number) {
            boolean z = true;
            if (((Number) obj).intValue() != 1) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value : " + obj);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> cls) {
        return cast(obj, cls, ParserConfig.getGlobalInstance());
    }

    public static <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) {
        T t;
        if (obj == null) {
            return null;
        }
        if (cls == null) {
            throw new IllegalArgumentException("clazz is null");
        } else if (cls == obj.getClass()) {
            return obj;
        } else {
            if (!(obj instanceof Map)) {
                if (cls.isArray()) {
                    if (obj instanceof Collection) {
                        Collection<Object> collection = (Collection) obj;
                        int i = 0;
                        T newInstance = Array.newInstance(cls.getComponentType(), collection.size());
                        for (Object cast : collection) {
                            Array.set(newInstance, i, cast(cast, cls.getComponentType(), parserConfig));
                            i++;
                        }
                        return newInstance;
                    } else if (cls == byte[].class) {
                        return castToBytes(obj);
                    }
                }
                if (cls.isAssignableFrom(obj.getClass())) {
                    return obj;
                }
                if (cls == Boolean.TYPE || cls == Boolean.class) {
                    return castToBoolean(obj);
                }
                if (cls == Byte.TYPE || cls == Byte.class) {
                    return castToByte(obj);
                }
                if (cls == Short.TYPE || cls == Short.class) {
                    return castToShort(obj);
                }
                if (cls == Integer.TYPE || cls == Integer.class) {
                    return castToInt(obj);
                }
                if (cls == Long.TYPE || cls == Long.class) {
                    return castToLong(obj);
                }
                if (cls == Float.TYPE || cls == Float.class) {
                    return castToFloat(obj);
                }
                if (cls == Double.TYPE || cls == Double.class) {
                    return castToDouble(obj);
                }
                if (cls == String.class) {
                    return castToString(obj);
                }
                if (cls == BigDecimal.class) {
                    return castToBigDecimal(obj);
                }
                if (cls == BigInteger.class) {
                    return castToBigInteger(obj);
                }
                if (cls == Date.class) {
                    return castToDate(obj);
                }
                if (cls == java.sql.Date.class) {
                    return castToSqlDate(obj);
                }
                if (cls == Timestamp.class) {
                    return castToTimestamp(obj);
                }
                if (cls.isEnum()) {
                    return castToEnum(obj, cls, parserConfig);
                }
                if (Calendar.class.isAssignableFrom(cls)) {
                    Date castToDate = castToDate(obj);
                    if (cls == Calendar.class) {
                        t = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
                    } else {
                        try {
                            t = (Calendar) cls.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("can not cast to : " + cls.getName(), e);
                        }
                    }
                    t.setTime(castToDate);
                    return t;
                }
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                        return null;
                    }
                    if (cls == Currency.class) {
                        return Currency.getInstance(str);
                    }
                }
                throw new JSONException("can not cast to : " + cls.getName());
            } else if (cls == Map.class) {
                return obj;
            } else {
                Map map = (Map) obj;
                if (cls != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                    return castToJavaBean(map, cls, parserConfig);
                }
                return obj;
            }
        }
    }

    public static <T> T castToEnum(Object obj, Class<T> cls, ParserConfig parserConfig) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0) {
                    return null;
                }
                return Enum.valueOf(cls, str);
            }
            if (obj instanceof Number) {
                int intValue = ((Number) obj).intValue();
                T[] enumConstants = cls.getEnumConstants();
                if (intValue < enumConstants.length) {
                    return enumConstants[intValue];
                }
            }
            throw new JSONException("can not cast to : " + cls.getName());
        } catch (Exception e) {
            throw new JSONException("can not cast to : " + cls.getName(), e);
        }
    }

    public static <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            return cast(obj, (Class) type, parserConfig);
        }
        if (type instanceof ParameterizedType) {
            return cast(obj, (ParameterizedType) type, parserConfig);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        T t;
        Type rawType = parameterizedType.getRawType();
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawType == Set.class || rawType == HashSet.class) {
                    t = new HashSet();
                } else if (rawType == TreeSet.class) {
                    t = new TreeSet();
                } else {
                    t = new ArrayList();
                }
                for (Object cast : (Iterable) obj) {
                    t.add(cast(cast, type, parserConfig));
                }
                return t;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            Type type3 = parameterizedType.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                T hashMap = new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    hashMap.put(cast(entry.getKey(), type2, parserConfig), cast(entry.getValue(), type3, parserConfig));
                }
                return hashMap;
            }
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        if (parameterizedType.getActualTypeArguments().length == 1 && (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType)) {
            return cast(obj, rawType, parserConfig);
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    /* JADX WARNING: type inference failed for: r4v1, types: [com.alibaba.fastjson.parser.deserializer.ObjectDeserializer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T castToJavaBean(java.util.Map<java.lang.String, java.lang.Object> r3, java.lang.Class<T> r4, com.alibaba.fastjson.parser.ParserConfig r5) {
        /*
            java.lang.Class<java.lang.StackTraceElement> r0 = java.lang.StackTraceElement.class
            r1 = 0
            if (r4 != r0) goto L_0x0032
            java.lang.String r4 = "className"
            java.lang.Object r4 = r3.get(r4)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r5 = "methodName"
            java.lang.Object r5 = r3.get(r5)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r0 = "fileName"
            java.lang.Object r0 = r3.get(r0)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r2 = "lineNumber"
            java.lang.Object r3 = r3.get(r2)     // Catch:{ Exception -> 0x00b7 }
            java.lang.Number r3 = (java.lang.Number) r3     // Catch:{ Exception -> 0x00b7 }
            if (r3 != 0) goto L_0x0028
            goto L_0x002c
        L_0x0028:
            int r1 = r3.intValue()     // Catch:{ Exception -> 0x00b7 }
        L_0x002c:
            java.lang.StackTraceElement r3 = new java.lang.StackTraceElement     // Catch:{ Exception -> 0x00b7 }
            r3.<init>(r4, r5, r0, r1)     // Catch:{ Exception -> 0x00b7 }
            return r3
        L_0x0032:
            java.lang.String r0 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ Exception -> 0x00b7 }
            java.lang.Object r0 = r3.get(r0)     // Catch:{ Exception -> 0x00b7 }
            boolean r2 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x00b7 }
            if (r2 == 0) goto L_0x0066
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x00b7 }
            java.lang.Class r2 = loadClass(r0)     // Catch:{ Exception -> 0x00b7 }
            if (r2 == 0) goto L_0x004f
            boolean r0 = r2.equals(r4)     // Catch:{ Exception -> 0x00b7 }
            if (r0 != 0) goto L_0x0066
            java.lang.Object r3 = castToJavaBean(r3, r2, r5)     // Catch:{ Exception -> 0x00b7 }
            return r3
        L_0x004f:
            java.lang.ClassNotFoundException r3 = new java.lang.ClassNotFoundException     // Catch:{ Exception -> 0x00b7 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b7 }
            r4.<init>()     // Catch:{ Exception -> 0x00b7 }
            r4.append(r0)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r5 = " not found"
            r4.append(r5)     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00b7 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x00b7 }
            throw r3     // Catch:{ Exception -> 0x00b7 }
        L_0x0066:
            boolean r0 = r4.isInterface()     // Catch:{ Exception -> 0x00b7 }
            if (r0 == 0) goto L_0x008b
            boolean r5 = r3 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x00b7 }
            if (r5 == 0) goto L_0x0073
            com.alibaba.fastjson.JSONObject r3 = (com.alibaba.fastjson.JSONObject) r3     // Catch:{ Exception -> 0x00b7 }
            goto L_0x0079
        L_0x0073:
            com.alibaba.fastjson.JSONObject r5 = new com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x00b7 }
            r5.<init>((java.util.Map<java.lang.String, java.lang.Object>) r3)     // Catch:{ Exception -> 0x00b7 }
            r3 = r5
        L_0x0079:
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x00b7 }
            java.lang.ClassLoader r5 = r5.getContextClassLoader()     // Catch:{ Exception -> 0x00b7 }
            r0 = 1
            java.lang.Class[] r0 = new java.lang.Class[r0]     // Catch:{ Exception -> 0x00b7 }
            r0[r1] = r4     // Catch:{ Exception -> 0x00b7 }
            java.lang.Object r3 = java.lang.reflect.Proxy.newProxyInstance(r5, r0, r3)     // Catch:{ Exception -> 0x00b7 }
            return r3
        L_0x008b:
            if (r5 != 0) goto L_0x0091
            com.alibaba.fastjson.parser.ParserConfig r5 = com.alibaba.fastjson.parser.ParserConfig.getGlobalInstance()     // Catch:{ Exception -> 0x00b7 }
        L_0x0091:
            r0 = 0
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r4 = r5.getDeserializer((java.lang.reflect.Type) r4)     // Catch:{ Exception -> 0x00b7 }
            boolean r1 = r4 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ Exception -> 0x00b7 }
            if (r1 == 0) goto L_0x009e
            r0 = r4
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0     // Catch:{ Exception -> 0x00b7 }
            goto L_0x00a8
        L_0x009e:
            boolean r1 = r4 instanceof com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer     // Catch:{ Exception -> 0x00b7 }
            if (r1 == 0) goto L_0x00a8
            com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer r4 = (com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer) r4     // Catch:{ Exception -> 0x00b7 }
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r0 = r4.getInnterSerializer()     // Catch:{ Exception -> 0x00b7 }
        L_0x00a8:
            if (r0 == 0) goto L_0x00af
            java.lang.Object r3 = r0.createInstance((java.util.Map<java.lang.String, java.lang.Object>) r3, (com.alibaba.fastjson.parser.ParserConfig) r5)     // Catch:{ Exception -> 0x00b7 }
            return r3
        L_0x00af:
            com.alibaba.fastjson.JSONException r3 = new com.alibaba.fastjson.JSONException     // Catch:{ Exception -> 0x00b7 }
            java.lang.String r4 = "can not get javaBeanDeserializer"
            r3.<init>(r4)     // Catch:{ Exception -> 0x00b7 }
            throw r3     // Catch:{ Exception -> 0x00b7 }
        L_0x00b7:
            r3 = move-exception
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException
            java.lang.String r5 = r3.getMessage()
            r4.<init>(r5, r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.castToJavaBean(java.util.Map, java.lang.Class, com.alibaba.fastjson.parser.ParserConfig):java.lang.Object");
    }

    private static void addBaseClassMappings() {
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put(HashMap.class.getName(), HashMap.class);
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static Class<?> loadClass(String str) {
        return loadClass(str, (ClassLoader) null);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0072 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Class<?> loadClass(java.lang.String r5, java.lang.ClassLoader r6) {
        /*
            if (r5 == 0) goto L_0x007c
            int r0 = r5.length()
            if (r0 != 0) goto L_0x0009
            goto L_0x007c
        L_0x0009:
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r0 = mappings
            java.lang.Object r0 = r0.get(r5)
            java.lang.Class r0 = (java.lang.Class) r0
            if (r0 == 0) goto L_0x0014
            return r0
        L_0x0014:
            r1 = 0
            char r2 = r5.charAt(r1)
            r3 = 91
            r4 = 1
            if (r2 != r3) goto L_0x002f
            java.lang.String r5 = r5.substring(r4)
            java.lang.Class r5 = loadClass(r5, r6)
            java.lang.Object r5 = java.lang.reflect.Array.newInstance(r5, r1)
            java.lang.Class r5 = r5.getClass()
            return r5
        L_0x002f:
            java.lang.String r1 = "L"
            boolean r1 = r5.startsWith(r1)
            if (r1 == 0) goto L_0x004d
            java.lang.String r1 = ";"
            boolean r1 = r5.endsWith(r1)
            if (r1 == 0) goto L_0x004d
            int r0 = r5.length()
            int r0 = r0 - r4
            java.lang.String r5 = r5.substring(r4, r0)
            java.lang.Class r5 = loadClass(r5, r6)
            return r5
        L_0x004d:
            if (r6 == 0) goto L_0x005d
            java.lang.Class r0 = r6.loadClass(r5)     // Catch:{ all -> 0x0059 }
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r6 = mappings     // Catch:{ all -> 0x0059 }
            r6.put(r5, r0)     // Catch:{ all -> 0x0059 }
            return r0
        L_0x0059:
            r6 = move-exception
            r6.printStackTrace()
        L_0x005d:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0072 }
            java.lang.ClassLoader r6 = r6.getContextClassLoader()     // Catch:{ all -> 0x0072 }
            if (r6 == 0) goto L_0x0072
            java.lang.Class r6 = r6.loadClass(r5)     // Catch:{ all -> 0x0072 }
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r0 = mappings     // Catch:{ all -> 0x0071 }
            r0.put(r5, r6)     // Catch:{ all -> 0x0071 }
            return r6
        L_0x0071:
            r0 = r6
        L_0x0072:
            java.lang.Class r0 = java.lang.Class.forName(r5)     // Catch:{ all -> 0x007b }
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r6 = mappings     // Catch:{ all -> 0x007b }
            r6.put(r5, r0)     // Catch:{ all -> 0x007b }
        L_0x007b:
            return r0
        L_0x007c:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.loadClass(java.lang.String, java.lang.ClassLoader):java.lang.Class");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01a5, code lost:
        if (r0 == null) goto L_0x0029;
     */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x02e1 A[PHI: r0 
      PHI: (r0v36 java.lang.String) = (r0v35 java.lang.String), (r0v39 java.lang.String) binds: [B:121:0x02d6, B:123:0x02de] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x02f3 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.alibaba.fastjson.util.FieldInfo> computeGetters(java.lang.Class<?> r31, com.alibaba.fastjson.annotation.JSONType r32, java.util.Map<java.lang.String, java.lang.String> r33, boolean r34) {
        /*
            r11 = r31
            r12 = r33
            java.util.LinkedHashMap r13 = new java.util.LinkedHashMap
            r13.<init>()
            java.lang.reflect.Method[] r14 = r31.getMethods()
            int r15 = r14.length
            r16 = 0
            r10 = 0
        L_0x0011:
            r17 = 0
            if (r10 >= r15) goto L_0x02fb
            r9 = r14[r10]
            java.lang.String r8 = r9.getName()
            r18 = 0
            int r0 = r9.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)
            if (r0 == 0) goto L_0x002f
        L_0x0027:
            r20 = r10
        L_0x0029:
            r32 = r14
            r28 = r15
            goto L_0x02f3
        L_0x002f:
            java.lang.Class r0 = r9.getReturnType()
            java.lang.Class r1 = java.lang.Void.TYPE
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            goto L_0x0027
        L_0x003c:
            java.lang.Class[] r0 = r9.getParameterTypes()
            int r0 = r0.length
            if (r0 == 0) goto L_0x0044
            goto L_0x0027
        L_0x0044:
            java.lang.Class r0 = r9.getReturnType()
            java.lang.Class<java.lang.ClassLoader> r1 = java.lang.ClassLoader.class
            if (r0 != r1) goto L_0x004d
            goto L_0x0027
        L_0x004d:
            java.lang.String r0 = r9.getName()
            java.lang.String r1 = "getMetaClass"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x006a
            java.lang.Class r0 = r9.getReturnType()
            java.lang.String r0 = r0.getName()
            java.lang.String r1 = "groovy.lang.MetaClass"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x006a
            goto L_0x0027
        L_0x006a:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = r9.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            if (r0 != 0) goto L_0x0078
            com.alibaba.fastjson.annotation.JSONField r0 = getSupperMethodAnnotation(r11, r9)
        L_0x0078:
            r19 = r0
            if (r19 == 0) goto L_0x00e2
            boolean r0 = r19.serialize()
            if (r0 != 0) goto L_0x0083
            goto L_0x0027
        L_0x0083:
            int r6 = r19.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r0 = r19.serialzeFeatures()
            int r7 = com.alibaba.fastjson.serializer.SerializerFeature.of(r0)
            java.lang.String r0 = r19.name()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x00d1
            java.lang.String r0 = r19.name()
            if (r12 == 0) goto L_0x00a9
            java.lang.Object r0 = r12.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x00a9
            goto L_0x0027
        L_0x00a9:
            r8 = r0
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r17 = 0
            r20 = 0
            r0 = r5
            r1 = r8
            r2 = r9
            r4 = r31
            r9 = r5
            r5 = r17
            r21 = r8
            r8 = r19
            r22 = r9
            r9 = r20
            r20 = r10
            r10 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r1 = r21
            r0 = r22
            r13.put(r1, r0)
            goto L_0x0029
        L_0x00d1:
            r20 = r10
            java.lang.String r0 = r19.label()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x00e6
            java.lang.String r18 = r19.label()
            goto L_0x00e6
        L_0x00e2:
            r20 = r10
            r6 = 0
            r7 = 0
        L_0x00e6:
            java.lang.String r0 = "get"
            boolean r0 = r8.startsWith(r0)
            r10 = 102(0x66, float:1.43E-43)
            r5 = 95
            r4 = 3
            if (r0 == 0) goto L_0x0211
            int r0 = r8.length()
            r1 = 4
            if (r0 >= r1) goto L_0x00fc
        L_0x00fa:
            goto L_0x0029
        L_0x00fc:
            java.lang.String r0 = "getClass"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x0105
        L_0x0104:
            goto L_0x00fa
        L_0x0105:
            char r0 = r8.charAt(r4)
            boolean r2 = java.lang.Character.isUpperCase(r0)
            if (r2 != 0) goto L_0x013c
            r2 = 512(0x200, float:7.175E-43)
            if (r0 <= r2) goto L_0x0114
            goto L_0x013c
        L_0x0114:
            if (r0 != r5) goto L_0x011b
            java.lang.String r0 = r8.substring(r1)
            goto L_0x0164
        L_0x011b:
            if (r0 != r10) goto L_0x0122
            java.lang.String r0 = r8.substring(r4)
            goto L_0x0164
        L_0x0122:
            int r0 = r8.length()
            r2 = 5
            if (r0 < r2) goto L_0x0029
            char r0 = r8.charAt(r1)
            boolean r0 = java.lang.Character.isUpperCase(r0)
            if (r0 == 0) goto L_0x0029
            java.lang.String r0 = r8.substring(r4)
            java.lang.String r0 = decapitalize(r0)
            goto L_0x0164
        L_0x013c:
            boolean r0 = compatibleWithJavaBean
            if (r0 == 0) goto L_0x0149
            java.lang.String r0 = r8.substring(r4)
            java.lang.String r0 = decapitalize(r0)
            goto L_0x0164
        L_0x0149:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            char r2 = r8.charAt(r4)
            char r2 = java.lang.Character.toLowerCase(r2)
            r0.append(r2)
            java.lang.String r1 = r8.substring(r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L_0x0164:
            boolean r1 = isJSONTypeIgnore(r11, r0)
            if (r1 == 0) goto L_0x016b
            goto L_0x00fa
        L_0x016b:
            java.lang.reflect.Field r3 = com.alibaba.fastjson.parser.ParserConfig.getField(r11, r0)
            if (r3 == 0) goto L_0x01ce
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r3.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            if (r1 == 0) goto L_0x01c5
            boolean r2 = r1.serialize()
            if (r2 != 0) goto L_0x0183
            goto L_0x00fa
        L_0x0183:
            int r2 = r1.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r6 = r1.serialzeFeatures()
            int r6 = com.alibaba.fastjson.serializer.SerializerFeature.of(r6)
            java.lang.String r7 = r1.name()
            int r7 = r7.length()
            if (r7 == 0) goto L_0x01a9
            java.lang.String r0 = r1.name()
            if (r12 == 0) goto L_0x01a9
            java.lang.Object r0 = r12.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x01a9
            goto L_0x0104
        L_0x01a9:
            java.lang.String r7 = r1.label()
            int r7 = r7.length()
            if (r7 == 0) goto L_0x01c0
            java.lang.String r7 = r1.label()
            r18 = r1
            r22 = r2
            r23 = r6
            r21 = r7
            goto L_0x01d6
        L_0x01c0:
            r22 = r2
            r23 = r6
            goto L_0x01c9
        L_0x01c5:
            r22 = r6
            r23 = r7
        L_0x01c9:
            r21 = r18
            r18 = r1
            goto L_0x01d6
        L_0x01ce:
            r22 = r6
            r23 = r7
            r21 = r18
            r18 = r17
        L_0x01d6:
            if (r12 == 0) goto L_0x01e2
            java.lang.Object r0 = r12.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x01e2
            goto L_0x0104
        L_0x01e2:
            r7 = r0
            com.alibaba.fastjson.util.FieldInfo r6 = new com.alibaba.fastjson.util.FieldInfo
            r24 = 0
            r0 = r6
            r1 = r7
            r2 = r9
            r32 = r14
            r14 = 3
            r4 = r31
            r5 = r24
            r14 = r6
            r6 = r22
            r25 = r7
            r7 = r23
            r26 = r8
            r8 = r19
            r27 = r9
            r9 = r18
            r28 = r15
            r15 = 102(0x66, float:1.43E-43)
            r10 = r21
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r25
            r13.put(r0, r14)
            r18 = r21
            goto L_0x021b
        L_0x0211:
            r26 = r8
            r27 = r9
            r32 = r14
            r28 = r15
            r15 = 102(0x66, float:1.43E-43)
        L_0x021b:
            java.lang.String r0 = "is"
            r1 = r26
            boolean r0 = r1.startsWith(r0)
            if (r0 == 0) goto L_0x02f3
            int r0 = r1.length()
            r2 = 3
            if (r0 >= r2) goto L_0x022e
            goto L_0x02f3
        L_0x022e:
            r0 = 2
            char r2 = r1.charAt(r0)
            boolean r3 = java.lang.Character.isUpperCase(r2)
            if (r3 == 0) goto L_0x0263
            boolean r2 = compatibleWithJavaBean
            if (r2 == 0) goto L_0x0246
            java.lang.String r0 = r1.substring(r0)
            java.lang.String r0 = decapitalize(r0)
            goto L_0x0273
        L_0x0246:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            char r0 = r1.charAt(r0)
            char r0 = java.lang.Character.toLowerCase(r0)
            r2.append(r0)
            r3 = 3
            java.lang.String r0 = r1.substring(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            goto L_0x0273
        L_0x0263:
            r3 = 3
            r4 = 95
            if (r2 != r4) goto L_0x026d
            java.lang.String r0 = r1.substring(r3)
            goto L_0x0273
        L_0x026d:
            if (r2 != r15) goto L_0x02f3
            java.lang.String r0 = r1.substring(r0)
        L_0x0273:
            java.lang.reflect.Field r2 = com.alibaba.fastjson.parser.ParserConfig.getField(r11, r0)
            if (r2 != 0) goto L_0x027f
            java.lang.reflect.Field r1 = com.alibaba.fastjson.parser.ParserConfig.getField(r11, r1)
            r3 = r1
            goto L_0x0280
        L_0x027f:
            r3 = r2
        L_0x0280:
            if (r3 == 0) goto L_0x02d2
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r3.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            if (r1 == 0) goto L_0x02d0
            boolean r2 = r1.serialize()
            if (r2 != 0) goto L_0x0294
            goto L_0x02f3
        L_0x0294:
            int r2 = r1.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r4 = r1.serialzeFeatures()
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.of(r4)
            java.lang.String r5 = r1.name()
            int r5 = r5.length()
            if (r5 == 0) goto L_0x02b9
            java.lang.String r0 = r1.name()
            if (r12 == 0) goto L_0x02b9
            java.lang.Object r0 = r12.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x02b9
            goto L_0x02f3
        L_0x02b9:
            java.lang.String r5 = r1.label()
            int r5 = r5.length()
            if (r5 == 0) goto L_0x02cc
            java.lang.String r5 = r1.label()
            r9 = r1
            r6 = r2
            r7 = r4
            r10 = r5
            goto L_0x02d6
        L_0x02cc:
            r9 = r1
            r6 = r2
            r7 = r4
            goto L_0x02d4
        L_0x02d0:
            r9 = r1
            goto L_0x02d4
        L_0x02d2:
            r9 = r17
        L_0x02d4:
            r10 = r18
        L_0x02d6:
            if (r12 == 0) goto L_0x02e1
            java.lang.Object r0 = r12.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x02e1
            goto L_0x02f3
        L_0x02e1:
            r14 = r0
            com.alibaba.fastjson.util.FieldInfo r15 = new com.alibaba.fastjson.util.FieldInfo
            r5 = 0
            r0 = r15
            r1 = r14
            r2 = r27
            r4 = r31
            r8 = r19
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r13.put(r14, r15)
        L_0x02f3:
            int r10 = r20 + 1
            r14 = r32
            r15 = r28
            goto L_0x0011
        L_0x02fb:
            java.lang.reflect.Field[] r14 = r31.getFields()
            int r15 = r14.length
            r10 = 0
        L_0x0301:
            if (r10 >= r15) goto L_0x0396
            r3 = r14[r10]
            int r0 = r3.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)
            if (r0 == 0) goto L_0x0313
        L_0x030f:
            r19 = r10
            goto L_0x0392
        L_0x0313:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = r3.getAnnotation(r0)
            r9 = r0
            com.alibaba.fastjson.annotation.JSONField r9 = (com.alibaba.fastjson.annotation.JSONField) r9
            java.lang.String r0 = r3.getName()
            if (r9 == 0) goto L_0x035b
            boolean r1 = r9.serialize()
            if (r1 != 0) goto L_0x0329
            goto L_0x030f
        L_0x0329:
            int r1 = r9.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r2 = r9.serialzeFeatures()
            int r2 = com.alibaba.fastjson.serializer.SerializerFeature.of(r2)
            java.lang.String r4 = r9.name()
            int r4 = r4.length()
            if (r4 == 0) goto L_0x0343
            java.lang.String r0 = r9.name()
        L_0x0343:
            java.lang.String r4 = r9.label()
            int r4 = r4.length()
            if (r4 == 0) goto L_0x0356
            java.lang.String r4 = r9.label()
            r6 = r1
            r7 = r2
            r18 = r4
            goto L_0x035f
        L_0x0356:
            r6 = r1
            r7 = r2
            r18 = r17
            goto L_0x035f
        L_0x035b:
            r18 = r17
            r6 = 0
            r7 = 0
        L_0x035f:
            if (r12 == 0) goto L_0x036a
            java.lang.Object r0 = r12.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x036a
            goto L_0x030f
        L_0x036a:
            r8 = r0
            boolean r0 = r13.containsKey(r8)
            if (r0 != 0) goto L_0x030f
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r2 = 0
            r19 = 0
            r20 = 0
            r0 = r5
            r1 = r8
            r4 = r31
            r29 = r5
            r5 = r19
            r30 = r8
            r8 = r20
            r19 = r10
            r10 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r1 = r29
            r0 = r30
            r13.put(r0, r1)
        L_0x0392:
            int r10 = r19 + 1
            goto L_0x0301
        L_0x0396:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r1 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r1 = r11.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONType r1 = (com.alibaba.fastjson.annotation.JSONType) r1
            if (r1 == 0) goto L_0x03c4
            java.lang.String[] r1 = r1.orders()
            if (r1 == 0) goto L_0x03c6
            int r2 = r1.length
            int r3 = r13.size()
            if (r2 != r3) goto L_0x03c6
            int r2 = r1.length
            r3 = 0
        L_0x03b4:
            if (r3 >= r2) goto L_0x03c2
            r4 = r1[r3]
            boolean r4 = r13.containsKey(r4)
            if (r4 != 0) goto L_0x03bf
            goto L_0x03c6
        L_0x03bf:
            int r3 = r3 + 1
            goto L_0x03b4
        L_0x03c2:
            r2 = 1
            goto L_0x03c7
        L_0x03c4:
            r1 = r17
        L_0x03c6:
            r2 = 0
        L_0x03c7:
            if (r2 == 0) goto L_0x03db
            int r2 = r1.length
            r3 = 0
        L_0x03cb:
            if (r3 >= r2) goto L_0x03f8
            r4 = r1[r3]
            java.lang.Object r4 = r13.get(r4)
            com.alibaba.fastjson.util.FieldInfo r4 = (com.alibaba.fastjson.util.FieldInfo) r4
            r0.add(r4)
            int r3 = r3 + 1
            goto L_0x03cb
        L_0x03db:
            java.util.Collection r1 = r13.values()
            java.util.Iterator r1 = r1.iterator()
        L_0x03e3:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x03f3
            java.lang.Object r2 = r1.next()
            com.alibaba.fastjson.util.FieldInfo r2 = (com.alibaba.fastjson.util.FieldInfo) r2
            r0.add(r2)
            goto L_0x03e3
        L_0x03f3:
            if (r34 == 0) goto L_0x03f8
            java.util.Collections.sort(r0)
        L_0x03f8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeGetters(java.lang.Class, com.alibaba.fastjson.annotation.JSONType, java.util.Map, boolean):java.util.List");
    }

    public static JSONField getSupperMethodAnnotation(Class<?> cls, Method method) {
        boolean z;
        JSONField jSONField;
        Class[] interfaces = cls.getInterfaces();
        if (interfaces.length <= 0) {
            return null;
        }
        Class[] parameterTypes = method.getParameterTypes();
        for (Class methods : interfaces) {
            for (Method method2 : methods.getMethods()) {
                Class[] parameterTypes2 = method2.getParameterTypes();
                if (parameterTypes2.length == parameterTypes.length && method2.getName().equals(method.getName())) {
                    int i = 0;
                    while (true) {
                        if (i >= parameterTypes.length) {
                            z = true;
                            break;
                        } else if (!parameterTypes2[i].equals(parameterTypes[i])) {
                            z = false;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (z && (jSONField = (JSONField) method2.getAnnotation(JSONField.class)) != null) {
                        return jSONField;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> cls, String str) {
        JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
        if (jSONType != null) {
            String[] includes = jSONType.includes();
            if (includes.length > 0) {
                for (String equals : includes) {
                    if (str.equals(equals)) {
                        return false;
                    }
                }
                return true;
            }
            String[] ignores = jSONType.ignores();
            for (String equals2 : ignores) {
                if (str.equals(equals2)) {
                    return true;
                }
            }
        }
        return (cls.getSuperclass() == Object.class || cls.getSuperclass() == null || !isJSONTypeIgnore(cls.getSuperclass(), str)) ? false : true;
    }

    public static boolean isGenericParamType(Type type) {
        Type genericSuperclass;
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (!(type instanceof Class) || (genericSuperclass = ((Class) type).getGenericSuperclass()) == Object.class) {
            return false;
        }
        return isGenericParamType(genericSuperclass);
    }

    public static Type getGenericParamType(Type type) {
        return (!(type instanceof ParameterizedType) && (type instanceof Class)) ? getGenericParamType(((Class) type).getGenericSuperclass()) : type;
    }

    public static Type unwrap(Type type) {
        if (!(type instanceof GenericArrayType)) {
            return type;
        }
        Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
        if (genericComponentType == Byte.TYPE) {
            return byte[].class;
        }
        return genericComponentType == Character.TYPE ? char[].class : type;
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception unused) {
            } catch (Throwable th) {
                optionalClassInited = true;
                throw th;
            }
            optionalClassInited = true;
        }
        if (!(type instanceof ParameterizedType)) {
            return type;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getRawType() == optionalClass ? parameterizedType.getActualTypeArguments()[0] : type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            return (Class) ((TypeVariable) type).getBounds()[0];
        }
        return Object.class;
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr) {
        for (Field field : fieldArr) {
            if (str.equals(field.getName())) {
                return field;
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return getField(superclass, str, superclass.getDeclaredFields());
    }

    public static JSONType getJSONType(Class<?> cls) {
        return (JSONType) cls.getAnnotation(JSONType.class);
    }

    public static int getSerializeFeatures(Class<?> cls) {
        JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
        if (jSONType == null) {
            return 0;
        }
        return SerializerFeature.of(jSONType.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> cls) {
        JSONType jSONType = (JSONType) cls.getAnnotation(JSONType.class);
        if (jSONType == null) {
            return 0;
        }
        return Feature.of(jSONType.parseFeatures());
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    static void setAccessible(AccessibleObject accessibleObject) {
        if (setAccessibleEnable && !accessibleObject.isAccessible()) {
            try {
                accessibleObject.setAccessible(true);
            } catch (AccessControlException unused) {
                setAccessibleEnable = false;
            }
        }
    }

    public static Class<?> getCollectionItemClass(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return Object.class;
        }
        Type type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (type2 instanceof Class) {
            Class<?> cls = (Class) type2;
            if (Modifier.isPublic(cls.getModifiers())) {
                return cls;
            }
            throw new JSONException("can not create ASMParser");
        }
        throw new JSONException("can not create ASMParser");
    }

    public static Collection createCollection(Type type) {
        Type type2;
        Class<?> rawClass = getRawClass(type);
        if (rawClass == AbstractCollection.class || rawClass == Collection.class) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            return new LinkedHashSet();
        }
        if (rawClass.isAssignableFrom(TreeSet.class)) {
            return new TreeSet();
        }
        if (rawClass.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(EnumSet.class)) {
            if (type instanceof ParameterizedType) {
                type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                type2 = Object.class;
            }
            return EnumSet.noneOf((Class) type2);
        }
        try {
            return (Collection) rawClass.newInstance();
        } catch (Exception unused) {
            throw new JSONException("create instane error, class " + rawClass.getName());
        }
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        }
        throw new JSONException("TODO");
    }
}
