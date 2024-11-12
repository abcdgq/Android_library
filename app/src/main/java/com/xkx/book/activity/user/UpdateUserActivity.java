package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.xkx.book.R;
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_id, et_name, et_password;
    private Button btn_save, btn_cancel;
    private UserDBHelper mHelper;
    private CheckBox checkBox;
    private String view_user_uid;
    int user_status;
    private int view_user_isadmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        et_id = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_pwd);

        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        checkBox = findViewById(R.id.ck_updata_admin);

        et_id.setEnabled(false);

        Intent intent = getIntent();
        view_user_uid = intent.getStringExtra("view_user_uid");
        view_user_isadmin = intent.getIntExtra("view_user_isadmin", 0);

        Log.e("ning", String.valueOf(view_user_isadmin));
        if (view_user_isadmin == 1)
            checkBox.setChecked(true);
        else checkBox.setChecked(false);

        et_id.setText(view_user_uid);

        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            String tempId = view_user_uid;
            String tempName = et_name.getText().toString();
            String tempPassword = et_password.getText().toString();
            // 管理员(0-用户, 1-管理员)
            int user_status = checkBox.isChecked() ? 1 : 0;

            if (!tempId.isEmpty() && !tempName.isEmpty() && !tempPassword.isEmpty()) {
                // 声明一个用户信息对象，并填写它的字段值
                User user = new User(view_user_uid, tempName, Long.parseLong(tempPassword), 0, user_status, 0);

                if (mHelper.update(user) > 0) {
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