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

import java.io.IOException;
import java.util.List;

import Item.TagItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<TagItem> tagItemList;
    private int visibilityState1 = View.VISIBLE;
    private int visibilityState2 = View.GONE;

    public TagAdapter(List<TagItem> tagItemList) {
        this.tagItemList = tagItemList;
        // 初始化可见性状态
        this.visibilityState1 = View.VISIBLE;
        this.visibilityState2 = View.GONE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TagItem tagItem = tagItemList.get(position);
        System.out.println(tagItem.getTagName());
        holder.tagNameTextView.setText(tagItem.getTagName());
        holder.tagDescriptionTextView.setText(tagItem.getTagDescription());

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

        // 设置detailEdit的可见性状态
        holder.detailEdit.setVisibility(visibilityState1);
        // 设置detailDelete的可见性状态
        holder.detailDelete.setVisibility(visibilityState2);

        holder.detailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTag(tagItem, holder.getAdapterPosition(), holder.itemView.getContext());
            }
        });

        // 添加渐变动画效果
        if (visibilityState1 == View.VISIBLE) {
            animateView(holder.detailEdit, true);
        } else {
            animateView(holder.detailEdit, false);
        }

        if (visibilityState2 == View.VISIBLE) {
            animateView(holder.detailDelete, true);
        } else {
            animateView(holder.detailDelete, false);
        }
    }

    // 辅助方法来执行渐变动画
    private void animateView(View view, boolean show) {
        float startAlpha = show ? 0f : 1f;
        float endAlpha = show ? 1f : 0f;

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
        alphaAnimator.setDuration(300); // 设置动画持续时间

        alphaAnimator.start();
    }

    private void deleteTag(TagItem tagItem, int position, Context context) {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/tag/delete";
        // 请求数据
        String requestData = tagItem.getTagName();
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
                    tagItemList.remove(position);

                    // 通知适配器删除了特定位置的项
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRemoved(position);
                            Toast.makeText(context.getApplicationContext(), "删除标签成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // 通知适配器更新从删除位置到列表末尾的所有项
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRangeChanged(position, tagItemList.size());
                        }
                    });
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
    @Override
    public int getItemCount() {
        return tagItemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagNameTextView;
        TextView tagDescriptionTextView;

        // 获取activity_nav.xml中的根布局
        View Nav;
        ImageView navDelete;
        CardView detailEdit;
        CardView detailDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTextView = itemView.findViewById(R.id.tagName);
            tagDescriptionTextView = itemView.findViewById(R.id.tagDescription);

            Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);
            navDelete = Nav.findViewById(R.id.navDelete);
            detailEdit = itemView.findViewById(R.id.detailEdit);
            detailDelete = itemView.findViewById(R.id.detailDelete);
        }
    }
}
