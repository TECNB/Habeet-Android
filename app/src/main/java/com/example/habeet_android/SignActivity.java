package com.example.habeet_android;

import static com.example.habeet_android.HomeActivity.userEmail;
import static com.example.habeet_android.LoginActivity.userName;
import static com.example.habeet_android.LoginActivity.userPoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignActivity extends AppCompatActivity {
    private EditText signUserPassEditText;
    private EditText signUserNameEditText;
    private EditText signCodeEditText;
    private ImageView signBack;
    private CardView signCardView;
    private CardView codeCardView;
    private String userPass;
    private String userCode;


    private TextView codeText;
    private CountDownTimer countDownTimer;
    private boolean isCounting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signUserNameEditText=findViewById(R.id.signUserName);
        signUserPassEditText=findViewById(R.id.signUserPass);
        signCodeEditText=findViewById(R.id.signCode);


        signCardView=findViewById(R.id.signCardView);
        codeCardView=findViewById(R.id.codeCardView);

        signBack=findViewById(R.id.signBack);

        codeText = findViewById(R.id.codeText);

        signCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=signUserNameEditText.getText().toString();
                userPass=signUserPassEditText.getText().toString();
                userCode=signCodeEditText.getText().toString();
                sign();
            }
        });

        signBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HomeActivity.class);
            }
        });
        codeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCounting) {
                    code();
                    startCountDown();
                } else {
                    // 如果正在倒计时中，不执行code()方法
                }
            }
        });
    }
    private void startCountDown() {
        codeCardView.setEnabled(false); // 禁用点击
        isCounting = true; // 设置正在倒计时

        countDownTimer = new CountDownTimer(60000, 1000) { // 60秒，每秒更新一次
            public void onTick(long millisUntilFinished) {
                codeText.setText(String.valueOf(millisUntilFinished / 1000) + "秒");
            }

            public void onFinish() {
                codeText.setText("发送验证码");
                codeCardView.setEnabled(true); // 启用点击
                isCounting = false; // 倒计时结束
            }
        }.start();
    }

    private void code(){
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/user/code";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("ifUpdate", 0);
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
                        String code = jsonResponse.getString("code");
                        if("00000".equals(code)){

                        }else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("signActivity", "请求失败，状态码: " + response.code());
                    Log.e("signActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void sign(){
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/user/sign";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("userPassword", userPass);
            requestData.put("picUrl", "https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0");
            requestData.put("userName", userName);
            requestData.put("userCode", userCode);
            requestData.put("ifUpdate", 0);

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
                        String code = jsonResponse.getString("code");
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String userNameData = data.getString("userName");
                        String userPointData = data.getString("point");

                        if("00000".equals(code)){
                            userName=userNameData;
                            userPoint=userPointData;
                            startActivity(MainActivity.class);
                        }else{

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("signActivity", "请求失败，状态码: " + response.code());
                    Log.e("signActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
        this.finish(); // 关闭当前活动
    }
}