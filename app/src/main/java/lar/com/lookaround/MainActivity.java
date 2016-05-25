package lar.com.lookaround;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lar.com.lookaround.adapters.AddImageAdapter;
import lar.com.lookaround.adapters.CalendarAdapter;
import lar.com.lookaround.adapters.CalendarBookingAdapter;
import lar.com.lookaround.adapters.EstateAdapter;
import lar.com.lookaround.adapters.MessageAdapter;
import lar.com.lookaround.adapters.SpinnerAdapter;
import lar.com.lookaround.restapi.ImageUploadService;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.util.AddImageUtil;
import lar.com.lookaround.util.CalendarBookingUtil;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.LoginUtil;
import lar.com.lookaround.util.MessageUtil;
import lar.com.lookaround.util.ScalingUtilities;
import lar.com.lookaround.util.SettingUtil;
import lar.com.lookaround.util.SpinnerUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ViewFlipper viewFlip, viewFlipAddEstate;
    LayoutInflater inflater;
    private int mCurrentLayoutState;
    private int mCurrentLayoutStateAddEstate;

    private static final int ESTATESLIST = 0;
    private static final int CONTENTESTATE = 1;
    private static final int ADDESTATE = 2;

    private static final int INVITE = 3;
    private static final int PROFILE = 4;
    private static final int MESSAGES = 5;
    private static final int BOOKING = 6;
    private static final int MESSAGES2 = 7;
    private static final int ADMONITOR = 8;

    View estatesView, contentRealestate, addEstate, addEstate2, addEstate3, addEstate4, addEstate5, addEstate1, invite, profile, messages, booking, message2, admonitor;

    DrawerLayout drawer;

    private SwipeRefreshLayout swipeContainer;

    private static int page = 0;
    private static String id;

    private boolean isShowingFavorites = false;


    final int TAKE_PHOTO_CODE = 9999;
    private String mobileNum = "0";


    File cacheFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realestate);
        Debug.getNativeHeapSize();

        setViewFlipper();

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hirdetések");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        /**
         *  Képfeltöltés
         *
         */

        // Here, the counter will be incremented each time, and the
        // picture taken by camera will be stored as 1.jpg,2.jpg
        // and likewise.

        /*File outputDir = getCacheDir(); // context being the Activity pointer
        try {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /**
         * Vége
         */

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                switchLayoutTo(ADDESTATE);
                setAddestatePageIndicator(whichAddestatePage);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                fab.setVisibility(View.INVISIBLE);

            }
        });



        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab_phone.setVisibility(View.INVISIBLE);
        fab_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNum));
                startActivity(intent);
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
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
                } else {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1", String.valueOf(adType), String.valueOf(sortingSpinner_int), 0);
                }
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        loadSearchSpinners();


        loadAddEstateSpinners();

        autocompleteSetter();
        // string array-be kéne rakni a resource-ból oszt menne csak valamiért mégse megy



        Calendar now = Calendar.getInstance();

        setCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH));

        //loadEstateImages();

        prewViews.add(0);

        Log.d("TOKEN", SettingUtil.getToken(getBaseContext()));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                refreshMessageCount();
                handler.postDelayed(this, 60 * 1000);
            }
        }, 1000);

    }

    int prewMessageCount = 0;
    public void refreshMessageCount() {
        MessageUtil.getMessageCount(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                try {
                    int c = (int) result;
                    TextView count = (TextView) findViewById(R.id.nav_message_count);
                    count.setText(String.valueOf(result));
                    if (prewMessageCount < c) {
                        if (prewMessageCount != 0) {
                            Snackbar.make(viewFlip.getCurrentView(), "Üzenete érkezett!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        prewMessageCount = c;
                    }
                } catch (Exception e) {
                    Log.e("MSG", "COUNT");
                }
            }
        }, SettingUtil.getToken(MainActivity.this));
    }


    public void autocompleteSetter(){
        String[] some_array = getResources().getStringArray(R.array.varosok_array);

        AutoCompleteTextView autocomplete = (AutoCompleteTextView)
                findViewById(R.id.keyword_realestate_search_edittext);

        AutoCompleteTextView autocomplete_addestate = (AutoCompleteTextView)
                findViewById(R.id.add_advert_city_edittext);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, some_array);

        autocomplete.setThreshold(2);
        autocomplete.setAdapter(adapter);

        autocomplete_addestate.setThreshold(2);
        autocomplete_addestate.setAdapter(adapter);
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


                //TODO: nem kell az összeset null-ra állítani, meg kell jegyeztetni az elsőt, és csak azt nullázni
                for (int i=0; i < gridview.getChildCount();i++) {
                    gridview.getChildAt(i).setBackground(null);
                }
                gridview.getChildAt(position).setBackgroundResource(R.drawable.b_d_border);
                gridview.getChildAt(position).setHovered(true);

                for (int i = 0; i < 24; i++) {
                    for (int j = 0; j < 31; j+=30) {
                        CalendarBookingUtil.addAppointment(i,j);
                    }
                }



                final ArrayList<CalendarBookingUtil> appointments = (ArrayList) CalendarBookingUtil.getAppointments();
                final CalendarBookingAdapter cba = new CalendarBookingAdapter(MainActivity.this, appointments);
                final ListView listView = (ListView) findViewById(R.id.listView_booking);
                listView.setAdapter(cba);

                //final ArrayList<CalendarBookingUtil> appointmentsNew = (ArrayList) CalendarBookingUtil.getAppointments();
                //cba.addAll(appointmentsNew);

                //Toast.makeText(MainActivity.this, "" + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


    Spinner hirdetesSpinner, szobaszamSpinner, allapotSpinner, emeletekSpinner, ingatlanTipusSpinner, parkolasSpinner, futesSpinner;
    Spinner energiaSpinner, kilatasSpinner, typeSpinner, panoramaSpinner;

    ArrayAdapter<CharSequence> typespinnerAdapter;

    int hirdetesSpinner_int = 0;
    int szobaszamSpinner_int = 0;
    int allapotSpinner_int = 0;
    int emeletekSpinner_int = 0;
    int ingatlanTipusSpinner_int = 0;
    int parkolasSpinner_int = 0;
    int futesSpinner_int = 0;
    int energiaSpinner_int = 0;
    int kilatasSpinner_int = 0;
    int butorozottSpinner_int = 0;
    int balconySpinner_int = 0;
    int elevatorSpinner_int = 0;



    public void loadAddEstateSpinners() {
        //add_advert_type_spinner
        SpinnerUtil.get_list_hirdetestipusa(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                hirdetesSpinner = (Spinner) findViewById(R.id.add_advert_type_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hirdetesSpinner.setAdapter(adapter);

                hirdetesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        hirdetesSpinner_int = spinnerUtil.getId();
                        TextView ar = (TextView) findViewById(R.id.add_advert_price_textview);
                        switch (spinnerUtil.getId()) {
                            case 1:
                                ar.setText("Ingatlan Ára*:");
                                break;
                            case 2:
                                ar.setText("Bérleti Díj*:");
                                break;
                            default:
                                ar.setText("Ingatlan Ára*:");
                                break;
                        }
                        if (hirdetesSpinner_int != 0) {
                            hirdetesSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            hirdetesSpinner.invalidate();
                        }

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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                szobaszamSpinner = (Spinner) findViewById(R.id.addestate_rooms_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                szobaszamSpinner.setAdapter(adapter);

                szobaszamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        szobaszamSpinner_int = spinnerUtil.getId();
                        if (szobaszamSpinner_int != 0) {
                            szobaszamSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            szobaszamSpinner.invalidate();
                        }
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                allapotSpinner = (Spinner) findViewById(R.id.addestate_condition_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                allapotSpinner.setAdapter(adapter);

                allapotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        allapotSpinner_int = spinnerUtil.getId();
                        if (allapotSpinner_int != 0) {
                            allapotSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            allapotSpinner.invalidate();
                        }

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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                emeletekSpinner = (Spinner) findViewById(R.id.addestate_floors_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                emeletekSpinner.setAdapter(adapter);

                emeletekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        emeletekSpinner_int = spinnerUtil.getId();
                        if (emeletekSpinner_int != 0) {
                            emeletekSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            emeletekSpinner.invalidate();
                        }
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                ingatlanTipusSpinner = (Spinner) findViewById(R.id.addestate_type_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ingatlanTipusSpinner.setAdapter(adapter);

                ingatlanTipusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        ingatlanTipusSpinner_int = spinnerUtil.getId();
                        if (ingatlanTipusSpinner_int != 0) {
                            ingatlanTipusSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            ingatlanTipusSpinner.invalidate();
                        }
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                parkolasSpinner = (Spinner) findViewById(R.id.addestate_parking_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parkolasSpinner.setAdapter(adapter);

                parkolasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        parkolasSpinner_int = spinnerUtil.getId();
                        if (parkolasSpinner_int != 0) {
                            parkolasSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            parkolasSpinner.invalidate();
                        }
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                futesSpinner = (Spinner) findViewById(R.id.addestate_heatingtype_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                futesSpinner.setAdapter(adapter);

                futesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        futesSpinner_int = spinnerUtil.getId();
                        if (futesSpinner_int != 0) {
                            futesSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            futesSpinner.invalidate();
                        }
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                energiaSpinner = (Spinner) findViewById(R.id.addestate_ecertificate_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                energiaSpinner.setAdapter(adapter);

                energiaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        energiaSpinner_int = spinnerUtil.getId();
                        if (energiaSpinner_int != 0) {
                            energiaSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            energiaSpinner.invalidate();
                        }
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                kilatasSpinner = (Spinner) findViewById(R.id.adestate_view_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kilatasSpinner.setAdapter(adapter);

                kilatasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        kilatasSpinner_int = spinnerUtil.getId();
                        if (kilatasSpinner_int != 0) {
                            kilatasSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                            kilatasSpinner.invalidate();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                    }
                });
            }
        });


        //add_advert_furniture_spinner
        ArrayList<SpinnerUtil> arrayListButor = (ArrayList) SpinnerUtil.get_list_butorozott();
        final SpinnerAdapter adapterButor = new SpinnerAdapter(MainActivity.this, arrayListButor);
        butorozottSpinner = (Spinner) findViewById(R.id.add_advert_furniture_spinner);
        adapterButor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        butorozottSpinner.setAdapter(adapterButor);

        butorozottSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterButor.getItem(position);
                butorozottSpinner_int = spinnerUtil.getId();
                if (butorozottSpinner_int != 0) {
                    butorozottSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                    butorozottSpinner.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });


        //addestate_balcony_spinner
        ArrayList<SpinnerUtil> arrayListBalcony = (ArrayList) SpinnerUtil.get_list_erkely();
        final SpinnerAdapter adapterBalcony = new SpinnerAdapter(MainActivity.this, arrayListBalcony);
        balconySpinner = (Spinner) findViewById(R.id.addestate_balcony_spinner);
        adapterBalcony.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        balconySpinner.setAdapter(adapterBalcony);

        balconySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterBalcony.getItem(position);
                balconySpinner_int = spinnerUtil.getId();
                if (balconySpinner_int != 0) {
                    balconySpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                    balconySpinner.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });


        //addestate_elevator_spinner
        ArrayList<SpinnerUtil> arrayListElevator = (ArrayList) SpinnerUtil.get_list_lift();
        final SpinnerAdapter adapterElevator = new SpinnerAdapter(MainActivity.this, arrayListElevator);
        elevatorSpinner = (Spinner) findViewById(R.id.addestate_elevator_spinner);
        adapterElevator.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elevatorSpinner.setAdapter(adapterElevator);

        elevatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterElevator.getItem(position);
                elevatorSpinner_int = spinnerUtil.getId();
                if (elevatorSpinner_int != 0) {
                    elevatorSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                    elevatorSpinner.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });


    }

    Spinner enegigenyo, butorozottSpinner, balconySpinner, elevatorSpinner, sortingSpinner, floorsMin, floorsMax, szobaMin, szobaMax, meret, balcony, lift, furniture;
    int sortingSpinner_int = 0;
    int floorsMint_int = 0;
    int floorsMax_int = 0;
    int szobaMin_int = 0;
    int szobaMax_int = 0;
    int meret_int = 0;
    int balcony_int = 0;
    int lift_int = 0;
    int furniture_int = 0;


    public void loadSearchSpinners() {
        //hasfurniture_spinner
        ArrayList<SpinnerUtil> arrayListFurniture = (ArrayList) SpinnerUtil.get_list_butorozott();
        final SpinnerAdapter adapterFurniture = new SpinnerAdapter(MainActivity.this, arrayListFurniture);
        furniture = (Spinner) findViewById(R.id.hasfurniture_spinner);
        adapterFurniture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        furniture.setAdapter(adapterFurniture);

        furniture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterFurniture.getItem(position);
                furniture_int = spinnerUtil.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        //realestate_elevator_spinner
        ArrayList<SpinnerUtil> arrayListElevator = (ArrayList) SpinnerUtil.get_list_lift();
        final SpinnerAdapter adapterElevator = new SpinnerAdapter(MainActivity.this, arrayListElevator);
        lift = (Spinner) findViewById(R.id.realestate_elevator_spinner);
        adapterElevator.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lift.setAdapter(adapterElevator);

        lift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterElevator.getItem(position);
                lift_int = spinnerUtil.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        ArrayList<SpinnerUtil> arrayListBalcony = (ArrayList) SpinnerUtil.get_list_erkely();
        final SpinnerAdapter adapterBalcony = new SpinnerAdapter(MainActivity.this, arrayListBalcony);
        balcony = (Spinner) findViewById(R.id.realestate_balcony_spinner);
        adapterBalcony.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        balcony.setAdapter(adapterBalcony);

        balcony.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterBalcony.getItem(position);
                balcony_int = spinnerUtil.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        ArrayList<SpinnerUtil> arrayListSize= (ArrayList) SpinnerUtil.get_list_meret();
        final SpinnerAdapter adapterSize = new SpinnerAdapter(MainActivity.this, arrayListSize);
        meret = (Spinner) findViewById(R.id.realestate_size_spinner);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meret.setAdapter(adapterSize);

        meret.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterSize.getItem(position);
                meret_int = spinnerUtil.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        SpinnerUtil.get_list_ingatlanszoba(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                szobaMin = (Spinner) findViewById(R.id.realestate_roomcount_min_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                szobaMin.setAdapter(adapter);

                szobaMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        szobaMin_int = spinnerUtil.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                    }
                });

            }
        });

        SpinnerUtil.get_list_ingatlanszoba(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                szobaMax = (Spinner) findViewById(R.id.realestate_roomcount_max_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                szobaMax.setAdapter(adapter);

                szobaMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        szobaMax_int = spinnerUtil.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                    }
                });

            }
        });

        //realestate_floors_min_spinner
        SpinnerUtil.get_list_ingatlanemelet(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                floorsMin = (Spinner) findViewById(R.id.realestate_floors_min_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                floorsMin.setAdapter(adapter);

                floorsMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        floorsMint_int = spinnerUtil.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                    }
                });

            }
        });

        SpinnerUtil.get_list_ingatlanemelet(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                floorsMax = (Spinner) findViewById(R.id.realestate_floors_max_spinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                floorsMax.setAdapter(adapter);

                floorsMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        floorsMax_int = spinnerUtil.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                    }
                });

            }
        });

        ArrayList<SpinnerUtil> arrayListSorting = (ArrayList) SpinnerUtil.get_list_szures();
        final SpinnerAdapter adapterSorting = new SpinnerAdapter(MainActivity.this, arrayListSorting);
        sortingSpinner = (Spinner) findViewById(R.id.sort_estates_spinner);
        adapterSorting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(adapterSorting);

        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterSorting.getItem(position);
                sortingSpinner_int = spinnerUtil.getId();
                String showFav;
                if (isShowingFavorites) {
                    showFav = "1";
                } else {
                    showFav = "0";
                }
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });




        SpinnerUtil.get_list_ingatlantipus(new SoapObjectResult() {
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

                typeSpinner = (Spinner) findViewById(R.id.condition_spinner);
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

        //ecertificate_search_spinner
        SpinnerUtil.get_list_ingatlanenergia(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                enegigenyo = (Spinner) findViewById(R.id.ecertificate_search_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                enegigenyo.setAdapter(adapter);

                enegigenyo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //parkolas_search_cpinner
        SpinnerUtil.get_list_ingatlanparkolas(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                parkolasSpinner = (Spinner) findViewById(R.id.parking_type_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
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

        //ingatlan kilatas search spinner
        SpinnerUtil.get_list_ingatlankilatas(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                panoramaSpinner = (Spinner) findViewById(R.id.view_type_realestate_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                panoramaSpinner.setAdapter(adapter);

                panoramaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        message2 = inflater.inflate(R.layout.content_message_thread, null);
        booking = inflater.inflate(R.layout.content_booking, null);
        admonitor = inflater.inflate(R.layout.content_add_admonitor, null);



        addEstate = inflater.inflate(R.layout.content_addrealestate, null);

        addEstate1 = inflater.inflate(R.layout.content_addrealestate_page1, null);
        addEstate2 = inflater.inflate(R.layout.content_addrealestate_page2, null);
        addEstate3 = inflater.inflate(R.layout.content_addrealestate_page3, null);
        addEstate4 = inflater.inflate(R.layout.content_addrealestate_page4, null);
        addEstate5 = inflater.inflate(R.layout.content_addrealestate_page5, null);



        viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.addView(estatesView, ESTATESLIST);
        viewFlip.addView(contentRealestate, CONTENTESTATE);
        viewFlip.addView(addEstate, ADDESTATE);
        viewFlip.addView(invite, INVITE);
        viewFlip.addView(profile, PROFILE);
        viewFlip.addView(messages, MESSAGES);
        viewFlip.addView(booking, BOOKING);
        viewFlip.addView(message2, MESSAGES2);
        viewFlip.addView(admonitor, ADMONITOR);

        viewFlipAddEstate = (ViewFlipper) findViewById(R.id.viewFlipperAddEstate);

        viewFlipAddEstate.addView(addEstate1, 0);
        viewFlipAddEstate.addView(addEstate2, 1);
        viewFlipAddEstate.addView(addEstate3, 2);
        viewFlipAddEstate.addView(addEstate4, 3);
        viewFlipAddEstate.addView(addEstate5, 4);



        viewFlip.setDisplayedChild(ESTATESLIST);
    }



    private int PICK_IMAGE_REQUEST = 1;
    private int TAKE_IMAGE_REQUEST = 0;

    public void pickImage(View view) {
        takeOrPick = false;
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    boolean takeOrPick;
    public void TakeImage(View view) {
        takeOrPick = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentURI, proj, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    int imageID = 0;
    int camImageID = 0;
    int galleryImageID = 0;

    public ArrayList<Uri> uris = new ArrayList <Uri>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

            Uri selectedImageURI = data.getData();
            File imageFile = new File(getRealPathFromURI(selectedImageURI));


            ImageUploadService service = new ImageUploadService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.e("error", "succes" + result);
                }
            }, imageFile);
            try {
                // ingatlan hash-t átadni.
                service.execute("FIKAMIKA");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                //imagesUri[imageID] = data.getData();
                uris.add(data.getData());
                Log.d("URIS_GET", uris.get(imageID).toString());


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                AddImageUtil.addImage(imageID, ScalingUtilities.createScaledBitmap(bitmap, 200, 200, ScalingUtilities.ScalingLogic.CROP));

                final LinearLayout linearLayoutGallery = (LinearLayout) findViewById(R.id.uploaded_images_linearlayout);

                ArrayList<AddImageUtil> allImages = AddImageUtil.getAllImages();
                final AddImageAdapter adapter = new AddImageAdapter(MainActivity.this, allImages);

                View galleryImage = (View) adapter.getView(imageID, null, null);

                final AddImageUtil addImageUtil = adapter.getItem(galleryImageID);

                galleryImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("GALLERY_ID: ", String.valueOf(addImageUtil.getId()));
                        callPopup(addImageUtil.getId(), linearLayoutGallery);
                    }
                });



                linearLayoutGallery.addView(galleryImage, addImageUtil.getId());
                linearLayoutGallery.setDividerPadding(0);
                imageID += 1;
                galleryImageID += 1;

                } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uris.add(data.getData());

            Bitmap bitmapCam = (Bitmap) data.getExtras().get("data");

            AddImageUtil.addImage(imageID, ScalingUtilities.createScaledBitmap(bitmapCam, 200, 200, ScalingUtilities.ScalingLogic.CROP));

            final LinearLayout linearLayoutCam = (LinearLayout) findViewById(R.id.camera_images_linearlayout);

            ArrayList<AddImageUtil> allImages = AddImageUtil.getAllImages();
            AddImageAdapter adapter = new AddImageAdapter(MainActivity.this, allImages);

            final AddImageUtil addImageUtil = adapter.getItem(imageID);

            View camImage = (View) adapter.getView(imageID, null, null);
            camImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CAM_ID: ", String.valueOf(addImageUtil.getId()));
                    callPopup(addImageUtil.getId(), linearLayoutCam);
                }
            });

            linearLayoutCam.addView(camImage, addImageUtil.getId());

            camImageID += 1;
            imageID += 1;
            linearLayoutCam.setDividerPadding(0);

        }

        if (requestCode == 69 && resultCode == Activity.RESULT_OK) {
                int result=data.getIntExtra("result", 0);
                if (result != 0) {
                    getEstateContent(result);
                }
        }
    }


    private void callPopup(final int id, final LinearLayout layout) {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.picture_modify_popup, null);

        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT,
                true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        ((Button) popupView.findViewById(R.id.swap_pic_button))
                .setOnClickListener(new View.OnClickListener() {


                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        uris.set(id, null);
                        layout.getChildAt(id).setVisibility(View.GONE);
                        popupWindow.dismiss();
                        if (takeOrPick) {
                            TakeImage(viewFlipAddEstate.getCurrentView());
                        } else {
                            pickImage(viewFlipAddEstate.getCurrentView());
                        }
                    }
                });

        ((Button) popupView.findViewById(R.id.cancel_pic_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });

        ((Button) popupView.findViewById(R.id.delete_pic_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        //layout.removeViewAt(id);
                        layout.getChildAt(id).setVisibility(View.GONE);
                        uris.set(id, null);
                        popupWindow.dismiss();

                    }
                });
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

    int adType = 0;
    public void adTypeChange(View view) {
        TextView typeText = (TextView) findViewById(R.id.estate_type_textview);
        String showFav;
        if (isShowingFavorites) {
            showFav = "1";
        } else {
            showFav = "0";
        }
        adType += 1;
        switch (adType) {
            case 1:
                typeText.setText("Eladó");
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, "1", String.valueOf(sortingSpinner_int), isMyAds);
                break;
            case 2:
                typeText.setText("Kiadó");
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, "2", String.valueOf(sortingSpinner_int), isMyAds);
                break;
            default:
                typeText.setText("Mindegy");
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, "0", String.valueOf(sortingSpinner_int), isMyAds);
                adType = 0;
                break;
        }
    }

    public void seeOnMap(View view) {
        if (latMap != null || !latMap.equals("0.0")) {
            SettingUtil.setLatForMap(getBaseContext(), latMap);
            Log.d("SETLAT", "FINISHED");
            Log.d("GETLAT", SettingUtil.getLatForMap(getBaseContext()));
        }

        if (lngMap != null || !lngMap.equals("0.0")) {
            SettingUtil.setLngForMap(getBaseContext(), lngMap);
        }

        if (SettingUtil.getLatForMap(getBaseContext()) != null && SettingUtil.getLngForMap(getBaseContext()) != null && !SettingUtil.getLatForMap(getBaseContext()).equals("0.0") && !SettingUtil.getLngForMap(getBaseContext()).equals("0.0")) {
            //startActivity(new Intent(MainActivity.this, MapsActivity.class));
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivityForResult(i, 69);
        } else {
            Snackbar.make(view, "Sikertelen művelet!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }


    }

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
    public String postalCode = "0";

    public double lat;
    public double lng;
    public String zipcodeToSend = "0000";




    private boolean isValidString(String string) {
        if (string != null && string.length() != 0) {
            return true;
        }
        return false;
    }


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

private int whichAddestatePage = 0;

private boolean isAddingEstate = false;

    public void nextAddestatePage(final View view) {
        if (whichAddestatePage < 5) {
            whichAddestatePage += 1;
            boolean isFilledOut = true;
            switch (whichAddestatePage) {
                case 1:
                    TextView title = (TextView) findViewById(R.id.adverttitle_edittext);
                    TextView description = (TextView) findViewById(R.id.advert_description_edittext);
                    TextView price = (TextView) findViewById(R.id.add_advert_price_edittext);
                    TextView city = (TextView) findViewById(R.id.add_advert_city_edittext);

                    TextView street = (TextView) findViewById(R.id.add_advert_street_edittext);
                    TextView num = (TextView) findViewById(R.id.add_advert_str_number_edittext);
                    TextView size = (TextView) findViewById(R.id.estate_size_edittext);


                    estateTitle = title.getText().toString();
                    estateDescription = description.getText().toString();
                    estatePrice = price.getText().toString();
                    estateCity = city.getText().toString();

                    estateStreet = street.getText().toString();
                    estetaHouseNumber = num.getText().toString();
                    estateSize = size.getText().toString();

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

                    if(!isValidString(estateStreet)) {
                        street.setError("Hiba!");
                        street.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estetaHouseNumber)) {
                        num.setError("Hiba!");
                        num.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateSize)) {
                        size.setError("Hiba!");
                        size.invalidate();
                        isFilledOut = false;
                    }

                    if (hirdetesSpinner_int == 0) {
                        hirdetesSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        hirdetesSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        hirdetesSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        hirdetesSpinner.invalidate();
                    }

                    if (butorozottSpinner_int == 0) {
                        butorozottSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        butorozottSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        butorozottSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        butorozottSpinner.invalidate();
                    }

                    if (isFilledOut) {
                        Geocoder gc = new Geocoder(this);

                        String fullAdress = estateCity + " " + estateStreet;
                        List<Address> list = null;
                        try {
                            list = gc.getFromLocationName(fullAdress, 1);

                            if (!list.isEmpty()) {
                                Address add = list.get(0);
                                zipcodeToSend = add.getPostalCode();
                                lat = add.getLatitude();
                                lng = add.getLongitude();
                                if (zipcodeToSend != null) {
                                    Log.d("ZIPCODE_1", zipcodeToSend);
                                } else {
                                    zipcodeToSend = "0000";
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        setAddestatePageIndicator(whichAddestatePage);
                        switchLayoutToAddEstate(whichAddestatePage);
                    } else {
                        whichAddestatePage = 0;
                    }
                    break;

                case 2:
                    if (szobaszamSpinner_int == 0) {
                        szobaszamSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        szobaszamSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        szobaszamSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        szobaszamSpinner.invalidate();
                    }

                    if (allapotSpinner_int == 0) {
                        allapotSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        allapotSpinner.invalidate();
                        isFilledOut = false;
                    } else  {
                        allapotSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        allapotSpinner.invalidate();
                    }

                    if (emeletekSpinner_int == 0) {
                        emeletekSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        emeletekSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        emeletekSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        emeletekSpinner.invalidate();
                    }

                    if (ingatlanTipusSpinner_int == 0) {
                        ingatlanTipusSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        ingatlanTipusSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        ingatlanTipusSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        ingatlanTipusSpinner.invalidate();
                    }

                    if (parkolasSpinner_int == 0) {
                        parkolasSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        parkolasSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        parkolasSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        parkolasSpinner.invalidate();
                    }

                    if (futesSpinner_int == 0) {
                        futesSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        futesSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        futesSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        futesSpinner.invalidate();
                    }

                    if (energiaSpinner_int == 0) {
                        energiaSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        energiaSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        energiaSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        energiaSpinner.invalidate();
                    }

                    if (kilatasSpinner_int == 0) {
                        kilatasSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        kilatasSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        kilatasSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        kilatasSpinner.invalidate();
                    }

                    if (balconySpinner_int == 0) {
                        balconySpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        balconySpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        balconySpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        balconySpinner.invalidate();
                    }

                    if (elevatorSpinner_int == 0) {
                        elevatorSpinner.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        elevatorSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        elevatorSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        elevatorSpinner.invalidate();

                    }




                    if (isFilledOut) {
                        setAddestatePageIndicator(whichAddestatePage);
                        switchLayoutToAddEstate(whichAddestatePage);
                    } else {
                        whichAddestatePage = 1;
                    }
                    break;

                case 3:
                    setAddestatePageIndicator(whichAddestatePage);
                    switchLayoutToAddEstate(whichAddestatePage);
                    break;

                case 4:
                    setAddestatePageIndicator(whichAddestatePage);
                    switchLayoutToAddEstate(whichAddestatePage);
                    break;

                case 5:
                    Log.d("ZIPCODE_2", zipcodeToSend);
                    EstateUtil.addEstate(new SoapObjectResult() {
                                             @Override
                                             public void parseRerult(Object result) {
                                                 final ArrayList<EstateUtil> resArray = (ArrayList) result;

                                                 if (!resArray.get(resArray.size() - 1).isError()) {

                                                     hash = resArray.get(resArray.size() - 1).getHash();


                                                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                         if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                             if (!Settings.System.canWrite(MainActivity.this)) {
                                                                 requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                         Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                                                             } else {
                                                                 uploadImages(hash);
                                                             }
                                                         } else {
                                                             uploadImages(hash);
                                                         }
                                                     } else {
                                                         uploadImages(hash);
                                                     }

                                                     //Toast.makeText(MainActivity.this, "Hirdetés feladva!", Toast.LENGTH_SHORT).show();
                                                     isBackPressed = true;
                                                     hirdetesSpinner_int = 0;
                                                     szobaszamSpinner_int = 0;
                                                     allapotSpinner_int = 0;
                                                     emeletekSpinner_int = 0;
                                                     ingatlanTipusSpinner_int = 0;
                                                     parkolasSpinner_int = 0;
                                                     futesSpinner_int = 0;
                                                     energiaSpinner_int = 0;
                                                     kilatasSpinner_int = 0;
                                                     butorozottSpinner_int = 0;
                                                     balconySpinner_int = 0;
                                                     elevatorSpinner_int = 0;
                                                     whichAddestatePage = 0;
                                                     TextView title = (TextView) findViewById(R.id.adverttitle_edittext);
                                                     TextView description = (TextView) findViewById(R.id.advert_description_edittext);
                                                     TextView price = (TextView) findViewById(R.id.add_advert_price_edittext);
                                                     TextView city = (TextView) findViewById(R.id.add_advert_city_edittext);

                                                     TextView street = (TextView) findViewById(R.id.add_advert_street_edittext);
                                                     TextView num = (TextView) findViewById(R.id.add_advert_str_number_edittext);
                                                     TextView size = (TextView) findViewById(R.id.estate_size_edittext);
                                                     title.setText("");
                                                     description.setText("");
                                                     price.setText("");
                                                     city.setText("");
                                                     street.setText("");
                                                     num.setText("");
                                                     size.setText("");

                                                     loadAddEstateSpinners();

                                                     switchLayoutToAddEstate(0);
                                                     getEstateContent(resArray.get(resArray.size() - 1).getId());
                                                     Snackbar.make(view, "Hirdetés feladva!", Snackbar.LENGTH_LONG)
                                                             .setAction("Action", null).show();
                                                     Log.d("ADDESTATE_HASH: ", resArray.get(0).getHash());
                                                 } else {
                                                     showAlertError("Sikertelen feltöltés");

                                                 }

                                             }
                                         }, estateSize, estateCity, estateStreet, estateDescription, estatePrice,
                            String.valueOf(energiaSpinner_int), String.valueOf(butorozottSpinner_int), String.valueOf(kilatasSpinner_int), String.valueOf(elevatorSpinner_int),
                            String.valueOf(futesSpinner_int), String.valueOf(parkolasSpinner_int), String.valueOf(balconySpinner_int), String.valueOf(ingatlanTipusSpinner_int),
                            String.valueOf(emeletekSpinner_int), String.valueOf(allapotSpinner_int), String.valueOf(szobaszamSpinner_int),
                            String.valueOf(lng), String.valueOf(lat), estateTitle, String.valueOf(hirdetesSpinner_int), SettingUtil.getToken(getBaseContext()), zipcodeToSend);
                    break;
            }
        }
    }

    public void uploadImages(String hash) {
        Log.d("UPLOAD_URI_SIZE", String.valueOf(uris.size()));
        ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("Feltöltés...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        int imageCount = 0;
        /*for (int i = 0; i < uris.size(); i++) {
            if (uris.get(i) != null) {
                imageCount += 1;
            }
        }
        progressBar.setMax(imageCount);*/
        progressBar.setMax(uris.size());

        progressBar.show();
        //final int[] progress = {0};
        Log.d("UPLOAD_URI_FOR_CYCLE", "CALLED");
        for (int j = 0; j < uris.size(); j++) {
            Log.d("UPLOAD_URI_FOR_CYCLE", String.valueOf(j));
            //TODO: ez az if csak balász telefonján fut le...
            if (uris.get(j) != null) {
                Log.d("URI_IN_FOR_CYCLE", uris.get(j).toString());
            //TODO: képfeltöltés megint fos, lehal a getRealPathFromURI...
                File imageFile = new File(getRealPathFromURI(uris.get(j)));

                ImageUploadService service = new ImageUploadService(new SoapResult() {
                    @Override
                    public void parseRerult(String result) {
                        Log.e("error", "succes" + result);
                        //progress[0] += 1;
                        //progressBar.setProgress(progress[0]);
                        //Log.d("UPLOAD_PROGRESS", String.valueOf(progress[0]));
                    }
                }, imageFile, progressBar);
                try {
                    // ingatlan hash-t átadni.
                    Log.d("UPLOAD_HASH", hash);
                    service.execute(hash);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    String hash;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImages(hash);
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    String latMap = "0.0";
    String lngMap = "0.0";

    public void getEstateContent(int id) {

        final TextView price = (TextView) findViewById(R.id.item_realestate_price);
        final TextView title = (TextView) findViewById(R.id.item_realestate_needed_address);
        final TextView adress = (TextView) findViewById(R.id.item_realestate_optional_address);
        final TextView roomcount = (TextView) findViewById(R.id.roomcount_realestate_value);
        final TextView size = (TextView) findViewById(R.id.size_realestate_item_value);
        final TextView type= (TextView) findViewById(R.id.type_realestate_value);
        final TextView elevator = (TextView) findViewById(R.id.elevator_realestate_value);
        final TextView balcony = (TextView) findViewById(R.id.balcony_realestate_value);
        final TextView parking = (TextView) findViewById(R.id.parking_realestate_value);

        final TextView kilatas = (TextView) findViewById(R.id.view_realestate_value);
        final TextView condition = (TextView) findViewById(R.id.condition_realestate_value);
        final TextView floors = (TextView) findViewById(R.id.floors_realestate_value);
        final TextView heating = (TextView) findViewById(R.id.heating_realestate_value);
        final TextView ecertificate = (TextView) findViewById(R.id.energy_certificate_realestate_item_value);
        final TextView hasfurniture = (TextView) findViewById(R.id.hasfurniture_realestate_item_value);


        final TextView item_realestate_description_text = (TextView)findViewById(R.id.item_realestate_description_text);

        final TextView name = (TextView) findViewById(R.id.profile_name_text);
        final TextView profile_type = (TextView) findViewById(R.id.profile_type_text);





        EstateUtil.getEstate(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                Log.d("GET_ESTATE Result: ", result.toString());
                JSONObject obj = (JSONObject) result;
                try {

                    JSONArray kepekArray = new JSONArray(obj.getString("kepek"));
                    List<String> imageUrls = new ArrayList<String>();
                    imageUrls.clear();
                    for (int j=0; j < kepekArray.length(); j++) {
                        JSONObject jsonKep = kepekArray.getJSONObject(j);
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

                    name.setText(obj.getString("vezeteknev") + " " + obj.getString("keresztnev"));
                    profile_type.setText(obj.getString("tipus"));
                    mobileNum = obj.getString("mobil");

                    Locale locale = new Locale("en", "UK");

                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                    //symbols.setDecimalSeparator(';');
                    symbols.setGroupingSeparator('.');

                    String pattern = "###,###";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                    String format = decimalFormat.format(obj.getInt("ingatlan_ar"));
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


                    isFavEstate = obj.getBoolean("kedvenc");

                    favItem = (MenuView.ItemView) findViewById(R.id.action_fav);

                    if (favItem != null) {
                        if (isFavEstate) {
                            Log.d("ISFAV ", "TRUE");
                            favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_filled));
                        } else {
                            Log.d("ISFAV ", "FALSE");
                            favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_content));
                        }
                    }

                    if (obj.getString("ingatlan_lat") != null || !obj.getString("ingatlan_lat").equals("0.0")) {
                        //SettingUtil.setLatForMap(getBaseContext(), obj.getString("ingatlan_lat"));
                        latMap = obj.getString("ingatlan_lat");
                        Log.d("SETLAT", "BEGINNED");
                    }

                    if (obj.getString("ingatlan_lng") != null || !obj.getString("ingatlan_lat").equals("0.0")) {
                        //SettingUtil.setLngForMap(getBaseContext(), obj.getString("ingatlan_lng"));
                        lngMap = obj.getString("ingatlan_lng");
                    }

                    //TODO: a honlapon felrakott ingatlanok nem térnek vissza ingatlan_allapottal csak ingatlan_allapot_id-vel
                    condition.setText(obj.getString("ingatlan_allapot"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, String.valueOf(id), SettingUtil.getToken(getBaseContext()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab_phone.setVisibility(View.VISIBLE);

        if (viewFlip.getDisplayedChild() != CONTENTESTATE) {
            //TODO: megtekintés térképen->infoWindow->MainActivity ContentEstate fav icon bug
            switchLayoutTo(CONTENTESTATE);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
            supportInvalidateOptionsMenu();
        }
        findViewById(R.id.scrollView2).scrollTo(0, 0);

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
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), 0);
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

    public void showAlertError(String message) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("HIBA")
                .create();
        myAlert.show();
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



    public void loadEstateImages(final List<String> urls) {
        final SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.slider);

        //final List<String> urls = slideImageURLLists();

        /*final List<String> urls = new ArrayList<String>();
        urls.add("https://s-media-cache-ak0.pinimg.com/736x/e7/f2/81/e7f2812089086a6e9e7e6408457c76c4.jpg");
        urls.add("https://scontent.fomr1-1.fna.fbcdn.net/hphotos-xfp1/v/t1.0-9/10399388_1037153376364726_5568922816957393250_n.jpg?oh=6c8027e95134a0fc5310ba3e0847372d&oe=577B8A7D");
        urls.add("https://scontent.fomr1-1.fna.fbcdn.net/v/t1.0-9/10500443_784808998244322_3120390074428787735_n.jpg?oh=f5ca0004521550b3146dff315a43f37d&oe=5779D2CB");
        */
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

    EstateUtil estateUtil_fav;
    int estateID;
    String estateHash;
    private class ItemList implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EstateAdapter adapter = (EstateAdapter)parent.getAdapter();
            EstateUtil estateUtil = adapter.getItem(position);
            estateID = estateUtil.getId();
            estateHash = estateUtil.getHash();
            estateUtil_fav = estateUtil;
            getEstateContent(estateID);
        }
    }

    private int pageCount = 0;
    private boolean isRefreshing = false;
    private String favToSend = "0";

    public void loadMessages() {
        switchLayoutTo(MESSAGES);

        MessageUtil.listMessages(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<MessageUtil> lst = (ArrayList<MessageUtil>) result;
                ListView thread = (ListView) findViewById(R.id.allmessages);

                final MessageAdapter adapter = new MessageAdapter(MainActivity.this, lst);

                thread.setAdapter(adapter);
                thread.setDividerHeight(0);

                thread.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        MessageUtil ms = adapter.getItem(i);
                        loadMessagesForEstate(ms.getHash(), ms.getUid());
                    }
                });

            }
        }, SettingUtil.getToken(this));
    }

    public void loadMessagesForEstate(final String hash, int uid) {
        switchLayoutTo(MESSAGES2);

        MessageUtil.listMessagesForEstate(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<MessageUtil> lst = (ArrayList<MessageUtil>)result;
                ListView thread = (ListView) findViewById(R.id.messagethread);

                final MessageAdapter adapter = new MessageAdapter(MainActivity.this, lst);

                thread.setAdapter(adapter);
                thread.setDividerHeight(0);

                final EditText et = (EditText) findViewById(R.id.write_message_edittext);
                RelativeLayout send = (RelativeLayout)findViewById(R.id.sent_message_text_rlayout);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MessageUtil.sendMessage(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                boolean b = (boolean) result;
                                if (!b) {
                                    MessageUtil ms = new MessageUtil();
                                    ms.setFromme(1);
                                    ms.setMsg(et.getText().toString());
                                    adapter.add(ms);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    // TODO ERROR DIALOG
                                }
                                et.setText("");
                            }
                        }, SettingUtil.getToken(MainActivity.this), hash, et.getText().toString());
                    }
                });

            }
        }, SettingUtil.getToken(this), hash, uid);
    }

    public void loadRealEstates(String idPost, String pagePost, final String tokenToSend, final String fav, final String etypeString, final String ordering, final int jstme) {
        pageCount = 0;
        isRefreshing = false;
        EstateUtil.largestId = 0;
        favToSend = "0";
        if (jstme == 1 && fav.equals("1")) {
            favToSend = "0";
        }
        if (jstme == 0 && fav.equals("1")) {
            favToSend = "1";
        }
        EstateUtil.listEstates(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                final ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;

                final EstateAdapter adapter = new EstateAdapter(MainActivity.this, arrayOfUsers);
                // Attach the adapter to a ListView
                final ListView listView = (ListView) findViewById(R.id.estateListView);
                listView.setAdapter(adapter);
                listView.setDivider(null);
                listView.setDividerHeight(0);
                listView.setOnItemClickListener(new ItemList());
                final int mPosition=0;
                final int mOffset=0;


                //final RelativeLayout csakcsok = (RelativeLayout) findViewById(R.id.sorting_estates_relativeLayout);
                final View csok = (View) findViewById(R.id.sorting_estates_relativeLayout);

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        int position = listView.getFirstVisiblePosition();
                        View v = listView.getChildAt(0);
                        int offset = (v == null) ? 0 : v.getTop();

                        if (mPosition < position || (mPosition == position && mOffset < offset)){
                            // Scrolled up
                            /*csok.animate()
                                    .translationY(0)
                                    .alpha(0.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            csok.setVisibility(View.GONE);
                                        }
                                    });*/
                        } else {
                            // Scrolled down
                            /*csok.animate()
                                    .translationY(csok.getHeight()-75)
                                    .alpha(1.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            csok.setVisibility(View.VISIBLE);
                                        }
                                    });*/
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        adapter.stopDownloadingImage(firstVisibleItem, firstVisibleItem + visibleItemCount);
                        if (firstVisibleItem + visibleItemCount == totalItemCount) {
                            if (!isRefreshing) {
                                pageCount += 1;
                                String pageStr = String.valueOf(pageCount);
                                String lrgst = String.valueOf(EstateUtil.largestId);
                                if (jstme == 1 && fav.equals("1")) {
                                    favToSend = "0";
                                }
                                EstateUtil.listEstates(new SoapObjectResult() {
                                    @Override
                                    public void parseRerult(Object result) {
                                        ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                                        adapter.addAll(arrayOfUsers);
                                        isRefreshing = true;
                                    }
                                }, lrgst, pageStr, tokenToSend, favToSend, etypeString, ordering, String.valueOf(jstme));
                            }
                        }
                    }
                });
                swipeContainer.setRefreshing(false);
            }
        }, idPost, pagePost, tokenToSend, favToSend, etypeString, ordering, String.valueOf(jstme));


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        if (prewViews.size() > 0) {
            isBackPressed = true;
            switchLayoutTo(prewViews.get(prewViews.size() - 1));
            prewViews.remove(prewViews.size()-1);
        } else {
            prewViews.add(0);
        }


        //TODO: visszalépésnl tökéletesíteni a megjelenő ikonokat (mindig a megfelelőek jelenejenek meg)
        switch (viewFlip.getDisplayedChild()) {
            case ESTATESLIST:
                if (isShowingFavorites) {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), 0);
                }
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                fab.setVisibility(View.VISIBLE);
                fab_phone.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Hirdetések");
                break;
            case CONTENTESTATE:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                fab.setVisibility(View.INVISIBLE);
                fab_phone.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(null);
                break;
            case ADDESTATE:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                fab.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Hirdetés feladás");
                break;
            case PROFILE:
                getSupportActionBar().setTitle("Profilom");
                break;
            case MESSAGES:
                fab.setVisibility(View.INVISIBLE);
                fab_phone.setVisibility(View.INVISIBLE);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                getSupportActionBar().setTitle("Üzenetek");
                break;
            case BOOKING:
                getSupportActionBar().setTitle("Időpontfoglalás");
                break;
            default:
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), 0);
                fab.setVisibility(View.VISIBLE);
                fab_phone.setVisibility(View.INVISIBLE);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    //private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*if(viewFlip.getDisplayedChild() == ESTATESLIST) {
            getMenuInflater().inflate(R.menu.main, menu);
        }*/
        //this.menu = menu;
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

    MenuView.ItemView favItem;
    boolean isFavEstate = false;

    /*
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
                isMyAds = 0;
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
                break;
            case R.id.nav_admonitor:
                break;
            case R.id.nav_invitation:
                switchLayoutTo(INVITE);
                break;
            case R.id.nav_logout:
                //finishActivity();
                LoginUtil.logout(this, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        if ((boolean) result) {
                            Snackbar.make(viewFlip.getCurrentView(), "Sikertelen művelet!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)

                                    .show();
                        } else {
                            SettingUtil.setToken(MainActivity.this, "");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    }
                });

                //finish();
                break;
     */

    public void doLogout(View view) {
        LoginUtil.logout(this, new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if ((boolean) result) {
                    Snackbar.make(viewFlip.getCurrentView(), "Sikertelen művelet!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)

                            .show();
                } else {
                    SettingUtil.setToken(MainActivity.this, "");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public void showAdmonitor(View view) {
        isBackPressed = false;
        switchLayoutTo(ADMONITOR);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        closeDrawer();
    }

    public void showInviteFriends(View view) {
        isBackPressed = false;
        switchLayoutTo(INVITE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        closeDrawer();
    }

    public void showMyFavs(View view) {
        isBackPressed = false;
        isShowingFavorites = true;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        if(viewFlip.getDisplayedChild() != ESTATESLIST) {
            switchLayoutTo(ESTATESLIST);
        }
        isMyAds = 0;
        Log.d("MY", "FAVS");
        getSupportActionBar().setTitle("Kedvencek");
        loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1", String.valueOf(adType), String.valueOf(sortingSpinner_int), 0);
        closeDrawer();
    }

    public void showProfile(View view) {
        isBackPressed = false;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        switchLayoutTo(PROFILE);
        closeDrawer();
    }

    public void showAllEstates(View view) {
        isBackPressed = false;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
        isShowingFavorites = false;
        if (viewFlip.getDisplayedChild() != ESTATESLIST) {
            switchLayoutTo(ESTATESLIST);
        }
        isMyAds = 0;
        loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
        closeDrawer();
    }


    public void showMessages(View view) {
        isBackPressed = false;
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        loadMessages();
        closeDrawer();
    }

    int isMyAds = 0;
    public void showMyAds(View view) {
        isBackPressed = false;
        isMyAds = 1;
        switchLayoutTo(ESTATESLIST);
        loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
        FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_phone.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
        getSupportActionBar().setTitle("Saját");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        closeDrawer();
    }

    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.action_map:
                SettingUtil.setLatForMap(getBaseContext(), "0.0");
                SettingUtil.setLngForMap(getBaseContext(), "0.0");
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivityForResult(i, 69);
                break;
            case R.id.action_calendar:
                switchLayoutTo(BOOKING);
                break;
            case R.id.action_fav:
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
                                estateUtil_fav.setIsFavourite(true);
                            }
                        }
                    },String.valueOf(estateID), SettingUtil.getToken(MainActivity.this), "1");
                } else {
                    EstateUtil.setFavorite(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            Log.d("FAV RESULT", result.toString());
                            if (!(boolean) result) {
                                favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_content));
                                isFavEstate = false;
                                estateUtil_fav.setIsFavourite(false);
                            }
                        }
                    }, String.valueOf(estateID), SettingUtil.getToken(MainActivity.this), "0");
                }
                break;
            case R.id.action_message:
                loadMessagesForEstate(estateHash, 0);
                break;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.advert_city)));
                break;
            case android.R.id.home:
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
                switch (viewFlip.getDisplayedChild()) {
                    case ESTATESLIST:
                        if (isShowingFavorites) {
                            isMyAds = 0;
                            isShowingFavorites = false;
                            getSupportActionBar().setTitle("Kedvencek");
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                            loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
                        } else if (isMyAds == 1) {
                            isMyAds = 0;
                            isShowingFavorites = false;
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                            getSupportActionBar().setTitle("Saját");
                            loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
                        } else {
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            } else {
                                drawer.openDrawer(GravityCompat.START);
                            }
                        }
                        break;
                    default:
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                        switchLayoutTo(ESTATESLIST);
                        fab.setVisibility(View.VISIBLE);
                        fab_phone.setVisibility(View.INVISIBLE);
                        supportInvalidateOptionsMenu();
                        break;
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*switch (id) {
            case R.id.nav_mainpage:
                isShowingFavorites = false;
                if (viewFlip.getDisplayedChild() != ESTATESLIST) {
                    switchLayoutTo(ESTATESLIST);
                }
                isMyAds = 0;
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
                break;
            case R.id.nav_profile:
                switchLayoutTo(PROFILE);
                break;
            case R.id.nav_messages:
                switchLayoutTo(MESSAGES);
                break;
            case R.id.nav_billing:
                switchLayoutTo(BOOKING);
                break;
            case R.id.nav_myads:
                //TODO: estateUtil.listEstatest meghívni (loadRealEstates) és átalakítani h csak a sajátunk jelenjen meg
                break;
            case R.id.nav_myfavs:
                isShowingFavorites = true;
                if(viewFlip.getDisplayedChild() != ESTATESLIST) {
                    switchLayoutTo(ESTATESLIST);
                }
                isMyAds = 0;
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1", String.valueOf(adType), String.valueOf(sortingSpinner_int), isMyAds);
                break;
            case R.id.nav_admonitor:
                //TODO: létrehozni hozzá az API-t
                break;
            case R.id.nav_agency:
                //TODO: kitalálni h mi és h is lesz
                break;
            case R.id.nav_invitation:
                switchLayoutTo(INVITE);
                break;
            case R.id.nav_logout:
                //finishActivity();
                LoginUtil.logout(this, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        if ((boolean) result) {
                            Snackbar.make(viewFlip.getCurrentView(), "Sikertelen művelet!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)

                                    .show();
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
        drawer.closeDrawer(GravityCompat.END);*/
        return true;
    }





    ArrayList<Integer> prewViews = new ArrayList<Integer>();
    boolean isBackPressed = false;

    public void switchLayoutTo(int switchTo){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        switch(switchTo) {
            case ADDESTATE:
                getSupportActionBar().setTitle("Hirdetés feladás");
                break;
            case ESTATESLIST:
                getSupportActionBar().setTitle("Hirdetések");
                break;
            case INVITE:
                getSupportActionBar().setTitle("Meghívás");
                break;
            case PROFILE:
                getSupportActionBar().setTitle("Profilom");
                break;
            case MESSAGES:
                fab_phone.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Üzenetek");
                break;
            case MESSAGES2:
                fab_phone.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Üzenetek");
                break;
            case BOOKING:
                getSupportActionBar().setTitle("Időpontfoglalás");
                break;
            case ADMONITOR:
                fab_phone.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Hirdetésfigyelő");
                break;
            default:
                getSupportActionBar().setTitle(null);
                break;
        }

        // TODO: ez itt nem jó :D de nagyon nem. de egylőre gyorsan javítottam.
        if (viewFlip.getDisplayedChild() == switchTo && isBackPressed) {
            if (prewViews.size() > 1) {
                switchTo = prewViews.get(prewViews.size() - 2);
            }
        }

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



        if (!isBackPressed) {
            prewViews.add(viewFlip.getDisplayedChild());
            for (int i = 0; i < prewViews.size(); i++) {
                Log.d("VIEWS: ", prewViews.get(i).toString());
            }
        } else {
            isBackPressed = false;
        }

        supportInvalidateOptionsMenu();
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
