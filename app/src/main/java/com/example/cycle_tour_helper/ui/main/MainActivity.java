package com.example.cycle_tour_helper.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cycle_tour_helper.BaseActivity;
import com.example.cycle_tour_helper.R;
import com.example.cycle_tour_helper.ViewModelProviderFactory;
import com.example.cycle_tour_helper.models.Route;
import com.example.cycle_tour_helper.ui.auth.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements ListFragment.OnRouteSelectedListener {

    private static final String TAG = "MainActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    BottomNavigationView bottomNavigationView;
    RouteViewModel routeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomNavView();
        routeViewModel = ViewModelProviders.of(this, providerFactory).get(RouteViewModel.class);
    }

    public void setUpBottomNavView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listFragment:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new ListFragment())
                                .commit();
                        break;
                    case R.id.mapFragment:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new MapFragment())
                                .commit();
                        break;
                    case R.id.profileFragment:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, new ProfileFragment())
                                .commit();
                        break;
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.listFragment);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ListFragment) {
            ListFragment listFragment = (ListFragment) fragment;
            listFragment.setOnRouteSelectedListener(this);
        }
    }

    @Override
    public void onRouteSelected(Route route) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, new MapFragment())
                .commit();
        routeViewModel.setSelectedRoute(route);
    }
}
