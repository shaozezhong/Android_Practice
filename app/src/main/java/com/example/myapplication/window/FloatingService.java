package com.example.myapplication.window;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szz on 2020/2/28.
 */

public class FloatingService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private ImageView delete;
    private View displayView;
    private RecyclerView recyclerView;
    private List<String> data = new ArrayList(){};
    private RecyclerView.Adapter adapter;
//与活动进行通信
    private ConnectBinder mBinder = new ConnectBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 600;
        layoutParams.height = 600;
        layoutParams.x = 300;
        layoutParams.y = 300;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }


    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.float_view, null);
            displayView.setOnTouchListener(new FloatingOnTouchListener());
            delete = displayView.findViewById(R.id.delete);
            //列表
            data.add("  ");
            recyclerView = displayView.findViewById(R.id.float_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            initAdapter();
            recyclerView.setAdapter(adapter);

            windowManager.addView(displayView, layoutParams);
            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    windowManager.removeView(displayView);
                    isStarted = false;
                 //   onDestroy();
                }
            });
        }
    }
    private void initAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.float_list, parent, false);
                return new GoodsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                GoodsViewHolder viewHolder = (GoodsViewHolder) holder;
                viewHolder.testview.setText(data.get(position));
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        };
    }
    class GoodsViewHolder extends RecyclerView.ViewHolder {

        TextView testview;

        public GoodsViewHolder(final View itemView) {
            super(itemView);
            this.testview = (TextView) itemView.findViewById(R.id.text1);
        }
    };




    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
   public  class ConnectBinder extends Binder{
        public void changeReceyerView(String input){
            for(int i=0;i<2;i++){
                data.add(0,input.split(" ")[i]);
                adapter.notifyItemInserted(0);
                recyclerView.getLayoutManager().scrollToPosition(0);

            }

        }
    }
}
