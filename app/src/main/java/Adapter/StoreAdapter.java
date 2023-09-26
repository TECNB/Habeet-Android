package Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habeet_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import Item.StoreItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<StoreItem> storeItemList;

    public StoreAdapter(List<StoreItem> storeItemList) {
        this.storeItemList = storeItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreItem storeItem = storeItemList.get(position);
        System.out.println(storeItem.getStoreName());
        holder.storeNameTextView.setText(storeItem.getStoreName());
        holder.storeDescriptionTextView.setText(storeItem.getStoreDescribe());
        holder.storeHourTextView.setText(storeItem.getStoreHour());
        holder.storeMinuteTextView.setText(storeItem.getStoreMinute());
        holder.storePointTextView.setText("X"+storeItem.getStorePoint());

        holder.storeDeleteCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteStore(storeItem, holder.getAdapterPosition(), holder.itemView.getContext());
            }
        });
    }


    private void deleteStore(StoreItem storeItem, int position, Context context) {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/store/delete";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("storeName", storeItem.getStoreName());
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
                    storeItemList.remove(position);

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
                            notifyItemRangeChanged(position, storeItemList.size());
                        }
                    });
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

    @Override
    public int getItemCount() {
        return storeItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeNameTextView;
        TextView storeDescriptionTextView;
        TextView storePointTextView;
        TextView storeHourTextView;
        TextView storeMinuteTextView;

        CardView storeDeleteCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.storeName);
            storeDescriptionTextView = itemView.findViewById(R.id.storeDescribe);
            storePointTextView = itemView.findViewById(R.id.storePoint);
            storeHourTextView = itemView.findViewById(R.id.storeHour);
            storeMinuteTextView = itemView.findViewById(R.id.storeMinute);

            storeDeleteCardView=itemView.findViewById(R.id.storeDelete);
        }
    }
}
