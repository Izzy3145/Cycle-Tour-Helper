package com.example.cycle_tour_helper.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cycle_tour_helper.R;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> routeFiles = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RouteViewHolder)holder).bind(routeFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return routeFiles.size();
    }

    public void setRoutes(List<String> routeFiles){
        this.routeFiles = routeFiles;
        notifyDataSetChanged();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.route_title);
        }

        public void bind(String routeTitle){
            title.setText(routeTitle);
        }
    }
}
