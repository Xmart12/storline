package com.xmart.projects.storline.data;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.activities.MainActivity;

import java.io.IOException;


/**
 * Created by smart on 27/4/2017.
 */

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {

            String titulo, mensaje, url_img;

            titulo = remoteMessage.getData().get("title");
            mensaje = remoteMessage.getData().get("message");
            url_img = remoteMessage.getData().get("url_img");

            Intent i = new Intent(this, MainActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            final NotificationCompat.Builder notification = new NotificationCompat.Builder(this);

            notification.setContentTitle(titulo);
            notification.setContentText(mensaje);
            notification.setContentIntent(pendingIntent);
            notification.setAutoCancel(true);
            notification.setSmallIcon(R.mipmap.storline);

            Bitmap bmp = Picasso.get().load(url_img).get();

            notification.setLargeIcon(bmp);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification.build());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void mensajeTipo1(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(title);
        notification.setContentText(message);
        notification.setAutoCancel(true);
        notification.setSmallIcon(android.R.drawable.ic_dialog_alert);
        notification.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification.build());
    }

    private  void mensajeTipo2(RemoteMessage remoteMessage) {
        try {
            String titulo,mensaje,url_img;
            titulo = remoteMessage.getData().get("title");
            mensaje = remoteMessage.getData().get("message");
            url_img = remoteMessage.getData().get("url_img");
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
            final NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
            notification.setContentTitle(titulo);
            notification.setContentText(mensaje);
            notification.setContentIntent(pendingIntent);
            notification.setAutoCancel(true);
            notification.setSmallIcon(R.mipmap.ic_launcher);

            Bitmap bmp = Picasso.get().load(url_img).get();


            notification.setLargeIcon(bmp);
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class FiidService extends FirebaseInstanceIdService {
        @Override
        public void onTokenRefresh() {
            String recent_token = FirebaseInstanceId.getInstance().getToken();
            UserDriver ud = new UserDriver(this, "Messaging");

            ud.refreshToken(recent_token);
        }
    }



}
