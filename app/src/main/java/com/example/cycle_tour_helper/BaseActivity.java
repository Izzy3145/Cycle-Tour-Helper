package com.example.cycle_tour_helper;


import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cycle_tour_helper.ui.MainActivity;
import com.example.cycle_tour_helper.ui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity";
    public ProgressBar mProgressBar;

    @Override
    public void setContentView(int layoutResID) {
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(constraintLayout);
    }

    public void showProgressBar(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }


}
