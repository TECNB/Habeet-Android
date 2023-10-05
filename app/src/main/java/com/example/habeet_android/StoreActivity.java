package com.example.habeet_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.StoreAdapter;
import Item.StoreItem;
import Util.DrawerMenuHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView storeRecyclerView;
    private StoreAdapter storeAdapter;
    private List<StoreItem> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(3);
        drawerMenuHelper.getUserData();

        CardView storeCardView=findViewById(R.id.storeCardView);
        storeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听按钮，如果点击，就跳转
                startActivity(CreateActivity.class);
            }
        });

        // 初始化RecyclerView
        storeRecyclerView = findViewById(R.id.storeRecyclerView);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化数据列表
        storeList = new ArrayList<>(); // 初始化 storeList

        //设置为横向布局的storeRecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        storeRecyclerView.setLayoutManager(layoutManager);

        // 初始化适配器并将其与RecyclerView关联
        storeAdapter = new StoreAdapter(storeList);
        storeRecyclerView.setAdapter(storeAdapter);




        // 启动异步任务来执行网络请求
        fetchStoreData();
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("sourceActivity","Store");
        this.startActivity(intent);
        this.finish(); // 关闭当前活动
    }

    private void fetchStoreData() {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/store/get";
        // 请求数据
        String requestData = "3489044730@qq.com";
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
                    try {
                        // 解析JSON响应
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray data = jsonResponse.getJSONArray("data");

                        // 清空已有数据
                        storeList.clear();

                        // 遍历JSON数据并添加到tagList中
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String storeName = item.getString("storeName");
                            String storeDescribe = item.getString("storeDescribe");
                            String storePoint = item.getString("storePoint");
                            String storeHour = item.getString("storeHour");
                            String storeMinute = item.getString("storeMinute");

                            // 创建TagItem对象并添加到tagList中
                            storeList.add(new StoreItem(storeName, storeDescribe,storePoint,storeHour,storeMinute));
                        }

                        // 更新 UI，确保在主线程中执行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 通知适配器数据已更新
                                storeAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    Log.e("TagActivity", "请求失败，状态码: " + response.code());
                    Log.e("TagActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}