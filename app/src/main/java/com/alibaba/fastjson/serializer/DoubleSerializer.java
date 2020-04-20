package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;

public class DoubleSerializer implements ObjectSerializer {
    public static final DoubleSerializer instance = new DoubleSerializer();
    private DecimalFormat decimalFormat;

    public DoubleSerializer() {
        this.decimalFormat = null;
    }

    public DoubleSerializer(DecimalFormat decimalFormat2) {
        this.decimalFormat = null;
        this.decimalFormat = decimalFormat2;
    }

    public DoubleSerializer(String str) {
        this(new DecimalFormat(str));
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        String str;
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj != null) {
            double doubleValue = ((Double) obj).doubleValue();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                serializeWriter.writeNull();
                return;
            }
            DecimalFormat decimalFormat2 = this.decimalFormat;
            if (decimalFormat2 == null) {
                str = Double.toString(doubleValue);
                if (str.endsWith(".0")) {
                    str = str.substring(0, str.length() - 2);
                }
            } else {
                str = decimalFormat2.format(doubleValue);
            }
            serializeWriter.append((CharSequence) str);
            if (serializeWriter.isEnabled(SerializerFeature.WriteClassName)) {
                serializeWriter.write(68);
            }
        } else if (serializeWriter.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            serializeWriter.write(48);
        } else {
            serializeWriter.writeNull();
        }
    }
}
