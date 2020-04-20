package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    private Class<?>[] clazzes = null;
    public Feature[] features = new Feature[0];
    public ParserConfig parserConfig = ParserConfig.getGlobalInstance();
    public SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
    public Map<Class<?>, SerializeFilter> serializeFilters;
    public SerializerFeature[] serializerFeatures = new SerializerFeature[0];
    @Context
    UriInfo uriInfo;

    public long getSize(Object obj, Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType) {
        return -1;
    }

    public FastJsonProvider() {
    }

    public FastJsonProvider(Class<?>[] clsArr) {
        this.clazzes = clsArr;
    }

    /* access modifiers changed from: protected */
    public boolean isValidType(Class<?> cls, Annotation[] annotationArr) {
        if (cls == null) {
            return false;
        }
        Class<?>[] clsArr = this.clazzes;
        if (clsArr == null) {
            return true;
        }
        for (Class<?> cls2 : clsArr) {
            if (cls2 == cls) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        String subtype = mediaType.getSubtype();
        if ("json".equalsIgnoreCase(subtype) || subtype.endsWith("+json") || "javascript".equals(subtype) || "x-javascript".equals(subtype)) {
            return true;
        }
        return false;
    }

    public boolean isWriteable(Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }
        return isValidType(cls, annotationArr);
    }

    public void writeTo(Object obj, Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        SerializerFeature[] serializerFeatureArr = this.serializerFeatures;
        UriInfo uriInfo2 = this.uriInfo;
        if (uriInfo2 != null && uriInfo2.getQueryParameters().containsKey("pretty")) {
            if (serializerFeatureArr == null) {
                serializerFeatureArr = new SerializerFeature[]{SerializerFeature.PrettyFormat};
            } else {
                List asList = Arrays.asList(serializerFeatureArr);
                asList.add(SerializerFeature.PrettyFormat);
                serializerFeatureArr = (SerializerFeature[]) asList.toArray(serializerFeatureArr);
            }
        }
        Map<Class<?>, SerializeFilter> map = this.serializeFilters;
        String jSONString = JSON.toJSONString(obj, map != null ? map.get(cls) : null, serializerFeatureArr);
        if (jSONString != null) {
            outputStream.write(jSONString.getBytes());
        }
    }

    public boolean isReadable(Class<?> cls, Type type, Annotation[] annotationArr, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }
        return isValidType(cls, annotationArr);
    }

    public Object readFrom(Class<Object> cls, Type type, Annotation[] annotationArr, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        String str;
        try {
            str = IOUtils.toString(inputStream);
        } catch (Exception unused) {
            str = null;
        }
        if (str == null) {
            return null;
        }
        return JSON.parseObject(str, cls, this.parserConfig, JSON.DEFAULT_PARSER_FEATURE, this.features);
    }
}
