package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;

public class AwtCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AwtCodec instance = new AwtCodec();

    public int getFastMatchToken() {
        return 12;
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        if (obj instanceof Point) {
            Point point = (Point) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Point.class, '{'), "x", point.getX());
            serializeWriter.writeFieldValue(',', "y", point.getY());
        } else if (obj instanceof Font) {
            Font font = (Font) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Font.class, '{'), "name", font.getName());
            serializeWriter.writeFieldValue(',', "style", font.getStyle());
            serializeWriter.writeFieldValue(',', "size", font.getSize());
        } else if (obj instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Rectangle.class, '{'), "x", rectangle.getX());
            serializeWriter.writeFieldValue(',', "y", rectangle.getY());
            serializeWriter.writeFieldValue(',', "width", rectangle.getWidth());
            serializeWriter.writeFieldValue(',', "height", rectangle.getHeight());
        } else if (obj instanceof Color) {
            Color color = (Color) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Color.class, '{'), "r", color.getRed());
            serializeWriter.writeFieldValue(',', "g", color.getGreen());
            serializeWriter.writeFieldValue(',', "b", color.getBlue());
            if (color.getAlpha() > 0) {
                serializeWriter.writeFieldValue(',', "alpha", color.getAlpha());
            }
        } else {
            throw new JSONException("not support awt class : " + obj.getClass().getName());
        }
        serializeWriter.write(125);
    }

    /* access modifiers changed from: protected */
    public char writeClassName(SerializeWriter serializeWriter, Class<?> cls, char c) {
        if (!serializeWriter.isEnabled(SerializerFeature.WriteClassName)) {
            return c;
        }
        serializeWriter.write(123);
        serializeWriter.writeFieldName(JSON.DEFAULT_TYPE_KEY);
        serializeWriter.writeString(cls.getName());
        return ',';
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken(16);
            return null;
        } else if (jSONLexer.token() == 12 || jSONLexer.token() == 16) {
            jSONLexer.nextToken();
            if (type == Point.class) {
                return parsePoint(defaultJSONParser);
            }
            if (type == Rectangle.class) {
                return parseRectangle(defaultJSONParser);
            }
            if (type == Color.class) {
                return parseColor(defaultJSONParser);
            }
            if (type == Font.class) {
                return parseFont(defaultJSONParser);
            }
            throw new JSONException("not support awt class : " + type);
        } else {
            throw new JSONException("syntax error");
        }
    }

    /* access modifiers changed from: protected */
    public Font parseFont(DefaultJSONParser defaultJSONParser) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = 0;
        String str = null;
        int i2 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() == 4) {
                String stringVal = jSONLexer.stringVal();
                jSONLexer.nextTokenWithColon(2);
                if (stringVal.equalsIgnoreCase("name")) {
                    if (jSONLexer.token() == 4) {
                        str = jSONLexer.stringVal();
                        jSONLexer.nextToken();
                    } else {
                        throw new JSONException("syntax error");
                    }
                } else if (stringVal.equalsIgnoreCase("style")) {
                    if (jSONLexer.token() == 2) {
                        i = jSONLexer.intValue();
                        jSONLexer.nextToken();
                    } else {
                        throw new JSONException("syntax error");
                    }
                } else if (!stringVal.equalsIgnoreCase("size")) {
                    throw new JSONException("syntax error, " + stringVal);
                } else if (jSONLexer.token() == 2) {
                    i2 = jSONLexer.intValue();
                    jSONLexer.nextToken();
                } else {
                    throw new JSONException("syntax error");
                }
                if (jSONLexer.token() == 16) {
                    jSONLexer.nextToken(4);
                }
            } else {
                throw new JSONException("syntax error");
            }
        }
        jSONLexer.nextToken();
        return new Font(str, i, i2);
    }

    /* access modifiers changed from: protected */
    public Color parseColor(DefaultJSONParser defaultJSONParser) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() == 4) {
                String stringVal = jSONLexer.stringVal();
                jSONLexer.nextTokenWithColon(2);
                if (jSONLexer.token() == 2) {
                    int intValue = jSONLexer.intValue();
                    jSONLexer.nextToken();
                    if (stringVal.equalsIgnoreCase("r")) {
                        i = intValue;
                    } else if (stringVal.equalsIgnoreCase("g")) {
                        i2 = intValue;
                    } else if (stringVal.equalsIgnoreCase("b")) {
                        i3 = intValue;
                    } else if (stringVal.equalsIgnoreCase("alpha")) {
                        i4 = intValue;
                    } else {
                        throw new JSONException("syntax error, " + stringVal);
                    }
                    if (jSONLexer.token() == 16) {
                        jSONLexer.nextToken(4);
                    }
                } else {
                    throw new JSONException("syntax error");
                }
            } else {
                throw new JSONException("syntax error");
            }
        }
        jSONLexer.nextToken();
        return new Color(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public Rectangle parseRectangle(DefaultJSONParser defaultJSONParser) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() == 4) {
                String stringVal = jSONLexer.stringVal();
                jSONLexer.nextTokenWithColon(2);
                if (jSONLexer.token() == 2) {
                    int intValue = jSONLexer.intValue();
                    jSONLexer.nextToken();
                    if (stringVal.equalsIgnoreCase("x")) {
                        i = intValue;
                    } else if (stringVal.equalsIgnoreCase("y")) {
                        i2 = intValue;
                    } else if (stringVal.equalsIgnoreCase("width")) {
                        i3 = intValue;
                    } else if (stringVal.equalsIgnoreCase("height")) {
                        i4 = intValue;
                    } else {
                        throw new JSONException("syntax error, " + stringVal);
                    }
                    if (jSONLexer.token() == 16) {
                        jSONLexer.nextToken(4);
                    }
                } else {
                    throw new JSONException("syntax error");
                }
            } else {
                throw new JSONException("syntax error");
            }
        }
        jSONLexer.nextToken();
        return new Rectangle(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public Point parsePoint(DefaultJSONParser defaultJSONParser) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = 0;
        int i2 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() == 4) {
                String stringVal = jSONLexer.stringVal();
                if (JSON.DEFAULT_TYPE_KEY.equals(stringVal)) {
                    defaultJSONParser.acceptType("java.awt.Point");
                } else {
                    jSONLexer.nextTokenWithColon(2);
                    if (jSONLexer.token() == 2) {
                        int intValue = jSONLexer.intValue();
                        jSONLexer.nextToken();
                        if (stringVal.equalsIgnoreCase("x")) {
                            i = intValue;
                        } else if (stringVal.equalsIgnoreCase("y")) {
                            i2 = intValue;
                        } else {
                            throw new JSONException("syntax error, " + stringVal);
                        }
                        if (jSONLexer.token() == 16) {
                            jSONLexer.nextToken(4);
                        }
                    } else {
                        throw new JSONException("syntax error : " + jSONLexer.tokenName());
                    }
                }
            } else {
                throw new JSONException("syntax error");
            }
        }
        jSONLexer.nextToken();
        return new Point(i, i2);
    }
}
