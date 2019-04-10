package com.yizhilu.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.eduapp.R;

/**
 * Created by xiangyao on 2017/8/3.
 */

public class TeacherHomeViewHolder extends RecyclerView.ViewHolder {
    public TeacherHomeViewHolder(View itemView) {
        super(itemView);
        userIcon = (ImageView) itemView.findViewById(R.id.teacher_image);
        teacherNmae = (TextView) itemView.findViewById(R.id.teacherName);
        gradeTitle = (TextView) itemView.findViewById(R.id.gradeTitle);
        teacher_content = (TextView) itemView.findViewById(R.id.teacher_content);
    }

    public TextView teacherNmae, gradeTitle, teacher_content;
    public ImageView userIcon;

}
