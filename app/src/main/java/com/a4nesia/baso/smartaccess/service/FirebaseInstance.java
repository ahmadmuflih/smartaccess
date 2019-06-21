package com.a4nesia.baso.smartaccess.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.activities.ConfirmPinActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.hawk.Hawk;

import java.util.Map;

import androidx.core.app.NotificationCompat;

public class FirebaseInstance extends FirebaseMessagingService {
    private static final String TAG = "blabla";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData()!=null){
            sendNotif(remoteMessage);
        }
    }
    private void sendNotif(RemoteMessage remoteMessage){
        Hawk.init(getApplicationContext()).build();
        String token = Hawk.get("token","");
        if(!"".equals(token)) {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String content = data.get("message");
            int access_id = Integer.parseInt(data.get("access_id"));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "id1";
            Intent resultIntent = new Intent(this, ConfirmPinActivity.class);
            resultIntent.putExtra("access_id", access_id);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("deskripsi");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationChannel.setShowBadge(true);
                notificationManager.createNotificationChannel(notificationChannel);


            }
            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
            notiBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title)
                    .setSmallIcon(R.color.colorAccent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                    .setContentText(content)
                    .setColor(24714829);
            notiBuilder.setContentIntent(resultPendingIntent);
            notificationManager.notify(1, notiBuilder.build());
        }
    }

}
