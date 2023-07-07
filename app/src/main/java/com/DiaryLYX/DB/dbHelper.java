package com.DiaryLYX.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
//Android 提供的用于管理 SQLite 数据库的辅助类。
public class dbHelper extends SQLiteOpenHelper {
    //存储创建 "diary" 表的 SQL 语句。
    public static final String CREATE_DIARY = "create table diary ("+
            "id integer primary key autoincrement,title text,content text"+
            ",date Date,author text,image text)";
    private Context context;
    //构造函数 dbHelper 接收一个 Context 对象作为参数，并调用父类的构造函数初始化数据库。
    // 数据库名为 "diary.db"，版本号为 1。
    public dbHelper(@Nullable Context context) {
        super(context, "diary.db", null, 1);
        this.context=context;
    }
    //重写了 onCreate() 方法，在数据库首次创建时调用该方法，执行创建表的操作，
    // 即执行 db.execSQL(CREATE_DIARY)。
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
