package lar.com.lookaround;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.SettingUtil;

public class MapsActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private MapView mapView;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_maps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //switchLayoutTo(ADDESTATE);
                //setAddestatePageIndicator(whichAddestatePage);
                //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                fab.setVisibility(View.INVISIBLE);

            }
        });


        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {


            } else {
                ActivityCompat.requestPermissions(
                        MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
                gotoLocation(arrayOfUsers.get(4).getLat(),arrayOfUsers.get(4).getLng(), 30);
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
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                finish();
            }
        });

    }


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
            //TODO: megcsinálni h jókat kérjen le ez a fos ha rákattintok

            if (lastItemId != clickedClusterItem.getID()) {
                lastItemId = clickedClusterItem.getID();

                final TextView tvCity = ((TextView) myContentsView.findViewById(R.id.item_realestate_adress1_maps));
                final TextView tvStreet = ((TextView) myContentsView.findViewById(R.id.item_realestate_adress2_maps));
                final TextView tvSize = ((TextView) myContentsView.findViewById(R.id.list_size_textView_maps));
                final TextView tvRooms = ((TextView) myContentsView.findViewById(R.id.list_roomcount_textView_maps));
                EstateUtil.getEstate(new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        JSONObject obj = (JSONObject) result;
                        Log.d("MAPS_RESULT ", result.toString());

                        try {
                            Log.d("MAPS_TRY ", "called");

                            tvCity.setText(obj.getString("ingatlan_varos"));
                            tvStreet.setText(obj.getString("ingatlan_utca"));
                            tvSize.setText(obj.getString("ingatlan_meret"));
                            tvRooms.setText(obj.getString("ingatlan_szsz"));
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_list) {
            startActivity(new Intent(MapsActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
