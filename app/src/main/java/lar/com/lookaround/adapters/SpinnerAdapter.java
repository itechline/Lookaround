package lar.com.lookaround.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lar.com.lookaround.R;
import lar.com.lookaround.util.SpinnerUtil;

/**
 * Created by Attila_Dan on 16. 05. 03..
 */
public class SpinnerAdapter extends ArrayAdapter<SpinnerUtil> implements ThemedSpinnerAdapter {
    final private ThemedSpinnerAdapter.Helper mDropDownHelper;

    public SpinnerAdapter(Context context, ArrayList<SpinnerUtil> spinner) {
        super(context, android.R.layout.simple_spinner_item, spinner);
        mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final SpinnerUtil spinnerUtil = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_simple, parent, false);
        }

        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);

        if (text1.getText().length() == 0) {
            text1.setText(spinnerUtil.getName());
            Log.d("SPINNER_GETVIEW", spinnerUtil.getName());
        }



        return convertView;
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        mDropDownHelper.setDropDownViewTheme(theme);
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownHelper.getDropDownViewTheme();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final SpinnerUtil spinnerUtil = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
            convertView = inflater.inflate(R.layout.item_spinner_dropdown, parent, false);
        }


        TextView text1 = (TextView) convertView.findViewById(R.id.dropdown_text);
        text1.setText(spinnerUtil.getName());

        Log.d("SPINNER_GETVIEW", spinnerUtil.getName());

        return convertView;
    }
}
