package com.example.myapp;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    Context context;
    public static int NOTIFICATION_ID = 21321;
    public String title;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        this.context=context;
        if(intent.getAction().equals("com.example.action.MY_RECEIVER"))
            title="myReceiver";
        else
            title="otherApp";
        showNotification();
    }

    protected void showNotification() {
        
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher,
                "myReceiver", System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MyReceiver.class), 0);
        
        notification.setLatestEventInfo(context, title, null,
                contentIntent);
        notificationManager.notify(NOTIFICATION_ID, notification);
        Toast.makeText(context, "My Receiver", Toast.LENGTH_LONG).show();
    }
}
