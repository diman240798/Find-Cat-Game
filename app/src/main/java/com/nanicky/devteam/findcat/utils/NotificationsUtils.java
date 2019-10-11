package com.nanicky.devteam.findcat.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.nanicky.devteam.findcat.MainActivity;

import srsdt1.findacat.R;

public class NotificationsUtils {
    private static final String CHANNEL_ID = "findacat";
    public static final int REGULAR_NOTIFICATION_ID = 1;
    private static Bitmap largeBitmap;

    public static void showReminderNotification(Context context) {
        String string = context.getString(R.string.app_name);
        String string2 = context.getString(R.string.regular_notification);
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder create = TaskStackBuilder.create(context);
        create.addNextIntentWithParentStack(intent);
        showNotification(context, string, string2, 1, create.getPendingIntent(0, 134217728), false);
    }

    public static void showNotification(Context context, String str, String str2, int i, PendingIntent pendingIntent, boolean z) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                String string = context.getString(R.string.channel_name);
                String string2 = context.getString(R.string.channel_description);
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, string, 3);
                notificationChannel.setDescription(string2);
                notificationChannel.enableVibration(false);
                notificationChannel.enableLights(false);
                notificationChannel.setVibrationPattern(new long[]{0});
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder ongoing = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.status_smal_icon).setContentTitle(str).setContentText(str2).setOngoing(z);
            if (Build.VERSION.SDK_INT >= 26) {
                if (largeBitmap == null) {
                    largeBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round, new BitmapFactory.Options());
                }
                ongoing.setLargeIcon(largeBitmap);
            }
            if (pendingIntent != null) {
                ongoing.setContentIntent(pendingIntent);
            }
            notificationManager.notify(i, ongoing.build());
        }
    }
}