package com.xkx.book.activity.seat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xkx.book.R;
import com.xkx.book.adapter.ReservationAdapter;
import com.xkx.book.adapter.ViewReservationAdapter;
import com.xkx.book.database.ReservationDBHelper;
import com.xkx.book.enity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ViewReservationActivity extends AppCompatActivity {

    private ListView listViewReservations;
    private ViewReservationAdapter viewReservationAdapter;
    private List<Reservation> reservationList;
    private ReservationDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservation);

        listViewReservations = findViewById(R.id.listViewReservations);
        reservationList = new ArrayList<>();
        dbHelper = new ReservationDBHelper(this);

        // 加载所有预约记录
        loadAllReservations();

        // 设置适配器
        viewReservationAdapter = new ViewReservationAdapter(this, reservationList);
        listViewReservations.setAdapter(viewReservationAdapter);
    }

    private void loadAllReservations() {
        // 清空当前列表
        reservationList.clear();

        // 查询所有预约记录
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + ReservationDBHelper.TABLE_RESERVATIONS, null);

        // 打印列索引，帮助调试
        int userIdIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_USER_ID);
        int dateIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_DATE);
        int timeSlotIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_TIME_SLOT);
        int seatNumberIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_SEAT_NUMBER);

        Log.d("Database", "Column Indexes: " +
                "userId=" + userIdIndex + ", " +
                "date=" + dateIndex + ", " +
                "timeSlot=" + timeSlotIndex + ", " +
                "seatNumber=" + seatNumberIndex);

        // 如果列索引为 -1，说明列名不匹配
        if (userIdIndex == -1 || dateIndex == -1 || timeSlotIndex == -1 || seatNumberIndex == -1) {
            Toast.makeText(this, "数据库列名不匹配，请检查表结构", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }

        // 处理查询结果
        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(userIdIndex);
                String date = cursor.getString(dateIndex);
                String timeSlot = cursor.getString(timeSlotIndex);
                int seatNumber = cursor.getInt(seatNumberIndex);

                // 将查询结果添加到预约列表
                reservationList.add(new Reservation(userId, date, timeSlot, seatNumber));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (reservationList.isEmpty())
        {
            Toast.makeText(this, "没有预约记录", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCancelConfirmationDialog(Reservation reservation) {
        new AlertDialog.Builder(this)
                .setTitle("确认取消预约")
                .setMessage("您确定要取消此预约吗？")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 用户点击确认后，执行取消预约
                    cancelReservation(reservation);
                })
                .setNegativeButton("取消", null) // 用户点击取消，什么都不做
                .show();
    }

    // 取消预约的方法
    private void cancelReservation(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 删除预约记录
        String whereClause = ReservationDBHelper.COLUMN_USER_ID + " = ? AND " + ReservationDBHelper.COLUMN_DATE + " = ? AND " + ReservationDBHelper.COLUMN_TIME_SLOT + " = ? AND " + ReservationDBHelper.COLUMN_SEAT_NUMBER + " = ?";
        String[] whereArgs = {
                reservation.getUserId(),
                reservation.getDate(),
                reservation.getTimeSlot(),
                String.valueOf(reservation.getSeatNumber())
        };
        db.delete(ReservationDBHelper.TABLE_RESERVATIONS, whereClause, whereArgs);

        // 从列表中移除取消的预约
        reservationList.remove(reservation);
        viewReservationAdapter.notifyDataSetChanged(); // 更新列表视图

        // 显示取消成功提示
        Toast.makeText(this, "取消成功！", Toast.LENGTH_SHORT).show();
    }
}
