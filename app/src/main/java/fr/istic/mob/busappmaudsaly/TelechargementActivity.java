package fr.istic.mob.busappmaudsaly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.services.CreateData;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Maud Garcon & Saly Knab
 *
 * Service qui consulte le site : https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/export/
 * Qui regarde si on nouveau fichier CSV est disponible
 *
 */

public class TelechargementActivity extends AppCompatActivity {

    private TextView progressionText;
    private ProgressBar progressionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telechargement);

        progressionText = findViewById(R.id.progress);
        progressionBar = findViewById(R.id.progressBar);

        Intent intent = new Intent(TelechargementActivity.this, CreateData.class);
        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }
    }

    private class DownloadReceiver extends ResultReceiver {

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            if (resultCode == CreateData.UPDATE_PROGRESS) {

                int progress = resultData.getInt("progress"); //get the progress
                progressionBar.setProgress(progress);
                String progressText = resultData.getString("progressText"); //get the progress
                progressionText.setText(progressText);
            }
        }
    }
}
