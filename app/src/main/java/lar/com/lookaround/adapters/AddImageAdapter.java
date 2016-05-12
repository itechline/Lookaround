package lar.com.lookaround.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    public AddImageAdapter(Context context, ArrayList<AddImageUtil> images) {
        super(context, R.layout.item_addrealestate_pic, images);
        Log.d("IMAGE_", "ADDIMAGEADAPTER");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("IMAGE_", "GETVIEW");

        final AddImageUtil imageUtil = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_addrealestate_pic, parent, false);
        } else {
            /*ImageView image = (ImageView) convertView.findViewById(R.id.item_add_realestate_pic_imageview);
            if (image != null && image.getDrawable() != null && ((BitmapDrawable) image.getDrawable()).getBitmap() != null) {
                ((BitmapDrawable) image.getDrawable()).getBitmap().recycle();
            }
            image.setImageBitmap(null);*/

        }
        ImageView image = (ImageView) convertView.findViewById(R.id.item_add_realestate_pic_imageview);
        image.setImageBitmap(imageUtil.getBitmap());

        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GALLERY_ID", String.valueOf(imageUtil.getId()));
                callPopup();
            }
        });*/


        return convertView;
    }
}
