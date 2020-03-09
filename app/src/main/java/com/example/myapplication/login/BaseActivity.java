package com.example.myapplication.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by szz on 2020/3/6.
 */

public class BaseActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityCollector.addActivity(this);//将活动添加到活动收集器
        }

        @Override
        protected void onDestroy(){
            super.onDestroy();
            ActivityCollector.removeActivity(this);//将活动移除活动收集器
        }
    }

