package com.fenbitou.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;

/**
 * Created by xiangyao on 2017/8/9.
 */

public class MyCollectionViewHolder extends RecyclerView.ViewHolder {
    public ImageView course_image, imagecheck;
    public TextView course_title, course_teacher, course_lessionnum;

    public MyCollectionViewHolder(View view) {
        super(view);
        course_image = (ImageView) view.findViewById(R.id.course_image);
        imagecheck = (ImageView) view.findViewById(R.id.imagecheck);
        course_title = (TextView) view.findViewById(R.id.course_title);
        course_teacher = (TextView) view.findViewById(R.id.course_teacher);
        course_lessionnum = (TextView) view.findViewById(R.id.course_lessionnum);
    }
}
