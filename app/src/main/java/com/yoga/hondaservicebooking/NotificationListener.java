package com.yoga.hondaservicebooking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.yoga.hondaservicebooking.R;
import com.yoga.hondaservicebooking.lib.Constants;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;


//Class extending service as it is a service that will run in background
public class NotificationListener extends Service {

    NotificationManager notificationManager;
    m_auth auth;
    m_akun akun;
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        auth = new m_auth(getApplicationContext());
        akun = auth.getAkun();

        //Creating a firebase object
        Firebase firebase = new Firebase(Constants.FIREBASE_APP + akun.getID());
        firebase.addValueEventListener(new ValueEventListener() {

            //This method is called whenever we change the value in firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                setNotif(1, "Event",snapshot, "event");
                setNotif(2, "Booking",snapshot, "booking");
                setNotif(3, "Service Kendaraan Selesai",snapshot, "service");
            }

            private void setNotif(int id, String title, DataSnapshot snapshot, String key){

                NotificationCompat.InboxStyle group;
                long num = snapshot.child(key).getChildrenCount();
                if (num>0){
                    Iterable<DataSnapshot> data = snapshot.child(key).getChildren();
                    if (num>1){
                        group = new NotificationCompat.InboxStyle();
                        DataSnapshot one = data.iterator().next();
                        String msg = one.getValue().toString();
                        String first = msg;
                        for(int i=0;i<num-1;i++){
                            if (msg.equals("none"))
                                return;
                            group.addLine(msg);
                            one = data.iterator().next();
                            msg = one.getValue().toString();
                        }
                        group.addLine(msg);
                        title = snapshot.child(key).getChildrenCount() + " " + title;
                        group.setBigContentTitle(title);

                        showNotification(id, title, first,group, key);
                    }else{
                        DataSnapshot one = data.iterator().next();
                        String msgid = one.getKey().toString();
                        String msg = one.getValue().toString();
                        showNotification(id, title, msgid, msg, key);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });

        return START_STICKY;
    }


    private void showNotification(int id, String title, String msg, NotificationCompat.InboxStyle group, String kategori ){
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (kategori.equalsIgnoreCase("event")){
            Intent intent = new Intent(getApplicationContext(), Main.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,  PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        } else if(kategori.equalsIgnoreCase("booking")){
            Intent intent = new Intent(getApplicationContext(), Booking.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,  PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        } else  if (kategori.equalsIgnoreCase("service")){
            Intent intent = new Intent(getApplicationContext(), Riwayat.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,  PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        }
       // Creating a notification
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setStyle(group);
        builder.setContentText(msg);
        builder.setVibrate(new long[] { 300, 300, 300, 300, 300 });
        builder.setSound(soundUri);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }


    private void showNotification(int id, String title, String msgid, String msg, String kategori){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        if (kategori.equalsIgnoreCase("event")){
            Intent intent = new Intent(getApplicationContext(), Event.class);
            intent.putExtra("id", msgid);
            intent.putExtra("judul", msg);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,  PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        } else if(kategori.equalsIgnoreCase("booking")){
            Intent intent = new Intent(getApplicationContext(), Booking.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,  PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        } else  if (kategori.equalsIgnoreCase("service")){
            Intent intent = new Intent(getApplicationContext(), RiwayatDetail.class);
            intent.putExtra("id", Integer.parseInt(msgid));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent,  PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        }

        //Creating a notification
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setVibrate(new long[] { 300, 300, 300, 300, 300 });
        builder.setSound(soundUri);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}