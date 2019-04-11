package com.fenbitou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenbitou.base.BaseAdapter;
import com.fenbitou.entity.EntityPublic;
import com.fenbitou.wantongzaixian.R;

import java.util.List;

/**
 * Created by admin on 2017/7/4.
 * 课程目录 适配器
 */

public class CourseDirectoryAdapter extends BaseAdapter<EntityPublic>{

    private int selectPosition = 0;//选中下标

    public CourseDirectoryAdapter(Context context, List<EntityPublic> mList) {
        super(context, mList);
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_course_directory, null);
            holder.directory_layout = (LinearLayout) convertView.findViewById(R.id.directory_layout);
            holder.directory_image = (ImageView) convertView.findViewById(R.id.directory_image);
            holder.directory_name = (TextView) convertView.findViewById(R.id.directory_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        EntityPublic entity = getList().get(position);

        holder.directory_name.setText(entity.getName());

        if(selectPosition == position){
            holder.directory_layout.setBackgroundResource(R.drawable.chapter_frame_selected);
            holder.directory_image.setBackgroundResource(R.drawable.iconfont_wodekecheng_select);
            holder.directory_name.setTextColor(getContext().getResources().getColor(R.color.color_main));
        }else{
            holder.directory_layout.setBackgroundResource(R.drawable.chapter_frame);
            holder.directory_image.setBackgroundResource(R.drawable.iconfont_wodekecheng);
            holder.directory_name.setTextColor(getContext().getResources().getColor(R.color.color_9a));
        }

        return convertView;
    }

    class ViewHolder{
        LinearLayout directory_layout;
        ImageView directory_image;
        TextView directory_name;
    }
}
