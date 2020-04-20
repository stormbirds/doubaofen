package org.jetbrains.anko.db;

import java.lang.reflect.Constructor;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.anko.AnkoException;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001d\u0010\b\u001a\u00028\u00002\u000e\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0004H\u0016¢\u0006\u0002\u0010\u000bRF\u0010\u0003\u001a8\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005 \u0006*\u001c\u0012\u0016\b\u0001\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\f"}, d2 = {"org/jetbrains/anko/db/ClassParserKt$classParser$1", "Lorg/jetbrains/anko/db/RowParser;", "(Ljava/lang/reflect/Constructor;)V", "parameterTypes", "", "Ljava/lang/Class;", "kotlin.jvm.PlatformType", "[Ljava/lang/Class;", "parseRow", "columns", "", "([Ljava/lang/Object;)Ljava/lang/Object;", "sqlite_release"}, k = 1, mv = {1, 1, 5})
/* compiled from: ClassParser.kt */
public final class ClassParserKt$classParser$1 implements RowParser<T> {
    final /* synthetic */ Constructor $preferredConstructor;
    private final Class<?>[] parameterTypes;

    ClassParserKt$classParser$1(Constructor constructor) {
        this.$preferredConstructor = constructor;
        this.parameterTypes = constructor.getParameterTypes();
    }

    public T parseRow(Object[] objArr) {
        Object[] objArr2 = objArr;
        Intrinsics.checkParameterIsNotNull(objArr2, "columns");
        Class<?>[] clsArr = this.parameterTypes;
        if (((Object[]) clsArr).length == objArr2.length) {
            int i = 0;
            int length = ((Object[]) clsArr).length - 1;
            if (length >= 0) {
                while (true) {
                    Class<?> cls = this.parameterTypes[i];
                    Object obj = objArr2[i];
                    if (!cls.isInstance(obj)) {
                        Intrinsics.checkExpressionValueIsNotNull(cls, "type");
                        objArr2[i] = ClassParserKt.castValue(obj, cls);
                    }
                    if (i == length) {
                        break;
                    }
                    i++;
                }
            }
            return JavaSqliteUtils.newInstance(this.$preferredConstructor, objArr2);
        }
        String joinToString$default = ArraysKt.joinToString$default(objArr, (CharSequence) null, (CharSequence) "[", (CharSequence) "]", 0, (CharSequence) null, (Function1) null, 57, (Object) null);
        String joinToString$default2 = ArraysKt.joinToString$default((Object[]) this.parameterTypes, (CharSequence) null, (CharSequence) "[", (CharSequence) "]", 0, (CharSequence) null, (Function1) ClassParserKt$classParser$1$parseRow$parameterTypesRendered$1.INSTANCE, 25, (Object) null);
        StringBuilder sb = new StringBuilder();
        sb.append("Class parser for " + this.$preferredConstructor.getName() + " ");
        sb.append("failed to parse the row: " + joinToString$default + " (constructor parameter types: " + joinToString$default2 + ")");
        throw new AnkoException(sb.toString());
    }
}
