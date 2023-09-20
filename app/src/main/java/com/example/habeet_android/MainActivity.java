package com.example.habeet_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取activity_nav.xml中的根布局
        View Nav = findViewById(R.id.Nav);
        // 获取navPoint和navAvatar控件
        ImageView navDelete = Nav.findViewById(R.id.navDelete);
        // 设置它们的visibility为GONE
        navDelete.setVisibility(View.GONE);

        ImageView navMenuImageView = Nav.findViewById(R.id.navMenu);

        navMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理点击事件，打开抽屉
                Log.v("drawerMenu","点击成功");
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START); // 打开左侧抽屉
            }
        });

        NavigationView navigationView = findViewById(R.id.drawerMenu);

        CardView drawerMenuTo1=navigationView.findViewById(R.id.drawerMenuTo1);

        LinearLayout drawerMenuTo2=navigationView.findViewById(R.id.drawerMenuTo2);
        drawerMenuTo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("drawerMenu","点击抽屉的第一个按钮");
            }
        });
        drawerMenuTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(MainActivity.this,TargetActivity.class);
                startActivity(intent);
                Log.v("drawerMenu","点击抽屉的第二个按钮");
            }
        });
    }
}