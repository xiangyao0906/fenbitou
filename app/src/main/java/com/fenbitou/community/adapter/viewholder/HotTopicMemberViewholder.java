package com.fenbitou.community.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;

/**
 * Created by Administrator on 2017/08/29.
 */

public class HotTopicMemberViewholder extends RecyclerView.ViewHolder {
    public TextView hot_group_name;
    public ImageView hot_group_avatar;

    public HotTopicMemberViewholder(View itemView) {
        super(itemView);
        hot_group_name = (TextView) itemView.findViewById(R.id.hot_group_name);
        hot_group_avatar = (ImageView) itemView.findViewById(R.id.hot_group_avatar);
    }
}
