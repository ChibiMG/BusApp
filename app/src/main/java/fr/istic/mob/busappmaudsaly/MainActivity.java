package fr.istic.mob.busappmaudsaly;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import fr.istic.mob.busappmaudsaly.task.SiteConsultationTask;

public class MainActivity extends AppCompatActivity {

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

    }
}
