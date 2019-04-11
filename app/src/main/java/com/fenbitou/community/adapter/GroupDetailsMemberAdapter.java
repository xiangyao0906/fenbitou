package com.fenbitou.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fenbitou.community.adapter.viewholder.GroupDetailsMemberViewHolder;
import com.fenbitou.community.utils.Address;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.GlideUtil;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/24.
 */

public class GroupDetailsMemberAdapter extends RecyclerView.Adapter<GroupDetailsMemberViewHolder> {

    private Context context;
    private List<EntityPublic> groupMembers;
    private HEAD_URL head_url;

    public enum HEAD_URL {
        AVATAR, IMAGEURL;
    }

    public List<EntityPublic> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<EntityPublic> groupMembers) {

        this.groupMembers = groupMembers;
    }

    public GroupDetailsMemberAdapter(Context context, List<EntityPublic> groupMembers,HEAD_URL head_url) {
        this.context = context;
        this.groupMembers = groupMembers;
        this.head_url=head_url;
    }

    @Override
    public GroupDetailsMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupDetailsMemberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_members_list, null));
    }

    @Override
    public void onBindViewHolder(GroupDetailsMemberViewHolder holder, int position) {
       if(head_url.equals(HEAD_URL.AVATAR)){
           GlideUtil.loadCircleImage(context, Address.IMAGE + groupMembers.get(position).getAvatar(), holder.group_members_image);
       }else if(head_url.equals(HEAD_URL.IMAGEURL)){
           GlideUtil.loadCircleImage(context, Address.IMAGE + groupMembers.get(position).getImageUrl(), holder.group_members_image);
       }
    }

    @Override
    public int getItemCount() {
        return getnum();
    }


    //防止小组成员过多影响加载速度
    private int getnum() {
        return groupMembers.size() > 10 ? 10 : getGroupMembers().size();
    }
}
