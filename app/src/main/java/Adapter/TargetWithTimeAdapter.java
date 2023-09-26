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
import androidx.recyclerview.widget.RecyclerView;

import com.example.habeet_android.R;

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
    private int visibilityState1 = View.VISIBLE;
    private int visibilityState2 = View.GONE;

    public TargetWithTimeAdapter(List<TargetItem> targetItemList) {
        this.targetItemList = targetItemList;
        // 初始化可见性状态
        this.visibilityState1 = View.VISIBLE;
        this.visibilityState2 = View.GONE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target_withtime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TargetItem targetItem = targetItemList.get(position);
        System.out.println(targetItem.getTargetName());
        holder.targetNameTextView.setText(targetItem.getTargetName());
        holder.targetDescriptionTextView.setText(targetItem.getTargetDescribe());
        holder.targetWithTimePointTextView.setText("X"+targetItem.getTargetPoint());

        holder.navDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换可见性状态
                visibilityState1 = (visibilityState1 == View.GONE) ? View.VISIBLE : View.GONE;
                visibilityState2 = (visibilityState2 == View.GONE) ? View.VISIBLE : View.GONE;

                // 通知适配器数据已更改，以便刷新所有itemView
                notifyDataSetChanged();
            }
        });
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
        // 请求数据
        String requestData = targetItem.getTargetName();
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
        return targetItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView targetNameTextView;
        TextView targetDescriptionTextView;
        TextView targetWithTimePointTextView;

        // 获取activity_nav.xml中的根布局
        View Nav;
        ImageView navDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetNameTextView = itemView.findViewById(R.id.targetWithTimeName);
            targetDescriptionTextView = itemView.findViewById(R.id.targetWithTimeDescribe);
            targetWithTimePointTextView = itemView.findViewById(R.id.targetWithTimePoint);

            Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);
            navDelete = Nav.findViewById(R.id.navDelete);
        }
    }
}
