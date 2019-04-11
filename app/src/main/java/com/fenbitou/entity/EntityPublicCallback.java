package com.fenbitou.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class EntityPublicCallback extends Callback<EntityPublic>
{
    @Override
    public EntityPublic parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        EntityPublic publicEntity = new Gson().fromJson(string, EntityPublic.class);
        return publicEntity;
    }


}
