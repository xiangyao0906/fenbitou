package com.yizhilu.community.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.eduapp.R;

/**
 * Created by Administrator on 2017/08/28.
 */

public class MemberViewholder extends RecyclerView.ViewHolder {
    public TextView tvName, tile;
    public ImageView avatar;

    public MemberViewholder(View itemView) {
        super(itemView);
//        tile = (TextView) itemView.findViewById(R.id.tv_sticky_header_view);
        tvName = (TextView) itemView.findViewById(R.id.members_name);
        avatar = (ImageView) itemView.findViewById(R.id.members_avatar);

    }
}
