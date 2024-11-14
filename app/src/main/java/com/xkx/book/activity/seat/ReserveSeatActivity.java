package com.xkx.book.activity.seat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.ContentValues;

import com.xkx.book.R;
import com.xkx.book.adapter.ReservationAdapter;
import com.xkx.book.database.ReservationDBHelper;
import com.xkx.book.enity.Reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReserveSeatActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private RadioGroup radioGroupTime;
    private TextView tvReservationInfo, tvRemainingSeats;
    private String uid;

    private ReservationDBHelper dbHelper;
    private List<Reservation> userReservations;

    private String[] timeSlots = {
            "08:00 - 10:00", "10:00 - 12:00", "12:00 - 14:00", "14:00 - 16:00",
            "16:00 - 18:00", "18:00 - 20:00"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);

        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        datePicker = findViewById(R.id.datePicker);
        radioGroupTime = findViewById(R.id.radioGroupTime);
        Button btnReserve = findViewById(R.id.btn_reserve);
        Button btnViewReservations = findViewById(R.id.btn_view_reservations);
        tvReservationInfo = findViewById(R.id.tv_reservation_info);
        tvRemainingSeats = findViewById(R.id.tv_remaining_seats);

        dbHelper = new ReservationDBHelper(this);
        userReservations = new ArrayList<>();

        // 初始化剩余座位数
        updateRemainingSeats();

        radioGroupTime.setOnCheckedChangeListener((group, checkedId) -> updateRemainingSeats());

        btnReserve.setOnClickListener(v -> reserveSeat());

        btnViewReservations.setOnClickListener(v -> viewUserReservations());
    }

    private void reserveSeat() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int selectedId = radioGroupTime.getCheckedRadioButtonId();

        if (selectedId == -1) {
            tvReservationInfo.setText("请选择一个时间段。");
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String timeSlot = selectedRadioButton.getText().toString(); // 选择的时间段

        // 检查用户是否在同一日期和时间段已经预约
        if (userHasReservationOnSameDayAndTimeSlot(day, month, year, timeSlot)) {
            tvReservationInfo.setText("您已经在该时间段预约了座位，不能重复预约！");
            return;
        }

        Calendar currentDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_YEAR, 7);

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        if (selectedDate.after(currentDate) && selectedDate.before(endDate) && !selectedDate.equals(currentDate)) {
            // 查询数据库获取当前时段的剩余座位
            int seatNumber = findAvailableSeat(timeSlot);
            if (seatNumber == -1) {
                tvReservationInfo.setText("该时段座位已满，请选择其他时段。");
            } else {
                tvReservationInfo.setText("预约成功！预约座位为：" + seatNumber + "，预约日期为：" + year + "/" + (month + 1) + "/" + day + "  " + timeSlot);
                addReservationToDatabase(new Reservation(uid, day + "/" + (month + 1) + "/" + year, timeSlot, seatNumber));
                updateRemainingSeats();
            }
        } else {
            tvReservationInfo.setText("只能预约七天内的座位，且至少提前一天");
        }
    }

    // 检查用户是否已在同一日期和时间段预约
    private boolean userHasReservationOnSameDayAndTimeSlot(int day, int month, int year, String timeSlot) {
        // 查询用户的所有预约
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = ReservationDBHelper.COLUMN_USER_ID + " = ? AND " +
                ReservationDBHelper.COLUMN_DATE + " = ? AND " +
                ReservationDBHelper.COLUMN_TIME_SLOT + " = ?";
        String[] selectionArgs = {uid, day + "/" + (month + 1) + "/" + year, timeSlot};

        Cursor cursor = db.query(ReservationDBHelper.TABLE_RESERVATIONS,
                new String[]{ReservationDBHelper.COLUMN_USER_ID},
                selection,
                selectionArgs,
                null,
                null,
                null);

        boolean hasReservation = cursor.getCount() > 0;
        cursor.close();
        return hasReservation;
    }

    private int findAvailableSeat(String timeSlot) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询此时段的座位状态
        String selection = ReservationDBHelper.COLUMN_TIME_SLOT + " = ?";
        String[] selectionArgs = { timeSlot };

        Cursor cursor = db.query(ReservationDBHelper.TABLE_RESERVATIONS,
                new String[]{ReservationDBHelper.COLUMN_SEAT_NUMBER},
                selection,
                selectionArgs,
                null,
                null,
                null);

        boolean[] seats = new boolean[20]; // 标记座位的预约情况

        int seatNumberColumnIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_SEAT_NUMBER);
        if (seatNumberColumnIndex == -1) {
            // 如果列索引无效，则记录日志并返回
            //Log.e("Database", "Column " + ReservationDBHelper.COLUMN_SEAT_NUMBER + " not found.");
            return -1; // 如果查询失败，返回 -1
        }

        while (cursor.moveToNext()) {
            int seatNumber = cursor.getInt(seatNumberColumnIndex);
            seats[seatNumber - 1] = true; // 标记为已预约
        }

        cursor.close();

        // 查找一个可用的座位
        for (int i = 0; i < seats.length; i++) {
            if (!seats[i]) {
                return i + 1; // 返回未被预约的座位号
            }
        }
        return -1; // 所有座位都已预约
    }


    private boolean addReservationToDatabase(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReservationDBHelper.COLUMN_USER_ID, reservation.getUserId());
        values.put(ReservationDBHelper.COLUMN_DATE, reservation.getDate());
        values.put(ReservationDBHelper.COLUMN_TIME_SLOT, reservation.getTimeSlot());
        values.put(ReservationDBHelper.COLUMN_SEAT_NUMBER, reservation.getSeatNumber());
        return db.insert(ReservationDBHelper.TABLE_RESERVATIONS, null, values) != -1;
    }

    private void viewUserReservations() {
        userReservations = getUserReservationsFromDB();
        if (!userReservations.isEmpty()) {
            // Show the reservations (you may implement a list activity here)
            ReservationAdapter adapter = new ReservationAdapter(this, userReservations);
            // Use a dialog or a new activity to display the user's reservations
            // Example:
            new AlertDialog.Builder(this)
                    .setTitle("您的预约(点击取消预约)")
                    .setAdapter(adapter, (dialog, which) -> {
                        // Handle reservation cancellation
                        cancelReservation(userReservations.get(which));
                    })
                    .show();
        } else {
            Toast.makeText(this, "没有找到您的预约记录", Toast.LENGTH_SHORT).show();
        }
    }

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

        // 更新座位状态
        updateRemainingSeats();

        // 显示取消信息
        Toast.makeText(this, "取消成功！", Toast.LENGTH_SHORT).show();
    }

    private List<Reservation> getUserReservationsFromDB() {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = ReservationDBHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { uid };

        Cursor cursor = db.query(
                ReservationDBHelper.TABLE_RESERVATIONS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int dateColumnIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_DATE);
        int timeSlotColumnIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_TIME_SLOT);
        int seatNumberColumnIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_SEAT_NUMBER);

        if (dateColumnIndex == -1 || timeSlotColumnIndex == -1 || seatNumberColumnIndex == -1) {
            // 如果列索引无效，则记录日志并返回空的预约列表
            //Log.e("Database", "One or more columns not found in the database.");
            return reservations;
        }

        while (cursor.moveToNext()) {
            String date = cursor.getString(dateColumnIndex);
            String timeSlot = cursor.getString(timeSlotColumnIndex);
            int seatNumber = cursor.getInt(seatNumberColumnIndex);

            Reservation reservation = new Reservation(uid, date, timeSlot, seatNumber);
            reservations.add(reservation);
        }
        cursor.close();
        return reservations;
    }


    // 更新剩余座位数
    private void updateRemainingSeats() {
        int selectedId = radioGroupTime.getCheckedRadioButtonId();
        if (selectedId == -1) return; // 没有选择时间段

        RadioButton selectedRadioButton = findViewById(selectedId);
        String timeSlot = selectedRadioButton.getText().toString(); // 选择的时间段

        int availableSeats = getAvailableSeats(timeSlot);
        tvRemainingSeats.setText("剩余座位数：" + availableSeats);
    }

    private int getAvailableSeats(String timeSlot) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询已预约的座位
        String selection = ReservationDBHelper.COLUMN_TIME_SLOT + " = ?";
        String[] selectionArgs = { timeSlot };

        Cursor cursor = db.query(ReservationDBHelper.TABLE_RESERVATIONS,
                new String[]{ReservationDBHelper.COLUMN_SEAT_NUMBER},
                selection,
                selectionArgs,
                null,
                null,
                null);

        boolean[] seats = new boolean[20]; // 标记座位的预约情况

        int seatNumberColumnIndex = cursor.getColumnIndex(ReservationDBHelper.COLUMN_SEAT_NUMBER);
        if (seatNumberColumnIndex == -1) {
            // 如果列索引无效，则记录日志并返回
            //Log.e("Database", "Column " + ReservationDBHelper.COLUMN_SEAT_NUMBER + " not found.");
            return 0; // 如果查询失败，返回 0
        }

        while (cursor.moveToNext()) {
            int seatNumber = cursor.getInt(seatNumberColumnIndex);
            seats[seatNumber - 1] = true; // 标记为已预约
        }

        cursor.close();

        // 计算剩余座位数
        int availableSeats = 0;
        for (boolean seat : seats) {
            if (!seat) availableSeats++;
        }

        return availableSeats;
    }

}