package fr.istic.mob.busappmaudsaly.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.opencsv.CSVReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.arch.core.util.Function;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.database.BusRoute;
import fr.istic.mob.busappmaudsaly.database.Trip;

public class CreateData extends IntentService {

    //Channel IDs
    private String CHANNEL_ID = "FSC";

    //notification du service
    private Notification notification;

    private AppDatabase db;

    //CurentIDs
    SharedPreferences sharedPreferencesCurrentIDs;
    SharedPreferences.Editor editorIDs;

    //Map NewIDs
    private Map<String, String> newIDs;

    public static final int UPDATE_PROGRESS = 8344;

    private ResultReceiver receiver;

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

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
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
        receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        try {

            for (Map.Entry<String, String> element : newIDs.entrySet()) {
                //creer utl et connection
                URL url = new URL(element.getValue());
                URLConnection connection = url.openConnection();
                connection.connect();

                // c'est utilisé pour voir la bar de progression
                int fileLength = connection.getContentLength();
                //telecharger le fichier
                InputStream input = new BufferedInputStream(connection.getInputStream());

                String path = getCacheDir().getPath() + "/" + element.getKey() + ".zip";
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // publishing the progress....
                    sendToReceiver((int) (total * 100 / fileLength), "Téléchargement des données");

                    output.write(data, 0, count);
                }

                // fermer les streams
                output.flush();
                output.close();
                input.close();

                unpackZip(getCacheDir().getPath()+"/",element.getKey()+".zip");
            }
            File[] files = getCacheDir().listFiles();



        } catch (IOException e) {
            e.printStackTrace();
        }

        sendToReceiver(100, "Téléchargement fini");

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

            addFileInBdd("routes.txt", "(1/5)", new Function<String[], Void>() {
                @Override
                public Void apply(String[] line) {
                    db.busRouteDao().insert(new BusRoute(Integer.parseInt(line[0]), line[2], line[3], line[7], line[8], line[9]));
                    return null;
                }
            });
            addFileInBdd("trips.txt", "(2/5)", new Function<String[], Void>() {
                @Override
                public Void apply(String[] line) {
                    db.tripDao().insert(new Trip(Integer.parseInt(line[2]), Integer.parseInt(line[0]), line[3], Integer.parseInt(line[5])));
                    return null;
                }
            });

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addFileInBdd(String file, String avancement, Function<String[], Void> f) {
        try {
            CSVReader reader = new CSVReader(new FileReader(getCacheDir()+"/" + file));
            reader.skip(1);
            List<String[]> lines = reader.readAll();
            int i = 0;
            for (String[] line : lines) {
                f.apply(line);
                sendToReceiver((int) 100 * i/lines.size(), "Ajout des données " + avancement);
                i++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendToReceiver(int progress, String status) {
        Bundle resultData = new Bundle();
        resultData.putInt("progress", progress);
        resultData.putString("progressText", status);
        receiver.send(UPDATE_PROGRESS, resultData);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
