package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class FieldDeserializer {
    protected final Class<?> clazz;
    public final FieldInfo fieldInfo;

    public int getFastMatchToken() {
        return 0;
    }

    public abstract void parseField(DefaultJSONParser defaultJSONParser, Object obj, Type type, Map<String, Object> map);

    public FieldDeserializer(Class<?> cls, FieldInfo fieldInfo2) {
        this.clazz = cls;
        this.fieldInfo = fieldInfo2;
    }

    public void setValue(Object obj, boolean z) {
        setValue(obj, (Object) Boolean.valueOf(z));
    }

    public void setValue(Object obj, int i) {
        setValue(obj, (Object) Integer.valueOf(i));
    }

    public void setValue(Object obj, long j) {
        setValue(obj, (Object) Long.valueOf(j));
    }

    public void setValue(Object obj, String str) {
        setValue(obj, (Object) str);
    }

    public void setValue(Object obj, Object obj2) {
        Class<?> cls;
        if (obj2 != null || ((cls = this.fieldInfo.fieldClass) != Byte.TYPE && cls != Short.TYPE && cls != Integer.TYPE && cls != Long.TYPE && cls != Float.TYPE && cls != Double.TYPE && cls != Boolean.TYPE && cls != Character.TYPE)) {
            Method method = this.fieldInfo.method;
            if (method != null) {
                try {
                    if (this.fieldInfo.getOnly) {
                        if (this.fieldInfo.fieldClass == AtomicInteger.class) {
                            AtomicInteger atomicInteger = (AtomicInteger) method.invoke(obj, new Object[0]);
                            if (atomicInteger != null) {
                                atomicInteger.set(((AtomicInteger) obj2).get());
                            }
                        } else if (this.fieldInfo.fieldClass == AtomicLong.class) {
                            AtomicLong atomicLong = (AtomicLong) method.invoke(obj, new Object[0]);
                            if (atomicLong != null) {
                                atomicLong.set(((AtomicLong) obj2).get());
                            }
                        } else if (this.fieldInfo.fieldClass == AtomicBoolean.class) {
                            AtomicBoolean atomicBoolean = (AtomicBoolean) method.invoke(obj, new Object[0]);
                            if (atomicBoolean != null) {
                                atomicBoolean.set(((AtomicBoolean) obj2).get());
                            }
                        } else if (Map.class.isAssignableFrom(method.getReturnType())) {
                            Map map = (Map) method.invoke(obj, new Object[0]);
                            if (map != null) {
                                map.putAll((Map) obj2);
                            }
                        } else {
                            Collection collection = (Collection) method.invoke(obj, new Object[0]);
                            if (collection != null) {
                                collection.addAll((Collection) obj2);
                            }
                        }
                    } else if (obj2 != null || !this.fieldInfo.fieldClass.isPrimitive()) {
                        method.invoke(obj, new Object[]{obj2});
                    }
                } catch (Exception e) {
                    throw new JSONException("set property error, " + this.fieldInfo.name, e);
                }
            } else {
                Field field = this.fieldInfo.field;
                if (field != null) {
                    try {
                        field.set(obj, obj2);
                    } catch (Exception e2) {
                        throw new JSONException("set property error, " + this.fieldInfo.name, e2);
                    }
                }
            }
        }
    }
}
