package com.yizhilu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yizhilu.adapter.viewholder.CourseViewHolder;
import com.yizhilu.base.BaseAdapter;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.eduapp.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.GlideUtil;

import java.util.List;

/**
 * Created by admin on 2017/6/30.
 * 记录 listView 适配器
 */

public class RecordListAdapter extends BaseAdapter<EntityPublic> {

    public RecordListAdapter(Context context, List<EntityPublic> mList) {
        super(context, mList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseViewHolder holder;
        if (convertView == null) {
            holder = new CourseViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_record_list, parent, false);
            holder.courseImage = (ImageView) convertView.findViewById(R.id.courseImage);
            holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
            holder.coursePlayTime = (TextView) convertView.findViewById(R.id.coursePlayTime);
            holder.kpointName = (TextView) convertView.findViewById(R.id.kpointName);
            convertView.setTag(holder);
        } else {
            holder = (CourseViewHolder) convertView.getTag();
        }
        EntityPublic entity = getList().get(position);
        holder.courseName.setText(entity.getCourseName());

        ViewGroup.LayoutParams params = holder.courseImage.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(mContext);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.courseImage.setLayoutParams(params);

        GlideUtil.loadImage(getContext(), Address.IMAGE_NET + entity.getMobileLogo(), holder.courseImage);
        String timeString = entity.getUpdateTime();
        String month = timeString.split(":")[0];
        String time = timeString.split(":")[1].split(":")[0];
        holder.coursePlayTime.setText(month + ":" + time + " 学习");
        holder.kpointName.setText(entity.getKpointName());
        holder.kpointName.setVisibility(View.VISIBLE);
        return convertView;
    }

}
