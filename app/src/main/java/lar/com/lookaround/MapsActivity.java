package lar.com.lookaround;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Térkép");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //setContentView(R.layout.teszt_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);


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

        String[] some_array = getResources().getStringArray(R.array.varosok_array);

        AutoCompleteTextView autocomplete = (AutoCompleteTextView)
                findViewById(R.id.maps_edittext_input);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, some_array);

        autocomplete.setThreshold(2);
        autocomplete.setAdapter(adapter);
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
                getMenuInflater().inflate(R.menu.maps, menu);
        return true;
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private final int id;

        public String getHash() {
            return hash;
        }

        private final String hash;

        public MyItem(double lat, double lng, int id, String hash) {
            mPosition = new LatLng(lat, lng);
            this.id = id;
            this.hash = hash;
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
                            arrayOfUsers.get(i).getId(),
                            arrayOfUsers.get(i).getHash());

                    mClusterManager.addItem(offsetItem);
                }
                if (Math.round(SettingUtil.getLatForMap(getBaseContext())) != 0 && Math.round(SettingUtil.getLngForMap(getBaseContext())) != 0) {
                    gotoLocation(SettingUtil.getLatForMap(getBaseContext()),SettingUtil.getLngForMap(getBaseContext()), 14);
                } else {
                    gotoLocation(arrayOfUsers.get(4).getLat(), arrayOfUsers.get(4).getLng(), 10);
                }
                //mapView.invalidate();
            }
        }, SettingUtil.getToken(getBaseContext()));
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
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",clickedClusterItem.getID());
                returnIntent.putExtra("hash", clickedClusterItem.getHash());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
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
                //final TextView tvDesc = (TextView) myContentsView.findViewById(R.id.item_realestate_description_maps);
                final ImageView imageView = (ImageView) myContentsView.findViewById(R.id.item_realestate_mainpic_maps);


                tvCity.setText("");
                tvStreet.setText("");
                tvSize.setText("");
                tvRooms.setText("");
                tvPrice.setText("");
                //tvDesc.setText("");
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

                            //tvDesc.setText(obj.getString("ingatlan_rovidleiras"));

                            myContentsView.invalidate();
                            Log.d("MAPS_TRY ", "finished");
                            marker.showInfoWindow();
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
            if (!list.isEmpty()) {
                Address add = list.get(0);
                double lat = add.getLatitude();
                double lng = add.getLongitude();
                gotoLocation(lat, lng, 12);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.maps_search_main_relative);
                linearLayout.setVisibility(View.GONE);
            } else {
                Snackbar.make(mapView, "Hibás cím!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        //MyItem offsetItem = new MyItem(lat, lng, 1);
        //mClusterManager.addItem(offsetItem);
        //gotoLocation(lat, lng, 10);
    }


    /*private void addItems() {

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
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_list:
                //startActivity(new Intent(MapsActivity.this, MainActivity.class));
                //finish();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
            case R.id.action_search_maps:
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.maps_search_main_relative);
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
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

    public void searchOnMap(View view) {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.maps_edittext_input);
        try {
            getLocate(autoCompleteTextView.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
                //startActivity(new Intent(MapsActivity.this, MainActivity.class));
                //finish();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
