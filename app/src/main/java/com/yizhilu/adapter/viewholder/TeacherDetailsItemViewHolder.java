package com.yizhilu.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.eduapp.R;

/**
 * Created by xiangyao on 2017/8/4.
 */

public class TeacherDetailsItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView courseImg;
    public TextView courseNmae, coursePrice, playNum;
    public LinearLayout recommend_freePrice, recommend_currentPrice;

    public TeacherDetailsItemViewHolder(View itemView) {
        super(itemView);
        courseImg = (ImageView) itemView.findViewById(R.id.lecturer_courseImage);
        courseNmae = (TextView) itemView.findViewById(R.id.lecturer_courseName);
        coursePrice = (TextView) itemView.findViewById(R.id.recommendMoney);
        playNum = (TextView) itemView.findViewById(R.id.lecturer_number);
        recommend_currentPrice = (LinearLayout) itemView.findViewById(R.id.recommend_currentPrice);
        recommend_freePrice = (LinearLayout) itemView.findViewById(R.id.recommend_freePrice);

    }

}
