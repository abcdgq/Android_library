package com.xkx.book.activity.book;

import static com.xkx.book.openai.openaiMain.func1;

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
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;
import com.xkx.book.enity.User;

import java.util.ArrayList;
import java.util.List;

public class RecommendBookActivity extends AppCompatActivity {

    private BookDBHelper mHelper;
    private SharedPreferences sharedPreferences;
    private BorrowDBHistory nHistory;
    private UserDBHelper userDBHelper;
    List<Book> books = null;
    //当前用户的id
    private String uid;

    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_book);
        textView1 = findViewById(R.id.tv_recommend_id);


        // 获取按钮的引用
        Button myButton = findViewById(R.id.my_button);

        // 设置点击事件监听器
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在按钮点击时执行的代码
                handleOnClick();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //获取数据库帮助器的实例
        mHelper = BookDBHelper.getInstance(this);
        nHistory = BorrowDBHistory.getInstance(this);
        userDBHelper = UserDBHelper.getInstance(this);

        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();

        nHistory.openWriteLink();
        nHistory.openReadLink();

        userDBHelper.openWriteLink();
        userDBHelper.openReadLink();
        textView1.setText("默认值");
//
        // 这里要想办法获取大模型推荐的返回语句
        //textView1.setText(s);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭数据库连接
//        mHelper.closeLink();
//        nHelper.closeLink();
    }


    public void handleOnClick(){
        books = mHelper.queryAll();

        StringBuilder sb = new StringBuilder();
        sb.append("图书馆书库里有的书籍：");
        for (Book u : books) {
            sb.append(u.toRecommendString());
            sb.append("\n");
        }

        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        User user = userDBHelper.queryById(uid).get(0);
        sb.append("\n我的喜好：");
        sb.append(user.getTag() + " " + user.getPrefer());
        sb.append("\n我的图书借阅历史为：");
//
        List<Borrow> borrows = nHistory.queryById(uid);
        if(borrows.isEmpty()){
            sb.append("空");
        }
        for (Borrow borrow : borrows) {
            Book book = mHelper.queryById(borrow.getBorrowBookId()).get(0);
            sb.append(book.toRecommendString());
            sb.append("\n");
        }
//
//
        String temp = "The book I would like is ";
        sb.append("\n" + temp);
        String s = sb.toString();//"大模型推荐返回语";
        func1(s).thenAccept(result -> {
            // 处理返回的结果
            int index = result.indexOf(temp) + temp.length();
            System.out.println("Response: " + result.substring(index).trim());
            textView1.setText(result.substring(index) + "\n\n");
        }).exceptionally(ex -> {
            // 处理异常
            System.err.println("Error: " + ex.getMessage());
            textView1.setText("Error: " + ex.getMessage());
            return null;
        });
    }


}