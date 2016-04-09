package lar.com.lookaround.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import lar.com.lookaround.MainActivity;
import lar.com.lookaround.R;
import lar.com.lookaround.models.RealEstate;

public class EstateAdapter extends ArrayAdapter<RealEstate> {
    public EstateAdapter(Context context, ArrayList<RealEstate> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RealEstate estate = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_realestate, parent, false);
        }
        // Lookup view for data population
        final TextView adress = (TextView) convertView.findViewById(R.id.item_realestate_adress1);
        TextView street = (TextView) convertView.findViewById(R.id.item_realestate_adress2);
        TextView description = (TextView) convertView.findViewById(R.id.item_realestate_description);
        TextView price = (TextView) convertView.findViewById(R.id.Price);
        final CheckBox fav = (CheckBox) convertView.findViewById(R.id.item_realestate_isfavourite);
        // Populate the data into the template view using the data object

        adress.setText(estate.getAdress());
        street.setText(estate.getStreet());
        description.setText(estate.getDescription());
        price.setText(estate.getPrice());
        fav.setChecked(estate.isFavourite());

        return convertView;
    }


}
