package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.UserBox;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyBoxParserImpl extends AbstractBoxParser {
    static String[] EMPTY_STRING_ARRAY = new String[0];
    StringBuilder buildLookupStrings = new StringBuilder();
    ThreadLocal<String> clazzName = new ThreadLocal<>();
    Pattern constuctorPattern = Pattern.compile("(.*)\\((.*?)\\)");
    Properties mapping;
    ThreadLocal<String[]> param = new ThreadLocal<>();

    public PropertyBoxParserImpl(String... strArr) {
        InputStream openStream;
        InputStream resourceAsStream = getClass().getResourceAsStream("/isoparser-default.properties");
        try {
            this.mapping = new Properties();
            try {
                this.mapping.load(resourceAsStream);
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                Enumeration<URL> resources = (contextClassLoader == null ? ClassLoader.getSystemClassLoader() : contextClassLoader).getResources("isoparser-custom.properties");
                while (resources.hasMoreElements()) {
                    openStream = resources.nextElement().openStream();
                    this.mapping.load(openStream);
                    openStream.close();
                }
                for (String resourceAsStream2 : strArr) {
                    this.mapping.load(getClass().getResourceAsStream(resourceAsStream2));
                }
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            } catch (Throwable th) {
                openStream.close();
                throw th;
            }
        } catch (Throwable th2) {
            try {
                resourceAsStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            throw th2;
        }
    }

    public PropertyBoxParserImpl(Properties properties) {
        this.mapping = properties;
    }

    public Box createBox(String str, byte[] bArr, String str2) {
        invoke(str, bArr, str2);
        String[] strArr = this.param.get();
        try {
            Class<?> cls = Class.forName(this.clazzName.get());
            if (strArr.length <= 0) {
                return (Box) cls.newInstance();
            }
            Class[] clsArr = new Class[strArr.length];
            Object[] objArr = new Object[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                if ("userType".equals(strArr[i])) {
                    objArr[i] = bArr;
                    clsArr[i] = byte[].class;
                } else if ("type".equals(strArr[i])) {
                    objArr[i] = str;
                    clsArr[i] = String.class;
                } else if ("parent".equals(strArr[i])) {
                    objArr[i] = str2;
                    clsArr[i] = String.class;
                } else {
                    throw new InternalError("No such param: " + strArr[i]);
                }
            }
            return (Box) cls.getConstructor(clsArr).newInstance(objArr);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3);
        } catch (InvocationTargetException e4) {
            throw new RuntimeException(e4);
        } catch (NoSuchMethodException e5) {
            throw new RuntimeException(e5);
        }
    }

    public void invoke(String str, byte[] bArr, String str2) {
        String str3;
        if (bArr == null) {
            str3 = this.mapping.getProperty(str);
            if (str3 == null) {
                StringBuilder sb = this.buildLookupStrings;
                sb.append(str2);
                sb.append('-');
                sb.append(str);
                String sb2 = sb.toString();
                this.buildLookupStrings.setLength(0);
                str3 = this.mapping.getProperty(sb2);
            }
        } else if (UserBox.TYPE.equals(str)) {
            Properties properties = this.mapping;
            str3 = properties.getProperty("uuid[" + Hex.encodeHex(bArr).toUpperCase() + "]");
            if (str3 == null) {
                Properties properties2 = this.mapping;
                str3 = properties2.getProperty(String.valueOf(str2) + "-uuid[" + Hex.encodeHex(bArr).toUpperCase() + "]");
            }
            if (str3 == null) {
                str3 = this.mapping.getProperty(UserBox.TYPE);
            }
        } else {
            throw new RuntimeException("we have a userType but no uuid box type. Something's wrong");
        }
        if (str3 == null) {
            str3 = this.mapping.getProperty("default");
        }
        if (str3 == null) {
            throw new RuntimeException("No box object found for " + str);
        } else if (!str3.endsWith(")")) {
            this.param.set(EMPTY_STRING_ARRAY);
            this.clazzName.set(str3);
        } else {
            Matcher matcher = this.constuctorPattern.matcher(str3);
            if (matcher.matches()) {
                this.clazzName.set(matcher.group(1));
                if (matcher.group(2).length() == 0) {
                    this.param.set(EMPTY_STRING_ARRAY);
                } else {
                    this.param.set(matcher.group(2).length() > 0 ? matcher.group(2).split(",") : new String[0]);
                }
            } else {
                throw new RuntimeException("Cannot work with that constructor: " + str3);
            }
        }
    }
}
