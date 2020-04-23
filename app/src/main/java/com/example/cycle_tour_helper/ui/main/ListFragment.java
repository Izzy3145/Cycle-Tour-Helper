package com.example.cycle_tour_helper.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cycle_tour_helper.R;
import com.example.cycle_tour_helper.ViewModelProviderFactory;
import com.example.cycle_tour_helper.models.Route;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ListFragment extends DaggerFragment implements RoutesListAdapter.OnRouteListener{

    @Inject
    ViewModelProviderFactory providerFactory;
    RouteViewModel routeViewModel;

    RoutesListAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Route> routes = new ArrayList<>();
    OnRouteSelectedListener mainActivityCallback;

    public ListFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new RoutesListAdapter(getActivity(), routes, this);
        recyclerView = view.findViewById(R.id.routes_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        routes.add(new Route("Oxford to Cambridge", "oxfordcambridge"));
        routes.add(new Route("Coast and Castles", "coastandcastles"));
        routes.add(new Route("Penine Way", "penineway"));
        adapter.setRouteData(routes);

        routeViewModel = ViewModelProviders.of(getActivity(), providerFactory).get(RouteViewModel.class);
    }

    public void setOnRouteSelectedListener(OnRouteSelectedListener callback) {
        this.mainActivityCallback = callback;
    }

    public interface OnRouteSelectedListener {
        void onRouteSelected(Route route);
    }

    @Override
    public void onRouteClick(Route route) {
        mainActivityCallback.onRouteSelected(route);
    }
}
