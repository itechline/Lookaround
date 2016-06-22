package lar.com.lookaround.adapters;

import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;

import lar.com.lookaround.R;
import lar.com.lookaround.util.CalendarBookingUtil;
import lar.com.lookaround.util.EstateUtil;

/**
 * Created by Attila_Dan on 16. 05. 12..
 */
public class CalendarBookingAdapter extends ArrayAdapter<CalendarBookingUtil> {

    public CalendarBookingAdapter(Context context, ArrayList<CalendarBookingUtil> users) {
        super(context, R.layout.content_booking_item, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final CalendarBookingUtil appointment = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_booking_item, parent, false);
        } else {

        }

        TextView hours = (TextView) convertView.findViewById(R.id.booking_hours_text);
        TextView minutes = (TextView) convertView.findViewById(R.id.booking_minutes_text);
        TextView foglal = (TextView) convertView.findViewById(R.id.booking_date_agree);
        if(appointment.isFoglalt()) {
            foglal.setText("Foglalt");
            foglal.setTextColor(Color.RED);
        } else {
            foglal.setText("Foglalás");
            foglal.setTextColor(Color.parseColor("#0066cc"));
        }

        hours.setText(String.valueOf(appointment.getHours()));
        minutes.setText(String.valueOf(appointment.getMinutes()));

        return convertView;
    }

}
