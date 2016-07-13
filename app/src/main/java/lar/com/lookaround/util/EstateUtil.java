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

import lar.com.lookaround.models.Idopont;
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
    private int lift;

    private String estateTitle;
    private String estateDescription;
    private String estatePrice;
    private String estateCity;
    private String estateStreet;
    private String estetaHouseNumber;
    private String estateSize;
    private int estateCityID;

    public int getEstateCityID() {
        return estateCityID;
    }

    public void setEstateCityID(int estateCityID) {
        this.estateCityID = estateCityID;
    }

    public String getEstateTitle() {
        return estateTitle;
    }

    public void setEstateTitle(String estateTitle) {
        this.estateTitle = estateTitle;
    }

    public String getEstateDescription() {
        return estateDescription;
    }

    public void setEstateDescription(String estateDescription) {
        this.estateDescription = estateDescription;
    }

    public String getEstatePrice() {
        return estatePrice;
    }

    public void setEstatePrice(String estatePrice) {
        this.estatePrice = estatePrice;
    }

    public String getEstateCity() {
        return estateCity;
    }

    public void setEstateCity(String estateCity) {
        this.estateCity = estateCity;
    }

    public String getEstateStreet() {
        return estateStreet;
    }

    public void setEstateStreet(String estateStreet) {
        this.estateStreet = estateStreet;
    }

    public String getEstetaHouseNumber() {
        return estetaHouseNumber;
    }

    public void setEstetaHouseNumber(String estetaHouseNumber) {
        this.estetaHouseNumber = estetaHouseNumber;
    }

    public String getEstateSize() {
        return estateSize;
    }

    public void setEstateSize(String estateSize) {
        this.estateSize = estateSize;
    }

    public int getLift() {
        return lift;
    }

    public void setLift(int lift) {
        this.lift = lift;
    }

    private int ingatlan_energiatan_id;
    private int ingatlan_kilatas_id;
    private int ingatlan_futestipus_id;
    private int ingatlan_parkolas_id;
    private int ingatlan_tipus_id;
    private int ingatlan_emelet_id;
    private int ingatlan_allapot_id;
    private int ingatlan_szsz_id;
    private int ing_e_type_id;

    public int getIngatlan_energiatan_id() {
        return ingatlan_energiatan_id;
    }

    public void setIngatlan_energiatan_id(int ingatlan_energiatan_id) {
        this.ingatlan_energiatan_id = ingatlan_energiatan_id;
    }

    public int getIngatlan_kilatas_id() {
        return ingatlan_kilatas_id;
    }

    public void setIngatlan_kilatas_id(int ingatlan_kilatas_id) {
        this.ingatlan_kilatas_id = ingatlan_kilatas_id;
    }

    public int getIngatlan_futestipus_id() {
        return ingatlan_futestipus_id;
    }

    public void setIngatlan_futestipus_id(int ingatlan_futestipus_id) {
        this.ingatlan_futestipus_id = ingatlan_futestipus_id;
    }

    public int getIngatlan_parkolas_id() {
        return ingatlan_parkolas_id;
    }

    public void setIngatlan_parkolas_id(int ingatlan_parkolas_id) {
        this.ingatlan_parkolas_id = ingatlan_parkolas_id;
    }

    public int getIngatlan_tipus_id() {
        return ingatlan_tipus_id;
    }

    public void setIngatlan_tipus_id(int ingatlan_tipus_id) {
        this.ingatlan_tipus_id = ingatlan_tipus_id;
    }

    public int getIngatlan_emelet_id() {
        return ingatlan_emelet_id;
    }

    public void setIngatlan_emelet_id(int ingatlan_emelet_id) {
        this.ingatlan_emelet_id = ingatlan_emelet_id;
    }

    public int getIngatlan_allapot_id() {
        return ingatlan_allapot_id;
    }

    public void setIngatlan_allapot_id(int ingatlan_allapot_id) {
        this.ingatlan_allapot_id = ingatlan_allapot_id;
    }

    public int getIngatlan_szsz_id() {
        return ingatlan_szsz_id;
    }

    public void setIngatlan_szsz_id(int ingatlan_szsz_id) {
        this.ingatlan_szsz_id = ingatlan_szsz_id;
    }

    public int getIng_e_type_id() {
        return ing_e_type_id;
    }

    public void setIng_e_type_id(int ing_e_type_id) {
        this.ing_e_type_id = ing_e_type_id;
    }

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

    public EstateUtil() {

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


    public static void listEstates(final SoapObjectResult getBackWhenItsDone, String idPost, String pagePost, String tokenTosend, String favorites, String etype, String ordering, final String jstme,
                                   int furniture_int, int lift_int, int balcony_int, int meret_int, int szobaMax_int, int szobaMin_int, int floorsMax_int, int floorsMint_int,
                                   int type_int, int allapot_int,int  energigenyo_int, int panoramaSpinner_int, int parkolas_int, String price_from, String price_to, String key) {
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

            postadatok.put("ingatlan_butorozott", String.valueOf(furniture_int));
            postadatok.put("ingatlan_lift", String.valueOf(lift_int));
            postadatok.put("ingatlan_erkely", String.valueOf(balcony_int));
            postadatok.put("ingatlan_meret", String.valueOf(meret_int));
            postadatok.put("ingatlan_szsz_max", String.valueOf(szobaMax_int));
            postadatok.put("ingatlan_szsz_min", String.valueOf(szobaMin_int));
            postadatok.put("ingatlan_emelet_max", String.valueOf(floorsMax_int));
            postadatok.put("ingatlan_emelet_min", String.valueOf(floorsMint_int));
            postadatok.put("ingatlan_tipus_id", String.valueOf(type_int));
            postadatok.put("ingatlan_allapot_id", String.valueOf(allapot_int));
            postadatok.put("ingatlan_energiatan_id", String.valueOf(energigenyo_int));
            postadatok.put("ingatlan_kilatas_id", String.valueOf(panoramaSpinner_int));
            postadatok.put("ingatlan_ar_min", String.valueOf(price_from));
            postadatok.put("ingatlan_ar_max", String.valueOf(price_to));
            postadatok.put("keyword", String.valueOf(key));
            postadatok.put("ingatlan_parkolas_id", String.valueOf(parkolas_int));



                SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    if (result != null) {
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
                                }

                                String meretJson = json_data.getString(INGATLAN_MERET);
                                int parkolasJson = json_data.getInt(INGATLAN_PARKOLAS);
                                int szszJson = json_data.getInt(INGATLAN_SZOBASZAM);
                                int butorJson = json_data.getInt(INGATLAN_BUTOROZOTT);
                                int erkelyJson = json_data.getInt(INGATLAN_ERKELY);
                                int typeJson = json_data.getInt(INGATLAN_TYPE);


                                EstateUtil util = new EstateUtil(idJson, adressJson, streetJson, descriptionJson, priceJson, isFav, meretJson, parkolasJson, szszJson, butorJson, erkelyJson, typeJson, imageURL, jstme, hash);

                                /*
                                    private int ingatlan_energiatan_id;
                                    private int ingatlan_kilatas_id;
                                    private int ingatlan_futestipus_id;
                                    private int ingatlan_parkolas_id;
                                    private int ingatlan_tipus_id;
                                    private int ingatlan_emelet_id;
                                    private int ingatlan_allapot_id;
                                    private int ingatlan_szsz_id;
                                    private int ing_e_type_id;
                                 */

                                util.setIngatlan_energiatan_id(json_data.getInt("ingatlan_energiatan_id"));
                                util.setIngatlan_kilatas_id(json_data.getInt("ingatlan_kilatas_id"));
                                util.setIngatlan_futestipus_id(json_data.getInt("ingatlan_futestipus_id"));
                                util.setIngatlan_parkolas_id(json_data.getInt("ingatlan_parkolas_id"));
                                util.setIngatlan_tipus_id(json_data.getInt("ingatlan_tipus_id"));
                                util.setIngatlan_emelet_id(json_data.getInt("ingatlan_emelet_id"));
                                util.setIngatlan_allapot_id(json_data.getInt("ingatlan_allapot_id"));
                                util.setIngatlan_szsz_id(json_data.getInt("ingatlan_szsz_id"));
                                util.setIng_e_type_id(json_data.getInt("ingatlan_e_type_id"));
                                util.setLift(json_data.getInt("ingatlan_lift"));

                                util.setEstateTitle(json_data.getString("ingatlan_title"));
                                util.setEstateDescription(json_data.getString("ingatlan_rovidleiras"));
                                util.setEstatePrice(json_data.getString("ingatlan_ar"));
                                util.setEstateCity(json_data.getString("ingatlan_varos"));
                                util.setEstateStreet(json_data.getString("ingatlan_utca"));
                                util.setEstetaHouseNumber(json_data.getString("ingatlan_hsz"));
                                util.setEstateSize(json_data.getString("ingatlan_meret"));
                                util.setEstateCityID(json_data.getInt("ingatlan_varos_id"));

                                estates.add(util);

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

    public static void deleteFigyelo(Context ctx, final SoapObjectResult getBackWhenItsDone, int id) {
        try {
            String url = "https://bonodom.com/api/delete_figyelo";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("hir_id", String.valueOf(id));
            postadatok.put("token", SettingUtil.getToken(ctx));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.e("error", "_"+result);
                    getBackWhenItsDone.parseRerult(true);
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void listFigyelo(final SoapObjectResult getBackWhenItsDone, String tokenTosend) {
        try {
            String url = "https://bonodom.com/api/list_figyelo";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", tokenTosend);

            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    if (result != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            ArrayList<AdmonitorUtil> figyelok = new ArrayList<>();

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject json_data = jsonArray.getJSONObject(i);
                                AdmonitorUtil mon = new AdmonitorUtil();
                                mon.setId(json_data.getInt("hir_id"));
                                mon.setName(json_data.getString("name"));
                                mon.setHasFurniture(json_data.getInt("ingatlan_butorozott"));
                                mon.setElevator(json_data.getInt("ingatlan_lift"));
                                mon.setBalcony(json_data.getInt("ingatlan_erkely"));
                                mon.setSize(json_data.getInt("ingatlan_meret"));
                                mon.setRoomsMin(json_data.getInt("ingatlan_szsz_min"));
                                mon.setRoomsMax(json_data.getInt("ingatlan_szsz_max"));
                                mon.setFloorsMax(json_data.getInt("ingatlan_emelet_max"));
                                mon.setFloorsMin(json_data.getInt("ingatlan_emelet_min"));
                                mon.setCondition(json_data.getInt("ingatlan_allapot_id"));
                                mon.setType(json_data.getInt("ingatlan_tipus_id"));
                                mon.setEtype(json_data.getInt("ingatlan_energiatan_id"));
                                mon.setView(json_data.getInt("ingatlan_kilatas_id"));
                                mon.setParking(json_data.getInt("ingatlan_parkolas_id"));
                                mon.setPriceMax(json_data.getString("ingatlan_ar_max"));
                                mon.setPriceMin(json_data.getString("ingatlan_ar_min"));
                                mon.setSearch(json_data.getString("keyword"));
                                figyelok.add(mon);

                            }

                            getBackWhenItsDone.parseRerult(figyelok);

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

    public static void addFigyelo(final SoapObjectResult getBackWhenItsDone, String tokenTosend,
                                  String name,
                                   int furniture_int, int lift_int, int balcony_int, int meret_int, int szobaMax_int, int szobaMin_int, int floorsMax_int, int floorsMint_int,
                                   int type_int, int allapot_int,int  energigenyo_int, int panoramaSpinner_int, int parkolas_int, String price_from, String price_to, String key) {
        try {
            String url = "https://bonodom.com/api/add_figyelo";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", tokenTosend);
            postadatok.put("name", name);
            postadatok.put("ingatlan_butorozott", String.valueOf(furniture_int));
            postadatok.put("ingatlan_lift", String.valueOf(lift_int));
            postadatok.put("ingatlan_erkely", String.valueOf(balcony_int));
            postadatok.put("ingatlan_meret", String.valueOf(meret_int));
            postadatok.put("ingatlan_szsz_max", String.valueOf(szobaMax_int));
            postadatok.put("ingatlan_szsz_min", String.valueOf(szobaMin_int));
            postadatok.put("ingatlan_emelet_max", String.valueOf(floorsMax_int));
            postadatok.put("ingatlan_emelet_min", String.valueOf(floorsMint_int));
            postadatok.put("ingatlan_tipus_id", String.valueOf(type_int));
            postadatok.put("ingatlan_allapot_id", String.valueOf(allapot_int));
            postadatok.put("ingatlan_energiatan_id", String.valueOf(energigenyo_int));
            postadatok.put("ingatlan_kilatas_id", String.valueOf(panoramaSpinner_int));
            postadatok.put("ingatlan_ar_min", String.valueOf(price_from));
            postadatok.put("ingatlan_ar_max", String.valueOf(price_to));
            postadatok.put("keyword", String.valueOf(key));
            postadatok.put("ingatlan_parkolas_id", String.valueOf(parkolas_int));



            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    if (result != null) {
                        try {

                            JSONObject obj = new JSONObject(result);
                            getBackWhenItsDone.parseRerult(obj.getBoolean("error"));

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

    public static void deleteEstate(Context ctx, final SoapObjectResult getBackWhenItsDone, int id, int status) {
        try {
            String url = "https://bonodom.com/api/delete_estate";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("ingatlan_id", String.valueOf(id));
            postadatok.put("ingatlan_status", String.valueOf(status));
            postadatok.put("token", SettingUtil.getToken(ctx));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    Log.e("error", "_"+result);
                    getBackWhenItsDone.parseRerult(true);
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

    public static void getIdopontsByDate(final SoapObjectResult getBackWhenItsDone, String token, String mikor, int ingatlan_id) {
        try {
            String url = "https://bonodom.com/api/get_idopont_by_date";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            postadatok.put("datum", mikor);
            postadatok.put("ingatlan_id", String.valueOf(ingatlan_id));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    ArrayList<Idopont> dates = new ArrayList<>();

                    if (result != null) {
                        try {
                            JSONArray arrs = new JSONArray(result);
                            for (int i = 0; i < arrs.length(); i++) {
                                JSONObject obj = arrs.getJSONObject(i);
                                Idopont tmp = new Idopont();
                                tmp.setId(obj.getInt("idopont_id"));
                                tmp.setIid(obj.getInt("idopont_ingatlan_id"));
                                tmp.setDatum(obj.getString("idopont_bejelentkezes"));
                                tmp.setStatus(obj.getInt("idopont_statusz"));
                                tmp.setFelid(obj.getInt("idopont_id"));
                                dates.add(tmp);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getBackWhenItsDone.parseRerult(dates);
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

    public static void updateIdopont(final SoapObjectResult getBackWhenItsDone, String token, int stat, int id) {
        try {
            String url = "https://bonodom.com/api/update_idopont";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            postadatok.put("s", String.valueOf(stat));
            postadatok.put("id", String.valueOf(id));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    getBackWhenItsDone.parseRerult(true);
                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void getIdopontByUser(final SoapObjectResult getBackWhenItsDone, String token) {
        try {
            String url = "https://bonodom.com/api/get_idopont_by_user";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    ArrayList<Idopont> dates = new ArrayList<>();

                    if (result != null) {
                        Log.d("IDOPONT RESULT", result.toString());
                        try {
                            JSONArray arrs = new JSONArray(result);
                            for (int i = 0; i < arrs.length(); i++) {
                                JSONObject obj = arrs.getJSONObject(i);
                                Idopont tmp = new Idopont();
                                tmp.setId(obj.getInt("idopont_id"));
                                tmp.setIid(obj.getInt("idopont_ingatlan_id"));
                                tmp.setDatum(obj.getString("idopont_bejelentkezes"));
                                tmp.setStatus(obj.getInt("idopont_statusz"));
                                tmp.setFelid(obj.getInt("idopont_id"));
                                tmp.setFel(obj.getString("fel"));
                                tmp.setMobile(obj.getString("fel_telefon"));
                                dates.add(tmp);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getBackWhenItsDone.parseRerult(dates);
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

    public static void getIdoponts(final SoapObjectResult getBackWhenItsDone, String token, int ingatlan_id) {
        try {
            String url = "https://bonodom.com/api/get_idopont_dates";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            postadatok.put("ingatlan_id", String.valueOf(ingatlan_id));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {
                    ArrayList<String> dates = new ArrayList<>();

                    if (result != null) {
                        try {
                            JSONArray arrs = new JSONArray(result);
                            for (int i = 0; i < arrs.length(); i++) {
                                JSONObject obj = arrs.getJSONObject(i);
                                dates.add(obj.getString("idopont"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getBackWhenItsDone.parseRerult(dates);
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

    public static void addIdopont(final SoapObjectResult getBackWhenItsDone, String token, String ingatlan_id, String mikor) {
        try {
            String url = "https://bonodom.com/api/add_idopont";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            postadatok.put("mikor", mikor);
            postadatok.put("ingatlan_id", ingatlan_id);
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

    public static void sendInvites(String token, String email1, String email2, String email3, String email4, String email5) {
        try {
            String url = "https://bonodom.com/api/sendmeghivo";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            postadatok.put("email1", email1);
            postadatok.put("email2", email2);
            postadatok.put("email3", email3);
            postadatok.put("email4", email4);
            postadatok.put("email5", email5);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void addSerto(String token, int ingId) {
        try {
            String url = "https://bonodom.com/api/addserto";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            postadatok.put("ingid", String.valueOf(ingId));
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

                }
            }, postadatok);
            ss.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void addVR(String token) {
        try {
            String url = "https://bonodom.com/api/addvr";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("token", token);
            SoapService ss = new SoapService(new SoapResult() {
                @Override
                public void parseRerult(String result) {

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
                                 String utca, String hsz, String leiras, String ar, String energia, String butor, String kilatas,
                                 String lift, String futes, String parkolas, String erkely, String tipus, String emelet,
                                 String allapot, String szobaszam, String lng, String lat, String title, String type, String token, String zipcode,
                                 String mon, String tue, String wed, String thu, String fri, String sat, String sun, String start, String finish, int updateingid) {
        try {
            String url = "https://bonodom.com/api/add_estate";

            HashMap<String, String> postadatok = new HashMap<String, String>();
            postadatok.put("id", String.valueOf(updateingid));
            postadatok.put("ingatlan_meret", meret);
            postadatok.put("ingatlan_varos", varos);
            postadatok.put("ingatlan_utca", utca);
            postadatok.put("ingatlan_hsz", hsz);
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
            postadatok.put("ingatlan_irszam", zipcode);
            postadatok.put("mon", mon);
            postadatok.put("tue", tue);
            postadatok.put("wed", wed);
            postadatok.put("thu", thu);
            postadatok.put("fri", fri);
            postadatok.put("sat", sat);
            postadatok.put("sun", sun);
            postadatok.put("start", start);
            postadatok.put("finish", finish);
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
