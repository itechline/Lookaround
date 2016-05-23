package lar.com.lookaround.restapi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Attila_Dan on 16. 05. 11..
 */
public class ImageUploadService extends AsyncTask<String, Integer, String> {

    SoapResult result;

    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    File toUpload;
    ProgressDialog pg;

    public ImageUploadService(SoapResult result, File toUpload, ProgressDialog pg) {
        this.result = result;
        this.toUpload = toUpload;
        this.pg = pg;
    }

    @Override
    protected String doInBackground(String... params) {
        //HttpURLConnection urlConnection = null;

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("https://bonodom.com/upload/uploadtoserver?ing_hash=" + params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


            DataOutputStream request = new DataOutputStream(
                    urlConnection.getOutputStream());

            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    "file" + "\";filename=\"" +
                    this.toUpload.getName() + "\"" + this.crlf);
            request.writeBytes(this.crlf);


            FileInputStream fis = new FileInputStream(toUpload);

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1*1024*1024;

            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fis.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                if (pg != null) {
                    pg.setMessage("LOFASZ...");
                    pg.setProgress(8);
                    pg.show();
                    Log.d("PROGRESSBAR", "CALLED");
                }
                request.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }

            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            fis.close();
            request.flush();
            request.close();

            InputStream responseStream = new
                    BufferedInputStream(urlConnection.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();

            responseStream.close();
            urlConnection.disconnect();

            return response;

            /*OutputStream os = urlConnection.getOutputStream();
            ByteArrayOutputStream
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(bitmap));


            writer.flush();
            writer.close();
            os.close();*/

            /*InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            int content;
            while((content = in.read()) != -1) {
                result.append((char)content);
            }
            in.close();*/

            //return result.toString();
        } catch (Exception e) {
            Log.d("SoapService", e.toString());
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        result.parseRerult(s);
    }
}
