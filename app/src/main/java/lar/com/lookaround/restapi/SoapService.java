package lar.com.lookaround.restapi;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dante on 2015.05.13..
 */
public class SoapService extends AsyncTask<URL, Integer, String> {

    SoapResult result;
    HashMap<String, String> postData;

    public SoapService(SoapResult result, HashMap<String, String> postData) {
        this.result = result;
        this.postData = postData;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected String doInBackground(URL... params) {
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) params[0].openConnection();
            StringBuilder result = new StringBuilder();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postData));

            writer.flush();
            writer.close();
            os.close();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            int content;
            while((content = in.read()) != -1) {
                result.append((char)content);
            }
            in.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SoapService", "link: " + params[0].toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        result.parseRerult(s);
    }
}
