package fr.istic.mob.busappmaudsaly;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * @author Maud Garcon & Saly Knab
 *
 * Service qui consulte le site : https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/export/
 * On recupere : https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td
 *
 * Il regarde si on nouveau fichier CSV est disponible.
 *
 * TODO : enregister la verion (pas dans le service)
 * TODO : test : lors de l'installation on télécharge automatiquement le 1 fichier JSON (mais pas zip qui est dedans) ?
 * TODO : le service se lance au démarrage du téléphone
 * TODO : appelle réseau en periodique : workmanager => service qui regarde de temps en temps si nouvelle version (dans ce cas si tel echou ou aura toujours pas la bonne version donc une nouvelle notif plus tard)
 */

public class SiteConsultation extends Service{

    //URL à consulter
    private URL url;

    //Channel IDs
    private String CHANNEL_ID = "FSC";
    private String CHANNEL_ID1 = "ABC";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    //notification du service
    private Notification notification;

    //notification de la mise à jour
    private Notification notificationMAJ;

    //ID actuels
    // TODO ici ou pas ?
    private Map<String, String> ids;
    SharedPreferences sharedPreferencesIDs;
    SharedPreferences.Editor editorIDs;

    //Id a verifier
    private Map<String, String> recordids;
    SharedPreferences sharedPreferencesRIDs;
    SharedPreferences.Editor editor1RIDs;

    public SiteConsultation() {
        recordids = new HashMap<>();
        ids = new HashMap<>();

        //URL a verifier
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
        //Creation du thread
        HandlerThread thead = new HandlerThread("SSA", Process.THREAD_PRIORITY_BACKGROUND);
        thead.start();
        mServiceLooper = thead.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        //Notification du service
        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Service en route").setPriority(Notification.PRIORITY_DEFAULT).build();
        startForeground(1, notification);

        //TODO : ici ou pas ?
        sharedPreferencesIDs = PreferenceManager.getDefaultSharedPreferences(this);
        editorIDs = sharedPreferencesIDs.edit();

        sharedPreferencesRIDs = PreferenceManager.getDefaultSharedPreferences(this);
        editor1RIDs = sharedPreferencesRIDs.edit();

        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);

        Toast.makeText(this, "Services ok", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    //Fonction de la notification channel
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

                //Ajouter les recordIDs
                for (int i = 0; i < array.length(); i++){
                    JSONObject tab = new JSONObject(array.getString(i));
                    String recordid = tab.getString("recordid");

                    JSONObject fields = tab.getJSONObject("fields");
                    String url = fields.getString("url");

                    editorIDs.putString(recordid,url);
                    editorIDs.commit();
                    recordids.put(tab.getString("recordid"), fields.getString("url"));
                }

            }catch (IOException e){
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            stopSelf(msg.arg1);

            //Notification de MAJ
            notificationMAJ();
        }

    }

    //Fonction de la notification de MAJ
    public void notificationMAJ(){
        //Si recordIDs != de IDs)
        if (!sharedPreferencesIDs.getAll().containsKey(sharedPreferencesRIDs.getAll().keySet())){
            NotificationChannel serviceChannel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                serviceChannel = new NotificationChannel(CHANNEL_ID1, "ABC", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
            Intent intent = new Intent(this, TelechargementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            notification = new NotificationCompat.Builder(this, CHANNEL_ID1).setContentTitle("Mise à jour disponible").setContentText("Mettre à jour les horaires des bus").setSmallIcon(R.drawable.ic_update_black_24dp).setContentIntent(pendingIntent).setAutoCancel(true).build();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(2, notification);
        }
    }
}
