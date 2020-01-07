package fr.istic.mob.busappmaudsaly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import fr.istic.mob.busappmaudsaly.GuideResearch.GuideActivity;
import fr.istic.mob.busappmaudsaly.task.SiteConsultationTask;

public class MainActivity extends AppCompatActivity {

    private Button researchGuide;

    private Button researchArret;

    private EditText researchArretText;

    private Intent intent;

    /**
     * TODO : consulter les horaires des bus
     * TODO : fragement affichés contenant 1, 2 ou 3 en fonction de la conf de l'écran (1 ou 2 tel, 3 tablette)
     * TODO : fragment 1 : selectionneur de date et heure, snipper ligne de bus, et qd ligne semectionné snipper direction
     * TODO : fragement 2 : apres date, heure direction slectionnées, arret affichés, de l'arrivée au départ
     * TODO : fragement 3 : apres clique sur arret, horraire de bus choisi, direction choisi, arret choisi, heure choisi (jusqua fin de journée)
     * TODO : fragement 4 : clique sur horaire : heures de passage du bus choisi entre l'arret choisi et le terminus
     * TODO : fragement affiche profressionement : apparaissant de droite a gauche, animation de 1 sec
     * TODO : clique sur le bouton retour : enleve le dernier fragement affiché, reviens à la conf précédente (retour progression de gauche a droite), animation de 1 sec
     *TODO : rotation ecran : fragement dispo affiché du plus a gauche vers les autres
     * recherche textuel noms arret bus, avec ligne de bus qui y passent (les affichés une fois que ce soit pour aller retour)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SiteConsultationTask.class, 10, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

        researchGuide = findViewById(R.id.searchGuideButton);
        researchGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "recherche guidee", Toast.LENGTH_LONG).show();
                intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });

        researchArretText = findViewById(R.id.searchText);

        researchArret = findViewById(R.id.searchButton);
        researchArret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "recherche arret", Toast.LENGTH_LONG).show();
                //go ??? + researchArretText.getText()
            }
        });
    }
}
