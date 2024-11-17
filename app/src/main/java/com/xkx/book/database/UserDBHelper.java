package com.xkx.book.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xkx.book.enity.Book;
import com.xkx.book.enity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db";
    private static final String TABLE_NAME = "user_info";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;
//    private static final HashMap<String, Integer> tags = new HashMap<>();

    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //利用单例模式获取数据库帮助器的唯一实例
    public static UserDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    // 创建数据库，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " userid String not null," + // 学号，不能重复
                " username varchar not null," +
                " password LONG not null," +
                " is_book INTEGER not null," +
                " user_status INTEGER not null," +
                " is_deleted INTEGER not null," +
                " tag varchar not null," +
                " prefer varchar not null);";
        db.execSQL(sql);
        db.execSQL("insert into " + TABLE_NAME + "(userid,username,password,is_book,user_status,is_deleted,tag,prefer)Values('admin','管理员','1','0','1','0','无','无')");
        db.execSQL("insert into " + TABLE_NAME + "(userid,username,password,is_book,user_status,is_deleted,tag,prefer)Values('1001','用户1001','1','0','0','0','文学与艺术','无')");
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



    //  删除用户
    public long deleteById(String userid) {
        //删除所有
//        return mWDB.delete(TABLE_NAME, "1=1", null);
        //按名字删除
        return mWDB.delete(TABLE_NAME, "userid=?", new String[]{userid});
    }

    // 修改用户
    public long update(User user) {
        ContentValues values = new ContentValues();
        values.put("userid", user.userid);
        values.put("username", user.username);
        values.put("password", user.password);
        values.put("is_book", user.is_book);
        values.put("user_status", user.user_status);
        values.put("is_deleted", user.is_deleted);
        values.put("tag", user.tag);
        values.put("prefer", user.prefer);
        return mWDB.update(TABLE_NAME, values, "userid=?", new String[]{user.userid});
    }


    //查询所有
    public List<User> queryAll() {
        List<User> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, null, null, null, null, null);
        //循环游标，取出游标所指的每条记录
        while (cursor.moveToNext()) {
            User user = new User();
            user.id = cursor.getInt(0);
            user.userid = cursor.getString(1);
            user.username = cursor.getString(2);
            user.password = cursor.getLong(3);
            user.is_book = cursor.getInt(4);
            user.user_status = cursor.getInt(5);
            user.is_deleted = cursor.getInt(6);
            user.tag = cursor.getString(7);
            user.prefer = cursor.getString(8);
            list.add(user);
        }

        return list;
    }

    //按name 查询
//    public List<User> queryByName(String name) {
//        List<User> list = new ArrayList<>();
//        //执行记录查询动作，该语句返回结果集的游标
//        Cursor cursor = mRDB.query(TABLE_NAME, null, "name=?", new String[]{name}, null, null, null);
//        //循环游标，取出游标所指的每条记录
//        while (cursor.moveToNext()) {
//            User user = new User();
//            user.id = cursor.getInt(0);
//            user.userid = cursor.getString(1);
//            user.username = cursor.getString(2);
//            user.password = cursor.getLong(3);
//            user.is_book = cursor.getInt(4);
//            user.user_status = cursor.getInt(5);
//            user.is_deleted = cursor.getInt(6);
//            list.add(user);
//        }
//        return list;
//    }

    //添加用户
    public long insert(User user) {
        ContentValues values = new ContentValues();
        values.put("userid", user.userid);
        values.put("username", user.username);
        values.put("password", user.password);
        values.put("is_book", user.is_book);
        values.put("user_status", user.user_status);
        values.put("is_deleted", user.is_deleted);
        values.put("tag", user.tag);
        values.put("prefer", user.prefer);
        //执行插入记录动作，该语句返回插入记录的行号
        //如果第三个参数values 为null或者元素个数为0，由于insert()方法必须添加一条除了主键之外其它字段
        //
        //
        //如果第三个参数values 不为null并且元素的个数大于0，可以把第二个参数设置为null
        return mWDB.insert(TABLE_NAME, null, values);
    }

    //按Uid 查询,,这真是个烂方法，定义了一个自动生成的主键id，又有uid,这里查的是uid但是命名是id，而且，uid,账户，学号，被他设置成同一个东西的不同显示
    @SuppressLint("Range")
    public List<User> queryById(String uid) {
        List<User> list = new ArrayList<>();
        //执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_NAME, null, "userid=?", new String[]{uid}, null, null, null);
        //循环游标，取出游标所指的每条记录
        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.id = cursor.getInt(cursor.getColumnIndex("_id"));  // 获取主键
                user.userid = cursor.getString(cursor.getColumnIndex("userid"));
                user.username = cursor.getString(cursor.getColumnIndex("username"));
                user.password = cursor.getLong(cursor.getColumnIndex("password"));
                user.is_book = cursor.getInt(cursor.getColumnIndex("is_book"));
                user.user_status = cursor.getInt(cursor.getColumnIndex("user_status"));
                user.is_deleted = cursor.getInt(cursor.getColumnIndex("is_deleted"));
                user.tag = cursor.getString(cursor.getColumnIndex("tag"));
                user.prefer = cursor.getString(cursor.getColumnIndex("prefer"));
                list.add(user);
            } while (cursor.moveToNext());
        } else {
            // 游标为空的处理
            Log.e("UserDBHelper", "No data found for userid: " + uid);
        }
        return list;
    }

    @SuppressLint("Range")
    public String getTagById(String uid) {
        String tag = null;
        Cursor cursor = null;
        try {
            // 执行查询，获取与uid匹配的记录
            cursor = mRDB.query(TABLE_NAME, new String[]{"tag"}, "userid=?", new String[]{uid}, null, null, null);

            // 如果有记录，读取tag字段的值
            if (cursor != null && cursor.moveToFirst()) {
                tag = cursor.getString(cursor.getColumnIndex("tag")); // 获取tag字段
            }
        } catch (Exception e) {
            e.printStackTrace();  // 异常处理
        } finally {
            // 确保游标被关闭
            if (cursor != null) {
                cursor.close();
            }
        }
        return tag;  // 返回tag值，若无记录则返回null
    }

    //按Uid和psw 查询
//    public List<User> queryByUid(String uid, String psw) {
//        List<User> list = new ArrayList<>();
//        //执行记录查询动作，该语句返回结果集的游标
//        Cursor cursor = mRDB.query(TABLE_NAME, null, "userid=? and password=?", new String[]{uid, psw}, null, null, null);
//        //循环游标，取出游标所指的每条记录
//        while (cursor.moveToNext()) {
//            User user = new User();
//            user.id = cursor.getInt(0);
//            user.userid = cursor.getString(1);
//            user.username = cursor.getString(2);
//            user.password = cursor.getLong(3);
//            user.is_book = cursor.getInt(4);
//            user.user_status = cursor.getInt(5);
//            user.is_deleted = cursor.getInt(6);
//            list.add(user);
//        }
//        return list;
//    }
    
//    public void addTag(String tag) {
//        if (!this.tags.containsKey(tag)) {
//            tags.put(tag, 1);
//        }
//    }
//
//    public HashMap<String, Integer> getTags() {
//        return this.tags;
//    }
//
//    public void setTags(String tags) {
//        // 使用 split 方法按照 & 分隔字符串
//        String[] tagArray = tags.split("&");
//        // 打印分隔后的标签
//        for (String tag : tagArray) {
//            addTag(tag);
//        }
//    }
//
//    public String getTagToString() {
//        StringBuilder sb = new StringBuilder();
//        for (String tag : tags.keySet()) {
//            sb.append(tag).append("&");
//        }
//        if (sb.length() > 0) {
//            sb.deleteCharAt(sb.length() - 1);
//        }
//        if (sb.length() == 0) {
//            return null;
//        }
//        return sb.toString();
//    }

    //按Uid和psw 查询
//    public User queryByUid(String uid) {
//        //List<User> list = new ArrayList<>();
//        //执行记录查询动作，该语句返回结果集的游标
//        Cursor cursor = mRDB.query(TABLE_NAME, null, "userid=?", new String[]{uid}, null, null, null);
//        //循环游标，取出游标所指的每条记录
//        if(cursor.moveToNext()) { // 只会有一个人，uid不能重复,不然登录时候出问题
//            User user = new User();
//            user.id = cursor.getInt(0);
//            user.userid = cursor.getString(1);
//            user.username = cursor.getString(2);
//            user.password = cursor.getLong(3);
//            user.is_book = cursor.getInt(4);
//            user.user_status = cursor.getInt(5);
//            user.is_deleted = cursor.getInt(6);
//            user.tag = cursor.getString(7);
//            return user;
//        }
//        return null;
//    }

}

