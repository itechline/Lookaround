package lar.com.lookaround;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import lar.com.lookaround.adapters.CalendarAdapter;
import lar.com.lookaround.adapters.EstateAdapter;
import lar.com.lookaround.adapters.SpinnerAdapter;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.LoginUtil;
import lar.com.lookaround.util.ScalingUtilities;
import lar.com.lookaround.util.SettingUtil;
import lar.com.lookaround.util.SpinnerUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ViewFlipper viewFlip, viewFlipAddEstate;
    LayoutInflater inflater;
    private int mCurrentLayoutState;
    private int mCurrentLayoutStateAddEstate;
    private int prewView;

    private static final int ESTATESLIST = 0;
    private static final int CONTENTESTATE = 1;
    private static final int ADDESTATE = 2;

    private static final int INVITE = 3;
    private static final int PROFILE = 4;
    private static final int MESSAGES = 5;
    private static final int BOOKING = 6;

    View estatesView, contentRealestate, addEstate, addEstate2, addEstate3, addEstate4, addEstate5, addEstate1, invite, profile, messages, booking;

    DrawerLayout drawer;

    private SwipeRefreshLayout swipeContainer;

    private static int page = 0;
    private static String id;

    private boolean isShowingFavorites = false;


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
        Debug.getNativeHeapSize();
        //addContentView(R.layout.activity_search);

        setViewFlipper();
        setViewFlipperTwo();

        if (isNetworkAvailable()) {
            if (!SettingUtil.getToken(this).equals("")) {
                tokenValidation();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        } else {
            showAlert();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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
                setAddestatePageIndicator(whichAddestatePage);
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

        //loadEstateImages();

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
                if (!isShowingFavorites) {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0");
                } else {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1");
                }
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        loadSearchSpinners();


        loadAddEstateSpinners();

        Calendar now = Calendar.getInstance();

        setCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH));

    }




    Calendar most = Calendar.getInstance();
    int whichYear = most.get(Calendar.YEAR);
    int whichMonth = most.get(Calendar.MONTH);
    int monthSetter = 0;


    public void nextMonth(View view) {
        whichMonth += 1;
        monthSetter += 1;

        setCalendar(whichYear, whichMonth);

    }

    public void prewMonth(View view) {
        whichMonth -= 1;
        monthSetter -= 1;

        setCalendar(whichYear, whichMonth);

    }


    public void setCalendar(int year, int month) {
        final ArrayList<String> lst = new ArrayList<String>();

        Calendar hlper = Calendar.getInstance();
        hlper.set(Calendar.DAY_OF_MONTH, 1);
        hlper.set(Calendar.MONTH, month);
        hlper.set(Calendar.YEAR, year);

        int dow = hlper.get(Calendar.DAY_OF_WEEK);
        if(dow == Calendar.MONDAY) {
            dow = 0;
        } else if(dow == Calendar.TUESDAY) {
            dow = 1;
        } else if(dow == Calendar.WEDNESDAY) {
            dow = 2;
        } else if(dow == Calendar.THURSDAY) {
            dow = 3;
        } else if(dow == Calendar.FRIDAY) {
            dow = 4;
        } else if(dow == Calendar.SATURDAY) {
            dow = 5;
        } else if(dow == Calendar.SUNDAY) {
            dow = 6;
        }
        int day = 1;
        int maxday = hlper.getActualMaximum(Calendar.DAY_OF_MONTH);

        //for (int i = 0; i < 7; i++) {
        //    lst.add(weekdays[i]);
        //}

        for (int i = 0; i < maxday + dow; i++) {
            if(dow > i) {
                lst.add("");
            } else {
                lst.add(String.valueOf(day));
                day++;
            }
        }



        int thisMonth = hlper.get(Calendar.MONTH);
        int thisYear = hlper.get(Calendar.YEAR);

        TextView month_o_year = (TextView) findViewById(R.id.current_date_textView);
        month_o_year.setText(getMonth(month));


        CalendarAdapter calendarAdapter = new CalendarAdapter(MainActivity.this, lst, thisYear , thisMonth);

        final GridView gridview = (GridView) findViewById(R.id.booking_calendar);
        gridview.setAdapter(calendarAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                CalendarAdapter adapter = (CalendarAdapter)parent.getAdapter();

                for (int i=0; i < gridview.getChildCount();i++) {
                    gridview.getChildAt(i).setBackground(null);
                }
                gridview.getChildAt(position).setBackgroundResource(R.drawable.b_d_border);
                gridview.getChildAt(position).setHovered(true);

                //Toast.makeText(MainActivity.this, "" + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


    Spinner hirdetesSpinner, szobaszamSpinner, allapotSpinner, emeletekSpinner, ingatlanTipusSpinner, parkolasSpinner, futesSpinner;
    Spinner energiaSpinner, kilatasSpinner;

    public void loadAddEstateSpinners() {
        //add_advert_type_spinner
        SpinnerUtil.get_list_hirdetestipusa(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                hirdetesSpinner = (Spinner) findViewById(R.id.add_advert_type_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hirdetesSpinner.setAdapter(adapter);

                hirdetesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });


        //addestate_rooms_spinner
        SpinnerUtil.get_list_ingatlanszoba(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                szobaszamSpinner = (Spinner) findViewById(R.id.addestate_rooms_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                szobaszamSpinner.setAdapter(adapter);

                szobaszamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });



        //addestate_condition_spinner
        SpinnerUtil.get_list_ingatlanallapota(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                allapotSpinner = (Spinner) findViewById(R.id.addestate_condition_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                allapotSpinner.setAdapter(adapter);

                allapotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });




        //addestate_floors_spinner
        SpinnerUtil.get_list_ingatlanemelet(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                emeletekSpinner = (Spinner) findViewById(R.id.addestate_floors_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                emeletekSpinner.setAdapter(adapter);

                emeletekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });



        //addestate_type_spinner
        SpinnerUtil.get_list_ingatlantipus(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                ingatlanTipusSpinner = (Spinner) findViewById(R.id.addestate_type_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ingatlanTipusSpinner.setAdapter(adapter);

                ingatlanTipusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });




        //addestate_parking_spinner
        SpinnerUtil.get_list_ingatlanparkolas(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                parkolasSpinner = (Spinner) findViewById(R.id.addestate_parking_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parkolasSpinner.setAdapter(adapter);

                parkolasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });



        //addestate_heatingtype_spinner
        SpinnerUtil.get_list_ingatlanfutes(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                futesSpinner = (Spinner) findViewById(R.id.addestate_heatingtype_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                futesSpinner.setAdapter(adapter);

                futesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });




        //addestate_ecertificate_spinner
        SpinnerUtil.get_list_ingatlanenergia(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                energiaSpinner = (Spinner) findViewById(R.id.addestate_ecertificate_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                energiaSpinner.setAdapter(adapter);

                energiaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });



        //adestate_view_spinner
        SpinnerUtil.get_list_ingatlankilatas(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                kilatasSpinner = (Spinner) findViewById(R.id.adestate_view_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kilatasSpinner.setAdapter(adapter);

                kilatasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

    }


    public void loadSearchSpinners() {
        SpinnerUtil.get_list_hirdetestipusa(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                typeSpinner = (Spinner) findViewById(R.id.realestate_type_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpinner.setAdapter(adapter);

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

            }
        });

        SpinnerUtil.get_list_ingatlanallapota(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                localSpinner = (Spinner) findViewById(R.id.realestate_localisation_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                localSpinner.setAdapter(adapter);

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

            }
        });
    }


    public void setViewFlipper() {
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        estatesView = inflater.inflate(R.layout.content_main, null);
        contentRealestate = inflater.inflate(R.layout.content_realestate, null);
        invite = inflater.inflate(R.layout.content_invite, null);
        profile = inflater.inflate(R.layout.content_profile, null);
        messages = inflater.inflate(R.layout.content_messages, null);
        booking = inflater.inflate(R.layout.content_booking, null);



        addEstate = inflater.inflate(R.layout.content_addrealestate, null);

        addEstate1 = inflater.inflate(R.layout.content_addrealestate_page1, null);
        addEstate2 = inflater.inflate(R.layout.content_addrealestate_page2, null);
        addEstate3 = inflater.inflate(R.layout.content_addrealestate_page3, null);
        addEstate4 = inflater.inflate(R.layout.content_addrealestate_page4, null);
        addEstate5 = inflater.inflate(R.layout.content_addrealestate_page5, null);



        viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.addView(estatesView, ESTATESLIST);



        viewFlip.setDisplayedChild(ESTATESLIST);
    }

    public void setViewFlipperTwo() {
        viewFlip.addView(contentRealestate, CONTENTESTATE);
        viewFlip.addView(addEstate, ADDESTATE);
        viewFlip.addView(invite, INVITE);
        viewFlip.addView(profile, PROFILE);
        viewFlip.addView(messages, MESSAGES);
        viewFlip.addView(booking, BOOKING);

        viewFlipAddEstate = (ViewFlipper) findViewById(R.id.viewFlipperAddEstate);

        viewFlipAddEstate.addView(addEstate1, 0);
        viewFlipAddEstate.addView(addEstate2, 1);
        viewFlipAddEstate.addView(addEstate3, 2);
        viewFlipAddEstate.addView(addEstate4, 3);
        viewFlipAddEstate.addView(addEstate5, 4);
    }



    private int PICK_IMAGE_REQUEST = 1;
    private int TAKE_IMAGE_REQUEST = 0;

    public void pickImage(View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void TakeImage(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        TAKE_IMAGE_REQUEST);

            }
        } else {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
        }
    }

    private static boolean isDay1 = false;
    private static boolean isDay2 = false;
    private static boolean isDay3 = false;
    private static boolean isDay4 = false;
    private static boolean isDay5 = false;
    private static boolean isDay6 = false;
    private static boolean isDay7 = false;

    public void daypicker1(View view) {
        TextView day1text = (TextView) findViewById(R.id.daypicker_day1);
        if (isDay1) {
            day1text.setTextColor(Color.parseColor("#000000"));
            day1text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay1 = false;
        } else {
            day1text.setTextColor(Color.parseColor("#FFFFFF"));
            day1text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay1 = true;
        }
    }

    public void daypicker2(View view) {
        TextView day2text = (TextView) findViewById(R.id.daypicker_day2);
        if (isDay2) {
            day2text.setTextColor(Color.parseColor("#000000"));
            day2text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay2 = false;
        } else {
            day2text.setTextColor(Color.parseColor("#FFFFFF"));
            day2text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay2 = true;
        }
    }

    public void daypicker3(View view) {
        TextView day3text = (TextView) findViewById(R.id.daypicker_day3);
        if (isDay3) {
            day3text.setTextColor(Color.parseColor("#000000"));
            day3text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay3 = false;
        } else {
            day3text.setTextColor(Color.parseColor("#FFFFFF"));
            day3text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay3 = true;
        }
    }

    public void daypicker4(View view) {
        TextView day4text = (TextView) findViewById(R.id.daypicker_day4);
        if (isDay4) {
            day4text.setTextColor(Color.parseColor("#000000"));
            day4text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay4 = false;
        } else {
            day4text.setTextColor(Color.parseColor("#FFFFFF"));
            day4text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay4 = true;
        }
    }

    public void daypicker5(View view) {
        TextView day5text = (TextView) findViewById(R.id.daypicker_day5);
        if (isDay5) {
            day5text.setTextColor(Color.parseColor("#000000"));
            day5text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay5 = false;
        } else {
            day5text.setTextColor(Color.parseColor("#FFFFFF"));
            day5text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay5 = true;
        }
    }

    public void daypicker6(View view) {
        TextView day6text = (TextView) findViewById(R.id.daypicker_day6);
        if (isDay6) {
            day6text.setTextColor(Color.parseColor("#000000"));
            day6text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay6 = false;
        } else {
            day6text.setTextColor(Color.parseColor("#FFFFFF"));
            day6text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay6 = true;
        }
    }

    public void daypicker7(View view) {
        TextView day7text = (TextView) findViewById(R.id.daypicker_day7);
        if (isDay7) {
            day7text.setTextColor(Color.parseColor("#000000"));
            day7text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay7 = false;
        } else {
            day7text.setTextColor(Color.parseColor("#FFFFFF"));
            day7text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay7 = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //ScalingUtilities.createScaledBitmap(bitmap, 500, 500, ScalingUtilities.ScalingLogic.FIT);

                ImageView imageView = (ImageView) findViewById(R.id.upload_image_imageview);
                //imageView.setImageBitmap(bitmap);
                imageView.setImageBitmap(ScalingUtilities.createScaledBitmap(bitmap, 200, 200, ScalingUtilities.ScalingLogic.FIT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            //RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.upload_camera_rlayout);
            //View  itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_realestate,null, false);
            //myLayout.addView(itemView);

            //itemView.findViewById(R.id.item_realestate_mainpic);


            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.add_campic_imageview);
            imageView.setImageBitmap(ScalingUtilities.createScaledBitmap(bitmap, 200, 200, ScalingUtilities.ScalingLogic.FIT));
        }
    }

    static final int DIALOG_ID = 0;
    static final int DIALOGEND_ID = 1;
    int hour_x;
    int minute_x;
    int hour_x_end;
    int minute_x_end;

    public void showTimePicker(View view) {
        showDialog(DIALOG_ID);
    }

    public void showTimePickerEnd(View view) {
        showDialog(DIALOGEND_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new TimePickerDialog(MainActivity.this, kTimpePickerDialog, hour_x, minute_x, true);
        } else if (id == DIALOGEND_ID) {
            return new TimePickerDialog(MainActivity.this, kTimePickerEndDialog, hour_x_end, minute_x_end, true);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimpePickerDialog =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x = hourOfDay;
                    minute_x = minute;
                }
    };

    protected TimePickerDialog.OnTimeSetListener kTimePickerEndDialog =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x_end = hourOfDay;
                    minute_x_end = minute;
                }
            };


    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;


        /*ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) {

            return true;

        }
        else if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            return false;
        }

        return true;*/

    }


    public String estateTitle;
    public String estateDescription;
    public int advertType;
    public String estatePrice;
    public String estateCity;
    public String estateStreet;
    public String estetaHouseNumber;
    public String estateSize;







    private boolean isValidString(String string) {
        if (string != null && string.length() != 0) {
            return true;
        }
        return false;
    }

private int whichAddestatePage = 0;

    public void nextAddestatePage(View view) {
        if (whichAddestatePage < 4) {
            whichAddestatePage += 1;
            boolean isFilledOut = true;
            switch (whichAddestatePage) {
                case 1:
                    TextView title = (TextView) findViewById(R.id.adverttitle_edittext);
                    TextView description = (TextView) findViewById(R.id.advert_description_edittext);
                    TextView price = (TextView) findViewById(R.id.add_advert_price_edittext);
                    TextView city = (TextView) findViewById(R.id.add_advert_city_edittext);

                    estateTitle = title.getText().toString();
                    estateDescription = description.getText().toString();
                    estatePrice = price.getText().toString();
                    estateCity = city.getText().toString();

                    if(!isValidString(estateTitle)) {
                        title.setError("Hiba!");
                        title.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateDescription)) {
                        description.setError("Hiba!");
                        description.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estatePrice)) {
                        price.setError("Hiba!");
                        price.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateCity)) {
                        city.setError("Hiba!");
                        city.invalidate();
                        isFilledOut = false;
                    }


                    if (isFilledOut) {
                        setAddestatePageIndicator(whichAddestatePage);
                        switchLayoutToAddEstate(whichAddestatePage);
                    } else {
                        whichAddestatePage = 0;
                    }

                    break;
            }





        }
    }

    public void prewAddestatePage(View view) {
        if (whichAddestatePage > 0) {
            whichAddestatePage -= 1;
            setAddestatePageIndicator(whichAddestatePage);
            switchLayoutToAddEstate(whichAddestatePage);
        }
    }

    public void setAddestatePageIndicator(int page) {

        ImageView layoneIndicator = (ImageView) findViewById(R.id.image_step1);
        ImageView laytwoIndicator = (ImageView) findViewById(R.id.image_step2);
        ImageView laythreendicator = (ImageView) findViewById(R.id.image_step3);
        ImageView layfourIndicator = (ImageView) findViewById(R.id.image_step4);
        ImageView layfiveIndicator = (ImageView) findViewById(R.id.image_step5);

        Resources res = getResources();
        Drawable kekpotty = res.getDrawable(R.drawable.kekpotty);
        Drawable szurkepotty = res.getDrawable(R.drawable.szurkepotty);

        layoneIndicator.setImageDrawable(szurkepotty);
        laytwoIndicator.setImageDrawable(szurkepotty);
        laythreendicator.setImageDrawable(szurkepotty);
        layfourIndicator.setImageDrawable(szurkepotty);
        layfiveIndicator.setImageDrawable(szurkepotty);

        switch (page) {
            case 0:
                layoneIndicator.setImageDrawable(kekpotty);
                break;
            case 1:
                laytwoIndicator.setImageDrawable(kekpotty);
                break;
            case 2:
                laythreendicator.setImageDrawable(kekpotty);
                break;
            case 3:
                layfourIndicator.setImageDrawable(kekpotty);
                break;
            case 4:
                layfiveIndicator.setImageDrawable(kekpotty);
                break;
        }
    }


    public void tokenValidation() {
        //launchRingDialog();
        LoginUtil.tokenValidator(this, new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if ((boolean) result) {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0");
                    //ringProgressDialog.dismiss();
                    Log.d("RESULT: ", result.toString());

                } else {
                    //ringProgressDialog.dismiss();
                    Log.d("RESULT: ", result.toString());

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
        if (isNetworkAvailable()) {
            if (!SettingUtil.getToken(this).equals("")) {
                tokenValidation();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        } else {
            showAlert();
        }

    }

    public void showAlert() {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("Napasztmek! Nincs internet! :'(")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
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

        iheightSpinner = (Spinner)findViewById(R.id.realestate_size_spinner);
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

        bathroomwcSpinner = (Spinner)findViewById(R.id.view_type_realestate_spinner);
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

        aircondiSpinner = (Spinner)findViewById(R.id.hasfurniture_spinner);
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

        gardenRSpinner = (Spinner)findViewById(R.id.parking_type_spinner);
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



    /*public void loadEstateImages() {
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
                            if(sliderLayout.getScaleX() == 1) {
                                sliderLayout.setScaleY(0.5f);
                                sliderLayout.setScaleX(0.5f);
                            } else {
                                sliderLayout.setScaleY(1);
                                sliderLayout.setScaleX(1);
                            }
                        }
                    });

            //sliderLayout.addOnPageChangeListener();

            sliderLayout.addSlider(defaultSliderView);

            //sliderLayout.destroyDrawingCache();
        }

    }*/

    private class ItemList implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EstateAdapter adapter = (EstateAdapter)parent.getAdapter();
            EstateUtil estateUtil = adapter.getItem(position);
            //estateUtil.getId();

            final TextView price = (TextView) findViewById(R.id.item_realestate_price);
            final TextView item_realestate_needed_address = (TextView) findViewById(R.id.item_realestate_needed_address);
            final TextView item_realestate_optional_address = (TextView) findViewById(R.id.item_realestate_optional_address);
            //final TextView type_realestate_value = (TextView) findViewById(R.id.type_realestate_value);
            //final TextView elevator_realestate_value = (TextView) findViewById(R.id.elevator_realestate_value);
            //final TextView balcony_realestate_value = (TextView) findViewById(R.id.balcony_realestate_value);
            //final TextView parking_realestate_value = (TextView) findViewById(R.id.parking_realestate_value);
            //final TextView view_realestate_value = (TextView) findViewById(R.id.view_realestate_value);
            //final TextView heating_realestate_value = (TextView) findViewById(R.id.heating_realestate_value);
            //final TextView comfort_realestate_value = (TextView) findViewById(R.id.comfort_realestate_value);

            final TextView item_realestate_description_text = (TextView)findViewById(R.id.item_realestate_description_text);



            //ingatlan_varos parking_realestate_value
            //ingatlan_utca
            //ingatlan_rovidleiras
            //ingatlan_picture_url
            //kedvenc (bool)
            //kepek (array)


            EstateUtil.getEstate(new SoapObjectResult() {
                @Override
                public void parseRerult(Object result) {
                    JSONObject obj = (JSONObject)result;
                    //obj.getString("")
                    try {
                        item_realestate_needed_address.setText(obj.getString("ingatlan_varos") + " " + obj.getString("ingatlan_utca"));


                        Locale locale = new Locale("en", "UK");
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                        //symbols.setDecimalSeparator(';');
                        symbols.setGroupingSeparator('.');
                        String pattern = "###,###";
                        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                        String format = decimalFormat.format(obj.getInt("ingatlan_ar"));
                        price.setText(format + " Ft");


                        item_realestate_description_text.setText(obj.getString("ingatlan_rovidleiras"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, String.valueOf(estateUtil.getId()), SettingUtil.getToken(getBaseContext()));

            prewView = viewFlip.getDisplayedChild();
            switchLayoutTo(CONTENTESTATE);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
            findViewById(R.id.scrollView2).scrollTo(0, 0);
            supportInvalidateOptionsMenu();
        }
    }

    private int pageCount = 0;
    private boolean isRefreshing = false;

    public void loadRealEstates(String idPost, String pagePost, final String tokenToSend, final String fav) {
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
                final int mPosition=0;
                final int mOffset=0;
                final RelativeLayout csakcsok = (RelativeLayout) findViewById(R.id.sorting_estates_relativeLayout);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        int position = listView.getFirstVisiblePosition();
                        View v = listView.getChildAt(0);
                        int offset = (v == null) ? 0 : v.getTop();

                        if (mPosition < position || (mPosition == position && mOffset < offset)){
                            // Scrolled up
                            csakcsok.setVisibility(View.GONE);

                        } else {
                            // Scrolled down
                            csakcsok.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        adapter.stopDownloadingImage(firstVisibleItem, firstVisibleItem + visibleItemCount);
                        if (firstVisibleItem + visibleItemCount == totalItemCount) {
                            if (!isRefreshing) {
                                //Log.e("REFRESHING", "PAGE 1");

                                //isRefreshing = true;
                                pageCount += 1;

                                String pageStr = String.valueOf(pageCount);
                                String lrgst = String.valueOf(EstateUtil.largestId);
                                EstateUtil.listEstates(new SoapObjectResult() {
                                    @Override
                                    public void parseRerult(Object result) {
                                        ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                                        adapter.addAll(arrayOfUsers);
                                        isRefreshing = true;
                                    }
                                }, lrgst, pageStr, tokenToSend, fav);
                            }
                        }
                    }
                });
                swipeContainer.setRefreshing(false);
            }
        }, idPost, pagePost, tokenToSend, fav);


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
                if (isShowingFavorites) {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0");
                }


                break;
            case CONTENTESTATE:
                //findViewById(R.id.estateListView).invalidate();
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                switchLayoutTo(prewView);
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
            default:
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0");
                switchLayoutTo(ESTATESLIST);
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

        if (id == R.id.action_map) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
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
                default:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
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
            case R.id.nav_mainpage:
                isShowingFavorites = false;
                if (viewFlip.getDisplayedChild() != ESTATESLIST) {
                    switchLayoutTo(ESTATESLIST);
                }
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0");
                break;
            case R.id.nav_profile:
                prewView = viewFlip.getDisplayedChild();
                switchLayoutTo(PROFILE);
                break;
            case R.id.nav_messages:
                switchLayoutTo(MESSAGES);
                break;
            case R.id.nav_billing:
                switchLayoutTo(BOOKING);
                break;
            case R.id.nav_myads:

                break;
            case R.id.nav_myfavs:
                isShowingFavorites = true;
                if(viewFlip.getDisplayedChild() != ESTATESLIST) {
                    switchLayoutTo(ESTATESLIST);
                }
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1");
                break;
            case R.id.nav_admonitor:

                break;
            case R.id.nav_agency:

                break;
            case R.id.nav_invitation:
                prewView = viewFlip.getDisplayedChild();
                switchLayoutTo(INVITE);
                break;
            case R.id.nav_logout:
                //finishActivity();
                LoginUtil.logout(this, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        if ((boolean) result) {

                        } else {
                            SettingUtil.setToken(MainActivity.this, "");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    }
                });

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

    public void switchLayoutToAddEstate(int switchTo){
        while(mCurrentLayoutStateAddEstate != switchTo){
            if(mCurrentLayoutStateAddEstate > switchTo){
                mCurrentLayoutStateAddEstate--;
                viewFlipAddEstate.setInAnimation(inFromLeftAnimation());
                viewFlipAddEstate.setOutAnimation(outToRightAnimation());
                viewFlipAddEstate.setDisplayedChild(switchTo);
            } else {
                mCurrentLayoutStateAddEstate++;
                viewFlipAddEstate.setInAnimation(inFromRightAnimation());
                viewFlipAddEstate.setOutAnimation(outToLeftAnimation());
                viewFlipAddEstate.setDisplayedChild(switchTo);
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
