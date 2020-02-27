package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ArticleContentShow extends AppCompatActivity {
    private WebView webView = null;
    private ImageView image_mistake = null;
    private TextView text_mistake1 = null;
    private TextView text_mistake2 = null;
    private ProgressDialog dialog = null;

    private Intent data;
    private String share_url;
    private Toolbar toolbar;
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



    private void initViews() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        webView = (WebView) findViewById(R.id.webView);
        image_mistake = (ImageView) findViewById(R.id.mistake);
        text_mistake1= (TextView) findViewById(R.id.mistake_text1);
        text_mistake2= (TextView) findViewById(R.id.mistake_text1);
        data = getIntent();
        share_url = data.getStringExtra("url");
        webView.loadUrl(share_url);
        webView.setWebViewClient(new WebViewClient() {
            private boolean loadError = false;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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
                super.onPageFinished(view, url);
                if (loadError != true) {
                    image_mistake.setVisibility(View.GONE);
                    text_mistake1.setVisibility(View.GONE);
                    text_mistake2.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }


            }
        });
    }

}
