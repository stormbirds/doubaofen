package org.jetbrains.anko.db;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.anko.AnkoException;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a \u0010\u0000\u001a\u0004\u0018\u00010\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0002\u001a\u001b\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\n\b\u0000\u0010\u0007\u0018\u0001*\u00020\u0001H\b\u001a\"\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0004H\u0001\u001a\u0014\u0010\t\u001a\u00020\n2\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0002¨\u0006\u000b"}, d2 = {"castValue", "", "value", "type", "Ljava/lang/Class;", "classParser", "Lorg/jetbrains/anko/db/RowParser;", "T", "clazz", "hasApplicableType", "", "sqlite_release"}, k = 2, mv = {1, 1, 5})
/* compiled from: ClassParser.kt */
public final class ClassParserKt {
    private static final <T> RowParser<T> classParser() {
        Intrinsics.reifiedOperationMarker(4, "T");
        return classParser(Object.class);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0053, code lost:
        if (r6 != false) goto L_0x0057;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> org.jetbrains.anko.db.RowParser<T> classParser(java.lang.Class<T> r9) {
        /*
            java.lang.String r0 = "clazz"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r0)
            java.lang.reflect.Constructor[] r0 = r9.getDeclaredConstructors()
            java.lang.Object[] r0 = (java.lang.Object[]) r0
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.Collection r1 = (java.util.Collection) r1
            r2 = 0
            r3 = 0
        L_0x0014:
            int r4 = r0.length
            r5 = 1
            if (r3 >= r4) goto L_0x005f
            r4 = r0[r3]
            r6 = r4
            java.lang.reflect.Constructor r6 = (java.lang.reflect.Constructor) r6
            boolean r7 = r6.isVarArgs()
            if (r7 != 0) goto L_0x0056
            int r7 = r6.getModifiers()
            boolean r7 = java.lang.reflect.Modifier.isPublic(r7)
            if (r7 != 0) goto L_0x002e
            goto L_0x0056
        L_0x002e:
            java.lang.Class[] r6 = r6.getParameterTypes()
            if (r6 == 0) goto L_0x0056
            java.lang.Object[] r6 = (java.lang.Object[]) r6
            int r7 = r6.length
            if (r7 != 0) goto L_0x003b
            r7 = 1
            goto L_0x003c
        L_0x003b:
            r7 = 0
        L_0x003c:
            r7 = r7 ^ r5
            if (r7 == 0) goto L_0x0056
            r7 = 0
        L_0x0040:
            int r8 = r6.length
            if (r7 >= r8) goto L_0x0052
            r8 = r6[r7]
            java.lang.Class r8 = (java.lang.Class) r8
            boolean r8 = hasApplicableType(r8)
            if (r8 != 0) goto L_0x004f
            r6 = 0
            goto L_0x0053
        L_0x004f:
            int r7 = r7 + 1
            goto L_0x0040
        L_0x0052:
            r6 = 1
        L_0x0053:
            if (r6 == 0) goto L_0x0056
            goto L_0x0057
        L_0x0056:
            r5 = 0
        L_0x0057:
            if (r5 == 0) goto L_0x005c
            r1.add(r4)
        L_0x005c:
            int r3 = r3 + 1
            goto L_0x0014
        L_0x005f:
            java.util.List r1 = (java.util.List) r1
            boolean r0 = r1.isEmpty()
            if (r0 != 0) goto L_0x00b6
            int r9 = r1.size()
            if (r9 <= r5) goto L_0x00a8
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.Collection r9 = (java.util.Collection) r9
            java.util.Iterator r0 = r1.iterator()
        L_0x007a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0093
            java.lang.Object r1 = r0.next()
            r2 = r1
            java.lang.reflect.Constructor r2 = (java.lang.reflect.Constructor) r2
            java.lang.Class<org.jetbrains.anko.db.ClassParserConstructor> r3 = org.jetbrains.anko.db.ClassParserConstructor.class
            boolean r2 = r2.isAnnotationPresent(r3)
            if (r2 == 0) goto L_0x007a
            r9.add(r1)
            goto L_0x007a
        L_0x0093:
            java.util.List r9 = (java.util.List) r9
            java.lang.Object r9 = kotlin.collections.CollectionsKt.singleOrNull(r9)
            java.lang.reflect.Constructor r9 = (java.lang.reflect.Constructor) r9
            if (r9 == 0) goto L_0x009e
            goto L_0x00ae
        L_0x009e:
            org.jetbrains.anko.AnkoException r9 = new org.jetbrains.anko.AnkoException
            java.lang.String r0 = "Several constructors are annotated with ClassParserConstructor"
            r9.<init>(r0)
            java.lang.Throwable r9 = (java.lang.Throwable) r9
            throw r9
        L_0x00a8:
            java.lang.Object r9 = r1.get(r2)
            java.lang.reflect.Constructor r9 = (java.lang.reflect.Constructor) r9
        L_0x00ae:
            org.jetbrains.anko.db.ClassParserKt$classParser$1 r0 = new org.jetbrains.anko.db.ClassParserKt$classParser$1
            r0.<init>(r9)
            org.jetbrains.anko.db.RowParser r0 = (org.jetbrains.anko.db.RowParser) r0
            return r0
        L_0x00b6:
            org.jetbrains.anko.AnkoException r0 = new org.jetbrains.anko.AnkoException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Can't initialize object parser for "
            r1.append(r2)
            java.lang.String r9 = r9.getCanonicalName()
            r1.append(r9)
            java.lang.String r9 = ", no acceptable constructors found"
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            r0.<init>(r9)
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jetbrains.anko.db.ClassParserKt.classParser(java.lang.Class):org.jetbrains.anko.db.RowParser");
    }

    private static final boolean hasApplicableType(Class<?> cls) {
        if (!cls.isPrimitive() && !Intrinsics.areEqual((Object) cls, (Object) String.class) && !Intrinsics.areEqual((Object) cls, (Object) CharSequence.class) && !Intrinsics.areEqual((Object) cls, (Object) Long.class) && !Intrinsics.areEqual((Object) cls, (Object) Integer.class) && !Intrinsics.areEqual((Object) cls, (Object) Byte.class) && !Intrinsics.areEqual((Object) cls, (Object) Character.class) && !Intrinsics.areEqual((Object) cls, (Object) Boolean.class) && !Intrinsics.areEqual((Object) cls, (Object) Float.class) && !Intrinsics.areEqual((Object) cls, (Object) Double.class) && !Intrinsics.areEqual((Object) cls, (Object) Short.class)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static final Object castValue(Object obj, Class<?> cls) {
        if (obj == null && cls.isPrimitive()) {
            throw new AnkoException("null can't be converted to the value of primitive type " + cls.getCanonicalName());
        } else if (obj == null || Intrinsics.areEqual((Object) cls, (Object) Object.class)) {
            return obj;
        } else {
            if (cls.isPrimitive() && Intrinsics.areEqual((Object) JavaSqliteUtils.PRIMITIVES_TO_WRAPPERS.get(cls), (Object) obj.getClass())) {
                return obj;
            }
            if ((obj instanceof Double) && (Intrinsics.areEqual((Object) cls, (Object) Float.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Float.class))) {
                return Float.valueOf((float) ((Number) obj).doubleValue());
            }
            if ((obj instanceof Float) && (Intrinsics.areEqual((Object) cls, (Object) Double.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Double.class))) {
                return Double.valueOf((double) ((Number) obj).floatValue());
            }
            if ((obj instanceof Character) && CharSequence.class.isAssignableFrom(cls)) {
                return obj.toString();
            }
            if (obj instanceof Long) {
                if (Intrinsics.areEqual((Object) cls, (Object) Integer.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Integer.class)) {
                    return Integer.valueOf((int) ((Number) obj).longValue());
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Short.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Short.class)) {
                    return Short.valueOf((short) ((int) ((Number) obj).longValue()));
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Byte.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Byte.class)) {
                    return Byte.valueOf((byte) ((int) ((Number) obj).longValue()));
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Boolean.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Boolean.class)) {
                    return Boolean.valueOf(!Intrinsics.areEqual(obj, (Object) 0L));
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Character.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Character.class)) {
                    return Character.valueOf((char) ((int) ((Number) obj).longValue()));
                }
            }
            if (obj instanceof Integer) {
                if (Intrinsics.areEqual((Object) cls, (Object) Long.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Long.class)) {
                    return Long.valueOf((long) ((Number) obj).intValue());
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Short.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Short.class)) {
                    return Short.valueOf((short) ((Number) obj).intValue());
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Byte.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Byte.class)) {
                    return Byte.valueOf((byte) ((Number) obj).intValue());
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Boolean.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Boolean.class)) {
                    return Boolean.valueOf(!Intrinsics.areEqual(obj, (Object) 0));
                }
                if (Intrinsics.areEqual((Object) cls, (Object) Character.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Character.class)) {
                    return Character.valueOf((char) ((Number) obj).intValue());
                }
            }
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 1 && (Intrinsics.areEqual((Object) cls, (Object) Character.TYPE) || Intrinsics.areEqual((Object) cls, (Object) Character.class))) {
                    return Character.valueOf(str.charAt(0));
                }
            }
            throw new AnkoException("Value " + obj + " of type " + obj.getClass() + " can't be cast to " + cls.getCanonicalName());
        }
    }
}
