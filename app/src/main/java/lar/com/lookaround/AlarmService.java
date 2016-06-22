package lar.com.lookaround;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import lar.com.lookaround.restapi.SoapObjectResult;
import lar.com.lookaround.util.LoginUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class AlarmService extends IntentService {

    public static final String TAG = "AlarmService";

    public AlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LoginUtil.getNotifications(getBaseContext(), new SoapObjectResult() {
            @Override
            public void parseRerult(Object result) {
                ArrayList<String> r = (ArrayList<String>) result;
                if (r.size() == 2) {
                    sendNotification(r.get(0), r.get(1));
                }
            }
        });
    }


    private void sendNotification(String title, String msg) {
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, iUniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int icon = R.mipmap.ic_launcher;


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setVibrate(new long[]{1000, 1000, 1000})
                        .setLights(Color.BLUE, 3000, 3000)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
