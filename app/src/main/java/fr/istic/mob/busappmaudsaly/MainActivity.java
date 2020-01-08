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
     * TODO : recherche textuel noms arret bus, avec ligne de bus qui y passent (les affichés une fois que ce soit pour aller retour)
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
                intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });

        researchArretText = findViewById(R.id.searchText);

        researchArret = findViewById(R.id.searchButton);
        researchArret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "recherche arret : vide actuellement", Toast.LENGTH_LONG).show();
                //go ??? + researchArretText.getText()
            }
        });
    }
}
