package com.example.habeet_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Util.DrawerMenuHelper;

public class TargetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(1);
    }
}