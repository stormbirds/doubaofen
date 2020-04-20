package org.mp4parser.aspectj.internal.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.AjTypeSystem;
import org.mp4parser.aspectj.lang.reflect.InterTypeFieldDeclaration;

public class InterTypeFieldDeclarationImpl extends InterTypeDeclarationImpl implements InterTypeFieldDeclaration {
    private Type genericType;
    private String name;
    private AjType<?> type;

    public InterTypeFieldDeclarationImpl(AjType<?> ajType, String str, int i, String str2, AjType<?> ajType2, Type type2) {
        super(ajType, str, i);
        this.name = str2;
        this.type = ajType2;
        this.genericType = type2;
    }

    public InterTypeFieldDeclarationImpl(AjType<?> ajType, AjType<?> ajType2, Field field) {
        super(ajType, ajType2, field.getModifiers());
        this.name = field.getName();
        this.type = AjTypeSystem.getAjType(field.getType());
        Type genericType2 = field.getGenericType();
        if (genericType2 instanceof Class) {
            this.genericType = AjTypeSystem.getAjType((Class) genericType2);
        } else {
            this.genericType = genericType2;
        }
    }

    public String getName() {
        return this.name;
    }

    public AjType<?> getType() {
        return this.type;
    }

    public Type getGenericType() {
        return this.genericType;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Modifier.toString(getModifiers()));
        stringBuffer.append(" ");
        stringBuffer.append(getType().toString());
        stringBuffer.append(" ");
        stringBuffer.append(this.targetTypeName);
        stringBuffer.append(".");
        stringBuffer.append(getName());
        return stringBuffer.toString();
    }
}
