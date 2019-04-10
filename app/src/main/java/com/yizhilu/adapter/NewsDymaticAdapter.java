package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yizhilu.adapter.viewholder.NewsDymaticViewHolder;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.GlideUtil;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/11.
 */

public class NewsDymaticAdapter extends RecyclerView.Adapter<NewsDymaticViewHolder> {

    private Context context;  //上下文对象
    private List<EntityCourse> informationList;  //文章的实体


    public NewsDymaticAdapter(Context context, List<EntityCourse> informationList) {
        this.context = context;
        this.informationList = informationList;
    }

    @Override
    public NewsDymaticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsDymaticViewHolder(LayoutInflater.from(context).inflate(R.layout.item_article, null));
    }

    @Override
    public void onBindViewHolder(NewsDymaticViewHolder holder, int position) {

        holder.articleTitle.setText(informationList.get(position).getTitle());
        String timeString = informationList.get(position).getUpdateTime();
        String month = timeString.split(":")[0];
        String time = timeString.split(":")[1].split(":")[0];
        holder.articleTime.setText(month + ":" + time);
        holder.articleNum.setText(informationList.get(position).getClickTimes() + "");

        ViewGroup.LayoutParams params = holder.articleImage.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(context);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.articleImage.setLayoutParams(params);

        Glide.with(context).load(Address.IMAGE_NET + informationList.get(position).getPicture()).into(holder.articleImage);
//        GlideUtil.loadImage(context, Address.IMAGE_NET + informationList.get(position).getPicture(), holder.articleImage);
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public List<EntityCourse> getInformationList() {
        return informationList;
    }

    public void setInformationList(List<EntityCourse> informationList) {
        this.informationList = informationList;
    }
}
