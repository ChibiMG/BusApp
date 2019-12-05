package fr.istic.mob.busappmaudsaly;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private String CHANNEL_ID = "FSC";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private Notification notification;

    private Map<String, String> ids;
    private Map<String, String> recordids;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor1;

    public SiteConsultation() {
        recordids = new HashMap<>();
        ids = new HashMap<>();

        try {
            url = new URL("https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thead = new HandlerThread("SSA", Process.THREAD_PRIORITY_BACKGROUND);
        thead.start();
        mServiceLooper = thead.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Notif").setPriority(Notification.PRIORITY_DEFAULT).build();
        startForeground(1, notification);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        editor1 = sharedPreferences.edit();

        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);

        Toast.makeText(this, "Services ok", Toast.LENGTH_LONG).show();
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


        @Override
        public void handleMessage(Message msg){
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String contenu = br.readLine();
                JSONObject json = new JSONObject(contenu);
                JSONArray array = new JSONArray(json.getString("records"));
                recordids.clear();

                for (int i = 0; i < array.length(); i++){
                    JSONObject tab = new JSONObject(array.getString(i));
                    String recordid = tab.getString("recordid");

                    JSONObject fields = tab.getJSONObject("fields");
                    String url = fields.getString("url");

                    editor.putString(recordid,url);
                    editor.commit();
                    recordids.put(tab.getString("recordid"), fields.getString("url"));
                }

            }catch (IOException e){
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            stopSelf(msg.arg1);

            notificationMAJ();
        }

    }

    public void notificationMAJ(){
        if (!sharedPreferences.getAll().containsKey(sharedPreferences1.getAll().keySet())){
            NotificationChannel serviceChannel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                serviceChannel = new NotificationChannel("saly", "saly", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
            notification = new NotificationCompat.Builder(this, "saly").setContentTitle("Mise à jour disponible").setContentText("Mettre à jour les horaires des bus").build();
            startForeground(2, notification);
            System.out.println("Je suis ici");
        }
    }
}
