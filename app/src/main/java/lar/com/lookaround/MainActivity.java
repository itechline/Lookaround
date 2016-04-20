package lar.com.lookaround;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import lar.com.lookaround.adapters.EstateAdapter;
import lar.com.lookaround.models.RealEstate;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.LoginUtil;
import lar.com.lookaround.util.SettingUtil;

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

    private SwipeRefreshLayout swipeContainer;

    private static int page = 0;
    private static String id;


    //keresés szűkítése spinnerek
    Spinner typeSpinner, localSpinner, minfloorSpinner, maxfloorSpinner, minroomsSpinner, maxroomsSpinner;
    Spinner elevatorSpinner, balconySpinner, iheightSpinner, bathroomwcSpinner, aircondiSpinner;
    Spinner gardenRSpinner, conditionSpinner, atticSpinner;
    ArrayAdapter<CharSequence> typespinnerAdapter, localSpinnerAdapter, minfloorSPAdapter, maxfloorSPAdapter, minroomsSPAdapter, maxroomsSPAdapter;
    ArrayAdapter<CharSequence> elevatorSpinnerAdapter, balconySpinnerAdapter, iheightSpinnerAdapter, bathroomwcSpinnerAdapter, aircondiSPAdapter;
    ArrayAdapter<CharSequence> gardenRSpinnerAdapter, conditionSpinnerAdapter, atticSpinnerAdapter;
        //hirdetés feladás spinnerek
    Spinner advertTypeSpinner;
    ArrayAdapter<CharSequence> advertTypeSPAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realestate);
        //addContentView(R.layout.activity_search);

        if(!SettingUtil.getToken(this).equals("")) {
            tokenValidation();
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

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
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
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

        // TODO: do it in XML
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // TODO: refactor to a method
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        estatesView = inflater.inflate(R.layout.content_main, null);
        contentRealestate = inflater.inflate(R.layout.content_realestate, null);
        addEstate = inflater.inflate(R.layout.content_addrealestate, null);

        viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.addView(estatesView, ESTATESLIST);
        viewFlip.addView(contentRealestate, CONTENTESTATE);
        viewFlip.addView(addEstate, ADDESTATE);

        viewFlip.setDisplayedChild(ESTATESLIST);

        //ViewPager viewPager = (ViewPager) findViewById(R.id.addrealestateViewpager);
        //viewPager.setAdapter(new CustomPagerAdapter(this));


        loadRealEstates("0", "0");

        loadEstateImages();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view_search);
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);

                //String lrgst = String.valueOf(EstateUtil.largestId);
                loadRealEstates("0", "0");
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        spinnerCreator();


    }


private int whichAddestatePage = 0;
    public void nextAddestatePage(View view) {
        whichAddestatePage += 1;
        setAddestatePage(whichAddestatePage);
    }

    public void prewAddestatePage(View view) {
        whichAddestatePage -= 1;
        setAddestatePage(whichAddestatePage);
    }

    public void setAddestatePage(int page) {
        RelativeLayout layone = (RelativeLayout) findViewById(R.id.relativeLayout_withcontent_page1);
        RelativeLayout laytwo = (RelativeLayout) findViewById(R.id.relativeLayout_withcontent_page2);
        RelativeLayout laythree = (RelativeLayout) findViewById(R.id.relativeLayout_withcontent_page3);
        RelativeLayout layfour = (RelativeLayout) findViewById(R.id.relativeLayout_withcontent_page4);
        RelativeLayout layfive = (RelativeLayout) findViewById(R.id.relativeLayout_withcontent_page5);

        layone.setVisibility(View.INVISIBLE);
        laytwo.setVisibility(View.INVISIBLE);
        laythree.setVisibility(View.INVISIBLE);
        layfour.setVisibility(View.INVISIBLE);
        layfive.setVisibility(View.INVISIBLE);

        switch (page) {
            case 0:
                layone.setVisibility(View.VISIBLE);
                break;
            case 1:
                laytwo.setVisibility(View.VISIBLE);
                break;
            case 2:
                laythree.setVisibility(View.VISIBLE);
                break;
            case 3:
                layfour.setVisibility(View.VISIBLE);
                break;
            case 4:
                layfive.setVisibility(View.VISIBLE);
                break;
        }


        //layone.setVisibility(View.VISIBLE);

    }


    public void tokenValidation() {
            launchRingDialog();
            LoginUtil.tokenValidator(this, new SoapObjectResult() {
                @Override
                public void parseRerult(Object result) {
                    if ((boolean) result) {
                        Log.d("RESULT: ", result.toString());
                        ringProgressDialog.dismiss();
                    } else {
                        Log.d("RESULT: ", result.toString());
                        ringProgressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }

                }
            }, SettingUtil.getToken(this));
    }

    ProgressDialog ringProgressDialog;
    public void launchRingDialog() {
        ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Logging In ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(10000);
                } catch (Exception e) {

                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

    }

    public void showAlert(View view) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("Rossz felhasználónév vagy jelszó!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText abc = (EditText)findViewById(R.id.keresztNev);
                        abc.getText().toString();
                        Log.d("DEBUG: ", abc.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setTitle("HIBA")
                .create();
        myAlert.show();
    }

    public void spinnerCreator() {
        typeSpinner = (Spinner) findViewById(R.id.realestate_type_spinner);
        typespinnerAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_type_spinner_array, android.R.layout.simple_spinner_item);
        typespinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typespinnerAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });
        localSpinner = (Spinner)findViewById(R.id.realestate_localisation_spinner);
        localSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_local_spinner_array, android.R.layout.simple_spinner_item);
        localSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localSpinner.setAdapter(localSpinnerAdapter);
        localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        minfloorSpinner = (Spinner)findViewById(R.id.realestate_floors_min_spinner);
        minfloorSPAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_floors_min_array, android.R.layout.simple_spinner_item);
        minfloorSPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minfloorSpinner.setAdapter(minfloorSPAdapter);
        minfloorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        maxfloorSpinner = (Spinner)findViewById(R.id.realestate_floors_max_spinner);
        maxfloorSPAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_floors_max_array, android.R.layout.simple_spinner_item);
        maxfloorSPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxfloorSpinner.setAdapter(maxfloorSPAdapter);
        maxfloorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        minroomsSpinner = (Spinner)findViewById(R.id.realestate_roomcount_min_spinner);
        minroomsSPAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_rooms_min_array, android.R.layout.simple_spinner_item);
        minroomsSPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minroomsSpinner.setAdapter(minroomsSPAdapter);
        minroomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        maxroomsSpinner = (Spinner)findViewById(R.id.realestate_roomcount_max_spinner);
        maxroomsSPAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_rooms_max_array, android.R.layout.simple_spinner_item);
        maxroomsSPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxroomsSpinner.setAdapter(maxroomsSPAdapter);
        maxroomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        elevatorSpinner = (Spinner)findViewById(R.id.realestate_elevator_spinner);
        elevatorSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_elevator_array, android.R.layout.simple_spinner_item);
        elevatorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elevatorSpinner.setAdapter(elevatorSpinnerAdapter);
        elevatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        balconySpinner = (Spinner)findViewById(R.id.realestate_balcony_spinner);
        balconySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_balcony_array, android.R.layout.simple_spinner_item);
        balconySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        balconySpinner.setAdapter(balconySpinnerAdapter);
        balconySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        iheightSpinner = (Spinner)findViewById(R.id.realestate_innerheight_spinner);
        iheightSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.realestate_innerheight_array, android.R.layout.simple_spinner_item);
        iheightSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iheightSpinner.setAdapter(iheightSpinnerAdapter);
        iheightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        bathroomwcSpinner = (Spinner)findViewById(R.id.bathroom_wc_realestate_spinner);
        bathroomwcSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.bathroom_wc_array, android.R.layout.simple_spinner_item);
        bathroomwcSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bathroomwcSpinner.setAdapter(bathroomwcSpinnerAdapter);
        bathroomwcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        aircondiSpinner = (Spinner)findViewById(R.id.aircondition_spinner);
        aircondiSPAdapter = ArrayAdapter.createFromResource(this, R.array.mindegy_van_nincs_array, android.R.layout.simple_spinner_item);
        aircondiSPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aircondiSpinner.setAdapter(aircondiSPAdapter);
        aircondiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        gardenRSpinner = (Spinner)findViewById(R.id.gardenrelation_spinner);
        gardenRSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.mindegy_van_nincs_array, android.R.layout.simple_spinner_item);
        gardenRSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gardenRSpinner.setAdapter(gardenRSpinnerAdapter);
        gardenRSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        conditionSpinner = (Spinner)findViewById(R.id.condition_spinner);
        conditionSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.condition_array, android.R.layout.simple_spinner_item);
        conditionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionSpinnerAdapter);
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        atticSpinner = (Spinner)findViewById(R.id.attic_spinner);
        atticSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.attic_array, android.R.layout.simple_spinner_item);
        atticSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        atticSpinner.setAdapter(atticSpinnerAdapter);
        atticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        advertTypeSpinner = (Spinner)findViewById(R.id.add_advert_type_spinner);
        advertTypeSPAdapter = ArrayAdapter.createFromResource(this, R.array.advert_tpye_array, android.R.layout.simple_spinner_item);
        advertTypeSPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        advertTypeSpinner.setAdapter(advertTypeSPAdapter);
        advertTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });
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

            //sliderLayout.addOnPageChangeListener();

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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
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

    private int pageCount = 0;
    private boolean isRefreshing = false;

    public void loadRealEstates(String idPost, String pagePost) {
        pageCount = 0;
        isRefreshing = false;
        EstateUtil.largestId = 0;
        EstateUtil.listEstates(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                final ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;

                //ArrayList<EstateUtil> arrayOfUsers = new ArrayList<EstateUtil>();


                // Create the adapter to convert the array to views
                final EstateAdapter adapter = new EstateAdapter(MainActivity.this, arrayOfUsers);
                // Attach the adapter to a ListView
                final ListView listView = (ListView) findViewById(R.id.estateListView);
                listView.setAdapter(adapter);
                listView.setDivider(null);
                listView.setDividerHeight(0);
                listView.setOnItemClickListener(new ItemList());
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        adapter.stopDownloadingImage(firstVisibleItem, firstVisibleItem + visibleItemCount);
                        if (firstVisibleItem + visibleItemCount == totalItemCount) {
                            if (!isRefreshing) {
                                Log.e("REFRESHING", "PAGE 1");

                                isRefreshing = true;
                                pageCount += 1;

                                String pageStr = String.valueOf(pageCount);
                                String lrgst = String.valueOf(EstateUtil.largestId);
                                EstateUtil.listEstates(new SoapObjectResult() {
                                    @Override
                                    public void parseRerult(Object result) {
                                        ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                                        adapter.addAll(arrayOfUsers);
                                        isRefreshing = false;
                                    }
                                }, lrgst, pageStr);
                            }
                        }
                    }
                });
                swipeContainer.setRefreshing(false);
            }
        }, idPost, pagePost);


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
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                switchLayoutTo(ESTATESLIST);
                //loadRealEstates("0", "0");
                break;
            case ADDESTATE:
                if (prewView == CONTENTESTATE) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                }
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
        }

        supportInvalidateOptionsMenu();
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
            switch (viewFlip.getDisplayedChild()) {
                case ESTATESLIST:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                    break;
                case CONTENTESTATE:
                    //findViewById(R.id.estateListView).invalidate();
                    supportInvalidateOptionsMenu();
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                    switchLayoutTo(ESTATESLIST);
                    //loadRealEstates("0", "0");
                    break;
                case ADDESTATE:
                    if (prewView == CONTENTESTATE) {
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                    } else {
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                    }
                    switchLayoutTo(prewView);
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setVisibility(View.VISIBLE);
                    break;
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
                LoginUtil.logout(this, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        if ((boolean) result) {

                        } else {

                        }
                    }
                });
                SettingUtil.setToken(this, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                //finish();
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
        }
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
