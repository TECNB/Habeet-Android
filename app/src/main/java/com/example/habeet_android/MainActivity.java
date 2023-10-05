package com.example.habeet_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import Util.DrawerMenuHelper;

public class MainActivity extends AppCompatActivity {
    private ImageView arrowDownImage;
    private ImageView arrowUpImage;

    private CardView targetMenuDetail;

    private CardView tagMenuDetail;

    private CardView tagMenuNav;

    private CardView timeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(0);
        drawerMenuHelper.getUserData();

        arrowDownImage=findViewById(R.id.arrowDownImage);
        arrowUpImage=findViewById(R.id.arrowUpImage);
        targetMenuDetail=findViewById(R.id.targetMenuDetail);
        tagMenuDetail=findViewById(R.id.tagMenuDetail);
        tagMenuNav=findViewById(R.id.tagMenuNav);

        timeButton=findViewById(R.id.timeButton);

        arrowDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowUpImage.setVisibility(View.VISIBLE);
                arrowDownImage.setVisibility(View.GONE);

                targetMenuDetail.setVisibility(View.VISIBLE);
            }
        });

        arrowUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowUpImage.setVisibility(View.GONE);
                arrowDownImage.setVisibility(View.VISIBLE);

                targetMenuDetail.setVisibility(View.GONE);
            }
        });
        tagMenuNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tagMenuDetail.getVisibility()==View.GONE){
                    tagMenuDetail.setVisibility(View.VISIBLE);
                }else{
                    tagMenuDetail.setVisibility(View.GONE);
                }

            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}