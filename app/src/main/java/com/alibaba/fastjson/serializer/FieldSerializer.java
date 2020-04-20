package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.Collection;
import kotlin.text.Typography;

public class FieldSerializer implements Comparable<FieldSerializer> {
    private final String double_quoted_fieldPrefix;
    protected int features;
    protected BeanContext fieldContext;
    public final FieldInfo fieldInfo;
    private String format;
    private RuntimeSerializerInfo runtimeInfo;
    private String single_quoted_fieldPrefix;
    private String un_quoted_fieldPrefix;
    protected boolean writeEnumUsingName = false;
    protected boolean writeEnumUsingToString = false;
    protected final boolean writeNull;
    protected boolean writeNullBooleanAsFalse = false;
    protected boolean writeNullListAsEmpty = false;
    protected boolean writeNullStringAsEmpty = false;
    protected boolean writeNumberAsZero = false;

    public FieldSerializer(Class<?> cls, FieldInfo fieldInfo2) {
        boolean z;
        this.fieldInfo = fieldInfo2;
        this.fieldContext = new BeanContext(cls, fieldInfo2);
        fieldInfo2.setAccessible();
        this.double_quoted_fieldPrefix = Typography.quote + fieldInfo2.name + "\":";
        JSONField annotation = fieldInfo2.getAnnotation();
        if (annotation != null) {
            SerializerFeature[] serialzeFeatures = annotation.serialzeFeatures();
            int length = serialzeFeatures.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                } else if (serialzeFeatures[i] == SerializerFeature.WriteMapNullValue) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            this.format = annotation.format();
            if (this.format.trim().length() == 0) {
                this.format = null;
            }
            for (SerializerFeature serializerFeature : annotation.serialzeFeatures()) {
                if (serializerFeature == SerializerFeature.WriteNullNumberAsZero) {
                    this.writeNumberAsZero = true;
                } else if (serializerFeature == SerializerFeature.WriteNullStringAsEmpty) {
                    this.writeNullStringAsEmpty = true;
                } else if (serializerFeature == SerializerFeature.WriteNullBooleanAsFalse) {
                    this.writeNullBooleanAsFalse = true;
                } else if (serializerFeature == SerializerFeature.WriteNullListAsEmpty) {
                    this.writeNullListAsEmpty = true;
                } else if (serializerFeature == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                } else if (serializerFeature == SerializerFeature.WriteEnumUsingName) {
                    this.writeEnumUsingName = true;
                }
            }
            this.features = SerializerFeature.of(annotation.serialzeFeatures());
        } else {
            z = false;
        }
        this.writeNull = z;
    }

    public void writePrefix(JSONSerializer jSONSerializer) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (!serializeWriter.quoteFieldNames) {
            if (this.un_quoted_fieldPrefix == null) {
                this.un_quoted_fieldPrefix = this.fieldInfo.name + ":";
            }
            serializeWriter.write(this.un_quoted_fieldPrefix);
        } else if (serializeWriter.useSingleQuotes) {
            if (this.single_quoted_fieldPrefix == null) {
                this.single_quoted_fieldPrefix = '\'' + this.fieldInfo.name + "':";
            }
            serializeWriter.write(this.single_quoted_fieldPrefix);
        } else {
            serializeWriter.write(this.double_quoted_fieldPrefix);
        }
    }

    public Object getPropertyValue(Object obj) throws Exception {
        try {
            return this.fieldInfo.get(obj);
        } catch (Exception e) {
            Member member = this.fieldInfo.getMember();
            throw new JSONException("get property error。 " + (member.getDeclaringClass().getName() + "." + member.getName()), e);
        }
    }

    public int compareTo(FieldSerializer fieldSerializer) {
        return this.fieldInfo.compareTo(fieldSerializer.fieldInfo);
    }

    public void writeValue(JSONSerializer jSONSerializer, Object obj) throws Exception {
        Class<?> cls;
        String str = this.format;
        if (str != null) {
            jSONSerializer.writeWithFormat(obj, str);
            return;
        }
        if (this.runtimeInfo == null) {
            if (obj == null) {
                cls = this.fieldInfo.fieldClass;
            } else {
                cls = obj.getClass();
            }
            this.runtimeInfo = new RuntimeSerializerInfo(jSONSerializer.getObjectWriter(cls), cls);
        }
        RuntimeSerializerInfo runtimeSerializerInfo = this.runtimeInfo;
        int i = this.fieldInfo.serialzeFeatures;
        if (obj == null) {
            Class<?> cls2 = runtimeSerializerInfo.runtimeFieldClass;
            SerializeWriter serializeWriter = jSONSerializer.out;
            if ((this.writeNumberAsZero || (serializeWriter.features & SerializerFeature.WriteNullNumberAsZero.mask) != 0) && Number.class.isAssignableFrom(cls2)) {
                serializeWriter.write(48);
            } else if (this.writeNullStringAsEmpty && String.class == cls2) {
                serializeWriter.write("\"\"");
            } else if (this.writeNullBooleanAsFalse && Boolean.class == cls2) {
                serializeWriter.write("false");
            } else if (!this.writeNullListAsEmpty || !Collection.class.isAssignableFrom(cls2)) {
                ObjectSerializer objectSerializer = runtimeSerializerInfo.fieldSerializer;
                if (!serializeWriter.writeMapNullValue || !(objectSerializer instanceof ASMJavaBeanSerializer)) {
                    objectSerializer.write(jSONSerializer, (Object) null, this.fieldInfo.name, this.fieldInfo.fieldType, i);
                } else {
                    serializeWriter.writeNull();
                }
            } else {
                serializeWriter.write("[]");
            }
        } else {
            if (this.fieldInfo.isEnum) {
                if (this.writeEnumUsingName) {
                    jSONSerializer.out.writeString(((Enum) obj).name());
                    return;
                } else if (this.writeEnumUsingToString) {
                    jSONSerializer.out.writeString(((Enum) obj).toString());
                    return;
                }
            }
            Class<?> cls3 = obj.getClass();
            if (cls3 == runtimeSerializerInfo.runtimeFieldClass) {
                runtimeSerializerInfo.fieldSerializer.write(jSONSerializer, obj, this.fieldInfo.name, this.fieldInfo.fieldType, i);
            } else {
                jSONSerializer.getObjectWriter(cls3).write(jSONSerializer, obj, this.fieldInfo.name, this.fieldInfo.fieldType, i);
            }
        }
    }

    static class RuntimeSerializerInfo {
        ObjectSerializer fieldSerializer;
        Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer objectSerializer, Class<?> cls) {
            this.fieldSerializer = objectSerializer;
            this.runtimeFieldClass = cls;
        }
    }
}
