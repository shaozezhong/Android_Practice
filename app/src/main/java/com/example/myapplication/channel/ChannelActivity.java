package com.example.myapplication.channel;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChannelActivity extends AppCompatActivity implements ChannelAdapter.OnStartDragListener {
    private RecyclerView channelRecyler;
    private ChannelAdapter mAdapter;
    private ItemTouchHelper mTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_activity);
        channelRecyler = (RecyclerView) findViewById(R.id.channl_recyler);
        mAdapter = new ChannelAdapter(this,getDatas(),channelRecyler);
        GridLayoutManager manager=new GridLayoutManager(this,4);
        //表示一个item的跨度，跨度了多少个span
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                if(itemViewType==mAdapter.CHANNEL_TITLE){
                    return 4;
                }
                return 1;
            }
        });
        channelRecyler.setLayoutManager(manager);
        channelRecyler.setAdapter(mAdapter);
        //创建ItemTouchHelper
        mTouchHelper = new ItemTouchHelper(new ToucheCallBcak(mAdapter));
        //attach到RecyclerView中
        mTouchHelper.attachToRecyclerView(channelRecyler);
        mAdapter.setOnStartDragListener(this);
    }

    private List<ChannelBean> getDatas(){
        List<ChannelBean> beanList=new ArrayList<>();
        beanList.add(new ChannelBean("我的频道",1));

        beanList.add(new ChannelBean("要闻",2));
        beanList.add(new ChannelBean("外汇",2));
        beanList.add(new ChannelBean("宏观",2));
        beanList.add(new ChannelBean("自选",2));
        beanList.add(new ChannelBean("基金",2));
        beanList.add(new ChannelBean("主题投资",2));
        beanList.add(new ChannelBean("日历",2));
        beanList.add(new ChannelBean("互动掘金",2));
        beanList.add(new ChannelBean("大盘",2));
        beanList.add(new ChannelBean("操盘必读",2));

        beanList.add(new ChannelBean("其他频道",1));

        beanList.add(new ChannelBean("快讯",3));
        beanList.add(new ChannelBean("机会",3));
        beanList.add(new ChannelBean("研报",3));
        beanList.add(new ChannelBean("证券",3));
        beanList.add(new ChannelBean("公司",3));
        beanList.add(new ChannelBean("美股",3));
        beanList.add(new ChannelBean("港股",3));
        beanList.add(new ChannelBean("债券",3));
        beanList.add(new ChannelBean("期货",3));
        beanList.add(new ChannelBean("黄金",3));

        return beanList;

    }

    @Override
    public void startDrag(RecyclerView.ViewHolder holder) {
        mTouchHelper.startDrag(holder);
    }
}
