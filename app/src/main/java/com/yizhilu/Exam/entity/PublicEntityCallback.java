package com.yizhilu.Exam.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class PublicEntityCallback extends Callback<com.yizhilu.Exam.entity.PublicEntity>
{
    @Override
    public com.yizhilu.Exam.entity.PublicEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        com.yizhilu.Exam.entity.PublicEntity publicEntity = new Gson().fromJson(string, com.yizhilu.Exam.entity.PublicEntity.class);
        return publicEntity;
    }


}
