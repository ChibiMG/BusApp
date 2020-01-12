package fr.istic.mob.busappmaudsaly.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import fr.istic.mob.busappmaudsaly.R;
import fr.istic.mob.busappmaudsaly.database.BusRoute;
import fr.istic.mob.busappmaudsaly.database.Stop;

public class ArretAdapter extends RecyclerView.Adapter<ArretAdapter.MyViewHolder> {

    private List<Stop> mDataset;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(Stop item);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView arret;

        public MyViewHolder(View view) {
            super(view);
            arret = view.findViewById(R.id.arret);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArretAdapter(List<Stop> myDataset, OnItemClickListener onItemClickListener) {
        mDataset = myDataset;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArretAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arret, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Stop item = mDataset.get(position);
        holder.arret.setText(item.stopName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(item);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
