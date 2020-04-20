package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalCodec implements ObjectSerializer, ObjectDeserializer {
    public static final BigDecimalCodec instance = new BigDecimalCodec();

    public int getFastMatchToken() {
        return 2;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj != null) {
            BigDecimal bigDecimal = (BigDecimal) obj;
            serializeWriter.write(bigDecimal.toString());
            if (serializeWriter.isEnabled(SerializerFeature.WriteClassName) && type != BigDecimal.class && bigDecimal.scale() == 0) {
                serializeWriter.write(46);
            }
        } else if (serializeWriter.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            serializeWriter.write(48);
        } else {
            serializeWriter.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return deserialze(defaultJSONParser);
    }

    public static <T> T deserialze(DefaultJSONParser defaultJSONParser) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 2) {
            T decimalValue = jSONLexer.decimalValue();
            jSONLexer.nextToken(16);
            return decimalValue;
        } else if (jSONLexer.token() == 3) {
            T decimalValue2 = jSONLexer.decimalValue();
            jSONLexer.nextToken(16);
            return decimalValue2;
        } else {
            Object parse = defaultJSONParser.parse();
            if (parse == null) {
                return null;
            }
            return TypeUtils.castToBigDecimal(parse);
        }
    }
}
