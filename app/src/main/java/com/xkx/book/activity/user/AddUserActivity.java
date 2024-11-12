package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xkx.book.R;
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_id, et_name, et_password;
    private Button btn_save_info, btn_cancel_add;
    User user = null;
    UserDBHelper mHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        et_id = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_pwd);

        btn_save_info = findViewById(R.id.btn_save_user);
        btn_cancel_add = findViewById(R.id.btn_cancel_user);

        btn_save_info.setOnClickListener(this);
        btn_cancel_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save_user) {
            String uid = et_id.getText().toString();
            String name = et_name.getText().toString();
            String password = et_password.getText().toString();
            int is_book = 0;
            int user_status = 0;
            int is_deleted = 0;

            if (!uid.isEmpty() && !name.isEmpty() && !password.isEmpty()) {
                // 声明一个用户信息对象，并填写它的字段值
                User user = new User(uid,
                        name,
                        Long.parseLong(password),
                        is_book,
                        user_status,
                        is_deleted);

                if (mHelper.insert(user) > 0) {
                    ToastUtil.show(this, "注册成功！");
                    et_id.setText("");
                    et_name.setText("");
                    et_password.setText("");
                }
            } else {
                ToastUtil.show(this, "不允许留空");
            }
        } else if (view.getId() == R.id.btn_cancel_user) {
            finish();
        }

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
}