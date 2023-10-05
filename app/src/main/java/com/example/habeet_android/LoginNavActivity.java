package com.example.habeet_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import Adapter.CarouselAdapter;
import Item.CarouselItem;

public class LoginNavActivity extends AppCompatActivity {
    private TextView userAgreement;
    private TextView privacyPolicy;

    private ViewPager2 viewPager;
    private List<CarouselItem> carouselItems = new ArrayList<>();
    private int currentItem = 0; // 当前轮播图项的索引
    private Handler handler = new Handler();
    private Runnable runnable;

    private LinearLayout loginNavLoginLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_nav);

        userAgreement=findViewById(R.id.userAgreement);
        privacyPolicy=findViewById(R.id.privacyPolicy);

        userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UserAgreementActivity.class);
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PrivacyPolicyActivity.class);
            }
        });

        viewPager = findViewById(R.id.viewPager);

        // 添加轮播图项数据
        carouselItems.add(new CarouselItem(R.drawable.loginnavlogo1, "兑换\n商店积分"));
        carouselItems.add(new CarouselItem(R.drawable.loginnavlogo2, "发现\n自我进步"));
        carouselItems.add(new CarouselItem(R.drawable.loginnavlogo3, "建立\n计时标签"));
        carouselItems.add(new CarouselItem(R.drawable.loginnavlogo4, "建立\n你的目标"));

        // 创建适配器并设置到ViewPager2
        CarouselAdapter carouselAdapter = new CarouselAdapter(carouselItems);
        viewPager.setAdapter(carouselAdapter);

        // 创建轮播任务
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentItem == carouselItems.size() - 1) {
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                viewPager.setCurrentItem(currentItem);
                handler.postDelayed(this, 1800); // 1秒后再次执行
            }
        };

        // 开始轮播任务
        handler.postDelayed(runnable, 1800);

        loginNavLoginLinearLayout=findViewById(R.id.loginNavLogin);
        loginNavLoginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听按钮，如果点击，就跳转
                startActivity(HomeActivity.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时停止轮播任务，防止内存泄漏
        handler.removeCallbacks(runnable);
    }
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
        this.finish(); // 关闭当前活动
    }
}