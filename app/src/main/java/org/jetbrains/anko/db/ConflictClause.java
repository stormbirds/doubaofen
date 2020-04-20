package org.jetbrains.anko.db;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lorg/jetbrains/anko/db/ConflictClause;", "", "(Ljava/lang/String;I)V", "ROLLBACK", "ABORT", "FAIL", "IGNORE", "REPLACE", "sqlite_release"}, k = 1, mv = {1, 1, 5})
/* compiled from: sqlTypes.kt */
public enum ConflictClause {
    ROLLBACK,
    ABORT,
    FAIL,
    IGNORE,
    REPLACE
}
