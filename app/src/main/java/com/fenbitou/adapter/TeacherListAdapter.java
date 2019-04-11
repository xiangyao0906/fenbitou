package com.fenbitou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenbitou.adapter.viewholder.TeacherViewHolder;
import com.fenbitou.base.BaseAdapter;
import com.fenbitou.entity.TeacherEntity;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.GlideUtil;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 * 讲师列表适配器
 */

public class TeacherListAdapter extends BaseAdapter<TeacherEntity> {

    public TeacherListAdapter(Context context, List<TeacherEntity> mList) {
        super(context, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TeacherViewHolder holder;
        if(convertView == null){
            holder = new TeacherViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_teacher_list, null);
            holder.teacherImage = (ImageView) convertView.findViewById(R.id.teacher_image);
            holder.teacherName = (TextView) convertView.findViewById(R.id.teacher_name);
            holder.teacherPosition = (TextView) convertView.findViewById(R.id.teacher_position);
            convertView.setTag(holder);
        }else{
            holder = (TeacherViewHolder) convertView.getTag();
        }
        TeacherEntity entity = getList().get(position);
        holder.teacherName.setText(entity.getName());
        if(entity.getIsStar()==0){
            holder.teacherPosition.setText("高级讲师");
        }else{
            holder.teacherPosition.setText("首席讲师");
        }
        GlideUtil.loadCircleHeadImage(getContext(),Address.IMAGE_NET+entity.getPicPath(),holder.teacherImage);

        return convertView;
    }
}
