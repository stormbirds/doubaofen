package com.alibaba.fastjson;

import android.support.v4.view.PointerIconCompat;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class JSONReader implements Closeable {
    private JSONStreamContext context;
    private final DefaultJSONParser parser;

    public JSONReader(Reader reader) {
        this((JSONLexer) new JSONReaderScanner(reader));
    }

    public JSONReader(JSONLexer jSONLexer) {
        this(new DefaultJSONParser(jSONLexer));
    }

    public JSONReader(DefaultJSONParser defaultJSONParser) {
        this.parser = defaultJSONParser;
    }

    public void config(Feature feature, boolean z) {
        this.parser.config(feature, z);
    }

    public void startObject() {
        if (this.context == null) {
            this.context = new JSONStreamContext((JSONStreamContext) null, PointerIconCompat.TYPE_CONTEXT_MENU);
        } else {
            startStructure();
            this.context = new JSONStreamContext(this.context, PointerIconCompat.TYPE_CONTEXT_MENU);
        }
        this.parser.accept(12, 18);
    }

    public void endObject() {
        this.parser.accept(13);
        endStructure();
    }

    public void startArray() {
        if (this.context == null) {
            this.context = new JSONStreamContext((JSONStreamContext) null, PointerIconCompat.TYPE_WAIT);
        } else {
            startStructure();
            this.context = new JSONStreamContext(this.context, PointerIconCompat.TYPE_WAIT);
        }
        this.parser.accept(14);
    }

    public void endArray() {
        this.parser.accept(15);
        endStructure();
    }

    private void startStructure() {
        switch (this.context.state) {
            case PointerIconCompat.TYPE_CONTEXT_MENU:
            case PointerIconCompat.TYPE_WAIT:
                return;
            case PointerIconCompat.TYPE_HAND:
                this.parser.accept(17);
                return;
            case PointerIconCompat.TYPE_HELP:
            case 1005:
                this.parser.accept(16);
                return;
            default:
                throw new JSONException("illegal state : " + this.context.state);
        }
    }

    private void endStructure() {
        int i;
        this.context = this.context.parent;
        JSONStreamContext jSONStreamContext = this.context;
        if (jSONStreamContext != null) {
            switch (jSONStreamContext.state) {
                case PointerIconCompat.TYPE_CONTEXT_MENU:
                case PointerIconCompat.TYPE_HELP:
                    i = PointerIconCompat.TYPE_HAND;
                    break;
                case PointerIconCompat.TYPE_HAND:
                    i = PointerIconCompat.TYPE_HELP;
                    break;
                case PointerIconCompat.TYPE_WAIT:
                    i = 1005;
                    break;
                default:
                    i = -1;
                    break;
            }
            if (i != -1) {
                this.context.state = i;
            }
        }
    }

    public boolean hasNext() {
        if (this.context != null) {
            int i = this.parser.lexer.token();
            int i2 = this.context.state;
            switch (i2) {
                case PointerIconCompat.TYPE_CONTEXT_MENU:
                case PointerIconCompat.TYPE_HELP:
                    if (i != 13) {
                        return true;
                    }
                    return false;
                case PointerIconCompat.TYPE_WAIT:
                case 1005:
                    if (i != 15) {
                        return true;
                    }
                    return false;
                default:
                    throw new JSONException("illegal state : " + i2);
            }
        } else {
            throw new JSONException("context is null");
        }
    }

    public int peek() {
        if (this.context != null) {
            return this.parser.lexer.token();
        }
        throw new JSONException("context is null");
    }

    public void close() {
        IOUtils.close(this.parser);
    }

    public Integer readInteger() {
        Object obj;
        if (this.context == null) {
            obj = this.parser.parse();
        } else {
            readBefore();
            obj = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToInt(obj);
    }

    public Long readLong() {
        Object obj;
        if (this.context == null) {
            obj = this.parser.parse();
        } else {
            readBefore();
            obj = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToLong(obj);
    }

    public String readString() {
        Object obj;
        if (this.context == null) {
            obj = this.parser.parse();
        } else {
            readBefore();
            obj = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToString(obj);
    }

    public <T> T readObject(TypeReference<T> typeReference) {
        return readObject(typeReference.getType());
    }

    public <T> T readObject(Type type) {
        if (this.context == null) {
            return this.parser.parseObject(type);
        }
        readBefore();
        T parseObject = this.parser.parseObject(type);
        readAfter();
        return parseObject;
    }

    public <T> T readObject(Class<T> cls) {
        if (this.context == null) {
            return this.parser.parseObject(cls);
        }
        readBefore();
        T parseObject = this.parser.parseObject(cls);
        readAfter();
        return parseObject;
    }

    public void readObject(Object obj) {
        if (this.context == null) {
            this.parser.parseObject(obj);
            return;
        }
        readBefore();
        this.parser.parseObject(obj);
        readAfter();
    }

    public Object readObject() {
        Object obj;
        if (this.context == null) {
            return this.parser.parse();
        }
        readBefore();
        int i = this.context.state;
        if (i == 1001 || i == 1003) {
            obj = this.parser.parseKey();
        } else {
            obj = this.parser.parse();
        }
        readAfter();
        return obj;
    }

    public Object readObject(Map map) {
        if (this.context == null) {
            return this.parser.parseObject(map);
        }
        readBefore();
        Object parseObject = this.parser.parseObject(map);
        readAfter();
        return parseObject;
    }

    private void readBefore() {
        int i = this.context.state;
        switch (i) {
            case PointerIconCompat.TYPE_CONTEXT_MENU:
            case PointerIconCompat.TYPE_WAIT:
                return;
            case PointerIconCompat.TYPE_HAND:
                this.parser.accept(17);
                return;
            case PointerIconCompat.TYPE_HELP:
                this.parser.accept(16, 18);
                return;
            case 1005:
                this.parser.accept(16);
                return;
            default:
                throw new JSONException("illegal state : " + i);
        }
    }

    private void readAfter() {
        int i = this.context.state;
        int i2 = PointerIconCompat.TYPE_HAND;
        switch (i) {
            case PointerIconCompat.TYPE_CONTEXT_MENU:
            case PointerIconCompat.TYPE_HELP:
                break;
            case PointerIconCompat.TYPE_HAND:
                i2 = PointerIconCompat.TYPE_HELP;
                break;
            case PointerIconCompat.TYPE_WAIT:
                i2 = 1005;
                break;
            case 1005:
                i2 = -1;
                break;
            default:
                throw new JSONException("illegal state : " + i);
        }
        if (i2 != -1) {
            this.context.state = i2;
        }
    }
}
