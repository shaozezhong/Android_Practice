package com.example.myapplication;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;



public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "LoadMore";
    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Handler handler;
    private List<Integer> listData = new ArrayList<>();
    private int count = 0;
    private OnLoadMoreListener mOnLoadMoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
  //      setSupportActionBar(toolbar);
        init();
    }
    /*public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }*/


    private void init() {
        myAdapter = new MyAdapter();
        handler = new Handler();
        layoutManager = new LinearLayoutManager(this);

        refreshLayout = findViewById(R.id.swiperefreshlayout);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        //设置下拉时圆圈的颜色（可以尤多种颜色拼成）
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        //设置下拉时圆圈的背景颜色（这里设置成白色）
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
      //设置下拉刷新时的操作
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()  {
            @Override
            public void onRefresh() {
                getData("refresh");
            }
        });
        mOnLoadMoreListener=new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData("loadMore");
                    }
                }, 3000);
            }
        };
        recyclerView.addOnScrollListener(mOnLoadMoreListener);

        getData("reset");
    }


    private void getData(final String type) {
        if ("reset".equals(type)) {
            listData.clear();
            count = 0;
            for (int i = 0; i < 10; i++) {
                count += 1;
                listData.add(count);
            }
        }
        else if ("refresh".equals(type)) {
            listData.clear();
            count = 0;
            for (int i = 0; i < 20; i++) {
                count += 1;
                listData.add(count);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                count += 1;
                listData.add(count);
            }
        }

        myAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if ("refresh".equals(type)) {
            Toast.makeText(getApplicationContext(),"刷新完毕", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "加载完毕", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CONTENT = 0;//正常内容
        private final static int TYPE_FOOTER = 1;//加载View

        @Override
        public int getItemViewType(int position) {
            if (position == listData.size() && mOnLoadMoreListener.isAllScreen())  {
                return TYPE_FOOTER;
            }
            return TYPE_CONTENT;
        }
//用于创建viewholder实例，把布局加载进来
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main_foot, parent, false);
                return new FootViewHolder(view);
            } else {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_item, parent, false);
                NewsViewHolder myViewHolder = new NewsViewHolder(view);
                return myViewHolder;
            }
        }
//对recyclerview子项的数据进行赋值
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
            } else {
                NewsViewHolder viewHolder = (NewsViewHolder) holder;
                viewHolder.newsTextView.setText("新闻  " + position);
            }
        }


        @Override
        public int getItemCount() {
            return listData.size() + 1;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
        }
    }
    private class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTextView;
        private ImageView newsImageView;
        private Button newsButton;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTextView = itemView.findViewById(R.id.title);
            newsImageView = itemView.findViewById(R.id.sItemIcon);
            newsButton = itemView.findViewById(R.id.view_btn);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
        }
    }
}

