package com.meizu.pushdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.meizu.cloud.pushinternal.DebugLogger;


/**
 * Created by liaojinlong on 16-4-21.
 */
public class ForegroundService extends Service {

    public static int FOREGROUND_SERVICE = 101;
    public static String START_FOREGROUD_SERVICE = "start_foreground_service";

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DebugLogger.i("LocalService", "Received start id " + startId + ": " + intent);
        // In this sample, we'll use the same text for the ticker and the expanded notification
        if(START_FOREGROUD_SERVICE.equals(intent.getAction())){
            CharSequence text = getText(R.string.local_service_started);

            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);

            // Set the info for the views that show in the notification panel.
            Notification notification = new Notification.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.flyme_status_ic_notification ))
                    .setSmallIcon(R.drawable.mz_push_notification_small_icon)  // the status icon
                    .setTicker(text)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle("PushSevice foreground")  // the label of the entry
                    .setContentText(text)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                    .build();

            startForeground(FOREGROUND_SERVICE,notification);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        stopForeground(true);
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
