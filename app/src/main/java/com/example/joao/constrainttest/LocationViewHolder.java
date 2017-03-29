package com.example.joao.constrainttest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


class LocationViewHolder extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvDesc;

    LocationViewHolder(View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.location_name);
        tvDesc = (TextView) itemView.findViewById(R.id.location_desc);
    }


}
