package lar.com.lookaround;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Attila_Dan on 16. 07. 06..
 */
public class ViewPagerFragment extends Fragment {

    private static final String BUNDLE_ASSET = "asset";

    private String asset;

    public ViewPagerFragment() {
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_zoom, container, false);

        if (savedInstanceState != null) {
            if (asset == null && savedInstanceState.containsKey(BUNDLE_ASSET)) {
                asset = savedInstanceState.getString(BUNDLE_ASSET);
            }
        }
        if (asset != null) {
            SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)rootView.findViewById(R.id.imageView_zoom);
            //imageView.setImage(ImageSource.asset(asset));
                            final DownloadImage task = new DownloadImage(imageView);
                                task.execute(asset);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View rootView = getView();
        if (rootView != null) {
            outState.putString(BUNDLE_ASSET, asset);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, BitmapDrawable> {
        SubsamplingScaleImageView bmImage;

        public DownloadImage(SubsamplingScaleImageView bmImage) {
            this.bmImage = bmImage;
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


            if (bmImage != null && result != null) {
                SubsamplingScaleImageView imageView = bmImage;
                if (imageView != null) {
                    //imageView.setImageDrawable(result);
                    imageView.setImage(ImageSource.bitmap(result.getBitmap()));
                }
            }
        }
    }

}
