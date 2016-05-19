package lar.com.lookaround;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.DefaultSliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
import lar.com.lookaround.adapters.SpinnerAdapter;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.AddImageUtil;
import lar.com.lookaround.util.CalendarBookingUtil;
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
                intent.setData(Uri.parse("tel:06304979787"));
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
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int));
                } else {
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1", String.valueOf(adType), String.valueOf(sortingSpinner_int));
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

        loadEstateImages();

        prewViews.add(0);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });


    }

    Spinner enegigenyo, butorozottSpinner, balconySpinner, elevatorSpinner, sortingSpinner;
    int sortingSpinner_int = 0;


    public void loadSearchSpinners() {

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
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, String.valueOf(adType), String.valueOf(sortingSpinner_int));
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
        booking = inflater.inflate(R.layout.content_booking, null);



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

    int imageID = 0;
    int camImageID = 0;
    int galleryImageID = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
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

                        popupWindow.dismiss();
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
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, "1", String.valueOf(sortingSpinner_int));
                break;
            case 2:
                typeText.setText("Kiadó");
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, "2", String.valueOf(sortingSpinner_int));
                break;
            default:
                typeText.setText("Mindegy");
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), showFav, "0", String.valueOf(sortingSpinner_int));
                adType = 0;
                break;
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


                    //TODO: hátttérszín változtatás tökéletesítése
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
                        hirdetesSpinner.setBackgroundColor(0xFFFF0000);
                        hirdetesSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        hirdetesSpinner.setBackgroundColor(0xFFFFFFFF);
                        hirdetesSpinner.invalidate();
                    }

                    if (isFilledOut) {
                        Geocoder gc = new Geocoder(this);

                        String fullAdress = estateCity + " " + estateStreet;
                        List<Address> list = null;
                        try {
                            list = gc.getFromLocationName(fullAdress, 1);

                            if (!list.isEmpty()) {
                                Address add = list.get(0);
                                lat = add.getLatitude();
                                lng = add.getLongitude();
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
                    //TODO: hátttérszín változtatás tökéletesítése
                    if (szobaszamSpinner_int == 0) {
                        szobaszamSpinner.setBackgroundColor(0xFFFF0000);
                        szobaszamSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        szobaszamSpinner.setBackgroundColor(0xFFFFFFFF);
                        szobaszamSpinner.invalidate();
                    }

                    if (allapotSpinner_int == 0) {
                        allapotSpinner.setBackgroundColor(0xFFFF0000);
                        allapotSpinner.invalidate();
                        isFilledOut = false;
                    } else  {
                        allapotSpinner.setBackgroundColor(0xFFFFFFFF);
                        allapotSpinner.invalidate();
                    }

                    if (emeletekSpinner_int == 0) {
                        emeletekSpinner.setBackgroundColor(0xFFFF0000);
                        emeletekSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        emeletekSpinner.setBackgroundColor(0xFFFFFFFF);
                        emeletekSpinner.invalidate();
                    }

                    if (ingatlanTipusSpinner_int == 0) {
                        ingatlanTipusSpinner.setBackgroundColor(0xFFFF0000);
                        ingatlanTipusSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        ingatlanTipusSpinner.setBackgroundColor(0xFFFFFFFF);
                        ingatlanTipusSpinner.invalidate();
                    }

                    if (parkolasSpinner_int == 0) {
                        parkolasSpinner.setBackgroundColor(0xFFFF0000);
                        parkolasSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        parkolasSpinner.setBackgroundColor(0xFFFFFFFF);
                        parkolasSpinner.invalidate();
                    }

                    if (futesSpinner_int == 0) {
                        futesSpinner.setBackgroundColor(0xFFFF0000);
                        futesSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        futesSpinner.setBackgroundColor(0xFFFFFFFF);
                        futesSpinner.invalidate();
                    }

                    if (energiaSpinner_int == 0) {
                        energiaSpinner.setBackgroundColor(0xFFFF0000);
                        energiaSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        energiaSpinner.setBackgroundColor(0xFFFFFFFF);
                        energiaSpinner.invalidate();
                    }

                    if (kilatasSpinner_int == 0) {
                        kilatasSpinner.setBackgroundColor(0xFFFF0000);
                        kilatasSpinner.invalidate();
                        isFilledOut = false;
                    } else {
                        kilatasSpinner.setBackgroundColor(0xFFFFFFFF);
                        kilatasSpinner.invalidate();
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
                    //TODO: bútor spinner, erkély spinner

                    EstateUtil.addEstate(new SoapObjectResult() {
                                             @Override
                                             public void parseRerult(Object result) {
                                                 final ArrayList<EstateUtil> resArray = (ArrayList) result;

                                                 if (!resArray.get(resArray.size() - 1).isError()) {
                                                     //TODO: képfeltöltés...
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
                            String.valueOf(emeletekSpinner_int), String.valueOf(allapotSpinner_int), String.valueOf(szobaszamSpinner_int), String.valueOf(lng), String.valueOf(lat), estateTitle, String.valueOf(hirdetesSpinner_int));
                    break;
            }
        }
    }

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

        EstateUtil.getEstate(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                Log.d("GET_ESTATE Result: ", result.toString());
                JSONObject obj = (JSONObject) result;
                try {

                    title.setText(obj.getString("ingatlan_title"));
                    adress.setText(obj.getString("ingatlan_varos") + " " + obj.getString("ingatlan_utca"));
                    roomcount.setText(obj.getString("ingatlan_szsz"));
                    size.setText(obj.getString("ingatlan_meret"));
                    type.setText(obj.getString("ingatlan_tipus"));

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

                    Locale locale = new Locale("en", "UK");

                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                    //symbols.setDecimalSeparator(';');
                    symbols.setGroupingSeparator('.');

                    String pattern = "###,###";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                    String format = decimalFormat.format(obj.getInt("ingatlan_ar"));
                    price.setText(format + " Ft");

                    isFavEstate = obj.getBoolean("kedvenc");

                    favItem = (MenuView.ItemView) findViewById(R.id.action_fav);

                    if (isFavEstate) {
                        Log.d("ISFAV ", "TRUE");
                        favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_filled));
                    } else {
                        Log.d("ISFAV ", "FALSE");
                        favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_content));
                    }

                    item_realestate_description_text.setText(obj.getString("ingatlan_rovidleiras"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, String.valueOf(id), SettingUtil.getToken(getBaseContext()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab_phone.setVisibility(View.VISIBLE);

        switchLayoutTo(CONTENTESTATE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        findViewById(R.id.scrollView2).scrollTo(0, 0);
        supportInvalidateOptionsMenu();
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
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int));
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

    }

    EstateUtil estateUtil_fav;
    int estateID;
    private class ItemList implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EstateAdapter adapter = (EstateAdapter)parent.getAdapter();
            EstateUtil estateUtil = adapter.getItem(position);
            estateID = estateUtil.getId();
            estateUtil_fav = estateUtil;
            getEstateContent(estateID);
        }
    }

    private int pageCount = 0;
    private boolean isRefreshing = false;


    public void loadRealEstates(String idPost, String pagePost, final String tokenToSend, final String fav, final String etypeString, final String ordering) {
        pageCount = 0;
        isRefreshing = false;
        EstateUtil.largestId = 0;
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
                                EstateUtil.listEstates(new SoapObjectResult() {
                                    @Override
                                    public void parseRerult(Object result) {
                                        ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                                        adapter.addAll(arrayOfUsers);
                                        isRefreshing = true;
                                    }
                                }, lrgst, pageStr, tokenToSend, fav, etypeString, ordering);
                            }
                        }
                    }
                });
                swipeContainer.setRefreshing(false);
            }
        }, idPost, pagePost, tokenToSend, fav, etypeString, ordering);


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
                    loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int));
                }
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                fab.setVisibility(View.VISIBLE);
                fab_phone.setVisibility(View.INVISIBLE);
                break;
            case CONTENTESTATE:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                fab.setVisibility(View.INVISIBLE);
                fab_phone.setVisibility(View.VISIBLE);
                break;
            case ADDESTATE:
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
                //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
                fab.setVisibility(View.VISIBLE);
                break;
            case PROFILE:

                break;
            case MESSAGES:

                break;
            case BOOKING:

                break;
            default:
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int));
                fab.setVisibility(View.VISIBLE);
                fab_phone.setVisibility(View.INVISIBLE);
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

    MenuView.ItemView favItem;
    boolean isFavEstate = false;

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
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
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
                switchLayoutTo(MESSAGES);
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
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
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

        switch (id) {
            case R.id.nav_mainpage:
                isShowingFavorites = false;
                if (viewFlip.getDisplayedChild() != ESTATESLIST) {
                    switchLayoutTo(ESTATESLIST);
                }
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "0", String.valueOf(adType), String.valueOf(sortingSpinner_int));
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
                loadRealEstates("0", "0", SettingUtil.getToken(MainActivity.this), "1", String.valueOf(adType), String.valueOf(sortingSpinner_int));
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
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }





    ArrayList<Integer> prewViews = new ArrayList<Integer>();
    boolean isBackPressed = false;

    public void switchLayoutTo(int switchTo){
        if (viewFlip.getDisplayedChild() == switchTo) {
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
