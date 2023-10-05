package com.example.habeet_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

public class HomeActivity extends AppCompatActivity {
    private EditText userEmailEditText;
    private ImageView homeBack;
    private CardView homeCardView;
    private String code="";

    public static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userEmailEditText=findViewById(R.id.userEmail);


        homeCardView=findViewById(R.id.homeCardView);
        homeBack=findViewById(R.id.homeBack);

        homeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail=userEmailEditText.getText().toString();
                UserDataManager();
            }
        });

        homeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginNavActivity.class);
            }
        });
    }
    private void UserDataManager(){
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/user/home";
        // 请求数据
        String requestData = userEmail;
        // 设置请求体
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, requestData);
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
                    if (responseBody.isEmpty()){
                        startActivity(SignActivity.class);
                    }else{
                        try {
                            // 解析JSON响应
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            code = jsonResponse.getString("code");

                            if("00000".equals(code)){
                                startActivity(LoginActivity.class);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // 请求失败，输出错误信息
                    Log.e("HomeActivity", "请求失败，状态码: " + response.code());
                    Log.e("HomeActivity", response.body().string());
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