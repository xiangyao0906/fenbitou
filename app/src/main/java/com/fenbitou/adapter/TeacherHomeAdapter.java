package com.fenbitou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fenbitou.adapter.viewholder.TeacherHomeViewHolder;
import com.fenbitou.entity.TeacherEntity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;

import java.util.List;

/**
 * Created by xiangyao on 2017/8/3.
 */

public class TeacherHomeAdapter extends RecyclerView.Adapter<TeacherHomeViewHolder> {
    private List<TeacherEntity> teacherEntities;
    private Context context;

    public TeacherHomeAdapter(List<TeacherEntity> teacherEntities, Context context) {
        this.teacherEntities = teacherEntities;
        this.context = context;
    }


    @Override
    public TeacherHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeacherHomeViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_teacher, null));
    }
    @Override
    public void onBindViewHolder(TeacherHomeViewHolder holder, int position) {
        holder.teacherNmae.setText(teacherEntities.get(position).getName());
        if (teacherEntities.get(position).getIsStar() == 0) {
            holder.gradeTitle.setText("高级讲师");
        } else {
            holder.gradeTitle.setText("首席讲师");
        }
        holder.teacher_content.setText(teacherEntities.get(position).getCareer());
        GlideUtil.loadCircleImage(context, Address.IMAGE_NET + teacherEntities.get(position).getPicPath(), holder.userIcon);
    }

    @Override
    public int getItemCount() {
        return teacherEntities.size();
    }

    public List<TeacherEntity> getTeacherEntities() {
        return teacherEntities;
    }

    public void setTeacherEntities(List<TeacherEntity> teacherEntities) {
        this.teacherEntities = teacherEntities;
    }
}
