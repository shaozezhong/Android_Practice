package com.example.myapplication.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Bottom;
import com.example.myapplication.Http.OkHttpUtil;
import com.example.myapplication.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private MyProgressbar progressbar;
    private View mInputLayout;
    private View progress;
    private static final String URL = "https://10jqka.com.cn/upass/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

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
                String name = login_name.getText().toString().trim();
                String pass = pass_word.getText().toString().trim();
                if (name.length() == 0)
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.length() == 0)
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    else {
                        Map<String,String> map =  new HashMap<String, String>();
                               map.put("username", name);
                               map.put("password", pass);
                        OkHttpUtil.doPost(URL,map, new Callback() {// 新建异步请求的回调函数
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("LoginActivity", "request Failed ; " + e.getLocalizedMessage());
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String result = response.body().string();
                                    if(result.equals("true")){
                                        //换成进度条
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                mInputLayout.setVisibility(View.INVISIBLE);
                                                progress.setVisibility(View.VISIBLE);
                                                progressbar.setProgress(100, 100);
                                            }
                                        });
                                        try{
                                            Thread.sleep(1500);
                                        }catch (InterruptedException e) {}
                                                Intent intent = new Intent(LoginActivity.this,Bottom.class);
                                                startActivity(intent);
                                                finish();
                                                Looper.prepare();
                                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                                Looper.loop();

                                    }
                                    if (result.equals("false")){
                                        Looper.prepare();
                                        Toast.makeText(LoginActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                    Log.e("LoginActivity", "onResponse:" + result);
                                } else {
                                    Log.e("LoginActivity", "onResponse failed");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
   /* private void testAsyncGetRequest(String name ,String password) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HttpInterceptor())
                .build();//新建一个okhttpClient对象，并且设置拦截器
        RequestBody body = new FormBody.Builder()
                .add("username", name)
                .add("password", password).build();

        Request request = new Request.Builder()
                .url("https://10jqka.com.cn/upass/api/login")
                .post(body)
                .build();//新建Request对象
        Callback callback = new Callback() {// 新建异步请求的回调函数
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("LoginActivity", "request Failed ; " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                     String result = response.body().string();
                     if(result.equals("true")){
                         Intent intent = new Intent(LoginActivity.this,Bottom.class);
                         startActivity(intent);
                         finish();
                     }
                    Log.e("LoginActivity", "onResponse:" + result);
                } else {
                    Log.e("LoginActivity", "onResponse failed");
                }
            }
        };
        okHttpClient.newCall(request).enqueue(callback);//用okhttpClient执行request，并且注册回调函数

    }*/
    public void init(){
        progressbar = findViewById(R.id.progressBar2);
        login_name = (EditText) findViewById(R.id.et_zh);
        pass_word = (EditText) findViewById(R.id.et_mima);
        btn_login = (TextView) findViewById(R.id.main_btn_login);
        sms = (TextView) findViewById(R.id.textView26);
        mInputLayout = findViewById(R.id.input_layout);
        progress = findViewById(R.id.layout_progress);
        login_name.setText("admin");
        pass_word.setText("123");
    }
}
