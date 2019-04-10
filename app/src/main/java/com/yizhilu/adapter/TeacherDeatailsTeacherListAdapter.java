package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yizhilu.adapter.viewholder.TeacherDetailsItemViewHolder;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.GlideUtil;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/4.
 */

public class TeacherDeatailsTeacherListAdapter extends RecyclerView.Adapter<TeacherDetailsItemViewHolder> {

    private Context context;
    private List<EntityCourse> entitieCourses;

    public TeacherDeatailsTeacherListAdapter(Context context, List<EntityCourse> entitieCourses) {
        this.context = context;
        this.entitieCourses = entitieCourses;
    }

    @Override
    public TeacherDetailsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeacherDetailsItemViewHolder(LayoutInflater.from(context).inflate(R.layout.course_details_item, null));
    }

    @Override
    public void onBindViewHolder(TeacherDetailsItemViewHolder holder, int position) {
        holder.courseNmae.setText(entitieCourses.get(position).getName());

        int ispay = entitieCourses.get(position).getIsPay();
        float price = entitieCourses.get(position).getCurrentprice();
        if (ispay == 0 || price <= 0) {
            holder.recommend_currentPrice.setVisibility(View.GONE);
            holder.recommend_freePrice.setVisibility(View.VISIBLE);
        } else {
            holder.coursePrice.setText(price + "");
        }

        ViewGroup.LayoutParams params = holder.courseImg.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(context);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.courseImg.setLayoutParams(params);

        Glide.with(context).load(Address.IMAGE_NET + entitieCourses.get(position).getMobileLogo()).into(holder.courseImg);
//        GlideUtil.loadImage(context, Address.IMAGE_NET + entitieCourses.get(position).getMobileLogo(), holder.courseImg);

        holder.playNum.setText("播放量 : " + entitieCourses.get(position).getPlayNum());

    }

    @Override
    public int getItemCount() {
        return entitieCourses.size();
    }

    public List<EntityCourse> getEntitieCourses() {
        return entitieCourses;
    }

    public void setEntitieCourses(List<EntityCourse> entitieCourses) {
        this.entitieCourses = entitieCourses;
    }
}
