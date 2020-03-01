package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myapplication.Interface.OnItemClickListener;
import com.example.myapplication.allbean.RecommendBean;
import com.example.myapplication.window.FloatingService;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.MediaType;


public class MainFragment extends Fragment {
    private static final String TAG = "LoadMore";
    private MainFragment.MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Handler handler;
    //方案2
    public   List<RecommendBean> newList;
    private RecommendAdapter adapter;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecommendBean mainAdapterBean;
    private String url_next="";

    private List<Integer> listData = new ArrayList<>();
    private int count = 0;
    public static  OnLoadMoreListener mOnLoadMoreListener;
 //   public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private FloatingService.ConnectBinder connectBinder;
    private ServiceConnection connect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connectBinder = (FloatingService.ConnectBinder) service;
            connectBinder.changeReceyerView("你好");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main2, container, false);
        initView(view);
 //       init(view);
    //    httpTransfer();
        seeRefresh();
        return view;
    }
    //设置刷新
    public void seeRefresh() {
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        //监听刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()  {
            @Override
            public void onRefresh()  {
                Intent bindtent = new Intent(getActivity(),FloatingService.class);
                getActivity().bindService(bindtent,connect, Context.BIND_AUTO_CREATE);
        //        createThread();
          //      Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void initView(View view2) {
        newList = new ArrayList<RecommendBean>();
        adapter = new RecommendAdapter(newList, getContext());
        rv = (RecyclerView) view2.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view2.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setRefreshing(true);
        createThread();
        handler = new Handler();
        mOnLoadMoreListener=new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNextData();
                    }
                }, 2000);
            }
        };
        rv.addOnScrollListener(mOnLoadMoreListener);
       // seeRefresh();
    }

    public void getNextData(){
            OkHttpUtils
                    .get()
                    .url(url_next)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(getActivity(),
                                    "请求失败", Toast.LENGTH_LONG).show();
                        }

                        public void onResponse(String response) {
                            String[] a = response.split(",");
                            url_next = a[0].substring(13,a[0].length()-1);
                            Message msg = new Message();
                            msg.obj = response.trim();
                            handler3.sendMessage(msg);
                            //   Log.e("response",response);
                        }
                    });
    }
    Handler handler3 = new Handler()  {
        public void handleMessage(Message msg) {
            String str = msg.obj + "";
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ary = root.getJSONArray("pageItems");
                for (int i = 0; i < ary.length() - 1; i++) {
                    JSONObject root1 = ary.getJSONObject(i);
                    mainAdapterBean = new RecommendBean();
                    mainAdapterBean.setTitle(root1.optString("title"));
                    mainAdapterBean.setComment_couont(root1.optString("hot"));
                    mainAdapterBean.setTime(root1.optString("ctime"));
                    mainAdapterBean.setShare_url(root1.optString("url"));
                    mainAdapterBean.setImg(root1.optString("pic"));
                    newList.add( mainAdapterBean);
                }

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //创建进程
    public void createThread() {
        new Thread() {
            public void run() {
                httpTransfer();
            }
        }.start();
    }
    //数据处理
    Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            String str = msg.obj + "";
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ary = root.getJSONArray("pageItems");
                for (int i = 0; i < ary.length() - 1; i++) {
                    JSONObject root1 = ary.getJSONObject(i);
                    mainAdapterBean = new RecommendBean();
                    mainAdapterBean.setTitle(root1.optString("title"));
                    mainAdapterBean.setComment_couont(root1.optString("hot"));
                    mainAdapterBean.setTime(root1.optString("ctime"));
                    mainAdapterBean.setShare_url(root1.optString("url"));
                 //   mainAdapterBean.setGroup_id(root1.optString("source_url"));
                        mainAdapterBean.setImg(root1.optString("pic"));
                    newList.add(0, mainAdapterBean);
                }
                adapter.add(newList);
                //设置点击事件
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(getContext(), ArticleContentShow.class);
                        i.putExtra("url", newList.get(position).getShare_url());
                        startActivity(i);
                    }
                });
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    protected void httpTransfer() {
        OkHttpUtils
                .get()
                .url("https://m.10jqka.com.cn/gsrd/V2/todayhot_1.json")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),
                                "请求失败", Toast.LENGTH_LONG).show();
                    }

                    public void onResponse(String response) {
                        String[] a = response.split(",");
                        url_next = a[0].substring(13,a[0].length()-1);
                        Message msg = new Message();
                        msg.obj = response.trim();
                        handler2.sendMessage(msg);
                     //   Log.e("response",response);
                    }
                });
    }

    private void init(View view2) {
        myAdapter = new MainFragment.MyAdapter();
        handler = new Handler();
        layoutManager = new LinearLayoutManager(getActivity());

        refreshLayout = view2.findViewById(R.id.swiperefreshlayout);
        recyclerView = view2.findViewById(R.id.recyclerview);

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
            for (int i = 0; i < 3; i++) {
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
            Toast.makeText(getActivity(),"刷新完毕", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "加载完毕", Toast.LENGTH_SHORT).show();
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
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_foot, parent, false);
                return new MainFragment.FootViewHolder(view);
            } else {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_item, parent, false);
                MainFragment.NewsViewHolder myViewHolder = new MainFragment.NewsViewHolder(view);
                return myViewHolder;
            }
        }
        //对recyclerview子项的数据进行赋值
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
            } else {
                MainFragment.NewsViewHolder viewHolder = (MainFragment.NewsViewHolder) holder;
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
