package lar.com.lookaround.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.restapi.SoapService;

/**
 * Created by Attila_Dan on 16. 04. 14..
 */


public class LoginUtil {
    private static final String datas = "logged_in";

    public static void login(final SoapObjectResult getBackWhenItsDone, String mail, String passw) {
        try {
            String url = "http://lookrnd.me/dev/api/do_login";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            //postadatok.put("email", "nagy.roland@nye.hu");
            //postadatok.put("pass", "narol");
            postadatok.put("email", mail);
            postadatok.put("pass", passw);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("TESTADATOK", "Return: " + result);

                    //jsonParser(result);

                    if (result != null) {
                        try {

                            JSONObject jsonObj = new JSONObject(result);


                            Object isAbleObj = jsonObj.getBoolean(datas);
                            getBackWhenItsDone.parseRerult(isAbleObj);



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

