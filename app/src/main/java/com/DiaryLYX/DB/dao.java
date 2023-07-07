package com.DiaryLYX.DB;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.DiaryLYX.diary.diary;
public class dao extends Application {
    //提供数据库帮助和管理
    // 通过创建和使用 dbhelper 对象，dao 类可以调用其中定义的方法来执行数据库操作，
    // 如插入、更新和查询数据。
    private dbHelper dbhelper;
    private SQLiteDatabase db;
    // 初始化databaseHelper对象
    public dao(Context context)
    {
        dbhelper = new dbHelper(context);
    }

    /**
     * 插入
     * @param diary
     */
    public void insert(diary diary){
        String str="insert into diary (title,content,date,author,image) values ('"+diary.getTitle()+"','"+
                diary.getContent()+"','"+diary.getDate()+"','"+diary.getAuthor()+"','"+diary.getImage()+"')";
        db.execSQL(str);
    }

    /**
     * 更新，参数化查询的方式来处理 SQL 语句
     * @param d
     */
    public void update(diary d) {
        // 获取可写的数据库对象 db。
        db = dbhelper.getWritableDatabase();
        String sql = "update diary set title=?,content=?,image=? where date=? and author=?";
        Object bindArgs[] = new Object[] { d.getTitle(), d.getContent(),d.getImage(),d.getDate(),d.getAuthor() };
        db.execSQL(sql, bindArgs);
    }

    /**
     * 查找
     * @param
     * @return
     */
    @SuppressLint("Range")//用于抑制 "Range" 警告。
    public ArrayList<diary> find(String str) {
        //存储查询结果
        ArrayList<diary> diarylist = new ArrayList<>();
        db = dbhelper.getWritableDatabase();
        String sql = "select * from diary where author=?";
        String[] selectionArgs = new String[] { str };
        //执行查询操作，获取一个 Cursor 对象 cursor，用于遍历查询结果。
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        // 游标从头读到尾
        // 通过查询数据库中满足条件的日记数据，并将其封装成 diary 对象的集合返回。
        // 使用了游标来遍历查询结果，
        // 并通过 getColumnIndex() 方法根据列名获取相应字段的值。
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                diary diary = new diary();
                diary.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                diary.setContent(cursor.getString(cursor.getColumnIndex("content")));
                diary.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                diary.setDate(cursor.getString(cursor.getColumnIndex("date")));
                diary.setImage(cursor.getString(cursor.getColumnIndex("image")));
                diarylist.add(diary);
        }
        return diarylist;
    }

    /**
     * 删除
     * @param d
     */
    public void delete(diary d) {
        db = dbhelper.getWritableDatabase();
        String sql = "delete from diary where author=? and date=?";
        Object bindArgs[] = new Object[] { d.getAuthor(),d.getDate()};
        db.execSQL(sql, bindArgs);
    }
    /**
     * 退出，关闭数据库连接，以释放资源并确保数据库操作的完整性和安全性。
     */
    public void close(){
        db = dbhelper.getWritableDatabase();
        if(db != null){
            db.close();
        }
    }
}
