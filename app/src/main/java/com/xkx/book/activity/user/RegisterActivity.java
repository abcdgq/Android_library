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

import java.util.List;

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
//            intent.setClass(RegisterActivity.this, LoginActivity.class);
//            startActivity(intent);
            finish();// 直接退出这个，返回上一个页面就行，不然，这个页面还留在堆栈，没必要
        } else if (view.getId() == R.id.btn_register) {
            if (!username.isEmpty() && !userid.isEmpty() && !password.isEmpty()) {
                // 声明一个用户信息对象，并填写它的字段值
                List<User> users = mHelper.queryById(userid); //实际上，我准备让他最大为1，即uid不重复
                if (users.isEmpty()){
                    user = new User(userid,
                            username,
                            Long.parseLong(password),
                            is_book,
                            user_status,
                            is_deleted, "", "");

                    if (mHelper.insert(user) > 0) { // 很难插入不成功啊
                        ToastUtil.show(this, "注册成功！");
                        //Intent intent1 = new Intent(this, LoginActivity.class);
                        //startActivity(intent1);//注册成功的话，直接返回登录界面
//                    ToastUtil.show(this, "注册成功！");
                        finish();//直接退出这个界面，因为这个界面是上个界面调用的，所以退出后，还是到上个界面
                    } else {
                        ToastUtil.show(this, "数据不合法或空间不足");
                    }
                } else {
                    ToastUtil.show(this, "该账户已经存在");
                }
            } else {
                ToastUtil.show(this, "不允许留空");
            }
        }

    }
}
