package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.FieldInfo;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JSONSerializer {
    protected List<AfterFilter> afterFilters;
    protected List<BeforeFilter> beforeFilters;
    private final SerializeConfig config;
    protected SerialContext context;
    protected List<ContextValueFilter> contextValueFilters;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private String indent;
    private int indentCount;
    protected List<LabelFilter> labelFilters;
    protected Locale locale;
    protected List<NameFilter> nameFilters;
    public final SerializeWriter out;
    protected List<PropertyFilter> propertyFilters;
    protected List<PropertyPreFilter> propertyPreFilters;
    protected IdentityHashMap<Object, SerialContext> references;
    protected TimeZone timeZone;
    protected List<ValueFilter> valueFilters;

    public FieldInfo getFieldInfo() {
        return null;
    }

    public JSONSerializer() {
        this(new SerializeWriter(), SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeWriter serializeWriter) {
        this(serializeWriter, SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeConfig serializeConfig) {
        this(new SerializeWriter(), serializeConfig);
    }

    public JSONSerializer(SerializeWriter serializeWriter, SerializeConfig serializeConfig) {
        this.beforeFilters = null;
        this.afterFilters = null;
        this.propertyFilters = null;
        this.valueFilters = null;
        this.nameFilters = null;
        this.propertyPreFilters = null;
        this.labelFilters = null;
        this.contextValueFilters = null;
        this.indentCount = 0;
        this.indent = "\t";
        this.references = null;
        this.timeZone = JSON.defaultTimeZone;
        this.locale = JSON.defaultLocale;
        this.out = serializeWriter;
        this.config = serializeConfig;
    }

    public String getDateFormatPattern() {
        DateFormat dateFormat2 = this.dateFormat;
        if (dateFormat2 instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) dateFormat2).toPattern();
        }
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        String str;
        if (this.dateFormat == null && (str = this.dateFormatPattern) != null) {
            this.dateFormat = new SimpleDateFormat(str, this.locale);
            this.dateFormat.setTimeZone(this.timeZone);
        }
        return this.dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat2) {
        this.dateFormat = dateFormat2;
        if (this.dateFormatPattern != null) {
            this.dateFormatPattern = null;
        }
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        if (this.dateFormat != null) {
            this.dateFormat = null;
        }
    }

    public SerialContext getContext() {
        return this.context;
    }

    public void setContext(SerialContext serialContext) {
        this.context = serialContext;
    }

    public void setContext(SerialContext serialContext, Object obj, Object obj2, int i) {
        setContext(serialContext, obj, obj2, i, 0);
    }

    public void setContext(SerialContext serialContext, Object obj, Object obj2, int i, int i2) {
        if (!this.out.disableCircularReferenceDetect) {
            this.context = new SerialContext(serialContext, obj, obj2, i, i2);
            if (this.references == null) {
                this.references = new IdentityHashMap<>();
            }
            this.references.put(obj, this.context);
        }
    }

    public void setContext(Object obj, Object obj2) {
        setContext(this.context, obj, obj2, 0);
    }

    public void popContext() {
        SerialContext serialContext = this.context;
        if (serialContext != null) {
            this.context = serialContext.parent;
        }
    }

    public final boolean isWriteClassName(Type type, Object obj) {
        return this.out.isEnabled(SerializerFeature.WriteClassName) && !(type == null && this.out.isEnabled(SerializerFeature.NotWriteRootClassName) && this.context.parent == null);
    }

    public boolean containsReference(Object obj) {
        IdentityHashMap<Object, SerialContext> identityHashMap = this.references;
        return identityHashMap != null && identityHashMap.containsKey(obj);
    }

    public void writeReference(Object obj) {
        SerialContext serialContext = this.context;
        if (obj == serialContext.object) {
            this.out.write("{\"$ref\":\"@\"}");
            return;
        }
        SerialContext serialContext2 = serialContext.parent;
        if (serialContext2 == null || obj != serialContext2.object) {
            while (serialContext.parent != null) {
                serialContext = serialContext.parent;
            }
            if (obj == serialContext.object) {
                this.out.write("{\"$ref\":\"$\"}");
                return;
            }
            this.out.write("{\"$ref\":\"");
            this.out.write(this.references.get(obj).toString());
            this.out.write("\"}");
            return;
        }
        this.out.write("{\"$ref\":\"..\"}");
    }

    public List<ValueFilter> getValueFilters() {
        if (this.valueFilters == null) {
            this.valueFilters = new ArrayList();
        }
        return this.valueFilters;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r0 = r1.contextValueFilters;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkValue() {
        /*
            r1 = this;
            java.util.List<com.alibaba.fastjson.serializer.ValueFilter> r0 = r1.valueFilters
            if (r0 == 0) goto L_0x000a
            int r0 = r0.size()
            if (r0 > 0) goto L_0x001a
        L_0x000a:
            java.util.List<com.alibaba.fastjson.serializer.ContextValueFilter> r0 = r1.contextValueFilters
            if (r0 == 0) goto L_0x0014
            int r0 = r0.size()
            if (r0 > 0) goto L_0x001a
        L_0x0014:
            com.alibaba.fastjson.serializer.SerializeWriter r0 = r1.out
            boolean r0 = r0.writeNonStringValueAsString
            if (r0 == 0) goto L_0x001c
        L_0x001a:
            r0 = 1
            goto L_0x001d
        L_0x001c:
            r0 = 0
        L_0x001d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JSONSerializer.checkValue():boolean");
    }

    public List<ContextValueFilter> getContextValueFilters() {
        if (this.contextValueFilters == null) {
            this.contextValueFilters = new ArrayList();
        }
        return this.contextValueFilters;
    }

    public int getIndentCount() {
        return this.indentCount;
    }

    public void incrementIndent() {
        this.indentCount++;
    }

    public void decrementIdent() {
        this.indentCount--;
    }

    public void println() {
        this.out.write(10);
        for (int i = 0; i < this.indentCount; i++) {
            this.out.write(this.indent);
        }
    }

    public List<BeforeFilter> getBeforeFilters() {
        if (this.beforeFilters == null) {
            this.beforeFilters = new ArrayList();
        }
        return this.beforeFilters;
    }

    public List<AfterFilter> getAfterFilters() {
        if (this.afterFilters == null) {
            this.afterFilters = new ArrayList();
        }
        return this.afterFilters;
    }

    public boolean hasNameFilters() {
        List<NameFilter> list = this.nameFilters;
        return list != null && list.size() > 0;
    }

    public List<NameFilter> getNameFilters() {
        if (this.nameFilters == null) {
            this.nameFilters = new ArrayList();
        }
        return this.nameFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (this.propertyPreFilters == null) {
            this.propertyPreFilters = new ArrayList();
        }
        return this.propertyPreFilters;
    }

    public List<LabelFilter> getLabelFilters() {
        if (this.labelFilters == null) {
            this.labelFilters = new ArrayList();
        }
        return this.labelFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (this.propertyFilters == null) {
            this.propertyFilters = new ArrayList();
        }
        return this.propertyFilters;
    }

    public SerializeWriter getWriter() {
        return this.out;
    }

    public String toString() {
        return this.out.toString();
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        this.out.config(serializerFeature, z);
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        return this.out.isEnabled(serializerFeature);
    }

    public void writeNull() {
        this.out.writeNull();
    }

    public SerializeConfig getMapping() {
        return this.config;
    }

    public static void write(Writer writer, Object obj) {
        SerializeWriter serializeWriter = new SerializeWriter();
        try {
            new JSONSerializer(serializeWriter).write(obj);
            serializeWriter.writeTo(writer);
            serializeWriter.close();
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        } catch (Throwable th) {
            serializeWriter.close();
            throw th;
        }
    }

    public static void write(SerializeWriter serializeWriter, Object obj) {
        new JSONSerializer(serializeWriter).write(obj);
    }

    public final void write(Object obj) {
        if (obj == null) {
            this.out.writeNull();
            return;
        }
        try {
            getObjectWriter(obj.getClass()).write(this, obj, (Object) null, (Type) null, 0);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFieldName(Object obj, Object obj2) {
        writeWithFieldName(obj, obj2, (Type) null, 0);
    }

    /* access modifiers changed from: protected */
    public final void writeKeyValue(char c, String str, Object obj) {
        if (c != 0) {
            this.out.write((int) c);
        }
        this.out.writeFieldName(str);
        write(obj);
    }

    public final void writeWithFieldName(Object obj, Object obj2, Type type, int i) {
        if (obj == null) {
            try {
                this.out.writeNull();
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        } else {
            getObjectWriter(obj.getClass()).write(this, obj, obj2, type, i);
        }
    }

    public final void writeWithFormat(Object obj, String str) {
        if (obj instanceof Date) {
            DateFormat dateFormat2 = getDateFormat();
            if (dateFormat2 == null) {
                dateFormat2 = new SimpleDateFormat(str, this.locale);
                dateFormat2.setTimeZone(this.timeZone);
            }
            this.out.writeString(dateFormat2.format((Date) obj));
            return;
        }
        write(obj);
    }

    public final void write(String str) {
        StringCodec.instance.write(this, str);
    }

    public ObjectSerializer getObjectWriter(Class<?> cls) {
        return this.config.getObjectWriter(cls);
    }

    public void close() {
        this.out.close();
    }

    public boolean writeDirect() {
        return this.out.writeDirect && this.beforeFilters == null && this.afterFilters == null && this.valueFilters == null && this.contextValueFilters == null && this.propertyFilters == null && this.nameFilters == null && this.propertyPreFilters == null && this.labelFilters == null;
    }

    public Object processValue(JavaBeanSerializer javaBeanSerializer, Object obj, String str, Object obj2) {
        if (obj2 != null && this.out.writeNonStringValueAsString && ((obj2 instanceof Number) || (obj2 instanceof Boolean))) {
            obj2 = obj2.toString();
        }
        List<ValueFilter> list = this.valueFilters;
        if (list != null) {
            for (ValueFilter process : list) {
                obj2 = process.process(obj, str, obj2);
            }
        }
        List<ContextValueFilter> list2 = this.contextValueFilters;
        if (list2 != null) {
            BeanContext beanContext = javaBeanSerializer.getFieldSerializer(str).fieldContext;
            for (ContextValueFilter process2 : list2) {
                obj2 = process2.process(beanContext, obj, str, obj2);
            }
        }
        return obj2;
    }

    public String processKey(Object obj, String str, Object obj2) {
        List<NameFilter> list = this.nameFilters;
        if (list != null) {
            for (NameFilter process : list) {
                str = process.process(obj, str, obj2);
            }
        }
        return str;
    }

    public boolean applyName(Object obj, String str) {
        List<PropertyPreFilter> list = this.propertyPreFilters;
        if (list == null) {
            return true;
        }
        for (PropertyPreFilter apply : list) {
            if (!apply.apply(this, obj, str)) {
                return false;
            }
        }
        return true;
    }

    public boolean apply(Object obj, String str, Object obj2) {
        List<PropertyFilter> list = this.propertyFilters;
        if (list == null) {
            return true;
        }
        for (PropertyFilter apply : list) {
            if (!apply.apply(obj, str, obj2)) {
                return false;
            }
        }
        return true;
    }

    public char writeBefore(Object obj, char c) {
        List<BeforeFilter> list = this.beforeFilters;
        if (list != null) {
            for (BeforeFilter writeBefore : list) {
                c = writeBefore.writeBefore(this, obj, c);
            }
        }
        return c;
    }

    public char writeAfter(Object obj, char c) {
        List<AfterFilter> list = this.afterFilters;
        if (list != null) {
            for (AfterFilter writeAfter : list) {
                c = writeAfter.writeAfter(this, obj, c);
            }
        }
        return c;
    }

    public boolean applyLabel(String str) {
        List<LabelFilter> list = this.labelFilters;
        if (list != null) {
            for (LabelFilter apply : list) {
                if (!apply.apply(str)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addFilter(SerializeFilter serializeFilter) {
        if (serializeFilter != null) {
            if (serializeFilter instanceof PropertyPreFilter) {
                getPropertyPreFilters().add((PropertyPreFilter) serializeFilter);
            }
            if (serializeFilter instanceof NameFilter) {
                getNameFilters().add((NameFilter) serializeFilter);
            }
            if (serializeFilter instanceof ValueFilter) {
                getValueFilters().add((ValueFilter) serializeFilter);
            }
            if (serializeFilter instanceof ContextValueFilter) {
                getContextValueFilters().add((ContextValueFilter) serializeFilter);
            }
            if (serializeFilter instanceof PropertyFilter) {
                getPropertyFilters().add((PropertyFilter) serializeFilter);
            }
            if (serializeFilter instanceof BeforeFilter) {
                getBeforeFilters().add((BeforeFilter) serializeFilter);
            }
            if (serializeFilter instanceof AfterFilter) {
                getAfterFilters().add((AfterFilter) serializeFilter);
            }
            if (serializeFilter instanceof LabelFilter) {
                getLabelFilters().add((LabelFilter) serializeFilter);
            }
        }
    }
}
