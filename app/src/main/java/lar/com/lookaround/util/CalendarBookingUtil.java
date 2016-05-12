package lar.com.lookaround.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Attila_Dan on 16. 05. 12..
 */
public class CalendarBookingUtil {
    private int hours;
    private int minutes;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public CalendarBookingUtil(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    private static ArrayList<CalendarBookingUtil> appointments = new ArrayList<CalendarBookingUtil>();

    public static void addAppointment(int hours, int minutes) {
        appointments.add(new CalendarBookingUtil(hours, minutes));
    }

    public static ArrayList<CalendarBookingUtil> getAppointments() {
        return appointments;
    }
}
