package lar.com.lookaround.adapters;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import lar.com.lookaround.MainActivity;
import lar.com.lookaround.R;
import lar.com.lookaround.models.RealEstate;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.MessageUtil;
import lar.com.lookaround.util.SettingUtil;

public class MessageAdapter extends ArrayAdapter<MessageUtil> {

    public MessageAdapter(Context context, ArrayList<MessageUtil> users) {
        super(context, R.layout.message_you_item, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MessageUtil message = getItem(position);

        if(message.getFromme() == 0) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_you_item, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_me_item, parent, false);
        }

        TextView msg = (TextView) convertView.findViewById(R.id.single_message_item);
        msg.setText(message.getMsg());

        return convertView;
    }

}
