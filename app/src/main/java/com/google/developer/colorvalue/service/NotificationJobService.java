package com.google.developer.colorvalue.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;

public class NotificationJobService extends JobService {

    public static final int NOTIFICATION_ID = 18;
    public static String ANDROID_CHANNEL_ID = "android";
    public static String ANDROID_CHANNEL_NAME = "colorvalue_android";


    @Override
    public boolean onStartJob(JobParameters params) {
        // TODO notification

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel androidChannel = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.enableLights(true);
            androidChannel.setLightColor(Color.GREEN);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(androidChannel);

            Notification.Builder builder = new Notification.Builder(this, ANDROID_CHANNEL_ID)
                    .setContentTitle(getString(R.string.time_to_practice))
                    .setContentText(getString(R.string.it_is_time_to_practice))
                    .setContentIntent(contentPendingIntent)
                    .setSmallIcon(R.drawable.ic_dialog_info)
                    .setChannelId(ANDROID_CHANNEL_ID)
                    .setAutoCancel(true);
            manager.notify(NOTIFICATION_ID, builder.build());
        }
        else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.time_to_practice))
                    .setContentText(getString(R.string.it_is_time_to_practice))
                    .setContentIntent(contentPendingIntent)
                    .setSmallIcon(R.drawable.ic_dialog_info)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);
            manager.notify(NOTIFICATION_ID, builder.build());
        }


        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(NOTIFICATION_ID);
        }
        return false;
    }

}