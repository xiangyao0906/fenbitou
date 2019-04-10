package com.yizhilu.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiangyao on 2017/12/18.
 * 缓存拦截器
 * 表示当访问此网页后的max-age秒内再次访问不会去服务器请求
 *
 */

public class CacheIntercptor implements Interceptor {
    Context context;

    public CacheIntercptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetWorkUtils.isNetworkAvailable(context)) {
            Response response = chain.proceed(request);
            int maxAge = 60;
            String cacheControl = request.cacheControl().toString();
            Log.e("CacheInterceptor", "6s load cahe" + cacheControl);
            return response.newBuilder().removeHeader("Pragma")
                    .removeHeader("Cache-Control").header("Cache-Control", "public, max-age=" + maxAge)
                    .build();

        } else {
            Log.e("CacheInterceptor", " no network load cahe");
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            Response response = chain.proceed(request);
            /**
             * three daus valueable
             * */
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder().removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
        }


    }
}
