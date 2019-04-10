package com.yizhilu.community.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yizhilu.eduapp.R;

/**
 * Created by xiangyao on 2017/8/24.
 */

public class HotSearchItemViewHolder extends RecyclerView.ViewHolder{
    public TextView titleText;
    public HotSearchItemViewHolder(View itemView) {
        super(itemView);
       titleText = (TextView) itemView.findViewById(R.id.tv_hotsearch);
    }
}
