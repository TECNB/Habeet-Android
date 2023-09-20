package com.example.habeet_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // 获取activity_nav.xml中的根布局
        View nav = findViewById(R.id.Nav);
        // 获取navPoint和navAvatar控件
        ImageView navDelete = nav.findViewById(R.id.navDelete);
        // 设置它们的visibility为GONE
        navDelete.setVisibility(View.GONE);
    }
}