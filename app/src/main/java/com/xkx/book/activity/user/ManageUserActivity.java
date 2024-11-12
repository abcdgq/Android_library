package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xkx.book.R;

public class ManageUserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_add_user, btn_view_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        btn_add_user = findViewById(R.id.btn_add_user);
        btn_view_user = findViewById(R.id.btn_view_user);

        btn_add_user.setOnClickListener(this);
        btn_view_user.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.btn_add_user) {
            // 添加用户界面
            intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_view_user) {
            // 查看用户界面
            intent = new Intent(this, ViewUserActivity.class);
            startActivity(intent);
        }

    }
}