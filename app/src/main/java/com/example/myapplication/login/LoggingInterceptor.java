package com.example.myapplication.login;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class LoggingInterceptor implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.e("HttpLogInfo", message);
    }
}