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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.xkx.book.R;
import com.xkx.book.adapter.BookAdapter;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.database.BorrowDBHelper;
import com.xkx.book.database.BorrowDBHistory;
import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;
import com.xkx.book.util.ToastUtil;

import java.util.ArrayList;
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
    private BorrowDBHistory nHistory;
    List<Book> books = null;
    //当前用户的id
    private String uid;
    Intent intent;

    private EditText etInput;// 这两个是用来搜索框的
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_book);


        etInput = findViewById(R.id.et_input);
        btnConfirm = findViewById(R.id.btn_confirm);
        // 设置按钮点击事件
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(); // 调用单独的方法
            }
        });

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
        nHistory = BorrowDBHistory.getInstance(this);

        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();
        nHelper.openWriteLink();
        nHelper.openReadLink();

        nHistory.openWriteLink();
        nHistory.openReadLink();

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
                        nHistory.insert(borrow);
                        ToastUtil.show(this, "借书成功");
                    onStart();
                });
                builder.setNegativeButton("取消", (dialog, whichButton) -> dialog.dismiss());
                builder.show();
            }
        }


    }

    // 定义处理按钮点击的单独方法
    private void handleButtonClick() {
        // 获取输入框中的文本
        //String inputText = etInput.getText().toString().trim();

        String query = etInput.getText().toString().trim().toLowerCase(); // 获取用户输入
        List<Book> filteredBooks = new ArrayList<>();

        for (Book book : books) {
            // 检查每本书的名称是否包含查询内容
            if (book.getBookName().toLowerCase().contains(query)) {
                filteredBooks.add(book); // 如果匹配则添加到筛选列表
            }
        }

        if (filteredBooks.isEmpty()) {
            ToastUtil.show(this, "没找到相关书籍");
        } else {
            // 更新适配器数据
            //bookAdapter.updateBooks(filteredBooks); // 假设您的适配器有一个更新方法
            bookAdapter = new BookAdapter(filteredBooks, FindBookActivity.this);
            lv_find_book.setAdapter(bookAdapter); // 设置更新后的适配器
        }
    }


}


