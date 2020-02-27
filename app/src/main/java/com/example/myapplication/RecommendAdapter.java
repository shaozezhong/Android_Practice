package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.myapplication.Interface.OnItemClickListener;
import com.example.myapplication.allbean.RecommendBean;

import java.util.Collections;
import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter {
    private final static int TYPE_CONTENT = 0;//正常内容
    private final static int TYPE_FOOTER = 1;//加载View
    private List<RecommendBean> lists;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public RecommendAdapter(List<RecommendBean> lists, Context context) {
        this.lists = lists;
        this.context=context;
    }

    public void add(List<RecommendBean> newlist) {
        Collections.addAll(newlist);
    }

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //有图片的模板
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hotTitle;
        private TextView hotExtra;
        private ImageView hotImg1;

        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            hotTitle = (TextView) itemView.findViewById(R.id.hot_title);
            hotExtra = (TextView) itemView.findViewById(R.id.hot_extra);
            hotImg1 = (ImageView) itemView.findViewById(R.id.hot_image);

            //设置点击事件
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getLayoutPosition());
            }
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == lists.size())  {
            Log.e("TYPE_FOOTER",String.valueOf(TYPE_FOOTER));
            return TYPE_FOOTER;
        }
   //     Log.e("TYPE_FOOTER",String.valueOf(TYPE_CONTENT));

        return TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_foot, parent, false);
            return new FootViewHolder(view);

        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_home_has_image, parent, false);
            return new ViewHolder(view, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)  {
        if (getItemViewType(position) == TYPE_FOOTER) {
        } else {
            ViewHolder vh = (ViewHolder) holder;
            vh.hotTitle.setText(lists.get(position).getTitle());
            vh.hotExtra.setText(lists.get(position).getTime() + " - " + lists.get(position).getComment_couont() + "条评论");
            Log.e("pic_path", lists.get(position).getImg());
            //   Log.e("pic_path",lists.get(position).getSource() + " - " + lists.get(position).getComment_couont() + "条评论"+"-------");
            Glide.with(context)
                    .load(lists.get(position).getImg().trim() )
                 //   .placeholder(R.mipmap.ic_placeholder)
            //        .error(R.mipmap.ic_placeholder)
                    .into(vh.hotImg1);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size()+1;
    }
}
