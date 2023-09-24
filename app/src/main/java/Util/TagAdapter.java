package Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habeet_android.R;

import java.util.List;

import Item.TagItem;

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
                // 启动可见性动画
                animateVisibility(holder.detailEdit, visibilityState1);
                animateVisibility(holder.detailDelete, visibilityState2);

                // 通知适配器数据已更改，以便刷新所有itemView
                notifyDataSetChanged();
            }
        });

        // 设置detailEdit和detailDelete的可见性状态
        holder.detailEdit.setVisibility(visibilityState1);
        holder.detailDelete.setVisibility(visibilityState2);
    }

    private void animateVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
            ObjectAnimator animator;
            if (visibility == View.VISIBLE) {
                animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            } else {
                animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            }

            animator.setDuration(300); // 设置动画时长
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if (visibility == View.VISIBLE) {
                        view.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (visibility == View.GONE) {
                        view.setVisibility(View.GONE);
                    }
                }
            });

            animator.start();
        }
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

            Nav= ((Activity)itemView.getContext()).findViewById(R.id.Nav);
            navDelete = Nav.findViewById(R.id.navDelete);
            detailEdit = itemView.findViewById(R.id.detailEdit);
            detailDelete = itemView.findViewById(R.id.detailDelete);
        }
    }
}