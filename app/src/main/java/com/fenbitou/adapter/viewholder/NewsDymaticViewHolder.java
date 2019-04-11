package com.fenbitou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;

/**
 * Created by xiangyao on 2017/8/11.
 */

public class NewsDymaticViewHolder extends RecyclerView.ViewHolder {

    public ImageView articleImage;
    public TextView articleTitle, articleTime, articleNum;


    public NewsDymaticViewHolder(View itemView) {
        super(itemView);
        articleTitle = (TextView) itemView.findViewById(R.id.article_title);
        articleTime = (TextView) itemView.findViewById(R.id.article_time);
        articleNum = (TextView) itemView.findViewById(R.id.article_num);
        articleImage = (ImageView) itemView.findViewById(R.id.article_image);
    }
}
