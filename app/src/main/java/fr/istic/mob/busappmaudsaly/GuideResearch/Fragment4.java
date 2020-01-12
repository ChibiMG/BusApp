package fr.istic.mob.busappmaudsaly.GuideResearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.Adapter.HoraireAdapter;
import fr.istic.mob.busappmaudsaly.Adapter.StopAndTimeAdapter;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;

public class Fragment4 extends Fragment {

    private AppDatabase db;
    private StopAndTimeAdapter mAdapter;
    private String horaire;
    private int tripId;


    public Fragment4() {
    }

    public static Fragment4 newInstance(String param1, String param2) {
        Fragment4 fragment = new Fragment4();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        horaire = getArguments().getString("horaire");
        tripId = getArguments().getInt("tripId");



        View view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stopAndTimeList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "database1").enableMultiInstanceInvalidation().allowMainThreadQueries().build();
        // specify an adapter (see also next example)
        mAdapter = new StopAndTimeAdapter(db.stopTimeDao().getStopAndTimeFromTime(tripId, horaire));
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
}
