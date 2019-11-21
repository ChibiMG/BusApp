package fr.istic.mob.busappmaudsaly;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * @author Maud Garcon & Saly Knab
 *
 * Service qui consulte le site : https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/export/
 * On recupere : https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td
 *
 * Qui regarde si on nouveau fichier CSV est disponible
 *
 * TODO : rechercher comment trouver le nouveau fihcier csv dispo sur le site
 * TODO : faire une notification comme quoi nouveau csv
 * TODO : cliqué et ouvre l'activity TelechargementActivity
 * TODO : enregister la verion (pas dans le service)
 * TODO : service qui regarde de temps en temps si nouvelle version (dans ce cas si tel echou ou aura toujours pas la bonne version donc une nouvelle notif plus tard)
 * TODO : test : lors de l'installation on télécharge automatiquement le 1 fichier JSON (mais pas zip qui est dedans)
 * TODO : le service se lance au démarrage du téléphone
 * TODO : appelle réseau en periodique : workmanager
 */

public class SiteConsultation extends Service{

    private URL url;
    private String contenu;
    private BufferedReader br;

    private String CHANNEL_ID = "FSC";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private Notification notification;

    public SiteConsultation() {
        try {
            url = new URL("https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Notif").build();
        startForeground(1, notification);

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startID;
        mServiceHandler.sendMessage(msg);

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            contenu = br.readLine();
            System.out.println(contenu);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Services ok", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HandlerThread thead = new HandlerThread("SSA", Process.THREAD_PRIORITY_BACKGROUND);
        thead.start();
        mServiceLooper = thead.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        //super.onCreate();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "FSC", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }

    private class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper){
            super(looper);
        }

        /*
        @Override
        public void handleMessage(Message msg){
            try{
                createNotificationChannel();
                //Notification notification = new NotificationCompat.Builder().setContentTitle("Notif").build();
                startForeground(1, notification);
            }catch (){
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }

         */
    }
}
