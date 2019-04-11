package com.fenbitou.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by bishuang on 2017/8/15.
 * 直播的实体类
 */

public abstract  class PublicLiveEntityCallback extends Callback<LiveEntity>{
    @Override
    public LiveEntity parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        LiveEntity liveEntity = new Gson().fromJson(string, LiveEntity.class);
        return liveEntity;
    }

}