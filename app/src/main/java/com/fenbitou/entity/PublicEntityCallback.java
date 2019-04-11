package com.fenbitou.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class PublicEntityCallback extends Callback<PublicEntity>
{
    @Override
    public PublicEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        PublicEntity publicEntity = new Gson().fromJson(string, PublicEntity.class);
        return publicEntity;
    }


}
