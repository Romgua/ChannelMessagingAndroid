package romain.guarnotta.channelmessaging.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Set;

import romain.guarnotta.channelmessaging.R;

public class GCMBroadcastReceiver extends BroadcastReceiver {
    private static final int UNIQUE_ID = 1;

    public GCMBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Set<String> list = intent.getExtras().keySet();
        if (list.contains("message") && list.contains("channelid") && list.contains("fromUser")) {
            showNotification(context,
                    intent.getStringExtra("message"),
                    intent.getIntExtra("channelID", 0),
                    intent.getStringExtra("username"));
        }
    }

    public void showNotification(Context context, String message, Integer channelID, String username) {
        Intent intentStreamingUI = new Intent(context, LoginActivity.class);
//        intentStreamingUI.setAction(MY_CUSTOM_ACTION);
//        intentStreamingUI.putExtra(EXTRA_MY_EXTRA,someExtraData);
        intentStreamingUI.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            intentStreamingUI.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intentStreamingUI,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder noti = new NotificationCompat.Builder(context)
                .setContentTitle("Message from "+username)
                .setContentText(message)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 250, 200, 250, 100, 100, 100, 100, 100, 100})
                        //Set the color of the notification led (example on Nexus 5)
                .setLights(Color.parseColor("#006ab9"), 2000, 1000)
                .setSmallIcon(android.R.drawable.ic_dialog_email);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            noti.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        noti.setContentIntent(pendingIntent);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("My Big Content Title");
        // Moves events into the expanded layout
        inboxStyle.addLine("My Long Text");
        inboxStyle.addLine("My Long Text");
        inboxStyle.addLine("My Long Text");
        inboxStyle.addLine("My Long Text");
        // Moves the expanded layout object into the notification object.
        noti.setStyle(inboxStyle);
        Notification notification = noti.build();
        notification.flags|= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(UNIQUE_ID, notification);
    }
}
