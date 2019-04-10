package com.yizhilu.community.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yizhilu.eduapp.R;

/**
 * Created by xiangyao on 2017/8/24.
 */

public class GroupDetailsMemberViewHolder extends RecyclerView.ViewHolder {
    public ImageView group_members_image;

    public GroupDetailsMemberViewHolder(View itemView) {
        super(itemView);
        group_members_image = (ImageView) itemView.findViewById(R.id.group_members_image);
    }
}
