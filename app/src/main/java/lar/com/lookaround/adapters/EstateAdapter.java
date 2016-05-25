package lar.com.lookaround.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import lar.com.lookaround.R;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.SettingUtil;

public class EstateAdapter extends ArrayAdapter<EstateUtil> {

    public EstateAdapter(Context context, ArrayList<EstateUtil> users) {
        super(context, R.layout.item_realestate, users);
    }



    public void stopDownloadingImage(int firstVisibleItem, int lastVisibleItem) {
        ArrayList<DownloadImageTask> tmp = new ArrayList<>();
        for (DownloadImageTask tsk :
                imageList) {
            if(tsk.position < firstVisibleItem || tsk.position > lastVisibleItem) {
                tmp.add(tsk);
            }
        }
        for (DownloadImageTask tsk: tmp) {
            imageList.remove(tsk);
            tsk.cancel(true);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final EstateUtil estate = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if (estate.getJustme().equals("0")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_realestate, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_myestates, parent, false);
            }
            //new DownloadImageTask((ImageView) convertView.findViewById(R.id.item_realestate_mainpic), convertView.getId()).execute(estate.getUrls());
        } else {
            ImageView image = (ImageView) convertView.findViewById(R.id.item_realestate_mainpic);
            if (image != null && image.getDrawable() != null && ((BitmapDrawable) image.getDrawable()).getBitmap() != null) {
                ((BitmapDrawable) image.getDrawable()).getBitmap().recycle();
            }
            ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.item_realestate_progressbar);
            progressBar.setVisibility(View.VISIBLE);
            image.setImageBitmap(null);
        }
            TextView adress = (TextView) convertView.findViewById(R.id.item_realestate_adress1);
            TextView street = (TextView) convertView.findViewById(R.id.item_realestate_adress2);
            TextView description = (TextView) convertView.findViewById(R.id.item_realestate_description);

            final CheckBox fav = (CheckBox) convertView.findViewById(R.id.item_realestate_isfavourite);
            ImageView image = (ImageView) convertView.findViewById(R.id.item_realestate_mainpic);

            TextView size = (TextView) convertView.findViewById(R.id.list_size_textView);
            TextView rooms = (TextView) convertView.findViewById(R.id.list_roomcount_textView);
            ImageView furniture = (ImageView) convertView.findViewById(R.id.list_furniture_imageview);
            ImageView balcony = (ImageView) convertView.findViewById(R.id.list_balcony_imageview);
            ImageView parking = (ImageView) convertView.findViewById(R.id.list_parking_imageview);

            size.setText(estate.getMeret());
            rooms.setText(String.valueOf(estate.getSzobaszam()));

            if (estate.getButor() == 1) {
                furniture.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_nofurniture));
            } else {
                furniture.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_furniture));
            }

            if (estate.getErkely() == 0) {
                balcony.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_nobalcony));
            } else {
                balcony.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_balcony));
            }

            if (estate.getParkolas() == 4) {
                parking.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_noparking));
            } else {
                parking.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_parking));
            }


            adress.setText(estate.getAdress());
            street.setText(estate.getStreet());
            description.setText(estate.getDescription());

            if (estate.getJustme().equals("0")) {
                TextView price = (TextView) convertView.findViewById(R.id.Price);

                Locale locale = new Locale("en", "UK");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                //symbols.setDecimalSeparator(';');
                symbols.setGroupingSeparator('.');

                String pattern = "###,###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                String format = decimalFormat.format(estate.getPrice());

                if (estate.getType() == 1) {
                    price.setText(format + " Ft");
                } else {
                    price.setText(format + " Ft/hó");
                }
                fav.setChecked(estate.isFavourite());

                fav.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //is chkIos checked?
                        if (((CheckBox) v).isChecked()) {
                            //if (fav.isChecked()) {
                            setEstateFavourite(estate, fav, String.valueOf(estate.getId()), SettingUtil.getToken(getContext()), "1");
                        } else {
                            setEstateFavourite(estate, fav, String.valueOf(estate.getId()), SettingUtil.getToken(getContext()), "0");
                        }
                    }
                });
            } else {
                Button delete = (Button) convertView.findViewById(R.id.myads_delete_button);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPopupDelete(estate.getId());
                    }
                });
            }

            // TODO: remove comment signs to load images
            if (estate.getUrls() != null || !estate.getUrls().equals("")) {
                Log.d("URLS", estate.getUrls());
                final DownloadImageTask task = new DownloadImageTask(image, position, convertView);
                imageList.add(task);
                task.execute(estate.getUrls());
            }


        return convertView;
    }


    private void callPopupDelete(int id) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.areyousure_popup, null);

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



    public void setEstateFavourite(final EstateUtil estate,final CheckBox fav, final String idSend, final String tokenSend, final String favSend) {

        EstateUtil.setFavorite(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if((boolean)result){
                    if (fav.isChecked()) {
                        fav.setChecked(false);
                    } else {
                        fav.setChecked(true);
                    }
                } else {
                    if (fav.isChecked()){
                        estate.setIsFavourite(true);
                    } else {
                        estate.setIsFavourite(false);
                    }
                }
            }
        },idSend, tokenSend, favSend);
    }



    private static ArrayList<DownloadImageTask> imageList= new ArrayList<>();



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        //ImageView bmImage;
        View convertView;
        public int position;
        private final WeakReference<ImageView> imageViewReference;



        public DownloadImageTask(ImageView bmImage, int position, View convertView) {
            //this.bmImage = bmImage;
            imageViewReference = new WeakReference<ImageView>(bmImage);
            this.position = position;
            this.convertView = convertView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (isCancelled())
                result = null;

            if(Thread.interrupted()) {
                result = null;
            }

            if (imageList == null) {
                imageList = new ArrayList<>();
            }
            imageList.remove(this);

            if (result != null) {
                //bmImage.setImageBitmap(result);
                //bmImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (imageViewReference != null && result != null) {
                    final ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(result);
                    }
                }

                ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.item_realestate_progressbar);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
