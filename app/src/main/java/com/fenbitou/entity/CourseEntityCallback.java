package com.fenbitou.entity;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;


public abstract class CourseEntityCallback extends Callback<CourseEntity> {
    @Override
    public CourseEntity parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();
        CourseEntity publicEntity = new Gson().fromJson(string, CourseEntity.class);
        return publicEntity;
    }


}
