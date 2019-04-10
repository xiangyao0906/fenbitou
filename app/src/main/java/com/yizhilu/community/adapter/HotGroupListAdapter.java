package com.yizhilu.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yizhilu.community.adapter.viewholder.HotTopicMemberViewholder;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.GlideUtil;

import java.util.List;

/**
 * Created by ming on 2016/6/24 18:32. Explain: 热点推荐小组适配器
 */
public class HotGroupListAdapter extends RecyclerView.Adapter<HotTopicMemberViewholder> {

    private List<EntityPublic> hotGroupList;
    private Context context;
    private OnItemClickLitener onItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public HotGroupListAdapter(List<EntityPublic> hotGroupList, Context context) {
        this.hotGroupList = hotGroupList;
        this.context = context;
    }

    @Override
    public HotTopicMemberViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotTopicMemberViewholder(
                LayoutInflater.from(context).inflate(R.layout.item_hot_group_list, null, false));
    }

    @Override
    public void onBindViewHolder(final HotTopicMemberViewholder holder, final int position) {
        holder.hot_group_name.setText(hotGroupList.get(position).getName());
        GlideUtil.loadCircleHeadImage(context, Address.IMAGE + hotGroupList.get(position).getImageUrl(), holder.hot_group_avatar);
    }

    @Override
    public int getItemCount() {
        return hotGroupList.size();
    }

}
