package com.xkx.book.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;

import java.util.ArrayList;
import java.util.List;

/**
 * 借阅的情况分析:
 * 管理员而言，可以删除用户的借阅情况，可以查看借阅情况。
 * 用户而言，可以借书和还书，这就需要增加，删除，查看
 * 所以，这里只设增加，删除，查看，不实现修改。
 */

public class BorrowDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "borrow.db";
    private static final String TABLE_NAME = "borrow_info";
    private static final int DB_VERSION = 1;
    private static BorrowDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private BorrowDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static BorrowDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new BorrowDBHelper(context);
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建借阅信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "borrow_id varchar(30)," +
                "borrow_book_id varchar(30)," +
                "borrow_book_name varchar(30)" +
                ")";
        db.execSQL(sql);
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
    public long insert(Borrow borrow) {
        ContentValues values = new ContentValues();
        values.put("borrow_id", borrow.borrowId);
        values.put("borrow_book_id", borrow.borrowBookId);
        values.put("borrow_book_name", borrow.borrowBookName);
        //执行插入记录动作，该语句返回插入记录的行号
        //如果第三个参数values 为null或者元素个数为0，由于insert()方法必须添加一条除了主键之外其它字段
        //
        //
        //如果第三个参数values 不为null并且元素的个数大于0，可以把第二个参数设置为null
        return mWDB.insert(TABLE_NAME, null, values);
    }

    //  删除书籍
    public long deleteById(String borrowid) {
        //删除所有
//        return mWDB.delete(TABLE_NAME, "1=1", null);
        //按id删除
        return mWDB.delete(TABLE_NAME, "borrow_id=?", new String[]{borrowid});
    }

    //  根据uid和bookid 删除书籍
    public long deleteByid(String borrowid, String borrowbookid) {
        //按id删除
        return mWDB.delete(TABLE_NAME, "borrow_id=? and borrow_book_id=?", new String[]{borrowid, borrowbookid});
    }

    //查询所有
    public List<Borrow> queryAll() {
        List<Borrow> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            Borrow borrow = new Borrow();
            borrow.borrowId = cursor.getString(0);
            borrow.borrowBookId = cursor.getString(1);
            borrow.borrowBookName = cursor.getString(2);
            list.add(borrow);
        }

        return list;
    }

    //按借阅人id 查询
    public List<Borrow> queryById(String borrowid) {
        List<Borrow> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, "borrow_id=?", new String[]{borrowid}, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            Borrow borrow = new Borrow();
            borrow.borrowId = cursor.getString(0);
            borrow.borrowBookId = cursor.getString(1);
            borrow.borrowBookName = cursor.getString(2);
            list.add(borrow);
        }
        return list;
    }

    //按图书id 查询
    public List<Borrow> queryByName(String borrowbookname) {
        List<Borrow> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, "borrow_book_id=?", new String[]{borrowbookname}, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            Borrow borrow = new Borrow();
            borrow.borrowId = cursor.getString(0);
            borrow.borrowBookId = cursor.getString(1);
            borrow.borrowBookName = cursor.getString(2);
            list.add(borrow);
        }
        return list;
    }
}
