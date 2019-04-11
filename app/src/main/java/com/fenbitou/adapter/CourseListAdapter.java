package com.fenbitou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fenbitou.adapter.viewholder.CourseViewHolder;
import com.fenbitou.base.BaseAdapter;
import com.fenbitou.entity.EntityCourse;
import com.fenbitou.wantongzaixian.R;
import com.fenbitou.utils.Address;
import com.fenbitou.utils.DensityUtil;

import java.util.List;

/**
 * Created by admin on 2017/6/30.
 * 课程 listView 适配器
 */

public class CourseListAdapter extends BaseAdapter<EntityCourse> {

    private Context context;

    public CourseListAdapter(Context context, List<EntityCourse> mList) {
        super(context, mList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseViewHolder holder;
        if (convertView == null) {
            holder = new CourseViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.item_course_list, parent, false);
            holder.courseImage = (ImageView) convertView.findViewById(R.id.courseImage);
            holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
            holder.coursePlayNum = (TextView) convertView.findViewById(R.id.coursePlayNum);
            holder.coursePrice = (TextView) convertView.findViewById(R.id.coursePrice);
            holder.courseFree = (ImageView) convertView.findViewById(R.id.courseFree);
            convertView.setTag(holder);
        } else {
            holder = (CourseViewHolder) convertView.getTag();
        }
        EntityCourse entity = getList().get(position);
        holder.courseName.setText(entity.getName());

        int isPay = entity.getIsPay();
        float price = entity.getCurrentprice();
        if (isPay == 0 || price <= 0) {
            holder.courseFree.setVisibility(View.VISIBLE);
            holder.coursePrice.setText("免费");
        } else {
            holder.courseFree.setVisibility(View.GONE);
            holder.coursePrice.setText("￥" + price);
        }
        holder.coursePlayNum.setText("播放量: " + entity.getPageViewcount());

        ViewGroup.LayoutParams params = holder.courseImage.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(mContext);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.courseImage.setLayoutParams(params);

        Glide.with(context).load(Address.IMAGE_NET + entity.getMobileLogo()).into(holder.courseImage);
        return convertView;
    }

}
