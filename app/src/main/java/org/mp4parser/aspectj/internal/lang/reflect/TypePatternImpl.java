package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.TypePattern;

public class TypePatternImpl implements TypePattern {
    private String typePattern;

    public TypePatternImpl(String str) {
        this.typePattern = str;
    }

    public String asString() {
        return this.typePattern;
    }

    public String toString() {
        return asString();
    }
}