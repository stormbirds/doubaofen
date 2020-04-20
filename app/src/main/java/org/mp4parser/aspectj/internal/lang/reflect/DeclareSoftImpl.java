package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.AjTypeSystem;
import org.mp4parser.aspectj.lang.reflect.DeclareSoft;
import org.mp4parser.aspectj.lang.reflect.PointcutExpression;

public class DeclareSoftImpl implements DeclareSoft {
    private AjType<?> declaringType;
    private AjType<?> exceptionType;
    private String missingTypeName;
    private PointcutExpression pointcut;

    public DeclareSoftImpl(AjType<?> ajType, String str, String str2) {
        this.declaringType = ajType;
        this.pointcut = new PointcutExpressionImpl(str);
        try {
            this.exceptionType = AjTypeSystem.getAjType(Class.forName(str2, false, ajType.getJavaClass().getClassLoader()));
        } catch (ClassNotFoundException unused) {
            this.missingTypeName = str2;
        }
    }

    public AjType getDeclaringType() {
        return this.declaringType;
    }

    public AjType getSoftenedExceptionType() throws ClassNotFoundException {
        String str = this.missingTypeName;
        if (str == null) {
            return this.exceptionType;
        }
        throw new ClassNotFoundException(str);
    }

    public PointcutExpression getPointcutExpression() {
        return this.pointcut;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("declare soft : ");
        String str = this.missingTypeName;
        if (str != null) {
            stringBuffer.append(this.exceptionType.getName());
        } else {
            stringBuffer.append(str);
        }
        stringBuffer.append(" : ");
        stringBuffer.append(getPointcutExpression().asString());
        return stringBuffer.toString();
    }
}
