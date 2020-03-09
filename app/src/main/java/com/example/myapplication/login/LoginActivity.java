package com.example.myapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Bottom;
import com.example.myapplication.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private EditText login_name;
    private EditText pass_word;
    private TextView btn_login;
    private TextView sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
         init();
        sms.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 登录监听事件
        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

             /*   new Thread() {
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("https://www.baidu.com")
                                .build();
                        try{
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()){
                                String data = response.body().string();
                                Log.e("LoginActivity",data);
                            }else {
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();*/





                String name = login_name.getText().toString().trim();
                String pass = pass_word.getText().toString().trim();
                if (name.length() == 0)
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.length() == 0)
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    else {
                        testAsyncGetRequest(name,pass);
                      /*  new Thread() {
                            public void run() {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("https://www.baidu.com")
                                        .build();
                                try{
                                    Response response = client.newCall(request).execute();
                                    if (response.isSuccessful()){
                                        String data = response.body().string();
                                        Log.e("LoginActivity",data);
                                    }else {
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }.start();*/
                   //     loginJudge(name,pass);

                    }
                }
            }
        });
    }
    private void testAsyncGetRequest(String name ,String password) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HttpInterceptor())
                .build();//新建一个okhttpClient对象，并且设置拦截器
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("password", password).build();

        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .post(body)
                .build();//新建Request对象
        Callback callback = new Callback() {// 新建异步请求的回调函数
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

                Log.e("LoginActivity", "request Failed ; " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    Intent intent = new Intent(LoginActivity.this,Bottom.class);
                    startActivity(intent);

                    Headers responseHeaders = response.headers();

                    int responseHeadersLength = responseHeaders.size();
                    for (int i = 0; i < responseHeadersLength; i++){
                        String headerName = responseHeaders.name(i);
                        String headerValue = responseHeaders.get(headerName);
                        System.out.print("TAG----------->Name:"+headerName+"------------>Value:"+headerValue+"\n");
                    }

                    Log.e("LoginActivity", "onResponse:" + response.body().string());
                } else {
                    Log.e("LoginActivity", "onResponse failed");
                }
            }
        };
        okHttpClient.newCall(request).enqueue(callback);//用okhttpClient执行request，并且注册回调函数

    }
    public void init(){
        login_name = (EditText) findViewById(R.id.et_zh);
        pass_word = (EditText) findViewById(R.id.et_mima);
        btn_login = (TextView) findViewById(R.id.main_btn_login);
        sms = (TextView) findViewById(R.id.textView26);


    }
    protected void loginJudge(final String userName, final String userPassword) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(100000, TimeUnit.SECONDS)
                .readTimeout(100000, TimeUnit.SECONDS)
                .writeTimeout(100000, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();;
        RequestBody requestBody = new FormBody.Builder()
                .add("username",userName)
                .add("password",userPassword)
                .build();
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .post(requestBody)
                .build();
        Callback callback = new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LoginActivity", "request Failed ; " + e.getLocalizedMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("LoginActivity", "onResponse:" + response.body().string());
                } else {
                    Log.e("LoginActivity", "onResponse failed");
                }
            }
        };
        okHttpClient.newCall(request).enqueue(callback);
   //     Response response = okHttpClient.newCall(request).execute();
    }
}
