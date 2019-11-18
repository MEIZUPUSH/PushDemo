package com.meizu.pushdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.meizu.cloud.pushinternal.DebugLogger;


/**
 * Created by liaojinlong on 16-4-21.
 */
public class ForegroundService extends Service {

    public static int FOREGROUND_SERVICE = 101;
    public static String START_FOREGROUND_SERVICE = "start_foreground_service";
    private String mChannelId;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.


    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DebugLogger.i("LocalService", "Received start id " + startId + ": " + intent);
        // In this sample, we'll use the same text for the ticker and the expanded notification
        if(START_FOREGROUND_SERVICE.equals(intent.getAction())){
            showNotification();
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

    private void createChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(mChannelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
    }

    private void showNotification() {
        CharSequence content = getText(R.string.notification_content);
        CharSequence title = getText(R.string.notification_title);

        Notification notification = new NotificationCompat.Builder(this, mChannelId)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.flyme_status_ic_notification))
                .setSmallIcon(R.drawable.mz_push_notification_small_icon)
                .setTicker(content)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .build();

        startForeground(FOREGROUND_SERVICE, notification);
    }
}
