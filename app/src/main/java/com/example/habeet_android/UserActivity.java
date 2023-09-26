package com.example.habeet_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Util.DrawerMenuHelper;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(4);
        drawerMenuHelper.getUserData();
    }
}