package com.fenbitou.base;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 *  Created by admin on 2017/6/30.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    private LayoutInflater mLayoutInflater;

    public Context mContext;

    private List<T> mList;

    public BaseAdapter(Context context, List<T> mList) {
        setContext(context);
        setData(mList);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }


    public Context getContext() {
        return mContext;
    }


    public void setContext(Context context) {
        this.mContext = context;
    }


    public List<T> getList() {
        return mList;
    }

    public void setData(List<T> mList) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
