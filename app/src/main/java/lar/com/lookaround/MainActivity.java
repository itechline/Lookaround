package lar.com.lookaround;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lar.com.lookaround.adapters.AddImageAdapter;
import lar.com.lookaround.adapters.AdmonitorAdapter;
import lar.com.lookaround.adapters.CalendarAdapter;
import lar.com.lookaround.adapters.CalendarBookingAdapter;
import lar.com.lookaround.adapters.EstateAdapter;
import lar.com.lookaround.adapters.MessageAdapter;
import lar.com.lookaround.adapters.SpinnerAdapter;
import lar.com.lookaround.models.UserModel;
import lar.com.lookaround.restapi.ImageUploadService;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.AddImageUtil;
import lar.com.lookaround.util.AdmonitorUtil;
import lar.com.lookaround.util.CalendarBookingUtil;
import lar.com.lookaround.util.EstateUtil;
import lar.com.lookaround.util.LoginUtil;
import lar.com.lookaround.util.MessageUtil;
import lar.com.lookaround.util.ScalingUtilities;
import lar.com.lookaround.util.SettingUtil;
import lar.com.lookaround.util.SpinnerUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EstateUtil currentEstate;

    DrawerLayout drawer;
    private int furniture_int = 0;
    private int lift_int = 0;
    private int balcony_int = 0;
    private int meret_int = 0;
    private int szobaMax_int = 0;
    private int szobaMin_int = 0;
    private int floorsMint_int = 0;
    private int floorsMax_int = 0;
    private int type_int = 0;
    private int allapot_int = 0;
    private int energigenyo_int = 0;
    private int panoramaSpinner_int = 0;
    private String price_from = "";
    private String price_to = "";
    private String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realestate);

        if (isNetworkAvailable()) {
            if (!SettingUtil.getToken(this).equals("")) {
                tokenValidation(true);
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

        currentEstate = null;

        // TODO: do it in XML
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view_search);
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);

        loadSearchSpinners();


        //loadAddEstateSpinners();

        //autocompleteSetter();
        // string array-be kéne rakni a resource-ból oszt menne csak valamiért mégse megy


        // Calendar now = Calendar.getInstance();

        //setCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH));

        //loadEstateImages();

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
                            Snackbar.make(count, "Üzenete érkezett!", Snackbar.LENGTH_LONG)
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
        if (whichMonth < 11) {
            whichMonth += 1;
            monthSetter += 1;
        } else {
            whichMonth = 0;
            monthSetter = 0;
            whichYear += 1;
        }

        setCalendar(whichYear, whichMonth);

    }

    public void prewMonth(View view) {
        if (whichMonth > 0) {
            whichMonth -= 1;
            monthSetter -= 1;
        } else {
            whichMonth = 11;
            monthSetter = 11;
            whichYear -= 1;
        }

        setCalendar(whichYear, whichMonth);

    }

    public void setCalendar(int year, int month) {
        final ArrayList<String> lst = new ArrayList<String>();

        final Calendar hlper = Calendar.getInstance();
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
                hlper.set(Calendar.DAY_OF_MONTH, Integer.valueOf(adapter.getItem(position)));

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
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CalendarBookingUtil u = (CalendarBookingUtil) appointments.get(i);
                        cba.getItem(i).setFoglalt(true);
                        cba.notifyDataSetChanged();

                        String mikor = hlper.get(Calendar.YEAR)+"-"+hlper.get(Calendar.MONTH)+"-"+hlper.get(Calendar.DAY_OF_MONTH)+" "+ u.getHours() +":"+ u.getMinutes() +":00";

                        EstateUtil.addIdopont(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                Snackbar.make(listView, "Sikeres foglalás!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }, SettingUtil.getToken(MainActivity.this), String.valueOf(currentEstate.getId()), mikor);
                    }
                });

                //final ArrayList<CalendarBookingUtil> appointmentsNew = (ArrayList) CalendarBookingUtil.getAppointments();
                //cba.addAll(appointmentsNew);

                //Toast.makeText(MainActivity.this, "" + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public void loadAddEstateSpinners() {
        //add_advert_type_spinner
        /*

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


    }*/
    }
    public void loadSearchSpinners() {
        TextView go = (TextView) findViewById(R.id.search_button_go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText keyword = (EditText) findViewById(R.id.keyword_realestate_search_edittext);
                EditText pf = (EditText) findViewById(R.id.realestate_price_edittext_min);
                EditText pt = (EditText) findViewById(R.id.realestate_price_edittext_max);

                price_from = pf.getText().toString();
                price_to = pt.getText().toString();
                key = keyword.getText().toString();

                closeDrawer();
                loadRealEstates();
            }
        });

        //hasfurniture_spinner
        ArrayList<SpinnerUtil> arrayListFurniture = (ArrayList) SpinnerUtil.get_list_butorozott();
        final SpinnerAdapter adapterFurniture = new SpinnerAdapter(MainActivity.this, arrayListFurniture);
        Spinner furniture = (Spinner) findViewById(R.id.hasfurniture_spinner);
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
        Spinner lift = (Spinner) findViewById(R.id.realestate_elevator_spinner);
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
        Spinner balcony = (Spinner) findViewById(R.id.realestate_balcony_spinner);
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
        Spinner meret = (Spinner) findViewById(R.id.realestate_size_spinner);
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

                Spinner szobaMin = (Spinner) findViewById(R.id.realestate_roomcount_min_spinner);
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

                Spinner szobaMax = (Spinner) findViewById(R.id.realestate_roomcount_max_spinner);
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

                Spinner floorsMin = (Spinner) findViewById(R.id.realestate_floors_min_spinner);
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

                Spinner floorsMax = (Spinner) findViewById(R.id.realestate_floors_max_spinner);
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

        SpinnerUtil.get_list_ingatlantipus(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                Spinner typeSpinner = (Spinner) findViewById(R.id.realestate_type_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typeSpinner.setAdapter(adapter);

                typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);


                        type_int = spinnerUtil.getId();
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                Spinner allapot = (Spinner) findViewById(R.id.condition_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                allapot.setAdapter(adapter);

                allapot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        allapot_int = spinnerUtil.getId();
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                Spinner enegigenyo = (Spinner) findViewById(R.id.ecertificate_search_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                enegigenyo.setAdapter(adapter);

                enegigenyo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        energigenyo_int = spinnerUtil.getId();
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
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                Spinner parkolasSpinner = (Spinner) findViewById(R.id.parking_type_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
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

        //ingatlan kilatas search spinner
        SpinnerUtil.get_list_ingatlankilatas(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                Spinner panoramaSpinner = (Spinner) findViewById(R.id.view_type_realestate_spinner);
                //ArrayAdapter<SpinnerUtil> adapter2 = new ArrayAdapter<Spinner>(getBaseContext(), android.R.layout.simple_spinner_item ,arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                panoramaSpinner.setAdapter(adapter);

                panoramaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(10);
                        SpinnerUtil spinnerUtil = adapter.getItem(position);
                        panoramaSpinner_int = spinnerUtil.getId();
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
        /*
        inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        estatesView = inflater.inflate(R.layout.content_main, null);
        contentRealestate = inflater.inflate(R.layout.content_realestate, null);
        invite = inflater.inflate(R.layout.content_invite, null);
        profile = inflater.inflate(R.layout.content_profile, null);
        messages = inflater.inflate(R.layout.content_messages, null);
        message2 = inflater.inflate(R.layout.content_message_thread, null);
        booking = inflater.inflate(R.layout.content_booking, null);
        admonitor = inflater.inflate(R.layout.content_admonitor, null);
        addAdmonitor = inflater.inflate(R.layout.content_add_admonitor, null);



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
        viewFlip.addView(addAdmonitor, ADDADMONITOR);

        viewFlipAddEstate = (ViewFlipper) findViewById(R.id.viewFlipperAddEstate);

        viewFlipAddEstate.addView(addEstate1, 0);
        viewFlipAddEstate.addView(addEstate2, 1);
        viewFlipAddEstate.addView(addEstate3, 2);
        viewFlipAddEstate.addView(addEstate4, 3);
        viewFlipAddEstate.addView(addEstate5, 4);



        viewFlip.setDisplayedChild(ESTATESLIST);*/
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
    //TODO: max 10 kép engedlyezése
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
            mon = 0;
        } else {
            day1text.setTextColor(Color.parseColor("#FFFFFF"));
            day1text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay1 = true;
            mon = 1;
        }
    }

    public void daypicker2(View view) {
        TextView day2text = (TextView) findViewById(R.id.daypicker_day2);
        if (isDay2) {
            day2text.setTextColor(Color.parseColor("#000000"));
            day2text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay2 = false;
            tue = 0;
        } else {
            day2text.setTextColor(Color.parseColor("#FFFFFF"));
            day2text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay2 = true;
            tue = 1;
        }
    }

    public void daypicker3(View view) {
        TextView day3text = (TextView) findViewById(R.id.daypicker_day3);
        if (isDay3) {
            day3text.setTextColor(Color.parseColor("#000000"));
            day3text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay3 = false;
            wed = 0;
        } else {
            day3text.setTextColor(Color.parseColor("#FFFFFF"));
            day3text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay3 = true;
            wed = 1;
        }
    }

    public void daypicker4(View view) {
        TextView day4text = (TextView) findViewById(R.id.daypicker_day4);
        if (isDay4) {
            day4text.setTextColor(Color.parseColor("#000000"));
            day4text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay4 = false;
            thu = 0;
        } else {
            day4text.setTextColor(Color.parseColor("#FFFFFF"));
            day4text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay4 = true;
            thu = 1;
        }
    }

    public void daypicker5(View view) {
        TextView day5text = (TextView) findViewById(R.id.daypicker_day5);
        if (isDay5) {
            day5text.setTextColor(Color.parseColor("#000000"));
            day5text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay5 = false;
            fri = 0;
        } else {
            day5text.setTextColor(Color.parseColor("#FFFFFF"));
            day5text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay5 = true;
            fri = 1;
        }
    }

    public void daypicker6(View view) {
        TextView day6text = (TextView) findViewById(R.id.daypicker_day6);
        if (isDay6) {
            day6text.setTextColor(Color.parseColor("#000000"));
            day6text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay6 = false;
            sat = 0;
        } else {
            day6text.setTextColor(Color.parseColor("#FFFFFF"));
            day6text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay6 = true;
            sat = 1;
        }
    }

    public void daypicker7(View view) {
        TextView day7text = (TextView) findViewById(R.id.daypicker_day7);
        if (isDay7) {
            day7text.setTextColor(Color.parseColor("#000000"));
            day7text.setBackgroundColor(Color.parseColor("#FFFFFF"));
            isDay7 = false;
            sun = 0;
        } else {
            day7text.setTextColor(Color.parseColor("#FFFFFF"));
            day7text.setBackgroundColor(Color.parseColor("#0066CC"));
            isDay7 = true;
            sun = 1;
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
            int idx = cursor.getColumnIndex( MediaStore.MediaColumns.DATA );
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                uris.add(data.getData());

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                AddImageUtil.addImage(imageID, ScalingUtilities.createScaledBitmap(bitmap, 200, 200, ScalingUtilities.ScalingLogic.CROP));

                final LinearLayout linearLayoutGallery = (LinearLayout) findViewById(R.id.uploaded_images_linearlayout);

                ArrayList<AddImageUtil> allImages = AddImageUtil.getAllImages();
                final AddImageAdapter adapter = new AddImageAdapter(MainActivity.this, allImages);

                View galleryImage = adapter.getView(imageID, null, null);

                final AddImageUtil addImageUtil = adapter.getItem(galleryImageID);

                galleryImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                            TakeImage(layout);
                        } else {
                            pickImage(layout);
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

    int hour_x = 0;
    int minute_x = 0;
    int hour_x_end = 0;
    int minute_x_end = 0;

    int adType = 0;
    public void adTypeChange(View view) {
        adType += 1;
        switch (adType) {
            case 1:
                ing_type = 1;
                loadRealEstates();
                break;
            case 2:
                ing_type = 2;
                loadRealEstates();
                break;
            default:
                ing_type = 0;
                loadRealEstates();
                adType = 0;
                break;
        }
    }



    public void doVR(View view) {
        EstateUtil.addVR(SettingUtil.getToken(getBaseContext()));
    }

    public void seeOnMap(View view) {
        if (latMap != null || !latMap.equals("0.0")) {
            SettingUtil.setLatForMap(getBaseContext(), latMap);
        }
        if (lngMap != null || !lngMap.equals("0.0")) {
            SettingUtil.setLngForMap(getBaseContext(), lngMap);
        }

        if (SettingUtil.getLatForMap(getBaseContext()) != null && SettingUtil.getLngForMap(getBaseContext()) != null && !SettingUtil.getLatForMap(getBaseContext()).equals("0.0") && !SettingUtil.getLngForMap(getBaseContext()).equals("0.0")) {
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

    private int whichAddestatePage = 0;

    private boolean isAddingEstate = false;

    private int mon = 0;
    private int tue = 0;
    private int wed = 0;
    private int thu = 0;
    private int fri = 0;
    private int sat = 0;
    private int sun = 0;
    private String start = "0";
    private String finish = "0";
    private int hirdetesSpinner_int = 0;
    private int butorozottSpinner_int = 0;
    private int szobaszamSpinner_int = 0;
    private int allapotSpinner_int = 0;
    private int emeletekSpinner_int = 0;
    private int ingatlanTipusSpinner_int = 0;
    private int parkolasSpinner_int = 0;
    private int futesSpinner_int = 0;
    private int energiaSpinner_int = 0;
    private int kilatasSpinner_int = 0;
    private int balconySpinner_int = 0;
    private int elevatorSpinner_int = 0;

    public void nextAddestatePage(int whichAddestatePage) {
        if (whichAddestatePage <= 5) {

            final ViewFlipper viewFlipAddEstate = (ViewFlipper) findViewById(R.id.viewFlipperAddEstate);
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            boolean isFilledOut = true;
            switch (whichAddestatePage) {
                case 0:
                    viewFlipAddEstate.setDisplayedChild(whichAddestatePage);


                    TextView title = (TextView) findViewById(R.id.adverttitle_edittext);
                    TextView description = (TextView) findViewById(R.id.advert_description_edittext);
                    TextView price = (TextView) findViewById(R.id.add_advert_price_edittext);
                    TextView city = (TextView) findViewById(R.id.add_advert_city_edittext);

                    TextView street = (TextView) findViewById(R.id.add_advert_street_edittext);
                    TextView num = (TextView) findViewById(R.id.add_advert_str_number_edittext);
                    TextView size = (TextView) findViewById(R.id.estate_size_edittext);

                    title.setText(estateTitle);
                    description.setText(estateDescription);
                    price.setText(estatePrice);
                    city.setText(estateCity);
                    street.setText(estateStreet);
                    num.setText(estetaHouseNumber);
                    size.setText(estateSize);

                    TextView next = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.advert1_next_button);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextAddestatePage(1);
                        }
                    });

                    final Spinner hirdetesSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.add_advert_type_spinner);

                    SpinnerUtil.get_list_hirdetestipusa(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            ArrayList<SpinnerUtil> arrayList = (ArrayList) result;
                            final SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, arrayList);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            hirdetesSpinner.setAdapter(adapter);
                            for(int i = 0; i < arrayList.size(); i++) {
                                if(hirdetesSpinner_int == arrayList.get(i).getId())
                                    hirdetesSpinner.setSelection(i);
                            }

                            hirdetesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                                }
                            });

                        }
                    });

                    ArrayList<SpinnerUtil> arrayListButor = (ArrayList) SpinnerUtil.get_list_butorozott();
                    final SpinnerAdapter adapterButor = new SpinnerAdapter(MainActivity.this, arrayListButor);
                    final Spinner butorozottSpinner = (Spinner) viewFlipAddEstate.getCurrentView().findViewById(R.id.add_advert_furniture_spinner);
                    adapterButor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    butorozottSpinner.setAdapter(adapterButor);

                    for(int i = 0; i < arrayListButor.size(); i++) {
                        if(butorozottSpinner_int == arrayListButor.get(i).getId())
                            butorozottSpinner.setSelection(i);
                    }

                    butorozottSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

                    break;
                case 1:
                    viewFlipAddEstate.setDisplayedChild(0);
                    TextView title2 = (TextView) findViewById(R.id.adverttitle_edittext);
                    TextView description2 = (TextView) findViewById(R.id.advert_description_edittext);
                    TextView price2 = (TextView) findViewById(R.id.add_advert_price_edittext);
                    TextView city2 = (TextView) findViewById(R.id.add_advert_city_edittext);

                    TextView street2 = (TextView) findViewById(R.id.add_advert_street_edittext);
                    TextView num2 = (TextView) findViewById(R.id.add_advert_str_number_edittext);
                    TextView size2 = (TextView) findViewById(R.id.estate_size_edittext);

                    estateTitle = title2.getText().toString();
                    estateDescription = description2.getText().toString();
                    estatePrice = price2.getText().toString();
                    estateCity = city2.getText().toString();

                    estateStreet = street2.getText().toString();
                    estetaHouseNumber = num2.getText().toString();
                    estateSize = size2.getText().toString();

                    if(!isValidString(estateTitle)) {
                        title2.setError("Hiba!");
                        title2.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateDescription)) {
                        description2.setError("Hiba!");
                        description2.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estatePrice)) {
                        price2.setError("Hiba!");
                        price2.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateCity)) {
                        city2.setError("Hiba!");
                        city2.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateStreet)) {
                        street2.setError("Hiba!");
                        street2.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estetaHouseNumber)) {
                        num2.setError("Hiba!");
                        num2.invalidate();
                        isFilledOut = false;
                    }

                    if(!isValidString(estateSize)) {
                        size2.setError("Hiba!");
                        size2.invalidate();
                        isFilledOut = false;
                    }

                    Spinner hirdetesSpinner_ = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.add_advert_type_spinner);
                    if (hirdetesSpinner_int == 0) {
                        hirdetesSpinner_.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        hirdetesSpinner_.invalidate();
                        isFilledOut = false;
                    } else {
                        hirdetesSpinner_.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        hirdetesSpinner_.invalidate();
                    }

                    Spinner butorozottSpinner_ = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.add_advert_furniture_spinner);
                    if (butorozottSpinner_int == 0) {
                        butorozottSpinner_.setBackground(getResources().getDrawable(R.drawable.buttondelete_border));
                        butorozottSpinner_.invalidate();
                        isFilledOut = false;
                    } else {
                        butorozottSpinner_.setBackground(getResources().getDrawable(R.drawable.spinner_border));
                        butorozottSpinner_.invalidate();
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

                        viewFlipAddEstate.setDisplayedChild(whichAddestatePage);

                        TextView next2 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.button_go_page2);
                        next2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextAddestatePage(2);
                            }
                        });

                        TextView back2 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.back_button_page2);
                        back2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextAddestatePage(0);
                            }
                        });

                        Spinner szobaszamSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_rooms_spinner);
                        Spinner allapotSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_condition_spinner);
                        Spinner emeletekSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_floors_spinner);
                        Spinner ingatlanTipusSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_type_spinner);
                        Spinner parkolasSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_parking_spinner);
                        Spinner futesSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_heatingtype_spinner);
                        Spinner energiaSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_ecertificate_spinner);
                        Spinner kilatasSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.adestate_view_spinner);
                        Spinner balconySpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_balcony_spinner);
                        Spinner elevatorSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_elevator_spinner);

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                szobaszamSpinner_int = util.getId();
                            }
                        }, szobaszamSpinner, szobaszamSpinner_int, "list_ingatlanszoba");

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil) result;
                                allapotSpinner_int = util.getId();
                            }
                        }, allapotSpinner, allapotSpinner_int, "list_ingatlanallapota");

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                emeletekSpinner_int = util.getId();
                            }
                        }, emeletekSpinner, emeletekSpinner_int, "list_ingatlanemelet");

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                ingatlanTipusSpinner_int = util.getId();
                            }
                        }, ingatlanTipusSpinner, ingatlanTipusSpinner_int, "list_ingatlantipus");

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                parkolasSpinner_int = util.getId();
                            }
                        }, parkolasSpinner, parkolasSpinner_int, "list_ingatlanparkolas");

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                futesSpinner_int = util.getId();
                            }
                        }, futesSpinner, futesSpinner_int, "list_ingatlanfutes");

                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                energiaSpinner_int = util.getId();
                            }
                        }, energiaSpinner, energiaSpinner_int, "list_ingatlanenergia");
                        SpinnerUtil.get_list(new SoapObjectResult() {
                            @Override
                            public void parseRerult(Object result) {
                                SpinnerUtil util = (SpinnerUtil)result;
                                kilatasSpinner_int = util.getId();
                            }
                        }, kilatasSpinner, kilatasSpinner_int, "list_ingatlankilatas");

                        ArrayList<SpinnerUtil> balclist = (ArrayList) SpinnerUtil.get_list_erkely();
                        final SpinnerAdapter adapter2 = new SpinnerAdapter(MainActivity.this, balclist);
                        balconySpinner.setAdapter(adapter2);

                        for(int i = 0; i < balclist.size(); i++) {
                            if(balconySpinner_int == balclist.get(i).getId())
                                balconySpinner.setSelection(i);
                        }

                        balconySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SpinnerUtil spinnerUtil = adapter2.getItem(position);
                                balconySpinner_int = spinnerUtil.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        ArrayList<SpinnerUtil> list3 = (ArrayList) SpinnerUtil.get_list_erkely();
                        final SpinnerAdapter adapter3 = new SpinnerAdapter(MainActivity.this, list3);
                        elevatorSpinner.setAdapter(adapter3);

                        for(int i = 0; i < list3.size(); i++) {
                            if(elevatorSpinner_int == list3.get(i).getId())
                                elevatorSpinner.setSelection(i);
                        }

                        elevatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                SpinnerUtil spinnerUtil = adapter3.getItem(position);
                                elevatorSpinner_int = spinnerUtil.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        setAddestatePageIndicator(whichAddestatePage);
                    } else {
                        whichAddestatePage = 0;
                    }
                    break;

                case 2:
                    viewFlipAddEstate.setDisplayedChild(1);
                    Spinner szobaszamSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_rooms_spinner);
                    Spinner allapotSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_condition_spinner);
                    Spinner emeletekSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_floors_spinner);
                    Spinner ingatlanTipusSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_type_spinner);
                    Spinner parkolasSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_parking_spinner);
                    Spinner futesSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_heatingtype_spinner);
                    Spinner energiaSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_ecertificate_spinner);
                    Spinner kilatasSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.adestate_view_spinner);
                    Spinner balconySpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_balcony_spinner);
                    Spinner elevatorSpinner = (Spinner)viewFlipAddEstate.getCurrentView().findViewById(R.id.addestate_elevator_spinner);

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
                        viewFlipAddEstate.setDisplayedChild(whichAddestatePage);


                        TextView next2 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.button_go_page3);
                        next2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextAddestatePage(3);
                            }
                        });

                        TextView back2 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.back_button_page3);
                        back2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextAddestatePage(1);
                            }
                        });


                    } else {
                        whichAddestatePage = 1;
                    }
                    break;

                case 3:
                    setAddestatePageIndicator(whichAddestatePage);
                    viewFlipAddEstate.setDisplayedChild(whichAddestatePage);


                    TextView next2 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.button_go_page4);
                    next2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextAddestatePage(4);
                        }
                    });

                    TextView back2 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.back_button_page4);
                    back2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nextAddestatePage(2);
                        }
                    });

                    break;

                case 4:
                    boolean isCorrect = true;
                    boolean isCorrect2 = true;
                    if (isDay1 || isDay2 || isDay3 || isDay4 || isDay5 || isDay6 || isDay7) {
                        if (hour_x == 0 || hour_x_end == 0) {
                            isCorrect = false;
                        }
                    }

                    if (hour_x != 0) {
                        if (!isDay1 && !isDay2 && !isDay3 && !isDay4 && !isDay5 && !isDay6 && !isDay7) {
                            isCorrect2 = false;
                        }
                    }

                    if (hour_x_end != 0) {
                        if (!isDay1 && !isDay2 && !isDay3 && !isDay4 && !isDay5 && !isDay6 && !isDay7) {
                            isCorrect2 = false;
                        }
                    }

                    if (isCorrect && isCorrect2) {
                        Log.e("ERROR", "page4");
                        setAddestatePageIndicator(whichAddestatePage);

                        viewFlipAddEstate.setDisplayedChild(whichAddestatePage);


                        TextView next_5 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.button_go_page5);
                        next_5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("ERROR", "click");
                                nextAddestatePage(5);
                            }
                        });

                        TextView back4 = (TextView) viewFlipAddEstate.getCurrentView().findViewById(R.id.back_button_page5);
                        back4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextAddestatePage(3);
                            }
                        });

                    } else {
                        if (!isCorrect && !isCorrect2) {
                            Snackbar.make(viewFlipAddEstate.getCurrentView(), "Hibás időpont és nap adatok!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        if (isCorrect && !isCorrect2) {
                            Snackbar.make(viewFlipAddEstate.getCurrentView(), "Hibás nap adatok!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        if (!isCorrect && isCorrect2) {
                            Snackbar.make(viewFlipAddEstate.getCurrentView(), "Hibás időpont adatok!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        whichAddestatePage = 3;
                    }
                    break;

                case 5:
                    Log.e("ERROR", "ERROR");
                    start = String.valueOf(hour_x) + ":" + String.valueOf(minute_x);
                    finish = String.valueOf(hour_x_end) + ":" + String.valueOf(minute_x_end);
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
                                                     mon = 0;
                                                     tue = 0;
                                                     wed = 0;
                                                     thu = 0;
                                                     fri = 0;
                                                     sat = 0;
                                                     sun = 0;
                                                     start = "0";
                                                     finish = "0";
                                                     estateTitle = "";
                                                     estateDescription = "";
                                                     estatePrice = "";
                                                     estateCity = "";
                                                     estateStreet = "";
                                                     estetaHouseNumber = "";
                                                     estateSize = "";

                                                     getEstateContent(resArray.get(resArray.size() - 1).getId());
                                                     Snackbar.make(viewFlipAddEstate.getCurrentView(), "Hirdetés feladva!", Snackbar.LENGTH_LONG)
                                                             .setAction("Action", null).show();
                                                     Log.d("ADDESTATE_HASH: ", resArray.get(0).getHash());
                                                 } else {
                                                     showAlertError("Sikertelen feltöltés");

                                                 }

                                             }
                                         }, estateSize, estateCity, estateStreet, estetaHouseNumber, estateDescription, estatePrice,
                            String.valueOf(energiaSpinner_int), String.valueOf(butorozottSpinner_int), String.valueOf(kilatasSpinner_int), String.valueOf(elevatorSpinner_int),
                            String.valueOf(futesSpinner_int), String.valueOf(parkolasSpinner_int), String.valueOf(balconySpinner_int), String.valueOf(ingatlanTipusSpinner_int),
                            String.valueOf(emeletekSpinner_int), String.valueOf(allapotSpinner_int), String.valueOf(szobaszamSpinner_int),
                            String.valueOf(lng), String.valueOf(lat), estateTitle, String.valueOf(hirdetesSpinner_int), SettingUtil.getToken(getBaseContext()), zipcodeToSend,
                            String.valueOf(mon), String.valueOf(tue), String.valueOf(wed), String.valueOf(thu), String.valueOf(fri), String.valueOf(sat), String.valueOf(sun), start, finish, updateingid);
                    break;
            }

        }

    }

    public void loadAddEstateBookingSpinners() {
        ArrayList<SpinnerUtil> arrayListStart = (ArrayList) SpinnerUtil.get_list_booking_start();
        final SpinnerAdapter adapterStart = new SpinnerAdapter(MainActivity.this, arrayListStart);
        Spinner startSpinner = (Spinner) findViewById(R.id.booking_time_start_spinner);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startSpinner.setAdapter(adapterStart);

        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterStart.getItem(position);
                start = spinnerUtil.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });

        ArrayList<SpinnerUtil> arrayListFinish = (ArrayList) SpinnerUtil.get_list_booking_finish();
        final SpinnerAdapter adapterFurniture = new SpinnerAdapter(MainActivity.this, arrayListFinish);
        Spinner finishSpinner = (Spinner) findViewById(R.id.booking_time_end_spinner);
        adapterFurniture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        finishSpinner.setAdapter(adapterFurniture);

        finishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
                SpinnerUtil spinnerUtil = adapterFurniture.getItem(position);
                finish = spinnerUtil.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextSize(10);
            }
        });
    }

    public void uploadImages(String hash) {
        ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("Feltöltés...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        ArrayList<File> files = new ArrayList<>();
        for (int j = 0; j < uris.size(); j++) {
            File imageFile = new File(getRealPathFromURI(uris.get(j)));
            files.add(imageFile);
        }
        try {
            ImageUploadService service = new ImageUploadService(files, progressBar);
            service.execute(hash);
        } catch (Exception e) {
            e.printStackTrace();
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

    /*private void call(final int id) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.areyousure_popup, null);

        TextView textView = (TextView) popupView.findViewById(R.id.areyousure_delete_textView);
        textView.setText("Jelenti a hírdetést ?");

        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT,
                true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        ((Button) popupView.findViewById(R.id.delete_ad_yes_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });

        ((Button) popupView.findViewById(R.id.delete_ad_no_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });
    }*/

    private void callSertoPopup(final int id) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.areyousure_popup, null);

        TextView textView = (TextView) popupView.findViewById(R.id.areyousure_delete_textView);
        textView.setText("Jelenti a hírdetést ?");

        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT,
                true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        ((Button) popupView.findViewById(R.id.delete_ad_yes_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        EstateUtil.addSerto(SettingUtil.getToken(getBaseContext()), id);
                        popupWindow.dismiss();
                    }
                });

        ((Button) popupView.findViewById(R.id.delete_ad_no_button))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        popupWindow.dismiss();
                    }
                });
    }

    public void getEstateContent(final int id) {

        LayoutInflater inflater = (LayoutInflater)   getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentRealestate = inflater.inflate(R.layout.content_realestate, null);
        ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(contentRealestate, 0);

        viewFlip.setDisplayedChild(0);

        menuType = ESTATE_MENU;
        supportInvalidateOptionsMenu();


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
        RelativeLayout serto = (RelativeLayout) findViewById(R.id.item_offensive_layout);

        serto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSertoPopup(id);
            }
        });

        final TextView item_realestate_description_text = (TextView)findViewById(R.id.item_realestate_description_text);

        final TextView name = (TextView) findViewById(R.id.profile_name_text);
        final TextView profile_type = (TextView) findViewById(R.id.profile_type_text);

        EstateUtil.getEstate(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                JSONObject obj = (JSONObject) result;
                try {

                    JSONArray kepekArray = new JSONArray(obj.getString("kepek"));
                    List<String> imageUrls = new ArrayList<String>();
                    imageUrls.clear();
                    for (int j=0; j < kepekArray.length(); j++) {
                        JSONObject jsonKep = kepekArray.getJSONObject(j);
                        imageUrls.add(jsonKep.getString("kepek_url"));
                    }

                    SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.slider);
                    RelativeLayout item_realestate_profile_layout = (RelativeLayout) findViewById(R.id.item_realestate_profile_layout);
                    if(imageUrls.size() != 0) {
                        sliderLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 250, getResources().getDisplayMetrics()))));

                        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)item_realestate_profile_layout.getLayoutParams();
                        relativeParams.setMargins(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 250, getResources().getDisplayMetrics())), 0, 0);  // left, top, right, bottom
                        item_realestate_profile_layout.setLayoutParams(relativeParams);

                        loadEstateImages(imageUrls);
                    } else {
                        sliderLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

                        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)item_realestate_profile_layout.getLayoutParams();
                        item_realestate_profile_layout.setLayoutParams(relativeParams);
                        relativeParams.setMargins(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 0, getResources().getDisplayMetrics())), 0, 0);  // left, top, right, bottom
                    }

                    title.setText(obj.getString("ingatlan_title"));
                    adress.setText(obj.getString("ingatlan_varos") + " " + obj.getString("ingatlan_utca"));
                    roomcount.setText(obj.getString("ingatlan_szsz"));
                    size.setText(obj.getString("ingatlan_meret"));
                    type.setText(obj.getString("ingatlan_tipus"));
                    item_realestate_description_text.setText(obj.getString("ingatlan_rovidleiras"));

                    name.setText(obj.getString("vezeteknev") + " " + obj.getString("keresztnev"));
                    profile_type.setText(obj.getString("tipus"));
                    String mobileNum = obj.getString("mobil");

                    Locale locale = new Locale("en", "UK");

                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
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


                    boolean isFavEstate = obj.getBoolean("kedvenc");

                    MenuView.ItemView favItem = (MenuView.ItemView) findViewById(R.id.action_fav);

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
                        latMap = obj.getString("ingatlan_lat");
                    }

                    if (obj.getString("ingatlan_lng") != null || !obj.getString("ingatlan_lat").equals("0.0")) {
                        lngMap = obj.getString("ingatlan_lng");
                    }

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

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        supportInvalidateOptionsMenu();

        //if (viewFlip.getDisplayedChild() != CONTENTESTATE) {
        //    //TODO: megtekintés térképen->infoWindow->MainActivity ContentEstate fav icon bug
        //    switchLayoutTo(CONTENTESTATE);
        //    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        //    supportInvalidateOptionsMenu();
        //}
        findViewById(R.id.scrollView2).scrollTo(0, 0);

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


    public void tokenValidation(final boolean reload) {
        LoginUtil.tokenValidator(this, new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                UserModel model = (UserModel)result;
                if (!model.isError()) {
                    justme = false;
                    isFavorite = false;
                    ing_ordering = 0;
                    ing_type = 0;

                    TextView tv = (TextView)findViewById(R.id.user_fullname);
                    ImageView iw = (ImageView)findViewById(R.id.imageViewProfile);

                    iw.setImageResource(R.drawable.avatar);
                    tv.setText(model.getVezeteknev() + " " + model.getKeresztnev());

                    if(reload)
                        loadRealEstates();
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

            }
        }, SettingUtil.getToken(this));
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
                tokenValidation(false);
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        } else {
            showAlert();
        }

    }
    public void showAlertError(String msg) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(msg)
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

    public void showAlert() {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("Nincs internet! :(")
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

            sliderLayout.addSlider(defaultSliderView);

            sliderLayout.destroyDrawingCache();
        }
        urls.clear();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    private class ItemList implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EstateAdapter adapter = (EstateAdapter)parent.getAdapter();
            EstateUtil estateUtil = adapter.getItem(position);
            currentEstate = estateUtil;
            getEstateContent(currentEstate.getId());
        }
    }

    private int pageCount = 0;
    private boolean isRefreshing = false;

    public void loadMessagesForEstate(final String hash, int uid) {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);

        //messages = inflater.inflate(R.layout.content_messages, null);
        //message2 = inflater.inflate(R.layout.content_message_thread, null);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_message_thread, null);
        final ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab.setVisibility(View.INVISIBLE);
        fab_phone.setVisibility(View.INVISIBLE);

        viewFlip.setDisplayedChild(0);

        menuType = MESSAGES_THREAD_MENU;
        supportInvalidateOptionsMenu();

        MessageUtil.listMessagesForEstate(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<MessageUtil> lst = (ArrayList<MessageUtil>) result;
                ListView thread = (ListView) findViewById(R.id.messagethread);

                final MessageAdapter adapter = new MessageAdapter(MainActivity.this, lst);

                thread.setAdapter(adapter);
                thread.setDividerHeight(0);

                final EditText et = (EditText) findViewById(R.id.write_message_edittext);
                RelativeLayout send = (RelativeLayout) findViewById(R.id.sent_message_text_rlayout);
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

    public void showMyFavs(View view) {
        isFavorite = true;
        justme = false;
        ing_type = 0;
        ing_ordering = 0;

        furniture_int = 0;
        lift_int = 0;
        balcony_int = 0;
        meret_int = 0;
        szobaMax_int = 0;
        szobaMin_int = 0;
        floorsMint_int = 0;
        floorsMax_int = 0;
        type_int = 0;
        allapot_int = 0;
        energigenyo_int = 0;
        panoramaSpinner_int = 0;

        price_from = "";
        price_to = "";
        key = "";

        loadRealEstates();
        closeDrawer();
    }

    boolean isFavorite = false;
    int ing_type = 0;
    int ing_ordering = 0;
    boolean justme = false;
    int updateingid = 0;
    public void loadRealEstates() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_istview, null);
        ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);

        viewFlip.setDisplayedChild(0);

        getSupportActionBar().setTitle("Hirdetések");
        if(isFavorite) {
            getSupportActionBar().setTitle("Kedvencek");
        }
        if(justme) {
            fab.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Hírdetéseim");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        }

        ArrayList<SpinnerUtil> arrayListSorting = (ArrayList) SpinnerUtil.get_list_szures();
        final SpinnerAdapter adapterSorting = new SpinnerAdapter(MainActivity.this, arrayListSorting);
        Spinner sortingSpinner = (Spinner) findViewById(R.id.sort_estates_spinner);
        adapterSorting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(adapterSorting);
        for (int i = 0; i < arrayListSorting.size(); i++) {
            if(arrayListSorting.get(i).getId() == ing_ordering) {
                sortingSpinner.setSelection(i);
            }
        }

        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerUtil spinnerUtil = adapterSorting.getItem(position);
                if(ing_ordering != spinnerUtil.getId()) {
                    ing_ordering = spinnerUtil.getId();
                    loadRealEstates();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TextView typeText = (TextView) viewFlip.getCurrentView().findViewById(R.id.estate_type_textview);
        switch (adType) {
            case 1:
                typeText.setText("Eladó");
                break;
            case 2:
                typeText.setText("Kiadó");
                break;
            default:
                typeText.setText("Mindegy");
                break;
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menuicon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crudEstate(null);
            }
        });
        fab.setVisibility(View.VISIBLE);

        fab_phone.setVisibility(View.INVISIBLE);

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setRefreshing(true);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRealEstates();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        pageCount = 0;
        isRefreshing = false;
        EstateUtil.largestId = 0;
        updateingid = 0;
        EstateUtil.listEstates(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                final ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                final EstateAdapter adapter = new EstateAdapter(MainActivity.this, arrayOfUsers, new SoapObjectResult() {
                    @Override
                    public void parseRerult(Object result) {
                        //TODO: szerkeszt
                        EstateUtil util = (EstateUtil)result;
                        crudEstate(util);
                    }
                });
                final ListView listView = (ListView) findViewById(R.id.estateListView);
                if(listView == null)
                    return;
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
                                pageCount += 1;
                                isRefreshing = true;
                                String pageStr = String.valueOf(pageCount);
                                String lrgst = String.valueOf(EstateUtil.largestId);
                                EstateUtil.listEstates(new SoapObjectResult() {
                                    @Override
                                    public void parseRerult(Object result) {
                                        ArrayList<EstateUtil> arrayOfUsers = (ArrayList) result;
                                        if(arrayOfUsers.size() == 0) {

                                        } else {
                                            adapter.addAll(arrayOfUsers);
                                            isRefreshing = false;
                                        }
                                    }
                                }, lrgst, pageStr, SettingUtil.getToken(MainActivity.this), isFavorite ? "1" : "0", String.valueOf(ing_type), String.valueOf(ing_ordering), justme ? "1" : "0",
                                        furniture_int, lift_int, balcony_int, meret_int, szobaMax_int, szobaMin_int, floorsMax_int, floorsMint_int,
                                        type_int, allapot_int, energigenyo_int, panoramaSpinner_int, parkolasSpinner_int, price_from, price_to, key);
                            }
                        }
                    }
                });
                if (swipeContainer != null)
                    swipeContainer.setRefreshing(false);
            }
        }, String.valueOf(0), String.valueOf(pageCount), SettingUtil.getToken(this), isFavorite ? "1" : "0", String.valueOf(ing_type), String.valueOf(ing_ordering), justme ? "1" : "0",
                furniture_int, lift_int, balcony_int, meret_int, szobaMax_int, szobaMin_int, floorsMax_int, floorsMint_int,
                type_int, allapot_int, energigenyo_int, panoramaSpinner_int, parkolasSpinner_int, price_from, price_to, key);

        menuType = MAIN_MENU;
        supportInvalidateOptionsMenu();
    }

    public void loadBooking() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_booking, null);
        ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        viewFlip.setDisplayedChild(0);

        Calendar hlper = Calendar.getInstance();

        setCalendar(hlper.get(Calendar.YEAR), hlper.get(Calendar.MONTH));

    }

    public void crudEstate(EstateUtil util) {

        if(util == null) {
            updateingid = 0;
        } else {
            updateingid = util.getId();
            hirdetesSpinner_int = util.getIng_e_type_id();
            szobaszamSpinner_int = util.getIngatlan_szsz_id();
            allapotSpinner_int = util.getIngatlan_allapot_id();
            emeletekSpinner_int = util.getIngatlan_emelet_id();
            ingatlanTipusSpinner_int = util.getIngatlan_tipus_id();
            parkolasSpinner_int = util.getIngatlan_parkolas_id();
            futesSpinner_int = util.getIngatlan_futestipus_id();
            energiaSpinner_int = util.getIngatlan_energiatan_id();
            kilatasSpinner_int = util.getIngatlan_kilatas_id();
            butorozottSpinner_int = util.getButor();
            balconySpinner_int = util.getErkely();
            elevatorSpinner_int = util.getLift();

            estateTitle = util.getEstateTitle();
            estateDescription = util.getEstateDescription();
            estatePrice = util.getEstatePrice();
            estateCity = util.getEstateCity();
            estateStreet = util.getEstateStreet();
            estetaHouseNumber = util.getEstetaHouseNumber();
            estateSize = util.getEstateSize();

            mon = 0;
            tue = 0;
            wed = 0;
            thu = 0;
            fri = 0;
            sat = 0;
            sun = 0;
            start = "0";
            finish = "0";
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_addrealestate, null);
        ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        viewFlip.setDisplayedChild(0);

        View addEstate1 = inflater.inflate(R.layout.content_addrealestate_page1, null);
        View addEstate2 = inflater.inflate(R.layout.content_addrealestate_page2, null);
        View addEstate3 = inflater.inflate(R.layout.content_addrealestate_page3, null);
        View addEstate4 = inflater.inflate(R.layout.content_addrealestate_page4, null);
        View addEstate5 = inflater.inflate(R.layout.content_addrealestate_page5, null);

        ViewFlipper viewFlipAddEstate = (ViewFlipper) findViewById(R.id.viewFlipperAddEstate);

        viewFlipAddEstate.addView(addEstate1, 0);
        viewFlipAddEstate.addView(addEstate2, 1);
        viewFlipAddEstate.addView(addEstate3, 2);
        viewFlipAddEstate.addView(addEstate4, 3);
        viewFlipAddEstate.addView(addEstate5, 4);

        getSupportActionBar().setTitle("Új hírdetés");
        loadAddEstateBookingSpinners();
        setAddestatePageIndicator(0);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        nextAddestatePage(0);

        menuType = ADD_ESTATE_MENU;
        supportInvalidateOptionsMenu();
    }




    static final int MAIN_MENU = 0;
    static final int ESTATE_MENU = 1;
    static final int PROFILE_MENU = 2;
    static final int INVITE_MENU = 3;
    static final int MESSAGES_MENU = 4;
    static final int MESSAGES_THREAD_MENU = 5;
    static final int ADD_ESTATE_MENU = 6;

    static final int FIGYELO_MENU = 7;
    static final int ADD_FIGYELO_MENU = 8;

    int menuType = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (menuType) {
            case MAIN_MENU:
                getMenuInflater().inflate(R.menu.main, menu);
                break;
            case ESTATE_MENU:
                getMenuInflater().inflate(R.menu.menu_realestate, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.main, menu);
                break;
        }
        return true;
    }

    View header;
    public void admonitorList() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.show();

        EstateUtil.listFigyelo(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if (pd != null) {
                    pd.dismiss();
                }

                ArrayList<AdmonitorUtil> arrayList = (ArrayList) result;

                final AdmonitorAdapter adapter = new AdmonitorAdapter(MainActivity.this, arrayList);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.admonitor_listView);
                if (arrayList.isEmpty() && listView.getHeaderViewsCount() == 0) {
                    //header = getLayoutInflater().inflate(R.layout.item_empty_list, null);
                    //Log.d("HEADER", "ADDED");
                    //listView.addHeaderView(header);
                } else {
                    //Log.d("HEADER", "REMOVED");
                    //header.findViewById(R.id.item_empty_linear).setVisibility(View.GONE);
                    //listView.removeHeaderView(header);
                }
                listView.setAdapter(adapter);

                listView.setDivider(null);
                listView.setDividerHeight(0);
                /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ImageView asd = (ImageView) findViewById(R.id.admonitor_list_item_edit_image);
                        asd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("CLICK", "CLICK");
                            }
                        });
                        // TODO: switchLayoutTo(ESTATESLIST);
                        justme = false;
                        isFavorite = false;
                        ing_ordering = 0;
                        ing_type = 0;
                        loadRealEstates();
                    }
                });*/

            }
        }, SettingUtil.getToken(this));

    }

    public void saveAdmonitor(View view) {
        TextView name = (TextView) findViewById(R.id.add_admonitor_edittext);
        AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.keyword_realestate_admonitor_edittext);
        TextView minPrice = (TextView) findViewById(R.id.realestate_price_admonitor_min);
        TextView maxPrice = (TextView) findViewById(R.id.realestate_price_admonitor_max);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.show();

        EstateUtil.addFigyelo(new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if( pd != null) {
                    pd.dismiss();
                }
                showAdmonitor(null);
            }
        }, SettingUtil.getToken(this), name.getText().toString(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "", "");
    }

    public void doLogout(final View view) {
        LoginUtil.logout(this, new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if ((boolean) result) {
                    Snackbar.make(view, "Sikertelen művelet!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)

                            .show();
                } else {
                    SettingUtil.setToken(MainActivity.this, "");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public void showAddAdmonitor(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_add_admonitor, null);
        ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        viewFlip.setDisplayedChild(0);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);

        closeDrawer();

        menuType = ADD_FIGYELO_MENU;
        supportInvalidateOptionsMenu();
    }

    public void showAdmonitor(View view) {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab.setVisibility(View.INVISIBLE);
        fab_phone.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_admonitor, null);
        ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        viewFlip.setDisplayedChild(0);

        admonitorList();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        closeDrawer();

        menuType = FIGYELO_MENU;
        supportInvalidateOptionsMenu();
    }

    public void showInviteFriends(View view) {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_invite, null);
        final ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab.setVisibility(View.INVISIBLE);
        fab_phone.setVisibility(View.INVISIBLE);

        final EditText email1 = (EditText) findViewById(R.id.email1);
        final EditText email2 = (EditText) findViewById(R.id.email2);
        final EditText email3 = (EditText) findViewById(R.id.email3);
        final EditText email4 = (EditText) findViewById(R.id.email4);
        final EditText email5 = (EditText) findViewById(R.id.email5);

        viewFlip.setDisplayedChild(0);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        getSupportActionBar().setTitle("Meghívás");

        TextView tv = (TextView) viewFlip.getCurrentView().findViewById(R.id.intive_send_button);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EstateUtil.sendInvites(SettingUtil.getToken(MainActivity.this), email1.getText().toString(),
                        email2.getText().toString(), email3.getText().toString(), email4.getText().toString(), email5.getText().toString());

                loadRealEstates();

                Snackbar.make(viewFlip.getCurrentView(), "Köszönjük!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        menuType = INVITE_MENU;

        closeDrawer();
    }

    public void showProfile(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_profile, null);
        final ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab.setVisibility(View.INVISIBLE);
        fab_phone.setVisibility(View.INVISIBLE);

        viewFlip.setDisplayedChild(0);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        getSupportActionBar().setTitle("Profilom");

        menuType = PROFILE_MENU;

        final ProgressDialog pd = new ProgressDialog(this);
        pd.show();

        final EditText veznev = (EditText) viewFlip.getCurrentView().findViewById(R.id.profile_surename_editText);
        final EditText kernev = (EditText) viewFlip.getCurrentView().findViewById(R.id.profile_firstname_editText);
        final EditText mobil = (EditText) viewFlip.getCurrentView().findViewById(R.id.profile_phone_editText);
        final EditText email = (EditText) viewFlip.getCurrentView().findViewById(R.id.profile_mail_editText);

        final EditText pw = (EditText) viewFlip.getCurrentView().findViewById(R.id.change_pass_new_editText);
        final EditText pw2 = (EditText) viewFlip.getCurrentView().findViewById(R.id.passw_again_editText);

        TextView tv = (TextView) viewFlip.getCurrentView().findViewById(R.id.profile_save_button);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pw.getText().toString().equals(pw2.getText().toString())) {
                    Snackbar.make(viewFlip.getCurrentView(), "Két jelszó nem egyezik", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (pw.getText().toString().length() < 6 && pw.getText().toString().length() != 0) {
                    Snackbar.make(viewFlip.getCurrentView(), "Jelszónak legalább 6 karakter hosszúnak kell lennie", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    LoginUtil.updateProfile(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            if(!(boolean)result) {
                                loadRealEstates();
                            } else {
                                Snackbar.make(viewFlip.getCurrentView(), "Mentés sikertelen!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }, veznev.getText().toString(), kernev.getText().toString(), mobil.getText().toString(), pw.getText().toString(), SettingUtil.getToken(MainActivity.this));
                }
        }
    });

        LoginUtil.getProfile(this, new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                if(pd != null) {
                    pd.dismiss();
                }
                UserModel model = (UserModel) result;
                if (!model.isError()) {
                    justme = false;
                    isFavorite = false;
                    ing_ordering = 0;
                    ing_type = 0;

                    veznev.setText(model.getVezeteknev());
                    kernev.setText(model.getKeresztnev());
                    email.setText(model.getEmail());
                    mobil.setText(model.getMobil());

                } else {
                    Snackbar.make(viewFlip.getCurrentView(), "HIBA", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        }, SettingUtil.getToken(this));

        closeDrawer();
    }

    public void showAllEstates(View view) {
        justme = false;
        isFavorite = false;
        ing_ordering = 0;
        ing_type = 0;

        furniture_int = 0;
        lift_int = 0;
        balcony_int = 0;
        meret_int = 0;
        szobaMax_int = 0;
        szobaMin_int = 0;
        floorsMint_int = 0;
        floorsMax_int = 0;
        type_int = 0;
        allapot_int = 0;
        energigenyo_int = 0;
        panoramaSpinner_int = 0;

        price_from = "";
        price_to = "";
        key = "";

        loadRealEstates();
        closeDrawer();
    }

    public void showMessages(View view) {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);

        currentEstate = null;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = inflater.inflate(R.layout.content_messages, null);
        final ViewFlipper viewFlip = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        viewFlip.removeAllViews();
        viewFlip.addView(list, 0);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab_phone = (FloatingActionButton) findViewById(R.id.fab_phone);
        fab.setVisibility(View.INVISIBLE);
        fab_phone.setVisibility(View.INVISIBLE);

        viewFlip.setDisplayedChild(0);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_backicon);
        getSupportActionBar().setTitle("Üzenetek");

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

        menuType = MESSAGES_MENU;
        supportInvalidateOptionsMenu();

        closeDrawer();
    }

    public void showMyAds(View view) {
        justme = true;
        isFavorite = false;
        ing_ordering = 0;
        ing_type = 0;

        furniture_int = 0;
        lift_int = 0;
        balcony_int = 0;
        meret_int = 0;
        szobaMax_int = 0;
        szobaMin_int = 0;
        floorsMint_int = 0;
        floorsMax_int = 0;
        type_int = 0;
        allapot_int = 0;
        energigenyo_int = 0;
        panoramaSpinner_int = 0;

        price_from = "";
        price_to = "";
        key = "";

        loadRealEstates();
        closeDrawer();
    }

    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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
                loadBooking();
                break;
            case R.id.action_fav:
                if (!currentEstate.isFavourite()) {
                    EstateUtil.setFavorite(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            Log.d("FAV RESULT", result.toString());
                            if (!(boolean)result) {
                                MenuView.ItemView favItem = (MenuView.ItemView) findViewById(R.id.action_fav);
                                if(favItem != null) {
                                    favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_filled));
                                    currentEstate.setIsFavourite(true);
                                }
                            }
                        }
                    },String.valueOf(currentEstate.getId()), SettingUtil.getToken(MainActivity.this), "1");
                } else {
                    EstateUtil.setFavorite(new SoapObjectResult() {
                        @Override
                        public void parseRerult(Object result) {
                            Log.d("FAV RESULT", result.toString());
                            if (!(boolean) result) {
                                MenuView.ItemView favItem = (MenuView.ItemView) findViewById(R.id.action_fav);
                                if(favItem != null) {
                                    favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_heart_content));
                                    currentEstate.setIsFavourite(false);
                                }
                            }
                        }
                    }, String.valueOf(currentEstate.getId()), SettingUtil.getToken(MainActivity.this), "0");
                }
                break;
            case R.id.action_message:
                loadMessagesForEstate(currentEstate.getHash(), 0);
                break;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.advert_city)));
                break;
            case android.R.id.home:
                switch (menuType) {
                    case MAIN_MENU:
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                        break;
                    case ESTATE_MENU:
                        loadRealEstates();
                        break;
                    case MESSAGES_THREAD_MENU:
                        if(currentEstate == null) {
                            showMessages(null);
                        } else {
                            getEstateContent(currentEstate.getId());
                        }
                        break;
                    case ADD_FIGYELO_MENU:
                        showAdmonitor(null);
                        break;
                    case ADD_ESTATE_MENU:

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
                        mon = 0;
                        tue = 0;
                        wed = 0;
                        thu = 0;
                        fri = 0;
                        sat = 0;
                        sun = 0;
                        start = "0";
                        finish = "0";

                        loadRealEstates();

                        break;
                    default:
                        loadRealEstates();
                        break;
                }
                supportInvalidateOptionsMenu();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    boolean isBackPressed = false;
}
