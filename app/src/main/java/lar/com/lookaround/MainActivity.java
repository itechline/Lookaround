package lar.com.lookaround;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import lar.com.lookaround.adapters.EstateAdapter;
import lar.com.lookaround.models.RealEstate;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewFlipper viewFlip;
    LayoutInflater inflater;
    private int mCurrentLayoutState;

    private static final int ESTATESLIST = 0;
    private static final int CONTENTESTATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realestate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View estatesView, contentRealestate;
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        estatesView = inflater.inflate(R.layout.content_main, null);
        contentRealestate = inflater.inflate(R.layout.content_realestate, null);

        viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.addView(estatesView, ESTATESLIST);
        viewFlip.addView(contentRealestate, CONTENTESTATE);

        viewFlip.setDisplayedChild(ESTATESLIST);

        loadRealEstates();


    }

    private class ItemList implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewGroup viewg=(ViewGroup)view;
            TextView textv=(TextView)viewg.findViewById(R.id.item_realestate_adress1);
            //EditText abc = (EditText)findViewById(R.id.keresztNev);
            //abc.getText().toString();
            //viewFlip.setDisplayedChild(CONTENTESTATE);
            switchLayoutTo(CONTENTESTATE);
            Log.d("DEBUG: ", textv.getText().toString());
            supportInvalidateOptionsMenu();
        }
    }


    public void loadRealEstateContent() {


    }


    public void loadRealEstates() {
        /*ListView listView = (ListView) findViewById(R.id.estateListView);
        ArrayList<RealEstate> arrayOfEstates = new ArrayList<RealEstate>();
        EstateAdapter abc = new EstateAdapter(this,arrayOfEstates);
        listView.setAdapter(abc);*/

        ArrayList<RealEstate> arrayOfUsers = RealEstate.getUsers();
        // Create the adapter to convert the array to views
        EstateAdapter adapter = new EstateAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        final ListView listView = (ListView) findViewById(R.id.estateListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemList());
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
        switch (viewFlip.getDisplayedChild()) {
            case ESTATESLIST:

                break;
            case CONTENTESTATE:
                switchLayoutTo(ESTATESLIST);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*if(viewFlip.getDisplayedChild() == ESTATESLIST) {
            getMenuInflater().inflate(R.menu.main, menu);
        }*/

        switch (viewFlip.getDisplayedChild()) {
            case ESTATESLIST:
                getMenuInflater().inflate(R.menu.main, menu);
                break;
            case CONTENTESTATE:
                getMenuInflater().inflate(R.menu.menu_realestate, menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






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
        };
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
