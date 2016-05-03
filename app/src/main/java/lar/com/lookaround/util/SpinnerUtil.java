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



    private int id;
    private String name;

    public SpinnerUtil (String name) {
        //this.id = id;
        this.name = name;
    }




    public static void get_list_hirdetestipusa(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "http://lookrnd.me/dev/api/list_hirdetestipusa";

            HashMap<String, String> postadatok = new HashMap<String, String>();

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("listEstates", "Return: " + result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            Log.d("SPINNER_UTIL", "Return: " + jsonArray);
                            ArrayList<SpinnerUtil> hiredtestipusa = new ArrayList<SpinnerUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(ING_E_TYPE_ID);
                                String name = json_data.getString(ING_E_TYPE_NAME);

                                hiredtestipusa.add(new SpinnerUtil(name));
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
