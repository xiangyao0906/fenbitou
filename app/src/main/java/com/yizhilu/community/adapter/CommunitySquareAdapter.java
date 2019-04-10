package com.yizhilu.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yizhilu.community.adapter.viewholder.CommunitySquareViewHolder;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.GlideUtil;
import com.yizhilu.community.interfaceUtils.OnTwoItemClickListener;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/23.
 */

public class CommunitySquareAdapter extends RecyclerView.Adapter<CommunitySquareViewHolder> {
    private List<EntityPublic> groupList;
    private Context context;
    private int userId;
    private OnTwoItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnTwoItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CommunitySquareAdapter(List<EntityPublic> groupList, Context context, int userId) {
        this.groupList = groupList;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public CommunitySquareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunitySquareViewHolder(LayoutInflater.from(context).inflate(R.layout.item_find_group_list, null));
    }

    @Override
    public void onBindViewHolder(CommunitySquareViewHolder holderView, final int position) {

        if (groupList.get(position).getCusId() == userId || groupList.get(position).getWhetherTheMembers() == 1) {
            holderView.join_btn.setText("已加入");
            holderView.join_btn.setTextColor(context.getResources().getColor(R.color.text_grayB1B));
            holderView.join_btn.setEnabled(false);
        } else {
            holderView.join_btn.setText("加入");
            holderView.join_btn.setTextColor(context.getResources().getColor(R.color.text_blue3F8));
            holderView.join_btn.setEnabled(true);
        }
        GlideUtil.loadCircleImage(context, Address.IMAGE + groupList.get(position).getImageUrl(), holderView.find_list_avatar);
        holderView.find_list_name.setText(groupList.get(position).getName());
        holderView.find_list_member.setText("成员" + groupList.get(position).getMemberNum() + "     话题" + groupList.get(position).getTopicCounts());
        holderView.join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemJoinClickListener(position);
            }
        });

        holderView.item_group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public List<EntityPublic> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<EntityPublic> groupList) {
        this.groupList = groupList;
    }
}
