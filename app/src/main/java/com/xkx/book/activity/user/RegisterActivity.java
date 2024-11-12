package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xkx.book.R;
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView register_back;
    private EditText etd_username;
    private EditText etd_userid;
    private EditText etd_userpsw;
    private UserDBHelper mHelper;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etd_username = findViewById(R.id.reg_username);
        etd_userid = findViewById(R.id.reg_userid);
        etd_userpsw = findViewById(R.id.reg_userpasswpord);

        register_back = findViewById(R.id.register_return);
        btn_register = findViewById(R.id.btn_register);

        register_back.setOnClickListener(this);
        etd_username.setOnClickListener(this);
        etd_userid.setOnClickListener(this);
        etd_userpsw.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //获取数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this);
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

    @Override
    public void onClick(View view) {

        String userid = etd_userid.getText().toString();
        String username = etd_username.getText().toString();
        String password = etd_userpsw.getText().toString();
        int is_book = 0;
        int user_status = 0;
        int is_deleted = 0;

        Intent intent = new Intent();
        User user = null;

        if (view.getId() == R.id.register_return) {
            intent.setClass(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_register) {
            if (!username.isEmpty() && !userid.isEmpty() && !password.isEmpty()) {
                // 声明一个用户信息对象，并填写它的字段值
                user = new User(userid,
                        username,
                        Long.parseLong(password),
                        is_book,
                        user_status,
                        is_deleted);

                if (mHelper.insert(user) > 0) {
                    ToastUtil.show(this, "注册成功！");
                }
            } else {
                ToastUtil.show(this, "不允许留空");
            }
        }

    }
}
