package com.xkx.book.enity;

public class Seat {
    private int id;
    private String seatCode; // 座位号，如A1, A2等
    private boolean isAvailable; // 座位是否可用

    public Seat(int id, String seatCode, boolean isAvailable) {
        this.id = id;
        this.seatCode = seatCode;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
