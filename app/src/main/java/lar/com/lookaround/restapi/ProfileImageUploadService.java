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
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Attila_Dan on 16. 06. 22..
 */
public class ProfileImageUploadService extends AsyncTask<String, Integer, String> {
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    File toUpload;
    SoapObjectResult sor;

    public ProfileImageUploadService(File toUpload, SoapObjectResult sor) {
        this.toUpload = toUpload;
        this.sor = sor;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpsURLConnection urlConnection = null;

        try {
            long current = 0;
            long all = 0;
            //for (File files: toUpload) {
            //    all += files.length();
            //}
            //for (File file: toUpload) {
                URL url = new URL("https://bonodom.com/upload/uplodeprofilepicture?token=" + params[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
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
                        toUpload.getName() + "\"" + this.crlf);
                request.writeBytes(this.crlf);


                FileInputStream fis = new FileInputStream(toUpload);

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fis.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    current += bytesRead;
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
                        BufferedInputStream(urlConnection.getErrorStream());

                BufferedReader responseStreamReader =
                        new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();


                responseStream.close();
                urlConnection.disconnect();

                return stringBuilder.toString();

            //}
        } catch (Exception e) {
            Log.d("SoapService", e.toString());
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        Log.e("ERROR", "errorMsg " + s);
        sor.parseRerult(true);
    }
}
