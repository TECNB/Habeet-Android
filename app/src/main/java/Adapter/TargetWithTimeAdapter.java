package Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class TargetWithTimeAdapter extends RecyclerView.Adapter<TargetWithTimeAdapter.ViewHolder> {

    private List<TargetItem> targetItemList;
    private int targetWithTimeVisibilityState1 = View.VISIBLE;
    private int targetWithTimeVisibilityState2 = View.GONE;

    public TargetWithTimeAdapter(List<TargetItem> targetItemList,int visibilityState1, int visibilityState2) {
        this.targetItemList = targetItemList;
        // 初始化可见性状态
        this.targetWithTimeVisibilityState1 = visibilityState1;
        this.targetWithTimeVisibilityState2 = visibilityState2;
    }
    @Override
    public int getItemViewType(int position) {
        if (targetItemList.size() == 0) {
            return 1; // 返回1表示空页面
        } else {
            return 0; // 返回0表示正常数据项
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target_withtime, parent, false);
            return new TargetWithTimeAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target_withtime_null, parent, false);
            return new TargetWithTimeAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (targetItemList.size() == 0) {
            // 当数据为空时，可以设置空页面的提示信息或样式

            // 可以设置其他空页面的样式或操作
        } else {
            TargetItem targetItem = targetItemList.get(position);
            System.out.println(targetItem.getTargetName());
            holder.targetNameTextView.setText(targetItem.getTargetName());
            holder.targetDescriptionTextView.setText(targetItem.getTargetDescribe());
            holder.targetWithTimePointTextView.setText("X"+targetItem.getTargetPoint());
            holder.targetWithTimeDayDifference.setText(targetItem.getDayDifference());

            // 设置detailEdit的可见性状态
            holder.targetWithTimePointCardView.setVisibility(targetWithTimeVisibilityState1);
            holder.targetWithTimeDayDifference.setVisibility(targetWithTimeVisibilityState1);
            // 设置detailDelete的可见性状态
            holder.targetWithTimeDelete.setVisibility(targetWithTimeVisibilityState2);

            holder.targetWithTimeDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTarget(targetItem, holder.getAdapterPosition(), holder.itemView.getContext());
                }
            });
            holder.targetWithTimePointCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTarget(targetItem, holder.getAdapterPosition(), holder.itemView.getContext());
                }
            });

            // 添加渐变动画效果
            if (targetWithTimeVisibilityState1 == View.VISIBLE) {
                animateView(holder.targetWithTimePointCardView, true);
                animateView(holder.targetWithTimeDayDifference, true);
            } else {
                animateView(holder.targetWithTimePointCardView, false);
                animateView(holder.targetWithTimeDayDifference, false);
            }

            if (targetWithTimeVisibilityState2 == View.VISIBLE) {
                animateView(holder.targetWithTimeDelete, true);
            } else {
                animateView(holder.targetWithTimeDelete, false);
            }
        }

    }

    public void updateVisibility(int visibilityState1, int visibilityState2) {
        targetWithTimeVisibilityState1 = visibilityState1;
        targetWithTimeVisibilityState2 = visibilityState2;
        notifyDataSetChanged();
    }


    // 辅助方法来执行渐变动画
    private void animateView(View view, boolean show) {
        float startAlpha = show ? 0f : 1f;
        float endAlpha = show ? 1f : 0f;

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
        alphaAnimator.setDuration(300); // 设置动画持续时间

        alphaAnimator.start();
    }

    private void deleteTarget(TargetItem targetItem, int position, Context context) {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/target/delete";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", "3489044730@qq.com");
            requestData.put("targetName", targetItem.getTargetName());
            requestData.put("ifPoints", 0);
            requestData.put("targetId", Long.valueOf(targetItem.getTargetId()));
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
                    targetItemList.remove(position);

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
                            notifyItemRangeChanged(position, targetItemList.size());
                        }
                    });
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
    private void finishTarget(TargetItem targetItem, int position, Context context) {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/target/delete";
        System.out.println("targetId:"+targetItem.getTargetId());
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("userEmail", "3489044730@qq.com");
            requestData.put("targetName", targetItem.getTargetName());
            requestData.put("ifPoints", 1);
            requestData.put("targetId", Long.valueOf(targetItem.getTargetId()));
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
                    targetItemList.remove(position);

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
                            notifyItemRangeChanged(position, targetItemList.size());
                        }
                    });
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

    @Override
    public int getItemCount() {
        if (targetItemList.size() == 0) {
            return 1; // 返回1项以显示空页面
        } else {
            return targetItemList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView targetNameTextView;
        TextView targetDescriptionTextView;
        TextView targetWithTimePointTextView;

        // 获取activity_nav.xml中的根布局
        View Nav;
        ImageView navDelete;
        CardView targetWithTimePointCardView;
        CardView targetWithTimeDelete;
        TextView targetWithTimeDayDifference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetNameTextView = itemView.findViewById(R.id.targetWithTimeName);
            targetDescriptionTextView = itemView.findViewById(R.id.targetWithTimeDescribe);
            targetWithTimePointTextView = itemView.findViewById(R.id.targetWithTimePoint);

            Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);
            navDelete = Nav.findViewById(R.id.navDelete);
            targetWithTimePointCardView = itemView.findViewById(R.id.targetWithTimePointCardView);
            targetWithTimeDelete = itemView.findViewById(R.id.targetWithTimeDelete);
            targetWithTimeDayDifference = itemView.findViewById(R.id.targetWithTimeDayDifference);
        }
    }
}
