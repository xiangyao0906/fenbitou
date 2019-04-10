package com.yizhilu.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yizhilu.community.Bean.Member;
import com.yizhilu.community.adapter.viewholder.MemberViewholder;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.GlideUtil;

import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<MemberViewholder> {

    private Context context;
    private List<Member> members;

    public GroupMemberAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public MemberViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item_layout, null);
        return new MemberViewholder(view);
    }

    @Override
    public void onBindViewHolder(MemberViewholder viewHolder, int position) {

        Member member = members.get(position);
        viewHolder.tvName.setText(member.getName());
        GlideUtil.loadCircleHeadImage(context, member.getAvatar(), viewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return members == null ? 0 : members.size();
    }

}
