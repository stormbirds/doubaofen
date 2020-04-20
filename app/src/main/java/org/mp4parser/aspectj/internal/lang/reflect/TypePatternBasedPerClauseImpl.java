package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.PerClauseKind;
import org.mp4parser.aspectj.lang.reflect.TypePattern;
import org.mp4parser.aspectj.lang.reflect.TypePatternBasedPerClause;

public class TypePatternBasedPerClauseImpl extends PerClauseImpl implements TypePatternBasedPerClause {
    private TypePattern typePattern;

    public TypePatternBasedPerClauseImpl(PerClauseKind perClauseKind, String str) {
        super(perClauseKind);
        this.typePattern = new TypePatternImpl(str);
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    public String toString() {
        return "pertypewithin(" + this.typePattern.asString() + ")";
    }
}
