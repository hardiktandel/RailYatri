package com.example.railyatri.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.railyatri.Activity.CoolieActivity;
import com.example.railyatri.Activity.FoodShopActivity;
import com.example.railyatri.Activity.HomeActivity;
import com.example.railyatri.Activity.PassTicketActivity;
import com.example.railyatri.R;
import com.example.railyatri.Utils.SharedPref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPref pref = new SharedPref(this);
        String id = pref.getID();

        //handle when receive notification via data event
        if (remoteMessage.getData().size() > 0) {
            switch (Objects.requireNonNull(remoteMessage.getData().get("type"))) {
                case "shop":
                    if (id.equalsIgnoreCase(remoteMessage.getData().get("id"))) {
                        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), FoodShopActivity.class);
                    }
                    break;
                case "coolie":
                    if (id.equalsIgnoreCase(remoteMessage.getData().get("id"))) {
                        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), CoolieActivity.class);
                    }
                    break;
                case "student":
                case "user":
                    if (id.equalsIgnoreCase(remoteMessage.getData().get("id"))) {
                        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), PassTicketActivity.class);
                    }
                    break;
            }
        }

        //handle when receive notification
//        if (remoteMessage.getNotification() != null) {
//            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//        }

    }

    public void showNotification(String title, String message, Class<?> targetClass) {
        String channel_id = "e_rail_channel";
        Intent[] intents = new Intent[]{
                new Intent(Intent.ACTION_VIEW, Uri.EMPTY, this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                new Intent(Intent.ACTION_VIEW, Uri.EMPTY, this, targetClass)};
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getResources().getColor(R.color.white))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round))
                .setSound(uri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "E-Rail", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationChannel.setShowBadge(true);
            Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);
        }

        Objects.requireNonNull(notificationManager).notify(0, builder.build());
    }
}
