package fr.istic.mob.busappmaudsaly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/**
 * @author Maud Garcon & Saly Knab
 *
 * Service qui consulte le site : https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/export/
 * Qui regarde si on nouveau fichier CSV est disponible
 *
 * TODO : effectué le téléchargement du nouveau fichier CSV (cherhcher comment avoir csv) dans cette activity (via un service : CM6)
 * TODO : effectué le remplissage de la base (via le service) => il faut deziper la zip (cherhcher comment)
 * TODO : afficher une barre de progression (activity)
 * TODO :ne doit pas bloquer à la rotation de l'appareil => c'est le service lié à l'acitvity qui va faire le téléchargement
 * TODO : utiliser les bonnes tables (via le service) : bus_route, trip, stop, stop_time, calendar
 * TODO : on efface toutes les anciennes données et on remet tout (CM3 : SQLite)
 */

public class TelechargementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telechargement);

        Intent intent = new Intent(TelechargementActivity.this, CreateData.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }
    }
}
