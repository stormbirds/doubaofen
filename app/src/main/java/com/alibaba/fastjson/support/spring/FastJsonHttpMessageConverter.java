package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bumptech.glide.load.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final Charset UTF8 = Charset.forName(Key.STRING_CHARSET_NAME);
    private Charset charset = UTF8;
    protected String dateFormat;
    private SerializerFeature[] features = new SerializerFeature[0];
    protected SerializeFilter[] serialzeFilters = new SerializeFilter[0];

    /* access modifiers changed from: protected */
    public boolean supports(Class<?> cls) {
        return true;
    }

    public FastJsonHttpMessageConverter() {
        super(new MediaType[]{new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8)});
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset2) {
        this.charset = charset2;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormat = str;
    }

    public SerializerFeature[] getFeatures() {
        return this.features;
    }

    public void setFeatures(SerializerFeature... serializerFeatureArr) {
        this.features = serializerFeatureArr;
    }

    /* access modifiers changed from: protected */
    public Object readInternal(Class<? extends Object> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream body = httpInputMessage.getBody();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = body.read(bArr);
            if (read == -1) {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                return JSON.parseObject(byteArray, 0, byteArray.length, this.charset.newDecoder(), (Type) cls, new Feature[0]);
            } else if (read > 0) {
                byteArrayOutputStream.write(bArr, 0, read);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeInternal(Object obj, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = httpOutputMessage.getHeaders();
        byte[] bytes = JSON.toJSONString(obj, SerializeConfig.globalInstance, this.serialzeFilters, this.dateFormat, JSON.DEFAULT_GENERATE_FEATURE, this.features).getBytes(this.charset);
        headers.setContentLength((long) bytes.length);
        httpOutputMessage.getBody().write(bytes);
    }

    public void addSerializeFilter(SerializeFilter serializeFilter) {
        if (serializeFilter != null) {
            SerializeFilter[] serializeFilterArr = this.serialzeFilters;
            SerializeFilter[] serializeFilterArr2 = new SerializeFilter[(serializeFilterArr.length + 1)];
            System.arraycopy(serializeFilterArr, 0, serializeFilter, 0, serializeFilterArr.length);
            serializeFilterArr2[serializeFilterArr2.length - 1] = serializeFilter;
            this.serialzeFilters = serializeFilterArr2;
        }
    }
}
