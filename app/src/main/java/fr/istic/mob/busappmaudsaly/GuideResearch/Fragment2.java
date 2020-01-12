package fr.istic.mob.busappmaudsaly.GuideResearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.Adapter.ArretAdapter;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.database.Stop;

public class Fragment2 extends Fragment implements ArretAdapter.OnItemClickListener {

    private int idRoute;
    private int date;
    private String time;
    private int dayOfWeek;
    private TextView depart;
    private TextView arrivee;
    private LinearLayout direction;
    private int currentDirection;
    private List<Stop> donnees;
    private AppDatabase db;
    private ArretAdapter mAdapter;
    private int idStop;

    public Fragment2() {
    }

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        donnees = new ArrayList<>();
        currentDirection = 0;
        idRoute = getArguments().getInt("routeId");
        date = getArguments().getInt("date");
        time = getArguments().getString("time");
        dayOfWeek = getArguments().getInt("dayOfWeek");

        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        depart = view.findViewById(R.id.depart);
        arrivee = view.findViewById(R.id.arrivee);
        direction = view.findViewById(R.id.direction);

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDirection();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stopList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "database1").enableMultiInstanceInvalidation().allowMainThreadQueries().build();

        changeDirection();

        // specify an adapter (see also next example)
        mAdapter = new ArretAdapter(donnees, this);
        recyclerView.setAdapter(mAdapter);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(Stop item) {
        idStop = item.stopId;

        Bundle bundle = getArguments();
        bundle.putInt("stopId", idStop);
        bundle.putInt("direction", currentDirection);

        Fragment3 fragment3 = new Fragment3();
        fragment3.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment3).addToBackStack(null).commit();
    }

    private void changeDirection(){
        currentDirection = 1 - currentDirection;
        List<Stop> donnees2 = db.stopDao().getBusArret(date, idRoute, currentDirection, time, dayOfWeek == 1? 1 : 2, dayOfWeek == 2? 1 : 2, dayOfWeek == 3? 1 : 2, dayOfWeek == 4? 1 : 2, dayOfWeek == 5? 1 : 2, dayOfWeek == 6? 1 : 2, dayOfWeek == 7? 1 : 2);
        donnees.clear();
        donnees.addAll(donnees2);
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
        depart.setText(donnees.get(0).stopName);
        arrivee.setText(donnees.get(donnees.size()-1).stopName);
    }
}
