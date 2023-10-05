package com.example.habeet_android;

import static com.example.habeet_android.HomeActivity.userEmail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginActivity extends AppCompatActivity {
    private EditText userPassEditText;
    private ImageView loginBack;
    private CardView loginCardView;
    private String userPass;

    public static String userName;
    public static String userPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPassEditText=findViewById(R.id.userPass);


        loginCardView=findViewById(R.id.loginCardView);
        loginBack=findViewById(R.id.loginBack);

        loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPass=userPassEditText.getText().toString();
                checkPassword();
            }
        });

        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HomeActivity.class);
            }
        });
    }
    private void checkPassword(){
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/user/login";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("userPassword", userPass);

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
                    Log.e("LoginActivity", "请求失败，状态码: " + response.code());
                    Log.e("LoginActivity", response.body().string());
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