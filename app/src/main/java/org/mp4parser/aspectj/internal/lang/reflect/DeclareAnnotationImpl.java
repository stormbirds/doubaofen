package org.mp4parser.aspectj.internal.lang.reflect;

import java.lang.annotation.Annotation;
import org.mp4parser.aspectj.lang.reflect.AjType;
import org.mp4parser.aspectj.lang.reflect.DeclareAnnotation;
import org.mp4parser.aspectj.lang.reflect.SignaturePattern;
import org.mp4parser.aspectj.lang.reflect.TypePattern;

public class DeclareAnnotationImpl implements DeclareAnnotation {
    private String annText;
    private AjType<?> declaringType;
    private DeclareAnnotation.Kind kind;
    private SignaturePattern signaturePattern;
    private Annotation theAnnotation;
    private TypePattern typePattern;

    public DeclareAnnotationImpl(AjType<?> ajType, String str, String str2, Annotation annotation, String str3) {
        this.declaringType = ajType;
        if (str.equals("at_type")) {
            this.kind = DeclareAnnotation.Kind.Type;
        } else if (str.equals("at_field")) {
            this.kind = DeclareAnnotation.Kind.Field;
        } else if (str.equals("at_method")) {
            this.kind = DeclareAnnotation.Kind.Method;
        } else if (str.equals("at_constructor")) {
            this.kind = DeclareAnnotation.Kind.Constructor;
        } else {
            throw new IllegalStateException("Unknown declare annotation kind: " + str);
        }
        if (this.kind == DeclareAnnotation.Kind.Type) {
            this.typePattern = new TypePatternImpl(str2);
        } else {
            this.signaturePattern = new SignaturePatternImpl(str2);
        }
        this.theAnnotation = annotation;
        this.annText = str3;
    }

    public AjType<?> getDeclaringType() {
        return this.declaringType;
    }

    public DeclareAnnotation.Kind getKind() {
        return this.kind;
    }

    public SignaturePattern getSignaturePattern() {
        return this.signaturePattern;
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    public Annotation getAnnotation() {
        return this.theAnnotation;
    }

    public String getAnnotationAsText() {
        return this.annText;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("declare @");
        int i = AnonymousClass1.$SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind[getKind().ordinal()];
        if (i == 1) {
            stringBuffer.append("type : ");
            stringBuffer.append(getTypePattern().asString());
        } else if (i == 2) {
            stringBuffer.append("method : ");
            stringBuffer.append(getSignaturePattern().asString());
        } else if (i == 3) {
            stringBuffer.append("field : ");
            stringBuffer.append(getSignaturePattern().asString());
        } else if (i == 4) {
            stringBuffer.append("constructor : ");
            stringBuffer.append(getSignaturePattern().asString());
        }
        stringBuffer.append(" : ");
        stringBuffer.append(getAnnotationAsText());
        return stringBuffer.toString();
    }

    /* renamed from: org.mp4parser.aspectj.internal.lang.reflect.DeclareAnnotationImpl$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind = new int[DeclareAnnotation.Kind.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                org.mp4parser.aspectj.lang.reflect.DeclareAnnotation$Kind[] r0 = org.mp4parser.aspectj.lang.reflect.DeclareAnnotation.Kind.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind = r0
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind     // Catch:{ NoSuchFieldError -> 0x0014 }
                org.mp4parser.aspectj.lang.reflect.DeclareAnnotation$Kind r1 = org.mp4parser.aspectj.lang.reflect.DeclareAnnotation.Kind.Type     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind     // Catch:{ NoSuchFieldError -> 0x001f }
                org.mp4parser.aspectj.lang.reflect.DeclareAnnotation$Kind r1 = org.mp4parser.aspectj.lang.reflect.DeclareAnnotation.Kind.Method     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind     // Catch:{ NoSuchFieldError -> 0x002a }
                org.mp4parser.aspectj.lang.reflect.DeclareAnnotation$Kind r1 = org.mp4parser.aspectj.lang.reflect.DeclareAnnotation.Kind.Field     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$DeclareAnnotation$Kind     // Catch:{ NoSuchFieldError -> 0x0035 }
                org.mp4parser.aspectj.lang.reflect.DeclareAnnotation$Kind r1 = org.mp4parser.aspectj.lang.reflect.DeclareAnnotation.Kind.Constructor     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mp4parser.aspectj.internal.lang.reflect.DeclareAnnotationImpl.AnonymousClass1.<clinit>():void");
        }
    }
}
