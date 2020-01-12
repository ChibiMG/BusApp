package fr.istic.mob.busappmaudsaly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import androidx.room.Room;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import fr.istic.mob.busappmaudsaly.Adapter.ArretAdapter;
import fr.istic.mob.busappmaudsaly.GuideResearch.GuideActivity;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.database.Stop;
import fr.istic.mob.busappmaudsaly.task.SiteConsultationTask;

public class MainActivity extends AppCompatActivity implements ArretAdapter.OnItemClickListener, View.OnClickListener {

    private Button researchGuide;

    private Button researchArret;

    private AutoCompleteTextView researchArretText;

    private Intent intent;

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

        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "database1").enableMultiInstanceInvalidation().allowMainThreadQueries().build();
        ArrayAdapter arretAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, db.stopDao().getAllStopName());
        researchArretText = findViewById(R.id.searchText);
        researchArretText.setAdapter(arretAdapter);
        researchArretText.setThreshold(1);

        researchArret = findViewById(R.id.searchButton);
        researchArret.setOnClickListener(this);
    }

    @Override
    public void onItemClick(Stop item) {

    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        String arret = researchArretText.getText().toString();
        bundle.putString("arret", arret);

        if (!arret.equals("")){
            intent = new Intent(MainActivity.this, searchArretActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, getString(R.string.notArret), Toast.LENGTH_LONG).show();
        }
    }
}
