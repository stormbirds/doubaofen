package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Currency;

public class CurrencyCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CurrencyCodec instance = new CurrencyCodec();

    public int getFastMatchToken() {
        return 4;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
        } else {
            serializeWriter.writeString(((Currency) obj).getCurrencyCode());
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        String str = (String) defaultJSONParser.parse();
        if (str == null || str.length() == 0) {
            return null;
        }
        return Currency.getInstance(str);
    }
}
