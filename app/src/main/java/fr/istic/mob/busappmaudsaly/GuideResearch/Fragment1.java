package fr.istic.mob.busappmaudsaly.GuideResearch;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import fr.istic.mob.busappmaudsaly.Adapter.BusRouteAdapter;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.AppDatabase;
import fr.istic.mob.busappmaudsaly.dialog.TimePickerFragment;

/**
 * TODO : fragment 1 : selectionneur de date et heure, snipper ligne de bus, et qd ligne semectionné snipper direction
 * TODO : date
 * TODO : xorrier heure quand 00 => 09
 */

public class Fragment1 extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private TextView time;

    private int hour;
    private int minute;

    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        time = view.findViewById(R.id.time);

        time.setText(hour + "h" + minute);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.busRouteList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "database-name").enableMultiInstanceInvalidation().allowMainThreadQueries().build();

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new BusRouteAdapter(db.busRouteDao().getAll());
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        time.setText(hour + "h" + minute);
    }
}
