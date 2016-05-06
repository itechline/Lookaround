package lar.com.lookaround.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import lar.com.lookaround.R;

/**
 * Created by Attila_Dan on 16. 05. 04..
 */
public class CalendarAdapter extends ArrayAdapter<String> {

    int m, y;

    public CalendarAdapter(Context context, List<String> objs, int y, int m) {
        super(context, R.layout.content_booking_days, objs);
        this.m = m;
        this.y = y;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_booking_days, parent, false);

        }
        String key = getItem(position);
        final TextView day = (TextView) convertView.findViewById(R.id.booking_date_textView);
        final RelativeLayout daybg = (RelativeLayout) convertView.findViewById(R.id.booking_date_rlayout);
        if(key.isEmpty()) {
            daybg.setBackground(null);
        }

        day.setText(key);
        day.setTag(key);

        int d = 0;
        try {
            d = Integer.valueOf(key);
        } catch (Exception e) {

        }



        //Calendar thisDay = Calendar.getInstance();

        day.setBackground(null);



        return convertView;
    }



}
