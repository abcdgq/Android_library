package com.xkx.book.activity.book;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.xkx.book.R;
import com.xkx.book.adapter.FindWayAdapter;

import java.util.ArrayList;
import java.util.List;


// 活动类：实现轮播功能

public class FindWayActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FindWayAdapter adapter;
    private List<String> locationData; // 存储解析后的传入数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_way);

        // 获取传递过来的数据
        Intent intent = getIntent();
        String locationString = intent.getStringExtra("location"); // 传入数据结构：x层-xxx-yyy书架-xxxx

        // 解析数据
        locationData = new ArrayList<>();
        if (locationString != null) {
            String[] parts = locationString.split("-"); // 分割字符串
            for (String part : parts) {
                locationData.add(part); // 每个部分添加到列表
            }
        } else {
            locationData.add("缺省"); // 如果没有数据传入，添加默认值
        }

        // 初始化 ViewPager2 和适配器
        viewPager = findViewById(R.id.viewPager);
        adapter = new FindWayAdapter(locationString, locationData, this); // 传入完整数据和分割后的部分数据
        viewPager.setAdapter(adapter);
    }
}
