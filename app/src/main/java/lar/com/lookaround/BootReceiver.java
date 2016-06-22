package lar.com.lookaround;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by laci on 16. 06. 21..
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmReceiver alarmReceiver = new AlarmReceiver();
            alarmReceiver.resetAllReminder(context);
        }
    }
}
