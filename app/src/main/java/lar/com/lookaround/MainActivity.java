package lar.com.lookaround;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

import lar.com.lookaround.adapters.EstateAdapter;
import lar.com.lookaround.models.RealEstate;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewFlipper viewFlip;
    LayoutInflater inflater;
    private int mCurrentLayoutState;
    private int prewView;

    private static final int ESTATESLIST = 0;
    private static final int CONTENTESTATE = 1;
    private static final int ADDESTATE = 2;

    View estatesView, contentRealestate, addEstate;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realestate);
        //addContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                prewView = viewFlip.getDisplayedChild();
                switchLayoutTo(ADDESTATE);
                fab.setVisibility(View.INVISIBLE);

            }
        });

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        estatesView = inflater.inflate(R.layout.content_main, null);
        contentRealestate = inflater.inflate(R.layout.content_realestate, null);
        addEstate = inflater.inflate(R.layout.content_addrealestate, null);

        viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.addView(estatesView, ESTATESLIST);
        viewFlip.addView(contentRealestate, CONTENTESTATE);
        viewFlip.addView(addEstate, ADDESTATE);

        viewFlip.setDisplayedChild(ESTATESLIST);
        loadRealEstates();

        loadEstateImages();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view_search);
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);


    }

    public void loadEstateImages() {
        final SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.slider);

        //final List<String> urls = slideImageURLLists();

        final List<String> urls = new ArrayList<String>();
        urls.add("https://s-media-cache-ak0.pinimg.com/736x/e7/f2/81/e7f2812089086a6e9e7e6408457c76c4.jpg");
        urls.add("https://scontent.fomr1-1.fna.fbcdn.net/hphotos-xfp1/v/t1.0-9/10399388_1037153376364726_5568922816957393250_n.jpg?oh=6c8027e95134a0fc5310ba3e0847372d&oe=577B8A7D");
        urls.add("https://scontent.fomr1-1.fna.fbcdn.net/v/t1.0-9/10500443_784808998244322_3120390074428787735_n.jpg?oh=f5ca0004521550b3146dff315a43f37d&oe=5779D2CB");

        for(int i = 0; i<urls.size();i ++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            final int finalI = i;
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            defaultSliderView.image(urls.get(i))
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Log.d("CLICKED ON: ", urls.get(finalI));
                            /*if(sliderLayout.getScaleX() == 1) {
                                sliderLayout.setScaleY(0.5f);
                                sliderLayout.setScaleX(0.5f);
                            } else {
                                sliderLayout.setScaleY(1);
                                sliderLayout.setScaleX(1);
                            }*/
                        }
                    });

            sliderLayout.addSlider(defaultSliderView);

            //sliderLayout.destroyDrawingCache();
        }

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
            loadRealEstateContent(view);
            supportInvalidateOptionsMenu();
        }
    }


    public void loadRealEstateContent(View view) {
        findViewById(R.id.scrollView2).scrollTo(0, 0);

        ViewGroup viewg=(ViewGroup)view;
        //TextView t=(TextView)viewg.findViewById(R.id.item_realestate_description);
        //Log.d("DEBUG: ", t.getText().toString());
        TextView felh_szoveg = (TextView)findViewById(R.id.item_realestate_description_text);

        felh_szoveg.setText(Html.fromHtml("<br><br><br>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec dictum sit amet urna vulputate sagittis. Nulla vulputate lacus ac velit congue tincidunt. Fusce viverra mi nec sodales convallis. Integer porta elit ipsum, eget consequat mi suscipit in. In varius velit et est suscipit commodo. Sed vitae malesuada nisl. Cras vestibulum consectetur tortor, quis rutrum urna iaculis gravida. Proin rhoncus lectus aliquet, luctus massa vel, fermentum ipsum. Proin vitae magna a justo viverra egestas in sed ex. Donec pretium elit arcu, et cursus ligula lacinia in. Sed fermentum facilisis magna, eget viverra nisi maximus sit amet." +
                "<br><br>Integer quis massa non mi elementum posuere. Sed vestibulum enim nec bibendum sodales. Aenean pretium eleifend orci, ut laoreet diam sodales ac. Maecenas eu suscipit ante. Pellentesque dignissim tincidunt dolor at gravida. Cras sit amet elementum urna, et gravida leo. Nunc eleifend vel mauris non semper. Mauris in urna sed elit aliquet lacinia at et nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean maximus risus leo, non consectetur nisi mollis non. Suspendisse fringilla ipsum ac tempor mollis."));

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
                //findViewById(R.id.estateListView).invalidate();

                switchLayoutTo(ESTATESLIST);
                loadRealEstates();
                break;
            case ADDESTATE:
                switchLayoutTo(prewView);
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);
                break;
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
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
        if (id == R.id.action_search) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }
        if (id == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:

                break;
            case R.id.nav_messages:

                break;
            case R.id.nav_billing:

                break;
            case R.id.nav_myads:

                break;
            case R.id.nav_myfavs:

                break;
            case R.id.nav_admonitor:

                break;
            case R.id.nav_agency:

                break;
            case R.id.nav_invitation:

                break;
            case R.id.nav_logout:
                //finishActivity();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
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
