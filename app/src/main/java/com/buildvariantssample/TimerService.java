package com.buildvariantssample;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TimerService extends Service {

    NotificationCompat.Builder mBuilder;
    NotificationManagerCompat mNotificationManager;
    Notification mNotification;
    public static final int NOTIFICATION_ID=200;
    public static final String CHANNEL_ID="timer";

    public static final long TOTAL_SECONDS=5*60*1000;   ///5 min

    public static final long INTERVAL=1000;   ///5 min


    @Override
    public void onCreate() {
        super.onCreate();
        startTimer();
        startForeground(NOTIFICATION_ID,mNotification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY ;
    }


    /*
       start timer for 5 minutes
     */

    public void startTimer()
    {

        new CountDownTimer(TOTAL_SECONDS, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

              createNotification(millisUntilFinished);


            }

            @Override
            public void onFinish() {

                mNotificationManager.cancel(NOTIFICATION_ID);
                stopForeground(true);
                stopSelf();

            }
        }.start();

    }


    /*
      Create and Display Notification
     */
    public void createNotification(long millisUntilFinished)
    {
        String timeLeft="Time Left: " + String.format("%d min: %d sec", TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You are on the break")
                .setContentText(timeLeft)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mNotificationManager = NotificationManagerCompat.from(this);
        mNotification= mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }


}
