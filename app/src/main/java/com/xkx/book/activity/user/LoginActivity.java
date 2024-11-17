package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xkx.book.R;
import com.xkx.book.activity.MainActivity;
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgview_close;
    private EditText et_userid;
    private EditText et_pwd;
    private Button btn_login, btn_register;

    private UserDBHelper mHelper;
    private List<User> list;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgview_close = findViewById(R.id.close);
        et_userid = findViewById(R.id.et_login_userid);
        et_pwd = findViewById(R.id.et_login_passwd);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        imgview_close.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取数据库帮助器的实例
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
    public void onClick(View v) {

        String uid = et_userid.getText().toString();
        String pwd = et_pwd.getText().toString();
        int User_status = 0;
        Intent intent = new Intent();

        if (v.getId() == R.id.btn_login) {
            System.out.println("mark1");
            list = mHelper.queryById(uid);
            System.out.println("mark2");
            if(list.isEmpty()){
                ToastUtil.show(this, "账号不存在");
            } else {
                User u = list.get(0);// 同名账号最多一个，因为我想让这个作为学号的
                if (u.getUserid().equals(uid) && u.getPassword() == Long.parseLong(pwd)) {
                    ToastUtil.show(this, "登录成功");
                    // intent.putExtra("uid", uid);

                    User_status = u.getUser_status();

                    // 用 SharedPreferences 将 uid 存起来，方便后面的页面使用
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("uid", uid);
                    edit.putInt("User_status", User_status);
                    edit.apply();

                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, "密码错误");
                }
            }
        } else if (v.getId() == R.id.btn_register) {
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

    }
}