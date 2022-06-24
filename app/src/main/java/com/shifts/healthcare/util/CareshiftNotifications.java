package com.shifts.healthcare.util;

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
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shifts.healthcare.R;
import com.shifts.healthcare.activites.HomeActivity;
import com.shifts.healthcare.workerSide.WorkerHomeAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CareshiftNotifications extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String notificationType, title, meetupId, messageId;
    private String message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "payload:" + remoteMessage.getData());

            Map<String,String> map = remoteMessage.getData();

            try {
                sendNotification("","",map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "onMessageReceived for FCM");
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "c: " + remoteMessage.getData());
            try {
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String message, String title,Map<String,String> map) throws JSONException {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject(map);
        String key = jsonObject.getString("key");
        Intent intent = new Intent();
        String key1 = jsonObject.getString("message");
        String title1 = jsonObject.getString("title");

        if(key.equalsIgnoreCase("shiftsstaus"))
        {

            String type = jsonObject.getString("type");

            if(type.equalsIgnoreCase("User"))
            {
                intent = new Intent(this, HomeActivity.class);
            }
            else
            {
                intent = new Intent(this, WorkerHomeAct.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        } else  if(key.equalsIgnoreCase("rehireshifts"))
        {

            String type = jsonObject.getString("type");

            if(type.equalsIgnoreCase("User"))
            {
                intent = new Intent(this, HomeActivity.class);
            }
            else
            {
                intent = new Intent(this, WorkerHomeAct.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }else  if(key.equalsIgnoreCase("recruitmentshiftrequest"))
        {

            String type = jsonObject.getString("type");

            if(type.equalsIgnoreCase("User"))
            {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("key","recruitmentshiftrequest");
            }
            else
            {
                intent = new Intent(this, WorkerHomeAct.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }else  if(key.equalsIgnoreCase("rehireshiftsforworker"))
        {

            String type = jsonObject.getString("type");

            if(type.equalsIgnoreCase("User"))
            {
                intent = new Intent(this, HomeActivity.class);
            }
            else
            {
                intent = new Intent(this, WorkerHomeAct.class);
                intent.putExtra("key","rehireshiftsforworker");
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }else  if(key.equalsIgnoreCase("RehirerequestRejected"))
        {
            String type = jsonObject.getString("type");
            if(type.equalsIgnoreCase("User"))
            {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("key","RehirerequestRejected");
            }
            else
            {
                intent = new Intent(this, WorkerHomeAct.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }else  if(key.equalsIgnoreCase("chatmessage"))
        {
            String type = jsonObject.getString("type");

            String senderId = jsonObject.getString("sender_id");

            if(type.equalsIgnoreCase("User"))
            {

                if(Util.appInForeground(this))
                {
                    Intent intent1 = new Intent("filter_string_1");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                }

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("key","chat");
                intent.putExtra("senderId",senderId);
            }
            else
            {


                if(Util.appInForeground(this))
                {
                    Intent intent1 = new Intent("filter_string_1");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);

                }

                intent = new Intent(this, WorkerHomeAct.class);
                intent.putExtra("key","chat");
                intent.putExtra("senderId",senderId);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }else  if(key.equalsIgnoreCase("adminmessage"))
        {
            String type = jsonObject.getString("type");

            String senderId = jsonObject.getString("sender_id");

            if(type.equalsIgnoreCase("User"))
            {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("key","adminmessage");
                intent.putExtra("senderId",senderId);

                if(Util.appInForeground(this))
                {
                    Intent intent1 = new Intent("filter_string");
                    intent.putExtra("key", "My Data");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                }

            }
            else
            {
                intent = new Intent(this, WorkerHomeAct.class);
                intent.putExtra("key","adminmessage");
                intent.putExtra("senderId",senderId);
                if(Util.appInForeground(this))
                {
                    Intent intent1 = new Intent("filter_string");
                    intent.putExtra("key", "My Data");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                }

            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }else if (key.equalsIgnoreCase("invoice")) {
            String type1 = jsonObject.getString("type");

            if (type1.equalsIgnoreCase("User")) {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("key", "invoice");

            } else {
                intent = new Intent(this, WorkerHomeAct.class);
                intent.putExtra("key", "invoice");

            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_right_logo);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(title1)
                .setContentText(key1)
                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSubText(title1)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(key1))
                 ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
