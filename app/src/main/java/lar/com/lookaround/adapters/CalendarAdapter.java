package lar.com.lookaround.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lar.com.lookaround.R;

/**
 * Created by Attila_Dan on 16. 05. 04..
 */
public class CalendarAdapter extends ArrayAdapter<String> {

    int m, y;

    ArrayList<String> selected;

    public CalendarAdapter(Context context, List<String> objs, ArrayList<String> objs2, int y, int m) {
        super(context, R.layout.content_booking_days, objs);
        this.m = m;
        this.y = y;
        this.selected = objs2;
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

        final Calendar hlper = Calendar.getInstance();
        final java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");

        int d = 0;
        try {
            d = Integer.valueOf(key);
        } catch (Exception e) {

        }

        if(d != 0) {
            hlper.set(Calendar.DAY_OF_MONTH, d);
            hlper.set(Calendar.MONTH, m);
            hlper.set(Calendar.YEAR, y);

            if(selected.contains(sdf2.format(hlper.getTime()))) {
                convertView.setBackgroundResource(R.drawable.b_d_border1);
            } else {
                convertView.setBackground(null);
            }
        } else {
            convertView.setBackground(null);
        }


        //Calendar thisDay = Calendar.getInstance();

        day.setBackground(null);



        return convertView;
    }



}
