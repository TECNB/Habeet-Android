package Util;

import static com.example.habeet_android.LoginActivity.userName;
import static com.example.habeet_android.LoginActivity.userPoint;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.habeet_android.HelperActivity;
import com.example.habeet_android.MainActivity;
import com.example.habeet_android.R;
import com.example.habeet_android.SettingActivity;
import com.example.habeet_android.StoreActivity;
import com.example.habeet_android.TagActivity;
import com.example.habeet_android.TargetActivity;
import com.example.habeet_android.UserActivity;
import com.google.android.material.navigation.NavigationView;

import Item.UserItem;

public class DrawerMenuHelper {
    private AppCompatActivity activity;

    private UserItem userItem;

    public DrawerMenuHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void getUserData(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 修改UI
                // 获取activity_nav.xml中的根布局
                View Nav = activity.findViewById(R.id.Nav);
                // 获取navPoint和navAvatar控件
                TextView navNameTextView = Nav.findViewById(R.id.navNameTextView);
                TextView navPointTextView = Nav.findViewById(R.id.navPointTextView);

                navNameTextView.setText(userName);
                navPointTextView.setText(userPoint);
            }
        });
    }
    public void setupDrawerMenu(int num) {
        // 获取activity_nav.xml中的根布局
        View Nav = activity.findViewById(R.id.Nav);
        // 获取navPoint和navAvatar控件
        CardView navPoint = Nav.findViewById(R.id.navPoint);
        CardView navAvatar = Nav.findViewById(R.id.navAvatar);
        // 获取navPoint和navAvatar控件
        ImageView navDelete = Nav.findViewById(R.id.navDelete);
        if (num==0||num==3||num==4||num==5||num==6){
            // 设置它们的visibility为GONE
            navDelete.setVisibility(View.GONE);
        }else{
            // 设置它们的visibility为GONE
            navPoint.setVisibility(View.GONE);
            navAvatar.setVisibility(View.GONE);
        }

        NavigationView navigationView = activity.findViewById(R.id.drawerMenu);

        int[] drawerMenuToAIds = {
                R.id.drawerMenuTo1A, R.id.drawerMenuTo2A, R.id.drawerMenuTo3A,
                R.id.drawerMenuTo4A, R.id.drawerMenuTo5A, R.id.drawerMenuTo6A, R.id.drawerMenuTo7A
        };

        int[] drawerMenuToBIds = {
                R.id.drawerMenuTo1B, R.id.drawerMenuTo2B, R.id.drawerMenuTo3B,
                R.id.drawerMenuTo4B, R.id.drawerMenuTo5B, R.id.drawerMenuTo6B, R.id.drawerMenuTo7B
        };

        CardView drawerMenuToA = navigationView.findViewById(drawerMenuToAIds[num]);
        LinearLayout drawerMenuToB = navigationView.findViewById(drawerMenuToBIds[num]);

        drawerMenuToA.setVisibility(View.VISIBLE);
        drawerMenuToB.setVisibility(View.GONE);

        CardView drawerMenuTo1A=navigationView.findViewById(drawerMenuToAIds[0]);
        CardView drawerMenuTo2A=navigationView.findViewById(drawerMenuToAIds[1]);
        CardView drawerMenuTo3A=navigationView.findViewById(drawerMenuToAIds[2]);
        CardView drawerMenuTo4A=navigationView.findViewById(drawerMenuToAIds[3]);
        CardView drawerMenuTo5A=navigationView.findViewById(drawerMenuToAIds[4]);
        CardView drawerMenuTo6A=navigationView.findViewById(drawerMenuToAIds[5]);
        CardView drawerMenuTo7A=navigationView.findViewById(drawerMenuToAIds[6]);

        LinearLayout drawerMenuTo1=navigationView.findViewById(drawerMenuToBIds[0]);
        LinearLayout drawerMenuTo2=navigationView.findViewById(drawerMenuToBIds[1]);
        LinearLayout drawerMenuTo3=navigationView.findViewById(drawerMenuToBIds[2]);
        LinearLayout drawerMenuTo4=navigationView.findViewById(drawerMenuToBIds[3]);
        LinearLayout drawerMenuTo5=navigationView.findViewById(drawerMenuToBIds[4]);
        LinearLayout drawerMenuTo6=navigationView.findViewById(drawerMenuToBIds[5]);
        LinearLayout drawerMenuTo7=navigationView.findViewById(drawerMenuToBIds[6]);

        drawerMenuTo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听按钮，如果点击，就跳转
                startActivity(MainActivity.class);
                Log.v("drawerMenu","点击抽屉的第一个按钮");
            }
        });
        drawerMenuTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TargetActivity.class);
                Log.v("drawerMenu","点击抽屉的第二个按钮");
            }
        });
        drawerMenuTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TagActivity.class);
                Log.v("drawerMenu","点击抽屉的第三个按钮");
            }
        });
        drawerMenuTo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StoreActivity.class);
                Log.v("drawerMenu","点击抽屉的第四个按钮");
            }
        });
        drawerMenuTo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UserActivity.class);
                Log.v("drawerMenu","点击抽屉的第五个按钮");
            }
        });
        drawerMenuTo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.class);
                Log.v("drawerMenu","点击抽屉的第六个按钮");
            }
        });
        drawerMenuTo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HelperActivity.class);
                Log.v("drawerMenu","点击抽屉的第七个按钮");
            }
        });

        ImageView navMenuImageView = Nav.findViewById(R.id.navMenu);
        navMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
    }


    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish(); // 关闭当前活动
    }

    private void openDrawer() {
        DrawerLayout drawerLayout = activity.findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START); // 打开左侧抽屉
        Log.v("drawerMenu","点击按钮");
    }
}
