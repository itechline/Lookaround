package lar.com.lookaround.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import lar.com.lookaround.R;
import lar.com.lookaround.util.AdmonitorUtil;

public class AdmonitorAdapter extends ArrayAdapter<AdmonitorUtil> {

    public AdmonitorAdapter(Context context, ArrayList<AdmonitorUtil> users) {
        super(context, R.layout.content_admonitor_item, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final AdmonitorUtil admonitorUtil = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_admonitor_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.admonitor_liset_item_textView);
        name.setText(admonitorUtil.getName());

        ImageView modify = (ImageView) convertView.findViewById(R.id.admonitor_list_item_edit_image);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView delete = (ImageView) convertView.findViewById(R.id.admonitor_list_item_delete_image);
        final View finalConvertView = convertView;
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopupDelete(admonitorUtil.getId(), finalConvertView);
            }
        });


        return convertView;
    }


    private void callPopupDelete(final int id, final View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.areyousure_popup, null);
        TextView textView = (TextView) popupView.findViewById(R.id.areyousure_delete_textView);
        textView.setText("Biztosan törli a hirdetésfigyelőt?");

        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT,
                true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        ((Button) popupView.findViewById(R.id.delete_ad_yes_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        AdmonitorUtil.removeAdmonitor(id);
                        view.setVisibility(View.INVISIBLE);
                        popupWindow.dismiss();
                    }
                });

        ((Button) popupView.findViewById(R.id.delete_ad_no_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });
    }
}
