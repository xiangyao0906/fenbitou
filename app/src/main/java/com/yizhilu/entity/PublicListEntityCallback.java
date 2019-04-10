package com.yizhilu.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class PublicListEntityCallback extends Callback<PublicListEntity>
{
    @Override
    public PublicListEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        PublicListEntity publicListEntity = new Gson().fromJson(string, PublicListEntity.class);
        return publicListEntity;
    }


}
