package com.xkx.book.activity.borrow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xkx.book.R;
import com.xkx.book.adapter.BorrowAdapter;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.database.BorrowDBHelper;
import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

import java.util.List;

public class BorrowBookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lv_borrow_book;
    private String book_id, borrowid;
    private User user;
    private BorrowDBHelper mHelper;
    private BookDBHelper nHelper;
    private BorrowAdapter borrowAdapter;
    Intent intent;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

//        intent = getIntent();
//        uid = intent.getStringExtra("uid");
        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        lv_borrow_book = findViewById(R.id.lv_borrow_book);
        lv_borrow_book.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取数据库帮助器的实例
        mHelper = BorrowDBHelper.getInstance(this);
        nHelper = BookDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();
        nHelper.openWriteLink();
        nHelper.openReadLink();


        List<Borrow> borrows = mHelper.queryById(uid);
        for (Borrow borrow : borrows) {
            Log.e("ning", borrow.toString());
        }
        borrowAdapter = new BorrowAdapter(borrows, BorrowBookActivity.this);
        lv_borrow_book.setAdapter(borrowAdapter);

        lv_borrow_book.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Borrow borrow = (Borrow) adapterView.getItemAtPosition(position);
        book_id = borrow.getBorrowBookId();
        borrowid = borrow.getBorrowId();
        AlertDialog.Builder builder = new AlertDialog.Builder(BorrowBookActivity.this);
        builder.setTitle("确认还书？");
        builder.setPositiveButton("确认", (dialog, whichButton) -> {

            String bookid = null, bookname = null;
            int booknum = 0;

            //获取当前book里边的数量
            List<Book> book = nHelper.queryById(book_id);
            for (Book book1 : book) {
                bookid = book_id;
                bookname = book1.bookName;
                booknum = book1.bookNumber;
            }
            Book returnbook = new Book(bookid, bookname, booknum + 1);

            if (nHelper.update(returnbook) > 0 && mHelper.deleteByid(borrowid, bookid) > 0) {
                ToastUtil.show(this, "归还成功！");
            }

            onStart();
        });
        builder.setNegativeButton("取消", (dialog, whichButton) -> dialog.dismiss());
        builder.show();
    }
}