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
    private int fromme;
    private String msg;
    private String veznev;
    private String kernev;
    private String varos;
    private String utca;
    private int uid;
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getVeznev() {
        return veznev;
    }

    public void setVeznev(String veznev) {
        this.veznev = veznev;
    }

    public String getKernev() {
        return kernev;
    }

    public void setKernev(String kernev) {
        this.kernev = kernev;
    }

    public String getVaros() {
        return varos;
    }

    public void setVaros(String varos) {
        this.varos = varos;
    }

    public String getUtca() {
        return utca;
    }

    public void setUtca(String utca) {
        this.utca = utca;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFromme() {
        return fromme;
    }

    public void setFromme(int fromme) {
        this.fromme = fromme;
    }

    private ArrayList messages;

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

    public MessageUtil() {

    }

    public MessageUtil(String profile_url, String message, String profile_name, String date) {
        this.profile_url = profile_url;
        this.message = message;
        this.profile_name = profile_name;
        this.date = date;
    }

    public MessageUtil(String profile_url, ArrayList messages, String profile_name) {
        this.profile_url = profile_url;
        this.messages = messages;
        this.profile_name = profile_name;

    }


    public static void getMessageCount(final SoapObjectResult getBackWhenItsDone, String tokenTosend) {
        try {
            String url = "https://bonodom.com/api/get_messagecount";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", tokenTosend);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("getMessageCount", "Return: " + result);
                    int count = 0;
                    if (result != null) {
                        try {
                            JSONObject obj = new JSONObject(result);

                            count = obj.getInt("count");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }

                    getBackWhenItsDone.parseRerult(count);
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public static void sendMessage(final SoapObjectResult getBackWhenItsDone, String tokenTosend, String hash, String msg) {
        try {
            String url = "https://bonodom.com/api/send_message";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", tokenTosend);
            postadatok.put("hash", hash);
            postadatok.put("msg", msg);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("sendmeesage", "Return: " + result);
                    boolean err = true;
                    if (result != null) {
                        try {
                            JSONObject obj = new JSONObject(result);

                            err = obj.getBoolean("error");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }

                    getBackWhenItsDone.parseRerult(err);
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void listMessages(final SoapObjectResult getBackWhenItsDone, String tokenTosend) {
        try {
            String url = "https://bonodom.com/api/get_messages";

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

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_data = jsonArray.getJSONObject(i);

                                MessageUtil msg = new MessageUtil();
                                msg.setDate(json_data.getString("date"));
                                msg.setVeznev(json_data.getString("fel_vezeteknev"));
                                msg.setKernev(json_data.getString("fel_keresztnev"));
                                msg.setVaros(json_data.getString("ingatlan_varos"));
                                msg.setUtca(json_data.getString("ingatlan_utca"));
                                msg.setHash(json_data.getString("hash"));
                                msg.setUid(json_data.getInt("uid"));
                                msg.setFromme(-1);

                                messages.add(msg);
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

    public static void listMessagesForEstate(final SoapObjectResult getBackWhenItsDone, String tokenTosend, String hash, int uid) {
        try {
            String url = "https://bonodom.com/api/get_messagebyestate";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", tokenTosend);
            postadatok.put("hash", hash);
            if(uid != 0) {
                postadatok.put("uid", String.valueOf(uid));
            }
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

                                MessageUtil msg = new MessageUtil();
                                msg.setMsg(json_data.getString("conv_msg"));
                                msg.setFromme(json_data.getInt("fromme"));

                                messages.add(msg);
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
