package com.yizhilu.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class PublicStringEntityCallback extends Callback<PublicStringEntity>
{
    @Override
    public PublicStringEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        PublicStringEntity publicListEntity = new Gson().fromJson(string, PublicStringEntity.class);
        return publicListEntity;
    }


}
