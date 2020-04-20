package org.jetbrains.anko.db;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.anko.AnkoException;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0003J\u001f\u0010\u0005\u001a\u00020\u00002\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u000f\"\u00020\u0003¢\u0006\u0002\u0010\u001aJ\u0006\u0010\b\u001a\u00020\u0000J\b\u0010\u001b\u001a\u00020\u001cH\u0001J*\u0010\u001d\u001a\u0002H\u001e\"\u0004\b\u0000\u0010\u001e2\u0017\u0010\u001f\u001a\u0013\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u0002H\u001e0 ¢\u0006\u0002\b!¢\u0006\u0002\u0010\"Jk\u0010#\u001a\u00020\u001c2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00032\u0010\u0010$\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0003\u0018\u00010\u000f2\u0006\u0010\n\u001a\u00020\u00032\b\u0010\u000b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0011\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u0003H$¢\u0006\u0002\u0010%J\u000e\u0010\n\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u0003J\u000e\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0003J?\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u00032*\u0010'\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010(0\u000f\"\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010(¢\u0006\u0002\u0010)J\u000e\u0010\r\u001a\u00020\u00002\u0006\u0010*\u001a\u00020+J\u0016\u0010\r\u001a\u00020\u00002\u0006\u0010,\u001a\u00020+2\u0006\u0010*\u001a\u00020+J\u0018\u0010\u0011\u001a\u00020\u00002\u0006\u0010&\u001a\u00020\u00032\b\b\u0002\u0010-\u001a\u00020.J'\u0010/\u001a\b\u0012\u0004\u0012\u0002H\u001e00\"\b\b\u0000\u0010\u001e*\u00020\u00012\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u001e02H\bJ'\u0010/\u001a\b\u0012\u0004\u0012\u0002H\u001e00\"\b\b\u0000\u0010\u001e*\u00020\u00012\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u001e03H\bJ(\u00104\u001a\u0004\u0018\u0001H\u001e\"\b\b\u0000\u0010\u001e*\u00020\u00012\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u001e02H\b¢\u0006\u0002\u00105J(\u00104\u001a\u0004\u0018\u0001H\u001e\"\b\b\u0000\u0010\u001e*\u00020\u00012\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u001e03H\b¢\u0006\u0002\u00106J&\u00107\u001a\u0002H\u001e\"\b\b\u0000\u0010\u001e*\u00020\u00012\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u001e02H\b¢\u0006\u0002\u00105J&\u00107\u001a\u0002H\u001e\"\b\b\u0000\u0010\u001e*\u00020\u00012\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u001e03H\b¢\u0006\u0002\u00106J\u0010\u00108\u001a\u00020\u00002\u0006\u00109\u001a\u00020\u0003H\u0007JA\u00108\u001a\u00020\u00002\u0006\u00109\u001a\u00020\u00032*\u0010'\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010(0\u000f\"\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010(H\u0007¢\u0006\u0002\u0010)J\u000e\u0010:\u001a\u00020\u00002\u0006\u00109\u001a\u00020\u0003J?\u0010:\u001a\u00020\u00002\u0006\u00109\u001a\u00020\u00032*\u0010'\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010(0\u000f\"\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010(¢\u0006\u0002\u0010)J'\u0010;\u001a\u00020\u00002\u0006\u00109\u001a\u00020\u00032\u0012\u0010'\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u000f\"\u00020\u0003¢\u0006\u0002\u0010<J)\u0010=\u001a\u00020\u00002\u0006\u00109\u001a\u00020\u00032\u0012\u0010'\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u000f\"\u00020\u0003H\u0007¢\u0006\u0002\u0010<R\u001e\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u0006j\b\u0012\u0004\u0012\u00020\u0003`\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u0006j\b\u0012\u0004\u0012\u00020\u0003`\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0003\u0018\u00010\u000fX\u000e¢\u0006\u0004\n\u0002\u0010\u0010R\u001e\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u0006j\b\u0012\u0004\u0012\u00020\u0003`\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000¨\u0006>"}, d2 = {"Lorg/jetbrains/anko/db/SelectQueryBuilder;", "", "tableName", "", "(Ljava/lang/String;)V", "columns", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "distinct", "", "groupBy", "having", "havingApplied", "limit", "nativeSelectionArgs", "", "[Ljava/lang/String;", "orderBy", "selection", "selectionApplied", "getTableName", "()Ljava/lang/String;", "useNativeSelection", "column", "name", "names", "([Ljava/lang/String;)Lorg/jetbrains/anko/db/SelectQueryBuilder;", "doExec", "Landroid/database/Cursor;", "exec", "T", "f", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "execQuery", "selectionArgs", "(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "value", "args", "Lkotlin/Pair;", "(Ljava/lang/String;[Lkotlin/Pair;)Lorg/jetbrains/anko/db/SelectQueryBuilder;", "count", "", "offset", "direction", "Lorg/jetbrains/anko/db/SqlOrderDirection;", "parseList", "", "parser", "Lorg/jetbrains/anko/db/MapRowParser;", "Lorg/jetbrains/anko/db/RowParser;", "parseOpt", "(Lorg/jetbrains/anko/db/MapRowParser;)Ljava/lang/Object;", "(Lorg/jetbrains/anko/db/RowParser;)Ljava/lang/Object;", "parseSingle", "where", "select", "whereArgs", "whereSimple", "(Ljava/lang/String;[Ljava/lang/String;)Lorg/jetbrains/anko/db/SelectQueryBuilder;", "whereSupport", "sqlite_release"}, k = 1, mv = {1, 1, 5})
/* compiled from: SelectQueryBuilder.kt */
public abstract class SelectQueryBuilder {
    private final ArrayList<String> columns = new ArrayList<>();
    private boolean distinct;
    private final ArrayList<String> groupBy = new ArrayList<>();
    private String having;
    private boolean havingApplied;
    private String limit;
    private String[] nativeSelectionArgs;
    private final ArrayList<String> orderBy = new ArrayList<>();
    private String selection;
    private boolean selectionApplied;
    private final String tableName;
    private boolean useNativeSelection;

    /* access modifiers changed from: protected */
    public abstract Cursor execQuery(boolean z, String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6);

    public SelectQueryBuilder(String str) {
        Intrinsics.checkParameterIsNotNull(str, "tableName");
        this.tableName = str;
    }

    public final String getTableName() {
        return this.tableName;
    }

    public final <T> T exec(Function1<? super Cursor, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "f");
        Cursor doExec = doExec();
        try {
            Cursor cursor = doExec;
            return function1.invoke(doExec);
        } finally {
            try {
                doExec.close();
            } catch (Exception unused) {
            }
        }
    }

    public final <T> T parseSingle(RowParser<? extends T> rowParser) {
        Intrinsics.checkParameterIsNotNull(rowParser, "parser");
        Cursor doExec = doExec();
        try {
            return SqlParsersKt.parseSingle(doExec, rowParser);
        } finally {
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused) {
            }
            InlineMarker.finallyEnd(1);
        }
    }

    public final <T> T parseOpt(RowParser<? extends T> rowParser) {
        Intrinsics.checkParameterIsNotNull(rowParser, "parser");
        Cursor doExec = doExec();
        try {
            return SqlParsersKt.parseOpt(doExec, rowParser);
        } finally {
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused) {
            }
            InlineMarker.finallyEnd(1);
        }
    }

    /* JADX INFO: finally extract failed */
    public final <T> List<T> parseList(RowParser<? extends T> rowParser) {
        Intrinsics.checkParameterIsNotNull(rowParser, "parser");
        Cursor doExec = doExec();
        try {
            List<T> parseList = SqlParsersKt.parseList(doExec, rowParser);
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused) {
            }
            InlineMarker.finallyEnd(1);
            return parseList;
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused2) {
            }
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    public final <T> T parseSingle(MapRowParser<? extends T> mapRowParser) {
        Intrinsics.checkParameterIsNotNull(mapRowParser, "parser");
        Cursor doExec = doExec();
        try {
            return SqlParsersKt.parseSingle(doExec, mapRowParser);
        } finally {
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused) {
            }
            InlineMarker.finallyEnd(1);
        }
    }

    public final <T> T parseOpt(MapRowParser<? extends T> mapRowParser) {
        Intrinsics.checkParameterIsNotNull(mapRowParser, "parser");
        Cursor doExec = doExec();
        try {
            return SqlParsersKt.parseOpt(doExec, mapRowParser);
        } finally {
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused) {
            }
            InlineMarker.finallyEnd(1);
        }
    }

    /* JADX INFO: finally extract failed */
    public final <T> List<T> parseList(MapRowParser<? extends T> mapRowParser) {
        Intrinsics.checkParameterIsNotNull(mapRowParser, "parser");
        Cursor doExec = doExec();
        try {
            List<T> parseList = SqlParsersKt.parseList(doExec, mapRowParser);
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused) {
            }
            InlineMarker.finallyEnd(1);
            return parseList;
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            try {
                doExec.close();
            } catch (Exception unused2) {
            }
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    public final Cursor doExec() {
        String str = this.selectionApplied ? this.selection : null;
        String[] strArr = (!this.selectionApplied || !this.useNativeSelection) ? null : this.nativeSelectionArgs;
        boolean z = this.distinct;
        String str2 = this.tableName;
        Collection collection = this.columns;
        if (collection != null) {
            Collection collection2 = collection;
            Object[] array = collection2.toArray(new String[collection2.size()]);
            if (array != null) {
                return execQuery(z, str2, (String[]) array, str, strArr, CollectionsKt.joinToString$default(this.groupBy, ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null), this.having, CollectionsKt.joinToString$default(this.orderBy, ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null), this.limit);
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
    }

    public final SelectQueryBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public final SelectQueryBuilder column(String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        this.columns.add(str);
        return this;
    }

    public final SelectQueryBuilder groupBy(String str) {
        Intrinsics.checkParameterIsNotNull(str, "value");
        this.groupBy.add(str);
        return this;
    }

    public static /* bridge */ /* synthetic */ SelectQueryBuilder orderBy$default(SelectQueryBuilder selectQueryBuilder, String str, SqlOrderDirection sqlOrderDirection, int i, Object obj) {
        if (obj == null) {
            if ((i & 2) != 0) {
                sqlOrderDirection = SqlOrderDirection.ASC;
            }
            return selectQueryBuilder.orderBy(str, sqlOrderDirection);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: orderBy");
    }

    public final SelectQueryBuilder orderBy(String str, SqlOrderDirection sqlOrderDirection) {
        Intrinsics.checkParameterIsNotNull(str, "value");
        Intrinsics.checkParameterIsNotNull(sqlOrderDirection, "direction");
        if (Intrinsics.areEqual((Object) sqlOrderDirection, (Object) SqlOrderDirection.DESC)) {
            ArrayList<String> arrayList = this.orderBy;
            arrayList.add(str + " DESC");
        } else {
            this.orderBy.add(str);
        }
        return this;
    }

    public final SelectQueryBuilder limit(int i) {
        this.limit = String.valueOf(i);
        return this;
    }

    public final SelectQueryBuilder limit(int i, int i2) {
        this.limit = i + ", " + i2;
        return this;
    }

    public final SelectQueryBuilder columns(String... strArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "names");
        CollectionsKt.addAll(this.columns, (T[]) (Object[]) strArr);
        return this;
    }

    public final SelectQueryBuilder having(String str) {
        Intrinsics.checkParameterIsNotNull(str, "having");
        if (!this.havingApplied) {
            this.havingApplied = true;
            this.having = str;
            return this;
        }
        throw new AnkoException("Query having was already applied.");
    }

    public final SelectQueryBuilder having(String str, Pair<String, ? extends Object>... pairArr) {
        Intrinsics.checkParameterIsNotNull(str, "having");
        Intrinsics.checkParameterIsNotNull(pairArr, "args");
        if (!this.selectionApplied) {
            this.havingApplied = true;
            this.having = DatabaseKt.applyArguments(str, (Pair<String, ? extends Object>[]) (Pair[]) Arrays.copyOf(pairArr, pairArr.length));
            return this;
        }
        throw new AnkoException("Query having was already applied.");
    }

    @Deprecated(message = "Use whereArgs(select, args) instead.", replaceWith = @ReplaceWith(expression = "whereArgs(select, args)", imports = {}))
    public final SelectQueryBuilder where(String str, Pair<String, ? extends Object>... pairArr) {
        Intrinsics.checkParameterIsNotNull(str, "select");
        Intrinsics.checkParameterIsNotNull(pairArr, "args");
        return whereArgs(str, (Pair[]) Arrays.copyOf(pairArr, pairArr.length));
    }

    public final SelectQueryBuilder whereArgs(String str, Pair<String, ? extends Object>... pairArr) {
        Intrinsics.checkParameterIsNotNull(str, "select");
        Intrinsics.checkParameterIsNotNull(pairArr, "args");
        if (!this.selectionApplied) {
            this.selectionApplied = true;
            this.useNativeSelection = false;
            this.selection = DatabaseKt.applyArguments(str, (Pair<String, ? extends Object>[]) (Pair[]) Arrays.copyOf(pairArr, pairArr.length));
            return this;
        }
        throw new AnkoException("Query selection was already applied.");
    }

    @Deprecated(message = "Use whereArgs(select) instead.", replaceWith = @ReplaceWith(expression = "whereArgs(select)", imports = {}))
    public final SelectQueryBuilder where(String str) {
        Intrinsics.checkParameterIsNotNull(str, "select");
        return whereArgs(str);
    }

    public final SelectQueryBuilder whereArgs(String str) {
        Intrinsics.checkParameterIsNotNull(str, "select");
        if (!this.selectionApplied) {
            this.selectionApplied = true;
            this.useNativeSelection = false;
            this.selection = str;
            return this;
        }
        throw new AnkoException("Query selection was already applied.");
    }

    public final SelectQueryBuilder whereSimple(String str, String... strArr) {
        Intrinsics.checkParameterIsNotNull(str, "select");
        Intrinsics.checkParameterIsNotNull(strArr, "args");
        if (!this.selectionApplied) {
            this.selectionApplied = true;
            this.useNativeSelection = true;
            this.selection = str;
            this.nativeSelectionArgs = strArr;
            return this;
        }
        throw new AnkoException("Query selection was already applied.");
    }

    @Deprecated(message = "Use whereSimple() instead", replaceWith = @ReplaceWith(expression = "whereSimple(select, *args)", imports = {}))
    public final SelectQueryBuilder whereSupport(String str, String... strArr) {
        Intrinsics.checkParameterIsNotNull(str, "select");
        Intrinsics.checkParameterIsNotNull(strArr, "args");
        return whereSimple(str, (String[]) Arrays.copyOf(strArr, strArr.length));
    }
}
