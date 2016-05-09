package lar.com.lookaround;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        setUpClusterer();
    }

    private String mSnippet;
    private String mTitle;

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(double lat, double lng, String t, String s) {
            mPosition = new LatLng(lat, lng);
            mTitle = t;
            mSnippet = s;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public String getTitle(){
            return mTitle;
        }

        public String getSnippet(){
            return mSnippet;
        }


    }


    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                clusterItem.getPosition(), (float) Math.floor(mMap
                                        .getCameraPosition().zoom + 1)), 300,
                        null);
                return false;
            }
        });



        //mClusterManager.setOnClusterItemInfoWindowClickListener(this); //added


        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);


        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);




        // Add cluster items (markers) to the cluster manager.
        //addItems();

        for (int i = 0; i < 13; i++) {
            try {
                Log.d("ADD_CLUSTER ", "TRY");
                getLocate("Debrecen Kassai " + String.valueOf(i));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("CLUSTER_ERROR: ", e.toString());
            }
        }

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());

        /*for (int i = 0; i < 100; i++) {
            try {
                getLocate("Debrecen Böszörményi " + String.valueOf(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/


    }


    private MyItem clickedClusterItem;
    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(R.layout.maps_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.item_realestate_adress1_maps));
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.item_realestate_adress2_maps));

            tvTitle.setText(clickedClusterItem.getTitle());
            tvSnippet.setText(clickedClusterItem.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
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
        //String locality = add.getLocality();
        //Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lat = add.getLatitude();
        double lng = add.getLongitude();


        MyItem offsetItem = new MyItem(lat, lng, "LOFASZ", "LOFASZ2");
        mClusterManager.addItem(offsetItem);
        gotoLocation(lat, lng, 10);
        Log.d("CLUSTER: ", "ADDED");

    }






    /*@Override
    protected void onBeforeClusterItemRendered(MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        mImageView.setImageResource(person.profilePhoto);
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
    }*/




    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng, "LOFASZ", "LOFASZ2");
            mClusterManager.addItem(offsetItem);
        }
    }









    private void getCoordinates() {
        /*Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(<String address>, 1);
        if(addresses.size() > 0) {
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
        }*/

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName("Debrecen", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addresses.get(0);
        double longitude = address.getLongitude();
        double latitude = address.getLatitude();
        MyItem offsetItem = new MyItem(latitude, longitude, "asd", "asd2");
        mClusterManager.addItem(offsetItem);
    }
}
