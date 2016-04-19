package lar.com.lookaround.util;

import android.content.Context;
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
 * Created by Attila_Dan on 16. 04. 19..
 */
public class EstateUtil {
    int id;
    private String adress;
    private String street;
    private String description;
    private String price;
    //private boolean isFavourite;
    //private String urls;

    private static final String INGATLAN_ID = "ingatlan_id";
    private static final String INGATLAN_VAROS = "ingatlan_varos";
    private static final String INGATLAN_UTCA = "ingatlan_utca";
    private static final String INGATLAN_ROVIDLEIRAS = "ingatlan_rovidleiras";
    private static final String INGATLAN_AR = "ingatlan_ar";

    public EstateUtil(int id, String adress, String street, String description, String price) {
        this.id = id;
        this.adress = adress;
        this.street = street;
        this.description = description;
        this.price = price;
        //this.isFavourite = isFavourite;
        //this.urls = urls;
    }

    public static void listEstates(final SoapObjectResult getBackWhenItsDone) {
        try {
            String url = "http://lookrnd.me/dev/api/list_estates";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("listEstates", "Return: " + result);

                    if (result != null) {
                        try {

                            /*JSONObject jsonObj = new JSONObject(result);




                            Object isAbleObj = jsonObj.getBoolean(LOGIN);
                            if ((boolean)isAbleObj) {
                                String token = jsonObj.getString(TOKEN);
                                SettingUtil.setToken(ctx, token);
                            }

                            getBackWhenItsDone.parseRerult(isAbleObj);*/

                            JSONArray jsonArray = new JSONArray(result);

                            //Log.d("listEstatesArray", "Return: " + jsonArray.toString());
                            ArrayList<EstateUtil> estates = new ArrayList<EstateUtil>();

                            for(int i=0;i<jsonArray.length();i++){
                                //estates.add(new EstateUtil(jsonArray.getInt(INGATLAN_ID)));
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(INGATLAN_ID);
                                String adressJson = json_data.getString(INGATLAN_VAROS);
                                String streetJson = json_data.getString(INGATLAN_UTCA);
                                String descriptionJson = json_data.getString(INGATLAN_ROVIDLEIRAS);
                                String priceJson = json_data.getString(INGATLAN_AR);


                                estates.add(new EstateUtil(idJson, adressJson, streetJson, descriptionJson, priceJson));
                                Log.d("LOFASZ", "Return: " + adressJson.toString());
                            }


                            getBackWhenItsDone.parseRerult(estates);




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
