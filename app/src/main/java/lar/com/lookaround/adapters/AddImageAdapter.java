package lar.com.lookaround.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

import lar.com.lookaround.R;
import lar.com.lookaround.util.AddImageUtil;

/**
 * Created by Attila_Dan on 16. 05. 11..
 */
public class AddImageAdapter extends ArrayAdapter<AddImageUtil> {

    public AddImageAdapter(Context context, ArrayList<AddImageUtil> images) {
        super(context, R.layout.item_addrealestate_pic, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AddImageUtil imageUtil = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_addrealestate_pic, parent, false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.item_add_realestate_pic_imageview);
        image.setImageBitmap(imageUtil.getBitmap());


        return convertView;
    }
}
