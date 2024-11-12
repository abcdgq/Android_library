package com.xkx.book.activity.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xkx.book.R;
import com.xkx.book.adapter.BookAdapter;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.database.BorrowDBHelper;
import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;
import com.xkx.book.util.ToastUtil;

import java.util.List;

public class FindBookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lv_find_book;
    private Button btn_search;
    private String userId;
    private String bookId;
    private String bookName;
    private int bookNumber;
    private BookAdapter bookAdapter;
    private BookDBHelper mHelper;
    private BorrowDBHelper nHelper;
    List<Book> books = null;
    //当前用户的id
    private String uid;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_book);

//        intent = getIntent();
//        uid = intent.getStringExtra("uid");
        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");


        lv_find_book = findViewById(R.id.lv_find_book);

        lv_find_book.setOnItemClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        //获取数据库帮助器的实例
        mHelper = BookDBHelper.getInstance(this);
        nHelper = BorrowDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();
        nHelper.openWriteLink();
        nHelper.openReadLink();

        books = mHelper.queryAll();
        for (Book u : books) {
            Log.e("ning", u.toString());
        }
        bookAdapter = new BookAdapter(books, FindBookActivity.this);
        lv_find_book.setAdapter(bookAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭数据库连接
//        mHelper.closeLink();
//        nHelper.closeLink();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Book book = (Book) adapterView.getItemAtPosition(position);
        bookId = book.getBookId();
        bookName = book.getBookName();
        bookNumber = book.getBookNumber();
        Log.e("ning", bookId);

        if (bookNumber <= 0) {
            ToastUtil.show(this, "该图书余量:0，不可借阅");
        } else {
            boolean flag = false;
            // 查看某学号用户的全部借阅信息
            List<Borrow> borrows = nHelper.queryById(uid);
            for (Borrow borrow : borrows) {
                if (borrow.getBorrowBookId().equals(bookId))
                    flag = true;
            }
            if (flag) {
                ToastUtil.show(this, "你已借阅，不可重复借阅");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindBookActivity.this);
                builder.setTitle("确认借阅？");
                builder.setPositiveButton("确认", (dialog, whichButton) -> {
                    //得到bookid 的书籍 租借信息

                    Book borrowbook = new Book(bookId, bookName, bookNumber - 1);
                    Borrow borrow = new Borrow(uid, bookId, bookName);
                    if (mHelper.update(borrowbook) > 0 && nHelper.insert(borrow) > 0)
                        ToastUtil.show(this, "借书成功");
                    onStart();
                });
                builder.setNegativeButton("取消", (dialog, whichButton) -> dialog.dismiss());
                builder.show();
            }
        }


    }


}


