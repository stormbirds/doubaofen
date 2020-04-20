package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CollectionCodec instance = new CollectionCodec();

    public int getFastMatchToken() {
        return 14;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj != null) {
            Type type2 = null;
            int i2 = 0;
            if (serializeWriter.isEnabled(SerializerFeature.WriteClassName) && (type instanceof ParameterizedType)) {
                type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            }
            Collection collection = (Collection) obj;
            SerialContext serialContext = jSONSerializer.context;
            jSONSerializer.setContext(serialContext, obj, obj2, 0);
            if (serializeWriter.isEnabled(SerializerFeature.WriteClassName)) {
                if (HashSet.class == collection.getClass()) {
                    serializeWriter.append((CharSequence) "Set");
                } else if (TreeSet.class == collection.getClass()) {
                    serializeWriter.append((CharSequence) "TreeSet");
                }
            }
            try {
                serializeWriter.append('[');
                for (Object next : collection) {
                    int i3 = i2 + 1;
                    if (i2 != 0) {
                        serializeWriter.append(',');
                    }
                    if (next == null) {
                        serializeWriter.writeNull();
                    } else {
                        Class<?> cls = next.getClass();
                        if (cls == Integer.class) {
                            serializeWriter.writeInt(((Integer) next).intValue());
                        } else if (cls == Long.class) {
                            serializeWriter.writeLong(((Long) next).longValue());
                            if (serializeWriter.isEnabled(SerializerFeature.WriteClassName)) {
                                serializeWriter.write(76);
                            }
                        } else {
                            jSONSerializer.getObjectWriter(cls).write(jSONSerializer, next, Integer.valueOf(i3 - 1), type2, 0);
                        }
                    }
                    i2 = i3;
                }
                serializeWriter.append(']');
            } finally {
                jSONSerializer.context = serialContext;
            }
        } else if (serializeWriter.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            serializeWriter.write("[]");
        } else {
            serializeWriter.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Type type2;
        Type type3 = null;
        if (defaultJSONParser.lexer.token() == 8) {
            defaultJSONParser.lexer.nextToken(16);
            return null;
        } else if (type == JSONArray.class) {
            T jSONArray = new JSONArray();
            defaultJSONParser.parseArray((Collection) jSONArray);
            return jSONArray;
        } else {
            T createCollection = TypeUtils.createCollection(type);
            if (type instanceof ParameterizedType) {
                type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                if (type instanceof Class) {
                    Class cls = (Class) type;
                    if (!cls.getName().startsWith("java.")) {
                        Type genericSuperclass = cls.getGenericSuperclass();
                        if (genericSuperclass instanceof ParameterizedType) {
                            type3 = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
                        }
                    }
                }
                type2 = type3;
                if (type2 == null) {
                    type2 = Object.class;
                }
            }
            defaultJSONParser.parseArray(type2, createCollection, obj);
            return createCollection;
        }
    }
}
