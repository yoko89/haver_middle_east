package com.neklaway.havermiddle_east.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.visaLog.VisaLogActivity;

public class NotificationCreator {

    final Context context;
    String channelID;
    String channelName;
    String channelGroup;
    String channelGroupName;

    public NotificationCreator(Context context) {
        this.context = context;
    }

    public void CreateServiceNotification(String title, String content,int id){
        channelGroup = channelGroupName = "Services";
        channelID = channelName = "Notification";

        NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(channelGroup,channelGroupName);
        notificationManager.createNotificationChannelGroup(notificationChannelGroup);
        notificationChannel.setGroup(channelGroup);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelID);
        Notification notification = notificationBuilder.setSmallIcon(R.drawable.launcher_hb_small)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .build();

        notificationManager.notify(id, notification);
    }

    public Notification CreateForegroundServiceStart(String title, boolean onGoing){
        channelGroup = channelGroupName = "Services";
        channelID = channelName = "On going Service";

        NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(channelGroup,channelName);
        notificationManager.createNotificationChannelGroup(notificationChannelGroup);
        notificationChannel.setGroup(channelGroup);
        notificationManager.createNotificationChannel(notificationChannel);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelID);

        return notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.launcher_hb_small)
                .setContentTitle(title)
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setProgress(0, 0, true)
                .setOngoing(onGoing)
                .build();
    }

    public void CreateVisaNotification(String title, String content,int id){
        channelGroup = channelGroupName = "Services";
        channelID = channelName = "Visa";

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, VisaLogActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        android.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup(channelGroup,channelGroupName);
        notificationManager.createNotificationChannelGroup(notificationChannelGroup);
        notificationChannel.setGroup(channelGroup);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelID);
        Notification notification = notificationBuilder.setSmallIcon(R.drawable.launcher_hb_small)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setContentIntent(resultPendingIntent)
                .build();

        notificationManager.notify(id, notification);
    }
}
