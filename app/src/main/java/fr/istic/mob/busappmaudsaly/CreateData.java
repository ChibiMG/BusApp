package fr.istic.mob.busappmaudsaly;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CreateData extends IntentService {

    //Channel IDs
    private String CHANNEL_ID = "FSC";

    //notification du service
    private Notification notification;

    //Map recordIDs
    private Map<String, String> recordIDs;

    public CreateData() {
        super("CreateData");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Notification du service
        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Service en route").setPriority(Notification.PRIORITY_DEFAULT).build();
        startForeground(1, notification);

        //Recuperation des RecordIDs
        recordIDs = getSharedPreferences(sharedPreferencesRecordIDs,0);
    }

    //Fonction de la notification channel
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "FSC", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {}

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
