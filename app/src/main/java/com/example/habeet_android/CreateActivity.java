package com.example.habeet_android;

import static com.example.habeet_android.HomeActivity.userEmail;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateActivity extends AppCompatActivity {
    private TextView createNavTitle;
    private ImageView createX;

    private EditText nameEditText;
    private EditText describeEditText;

    private String Name;
    private String Describe;
    private Integer Point=0;
    private String deadlineString="";

    private Integer timeHour=0;
    private Integer timeMinute=0;

    private TextView deadlineTextView;
    private TextView scoreTextView;
    private TextView saveTextView;

    private String status;
    private String ifUpdate;

    private Long Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        createNavTitle = findViewById(R.id.createNavTitle);

        // 获取传递的标志
        String sourceActivity = getIntent().getStringExtra("sourceActivity");
        String tagName = getIntent().getStringExtra("tagName");
        String tagDescribe = getIntent().getStringExtra("tagDescribe");
        String tagPoint = getIntent().getStringExtra("tagPoint");
        String tagHour = getIntent().getStringExtra("tagHour");
        String tagMinute = getIntent().getStringExtra("tagMinute");
        String tagId = getIntent().getStringExtra("tagId");
        ifUpdate = getIntent().getStringExtra("ifUpdate");



        // 根据不同的标志设置不同的标题
        if ("Tag".equals(sourceActivity)) {
            createNavTitle.setText("建立标签");
        } else if ("Store".equals(sourceActivity)) {
            createNavTitle.setText("建立商品");
        }else if ("Target".equals(sourceActivity)) {
            createNavTitle.setText("建立目标");
        }
        createX=findViewById(R.id.createX);
        createX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 根据不同的标志设置不同的标题
                if ("Tag".equals(sourceActivity)) {
                    startActivity(TagActivity.class);
                } else if ("Store".equals(sourceActivity)) {
                    startActivity(StoreActivity.class);
                }else if ("Target".equals(sourceActivity)) {
                    startActivity(TargetActivity.class);
                }
            }
        });

        deadlineTextView = findViewById(R.id.deadlineTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        LinearLayout calendarLinearLayout = findViewById(R.id.Calendar);
        calendarLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Target".equals(sourceActivity)) {
                    showDatePicker();
                } else{
                    showTimePicker();
                }

            }
        });

        LinearLayout pointLinearLayout = findViewById(R.id.Point);
        pointLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPointPicker();
            }
        });

        nameEditText=findViewById(R.id.Name);
        describeEditText=findViewById(R.id.Describe);

        saveTextView=findViewById(R.id.Save);

        if ("1".equals(ifUpdate)){
            nameEditText.setText(tagName);
            describeEditText.setText(tagDescribe);
            deadlineTextView.setText(tagHour+"小时"+tagMinute+"分钟");
            scoreTextView.setText(tagPoint+" Point");
            timeMinute=Integer.valueOf(tagHour);
            timeMinute=Integer.valueOf(tagMinute);
            Point=Integer.valueOf(tagPoint);
            Id=Long.valueOf(tagId);
        }
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取输入框中的值
                Name = nameEditText.getText().toString();
                Describe = describeEditText.getText().toString();

                // 检查Name不为空
                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(getApplicationContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return; // 如果Name为空，不执行后续操作
                }

                // 检查Describe不为空
                if (TextUtils.isEmpty(Describe)) {
                    Toast.makeText(getApplicationContext(), "请输入备注", Toast.LENGTH_SHORT).show();
                    return; // 如果Describe为空，不执行后续操作
                }

                // 检查deadlineString不为空
                if ((timeMinute==0&&timeHour==0)&&!"Target".equals(sourceActivity)) {
                    Toast.makeText(getApplicationContext(), "请选择时间", Toast.LENGTH_SHORT).show();
                    return; // 如果deadlineString为空，不执行后续操作
                }

                // 检查Point不为0
                if (Point == 0) {
                    Toast.makeText(getApplicationContext(), "请选择分数", Toast.LENGTH_SHORT).show();
                    return; // 如果Point为0，不执行后续操作
                }


                // 根据不同的标志进行不同的创建
                if ("Tag".equals(sourceActivity)) {
                    saveTag();
                } else if ("Store".equals(sourceActivity)) {
                    saveStore();
                } else if ("Target".equals(sourceActivity)) {
                    saveTarget();
                }
            }
        });

    }

    private void saveTag(){
        Log.v("save",Name);
        Log.v("save",Describe);
        Log.v("save",Point.toString());
        Log.v("save",timeMinute.toString());
        Log.v("save",timeHour.toString());
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/tag/save";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("tagName", Name);
            requestData.put("tagDescribe", Describe);
            requestData.put("tagPoint", Point);
            requestData.put("tagHour", timeHour);
            requestData.put("tagMinute", timeMinute);
            if ("1".equals(ifUpdate)){
                requestData.put("ifTagUpdate", 1);
                requestData.put("tagId",Id );
            }else {
                requestData.put("ifTagUpdate", 0);
            }

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

                        String ifRepeat=data.getString("ifRepeat");

                        //create界面关于随时完成的目标名字重复的后端逻辑有问题（实际上会直接错误返回500）
                        if ("1".equals(ifRepeat)){
                            // 当status为4时，弹出提示信息
                            Log.d("CreateActivity", "标签名字重复提示将会显示");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateActivity.this, "标签名字重复", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            startActivity(TagActivity.class);
                        }

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

    private void saveStore(){
        Log.v("save",Name);
        Log.v("save",Describe);
        Log.v("save",Point.toString());
        Log.v("save",timeMinute.toString());
        Log.v("save",timeHour.toString());
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/store/save";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("storeName", Name);
            requestData.put("storeDescribe", Describe);
            requestData.put("storePoint", Point);
            requestData.put("storeHour", timeHour);
            requestData.put("storeMinute", timeMinute);
            requestData.put("ifStoreUpdate", 0);
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

                        String ifRepeat=data.getString("ifRepeat");

                        //create界面关于随时完成的目标名字重复的后端逻辑有问题（实际上会直接错误返回500）
                        if ("1".equals(ifRepeat)){
                            // 当status为4时，弹出提示信息
                            Log.d("CreateActivity", "商品名字重复提示将会显示");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateActivity.this, "商品名字重复", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            startActivity(StoreActivity.class);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 请求失败，输出错误信息
                    Log.e("StoreActivity", "请求失败，状态码: " + response.code());
                    Log.e("StoreActivity", response.body().string());
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveTarget(){
        Log.v("save",Name);
        Log.v("save",Describe);
        Log.v("save",Point.toString());
        Log.v("save",deadlineString);
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/target/save";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("targetName", Name);
            requestData.put("targetDescribe", Describe);
            requestData.put("targetPoint", Point);
            requestData.put("deadlineString", deadlineString);
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
                        JSONObject data = jsonResponse.getJSONObject("data");

                        status=data.getString("status");

                        //create界面关于随时完成的目标名字重复的后端逻辑有问题（实际上会直接错误返回500）
                        if ("4".equals(status)){
                            // 当status为4时，弹出提示信息
                            Log.d("CreateActivity", "目标名字重复提示将会显示");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CreateActivity.this, "目标名字重复", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            startActivity(TargetActivity.class);
                        }

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

    private void showDatePicker() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        // 创建日期选择对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 在选择日期后更新截止时间
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth + " " + String.format("%02d:%02d", hour, minute);
                deadlineTextView.setText(selectedDate);

                deadlineString=selectedDate;
            }
        }, year, month, day);

        // 显示日期选择对话框
        datePickerDialog.show();
    }

    private void showPointPicker() {
        // 创建一个Dialog来显示Point选择器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择Point");

        // 创建NumberPicker并设置数据源
        final NumberPicker numberPicker = new NumberPicker(this);
        final String[] points = new String[]{"1 Point", "2 Point", "3 Point", "4 Point", "5 Point", "6 Point", "7 Point", "8 Point"};
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(points.length - 1);
        numberPicker.setDisplayedValues(points);

        // 设置初始值
        numberPicker.setValue(0);

        builder.setView(numberPicker);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在点击确定后获取选择的Point值并更新UI
                String selectedPoint = points[numberPicker.getValue()];
                scoreTextView.setText(selectedPoint);
                Point=numberPicker.getValue()+1;
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户取消选择
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTimePicker() {
        // 创建一个Dialog来显示时间选择器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择时间");

        // 获取NumberPicker控件
        final NumberPicker hourPicker = new NumberPicker(this);
        final NumberPicker minutePicker = new NumberPicker(this);

        // 设置小时的数据源
        final String[] hours = new String[]{"0", "1", "2", "3", "4"};
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(hours.length - 1);
        hourPicker.setDisplayedValues(hours);

        // 设置分钟的数据源
        final String[] minutes = new String[]{"10", "20", "30", "40", "50"};
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(minutes.length - 1);
        minutePicker.setDisplayedValues(minutes);

        // 创建一个LinearLayout来包装两个NumberPicker
        LinearLayout pickerLayout = new LinearLayout(this);
        pickerLayout.setOrientation(LinearLayout.HORIZONTAL);
        pickerLayout.addView(hourPicker);
        pickerLayout.addView(minutePicker);

        // 设置权重以确保两列滚轮均匀占用空间
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.weight = 1;
        hourPicker.setLayoutParams(params);
        minutePicker.setLayoutParams(params);

        builder.setView(pickerLayout);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在点击确定后获取选择的小时和分钟值并更新UI
                String selectedHour = hours[hourPicker.getValue()];
                String selectedMinute = minutes[minutePicker.getValue()];
                timeMinute=(minutePicker.getValue()+1)*10;
                timeHour=hourPicker.getValue()*10;

                String selectedTime = selectedHour + "小时" + selectedMinute + "分钟";
                deadlineTextView.setText(selectedTime);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户取消选择
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
        this.finish(); // 关闭当前活动
    }
}