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
 * Created by Attila_Dan on 16. 04. 27..
 */
public class MessageUtil {
    private String profile_url;
    private String message;
    private String profile_name;
    private String date;

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MessageUtil(String profile_url, String message, String profile_name, String date) {
        this.profile_url = profile_url;
        this.message = message;
        this.profile_name = profile_name;
        this.date = date;
    }

    public static void listMessages(final SoapObjectResult getBackWhenItsDone, String tokenTosend) {
        try {
            String url = "http://lookrnd.me/dev/api/list_messages";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", tokenTosend);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("listEstates", "Return: " + result);

                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            Log.d("JSON_LENGTH", "Return: " + jsonArray.length());
                            ArrayList<MessageUtil> messages = new ArrayList<MessageUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data = jsonArray.getJSONObject(i);

                                String url = json_data.getString("url");
                                String msg = json_data.getString("msg");
                                String name = json_data.getString("name");
                                String date = json_data.getString("date");

                                messages.add(new MessageUtil(url, msg, name, date));
                                //Log.d("LOFASZ", "Return: " + idJson);
                            }
                            
                            getBackWhenItsDone.parseRerult(messages);

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
