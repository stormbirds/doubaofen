package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IOUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import kotlin.text.Typography;

public class CalendarCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CalendarCodec instance = new CalendarCodec();

    public int getFastMatchToken() {
        return 2;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        char[] cArr;
        JSONSerializer jSONSerializer2 = jSONSerializer;
        SerializeWriter serializeWriter = jSONSerializer2.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        Calendar calendar = (Calendar) obj;
        if (serializeWriter.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
            char c = serializeWriter.isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : Typography.quote;
            serializeWriter.append(c);
            int i2 = calendar.get(1);
            int i3 = calendar.get(2) + 1;
            int i4 = calendar.get(5);
            int i5 = calendar.get(11);
            int i6 = calendar.get(12);
            int i7 = calendar.get(13);
            int i8 = calendar.get(14);
            if (i8 != 0) {
                cArr = "0000-00-00T00:00:00.000".toCharArray();
                IOUtils.getChars(i8, 23, cArr);
                IOUtils.getChars(i7, 19, cArr);
                IOUtils.getChars(i6, 16, cArr);
                IOUtils.getChars(i5, 13, cArr);
                IOUtils.getChars(i4, 10, cArr);
                IOUtils.getChars(i3, 7, cArr);
                IOUtils.getChars(i2, 4, cArr);
            } else if (i7 == 0 && i6 == 0 && i5 == 0) {
                cArr = "0000-00-00".toCharArray();
                IOUtils.getChars(i4, 10, cArr);
                IOUtils.getChars(i3, 7, cArr);
                IOUtils.getChars(i2, 4, cArr);
            } else {
                cArr = "0000-00-00T00:00:00".toCharArray();
                IOUtils.getChars(i7, 19, cArr);
                IOUtils.getChars(i6, 16, cArr);
                IOUtils.getChars(i5, 13, cArr);
                IOUtils.getChars(i4, 10, cArr);
                IOUtils.getChars(i3, 7, cArr);
                IOUtils.getChars(i2, 4, cArr);
            }
            serializeWriter.write(cArr);
            int rawOffset = calendar.getTimeZone().getRawOffset() / 3600000;
            if (rawOffset == 0) {
                serializeWriter.append((CharSequence) "Z");
            } else if (rawOffset > 0) {
                serializeWriter.append((CharSequence) "+").append((CharSequence) String.format("%02d", new Object[]{Integer.valueOf(rawOffset)})).append((CharSequence) ":00");
            } else {
                serializeWriter.append((CharSequence) "-").append((CharSequence) String.format("%02d", new Object[]{Integer.valueOf(-rawOffset)})).append((CharSequence) ":00");
            }
            serializeWriter.append(c);
            return;
        }
        jSONSerializer2.write((Object) calendar.getTime());
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        T deserialze = DateCodec.instance.deserialze(defaultJSONParser, type, obj);
        if (deserialze instanceof Calendar) {
            return deserialze;
        }
        Date date = (Date) deserialze;
        if (date == null) {
            return null;
        }
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        T instance2 = Calendar.getInstance(jSONLexer.getTimeZone(), jSONLexer.getLocale());
        instance2.setTime(date);
        return instance2;
    }
}
