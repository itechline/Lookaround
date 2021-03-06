package lar.com.lookaround.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Attila_Dan on 16. 04. 14..
 */
public class SettingUtil {
    private static final String KEY_FILE = "LARPREFS";

    private static final String TOKEN_KEY = "LARTOKEN";

    private static final String LAT = "LAT";
    private static final String LNG = "LNG";

    private static final String BOOL = "BOOL";
    private static final String ARRAYLIST = "ARRAYLIST";

    public static final String getToken(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        Log.d("GET_TOKEN:", sharedPref.getString(TOKEN_KEY, ""));
        return sharedPref.getString(TOKEN_KEY, "");
    }

    public static final void setToken(Context ctx, String token) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN_KEY, token);
        editor.commit();
    }

    public static final double getLatForMap(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getFloat(LAT, 0);
    }

    public static final void setLatForMap(Context ctx, float d) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(LAT, d);
        editor.commit();
    }

    public static final double getLngForMap(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getFloat(LNG, 0);
    }

    public static final void setLngForMap(Context ctx, float d) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(LNG, d);
        editor.commit();
    }

    public static final String getAdmonitor(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(ARRAYLIST, null);
    }

    public static final void setAdmonitor(Context ctx, ArrayList<AdmonitorUtil> list) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //set.addAll(list);
        editor.putString(ARRAYLIST, list.toString());
        editor.commit();
    }

}
