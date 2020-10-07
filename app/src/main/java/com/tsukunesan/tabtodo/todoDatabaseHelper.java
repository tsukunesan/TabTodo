package com.tsukunesan.tabtodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class todoDatabaseHelper extends SQLiteOpenHelper {
    static final private String DBNAME = "todolist.sqlite";
    static final private int VERSION = 1;

    // コンストラクター
    todoDatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todos (" + "tab INTEGER, todo1 TEXT, todo2 TEXT,todo3 TEXT)");
        db.execSQL("INSERT INTO todos(tab, todo1)" + " VALUES(1,'todo1')");
        db.execSQL("INSERT INTO todos(tab, todo2)" + " VALUES(2,'todo2')");
        db.execSQL("INSERT INTO todos(tab, todo3)" + " VALUES(3,'todo3')");

        db.execSQL("CREATE TABLE btnnames (" + "btn1 TEXT, btn2 TEXT, btn3 TEXT)");
        db.execSQL("INSERT INTO btnnames(btn1, btn2, btn3)" + " VALUES('1', '2', '3')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(db);
    }
}
