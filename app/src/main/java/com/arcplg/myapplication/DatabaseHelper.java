package com.arcplg.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
        super(context, "MemoDB.sqlite", null, 1);
    }

    // データベースが作成されたときに呼ばれるメソッド
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("CREATE TABLE memo(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, detail TEXT, created_at TEXT)");

            sqLiteDatabase.execSQL("INSERT INTO memo VALUES(1, 'こんにちは', 'Hello, World!', '1778813470')");
        }
    }

    // データベースのバージョンが更新されたときに呼ばれるメソッド
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
