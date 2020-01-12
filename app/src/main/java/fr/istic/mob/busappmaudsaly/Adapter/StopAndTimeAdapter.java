package fr.istic.mob.busappmaudsaly.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.StopAndTime;
import fr.istic.mob.busappmaudsaly.database.StopTime;

public class StopAndTimeAdapter extends RecyclerView.Adapter<StopAndTimeAdapter.MyViewHolder> {

    private List<StopAndTime> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView arret;
        public TextView horaire;

        public MyViewHolder(View view) {
            super(view);
            arret = view.findViewById(R.id.arretTextView);
            horaire = view.findViewById(R.id.horaireTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StopAndTimeAdapter(List<StopAndTime> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StopAndTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stop_and_time, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final StopAndTime item = mDataset.get(position);
        holder.arret.setText(item.stop.stopName);
        holder.horaire.setText(item.time.arrivalTime);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
