package br.dexter.barmoes.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import br.dexter.barmoes.MenuPrincipal.menuPrincipal;
import br.dexter.barmoes.R;

public class BaseNotification extends BroadcastReceiver
{
    public static final String BaseAction = "br.dexter.barmoes";
    private DatabaseReference databaseReference;

    public BaseNotification() { }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            startBaseAlarmManager(context);
        }
        else if(BaseAction.equals(intent.getAction()))
        {
            StartYourService(context);
        }
    }

    private void StartYourService(@NotNull final Context context)
    {
        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(BaseAction, "Notification", NotificationManager.IMPORTANCE_DEFAULT);

                    if (notificationManager != null) {
                        notificationChannel.setDescription("EDMT Channel");
                        notificationChannel.enableLights(true);
                        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                        notificationChannel.enableLights(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, BaseAction);
                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_hamburguer)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentTitle("Hambúrguer's")
                        .setContentText("Um novo pedido foi adicionado em uma mesa")
                        .setAutoCancel(true);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, menuPrincipal.class), PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pendingIntent);

                if (notificationManager != null)
                    notificationManager.notify(0, notificationBuilder.build());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public static void startBaseAlarmManager(Context context)
    {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BaseNotification.class);
        intent.setAction(BaseAction);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmMgr != null) {
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000, 3000, alarmIntent);
        }
    }

    public static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, BaseNotification.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}