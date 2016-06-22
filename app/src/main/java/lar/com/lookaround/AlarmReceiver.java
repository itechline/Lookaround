package lar.com.lookaround;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by laci on 16. 06. 21..
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public void cancelPeriodReminder(Context context) {
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager!= null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            alarmManager.cancel(pendingIntent);
        }
    }

    public void resetAllReminder(Context context) {
        cancelPeriodReminder(context);

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 15 * 60 * 1000, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);

        startWakefulService(context, service);
    }
}
