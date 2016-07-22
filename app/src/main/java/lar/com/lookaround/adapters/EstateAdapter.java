package lar.com.lookaround.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import lar.com.lookaround.R;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.SettingUtil;
import lar.com.lookaround.util.SpinnerUtil;

public class EstateAdapter extends ArrayAdapter<EstateUtil> {

    SoapObjectResult result;

    public EstateAdapter(Context context, ArrayList<EstateUtil> users, SoapObjectResult result) {
        super(context, R.layout.item_realestate, users);
        this.result = result;
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
        }
            ImageView image = (ImageView) convertView.findViewById(R.id.item_realestate_mainpic);
            image.setImageBitmap(null);

            ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.item_realestate_progressbar);
            progressBar.setVisibility(View.VISIBLE);

            TextView adress = (TextView) convertView.findViewById(R.id.item_realestate_adress1);
            TextView street = (TextView) convertView.findViewById(R.id.item_realestate_adress2);
            TextView description = (TextView) convertView.findViewById(R.id.item_realestate_description);




            adress.setText(estate.getAdress());
            street.setText(estate.getStreet());
            description.setText(estate.getDescription());

            if (estate.getJustme().equals("0")) {
                final CheckBox fav = (CheckBox) convertView.findViewById(R.id.item_realestate_isfavourite);

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

                if (estate.getErkely() == 2) {
                    balcony.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_nobalcony));
                } else {
                    balcony.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_balcony));
                }

                if (estate.getParkolas() == 4) {
                    parking.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_noparking));
                } else {
                    parking.setImageDrawable(getContext().getResources().getDrawable(R.drawable.list_parking));
                }


                TextView price = (TextView) convertView.findViewById(R.id.Price);

                Locale locale = new Locale("en", "UK");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                //symbols.setDecimalSeparator(';');
                symbols.setGroupingSeparator('.');

                String pattern = "###,###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                String format = decimalFormat.format(estate.getPrice());

                if(price != null) {
                    if (estate.getType() == 1) {
                        price.setText(format + " Ft");
                    } else {
                        price.setText(format + " Ft/hó");
                    }
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
                        callPopupDelete(estate);
                    }
                });

                Button update = (Button) convertView.findViewById(R.id.myads_modify_button);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.parseRerult(estate);
                    }
                });

            }

            if (estate.getUrls() != null || !estate.getUrls().equals("")) {
                final DownloadImageTask task = new DownloadImageTask(image, position, progressBar);
                imageList.add(task);
                if(!estate.getUrls().isEmpty()) {
                    task.execute(estate.getUrls());
                } else {
                    image.setImageResource(R.drawable.noimage);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            } else {
                image.setImageResource(R.drawable.noimage);
                progressBar.setVisibility(View.INVISIBLE);
            }


        return convertView;
    }


    int popupDelete = 0;
    private void callPopupDelete(final EstateUtil estate) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.show();

        SpinnerUtil.listStatuses(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if (pd != null) {
                    pd.dismiss();
                }
                final ArrayList<SpinnerUtil> list = (ArrayList<SpinnerUtil>) result;
                String[] strArray = new String[list.size()];
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Miért törli a hírdetést ?");
                for(int i = 0; i < list.size(); i++) {
                    strArray[i] = list.get(i).getName();
                }
                builder.setSingleChoiceItems(strArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popupDelete = list.get(which).getId();
                    }
                });
                builder.setNegativeButton("Mégsem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog pd = new ProgressDialog(getContext());
                        pd.show();

                        EstateUtil.deleteEstate(getContext(), new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                if(pd != null)
                                    pd.hide();
                                remove(estate);
                                notifyDataSetChanged();
                            }
                        }, estate.getId(), popupDelete);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        /*

        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.areyousure_popup, null);

        TextView textView = (TextView) popupView.findViewById(R.id.areyousure_delete_textView);
        textView.setText("Biztosan törli a hirdetés?");

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
                        final ProgressDialog pd = new ProgressDialog(getContext());
                        pd.show();

                        EstateUtil.deleteEstate(getContext(), new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                if(pd != null)
                                    pd.hide();
                                remove(estate);
                                notifyDataSetChanged();
                            }
                        }, estate.getId());
                    }
                });

        ((Button) popupView.findViewById(R.id.delete_ad_no_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });*/
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



    private class DownloadImageTask extends AsyncTask<String, Void, BitmapDrawable> {
        //ImageView bmImage;
        public int position;
        private WeakReference<ImageView> imageViewReference;
        private WeakReference<ProgressBar> progressBarWeakReference;

        public DownloadImageTask(ImageView bmImage, int position, ProgressBar pg) {
            //this.bmImage = bmImage;
            imageViewReference = new WeakReference<ImageView>(bmImage);
            progressBarWeakReference = new WeakReference<ProgressBar>(pg);
            this.position = position;
        }

        protected BitmapDrawable doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            BitmapDrawable drawable = null;
            HttpURLConnection connection = null;
            InputStream is = null;
            try {
                connection = (HttpURLConnection) new URL(urldisplay).openConnection();
                is = connection.getInputStream();
                mIcon11 = BitmapFactory.decodeStream(is, null, null);

                //final float scale = getContext().getResources().getDisplayMetrics().density;
                //int w = (int) (150 * scale + 0.5f);
                //int h = (int) (200 * scale + 0.5f);

                drawable = new BitmapDrawable(getContext().getResources(), mIcon11);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null)
                        connection.disconnect();
                    if (is != null)
                        is.close();
                } catch (Exception e) {

                }
            }
            return drawable;
        }

        protected void onPostExecute(BitmapDrawable result) {
            super.onPostExecute(result);
            if (isCancelled())
                result = null;

            if(Thread.interrupted()) {
                result = null;
            }

            if (imageList == null) {
                imageList = new ArrayList<>();
            }
            imageList.remove(this);

            if (imageViewReference != null && result != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageDrawable(result);
                }
            }
            if (progressBarWeakReference != null) {
                ProgressBar pg = progressBarWeakReference.get();
                if (pg != null) {
                    pg.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
