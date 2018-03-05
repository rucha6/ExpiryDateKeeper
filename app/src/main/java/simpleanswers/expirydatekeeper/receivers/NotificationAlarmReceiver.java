package simpleanswers.expirydatekeeper.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import simpleanswers.expirydatekeeper.R;

/**
 * Created by ruchadeshmukh on 12/20/15.
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("Title");
        String desc = intent.getStringExtra("Description");
        Random random = new Random();
        int id = random.nextInt(9999 - 1000) + 1000;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_edk)
                        .setContentTitle("Expiry: " + title)
                        .setContentText(desc);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
