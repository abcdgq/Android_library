package com.xkx.book.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.xkx.book.activity.user.LoginActivity;
import com.xkx.book.R;

public class SplashActivity extends AppCompatActivity {
    // 最初开始界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //创建子线程
        Thread myThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                    finish();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        myThread.start();
    }
}