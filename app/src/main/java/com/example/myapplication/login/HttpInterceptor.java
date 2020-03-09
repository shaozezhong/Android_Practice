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
import java.util.List;

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
        if ("POST".equals(request.method())){
            List<String> login = new ArrayList<>();
            FormBody formBody = (FormBody) request.body();
            String responseString ;
            for (int i = 0; i < formBody.size(); i++) {
                login.add(formBody.encodedValue(i));
                Log.e("拦截器",formBody.encodedName(i)+"--->"+formBody.encodedValue(i));
            }
            if(login.get(0).equals("admin")&&login.get(1).equals("123")){
                 responseString = "true";
            }else {
                 responseString = "false";
            }

            responseBuilder.body(ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.getBytes()));
            response = responseBuilder.build();
        }else {
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
