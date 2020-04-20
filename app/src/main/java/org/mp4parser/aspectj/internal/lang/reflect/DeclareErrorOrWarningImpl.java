package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.DeclareErrorOrWarning;
import org.mp4parser.aspectj.lang.reflect.PointcutExpression;

public class DeclareErrorOrWarningImpl implements DeclareErrorOrWarning {
    private AjType declaringType;
    private boolean isError;
    private String msg;
    private PointcutExpression pc;

    public DeclareErrorOrWarningImpl(String str, String str2, boolean z, AjType ajType) {
        this.pc = new PointcutExpressionImpl(str);
        this.msg = str2;
        this.isError = z;
        this.declaringType = ajType;
    }

    public AjType getDeclaringType() {
        return this.declaringType;
    }

    public PointcutExpression getPointcutExpression() {
        return this.pc;
    }

    public String getMessage() {
        return this.msg;
    }

    public boolean isError() {
        return this.isError;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("declare ");
        stringBuffer.append(isError() ? "error : " : "warning : ");
        stringBuffer.append(getPointcutExpression().asString());
        stringBuffer.append(" : ");
        stringBuffer.append("\"");
        stringBuffer.append(getMessage());
        stringBuffer.append("\"");
        return stringBuffer.toString();
    }
}
