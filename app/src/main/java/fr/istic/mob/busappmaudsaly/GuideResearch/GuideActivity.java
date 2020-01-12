package fr.istic.mob.busappmaudsaly.GuideResearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fr.istic.mob.busappmaudsaly.R;

import android.os.Bundle;


/**
 * TODO : fragement affichés contenant 1, 2 ou 3 en fonction de la conf de l'écran (1 ou 2 tel, 3 tablette)
 * TODO : fragement 3 : apres clique sur arret, horraire de bus choisi, direction choisi, arret choisi, heure choisi (jusqua fin de journée)
 * TODO : fragement 4 : clique sur horaire : heures de passage du bus choisi entre l'arret choisi et le terminus
 * TODO : fragement affiche profressionement : apparaissant de droite a gauche, animation de 1 sec
 * TODO : clique sur le bouton retour : enleve le dernier fragement affiché, reviens à la conf précédente (retour progression de gauche a droite), animation de 1 sec
 * TODO : rotation ecran : fragement dispo affiché du plus a gauche vers les autres
 */

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment1 fragment1 = new Fragment1();
        fragmentTransaction.add(R.id.fragment_frame, fragment1);
        fragmentTransaction.commit();

    }
}
