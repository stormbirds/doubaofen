package org.jetbrains.anko.db;

import android.database.sqlite.SQLiteException;
import kotlin.Metadata;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001d\u0010\u0003\u001a\u00028\u00002\u000e\u0010\u0004\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0005H\u0016¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"org/jetbrains/anko/db/SQLiteParserHelpersKt__SqlParserHelpersKt$rowParser$10", "Lorg/jetbrains/anko/db/RowParser;", "(Lkotlin/jvm/functions/Function10;)V", "parseRow", "columns", "", "", "([Ljava/lang/Object;)Ljava/lang/Object;", "anko-sqlite_release"}, k = 1, mv = {1, 1, 5})
/* compiled from: SqlParserHelpers.kt */
public final class SQLiteParserHelpersKt__SqlParserHelpersKt$rowParser$10 implements RowParser<R> {
    final /* synthetic */ Function10 $parser;

    SQLiteParserHelpersKt__SqlParserHelpersKt$rowParser$10(Function10 function10) {
        this.$parser = function10;
    }

    public R parseRow(Object[] objArr) {
        Intrinsics.checkParameterIsNotNull(objArr, "columns");
        if (objArr.length == 10) {
            return this.$parser.invoke(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7], objArr[8], objArr[9]);
        }
        throw new SQLiteException("Invalid row: 10 columns required");
    }
}