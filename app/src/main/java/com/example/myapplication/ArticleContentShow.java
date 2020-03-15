package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Http.OkHttpUtil;
import com.example.myapplication.login.HttpInterceptor;
import com.example.myapplication.login.LoggingInterceptor;
import com.example.myapplication.window.FloatingService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ArticleContentShow extends AppCompatActivity {
    private WebView webView = null;
    private ImageView image_mistake = null;
    private TextView text_mistake1 = null;
    private TextView text_mistake2 = null;
    private ProgressDialog dialog = null;

    private Intent data;
    private String share_url;
    private Toolbar toolbar;
    private String text;
    public static  String out_put;
    public static  String response_print;

    private FloatingService.ConnectBinder connectBinder;
    private ServiceConnection connect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connectBinder = (FloatingService.ConnectBinder) service;
            connectBinder.changeReceyerView(out_put+";"+response_print);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content_show);
        toolbar = (Toolbar) findViewById(R.id.contentToolbar);
        toolbar.setTitle("同花顺资讯");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        initViews();

        // 启用支持JavaScript
        WebSettings webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);// 应用可以有缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAppCacheEnabled(true);// 缓存最多可以有10M
        // 优先使用缓存优化效率
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        image_mistake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initViews();
            }
        });
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            String html2 = html.substring(100,800);
           text = html2.substring(html2.indexOf("src=")+5,html2.indexOf(".js")+3)+" "+html2.substring(html2.substring(0,html2.indexOf("shtml")).lastIndexOf("http"),html2.indexOf("shtml")+5);
            if (FloatingService.isStarted){
                Intent bindtent = new Intent(ArticleContentShow.this , FloatingService.class);
                bindService(bindtent,connect, Context.BIND_AUTO_CREATE);

            }
            //     String output1 = html.substring(html.indexOf("src=")+4,html.indexOf(".js"+3));
        }
    }

    private void initViews() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        webView = (WebView) findViewById(R.id.webView);
        image_mistake = (ImageView) findViewById(R.id.mistake);
        text_mistake1= (TextView) findViewById(R.id.mistake_text1);
        text_mistake2= (TextView) findViewById(R.id.mistake_text1);
        data = getIntent();
        share_url = data.getStringExtra("url");
        webView.setWebViewClient(new WebViewClient() {
            private boolean loadError = false;
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains("html")){

                    OkHttpUtil.doGet(url, new Callback() {// 新建异步请求的回调函数
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("LoginActivity", "request Failed ; " + e.getLocalizedMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.isSuccessful()) {
                                response_print = response.body().toString();
                                if (FloatingService.isStarted){
                                    Intent bindtent = new Intent(ArticleContentShow.this , FloatingService.class);
                                    bindService(bindtent,connect, Context.BIND_AUTO_CREATE);
                                    unbindService(connect);

                                }
                                Log.e("LoginActivity", "--->"+response_print);

                            } else {
                                Log.e("LoginActivity", "onResponse failed");
                            }
                        }
                    });
                }
                return null;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                super.onReceivedError(view, errorCode, description, failingUrl);
                view.setVisibility(View.GONE);
                image_mistake.setVisibility(View.VISIBLE);
                text_mistake1.setVisibility(View.VISIBLE);
                text_mistake2.setVisibility(View.VISIBLE);

                loadError = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);");
                super.onPageFinished(view, url);
                if (loadError != true) {
                    image_mistake.setVisibility(View.GONE);
                    text_mistake1.setVisibility(View.GONE);
                    text_mistake2.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }


            }
        });
        webView.loadUrl(share_url);
    }
    private void testAsyncGetRequest(String url) {
   //     HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new LoggingInterceptor());
    //    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HttpInterceptor())
     //           .addNetworkInterceptor(logInterceptor)
                .build();//新建一个okhttpClient对象，并且设置拦截器
        Request request = new Request.Builder()
                .url(url)
                .build();//新建Request对象
        Callback callback = new Callback() {// 新建异步请求的回调函数
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LoginActivity", "request Failed ; " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    response_print = response.body().toString();
                    if (FloatingService.isStarted){
                        Intent bindtent = new Intent(ArticleContentShow.this , FloatingService.class);
                        bindService(bindtent,connect, Context.BIND_AUTO_CREATE);
                        unbindService(connect);

                    }
                    Log.e("LoginActivity", "--->"+response_print);

                } else {
                    Log.e("LoginActivity", "onResponse failed");
                }
            }
        };
        okHttpClient.newCall(request).enqueue(callback);//用okhttpClient执行request，并且注册回调函数

    }
}
