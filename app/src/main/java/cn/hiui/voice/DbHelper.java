package cn.hiui.voice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J \u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0016¨\u0006\r"}, d2 = {"Lcn/hiui/voice/DbHelper;", "Landroid/database/sqlite/SQLiteOpenHelper;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "onCreate", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "onUpgrade", "oldVersion", "", "newVersion", "voice_release"}, k = 1, mv = {1, 1, 15})
/* compiled from: DbHelper.kt */
public final class DbHelper extends SQLiteOpenHelper {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DbHelper(Context context) {
        super(context, "config.db", (SQLiteDatabase.CursorFactory) null, 1);
        Intrinsics.checkParameterIsNotNull(context, "context");
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Intrinsics.checkParameterIsNotNull(sQLiteDatabase, "db");
        sQLiteDatabase.execSQL("create table config (_id integer primary key autoincrement, name text,file text)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(sQLiteDatabase, "db");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS config");
        sQLiteDatabase.execSQL("create table config (_id integer primary key autoincrement, name text,value text)");
    }
}
