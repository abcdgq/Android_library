package com.xkx.book.activity.seat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    private Spinner spinnerSeatNumber;
    private ArrayAdapter<String> seatAdapter;

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
        spinnerSeatNumber = findViewById(R.id.spinner_seat_number);

        dbHelper = new ReservationDBHelper(this);
        userReservations = new ArrayList<>();

        // 初始化剩余座位数
        updateRemainingSeats();



        // 初始化座位号选择器
        spinnerSeatNumber.setEnabled(false); // 默认禁用，直到选择时间段

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                // 调用已有的方法处理日期变化
                updateRemainingSeats();
            }
        });
        radioGroupTime.setOnCheckedChangeListener((group, checkedId) -> updateRemainingSeats());

        btnReserve.setOnClickListener(v -> reserveSeat());

        btnViewReservations.setOnClickListener(v -> viewUserReservations());
    }

    private void reserveSeat() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String date = String.format("%04d-%02d-%02d", year, month + 1, day);
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
            int seatNumber = getAvailableSeatFromSpinner(timeSlot);
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
                        // 处理点击某个预约项时，弹出确认取消对话框
                        Reservation selectedReservation = userReservations.get(which);
                        showCancelConfirmationDialog(selectedReservation);  // 调用方法显示确认对话框
                    })
                    .show();
        } else {
            Toast.makeText(this, "没有找到您的预约记录", Toast.LENGTH_SHORT).show();
        }
    }

    // 显示取消确认对话框
    private void showCancelConfirmationDialog(Reservation reservation) {
        new AlertDialog.Builder(this)
                .setTitle("确认取消预约")
                .setMessage("您确定要取消此预约吗？")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 用户确认取消，执行取消操作
                    cancelReservation(reservation);
                })
                .setNegativeButton("取消", null)  // 用户取消，不做任何操作
                .show();
    }

    public void cancelReservation(Reservation reservation) {
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

        if (availableSeats > 0) {
            List<String> availableSeatList = getAvailableSeatsList(timeSlot);
            seatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSeatList);
            seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSeatNumber.setAdapter(seatAdapter);
            spinnerSeatNumber.setEnabled(true); // 启用座位选择器
        } else {
            spinnerSeatNumber.setEnabled(false); // 如果没有座位，禁用选择器
        }
    }

    private int getAvailableSeatFromSpinner(String timeSlot) {
        int seatNumber = -1;
        if (spinnerSeatNumber.getSelectedItem() != null) {
            seatNumber = Integer.parseInt(spinnerSeatNumber.getSelectedItem().toString());
        }
        return seatNumber;
    }

    private List<String> getAvailableSeatsList(String timeSlot) {
        List<String> availableSeats = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String date = String.format(day + "/" + (month + 1) + "/" + year);

        // 查询某个日期和时间段已预约的座位
        String selection = ReservationDBHelper.COLUMN_DATE + " = ? AND " + ReservationDBHelper.COLUMN_TIME_SLOT + " = ?";
        String[] selectionArgs = {date, timeSlot};

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
            cursor.close();
            return availableSeats; // 如果查询失败，返回空的座位列表
        }

        // 标记已预约的座位
        while (cursor.moveToNext()) {
            int seatNumber = cursor.getInt(seatNumberColumnIndex);
            seats[seatNumber - 1] = true; // 标记为已预约
        }
        cursor.close();

        // 获取剩余座位
        for (int i = 0; i < seats.length; i++) {
            if (!seats[i]) {
                availableSeats.add(String.valueOf(i + 1)); // 添加未被预约的座位号
            }
        }

        return availableSeats;
    }


    private int getAvailableSeats(String timeSlot) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        String date = String.format(day + "/" + (month + 1) + "/" + year);

        // 查询某个日期和时间段已预约的座位
        String selection = ReservationDBHelper.COLUMN_DATE + " = ? AND " + ReservationDBHelper.COLUMN_TIME_SLOT + " = ?";
        String[] selectionArgs = {date, timeSlot};

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
            cursor.close();
            return 0; // 如果查询失败，返回 0
        }

        // 标记已预约的座位
        while (cursor.moveToNext()) {
            int seatNumber = cursor.getInt(seatNumberColumnIndex);
            seats[seatNumber - 1] = true; // 标记为已预约
        }

        cursor.close();

        // 计算剩余座位数
        int availableSeats = 0;
        for (boolean seat : seats) {
            if (!seat) availableSeats++; // 如果座位没有被预约，则计数为可用座位
        }

        return availableSeats;
    }


}

