package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Date;

public abstract class AbstractDateDeserializer implements ObjectDeserializer {
    /* access modifiers changed from: protected */
    public abstract <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2);

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Long l;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 2) {
            Long valueOf = Long.valueOf(jSONLexer.longValue());
            jSONLexer.nextToken(16);
            l = valueOf;
        } else if (jSONLexer.token() == 4) {
            String stringVal = jSONLexer.stringVal();
            jSONLexer.nextToken(16);
            l = stringVal;
            if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                JSONScanner jSONScanner = new JSONScanner(stringVal);
                Date date = stringVal;
                if (jSONScanner.scanISO8601DateIfMatch()) {
                    date = jSONScanner.getCalendar().getTime();
                }
                jSONScanner.close();
                l = date;
            }
        } else if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            l = null;
        } else if (jSONLexer.token() == 12) {
            jSONLexer.nextToken();
            if (jSONLexer.token() == 4) {
                if (JSON.DEFAULT_TYPE_KEY.equals(jSONLexer.stringVal())) {
                    jSONLexer.nextToken();
                    defaultJSONParser.accept(17);
                    Class<?> loadClass = TypeUtils.loadClass(jSONLexer.stringVal(), defaultJSONParser.getConfig().getDefaultClassLoader());
                    if (loadClass != null) {
                        type = loadClass;
                    }
                    defaultJSONParser.accept(4);
                    defaultJSONParser.accept(16);
                }
                jSONLexer.nextTokenWithColon(2);
                if (jSONLexer.token() == 2) {
                    long longValue = jSONLexer.longValue();
                    jSONLexer.nextToken();
                    Long valueOf2 = Long.valueOf(longValue);
                    defaultJSONParser.accept(13);
                    l = valueOf2;
                } else {
                    throw new JSONException("syntax error : " + jSONLexer.tokenName());
                }
            } else {
                throw new JSONException("syntax error");
            }
        } else if (defaultJSONParser.getResolveStatus() == 2) {
            defaultJSONParser.setResolveStatus(0);
            defaultJSONParser.accept(16);
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            } else if ("val".equals(jSONLexer.stringVal())) {
                jSONLexer.nextToken();
                defaultJSONParser.accept(17);
                Object parse = defaultJSONParser.parse();
                defaultJSONParser.accept(13);
                l = parse;
            } else {
                throw new JSONException("syntax error");
            }
        } else {
            l = defaultJSONParser.parse();
        }
        return cast(defaultJSONParser, type, obj, l);
    }
}
