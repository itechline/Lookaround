package lar.com.lookaround.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Attila_Dan on 16. 04. 14..
 */
public class SettingUtil {
    private static final String KEY_FILE = "LARPREFS";

    private static final String TOKEN_KEY = "LARTOKEN";

    private static final String LAT = "LAT";
    private static final String LNG = "LNG";

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

    public static final String getLatForMap(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        Log.d("GET_TOKEN:", sharedPref.getString(LAT, ""));
        return sharedPref.getString(LAT, "");
    }

    public static final void setLatForMap(Context ctx, String token) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LAT, token);
        editor.commit();
    }

    public static final String getLngForMap(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        Log.d("GET_TOKEN:", sharedPref.getString(LNG, ""));
        return sharedPref.getString(LNG, "");
    }

    public static final void setLngForMap(Context ctx, String token) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LNG, token);
        editor.commit();
    }

}
