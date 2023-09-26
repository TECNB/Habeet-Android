package com.example.habeet_android;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.TargetNoTimeAdapter;
import Adapter.TargetWithTimeAdapter;
import Item.TargetItem;
import Util.DrawerMenuHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TargetActivity extends AppCompatActivity {
    private RecyclerView targetWithTimeRecyclerView;
    private RecyclerView targetNoTimeRecyclerView;
    private TargetWithTimeAdapter targetWithTimeAdapter;
    private TargetNoTimeAdapter targetNoTimeAdapter;
    private List<TargetItem> targetWithTimeList;
    private List<TargetItem> targetNoTimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(1);
        drawerMenuHelper.getUserData();

        // 初始化RecyclerView
        targetWithTimeRecyclerView = findViewById(R.id.targetWithTimeRecyclerView);
        targetNoTimeRecyclerView = findViewById(R.id.targetNoTimeRecyclerView);
        targetWithTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        targetNoTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化数据列表
        targetWithTimeList = new ArrayList<>(); // 初始化 targetWithTimeList
        targetNoTimeList = new ArrayList<>(); // 初始化 targetNoTimeList

        // 初始化适配器并将其与RecyclerView关联
        targetWithTimeAdapter = new TargetWithTimeAdapter(targetWithTimeList);
        targetNoTimeAdapter = new TargetNoTimeAdapter(targetNoTimeList);

        targetWithTimeRecyclerView.setAdapter(targetWithTimeAdapter);
        targetNoTimeRecyclerView.setAdapter(targetNoTimeAdapter);

        // 启动异步任务来执行网络请求
        fetchTargetData();
    }

    private void fetchTargetData() {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/target/get";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", "3489044730@qq.com");
            requestData.put("ifTargetUpdate", 0);
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
                        JSONArray data = jsonResponse.getJSONArray("data");

                        // 清空已有数据
                        targetWithTimeList.clear();
                        targetNoTimeList.clear();

                        // 遍历JSON数据并添加到targetList中
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            if(item.getString("status").equals("0")){
                                String targetName = item.getString("targetName");
                                String targetDescribe = item.getString("targetDescribe");
                                String targetPoint = item.getString("targetPoint");
                                String deadline = item.getString("deadline");

                                // 创建TargetItem对象并添加到targetList中
                                targetNoTimeList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline));
                            }else if(item.getString("status").equals("1")){
                                String targetName = item.getString("targetName");
                                String targetDescribe = item.getString("targetDescribe");
                                String targetPoint = item.getString("targetPoint");
                                String deadline = item.getString("deadline");

                                // 创建TargetItem对象并添加到targetList中
                                targetWithTimeList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline));
                            }

                        }

                        // 更新 UI，确保在主线程中执行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 通知适配器数据已更新
                                targetNoTimeAdapter.notifyDataSetChanged();
                                targetWithTimeAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    Log.e("TargetActivity", "请求失败，状态码: " + response.code());
                    Log.e("TargetActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}