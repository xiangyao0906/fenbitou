package com.yizhilu.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.yizhilu.community.BigImageActivity;
import com.yizhilu.community.utils.Address;
import com.yizhilu.eduapp.R;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.utils.GlideUtil;
import com.yizhilu.utils.StringUtil;
import com.yizhilu.widget.NineGridTestLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yjq on 2018/11/28.
 */

public class GroupDetailAdapter extends BaseAdapter {
    private Context context;
    private List<EntityPublic> groupTopicList;// 热点话题集合
    private boolean isClear;

    public GroupDetailAdapter(Context context, List<EntityPublic> hotTopicList) {
        this.context = context;
        this.groupTopicList = hotTopicList;
    }

    public void addData(List<EntityPublic> newdata) {
        if (isClear) {
            groupTopicList.clear();
            isClear = false;
        }
        groupTopicList.addAll(newdata);
        notifyDataSetChanged();
    }

    public void clearData() {
        isClear = true;
    }

    @Override
    public int getCount() {
        return groupTopicList.size();
    }

    @Override
    public EntityPublic getItem(int position) {
        return groupTopicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {
        final GroupDetailsItemViewHolder holder;
        if (itemView == null) {
            holder = new GroupDetailsItemViewHolder();
            itemView = LayoutInflater.from(context).inflate(R.layout.item_group_detail_topic_list, parent, false);
            holder.nine_layout = (NineGridImageView) itemView.findViewById(R.id.layout_nine_grid);
            holder.group_detail_topic_avatar = (ImageView) itemView.findViewById(R.id.group_detail_topic_avatar);
            holder.group_detail_topic_author_nickName = (TextView) itemView.findViewById(R.id.group_detail_topic_author_nickName);
            holder.group_detail_topic_author_name = (TextView) itemView.findViewById(R.id.group_detail_topic_author_name);
            holder.group_detail_topic_createTime = (TextView) itemView.findViewById(R.id.group_detail_topic_createTime);
            holder.group_detail_topic_title = (TextView) itemView.findViewById(R.id.group_detail_topic_title);
            holder.group_detail_topic_content = (TextView) itemView.findViewById(R.id.group_detail_topic_content);
            holder.group_detail_topic_comment = (TextView) itemView.findViewById(R.id.group_detail_topic_comment);
            holder.group_detail_topic_praise = (TextView) itemView.findViewById(R.id.group_detail_topic_praise);
            holder.group_detail_topic_browse = (TextView) itemView.findViewById(R.id.group_detail_topic_browse);
            holder.isDetailTop = (TextView) itemView.findViewById(R.id.isDetailTop);
            holder.isDetailEssence = (TextView) itemView.findViewById(R.id.isDetailEssence);
            holder.isDetailFiery = (TextView) itemView.findViewById(R.id.isDetailFiery);
            itemView.setTag(holder);
        } else {
            holder = (GroupDetailsItemViewHolder) itemView.getTag();
        }

        if (groupTopicList.get(position).getTop() == 1) {
            holder.isDetailTop.setVisibility(View.VISIBLE);
        } else {
            holder.isDetailTop.setVisibility(View.GONE);
        }
        if (groupTopicList.get(position).getEssence() == 1) {
            holder.isDetailEssence.setVisibility(View.VISIBLE);
        } else {
            holder.isDetailEssence.setVisibility(View.GONE);
        }
        if (groupTopicList.get(position).getFiery() == 1) {
            holder.isDetailFiery.setVisibility(View.VISIBLE);
        } else {
            holder.isDetailFiery.setVisibility(View.GONE);
        }

        GlideUtil.loadCircleImage(context, Address.IMAGE + groupTopicList.get(position).getAvatar(), holder.group_detail_topic_avatar);

        holder.group_detail_topic_author_nickName.setText(groupTopicList.get(position).getNickName());
        holder.group_detail_topic_author_name.setText(groupTopicList.get(position).getGroupName());
        holder.group_detail_topic_createTime.setText(groupTopicList.get(position).getCreateTime());
        holder.group_detail_topic_title.setText(groupTopicList.get(position).getTitle());
        holder.group_detail_topic_content.setText(StringUtil.replaceHtml(groupTopicList.get(position).getContent()));
        holder.group_detail_topic_comment.setText(String.valueOf(groupTopicList.get(position).getCommentCounts()));
        holder.group_detail_topic_praise.setText(String.valueOf(groupTopicList.get(position).getPraiseCounts()));
        holder.group_detail_topic_browse.setText(String.valueOf(groupTopicList.get(position).getBrowseCounts()));

        holder.nine_layout.setAdapter(new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                GlideUtil.loadImage(context, s, imageView);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                Intent intent = new Intent(context, BigImageActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("picUrls", list.toArray(new String[0]));
                context.startActivity(intent);
            }

            @Override
            protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                return super.onItemImageLongClick(context, imageView, index, list);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        });

        holder.nine_layout.setImagesData(groupTopicList.get(position).getHtmlImagesList());

        return itemView;
    }


    static class GroupDetailsItemViewHolder {
        public NineGridImageView nine_layout;
        public ImageView group_detail_topic_avatar;
        public TextView group_detail_topic_author_nickName, group_detail_topic_author_name,
                group_detail_topic_createTime, group_detail_topic_title, group_detail_topic_content,
                group_detail_topic_comment, group_detail_topic_praise, group_detail_topic_browse, isDetailTop,
                isDetailEssence, isDetailFiery;
    }
}
