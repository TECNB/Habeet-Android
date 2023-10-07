package com.example.habeet_android;

import static com.example.habeet_android.HomeActivity.userEmail;
import static com.example.habeet_android.LoginActivity.userPoint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapter.TagMenuAdapter;
import Adapter.TargetMenuAdapter;
import Item.TagItem;
import Item.TargetItem;
import Util.DrawerMenuHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView arrowDownImage;
    private ImageView arrowUpImage;

    private CardView targetMenuDetail;

    private CardView tagMenuDetail;

    private CardView tagMenuNav;

    private CardView timeButton;
    private CardView timeCardView;

    private List<TargetItem> targetMenuList;
    private RecyclerView targetMenuRecyclerView;
    private TargetMenuAdapter targetMenuAdapter;

    private Calendar calendar = Calendar.getInstance();

    private RecyclerView tagMenuRecyclerView;
    private TagMenuAdapter tagMenuAdapter;
    private List<TagItem> tagMenuList;
    private String targetPoint;
    private TextView navPointTextView;

    public static TextView timerTextView;
    public static TextView timeButtonTextView;
    public static TextView timerTagNameTextView;

    public static String timer;
    public static String tagPoint;
    public static String tagName;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;




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
        timeCardView=findViewById(R.id.timeCardView);

        timerTextView=findViewById(R.id.timerTextView);

        timeButtonTextView=findViewById(R.id.timeButtonTextView);
        timerTagNameTextView=findViewById(R.id.timerTagNameTextView);



        targetMenuRecyclerView=findViewById(R.id.targetMenuRecyclerView);
        targetMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        targetMenuList=new ArrayList<>();

        // 初始化适配器并将其与RecyclerView关联
        targetMenuAdapter = new TargetMenuAdapter(targetMenuList);
        targetMenuRecyclerView.setAdapter(targetMenuAdapter);



        tagMenuRecyclerView=findViewById(R.id.tagMenuRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagMenuRecyclerView.setLayoutManager(layoutManager);

        tagMenuList=new ArrayList<>();

        // 初始化适配器并将其与RecyclerView关联
        tagMenuAdapter = new TagMenuAdapter(tagMenuList);
        tagMenuRecyclerView.setAdapter(tagMenuAdapter);

        // 获取activity_nav.xml中的根布局
        View Nav = findViewById(R.id.Nav);

        navPointTextView = Nav.findViewById(R.id.navPointTextView);


        arrowDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowUpImage.setVisibility(View.VISIBLE);
                arrowDownImage.setVisibility(View.GONE);

                targetMenuDetail.setVisibility(View.VISIBLE);
                if (targetMenuList.isEmpty()){
                    Toast.makeText(getApplicationContext(), "还没有目标，快去建立吧", Toast.LENGTH_SHORT).show();
                }
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
                tagMenuDetail.setVisibility(tagMenuDetail.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTimerRunning) {
                    // 开始计时器
                    startTimer();
                } else {
                    // 取消计时器
                    cancelTimer();
                }
            }
        });

        timeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagMenuDetail.setVisibility(View.GONE);
            }
        });
        // 启动异步任务来执行网络请求
        fetchTagData();
        // 启动异步任务来执行网络请求
        fetchTargetData();
    }


    // 启动计时器
    private void startTimer() {
        String timerText = timerTextView.getText().toString();
        String[] parts = timerText.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        long totalTimeMillis = (hours * 3600 + minutes * 60) * 1000;

        countDownTimer = new CountDownTimer(totalTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新倒计时显示
                long seconds = millisUntilFinished / 1000;
                long remainingHours = seconds / 3600;
                long remainingMinutes = (seconds % 3600) / 60;
                long remainingSeconds = seconds % 60;
                timerTextView.setText(String.format("%02d:%02d:%02d", remainingHours, remainingMinutes, remainingSeconds));
            }

            @Override
            public void onFinish() {
                // 计时结束时的操作
                timerTextView.setText(timer);
                isTimerRunning = false;
                finishTag();

                Toast.makeText(getApplicationContext(), "计时结束，获得"+tagPoint+"Point", Toast.LENGTH_SHORT).show();

                timeButtonTextView.setText("开始");
            }
        }.start();

        isTimerRunning = true;
        timeButtonTextView.setText("取消");
    }

    // 取消计时器
    private void cancelTimer() {
        if (countDownTimer != null) {
            // 创建一个AlertDialog.Builder对象
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 设置弹窗标题
            builder.setTitle("确定要放弃吗？");

            // 设置弹窗正文
            builder.setMessage("本次计时将不会得到任何分数");

            // 设置确定按钮，并指定点击事件
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 用户点击了确定按钮，执行放弃计时的操作
                    countDownTimer.cancel();
                    timerTextView.setText(timer);
                    isTimerRunning = false;
                    timeButtonTextView.setText("开始");
                }
            });

            // 设置取消按钮，并指定点击事件
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 用户点击了取消按钮，不执行任何操作，只关闭弹窗
                    dialog.dismiss();
                }
            });

            // 创建并显示AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    private void finishTag() {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/tag/finish";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("tagName", tagName);
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

                    // 通知适配器删除了特定位置的项
                    userPoint = String.valueOf(Integer.parseInt(userPoint) + Integer.parseInt(tagPoint));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navPointTextView.setText(userPoint);
                        }
                    });
                } else {
                    // 请求失败，输出错误信息
                    Log.e("MainActivity", "请求失败，状态码: " + response.code());
                    Log.e("MainActivity", response.body().string());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


    // 执行网络请求的方法
    private void fetchTagData() {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/tag/get";
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
                    try {
                        // 解析JSON响应
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray data = jsonResponse.getJSONArray("data");

                        // 清空已有数据
                        tagMenuList.clear();

                        // 遍历JSON数据并添加到tagList中
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String tagName = item.getString("tagName");
                            String tagDescribe = item.getString("tagDescribe");
                            String tagPoint = item.getString("tagPoint");
                            String tagHour = item.getString("tagHour");
                            String tagMinute = item.getString("tagMinute");


                            // 创建TagItem对象并添加到tagList中
                            tagMenuList.add(new TagItem(tagName, tagDescribe,tagPoint,tagHour,tagMinute));
                        }

                        String hour = tagMenuList.get(0).getTagHour();
                        String minute = tagMenuList.get(0).getTagMinute();

                        // 将字符串转换为整数
                        int hourInt = Integer.parseInt(hour);
                        int minuteInt = Integer.parseInt(minute);

                        tagPoint=tagMenuList.get(0).getTagPoint();
                        tagName=tagMenuList.get(0).getTagName();

                        // 更新 UI，确保在主线程中执行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 使用String.format来格式化时间
                                timer = String.format("%02d:%02d:00", hourInt, minuteInt);
                                timerTextView.setText(timer);
                                timerTagNameTextView.setText(tagName);
                                // 通知适配器数据已更新
                                tagMenuAdapter.notifyDataSetChanged();

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    Log.e("MainActivity", "请求失败，状态码: " + response.code());
                    Log.e("MainActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void fetchTargetData() {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/target/get";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
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
                        targetMenuList.clear();

                        // 遍历JSON数据并添加到targetList中
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            if(item.getString("status").equals("0")){
                                String targetName = item.getString("targetName");
                                String targetDescribe = item.getString("targetDescribe");
                                String targetPoint = item.getString("targetPoint");
                                String deadline = item.getString("deadline");
                                String targetId=item.getString("targetId");

                                String dayDifferenceString;
                                dayDifferenceString="任意时间";

                                // 创建TargetItem对象并添加到targetList中
                                targetMenuList.add(0,new TargetItem(targetName, targetDescribe,targetPoint,deadline,targetId,dayDifferenceString));
                            }else if(item.getString("status").equals("1")){
                                String targetName = item.getString("targetName");
                                String targetDescribe = item.getString("targetDescribe");
                                String targetPoint = item.getString("targetPoint");
                                String deadline = item.getString("deadline");
                                String targetId=item.getString("targetId");
                                String dayDifferenceString;

                                // 解析deadline中的日期部分和时间部分
                                String[] deadlineParts = deadline.split("T");
                                String deadlineDate = deadlineParts[0]; // 日期部分，例如 "2023-07-19"
                                String deadlineTime = deadlineParts[1].substring(0, 5); // 时间部分，例如 "19:14"

                                // 获取选中日期的字符串形式，例如 "2023-07-19"
                                String selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

                                // 计算日期差
                                long dayDifference = calculateDayDifference(selectedDate, deadlineDate);

                                // 根据日期差的规则设置不同的值
                                if (dayDifference > 0) {
                                    // 相差日期大于0，获得相差的天数
                                    dayDifferenceString=(dayDifference + "天");
                                } else if (dayDifference == 0) {
                                    // 相差日期等于0，获得deadline的小时以及分钟
                                    dayDifferenceString=deadlineTime;
                                } else {
                                    // 相差日期小于0，获得deadline的月份和日子
                                    dayDifferenceString=deadlineDate.substring(5);
                                }

                                // 创建TargetItem对象并添加到targetList中
                                targetMenuList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline,targetId,dayDifferenceString));
                            }
                        }

                        // 更新 UI，确保在主线程中执行
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 通知适配器数据已更新
                                targetMenuAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    Log.e("MainActivity", "请求失败，状态码: " + response.code());
                    Log.e("MainActivity", response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
    // 计算日期差的方法
    private long calculateDayDifference(String date1, String date2) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate1 = dateFormat.parse(date1);
            Date parsedDate2 = dateFormat.parse(date2);
            long timeDifference = parsedDate2.getTime() - parsedDate1.getTime();
            return TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


}