package com.fenbitou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fenbitou.adapter.viewholder.MyCourseViewHolder;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.DensityUtil;
import com.fenbitou.utils.GlideUtil;

import java.util.List;


/**
 * Created by bishuang on 2017/8/3.
 * 我的课程的adapter
 */

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseViewHolder> {

    private Context context;
    private List<EntityCourse> datas;

    public MyCourseAdapter(Context context, List<EntityCourse> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyCourseViewHolder holder = new MyCourseViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_my_course, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(MyCourseViewHolder holder, int position) {
        StringBuffer buffer = new StringBuffer();
        holder.course_title.setText(datas.get(position).getName());
        for (int i = 0; i < datas.get(position).getTeacherList().size(); i++) {
            if (i == datas.get(position).getTeacherList().size() - 1) {
                buffer.append(datas.get(position).getTeacherList().get(i).getName());
            } else {
                buffer.append(datas.get(position).getTeacherList().get(i).getName() + ",");
            }

        }
        holder.course_teacher.setText("讲师 : " + buffer.toString());
        holder.course_lessionnum.setText("课时 : " + datas.get(position).getLessionnum());

        ViewGroup.LayoutParams params = holder.course_image.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(context);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.course_image.setLayoutParams(params);

        GlideUtil.loadImage(context, Address.IMAGE_NET + datas.get(position).getMobileLogo(), holder.course_image);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}
