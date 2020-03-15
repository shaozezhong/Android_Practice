package com.example.myapplication.Http;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.login.HttpInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


    public class OkHttpUtil {

    private static OkHttpClient okHttpClient = null;

    private OkHttpUtil() {
    }
    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            //加同步安全
            synchronized (OkHttpUtil.class)  {
                if (okHttpClient == null)  {
                    okHttpClient =new OkHttpClient()
                            .newBuilder()
                            .addInterceptor(new HttpInterceptor())
                            .build();//新建一个okhttpClient对象，并且设置拦截器
                }
            }
        }
        return okHttpClient;
    }
    public static void doGet(String url, Callback callback) {

        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = new Request.Builder().url(url).build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);
    }

    public static void doPost(String url, Map<String, String> params, Callback callback) {

        OkHttpClient okHttpClient = getInstance();
        FormBody.Builder builder = new FormBody.Builder();
        //遍历集合
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));

        }
        //创建Request
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

}
