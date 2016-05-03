package lar.com.lookaround.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import lar.com.lookaround.R;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.SettingUtil;
import lar.com.lookaround.util.SpinnerUtil;

/**
 * Created by Attila_Dan on 16. 05. 03..
 */
public class SpinnerAdapter extends ArrayAdapter<SpinnerUtil> {

    public SpinnerAdapter(Context context, ArrayList<SpinnerUtil> spinner) {
        super(context, android.R.layout.simple_spinner_item, spinner);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final SpinnerUtil spinnerUtil = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        text1.setText(spinnerUtil.getName());

        Log.d("SPINNER_GETVIEW", spinnerUtil.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final SpinnerUtil spinnerUtil = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        text1.setText(spinnerUtil.getName());

        Log.d("SPINNER_GETVIEW", spinnerUtil.getName());

        return convertView;
    }
}
