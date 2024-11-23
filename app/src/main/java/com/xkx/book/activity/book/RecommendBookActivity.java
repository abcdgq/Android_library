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
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> bookNames = new ArrayList<>();
        for (Borrow borrow : borrows) {
            Book book = mHelper.queryById(borrow.getBorrowBookId()).get(0);
            sb.append(book.toRecommendString());
            String tag = book.getBookTags();
            tags.add(tag);
            bookNames.add(book.getBookName());
            sb.append("\n");
        }
//
        String temp = "The book I would like is";
        sb.append("\n" + temp);
        String s = sb.toString();//"大模型推荐返回语";

        System.out.println(s);
        func1(s).thenAccept(result -> {
            // 处理返回的结果
            int index = result.indexOf(temp) + temp.length();
            System.out.println("Response: " + result.substring(index));

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("根据你的喜好和借阅历史，你可能会喜欢的书籍包括以下书名：\n");

            ArrayList<String> recommendBookName = new ArrayList<>();
            if (tags.isEmpty()){
                textView1.setText("您还未借阅任何书");
            } else {
                for (Book u : books) { // 对于书库的每本书
                    int sign = 0;
                    int sign1 = 0;
                    for(String borrowBookName : bookNames){
                        if (u.getBookName().equals(borrowBookName)){
                            sign = 1;
                            break;
                        }
                    }
                    if (sign == 0){ // 说明这本书我没借过
                        for (String tag : tags){
                            if (u.getBookTags().contains(tag) || tag.contains(u.getBookTags())){
//                                recommendBookName.add(u.getBookName());
                                stringBuilder.append(u.getBookName() + "\n");
                                break;
                            }
                        }

                    }
                }
                stringBuilder.append("这些书籍的主题和内容都可能与你的阅读偏好和兴趣点吻合，可以试着借阅。");
                textView1.setText(stringBuilder.toString());
            }
//            textView1.setText(result.substring(index) + "\n\n");
        }).exceptionally(ex -> {
            // 处理异常
            System.err.println("Error: " + ex.getMessage());
            textView1.setText("Error: " + ex.getMessage());
            return null;
        });
    }


}