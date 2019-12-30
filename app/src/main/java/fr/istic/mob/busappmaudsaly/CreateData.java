package fr.istic.mob.busappmaudsaly;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.core.app.NotificationCompat;

public class CreateData extends IntentService {

    //Channel IDs
    private String CHANNEL_ID = "FSC";

    //notification du service
    private Notification notification;

    //CurentIDs
    SharedPreferences sharedPreferencesCurrentIDs;
    SharedPreferences.Editor editorIDs;

    //Map NewIDs
    private Map<String, String> newIDs;

    public static final int UPDATE_PROGRESS = 8344;

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

        //Creation des IDs actuels
        sharedPreferencesCurrentIDs = getSharedPreferences(getString(R.string.Current_Ids), 0);
        editorIDs = sharedPreferencesCurrentIDs.edit();

        //Recuperation des RecordIDs
        newIDs = (Map<String, String>) getSharedPreferences(getString(R.string.New_Ids),0).getAll();
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
    protected void onHandleIntent(Intent intent) {
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        try {

            for (Map.Entry<String, String> element : newIDs.entrySet()) {
                //creer utl et connection
                URL url = new URL(element.getValue());
                URLConnection connection = url.openConnection();
                connection.connect();

                // c'est utilisé pour voir la bar de progression
                int fileLength = connection.getContentLength();
                Log.d("Download", String.valueOf(fileLength));
                Log.d("Download", connection.getContentType());
                //telecharger le fichier
                InputStream input = new BufferedInputStream(connection.getInputStream());

                String path = getCacheDir().getPath() + "/" + element.getKey() + ".zip";
                Log.d("Download", path);
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // publishing the progress....
                    Bundle resultData = new Bundle();
                    resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                    receiver.send(UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                }

                // fermer les streams
                output.flush();
                output.close();
                input.close();

                unpackZip(getCacheDir().getPath()+"/",element.getKey()+".zip");
            }
            File[] files = getCacheDir().listFiles();
            Log.d("Files", String.valueOf(files.length));
            for (int i = 0; i < files.length; i++){
                Log.d("Files", "FileName" + files[i].getName());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);

        receiver.send(UPDATE_PROGRESS, resultData);
    }

    private void unpackZip(String path, String zipname){
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
