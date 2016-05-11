package lar.com.lookaround.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import lar.com.lookaround.R;
import lar.com.lookaround.util.AddImageUtil;

/**
 * Created by Attila_Dan on 16. 05. 11..
 */
public class AddImageAdapter extends ArrayAdapter<AddImageUtil> {

    public AddImageAdapter(Context context, ArrayList<AddImageUtil> users) {
        super(context, R.layout.item_realestate, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AddImageUtil imageUtil = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_realestate, parent, false);
        } else {
            ImageView image = (ImageView) convertView.findViewById(R.id.item_realestate_mainpic);
            if (image != null && image.getDrawable() != null && ((BitmapDrawable) image.getDrawable()).getBitmap() != null) {
                ((BitmapDrawable) image.getDrawable()).getBitmap().recycle();
            }
            image.setImageBitmap(null);

        }
        ImageView image = (ImageView) convertView.findViewById(R.id.item_realestate_mainpic);



        return convertView;
    }
}
