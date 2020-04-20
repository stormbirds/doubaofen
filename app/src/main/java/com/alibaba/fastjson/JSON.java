package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public abstract class JSON implements JSONStreamAware, JSONAware {
    public static int DEFAULT_GENERATE_FEATURE = ((((0 | SerializerFeature.QuoteFieldNames.getMask()) | SerializerFeature.SkipTransientField.getMask()) | SerializerFeature.WriteEnumUsingName.getMask()) | SerializerFeature.SortField.getMask());
    public static int DEFAULT_PARSER_FEATURE = ((((((((Feature.AutoCloseSource.getMask() | 0) | Feature.InternFieldNames.getMask()) | Feature.UseBigDecimal.getMask()) | Feature.AllowUnQuotedFieldNames.getMask()) | Feature.AllowSingleQuotes.getMask()) | Feature.AllowArbitraryCommas.getMask()) | Feature.SortFeidFastMatch.getMask()) | Feature.IgnoreNotMatch.getMask());
    public static String DEFAULT_TYPE_KEY = "@type";
    public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String VERSION = "1.2.9";
    public static Locale defaultLocale = Locale.getDefault();
    public static TimeZone defaultTimeZone = TimeZone.getDefault();
    static final SerializeFilter[] emptyFilters = new SerializeFilter[0];

    public static Object parse(String str) {
        return parse(str, DEFAULT_PARSER_FEATURE);
    }

    public static Object parse(String str, int i) {
        if (str == null) {
            return null;
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance(), i);
        Object parse = defaultJSONParser.parse();
        defaultJSONParser.handleResovleTask(parse);
        defaultJSONParser.close();
        return parse;
    }

    public static Object parse(byte[] bArr, Feature... featureArr) {
        return parse(bArr, 0, bArr.length, IOUtils.getUTF8Decoder(), featureArr);
    }

    public static Object parse(byte[] bArr, int i, int i2, CharsetDecoder charsetDecoder, Feature... featureArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        int i3 = DEFAULT_PARSER_FEATURE;
        for (Feature config : featureArr) {
            i3 = Feature.config(i3, config, true);
        }
        return parse(bArr, i, i2, charsetDecoder, i3);
    }

    public static Object parse(byte[] bArr, int i, int i2, CharsetDecoder charsetDecoder, int i3) {
        charsetDecoder.reset();
        char[] chars = IOUtils.getChars((int) (((double) i2) * ((double) charsetDecoder.maxCharsPerByte())));
        ByteBuffer wrap = ByteBuffer.wrap(bArr, i, i2);
        CharBuffer wrap2 = CharBuffer.wrap(chars);
        IOUtils.decode(charsetDecoder, wrap, wrap2);
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(chars, wrap2.position(), ParserConfig.getGlobalInstance(), i3);
        Object parse = defaultJSONParser.parse();
        defaultJSONParser.handleResovleTask(parse);
        defaultJSONParser.close();
        return parse;
    }

    public static Object parse(String str, Feature... featureArr) {
        int i = DEFAULT_PARSER_FEATURE;
        for (Feature config : featureArr) {
            i = Feature.config(i, config, true);
        }
        return parse(str, i);
    }

    public static JSONObject parseObject(String str, Feature... featureArr) {
        return (JSONObject) parse(str, featureArr);
    }

    public static JSONObject parseObject(String str) {
        Object parse = parse(str);
        if (parse instanceof JSONObject) {
            return (JSONObject) parse;
        }
        return (JSONObject) toJSON(parse);
    }

    public static <T> T parseObject(String str, TypeReference<T> typeReference, Feature... featureArr) {
        return parseObject(str, typeReference.type, ParserConfig.global, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static <T> T parseObject(String str, Class<T> cls, Feature... featureArr) {
        return parseObject(str, cls, ParserConfig.global, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static <T> T parseObject(String str, Class<T> cls, ParseProcess parseProcess, Feature... featureArr) {
        return parseObject(str, (Type) cls, ParserConfig.global, parseProcess, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static <T> T parseObject(String str, Type type, Feature... featureArr) {
        return parseObject(str, type, ParserConfig.global, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static <T> T parseObject(String str, Type type, ParseProcess parseProcess, Feature... featureArr) {
        return parseObject(str, type, ParserConfig.global, DEFAULT_PARSER_FEATURE, featureArr);
    }

    public static <T> T parseObject(String str, Type type, int i, Feature... featureArr) {
        if (str == null) {
            return null;
        }
        for (Feature config : featureArr) {
            i = Feature.config(i, config, true);
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance(), i);
        T parseObject = defaultJSONParser.parseObject(type);
        defaultJSONParser.handleResovleTask(parseObject);
        defaultJSONParser.close();
        return parseObject;
    }

    public static <T> T parseObject(String str, Type type, ParserConfig parserConfig, int i, Feature... featureArr) {
        return parseObject(str, type, parserConfig, (ParseProcess) null, i, featureArr);
    }

    public static <T> T parseObject(String str, Type type, ParserConfig parserConfig, ParseProcess parseProcess, int i, Feature... featureArr) {
        if (str == null) {
            return null;
        }
        if (featureArr != null) {
            for (Feature config : featureArr) {
                i = Feature.config(i, config, true);
            }
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, parserConfig, i);
        if (parseProcess instanceof ExtraTypeProvider) {
            defaultJSONParser.getExtraTypeProviders().add((ExtraTypeProvider) parseProcess);
        }
        if (parseProcess instanceof ExtraProcessor) {
            defaultJSONParser.getExtraProcessors().add((ExtraProcessor) parseProcess);
        }
        if (parseProcess instanceof FieldTypeResolver) {
            defaultJSONParser.setFieldTypeResolver((FieldTypeResolver) parseProcess);
        }
        T parseObject = defaultJSONParser.parseObject(type);
        defaultJSONParser.handleResovleTask(parseObject);
        defaultJSONParser.close();
        return parseObject;
    }

    public static <T> T parseObject(byte[] bArr, Type type, Feature... featureArr) {
        return parseObject(bArr, 0, bArr.length, IOUtils.getUTF8Decoder(), type, featureArr);
    }

    public static <T> T parseObject(byte[] bArr, int i, int i2, CharsetDecoder charsetDecoder, Type type, Feature... featureArr) {
        charsetDecoder.reset();
        char[] chars = IOUtils.getChars((int) (((double) i2) * ((double) charsetDecoder.maxCharsPerByte())));
        ByteBuffer wrap = ByteBuffer.wrap(bArr, i, i2);
        CharBuffer wrap2 = CharBuffer.wrap(chars);
        IOUtils.decode(charsetDecoder, wrap, wrap2);
        return parseObject(chars, wrap2.position(), type, featureArr);
    }

    public static <T> T parseObject(char[] cArr, int i, Type type, Feature... featureArr) {
        if (cArr == null || cArr.length == 0) {
            return null;
        }
        int i2 = DEFAULT_PARSER_FEATURE;
        for (Feature config : featureArr) {
            i2 = Feature.config(i2, config, true);
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(cArr, i, ParserConfig.getGlobalInstance(), i2);
        T parseObject = defaultJSONParser.parseObject(type);
        defaultJSONParser.handleResovleTask(parseObject);
        defaultJSONParser.close();
        return parseObject;
    }

    public static <T> T parseObject(String str, Class<T> cls) {
        return parseObject(str, cls, new Feature[0]);
    }

    public static JSONArray parseArray(String str) {
        JSONArray jSONArray = null;
        if (str == null) {
            return null;
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance());
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
        } else if (jSONLexer.token() != 20) {
            jSONArray = new JSONArray();
            defaultJSONParser.parseArray((Collection) jSONArray);
            defaultJSONParser.handleResovleTask(jSONArray);
        }
        defaultJSONParser.close();
        return jSONArray;
    }

    public static <T> List<T> parseArray(String str, Class<T> cls) {
        ArrayList arrayList = null;
        if (str == null) {
            return null;
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance());
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = jSONLexer.token();
        if (i == 8) {
            jSONLexer.nextToken();
        } else if (i != 20 || !jSONLexer.isBlankInput()) {
            arrayList = new ArrayList();
            defaultJSONParser.parseArray((Class<?>) cls, (Collection) arrayList);
            defaultJSONParser.handleResovleTask(arrayList);
        }
        defaultJSONParser.close();
        return arrayList;
    }

    public static List<Object> parseArray(String str, Type[] typeArr) {
        List<Object> list = null;
        if (str == null) {
            return null;
        }
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, ParserConfig.getGlobalInstance());
        Object[] parseArray = defaultJSONParser.parseArray(typeArr);
        if (parseArray != null) {
            list = Arrays.asList(parseArray);
        }
        defaultJSONParser.handleResovleTask(list);
        defaultJSONParser.close();
        return list;
    }

    public static String toJSONString(Object obj) {
        return toJSONString(obj, emptyFilters, new SerializerFeature[0]);
    }

    public static String toJSONString(Object obj, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter((Writer) null, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
        try {
            new JSONSerializer(serializeWriter).write(obj);
            return serializeWriter.toString();
        } finally {
            serializeWriter.close();
        }
    }

    public static String toJSONStringWithDateFormat(Object obj, String str, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, SerializeConfig.globalInstance, (SerializeFilter[]) null, str, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, SerializeConfig.globalInstance, new SerializeFilter[]{serializeFilter}, (String) null, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, SerializeConfig.globalInstance, serializeFilterArr, (String) null, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static byte[] toJSONBytes(Object obj, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
            return serializeWriter.toBytes(Key.STRING_CHARSET_NAME);
        } finally {
            serializeWriter.close();
        }
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, (SerializeFilter) null, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter serializeFilter, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, new SerializeFilter[]{serializeFilter}, (String) null, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, serializeFilterArr, (String) null, DEFAULT_GENERATE_FEATURE, serializerFeatureArr);
    }

    public static String toJSONString(Object obj, SerializeConfig serializeConfig, SerializeFilter[] serializeFilterArr, String str, int i, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter((Writer) null, i, serializerFeatureArr);
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter, serializeConfig);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            if (!(str == null || str.length() == 0)) {
                jSONSerializer.setDateFormat(str);
                jSONSerializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            }
            if (serializeFilterArr != null) {
                for (SerializeFilter addFilter : serializeFilterArr) {
                    jSONSerializer.addFilter(addFilter);
                }
            }
            jSONSerializer.write(obj);
            return serializeWriter.toString();
        } finally {
            serializeWriter.close();
        }
    }

    public static String toJSONStringZ(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        return toJSONString(obj, serializeConfig, emptyFilters, (String) null, 0, serializerFeatureArr);
    }

    public static byte[] toJSONBytes(Object obj, SerializeConfig serializeConfig, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter, serializeConfig);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
            return serializeWriter.toBytes(Key.STRING_CHARSET_NAME);
        } finally {
            serializeWriter.close();
        }
    }

    public static String toJSONString(Object obj, boolean z) {
        if (!z) {
            return toJSONString(obj);
        }
        return toJSONString(obj, SerializerFeature.PrettyFormat);
    }

    public static void writeJSONStringTo(Object obj, Writer writer, SerializerFeature... serializerFeatureArr) {
        SerializeWriter serializeWriter = new SerializeWriter(writer);
        try {
            JSONSerializer jSONSerializer = new JSONSerializer(serializeWriter);
            for (SerializerFeature config : serializerFeatureArr) {
                jSONSerializer.config(config, true);
            }
            jSONSerializer.write(obj);
        } finally {
            serializeWriter.close();
        }
    }

    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            new JSONSerializer(serializeWriter).write((Object) this);
            return serializeWriter.toString();
        } finally {
            serializeWriter.close();
        }
    }

    public void writeJSONString(Appendable appendable) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            new JSONSerializer(serializeWriter).write((Object) this);
            appendable.append(serializeWriter.toString());
            serializeWriter.close();
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        } catch (Throwable th) {
            serializeWriter.close();
            throw th;
        }
    }

    public static Object toJSON(Object obj) {
        return toJSON(obj, ParserConfig.getGlobalInstance());
    }

    public static Object toJSON(Object obj, ParserConfig parserConfig) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSON) {
            return obj;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            JSONObject jSONObject = new JSONObject(map.size());
            for (Map.Entry entry : map.entrySet()) {
                jSONObject.put(TypeUtils.castToString(entry.getKey()), toJSON(entry.getValue()));
            }
            return jSONObject;
        } else if (obj instanceof Collection) {
            Collection<Object> collection = (Collection) obj;
            JSONArray jSONArray = new JSONArray(collection.size());
            for (Object json : collection) {
                jSONArray.add(toJSON(json));
            }
            return jSONArray;
        } else {
            Class<?> cls = obj.getClass();
            if (cls.isEnum()) {
                return ((Enum) obj).name();
            }
            if (cls.isArray()) {
                int length = Array.getLength(obj);
                JSONArray jSONArray2 = new JSONArray(length);
                for (int i = 0; i < length; i++) {
                    jSONArray2.add(toJSON(Array.get(obj, i)));
                }
                return jSONArray2;
            } else if (parserConfig.isPrimitive(cls)) {
                return obj;
            } else {
                try {
                    List<FieldInfo> computeGetters = TypeUtils.computeGetters(cls, (JSONType) cls.getAnnotation(JSONType.class), (Map<String, String>) null, false);
                    JSONObject jSONObject2 = new JSONObject(computeGetters.size());
                    for (FieldInfo next : computeGetters) {
                        jSONObject2.put(next.name, toJSON(next.get(obj)));
                    }
                    return jSONObject2;
                } catch (IllegalAccessException e) {
                    throw new JSONException("toJSON error", e);
                } catch (InvocationTargetException e2) {
                    throw new JSONException("toJSON error", e2);
                }
            }
        }
    }

    public static <T> T toJavaObject(JSON json, Class<T> cls) {
        return TypeUtils.cast((Object) json, cls, ParserConfig.getGlobalInstance());
    }

    public <T> T toJavaObject(Class<T> cls) {
        return TypeUtils.cast((Object) this, cls, ParserConfig.getGlobalInstance());
    }
}
