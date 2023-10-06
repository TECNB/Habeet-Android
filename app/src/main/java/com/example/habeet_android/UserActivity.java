package com.example.habeet_android;

import static com.example.habeet_android.HomeActivity.userEmail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Util.DrawerMenuHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {
    private ImageView userArrowDown;
    private ImageView userArrowUp;
    private TextView userPointTextView;
    private TextView userLogo1TextView;
    private TextView userTime1;
    private TextView userTime2;
    private TextView userTime3;
    private TextView userProgressTextView;
    private TextView userLogo2TextView;
    private TextView pointInsistenceTextView;
    private TextView completeTargetRateTextView;
    private TextView pointAverageTextView;
    private TextView completeTargetTextView;
    private TextView ifProgressTextView;
    private CardView userNav2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(4);
        drawerMenuHelper.getUserData();

        userArrowDown = findViewById(R.id.userArrowDown);
        userArrowUp = findViewById(R.id.userArrowUp);
        userPointTextView = findViewById(R.id.userPointTextView);
        userLogo1TextView = findViewById(R.id.userLogo1TextView);
        userTime1 = findViewById(R.id.userTime1);
        userTime2 = findViewById(R.id.userTime2);
        userTime3 = findViewById(R.id.userTime3);
        userProgressTextView = findViewById(R.id.userProgressTextView);
        userLogo2TextView = findViewById(R.id.userLogo2TextView);
        pointInsistenceTextView = findViewById(R.id.pointInsistenceTextView);
        completeTargetRateTextView = findViewById(R.id.completeTargetRateTextView);
        pointAverageTextView = findViewById(R.id.pointAverageTextView);
        completeTargetTextView = findViewById(R.id.completeTargetTextView);
        ifProgressTextView = findViewById(R.id.ifProgressTextView);
        userNav2 = findViewById(R.id.userNav2);



        userArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArrowUp.setVisibility(View.VISIBLE);
                userArrowDown.setVisibility(View.GONE);

                userNav2.setVisibility(View.VISIBLE);
            }
        });

        userArrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArrowUp.setVisibility(View.GONE);
                userArrowDown.setVisibility(View.VISIBLE);

                userNav2.setVisibility(View.GONE);
            }
        });

        userTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArrowUp.setVisibility(View.GONE);
                userArrowDown.setVisibility(View.VISIBLE);

                String userTime= (String) userTime1.getText();
                userTime1.setText(userTime2.getText());
                userNav2.setVisibility(View.GONE);
                userTime2.setText(userTime);
                fetchPointRecordData();
            }
        });

        userTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArrowUp.setVisibility(View.GONE);
                userArrowDown.setVisibility(View.VISIBLE);

                String userTime= (String) userTime1.getText();
                userTime1.setText(userTime3.getText());
                userNav2.setVisibility(View.GONE);
                userTime3.setText(userTime);
                fetchPointRecordData();
            }
        });
        fetchPointRecordData();
    }
    private void fetchPointRecordData() {

        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/pointRecord/get";

        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("userTimeP", userTime1.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 设置请求体
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, requestData.toString());
        // 创建POST请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 使用OkHttp3执行异步网络请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 获取响应数据
                    String responseBody = response.body().string();
                    try {
                        // 解析JSON响应
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject data = jsonResponse.getJSONObject("data");

                        String pointAll = data.getString("pointAll");
                        String progress = data.getString("progress");
                        String pointInsistence = data.getString("pointInsistence");
                        String pointAverage = data.getString("pointAverage");
                        String completeTarget = data.getString("completeTarget");
                        String completeTargetRate = data.getString("completeTargetRate");

                        String ifProgress = data.getString("ifProgress");


                        // 获取用户选择的时间范围
                        String userTime = userTime1.getText().toString();

                        // 根据用户选择的时间范围设置userLogo1TextView和userLogo2TextView的文本
                        String logo1Text = "";
                        String logo2Text = "";

                        if ("过去一周".equals(userTime)) {
                            logo1Text = "努力的一周";
                            logo2Text = "比起上一周";
                        } else if ("过去一月".equals(userTime)) {
                            logo1Text = "努力的一月";
                            logo2Text = "比起上一月";
                        } else if ("过去一天".equals(userTime)) {
                            logo1Text = "努力的一天";
                            logo2Text = "比起上一天";
                        }


                        final String finalLogo1Text = logo1Text;
                        final String finalLogo2Text = logo2Text;
                        // 更新 UI，确保在主线程中执行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userLogo1TextView.setText(finalLogo1Text);
                                userLogo2TextView.setText(finalLogo2Text);

                                if ("0".equals(ifProgress)){
                                    ifProgressTextView.setText("退步");
                                }else{
                                    ifProgressTextView.setText("进步");
                                }
                                userPointTextView.setText(pointAll);
                                userProgressTextView.setText(progress);
                                pointInsistenceTextView.setText(pointInsistence+"天\n连续得分");
                                pointAverageTextView.setText(pointAverage);
                                completeTargetTextView.setText(completeTarget+"个目标");
                                completeTargetRateTextView.setText(completeTargetRate+"%");
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // 请求失败，输出错误信息
                    Log.e("UserActivity", "请求失败，状态码: " + response.code());
                    Log.e("UserActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}