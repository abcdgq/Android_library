package com.xkx.book.activity.book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xkx.book.R;
import com.xkx.book.adapter.BookAdapter;
import com.xkx.book.adapter.BorrowAdapter;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.database.BorrowDBHelper;
import com.xkx.book.database.BorrowDBHistory;
import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;

import java.util.ArrayList;
import java.util.List;

public class RecommendBookActivity extends AppCompatActivity {
    private String userId, bookId, bookName;
    private BorrowDBHistory mHistory;
    private BookDBHelper mHelper;
    private BorrowAdapter borrowAdapter;
    private SharedPreferences sharedPreferences;

    private int bookNumber;

    private String bookTags;
    private String bookIntroduction;
    private String bookLocation;

    private BorrowDBHistory nHistory;
    List<Book> books = null;
    //当前用户的id
    private String uid;

    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_book);
        textView1 = findViewById(R.id.tv_recommend_id);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //获取数据库帮助器的实例
        mHelper = BookDBHelper.getInstance(this);
        nHistory = BorrowDBHistory.getInstance(this);

        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();

        nHistory.openWriteLink();
        nHistory.openReadLink();
//
        books = mHelper.queryAll();
//
        StringBuilder sb = new StringBuilder();
        sb.append("图书馆书库里有的书籍：");
        for (Book u : books) {
            sb.append(u.toRecommendString());
            sb.append("\n");
        }
//
        sb.append("\n我的图书借阅历史为：");
//
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        List<Borrow> borrows = nHistory.queryById(uid);
        for (Borrow borrow : borrows) {
            Book book = mHelper.queryById(borrow.getBorrowBookId()).get(0);
            sb.append(book.toRecommendString());
            sb.append("\n");
        }
//
//
        sb.append("\n请给我推荐我可能喜欢的书籍");
        String s = sb.toString();//"大模型推荐返回语";
        // 这里要想办法获取大模型推荐的返回语句
        textView1.setText(s);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭数据库连接
//        mHelper.closeLink();
//        nHelper.closeLink();
    }


}