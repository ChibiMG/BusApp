package fr.istic.mob.busappmaudsaly.GuideResearch;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.Adapter.HoraireAdapter;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.database.Stop;
import fr.istic.mob.busappmaudsaly.database.StopTime;

/**
 * TODO : fragement 3 : apres clique sur arret, horraire de bus choisi, direction choisi, arret choisi, heure choisi (jusqua fin de journée)
*/
public class Fragment3 extends Fragment implements HoraireAdapter.OnItemClickListener {

    private int idRoute;
    private int date;
    private String time;
    private int dayOfWeek;
    private int idStop;
    private int direction;
    private AppDatabase db;
    private HoraireAdapter mAdapter;

    public Fragment3() {
    }

    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        idRoute = getArguments().getInt("routeId");
        date = getArguments().getInt("date");
        time = getArguments().getString("time");
        dayOfWeek = getArguments().getInt("dayOfWeek");
        idStop = getArguments().getInt("stopId");
        direction = getArguments().getInt("direction");

        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.horairesList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, "database1").enableMultiInstanceInvalidation().allowMainThreadQueries().build();

        // specify an adapter (see also next example)
        mAdapter = new HoraireAdapter(db.stopTimeDao().getHoraireForOneStop(date, idRoute, direction, time, dayOfWeek == 1? 1 : 2, dayOfWeek == 2? 1 : 2, dayOfWeek == 3? 1 : 2, dayOfWeek == 4? 1 : 2, dayOfWeek == 5? 1 : 2, dayOfWeek == 6? 1 : 2, dayOfWeek == 7? 1 : 2, idStop), this);
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
    public void onItemClick(StopTime item) {

        Bundle bundle = getArguments();
        bundle.putString("horaire", item.arrivalTime);
        bundle.putInt("tripId", item.tripId);

        Fragment4 fragment4 = new Fragment4();
        fragment4.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment4).addToBackStack(null).commit();
    }
}
