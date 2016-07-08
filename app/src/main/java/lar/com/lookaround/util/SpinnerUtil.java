package lar.com.lookaround.util;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import lar.com.lookaround.adapters.SpinnerAdapter;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.restapi.SoapService;

/**
 * Created by Attila_Dan on 16. 05. 03..
 */
public class SpinnerUtil {
    private static final String ING_E_TYPE_ID = "ing_e_type_id";
    private static final String ING_E_TYPE_NAME = "ing_e_type_name";

    private static final String ALLAPOT_ID = "allapot_id";
    private static final String ALLAPOT_VAL = "allapot_val";

    private static final String ETAN_ID = "etan_id";
    private static final String ETAN_VAL = "etan_val";

    private static final String FUTESTIPUS_ID = "futestipus_id";
    private static final String FUTESTIPUS_VAL = "futestipus_val";

    private static final String KILATAS_ID = "kilatas_id";
    private static final String KILATAS_VAL = "kilatas_val";

    private static final String PARKOLAS_ID = "parkolas_id";
    private static final String PARKOLAS_VAL = "parkolas_val";

    private static final String SZOBASZAM_ID = "szsz_id";
    private static final String SZOBASZAM_VAL = "szsz_val";

    private static final String INGATLAN_TIPUS_ID = "tipus_id";
    private static final String INGATLAN_TIPUS_VAL = "tipus_val";

    private static final String EMELET_ID = "emelet_id";
    private static final String EMELET_VAL = "emelet_val";


    public int id;
    public String name;

    public SpinnerUtil (int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static void get_list_varosok(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_varos";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            Log.d("VAROSOK_JSONARRAY", jsonArray.toString());
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();


                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt("VarosID");
                                String nameJson = json_data.getString("VarosNev");

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public static void get_list_hirdetestipusa(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_hirdetestipusa";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "tipusresult "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            Log.d("HIRDETES_JSONARRAY", jsonArray.toString());
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ING_E_TYPE_ID);
                                String nameJson = json_data.getString(ING_E_TYPE_NAME);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            Log.d("HIRDETESTIPUSA_2: ", hiredtestipusa.toString());
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void get_list_ingatlanallapota(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlanallapota";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "allapotresult "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ALLAPOT_ID);
                                String nameJson = json_data.getString(ALLAPOT_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    //http://lookrnd.me/dev/api/list_ingatlanenergia
    public static void get_list_ingatlanenergia(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlanenergia";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "ingatlanenergiaresult "+result);
                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ETAN_ID);
                                String nameJson = json_data.getString(ETAN_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    //http://lookrnd.me/dev/api/list_ingatlanfutes
    public static void get_list_ingatlanfutes(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlanfutes";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "ingatlanfutesresult "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(FUTESTIPUS_ID);
                                String nameJson = json_data.getString(FUTESTIPUS_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }




    //http://bonodom.com/api/list_statuses
    public static void listStatuses(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_statuses";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "liststasusesresult "+result);
                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt("id");
                                String nameJson = json_data.getString("nev");

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //http://lookrnd.me/dev/api/list_ingatlankilatas
    public static void get_list_ingatlankilatas(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlankilatas";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "ingatlankilatas "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(KILATAS_ID);
                                String nameJson = json_data.getString(KILATAS_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }




    //http://lookrnd.me/dev/api/list_ingatlanparkolas
    public static void get_list_ingatlanparkolas(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlanparkolas";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "parkolas "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(PARKOLAS_ID);
                                String nameJson = json_data.getString(PARKOLAS_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    //http://lookrnd.me/dev/api/list_ingatlanszoba
    public static void get_list_ingatlanszoba(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlanszoba";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "szoba "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(SZOBASZAM_ID);
                                String nameJson = json_data.getString(SZOBASZAM_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void get_list(final SoapObjectResult soap, final Spinner spinner, final int id, final String urlend) {
        try {
            String url = "https://bonodom.com/api/" + urlend;

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "ingatlanemelet "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            if (urlend.equals("list_ingatlanemelet")) {
                                hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));
                            }

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                Iterator<String> keys = json_data.keys();

                                int idJson = json_data.getInt((String) keys.next());
                                String nameJson = json_data.getString((String) keys.next());
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }

                            final SpinnerAdapter adapter = new SpinnerAdapter(spinner.getContext(), hiredtestipusa);
                            spinner.setAdapter(adapter);

                            for(int i = 0; i < hiredtestipusa.size(); i++) {
                                if(hiredtestipusa.get(i).getId() == id) {
                                    spinner.setSelection(i);
                                }
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    SpinnerUtil spinnerUtil = adapter.getItem(i);
                                    soap.parseRerult(spinnerUtil);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    //http://lookrnd.me/dev/api/list_ingatlantipus
    public static void get_list_ingatlantipus(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlantipus";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    //Log.d("spinnerbonodom", "ingatlantipus "+result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            //hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(INGATLAN_TIPUS_ID);
                                String nameJson = json_data.getString(INGATLAN_TIPUS_VAL);
                                if(idJson == 0) {
                                    nameJson = "Nincs Megadva";
                                }

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    //http://lookrnd.me/dev/api/list_ingatlanemelet
    public static void get_list_ingatlanemelet(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "https://bonodom.com/api/list_ingatlanemelet";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                   // Log.d("spinnerbonodom", "ingatlanemelet "+result);


                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(EMELET_ID);
                                String nameJson = json_data.getString(EMELET_VAL);

                                hiredtestipusa.add(new SpinnerUtil(idJson, nameJson));
                            }
                            getBackWhenItsDone.parseRerult(hiredtestipusa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SpinnerUtil> get_list_butorozott() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));
        hiredtestipusa.add(new SpinnerUtil(1, "Nem"));
        hiredtestipusa.add(new SpinnerUtil(2, "Igen"));
        hiredtestipusa.add(new SpinnerUtil(3, "Alku tárgya"));
        return hiredtestipusa;
    }

    public static ArrayList<SpinnerUtil> get_list_erkely() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));
        hiredtestipusa.add(new SpinnerUtil(1, "Van"));
        hiredtestipusa.add(new SpinnerUtil(2, "Nincs"));
        return hiredtestipusa;
    }

    public static ArrayList<SpinnerUtil> get_list_lift() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));
        hiredtestipusa.add(new SpinnerUtil(1, "Van"));
        hiredtestipusa.add(new SpinnerUtil(2, "Nincs"));
        return hiredtestipusa;
    }

    public static ArrayList<SpinnerUtil> get_list_szures() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Dátum szerint"));
        hiredtestipusa.add(new SpinnerUtil(2, "Ár szerint csökkenő"));
        hiredtestipusa.add(new SpinnerUtil(1, "Ár szerint növekvő"));
        return hiredtestipusa;
    }

    public static ArrayList<SpinnerUtil> get_list_meret() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Nincs Megadva"));
        hiredtestipusa.add(new SpinnerUtil(1, "25-ig"));
        hiredtestipusa.add(new SpinnerUtil(2, "26-50"));
        hiredtestipusa.add(new SpinnerUtil(3, "51-75"));
        hiredtestipusa.add(new SpinnerUtil(4, "76-100"));
        hiredtestipusa.add(new SpinnerUtil(5, "101-125"));
        hiredtestipusa.add(new SpinnerUtil(6, "126-150"));
        hiredtestipusa.add(new SpinnerUtil(7, "150+"));
        return hiredtestipusa;
    }

    public static ArrayList<SpinnerUtil> get_list_booking_start() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Kezdés"));
        hiredtestipusa.add(new SpinnerUtil(1, "8:00"));
        hiredtestipusa.add(new SpinnerUtil(2, "8:30"));
        hiredtestipusa.add(new SpinnerUtil(3, "9:00"));
        hiredtestipusa.add(new SpinnerUtil(4, "9:30"));
        hiredtestipusa.add(new SpinnerUtil(5, "10:00"));
        hiredtestipusa.add(new SpinnerUtil(6, "10:30"));
        hiredtestipusa.add(new SpinnerUtil(7, "11:00"));
        hiredtestipusa.add(new SpinnerUtil(8, "11:30"));
        hiredtestipusa.add(new SpinnerUtil(9, "12:00"));
        hiredtestipusa.add(new SpinnerUtil(10, "12:30"));
        hiredtestipusa.add(new SpinnerUtil(11, "13:00"));
        hiredtestipusa.add(new SpinnerUtil(12, "13:30"));
        hiredtestipusa.add(new SpinnerUtil(13, "14:00"));
        hiredtestipusa.add(new SpinnerUtil(14, "14:30"));
        hiredtestipusa.add(new SpinnerUtil(15, "15:00"));
        hiredtestipusa.add(new SpinnerUtil(16, "15:30"));
        hiredtestipusa.add(new SpinnerUtil(17, "16:00"));
        hiredtestipusa.add(new SpinnerUtil(18, "16:30"));
        hiredtestipusa.add(new SpinnerUtil(19, "17:00"));
        hiredtestipusa.add(new SpinnerUtil(20, "17:30"));
        hiredtestipusa.add(new SpinnerUtil(21, "18:00"));
        return hiredtestipusa;
    }

    public static ArrayList<SpinnerUtil> get_list_booking_finish() {
        ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();
        hiredtestipusa.add(new SpinnerUtil(0, "Vége"));
        hiredtestipusa.add(new SpinnerUtil(1, "8:00"));
        hiredtestipusa.add(new SpinnerUtil(2, "8:30"));
        hiredtestipusa.add(new SpinnerUtil(3, "9:00"));
        hiredtestipusa.add(new SpinnerUtil(4, "9:30"));
        hiredtestipusa.add(new SpinnerUtil(5, "10:00"));
        hiredtestipusa.add(new SpinnerUtil(6, "10:30"));
        hiredtestipusa.add(new SpinnerUtil(7, "11:00"));
        hiredtestipusa.add(new SpinnerUtil(8, "11:30"));
        hiredtestipusa.add(new SpinnerUtil(9, "12:00"));
        hiredtestipusa.add(new SpinnerUtil(10, "12:30"));
        hiredtestipusa.add(new SpinnerUtil(11, "13:00"));
        hiredtestipusa.add(new SpinnerUtil(12, "13:30"));
        hiredtestipusa.add(new SpinnerUtil(13, "14:00"));
        hiredtestipusa.add(new SpinnerUtil(14, "14:30"));
        hiredtestipusa.add(new SpinnerUtil(15, "15:00"));
        hiredtestipusa.add(new SpinnerUtil(16, "15:30"));
        hiredtestipusa.add(new SpinnerUtil(17, "16:00"));
        hiredtestipusa.add(new SpinnerUtil(18, "16:30"));
        hiredtestipusa.add(new SpinnerUtil(19, "17:00"));
        hiredtestipusa.add(new SpinnerUtil(20, "17:30"));
        hiredtestipusa.add(new SpinnerUtil(21, "18:00"));
        return hiredtestipusa;
    }


}
