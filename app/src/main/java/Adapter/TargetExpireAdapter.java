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
import android.widget.Toast;

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

public class TargetExpireAdapter extends RecyclerView.Adapter<TargetExpireAdapter.ViewHolder>{
    private List<TargetItem> targetItemList;
    private int targetExpireVisibilityState1 = View.VISIBLE;
    private int targetExpireVisibilityState2= View.GONE;

    public TargetExpireAdapter(List<TargetItem> targetItemList, int visibilityState1, int visibilityState2) {
        this.targetItemList = targetItemList;
        // 初始化可见性状态
        this.targetExpireVisibilityState1 = visibilityState1;
        this.targetExpireVisibilityState2 = visibilityState2;
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
    public TargetExpireAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target_expire, parent, false);
            return new TargetExpireAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target_expire_null, parent, false);
            return new TargetExpireAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TargetExpireAdapter.ViewHolder holder, int position) {
        if (targetItemList.size() == 0) {
            // 当数据为空时，可以设置空页面的提示信息或样式

            // 可以设置其他空页面的样式或操作
        } else {
            TargetItem targetItem = targetItemList.get(position);
            System.out.println(targetItem.getTargetName());
            holder.targetNameTextView.setText(targetItem.getTargetName());
            holder.targetDescriptionTextView.setText(targetItem.getTargetDescribe());
            holder.targetExpirePointTextView.setText("X"+targetItem.getTargetPoint());


            // 设置detailEdit的可见性状态
            holder.targetExpirePointCardView.setVisibility(targetExpireVisibilityState1);
            holder.targetExpireDayDifference.setVisibility(targetExpireVisibilityState1);
            // 设置detailDelete的可见性状态
            holder.targetExpireDelete.setVisibility(targetExpireVisibilityState2);

            holder.targetExpireDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTarget(targetItem, holder.getAdapterPosition(), holder.itemView.getContext());
                }
            });
            holder.targetExpirePointCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishTarget(targetItem, holder.getAdapterPosition(), holder.itemView.getContext());
                }
            });

            // 添加渐变动画效果
            if (targetExpireVisibilityState1 == View.VISIBLE) {
                animateView(holder.targetExpirePointCardView, true);
                animateView(holder.targetExpireDayDifference, true);
            } else {
                animateView(holder.targetExpirePointCardView, false);
                animateView(holder.targetExpireDayDifference, false);
            }

            if (targetExpireVisibilityState2 == View.VISIBLE) {
                animateView(holder.targetExpireDelete, true);
            } else {
                animateView(holder.targetExpireDelete, false);
            }
        }
    }
    public void updateVisibility(int visibilityState1, int visibilityState2) {
        targetExpireVisibilityState1 = visibilityState1;
        targetExpireVisibilityState2 = visibilityState2;
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
        System.out.println("targetId:"+targetItem.getTargetId());
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
                            Toast.makeText(context.getApplicationContext(), "删除目标成功", Toast.LENGTH_SHORT).show();
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
        TextView targetExpirePointTextView;

        // 获取activity_nav.xml中的根布局
        View Nav;
        ImageView navDelete;

        CardView targetExpirePointCardView;
        CardView targetExpireDelete;
        TextView targetExpireDayDifference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetNameTextView = itemView.findViewById(R.id.targetExpireName);
            targetDescriptionTextView = itemView.findViewById(R.id.targetExpireDescribe);
            targetExpirePointTextView = itemView.findViewById(R.id.targetExpirePoint);

            Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);
            navDelete = Nav.findViewById(R.id.navDelete);
            targetExpirePointCardView = itemView.findViewById(R.id.targetExpirePointCardView);
            targetExpireDelete = itemView.findViewById(R.id.targetExpireDelete);
            targetExpireDayDifference = itemView.findViewById(R.id.targetExpireDayDifference);
        }
    }
}
