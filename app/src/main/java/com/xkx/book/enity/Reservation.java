// Reservation.java
package com.xkx.book.enity;

public class Reservation {
    private String userId;
    private String date;
    private String timeSlot;
    private int seatNumber;

    public Reservation(String userId, String date, String timeSlot, int seatNumber) {
        this.userId = userId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.seatNumber = seatNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}
