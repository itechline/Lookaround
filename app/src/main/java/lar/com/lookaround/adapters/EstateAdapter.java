package lar.com.lookaround.adapters;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import lar.com.lookaround.MainActivity;
import lar.com.lookaround.R;
import lar.com.lookaround.models.RealEstate;

public class EstateAdapter extends ArrayAdapter<RealEstate> {

    public EstateAdapter(Context context, ArrayList<RealEstate> users) {
        super(context, 0, users);
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

        //Log.e("KURVAFASZA", "1");

        // Get the data item for this position
        RealEstate estate = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_realestate, parent, false);
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
        TextView price = (TextView) convertView.findViewById(R.id.Price);
        CheckBox fav = (CheckBox) convertView.findViewById(R.id.item_realestate_isfavourite);
        ImageView image = (ImageView) convertView.findViewById(R.id.item_realestate_mainpic);


        adress.setText(estate.getAdress());
        street.setText(estate.getStreet());
        description.setText(estate.getDescription());
        price.setText(estate.getPrice());
        fav.setChecked(estate.isFavourite());

        //DownloadImageTask imageTask = new DownloadImageTask(image, estate.getId(), convertView);
        ////imageTask.cancel(true);
        //imageTask.execute(estate.getUrls());

        final DownloadImageTask task = new DownloadImageTask(image, position, convertView);
        //final AsyncDrawable asyncDrawable = new AsyncDrawable(task);

        imageList.add(task);

        //image.setImageDrawable(asyncDrawable);
        task.execute(estate.getUrls());


        return convertView;
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
            Log.e("LOSHIT", "darabszám: "+imageList.size());

            if (result != null) {
                //bmImage.setImageBitmap(result);
                //bmImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (imageViewReference != null && result != null) {
                    final ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setImageBitmap(result);
                    }
                }

                ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.item_realestate_progressbar);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<DownloadImageTask> bitmapWorkerTaskReference;

        public AsyncDrawable(DownloadImageTask bitmapWorkerTask) {
            //super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<DownloadImageTask>(bitmapWorkerTask);
        }

        public DownloadImageTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private static DownloadImageTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }












    /*public void loadEstateImages(View view, String url) {
        SliderLayout sliderLayout = (SliderLayout) view.findViewById(R.id.sliderSmall);



        DefaultSliderView defaultSliderView = new DefaultSliderView(view.getContext());

        defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
        defaultSliderView.image(url)
                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        //Log.d("CLICKED ON: ", SmallURLS.get(finalI));

                    }
                });
        sliderLayout.stopAutoCycle();
        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        sliderLayout.removeAllSliders();
        sliderLayout.addSlider(defaultSliderView);


    }*/


}
