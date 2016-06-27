package lar.com.lookaround.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import lar.com.lookaround.R;
import lar.com.lookaround.models.Idopont;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.CalendarBookingUtil;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.SettingUtil;

/**
 * Created by Attila_Dan on 16. 05. 12..
 */
public class CalendarMyBookingAdapter extends ArrayAdapter<Idopont> {

    public CalendarMyBookingAdapter(Context context, ArrayList<Idopont> users) {
        super(context, R.layout.content_booking_item_myads, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Idopont tmp = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_booking_item_myads, parent, false);
        } else {

        }

        TextView when = (TextView) convertView.findViewById(R.id.booking_hours_text);
        TextView who = (TextView) convertView.findViewById(R.id.who_booked_it_text);

        TextView ok = (TextView) convertView.findViewById(R.id.booking_date_agree);
        TextView cancel = (TextView) convertView.findViewById(R.id.booking_date_disaggree);

        if(tmp.getStatus() == 0) {
            ok.setText("Elfogad");
            cancel.setText("Elutasít");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmp.setStatus(1);
                    final ProgressDialog pd = new ProgressDialog(getContext());
                    pd.show();
                    EstateUtil.updateIdopont(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            pd.dismiss();
                            notifyDataSetChanged();
                        }
                    }, SettingUtil.getToken(getContext()), 1, tmp.getId());
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmp.setStatus(2);
                    final ProgressDialog pd = new ProgressDialog(getContext());
                    pd.show();
                    EstateUtil.updateIdopont(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            pd.dismiss();
                            notifyDataSetChanged();
                        }
                    }, SettingUtil.getToken(getContext()), 2, tmp.getId());
                }
            });
        } else if(tmp.getStatus() == 1) {
            ok.setText("Elfogadva");
            cancel.setText("");
        } else if(tmp.getStatus() == 2) {
            ok.setText("");
            cancel.setText("Elutasítva");
        }

        when.setText(tmp.getDatum());
        who.setText(tmp.getFel());


        return convertView;
    }

}
