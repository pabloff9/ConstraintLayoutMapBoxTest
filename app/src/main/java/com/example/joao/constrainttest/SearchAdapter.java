package com.example.joao.constrainttest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


class SearchAdapter extends RecyclerView.Adapter<LocationViewHolder> {

    private ArrayList<Location> dataset;

    public SearchAdapter() {
        dataset = new ArrayList<>();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.row_location, parent, false);

        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        Location location = dataset.get(position);

        holder.tvName.setText(location.getName());
        holder.tvDesc.setText(location.getDesc());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    void addItem(Location location) {
        this.dataset.add(0, location);

        notifyDataSetChanged();
    }

    public void updateList(ArrayList<Location> newList) {
        dataset.clear();
        dataset.addAll(newList);

        notifyDataSetChanged();
    }
}
