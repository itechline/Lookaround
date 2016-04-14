package lar.com.lookaround.util;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.restapi.SoapService;

/**
 * Created by Attila_Dan on 16. 04. 14..
 */
public class LoginUtil {
    public static void login() {
        try {
            String url = "http://lookrnd.me/dev/api/do_login";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("email", "nagy.roland@nye.hu");
            postadatok.put("pass", "narol");
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("TESTADATOK", "Return: " + result);
                }
            }, postadatok);

            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

