package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xkx.book.R;
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

import java.util.List;

public class UserUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_user_id, et_new_pwd, et_confirm_pwd;
    private Button btn_save, btn_cancel;
    private User user_change = null;
    Intent intent;
    private String uid;
    private UserDBHelper mHelper;
    List<User> list = null;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);


        initView();
        initData();
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
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

    private void initData() {
//        intent = getIntent();
//        uid = intent.getStringExtra("uid");
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        et_user_id.setText(uid);
        et_user_id.setEnabled(false);
    }

    private void initView() {
        et_user_id = findViewById(R.id.et_user_id);
        et_new_pwd = findViewById(R.id.et_new_pwd);
        et_confirm_pwd = findViewById(R.id.et_confirm_pwd);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            String newPassword = et_new_pwd.getEditableText().toString().trim();
            String confirmPassword = et_confirm_pwd.getEditableText().toString().trim();

            if (newPassword.length() == 0 || confirmPassword.length() == 0) {
                ToastUtil.show(this, "密码信息未填写完整");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                ToastUtil.show(this, "两次密码不一致");
                return;
            } else {
                String password = et_new_pwd.getText().toString();
                String userid = null;
                String username = null;

                list = mHelper.queryById(et_user_id.getText().toString());
                for (User u : list) {
                    System.out.println(u.toString());
                    userid = u.getUserid();
                    username = u.getUsername();
                }

                user_change = new User(userid, username, Long.parseLong(password), 0, 0, 0);

                if (mHelper.update(user_change) > 0) {
                    ToastUtil.show(this, "修改成功！");
                    finish();
                }
            }
        } else if (view.getId() == R.id.btn_cancel) {
            finish();
        }

    }

}
