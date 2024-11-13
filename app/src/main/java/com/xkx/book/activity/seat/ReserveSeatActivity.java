package com.xkx.book.activity.seat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.xkx.book.R;

public class ReserveSeatActivity extends AppCompatActivity {
    //当前用户的uid
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);

        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

    }
}
