package com.fenbitou.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fenbitou.community.adapter.viewholder.HotSearchItemViewHolder;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.wantongzaixian.R;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/24.
 */

public class HotSearAdapter extends RecyclerView.Adapter<HotSearchItemViewHolder> {
    private Context context;
    private List<EntityCourse> courseList;

    public HotSearAdapter(Context context, List<EntityCourse> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public HotSearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotSearchItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent,false));
    }

    @Override
    public void onBindViewHolder(HotSearchItemViewHolder holder, int position) {
        holder.titleText.setText(courseList.get(position).getCourseName());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public List<EntityCourse> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<EntityCourse> courseList) {
        this.courseList = courseList;
    }
}
