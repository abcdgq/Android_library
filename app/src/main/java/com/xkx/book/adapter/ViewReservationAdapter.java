// ReservationAdapter.java
package com.xkx.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xkx.book.activity.seat.ViewReservationActivity;
import com.xkx.book.enity.Reservation;

import java.util.List;

public class ViewReservationAdapter extends ArrayAdapter<Reservation> {

    private Context context;
    private List<Reservation> reservations;

    public ViewReservationAdapter(Context context, List<Reservation> reservations) {
        super(context, 0, reservations);
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Reservation reservation = reservations.get(position);

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("时间段: " + reservation.getTimeSlot() + " | 座位: " + reservation.getSeatNumber());
        text2.setText("日期: " + reservation.getDate() + " | 用户名: " + reservation.getUserId());

        convertView.setOnClickListener(v -> {
            if (context instanceof ViewReservationActivity) {
                ((ViewReservationActivity) context).showCancelConfirmationDialog(reservation);
            }
        });
        return convertView;
    }

}
