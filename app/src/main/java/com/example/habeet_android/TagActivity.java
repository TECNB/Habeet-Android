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

import Item.TagItem;
import Util.DrawerMenuHelper;
import Util.TagAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TagActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;
     private List<TagItem> tagList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        // 设置侧边栏菜单
        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(2);

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化数据列表
        tagList = new ArrayList<>(); // 初始化 tagList

        // 初始化适配器并将其与RecyclerView关联
        tagAdapter = new TagAdapter(tagList);
        recyclerView.setAdapter(tagAdapter);

        // 启动异步任务来执行网络请求
        performNetworkRequest();


    }

    // 执行网络请求的方法
    private void performNetworkRequest() {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/tag/get";
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
                        tagList.clear();

                        // 遍历JSON数据并添加到tagList中
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String tagName = item.getString("tagName");
                            String tagDescribe = item.getString("tagDescribe");

                            // 创建TagItem对象并添加到tagList中
                            tagList.add(new TagItem(tagName, tagDescribe));
                        }

                        // 更新 UI，确保在主线程中执行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 通知适配器数据已更新
                                tagAdapter.notifyDataSetChanged();
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