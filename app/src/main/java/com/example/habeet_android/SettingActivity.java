package com.example.habeet_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Util.DrawerMenuHelper;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(5);
    }
}