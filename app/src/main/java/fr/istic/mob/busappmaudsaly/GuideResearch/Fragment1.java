package fr.istic.mob.busappmaudsaly.GuideResearch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
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
import fr.istic.mob.busappmaudsaly.database.BusRoute;
import fr.istic.mob.busappmaudsaly.dialog.DatePickerFragment;
import fr.istic.mob.busappmaudsaly.dialog.TimePickerFragment;

/**
 * TODO : bundle l'heure et date regarder en bdd comment elle est mise
 */

public class Fragment1 extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, BusRouteAdapter.OnItemClickListener {

    private TextView time;
    private TextView date;

    private int hour;
    private int minute;

    private int year;
    private int month;
    private int day;

    private int idRoute;

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

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+ 1;
        day = c.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        time = view.findViewById(R.id.time);

        date = view.findViewById(R.id.date);

        setTime();
        setDate();

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
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
        RecyclerView.Adapter mAdapter = new BusRouteAdapter(db.busRouteDao().getAll(), this);
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

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;

        setDate();
    }

    public void setDate(){
        if(this.day < 10 && this.month < 10){
            date.setText("0"+day+"/0"+month+"/"+year);
        }
        else if (this.day < 10){
            date.setText("0"+day+"/"+month+"/"+year);
        }
        else if (this.month < 10){
            date.setText(day+"/0"+month+"/"+year);
        }
        else {
            date.setText(day+"/"+month+"/"+year);
        }
    }

    public void setTime(){
        if (this.minute < 10){
            time.setText(hour + "h0" + minute);
        }
        else{
            time.setText(hour + "h" + minute);
        }
    }

    @Override
    public void onItemClick(BusRoute item) {
        idRoute = item.routeId;

        Bundle bundle = new Bundle();
        bundle.putInt("routeId", idRoute);

        Fragment2 fragment2 = new Fragment2();
        fragment2.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment2).addToBackStack(null).commit();
    }
}
