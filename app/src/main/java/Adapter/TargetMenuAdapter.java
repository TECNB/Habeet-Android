package Adapter;

import static com.example.habeet_android.HomeActivity.userEmail;
import static com.example.habeet_android.LoginActivity.userPoint;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habeet_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import Item.TargetItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TargetMenuAdapter extends RecyclerView.Adapter<TargetMenuAdapter.ViewHolder> {
    private List<TargetItem> targetMenuItemList;

    private String targetPoint;

    private TextView navPointTextView;

    public TargetMenuAdapter(List<TargetItem> targetMenuItemList) {
        this.targetMenuItemList = targetMenuItemList;
    }

    @NonNull
    @Override
    public TargetMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target_menu, parent, false);
        return new TargetMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TargetMenuAdapter.ViewHolder holder, int position) {
        TargetItem targetMenuItem = targetMenuItemList.get(position);

        System.out.println(targetMenuItem.getTargetName());
        holder.targetMenuNameTextView.setText(targetMenuItem.getTargetName());
        holder.targetMenuPointTextView.setText("X"+targetMenuItem.getTargetPoint());

        String dayDifference = targetMenuItem.getDayDifference();
        if (dayDifference.contains("天")) {
            holder.targetMenuDeadlineTextView.setText("还有" + dayDifference);
        } else {
            holder.targetMenuDeadlineTextView.setText(dayDifference);
        }


        holder.targetMenuDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTarget(targetMenuItem, holder.getAdapterPosition(), holder.itemView.getContext());
            }
        });
    }

    private void finishTarget(TargetItem targetItem, int position, Context context) {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/target/delete";
        System.out.println("targetId:"+targetItem.getTargetId());
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", userEmail);
            requestData.put("targetName", targetItem.getTargetName());
            requestData.put("ifPoints", 1);
            requestData.put("targetId", Long.valueOf(targetItem.getTargetId()));

            targetPoint=targetItem.getTargetPoint();
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
                    // 从数据源中删除项
                    targetMenuItemList.remove(position);
                    userPoint = String.valueOf(Integer.parseInt(userPoint) + Integer.parseInt(targetPoint));

                    // 通知适配器删除了特定位置的项
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRemoved(position);
                        }
                    });

                    // 通知适配器更新从删除位置到列表末尾的所有项
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRangeChanged(position, targetMenuItemList.size());
                            navPointTextView.setText(userPoint);
                            Toast.makeText(context.getApplicationContext(), "完成目标", Toast.LENGTH_SHORT).show();
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
    @Override
    public int getItemCount() {
        return targetMenuItemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView targetMenuNameTextView;
        TextView targetMenuPointTextView;
        TextView targetMenuDeadlineTextView;

        LinearLayout targetMenuDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetMenuNameTextView = itemView.findViewById(R.id.timeTargetName);
            targetMenuPointTextView = itemView.findViewById(R.id.timeTargetPoint);
            targetMenuDeadlineTextView = itemView.findViewById(R.id.timeTargetDeadline);


            targetMenuDetail = itemView.findViewById(R.id.timeTargetDetail);

            // 获取activity_nav.xml中的根布局
            View Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);

            navPointTextView = Nav.findViewById(R.id.navPointTextView);
        }
    }
}
