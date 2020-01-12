package fr.istic.mob.busappmaudsaly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.Adapter.BusRouteAdapter;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.database.BusRoute;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class searchArretActivity extends AppCompatActivity implements BusRouteAdapter.OnItemClickListener {

    private String arret;
    private TextView arretText;
    private AppDatabase db;
    private BusRouteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_arret);

        arret = getIntent().getExtras().getString("arret");

        arretText = findViewById(R.id.currentArret);
        arretText.setText(arret);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.busRouteList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(this, AppDatabase.class, "database1").enableMultiInstanceInvalidation().allowMainThreadQueries().build();
        // specify an adapter (see also next example)
        mAdapter = new BusRouteAdapter(db.busRouteDao().getRouteInOneStop(arret), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(BusRoute item) {
        
    }
}
