package com.example.cycle_tour_helper.ui.main;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cycle_tour_helper.R;
import com.example.cycle_tour_helper.models.Route;

import java.util.ArrayList;

/**
 * Created by izzystannett on 15/03/2018.
 */

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.RouteViewHolder> {

    private static final String TAG = "RoutesListAdapter";

    private ArrayList<Route> mRoutesList;
    private OnRouteListener mOnRouteListener;
    private Context mContext;

    //first constructor to take in an array of movie items
    public RoutesListAdapter(Context c, ArrayList<Route> routesList, OnRouteListener onRouteListener) {
        mContext = c;
        mRoutesList = routesList;
        mOnRouteListener = onRouteListener;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item, parent,
                false);
        RouteViewHolder viewHolder = new RouteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        Route routeItem = mRoutesList.get(position);
        holder.routeTitle.setText(routeItem.getRouteTitle());
    }

    @Override
    public int getItemCount() {
        if (mRoutesList == null) {
            return 0;
        } else {
            return mRoutesList.size();
        }
    }

    public void setRouteData(ArrayList<Route> routeList) {
        mRoutesList = routeList;
        notifyDataSetChanged();
    }

    //define the ViewHolder that implements the click handler interface
    class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeTitle;

        //bind text views to itemView
        private RouteViewHolder(View itemView) {
            super(itemView);
            routeTitle = itemView.findViewById(R.id.route_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnRouteListener.onRouteClick(mRoutesList.get(getAdapterPosition()));
        }
    }

    public interface OnRouteListener{
        void onRouteClick(Route route);
    }
}

