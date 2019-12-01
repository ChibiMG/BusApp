package fr.istic.mob.busappmaudsaly;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CreateData extends IntentService {

    private URL url;

    private Notification notification;

    private String CHANNEL_ID = "FSC";

    private Looper mServiceLooper;
    private CreateData.ServiceHandler myServiceHandler;

    public CreateData() {
        super("CreateData");
        try {
            url = new URL("https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public int onStartCommand(Intent intent, int flags, int startID) {
        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Notif").build();
        startForeground(1, notification);
        Message msg = myServiceHandler.obtainMessage();
        msg.arg1 = startID;
        myServiceHandler.sendMessage(msg);


        Toast.makeText(this, "BDD ok", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        HandlerThread thead = new HandlerThread("SSA", Process.THREAD_PRIORITY_BACKGROUND);
        thead.start();
        mServiceLooper = thead.getLooper();
        myServiceHandler = new CreateData.ServiceHandler(mServiceLooper);
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


        @Override
        public void handleMessage(Message msg){
            String contenu;
            BufferedReader br;
            JSONObject json;
            JSONArray array;
            try{
                br = new BufferedReader(new InputStreamReader(url.openStream()));
                contenu = br.readLine();
                json = new JSONObject(contenu);
                array = new JSONArray(json.getString("records"));
                } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopSelf(msg.arg1);
        }
    }
}
