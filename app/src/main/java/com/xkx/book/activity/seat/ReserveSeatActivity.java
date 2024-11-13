package com.xkx.book.activity.seat;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xkx.book.R;

public class ReserveSeatActivity extends AppCompatActivity {
    //假设营业时间是8:00 到22:00 ,单次预约为2h
    //当前用户的uid
    private DatePicker datePicker;
    private TextView tvReservationInfo;

    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);

        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        datePicker = findViewById(R.id.datePicker);
        Button btnReserve = findViewById(R.id.btn_reserve);
        tvReservationInfo = findViewById(R.id.tv_reservation_info);

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveSeat();
            }
        });
    }

    private void reserveSeat() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // 获取当前日期
        Calendar currentDate = Calendar.getInstance();
        // 获取结束日期（当前日期 + 7 天）
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_YEAR, 7);

        // 创建用户选择的日期
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        // 比较日期
        if (selectedDate.after(currentDate) && selectedDate.before(endDate)) {
            tvReservationInfo.setText("预约成功！预约日期为：" + day + "/" + (month + 1) + "/" + year);
        } else {
            tvReservationInfo.setText("只能预约七天内的座位。");
        }
    }

}
