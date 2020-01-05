package fr.istic.mob.busappmaudsaly.task;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.TelechargementActivity;

public class SiteConsultationTask extends Worker {

    //URL à consulter
    private URL url;

    //Channel IDs
    private String CHANNEL_ID = "FSC";

    //ID actuels
    private Map<String, String> currentIDs;

    //Id a verifier
    SharedPreferences sharedPreferencesNewIDs;
    SharedPreferences.Editor editorNewIDs;

    public SiteConsultationTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        //URL a verifier
        try {
            url = new URL("https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        //Pour New IDs
        sharedPreferencesNewIDs = getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.New_Ids), 0);
        editorNewIDs = sharedPreferencesNewIDs.edit();

        retreiveNewIds();
        notificationMAJ();

        return Result.success();
    }

    private void retreiveNewIds() {
        editorNewIDs.clear();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String contenu = br.readLine();
            JSONObject json = new JSONObject(contenu);
            JSONArray array = new JSONArray(json.getString("records"));

            //Ajouter les recordIDs
            for (int i = 0; i < array.length(); i++){
                JSONObject tab = new JSONObject(array.getString(i));
                String recordid = tab.getString("recordid");

                JSONObject fields = tab.getJSONObject("fields");
                String url = fields.getString("url");

                editorNewIDs.putString(recordid,url);
                editorNewIDs.commit();
            }

        }catch (IOException e){
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Fonction de la notification de MAJ
    public void notificationMAJ(){
        //Récuperer ids actuels
        currentIDs = (Map<String, String>) getApplicationContext().getSharedPreferences(getApplicationContext().getString(R.string.Current_Ids),0).getAll();

        //Si recordIDs != de IDs)
        if (!currentIDs.keySet().containsAll(sharedPreferencesNewIDs.getAll().keySet())){
            NotificationChannel serviceChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serviceChannel = new NotificationChannel(CHANNEL_ID, "ABC", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
            Intent intent = new Intent(getApplicationContext(), TelechargementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setContentTitle("Mise à jour disponible").setContentText("Mettre à jour les horaires des bus").setSmallIcon(R.drawable.ic_update_black_24dp).setContentIntent(pendingIntent).setAutoCancel(true).build();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(2, notification);
        }
    }
}
