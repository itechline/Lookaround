package lar.com.lookaround.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import lar.com.lookaround.R;
import lar.com.lookaround.restapi.ImageUploadService;
import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.restapi.SoapResult;
import lar.com.lookaround.restapi.SoapService;

/**
 * Created by Attila_Dan on 16. 04. 19..
 */
public class EstateUtil {
    private int id;
    private String adress;
    private String street;
    private String description;
    private int price;
    private boolean error;
    private String hash;
    private String meret;
    private int parkolas;
    private int butor;
    private int erkely;
    private int szobaszam;

    public String getMeret() {
        return meret;
    }

    public void setMeret(String meret) {
        this.meret = meret;
    }

    public int getParkolas() {
        return parkolas;
    }

    public void setParkolas(int parkolas) {
        this.parkolas = parkolas;
    }

    public int getButor() {
        return butor;
    }

    public void setButor(int butor) {
        this.butor = butor;
    }

    public int getErkely() {
        return erkely;
    }

    public void setErkely(int erkely) {
        this.erkely = erkely;
    }

    public int getSzobaszam() {
        return szobaszam;
    }

    public void setSzobaszam(int szobaszam) {
        this.szobaszam = szobaszam;
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }


    public static void setLargestId(int largestId) {
        EstateUtil.largestId = largestId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    private boolean isFavourite;

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    private String urls;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public static int largestId = 0;

    public int getlargestId() {
        return largestId;
    }

    private static final String INGATLAN_ID = "ingatlan_id";
    private static final String INGATLAN_VAROS = "ingatlan_varos";
    private static final String INGATLAN_UTCA = "ingatlan_utca";
    private static final String INGATLAN_ROVIDLEIRAS = "ingatlan_rovidleiras";
    private static final String INGATLAN_AR = "ingatlan_ar";
    private static final String ISFAVOURITE = "kedvenc";
    private static final String INGATLAN_PIC_URL = "ingatlan_picture_url";
    private static final String INGATLAN_MERET = "ingatlan_meret";
    private static final String INGATLAN_BUTOROZOTT = "ingatlan_butorozott";
    private static final String INGATLAN_PARKOLAS = "ingatlan_parkolas_id";
    private static final String INGATLAN_ERKELY = "ingatlan_erkely";
    private static final String INGATLAN_SZOBASZAM = "ingatlan_szsz_id";
    private static final String INGATLAN_LAT = "ingatlan_lat";
    private static final String INGATLAN_LNG = "ingatlan_lng";


    public EstateUtil(int id, String adress, String street, String description, int price, boolean isFavourite, String meret, int parkolas, int szobaszam, int butor, int erkely, String urls) {
        this.id = id;
        this.adress = adress;
        this.street = street;
        this.description = description;
        this.price = price;
        this.isFavourite = isFavourite;
        this.meret = meret;
        this.parkolas = parkolas;
        this.szobaszam = szobaszam;
        this.butor = butor;
        this.erkely = erkely;
        this.urls = urls;
    }

   public EstateUtil(boolean error, String hash, int id) {
        this.error = error;
        this.hash = hash;
        this.id = id;
    }


    public static void listEstates(final SoapObjectResult getBackWhenItsDone, String idPost, String pagePost, String tokenTosend, String favorites) {
        try {
            String url = "http://lookrnd.me/dev/api/list_estates";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("ingatlan_id", idPost);
            postadatok.put("page", pagePost);
            postadatok.put("token", tokenTosend);
            postadatok.put("favorites", favorites);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        Log.d("LIST_ESTATES: Result: ", result.toString());
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<EstateUtil> estates = new ArrayList<EstateUtil>();

                            for(int i=0;i<jsonArray.length();i++){

                                /*
                                private int meret;
                                private int parkolas;
                                private int szobaszam;
                                private boolean butor;
                                private boolean erkely;
                                private double lat;
                                private double lng;
                                */

                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(INGATLAN_ID);
                                String adressJson = json_data.getString(INGATLAN_VAROS);
                                String streetJson = json_data.getString(INGATLAN_UTCA);
                                String descriptionJson = json_data.getString(INGATLAN_ROVIDLEIRAS);
                                int priceJson = json_data.getInt(INGATLAN_AR);
                                boolean isFav = json_data.getBoolean(ISFAVOURITE);
                                String url = json_data.getString(INGATLAN_PIC_URL);

                                String meretJson = json_data.getString(INGATLAN_MERET);
                                int parkolasJson = json_data.getInt(INGATLAN_PARKOLAS);
                                int szszJson = json_data.getInt(INGATLAN_SZOBASZAM);
                                int butorJson = json_data.getInt(INGATLAN_BUTOROZOTT);
                                int erkelyJson = json_data.getInt(INGATLAN_ERKELY);

                                estates.add(new EstateUtil(idJson, adressJson, streetJson, descriptionJson, priceJson, isFav, meretJson, parkolasJson, szszJson, butorJson, erkelyJson, url));

                                if (idJson > largestId) {
                                    largestId = idJson;
                                }
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






    public static void getEstate(final SoapObjectResult getBackWhenItsDone, String idPost, String tokenTosend) {
        try {
            String url = "http://lookrnd.me/dev/api/get_estate";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("id", idPost);
            postadatok.put("token", tokenTosend);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            Object obj = jsonObject;


                            getBackWhenItsDone.parseRerult(obj);


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



    public static void setFavorite(final SoapObjectResult getBackWhenItsDone, String idSend, String tokenSend, String favorite) {
        try {
            String url = "http://lookrnd.me/dev/api/set_favorite";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("ingatlan_id", idSend);
            postadatok.put("token", tokenSend);
            postadatok.put("favorite", favorite);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    if (result != null) {
                        try {

                            JSONObject favObj = new JSONObject(result);

                            Object err = favObj.getBoolean("error");


                            getBackWhenItsDone.parseRerult(err);

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





    public static void addEstate(final SoapObjectResult getBackWhenItsDone, String meret, String varos,
                                 String utca, String leiras, String ar, String energia, String butor, String kilatas,
                                 String lift, String futes, String parkolas, String erkely, String tipus, String emelet,
                                 String allapot, String szobaszam, String lng, String lat, String title, String type) {
        try {
            String url = "http://lookrnd.me/dev/api/add_estate";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("ingatlan_meret", meret);
            postadatok.put("ingatlan_varos", varos);
            postadatok.put("ingatlan_utca", utca);
            postadatok.put("ingatlan_rovidleiras", leiras);
            postadatok.put("ingatlan_ar", ar);
            postadatok.put("ingatlan_energiatan_id", energia);
            postadatok.put("ingatlan_butorozott", butor);
            postadatok.put("ingatlan_kilatas_id", kilatas);
            postadatok.put("ingatlan_lift", lift);
            postadatok.put("ingatlan_futestipus_id", futes);
            postadatok.put("ingatlan_parkolas_id", parkolas);
            postadatok.put("ingatlan_erkely", erkely);
            postadatok.put("ingatlan_tipus_id", tipus);
            postadatok.put("ingatlan_emelet_id", emelet);
            postadatok.put("ingatlan_allapot_id", allapot);
            postadatok.put("ingatlan_szsz_id", szobaszam);
            postadatok.put("ingatlan_lng", lng);
            postadatok.put("ingatlan_lat", lat);
            postadatok.put("ingatlan_title", title);
            postadatok.put("ing_e_type_id", type);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    Log.d("RESULT_ADD_ESTATE ", result.toString());
                    if (result != null) {
                        try {

                            ArrayList<EstateUtil> datas = new ArrayList<EstateUtil>();

                            JSONObject jsonObject = new JSONObject(result);

                            boolean err = jsonObject.getBoolean("error");
                            int id = jsonObject.getInt("id");
                            String hash = jsonObject.getString("hash");

                            datas.add(new EstateUtil(err, hash, id));


                            getBackWhenItsDone.parseRerult(datas);

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


    /*public static void uploadImage(final SoapObjectResult getBackWhenItsDone, Bitmap bitmap) {
        try {
            String url = "http://lookrnd.me/dev/upload/uploadtoserver?ing_hash=anh3x2fz5np1";

            //HashMap<String, String> postadatok = new HashMap<String, String>();
            //postadatok.put("ingatlan_meret", meret);



            ImageUploadService ss = new ImageUploadService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.d("ADDESTATE: ", "Return: " + result);

                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            Object isSuccesfull = jsonObject.getBoolean("error");
                            //Object id = jsonObject.getInt("id");

                            getBackWhenItsDone.parseRerult(isSuccesfull);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                }
            }, bitmap);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }*/

}
