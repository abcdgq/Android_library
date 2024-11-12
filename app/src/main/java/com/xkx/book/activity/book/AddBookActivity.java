package com.xkx.book.activity.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xkx.book.database.BookDBHelper;
import com.xkx.book.enity.Book;
import com.xkx.book.util.ToastUtil;
import com.xkx.book.R;//package.name.R;

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_book_id, et_book_name, et_book_number;
    private Button btn_save_info, btn_cancel_add;
    private BookDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        et_book_id = findViewById(R.id.et_book_id);
        et_book_name = findViewById(R.id.et_book_name);
        et_book_number = findViewById(R.id.et_book_number);

        btn_save_info = findViewById(R.id.btn_save_book);
        btn_cancel_add = findViewById(R.id.btn_cancel_book);

        btn_save_info.setOnClickListener(this);
        btn_cancel_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_book) {
            String tempBookId = et_book_id.getText().toString();
            String tempBookName = et_book_name.getText().toString();
            String tempBookNumber = et_book_number.getText().toString();

            if (!tempBookId.isEmpty() && !tempBookName.isEmpty() && !tempBookNumber.isEmpty()) {
                // 声明一个用户信息对象，并填写它的字段值
                Book book = new Book(tempBookId, tempBookName, Integer.parseInt(tempBookNumber));

                if (mHelper.insert(book) > 0) {
                    ToastUtil.show(this, "添加成功！");
                    et_book_id.setText("");
                    et_book_name.setText("");
                    et_book_number.setText("");
                }
            } else {
                ToastUtil.show(this, "不允许留空");
            }
        } else if (view.getId() == R.id.btn_cancel_book) {
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