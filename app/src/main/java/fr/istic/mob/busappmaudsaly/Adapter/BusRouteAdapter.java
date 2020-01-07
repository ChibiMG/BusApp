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

public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.MyViewHolder> {
    private List<BusRoute> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView routeNameView;
        public TextView routeNumberView;

        public MyViewHolder(View view) {
            super(view);
            routeNameView = view.findViewById(R.id.route_name);
            routeNumberView = view.findViewById(R.id.route_number);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BusRouteAdapter(List<BusRoute> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BusRouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_route, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        BusRoute item = mDataset.get(position);
        holder.routeNameView.setText(item.routeLongName);
        holder.routeNumberView.setText(item.routeShortName);
        holder.routeNumberView.setBackgroundColor(Color.parseColor("#" + item.routeColor));
        holder.routeNumberView.setTextColor(Color.parseColor("#" + item.routeTextColor));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
