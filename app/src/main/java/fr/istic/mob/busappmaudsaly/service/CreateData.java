package fr.istic.mob.busappmaudsaly.service;

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
import androidx.sqlite.db.SupportSQLiteStatement;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;

public class CreateData extends IntentService {

    //Channel IDs
    private String CHANNEL_ID = "FSC";

    //notification du service
    private Notification notification;

    private AppDatabase db;

    //CurentIDs
    SharedPreferences sharedPreferencesCurrentIDs;
    SharedPreferences.Editor editorCurrentIDs;

    //Map NewIDs
    private SharedPreferences newIDs;

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
        editorCurrentIDs = sharedPreferencesCurrentIDs.edit();

        //Recuperation des RecordIDs
        newIDs = getSharedPreferences(getString(R.string.New_Ids),0);

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

        boolean first = true;

        try {
            for (Map.Entry<String, String> element : ((Map<String, String>) newIDs.getAll()).entrySet()) {
                if (first) {
                    first = false;
                    db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database1").enableMultiInstanceInvalidation().build();
                }
                else {
                    db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database2").enableMultiInstanceInvalidation().build();
                }
                db.clearAllTables();
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        sendToReceiver(100, "Téléchargement fini");

        editorCurrentIDs.clear();
        for (Map.Entry<String, String> entry : ((Map<String, String>) newIDs.getAll()).entrySet()){
            editorCurrentIDs.putString(entry.getKey(), entry.getValue());
            editorCurrentIDs.commit();
        }
        newIDs.edit().clear();

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

            db.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    String sql = "INSERT or IGNORE INTO bus_route (route_id, route_short_name, route_long_name, route_color, route_text_color, route_sort_order) VALUES (?, ?, ?, ?, ?, ?)";
                    final SupportSQLiteStatement statement = db.compileStatement(sql);

                    parseCsvFile("routes.txt", "(1/5)", new Function<String[], Void>() {
                        @Override
                        public Void apply(String[] line) {
                            statement.clearBindings();
                            statement.bindLong(1, Long.parseLong(line[0]));
                            statement.bindString(2, line[2]);
                            statement.bindString(3, line[3]);
                            statement.bindString(4, line[7]);
                            statement.bindString(5, line[8]);
                            statement.bindString(6, line[9]);
                            statement.executeInsert();

                            return null;
                        }
                    });

                    try {
                        statement.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            db.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    String sql = "INSERT or IGNORE INTO calendar (service_id, monday, tuesday, wednesday, thursday, friday, saturday, sunday, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    final SupportSQLiteStatement statement = db.compileStatement(sql);

                    parseCsvFile("calendar.txt", "(2/5)", new Function<String[], Void>() {
                        @Override
                        public Void apply(String[] line) {
                            statement.clearBindings();
                            statement.bindLong(1, Long.parseLong(line[0]));
                            statement.bindLong(2, Long.parseLong(line[1]));
                            statement.bindLong(3, Long.parseLong(line[2]));
                            statement.bindLong(4, Long.parseLong(line[3]));
                            statement.bindLong(5, Long.parseLong(line[4]));
                            statement.bindLong(6, Long.parseLong(line[5]));
                            statement.bindLong(7, Long.parseLong(line[6]));
                            statement.bindLong(8, Long.parseLong(line[7]));
                            statement.bindLong(9, Long.parseLong(line[8]));
                            statement.bindLong(10, Long.parseLong(line[9]));
                            statement.executeInsert();

                            return null;
                        }
                    });

                    try {
                        statement.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            db.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    String sql = "INSERT or IGNORE INTO trip (trip_id, route_id, service_id, trip_headsign, direction_id) VALUES (?, ?, ?, ?, ?)";
                    final SupportSQLiteStatement statement = db.compileStatement(sql);

                    parseCsvFile("trips.txt", "(3/5)", new Function<String[], Void>() {
                        @Override
                        public Void apply(String[] line) {
                            statement.clearBindings();
                            statement.bindLong(1, Long.parseLong(line[2]));
                            statement.bindLong(2, Long.parseLong(line[0]));
                            statement.bindLong(3, Long.parseLong(line[1]));
                            statement.bindString(4, line[3]);
                            statement.bindLong(5, Long.parseLong(line[5]));
                            statement.executeInsert();

                            return null;
                        }
                    });

                    try {
                        statement.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            db.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    String sql = "INSERT or IGNORE INTO stop (stop_id, stop_name) VALUES (?, ?)";
                    final SupportSQLiteStatement statement = db.compileStatement(sql);

                    parseCsvFile("stops.txt", "(4/5)", new Function<String[], Void>() {
                        @Override
                        public Void apply(String[] line) {
                            statement.clearBindings();
                            statement.bindLong(1, Long.parseLong(line[0]));
                            statement.bindString(2, line[2]);
                            statement.executeInsert();

                            return null;
                        }
                    });

                    try {
                        statement.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            db.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    String sql = "INSERT or IGNORE INTO stop_time (trip_id, arrival_time, departure_time, stop_id) VALUES (?, ?, ?, ?)";
                    final SupportSQLiteStatement statement = db.compileStatement(sql);

                    parseCsvFile("stop_times.txt", "(5/5)", new Function<String[], Void>() {
                        @Override
                        public Void apply(String[] line) {
                            statement.clearBindings();
                            statement.bindLong(1, Long.parseLong(line[0]));
                            statement.bindString(2, line[1]);
                            statement.bindString(3, line[2]);
                            statement.bindLong(4, Long.parseLong(line[3]));
                            statement.executeInsert();

                            return null;
                        }
                    });

                    try {
                        statement.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void parseCsvFile(String file, String avancement, Function<String[], Void> f) {
        try {
            sendToReceiver(0, "Ouverture du fichier " + avancement);

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
