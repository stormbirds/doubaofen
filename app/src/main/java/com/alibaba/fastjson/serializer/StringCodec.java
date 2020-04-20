package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;

public class StringCodec implements ObjectSerializer, ObjectDeserializer {
    public static StringCodec instance = new StringCodec();

    public int getFastMatchToken() {
        return 4;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, (String) obj);
    }

    public void write(JSONSerializer jSONSerializer, String str) {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (str != null) {
            serializeWriter.writeString(str);
        } else if (serializeWriter.isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
            serializeWriter.writeString("");
        } else {
            serializeWriter.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        if (type == StringBuffer.class) {
            JSONLexer jSONLexer = defaultJSONParser.lexer;
            if (jSONLexer.token() == 4) {
                String stringVal = jSONLexer.stringVal();
                jSONLexer.nextToken(16);
                return new StringBuffer(stringVal);
            }
            Object parse = defaultJSONParser.parse();
            if (parse == null) {
                return null;
            }
            return new StringBuffer(parse.toString());
        } else if (type != StringBuilder.class) {
            return deserialze(defaultJSONParser);
        } else {
            JSONLexer jSONLexer2 = defaultJSONParser.lexer;
            if (jSONLexer2.token() == 4) {
                String stringVal2 = jSONLexer2.stringVal();
                jSONLexer2.nextToken(16);
                return new StringBuilder(stringVal2);
            }
            Object parse2 = defaultJSONParser.parse();
            if (parse2 == null) {
                return null;
            }
            return new StringBuilder(parse2.toString());
        }
    }

    public static <T> T deserialze(DefaultJSONParser defaultJSONParser) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 4) {
            T stringVal = lexer.stringVal();
            lexer.nextToken(16);
            return stringVal;
        } else if (lexer.token() == 2) {
            T numberString = lexer.numberString();
            lexer.nextToken(16);
            return numberString;
        } else {
            Object parse = defaultJSONParser.parse();
            if (parse == null) {
                return null;
            }
            return parse.toString();
        }
    }
}
