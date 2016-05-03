package lar.com.lookaround.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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

    public void setName(String name) {
        this.name = name;
    }

    public static void get_list_hirdetestipusa(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "http://lookrnd.me/dev/api/list_hirdetestipusa";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ING_E_TYPE_ID);
                                String nameJson = json_data.getString(ING_E_TYPE_NAME);

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

    public static void get_list_ingatlanallapota(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "http://lookrnd.me/dev/api/list_ingatlanallapota";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ALLAPOT_ID);
                                String nameJson = json_data.getString(ALLAPOT_VAL);

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
            String url = "http://lookrnd.me/dev/api/list_ingatlanenergia";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ETAN_ID);
                                String nameJson = json_data.getString(ETAN_VAL);

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
}
