package com.fenbitou.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenbitou.community.adapter.viewholder.TopicOfMyViewHolder;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;

import java.util.ArrayList;


/**
 * @author xiangyao
 * @date 2017/11/07
 */

public class TopicOfMyAdAdapter extends RecyclerView.Adapter<TopicOfMyViewHolder> {
    Context context;
    private ArrayList<EntityPublic> topList;

    public TopicOfMyAdAdapter(Context context, ArrayList<EntityPublic> topList) {
        this.context = context;
        this.topList = topList;
    }

    @Override
    public TopicOfMyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicOfMyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_topic_list, null, false));
    }

    @Override
    public void onBindViewHolder(TopicOfMyViewHolder holder, int position) {
        EntityPublic item = topList.get(position);

        holder.new_topic.setVisibility(View.VISIBLE);
        holder.checking.setVisibility(View.VISIBLE);

        switch (item.getIfAudit()) {
            case 1:
                holder.checking.setText("审");
                holder.checking.setBackground(context.getResources().getDrawable(R.drawable.text_red_bg_solid_frame));
                break;
            case 3:
                holder.checking.setText("驳回");
                holder.checking.setBackground(context.getResources().getDrawable(R.drawable.text_gray_bg_solid_frame));
                break;
            case 4:
                holder.checking.setText("冻结");
                holder.checking.setBackground(context.getResources().getDrawable(R.drawable.text_gray_bg_solid_frame));
                break;
        }

        if (item.getTop() == 1) {
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.top.setVisibility(View.GONE);
        }
        if (item.getEssence() == 1) {
            holder.essence.setVisibility(View.VISIBLE);
        } else {
            holder.essence.setVisibility(View.GONE);
        }
        if (item.getFiery() == 1) {
            holder.fiery.setVisibility(View.VISIBLE);
        } else {
            holder.fiery.setVisibility(View.GONE);
        }

        holder.layout.setIsShowAll(false);
        holder.layout.setUrlList(item.getHtmlImagesList());

        holder.my_topic_title.setText(item.getTitle());
        holder.my_topic_content.setText(Html.fromHtml(item.getContent()));
        holder.my_topic_comment.setText(String.valueOf(item.getCommentCounts()));
        holder.my_topic_praise.setText(String.valueOf(item.getPraiseCounts()));
        holder.my_topic_browse.setText(String.valueOf(item.getBrowseCounts()));
    }

    @Override
    public int getItemCount() {
        return topList.size();
    }
}
