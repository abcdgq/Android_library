package com.xkx.book.activity.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xkx.book.R;

public class ManageBookActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_add_book, btn_view_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_book);

        btn_add_book = findViewById(R.id.btn_add_book);
        btn_view_book = findViewById(R.id.btn_view_book);

        btn_add_book.setOnClickListener(this);
        btn_view_book.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.btn_add_book) {
            intent = new Intent(this, AddBookActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_view_book) {
            intent = new Intent(this, ViewBookActivity.class);
            startActivity(intent);
        }

    }
}