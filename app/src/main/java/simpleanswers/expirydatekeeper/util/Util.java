package simpleanswers.expirydatekeeper.util;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import simpleanswers.expirydatekeeper.R;
import simpleanswers.expirydatekeeper.activities.ExpiryActivity;

/**
 * Created by ruchadeshmukh on 4/26/15 .
 */
public class Util {
    public static String calculateDaysLeft(String date) {
        String daysLeft;

        String expDateList[] = date.split("/");

        Calendar cal1 = Calendar.getInstance();
        cal1.set(Integer.parseInt(expDateList[2]), Integer.parseInt(expDateList[0]), Integer.parseInt(expDateList[1]));
        long millisec1 = cal1.getTimeInMillis();

        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = sdf.format(System.currentTimeMillis() - 100);
        String currentDateList[] = currentDate.split("/");
        cal2.set(Integer.parseInt(currentDateList[2]), Integer.parseInt(currentDateList[0]), Integer.parseInt(currentDateList[1]));
        long millisec2 = cal2.getTimeInMillis();

        if (millisec1 >= millisec2) {
            long days = (millisec1 - millisec2) / (24 * 60 * 60 * 1000);
            daysLeft = String.valueOf(days) + "\nDays Left";
        } else {
            long days = (millisec2 - millisec1) / (24 * 60 * 60 * 1000);
            daysLeft = String.valueOf(days) + " Days\nPast Due";
        }

        return daysLeft;
    }

    public static String calNotificationTime(Context context, String expDate, String expTime, String notificationChoice){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse(expDate + " " + expTime);
        }catch (ParseException e){
            Log.e(ExpiryActivity.logTag, "Error while setting notification time. " + e.getMessage());
        }

        if(expiryDate==null)
            return "0";

        Calendar cal = Calendar.getInstance();
        cal.setTime(expiryDate);
        if(notificationChoice.equals(context.getString(R.string.pref_notification_time_day)))
            cal.add(Calendar.DATE, -1);
        if(notificationChoice.equals(context.getString(R.string.pref_notification_time_week)))
            cal.add(Calendar.DATE, -7);
        if(notificationChoice.equals(context.getString(R.string.pref_notification_time_hour)))
            cal.add(Calendar.HOUR, -1);

        return dateFormat.format(cal.getTime());
    }
}
