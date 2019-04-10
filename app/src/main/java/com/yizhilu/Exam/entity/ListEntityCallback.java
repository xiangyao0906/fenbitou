package com.yizhilu.Exam.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class ListEntityCallback extends Callback<com.yizhilu.Exam.entity.ListEntity>
{
    @Override
    public com.yizhilu.Exam.entity.ListEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        com.yizhilu.Exam.entity.ListEntity listEntity = new Gson().fromJson(string, ListEntity.class);
        return listEntity;
    }


}
