package com.xkx.book.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.xkx.book.activity.borrow.BorrowBookActivity;
import com.xkx.book.activity.book.FindBookActivity;
import com.xkx.book.activity.book.ManageBookActivity;
import com.xkx.book.activity.seat.ReserveSeatActivity;
import com.xkx.book.activity.seat.ViewReservationActivity;
import com.xkx.book.activity.user.ManageUserActivity;
import com.xkx.book.R;
import com.xkx.book.activity.user.UserUpdateActivity;
import com.xkx.book.activity.borrow.ViewBorrowActivity;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.database.BorrowDBHelper;
import com.xkx.book.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_user_info, btn_user_serch, btn_user_borrow, btn_admin_info, btn_admin_bookmanage, btn_admin_borrow, btn_admin_reserve;
    Button btn_user_seat;
    private CheckBox ck_admin;
    private BookDBHelper mHelper;
    private BorrowDBHelper nHelper;
    Intent intent;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
        initDatas();


    }

    @Override
    protected void onStart() {
        super.onStart();
//        //获取数据库帮助器的实例
        mHelper = BookDBHelper.getInstance(this);
        nHelper = BorrowDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();

        nHelper.openWriteLink();
        nHelper.openReadLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭数据库连接
//        mHelper.closeLink();
//        nHelper.closeLink();
    }

    private void initDatas() {
        intent = getIntent();
//        String uid = intent.getStringExtra("uid");
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");
        ToastUtil.show(this, "欢迎" + uid + "登录");

        //判断管理员模式
        ck_admin.setEnabled(false);
        int user_status = sharedPreferences.getInt("User_status", 0);
        if (user_status == 0) {
            ck_admin.setChecked(false);
            btn_admin_info.setEnabled(false);
            btn_admin_bookmanage.setEnabled(false);
            btn_admin_borrow.setEnabled(false);
            btn_admin_reserve.setEnabled(false);

        } else {
            ck_admin.setChecked(true);
            btn_admin_info.setEnabled(true);
            btn_admin_bookmanage.setEnabled(true);
            btn_admin_borrow.setEnabled(true);
            btn_admin_reserve.setEnabled(true);
        }
    }

    private void initview() {
        ck_admin = findViewById(R.id.ck_admin);
        btn_user_info = findViewById(R.id.btn_user_info);
        btn_user_serch = findViewById(R.id.btn_user_serch);
        btn_user_borrow = findViewById(R.id.btn_user_borrow);
        btn_admin_info = findViewById(R.id.btn_admin_info);
        btn_admin_bookmanage = findViewById(R.id.btn_admin_bookmanage);
        btn_admin_borrow = findViewById(R.id.btn_admin_borrow);
        btn_user_seat = findViewById(R.id.btn_user_seat);
        btn_admin_reserve = findViewById(R.id.btn_admin_reserve);

        ck_admin.setOnClickListener(this);
        btn_user_info.setOnClickListener(this);
        btn_user_serch.setOnClickListener(this);
        btn_user_borrow.setOnClickListener(this);
        btn_admin_info.setOnClickListener(this);
        btn_admin_bookmanage.setOnClickListener(this);
        btn_admin_borrow.setOnClickListener(this);
        btn_user_seat.setOnClickListener(this);
        btn_admin_reserve.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_user_info) {
            intent.setClass(this, UserUpdateActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_user_serch) {
            intent.setClass(this, FindBookActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_user_borrow) {
            intent.setClass(this, BorrowBookActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_admin_info) {
            intent.setClass(this, ManageUserActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_admin_bookmanage) {
            intent = new Intent(this, ManageBookActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_admin_borrow) {
            intent = new Intent(this, ViewBorrowActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_user_seat) {
            intent = new Intent(this, ReserveSeatActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_admin_reserve) {
            intent = new Intent(this, ViewReservationActivity.class);
            startActivity(intent);
        } else {
            ToastUtil.show(this,"error");

        }

    }
}