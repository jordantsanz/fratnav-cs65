package com.example.fratnav.tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.fratnav.R;

import com.example.fratnav.MainActivity;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServce";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String notificationTitle = null, notificationBody = null;
        Log.d(TAG, "onMessageReceived: ");

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            Log.d(TAG, "Message Notification Title:  " + remoteMessage.getNotification().getTitle());
            notificationBody = remoteMessage.getNotification().getBody();
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here
        sendLocalNotification(notificationTitle, notificationBody);
    }

    private void sendLocalNotification(String notificationTitle, String notificationBody) {
        FirebaseUser currentUser = AuthenticationHelper.getCurrentUser();
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                if (user.notificationSettings) {
                    Log.d(TAG, "sendLocalNotification: ");
                    Intent intent = new Intent(getApplicationContext(), Forum.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.greek_nav_logo);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setAutoCancel(true)   //Automatically delete the notification
                            .setSmallIcon(R.drawable.greek_nav_logo) //Notification icon
                            .setLargeIcon(icon)
                            .setContentIntent(pendingIntent)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationBody)
                            .setSound(defaultSoundUri);
                    Log.d(TAG, "sendLocalNotification: about to send");


                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    // === Removed some obsoletes
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelId = "Your_channel_id";
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
                        notificationBuilder.setChannelId(channelId);
                    }

                    notificationManager.notify(0, notificationBuilder.build());
                    Log.d(TAG, "sendLocalNotification: notified");
                }
            }
        });


    }
}