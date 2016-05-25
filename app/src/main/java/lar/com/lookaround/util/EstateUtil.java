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
    private int type;
    private double lat;
    private double lng;
    private String justme;

    public String getJustme() {
        return justme;
    }

    public void setJustme(String justme) {
        this.justme = justme;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
    private static final String INGATLAN_TYPE = "ing_e_type_id";
    private static final String KEPEK_URL = "kepek_url";

    public EstateUtil(int id, String adress, String street, String description, int price, boolean isFavourite, String meret, int parkolas, int szobaszam, int butor, int erkely, int type, String urls, String justme, String hash) {
        this.id = id;
        this.adress = adress;
        this.street = street;
        this.hash = hash;
        this.description = description;
        this.price = price;
        this.isFavourite = isFavourite;
        this.meret = meret;
        this.parkolas = parkolas;
        this.szobaszam = szobaszam;
        this.butor = butor;
        this.erkely = erkely;
        this.urls = urls;
        this.type = type;
        this.justme = justme;
    }

   public EstateUtil(boolean error, String hash, int id) {
        this.error = error;
        this.hash = hash;
        this.id = id;
    }

    public EstateUtil(int id, double lat, double lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }


    public static void listEstates(final SoapObjectResult getBackWhenItsDone, String idPost, String pagePost, String tokenTosend, String favorites, String etype, String ordering, final String jstme) {
        try {
            String url = "https://bonodom.com/api/list_estates";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("ingatlan_id", idPost);
            postadatok.put("page", pagePost);
            postadatok.put("token", tokenTosend);
            postadatok.put("favorites", favorites);
            postadatok.put("etype", etype);
            postadatok.put("ordering", ordering);
            postadatok.put("justme", jstme);
            Log.d("UTIL_INGATLAN_ID", idPost);
            Log.d("UTIL_PAGE", pagePost);
            Log.d("UTIL_TOKEN", tokenTosend);
            Log.d("UTIL_FAVORITES", favorites);
            Log.d("UTIL_ETYPE", etype);
            Log.d("UTIL_ORDERING", ordering);
            Log.d("UTIL_JUSTME", jstme);
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
                                String hash = json_data.getString("ingatlan_hash");
                                int priceJson = json_data.getInt(INGATLAN_AR);
                                boolean isFav = json_data.getBoolean(ISFAVOURITE);


                                String imageURL = "";
                                JSONArray kepekArray = new JSONArray(json_data.getString("kepek"));
                                for (int j=0; j < kepekArray.length(); j++) {
                                    JSONObject jsonKep = kepekArray.getJSONObject(j);
                                    imageURL = jsonKep.getString("kepek_url");
                                    Log.d("JSON_URL", imageURL);
                                }

                                String meretJson = json_data.getString(INGATLAN_MERET);
                                int parkolasJson = json_data.getInt(INGATLAN_PARKOLAS);
                                int szszJson = json_data.getInt(INGATLAN_SZOBASZAM);
                                int butorJson = json_data.getInt(INGATLAN_BUTOROZOTT);
                                int erkelyJson = json_data.getInt(INGATLAN_ERKELY);
                                int typeJson = json_data.getInt(INGATLAN_TYPE);


                                estates.add(new EstateUtil(idJson, adressJson, streetJson, descriptionJson, priceJson, isFav, meretJson, parkolasJson, szszJson, butorJson, erkelyJson, typeJson, imageURL, jstme, hash));

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
            String url = "https://bonodom.com/api/get_estate";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("id", idPost);
            postadatok.put("token", tokenTosend);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {


                    if (result != null) {
                        Log.d("GET_ESTATE ", result.toString());
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
            String url = "https://bonodom.com/api/set_favorite";

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


    public static void updateReg(final SoapObjectResult getBackWhenItsDone, String token, String lat, String lng, String mobil) {
    try {
        String url = "https://bonodom.com/api/updatereg";

        HashMap<String, String> postadatok = new HashMap<String, String>();
        postadatok.put("token", token);
        postadatok.put("fel_lat", lat);
        postadatok.put("fel_lng", lng);
        postadatok.put("fel_mobilszam", mobil);
        SoapService ss = new SoapService(new SoapResult() {
            @Override
            public void parseRerult(String result) {

                //Log.d("RESULT_ADD_ESTATE ", result.toString());
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        boolean err = jsonObject.getBoolean("error");

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
                                 String allapot, String szobaszam, String lng, String lat, String title, String type, String token) {
        try {
            String url = "https://bonodom.com/api/add_estate";

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
            postadatok.put("token", token);
            postadatok.put("ingatlan_irszam", "1234");
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    //Log.d("RESULT_ADD_ESTATE ", result.toString());
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


    public static void list_map_estates(final SoapObjectResult getBackWhenItsDone, String token) {
        try {
            String url = "https://bonodom.com/api/list_maps_estates";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                    Log.d("RESULT_MAP_ESTATE ", result.toString());
                    if (result != null) {
                        try {

                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<EstateUtil> estates = new ArrayList<EstateUtil>();

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject json_data = jsonArray.getJSONObject(i);
                                int idJson = json_data.getInt(INGATLAN_ID);
                                double latJson = json_data.getDouble(INGATLAN_LAT);
                                double lngJson = json_data.getDouble(INGATLAN_LNG);

                                estates.add(new EstateUtil(idJson, latJson, lngJson));

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

}
