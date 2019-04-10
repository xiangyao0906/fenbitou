package com.yizhilu.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.eduapp.R;

/**
 * Created by xiangyao on 2017/8/9.
 */

public class MyCourseViewHolder extends RecyclerView.ViewHolder {
    public ImageView course_image;
    public TextView course_title, course_teacher, course_lessionnum;

    public MyCourseViewHolder(View view) {
        super(view);
        course_image = (ImageView) view.findViewById(R.id.course_image);
        course_title = (TextView) view.findViewById(R.id.course_title);
        course_teacher = (TextView) view.findViewById(R.id.course_teacher);
        course_lessionnum = (TextView) view.findViewById(R.id.course_lessionnum);
    }
}
