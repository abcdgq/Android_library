package com.xkx.book.activity.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xkx.book.R;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.enity.Book;
import com.xkx.book.util.ToastUtil;

public class UpdateBookActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_book_id, et_book_name, et_book_number;
    private Button btn_save, btn_cancel;
    private Book book;
    private BookDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        et_book_id = findViewById(R.id.et_book_id);
        et_book_name = findViewById(R.id.et_book_name);
        et_book_number = findViewById(R.id.et_book_number);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        String view_book_id = intent.getStringExtra("view_book_id");
        String view_book_name = intent.getStringExtra("view_book_name");
        int view_book_num = intent.getIntExtra("view_book_num", 0);

        et_book_id.setText(view_book_id);
        et_book_id.setEnabled(false);
        et_book_name.setText(view_book_name);
        et_book_number.setText(String.valueOf(view_book_num));

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            String bookId = et_book_id.getText().toString();
            String bookName = et_book_name.getText().toString();
            String str_bookNumber = et_book_number.getText().toString();

            if (!bookId.isEmpty() && !bookName.isEmpty() && !str_bookNumber.isEmpty()) {
                // 声明一个用户信息对象，并填写它的字段值
                Book book = new Book(bookId, bookName, Integer.parseInt(str_bookNumber));

                if (mHelper.update(book) > 0) {
                    ToastUtil.show(this, "修改成功！");
                }
            } else {
                ToastUtil.show(this, "不允许留空");
            }
        } else if (view.getId() == R.id.btn_cancel) {
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        //获取数据库帮助器的实例
        mHelper = BookDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭数据库连接
//        mHelper.closeLink();
    }
}