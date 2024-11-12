package com.xkx.book.activity.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xkx.book.R;
import com.xkx.book.adapter.BookAdapter;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.enity.Book;
import com.xkx.book.util.ToastUtil;

import java.util.List;

public class ViewBookActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_view_book;
    private BookAdapter bookAdapter;
    private BookDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        lv_view_book = findViewById(R.id.lv_find_book);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取数据库帮助器的实例
        mHelper = BookDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();

        List<Book> books = mHelper.queryAll();
        for (Book u : books) {
            Log.e("ning", u.toString());
        }
        bookAdapter = new BookAdapter(books, ViewBookActivity.this);
        lv_view_book.setAdapter(bookAdapter);

        lv_view_book.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Book book = (Book) adapterView.getItemAtPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewBookActivity.this);
        builder.setTitle("选择操作？");
        // 修改
        builder.setPositiveButton("修改", (dialog, whichButton) -> {
            Log.e("ning", book.toString());
            Intent intent = new Intent();
            intent.putExtra("view_book_id", book.getBookId());
            intent.putExtra("view_book_name", book.getBookName());
            intent.putExtra("view_book_num", book.getBookNumber());
            intent.setClass(ViewBookActivity.this, UpdateBookActivity.class);
            startActivity(intent);
        });
        // 删除
        builder.setNegativeButton("删除", (dialog, whichButton) -> {
            // 删除用户
            if (mHelper.deleteById(book.bookId) > 0)
                ToastUtil.show(this, "删除成功");
            // 刷新页面信息
            onStart();
        });
        builder.show();
    }
}