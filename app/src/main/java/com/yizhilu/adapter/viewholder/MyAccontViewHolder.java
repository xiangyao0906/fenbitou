package com.yizhilu.adapter.viewholder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yizhilu.eduapp.R;

/**
 * Created by xiangyao on 2017/8/16.
 */

public class MyAccontViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_account_xiaofei, tv_account_time, tv_account_money;

    public MyAccontViewHolder(View itemView) {
        super(itemView);

        tv_account_xiaofei= (TextView) itemView.findViewById(R.id.tv_account_xiaofei);
        tv_account_time= (TextView) itemView.findViewById(R.id.tv_account_time);
        tv_account_money= (TextView) itemView.findViewById(R.id.tv_account_money);

    }
}
