package com.yizhilu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.eduapp.R;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.DensityUtil;
import com.yizhilu.utils.GlideUtil;


/**
 * @author bin 修改人: 时间:2015-12-1 上午10:46:31 类说明:课程订单的适配器
 */
public class CourseOrderAdapter extends BaseAdapter {
    private Context context; // 上下文对象
    // private OrderEntity orderEntity; //课程订单的实体
    private PublicEntity publicEntity;

    public CourseOrderAdapter(Context context, PublicEntity publicEntity) {
        super();
        this.context = context;
        this.publicEntity = publicEntity;
    }

    @Override
    public int getCount() {
        return publicEntity.getEntity().getDetailList().size();
    }

    @Override
    public Object getItem(int position) {
        return publicEntity.getEntity().getDetailList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course_order, null);
            holder.courseImage = (ImageView) convertView.findViewById(R.id.courseImage);
            holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams params = holder.courseImage.getLayoutParams();
        double width = DensityUtil.getCourseImageViewWidth(context);
        params.width = (int) width;
        params.height = (int) DensityUtil.getCourseImageViewHeight(width);
        holder.courseImage.setLayoutParams(params);

        EntityPublic entity = publicEntity.getEntity();
        if (entity.getDetailList().size() > 0) {
            if (entity.getDetailList().get(position).getCourse() != null) {
                holder.courseName.setText(entity.getDetailList().get(position).getCourse().getName());
                GlideUtil.loadImage(context, Address.IMAGE_NET + entity.getDetailList().get(position).getCourse().getMobileLogo(), holder.courseImage);
            }
            if (entity.getDetailList().get(position).getBook() != null) {
                holder.courseName.setText(entity.getDetailList().get(position).getBook().getBookName());
                GlideUtil.loadImage(context, Address.IMAGE_NET + entity.getDetailList().get(position).getBook().getBookImg(), holder.courseImage);
            }
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView courseImage; // 課程圖片
        private TextView courseName; // 課程名，播放量
    }
}
