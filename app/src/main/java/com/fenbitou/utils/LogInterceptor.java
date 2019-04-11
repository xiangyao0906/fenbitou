package com.fenbitou.utils;


import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * OKHttp 日志拦截器
 */
public class LogInterceptor implements Interceptor {

    private boolean showResponse; //是否显示返回json

    public LogInterceptor(boolean showResponse) {
        this.showResponse = showResponse;
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String method=request.method();
        if("POST".equals(method)){
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Log.i("demo","POST:"+request.url()+"?"+buffer.readUtf8());
            }
        }else {
            Log.i("demo","GET:"+request.url());
        }

        Response response = chain.proceed(request);
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();

        if (showResponse) {
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)){
                        String resp = body.string();
                        if (mediaType.subtype() != null && mediaType.subtype().equals("json")){
                            Logger.json(resp);//返回json
                        }else {
                            Logger.d(resp);
                        }
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    }else {
                        Logger.d("responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            String subtype =mediaType.subtype();
            if ("json".equals(subtype)|| "xml".equals(subtype)
                    || "html".equals(subtype)|| "webviewhtml".equals(subtype)){
                return true;
            }
        }
        return false;
    }

}