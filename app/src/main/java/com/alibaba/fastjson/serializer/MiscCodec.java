package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

public class MiscCodec implements ObjectSerializer, ObjectDeserializer {
    public static final MiscCodec instance = new MiscCodec();

    public int getFastMatchToken() {
        return 4;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        String str;
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        Class<?> cls = obj.getClass();
        if (cls == SimpleDateFormat.class) {
            str = ((SimpleDateFormat) obj).toPattern();
            if (serializeWriter.isEnabled(SerializerFeature.WriteClassName) && obj.getClass() != type) {
                serializeWriter.write(123);
                serializeWriter.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                jSONSerializer.write(obj.getClass().getName());
                serializeWriter.writeFieldValue(',', "val", str);
                serializeWriter.write(125);
                return;
            }
        } else if (cls == Class.class) {
            str = ((Class) obj).getName();
        } else if (cls == InetSocketAddress.class) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) obj;
            InetAddress address = inetSocketAddress.getAddress();
            serializeWriter.write(123);
            if (address != null) {
                serializeWriter.writeFieldName("address");
                jSONSerializer.write((Object) address);
                serializeWriter.write(44);
            }
            serializeWriter.writeFieldName("port");
            serializeWriter.writeInt(inetSocketAddress.getPort());
            serializeWriter.write(125);
            return;
        } else if (obj instanceof File) {
            str = ((File) obj).getPath();
        } else if (obj instanceof InetAddress) {
            str = ((InetAddress) obj).getHostAddress();
        } else if (obj instanceof TimeZone) {
            str = ((TimeZone) obj).getID();
        } else if (obj instanceof JSONStreamAware) {
            ((JSONStreamAware) obj).writeJSONString(serializeWriter);
            return;
        } else if (obj instanceof Iterator) {
            writeIterator(jSONSerializer, serializeWriter, (Iterator) obj);
            return;
        } else if (obj instanceof Iterable) {
            writeIterator(jSONSerializer, serializeWriter, ((Iterable) obj).iterator());
            return;
        } else {
            str = obj.toString();
        }
        serializeWriter.writeString(str);
    }

    /* access modifiers changed from: protected */
    public void writeIterator(JSONSerializer jSONSerializer, SerializeWriter serializeWriter, Iterator<?> it) {
        serializeWriter.write(91);
        int i = 0;
        while (it.hasNext()) {
            if (i != 0) {
                serializeWriter.write(44);
            }
            jSONSerializer.write((Object) it.next());
            i++;
        }
        serializeWriter.write(93);
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Object obj2;
        String str;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        InetAddress inetAddress = null;
        int i = 0;
        if (type != InetSocketAddress.class) {
            if (defaultJSONParser.resolveStatus == 2) {
                defaultJSONParser.resolveStatus = 0;
                defaultJSONParser.accept(16);
                if (jSONLexer.token() != 4) {
                    throw new JSONException("syntax error");
                } else if ("val".equals(jSONLexer.stringVal())) {
                    jSONLexer.nextToken();
                    defaultJSONParser.accept(17);
                    obj2 = defaultJSONParser.parse();
                    defaultJSONParser.accept(13);
                } else {
                    throw new JSONException("syntax error");
                }
            } else {
                obj2 = defaultJSONParser.parse();
            }
            if (obj2 == null) {
                str = null;
            } else if (obj2 instanceof String) {
                str = (String) obj2;
            } else {
                throw new JSONException("expect string");
            }
            if (str == null || str.length() == 0) {
                return null;
            }
            if (type == UUID.class) {
                return UUID.fromString(str);
            }
            if (type == URI.class) {
                return URI.create(str);
            }
            if (type == URL.class) {
                try {
                    return new URL(str);
                } catch (MalformedURLException e) {
                    throw new JSONException("create url error", e);
                }
            } else if (type == Pattern.class) {
                return Pattern.compile(str);
            } else {
                if (type == Locale.class) {
                    String[] split = str.split("_");
                    if (split.length == 1) {
                        return new Locale(split[0]);
                    }
                    if (split.length == 2) {
                        return new Locale(split[0], split[1]);
                    }
                    return new Locale(split[0], split[1], split[2]);
                } else if (type == SimpleDateFormat.class) {
                    T simpleDateFormat = new SimpleDateFormat(str, jSONLexer.getLocale());
                    simpleDateFormat.setTimeZone(jSONLexer.getTimeZone());
                    return simpleDateFormat;
                } else if (type == InetAddress.class || type == Inet4Address.class || type == Inet6Address.class) {
                    try {
                        return InetAddress.getByName(str);
                    } catch (UnknownHostException e2) {
                        throw new JSONException("deserialize inet adress error", e2);
                    }
                } else if (type == File.class) {
                    return new File(str);
                } else {
                    if (type == TimeZone.class) {
                        return TimeZone.getTimeZone(str);
                    }
                    if (type instanceof ParameterizedType) {
                        type = ((ParameterizedType) type).getRawType();
                    }
                    if (type == Class.class) {
                        return TypeUtils.loadClass(str, defaultJSONParser.getConfig().getDefaultClassLoader());
                    }
                    throw new JSONException("MiscCodec not support " + type);
                }
            }
        } else if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            return null;
        } else {
            defaultJSONParser.accept(12);
            while (true) {
                String stringVal = jSONLexer.stringVal();
                jSONLexer.nextToken(17);
                if (stringVal.equals("address")) {
                    defaultJSONParser.accept(17);
                    inetAddress = (InetAddress) defaultJSONParser.parseObject(InetAddress.class);
                } else if (stringVal.equals("port")) {
                    defaultJSONParser.accept(17);
                    if (jSONLexer.token() == 2) {
                        int intValue = jSONLexer.intValue();
                        jSONLexer.nextToken();
                        i = intValue;
                    } else {
                        throw new JSONException("port is not int");
                    }
                } else {
                    defaultJSONParser.accept(17);
                    defaultJSONParser.parse();
                }
                if (jSONLexer.token() == 16) {
                    jSONLexer.nextToken();
                } else {
                    defaultJSONParser.accept(13);
                    return new InetSocketAddress(inetAddress, i);
                }
            }
        }
    }
}
