package com.example.habeet_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import Adapter.TargetCompletedAdapter;
import Adapter.TargetExpireAdapter;
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
    private RecyclerView targetCompletedRecyclerView;
    private RecyclerView targetExpireRecyclerView;

    private TextView targetWithTimeTextView;
    private TextView targetNoTimeTextView;
    private TextView targetCompletedTextView;
    private TextView targetExpireTextView;

    private TargetWithTimeAdapter targetWithTimeAdapter;
    private TargetNoTimeAdapter targetNoTimeAdapter;
    private TargetCompletedAdapter targetCompletedAdapter;
    private TargetExpireAdapter targetExpireAdapter;

    private List<TargetItem> targetWithTimeList;
    private List<TargetItem> targetNoTimeList;
    private List<TargetItem> targetCompletedList;
    private List<TargetItem> targetExpireList;

    private int targetVisibilityState1 = View.VISIBLE;
    private int targetVisibilityState2 = View.GONE;

    private View Nav;
    ImageView navDelete;

    private LinearLayout dateSelectorLayout;
    private Calendar calendar = Calendar.getInstance();
    private TextView lastSelectedDayTextView = null;

    private TextView DoingBefore;
    private TextView DoneBefore;
    private TextView ExpireBefore;
    private CardView DoingAfter;
    private CardView DoneAfter;
    private CardView ExpireAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        DrawerMenuHelper drawerMenuHelper = new DrawerMenuHelper(this);
        drawerMenuHelper.setupDrawerMenu(1);
        drawerMenuHelper.getUserData();

        Nav = findViewById(R.id.Nav);
        navDelete = Nav.findViewById(R.id.navDelete);

        navDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换可见性状态
                targetVisibilityState1 = (targetVisibilityState1 == View.GONE) ? View.VISIBLE : View.GONE;
                targetVisibilityState2 = (targetVisibilityState2 == View.GONE) ? View.VISIBLE : View.GONE;

                // 更新两个适配器的可见性状态
                targetNoTimeAdapter.updateVisibility(targetVisibilityState1, targetVisibilityState2);
                targetWithTimeAdapter.updateVisibility(targetVisibilityState1, targetVisibilityState2);
                targetCompletedAdapter.updateVisibility(targetVisibilityState1, targetVisibilityState2);
                targetExpireAdapter.updateVisibility(targetVisibilityState1, targetVisibilityState2);
            }
        });

        // 初始化RecyclerView
        targetWithTimeRecyclerView = findViewById(R.id.targetWithTimeRecyclerView);
        targetNoTimeRecyclerView = findViewById(R.id.targetNoTimeRecyclerView);
        targetCompletedRecyclerView = findViewById(R.id.targetCompletedRecyclerView);
        targetExpireRecyclerView = findViewById(R.id.targetExpireRecyclerView);

        targetWithTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        targetNoTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        targetCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        targetExpireRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DoingBefore= findViewById(R.id.DoingBefore);
        DoneBefore= findViewById(R.id.DoneBefore);
        ExpireBefore= findViewById(R.id.ExpireBefore);
        DoingAfter= findViewById(R.id.DoingAfter);
        DoneAfter= findViewById(R.id.DoneAfter);
        ExpireAfter= findViewById(R.id.ExpireAfter);

        targetWithTimeTextView= findViewById(R.id.targetWithTimeTextView);
        targetNoTimeTextView= findViewById(R.id.targetNoTimeTextView);
        targetCompletedTextView= findViewById(R.id.targetCompletedTextView);
        targetExpireTextView= findViewById(R.id.targetExpireTextView);

        DoingBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoneAfter.setVisibility(View.GONE);
                DoingAfter.setVisibility(View.VISIBLE);
                ExpireAfter.setVisibility(View.GONE);
                DoingBefore.setVisibility(View.GONE);
                DoneBefore.setVisibility(View.VISIBLE);
                ExpireBefore.setVisibility(View.VISIBLE);

                targetWithTimeTextView.setVisibility(View.VISIBLE);
                targetNoTimeTextView.setVisibility(View.VISIBLE);
                targetWithTimeRecyclerView.setVisibility(View.VISIBLE);
                targetNoTimeRecyclerView.setVisibility(View.VISIBLE);

                targetCompletedTextView.setVisibility(View.GONE);
                targetExpireTextView.setVisibility(View.GONE);
                targetExpireRecyclerView.setVisibility(View.GONE);
                targetCompletedRecyclerView.setVisibility(View.GONE);
            }
        });
        DoneBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoneAfter.setVisibility(View.VISIBLE);
                DoingAfter.setVisibility(View.GONE);
                ExpireAfter.setVisibility(View.GONE);
                DoingBefore.setVisibility(View.VISIBLE);
                DoneBefore.setVisibility(View.GONE);
                ExpireBefore.setVisibility(View.VISIBLE);

                targetWithTimeTextView.setVisibility(View.GONE);
                targetNoTimeTextView.setVisibility(View.GONE);
                targetWithTimeRecyclerView.setVisibility(View.GONE);
                targetNoTimeRecyclerView.setVisibility(View.GONE);

                targetCompletedTextView.setVisibility(View.VISIBLE);
                targetExpireTextView.setVisibility(View.GONE);
                targetExpireRecyclerView.setVisibility(View.GONE);
                targetCompletedRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        ExpireBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoneAfter.setVisibility(View.GONE);
                DoingAfter.setVisibility(View.GONE);
                ExpireAfter.setVisibility(View.VISIBLE);
                DoingBefore.setVisibility(View.VISIBLE);
                DoneBefore.setVisibility(View.VISIBLE);
                ExpireBefore.setVisibility(View.GONE);

                targetWithTimeTextView.setVisibility(View.GONE);
                targetNoTimeTextView.setVisibility(View.GONE);
                targetWithTimeRecyclerView.setVisibility(View.GONE);
                targetNoTimeRecyclerView.setVisibility(View.GONE);

                targetCompletedTextView.setVisibility(View.GONE);
                targetExpireTextView.setVisibility(View.VISIBLE);
                targetExpireRecyclerView.setVisibility(View.VISIBLE);
                targetCompletedRecyclerView.setVisibility(View.GONE);
            }
        });

        // 初始化数据列表
        targetWithTimeList = new ArrayList<>(); // 初始化 targetWithTimeList
        targetNoTimeList = new ArrayList<>(); // 初始化 targetNoTimeList
        targetCompletedList = new ArrayList<>(); // 初始化 targetNoTimeList
        targetExpireList = new ArrayList<>(); // 初始化 targetNoTimeList

        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);

        for (int index = 0; index < 30; index++) {
            View dateItemView = createDateItemView(index,targetWithTimeList);
            dateSelectorLayout.addView(dateItemView);
        }

        // 初始化适配器并将其与RecyclerView关联
        targetWithTimeAdapter = new TargetWithTimeAdapter(targetWithTimeList,targetVisibilityState1, targetVisibilityState2);
        targetNoTimeAdapter = new TargetNoTimeAdapter(targetNoTimeList,targetVisibilityState1, targetVisibilityState2);
        targetCompletedAdapter = new TargetCompletedAdapter(targetCompletedList,targetVisibilityState1, targetVisibilityState2);
        targetExpireAdapter = new TargetExpireAdapter(targetExpireList,targetVisibilityState1, targetVisibilityState2);

        targetWithTimeRecyclerView.setAdapter(targetWithTimeAdapter);
        targetNoTimeRecyclerView.setAdapter(targetNoTimeAdapter);
        targetCompletedRecyclerView.setAdapter(targetCompletedAdapter);
        targetExpireRecyclerView.setAdapter(targetExpireAdapter);


        CardView targetCardView=findViewById(R.id.targetCardView);
        targetCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听按钮，如果点击，就跳转
                startActivity(CreateActivity.class);
            }
        });
        // 启动异步任务来执行网络请求
        fetchTargetData();
    }
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("sourceActivity","Target");
        this.startActivity(intent);
        this.finish(); // 关闭当前活动
    }


    private View createDateItemView(final int index,final List<TargetItem> targetWithTimeList) {
        // 获取当前日期
        Date currentDate = calendar.getTime();
        // 将日期向前或向后移动 index 天
        calendar.add(Calendar.DAY_OF_MONTH, index);
        // 获取新的日期
        Date date = calendar.getTime();
        // 恢复 calendar 到当前日期
        calendar.setTime(currentDate);

        // 创建日期的格式化工具，用于显示星期几和日期天数
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("E");
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("d");

        // 创建日期项的布局参数，设置边距
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(18, 0, 18, 0);

        // 创建日期项的外层布局
        RelativeLayout dateItemLayout = new RelativeLayout(this);
        dateItemLayout.setLayoutParams(layoutParams);

        // 创建显示星期几的文本视图
        TextView dayOfWeekTextView = new TextView(this);
        dayOfWeekTextView.setText(dayOfWeekFormat.format(date));
        dayOfWeekTextView.setId(View.generateViewId());
        dayOfWeekTextView.setTextColor(Color.GRAY);
        dayOfWeekTextView.setTextSize(12);

        // 设置星期几文本视图的布局参数，水平居中显示
        RelativeLayout.LayoutParams dayOfWeekParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        dayOfWeekParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        dayOfWeekTextView.setLayoutParams(dayOfWeekParams);

        // 创建显示日期天数的文本视图
        TextView dayOfMonthTextView = new TextView(this);
        dayOfMonthTextView.setText(dayOfMonthFormat.format(date));
        dayOfMonthTextView.setWidth(getResources().getDimensionPixelSize(R.dimen.date_item_width));
        dayOfMonthTextView.setHeight(getResources().getDimensionPixelSize(R.dimen.date_item_height));
        dayOfMonthTextView.setGravity(Gravity.CENTER);

        // 检查是否为当前日期，设置初始状态
        if (index == 0) {
            dayOfMonthTextView.setTextColor(Color.parseColor("#CFC8FF"));
            dayOfMonthTextView.setBackgroundResource(R.drawable.selected_date_background);
            lastSelectedDayTextView = dayOfMonthTextView;
        } else {
            dayOfMonthTextView.setTextColor(Color.parseColor("#CFC8FF"));
            dayOfMonthTextView.setBackgroundResource(R.drawable.unselected_date_background);
        }

        // 设置日期天数文本视图的布局参数，水平居中显示，位于星期几文本视图下方
        RelativeLayout.LayoutParams dayOfMonthParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        dayOfMonthParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        dayOfMonthParams.addRule(RelativeLayout.BELOW, dayOfWeekTextView.getId());
        dayOfMonthTextView.setLayoutParams(dayOfMonthParams);

        // 允许日期天数文本视图响应点击事件
        dayOfMonthTextView.setClickable(true);

        // 设置日期天数文本视图的点击事件监听器
        // 在日期项点击事件的回调中添加以下代码
        dayOfMonthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedDayTextView != null) {
                    // 取消上一个选中日期的背景和文本颜色
                    lastSelectedDayTextView.setBackgroundResource(R.drawable.unselected_date_background);
                    lastSelectedDayTextView.setTextColor(Color.parseColor("#CFC8FF"));
                }

                // 设置当前选中日期的背景和文本颜色
                dayOfMonthTextView.setBackgroundResource(R.drawable.selected_date_background);
                dayOfMonthTextView.setTextColor(Color.parseColor("#CFC8FF"));

                // 更新 lastSelectedDayTextView 为当前选中的日期
                lastSelectedDayTextView = dayOfMonthTextView;
                System.out.println("lastSelectedDayTextView:" + lastSelectedDayTextView);

                // 获取选中日期的字符串形式，例如 "2023-07-19"
                String selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                // 遍历目标列表
                for (int i = 0; i < targetWithTimeList.size(); i++) {
                    TargetItem targetItem = targetWithTimeList.get(i);
                    String deadline = targetItem.getDeadline(); // 获取deadline，例如 "2023-07-19T19:14:00"

                    // 解析deadline中的日期部分和时间部分
                    String[] deadlineParts = deadline.split("T");
                    String deadlineDate = deadlineParts[0]; // 日期部分，例如 "2023-07-19"
                    String deadlineTime = deadlineParts[1].substring(0, 5); // 时间部分，例如 "19:14"

                    // 计算日期差
                    long dayDifference = calculateDayDifference(selectedDate, deadlineDate);

                    // 根据日期差的规则设置不同的值
                    if (dayDifference > 0) {
                        // 相差日期大于0，获得相差的天数
                        targetItem.setDayDifference(dayDifference + "天");
                    } else if (dayDifference == 0) {
                        // 相差日期等于0，获得deadline的小时以及分钟
                        targetItem.setDayDifference(deadlineTime);
                    } else {
                        // 相差日期小于0，获得deadline的月份和日子
                        targetItem.setDayDifference(deadlineDate.substring(5));
                    }
                    System.out.println("deadlineTime:"+targetItem.getDayDifference());
                    // 通知适配器数据已更改
                    targetWithTimeAdapter.notifyDataSetChanged();
                }
            }
        });

        // 将星期几和日期天数文本视图添加到日期项的布局中
        dateItemLayout.addView(dayOfWeekTextView);
        dateItemLayout.addView(dayOfMonthTextView);

        // 返回日期项的布局
        return dateItemLayout;
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
                                String targetId=item.getString("targetId");

                                String dayDifferenceString;
                                dayDifferenceString="任意时间";


                                // 创建TargetItem对象并添加到targetList中
                                targetNoTimeList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline,targetId,dayDifferenceString));
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
                                targetWithTimeList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline,targetId,dayDifferenceString));
                            }else if(item.getString("status").equals("2")){
                                String targetName = item.getString("targetName");
                                String targetDescribe = item.getString("targetDescribe");
                                String targetPoint = item.getString("targetPoint");
                                String deadline = item.getString("deadline");
                                String targetId=item.getString("targetId");

                                String dayDifferenceString;
                                dayDifferenceString="完成";


                                // 创建TargetItem对象并添加到targetList中
                                targetCompletedList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline,targetId,dayDifferenceString));
                            }else if(item.getString("status").equals("3")){
                                String targetName = item.getString("targetName");
                                String targetDescribe = item.getString("targetDescribe");
                                String targetPoint = item.getString("targetPoint");
                                String deadline = item.getString("deadline");
                                String targetId=item.getString("targetId");

                                String dayDifferenceString;
                                dayDifferenceString="过期";


                                // 创建TargetItem对象并添加到targetList中
                                targetExpireList.add(new TargetItem(targetName, targetDescribe,targetPoint,deadline,targetId,dayDifferenceString));
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