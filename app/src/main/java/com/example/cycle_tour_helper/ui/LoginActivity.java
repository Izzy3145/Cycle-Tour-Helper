package com.example.cycle_tour_helper.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.cycle_tour_helper.R;

import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
