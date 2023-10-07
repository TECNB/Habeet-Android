package Adapter;

import static com.example.habeet_android.MainActivity.timer;
import static com.example.habeet_android.MainActivity.timerTagNameTextView;
import static com.example.habeet_android.MainActivity.timerTextView;
import static com.example.habeet_android.MainActivity.tagPoint;
import static com.example.habeet_android.MainActivity.tagName;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habeet_android.R;

import java.util.List;

import Item.TagItem;

public class TagMenuAdapter extends RecyclerView.Adapter<TagMenuAdapter.ViewHolder>{
    
    private List<TagItem> tagMenuItemList;
    private String timeData;
    private String timeDescribe;
    private Boolean ifFirst=true;

    private int selectedPosition = 0; // 默认没有选中项


    public TagMenuAdapter(List<TagItem> tagMenuItemList) {
        this.tagMenuItemList = tagMenuItemList;
    }

    @NonNull
    @Override
    public TagMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_menu, parent, false);
        return new TagMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagMenuAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TagItem tagMenuItem = tagMenuItemList.get(position);
        System.out.println(tagMenuItem.getTagName());
        holder.tagMenuNameTextView.setText(tagMenuItem.getTagName());


        timeData=tagMenuItemList.get(0).getTagHour()+"小时"+tagMenuItemList.get(0).getTagMinute()+"分钟";
        timeDescribe=tagMenuItemList.get(0).getTagDescribe();
        tagName=tagMenuItemList.get(0).getTagName();


        if(ifFirst){

            holder.tagMenuTimeTextView.setText(timeData);
            holder.tagMenuDescribeTextView.setText(timeDescribe);
            Log.v("分钟",timeData);
        }



        if (position == selectedPosition) {
            holder.tagMenuLinearLayout.setBackgroundResource(R.color.danZi);
        } else {
            holder.tagMenuLinearLayout.setBackgroundResource(android.R.color.white);
        }

        holder.tagMenuCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeData = tagMenuItem.getTagHour() + "小时" + tagMenuItem.getTagMinute() + "分钟";

                String hour = tagMenuItem.getTagHour();
                String minute = tagMenuItem.getTagMinute();

                // 将字符串转换为整数
                int hourInt = Integer.parseInt(hour);
                int minuteInt = Integer.parseInt(minute);

                // 使用String.format来格式化时间
                timer = String.format("%02d:%02d:00", hourInt, minuteInt);
                tagName = tagMenuItem.getTagName();
                tagPoint=tagMenuItem.getTagPoint();

                ((Activity)  holder.itemView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerTextView.setText(timer);
                        timerTagNameTextView.setText(tagName);
                    }
                });



                Log.v("timeData", timeData);

                timeDescribe=tagMenuItem.getTagDescribe();
                holder.tagMenuDescribeTextView.setText(timeDescribe);

                // 更新选中的位置
                selectedPosition = position;

                // 通知适配器数据已更改，以便刷新所有itemView
                notifyDataSetChanged();
                holder.tagMenuTimeTextView.setText(timeData);

                ifFirst = false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return tagMenuItemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagMenuNameTextView;

        // 获取activity_nav.xml中的根布局
        CardView tagMenuDetail;
        CardView tagMenuCardView;
        TextView tagMenuTimeTextView;
        TextView tagMenuDescribeTextView;

        LinearLayout tagMenuLinearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagMenuNameTextView = itemView.findViewById(R.id.tagMenuName);
            tagMenuCardView = itemView.findViewById(R.id.tagMenuCardView);
            tagMenuLinearLayout = itemView.findViewById(R.id.tagMenuLinearLayout);

            tagMenuDetail = ((Activity) itemView.getContext()).findViewById(R.id.tagMenuDetail);
            tagMenuTimeTextView = tagMenuDetail.findViewById(R.id.tagMenuTime);
            tagMenuDescribeTextView = tagMenuDetail.findViewById(R.id.tagMenuDescribe);

            // 获取activity_nav.xml中的根布局
            View Nav = ((Activity) itemView.getContext()).findViewById(R.id.Nav);
        }
    }
}
