package Adapter;

import static com.example.habeet_android.LoginActivity.userPoint;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView navPointTextView;
    private String storePoint;


    public StoreAdapter(List<StoreItem> storeItemList) {
        this.storeItemList = storeItemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (storeItemList.size() == 0) {
            return 1; // 返回1表示空页面
        } else {
            return 0; // 返回0表示正常数据项
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
            return new StoreAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_null, parent, false);
            return new StoreAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (storeItemList.size() == 0) {
            // 当数据为空时，可以设置空页面的提示信息或样式

            // 可以设置其他空页面的样式或操作
        } else {
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
    }


    private void deleteStore(StoreItem storeItem, int position, Context context) {
        OkHttpClient client = new OkHttpClient();
        // 请求URL
        String url = "https://tengenchang.top/store/delete";
        // 创建JSON对象
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("storeName", storeItem.getStoreName());
            storePoint=storeItem.getStorePoint();

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
                    // 合并为一个式子
                    userPoint = String.valueOf(Integer.parseInt(userPoint) - Integer.parseInt(storePoint));

                    // 通知适配器删除了特定位置的项
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context.getApplicationContext(), "兑换商品成功", Toast.LENGTH_SHORT).show();
                            navPointTextView.setText(userPoint);
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
        if (storeItemList.size() == 0) {
            return 1; // 返回1项以显示空页面
        } else {
            return storeItemList.size();
        }
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

            // 获取activity_nav.xml中的根布局
            View Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);

            navPointTextView = Nav.findViewById(R.id.navPointTextView);

            storeDeleteCardView=itemView.findViewById(R.id.storeDelete);
        }
    }
}
