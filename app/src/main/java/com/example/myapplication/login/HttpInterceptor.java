package com.example.myapplication.login;

import android.util.Log;


import com.example.myapplication.ArticleContentShow;

import java.io.IOException;

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
            String responseString = "true";
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
                output.append("Header_Name:"+headerName+"--->Value:"+headerValue+"<br>");
                //    responseString = responseString+"Header_Name:"+headerName+"------------>Value:"+headerValue+"\n";
          //      Log.e("output_test",output.toString());
                //   System.out.print(output.toString());
            }
            ArticleContentShow.out_put = output.toString();
       //     Log.e("output_test",ArticleContentShow.out_put );

        }
          return response;
    }
}
