package com.example.myapplication.login;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
        @Override
        public void onCreate() {//TODOAuto-generatedmethodstubsuper.
            super.onCreate();
            instance=this;}
            public static MyApplication getInstance(){
            //TODOAuto-generatedmethodstub
             return instance;}

        }