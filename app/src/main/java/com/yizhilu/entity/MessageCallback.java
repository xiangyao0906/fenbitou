package com.yizhilu.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class MessageCallback extends Callback<MessageEntity>
{
    @Override
    public MessageEntity parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        MessageEntity publicEntity = new Gson().fromJson(string, MessageEntity.class);
        return publicEntity;
    }


}
