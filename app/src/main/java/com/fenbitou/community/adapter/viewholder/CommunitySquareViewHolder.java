package com.fenbitou.community.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;

/**
 * Created by xiangyao on 2017/8/23.
 */

public class CommunitySquareViewHolder extends RecyclerView.ViewHolder {
    public ImageView find_list_avatar;
    public TextView find_list_name, find_list_member, find_list_topic_num, join_btn;
    public LinearLayout item_group_layout;

    public CommunitySquareViewHolder(View itemView) {
        super(itemView);
        find_list_avatar = (ImageView) itemView.findViewById(R.id.find_list_avatar);
        find_list_name = (TextView) itemView.findViewById(R.id.find_list_name);
        find_list_member = (TextView) itemView.findViewById(R.id.find_list_member);
        find_list_topic_num = (TextView) itemView.findViewById(R.id.find_list_topic_num);
        join_btn = (TextView) itemView.findViewById(R.id.join_btn);
        item_group_layout = (LinearLayout) itemView.findViewById(R.id.item_group_layout);
    }
}
