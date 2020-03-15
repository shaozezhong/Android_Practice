package com.example.myapplication.login;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


import com.example.myapplication.ArticleContentShow;
import com.example.myapplication.window.FloatingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException{
        Response response = null;
        Response.Builder responseBuilder = new Response.Builder()
                .code(200)
                .message("")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .addHeader("content-type","application/json");
        Request request = chain.request();
        //拦截指定地址
        Log.e("LoginActivity",request.url().toString());
        if (request.url().toString().equals("https://10jqka.com.cn/upass/api/login")) {
            Map<String,String> map =  new HashMap<String, String>();
            FormBody formBody = (FormBody) request.body();
            String responseString ;
            for (int i = 0; i < formBody.size(); i++) {
                map.put(formBody.encodedName(i),formBody.encodedValue(i));
                Log.e("拦截器",formBody.encodedName(i)+"--->"+formBody.encodedValue(i));
            }
            if(map.get("username").equals("admin")&&map.get("password").equals("123")){
                responseString = "true";
            }else {
                responseString = "false";
            }

            responseBuilder.body(ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.getBytes()));
            response = responseBuilder.build();
        }else if ("GET".equals(request.method())){
            response = chain.proceed(request);
            Headers responseHeaders = response.headers();
            int responseHeadersLength = responseHeaders.size();
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < responseHeadersLength; i++){
                String headerName = responseHeaders.name(i);
                String headerValue = responseHeaders.get(headerName);
                output.append("Header_Name:"+headerName+"--->Value:"+headerValue+";");
            }
            Log.e("output_test",output.toString());

            ArticleContentShow.out_put = output.toString()+request.url().toString();
        }
          return response;
    }
}
