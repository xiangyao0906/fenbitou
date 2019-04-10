package com.yizhilu.Exam.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public abstract class StringEntityCallback extends Callback<StringEntity> {
    @Override
    public StringEntity parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        StringEntity stringEntity = new Gson().fromJson(string, StringEntity.class);
        return stringEntity;
    }
}
