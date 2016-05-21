package lar.com.lookaround;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.SettingUtil;

public class MapsActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private MapView mapView;

    ViewFlipper viewFlip;
    View maps_view, content_view;
    LayoutInflater inflater;

    private static final int MAPS = 0;
    private static final int CONTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        setContentView(R.layout.teszt_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        maps_view = inflater.inflate(R.layout.activity_maps, null);
        content_view = inflater.inflate(R.layout.content_realestate, null);


        viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperTest);
        viewFlip.addView(maps_view, MAPS);
        viewFlip.addView(content_view, CONTENT);


        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);



        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone_maps);
        fab_phone.setVisibility(View.INVISIBLE);
        fab_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:06304979787"));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {


                } else {
                    ActivityCompat.requestPermissions(
                            MapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            } else {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        setUpClusterer();
                        getItems();
                        //addItems();
                    }
                });
            }
        } else {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    setUpClusterer();
                    getItems();
                    //addItems();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (viewFlip.getDisplayedChild()) {
            case MAPS:
                getMenuInflater().inflate(R.menu.maps, menu);
                break;
            case CONTENT:
                getMenuInflater().inflate(R.menu.menu_realestate, menu);
                break;
        }
        return true;
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private final int id;

        public MyItem(double lat, double lng, int id) {
            mPosition = new LatLng(lat, lng);
            this.id = id;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public int getID(){
            return id;
        }
    }


    private void getItems() {
        EstateUtil.list_map_estates(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                final ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                for (int i = 0; i < arrayOfUsers.size(); i++) {
                    Log.d("MAPS_ID", String.valueOf(arrayOfUsers.get(i).getId()));
                    MyItem offsetItem = new MyItem(
                            arrayOfUsers.get(i).getLat(),
                            arrayOfUsers.get(i).getLng(),
                            arrayOfUsers.get(i).getId());

                    mClusterManager.addItem(offsetItem);
                }
                if (SettingUtil.getLatForMap(getBaseContext()) != null && SettingUtil.getLngForMap(getBaseContext()) != null && !SettingUtil.getLatForMap(getBaseContext()).equals("0.0") && !SettingUtil.getLngForMap(getBaseContext()).equals("0.0")) {
                    gotoLocation(Double.parseDouble(SettingUtil.getLatForMap(getBaseContext())),Double.parseDouble(SettingUtil.getLngForMap(getBaseContext())), 14);
                } else {
                    gotoLocation(arrayOfUsers.get(4).getLat(), arrayOfUsers.get(4).getLng(), 10);
                }
                //mapView.invalidate();
            }
        });
    }


    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
        Log.d("MAPS setUpClusterer ", "called");
        // Declare a variable for the cluster manager.


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);

        mClusterManager = new ClusterManager<>(this, mMap);

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(final Cluster<MyItem> cluster) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(mMap
                                        .getCameraPosition().zoom + 1)), 300,
                        null);
                return true;
            }
        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem clusterItem) {
                clickedClusterItem = clusterItem;

                Log.d("CLICKED_ITEM_ID ", String.valueOf(clusterItem.getID()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                clusterItem.getPosition(), (float) Math.floor(mMap
                                        .getCameraPosition().zoom + 1)), 300,
                        null);
                return false;
            }
        });

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //startActivity(new Intent(MapsActivity.this, MainActivity.class));
                //finish();
                switchLayoutTo(CONTENT);
                FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone_maps);
                fab_phone.setVisibility(View.VISIBLE);

            }
        });

    }

    Marker whichMarker;

    private MyItem clickedClusterItem;
    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private View myContentsView;
        private int lastItemId;

        MyCustomAdapterForItems( ) {
            myContentsView = getLayoutInflater().inflate(R.layout.maps_info_window, null);
            lastItemId = -1;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            if (lastItemId != clickedClusterItem.getID()) {
                lastItemId = clickedClusterItem.getID();
                whichMarker = marker;

                final TextView tvCity = (TextView) myContentsView.findViewById(R.id.item_realestate_adress1_maps);
                final TextView tvStreet = (TextView) myContentsView.findViewById(R.id.item_realestate_adress2_maps);
                final TextView tvSize = (TextView) myContentsView.findViewById(R.id.list_size_textView_maps);
                final TextView tvRooms = (TextView) myContentsView.findViewById(R.id.list_roomcount_textView_maps);
                final TextView tvPrice = (TextView) myContentsView.findViewById(R.id.price_maps);
                final TextView tvDesc = (TextView) myContentsView.findViewById(R.id.item_realestate_description_maps);
                final ImageView imageView = (ImageView) myContentsView.findViewById(R.id.item_realestate_mainpic_maps);


                final TextView price = (TextView) findViewById(R.id.item_realestate_price);
                final TextView title = (TextView) findViewById(R.id.item_realestate_needed_address);
                final TextView adress = (TextView) findViewById(R.id.item_realestate_optional_address);
                final TextView roomcount = (TextView) findViewById(R.id.roomcount_realestate_value);
                final TextView size = (TextView) findViewById(R.id.size_realestate_item_value);
                final TextView type = (TextView) findViewById(R.id.type_realestate_value);
                final TextView elevator = (TextView) findViewById(R.id.elevator_realestate_value);
                final TextView balcony = (TextView) findViewById(R.id.balcony_realestate_value);
                final TextView parking = (TextView) findViewById(R.id.parking_realestate_value);
                final TextView kilatas = (TextView) findViewById(R.id.view_realestate_value);
                final TextView condition = (TextView) findViewById(R.id.condition_realestate_value);
                final TextView floors = (TextView) findViewById(R.id.floors_realestate_value);
                final TextView heating = (TextView) findViewById(R.id.heating_realestate_value);
                final TextView ecertificate = (TextView) findViewById(R.id.energy_certificate_realestate_item_value);
                final TextView hasfurniture = (TextView) findViewById(R.id.hasfurniture_realestate_item_value);
                final TextView item_realestate_description_text = (TextView) findViewById(R.id.item_realestate_description_text);
                TextView seeonmap = (TextView) findViewById(R.id.iwantoseeonmaps_button);
                seeonmap.setVisibility(View.GONE);

                tvCity.setText("");
                tvStreet.setText("");
                tvSize.setText("");
                tvRooms.setText("");
                tvPrice.setText("");
                tvDesc.setText("");
                if (imageView != null && imageView.getDrawable() != null && ((BitmapDrawable) imageView.getDrawable()).getBitmap() != null) {
                    ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
                }
                imageView.setImageBitmap(null);

                EstateUtil.getEstate(new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        JSONObject obj = (JSONObject) result;
                        Log.d("MAPS_RESULT ", result.toString());

                        try {
                            String imageURL = "";
                            JSONArray kepekArray = new JSONArray(obj.getString("kepek"));
                            for (int j=0; j < kepekArray.length(); j++) {
                                JSONObject jsonKep = kepekArray.getJSONObject(j);
                                imageURL = jsonKep.getString("kepek_url");
                            }

                            if (kepekArray.length() != 0) {
                            final DownloadImageTask task = new DownloadImageTask(imageView, myContentsView);
                            imageList.add(task);
                            task.execute(imageURL);
                            }

                            tvCity.setText(obj.getString("ingatlan_varos"));
                            tvStreet.setText(obj.getString("ingatlan_utca"));
                            tvSize.setText(obj.getString("ingatlan_meret"));
                            tvRooms.setText(obj.getString("ingatlan_szsz"));

                            Locale locale = new Locale("en", "UK");

                            DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                            //symbols.setDecimalSeparator(';');
                            symbols.setGroupingSeparator('.');

                            String pattern = "###,###";
                            DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                            String format = decimalFormat.format(obj.getInt("ingatlan_ar"));

                            if (obj.getString("ing_e_type").equals("Eladó")) {
                                tvPrice.setText(format + " Ft");
                            } else {
                                tvPrice.setText(format + " Ft/hó");
                            }

                            tvDesc.setText(obj.getString("ingatlan_rovidleiras"));

                            myContentsView.invalidate();
                            Log.d("MAPS_TRY ", "finished");
                            marker.showInfoWindow();



                            isFavEstate = obj.getBoolean("kedvenc");

                            JSONArray kepekArrayFull = new JSONArray(obj.getString("kepek"));
                            List<String> imageUrls = new ArrayList<String>();
                            imageUrls.clear();
                            for (int j = 0; j < kepekArrayFull.length(); j++) {
                                JSONObject jsonKep = kepekArrayFull.getJSONObject(j);
                                //imageURL = jsonKep.getString("kepek_url");
                                imageUrls.add(jsonKep.getString("kepek_url"));
                            }

                            loadEstateImages(imageUrls);

                            title.setText(obj.getString("ingatlan_title"));
                            adress.setText(obj.getString("ingatlan_varos") + " " + obj.getString("ingatlan_utca"));
                            roomcount.setText(obj.getString("ingatlan_szsz"));
                            size.setText(obj.getString("ingatlan_meret"));
                            type.setText(obj.getString("ingatlan_tipus"));
                            item_realestate_description_text.setText(obj.getString("ingatlan_rovidleiras"));

                            if (obj.getString("ing_e_type").equals("Eladó")) {
                                price.setText(format + " Ft");
                            } else {
                                price.setText(format + " Ft/hó");
                            }

                            if (obj.getInt("ingatlan_lift") == 1) {
                                elevator.setText("Van");
                            } else {
                                elevator.setText("Nincs");
                            }

                            if (obj.getInt("ingatlan_erkely") == 1) {
                                balcony.setText("Van");
                            } else {
                                balcony.setText("Nincs");
                            }

                            parking.setText(obj.getString("ingatlan_parkolas"));
                            kilatas.setText(obj.getString("ingatlan_kilatas"));
                            condition.setText(obj.getString("ingatlan_allapot"));
                            floors.setText(obj.getString("ingatlan_emelet"));
                            heating.setText(obj.getString("ingatlan_futestipus"));
                            ecertificate.setText(obj.getString("ingatlan_energiatan"));

                            if (obj.getInt("ingatlan_butorozott") == 1) {
                                hasfurniture.setText("Nem");
                            } else if (obj.getInt("ingatlan_butorozott") == 2) {
                                hasfurniture.setText("Igen");
                            } else if (obj.getInt("ingatlan_butorozott") == 3) {
                                hasfurniture.setText("Alku tárgya");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, String.valueOf(clickedClusterItem.getID()), SettingUtil.getToken(getBaseContext()));
            }

            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return myContentsView;
        }
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);

    }


    public void getLocate(String adress) throws IOException {
        Geocoder gc = new Geocoder(this);

        List<Address> list = gc.getFromLocationName(adress, 1);
        Address add = list.get(0);

        double lat = add.getLatitude();
        double lng = add.getLongitude();


        MyItem offsetItem = new MyItem(lat, lng, 1);
        mClusterManager.addItem(offsetItem);
        //gotoLocation(lat, lng, 10);

    }


    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10000; i++) {
            double offset = i / 2000000d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng, 1);
            mClusterManager.addItem(offsetItem);
        }
    }


    MenuView.ItemView favItem;
    boolean isFavEstate = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_list:
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.action_fav:
                //TODO: kedvencekhez hozzáadást térképen tökéletesíteni...
                favItem = (MenuView.ItemView) findViewById(R.id.action_fav);
                Log.d("FAV ", "MAIN");

                if (!isFavEstate) {
                    Log.d("FAV ", "IF");
                    EstateUtil.setFavorite(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            Log.d("FAV RESULT", result.toString());
                            if (!(boolean)result) {
                                favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_filled));
                                isFavEstate = true;
                            }
                        }
                    },String.valueOf(clickedClusterItem.getID()), SettingUtil.getToken(MapsActivity.this), "1");
                } else {
                    EstateUtil.setFavorite(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            Log.d("FAV RESULT", result.toString());
                            if (!(boolean) result) {
                                favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_content));
                            }
                        }
                    },String.valueOf(clickedClusterItem.getID()), SettingUtil.getToken(MapsActivity.this), "0");
                }
                break;
            case R.id.action_calendar:
                //switchLayoutTo(BOOKING);
                break;
            case R.id.action_message:
                //switchLayoutTo(MESSAGES);
                break;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.advert_city)));
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private static ArrayList<DownloadImageTask> imageList= new ArrayList<>();



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        //ImageView bmImage;
        View convertView;
        private final WeakReference<ImageView> imageViewReference;



        public DownloadImageTask(ImageView bmImage, View convertView) {
            //this.bmImage = bmImage;
            imageViewReference = new WeakReference<ImageView>(bmImage);
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
                        whichMarker.showInfoWindow();
                    }
                }
            }
        }
    }




    @Override
    public void onBackPressed() {
        switch (viewFlip.getDisplayedChild()) {
            case MAPS:
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                finish();
                break;
            case CONTENT:
                FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone_maps);
                fab_phone.setVisibility(View.INVISIBLE);
                switchLayoutTo(MAPS);
                break;
        }
    }

    

    public void loadEstateImages(final List<String> urls) {
        final SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.slider);
        sliderLayout.removeAllSliders();
        for(int i = 0; i<urls.size();i ++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            defaultSliderView.image(urls.get(i))
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            if (sliderLayout.getScaleY() == 1) {
                                sliderLayout.setScaleY(3);
                                sliderLayout.setScaleX(3);
                                //sliderLayout.getCurrentSlider().setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                            } else {
                                sliderLayout.setScaleY(1);
                                sliderLayout.setScaleX(1);
                            }
                        }
                    });

            /*sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });*/

            sliderLayout.addSlider(defaultSliderView);

            sliderLayout.destroyDrawingCache();
        }
        urls.clear();
    }



    private int mCurrentLayoutState;
    public void switchLayoutTo(int switchTo){
        while(mCurrentLayoutState != switchTo){
            if(mCurrentLayoutState > switchTo){
                mCurrentLayoutState--;
                viewFlip.setInAnimation(inFromLeftAnimation());
                viewFlip.setOutAnimation(outToRightAnimation());
                viewFlip.setDisplayedChild(switchTo);
            } else {
                mCurrentLayoutState++;
                viewFlip.setInAnimation(inFromRightAnimation());
                viewFlip.setOutAnimation(outToLeftAnimation());
                viewFlip.setDisplayedChild(switchTo);
            }
        }
        supportInvalidateOptionsMenu();
    }

    protected Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(300);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    protected Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(300);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    protected Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(300);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    protected Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(300);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}
