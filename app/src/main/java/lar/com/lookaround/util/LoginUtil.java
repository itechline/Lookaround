package lar.com.lookaround.util;

import android.content.Context;
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
    private static final String LOGIN = "logged_in";
    private static final String TOKEN = "token";
    private static final String TOKEN_ACTIVE = "token_active";
    private static final String STATUS = "status";
    private static final String LOGGED_IN = "logged_in";

    public static void login(final Context ctx, final SoapObjectResult getBackWhenItsDone, String mail, String passw) {
        try {
            String url = "http://lookrnd.me/dev/api/do_login";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("email", mail);
            postadatok.put("pass", passw);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {

                            JSONObject jsonObj = new JSONObject(result);


                            Object isAbleObj = jsonObj.getBoolean(LOGIN);
                            if ((boolean)isAbleObj) {
                                String token = jsonObj.getString(TOKEN);
                                SettingUtil.setToken(ctx, token);
                            }

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


    public static void logout(final Context ctx, final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "http://lookrnd.me/dev/api/do_logout";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", SettingUtil.getToken(ctx));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {

                            JSONObject jsonObj = new JSONObject(result);


                            Object isLoggedOut = jsonObj.getBoolean(LOGGED_IN);
                            //String token = jsonObj.getString(TOKEN);

                            getBackWhenItsDone.parseRerult(isLoggedOut);
                            //SettingUtil.setToken(ctx, token);




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

    public static void tokenValidator(final Context ctx, final SoapObjectResult getBackWhenItsDone, String token) {
        try {
            String url = "http://lookrnd.me/dev/api/token_validator";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {

                            JSONObject jsonObj = new JSONObject(result);

                            Object isTokenValid = jsonObj.getBoolean(TOKEN_ACTIVE);
                            getBackWhenItsDone.parseRerult(isTokenValid);
                            if (!(boolean)isTokenValid) {
                                SettingUtil.setToken(ctx, "");
                            }




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

    public static void sendRegistration(final Context ctx, final SoapObjectResult getBackWhenItsDone, String vezeteknev, String keresztnev, String email, String jelszo, String tipus) {
        try {
            String url = "http://lookrnd.me/dev/api/reg";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("fel_vezeteknev", vezeteknev);
            postadatok.put("fel_keresztnev", keresztnev);
            postadatok.put("fel_email", email);
            postadatok.put("fel_jelszo", jelszo);
            postadatok.put("fel_mobilszam", "+36306969696");
            postadatok.put("fel_tipus", tipus);
            postadatok.put("fel_status", "1");
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    Log.d("REGISTRATION: ", result.toString());
                    if (result != null) {
                        try {

                            JSONObject jsonObj = new JSONObject(result);

                            String token = jsonObj.getString(TOKEN);
                            SettingUtil.setToken(ctx, token);

                            Object isStatus = jsonObj.getBoolean(STATUS);
                            getBackWhenItsDone.parseRerult(isStatus);

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

