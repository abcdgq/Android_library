package com.xkx.book.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xkx.book.R;
import com.xkx.book.adapter.UserAdapter;
import com.xkx.book.database.UserDBHelper;
import com.xkx.book.enity.User;
import com.xkx.book.util.ToastUtil;

import java.util.List;

public class ViewUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lv_view_user;
    private UserDBHelper mHelper;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        lv_view_user = findViewById(R.id.lv_view_user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();

        List<User> user = mHelper.queryAll();
        for (User u : user) {
            Log.e("ning", u.toString());
        }
        userAdapter = new UserAdapter(user, ViewUserActivity.this);
        lv_view_user.setAdapter(userAdapter);

        lv_view_user.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        User user = (User) adapterView.getItemAtPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserActivity.this);
        builder.setTitle("选择操作？");
        // 修改
        builder.setPositiveButton("修改", (dialog, whichButton) -> {
            Log.e("ning", user.toString());
            Log.e("ning", user.getUserid());
            Log.e("ning", String.valueOf(user.getUser_status()));
            Intent intent = new Intent();
            intent.putExtra("view_user_uid", user.getUserid());
            intent.putExtra("view_user_isadmin", user.getUser_status());
            intent.setClass(ViewUserActivity.this, UpdateUserActivity.class);
            startActivity(intent);
        });
        // 删除
        builder.setNegativeButton("删除", (dialog, whichButton) -> {
            // 删除用户
            if (mHelper.deleteById(user.userid) > 0)
                ToastUtil.show(this, "删除成功");
            // 刷新页面信息
            onStart();
        });
        builder.show();

    }
}