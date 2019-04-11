package com.fenbitou.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenbitou.community.adapter.viewholder.HotopicViewHolder;
import com.fenbitou.community.interfaceUtils.OnSingleItemClicListener;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;

import java.util.List;

/**
 * Created by Administrator on 2017/08/30.
 */

public class HotopicAdapter extends RecyclerView.Adapter<HotopicViewHolder> {
    private Context context;
    private List<EntityPublic> myTopicList;// 我的话题集合


    private OnSingleItemClicListener onItemClickListener;


    public void setOnItemClickListener(OnSingleItemClicListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public HotopicAdapter(Context context, List<EntityPublic> myTopicList) {
        this.context = context;
        this.myTopicList = myTopicList;
    }


    @Override
    public HotopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotopicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_topic_list, null));
    }

    @Override
    public void onBindViewHolder(HotopicViewHolder holder, final int position) {
        holder.new_topic.setVisibility(View.VISIBLE);
        holder.checking.setVisibility(View.VISIBLE);
        if (myTopicList.get(position).getIfAudit() == 1) {
            holder.checking.setText("审");
            holder.checking.setBackground(context.getResources().getDrawable(R.drawable.text_red_bg_solid_frame));
        } else if (myTopicList.get(position).getIfAudit() == 3) {
            holder.checking.setText("驳回");
            holder.checking.setBackground(context.getResources().getDrawable(R.drawable.text_gray_bg_solid_frame));
        } else if (myTopicList.get(position).getIfAudit() == 4) {
            holder.checking.setText("冻结");
            holder.checking.setBackground(context.getResources().getDrawable(R.drawable.text_gray_bg_solid_frame));
        }
        if (myTopicList.get(position).getTop() == 1) {
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.top.setVisibility(View.GONE);
        }
        if (myTopicList.get(position).getEssence() == 1) {
            holder.essence.setVisibility(View.VISIBLE);
        } else {
            holder.essence.setVisibility(View.GONE);
        }
        if (myTopicList.get(position).getFiery() == 1) {
            holder.fiery.setVisibility(View.VISIBLE);
        } else {
            holder.fiery.setVisibility(View.GONE);
        }

        holder.layout.setIsShowAll(false);
        holder.layout.setUrlList(myTopicList.get(position).getHtmlImagesList());
        holder.alllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

        holder.my_topic_title.setText(myTopicList.get(position).getTitle());
        holder.my_topic_content.setText(Html.fromHtml(myTopicList.get(position).getContent()));
        holder.my_topic_comment.setText(String.valueOf(myTopicList.get(position).getCommentCounts()));
        holder.my_topic_praise.setText(String.valueOf(myTopicList.get(position).getPraiseCounts()));
        holder.my_topic_browse.setText(String.valueOf(myTopicList.get(position).getBrowseCounts()));
    }

    @Override
    public int getItemCount() {
        return myTopicList.size();
    }

    public List<EntityPublic> getMyTopicList() {
        return myTopicList;
    }

    public void setMyTopicList(List<EntityPublic> myTopicList) {
        this.myTopicList = myTopicList;
    }
}
