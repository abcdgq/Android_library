package com.xkx.book.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xkx.book.enity.Book;
import com.xkx.book.enity.User;

import java.util.ArrayList;
import java.util.List;

public class BookDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book.db";
    private static final String TABLE_NAME = "book_info";
    private static final int DB_VERSION = 1;
    private static BookDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;


    private BookDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static BookDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new BookDBHelper(context);
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建图书信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "book_id varchar(30)PRIMARY KEY," +
                "book_name varchar(30)," +
                "book_number int," +
                "book_tags varchar(50)," +
                "book_introduction varchar(200)," +
                "book_Location varchar(50)" +
                ")";
        db.execSQL(sql);
        db.execSQL("insert into " + TABLE_NAME + "(book_id,book_name,book_number,book_tags,book_introduction,book_Location)Values('14923275','中国近现代史纲要',10,'文学与艺术&社会科学','无','2层-206室-123书架-1231')");
        db.execSQL("insert into " + TABLE_NAME + "(book_id,book_name,book_number,book_tags,book_introduction,book_Location)Values('14923276','Android移动应用开发',0,'科学与技术','介绍Andorid课程的指导书','1层-105室-123书架-1201')");
        db.execSQL("insert into " + TABLE_NAME + "(book_id,book_name,book_number,book_tags,book_introduction,book_Location)Values('14312712','电子技术基础',10,'科学与技术','无','3层-306室-100书架-1031')");
        db.execSQL("insert into " + TABLE_NAME + "(book_id,book_name,book_number,book_tags,book_introduction,book_Location)Values('18964989','C语言项目开发实战入门',5,'科学与技术','无','2层-206室-010书架-0031')");
    }

    //打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    //打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //添加用户
    public long insert(Book book) {
        ContentValues values = new ContentValues();
        values.put("book_id", book.bookId);
        values.put("book_name", book.bookName);
        values.put("book_number", book.bookNumber);
        values.put("book_tags", book.bookTags);
        values.put("book_introduction", book.bookIntroduction);
        values.put("book_location", book.bookLocation);

        //执行插入记录动作，该语句返回插入记录的行号
        //如果第三个参数values 为null或者元素个数为0，由于insert()方法必须添加一条除了主键之外其它字段
        //
        //
        //如果第三个参数values 不为null并且元素的个数大于0，可以把第二个参数设置为null
        return mWDB.insert(TABLE_NAME, null, values);
    }

    //  删除书籍
    public long deleteById(String bookid) {
        //删除所有
//        return mWDB.delete(TABLE_NAME, "1=1", null);
        //按id删除
        return mWDB.delete(TABLE_NAME, "book_id=?", new String[]{bookid});
    }

    // 修改用户
    public long update(Book book) {
        ContentValues values = new ContentValues();
        values.put("book_id", book.bookId);
        values.put("book_name", book.bookName);
        values.put("book_number", book.bookNumber);
        values.put("book_tags", book.bookTags);
        values.put("book_introduction", book.bookIntroduction);
        values.put("book_location", book.bookLocation);
        return mWDB.update(TABLE_NAME, values, "book_id=?", new String[]{book.bookId});
    }

    //查询所有
    public List<Book> queryAll() {
        List<Book> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.bookId = cursor.getString(0);
            book.bookName = cursor.getString(1);
            book.bookNumber = cursor.getInt(2);
            book.bookTags = cursor.getString(3);
            book.bookIntroduction = cursor.getString(4);
            book.bookLocation = cursor.getString(5);

            list.add(book);
        }

        return list;
    }

    //按id 查询
    public List<Book> queryById(String bookid) {
        List<Book> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, "book_id=?", new String[]{bookid}, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.bookId = cursor.getString(0);
            book.bookName = cursor.getString(1);
            book.bookNumber = cursor.getInt(2);
            book.bookTags = cursor.getString(3);
            book.bookIntroduction = cursor.getString(4);
            book.bookLocation = cursor.getString(5);
            list.add(book);
        }
        return list;
    }

    //按书名 查询
    public List<Book> queryByName(String bookname) {
        List<Book> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, "book_name=?", new String[]{bookname}, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.bookId = cursor.getString(0);
            book.bookName = cursor.getString(1);
            book.bookNumber = cursor.getInt(2);
            book.bookTags = cursor.getString(3);
            book.bookIntroduction = cursor.getString(4);
            book.bookLocation = cursor.getString(5);
            list.add(book);
        }
        return list;
    }
    
}
