package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bumptech.glide.load.Key;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

public class FastJsonJsonView extends AbstractView {
    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final Charset UTF8 = Charset.forName(Key.STRING_CHARSET_NAME);
    private Charset charset = UTF8;
    private boolean disableCaching = true;
    private boolean extractValueFromSingleKeyModel = false;
    private Set<String> renderedAttributes;
    private SerializerFeature[] serializerFeatures = new SerializerFeature[0];
    private boolean updateContentLength = false;

    public FastJsonJsonView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    public void setRenderedAttributes(Set<String> set) {
        this.renderedAttributes = set;
    }

    @Deprecated
    public void setSerializerFeature(SerializerFeature... serializerFeatureArr) {
        setFeatures(serializerFeatureArr);
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset2) {
        this.charset = charset2;
    }

    public SerializerFeature[] getFeatures() {
        return this.serializerFeatures;
    }

    public void setFeatures(SerializerFeature... serializerFeatureArr) {
        this.serializerFeatures = serializerFeatureArr;
    }

    public boolean isExtractValueFromSingleKeyModel() {
        return this.extractValueFromSingleKeyModel;
    }

    public void setExtractValueFromSingleKeyModel(boolean z) {
        this.extractValueFromSingleKeyModel = z;
    }

    /* access modifiers changed from: protected */
    public void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] bytes = JSON.toJSONString(filterModel(map), this.serializerFeatures).getBytes(this.charset);
        ByteArrayOutputStream createTemporaryOutputStream = this.updateContentLength ? createTemporaryOutputStream() : httpServletResponse.getOutputStream();
        createTemporaryOutputStream.write(bytes);
        if (this.updateContentLength) {
            writeToResponse(httpServletResponse, createTemporaryOutputStream);
        }
    }

    /* access modifiers changed from: protected */
    public void prepareResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        setResponseContentType(httpServletRequest, httpServletResponse);
        httpServletResponse.setCharacterEncoding(UTF8.name());
        if (this.disableCaching) {
            httpServletResponse.addHeader("Pragma", "no-cache");
            httpServletResponse.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
            httpServletResponse.addDateHeader("Expires", 1);
        }
    }

    public void setDisableCaching(boolean z) {
        this.disableCaching = z;
    }

    public void setUpdateContentLength(boolean z) {
        this.updateContentLength = z;
    }

    /* access modifiers changed from: protected */
    public Object filterModel(Map<String, Object> map) {
        HashMap hashMap = new HashMap(map.size());
        Set<String> keySet = !CollectionUtils.isEmpty(this.renderedAttributes) ? this.renderedAttributes : map.keySet();
        for (Map.Entry next : map.entrySet()) {
            if (!(next.getValue() instanceof BindingResult) && keySet.contains(next.getKey())) {
                hashMap.put(next.getKey(), next.getValue());
            }
        }
        if (this.extractValueFromSingleKeyModel && hashMap.size() == 1) {
            Iterator it = hashMap.entrySet().iterator();
            if (it.hasNext()) {
                return ((Map.Entry) it.next()).getValue();
            }
        }
        return hashMap;
    }
}
