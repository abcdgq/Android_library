// ReservationDBHelper.java
package com.xkx.book.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReservationDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME_SLOT = "time_slot";
    public static final String COLUMN_SEAT_NUMBER = "seat_number";

    public ReservationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME_SLOT + " TEXT,"
                + COLUMN_SEAT_NUMBER + " INTEGER,"
                + "PRIMARY KEY(" + COLUMN_USER_ID + ", " + COLUMN_DATE + ", " + COLUMN_TIME_SLOT + "))";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }
}
