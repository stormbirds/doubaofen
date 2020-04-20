package org.mp4parser.aspectj.internal.lang.reflect;

import org.mp4parser.aspectj.lang.reflect.PerClauseKind;
import org.mp4parser.aspectj.lang.reflect.PointcutBasedPerClause;
import org.mp4parser.aspectj.lang.reflect.PointcutExpression;

public class PointcutBasedPerClauseImpl extends PerClauseImpl implements PointcutBasedPerClause {
    private final PointcutExpression pointcutExpression;

    public PointcutBasedPerClauseImpl(PerClauseKind perClauseKind, String str) {
        super(perClauseKind);
        this.pointcutExpression = new PointcutExpressionImpl(str);
    }

    public PointcutExpression getPointcutExpression() {
        return this.pointcutExpression;
    }

    /* renamed from: org.mp4parser.aspectj.internal.lang.reflect.PointcutBasedPerClauseImpl$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$aspectj$lang$reflect$PerClauseKind = new int[PerClauseKind.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            /*
                org.mp4parser.aspectj.lang.reflect.PerClauseKind[] r0 = org.mp4parser.aspectj.lang.reflect.PerClauseKind.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$aspectj$lang$reflect$PerClauseKind = r0
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$PerClauseKind     // Catch:{ NoSuchFieldError -> 0x0014 }
                org.mp4parser.aspectj.lang.reflect.PerClauseKind r1 = org.mp4parser.aspectj.lang.reflect.PerClauseKind.PERCFLOW     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$PerClauseKind     // Catch:{ NoSuchFieldError -> 0x001f }
                org.mp4parser.aspectj.lang.reflect.PerClauseKind r1 = org.mp4parser.aspectj.lang.reflect.PerClauseKind.PERCFLOWBELOW     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$PerClauseKind     // Catch:{ NoSuchFieldError -> 0x002a }
                org.mp4parser.aspectj.lang.reflect.PerClauseKind r1 = org.mp4parser.aspectj.lang.reflect.PerClauseKind.PERTARGET     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$org$aspectj$lang$reflect$PerClauseKind     // Catch:{ NoSuchFieldError -> 0x0035 }
                org.mp4parser.aspectj.lang.reflect.PerClauseKind r1 = org.mp4parser.aspectj.lang.reflect.PerClauseKind.PERTHIS     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mp4parser.aspectj.internal.lang.reflect.PointcutBasedPerClauseImpl.AnonymousClass1.<clinit>():void");
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = AnonymousClass1.$SwitchMap$org$aspectj$lang$reflect$PerClauseKind[getKind().ordinal()];
        if (i == 1) {
            stringBuffer.append("percflow(");
        } else if (i == 2) {
            stringBuffer.append("percflowbelow(");
        } else if (i == 3) {
            stringBuffer.append("pertarget(");
        } else if (i == 4) {
            stringBuffer.append("perthis(");
        }
        stringBuffer.append(this.pointcutExpression.asString());
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}
